package com.hnair.consumer.wallet.api.impl;

import javax.annotation.Resource;

import com.hnair.consumer.dao.spi.ICommonDao;
import com.hnair.consumer.dao.utils.PageFinder;
import com.hnair.consumer.dao.utils.Query;
import com.hnair.consumer.wallet.model.PayBillDetail;
import com.hnair.consumer.wallet.model.Wallet;
import com.hnair.consumer.wallet.model.WalletBillDetail;
import com.hnair.consumer.wallet.vo.WalletVo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.hnair.consumer.wallet.api.IWalletApi;
import com.hnair.consumer.wallet.service.IWalletService;
import com.hnair.consumer.wallet.vo.PaymentVo;
import com.hnair.consumer.wallet.vo.WalletMessageVo;

import java.util.List;
import java.util.Map;

@Component("walletApi")
public class WalletApiImpl implements IWalletApi {
	
	private static final Logger logger = LoggerFactory.getLogger(WalletApiImpl.class);
	
	@Resource
	private IWalletService walletService;
	@Resource
	private ICommonDao walletCommonDao;

	@Override
	public WalletMessageVo<String> getAccessToken(Long walletId, String secret) {
		
		return walletService.getAccessToken(walletId, secret);
	}

	@Override
	public WalletMessageVo<WalletVo> createWalletAccount(String tolen,String account_name, String user_id, Integer application_code, String payPassword, String phone) {

		return walletService.createWalletAccount(tolen,account_name, user_id,application_code,payPassword,phone);

	}

	@Override
	public WalletMessageVo<List<WalletVo>> queryWalletList(String token,WalletVo walletVo,Integer pageNumber,Integer pageSize) {
		return walletService.queryWalletList(token,walletVo,pageNumber,pageSize);
	}



	@Override
	public WalletMessageVo<WalletVo> getWalletInfo(String token) {
		return walletService.getWallet(token);
	}

	@Override
	public WalletMessageVo<List<WalletBillDetail>> getWalletBillDetali(String token,Integer pageNumber,Integer pageSize,Map<String, Object> map) {
		WalletMessageVo<List<WalletBillDetail>> walletMessageVo = new WalletMessageVo<>();
		WalletMessageVo<Long> walletIdByToken = walletService.getWalletIdByToken(token);
		if(!walletIdByToken.isResult()){
			walletMessageVo.setErrorCode(walletIdByToken.getErrorCode());
			walletMessageVo.setErrorMessage(walletIdByToken.getErrorMessage());
			return walletMessageVo;
		}
		Long walletId = walletIdByToken.getT();
		Query query = new Query();
        query.setPageSize(pageSize);
        query.setPage(pageNumber);
		//List<WalletBillDetail> list = walletCommonDao.getList(WalletBillDetail.class, "walletId",walletId);
		map.put("walletId", walletId);
		PageFinder<WalletBillDetail> pageFinder = walletCommonDao.getPageFinder(WalletBillDetail.class, query, map);
		walletMessageVo.setResult(true);
		walletMessageVo.setCount(pageFinder.getRowCount());
		walletMessageVo.setT(pageFinder.getData());
		return walletMessageVo;
	}

	@Override
	public WalletMessageVo<List<PayBillDetail>> getPayTradeDetail(String token,Integer pageNumber,Integer pageSize,Map<String, Object> map) {
		WalletMessageVo<List<PayBillDetail>> walletMessageVo = new WalletMessageVo<>();
		
		WalletMessageVo<Long> walletIdByToken = walletService.getWalletIdByToken(token);
		if(!walletIdByToken.isResult()){
			walletMessageVo.setErrorCode(walletIdByToken.getErrorCode());
			walletMessageVo.setErrorMessage(walletIdByToken.getErrorMessage());
			return walletMessageVo;
		}
		Query query = new Query();
        query.setPageSize(pageSize);
        query.setPage(pageNumber);
		Long walletId = walletIdByToken.getT();
		map.put("walletId", walletId);
		
		for(Map.Entry<String, Object> entry : map.entrySet()){  
			logger.info("Key = "+entry.getKey()+",value="+entry.getValue());
		}
		PageFinder<PayBillDetail> pageFinder = walletCommonDao.getPageFinder(PayBillDetail.class, query, map);
		walletMessageVo.setResult(true);
		walletMessageVo.setCount(pageFinder.getRowCount());
		walletMessageVo.setT(pageFinder.getData());
		return walletMessageVo;
	}
	

	@Override
	public WalletMessageVo<Boolean> modifyPayPassword(String token, String originalPwd, String newPwd) {
		
		return walletService.modifyPayPassword(token, originalPwd, newPwd);
	}
	

	@Override
	public WalletMessageVo<Map<String,String>> tradeCreate(PaymentVo req) {
		return walletService.tradeCreate(req);
	}

	@Override
	public WalletMessageVo<PayBillDetail> walletPayment(String token, String tradeNo, String payPwd) {
		return walletService.walletPayment(token, tradeNo, payPwd);
	}

	@Override
	public WalletMessageVo<PayBillDetail> getPayBillDetailByTradeNo(String token,String orderNo) {
		return walletService.getPayBillDetailByTradeNo(token, orderNo);
	}

	@Override
	public WalletMessageVo updateWallet(String token, WalletVo wallet) {
		return walletService.updateWallet(token,wallet);
	}


}
