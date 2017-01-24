package cn.aiyuedu.bs.back.service;

import cn.aiyuedu.bs.common.util.MailUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.util.Properties;

/**
 * Description:
 *
 * @author yz.wu
 */
@Service("mailService")
public class MailService {

    @Autowired
    private JavaMailSenderImpl mailSender;
    @Autowired
    private Properties bsBgConfig;

    @Async
    public void sendSimpleMail(String from, String to, String subject,
                               String text) {
        MailUtil.sendSimpleMail(mailSender, from, to, subject, text);
    }

    @Async
    public void sendHtmlMail(String from, String to, String subject, String text) {
        MailUtil.sendHtmlMail(mailSender, from, to, subject, text);
    }

    @Async
    public boolean sendHtmlMail(JavaMailSenderImpl sender, String from,
                                String to, String subject, String text) {
        MimeMessage mailMessage = mailSender.createMimeMessage();
        MimeMessageHelper messageHelper = null;
        try {
            messageHelper = new MimeMessageHelper(mailMessage, false, "utf-8");
            messageHelper.setTo(to);
            messageHelper.setFrom(from);
            messageHelper.setSubject(subject);

            messageHelper.setText(getHtmlMail(text), true);
        } catch (MessagingException e) {
            e.printStackTrace();
        }

        try {
            sender.send(mailMessage);
            return true;
        } catch (MailException e) {
            e.printStackTrace();
        }

        return false;
    }

    @Async
    public void sendAttachFileMail(String from, String to, String subject,
                                   String text, File file) {
        MailUtil.sendAttachFileMail(mailSender, from, to, subject, text, file);
    }

    public boolean resetPassword(String password, String email) {
        StringBuilder sb = new StringBuilder();
        sb.append("您RS系统的临时密码为：").append(password).append(", 请登录后立即修改新密码。");
        try {
            sendSimpleMail(bsBgConfig.getProperty("mail.from"), email,
                    "RS临时密码", sb.toString());
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public static String getHtmlMail(String text) {
        StringBuilder sb = new StringBuilder();
        sb.append(
                "<html><head><meta http-equiv='content-type' content='text/html; charset=utf-8'></head><body>")
                .append(text).append("</body></html>");

        return sb.toString();
    }
}
