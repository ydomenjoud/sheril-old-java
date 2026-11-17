"""Génère la matrice complète des combats à minerai égal : tailles T3 à T9 de chaque côté,
4 armes de chaque côté (niveau V), boucliers V oui/non pour chaque camp.

Le camp dont le vaisseau coûte le plus aligne 20 vaisseaux ; l'autre reçoit le même minerai
et en aligne autant que possible (« * »). À coût égal, 20 contre 20.

python3 gen_matrice.py dossier_sortie [taille_des_lots]
Les scénarios sont triés du plus coûteux au plus léger puis découpés en lots
(lot_000.bench, …) pour être lancés en parallèle (voir lancer_matrice.sh)."""
import os, sys

TAILLES = range(3, 10)
ARMES = ("laser", "plasma", "torp", "miss")
ARMES_PAR_TAILLE = {3: 16, 4: 32, 5: 64, 6: 128, 7: 256, 8: 511, 9: 1024}
MOTEUR = {3: 1, 4: 2, 5: 2, 6: 3, 7: 3, 8: 3, 9: 3}
MINERAI_MOTEUR = {1: 1, 2: 4, 3: 8}
BOUCLIERS = {3: 1, 4: 2, 5: 3, 6: 5, 7: 10, 8: 10, 9: 10}  # 4 cases d'armes chacun
NB_FIXE = 20


def vaisseau(t, arme, bouclier):
    nb = BOUCLIERS[t] if bouclier else 0
    n = ARMES_PAR_TAILLE[t] - 4 * nb
    plan = f"moteur@{MOTEUR[t]} {arme}@5x{n}" + (f" bouclier@5x{nb}" if nb else "")
    return plan, 5 * n + MINERAI_MOTEUR[MOTEUR[t]] + 25 * nb, n


def nom(ta, aa, ba, td, ad, bd):
    return f"T{ta} {aa}{'+B' if ba else ''} contre T{td} {ad}{'+B' if bd else ''}"


def scenarios():
    for ba in (False, True):
        for bd in (False, True):
            for ta in TAILLES:
                for td in TAILLES:
                    for aa in ARMES:
                        for ad in ARMES:
                            pa, ma, na = vaisseau(ta, aa, ba)
                            pd, md, nd = vaisseau(td, ad, bd)
                            budget = NB_FIXE * max(ma, md)
                            la = f"A: * T{ta} = {pa}" if ma < md else f"A: {NB_FIXE} T{ta} = {pa}"
                            ld = f"D: * T{td} = {pd}" if md < ma else f"D: {NB_FIXE} T{td} = {pd}"
                            # coût estimé : nombre de vaisseaux du plus petit camp × tirs du camp adverse
                            nb_a = budget // ma
                            nb_d = budget // md
                            cout = nb_a * nb_d * (na + nd)
                            yield cout, (f"[{nom(ta, aa, ba, td, ad, bd)}]\n"
                                         f"A: agressivite=rage" + (f" minerai={budget}" if ma < md else "") + "\n"
                                         f"{la}\n"
                                         f"D: agressivite=rage" + (f" minerai={budget}" if md < ma else "") + "\n"
                                         f"{ld}")


if __name__ == "__main__":
    sortie = sys.argv[1]
    taille_lot = int(sys.argv[2]) if len(sys.argv) > 2 else 8
    os.makedirs(sortie, exist_ok=True)
    tous = sorted(scenarios(), key=lambda s: -s[0])
    for i in range(0, len(tous), taille_lot):
        with open(os.path.join(sortie, f"lot_{i // taille_lot:03d}.bench"), "w", encoding="utf-8") as f:
            f.write("\n\n".join(b for _, b in tous[i:i + taille_lot]) + "\n")
    print(len(tous), "scénarios,", (len(tous) + taille_lot - 1) // taille_lot, "lots")
