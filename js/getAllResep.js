document.addEventListener("DOMContentLoaded", function() {
    const recipeButtons = document.querySelectorAll(".recipe-btn");
    const recipeCloseButton = document.getElementById("recipe-close-btn");
    const mealDetails = document.querySelector(".meal-details");
    let currentRecipeId;
    let currentPage = 1; // Menyimpan halaman saat ini
    const cardsPerPage = 6; // Jumlah kartu per halaman

    // Fungsi untuk menampilkan modal detail
    const displayModal = function() {
        mealDetails.style.display = "block";
    };

    // Fungsi untuk menyembunyikan modal detail
    const hideModal = function() {
        mealDetails.style.display = "none";
    };

    // Tambahkan penanganan klik pada setiap tombol "Coba"
    recipeButtons.forEach(button => {
        button.addEventListener("click", function() {
            displayModal();
        });
    });

    // Tambahkan penanganan klik pada tombol close
    recipeCloseButton.addEventListener("click", function() {
        hideModal();
    });

    // Tambahkan penanganan klik pada area di luar modal untuk menutup modal
    window.addEventListener("click", function(event) {
        if (event.target === mealDetails) {
            hideModal();
        }
    });

    const url = 'https://asia-south1.gcp.data.mongodb-api.com/app/application-0-dlgbn/endpoint/getAllResep';

    // Fetch data dan tampilkan kartu pada halaman pertama
    fetchAndDisplayRecipes(url, currentPage);

    // Tambahkan penanganan klik pada tombol Next dan Previous
    const prevPageButton = document.getElementById("prevPage");
    const nextPageButton = document.getElementById("nextPage");

    prevPageButton.addEventListener("click", function() {
        if (currentPage > 1) {
            currentPage--;
            fetchAndDisplayRecipes(url, currentPage);
            scrollBackToTitle();
        }
    });

    nextPageButton.addEventListener("click", function() {
        currentPage++;
        fetchAndDisplayRecipes(url, currentPage);
        scrollBackToTitle();
    });

    function fetchAndDisplayRecipes(url, page) {
        const startIndex = (page - 1) * cardsPerPage;
        const endIndex = startIndex + cardsPerPage;

        fetch(url)
            .then(response => response.json())
            .then(data => {
                const mealContainer = document.getElementById('meal');
                mealContainer.innerHTML = ''; // Hapus konten sebelum menambahkan kartu baru

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

                    // Memberikan fungsi untuk memperbarui detail resep saat kartu diklik
                    card.addEventListener('click', function() {
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
                        currentRecipeId = resep.id_resep;
                    });
                });

                document.getElementById('currentPage').textContent = currentPage;
            })
            .catch(error => console.error('Error:', error));
    }

    // Fungsi untuk menggulir halaman kembali ke elemen dengan ID "title"
    function scrollBackToTitle() {
        const titleElement = document.getElementById('title');
        titleElement.scrollIntoView({ behavior: 'smooth' });
    }
});
