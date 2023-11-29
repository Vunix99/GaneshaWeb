<?php
session_start(); // Memulai sesi

// Periksa apakah pengguna telah login
if (!isset($_SESSION['token'])) {
    // Jika pengguna belum login, arahkan kembali ke halaman login
    header("Location: index.php");
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
    header("Location: index.php"); // Arahkan kembali ke halaman login setelah logout
    exit();
}

?>

<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />

    <!-- Boxicons -->
    <link href="https://unpkg.com/boxicons@2.0.9/css/boxicons.min.css" rel="stylesheet" />
    <!-- Google fonts -->
    <link rel="preconnect" href="https://fonts.googleapis.com" />
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin />
    <link href="https://fonts.googleapis.com/css2?family=Poppins:ital,wght@0,100;0,200;0,300;0,400;0,500;0,600;0,700;0,800;0,900;1,100;1,200;1,300;1,400;1,500;1,600;1,700;1,800;1,900&display=swap" rel="stylesheet" />
    <!-- Using Font Awesome -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.2/css/all.min.css" integrity="sha512-z3gLpd7yknf1YoNbCzqRKc4qyor8gaKU1qmn+CShxbuBusANI9QpRohGBreCFkKxLhei6S9CQXFEbbKuqLg0DA==" crossorigin="anonymous" referrerpolicy="no-referrer" />
    <link rel="shortcut icon" href="images/logo-mini.png" type="image/x-icon" />
    <!-- My CSS -->
    <link rel="stylesheet" href="style/admin_dashboard.css" type="text/css" />
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/boxicons@latest/css/boxicons.min.css" />
    <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>

    <title>AdminDIUR</title>
</head>

