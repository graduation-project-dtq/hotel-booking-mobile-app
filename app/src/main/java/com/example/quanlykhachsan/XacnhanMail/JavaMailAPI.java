package com.example.quanlykhachsan.XacnhanMail;

import android.os.AsyncTask;
import android.util.Log;
import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class JavaMailAPI extends AsyncTask<Void, Void, Void> {
    private String email, subject, message;
    private Session session;

    public JavaMailAPI(String email, String subject, String message) {
        this.email = email;
        this.subject = subject;
        this.message = message;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "465");

        session = Session.getInstance(props, new javax.mail.Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                // Thay thế bằng thông tin đăng nhập an toàn
                return new PasswordAuthentication("qd2t.hotel.management@gmail.com", "aisf csmm pazo eksj");
            }
        });

        try {
            // Tạo nội dung email
            MimeMessage mimeMessage = new MimeMessage(session);
            mimeMessage.setFrom(new InternetAddress("qd2t.hotel.management@gmail.com"));

            // Kiểm tra email người nhận trước khi gửi
            if (email == null || email.isEmpty()) {
                throw new MessagingException("Email người nhận không hợp lệ.");
            }
            mimeMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(email));

            mimeMessage.setSubject(subject);
            mimeMessage.setContent(message, "text/html; charset=utf-8"); // Gửi nội dung dưới dạng HTML

            // Gửi email
            Transport.send(mimeMessage);
            Log.i("Email Status", "Email đã được gửi thành công!");

        } catch (MessagingException e) {
            e.printStackTrace();
            Log.e("Email Error", "Không thể gửi email: " + e.getMessage());
        }
        return null;
    }
}
