document.addEventListener('DOMContentLoaded', () => {
    async function getTourComplet() {
        const response = await fetch('tour.txt');

        // Récupérer le contenu
        const numeroTour = await response.text();

        // Récupérer la date de modification
        const lastModified = response.headers.get('Last-Modified');

        if (lastModified) {
            const date = new Date(lastModified);
            const dateFormatee = date.toLocaleDateString('fr-FR');
            const placeholder = document.querySelector('body>header');
            if (placeholder) {
                placeholder.insertAdjacentHTML(
                    'beforeend',
                    `<small>Dernier tour :  ${numeroTour.trim()}, le ${dateFormatee} - <a href="https://discord.gg/bdUtYSqrnK">discord</a></small> `
                );
            }
        } else {
            console.log("Date de modification inaccessible.");
        }
    }

    getTourComplet().catch(console.error);
});