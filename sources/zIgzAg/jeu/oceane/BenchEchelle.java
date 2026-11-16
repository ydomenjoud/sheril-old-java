package zIgzAg.jeu.oceane;
/** Essai jetable : remplace Const.NB_CIBLES par une échelle donnée (10 valeurs séparées par des virgules). */
public class BenchEchelle {
	public static void main(String[] a) throws Exception {
		String[] v = a[0].split(",");
		for (int i = 0; i < Const.NB_CIBLES.length; i++) Const.NB_CIBLES[i] = Integer.parseInt(v[i]);
		String[] reste = new String[a.length - 1];
		System.arraycopy(a, 1, reste, 0, reste.length);
		BenchCombat.main(reste);
	}
}
