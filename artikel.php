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
  <title>All Article - For your diet</title>
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
  <!---Sweet Alert-->
  <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>

  <style>
    .container-article {
      width: 80%;
      /* Sesuaikan lebar container sesuai kebutuhan */
    }


    .form-control {
      width: 600px;
      /* Sesuaikan lebar input sesuai kebutuhan */
    }

    main {
      text-align: center;
    }

    .article-card {
      margin: 10px auto;
      /* Perataan tengah */
      width: 300px;
      /* Sesuaikan lebar kartu sesuai kebutuhan */
      display: flex;
      flex-direction: column;
    }

    .card-content {
      flex-grow: 1;
      /* Agar konten fleksibel mengisi sisa ruang dalam kartu */
    }

    .read-more {
      margin-top: auto;
      /* Menempatkan tombol di bagian bawah kartu */
    }

    .input-group {
      margin: 30px auto;
    }

    @media (max-width: 300px) {
      .form-control {
        max-width: 150px;
        /* Sesuaikan lebar input sesuai kebutuhan */
      }

      .article-card {
        width: 200px;
      }
    }
  </style>

</head>

<body>
  <!-- Header -->
  <header id="navbar">
    <a href="artikel.php?id=<?php echo $_GET['id'] ?? ''; ?>"><img class="logo" src="images/logo-med-1.png"
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
    <div class="inner-bckg2 flex">
    </div>
    <!--Waves Container-->
    <div>
      <svg class="waves" xmlns="http://www.w3.org/2000/svg" xmlns:xlink="http://www.w3.org/1999/xlink"
        viewBox="0 24 150 28" preserveAspectRatio="none" shape-rendering="auto">
        <defs>
          <path id="gentle-wave" d="M-160 44c30 0 58-18 88-18s 58 18 88 18 58-18 88-18 58 18 88 18 v44h-352z" />
        </defs>
        <g class="parallax">
          <use xlink:href="#gentle-wave" x="48" y="0" fill="rgba(255,255,255,0.7" />
          <use xlink:href="#gentle-wave" x="48" y="3" fill="rgba(255,255,255,0.5)" />
          <use xlink:href="#gentle-wave" x="48" y="5" fill="rgba(255,255,255,0.3)" />
          <use xlink:href="#gentle-wave" x="48" y="7" fill="#fff" />
        </g>
      </svg>
    </div>
  </div>
  <!--Waves end-->
  <div class="container-article">
    <main>
      <!-- Move the search box here -->
      <div class="search-box">
        <div class="input-group">
          <input type="text" class="form-control" id="search-input" placeholder="Search article about diet"
            aria-label="Search article about diet" />
          <button class="btn btn-search" type="button" id="search-btn">
            <i class="fa fa-search"></i>
          </button>
        </div>
      </div>

      <section id="popular-article">
        <!-- heading -->
        <div class="article-heading" data-aos="fade-down" data-aos-easing="linear" data-aos-duration="500">
          <span id="objek">All</span>
        </div>
        <!-- box-container -->
        <div class="article-container" data-aos="fade-down" data-aos-easing="linear" data-aos-duration="500">
          <!-- box -->
          <div class="article-card" id="article-card-1" style="display: flex; flex-direction:column;">
            <img src="" alt="Card Image" id="gambarArtikel1" />
            <div class="card-content">
              <h5 class="card-title" id="judulArtikel1"></h5>
              <a href="#" class="read-more" id="readMoreLink1" style="margin-top: 10px;"><i
                  class="fas fa-arrow-right"></i> Read more</a>
            </div>
          </div>
          <!-- Tambahkan lebih banyak kartu di sini -->
        </div>
      </section>
    </main>
  </div>

  <!-- Tambahkan div untuk pagination -->
  <div class="pagination" id="pagination">
    <!-- Link pagination akan ditambahkan secara dinamis menggunakan JavaScript -->
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
  <script src="js/script.js" type="text/javascript"></script>
  <script src="js/getAllArtikel.js" type="text/javascript"></script>
  <script src="https://unpkg.com/aos@next/dist/aos.js"></script>
  <script>
    AOS.init();
  </script>
  <script>
    var encryptedId = "<?php echo $_GET['id'] ?? ''; ?>";
    const decryptedId = "<?php echo $decryptedId; ?>";


    const searchBtn = document.getElementById("search-btn");

    searchBtn.addEventListener("click", function () {
      const keyword = document.getElementById("search-input").value;

      // Panggil fungsi redirect dengan nilai input dari keyword
      redirect(keyword);
    });

    function redirect(keyword) {
      if (keyword) {
        console.log("Keyword:", keyword);
        window.location.href = "artikel_filtered.php?id=" + encryptedId + "&keyword=" + encodeURIComponent(keyword);
      } else {
        console.error("Element with ID 'search-input' not found.");
      }
      return false;
    }

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

    //ambil username
    document.addEventListener('DOMContentLoaded', function () {

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
  </script>

</body>

</html>