"""Produit une page HTML autonome (données intégrées) à partir de matrice.csv.

python3 generer_html.py matrice.csv sortie.html

La page permet de choisir l'armement et les boucliers de chaque camp ainsi que les règles,
et affiche le tableau croisé des tailles (attaquant en ligne, défenseur en colonne) avec le
pourcentage de minerai encore vivant en fin de combat pour chaque camp.
Minerai vivant = 1 - pertes moyennes / vaisseaux engagés (un vaisseau endommagé compte entier)."""
import csv, json, re, sys
from datetime import date

entree, sortie = sys.argv[1], sys.argv[2]
NOM = re.compile(r"T(\d) (\w+?)(\+B)? contre T(\d) (\w+?)(\+B)?$")

donnees, regles, combats = {}, [], set()
for r in csv.DictReader(open(entree, encoding="utf-8")):
    m = NOM.match(r["scenario"])
    if not m:
        continue
    ta, aa, ba, td, ad, bd = m[1], m[2], "1" if m[3] else "0", m[4], m[5], "1" if m[6] else "0"
    if r["regles"] not in regles:
        regles.append(r["regles"])
    combats.add(r["combats"])
    na, nd = int(r["vaisseaux_a"]), int(r["vaisseaux_d"])
    donnees["|".join((r["regles"], aa, ba, ad, bd, ta, td))] = [
        round(100 * (1 - float(r["pertes_a_moy"]) / na), 1),
        round(100 * (1 - float(r["pertes_d_moy"]) / nd), 1),
        round(float(r["victoires_a_pct"])), round(float(r["victoires_d_pct"])),
        na, nd, round(float(r["rounds_moy"]), 1), int(r["minerai_a"]),
    ]

meta = {"regles": regles, "combats": sorted(combats), "date": date.today().isoformat(), "n": len(donnees)}
html = open(__file__.replace("generer_html.py", "matrice_modele.html"), encoding="utf-8").read()
html = html.replace("/*DONNEES*/null", json.dumps(donnees, separators=(",", ":"))).replace("/*META*/null", json.dumps(meta))
open(sortie, "w", encoding="utf-8").write(html)
print(f"{len(donnees)} résultats, règles {regles} -> {sortie}")
