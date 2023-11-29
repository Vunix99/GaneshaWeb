<?php
session_start(); // Memulai sesi

// Periksa apakah pengguna telah login
if (!isset($_SESSION['token'])) {
  // Jika pengguna belum login, arahkan kembali ke halaman login
  header("Location: login.php");
  exit();
}

// Fungsi untuk dekripsi ID
function decryptId($encryptedId)
{
  $iv = '12345678901' . str_repeat("\0", 5);
  $decryptedId = openssl_decrypt(base64_decode($encryptedId), 'aes-128-cbc', 'secret_key', 0, $iv);
  return $decryptedId;
}

// Memeriksa apakah pengguna telah login
if (isset($_GET['id'])) {
  $decryptedId = decryptId($_GET['id'] ?? '');

  // Jika ID berhasil didekripsi, atur variabel sesi
  if ($decryptedId) {
    $_SESSION['id'] = $decryptedId;
  }
}

// Mengatur tindakan saat logout
if (isset($_GET['logout'])) {
  session_destroy(); // Menghapus semua data sesi
  header("Location: login.php"); // Arahkan kembali ke halaman login setelah logout
  exit();
}
?>


<!DOCTYPE html>
<html lang="en">

<head>
  <meta charset="UTF-8" />
  <meta name="viewport" content="width=device-width, initial-scale=1.0" />
  <title>Assistant - For your diet</title>
  <link rel="stylesheet" href="style/kalkustyle.css" type="text/css" media="all" />
  <!-- Google fonts -->
  <link rel="preconnect" href="https://fonts.googleapis.com" />
  <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin />
  <link
    href="https://fonts.googleapis.com/css2?family=Poppins:ital,wght@0,100;0,200;0,300;0,400;0,500;0,600;0,700;0,800;0,900;1,100;1,200;1,300;1,400;1,500;1,600;1,700;1,800;1,900&display=swap"
    rel="stylesheet" />
  <!-- Using Font Awesome -->
  <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.2/css/all.min.css"
    integrity="sha512-z3gLpd7yknf1YoNbCzqRKc4qyor8gaKU1qmn+CShxbuBusANI9QpRohGBreCFkKxLhei6S9CQXFEbbKuqLg0DA=="
    crossorigin="anonymous" referrerpolicy="no-referrer" />
  <link rel="shortcut icon" href="images/logo-mini.png" type="image/x-icon" />
  <!-- AOS -->
  <link rel="stylesheet" href="https://unpkg.com/aos@next/dist/aos.css" />
  <!-- Using boxicons -->
  <link href="https://unpkg.com/boxicons@2.1.4/css/boxicons.min.css" rel="stylesheet" />
  <script src="https://cdnjs.cloudflare.com/ajax/libs/crypto-js/4.0.0/crypto-js.min.js"></script>
  <!---Sweet Alert-->
  <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>



</head>

