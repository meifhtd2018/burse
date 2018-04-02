package com.hnair.consumer.wallet.model;

import java.io.Serializable;
import java.math.BigDecimal;



/**
 * Wallet Entity.
 */
public class Wallet implements Serializable{

	//列信息
	private Long walletId;

	private BigDecimal balance;

	private java.util.Date createTime;

	private java.util.Date modifyTime;

	private String appSecret;

	private String payPassword;

	private Integer walletType;

	private BigDecimal freezeAmount;

	private Integer state;

	private String remark;

	private Integer disclaimer;

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public void setWalletId(Long value) {
		this.walletId = value;
	}

	public Long getWalletId() {
		return this.walletId;
	}


	public void setBalance(BigDecimal value) {
		this.balance = value;
	}

	public BigDecimal getBalance() {
		return this.balance;
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


	public void setAppSecret(String value) {
		this.appSecret = value;
	}

	public String getAppSecret() {
		return this.appSecret;
	}


	public void setPayPassword(String value) {
		this.payPassword = value;
	}

	public String getPayPassword() {
		return this.payPassword;
	}


	public void setWalletType(Integer value) {
		this.walletType = value;
	}

	public Integer getWalletType() {
		return this.walletType;
	}


	public void setFreezeAmount(BigDecimal value) {
		this.freezeAmount = value;
	}

	public BigDecimal getFreezeAmount() {
		return this.freezeAmount;
	}


	public void setState(Integer value) {
		this.state = value;
	}

	public Integer getState() {
		return this.state;
	}

	public Integer getDisclaimer() {
		return disclaimer;
	}

	public void setDisclaimer(Integer disclaimer) {
		this.disclaimer = disclaimer;
	}
}
