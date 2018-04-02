package com.hnair.consumer.wallet.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import com.hnair.consumer.dao.utils.PageFinder;
import com.hnair.consumer.dao.utils.Query;
import com.hnair.consumer.utils.CollectionUtils;
import com.hnair.consumer.utils.DateUtil;
import com.hnair.consumer.utils.MD5Utils;
import com.hnair.consumer.wallet.model.WalletIdGen;
import com.hnair.consumer.wallet.model.WalletUser;
import com.hnair.consumer.wallet.vo.WalletVo;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.hnair.consumer.dao.spi.impl.CommonDaoImpl;
import com.hnair.consumer.wallet.enums.WalletErrorCodeEnum;
import com.hnair.consumer.wallet.model.Merchant;
import com.hnair.consumer.wallet.model.PayBillDetail;
import com.hnair.consumer.wallet.model.Wallet;
import com.hnair.consumer.wallet.model.WalletBillDetail;
import com.hnair.consumer.wallet.service.IWalletService;
import com.hnair.consumer.wallet.utils.WalletUtils;
import com.hnair.consumer.wallet.vo.PaymentVo;
import com.hnair.consumer.wallet.vo.WalletMessageVo;

import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import com.hnair.consumer.utils.system.ConfigPropertieUtils;

import static com.hnair.consumer.dao.utils.ParameterUtils.convertBeanToMap;

@Service("walletService")
public class WalletServiceImpl implements IWalletService {

	Logger logger = LoggerFactory.getLogger(WalletServiceImpl.class);

	// 钱包平台运营人员通用钱包id及密码
	String platform_admin_wallet_id = ConfigPropertieUtils.getString("platform_admin_wallet_id");
	String platform_admin_wallet_secret = ConfigPropertieUtils.getString("platform_admin_wallet_secret");

	@Resource
	private CommonDaoImpl walletCommonDao;

	@Resource
	@SuppressWarnings("rawtypes")
	private RedisTemplate masterRedisTemplate;

	@Resource
	@SuppressWarnings("rawtypes")
	private RedisTemplate slaveRedisTemplate;

	@SuppressWarnings("unchecked")
	@Override
	public WalletMessageVo<String> getAccessToken(Long walletId, String secret) {
		WalletMessageVo<String> result = new WalletMessageVo<>();
		if (walletId == null || secret == null) {
			result.setErrorCode(WalletErrorCodeEnum.ERROR_CODE_1000.getErrorCode().toString());
			result.setErrorMessage(WalletErrorCodeEnum.ERROR_CODE_1000.getErrorMessage());
			return result;
		}

		if (platform_admin_wallet_id.equals(walletId.toString())) {
			// 平台运营人员（管理员，财务等）
			if (!platform_admin_wallet_secret.equals(secret)) {
				result.setErrorCode(WalletErrorCodeEnum.ERROR_CODE_1002.getErrorCode().toString());
				result.setErrorMessage(WalletErrorCodeEnum.ERROR_CODE_1002.getErrorMessage());
				return result;
			}
		} else {
			// 获取钱包账户
			Wallet wallet = walletCommonDao.get(Wallet.class, "walletId", walletId);

			if (wallet == null) {
				result.setErrorCode(WalletErrorCodeEnum.ERROR_CODE_1001.getErrorCode().toString());
				result.setErrorMessage(WalletErrorCodeEnum.ERROR_CODE_1001.getErrorMessage());
				return result;
			}

			if (!secret.equals(wallet.getAppSecret())) {
				result.setErrorCode(WalletErrorCodeEnum.ERROR_CODE_1002.getErrorCode().toString());
				result.setErrorMessage(WalletErrorCodeEnum.ERROR_CODE_1002.getErrorMessage());
				return result;
			}
		}

		// 生成token
		String access_token = WalletUtils.buildToken();

		// 以token为key，walletid为value，放入缓存，时效20分钟
		String cacheKey = "walletid_by_access_token_" + access_token;
		masterRedisTemplate.delete(cacheKey);
		masterRedisTemplate.opsForValue().set(cacheKey, walletId);
		masterRedisTemplate.expire(cacheKey, 20, TimeUnit.MINUTES);

		result.setResult(true);
		result.setT(access_token);

		return result;
	}

