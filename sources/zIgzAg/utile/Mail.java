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
			String corpsTexte, String corpsHtml, String[] fichiers) {

		System.setProperty("https.protocols", "TLSv1.2");
		Properties props = getProperties();

		Authenticator auth = new SMTPAuthenticator();
		Session session = Session.getInstance(props, auth);

		try {
			MimeMessage msg = new MimeMessage(session);
			msg.setFrom(new InternetAddress(adresseEnvoi));
			InternetAddress[] adresse = new InternetAddress[1];
			try {
				adresse[0] = new InternetAddress(adresseDestinataire, nomDestinataire);
			} catch (UnsupportedEncodingException e) {
				System.out.println(ERREUR_CODAGE_TEXTE + nomDestinataire);
				e.printStackTrace();
				return false;
			}

			msg.setRecipients(Message.RecipientType.TO, adresse);
			try {
				msg.setSubject(MimeUtility.encodeText(sujet));
			} catch (UnsupportedEncodingException e) {
				System.out.println(ERREUR_CODAGE_TEXTE + sujet);
				e.printStackTrace();
				return false;
			}

			// --- 1. Conteneur principal (mixed) pour les pièces jointes + contenu ---
			MimeMultipart mpMain = new MimeMultipart("mixed");

			// --- 2. Conteneur alternatif (brut + HTML) ---
			MimeMultipart mpAlternative = new MimeMultipart("alternative");

			// Partie A : Texte brut
			MimeBodyPart textPart = new MimeBodyPart();
			textPart.setText(corpsTexte, "UTF-8");
			mpAlternative.addBodyPart(textPart);

			// Partie B : HTML
			MimeBodyPart htmlPart = new MimeBodyPart();
			htmlPart.setContent(corpsHtml, "text/html; charset=UTF-8");
			mpAlternative.addBodyPart(htmlPart);

			// Inclusion du bloc alternatif dans le conteneur principal
			MimeBodyPart bodyWrapper = new MimeBodyPart();
			bodyWrapper.setContent(mpAlternative);
			mpMain.addBodyPart(bodyWrapper);

			// --- 3. Ajout des pièces jointes ---
			for (int i = 0; i < fichiers.length; i++) {
				MimeBodyPart mbp2 = new MimeBodyPart();
				FileDataSource fds = new FileDataSource(fichiers[i]);
				mbp2.setDataHandler(new DataHandler(fds));
				mbp2.setFileName(fds.getName());
				mpMain.addBodyPart(mbp2);
			}

			msg.setContent(mpMain);
			msg.setSentDate(new Date());

			Transport.send(msg);

		} catch (MessagingException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	private static Properties getProperties() {
		Properties props = System.getProperties();
		props.put("mail.smtp.ssl.protocols", "TLSv1.2");
		props.put("mail.smtp.ssl.trust", Const.MAIL_SMTP_HOST);
		props.put("mail.smtp.user", Const.MAIL_SMTP_LOGIN);
		props.put("mail.smtp.password", Const.MAIL_SMTP_PASSWORD);
		props.put("mail.smtp.host", Const.MAIL_SMTP_HOST);
		props.put("mail.smtp.port", Const.MAIL_SMTP_PORT);
		props.put("mail.smtp.starttls.enable", Const.MAIL_SMTP_TTLS);
		props.put("mail.smtp.auth", Const.MAIL_SMTP_AUTH);
		props.put("mail.smtp.debug", "true");
		return props;
	}
}

class SMTPAuthenticator extends Authenticator {
	protected PasswordAuthentication getPasswordAuthentication() {
		return new PasswordAuthentication(Const.MAIL_SMTP_LOGIN, Const.MAIL_SMTP_PASSWORD);
	}
};