package com.hnair.consumer.wallet.enums;

public enum WalletErrorCodeEnum {

	ERROR_CODE_1000(1000, "参数错误"),
	ERROR_CODE_1001(1001, "钱包账户异常"),
	ERROR_CODE_1002(1002, "secret错误"),
	ERROR_CODE_1003(1003,"token失效"),
	ERROR_CODE_1004(1004,"该钱包账户存在未审核提现申请"),
	ERROR_CODE_1005(1005,"钱包账户余额不足"),
	ERROR_CODE_1006(1006,"查询数据库异常"),
	ERROR_CODE_1007(1007,"钱包绑定账户不存在"),
	ERROR_CODE_1008(1008,"支付密码错误"),
	ERROR_CODE_1009(1009,"提现申请不存在"),
	ERROR_CODE_1010(1010,"新密码不能和旧密码相同"),
	ERROR_CODE_1011(1011,"支付号已存在"),
	ERROR_CODE_1012(1012,"交易流水号错误"),
	ERROR_CODE_1013(1013,"付款账号不匹配"),
	ERROR_CODE_1014(1014,"支付状态异常"),
	ERROR_CODE_1015(1015,"钱包余额不足"),
	ERROR_CODE_1016(1016,"审核失败"),
	ERROR_CODE_1017(1017,"收款账户不存在"),
	ERROR_CODE_1018(1018,"签名错误"),
	ERROR_CODE_1019(1019,"钱包鉴权失败"),
	ERROR_CODE_1020(1020,"登录状态异常"),
	ERROR_CODE_1021(1021,"更新钱包失败"),
	ERROR_CODE_1022(1022, "钱包账户被冻结"),
	
	ERROR_CODE_9999(9999, "未知错误");

	private Integer errorCode;
	private String errorMessage;

	private WalletErrorCodeEnum(Integer errorCode, String errorMessage) {
		this.errorCode = errorCode;
		this.errorMessage = errorMessage;
	}

	public static WalletErrorCodeEnum getByErrorCode(int errorCode) {
		for (WalletErrorCodeEnum errorCodeEnum : WalletErrorCodeEnum.values()) {
			if (errorCodeEnum.errorCode.intValue() == errorCode) {
				return errorCodeEnum;
			}
		}
		return null;
	}

	public Integer getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(Integer errorCode) {
		this.errorCode = errorCode;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
}
