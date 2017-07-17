package com.example.demo.mail;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service("EmailServiceImpl")
@Component
public class EmailServiceImpl implements EmailService{
	
	@Autowired
    private JavaMailSender mailSender;
	
	@Autowired
	private EmailConfig emailConfig;

	@Override
	public void sendSimpleMail(String requestorEmailID, String receiverEmailID, String port, String status) {
		System.out.println("==" + requestorEmailID + "==" +receiverEmailID + "==" + port + status);
		SimpleMailMessage message = new SimpleMailMessage();
		message.setFrom(emailConfig.getEmailFrom());
		String[] to;
		if(requestorEmailID != null && !"".equals(requestorEmailID)){
			to = new String[]{requestorEmailID, receiverEmailID};
			message.setTo(to);
		}else{
			message.setTo(receiverEmailID);
		}
		
		message.setSubject("An " + status + " request to " + port + " has been sent.");
		message.setText("An " + status + " request to " + port + " has been sent.");
		mailSender.send(message);
	}

	@Override
	public void sendAttachmentsMail() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void sendTemplateMail() {
		// TODO Auto-generated method stub
		
	}



}
