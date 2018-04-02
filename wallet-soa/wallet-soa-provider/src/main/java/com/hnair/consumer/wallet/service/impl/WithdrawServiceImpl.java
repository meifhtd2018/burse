package com.hnair.consumer.wallet.service.impl;

import java.math.BigDecimal;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.dubbo.common.utils.CollectionUtils;
import com.hnair.consumer.dao.spi.ICommonDao;
import com.hnair.consumer.utils.DateUtil;
import com.hnair.consumer.wallet.enums.WalletErrorCodeEnum;
import com.hnair.consumer.wallet.model.Wallet;
import com.hnair.consumer.wallet.model.WalletBillDetail;
import com.hnair.consumer.wallet.model.WithdrawDetail;
import com.hnair.consumer.wallet.service.IWalletService;
import com.hnair.consumer.wallet.service.IWithdrawService;
import com.hnair.consumer.wallet.utils.WalletUtils;
import com.hnair.consumer.wallet.vo.WalletMessageVo;

@Service("withdrawService")
public class WithdrawServiceImpl implements IWithdrawService {
	
	@Resource
	private IWalletService walletService;
	
	@Autowired
	private ICommonDao walletCommonDao;

	@Override
	@Transactional(propagation = Propagation.REQUIRED,rollbackFor = Exception.class)
	public WalletMessageVo<WithdrawDetail> createWithdraw(Long walletId,String accountName,BigDecimal amount,Long userId,String userName,String blankName,String cardCode,String phone) {
		WalletMessageVo<WithdrawDetail> walletMessageVo = new WalletMessageVo<>();
		// 校验参数
		if(checkParam(walletId,accountName,amount,userName,blankName,cardCode)){
			walletMessageVo.setErrorCode(WalletErrorCodeEnum.ERROR_CODE_1000.getErrorCode().toString());
			walletMessageVo.setErrorMessage(WalletErrorCodeEnum.ERROR_CODE_1000.getErrorMessage());
			return walletMessageVo;
		}
		// 根据参数查询有没有未审核的提现申请有直接返回提示信息，没有继续
		List<WithdrawDetail> list = walletCommonDao.getList(WithdrawDetail.class, "walletId", walletId,"status", 0);
		if (CollectionUtils.isNotEmpty(list)) {
			walletMessageVo.setErrorCode(WalletErrorCodeEnum.ERROR_CODE_1004.getErrorCode().toString());
			walletMessageVo.setErrorMessage(WalletErrorCodeEnum.ERROR_CODE_1004.getErrorMessage());
			return walletMessageVo;
		}
		// 判断账户余额是否足够
		Wallet wallet = walletCommonDao.get(Wallet.class, "walletId",walletId);
		if(wallet == null){
			walletMessageVo.setErrorCode(WalletErrorCodeEnum.ERROR_CODE_1001.getErrorCode().toString());
			walletMessageVo.setErrorMessage(WalletErrorCodeEnum.ERROR_CODE_1001.getErrorMessage());
			return walletMessageVo;
		}
		if (wallet.getBalance().subtract(wallet.getFreezeAmount()).compareTo(amount)<0 ) {
			walletMessageVo.setErrorCode(WalletErrorCodeEnum.ERROR_CODE_1005.getErrorCode().toString());
			walletMessageVo.setErrorMessage(WalletErrorCodeEnum.ERROR_CODE_1005.getErrorMessage());
			return walletMessageVo;
		}
		
		// 创建申请提现
		WithdrawDetail withdrawDetail = new WithdrawDetail();
		withdrawDetail.setWalletId(walletId);
		withdrawDetail.setAccountName(accountName);
		withdrawDetail.setBlankName(blankName);
		withdrawDetail.setCardCode(cardCode);
		withdrawDetail.setUserId(userId);
		withdrawDetail.setUserName(userName);
		withdrawDetail.setAmount(amount);
		withdrawDetail.setReqTime(DateUtil.getCurrentDateTime());
		withdrawDetail.setStatus(0);
		
		withdrawDetail.setUserPhone(phone);
		walletCommonDao.save(withdrawDetail);
		//冻结提现金额
		wallet.setFreezeAmount(wallet.getFreezeAmount().add(amount));
		wallet.setModifyTime(DateUtil.getCurrentDateTime());
		walletCommonDao.update(wallet);
		
		walletMessageVo.setResult(true);
		walletMessageVo.setT(withdrawDetail);
		return walletMessageVo;
	}
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED,rollbackFor = Exception.class)
	public WalletMessageVo<WithdrawDetail> checkWithdraw(Long withdrawId,Long approverId,String userName, String beizhu,Integer status) {
		WalletMessageVo<WithdrawDetail> walletMessageVo = new WalletMessageVo<>();
		// 校验参数
		if(withdrawId == null || approverId== null || status == null || StringUtils.isBlank(userName)){
			walletMessageVo.setErrorCode(WalletErrorCodeEnum.ERROR_CODE_1000.getErrorCode().toString());
			walletMessageVo.setErrorMessage(WalletErrorCodeEnum.ERROR_CODE_1000.getErrorMessage());
			return walletMessageVo;
		}
		//查询提现信息
		List<WithdrawDetail> wdList = walletCommonDao.getList(WithdrawDetail.class, "id",withdrawId,"status",0);
		if(CollectionUtils.isEmpty(wdList)){
			walletMessageVo.setErrorCode(WalletErrorCodeEnum.ERROR_CODE_1009.getErrorCode().toString());
			walletMessageVo.setErrorMessage(WalletErrorCodeEnum.ERROR_CODE_1009.getErrorMessage());
			return walletMessageVo;
		}
		WithdrawDetail withdrawDetail = wdList.get(0);
		Wallet wallet = walletCommonDao.get(Wallet.class, "walletId",withdrawDetail.getWalletId());
		
		String uuid = WalletUtils.buildToken();
		// 通过审核 ，清空冻结余额
		withdrawDetail.setStatus(status);
		withdrawDetail.setApproverId(approverId);
		withdrawDetail.setApproverName(userName);
		withdrawDetail.setInspectTime(DateUtil.getCurrentDateTime());
		withdrawDetail.setInspectInfo(beizhu);
		//冻结金额无论是否通过都改变
		wallet.setFreezeAmount(wallet.getFreezeAmount().subtract(withdrawDetail.getAmount()));
		if(status == 1){
			withdrawDetail.setBusinessNo(uuid);
			//通过时将余额扣除,并记录钱包余额变动信息
			wallet.setBalance(wallet.getBalance().subtract(withdrawDetail.getAmount()));
			//记录提现记录
			WalletBillDetail walletBillDetail = new WalletBillDetail();
			walletBillDetail.setWalletId(wallet.getWalletId());
			walletBillDetail.setTaskType(1);
			walletBillDetail.setAmount(withdrawDetail.getAmount());
			walletBillDetail.setBalance(wallet.getBalance());
			walletBillDetail.setAmountType(1);
			walletBillDetail.setDescription(beizhu);
			walletBillDetail.setCreateTime(DateUtil.getCurrentDateTime());
			walletBillDetail.setBusinessNo(uuid);
			walletCommonDao.save(walletBillDetail);
		}
		wallet.setModifyTime(DateUtil.getCurrentDateTime());
		walletCommonDao.update(withdrawDetail);
		walletCommonDao.update(wallet);
		
		walletMessageVo.setResult(true);
		walletMessageVo.setT(withdrawDetail);
		return walletMessageVo;
	}
	
	
	public boolean checkParam(Long walletId,String accountName,BigDecimal amount,String userName,String blankName,String cardCode){
		if(walletId == null || StringUtils.isBlank(accountName) || StringUtils.isBlank(userName) || StringUtils.isBlank(blankName) || StringUtils.isBlank(cardCode)){
			return true;
		}
		Pattern pattern=Pattern.compile("^(([1-9]{1}\\d*)|([0]{1}))(\\.(\\d){0,2})?$");  
        //被校验的字符串  
        Matcher match=pattern.matcher(String.valueOf(amount.doubleValue()));  
        if(!match.matches()){
        	return true;
        }
		return false;
	}
	
}
