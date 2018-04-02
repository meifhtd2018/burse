package com.hnair.consumer.wallet.model;

import java.io.Serializable;
import java.util.Date;

/**
 * Description: 用户主键生成实体
 * All Rights Reserved.
 */
public class WalletIdGen implements Serializable{

	private static final long serialVersionUID = -2650261133425422961L;
	//用户id
	private Long walletId;
	//创建日期
	private Date createDate;
	
	public Long getWalletId() {
		return walletId;
	}
	public void setWalletId(Long walletId) {
		this.walletId = walletId;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	
	
}
