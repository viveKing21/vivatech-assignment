package com.vivatech.services;

import java.util.Map;

import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.spring.VelocityEngineUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class MailService {
	static final String TEMPLATE_PATH = "/templates/";
	
	@Autowired
	private JavaMailSender javaMailSender;
	
	@Autowired
	private VelocityEngine velocityEngine;
	
	@Async
	public void sendEmail(String to, String subject, String text) throws MessagingException{
		MimeMessage message = javaMailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
		
		helper.setTo(to);
		helper.setSubject(subject);
		helper.setText(text, true);
		
		javaMailSender.send(message);
	}
	public String renderTemplate(String templateName, Map<String, Object> model) throws MessagingException {
        String renderedTemplate = VelocityEngineUtils.mergeTemplateIntoString(velocityEngine, TEMPLATE_PATH + templateName, "UTF-8", model);
        return renderedTemplate;
    }
}