<body>
  <!-- Header -->
  <header id="navbar">
    <a href="kalku.php?id=<?php echo $_GET['id'] ?? ''; ?>"><img class="logo" src="images/logo-med-1.png"
        alt="Logo Diur" width="70px" height="auto" /></a>
    <div class="burger-menu">
      <div class="burger-icon">
        <div class="bar"></div>
        <div class="bar"></div>
        <div class="bar"></div>
      </div>
    </div>
    <nav>
      <ul class="nav">
        <li><a href="utama.php?id=<?php echo $_GET['id'] ?? ''; ?>">Home</a></li>
        <li><a href="resep.php?id=<?php echo $_GET['id'] ?? ''; ?>">Resep</a></li>
        <li><a href="artikel.php?id=<?php echo $_GET['id'] ?? ''; ?>">Artikel</a></li>
        <li><a href="kalku.php?id=<?php echo $_GET['id'] ?? ''; ?>">Kalkulator</a></li>
      </ul>
    </nav>
    <!--user profile--------------------->
    <div class="profile-pengguna">
      <div class="action">
        <img src="images/user1.png" />
      </div>
      <div class="pengguna">
        <div class="profile">
          <img src="images/user.png" />
          <div class="info">
            <h2 id="username-placeholder">User</h2>
          </div>
        </div>
        <a href="#" class="btn-user"> </a>
        <ul>
          <li>
            <img src="images/edit.png" />
            <a href="edit_profile.php?id=<?php echo $_GET['id'] ?? ''; ?>">Edit profile</a>
          </li>
          <li>
            <img src="images/History.png" />
            <a href="riwayat.php?id=<?php echo $_GET['id'] ?? ''; ?>">Riwayat</a>
          </li>
          <li>
            <img src="images/logout.png" />
            <a href="#" id="logoutButton">Log out</a>
          </li>
        </ul>
      </div>
    </div>
  </header>
  <!--nav-end--------------------->
  <!--parallax--------------------->
  <div class="bckg">
    <!--Content before waves-->
    <div class="inner-bckg2">
    </div>
    <!--Waves Container-->
    <div>
      <svg class="waves" xmlns="http://www.w3.org/2000/svg" xmlns:xlink="http://www.w3.org/1999/xlink"
        viewBox="0 24 150 28" preserveAspectRatio="none" shape-rendering="auto">
        <defs>
          <path id="gentle-wave" d="M-160 44c30 0 58-18 88-18s 58 18 88 18 58-18 88-18 58 18 88 18 v44h-352z" />
        </defs>
        <g class="parallax">
          <use xlink:href="#gentle-wave" x="48" y="0" fill="rgba(255,255,255,0.7)" />
          <use xlink:href="#gentle-wave" x="48" y="3" fill="rgba(255,255,255,0.5)" />
          <use xlink:href="#gentle-wave" x="48" y="5" fill="rgba(255,255,255,0.3)" />
          <use xlink:href="#gentle-wave" x="48" y="7" fill="#fff" />
        </g>
      </svg>
    </div>
  </div>
  <!--Waves end-->


  <!-------Kalkulator kalori ideal--------->
  <div class="calcu-title" data-aos="fade-down" data-aos-easing="linear" data-aos-duration="500">
    <h3 style="color: #45a049;">Kalkulator</h3>
    <div class="container-kalku">
      <button class="active-button" id="showKaloriButton">Kalori Ideal</button>
      <button class="inactive-button" id="showBBButton">Berat Badan Ideal</button>
    </div>
  </div>
  <div class="container-kalkulator-kalori">
    <div class="cards-container">
      <div class="card card-kiri" data-aos="fade-right" data-aos-easing="linear" data-aos-duration="500">
        <h3>Form Kalkulator Kalori</h3>
        <form action="#" class="form-kalkulator">
          <div class="form-group">
            <label for="usia">Usia:</label>
            <input type="number" id="usia" name="usia" required min="1">
          </div>
          <div class="form-group">
            <label for="berat-badan">Berat Badan (kg):</label>
            <input type="number" id="berat-badan" name="berat-badan" required>
          </div>
          <div class="form-group">
            <label for="tinggi-badan">Tinggi Badan (cm):</label>
            <input type="number" id="tinggi-badan" name="tinggi-badan" required>
          </div>
          <div class="form-group">
            <label for="jenis-kelamin">Jenis Kelamin:</label>
            <select id="jenis-kelamin" name="jenis-kelamin" required>
              <option value="laki-laki">Laki-laki</option>
              <option value="perempuan">Perempuan</option>
            </select>
          </div>
          <div class="form-group">
            <label for="level-aktivitas">Level Aktivitas:</label>
            <select id="level-aktivitas" name="level-aktivitas" required>
              <option value="Jarang">Jarang</option>
              <option value="Ringan">Ringan</option>
              <option value="Sedang">Sedang</option>
              <option value="Aktif">Aktif</option>
              <option value="Sangat-Aktif">Sangat Aktif</option>
            </select>
          </div>
          <button type="submit"
            style="padding: 10px 20px; background-color: #45a049; color: white; border: none; border-radius: 5px; cursor: pointer; transition: background-color 0.3s ease;"
            onmouseover="this.style.backgroundColor='#128512'" onmouseout="this.style.backgroundColor='#45a049'">
            Hitung Kalori Ideal
          </button>

        </form>
      </div>
      <div class="card card-kanan" data-aos="fade-left" data-aos-easing="linear" data-aos-duration="500">
        <h3>Hasil Perhitungan</h3>
        <div class="hasil-kalori">
          <p>Kalori Ideal = <span class="nilai-perhitungan-kalori" style="color: #45a049;">Nilai Perhitungan</span></p>
        </div>
      </div>
    </div>
  </div>


  <!-- Form Kalkulator Berat Badan Ideal -->
  <div class="container-kalkulator-bb">
    <div class="cards-container">
      <div class="card card-kiri" data-aos="fade-right" data-aos-easing="linear" data-aos-duration="500">
        <h3>Form Kalkulator Berat Badan Ideal</h3>
        <form action="#" class="form-kalkulator" id="formBB">
          <!-- Ganti "Usia" dengan "Kelamin" -->
          <div class="form-group">
            <label for="kelamin-bb">Kelamin:</label>
            <select id="kelamin-bb" name="kelamin-bb" required>
              <option value="pria">Pria</option>
              <option value="wanita">Wanita</option>
            </select>
          </div>
          <!-- Tetap gunakan "Tinggi Badan" -->
          <div class="form-group">
            <label for="tinggi-badan-bb">Tinggi Badan (cm):</label>
            <input type="number" id="tinggi-badan-bb" name="tinggi-badan-bb" required>
          </div>
          <button type="submit"
            style="padding: 10px 20px; background-color: #45a049; color: white; border: none; border-radius: 5px; cursor: pointer; transition: background-color 0.3s ease;"
            onmouseover="this.style.backgroundColor='#128512'" onmouseout="this.style.backgroundColor='#45a049'">
            Hitung Berat Badan Ideal
          </button>

        </form>
      </div>
      <div class="card card-kanan" data-aos="fade-left" data-aos-easing="linear" data-aos-duration="500">
        <h3>Hasil Perhitungan</h3>
        <div class="hasil-kalori">
          <p>Berat Badan Ideal = <span class="nilai-perhitungan-bb" style="color: #45a049;">Nilai Perhitungan</span></p>
        </div>
      </div>
    </div>
  </div>


  <!-- Footer -->
  <footer id="footer">
    <div class="container">
      <div class="content_footer">
        <div class="profil">
          <div class="logo_area">
            <img src="images/logo-med.png" alt="" class="logo_diur" />
          </div>
          <div class="desc_area">
            <p>
              Aplikasi yang ditargetkan untuk kamu yang ingin diet sayuran
              dengan memperhatikan asupan kalori harian, dan untuk menambah
              informasi terkait sayuran ataupun diet
            </p>
          </div>
          <div class="social_media">
            <a href="https://www.instagram.com/diurbyganesha/"
              style="text-decoration: none; transition: transform 0.3s ease-in-out;"
              onmouseover="this.style.transform='scale(1.2)'" onmouseout="this.style.transform='scale(1)'">
              <i class="fab fa-instagram fa-bounce"></i>
            </a>
          </div>
        </div>
      </div>
      <hr />
      <div class="footer_bottom">
        <div class="copy_right">
          <i class="bx bxs-copyright"></i>
          <span>2023 GANESHA</span>
        </div>
        <div class="tou">
          <a href="#">Term of Use</a>
          <a href="#">Privacy Police</a>
          <a href="#">Cookie</a>
        </div>
      </div>
    </div>
  </footer>

  <!-- Footer end -->
  <!-- jQuery CDN Link -->
  <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.6.0/jquery.min.js"></script>
  <script src="https://cdnjs.cloudflare.com/ajax/libs/crypto-js/4.0.0/crypto-js.min.js"></script>
  <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
  <script>
    document.addEventListener('DOMContentLoaded', function () {
      const showKaloriButton = document.getElementById('showKaloriButton');
      const showBBButton = document.getElementById('showBBButton');
      const containerKalkulatorKalori = document.querySelector('.container-kalkulator-kalori');
      const containerKalkulatorBB = document.querySelector('.container-kalkulator-bb');

      showKaloriButton.addEventListener('click', function () {
        containerKalkulatorBB.style.display = 'none';
        containerKalkulatorKalori.style.display = 'flex';
        showBBButton.classList.remove('active-button-2');
        showBBButton.classList.add('inactive-button');
        showKaloriButton.classList.remove('inactive-button-2');
        showKaloriButton.classList.add('active-button');
      });

      showBBButton.addEventListener('click', function () {
        containerKalkulatorKalori.style.display = 'none';
        containerKalkulatorBB.style.display = 'flex';
        showKaloriButton.classList.remove('active-button');
        showKaloriButton.classList.add('inactive-button-2');
        showBBButton.classList.remove('inactive-button');
        showBBButton.classList.add('active-button-2');
      });

      const formBB = document.getElementById('formBB');
      formBB.addEventListener('submit', function (e) {
        e.preventDefault();

        const kelaminBB = document.getElementById('kelamin-bb').value;
        const tinggiBB = parseInt(document.getElementById('tinggi-badan-bb').value);

        // Menggunakan rumus Broca untuk menghitung berat badan ideal
        let faktorPenurunan = kelaminBB === 'pria' ? 0.10 : 0.15;
        let beratIdeal = (tinggiBB - 100) - ((tinggiBB - 100) * faktorPenurunan);

        // Menampilkan hasil perhitungan
        const nilaiPerhitunganBB = document.querySelector('.container-kalkulator-bb .nilai-perhitungan-bb');
        nilaiPerhitunganBB.textContent = `${beratIdeal.toFixed(2)} kg`;
      });
    });





    document.addEventListener('DOMContentLoaded', function () {
      var encryptedId = "<?php echo $_GET['id'] ?? ''; ?>";
      var decryptedId = "<?php echo $decryptedId; ?>";

      if (decryptedId) {
        var url = 'https://asia-south1.gcp.data.mongodb-api.com/app/application-0-dlgbn/endpoint/getUserProfile?id=' + decryptedId;
        fetch(url)
          .then(response => response.json())
          .then(data => {
            document.getElementById('username-placeholder').innerHTML = data.username;
          })
          .catch(error => console.error('Error:', error));
      }
    });
    const form = document.querySelector('.form-kalkulator');
    form.addEventListener('submit', function (e) {
      e.preventDefault();

      const usia = parseInt(document.getElementById('usia').value);
      const tinggi = parseInt(document.getElementById('tinggi-badan').value);
      const berat = parseInt(document.getElementById('berat-badan').value);
      const gender = document.getElementById('jenis-kelamin').value;
      const levelAktivitas = document.getElementById('level-aktivitas').value;

      let bmr;
      if (gender === 'laki-laki') {
        bmr = 66 + 13.7 * berat + 5 * tinggi - 6.8 * usia;
      } else {
        bmr = 655 + 9.6 * berat + 1.8 * tinggi - 4.7 * usia;
      }

      // Menerapkan faktor level aktivitas
      if (levelAktivitas === 'Jarang') {
        bmr *= 1.2;
      } else if (levelAktivitas === 'Ringan') {
        bmr *= 1.375;
      } else if (levelAktivitas === 'Sedang') {
        bmr *= 1.55;
      } else if (levelAktivitas === 'Aktif') {
        bmr *= 1.725;
      } else if (levelAktivitas === 'Sangat-Aktif') {
        bmr *= 1.9;
      }

      bmr = Math.round(bmr);

      // Menampilkan hasil perhitungan
      const nilaiPerhitungan = document.querySelector('.nilai-perhitungan-kalori');
      nilaiPerhitungan.textContent = `${bmr} kalori/hari`;
    });

    document.addEventListener("DOMContentLoaded", function () {
      const logoutButton = document.getElementById("logoutButton");
      logoutButton.addEventListener("click", function (event) {
        event.preventDefault();
        Swal.fire({
          title: 'Yakin ingin log out?',
          showCancelButton: true,
          confirmButtonText: `Ya, Log Out`,
        }).then((result) => {
          if (result.isConfirmed) {
            window.location.href = "utama.php?logout=1"; // Tambahkan parameter logout
          }
        });
      });
    });
  </script>
  <script src="https://cdnjs.cloudflare.com/ajax/libs/crypto-js/4.0.0/crypto-js.min.js"></script>
  <script src="js/script.js" type="text/javascript"></script>
  <script src="js/calory.js" type="text/javascript"></script>
  <script src="js/getAllArtikel.js" type="text/javascript"></script>
  <script src="https://unpkg.com/aos@next/dist/aos.js"></script>
  <script>
    AOS.init();
  </script>
</body>

</html>