	@Override
	public WalletMessageVo<Long> getWalletIdByToken(String token) {
		WalletMessageVo<Long> result = new WalletMessageVo<>();

		String cacheKey = "walletid_by_access_token_" + token;
		// 根据token获取钱包Id
		Long walletId = (Long) masterRedisTemplate.opsForValue().get(cacheKey);
		if (walletId == null) {
			result.setErrorCode(WalletErrorCodeEnum.ERROR_CODE_1003.getErrorCode().toString());
			result.setErrorMessage(WalletErrorCodeEnum.ERROR_CODE_1003.getErrorMessage());
			return result;
		}

		result.setResult(true);
		result.setT(walletId);

		return result;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, transactionManager = "walletTransactionManager", rollbackFor = Exception.class)
	public WalletMessageVo<WalletVo> createWalletAccount(String token, String account_name, String user_id,
			Integer application_code, String payPassword, String phone) {
		WalletMessageVo<WalletVo> result = new WalletMessageVo<>();
		WalletMessageVo<Long> messageVo = this.getWalletIdByToken(token);
		if (!messageVo.isResult()) {
			result.setErrorCode(messageVo.getErrorCode());
			result.setErrorMessage(messageVo.getErrorMessage());
			return result;
		}
		// 生成钱包id
		Date date = new Date();
		WalletIdGen walletIdGen = new WalletIdGen();
		walletIdGen.setCreateDate(date);
		walletCommonDao.save(walletIdGen);

		Long walletId = walletIdGen.getWalletId();
		String secret = WalletUtils.buildSecret(walletId.toString());
		Wallet wallet = new Wallet();
		wallet.setBalance(BigDecimal.ZERO);
		wallet.setCreateTime(new Date());
		wallet.setModifyTime(new Date());
		wallet.setAppSecret(secret);
		wallet.setFreezeAmount(BigDecimal.ZERO);
		// wallet.setPayPassword(MD5Utils.MD5Encode(payPassword));
		wallet.setWalletId(walletId);
		wallet.setWalletType(1);
		wallet.setState(1);
		walletCommonDao.save(wallet);

		WalletUser walletUser = new WalletUser();
		walletUser.setApplicationCode(application_code);
		walletUser.setUserId(user_id);
		walletUser.setPhone(phone);
		walletUser.setAccountName(account_name);
		walletUser.setWalletId(walletId);
		walletCommonDao.save(walletUser);

		WalletVo walletVo = new WalletVo();
		walletVo.setWalletId(walletId);
		walletVo.setAppSecret(secret);
		result.setResult(true);
		result.setT(walletVo);
		return result;
	}

	@Override
	public WalletMessageVo<List<WalletVo>> queryWalletList(String token, WalletVo walletParamer, Integer pageNumber,
			Integer pageSize) {
		WalletMessageVo<List<WalletVo>> result = new WalletMessageVo<>();
		Query query = new Query();
		query.setPageSize(pageSize);
		query.setPage(pageNumber);
		WalletMessageVo<Long> messageVo = this.getWalletIdByToken(token);
		if (!messageVo.isResult()) {
			result.setErrorCode(messageVo.getErrorCode());
			result.setErrorMessage(messageVo.getErrorMessage());
			return result;
		}
		List<WalletVo> walletVos = new ArrayList<>();
		PageFinder<Wallet> pageFinder = walletCommonDao.getPageFinder(Wallet.class, query,
				convertBeanToMap(walletParamer));
		if (!CollectionUtils.isEmpty(pageFinder.getData())) {
			pageFinder.getData().forEach(wallet -> {
				WalletUser walletUser = walletCommonDao.get(WalletUser.class, "walletId", wallet.getWalletId());
				WalletVo walletVo = new WalletVo();
				walletVo.setWalletId(wallet.getWalletId());
				walletVo.setCreateTime(wallet.getCreateTime());
				walletVo.setPhone(walletUser.getPhone());
				walletVo.setState(wallet.getState());
				walletVo.setAccountName(walletUser.getAccountName());
				walletVo.setBalance(wallet.getBalance());
				walletVo.setRemark(wallet.getRemark());
				// 绑定信息
				// WithdrawAccount withdrawAccount =
				// walletCommonDao.get(WithdrawAccount.class, "walletId",
				// wallet.getWalletId(), "type", 1);
				// if (withdrawAccount != null) {
				// walletVo.setWithdrawaAccount(withdrawAccount.getAccount());
				// walletVo.setWithdrawaAccountName(withdrawAccount.getAccountName());
				// }
				walletVos.add(walletVo);
			});
		}

		result.setT(walletVos);
		result.setResult(true);
		result.setCount(pageFinder.getRowCount());
		return result;
	}

