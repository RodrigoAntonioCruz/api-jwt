package br.com.rodrigodacruz.services.email;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.mail.javamail.JavaMailSender;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Service
public class EmailSenderService {

    @Autowired
    private JavaMailSender emailSender;

    @Autowired
    private SpringTemplateEngine templateEngine;

    public void sendEmail(EmailConfigTamplate emailConfigTamplate) throws MessagingException, IOException {
        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message,
        MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
        StandardCharsets.UTF_8.name());
        String html = getHtmlContent(emailConfigTamplate);
        
        helper.setTo(emailConfigTamplate.getTo());
        helper.setFrom(emailConfigTamplate.getFrom());
        helper.setSubject(emailConfigTamplate.getSubject());
        helper.setText(html, true);
        emailSender.send(message);
    }

    private String getHtmlContent(EmailConfigTamplate emailConfigTamplate) {
        Context context = new Context();
        context.setVariables(emailConfigTamplate.getHtmlTemplate().getProps());
        return templateEngine.process(emailConfigTamplate.getHtmlTemplate().getTemplate(), context);
    }
}
