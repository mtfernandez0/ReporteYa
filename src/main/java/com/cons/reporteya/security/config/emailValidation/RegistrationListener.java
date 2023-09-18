package com.cons.reporteya.security.config.emailValidation;

import com.cons.reporteya.entity.User;
import com.cons.reporteya.service.UserService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.context.ApplicationListener;
import org.springframework.context.MessageSource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class RegistrationListener implements
        ApplicationListener<OnRegistrationCompleteEvent> {

    private String message = "<!DOCTYPE html>\n" +
            "<html lang=\"en\">\n" +
            "<head>\n" +
            "    <meta charset=\"UTF-8\">\n" +
            "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
            "    <title>Verificación</title>\n" +
            "    <style>\n" +
            "        body{\n" +
            "            width: 75%;\n" +
            "            margin: 40px auto;\n" +
            "        }\n" +
            "        .container{\n" +
            "            display: flex;\n" +
            "            flex-direction: column;\n" +
            "            gap: 20px;\n" +
            "            justify-content: center;\n" +
            "            align-items: center;\n" +
            "        }\n" +
            "        h1{\n" +
            "            font-weight: 500;\n" +
            "        }\n" +
            "        a{\n" +
            "            padding: 10px 20px;\n" +
            "            background-color: rgb(26, 88, 126);\n" +
            "            color: white;\n" +
            "            border-radius: 17px;\n" +
            "            font-size: 26px;\n" +
            "            text-decoration: none;\n" +
            "        }\n" +
            "    </style>\n" +
            "</head>\n" +
            "<body>\n" +
            "    <div class=\"container\">\n" +
            "        <h1>Confirmación de Registro</h1>\n" +
            "        <div class=\"link\">\n" +
            "            <a href=\"http://localhost:8080/registerConfirm?token=%TOKEN%\">Verificar</a>\n" +
            "        </div>\n" +
            "    </div>\n" +
            "</body>\n" +
            "</html>";


    private final UserService service;
    private final MessageSource source;
    private final JavaMailSender mailSender;

    public RegistrationListener(UserService service,
                                MessageSource source,
                                JavaMailSender mailSender) {
        this.service = service;
        this.source = source;
        this.mailSender = mailSender;
    }

    @Override
    public void onApplicationEvent(OnRegistrationCompleteEvent event) {
        try {
            this.confirmRegistration(event);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    private void confirmRegistration(OnRegistrationCompleteEvent event) throws MessagingException {
        User user = event.getUser();
        String token = UUID.randomUUID().toString();
        service.createVerificationToken(user, token);

        String recipientAddress = user.getEmail();
        String subject = "Verificación de email";

        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");
        String htmlMsg = message.replace("%TOKEN%", token);
        helper.setText(htmlMsg, true);
        helper.setTo(recipientAddress);
        helper.setSubject(subject);
        mailSender.send(mimeMessage);
    }
}