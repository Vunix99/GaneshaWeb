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
            <li>
                <a href="admin_artikel.php?id=<?php echo $_GET['id'] ?? ''; ?>">
                    <img src="images/artikeladmin.png" class="logo" alt="" />
                    <span class="text">Artikel</span>
                </a>
            </li>
            <li class="active">
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
    <!-- NAVBAR -->

    <section id="content">
        <nav>
            <i class="bx bx-menu"></i>
        </nav>
        <div class="content2">
            <div class="card-artikel">
                <h2>Edit Resep</h2>
            </div>
        </div>
    </section>
    <section id="content">
        <form class="postingan" id="resepForm">
            <div class="card-artikel">
                <div class="resep-title">
                    <input type="text" id="judul_resep" name="judul_resep" placeholder="Judul Resep" required />
                </div>
            </div>

            <div class="card-artikel">
                <div class="resep-title">
                    <input type="url" id="gambar_resep" name="gambar_resep" placeholder="Masukkan link gambar..." required />
                </div>
            </div>

            <div class="card-artikel">
                <div class="resep-title">
                    <textarea id="deskripsi" name="deskripsi" placeholder="Deskripsi Resep" required></textarea>
                </div>
            </div>

            <div class="card-artikel">
                <div class="resep-title">
                    <input type="number" id="jumlah_kalori_resep" name="jumlah_kalori_resep" placeholder="Jumlah Kalori" required />
                </div>
            </div>

            <div class="card-artikel" style="margin-bottom: 5px;">
                <div class="resep-title">
                    <input type="text" id="id_resep" name="id_resep" placeholder="ID Resep" required />
                </div>
            </div>

            <div class="kanan">
                <div class="button-artikel" style="margin-top:10px;">
                    <div class="buttons" style="flex-direction: row; margin-bottom:1rem;">
                        <button type="submit" class="btn-simpan">
                            <img src="images/floppy-disk.png" alt="Edit Icon" class="button-icon" />
                            <p>Simpan</p>
                        </button>
                    </div>
                    <div class="buttons" style="flex-direction: row; margin-bottom:1rem;">
                        <button type="reset" type="reset" class="btn-batal" style="margin-left: 1.5rem;">
                            <img src="images/delete-left.png" alt="Delete Icon" class="button-icon" />
                            <p>Batal</p>
                        </button>
                    </div>
                </div>
            </div>
        </form>
    </section>

    <?php
    // Check if id_resep parameter is set in the URL
    if (isset($_GET['id_resep'])) {
        // Get the resepId from the URL parameter
        $resepId = $_GET['id_resep'];

        // Create the URL endpoint for fetching resep data
        $fetchEndpoint = 'https://asia-south1.gcp.data.mongodb-api.com/app/application-0-dlgbn/endpoint/getResepById?id=' . $resepId;

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
                echo 'document.getElementById("judul_resep").value = "' . htmlspecialchars($result['judul_resep'], ENT_QUOTES, 'UTF-8') . '";';
                echo 'document.getElementById("gambar_resep").value = "' . htmlspecialchars($result['gambar_resep'], ENT_QUOTES, 'UTF-8') . '";';

                // Handle special characters and line breaks in deskripsi
                $deskripsiValue = str_replace(["\r\n", "\r", "\n"], '\n', $result['deskripsi']);
                $deskripsiValue = htmlspecialchars_decode($deskripsiValue, ENT_QUOTES);
                $deskripsiValue = str_replace(["\r\n", "\r", "\n"], '<br>', $deskripsiValue);
                echo 'document.getElementById("deskripsi").value = `' . $deskripsiValue . '`;';

                // Set the value of "jumlah_kalori_resep" directly from the retrieved data
                echo 'document.getElementById("jumlah_kalori_resep").value = "' . htmlspecialchars($result['jumlah_kalori_resep'], ENT_QUOTES, 'UTF-8') . '";';

                // Check if the 'id_resep' key exists in the $result array
                if (isset($_GET['id_resep'])) {
                    // Get the resepId from the URL parameter
                    $resepId = $_GET['id_resep'];

                    // Set the value of "id_resep" directly from the URL parameter
                    echo 'document.getElementById("id_resep").value = "' . htmlspecialchars($resepId, ENT_QUOTES, 'UTF-8') . '";';
                }

                echo '</script>';
            } else {
                // Handle error
                $errorMessage = $result['error'] ?? 'Terjadi kesalahan';
                echo '<script>';
                echo 'Swal.fire({
                    icon: "error",
                    title: "Gagal Mengambil Data Resep",
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
            const form = document.getElementById('resepForm');

            form.addEventListener('submit', async function(event) {
                event.preventDefault();

                const judul_resep = document.getElementById('judul_resep').value;
                const gambar_resep = document.getElementById('gambar_resep').value;
                const deskripsi = document.getElementById('deskripsi').value;
                const jumlah_kalori_resep = document.getElementById('jumlah_kalori_resep').value;
                const id_resep = document.getElementById('id_resep').value;

                const endpoint = 'https://asia-south1.gcp.data.mongodb-api.com/app/application-0-dlgbn/endpoint/updateResep';

                try {
                    const response = await fetch(endpoint, {
                        method: 'PUT',
                        headers: {
                            'Content-Type': 'application/json',
                        },
                        body: JSON.stringify({
                            id_resep: id_resep,
                            judul_resep: judul_resep,
                            gambar_resep: gambar_resep,
                            deskripsi: deskripsi,
                            jumlah_kalori_resep: jumlah_kalori_resep
                        }),
                    });

                    if (!response.ok) {
                        throw new Error(`HTTP error! Status: ${response.status}`);
                    }

                    const result = await response.json();

                    if (result.status === 'success') {
                        Swal.fire({
                            icon: 'success',
                            title: 'Resep Berhasil Diupdate',
                            text: result.message,
                            showConfirmButton: false,
                            timer: 1500
                        });

                        setTimeout(function() {
                            window.location.href = 'admin_resep.php?id=' + encryptedId; // Redirect to admin_resep.php after 1.5 seconds
                        }, 1500);
                    } else {
                        Swal.fire({
                            icon: 'error',
                            title: 'Gagal Mengupdate Resep',
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
                window.location.href = 'admin_resep.php?id=' + encryptedId;
            });
        });
    </script>

    <script src="js/admin_dashboard.js" type="text/javascript"></script>
</body>

</html>