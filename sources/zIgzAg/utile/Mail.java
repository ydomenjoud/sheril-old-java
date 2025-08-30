// v 1.00 01/01/01
// Copyright 2001 Julien Buret All Rights Reserved.
// Use is subject to license terms.

package zIgzAg.utile;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;

import zIgzAg.jeu.oceane.Const;

public class Mail {

	public static final String CHARSET = "ISO8859_1";
	public static final String ENCODING = "Q";

	public static final String ERREUR_CODAGE_TEXTE = "Erreur dans le codage norme RFC822 du texte suivant:";

	public static boolean envoyerMessageFichiersAttaches(
			String nomDestinataire, String adresseDestinataire,
			String adresseEnvoi, String host, String sujet,
			String corpsMessage, String[] fichiers) {
		/*
		 * try{ System.out.println(InetAddress.getLocalHost()); }catch(Exception
		 * e){e.printStackTrace();}
		 */
		Properties props = System.getProperties();
		props.put("mail.smtp.user", Const.MAIL_SMTP_HOST);
        props.put("mail.smtp.host", Const.MAIL_SMTP_HOST);
        props.put("mail.smtp.port", Const.MAIL_SMTP_PORT);
        props.put("mail.smtp.starttls.enable",Const.MAIL_SMTP_TTLS);
        props.put("mail.smtp.auth", Const.MAIL_SMTP_AUTH);
        props.put("mail.smtp.debug", "true");


		Session session = Session.getDefaultInstance(props, null);
		// session.setDebug(true);

		try {
			MimeMessage msg = new MimeMessage(session);
			msg.setFrom(new InternetAddress(adresseEnvoi));
			InternetAddress[] adresse = new InternetAddress[1];
			try {
				adresse[0] = new InternetAddress(adresseDestinataire,
						nomDestinataire/* ,CHARSET */);
			} catch (UnsupportedEncodingException e) {
				System.out.println(ERREUR_CODAGE_TEXTE + nomDestinataire);
				e.printStackTrace();
				return false;
			}

			msg.setRecipients(Message.RecipientType.TO, adresse);
			try {
				msg.setSubject(MimeUtility
						.encodeText(sujet/* ,CHARSET,ENCODING */));
			} catch (UnsupportedEncodingException e) {
				System.out.println(ERREUR_CODAGE_TEXTE + sujet);
				e.printStackTrace();
				return false;
			}

			Multipart mp = new MimeMultipart();

			MimeBodyPart mbp1 = new MimeBodyPart();
			mbp1.setText(corpsMessage/* ,CHARSET */);
			mp.addBodyPart(mbp1);

			for (int i = 0; i < fichiers.length; i++) {
				MimeBodyPart mbp2 = new MimeBodyPart();
				FileDataSource fds = new FileDataSource(fichiers[i]);
				mbp2.setDataHandler(new DataHandler(fds));
				// File inter=new File(fichiers[i]);
				mbp2.setFileName(fds.getName());
				mp.addBodyPart(mbp2);
			}

			msg.setContent(mp);

			msg.setSentDate(new Date());

			Transport.send(msg);

		} catch (MessagingException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public static boolean envoyerMessageFichiersAttaches( String nomDestinataire, String adresseDestinataire, String sujet, String corpsMessage, String[] fichiers) {

		Properties props = System.getProperties();
		props.put("mail.smtp.user", Const.MAIL_SMTP_LOGIN);
		props.put("mail.smtp.password", Const.MAIL_SMTP_PASSWORD);
        props.put("mail.smtp.host", Const.MAIL_SMTP_HOST);
        props.put("mail.smtp.port", Const.MAIL_SMTP_PORT);
        props.put("mail.smtp.starttls.enable",Const.MAIL_SMTP_TTLS);
        props.put("mail.smtp.auth", Const.MAIL_SMTP_AUTH);
        props.put("mail.smtp.debug", "true");
        props.put("mail.smtp.ehlo", "false");

        Authenticator auth = new SMTPAuthenticator();
		Session session = Session.getDefaultInstance(props, auth);
		session.setDebug(true);

		try {
			MimeMessage msg = new MimeMessage(session);
			msg.setFrom(new InternetAddress(Const.MAIL_SMTP_LOGIN));
			InternetAddress[] adresse = new InternetAddress[1];
			try {
				adresse[0] = new InternetAddress(adresseDestinataire,
						nomDestinataire/* ,CHARSET */);
			} catch (UnsupportedEncodingException e) {
				System.out.println(ERREUR_CODAGE_TEXTE + nomDestinataire);
				e.printStackTrace();
				return false;
			}

			msg.setRecipients(Message.RecipientType.TO, adresse);
			try {
				msg.setSubject(MimeUtility
						.encodeText(sujet/* ,CHARSET,ENCODING */));
			} catch (UnsupportedEncodingException e) {
				System.out.println(ERREUR_CODAGE_TEXTE + sujet);
				e.printStackTrace();
				return false;
			}

			Multipart mp = new MimeMultipart();

			MimeBodyPart mbp1 = new MimeBodyPart();
			mbp1.setText(corpsMessage/* ,CHARSET */);
			mp.addBodyPart(mbp1);

			for (int i = 0; i < fichiers.length; i++) {
				MimeBodyPart mbp2 = new MimeBodyPart();
				FileDataSource fds = new FileDataSource(fichiers[i]);
				mbp2.setDataHandler(new DataHandler(fds));
				// File inter=new File(fichiers[i]);
				mbp2.setFileName(fds.getName());
				mp.addBodyPart(mbp2);
			}

			msg.setContent(mp);

			msg.setSentDate(new Date());



			//Transport.send(msg);

			 Transport tr = session.getTransport("smtp");
			    tr.connect(Const.MAIL_SMTP_HOST, Const.MAIL_SMTP_LOGIN, Const.MAIL_SMTP_PASSWORD);
			    msg.saveChanges();

			    // tr.send(message);

			    tr.sendMessage(msg,msg.getAllRecipients());
			    tr.close();



		} catch (MessagingException e)  {
			e.printStackTrace();
			System.out.println("debug info");
			System.out.println("mail.smtp.user: " + props.get("mail.smtp.user") );
			System.out.println("mail.smtp.host: " + props.get("mail.smtp.host") );
			System.out.println("mail.smtp.port: " + props.get("mail.smtp.port") );
			System.out.println("mail.smtp.starttls.enable: " +  props.get("mail.smtp.starttls.enable") );
			System.out.println("mail.smtp.auth: " +  props.get("mail.smtp.auth") );
			System.out.println("mail.smtp.debug: " +  props.get("mail.smtp.debug") );
			return false;
		}
		return true;
	}

}

class SMTPAuthenticator extends Authenticator {
	protected PasswordAuthentication getPasswordAuthentication() {
		return new PasswordAuthentication(Const.MAIL_SMTP_LOGIN, Const.MAIL_SMTP_PASSWORD);
		}
	};