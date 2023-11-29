document.addEventListener("DOMContentLoaded", function () {
    
    const url = 'https://asia-south1.gcp.data.mongodb-api.com/app/application-0-dlgbn/endpoint/getResepByTitle';
    const cardsPerPage = 6;
    let currentPage = 1;
    const urlParams = new URLSearchParams(window.location.search);
    const keyword = urlParams.get('keyword');
    const encryptedId = urlParams.get('id');

    fetchAndDisplayRecipes(url, currentPage, keyword);

    const prevPageButton = document.getElementById("prevPage");
    const nextPageButton = document.getElementById("nextPage");

    prevPageButton.addEventListener("click", function () {
        if (currentPage > 1) {
            currentPage--;
            fetchAndDisplayRecipes(url, currentPage, keyword);
            scrollBackToTitle();
        }
    });

    nextPageButton.addEventListener("click", function () {
        currentPage++;
        fetchAndDisplayRecipes(url, currentPage, keyword);
        scrollBackToTitle();
    });

    function fetchAndDisplayRecipes(url, page, keyword) {
        const startIndex = (page - 1) * cardsPerPage;
        const endIndex = startIndex + cardsPerPage;

        fetch(`${url}?keyword=${encodeURIComponent(keyword)}`)
            .then(response => response.json())
            .then(data => {
                if (data.error && data.error === "Resep not found") {
                    // Tampilkan sweet alert info "Data tak ditemukan"
                    Swal.fire({
                        icon: 'info',
                        title: 'Data Tak Ditemukan',
                        text: 'Maaf, data resep tidak ditemukan.',
                    }).then(() => {
                        // Kembalikan user ke resep.php
                        window.location.href = 'resep.php?id='+encryptedId;
                    });
                } else {
                    const mealContainer = document.getElementById('meal');
                    mealContainer.innerHTML = '';

                    data.slice(startIndex, endIndex).forEach(resep => {
                        const card = document.createElement('div');
                        card.classList.add('meal-item');

                        const mealImg = document.createElement('div');
                        mealImg.classList.add('meal-img');
                        const calories = document.createElement('span');
                        calories.classList.add('calories');
                        calories.textContent = resep.jumlah_kalori_resep + ' kkal';
                        const img = document.createElement('img');
                        img.src = resep.gambar_resep;
                        img.alt = resep.judul_resep;
                        mealImg.appendChild(calories);
                        mealImg.appendChild(img);

                        const mealName = document.createElement('div');
                        mealName.classList.add('meal-name');
                        const name = document.createElement('h3');
                        name.textContent = resep.judul_resep;
                        const recipeLink = document.createElement('a');
                        recipeLink.classList.add('recipe-btn');
                        recipeLink.textContent = 'Coba';
                        mealName.appendChild(name);
                        mealName.appendChild(recipeLink);

                        card.appendChild(mealImg);
                        card.appendChild(mealName);
                        mealContainer.appendChild(card);

                        card.addEventListener('click', function () {
                            const mealDetails = document.querySelector('.meal-details');
                            const recipeTitle = document.getElementById('nama_resep');
                            const recipeDesc = document.getElementById('deskripsi_resep');
                            const recipeImg = document.getElementById('gambar_resep');
                            const recipeId = document.getElementById('id_resep');
                            recipeTitle.textContent = resep.judul_resep;
                            recipeDesc.textContent = resep.deskripsi;
                            recipeImg.src = resep.gambar_resep;
                            recipeId.textContent = resep.id_resep;
                            mealDetails.style.display = 'block';
                        });
                    });

                    document.getElementById('currentPage').textContent = currentPage;
                }
            })
            .catch(error => {
                console.error('Error:', error);
                // Tampilkan sweet alert info "Data tak ditemukan"
                Swal.fire({
                    icon: 'info',
                    title: 'Data Tak Ditemukan',
                    text: 'Maaf, data resep tidak ditemukan.',
                }).then(() => {
                    // Kembalikan user ke resep.php
                    window.location.href = 'resep.php?id='+encryptedId;
                });
            });
    }

    function scrollBackToTitle() {
        const titleElement = document.getElementById('title');
        titleElement.scrollIntoView({ behavior: 'smooth' });
    }
});
