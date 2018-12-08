package com.demo.util;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class MailUtil {

    private static final String MAIL_TRANSPORT_PROTOCOL = "SMTP";
    private static final String MAIL_SMTP_HOST_163 = "smtp.163.com";

    public static void sendMail(final String userName, final String password, String toAccount, String title, String content) throws MessagingException {
        //创建连接对象 连接到邮件服务器
        Properties properties = new Properties();
        //设置发送邮件的基本参数
        //发送邮件服务器
        properties.setProperty("mail.transport.protocol", MAIL_TRANSPORT_PROTOCOL);
        properties.put("mail.smtp.host", MAIL_SMTP_HOST_163);
        //发送端口
        properties.put("mail.smtp.port", "25");
        properties.put("mail.smtp.auth", "true");
        // 指定验证为true
        properties.setProperty("mail.smtp.auth", "true");
        //设置超时时间
        properties.setProperty("mail.smtp.timeout","3000");
        //设置发送邮件的账号和密码
        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                //两个参数分别是发送邮件的账户和密码
                return new PasswordAuthentication(userName,password);
            }
        });

        //创建邮件对象
        Message message = new MimeMessage(session);
        //设置发件人
        message.setFrom(new InternetAddress(userName));
        //设置收件人
        message.setRecipient(Message.RecipientType.TO,new InternetAddress(toAccount));
        //设置主题
        message.setSubject(title);
        //设置邮件正文  第二个参数是邮件发送的类型
        message.setContent(content,"text/html;charset=UTF-8");
        //发送一封邮件
        Transport.send(message);
    }
}
