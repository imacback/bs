package cn.aiyuedu.bs.common.util;

import java.io.File;
import java.util.Properties;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;

/**
 * Description:
 *
 * @author yz.wu
 */
public class MailUtil {
    public static void sendSimpleMail(JavaMailSender mailSender, String from, String to, String subject, String text) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(to);
        mailMessage.setFrom(from);
        mailMessage.setSubject(subject);
        mailMessage.setText(text);

        mailSender.send(mailMessage);
    }

    public static void sendSimpleMail(String host, String userName, String password, String from, String to, String subject, String text) {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(host);

        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(to);
        mailMessage.setFrom(from);
        mailMessage.setSubject(subject);
        mailMessage.setText(text);

        mailSender.setUsername(userName);
        mailSender.setPassword(password);

        Properties prop = new Properties();
        prop.put("mail.smtp.auth", "true");
        prop.put("mail.smtp.timeout", "25000");
        mailSender.setJavaMailProperties(prop);

        mailSender.send(mailMessage);
    }

    public static boolean sendHtmlMail(JavaMailSenderImpl mailSender, String from, String to, String subject, String html) {
        MimeMessage mailMessage = mailSender.createMimeMessage();
        MimeMessageHelper messageHelper = null;
        try {
            messageHelper = new MimeMessageHelper(mailMessage, false, "utf-8");
            messageHelper.setTo(to.split(","));
            messageHelper.setFrom(from);
            messageHelper.setSubject(subject);

            messageHelper.setText(getHtmlMail(html), true);
        } catch (MessagingException e) {
            e.printStackTrace();
        }

        try {
            mailSender.send(mailMessage);
            return true;
        } catch (MailException e) {
            e.printStackTrace();
        }

        return false;
    }

    public static void sendCommonHtmlMail(String host, String userName, String password, String from, String to, String subject, String html) {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(host);

        MimeMessage mailMessage = mailSender.createMimeMessage();
        MimeMessageHelper messageHelper = null;
        try {
            messageHelper = new MimeMessageHelper(mailMessage, false, "utf-8");
            messageHelper.setTo(to);
            messageHelper.setFrom(from);
            messageHelper.setSubject(subject);
            messageHelper.setText(getHtmlMail(html), true);
        } catch (MessagingException e) {
            e.printStackTrace();
        }

        mailSender.setUsername(userName);
        mailSender.setPassword(password);

        Properties prop = new Properties();
        prop.put("mail.smtp.auth", "true");
        prop.put("mail.smtp.timeout", "25000");
        mailSender.setJavaMailProperties(prop);

        mailSender.send(mailMessage);
    }

    public static boolean sendAttachFileMail(JavaMailSenderImpl mailSender, String from, String to, String subject, String html, File file) {
        MimeMessage mailMessage = mailSender.createMimeMessage();
        MimeMessageHelper messageHelper = null;
        try {
            messageHelper = new MimeMessageHelper(mailMessage, true, "utf-8");
            messageHelper.setTo(to);
            messageHelper.setFrom(from);
            messageHelper.setSubject(subject);
            messageHelper.setText(getHtmlMail(html), true);
            messageHelper.addAttachment(file.getName(), file);
        } catch (MessagingException e) {
            e.printStackTrace();
        }

        try {
            mailSender.send(mailMessage);
            return true;
        } catch (MailException e) {
            e.printStackTrace();
        }

        return false;
    }

    public static void sendCommonAttachFileMail(String host, String userName, String password, String from, String to, String subject, String html, File file) {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(host);

        MimeMessage mailMessage = mailSender.createMimeMessage();
        MimeMessageHelper messageHelper = null;
        try {
            messageHelper = new MimeMessageHelper(mailMessage, true, "utf-8");
            messageHelper.setTo(to);
            messageHelper.setFrom(from);
            messageHelper.setSubject(subject);
            messageHelper.setText(getHtmlMail(html), true);
            messageHelper.addAttachment(file.getName(), file);
        } catch (MessagingException e) {
            e.printStackTrace();
        }

        mailSender.setUsername(userName);
        mailSender.setPassword(password);

        Properties prop = new Properties();
        prop.put("mail.smtp.auth", "true");
        prop.put("mail.smtp.timeout", "25000");
        mailSender.setJavaMailProperties(prop);

        mailSender.send(mailMessage);
    }

    public static String getHtmlMail(String text) {
        StringBuilder sb = new StringBuilder();
        sb.append("<html><head><meta http-equiv='content-type' content='text/html; charset=utf-8'></head><body>").append(text).append("</body></html>");

        return sb.toString();
    }

}
