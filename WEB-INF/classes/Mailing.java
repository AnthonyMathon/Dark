import java.io.IOException;
import java.util.Date;
import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.*;

/**
 *	@author Anthony MATHON
 *  Classe permettant d'envoyer les mails de
 *  notification d'activité sur un compte particulier.
 */

public class Mailing {
	
	//Sujets
	static String confirm = "Confirmation de votre mail";
	static String warning = "Action étrange sur votre compte";
	static String blocked = "Votre compte a été bloqué";
	
	private static int port = 465;
	private static String host = "smtp.orange.fr";
	private static String from = "";
	private static boolean auth = true;
	private static String username = "";
	private static String password = "";
	private static Protocol protocol = Protocol.SMTPS;
	private static boolean debug = true;
	
	//protocole d'orange : SSL
	
	public static void sendEmail(String to, String subject, String body) throws IOException{
		try {
			Properties props = new Properties();
			props.put("mail.smtp.host", host);
			props.put("mail.smtp.port", port);
			
			switch (protocol) {
			    case SMTPS:
			        props.put("mail.smtp.ssl.enable", true);
			        break;
			    case TLS:
			        props.put("mail.smtp.starttls.enable", true);
			        break;
			default:
				break;
			}
			
			Authenticator authenticator = null;
			if (auth) {
			    props.put("mail.smtp.auth", true);
			    authenticator = new Authenticator() {
			        private PasswordAuthentication pa = new PasswordAuthentication(username, password);
			        @Override
			        public PasswordAuthentication getPasswordAuthentication() {
			            return pa;
			        }
			    };
			}
			
			Session session = Session.getInstance(props, authenticator);
			session.setDebug(debug);
			
			MimeMessage message = new MimeMessage(session);
		
		    message.setFrom(new InternetAddress(from));
		    InternetAddress[] address = {new InternetAddress(to)};
		    message.setRecipients(Message.RecipientType.TO, address);
		    message.setSubject(subject);
		    message.setSentDate(new Date());
		    message.setText(body);
		    Transport.send(message);
		} catch (Exception e) {
		    e.printStackTrace();
		}
	}
	
	public static void sendConfirmMail(String mail, String name) throws IOException {
		System.out.println("(mail)tentative d'envoi de mail de confirmation à "+name+" via l'adresse "+mail);
		String body = "Bonjour. \n"
				+ "Vous avez récemment demandé a valider votre e-mail afin d'être tenu informé des actions potentiellement illégitimes sur votre compte.\n"
				+ "Veuillez dès maintenant valider votre mail en cliquant sur le lien ci dessous :\n"
				+ "http://serveurdarkpit59.servegame.com:9876/Dark/Confirm?mail="+mail+"\n"
				+ "\n"
				+ "Cordialement, \n"
				+ "Anthony MATHON, Administrateur";
		sendEmail(mail, confirm, body);
	}

	public static void sendWarningMail(String mail, String name) throws IOException {
		System.out.println("(mail)tentative d'envoi de mail d'avertissement à "+name+" via l'adresse "+mail);
		String body = "Bonjour. \n"
				+ "Une adresse IP inhabituelle s'est récemment connectée à votre compte.\n"
				+ "Afin de vérifier que c'était vous, veuillez cliquer sur ce lien :\n"
				+ "http://serveurdarkpit59.servegame.com:9876/Dark/Justify?mail="+mail+"\n"
				+ "\n"
				+ "Cordialement, \n"
				+ "Anthony MATHON, Administrateur";
		sendEmail(mail, warning, body);
	}
	
	public static void sendBlockingMail(String mail, String name) throws IOException {
		System.out.println("(mail)tentative d'envoi de mail de blocage à "+name+" via l'adresse "+mail);
		String body = "Bonjour. \n"
				+ "Une personne potentiellement malintentionnée a récemment tenté de se connecter à votre compte.\n"
				+ "Votre compte a, en effet, tenté de se connecter avec un mot de passe incorrect à plusieurs reprises.\n"
				+ "Afin de déverrouiller votre compte et pouvoir vous connecter à nouveau, veuillez cliquer sur ce lien :\n"
				+ "http://serveurdarkpit59.servegame.com:9876/Dark/Unblock?mail="+mail+"\n"
				+ "Pensez par ailleurs à modifier votre Mot de passe.\n"
				+ "\n"
				+ "Cordialement, \n"
				+ "Anthony MATHON, Administrateur";
		sendEmail(mail, blocked, body);		
	}
	
	//mails
	
			/*"Bonjour. \n"
			+ "Vous avez récemment demandé a valider votre e-mail afin d'être tenu informé des actions potentiellement illégitimes sur votre compte.\n"
			+ "Veuillez dès maintenant valider votre mail en cliquant sur le lien ci dessous :\n"
			+ "http://serveurdarkpit59.servegame.com:9876/Dark/Confirm?mail="+mail+"\n"
			+ "\n"
			+ "Cordialement, \n"
			+ "Anthony MATHON, Administrateur*/
	
	
			/*"Bonjour. \n"
			+ "Une adresse IP inhabituelle s'est récemment connectée à votre compte.\n"
			+ "Afin de vérifier que c'était vous, veuillez cliquer sur ce lien :\n"
			+ "http://serveurdarkpit59.servegame.com:9876/Dark/Justify?mail="+mail+"\n"
			+ "\n"
			+ "Cordialement, \n"
			+ "Anthony MATHON, Administrateur"*/
	
	
			/*"Bonjour. \n"
			+ "Une personne potentiellement malintentionnée a récemment tenté de se connecter à votre compte.\n"
			+ "Votre compte a, en effet, tenté de se connecter avec un mot de passe incorrect à plusieurs reprises.\n"
			+ "Afin de déverrouiller votre compte et pouvoir vous connecter à nouveau, veuillez cliquer sur ce lien :\n"
			+ "http://serveurdarkpit59.servegame.com:9876/Dark/Unblock?mail="+mail+"\n"
			+ "Pensez par ailleurs à modifier votre Mot de passe.\n"
			+ "\n"
			+ "Cordialement, \n"
			+ "Anthony MATHON, Administrateur"*/
			
}
