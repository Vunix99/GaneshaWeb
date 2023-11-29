document.addEventListener('DOMContentLoaded', function () {
    // Fetch articles and populate the article-container
    fetch('https://asia-south1.gcp.data.mongodb-api.com/app/application-0-dlgbn/endpoint/getAllArtikel')
        .then(response => response.json())
        .then(data => {
            const articleContainer = document.querySelector('.article-container');
            const sampleCard = articleContainer.querySelector('.article-card');
            const sampleCardImage = sampleCard.querySelector('img');
            const sampleCardTitle = sampleCard.querySelector('.card-title');
            const sampleCardText = sampleCard.querySelector('.card-text-article');

            const maxArticles = 6;
            let count = 0;

            data.forEach(article => {
                if (count >= maxArticles) {
                    return;
                }

                const { judul, gambar_artikel, isi_artikel, id_artikel } = article;

                if (count === 0) {
                    sampleCardImage.src = gambar_artikel;
                    sampleCardTitle.textContent = judul;
                    sampleCardText.textContent = getFirstWords(isi_artikel, 25) + '...';

                    // Add event listener for the first card
                    sampleCard.addEventListener('click', function () {
                        window.location.href = `detail_artikel.php?id_artikel=${id_artikel}&id=${encryptedId}`;
                    });
                } else {
                    // Create a new article card
                    const articleCard = document.createElement('div');
                    articleCard.classList.add('article-card');

                    // Create elements for the article card
                    const cardImage = document.createElement('img');
                    cardImage.src = gambar_artikel;
                    cardImage.alt = 'Ini Gambar Artikel';
                    articleCard.appendChild(cardImage);

                    const cardContent = document.createElement('div');
                    cardContent.classList.add('card-content');
                    articleCard.appendChild(cardContent);

                    const cardTitle = document.createElement('h5');
                    cardTitle.classList.add('card-title');
                    cardTitle.textContent = judul;
                    cardContent.appendChild(cardTitle);

                    const cardTextArticle = document.createElement('p');
                    cardTextArticle.classList.add('card-text-article');
                    cardTextArticle.textContent = getFirstWords(isi_artikel, 25) + '...';
                    cardContent.appendChild(cardTextArticle);

                    const readMoreLink = document.createElement('a');
                    readMoreLink.classList.add('read-more');
                    readMoreLink.href = '#';
                    readMoreLink.innerHTML = '<i class="fas fa-arrow-right"></i> Baca selengkapnya';
                    cardContent.appendChild(readMoreLink);

                    // Add event listener for the article card
                    articleCard.addEventListener('click', function () {
                        window.location.href = `detail_artikel.php?id_artikel=${id_artikel}&id=${encryptedId}`;
                    });

                    // Append the article card to the container
                    articleContainer.appendChild(articleCard);
                }

                count++;
            });
        })
        .catch(error => console.error('Error:', error));

    // Helper function to get the first n words from a string
    function getFirstWords(str, n) {
        const words = str.split(' ');
        return words.slice(0, n).join(' ');
    }
});
