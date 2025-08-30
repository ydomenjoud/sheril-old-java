// v2.00 01/02/01
// Copyright 2001 Julien Buret All Rights Reserved.
// Use is subject to license terms.

package zIgzAg.jeu.oceane;

import java.text.MessageFormat;

import zIgzAg.utile.Copie;
import zIgzAg.utile.Fiche;
import zIgzAg.utile.Mail;

public class EnvoyerRapport {

	public static void zipper(Commandant c) {
		String chemin = Chemin.ZIP;

		String[] f = null;

		if (c.getTourArrivee() == Univers.getTour()) {
			f = new String[2];
			f[0] = Chemin.RAPPORTS_IMAGES;
			f[1] = Chemin.RAPPORTS + c.getNumero()+"tour"+Univers.getTour();
		} else {
			f = new String[1];
			f[0] = Chemin.RAPPORTS + c.getNumero()+"tour"+Univers.getTour();
		}
		Copie.zipper(f, chemin, Integer.toString(c.getNumero()) + "tour" + Univers.getTour() + ".zip");

		ProductionOrdres.ecrireSecurite(chemin, c);
		if (c.getTourArrivee() == Univers.getTour()) {
			ProductionOrdres.ecrirePasseport(Chemin.SECURITE_RAPPORT, c);
		}
	}

	public static void envoyer(Commandant c) {
		MessageFormat m = new MessageFormat(Univers.getMessageInfo(
				"MAIL_TITRE_RAPPORT", c.getLocale()));
		Object[] o = { new Integer(Univers.getTour()) };
		String sujet = m.format(o);

		if (c.getTourArrivee() == Univers.getTour())

			m = new MessageFormat(Univers.getMessageInfo(
					"MAIL_CORPS_NOUVEAU_RAPPORT", c.getLocale()));
		else
			m = new MessageFormat(Univers.getMessageInfo("MAIL_CORPS_RAPPORT", c.getLocale()));
		Object[] o2 = {
				c.getLogin(),
				c.getMotDePasse(),
				Const.ADRESSE_SITE_RAPPORTS + Integer.toString(c.getNumero())
						+ "/" + Integer.toString(c.getNumero()) + ".zip" };
		String corpsMessage = m.format(o2);
		String[] fichiers = new String[0];

		if (!Mail.envoyerMessageFichiersAttaches(c.getNomNumero(),
				c.getAdresseElectronique(), Const.ADRESSE_MJ, Const.SMTP_ENVOI,
				sujet, corpsMessage, fichiers))
			Fiche.ecriture(Const.TEMP, c.getNomNumero()
					+ ":erreur envoi rapport");
	}

	public static void envoyerMessage(Commandant c, String texte) {
		if (!Mail.envoyerMessageFichiersAttaches(c.getNomNumero(),
				c.getAdresseElectronique(), Const.ADRESSE_MJ, Const.SMTP_ENVOI,
				"[Sheril]Message-Info", texte, new String[0]))
			Fiche.ecriture(Const.TEMP, c.getNomNumero()
					+ ":erreur envoi message");
	}

	public static void envoyerMessage(Commandant c, String texte, Object[] param) {
		MessageFormat m = new MessageFormat(new String(texte));
		texte = m.format(param);
		if (!Mail.envoyerMessageFichiersAttaches(c.getNomNumero(),
				c.getAdresseElectronique(), Const.ADRESSE_MJ, Const.SMTP_ENVOI,
				"[Sheril]Message-Info", texte, new String[0]))
			Fiche.ecriture(Const.TEMP, c.getNomNumero()
					+ ":erreur envoi message");
	}

}