	@Override
	public WalletMessageVo<WalletVo> getWallet(String token) {
		WalletMessageVo<WalletVo> result = new WalletMessageVo<>();
		WalletMessageVo<Long> messageVo = this.getWalletIdByToken(token);
		if (!messageVo.isResult()) {
			result.setErrorCode(messageVo.getErrorCode());
			result.setErrorMessage(messageVo.getErrorMessage());
			return result;
		}
		Long walletId = messageVo.getT();
		Wallet wallet = walletCommonDao.get(Wallet.class, "walletId", walletId);
		if (wallet == null) {
			result.setErrorCode(WalletErrorCodeEnum.ERROR_CODE_1001.getErrorCode().toString());
			result.setErrorMessage(WalletErrorCodeEnum.ERROR_CODE_1001.getErrorMessage());
			return result;
		}
		WalletVo walletVo = new WalletVo();
		walletVo.setWalletId(wallet.getWalletId());
		walletVo.setCreateTime(wallet.getCreateTime());
		walletVo.setState(wallet.getState());
		walletVo.setBalance(wallet.getBalance());
		walletVo.setHaspwd(wallet.getPayPassword() == null ? "0" : "1");
		walletVo.setFreezeAmount(wallet.getFreezeAmount());
		WalletUser walletUser = walletCommonDao.get(WalletUser.class, "walletId", walletId);
		if (walletUser != null) {
			walletVo.setPhone(walletUser.getPhone());
			walletVo.setUserId(walletUser.getUserId());
			walletVo.setAccountName(walletUser.getAccountName());
		}
		result.setResult(true);
		result.setT(walletVo);
		return result;
	}

	@Override
	public WalletMessageVo<Boolean> checkPayPassword(Wallet wallet, String payPassword) {
		WalletMessageVo<Boolean> result = new WalletMessageVo<>();

		if (wallet == null || StringUtils.isBlank(payPassword)) {
			result.setErrorCode(WalletErrorCodeEnum.ERROR_CODE_1000.getErrorCode().toString());
			result.setErrorMessage(WalletErrorCodeEnum.ERROR_CODE_1000.getErrorMessage());
			return result;
		}

		// 判断支付密码是否正确
		if (!MD5Utils.MD5Encode(payPassword).equals(wallet.getPayPassword())) {
			result.setErrorCode(WalletErrorCodeEnum.ERROR_CODE_1008.getErrorCode().toString());
			result.setErrorMessage(WalletErrorCodeEnum.ERROR_CODE_1008.getErrorMessage());
		} else {
			result.setResult(true);
			result.setT(true);
		}

		return result;
	}

