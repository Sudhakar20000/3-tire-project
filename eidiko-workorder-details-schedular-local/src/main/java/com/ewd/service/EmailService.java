package com.ewd.service;

import java.util.List;

import com.ewd.dto.EmployeeAndProjectDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import com.ewd.entity.Employee;
import com.ewd.entity.Project;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class EmailService {

	private final JavaMailSender mailSender;

	private final SpringTemplateEngine templateEngine;

    public EmailService(JavaMailSender mailSender, SpringTemplateEngine templateEngine) {
        this.mailSender = mailSender;
        this.templateEngine = templateEngine;
    }

    public void sendPendingEmployeesEmail(List<Project> pendingProjects, List<Employee> pendingEmployees,
										  List<String> toAddresses, List<String> ccAddresses) throws  Exception {
		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message, true);

		helper.setSubject("Mashreq - Pending SOW/POs");

		helper.setFrom(new InternetAddress("harika.karre@eidiko.com", "EidikoNoReply"));

		helper.setTo(toAddresses.toArray(new String[0]));

		if (ccAddresses != null && !ccAddresses.isEmpty()) {
			helper.setCc(ccAddresses.toArray(new String[0]));
		}

		Context context = new Context();
		if (pendingEmployees != null && !pendingEmployees.isEmpty()) {
			context.setVariable("employees", pendingEmployees);
		}

		if (pendingProjects != null && !pendingProjects.isEmpty()) {
			context.setVariable("projects", pendingProjects);
		}

		String htmlContent = templateEngine.process("pending-emp-and-projects", context);

		helper.setText(htmlContent, true);

		log.info(htmlContent);
		log.info("Sending email to: {}",String.join(",",toAddresses));
		mailSender.send(message);
		log.info("Email sent Successfully");
	}

}
