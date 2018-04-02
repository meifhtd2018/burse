package com.hnair.consumer.wallet.model;

import java.io.Serializable;


/**
 * Merchant Entity.
 */
public class Merchant implements Serializable{
	
	//列信息
	private Long merchantId;
	
	private String merchantName;
	
	private String domainName;
	
	private String contactPerson;
	
	private String contactType;
	
	private String contactAddress;
	
	private java.util.Date createTime;
	
	private java.util.Date modifyTime;
	
	private Long payeeAccount;
	
	private String appSecret;
	

		
	public void setMerchantId(Long value) {
		this.merchantId = value;
	}
	
	public Long getMerchantId() {
		return this.merchantId;
	}
		
		
	public void setMerchantName(String value) {
		this.merchantName = value;
	}
	
	public String getMerchantName() {
		return this.merchantName;
	}
		
		
	public void setDomainName(String value) {
		this.domainName = value;
	}
	
	public String getDomainName() {
		return this.domainName;
	}
		
		
	public void setContactPerson(String value) {
		this.contactPerson = value;
	}
	
	public String getContactPerson() {
		return this.contactPerson;
	}
		
		
	public void setContactType(String value) {
		this.contactType = value;
	}
	
	public String getContactType() {
		return this.contactType;
	}
		
		
	public void setContactAddress(String value) {
		this.contactAddress = value;
	}
	
	public String getContactAddress() {
		return this.contactAddress;
	}
		
		
	public void setCreateTime(java.util.Date value) {
		this.createTime = value;
	}
	
	public java.util.Date getCreateTime() {
		return this.createTime;
	}
		
		
	public void setModifyTime(java.util.Date value) {
		this.modifyTime = value;
	}
	
	public java.util.Date getModifyTime() {
		return this.modifyTime;
	}
		
		
	public void setPayeeAccount(Long value) {
		this.payeeAccount = value;
	}
	
	public Long getPayeeAccount() {
		return this.payeeAccount;
	}
		
		
	public void setAppSecret(String value) {
		this.appSecret = value;
	}
	
	public String getAppSecret() {
		return this.appSecret;
	}
		
}

