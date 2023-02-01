package com.seidelsoft;

import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.internet.MimeMessage;
import lombok.Getter;
import lombok.Setter;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

@Getter
@Setter
public class EmailConfigs {

    private Properties properties;

    public Message getMimeMessage() {
        EmailConfigs configs = new EmailConfigs();
        Properties props = configs.getProperties();

        Authenticator auth = new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(configs.getUser(), configs.getSenha());
            }
        };

        Session session = Session.getInstance(props, auth);
        Message message = new MimeMessage(session);

        return message;
    }

    public Properties getProperties() {
        try {
            InputStream input = new FileInputStream("src/main/resources/email-config.properties");
            properties = new Properties();
            properties.load(input);

            return properties;

        } catch (IOException ex) {
            ex.printStackTrace();
        }

        return null;
    }

    public String getHost() {
        return getProperties().getProperty("mail.smtp.host");
    }
    public String getUser() {
        return getProperties().getProperty("email.user");
    }
    public String getSenha() {
        return getProperties().getProperty("email.senha");
    }
    public String smtpAuth() {
        return getProperties().getProperty("mail.smtp.auth");
    }
    public String smtpStartTls() {
        return getProperties().getProperty("mail.smtp.starttls.enable");
    }
    public String smtpPort() {
        return getProperties().getProperty("mail.smtp.port");
    }
    public String smtpSocketFactoryPort() {
        return getProperties().getProperty("mail.smtp.socketFactory.port");
    }
    public String smtpSocketFactoryClass() {
        return getProperties().getProperty("mail.smtp.socketFactory.class");
    }
}
