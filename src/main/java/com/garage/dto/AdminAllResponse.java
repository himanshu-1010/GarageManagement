package com.garage.dto;

import lombok.Data;

@Data
public class AdminAllResponse {
private String message;
private int code;
private Object data;
private String error;
public AdminAllResponse() {
	super();
	// TODO Auto-generated constructor stub
}
public AdminAllResponse(String message, int code, Object data, String error) {
	super();
	this.message = message;
	this.code = code;
	this.data = data;
	this.error = error;
}
public String getMessage() {
	return message;
}
public void setMessage(String message) {
	this.message = message;
}
public int getCode() {
	return code;
}
public void setCode(int code) {
	this.code = code;
}
public Object getData() {
	return data;
}
public void setData(Object data) {
	this.data = data;
}
public String getError() {
	return error;
}
public void setError(String error) {
	this.error = error;
}

}
