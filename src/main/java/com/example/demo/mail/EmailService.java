package com.example.demo.mail;

import org.springframework.stereotype.Component;

@Component
public interface EmailService {
	
	/*
	 * Send simple mail
	 */
	public void sendSimpleMail(String requestorEmailID, String receiverEmailID, String port, String status);
	
	/*
	 * Mail with attachments
	 * TODO
	 */
	public void sendAttachmentsMail();
	
	/*
	 * Mail content with Template
	 * TODO
	 */
	public void sendTemplateMail();

}
