package com.garage.dto;

import lombok.Data;

@Data
public class AdminResponse {
private String message;
private String error;
private Object data;
private int code;
private String token;
public AdminResponse() {
	super();
	// TODO Auto-generated constructor stub
}
public AdminResponse(String message, String error, Object data, int code, String token) {
	super();
	this.message = message;
	this.error = error;
	this.data = data;
	this.code = code;
	this.token = token;
}
public String getMessage() {
	return message;
}
public void setMessage(String message) {
	this.message = message;
}
public String getError() {
	return error;
}
public void setError(String error) {
	this.error = error;
}
public Object getData() {
	return data;
}
public void setData(Object data) {
	this.data = data;
}
public int getCode() {
	return code;
}
public void setCode(int code) {
	this.code = code;
}
public String getToken() {
	return token;
}
public void setToken(String token) {
	this.token = token;
}

}
