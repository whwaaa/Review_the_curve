package com.xiaojumao.utils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.show.api.ShowApiRequest;
import com.xiaojumao.bean.EmailSendConetent;
import com.xiaojumao.bean.ShowAPI;

import javax.mail.Address;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class EmailSend {

	// QQ邮箱,用户名无法设置,账号bug
//	final private static String username = "1661907160@qq.com"; // 登录SMTP服务器的用户名
//	final private static String password = "hwpematkssbebeba"; // 登录SMTP服务器的密码

	//
	private static ExecutorService executorService = Executors.newFixedThreadPool(5);

	private final static String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";
	final private static String smtpServer = "smtp.163.com"; // SMTP服务器地址
	final private static String port = "465"; // 端口

	final private static String password = "NVXMZDRMSOTIMGLR"; // 登录SMTP服务器的密码
	final private static String sendMail = "hanwei_wu@163.com"; // 登录SMTP服务器的邮箱账号
	final private static String sendName = "艾宾浩斯学习计划"; // 发送邮件昵称

	public static String recipient; // 收件人地址
	public static String subject; // 邮件主题
	public static String content; // 邮件正文

	/**
	 * 正式发邮件
	 */
	public static boolean sendMail() {
		Properties properties = new Properties();
		properties.put("mail.smtp.host", smtpServer);
		properties.put("mail.smtp.auth", "true");
		properties.put("mail.smtp.socketFactory.class", SSL_FACTORY); // 使用JSSE的SSL
		properties.put("mail.smtp.socketFactory.fallback", "false"); // 只处理SSL的连接,对于非SSL的连接不做处理
		properties.put("mail.smtp.port", port);
		properties.put("mail.smtp.socketFactory.port", port);
		properties.put("mail.smtp.ssl.enable", true);
		Session session = Session.getInstance(properties);
		session.setDebug(true);
		MimeMessage message = new MimeMessage(session);
		try {
			// 发件人
			message.setFrom(new InternetAddress(sendMail, sendName, "UTF-8"));
			// 收件人
			Address toAddress = new InternetAddress(recipient);
			message.setRecipient(MimeMessage.RecipientType.TO, toAddress); // 设置收件人,并设置其接收类型为TO
			/**
			 * TO：代表有健的主要接收者。 CC：代表有健的抄送接收者。 BCC：代表邮件的暗送接收者。
			 */
			// 主题
			message.setSubject(subject);
			// 时间
			message.setSentDate(new Date());
			message.setContent(content,"text/html;charset=UTF-8");
			message.saveChanges();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		try {
			Transport transport = session.getTransport("smtp");
			transport.connect(smtpServer, sendMail, password);
			transport.sendMessage(message, message.getAllRecipients());
			transport.close();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	/**
	 * 发送登录注册验证码
	 * @param email 发送到的邮箱
	 * @param code 验证码
	 */
	public static void sendLoginVer(String email, String code){
		executorService.execute(() -> {
			EmailSend.recipient = email;
			EmailSend.subject = "验证码";
			EmailSend.content = "本次登录/注册的验证码为：" + code + ".";
			EmailSend.sendMail();
		});
	}

	public static void createTaskSendEmail(String email, String taskTitle, String token){
		String con = "<!DOCTYPE html>\n" +
				"<html>\n" +
				"\t<head>\n" +
				"\t\t<meta charset=\"utf-8\">\n" +
				"\t\t<meta name=\"viewport\" content=\"width=device-width,initial-scale=1.0,maximum-scale=1.0,minimum-scale=1.0,user-scalable=no\">\n" +
				"\t\t<style>\n" +
				"\t\t\th4,h5,p{\n" +
				"\t\t\t\tpadding-left: 10px;padding-right: 10px;\n" +
				"\t\t\t}\n" +
				"\t\t\t.content{\n" +
				"\t\t\t\tpadding-left: 30px;\n" +
				"\t\t\t\tmargin-left: 10px;\n" +
				"\t\t\t\tmargin-right: 10px;\n" +
				"\t\t\t\tpadding: 16px;\n" +
				"\t\t\t\toverflow: auto;\n" +
				"\t\t\t\tfont-size: 85%;\n" +
				"\t\t\t\tline-height: 1.45;\n" +
				"\t\t\t\tbackground-color: #f7f7f7;\n" +
				"\t\t\t\tborder-radius: 3px;\n" +
				"\t\t\t}\n" +
				"\t\t\t.footer,.footer a{\n" +
				"\t\t\t\ttext-align: center;\n" +
				"\t\t\t\ttext-decoration: none;\n" +
//				"\t\t\t\tcolor: black;\n" +
				"\t\t\t\tfont-size: 0.8rem;\n" +
				"\t\t\t    font-weight: normal;\n" +
				"\t\t\t}\n" +
				"\t\t\t.footer{\n" +
				"\t\t\t\tmargin: 40px auto;\n" +
				"\t\t\t\ttext-align: center;\n" +
				"\t\t\t}\n" +
				"\t\t</style>\n" +
				"\t</head>\n" +
				"\t<body>\n" +
				"\t\t<h5>致敬爱学习的你 ('-'ゞ</h5>\n" +
				"\t\t<p>学习任务创建成功</p>\n" +
				"\t\t<h4>—— "+ taskTitle +"</h4>\n" +
				"\t\t<p>&nbsp;&nbsp;&nbsp;&nbsp;根据艾宾浩斯遗忘曲线规律，将为您制定以下学习计划：（数据来源于百度百科）</p>\n" +
				"\t\t<div class=\"content\">\n" +
				"\t\t\t->&nbsp;开始学习<br><hr>\n" +
				"\t\t\t->&nbsp;12小时（复习）<br><hr>\n" +
				"\t\t\t->&nbsp;第1天（复习）<br><hr>\n" +
				"\t\t\t->&nbsp;第2天（复习）<br><hr>\n" +
				"\t\t\t->&nbsp;第4天（复习）<br><hr>\n" +
				"\t\t\t->&nbsp;第7天（复习）<br><hr>\n" +
				"\t\t\t->&nbsp;第15天（学习完成）<br>\n" +
				"\t\t</div>\n" +
				"\t\t<p>&nbsp;&nbsp;&nbsp;&nbsp;不用刻意设定闹钟，你只需要打开你的邮件通知，\n" +
				"\t\t当天的学习任务会在上午8点和下午2点发送到你的邮箱，要努力完成复习任务加油！</p>\n" +
				"\t\t<div class=\"footer\">\n" +
				"\t\t\t<p>&copy;&nbsp;<a href=\"http://whw.free.idcfengye.com/login.html?token="+ token +"\" target=\"_blank\">艾宾浩斯遗忘曲线学习计划</a></p>\n" +
				"\t\t</div>\n" +
				"\t</body>\n" +
				"</html>\n";

		executorService.execute(() -> {
			EmailSend.recipient = email;
			EmailSend.subject = "学习任务创建提示";
			EmailSend.content = con;
			EmailSend.sendMail();
		});
	}
	/**
	 * 发送提醒邮件
	 * @param email
	 * @param uidCons
	 */
	public static void sendStudyTask(String email, List<EmailSendConetent> uidCons, String descTime, String token){
		String res=new ShowApiRequest("http://route.showapi.com/1211-1","710489","a816e82d9bc74868a367d71f99282f5d")
				.addTextPara("count","1")
				.post();
		Gson gson = new Gson();
		java.lang.reflect.Type type = new TypeToken<ShowAPI>() {}.getType();
		ShowAPI showAPI = gson.fromJson(res, type);
		String motivational = showAPI.getShowapi_res_body().getData().get(0).getChinese();
		String conHead = "<!DOCTYPE html>\n" +
				"<html>\n" +
				"\t<head>\n" +
				"\t\t<meta charset=\"utf-8\">\n" +
				"\t\t<meta name=\"viewport\" content=\"width=device-width,initial-scale=1.0,maximum-scale=1.0,minimum-scale=1.0,user-scalable=no\">\n" +
				"\t\t<style>\n" +
				"\t\t\t.content{\n" +
				"\t\t\t\tpadding: 16px;\n" +
				"\t\t\t\toverflow: auto;\n" +
				"\t\t\t\tfont-size: 85%;\n" +
				"\t\t\t\tline-height: 1.45;\n" +
				"\t\t\t\tbackground-color: #f7f7f7;\n" +
				"\t\t\t\tborder-radius: 3px;\n" +
				"\t\t\t}\n" +
				"\t\t\t.footer,.footer a{\n" +
				"\t\t\t\ttext-align: center;\n" +
				"\t\t\t\ttext-decoration: none;\n" +
//				"\t\t\t\tcolor: black;\n" +
				"\t\t\t\tfont-size: 0.8rem;\n" +
				"\t\t\t    font-weight: normal;\n" +
				"\t\t\t}\n" +
				"\t\t\t.footer{\n" +
				"\t\tmargin: 40px auto;\n" +
				"\t\ttext-align: center;" +
				"\t\t\t}\n" +
				"\t\t</style>" +
				"\t</head>\n" +
				"\t<body>\n" +
				"\t\t<p>今天"+ descTime +"的复习任务</p>\n";
		String con = "";
		String end =
				"<br><br><p style=\"padding-left: 50px;padding-right: 50px;\">&nbsp;&nbsp;&nbsp;&nbsp;"+ motivational +"</p>" +
				"\t\t<div class=\"footer\">\n" +
				"\t\t\t<p>&copy;&nbsp;<a href=\"http://whw.free.idcfengye.com/login.html?token="+ token +"\" target=\"_blank\">艾宾浩斯遗忘曲线学习计划</a></p>\n" +
				"\t\t</div>" +
				"\t</body>\n" +
				"</html>\n";
		for (EmailSendConetent uidCon : uidCons) {
			con +=
				"\t\t<h5>"+ uidCon.getTitle() +"&nbsp;&nbsp;—>&nbsp;&nbsp;"+ uidCon.getSchedule() +"</h5>\n" +
				"\t\t<div class=\"content\">\n"+ uidCon.getContent() +"\t\t</div>\n";
		}
		String comContent = conHead + con + end;

		executorService.execute(() -> {
			EmailSend.recipient = email;
			EmailSend.subject = "复习提醒";
			EmailSend.content = comContent;
			EmailSend.sendMail();
		});
	}
}