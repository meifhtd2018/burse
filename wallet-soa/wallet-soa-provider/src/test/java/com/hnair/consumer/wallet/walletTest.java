package com.hnair.consumer.wallet;

import java.math.BigDecimal;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.alibaba.dubbo.common.json.JSON;
import com.alibaba.fastjson.JSONObject;
import com.hnair.consumer.wallet.api.IWalletApi;
import com.hnair.consumer.wallet.api.IWithdrawApi;
import com.hnair.consumer.wallet.model.PayBillDetail;
import com.hnair.consumer.wallet.vo.PaymentVo;
import com.hnair.consumer.wallet.vo.WalletMessageVo;

public class walletTest {

	ApplicationContext context=new ClassPathXmlApplicationContext(new String[] { "config/spring_common.xml" });
	IWalletApi walletApi = (IWalletApi)context.getBean("walletApi");
	IWithdrawApi withdrawApi=(IWithdrawApi)context.getBean("withdrawApi");
	
	@Test
	public void TestGetAccessToken() {
		WalletMessageVo<String> result=walletApi.getAccessToken(new Long("100000001") , "e6f8c4636b989f44c5d2acb1104bb716603bd00c");
		System.out.println("token="+result.getT());
	}
	
	@Test
	public void TestModifyPayPassword(){
		WalletMessageVo<Boolean> result=walletApi.modifyPayPassword("b1c74c5f2c74458dafb5d87dbc2194e4", "123456", "1234567");
		System.out.println(result);
	}
	
	
	@Test
	public void TestWalletPayment() {
		
//		WalletMessageVo<Boolean> result = walletApi.walletPayment("9885b0089f1c4bf38ccf6779054de4a4", "2018030913570100115205749934031", "1234567");
//		System.out.println("walletTest.TestWalletPayment()="+result);
	}
	
	@Test
	public void TestGetPayBillDetailByTradeNo() {
		
		WalletMessageVo<PayBillDetail> result=walletApi.getPayBillDetailByTradeNo("1065dac38d9d45ed9930a6a82d7c035e", "2018030913570100115205749934031");
		System.out.println("result="+result);
		System.out.println("payBillDetail="+JSONObject.toJSONString(result.getT()));
	}
	
	@Test
	public void TestBindWithdrawAccount() {
		WalletMessageVo<Boolean> result = withdrawApi.bindWithdrawAccount("1065dac38d9d45ed9930a6a82d7c035e", 1, "621371065483702", "招商银行");
		System.out.println("result="+result);
	}
}
