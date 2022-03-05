package com.viram.dev.service;

import java.io.IOException;
import java.util.Date;
import java.util.Map;
import java.util.Properties;

import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import com.viram.dev.dto.EmailParam;
import com.viram.dev.dto.MailResponse;
import com.viram.dev.dto.OTPSystem;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

@Component
public class EmailService {

	@Autowired
	private Configuration config;
	
	private final static String FTL= ".ftl";
	
	public void sendmailOTP(EmailParam emailParam)
			throws AddressException, MessagingException, IOException {
		Properties props = new Properties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.port", "587");

		Session session = Session.getInstance(props, new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication("viram.dhangar@gmail.com", "Ramkishan!1");
			}
		});
		Message msg = new MimeMessage(session);
		msg.setFrom(new InternetAddress("viram.dhangar@gmail.com", false));
		msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(emailParam.getEmail()));
		msg.setSubject(emailParam.getSubject());
		msg.setContent(emailParam.getEmailBody(), "text/html");
		msg.setSentDate(new Date());
		Transport.send(msg);
	}
	
	public void sendmail(EmailParam emailParam, Map<String, Object> model)
			throws AddressException, MessagingException, IOException {
		Properties props = new Properties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", "smtpout.secureserver.net");
		props.put("mail.smtp.port", "465");

		Session session = Session.getInstance(props, new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication("support@v24mart.com", "Ramkishan!1");
			}
		});
		Message msg = new MimeMessage(session);
		msg.setFrom(new InternetAddress("support@v24mart.com", false));
		msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(emailParam.getEmail()));
		msg.setSubject(emailParam.getSubject());
		msg.setContent(emailParam.getEmailBody(), "text/html");
		msg.setSentDate(new Date());
		
		Template t = config.getTemplate("order"+FTL);
		String html = null;
		try {
			html = FreeMarkerTemplateUtils.processTemplateIntoString(t, model);
		} catch (TemplateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
		Multipart multipart = new MimeMultipart();
		MimeBodyPart htmlPart = new MimeBodyPart();
		htmlPart.setContent( html, "text/html; charset=utf-8" );
		multipart.addBodyPart( htmlPart );
		
		msg.setContent(multipart, "text/html");
		
		Transport.send(msg);
	}
	
	public void sendmail_old(EmailParam emailParam, Map<String, Object> model)
			throws AddressException, MessagingException, IOException {
		Properties props = new Properties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.port", "587");

		Session session = Session.getInstance(props, new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication("viram.dhangar@gmail.com", "Ramkishan!1");
			}
		});
		Message msg = new MimeMessage(session);
		msg.setFrom(new InternetAddress("viram.dhangar@gmail.com", false));
		msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(emailParam.getEmail()));
		msg.setSubject(emailParam.getSubject());
		msg.setContent(emailParam.getEmailBody(), "text/html");
		msg.setSentDate(new Date());
		
		Template t = config.getTemplate("order"+FTL);
		String html = null;
		try {
			html = FreeMarkerTemplateUtils.processTemplateIntoString(t, model);
		} catch (TemplateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
		Multipart multipart = new MimeMultipart();
		MimeBodyPart htmlPart = new MimeBodyPart();
		htmlPart.setContent( html, "text/html; charset=utf-8" );
		multipart.addBodyPart( htmlPart );
		
		msg.setContent(multipart, "text/html");
		
		Transport.send(msg);
	}
	
	public MailResponse sendOTP(OTPSystem request, Map<String, Object> models){
		
		String host = "smtp.hostinger.in";
		MailResponse response = new MailResponse();
		try {

			boolean sessionDebug = true;
			Properties props = System.getProperties();
			props.put("mail.smtp.starttls.enable", "true");

			props.put("mail.smtp.host", host);
			props.put("mail.smtp.ssl.trust", host);
			props.put("mail.transport.protocol.", "smtp");
			props.put("mail.smtp.user", "helpdesk@crickey11.com"); // User name
			props.put("mail.smtp.password", "Ramkishan!1"); // password

			props.put("mail.smtp.auth", "true");
			// props.put("mail.smtp.", "true");
			props.put("mail.smtp.port", "465");
			
		//	Template t = config.getTemplate(templateName+FTL);
		//	String html = FreeMarkerTemplateUtils.processTemplateIntoString(t, model);

			Session mailSession = Session.getDefaultInstance(props, new javax.mail.Authenticator() {
				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication("helpdesk@crickey11.com", "Ramkishan!1");
				}
			});
			mailSession.setDebug(sessionDebug);
			Message msg = new MimeMessage(mailSession);
			msg.setFrom(new InternetAddress("helpdesk@crickey11.com"));
			InternetAddress[] address = { new InternetAddress(request.getEmail()) };
			msg.setRecipients(Message.RecipientType.TO, address);
			msg.setSubject(request.getSubject());
			Multipart multipart = new MimeMultipart();
			
			BodyPart messageBodyPart = new MimeBodyPart();
			messageBodyPart.setText(request.getOtp());
		//	MimeBodyPart htmlPart = new MimeBodyPart();
		//    htmlPart.setContent( html, "text/html; charset=utf-8" );
		    
			// multipart.addBodyPart( htmlPart ); // add it when need template
			multipart.addBodyPart(messageBodyPart);
			messageBodyPart = new MimeBodyPart();
			msg.setContent(multipart, "text/html"); // use setText if you want
													// to send text
			Transport.send(msg);
			System.out.println("sent");
			// WasEmailSent = true; // assume it was sent
			response.setMessage("Email send to : " + request.getEmail());
			//response.setStatus(Boolean.TRUE);
		} catch (Exception err) {
			// WasEmailSent = false; // assume it's a fail
			System.out.println("Error" + err.getMessage());
			response.setMessage("Mail Sending failure : "+err.getMessage());
			//response.setStatus(Boolean.FALSE);
		}
		return response;
	}
	
}
