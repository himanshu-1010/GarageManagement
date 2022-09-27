package com.garage.service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailAuthenticationException;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSendException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

	/*
	 * @Autowired private UserRepository userRepository;
	 * 
	 * @Autowired private RoleRepository roleRepository;
	 */

	@Autowired
	private JavaMailSender javaMailSender;

	public void sendMail(String to, String subject, String message) throws Exception {

		MimeMessage msg = javaMailSender.createMimeMessage();

		try {

			MimeMessageHelper helper = new MimeMessageHelper(msg, true);

			helper.setTo(to);
			helper.setSubject(subject);
			helper.setText(message, true);

			javaMailSender.send(msg);
		} catch (MessagingException e) {
//			log.error("Multipart creation failed!", e);
			throw e;
		} catch (MailAuthenticationException e) {
//			log.error("Authentication failed!", e);
			throw e;
		} catch (MailSendException e) {
//			log.error("Send mail failed!", e);
			throw e;
		} catch (MailException e) {
//			log.error("Mail exception!", e);
			throw e;
		}
	}
	
	public void sendSupportMail(String to, String subject, String message) throws Exception {

		MimeMessage msg = javaMailSender.createMimeMessage();

		try {

			MimeMessageHelper helper = new MimeMessageHelper(msg, true);

			helper.setTo(to);
			helper.setSubject(subject);
			helper.setText(message, true);

			javaMailSender.send(msg);
		} catch (MessagingException e) {
//			log.error("Multipart creation failed!", e);
			throw e;
		} catch (MailAuthenticationException e) {
//			log.error("Authentication failed!", e);
			throw e;
		} catch (MailSendException e) {
//			log.error("Send mail failed!", e);
			throw e;
		} catch (MailException e) {
//			log.error("Mail exception!", e);
			throw e;
		}
	}

	public void informAdmin(String subject, String message) throws MessagingException {

//		List<User> users = userRepository.findByRoleAndActive(roleRepository.findByName("ADMIN"), (byte) 1);

		/*
		 * for (User user3 : users) sendMail(user3.getEmail(), subject, message);
		 */
	}
}