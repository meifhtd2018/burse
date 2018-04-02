package com.hnair.consumer.wallet.model;

import java.io.Serializable;
import java.math.BigDecimal;


/**
 * WithdrawDetail Entity.
 */
public class WithdrawDetail implements Serializable{

	private static final long serialVersionUID = -8518635455964128099L;

	//列信息
	private Long id;
	
	private Long walletId;
	
	private Long userId;
	
	private String userName;
	
	private String userPhone;
	
	private java.util.Date reqTime;
	
	private BigDecimal amount;
	
	private String accountName;
	
	private String blankName;
	
	private String cardCode;
	
	private Integer status;
	
	private Long approverId;
	
	private String approverName;
	
	private java.util.Date inspectTime;
	
	private String inspectInfo;
	
	private String businessNo;
	
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
		
		
	public void setUserId(Long value) {
		this.userId = value;
	}
	
	public Long getUserId() {
		return this.userId;
	}
		
		
	public void setUserName(String value) {
		this.userName = value;
	}
	
	public String getUserName() {
		return this.userName;
	}
		
		
	public void setReqTime(java.util.Date value) {
		this.reqTime = value;
	}
	
	public java.util.Date getReqTime() {
		return this.reqTime;
	}
	
	public void setAmount(BigDecimal value) {
		this.amount = value;
	}
	
	public BigDecimal getAmount() {
		return this.amount;
	}
		
	public void setBlankName(String value) {
		this.blankName = value;
	}
	
	public String getBlankName() {
		return this.blankName;
	}
		
		
	public void setCardCode(String value) {
		this.cardCode = value;
	}
	
	public String getCardCode() {
		return this.cardCode;
	}
		
		
	public void setStatus(Integer value) {
		this.status = value;
	}
	
	public Integer getStatus() {
		return this.status;
	}
		
		
	public void setApproverId(Long value) {
		this.approverId = value;
	}
	
	public Long getApproverId() {
		return this.approverId;
	}
		
		
	public void setApproverName(String value) {
		this.approverName = value;
	}
	
	public String getApproverName() {
		return this.approverName;
	}
		
		
	public void setInspectTime(java.util.Date value) {
		this.inspectTime = value;
	}
	
	public java.util.Date getInspectTime() {
		return this.inspectTime;
	}
		
		
	public void setInspectInfo(String value) {
		this.inspectInfo = value;
	}
	
	public String getInspectInfo() {
		return this.inspectInfo;
	}
		
	public String getBusinessNo() {
		return businessNo;
	}

	public void setBusinessNo(String businessNo) {
		this.businessNo = businessNo;
	}
	
	public String getUserPhone() {
		return userPhone;
	}

	public void setUserPhone(String userPhone) {
		this.userPhone = userPhone;
	}

	public String getAccountName() {
		return accountName;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}
	
}

