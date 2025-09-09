package in.main.hospitalManagement.service;

public interface EmailService {
    void sendEmail(String to, String subject, String body);
    void sendHtml(String to, String subject, String html);
    void sendWithAttachment(String to, String subject, String text, java.io.File file);
}
