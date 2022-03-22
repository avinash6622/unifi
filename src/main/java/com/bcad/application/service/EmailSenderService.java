package com.bcad.application.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.*;
import java.io.IOException;
import java.util.*;


@Service
public class EmailSenderService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendEmail(String toEmail,
                          String body,
                          String subject) {
        SimpleMailMessage message = new SimpleMailMessage();

        message.setFrom("avinashpandian79@gmail.com");
        message.setTo(toEmail);
        message.setText(body);
        message.setSubject(subject);

        mailSender.send(message);
        System.out.println("Mail Sent Successfully");
    }

    public void sendmailattachment() throws AddressException, MessagingException, IOException {


        try {
            String host = "smtp.gmail.com";
            String from = "avinashpandian79@gmail.com";
            String pass = "njlvjvdanliiahyt";
            Properties props = System.getProperties();
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.host", host);
            props.put("mail.smtp.user", from);
            props.put("mail.smtp.password", pass);
            props.put("mail.smtp.port", "587");
            props.put("mail.smtp.auth", "true");
            String[] to = {"avinashavi6622@gmail.com"};
            Session session = Session.getDefaultInstance(props, null);
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));
            InternetAddress[] toAddress = new InternetAddress[to.length];
            // To get the array of addresses
            for (int i = 0; i < to.length; i++) {
                toAddress[i] = new InternetAddress(to[i]);
            }
            System.out.println(Message.RecipientType.TO);
            for (int i = 0; i < toAddress.length; i++) {

                message.addRecipient(Message.RecipientType.TO, toAddress[i]);
            }
            message.setSubject("Subject of the Mail");
            message.setText("Welcome");

            BodyPart messageBodyPart1 = new MimeBodyPart();
            messageBodyPart1.setText("This is message body");

            String filename[] = {"C:\\Users\\Indium Software\\Pictures\\Screenshots\\ss.png", "C:\\Users\\Indium Software\\Pictures\\Screenshots\\ss11.png"};//change accordingly
            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(messageBodyPart1);
            for (int i = 0; i < filename.length; i++) {
                MimeBodyPart messageBodyPart2 = new MimeBodyPart();
                DataSource source = new FileDataSource(filename[i]);
                messageBodyPart2.setDataHandler(new DataHandler(source));
                messageBodyPart2.setFileName(filename[i]);
                multipart.addBodyPart(messageBodyPart2);
            }
            // both part add into multi part
            // set message content
            message.setContent(multipart);

            Transport transport = session.getTransport("smtp");
            transport.connect(host, from, pass);
            transport.sendMessage(message, message.getAllRecipients());
            transport.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public List<String> getFileName() {

        String[] fileName = {"C:\\Users\\Indium Software\\Pictures\\Screenshots\\ss.png", "C:\\Users\\Indium Software\\Pictures\\Screenshots\\ss11.png"};

        List<String> fileList = new ArrayList<>(Arrays.asList(fileName));
        return fileList;
    }

    }
