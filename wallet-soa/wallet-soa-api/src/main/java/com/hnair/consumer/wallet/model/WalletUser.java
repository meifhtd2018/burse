package com.hnair.consumer.wallet.model;

import java.io.Serializable;


/**
 * WalletUser Entity.
 */
public class WalletUser implements Serializable{
	
	//列信息
	private Long id;
	
	private Long walletId;
	
	private String userId;
	
	private Integer applicationCode;
	
	private String accountName;
	
	private String phone;
	

		
	public void setId(Long value) {
		this.id = value;
	}
	
	public Long getId() {
		return this.id;
	}
		
		
	public void setWalletId(Long value) {
		this.walletId = value;
	}
	
	public Long getWalletId() {
		return this.walletId;
	}
		
		
	public void setUserId(String value) {
		this.userId = value;
	}
	
	public String getUserId() {
		return this.userId;
	}
		
		
	public void setApplicationCode(Integer value) {
		this.applicationCode = value;
	}
	
	public Integer getApplicationCode() {
		return this.applicationCode;
	}
		
		
	public void setAccountName(String value) {
		this.accountName = value;
	}
	
	public String getAccountName() {
		return this.accountName;
	}
		
		
	public void setPhone(String value) {
		this.phone = value;
	}
	
	public String getPhone() {
		return this.phone;
	}
		
}

