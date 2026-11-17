#!/usr/bin/env bash
# Lance la matrice complète en parallèle, puis fusionne les résultats dans matrice.csv.
# Reprise possible : les lots dont le CSV existe déjà et n'est pas vide sont sautés.
#
#   benchmarks/matrice/lancer_matrice.sh DOSSIER [COMBATS] [PROCESSUS] [REGLES]
#
# À lancer depuis la racine du dépôt, après compilation dans classes/.
set -euo pipefail
dossier=${1:?dossier de travail}
combats=${2:-50}
processus=${3:-20}
regles=${4:-multi,multi+fiab,multi+fiab80}
ici=$(dirname "$0")

[ -f "$dossier/lot_000.bench" ] || python3 "$ici/gen_matrice.py" "$dossier" 4

export combats regles
ls "$dossier"/lot_*.bench | sort | xargs -P "$processus" -I{} bash -c '
    b=${1%.bench}
    [ -s "$b.csv" ] && exit 0
    java -Xmx2g -cp "classes:libs/*" zIgzAg.jeu.oceane.BenchCombat "$1" --n "$combats" --regles "$regles" --csv "$b.tmp.csv" > "$b.log" 2>&1 \
        && mv "$b.tmp.csv" "$b.csv" || echo "échec : $1 (voir $b.log)" >&2
' _ {}

# fusion : un seul en-tête (glob plutôt que « ls | head », qui casse sous pipefail)
lots=("$dossier"/lot_[0-9][0-9][0-9].csv)
echo "${#lots[@]} lots sur $(ls "$dossier"/lot_*.bench | wc -l) terminés"
{ head -1 "${lots[0]}"; for f in "${lots[@]}"; do tail -n +2 "$f"; done; } > "$dossier/matrice.csv"
echo "$(($(wc -l < "$dossier/matrice.csv") - 1)) lignes dans $dossier/matrice.csv"
