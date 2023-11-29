document.addEventListener("DOMContentLoaded", function () {
    const apiUrl = "https://asia-south1.gcp.data.mongodb-api.com/app/application-0-dlgbn/endpoint/getAllArtikel";
    const articlesPerPage = 6; // Jumlah artikel per halaman
    let currentPage = 1; // Halaman saat ini

    async function fetchData() {
        const start = (currentPage - 1) * articlesPerPage;
        const end = start + articlesPerPage;

        try {
            const response = await fetch(apiUrl);
            const data = await response.json();

            console.log('Mengambil data:', data);

            const articles = data.slice(start, end);
            console.log('Artikel:', articles);

            // Hapus semua kartu sebelum menambahkan yang baru
            const articleContainer = document.querySelector('.article-container');
            articleContainer.innerHTML = '';

            // Hanya tampilkan jumlah artikel yang sesuai
            const displayedArticles = articles.slice(0, articlesPerPage);

            displayedArticles.forEach((article, index) => {
                createArticleCard(index + 1, article);
            });

            // Update pagination setelah mengambil data
            updatePagination(data.length);

        } catch (error) {
            console.error("Kesalahan mengambil data:", error);
        }
    }

    function createArticleCard(index, article) {
        const articleContainer = document.querySelector('.article-container');
        const newArticleCard = document.createElement('div');
        newArticleCard.classList.add('article-card');
        newArticleCard.id = `article-card-${index}`;

        const cardImage = document.createElement('img');
        cardImage.src = ''; 
        cardImage.alt = 'Card Image';
        cardImage.id = `gambarArtikel${index}`;
        newArticleCard.appendChild(cardImage);

        const cardContent = document.createElement('div');
        cardContent.classList.add('card-content');
        newArticleCard.appendChild(cardContent);

        const cardTitle = document.createElement('h5');
        cardTitle.classList.add('card-title');
        cardTitle.id = `judulArtikel${index}`;
        cardContent.appendChild(cardTitle);

        const readMoreLink = document.createElement('a');
        readMoreLink.classList.add('read-more');
        readMoreLink.href = `detail_artikel.php?id_artikel=${article.id_artikel}&id=${encryptedId}`;
        readMoreLink.innerHTML = `<i class="fas fa-arrow-right"></i> Baca Selengkapnya`;
        cardContent.appendChild(readMoreLink);

        newArticleCard.addEventListener('click', function () {
            window.location.href = `detail_artikel.php?id_artikel=${article.id_artikel}&id=${encryptedId}`;
        });

        articleContainer.appendChild(newArticleCard);

        // Set konten untuk kartu baru
        const gambarArtikel = document.getElementById(`gambarArtikel${index}`);
        const judulArtikel = document.getElementById(`judulArtikel${index}`);

        gambarArtikel.src = article.gambar_artikel;
        judulArtikel.textContent = article.judul;
    }

    function createPaginationLinks(totalPages) {
        const paginationDiv = document.getElementById("pagination");
        paginationDiv.innerHTML = "";

        for (let i = 1; i <= totalPages; i++) {
            const pageLink = document.createElement("a");
            pageLink.href = "#";
            pageLink.classList.add("page");
            pageLink.textContent = i;

            pageLink.addEventListener("click", function (event) {
                event.preventDefault();
                currentPage = i;
                fetchData();
            });

            paginationDiv.appendChild(pageLink);
        }
    }

    function updatePagination(totalArticles) {
        const totalPages = Math.ceil(totalArticles / articlesPerPage);
        createPaginationLinks(totalPages);
    }

    function getTotalPages(totalArticles) {
        return Math.ceil(totalArticles / articlesPerPage);
    }

    fetch(apiUrl)
        .then(response => response.json())
        .then(data => {
            const totalArticles = data.length;
            const totalPages = getTotalPages(totalArticles);
            updatePagination(totalArticles);
            fetchData();
        })
        .catch(error => console.error("Kesalahan mengambil total artikel:", error));
});
