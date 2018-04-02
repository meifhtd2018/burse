package com.hnair.consumer.wallet.api.impl;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.hnair.consumer.dao.spi.ICommonDao;
import com.hnair.consumer.dao.utils.PageFinder;
import com.hnair.consumer.dao.utils.Query;
import com.hnair.consumer.wallet.api.IWithdrawApi;
import com.hnair.consumer.wallet.model.WithdrawAccount;
import com.hnair.consumer.wallet.model.WithdrawDetail;
import com.hnair.consumer.wallet.service.IWalletService;
import com.hnair.consumer.wallet.service.IWithdrawService;
import com.hnair.consumer.wallet.vo.WalletMessageVo;

@Component("withdrawApi")
public class WithdrawApiImpl implements IWithdrawApi {

	@Resource
	private IWithdrawService withdrawService;
	
	@Resource
	private IWalletService walletService;
	
	@Resource
	private ICommonDao walletCommonDao;
	
	@Override
	public WalletMessageVo<WithdrawDetail> withdraw(String token,String accountName,BigDecimal amount,Long userId,String userName,String blankName,String cardCode,String phone) {
		WalletMessageVo<WithdrawDetail> walletMessageVo = new WalletMessageVo<>();
		WalletMessageVo<Long> wallet = walletService.getWalletIdByToken(token);
		if(!wallet.isResult()){
			walletMessageVo.setErrorCode(wallet.getErrorCode());
			walletMessageVo.setErrorMessage(wallet.getErrorMessage());
			return walletMessageVo;
		}
		Long walletId = wallet.getT();
		walletMessageVo = withdrawService.createWithdraw(walletId,accountName,amount,userId,userName,blankName,cardCode,phone);
		
		return walletMessageVo;
	}

	@Override
	public WalletMessageVo<WithdrawDetail> withdrawCheck(String token,Long withdrawId,Long approverId,String userName, String beizhu,Integer status) {
		WalletMessageVo<WithdrawDetail> walletMessageVo = new WalletMessageVo<>();
		WalletMessageVo<Long> wallet = walletService.getWalletIdByToken(token);
		if(!wallet.isResult()){
			walletMessageVo.setErrorCode(wallet.getErrorCode());
			walletMessageVo.setErrorMessage(wallet.getErrorMessage());
			return walletMessageVo;
		}
		walletMessageVo = withdrawService.checkWithdraw(withdrawId,approverId,userName, beizhu, status);
		
		return walletMessageVo;
	}

	@Override
	public WalletMessageVo<List<WithdrawDetail>> queryWithdraw(String token,Integer pageNumber,Integer pageSize,Map<String, Object> map) {
		WalletMessageVo<List<WithdrawDetail>> walletMessageVo = new WalletMessageVo<>();
		
		WalletMessageVo<Long> wallet = walletService.getWalletIdByToken(token);
		if(!wallet.isResult()){
			walletMessageVo.setErrorCode(wallet.getErrorCode());
			walletMessageVo.setErrorMessage(wallet.getErrorMessage());
			return walletMessageVo;
		}
		Long walletId = wallet.getT();
		map.put("walletId", walletId);
		Query query = new Query();
		query.setPageSize(pageSize);
		query.setPage(pageNumber);
		//List<WithdrawDetail> list = walletCommonDao.getList(WithdrawDetail.class, "walletId",walletId);
		//List<WithdrawDetail> list = walletCommonDao.getBySqlId(WithdrawDetail.class, "selectWithdrawByMap", map);
		PageFinder<WithdrawDetail> pageFinder = walletCommonDao.getPageFinder(WithdrawDetail.class, query, map);
		walletMessageVo.setResult(true);
		walletMessageVo.setCount(pageFinder.getRowCount());
		walletMessageVo.setT(pageFinder.getData());
		return walletMessageVo;
	}

	@Override
	@Transactional
	public WalletMessageVo<Boolean> bindWithdrawAccount(String token, Integer type, String account,
			String acctounName) {
		WalletMessageVo<Boolean> result=new WalletMessageVo<>();
		
		//验证token是否有效
		WalletMessageVo<Long> tokenResult = walletService.getWalletIdByToken(token);
		if (!tokenResult.isResult()) {
			result.setErrorCode(tokenResult.getErrorCode());
			result.setErrorMessage(tokenResult.getErrorMessage());
			return result;
		}
		Long walletId=tokenResult.getT();
		WithdrawAccount withdrawAccount=new WithdrawAccount();
		withdrawAccount.setAccount(account);
		withdrawAccount.setAccountName(acctounName);
		withdrawAccount.setType(type);
		withdrawAccount.setWalletId(walletId);
		walletCommonDao.save(withdrawAccount);
		result.setResult(true);
		result.setT(true);
		
		return result;
	}

	@Override
	public WalletMessageVo<List<WithdrawDetail>> queryCheckWithdraw(String token,Integer pageNumber,Integer pageSize,Map<String, Object> map) {
		WalletMessageVo<List<WithdrawDetail>> walletMessageVo = new WalletMessageVo<>();
		//验证token是否有效
		WalletMessageVo<Long> tokenResult = walletService.getWalletIdByToken(token);
		if (!tokenResult.isResult()) {
			walletMessageVo.setErrorCode(tokenResult.getErrorCode());
			walletMessageVo.setErrorMessage(tokenResult.getErrorMessage());
			return walletMessageVo;
		}
		Query query = new Query();
		query.setPageSize(pageSize);
		query.setPage(pageNumber);
		Integer status = (Integer)map.get("status");
		if(status != null && status == 3){
			PageFinder<WithdrawDetail> pageFinder = walletCommonDao.getPageFinder(WithdrawDetail.class, query, "selectCheckCount", "selectCheckOver", map);
			walletMessageVo.setResult(true);
			walletMessageVo.setCount(pageFinder.getRowCount());
			walletMessageVo.setT(pageFinder.getData());
			return walletMessageVo;
		}
		PageFinder<WithdrawDetail> pageFinder = walletCommonDao.getPageFinder(WithdrawDetail.class, query, map);
		walletMessageVo.setResult(true);
		walletMessageVo.setCount(pageFinder.getRowCount());
		walletMessageVo.setT(pageFinder.getData());
		return walletMessageVo;
	}


}
