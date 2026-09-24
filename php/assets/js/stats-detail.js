/*
 * Graphiques de /statistiques/detail : une courbe par commandant et par statistique.
 * Les données sont dans <script id="stats-series"> ; les couleurs (--stats-serie-N) et l'encre
 * (--sheril-*) viennent du CSS. La couleur suit l'emplacement du commandant (slot), jamais son rang.
 */
(function () {
    var series = JSON.parse(document.getElementById('stats-series').textContent);
    var css = getComputedStyle(document.documentElement);
    var v = function (nom) { return css.getPropertyValue(nom).trim(); };
    var encre = {
        texte: v('--sheril-text-primary'),
        secondaire: v('--sheril-text-secondary'),
        muet: v('--sheril-text-tertiary'),
        grille: v('--sheril-border-subtle'),
        axe: v('--sheril-border-default'),
        surface: v('--sheril-surface-card')
    };
    var nombre = new Intl.NumberFormat('fr-FR');
    var compact = new Intl.NumberFormat('fr-FR', {notation: 'compact', maximumFractionDigits: 1});
    var etiquettes = series.length <= 4; // étiquettes en bout de courbe seulement s'il y a peu de séries

    Chart.defaults.font.family = '"IBM Plex Mono", monospace';
    Chart.defaults.font.size = 11;
    Chart.defaults.color = encre.muet;

    // Ligne verticale qui suit le tour survolé
    var reticule = {
        id: 'reticule',
        afterDatasetsDraw: function (chart) {
            var actifs = chart.tooltip && chart.tooltip.getActiveElements();
            if (!actifs || !actifs.length) return;
            var x = actifs[0].element.x, zone = chart.chartArea, ctx = chart.ctx;
            ctx.save();
            ctx.strokeStyle = encre.muet;
            ctx.lineWidth = 1;
            ctx.beginPath();
            ctx.moveTo(x, zone.top);
            ctx.lineTo(x, zone.bottom);
            ctx.stroke();
            ctx.restore();
        }
    };

    // Nom du commandant au bout de sa courbe, en encre de texte (la couleur reste sur le trait)
    var etiquettesFin = {
        id: 'etiquettesFin',
        afterDatasetsDraw: function (chart) {
            if (!etiquettes) return;
            var ctx = chart.ctx, pos = [];
            chart.data.datasets.forEach(function (ds, i) {
                var pts = chart.getDatasetMeta(i).data;
                if (pts.length) pos.push({y: pts[pts.length - 1].y, x: pts[pts.length - 1].x, texte: ds.label});
            });
            // Écarte les étiquettes qui se chevauchent
            pos.sort(function (a, b) { return a.y - b.y; });
            for (var i = 1; i < pos.length; i++) {
                if (pos[i].y - pos[i - 1].y < 13) pos[i].y = pos[i - 1].y + 13;
            }
            ctx.save();
            ctx.font = '11px "IBM Plex Sans", sans-serif';
            ctx.fillStyle = encre.secondaire;
            ctx.textBaseline = 'middle';
            pos.forEach(function (p) {
                var t = p.texte.length > 16 ? p.texte.slice(0, 15) + '…' : p.texte;
                ctx.fillText(t, p.x + 6, p.y);
            });
            ctx.restore();
        }
    };

    // Infobulle HTML : le tour, puis une ligne par commandant (valeur en avant, nom ensuite)
    var bulle = document.getElementById('infobulle');
    function infobulle(contexte) {
        var tip = contexte.tooltip;
        if (!tip || tip.opacity === 0 || !tip.dataPoints || !tip.dataPoints.length) { bulle.hidden = true; return; }
        bulle.replaceChildren();
        var titre = document.createElement('div');
        titre.className = 'stats-infobulle__titre';
        titre.textContent = 'Tour ' + tip.dataPoints[0].parsed.x;
        bulle.appendChild(titre);
        tip.dataPoints.slice().sort(function (a, b) { return b.parsed.y - a.parsed.y; }).forEach(function (p) {
            var ligne = document.createElement('div');
            ligne.className = 'stats-infobulle__ligne';
            var cle = document.createElement('span');
            cle.className = 'stats-infobulle__cle';
            cle.style.background = p.dataset.borderColor;
            var val = document.createElement('strong');
            val.textContent = nombre.format(p.parsed.y);
            var nom = document.createElement('span');
            nom.textContent = p.dataset.label;
            ligne.append(cle, val, nom);
            bulle.appendChild(ligne);
        });
        bulle.hidden = false;
        var r = contexte.chart.canvas.getBoundingClientRect();
        var x = r.left + window.scrollX + tip.caretX + 14;
        if (x + bulle.offsetWidth > window.scrollX + document.documentElement.clientWidth - 8) {
            x = r.left + window.scrollX + tip.caretX - bulle.offsetWidth - 14;
        }
        bulle.style.left = x + 'px';
        bulle.style.top = (r.top + window.scrollY + tip.caretY - bulle.offsetHeight / 2) + 'px';
    }

    document.querySelectorAll('canvas[data-stat]').forEach(function (canvas) {
        var stat = canvas.dataset.stat;
        new Chart(canvas, {
            type: 'line',
            data: {
                datasets: series.map(function (s) {
                    var couleur = v('--stats-serie-' + s.slot);
                    return {
                        label: s.nom,
                        data: s.points[stat] || [],
                        borderColor: couleur,
                        backgroundColor: couleur,
                        borderWidth: 2,
                        pointRadius: 0,
                        pointHoverRadius: 4,
                        pointHoverBorderWidth: 2,
                        pointHoverBorderColor: encre.surface,
                        tension: 0
                    };
                })
            },
            options: {
                responsive: true,
                maintainAspectRatio: false,
                animation: false,
                interaction: {mode: 'index', intersect: false},
                layout: {padding: {right: etiquettes ? 110 : 8, top: 4}},
                scales: {
                    x: {
                        type: 'linear',
                        ticks: {stepSize: 1, precision: 0, color: encre.muet, maxRotation: 0, autoSkipPadding: 8},
                        grid: {display: false},
                        border: {color: encre.axe},
                        title: {display: true, text: 'Tour', color: encre.muet}
                    },
                    y: {
                        ticks: {color: encre.muet, maxTicksLimit: 5, callback: function (val) { return compact.format(val); }},
                        grid: {color: encre.grille},
                        border: {display: false}
                    }
                },
                plugins: {
                    legend: {display: false}, // la légende est la liste des commandants au-dessus
                    tooltip: {enabled: false, external: infobulle}
                }
            },
            plugins: [reticule, etiquettesFin]
        });
        canvas.addEventListener('mouseleave', function () { bulle.hidden = true; });
    });
})();
