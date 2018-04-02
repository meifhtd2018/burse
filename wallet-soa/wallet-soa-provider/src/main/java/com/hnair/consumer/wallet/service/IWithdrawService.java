package com.hnair.consumer.wallet.service;

import java.math.BigDecimal;

import com.hnair.consumer.wallet.model.WithdrawDetail;
import com.hnair.consumer.wallet.vo.WalletMessageVo;

public interface IWithdrawService {
	/**
	 * 提现申请接口
	 * @param walletId 钱包id
	 * @param amount 提现金额
	 * @param userId 申请人id
	 * @param userName 申请人姓名
	 * @param beizhu 申请备注
	 * @return
	 */
	public WalletMessageVo<WithdrawDetail> createWithdraw(Long walletId,String accountName,BigDecimal amount,Long userId,String userName,String blankName,String cardCode,String phone);
	/**
	 * 提现审核接口
	 * @param withdrawId 提现申请id
	 * @param approverId 审核人id
	 * @param userName 审核人姓名
	 * @param beizhu 审核备注
	 * @param status 1通过，2不通过
	 * @return
	 */
	public WalletMessageVo<WithdrawDetail> checkWithdraw(Long withdrawId,Long approverId,String userName, String beizhu,Integer status);
}