	@Override
	@Transactional
	public WalletMessageVo<Boolean> modifyPayPassword(String token, String originalPwd, String newPwd) {
		WalletMessageVo<Boolean> result = new WalletMessageVo<>();

		if (StringUtils.isBlank(newPwd)) {
			result.setErrorCode(WalletErrorCodeEnum.ERROR_CODE_1000.getErrorCode().toString());
			result.setErrorMessage(WalletErrorCodeEnum.ERROR_CODE_1000.getErrorMessage());
			return result;
		}
		if (originalPwd.equals(newPwd)) {
			result.setErrorCode(WalletErrorCodeEnum.ERROR_CODE_1010.getErrorCode().toString());
			result.setErrorMessage(WalletErrorCodeEnum.ERROR_CODE_1010.getErrorMessage());
			return result;
		}

		// 验证token是否有效
		WalletMessageVo<Long> tokenResult = getWalletIdByToken(token);
		if (!tokenResult.isResult()) {
			result.setErrorCode(tokenResult.getErrorCode());
			result.setErrorMessage(tokenResult.getErrorMessage());
			return result;
		}
		// 查询钱包账户
		Wallet wallet = walletCommonDao.get(Wallet.class, "walletId", tokenResult.getT());
		if (wallet == null) {
			result.setErrorCode(WalletErrorCodeEnum.ERROR_CODE_1001.getErrorCode().toString());
			result.setErrorMessage(WalletErrorCodeEnum.ERROR_CODE_1001.getErrorMessage());
			return result;
		}

		if (!StringUtils.isBlank(wallet.getPayPassword())) {
			// 修改支付密码则，验证原密码
			WalletMessageVo<Boolean> checkPwdResult = checkPayPassword(wallet, originalPwd);
			if (!checkPwdResult.isResult()) {
				return checkPwdResult;
			}
		}

		Wallet modifyWallet = new Wallet();
		modifyWallet.setWalletId(wallet.getWalletId());
		modifyWallet.setPayPassword(MD5Utils.MD5Encode(newPwd));
		walletCommonDao.update(modifyWallet);

		result.setT(true);
		result.setResult(true);

		return result;
	}

	@Override
	@Transactional
	public WalletMessageVo<Map<String, String>> tradeCreate(PaymentVo req) {
		WalletMessageVo<Map<String, String>> result = new WalletMessageVo<>();
		logger.info("req=" + JSON.toJSONString(req));
		if (StringUtils.isBlank(req.getOrderNo()) || req.getPartnerId() == null || req.getBuyerId() == null
				|| req.getPayAmount() == null || StringUtils.isBlank(req.getSign())) {
			result.setErrorCode(WalletErrorCodeEnum.ERROR_CODE_1000.getErrorCode().toString());
			result.setErrorMessage(WalletErrorCodeEnum.ERROR_CODE_1000.getErrorMessage());
			return result;
		}

		// 获取收款账户
		Merchant merchant = walletCommonDao.get(Merchant.class, "merchantId", req.getPartnerId());
		if (merchant == null) {
			result.setErrorCode(WalletErrorCodeEnum.ERROR_CODE_1017.getErrorCode().toString());
			result.setErrorMessage(WalletErrorCodeEnum.ERROR_CODE_1017.getErrorMessage());
			return result;
		}

		// 验证sign是否有效
		if (!WalletUtils.toSign(req,merchant.getAppSecret()).equals(req.getSign())) {
			result.setErrorCode(WalletErrorCodeEnum.ERROR_CODE_1018.getErrorCode().toString());
			result.setErrorMessage(WalletErrorCodeEnum.ERROR_CODE_1018.getErrorMessage());
			return result;
		}

		Wallet payeeWallet = walletCommonDao.get(Wallet.class, "walletId", merchant.getPayeeAccount(), "state", 1);
		if (payeeWallet == null) {
			result.setErrorCode(WalletErrorCodeEnum.ERROR_CODE_1001.getErrorCode().toString());
			result.setErrorMessage(WalletErrorCodeEnum.ERROR_CODE_1001.getErrorMessage());
			return result;
		}

		// 验证订单号是否重复
		List<PayBillDetail> list = walletCommonDao.getList(PayBillDetail.class, "orderNo", req.getOrderNo());
		if (list != null && list.size() > 0) {
			result.setErrorCode(WalletErrorCodeEnum.ERROR_CODE_1011.getErrorCode().toString());
			result.setErrorMessage(WalletErrorCodeEnum.ERROR_CODE_1011.getErrorMessage());
			return result;
		}

		// 交易流水号(订单号+时间戳+平台编码)
		String tradeNo = req.getOrderNo() + merchant.getMerchantId().toString()
				+ String.valueOf(System.currentTimeMillis());

		try {
			PayBillDetail payBillDetail = new PayBillDetail();
			payBillDetail.setAmount(req.getPayAmount());
			payBillDetail.setCreateTime(DateUtil.getCurrentDateTime());
			payBillDetail.setOrderNo(req.getOrderNo());
			payBillDetail.setPayeeWalletId(payeeWallet.getWalletId());
			payBillDetail.setPayerWalletId(req.getBuyerId());
			payBillDetail.setStatus(1);
			payBillDetail.setSubject(req.getSubject());
			payBillDetail.setTradeNo(tradeNo);
			payBillDetail.setNotifyUrl(req.getNotifyUrl());
			payBillDetail.setRemark(req.getNote());

			walletCommonDao.save(payBillDetail);

			result.setResult(true);
			Map<String, String>map=new HashMap<>();
			map.put("tradeNo", tradeNo);
			map.put("merchant_secret", merchant.getAppSecret());
			result.setT(map);
		} catch (Exception e) {
			result.setErrorCode(WalletErrorCodeEnum.ERROR_CODE_9999.getErrorCode().toString());
			result.setErrorMessage(WalletErrorCodeEnum.ERROR_CODE_9999.getErrorMessage().toString());
			logger.error(e.getMessage());
		}

		return result;
	}

