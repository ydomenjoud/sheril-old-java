// Console d'ordres (/play/console) : en bureau, l'iframe affiche l'ancienne console en frameset
// (menu à gauche + fenêtre de l'ordre) ; en mobile, seulement la fenêtre de l'ordre, choisie
// dans la liste déroulante #console-choix.
(function () {
    var cadre = document.querySelector('#play > .page-frame');
    var choix = document.querySelector('#console-choix select');
    var bureau = cadre.getAttribute('src');
    var mobile = window.matchMedia('(max-width: 700px)'); // $breakpoint-mobile

    function adapter() {
        var url = mobile.matches ? (choix ? choix.value : cadre.getAttribute('data-mobile')) : bureau;
        if (cadre.getAttribute('src') !== url) cadre.setAttribute('src', url);
    }

    if (choix) {
        choix.addEventListener('change', function () { cadre.setAttribute('src', choix.value); });
        // Navigation dans l'iframe (lien, envoi d'un ordre) : la liste suit la page affichée
        cadre.addEventListener('load', function () {
            try {
                var l = cadre.contentWindow.location;
                var url = l.pathname + l.search;
                for (var i = 0; i < choix.options.length; i++) {
                    if (choix.options[i].value === url) { choix.selectedIndex = i; break; }
                }
            } catch (e) { /* page d'une autre origine : rien à faire */ }
        });
    }

    adapter();
    if (mobile.addEventListener) mobile.addEventListener('change', adapter);
    else mobile.addListener(adapter);
})();
