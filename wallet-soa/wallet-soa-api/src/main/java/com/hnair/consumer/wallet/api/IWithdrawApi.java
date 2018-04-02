package com.hnair.consumer.wallet.api;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.hnair.consumer.wallet.model.WithdrawDetail;
import com.hnair.consumer.wallet.vo.WalletMessageVo;

public interface IWithdrawApi {
	/**
	 * 提现申请接口
	 * @param token
	 * @param amount 金额
	 * @param userId 申请人Id
	 * @param userName 申请人名字
	 * @param blankName 开户行
	 * @param cardCode 开户行卡号
	 * @return
	 */
	public WalletMessageVo<WithdrawDetail> withdraw(String token,String accountName,BigDecimal amount,Long userId,String userName,String blankName,String cardCode,String phone);
	/**
	 * 提现审核接口
	 * @param token
	 * @param withdrawId 申请提现id
	 * @param approverId 审核人id
	 * @param userName 审核人名字
	 * @param beizhu 审核备注
	 * @param status 1通过，2不通过
	 * @param serialNumber 银行流水
	 * @param accountCode 转出行卡号
	 * @return
	 */
	public WalletMessageVo<WithdrawDetail> withdrawCheck(String token,Long withdrawId,Long approverId,String userName, String beizhu,Integer status);
	/**
	 * 查询提现信息
	 * @param token
	 * @return
	 */
	public WalletMessageVo<List<WithdrawDetail>> queryWithdraw(String token,Integer pageNumber,Integer pageSize,Map<String, Object> map);
	
	/**
	 * 查询提现申请记录
	 * @param token
	 * @param status 申请状态 0申请中1申请成功2申请失败
	 * @return
	 */
	public WalletMessageVo<List<WithdrawDetail>> queryCheckWithdraw(String token,Integer pageNumber,Integer pageSize,Map<String, Object> map);
	
	/**
	 * 绑定钱包提现账号
	 * @param token 钱包授权使用token
	 * @param type 提现账号类型：1-银行卡;2-支付宝；3-微信
	 * @param account 提现账号
	 * @param acctounName 账号名称/开户行名称
	 * @return
	 * @author 陶嘉骏
	 * @date 2018年3月6日
	 */
	public WalletMessageVo<Boolean> bindWithdrawAccount(String token,Integer type,String account,String acctounName);
	
	
}
