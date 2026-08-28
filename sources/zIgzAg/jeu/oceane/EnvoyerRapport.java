// v2.00 01/02/01
// Copyright 2001 Julien Buret All Rights Reserved.
// Use is subject to license terms.

package zIgzAg.jeu.oceane;

import java.text.MessageFormat;
import java.util.Arrays;

import zIgzAg.utile.Copie;
import zIgzAg.utile.Fiche;
import zIgzAg.utile.Mail;

public class EnvoyerRapport {

	public static void zipper(Commandant c) {
		String chemin = Chemin.ZIP;

		// on met toujours les images pour avoir les MAJ
		String[] f = new String[2];
		f[0] = Chemin.RAPPORTS_IMAGES;
		f[1] = Chemin.RAPPORTS + c.getNumero()+"tour"+Univers.getTour();
		Copie.zipper(f, chemin, c.getNumero() + "tour" + Univers.getTour() + ".zip");

		ProductionOrdres.ecrireSecurite(chemin, c);
		if (c.getTourArrivee() == Univers.getTour()) {
			ProductionOrdres.ecrirePasseport(Chemin.SECURITE_RAPPORT, c);
		}
	}

	public static void envoyer(Commandant c) {
		// si on envoit pas de mail ou que c'est un tour de test, on essaye même pas
		if((!Const.SEND_MAIL || Const.FAKE_TURN) && c.getNumero() != 1) { return; }

		String sujet = new MessageFormat(Univers.getMessageInfo("MAIL_TITRE_RAPPORT", c.getLocale()))
				.format(new Object[]{ Const.GAME_NAME, Univers.getTour() });

		Object[] o2 = {
				c.getLogin(),
				c.getMotDePasse(),
				Chemin.RACINE_SITE,
				Const.GAME_NAME,
		};
		String corpsMessageHTML = new MessageFormat(Univers.getMessageInfo("MAIL_CORPS_RAPPORT_HTML", c.getLocale()))
				.format(o2);
		String corpsMessageTXT = new MessageFormat(Univers.getMessageInfo("MAIL_CORPS_RAPPORT_TXT", c.getLocale()))
				.format(o2);
		String[] fichiers = new String[0];

		if (!Mail.envoyerMessageFichiersAttaches(c.getNomNumeroText(),
				c.getAdresseElectronique(), Const.ADRESSE_MJ, Const.SMTP_ENVOI,
				sujet, corpsMessageTXT, corpsMessageHTML, fichiers))
			Fiche.ecriture(Const.TEMP, c.getNomNumeroHtml()
					+ ":erreur envoi rapport");
	}


}
