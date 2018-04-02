package com.hnair.consumer.wallet.model;

import java.io.Serializable;
import java.math.BigDecimal;


/**
 * PayBillDetail Entity.
 */
public class PayBillDetail implements Serializable{
	
	//列信息
	private Long id;
	
	private Long payerWalletId;
	
	private Long payeeWalletId;
	
	private Integer applicationCode;
	
	private java.util.Date tradeTime;
	
	private String orderNo;
	
	private BigDecimal amount;
	
	private BigDecimal commission;
	
	private Integer status;
	
	private String remark;
	
	private java.util.Date createTime;
	
	private String tradeNo;
	
	private String subject;
	
	private String notifyUrl;
	

		
	public void setId(Long value) {
		this.id = value;
	}
	
	public Long getId() {
		return this.id;
	}
		
		
	public void setPayerWalletId(Long value) {
		this.payerWalletId = value;
	}
	
	public Long getPayerWalletId() {
		return this.payerWalletId;
	}
		
		
	public void setPayeeWalletId(Long value) {
		this.payeeWalletId = value;
	}
	
	public Long getPayeeWalletId() {
		return this.payeeWalletId;
	}
		
		
	public void setApplicationCode(Integer value) {
		this.applicationCode = value;
	}
	
	public Integer getApplicationCode() {
		return this.applicationCode;
	}
		
		
	public void setTradeTime(java.util.Date value) {
		this.tradeTime = value;
	}
	
	public java.util.Date getTradeTime() {
		return this.tradeTime;
	}
		
		
	public void setOrderNo(String value) {
		this.orderNo = value;
	}
	
	public String getOrderNo() {
		return this.orderNo;
	}
		
		
	public void setAmount(BigDecimal value) {
		this.amount = value;
	}
	
	public BigDecimal getAmount() {
		return this.amount;
	}
		
		
	public void setCommission(BigDecimal value) {
		this.commission = value;
	}
	
	public BigDecimal getCommission() {
		return this.commission;
	}
		
		
	public void setStatus(Integer value) {
		this.status = value;
	}
	
	public Integer getStatus() {
		return this.status;
	}
		
		
	public void setRemark(String value) {
		this.remark = value;
	}
	
	public String getRemark() {
		return this.remark;
	}
		
		
	public void setCreateTime(java.util.Date value) {
		this.createTime = value;
	}
	
	public java.util.Date getCreateTime() {
		return this.createTime;
	}
		
		
	public void setTradeNo(String value) {
		this.tradeNo = value;
	}
	
	public String getTradeNo() {
		return this.tradeNo;
	}
		
		
	public void setSubject(String value) {
		this.subject = value;
	}
	
	public String getSubject() {
		return this.subject;
	}
		
		
	public void setNotifyUrl(String value) {
		this.notifyUrl = value;
	}
	
	public String getNotifyUrl() {
		return this.notifyUrl;
	}
		
}

