package com.hnair.consumer.wallet.model;

import java.io.Serializable;


/**
 * WithdrawAccount Entity.
 */
public class WithdrawAccount implements Serializable{
	
	//列信息
	private Long withdrawAccountId;
	
	private Long walletId;
	
	private Integer type;
	
	private String account;
	
	private String accountName;
	

		
	public void setWithdrawAccountId(Long value) {
		this.withdrawAccountId = value;
	}
	
	public Long getWithdrawAccountId() {
		return this.withdrawAccountId;
	}
		
		
	public void setWalletId(Long value) {
		this.walletId = value;
	}
	
	public Long getWalletId() {
		return this.walletId;
	}
		
		
	public void setType(Integer value) {
		this.type = value;
	}
	
	public Integer getType() {
		return this.type;
	}
		
		
	public void setAccount(String value) {
		this.account = value;
	}
	
	public String getAccount() {
		return this.account;
	}
		
		
	public void setAccountName(String value) {
		this.accountName = value;
	}
	
	public String getAccountName() {
		return this.accountName;
	}
		
}

