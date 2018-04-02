package com.hnair.consumer.wallet.api;

import com.hnair.consumer.wallet.model.PayBillDetail;
import com.hnair.consumer.wallet.model.Wallet;
import com.hnair.consumer.wallet.model.WalletBillDetail;
import com.hnair.consumer.wallet.vo.PaymentVo;
import com.hnair.consumer.wallet.vo.WalletMessageVo;
import com.hnair.consumer.wallet.vo.WalletVo;

import java.util.List;
import java.util.Map;

public interface IWalletApi {
	/**
	 * 获取授权使用token
	 * @param walletId 钱包id
	 * @param secret 密钥
	 * @return
	 * @author 陶嘉骏
	 * @date 2018年3月2日
	 */
	public WalletMessageVo<String> getAccessToken(Long walletId,String secret);


	/**
	 * 生成钱包账户
	 * @param account_name
	 * @param user_id
	 * @param application_code
	 * @param payPassword
	 * @return
	 */
	WalletMessageVo<WalletVo> createWalletAccount(String token,String account_name, String user_id, Integer application_code, String payPassword, String phone);

	/**
	 * 钱包列表
	 */
	public WalletMessageVo<List<WalletVo>> queryWalletList(String tolen,WalletVo walletVo,Integer pageNumber,Integer pageSize);

	/**
	 * 查看指定钱包账户信息
	 */
	public WalletMessageVo<WalletVo> getWalletInfo(String token);


	/**
	 * 查询指定钱包历史变更信息
	 * @param token 钱包id
	 * @return
	 */
	public WalletMessageVo<List<WalletBillDetail>> getWalletBillDetali(String token,Integer pageNumber,Integer pageSize,Map<String, Object> map);

	/**
	 * 查看指定钱包支付记录
	 * @param token
	 * @return
	 */
	public WalletMessageVo<List<PayBillDetail>> getPayTradeDetail(String token,Integer pageNumber,Integer pageSize,Map<String, Object> map);
	
	/**
	 * 修改支付密码
	 * @param token 钱包授权使用token
	 * @param originalPwd 原密码
	 * @param newPwd 新密码
	 * @return 
	 * @author 陶嘉骏
	 * @date 2018年3月6日
	 */
	public WalletMessageVo<Boolean> modifyPayPassword(String token,String originalPwd,String newPwd);
	
	/**
	 * 创建支付信息
	 * @param req
	 * @return
	 * @author 陶嘉骏
	 * @date 2018年3月20日
	 */
	public WalletMessageVo<Map<String, String>> tradeCreate(PaymentVo req);
	
	/**
	 * 钱包支付
	 * @param token 钱包授权使用token
	 * @param tradeNo 支付流水号
	 * @param payPwd 支付密码
	 * @return
	 * @author 陶嘉骏
	 * @date 2018年3月20日
	 */
	public WalletMessageVo<PayBillDetail> walletPayment(String token, String tradeNo, String payPwd);
	
	/**
	 * 根据交易流水号查询支付信息
	 * @param token 钱包授权使用token
	 * @param tradeNo 订单号
	 * @return 支付信息
	 * @author 陶嘉骏
	 * @date 2018年3月7日
	 */
	public WalletMessageVo<PayBillDetail> getPayBillDetailByTradeNo(String token,String orderNo);


	/**
	 * 修改钱包账户信息
	 */
	public WalletMessageVo updateWallet(String token,WalletVo wallet);
}
