package com.hoaxify.ws.email;

import com.hoaxify.ws.configuration.HoaxifyProperties;
import com.hoaxify.ws.shared.Messages;
import jakarta.annotation.PostConstruct;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.util.Properties;

@Service
public class EmailService {
    private JavaMailSenderImpl mailSender = null;
    private HoaxifyProperties hoaxifyProperties = null;

    @Autowired
    public EmailService(HoaxifyProperties hoaxifyProperties) {
        this.hoaxifyProperties = hoaxifyProperties;
    }

    @PostConstruct
    public void initialize() {
        mailSender = new JavaMailSenderImpl();
        mailSender.setHost(hoaxifyProperties.getEmail().host());
        mailSender.setPort(hoaxifyProperties.getEmail().port());
        mailSender.setUsername(hoaxifyProperties.getEmail().username());
        mailSender.setPassword(hoaxifyProperties.getEmail().password());

        Properties properties = mailSender.getJavaMailProperties();
        properties.put("mail.smtp.starttls.enable", true);
    }

    String activationEmail = """
            <html>
                <body>
                    <h1>${title}</h1>
                    <a href="${url}">${clickHere}</a>
                </body>
            </html>
            """;

    public void sendActivationEmail(String email, String activationToken) {
        String activationUrl = hoaxifyProperties.getClient().host() + "/activation/" + activationToken;
        String title =  Messages.getMessageForLocale("hoaxify.mail.user.created.title");
        String clickHere = Messages.getMessageForLocale("hoaxify.mail.click.here");

        String mailBody = activationEmail.replace("${url}", activationUrl)
                .replace("${title}", title)
                .replace("${clickHere}", clickHere);

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(message, "UTF-8");

        try {
            mimeMessageHelper.setFrom(hoaxifyProperties.getEmail().from());
            mimeMessageHelper.setTo(email);
            mimeMessageHelper.setSubject(title);
            mimeMessageHelper.setText(mailBody, true);
        } catch (MessagingException e) {
            e.printStackTrace();
        }

        mailSender.send(message);
    }
}
