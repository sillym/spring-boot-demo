package com.example.demo.dao;

import java.util.List;

import org.springframework.stereotype.Component;

import com.example.bean.Port;
import com.example.bean.Requests;
import com.example.bean.Users;

@Component
public interface DemoDao {
	
	public Users AuthenticateUser(String username, String password);
	public List<Requests> getMyRequests(int userId);
	public List<Requests> getRequestsToMe(int userId);
	public void updateRequestStatus(int requestID, String status);
	public void deleteRequest(int requestID);
	public Users getOwnerByPort(String port);
	public void newRequest(int userid, int ownerid, String port);
	public Users getOwnerEmail(int reqID);
	public Port getPorts(int reqID);
}
