document.addEventListener('DOMContentLoaded', function () {
    // Mendapatkan decryptedId dari URL
    const urlParams = new URLSearchParams(window.location.search);
    const id_artikel = urlParams.get('id_artikel');

    // Mengeksekusi panggilan ke endpoint MongoDB Realm
    const endpointUrl = 'https://asia-south1.gcp.data.mongodb-api.com/app/application-0-dlgbn/endpoint/getArtikelById';
    
    fetch(`${endpointUrl}?id=${id_artikel}`)
        .then(response => response.json())
        .then(data => {
            // Lakukan sesuatu dengan data yang diterima, misalnya, mengisi elemen HTML
            const gambarArtikelElement = document.getElementById('gambarArtikel');
            gambarArtikelElement.src = data.gambar_artikel;

            const judulElement = document.getElementById('judulArtikel');
            judulElement.textContent = data.judul;

            const isiArtikelElement = document.getElementById('isiArtikel');
            isiArtikelElement.textContent = data.isi_artikel;

            const tanggalTerbitElement = document.getElementById('tanggalTerbit');
            tanggalTerbitElement.textContent = data.tanggal_terbit;

            const sumberElement = document.getElementById('sumber');
            sumberElement.textContent = data.sumber;
        })
        .catch(error => console.error('Error:', error));
});