	@Override
	@Transactional(isolation = Isolation.REPEATABLE_READ)
	public WalletMessageVo<PayBillDetail> walletPayment(String token, String tradeNo, String payPwd) {
		WalletMessageVo<PayBillDetail> result = new WalletMessageVo<>();

		// 参数验证
		if (StringUtils.isBlank(token) || StringUtils.isBlank(tradeNo) || StringUtils.isBlank(payPwd)) {
			result.setErrorCode(WalletErrorCodeEnum.ERROR_CODE_1000.getErrorCode().toString());
			result.setErrorMessage(WalletErrorCodeEnum.ERROR_CODE_1000.getErrorMessage());
			return result;
		}

		// 验证token是否有效
		WalletMessageVo<Long> tokenResult = getWalletIdByToken(token);
		if (!tokenResult.isResult()) {
			result.setErrorCode(tokenResult.getErrorCode());
			result.setErrorMessage(tokenResult.getErrorMessage());
			return result;
		}

		// 预下单信息验证
		PayBillDetail payBillDetail = walletCommonDao.get(PayBillDetail.class, "tradeNo", tradeNo);
		if (payBillDetail == null) {
			result.setErrorCode(WalletErrorCodeEnum.ERROR_CODE_1012.getErrorCode().toString());
			result.setErrorMessage(WalletErrorCodeEnum.ERROR_CODE_1012.getErrorMessage());
			return result;
		}
		if (!payBillDetail.getPayerWalletId().equals(tokenResult.getT())) {
			result.setErrorCode(WalletErrorCodeEnum.ERROR_CODE_1013.getErrorCode().toString());
			result.setErrorMessage(WalletErrorCodeEnum.ERROR_CODE_1013.getErrorMessage());
			return result;
		}
		if (payBillDetail.getStatus().intValue() != 1) {
			result.setErrorCode(WalletErrorCodeEnum.ERROR_CODE_1014.getErrorCode().toString());
			result.setErrorMessage(WalletErrorCodeEnum.ERROR_CODE_1014.getErrorMessage());
			return result;
		}

		// 付款账号
		Wallet payerWallet = walletCommonDao.get(Wallet.class, "walletId", payBillDetail.getPayerWalletId());
		// 支付密码验证
		WalletMessageVo<Boolean> pwdResult = checkPayPassword(payerWallet, payPwd);
		if (!pwdResult.isResult()) {
			result.setErrorCode(pwdResult.getErrorCode());
			result.setErrorMessage(pwdResult.getErrorMessage());
			return result;
		}
		// 余额验证
		if (payerWallet.getBalance().subtract(payerWallet.getFreezeAmount()).subtract(payBillDetail.getAmount())
				.compareTo(BigDecimal.ZERO) < 0) {
			result.setErrorCode(WalletErrorCodeEnum.ERROR_CODE_1015.getErrorCode().toString());
			result.setErrorMessage(WalletErrorCodeEnum.ERROR_CODE_1015.getErrorMessage());
			return result;
		}

		// 查询收款账号
		Wallet payeeWallet = walletCommonDao.get(Wallet.class, "walletId", payBillDetail.getPayeeWalletId());
		if (payeeWallet == null) {
			result.setErrorCode(WalletErrorCodeEnum.ERROR_CODE_1001.getErrorCode().toString());
			result.setErrorMessage(WalletErrorCodeEnum.ERROR_CODE_1001.getErrorMessage());
			return result;
		}

		try {
			payBillDetail.setStatus(2);
			walletCommonDao.update(payBillDetail);
			payeeWallet.setBalance(payeeWallet.getBalance().add(payBillDetail.getAmount()));
			walletCommonDao.update(payeeWallet);
			payerWallet.setBalance(payerWallet.getBalance().subtract(payBillDetail.getAmount()));
			walletCommonDao.update(payerWallet);

			Date date = DateUtil.getCurrentDateTime();
			// 付款账号变动记录
			WalletBillDetail payerWalletBillDetail = new WalletBillDetail();
			payerWalletBillDetail.setAmount(payBillDetail.getAmount());
			payerWalletBillDetail.setAmountType(1); // 支出
			payerWalletBillDetail.setBalance(payerWallet.getBalance());
			payerWalletBillDetail.setBusinessNo(payBillDetail.getTradeNo());
			payerWalletBillDetail.setCreateTime(date);
			payerWalletBillDetail.setTaskType(3); // 支付
			payerWalletBillDetail.setWalletId(payerWallet.getWalletId());
			payerWalletBillDetail.setDescription(payBillDetail.getSubject());
			walletCommonDao.save(payerWalletBillDetail);

			// 收款账号变动记录
			WalletBillDetail payeeWalletBillDetail = new WalletBillDetail();
			payeeWalletBillDetail.setAmount(payBillDetail.getAmount());
			payeeWalletBillDetail.setAmountType(2); // 收入
			payeeWalletBillDetail.setBalance(payeeWallet.getBalance());
			payeeWalletBillDetail.setBusinessNo(payBillDetail.getTradeNo());
			payeeWalletBillDetail.setCreateTime(date);
			payeeWalletBillDetail.setDescription(payBillDetail.getSubject());
			payeeWalletBillDetail.setTaskType(4); // 收款
			payeeWalletBillDetail.setWalletId(payeeWallet.getWalletId());
			walletCommonDao.save(payeeWalletBillDetail);

			result.setResult(true);
			result.setT(payBillDetail);
		} catch (Exception e) {
			result.setErrorCode(WalletErrorCodeEnum.ERROR_CODE_9999.getErrorCode().toString());
			result.setErrorMessage(WalletErrorCodeEnum.ERROR_CODE_9999.getErrorMessage().toString());
			logger.error(e.getMessage());
		}

		return result;
	}

