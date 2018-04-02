package com.hnair.consumer.wallet.vo;

import java.io.Serializable;
import java.math.BigDecimal;


/**
 * 交易创建实体
 * @author 陶嘉骏
 * @date 2018年3月7日
 */
public class PaymentVo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 付款方钱包id
	 */
	private Long buyerId;

	/**
	 * 订单编号
	 */
	private String orderNo;
	
	/**
	 * 支付金额
	 */
	private BigDecimal payAmount;
	
	/**
	 * 订单标题
	 */
	private String subject;
	
	/**
	 * 收款账号
	 */
	private Long partnerId;
	
	/**
	 * 备注
	 */
	private String note;
	
	/**
	 * 服务器异步通知页面路径
	 */
	private String notifyUrl;
	
	/**
	 * 页面跳转同步通知页面路径
	 */
	private String returnUrl;
	
	/**
	 * 签名
	 */
	private String sign;

	public Long getBuyerId() {
		return buyerId;
	}

	public void setBuyerId(Long buyerId) {
		this.buyerId = buyerId;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public BigDecimal getPayAmount() {
		return payAmount;
	}

	public void setPayAmount(BigDecimal payAmount) {
		this.payAmount = payAmount;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public Long getPartnerId() {
		return partnerId;
	}

	public void setPartnerId(Long partnerId) {
		this.partnerId = partnerId;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public String getNotifyUrl() {
		return notifyUrl;
	}

	public void setNotifyUrl(String notifyUrl) {
		this.notifyUrl = notifyUrl;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public String getReturnUrl() {
		return returnUrl;
	}

	public void setReturnUrl(String returnUrl) {
		this.returnUrl = returnUrl;
	}

}
