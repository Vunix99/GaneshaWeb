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
    <!-- SIDEBAR -->

    <!-- CONTENT -->
    <section id="content">
        <!-- NAVBAR -->
        <nav>
            <i class="bx bx-menu"></i>
            <h3 class="profile">Welcome, <span id="admin_name"></span></h3>
        </nav>
        <!-- NAVBAR -->
        <main>
            <div class="head-title">
                <div class="left">
                    <h1>Artikel List</h1>
                    <ul class="breadcrumb">
                        <li>
                            <a class="active" href="admin_dashboard.php?id=<?php echo $_GET['id'] ?? ''; ?>" style="color: blue;">Dashboard</a>
                        </li>
                        <li><i class="bx bx-chevron-right"></i></li>
                        <li>
                            <a class="active" href="#" style="cursor: default;">Resep</a>
                        </li>
                    </ul>
                </div>
            </div>
            <div class="button_postingan">
                <a href="admin_add_artikel.php?id=<?php echo $_GET['id'] ?? ''; ?>"><button><i class="bx bx-plus" id="add_artikel"></i>Tambah artikel</button></a>
            </div>

            <ul class="box-info">
                <li>
                    <i class="bx bxs-spreadsheet" style="color: greenyellow; background:green;"></i>
                    <span class="text">
                        <h3 id="totalArtikel">2543</h3>
                        <p>Jumlah Artikel</p>
                    </span>
                </li>
            </ul>
        </main>
    </section>
    <!-- CONTENT -->
    <!-- MAIN -->

    <!--  -->
    <section id="content" class="article-container">
        <div class="card">
            <div class="img-container">
                <img src="images/artikel-2.jpeg" alt="Gambar Artikel" id="gambar_artikel" />
            </div>
            <div class="card-title">
                <p id="judul_artikel">Judul Artikel</p>
                <div class="title">
                    <p>Id Artikel : <span id="id_artikel">ART000</span></p>
                    <p>Tanggal Upload : <span id="tanggal_terbit">DD/MM/YYY</span>
                    </p>
                </div>
            </div>
            <div class="card-content">
                <div class="action-buttons">
                    <button class="btn-edit">Ubah</button>
                    <button class="btn-delete">Hapus</button>
                </div>
            </div>
        </div>
    </section>



    <!-- end--->


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
        var encryptedId = "<?php echo $_GET['id'] ?? ''; ?>";
        const decryptedId = "<?php echo $decryptedId; ?>";


        document.addEventListener("DOMContentLoaded", function() {
            const apiUrl = "https://asia-south1.gcp.data.mongodb-api.com/app/application-0-dlgbn/endpoint/getAllArtikel";

            async function fetchData() {
                try {
                    const response = await fetch(apiUrl);
                    const data = await response.json();

                    console.log("Mengambil data:", data);

                    // Hapus semua kartu sebelum menambahkan yang baru
                    const articleContainer = document.querySelector(".article-container");
                    articleContainer.innerHTML = "";

                    data.forEach((article, index) => {
                        createArticleCard(index + 1, article);
                    });
                } catch (error) {
                    console.error("Kesalahan mengambil data:", error);
                }
            }

            function createArticleCard(index, article) {
                const articleContainer = document.querySelector(".article-container");
                const newArticleCard = document.createElement("div");
                newArticleCard.classList.add("card");
                newArticleCard.id = `article-card-${index}`;

                // Additional div for img-container
                const imgContainerDiv = document.createElement("div");
                imgContainerDiv.classList.add("img-container");
                newArticleCard.appendChild(imgContainerDiv);

                const cardImage = document.createElement("img");
                cardImage.src = article.gambar_artikel;
                cardImage.alt = "Gambar Artikel";
                cardImage.id = `gambarArtikel${index}`;
                imgContainerDiv.appendChild(cardImage);

                const cardTitle = document.createElement("div");
                cardTitle.classList.add("card-title");
                newArticleCard.appendChild(cardTitle);

                const titleText = document.createElement("p");
                titleText.id = `judulArtikel${index}`;
                titleText.textContent = article.judul;
                cardTitle.appendChild(titleText);

                const titleDetails = document.createElement("div");
                titleDetails.classList.add("title");
                cardTitle.appendChild(titleDetails);

                const idArtikel = document.createElement("p");
                idArtikel.textContent = `Id Artikel: `;
                titleDetails.appendChild(idArtikel);

                const idArtikelSpan = document.createElement("span");
                idArtikelSpan.id = `id_artikel${index}`;
                idArtikelSpan.textContent = article.id_artikel;
                idArtikel.appendChild(idArtikelSpan);

                const tanggalUpload = document.createElement("p");
                tanggalUpload.textContent = `Tanggal Upload: `;
                titleDetails.appendChild(tanggalUpload);

                const tanggalUploadSpan = document.createElement("span");
                tanggalUploadSpan.id = `tanggal_terbit${index}`;
                tanggalUploadSpan.textContent = article.tanggal_terbit; // Gantilah dengan properti yang sesuai
                tanggalUpload.appendChild(tanggalUploadSpan);

                const cardContent = document.createElement("div");
                cardContent.classList.add("card-content");
                newArticleCard.appendChild(cardContent);

                const actionButtons = document.createElement("div");
                actionButtons.classList.add("action-buttons");
                cardContent.appendChild(actionButtons);

                const editButton = document.createElement("button");
                editButton.classList.add("btn-edit");
                editButton.textContent = "Ubah";
                editButton.id = `editButton${index}`;
                actionButtons.appendChild(editButton);

                const deleteButton = document.createElement("button");
                deleteButton.classList.add("btn-delete");
                deleteButton.textContent = "Hapus";
                deleteButton.id = `deleteButton${index}`;
                // Mengambil id_artikel dari URL sebagai parameter query
                const id_artikel = article.id_artikel;
                deleteButton.dataset.id_artikel = id_artikel;
                actionButtons.appendChild(deleteButton);

                articleContainer.appendChild(newArticleCard);

                // Tambahkan event listener untuk tombol "Ubah"
                editButton.addEventListener("click", function() {
                    window.location.href = `admin_edit_artikel.php?id=${encryptedId}&id_artikel=${article.id_artikel}`;
                });

                // Tambahkan event listener untuk tombol "Hapus"
                deleteButton.addEventListener("click", function() {
                    // Mendapatkan nilai id_artikel dari atribut data-id_artikel pada tombol hapus
                    const id_artikel = this.dataset.id_artikel;

                    Swal.fire({
                        title: "Yakin ingin menghapus artikel?",
                        showCancelButton: true,
                        confirmButtonText: "Ya, Hapus",
                    }).then((result) => {
                        if (result.isConfirmed) {
                            // Panggil fungsi deleteArtikelById dengan id_artikel yang sesuai
                            deleteArtikelById(id_artikel);
                        }
                    });
                });
            }

            async function deleteArtikelById(id_artikel) {
                const deleteEndpoint = `https://asia-south1.gcp.data.mongodb-api.com/app/application-0-dlgbn/endpoint/deleteArtikelById?id_artikel=${id_artikel}`;

                try {
                    const response = await fetch(deleteEndpoint, {
                        method: "DELETE",
                        headers: {
                            "Content-Type": "application/json",
                        },
                    });

                    if (!response.ok) {
                        throw new Error(`HTTP error! Status: ${response.status}`);
                    }

                    const result = await response.json();

                    if (result.status === "success") {
                        Swal.fire({
                            icon: "success",
                            title: "Artikel Berhasil Dihapus",
                            text: result.message,
                            showConfirmButton: false,
                            timer: 1500,
                        });

                        // Perbarui tampilan setelah penghapusan berhasil
                        fetchData();
                    } else {
                        Swal.fire({
                            icon: "error",
                            title: "Gagal Menghapus Artikel",
                            text: result.message,
                        });
                    }
                    logoutButton
                } catch (error) {
                    console.error("Error:", error);

                    Swal.fire({
                        icon: "error",
                        title: "Error",
                        text: `Error: ${error.message}`,
                    });
                }
            }

            fetchData();

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