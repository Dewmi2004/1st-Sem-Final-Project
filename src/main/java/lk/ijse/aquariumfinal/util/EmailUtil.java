package lk.ijse.aquariumfinal.util;

import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

import java.util.Properties;

public class EmailUtil {

    private static final String FROM_EMAIL = "imashadewmi557@gmail.com";
    private static final String PASSWORD = "ywfbgpfoijtbakur";

    private static Session createSession() {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        return Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(FROM_EMAIL, PASSWORD);
            }
        });
    }

    private static void send(String toEmail, String subject, String body) {
        new Thread(() -> {
            try {
                Message message = new MimeMessage(createSession());
                message.setFrom(new InternetAddress(FROM_EMAIL, "imashadewmi557@gmail.com"));
                message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
                message.setSubject(subject);
                message.setText(body);

                Transport.send(message);
                System.out.println("Email sent to: " + toEmail);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    public static void sendSupplierRestockEmail(String toEmail, String itemName, int quantity) {
        String subject = "Restock Alert for " + itemName;
        String body = "Dear Supplier,\n\n" +
                "This is a notification from Aquarium Shop.\n" +
                "The stock quantity for item: " + itemName + " has reached " + quantity + ".\n" +
                "Please restock the item as soon as possible.\n\n" +
                "Thank you,\nAquarium Management System";
        send(toEmail, subject, body);
    }

    public static void sendCustomerWelcomeEmail(String toEmail, String name) {
        String subject = "Welcome to Aquarium Shop!";
        String body = "Dear " + name + ",\n\n" +
                "Thank you for registering with us!\n\n" +
                "We’re excited to have you on board.\n\n" +
                "Best regards,\nAquarium Shop Team";
        send(toEmail, subject, body);
    }

    public static void sendEmployeeWelcomeEmail(String toEmail, String name) {
        String subject = "Welcome to the Aquarium Shop Team!";
        String body = "Hi " + name + ",\n\n" +
                "We’re excited to welcome you as our new Employee" + "!\n" +
                "Looking forward to working with you.\n\n" +
                "Best,\nAquarium Shop Management";
        send(toEmail, subject, body);
    }
public static void sendOrderAllert( double price, String recipientEmail, String customerId){


            String subject = "Your Order Alert - Aquarium Shop";
            String body = "Dear Customer,\n\n" +
                    "This is to inform you that your order has been placed successfully. We will contact you shortly to confirm the details.\n\n" +
                    "Price: $" + price + "\n" +
                    "Customer ID: " + customerId + "\n" +
                    "Customer Email: " + recipientEmail + "\n\n" +
                    "Thank you for choosing Aquarium Shop.\n" +
                    "Best regards,\nAquarium Shop Team";

                createSession();
           send(recipientEmail, subject, body);


    }
}
