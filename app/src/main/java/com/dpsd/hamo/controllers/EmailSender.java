package com.dpsd.hamo.controllers;

import java.security.Security;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.util.ByteArrayDataSource;

public class EmailSender extends javax.mail.Authenticator
{
    private String smtpHost = "smtp.gmail.com";
    private String defaultUser;
    private String defaultPassword;
    private Session session;

    static {
        Security.addProvider(new com.dpsd.hamo.controllers.JSSEProvider());
    }

    public EmailSender() {
        defaultUser = "jitoe.hamo@gmail.com";
        defaultPassword = "Cmu@jitoe12";

        Properties props = new Properties();
        props.setProperty("mail.transport.protocol", "smtp");
        props.setProperty("mail.host", smtpHost);
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "465");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class",
                "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.socketFactory.fallback", "false");
        props.setProperty("mail.smtp.quitwait", "false");

        session = Session.getDefaultInstance(props, this);
    }

    protected PasswordAuthentication getPasswordAuthentication() {
        return new PasswordAuthentication(defaultUser, defaultPassword);
    }

    public synchronized void sendSignUpEmail(String subject, String emailBody, String recipient) throws Exception {
        try
        {
            MimeMessage message = new MimeMessage(session);
            DataHandler handler = new DataHandler(new ByteArrayDataSource(emailBody.getBytes(), "text/plain"));
            message.setSender(new InternetAddress(defaultUser));
            message.setSubject(subject);
            message.setDataHandler(handler);
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(recipient));
            Transport.send(message);
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
}
