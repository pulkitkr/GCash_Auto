package com.emailReport;

import java.util.ArrayList;
import java.util.Properties;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import com.CleverTap.QOEMatrix;
import com.extent.ExtentReporter;

public class SendPerformanceMail {
	
	private static String UserName = "teamviewerigs123@gmail.com";
	private static String Password = "gqvyrlfrixgizfbi";
//	private static String[] to = {"bosco.dsouza@zee.com","murali.appadi@zee.com","indu.karunakaran@zee.com"};
//	private static String[] cc = {"deepak.prakash@igsindia.net","naresh.h@igsindia.net","hitesh.c@igsindia.net","Shreenidhi.g@igsindia.net", "Basavaraj.PN@igsindia.net","kushal.revankar@igsindia.net","Tanisha.c@igsindia.net"};
	private static String[] bcc = {};
	private static String[] to = {"pulkit.kumar@igsindia.net"};
	private static String[] cc = {};


	@SuppressWarnings("unused")
	public static void EmailReport() {
		
		if(QOEMatrix.VerifyIteration()) {
			return;
		}else {
			QOEMatrix.calculateaverage();
		}
		String filepath = "";//ExcelUpdate.xlpath;
		String Subject = "QOE Matrix automation execution | Android TV";
		boolean EnableAttachment = true;;
		String fileName = "";//ExcelUpdate.xlFileName;
		String columnHeader = "Number of Total Test";
		StringBuilder InsertResult = ExtentReporter.updateResult();
		StringBuilder InsertModuleResult = ExtentReporter.updateModuleResult(); //updateModuleResult();
//		StringBuilder InsertModuleResult1 =	ExtentReporter.updatePercentageOffailure(); // updatePercentageOffailure();
		
		String columnHeader2 = "Number of Test";
		String moduleName = "Module Name";
		String moduleResult = "Module Result";
		String Table ;
//		System.out.println("Build Number:"+System.getenv("BUILD_NUMBER")); //BUILD_NUMBER - name of the environment variable
		
		if("ABC".equals("TV")) {
			Subject = "Android TV Analysed Report, APP verison - 20.21106.3";
			columnHeader = "Module Name";
//			InsertResult = ExtentReporter.updateTVResult();
			filepath = System.getProperty("user.dir") + "\\Analysed_Reports\\Analysed_Reports.xlsx";
			columnHeader2 = "Number of validation";
			Table ="Hi Team,<br/>Please find attached test automation execution results."
					+"<br>"
					+"<html>\r\n"
					+"<br>"
					+"<h3><table align=\"center\">"+"</h3>\n\n"
					+ "      <table width=\"600\" border=\"1\" cellspacing=\"0\" cellpadding=\"0\" style=\"border:1px solid #ccc;\">\r\n"
					+ "        <tr>\r\n"
					+ "          <td> "+columnHeader+" </span></td>\r\n"
					+ "          <td><span style=\"font-weight:bold\"> "+columnHeader2+" Passed </span></td>\r\n"
					+ "          <td><span style=\"font-weight:bold\"> "+columnHeader2+" Failed </span></td>\r\n"
					+ "        </tr>\r\n"
					+  			InsertResult
					+ "      </table>\r\n\n\n"
					+"<br>"
					+ "</html>"
					+"<br/> Regards,<br> IGS Automation Team";
		}else {
			
			Table ="Hi Team,<br/>Below table contains Performance test automation execution results."
					+"<br>"
					+"<html>\r\n"
					+"<br>"
					+"<span> Device Information: </span>"
					+"<h3><table align=\"center\">"+"</h3>\n\n"
					+ "      <table width=\"600\" border=\"1\" cellspacing=\"0\" cellpadding=\"0\" style=\"border:1px solid #ccc;\">\r\n"
					+ "        <tr>\r\n"
					+ "          <td> Device Name </span></td>\r\n"
					+ "          <td><span style=\"font-weight:bold\"> Version </span></td>\r\n"
					+ "          <td><span style=\"font-weight:bold\"> Build Version </span></td>\r\n"
					+ "        </tr>\r\n"
					+			ExtentReporter.DeviceDetails()
					+ "      </table>\r\n\n\n"
					+"<br>"
					+"<span style=\"font-weight: normal;\">Start Time : "+ExtentReporter.CTCurrentTime+"<br>End Time : "+QOEMatrix.getDate()+"</span>"
					+"<br>"
					+"<span> Execution Summary: </span>"
					+"<br>"
					+"<h3><table align=\"left\">"+"</h3>\n\n"
					+ "      <table width=\"850\" border=\"1\" cellspacing=\"0\" cellpadding=\"0\" style=\"border:1px solid #ccc;\">\r\n"
					+ "        <tr align=\"left\">\r\n"		
					+ "          <td><span style=\"font-weight:bold\"> Scenario	 </span></td>\r\n"
					+ "          <td><span style=\"font-weight:bold\"> Time Taken (Sec) </span></td>\r\n"
					+ "          <td><span style=\"font-weight:bold\"> App Native Heap Memory </span></td>\r\n"
					+ "          <td><span style=\"font-weight:bold\"> App Total Memory	</span></td>\r\n"
					+ "          <td><span style=\"font-weight:bold\"> CPU Usage  </span></td>\r\n"
					+ "          <td><span style=\"font-weight:bold\"> GPU Memory </span></td>\r\n"
					+ "          <td><span style=\"font-weight:bold\"> GPU FPS </span></td>\r\n"
					+ "          <td><span style=\"font-weight:bold\"> App Traffic usage </span></td>\r\n"
					+ "        </tr>\r\n"
					+				ExtentReporter.performanceDetails()
					+ "      </table>\r\n\n\n"
					+"<br>"
					+ "</html>"
					+"<br/> Regards,<br> IGS Automation Team";
		}
		
//		+"<h3><table align=\"center\">"+"</h3>\n\n"
//		+ "      <table width=\"600\" border=\"1\" cellspacing=\"0\" cellpadding=\"0\" style=\"border:1px solid #ccc;\">\r\n"
//		+ "        <tr>\r\n"
//		+ "          <td> "+columnHeader+" </span></td>\r\n"
//		+ "          <td><span style=\"font-weight:bold\"> "+columnHeader2+" Passed </span></td>\r\n"
//		+ "          <td><span style=\"font-weight:bold\"> "+columnHeader2+" Failed </span></td>\r\n"
//		+ "        </tr>\r\n"
//		+  			InsertResult
//		+ "      </table>\r\n\n\n"
//		+"<br>"
		
		
		sendMail(UserName, Password, to, cc, bcc, Subject, Table, filepath,fileName,EnableAttachment);
	}

