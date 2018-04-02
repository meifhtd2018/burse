package com.hnair.consumer.wallet;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.hnair.consumer.wallet.api.IWalletApi;
import com.hnair.consumer.wallet.api.IWithdrawApi;
import com.hnair.consumer.wallet.model.PayBillDetail;
import com.hnair.consumer.wallet.model.WalletBillDetail;
import com.hnair.consumer.wallet.model.WithdrawDetail;
import com.hnair.consumer.wallet.vo.WalletMessageVo;

public class WithdrawTest {
	ApplicationContext ioc = new ClassPathXmlApplicationContext("/config/spring_common.xml");
	IWalletApi walletApi = ioc.getBean(IWalletApi.class);
	IWithdrawApi withdrawApi = ioc.getBean(IWithdrawApi.class);
	
	@Test
	public void test(){
		WalletMessageVo<String> accessToken = walletApi.getAccessToken(100000001l, "e6f8c4636b989f44c5d2acb1104bb716603bd00c");
		String token = accessToken.getT();
		Map<String, Object> map	= new HashMap<>();
		map.put("startTime", "2018-03-12 16:47:55");
		map.put("endTime", "2018-03-13 18:49:01");
		WalletMessageVo<List<WithdrawDetail>> queryWithdraw = withdrawApi.queryWithdraw(token, 1, 2, map);
		boolean result = queryWithdraw.isResult();
		if(!result){
			System.out.println(queryWithdraw.getErrorMessage());
		}
		List<WithdrawDetail> t = queryWithdraw.getT();
		for (WithdrawDetail withdrawDetail : t) {
			System.out.println(withdrawDetail);
		}
	}
	
	@Test
	public void test1(){
		WalletMessageVo<String> accessToken = walletApi.getAccessToken(100000001l, "e6f8c4636b989f44c5d2acb1104bb716603bd00c");
		String token = accessToken.getT();
		Map<String, Object> map	= new HashMap<>();
		map.put("startTime", "2018-03-12 13:57:46");
		WalletMessageVo<List<WalletBillDetail>> walletBillDetali = walletApi.getWalletBillDetali(token, 2, 3, map);
		if(!walletBillDetali.isResult()){
			System.out.println(walletBillDetali.getErrorMessage());
		}
		List<WalletBillDetail> t = walletBillDetali.getT();
		for (WalletBillDetail walletBillDetail : t) {
			System.out.println(walletBillDetail);
		}
	}
	
	@Test
	public void test2(){
		WalletMessageVo<String> accessToken = walletApi.getAccessToken(100000001l, "e6f8c4636b989f44c5d2acb1104bb716603bd00c");
		String token = accessToken.getT();
		Map<String, Object> map = new HashMap<>();
		map.put("startTime", "2018-03-12 16:47:55");
		map.put("endTime", "2018-03-13 18:49:01");
		map.put("serialNumber", 321321321);
		map.put("status", 3);
		WalletMessageVo<List<WithdrawDetail>> queryCheckWithdraw = withdrawApi.queryCheckWithdraw(token, 1, 22, map);
		if(!queryCheckWithdraw.isResult()){
			System.out.println(queryCheckWithdraw.getErrorMessage());
		}
		List<WithdrawDetail> t = queryCheckWithdraw.getT();
		for (WithdrawDetail withdrawDetail : t) {
			System.out.println(withdrawDetail);
		}
	}
	
	@Test
	public void test3(){
		WalletMessageVo<String> accessToken = walletApi.getAccessToken(100000001l, "e6f8c4636b989f44c5d2acb1104bb716603bd00c");
		String token = accessToken.getT();
		Map<String, Object> map = new HashMap<>();
		//map.put("startTime", "2018-03-12 16:47:55");
		//map.put("endTime", "2018-03-13 18:49:01");
		//map.put("serialNumber", 321321321);
		//map.put("status", 3);
		
		WalletMessageVo<List<PayBillDetail>> queryCheckWithdraw = walletApi.getPayTradeDetail(token, 1, 50, map);
		if(!queryCheckWithdraw.isResult()){
			System.out.println(queryCheckWithdraw.getErrorMessage());
		}
		List<PayBillDetail> t = queryCheckWithdraw.getT();
		System.out.println(t.size());
		for (PayBillDetail withdrawDetail : t) {
			System.out.println(withdrawDetail);
			System.out.println();
		}
	}
	
	
	
}
