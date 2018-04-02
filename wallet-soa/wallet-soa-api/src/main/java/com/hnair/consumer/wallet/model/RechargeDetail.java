package com.hnair.consumer.wallet.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;


/**
 * RechargeDetail Entity.
 */
public class RechargeDetail implements Serializable{

	private static final long serialVersionUID = 8287418622548505006L;
	//列信息
	private Long id;

	private BigDecimal amount;

	private Integer status;

	private String applyUid;

	private Long walletId;

	private java.util.Date createTime;

	private java.util.Date modifyTime;

	private String auditUid;

	private String reqInfo;

	private String inspectInfo;

	private java.util.Date inspectTime;

	private String applyUserName;

	private String auditUserName;

	private String applyUsePhone;

	private String serialNumber;

	private String tradingAccountName;

	private String tradingAccountNumber;

	private String businessNo;

	private String startTime;

	private String endTime;


	private java.util.Date oldModifyTime;

	public Date getOldModifyTime() {
		return oldModifyTime;
	}

	public void setOldModifyTime(Date oldModifyTime) {
		this.oldModifyTime = oldModifyTime;
	}


	public void setId(Long value) {
		this.id = value;
	}

	public Long getId() {
		return this.id;
	}


	public void setAmount(BigDecimal value) {
		this.amount = value;
	}

	public BigDecimal getAmount() {
		return this.amount;
	}

	public void setStatus(Integer value) {
		this.status = value;
	}

	public Integer getStatus() {
		return this.status;
	}


	public void setApplyUid(String value) {
		this.applyUid = value;
	}

	public String getApplyUid() {
		return this.applyUid;
	}


	public void setWalletId(Long value) {
		this.walletId = value;
	}

	public Long getWalletId() {
		return this.walletId;
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


	public void setAuditUid(String value) {
		this.auditUid = value;
	}

	public String getAuditUid() {
		return this.auditUid;
	}


	public void setReqInfo(String value) {
		this.reqInfo = value;
	}

	public String getReqInfo() {
		return this.reqInfo;
	}


	public void setInspectInfo(String value) {
		this.inspectInfo = value;
	}

	public String getInspectInfo() {
		return this.inspectInfo;
	}


	public void setInspectTime(java.util.Date value) {
		this.inspectTime = value;
	}

	public java.util.Date getInspectTime() {
		return this.inspectTime;
	}


	public void setApplyUserName(String value) {
		this.applyUserName = value;
	}

	public String getApplyUserName() {
		return this.applyUserName;
	}


	public void setAuditUserName(String value) {
		this.auditUserName = value;
	}

	public String getAuditUserName() {
		return this.auditUserName;
	}


	public void setApplyUsePhone(String value) {
		this.applyUsePhone = value;
	}

	public String getApplyUsePhone() {
		return this.applyUsePhone;
	}


	public void setSerialNumber(String value) {
		this.serialNumber = value;
	}

	public String getSerialNumber() {
		return this.serialNumber;
	}


	public void setTradingAccountName(String value) {
		this.tradingAccountName = value;
	}

	public String getTradingAccountName() {
		return this.tradingAccountName;
	}


	public void setTradingAccountNumber(String value) {
		this.tradingAccountNumber = value;
	}

	public String getTradingAccountNumber() {
		return this.tradingAccountNumber;
	}

	public void setBusinessNo(String value) {
		this.businessNo = value;
	}

	public String getBusinessNo() {
		return this.businessNo;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
}

