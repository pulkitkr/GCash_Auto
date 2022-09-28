package com.emailReport;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.internet.MimeMessage;
import javax.mail.search.FlagTerm;

import org.jsoup.Jsoup;

public class GmailInbox {
		
	public static String readEmail(String subject){
		GmailInbox gmail = new GmailInbox();
		return gmail.fetchPassword(gmail.read(subject));
	}
	
	@SuppressWarnings("unused")
	public String read(String subject) {

		String plainText = null;
		Properties props = new Properties();
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.socketFactory.port", "465");
		props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
		props.put("mail.smtp.auth", true);
		props.put("mail.smtp.port", "465");
		try {
			Session session = Session.getInstance(props,
					new javax.mail.Authenticator() {
						protected PasswordAuthentication getPasswordAuthentication() {
							return new PasswordAuthentication(
									"Zee5latest@gmail.com", "User@123");
						}
					});
			Store store = session.getStore("imaps");
			store.connect("smtp.gmail.com", "Zee5latest@gmail.com", "User@123");
			System.out.println("connection is established with the Mail ID");
			Folder inbox = store.getFolder("inbox");
			inbox.open(Folder.READ_WRITE);
			int messageCount = inbox.getMessageCount();
			FlagTerm ft = new FlagTerm(new Flags(Flags.Flag.SEEN), false);
			Message[] message = inbox.search(ft);
			boolean emailFound = false;
			for (int i = 0; i < message.length; i++) {
				//				System.out.println("Mail Subject:- " + message[i].getSubject())
				if(emailFound == false){
					if(message[i].getSubject().contains(subject)){
						MimeMessage msg = (MimeMessage) message[i];
						String s = (String)msg.getContent();
						plainText = Jsoup.parse(s).text();
						//						KeywordUtil.markPassed("The content of the Email is : " +plainText)
						//						KeywordUtil.markPassed("The message is received from : " +msg.getFrom())
						FlagTerm ft1 = new FlagTerm(new Flags(Flags.Flag.SEEN), true);
						Message[] message1 = inbox.search(ft1);
						emailFound = true;
						break;
					}
				}
			}
			if(emailFound == false){
				System.out.println("User is not received the mail or the mail content is read");
			}
			inbox.close(true);
			store.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return plainText;
	}

	public String fetchPassword(String Message){
		Pattern p = Pattern.compile("\\b((?:https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|])");
		Matcher m = p.matcher(Message);
		m.find();
		return m.group(0);
	}

	}



