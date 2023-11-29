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
  <link rel="stylesheet" href="style/ustyle.css" type="text/css" media="all" />
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
  <style>
    body {
      overflow-x: hidden;
    }
  </style>

</head>

<body>
  <!-- Header -->
  <header id="navbar">
    <a href="utama.php?id=<?php echo $_GET['id'] ?? ''; ?>"><img class="logo" src="images/logo-med-1.png"
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
    <div class="inner-bckg flex">
      <img src="images/bg-1.png" class="bg-1" alt="bg" />
      <div class="search-banner-text">
        <h1>Nurturing your Diet , Transforming your Future</h1>
        <strong>#DietSayur</strong>
        <!-- search-box -->
        <form action="artikel_filtered.php" method="GET" class="search-box" onsubmit="return redirect();">

          <!-- icon -->
          <i class="fas fa-search"></i>
          <!-- input -->
          <input type="text" id="search-input" class="search-input" placeholder="Search article about diet"
            name="search" required />

          <!-- btn -->
          <input type="submit" class="search-btn" value="Search" />
        </form>
      </div>
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
  <section id="cprogress">
    <!-- Heading -->
    <div class="cprogress-heading" data-aos="fade-down" data-aos-easing="linear" data-aos-duration="500">
      <h2>Progress <span style="color: #4eb060">Kalori-mu</span></h2>
    </div>
  </section>
  <div class="progress-container">
    <!-- Card Progress -->
    <div class="progress-card" data-aos="fade-up">
      <div class="container-progress">
        <div class="circular-progress" data-aos="zoom-in"
          style="background: conic-gradient(#7d2ae8 0deg, #ededed 0deg);">
          <span class="progress-value" id="progress-value">0%</span>
        </div>
        <h2>Kalori Ideal : <span class="text" id="kalori_ideal"></span></h2>
        <h3 style="margin-top: 5px;">Kalori yang masuk : <span class="text-input-kalori" id="input_kalori"></span><span
            style="color: #4EB060;"> kcal</span></h3>
        <span class="text-status-kalori" style="color: black;" id="status_kalori"></span>
      </div>
    </div>
    <!-- Card Profile -->
    <div class="profile-card" data-aos="zoom-in">
      <div class="container-detail-profile">
        <h3 style="font-weight: bold;">Profil-mu</h3>
        <h3>Usia : <span id="usia"></span></h3>
        <h3>Jenis kelamin : <span id="jenis_kelamin"></span></h3>
        <h3>Tinggi badan : <span id="tinggi_badan"></span></h3>
        <h3>Berat badan : <span id="berat_badan"></span></h3>
        <h3>Level Aktivitas : <span id="level_aktivitas"></span></h3>
        <button type="edit-profile"><a href="edit_profile.php?id=<?php echo $_GET['id'] ?? ''; ?>">Edit
            Profile</a></button>
      </div>
    </div>
  </div>

  <!-----------Card Fitur----------->
  <!-- Container untuk card -->
  <div class="card-container">
    <!-- Card 1 -->
    <div class="card" data-aos="fade-up">
      <a href="resep.php?id=<?php echo $_GET['id'] ?? ''; ?>">
        <img src="images/button_resep.png" alt="Image Resep" class="card-image" />
        <p class="card-text">Resep</p>
      </a>
    </div>
    <!-- Card 2 -->
    <div class="card" data-aos="fade-up">
      <a href="artikel.php?id=<?php echo $_GET['id'] ?? ''; ?>">
        <img src="images/button_artikel.png" alt="Image artikel" class="card-image" />
        <p class="card-text">Artikel</p>
      </a>
    </div>
    <!-- Card 3 -->
    <div class="card" data-aos="fade-up">
      <a href="kalku.php?id=<?php echo $_GET['id'] ?? ''; ?>">
        <img src="images/button_kalku.png" alt="Image kalku" class="card-image" />
        <p class="card-text">Kalkulator</p>
      </a>
    </div>
  </div>

  <!-- Article -->
  <section id="recent-article">
    <!-- heading -->
    <div class="article-heading" data-aos="fade-down" data-aos-easing="linear" data-aos-duration="500">
      <h2>Recent Article</h2>
      <a href="artikel.php?id=<?php echo $_GET['id'] ?? ''; ?>"><span>All</span></a>
    </div>
    <!-- box-container -->
    <div class="article-container" data-aos="fade-down" data-aos-easing="linear" data-aos-duration="500">
      <!-- box -->
      <div class="article-card">
        <img src="" alt="Ini Gambar Artikel" />
        <div class="card-content">
          <h5 class="card-title">
            Ini adalah judul artikel
          </h5>
          <p class="card-text-article">
            Ini isian spoiler artikel
          </p>
          <a href="#" class="read-more"><i class="fas fa-arrow-right"></i> Baca selengkapnya</a>
        </div>
      </div>
    </div>
  </section>
  <!-- Article section end -->


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
    var encryptedId = "<?php echo $_GET['id'] ?? ''; ?>";
    const decryptedId = "<?php echo $decryptedId; ?>";


    function redirect() {
      var keywordInput = document.getElementById("search-input");
      if (keywordInput) {
        var keyword = keywordInput.value;
        console.log("Keyword:", keyword);
        window.location.href = "artikel_filtered.php?id=" + encryptedId + "&keyword=" + encodeURIComponent(keyword);
      } else {
        console.error("Element with ID 'search-input' not found.");
      }
      return false;
    }



    document.addEventListener('DOMContentLoaded', function () {
      if (decryptedId) {
        var url = 'https://asia-south1.gcp.data.mongodb-api.com/app/application-0-dlgbn/endpoint/getUserProfile?id=' + decryptedId;
        fetch(url)
          .then(response => response.json())
          .then(data => {
            document.getElementById('username-placeholder').innerHTML = data.username;
            document.getElementById('usia').innerHTML = data.usia;
            document.getElementById('jenis_kelamin').innerHTML = data.jenis_kelamin;
            document.getElementById('tinggi_badan').innerHTML = data.tinggi_badan;
            document.getElementById('berat_badan').innerHTML = data.berat_badan;
            document.getElementById('level_aktivitas').innerHTML = data.level_aktivitas;

            const usia = parseInt(data.usia);
            const tinggi = parseInt(data.tinggi_badan);
            const berat = parseInt(data.berat_badan);
            const gender = data.jenis_kelamin.toLowerCase();
            const level_aktivitas = data.level_aktivitas;
            let bmr;

            //ini blm dikalikan dengan level aktivitas
            if (gender === 'laki-laki') {
              bmr = 66 + 13.7 * berat + 5 * tinggi - 6.8 * usia;
            } else {
              bmr = 655 + 9.6 * berat + 1.8 * tinggi - 4.7 * usia;
            }

            //Dikali dengan level aktivitas
            if (level_aktivitas === 'Jarang') {
              bmr *= 1.2;
            } else if (level_aktivitas === 'Ringan') {
              bmr *= 1.375;
            } else if (level_aktivitas === 'Sedang') {
              bmr *= 1.55;
            } else if (level_aktivitas === 'Aktif') {
              bmr *= 1.725;
            } else if (level_aktivitas === 'Sangat-Aktif') {
              bmr *= 1.9;
            }

            bmr = Math.round(bmr);
            document.getElementById('kalori_ideal').textContent = `${bmr} kalori/hari`;
          })
          .catch(error => console.error('Error:', error));
      }
    });

    //Progress Bar
    document.addEventListener('DOMContentLoaded', function () {
      const url = `https://asia-south1.gcp.data.mongodb-api.com/app/application-0-dlgbn/endpoint/getConsumeById?id_orangDiet=${decryptedId}`;
      fetch(url)
        .then(response => response.json())
        .then(data => {
          if (data.success) {
            const totalKalori = data.total_kalori;
            const kaloriIdeal = parseInt(document.getElementById('kalori_ideal').textContent);

            // Menghitung persentase kalori
            let persentaseKalori = (totalKalori / kaloriIdeal) * 100;
            persentaseKalori = persentaseKalori.toFixed(2); // Bulatkan menjadi dua angka desimal

            // Mengubah nilai dari ID progress-value
            document.querySelector('.progress-value').textContent = `${persentaseKalori}%`;

            // Mengubah style conic-gradient
            const conicGradientValue = `${persentaseKalori * 3.6}deg`;
            const circularProgress = document.querySelector('.circular-progress');
            circularProgress.style.background = `conic-gradient(#006A70 0deg, #ededed ${conicGradientValue})`;

            // Memperbarui nilai dari span dengan id input_kalori
            document.getElementById('input_kalori').textContent = totalKalori;

            //Ubah Status Kalori
            let statusKalori;
            const statusKaloriElement = document.getElementById('status_kalori');
            if (persentaseKalori >= 90) {
              statusKalori = 'Terpenuhi';
              statusKaloriElement.style.color = 'darkgreen';
            } else if (persentaseKalori >= 70 && persentaseKalori < 90) {
              statusKalori = 'Cukup';
              statusKaloriElement.style.color = 'green';
            } else if (persentaseKalori >= 50 && persentaseKalori < 70) {
              statusKalori = 'Kurang';
              statusKaloriElement.style.color = 'orange';
            } else {
              statusKalori = 'Sangat Kekurangan';
              statusKaloriElement.style.color = 'red';
            }
            statusKaloriElement.textContent = ` (${statusKalori})`;
          } else {
            console.error('Failed to fetch data:', data.message);
          }
        })
        .catch(error => console.error('Error:', error));
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
  <script src="js/getRecentArtikel.js" type="text/javascript"></script>
  <script src="https://unpkg.com/aos@next/dist/aos.js"></script>
  <script>
    AOS.init();
  </script>
</body>

</html>