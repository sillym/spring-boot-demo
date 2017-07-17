package com.example.bean;

public class Requests {
	
	int ID = 0;
	public int getID() {
		return ID;
	}
	public void setID(int iD) {
		ID = iD;
	}
	String port = null;
	String portOwner = null;

	public String getPortOwner() {
		return portOwner;
	}
	public void setPortOwner(String portOwner) {
		this.portOwner = portOwner;
	}
	String status = null;
	String operator = null;
	public String getOperator() {
		return operator;
	}
	public void setOperator(String operator) {
		this.operator = operator;
	}
	public String getPort() {
		return port;
	}
	public void setPort(String port) {
		this.port = port;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}

}
