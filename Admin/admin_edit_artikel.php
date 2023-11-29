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
            <li>
                <a href="admin_dashboard.php?id=<?php echo $_GET['id'] ?? ''; ?>">
                    <img src="images/dashboard.png" class="logo" alt="" />
                    <span class="text">Dashboard</span>
                </a>
            </li>
            <li class="active">
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
    <!-- end--->

    <section id="content">
        <nav>
            <i class="bx bx-menu"></i>
        </nav>
        <div class="content2">
            <div class="card-artikel">
                <h2>Edit artikel</h2>
            </div>
        </div>
    </section>
    <section id="content">
        <form class="postingan" id="artikelForm" method="put">
            <div class="card-artikel">
                <div class="artikel-title">
                    <input type="text" id="judul" name="judul" placeholder="Judul Artikel" required />
                </div>
            </div>

            <div class="card-artikel">
                <div class="artikel-title">
                    <input type="url" id="gambar_artikel" name="gambar_artikel" placeholder="Masukkan link gambar..." required />
                </div>
            </div>

            <div class="card-artikel">
                <div class="artikel-title">
                    <textarea id="isi_artikel" name="isi_artikel" placeholder="Isi Artikel" required></textarea>
                </div>
            </div>

            <div class="card-artikel">
                <div class="artikel-title">
                    <input type="text" id="sumber" name="sumber" placeholder="Sumber Artikel" required />
                </div>
            </div>

            <div class="card-artikel" style="margin-bottom: 5px;">
                <div class="artikel-title">
                    <input type="text" id="id_artikel" name="id_artikel" placeholder="ID Artikel" required />
                </div>
            </div>

            <div class="kanan">
                <div class="button-artikel" style="margin-top:10px;">
                    <div class="buttons" style="flex-direction: row; margin-bottom:1rem;">
                        <button type="submit" class="btn-simpan">
                            <img src="images/floppy-disk.png" alt="Edit Icon" class="button-icon" />
                            <p>Simpan</p>
                        </button>
                        <button type="reset" class="btn-batal" style="margin-left: 1.5rem;">
                            <img src="images/delete-left.png" alt="Delete Icon" class="button-icon" />
                            <p>Batal</p>
                        </button>
                    </div>
                </div>
            </div>

        </form>
    </section>
    <?php
    // Check if id_artikel parameter is set in the URL
    if (isset($_GET['id_artikel'])) {
        // Get the articleId from the URL parameter
        $articleId = $_GET['id_artikel'];

        // Create the URL endpoint for fetching article data
        $fetchEndpoint = 'https://asia-south1.gcp.data.mongodb-api.com/app/application-0-dlgbn/endpoint/getArtikelById?id=' . $articleId;

        // Set up cURL for fetching data
        $ch = curl_init($fetchEndpoint);
        curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);

        // Execute cURL and get the response
        $response = curl_exec($ch);

        // Check for errors
        if (curl_errno($ch)) {
            echo '<script>';
            echo 'Swal.fire({
                icon: "error",
                title: "Error",
                text: "Error: ' . curl_error($ch) . '"
            });';
            echo '</script>';
        } else {
            // Decode the response
            $result = json_decode($response, true);

            if ($result && !isset($result['error'])) {
                // Populate form fields with retrieved data
                echo '<script>';
                echo 'document.getElementById("judul").value = "' . htmlspecialchars($result['judul'], ENT_QUOTES, 'UTF-8') . '";';
                echo 'document.getElementById("gambar_artikel").value = "' . htmlspecialchars($result['gambar_artikel'], ENT_QUOTES, 'UTF-8') . '";';

                // Handle special characters and line breaks in isi_artikel
                $isiArtikelValue = str_replace(["\r\n", "\r", "\n"], '\n', $result['isi_artikel']);
                $isiArtikelValue = htmlspecialchars_decode($isiArtikelValue, ENT_QUOTES);
                $isiArtikelValue = str_replace(["\r\n", "\r", "\n"], '<br>', $isiArtikelValue);
                echo 'document.getElementById("isi_artikel").value = `' . $isiArtikelValue . '`;';

                // Sanitize sumber data to avoid injecting HTML tags into JavaScript
                $sumberValue = htmlspecialchars($result['sumber'], ENT_QUOTES, 'UTF-8');
                echo 'document.getElementById("sumber").value = "' . $sumberValue . '";';

                // Check if the 'id_artikel' key exists in the $result array
                if (isset($_GET['id_artikel'])) {
                    // Get the articleId from the URL parameter
                    $articleId = $_GET['id_artikel'];

                    // Set the value of "id_artikel" directly from the URL parameter
                    echo 'document.getElementById("id_artikel").value = "' . htmlspecialchars($articleId, ENT_QUOTES, 'UTF-8') . '";';
                }

                echo '</script>';
            } else {
                // Handle error
                $errorMessage = $result['error'] ?? 'Terjadi kesalahan';
                echo '<script>';
                echo 'Swal.fire({
                    icon: "error",
                    title: "Gagal Mengambil Data Artikel",
                    text: "' . $errorMessage . '"
                });';
                echo '</script>';
            }
        }
    }
    ?>


    <script>
        var encryptedId = "<?php echo $_GET['id'] ?? ''; ?>";
        const decryptedId = "<?php echo $decryptedId; ?>";

        document.addEventListener('DOMContentLoaded', function() {
            const form = document.getElementById('artikelForm');

            form.addEventListener('submit', async function(event) {
                event.preventDefault();

                const judul = document.getElementById('judul').value;
                const gambar_artikel = document.getElementById('gambar_artikel').value;
                const isi_artikel = document.getElementById('isi_artikel').value;
                const sumber = document.getElementById('sumber').value;
                const id_artikel = document.getElementById('id_artikel').value;

                const endpoint = 'https://asia-south1.gcp.data.mongodb-api.com/app/application-0-dlgbn/endpoint/updateArtikel';

                try {
                    const response = await fetch(endpoint, {
                        method: 'PUT',
                        headers: {
                            'Content-Type': 'application/json',
                        },
                        body: JSON.stringify({
                            id_artikel: id_artikel,
                            judul: judul,
                            gambar_artikel: gambar_artikel,
                            isi_artikel: isi_artikel,
                            sumber: sumber
                        }),
                    });

                    if (!response.ok) {
                        throw new Error(`HTTP error! Status: ${response.status}`);
                    }

                    const result = await response.json();

                    if (result.status === 'success') {
                        Swal.fire({
                            icon: 'success',
                            title: 'Artikel Berhasil Diupdate',
                            text: result.message,
                            showConfirmButton: false,
                            timer: 1500
                        });

                        setTimeout(function() {
                            window.location.href = 'admin_artikel.php?id='+encryptedId; // Redirect to admin_artikel.php after 1.5 seconds
                        }, 1500);
                    } else {
                        Swal.fire({
                            icon: 'error',
                            title: 'Gagal Mengupdate Artikel',
                            text: result.message
                        });
                    }
                } catch (error) {
                    console.error('Error:', error);

                    Swal.fire({
                        icon: 'error',
                        title: 'Error',
                        text: `Error: ${error.message}`
                    });
                }
            });
        });

        document.addEventListener("DOMContentLoaded", function() {
            const batalButton = document.querySelector(".btn-batal");

            batalButton.addEventListener("click", function() {
                // Gantilah 'halaman_tujuan.php' dengan halaman yang ingin Anda tuju
                window.location.href = 'admin_artikel.php?id='+encryptedId;
            });
        });
    </script>
    <script src="js/admin_dashboard.js" type="text/javascript"></script>
</body>

</html>