	public static boolean sendMail(final String userName, final String passWord, String[] to,String[] cc, String[] bcc, String subject,String table, String attachmentPath,String fileName, boolean EnableAttachment) {
		
		Properties props = new Properties();
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.port", "587");

		try {
			Session session = Session.getInstance(props, new javax.mail.Authenticator() {
				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication(userName, passWord);
				}
			});
			MimeMessage msg = new MimeMessage(session);
			Multipart multipart = new MimeMultipart();
			msg.setSubject(subject);
			MimeBodyPart messageBodyPart= new MimeBodyPart();
//			if (EnableAttachment) {
//				File file = new File(attachmentPath);
//				if (((file.length() / (1024 * 1024)) < 25)) {
//					MimeBodyPart attachBody = new MimeBodyPart();
//					DataSource source = new FileDataSource(attachmentPath);
//					attachBody.setDataHandler(new DataHandler(source));
//					attachBody.setFileName(fileName);
//					multipart.addBodyPart(attachBody);
//				}
//			}
			messageBodyPart.setText(table, "utf-8", "html");
			multipart.addBodyPart(messageBodyPart);
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
			transport.connect("smtp.gmail.com", userName, passWord);
			transport.sendMessage(msg, msg.getAllRecipients());
			transport.close();
			return true;
		} catch (Exception mex) {
			mex.printStackTrace();
			return false;
		}
	}

	public static void main(String[] args) {
		EmailReport();
	}
	
	public static StringBuilder updateModuleResult() {
		ArrayList<String> moduleFail = new ArrayList<String>();
		moduleFail.add("Module1,Pass");
		moduleFail.add("Module2,Fail");
		moduleFail.add("Module3,Pass");
		StringBuilder builder = new StringBuilder();
		if (moduleFail.size() > 0) {
			for (int i = 0; i < moduleFail.size(); i++) {
				String result[] = moduleFail.get(i).toString().split(",");
				if(moduleFail.get(i).toString().contains("Pass")) {
					builder.append("<tr>\r\n" + "<td> " + result[0] + " </td>\r\n" + "<td> <span style=\"font-weight:bold;color:green\">"+ result[1] + " </td>\r\n"+ "</tr>\r\n");
				}else {
					builder.append("<tr>\r\n" + "<td> " + result[0] + " </td>\r\n" + "<td> <span style=\"font-weight:bold;color:red\">"+ result[1] + " </td>\r\n"+ "</tr>\r\n");
				}
			}
			return builder;
		}else {
			return null;
		}
	}
	
	public static StringBuilder updatePercentageOffailure() {
		StringBuilder builder = new StringBuilder();
		int total, score; 
	     float percentage;
	     total = 19;
	     score = 6;
	     percentage = (score * 100/ total);
	     builder.append("<tr>\r\n" + "<td>19</td>\r\n" + "<td>13</td>\r\n"+ "<td>6</td>\r\n"+"<td>"+percentage+"</td>\r\n"+"</tr>\r\n");
	     return builder;
	}
	
}
