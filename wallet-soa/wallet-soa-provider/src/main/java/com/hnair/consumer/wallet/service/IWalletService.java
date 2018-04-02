package com.hnair.consumer.wallet.service;

import com.hnair.consumer.wallet.model.PayBillDetail;
import com.hnair.consumer.wallet.model.Wallet;
import com.hnair.consumer.wallet.vo.PaymentVo;
import com.hnair.consumer.wallet.vo.WalletMessageVo;
import com.hnair.consumer.wallet.vo.WalletVo;

import java.util.List;
import java.util.Map;

public interface IWalletService {

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
	 * 验证token并获取钱包id
	 * @param token
	 * @return
	 * @author 陶嘉骏
	 * @date 2018年3月2日
	 */
	public WalletMessageVo<Long>getWalletIdByToken (String token);


	/**
	 * 生成钱包账户
	 * @param account_name
	 * @param user_id
	 * @param application_code
	 * @param payPassword
	 * @return
	 */
	WalletMessageVo<WalletVo> createWalletAccount(String tolen,String account_name, String user_id, Integer application_code, String payPassword,String phone);

	/**
	 * 钱包列表
	 */
	WalletMessageVo<List<WalletVo>> queryWalletList(String token,WalletVo walletVo,Integer pageNumber,Integer pageSize);

	WalletMessageVo<WalletVo> getWallet(String token);
	
	/**
	 * 支付密码验证
	 * @param wallet 钱包对象
	 * @param payPassword 输入的支付密码
	 * @return
	 * @author 陶嘉骏
	 * @date 2018年3月6日
	 */
	public WalletMessageVo<Boolean> checkPayPassword(Wallet wallet,String payPassword);
	
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
