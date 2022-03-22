package com.bcad.application.controller;

import com.bcad.application.service.EmailSenderService;
import org.hibernate.engine.jdbc.StreamUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@RestController
public class EmailController {

    @Autowired
    private JavaMailSender sender;
    private EmailSenderService emailSenderService;

    @Value("${spring.mail.username}")
    private  String username;

    public EmailController(JavaMailSender sender, EmailSenderService emailSenderService) {
        this.sender = sender;
        this.emailSenderService = emailSenderService;
    }

    @GetMapping("/user")
    public String getUsername() {
        return username;
    }
    @RequestMapping(value = "/sendemail")
    private String sendMail() {
        emailSenderService.sendEmail("avinashavi6622@gmail.com",
            "This is the body",
            "This is the subject");
        return "Mail Sent";


    }
    @RequestMapping(value = "/attach")
    public String mailAttachment() throws AddressException, MessagingException, IOException {
        emailSenderService.sendmailattachment();
        return "Attachement Sent";
    }


    @GetMapping("/downloadZip")
    public void downloadFile(HttpServletResponse response) {

        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition", "attachment;filename=download.zip");
        response.setStatus(HttpServletResponse.SC_OK);

        List<String> fileNames = emailSenderService.getFileName();

        System.out.println("############# file size ###########" + fileNames.size());

        try (ZipOutputStream zippedOut = new ZipOutputStream(response.getOutputStream())) {
            for (String file : fileNames) {
                FileSystemResource resource = new FileSystemResource(file);

                ZipEntry e = new ZipEntry(resource.getFilename());

                e.setSize(resource.contentLength());
                e.setTime(System.currentTimeMillis());

                zippedOut.putNextEntry(e);

                StreamUtils.copy(resource.getInputStream(), zippedOut);
                zippedOut.closeEntry();
            }
            zippedOut.finish();
        } catch (Exception e) {
        }
    }


}