<body>
    <!-- SIDEBAR -->
    <section id="sidebar">
        <a href="#" class="brand">
            <img src="images/logo-mini.png" class="img-logo" alt="" />
            <p class="text1">Admin<span class="sub-text">DIUR</span></p>
        </a>
        <ul class="side-menu top">
            <li class="active">
                <a href="#">
                    <img src="images/dashboard.png" class="logo" alt="" />
                    <span class="text">Dashboard</span>
                </a>
            </li>
            <li>
                <a href="admin_artikel.php?id=<?php echo $_GET['id'] ?? ''; ?>">
                    <img src="images/artikeladmin.png" class="logo" alt="" />
                    <span class="text">Artikel</span>
                </a>
            </li>
            <li>
                <a href="admin_resep.php?id=<?php echo $_GET['id'] ?? ''; ?>">
                    <img src="images/resepadmin.png" class="logo" alt="" />
                    <span class="text">Resep</span>
                </a>
            </li>
            <li>
                <a href="#" class="logout" id="logoutButton">
                    <i class="bx bxs-log-out-circle"></i>
                    <span class="text">Logout</span>
                </a>
            </li>
        </ul>
    </section>
    <!-- SIDEBAR -->

    <!-- CONTENT -->
    <section id="content">
        <!-- NAVBAR -->
        <nav>
            <i class="bx bx-menu"></i>
            <h3 class="profile">Welcome, <span id="admin_name"></span></h3>
        </nav>
        <!-- NAVBAR -->

        <!-- MAIN -->
        <main>
            <div class="head-title">
                <div class="left">
                    <h1>Dashboard</h1>
                    <ul class="breadcrumb">
                        <li>
                            <a href="#">Dashboard</a>
                        </li>
                        <li><i class="bx bx-chevron-right"></i></li>
                        <li>
                            <a class="active" href="#">Home</a>
                        </li>
                    </ul>
                </div>
            </div>
            <!-- <div class="button_postingan">
                <button><i class="bx bx-plus"></i>Buat Postingan</button>
            </div> -->

            <ul class="box-info">
                <li>
                    <i class="bx bxs-group"></i>
                    <span class="text">
                        <h3 id="totalUser">2834</h3>
                        <p>Jumlah Pelaku Diet</p>
                    </span>
                </li>
                <li>
                    <i class="bx bxs-bowl-rice"></i>
                    <span class="text">
                        <h3 id="totalResep">1020</h3>
                        <p>Jumlah Resep</p>
                    </span>
                </li>
                <li>
                    <i class="bx bxs-spreadsheet"></i>
                    <span class="text">
                        <h3 id="totalArtikel">2543</h3>
                        <p>Jumlah Artikel</p>
                    </span>
                </li>
            </ul>
        </main>
        <!-- MAIN -->
    </section>
    <!-- CONTENT -->


    <!-- end -->
    <?php
    // Replace this with your actual MongoDB endpoint URL
    $endpointUrl = 'https://asia-south1.gcp.data.mongodb-api.com/app/application-0-dlgbn/endpoint/getTotalData';

    // Set up cURL
    $ch = curl_init($endpointUrl);
    curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);

    // Execute cURL and get the response
    $response = curl_exec($ch);

    // Check for cURL errors and HTTP status code
    $httpCode = curl_getinfo($ch, CURLINFO_HTTP_CODE);
    if (curl_errno($ch) || $httpCode !== 200) {
        // Handle cURL error or non-200 HTTP status code
        echo '<script>';
        echo 'console.error("Error: ' . curl_error($ch) . ' (HTTP Code: ' . $httpCode . ')");';
        echo '</script>';
    } else {
        // Decode the response
        $result = json_decode($response, true);

        if ($result && isset($result['status']) && $result['status'] === 'success') {
            // Extract counts from the result
            $counts = $result['counts'] ?? [];

            // Embed counts into HTML using JavaScript
            echo '<script>';
            echo 'document.getElementById("totalUser").innerText = "' . $counts['orangDiet'] . '";';
            echo 'document.getElementById("totalResep").innerText = "' . $counts['resep'] . '";';
            echo 'document.getElementById("totalArtikel").innerText = "' . $counts['artikel'] . '";';
            echo '</script>';
        } else {
            // Handle the case when the status is not 'success'
            $errorMessage = $result['message'] ?? 'Terjadi kesalahan';
            echo '<script>';
            echo 'console.error("Error: ' . $errorMessage . '");';
            echo '</script>';
        }
    }

    // Close cURL
    curl_close($ch);

    // Endpoint URL untuk mendapatkan informasi admin berdasarkan decryptedId
    $adminEndpoint = 'https://asia-south1.gcp.data.mongodb-api.com/app/application-0-dlgbn/endpoint/getAdminById?id=' . $decryptedId;

    // Set up cURL
    $ch = curl_init($adminEndpoint);
    curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);

    // Execute cURL and get the response
    $adminResponse = curl_exec($ch);

    // Check for cURL errors and HTTP status code
    $adminHttpCode = curl_getinfo($ch, CURLINFO_HTTP_CODE);
    if (curl_errno($ch) || $adminHttpCode !== 200) {
        // Handle cURL error or non-200 HTTP status code
        echo '<script>';
        echo 'console.error("Error: ' . curl_error($ch) . ' (HTTP Code: ' . $adminHttpCode . ')");';
        echo '</script>';
    } else {
        // Decode the response
        $adminResult = json_decode($adminResponse, true);

        if ($adminResult && isset($adminResult['username'])) {
            // Set username to the JavaScript variable
            echo '<script>';
            echo 'var adminUsername = "' . $adminResult['username'] . '";';
            echo '</script>';
        } else {
            // Handle the case when the username is not available
            echo '<script>';
            echo 'console.error("Error: Username not available");';
            echo '</script>';
        }
    }

    // Close cURL
    curl_close($ch);
    ?>

    <script>
        document.addEventListener("DOMContentLoaded", function() {
            document.getElementById("admin_name").innerText = adminUsername;
            const logoutButton = document.getElementById("logoutButton");
            logoutButton.addEventListener("click", function(event) {
                event.preventDefault();
                Swal.fire({
                    title: 'Yakin ingin log out?',
                    showCancelButton: true,
                    confirmButtonText: `Ya, Log Out`,
                }).then((result) => {
                    if (result.isConfirmed) {
                        window.location.href = "admin_dashboard.php?logout=1"; // Tambahkan parameter logout
                    }
                });
            });
        });
    </script>

    <script src="js/admin_dashboard.js" type="text/javascript"></script>
</body>

</html>