package com.automation.manager;

import java.io.File;
import java.util.Date;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import com.automation.utils.UtilProperties;

public class mailSender {
	static Properties props = new Properties();
	static Session session = null;
	private static mailSender instance = new mailSender();

	//Remove params.
	public static mailSender getInstance(String EmailAddress, String Password , String ReportPath ) {
		//Move this to constructor
		SetProps();
		openSession(EmailAddress, Password);
		fillEmailbody(ReportPath);
		return instance;
	}

	//All methods should be private, except the send method for now.
	public static void SetProps() {
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.socketFactory.port", "465");
		props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.port", "465");

	}

	public static void openSession(String EmailAddress, String Password) {
		final String ReportEmailAddress = UtilProperties.getInstance().getProperty(EmailAddress);
		final String ReportEmailPassword = UtilProperties.getInstance().getProperty(Password);
		
		session = Session.getInstance(props, new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(ReportEmailAddress, ReportEmailPassword);
			}
		});

	}

	//SetTo;
	//
	//Remove all business  logic.
	public static void fillEmailbody(String ReportPath) {

		try {

			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress("QXL.Automation.report@gmail.com"));
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse("mdarabseh@souq.com, kkhamis@souq.com , rhailat@amazon.com"));
			message.setSubject("Automation Report");
			message.setSentDate(new Date());

			Multipart multipart = new MimeMultipart();
			MimeBodyPart textPart  = new MimeBodyPart();
			String textContent = "Dear QEXPRESS team, \n\n Please find the attached report file,\n\n Best Regards, \n\n ";
			textPart.setText(textContent);
			multipart.addBodyPart(textPart);
			MimeBodyPart attachementPart = new MimeBodyPart();
			attachementPart.attachFile(new File(ReportPath));
			multipart.addBodyPart(attachementPart);
			message.setContent(multipart);	
			Transport.send(message);
			
			System.out.println("Email Sent");

		}  catch (Exception ex) {
    	    ex.printStackTrace();
       }
	}
}