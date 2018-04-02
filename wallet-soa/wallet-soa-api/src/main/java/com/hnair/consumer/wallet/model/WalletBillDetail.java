package com.hnair.consumer.wallet.model;

import java.io.Serializable;
import java.math.BigDecimal;


/**
 * WalletBillDetail Entity.
 */
public class WalletBillDetail implements Serializable{
	
	//列信息
	private Long id;
	
	private Long walletId;
	
	private Integer taskType;
	
	private String businessNo;
	
	private BigDecimal amount;
	
	private BigDecimal balance;
	
	private Integer amountType;
	
	private String description;
	
	private java.util.Date createTime;
	

		
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
		
		
	public void setTaskType(Integer value) {
		this.taskType = value;
	}
	
	public Integer getTaskType() {
		return this.taskType;
	}
		
		
	public void setBusinessNo(String value) {
		this.businessNo = value;
	}
	
	public String getBusinessNo() {
		return this.businessNo;
	}
		
	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public BigDecimal getBalance() {
		return balance;
	}

	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}

	public void setAmountType(Integer value) {
		this.amountType = value;
	}
	
	public Integer getAmountType() {
		return this.amountType;
	}
		
		
	public void setDescription(String value) {
		this.description = value;
	}
	
	public String getDescription() {
		return this.description;
	}
		
		
	public void setCreateTime(java.util.Date value) {
		this.createTime = value;
	}
	
	public java.util.Date getCreateTime() {
		return this.createTime;
	}
		
}

