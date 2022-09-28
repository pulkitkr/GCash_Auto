package com.emailReport;

import java.util.Properties;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import com.extent.ExtentReporter;

public class MailReport {
	
	public static void EmailReport() {
		
		
		String filepath = ExtentReporter.filePath;

		// mail extent reports
		String[] to = { "igsautomationtester@igs.ind.in" };
		String[] cc = {};
		String[] bcc = {};

		sendMail("teamviewerigs123@gmail.com", "gqvyrlfrixgizfbi", "smtp.gmail.com", "25", "true", "true", true, "javax.net.ssl.SSLSocketFactory",
				"false", to, cc, bcc, "AUTOMATION REPORT", "Please find the reports attached.\n\n Regards\n Automation Team",
				filepath);
	}
	

	public static boolean sendMail(final String userName, final String passWord, String host, String port,
			String starttls, String auth, boolean debug, String socketFactoryClass, String fallback, String[] to,
			String[] cc, String[] bcc, String subject, String text, String attachmentPath) {

		Properties props = new Properties();
		props.put("mail.smtp.starttls.enable", starttls);
		props.put("mail.smtp.auth", auth);
		props.put("mail.smtp.host", host);
		props.put("mail.smtp.port", port);

		try {
			Session session = Session.getInstance(props, new javax.mail.Authenticator() {
				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication(userName, passWord);
				}
			});

			MimeMessage msg = new MimeMessage(session);
			msg.setText(text);
			msg.setSubject(subject);
			// attachment start
			// create the message part

			Multipart multipart = new MimeMultipart();
			MimeBodyPart messageBodyPart = new MimeBodyPart();
			DataSource source = new FileDataSource(attachmentPath);
			messageBodyPart.setDataHandler(new DataHandler(source));
			messageBodyPart.setFileName(ExtentReporter.fileName);
			multipart.addBodyPart(messageBodyPart);

			// attachment ends
			// Put parts in message
			msg.setContent(multipart);
			msg.setFrom(new InternetAddress(userName));

			for (int i = 0; i < to.length; i++) {
				msg.addRecipient(Message.RecipientType.TO, new InternetAddress(to[i]));
			}

			for (int i = 0; i < cc.length; i++) {
				msg.addRecipient(Message.RecipientType.CC, new InternetAddress(cc[i]));
			}

			for (int i = 0; i < bcc.length; i++) {
				msg.addRecipient(Message.RecipientType.BCC, new InternetAddress(bcc[i]));
			}
			msg.saveChanges();
			Transport transport = session.getTransport("smtp");
			transport.connect(host, userName, passWord);
			transport.sendMessage(msg, msg.getAllRecipients());
			transport.close();
			return true;
		} catch (Exception mex) {
			mex.printStackTrace();
			return false;
		}
	}
	
}