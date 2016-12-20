package com.quyiyuan.exportDataToExcel;

/**
 * <pre>
 * 描述：自定义异常
 * 作者：fengqianning 
 * 时间：2016年12月12日下午9:04:53
 * 类名: CustomException
 * </pre>
 */
public class CustomException extends Exception {

	private String errorCode;

	private String message;

	private static final long serialVersionUID = 1L;

	public CustomException(String message) {
		this.message = message;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