	@Override
	public WalletMessageVo<PayBillDetail> getPayBillDetailByTradeNo(String token, String orderNo) {
		WalletMessageVo<PayBillDetail> result = new WalletMessageVo<>();

		// 验证token是否有效
		WalletMessageVo<Long> tokenResult = getWalletIdByToken(token);
		if (!tokenResult.isResult()) {
			result.setErrorCode(tokenResult.getErrorCode());
			result.setErrorMessage(tokenResult.getErrorMessage());
			return result;
		}

		PayBillDetail payBillDetail = walletCommonDao.get(PayBillDetail.class, "orderNo", orderNo);
		result.setT(payBillDetail);
		result.setResult(true);

		return result;
	}

	@Override
	public WalletMessageVo updateWallet(String token, WalletVo walletVo) {
		WalletMessageVo result = new WalletMessageVo();
		// 验证token是否有效
		WalletMessageVo<Long> tokenResult = getWalletIdByToken(token);
		if (!tokenResult.isResult()) {
			result.setErrorCode(tokenResult.getErrorCode());
			result.setErrorMessage(tokenResult.getErrorMessage());
			return result;
		}
		Wallet wallet = new Wallet();
		wallet.setWalletId(walletVo.getWalletId());
		wallet.setState(walletVo.getState());
		wallet.setRemark(walletVo.getRemark());
		try {
			walletCommonDao.update(wallet);
		} catch (Exception e) {
			logger.info(e.getMessage());
			result.setResult(false);
			result.setErrorMessage(WalletErrorCodeEnum.ERROR_CODE_1021.getErrorMessage());
			result.setErrorCode(WalletErrorCodeEnum.ERROR_CODE_1021.getErrorCode().toString());
			return result;
		}

		result.setResult(true);
		return result;
	}
}
