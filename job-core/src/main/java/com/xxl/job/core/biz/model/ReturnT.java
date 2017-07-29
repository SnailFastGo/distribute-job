package com.xxl.job.core.biz.model;

import java.io.Serializable;


/**
 * @project 返回结果包装类
 * @file ReturnT.java 创建时间:2017年7月29日下午1:43:29
 * @description 描述（简要描述类的职责、实现方式、使用注意事项等）
 * @author dzn
 * @param <T>
 * @version 1.0
 *
 */
public class ReturnT<T> implements Serializable {
    
	public static final long serialVersionUID = 42L;

	
	/**
	 * @description 响应成功状态码
	 * @value value:SUCCESS_CODE
	 */
	public static final int SUCCESS_CODE = 200;
	
	/**
	 * @description 响应失败状态码
	 * @value value:FAIL_CODE
	 */
	public static final int FAIL_CODE = 500;
	
	/**
	 * @description 响应成功
	 * @value value:SUCCESS
	 */
	public static final ReturnT<String> SUCCESS = new ReturnT<String>(null);
	
	/**
	 * @description 响应失败
	 * @value value:FAIL
	 */
	public static final ReturnT<String> FAIL = new ReturnT<String>(FAIL_CODE, null);
	
	/**
	 * @description 响应状态码
	 * @value value:code
	 */
	private int code;
	
	/**
	 * @description 响应描述
	 * @value value:msg
	 */
	private String msg;
	
	/**
	 * @description 响应详情
	 * @value value:content
	 */
	private T content;

	public ReturnT(){}
	
	public ReturnT(int code, String msg) {
		this.code = code;
		this.msg = msg;
	}
	
	public ReturnT(T content) {
		this.code = SUCCESS_CODE;
		this.content = content;
	}
	
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public T getContent() {
		return content;
	}
	public void setContent(T content) {
		this.content = content;
	}

	@Override
	public String toString() {
		return "ReturnT [code=" + code + ", msg=" + msg + ", content=" + content + "]";
	}

}
