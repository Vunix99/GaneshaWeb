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
    <title>Edit Profile</title>
    <link rel="stylesheet" type="text/css" href="style/register_finishing.css" media="all">
    <link rel="shortcut icon" href="images/logo-mini.png" type="image/x-icon" />
    <link rel="stylesheet" href="https://fonts.googleapis.com/css2?family=Poppins:wght@400;700&display=swap">
    <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/aos@2.3.4/dist/aos.css" />
    <script src="https://cdn.jsdelivr.net/npm/aos@2.3.4/dist/aos.js"></script>
    <script>
        // Fungsi untuk menampilkan SweetAlert
        function showSweetAlert(icon, title, text) {
            Swal.fire({
                icon: icon,
                title: title,
                text: text
            });
        }
    </script>
    <style>
        body {
            overflow-x: hidden;
        }
    </style>
</head>

<body style="background: url('images/bg-register.png') center center fixed; background-size: cover;">
    <div class="container" data-aos="fade-right">
        <div class="image-container">
            <h2 class="form-title">Edit profil-mu</h2>
            <img src="images/fill-data.png" alt="Fill Your Data">
        </div>
        <div class="form-container">
            <form id="profileForm" method="post" data-aos="fade-up" data-aos-duration="1200">
                <label for="age">Usia:</label>
                <input type="number" id="age" name="age" min="1" required>

                <label for="gender">Jenis Kelamin:</label>
                <select id="gender" name="gender" required>
                    <option value="Laki-laki">Laki-laki</option>
                    <option value="Perempuan">Perempuan</option>
                </select>

                <label for="weight">Berat Badan (kg):</label>
                <div>
                    <input type="number" id="weight" name="weight" min="1" required>
                </div>

                <label for="height">Tinggi Badan (cm):</label>
                <div>
                    <input type="number" id="height" name="height" min="1" required>
                </div>

                <label for="activity">Level Aktivitas Harian:</label>
                <select id="activity" name="activity" required>
                    <option value="Jarang">Jarang</option>
                    <option value="Ringan">Ringan</option>
                    <option value="Sedang">Sedang</option>
                    <option value="Aktif">Aktif</option>
                    <option value="Sangat-Aktif">Sangat Aktif</option>
                </select>
                <div>
                    <button type="button" onclick="showSecurityKeyModal()">Lihat Security Key</button>
                    <button type="button" id="changePasswordBtn">Ganti Password</button>
                </div>

                <!-- Tombol Selesaikan Pendaftaran -->
                <button type="submit" id="submitBtn">Simpan perubahan</button>
                <a href="utama.php?id=<?php echo $_GET['id'] ?? ''; ?>" class="back-button">&#8592; Back to Utama</a>
            </form>

            <div id="securityKeyModal" class="modal">
                <div class="modal-content">
                    <span class="close" onclick="closeSecurityKeyModal()">&times;</span>
                    <form id="securityKeyForm">
                        <label for="modalUsername">Username:</label>
                        <input type="text" id="modalUsername" name="modalUsername" required disabled>

                        <label for="modalPassword">Password:</label>
                        <input type="password" id="modalPassword" name="modalPassword" required>

                        <label for="showPassword">
                            <input type="checkbox" id="showPassword" onclick="togglePasswordVisibility()"> Show Password
                        </label>

                        <button type="button" onclick="submitSecurityKeyForm()">Submit</button>
                    </form>
                    <div>
                        <h3>Security Key: <br><span id="modalSecurityKey"></span></h3>
                        <button type="button" onclick="copyModalSecurityKey()">Copy to Clipboard</button>
                    </div>
                </div>
            </div>
            <!-- Change Password Modal -->
            <div id="changePasswordModal" class="modal">
                <div class="modal-content">
                    <span class="close" onclick="closeChangePasswordModal()">&times;</span>
                    <form id="changePasswordForm">
                        <label for="oldPassword">Password Lama:</label>
                        <input type="password" id="oldPassword" name="oldPassword" required>

                        <label for="newPassword">Password Baru:</label>
                        <input type="password" id="newPassword" name="newPassword" required>

                        <label for="confirmNewPassword">Konfirmasi Password Baru:</label>
                        <input type="password" id="confirmNewPassword" name="confirmNewPassword" required>

                        <button type="button" onclick="submitChangePasswordForm()">Ubah Password</button>
                    </form>
                </div>
            </div>

        </div>
    </div>
    <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
    <script>
        var encryptedId = "<?php echo $_GET['id'] ?? ''; ?>";
        const decryptedId = "<?php echo $decryptedId; ?>";

        function showChangePasswordModal() {
            var modal = document.getElementById('changePasswordModal');
            modal.style.display = 'block';
        }

        // Event handler untuk tombol "Ganti Password"
        document.getElementById('changePasswordBtn').addEventListener('click', function() {
            showChangePasswordModal();
        });

        // Fungsi untuk menutup modal ganti password
        function closeChangePasswordModal() {
            var modal = document.getElementById('changePasswordModal');
            modal.style.display = 'none';
        }

        function submitChangePasswordForm() {
            const oldPassword = document.getElementById('oldPassword').value;
            const newPassword = document.getElementById('newPassword').value;
            const confirmNewPassword = document.getElementById('confirmNewPassword').value;

            // Perform validation and submit the form as needed

            // Ensure proper validation of new password and confirmation
            if (newPassword !== confirmNewPassword) {
                showSweetAlert("error", "Gagal Mengubah Password", "Konfirmasi password baru tidak sesuai");
                return;
            }

            const changePasswordUrl = `https://asia-south1.gcp.data.mongodb-api.com/app/application-0-dlgbn/endpoint/changePassword?id=${decryptedId}&password_lama=${oldPassword}&password_baru=${newPassword}`;

            const requestBody = {
                method: 'PUT',
                headers: {
                    'Content-Type': 'application/json',
                },
            };

            fetch(changePasswordUrl, requestBody)
                .then(response => {
                    if (!response.ok) {
                        throw new Error(`HTTP error! Status: ${response.status}`);
                    }
                    return response.json();
                })
                .then(data => {
                    if (data.success === "success") {
                        showSweetAlert("success", "Password Berhasil Diubah", "Password Anda telah diubah dengan sukses!");
                        closeChangePasswordModal();
                    } else {
                        showSweetAlert("error", "Gagal Mengubah Password", data.error || "Terjadi kesalahan");
                    }
                })
                .catch(error => {
                    console.error('Fetch error:', error);
                    showSweetAlert("error", "Error", "Terjadi kesalahan saat mengubah password");
                });
        }





        function togglePasswordVisibility() {
            var passwordInput = document.getElementById('modalPassword');
            var showPasswordCheckbox = document.getElementById('showPassword');

            // Ganti tipe input berdasarkan status centang checkbox
            passwordInput.type = showPasswordCheckbox.checked ? 'text' : 'password';
        }

        function showSecurityKeyModal() {
            var modal = document.getElementById('securityKeyModal');
            modal.style.display = 'block';
        }

        function closeSecurityKeyModal() {
            var modal = document.getElementById('securityKeyModal');
            modal.style.display = 'none';
        }

        // Menambahkan listener untuk menutup modal saat mengklik di luar modal
        window.onclick = function(event) {
            var modal = document.getElementById('securityKeyModal');
            if (event.target == modal) {
                closeSecurityKeyModal();
            }
        }

        function submitSecurityKeyForm() {
            const username = document.getElementById('modalUsername').value;
            const password = document.getElementById('modalPassword').value;

            const credentialsUrl = `https://asia-south1.gcp.data.mongodb-api.com/app/application-0-dlgbn/endpoint/getSecurityKey?id=${decryptedId}&password=${password}`;

            fetch(credentialsUrl)
                .then(response => {
                    if (!response.ok) {
                        throw new Error(`HTTP error! Status: ${response.status}`);
                    }
                    return response.json();
                })
                .then(data => {
                    if (data.security_key) {
                        document.getElementById('modalSecurityKey').innerText = data.security_key;
                        showSweetAlert("success", "Kredensial Valid", "Security key ditemukan!");
                    } else {
                        showSweetAlert("error", "Kredensial Tidak Valid", "Pengguna tidak ditemukan atau kata sandi salah");
                    }
                })
                .catch(error => {
                    console.error('Fetch error:', error);
                    showSweetAlert("error", "Error", "Terjadi kesalahan saat mengambil security key");
                });
        }

        function copyModalSecurityKey() {
            const modalSecurityKey = document.getElementById('modalSecurityKey').innerText;

            if (modalSecurityKey.trim() === '') {
                showSweetAlert('error', 'Gagal Copy', 'Security key kosong. Tidak dapat menyalin.');
            } else {
                const textarea = document.createElement('textarea');
                textarea.value = modalSecurityKey;
                document.body.appendChild(textarea);
                textarea.select();
                document.execCommand('copy');
                document.body.removeChild(textarea);
                showSweetAlert('success', 'Berhasil Copy', 'Security key berhasil di-copy. Silahkan taruh pada penyimpanan/note anda.');
            }
        }


        // Ambil data pengguna dari endpoint getUserProfile
        fetch(`https://asia-south1.gcp.data.mongodb-api.com/app/application-0-dlgbn/endpoint/getUserProfile?id=${decryptedId}`)
            .then(response => {
                if (!response.ok) {
                    throw new Error(`HTTP error! Status: ${response.status}`);
                }
                return response.json();
            })
            .then(data => {
                // Isi data formulir dengan nilai yang diterima
                document.getElementById('age').value = data.usia || '';
                document.getElementById('gender').value = data.jenis_kelamin || '';
                document.getElementById('weight').value = data.berat_badan || '';
                document.getElementById('height').value = data.tinggi_badan || '';
                document.getElementById('activity').value = data.level_aktivitas || '';
                document.getElementById('modalUsername').value = data.username || '';

                // Tampilkan security key
                document.getElementById('security_key').innerText = data.security_key || '';
            })
            .catch(error => {
                console.error('Fetch error:', error);
                // Handle error
            });

        function showSweetAlert(icon, title, text) {
            Swal.fire({
                icon: icon,
                title: title,
                text: text
            });
        }
    </script>
    <?php
    $encryptedId = $_GET['id'] ?? '';
    if (isset($_POST['age']) && isset($_POST['gender']) && isset($_POST['weight']) && isset($_POST['height']) && isset($_POST['activity'])) {
        $age = $_POST['age'];
        $gender = $_POST['gender'];
        $weight = $_POST['weight'];
        $height = $_POST['height'];
        $activity = $_POST['activity'];

        $url = 'https://asia-south1.gcp.data.mongodb-api.com/app/application-0-dlgbn/endpoint/updateProfile?id=' . $decryptedId . '&age=' . $age . '&gender=' . $gender . '&weight=' . $weight . '&height=' . $height . '&activity=' . $activity;

        $options = array(
            'http' => array(
                'method' => 'PUT',
                'header' => "Content-type: application/json\r\n",
            )
        );

        $context = stream_context_create($options);
        $result = file_get_contents($url, false, $context);

        if ($result === FALSE) {
            // Handle error
            echo '<script>Swal.fire("Gagal", "Gagal memperbarui data", "error")</script>';
        } else {
            echo '<script>
                        showSweetAlert("success", "Edit profil Berhasil", "Data berhasil disimpan!");
                        setTimeout(function() {
                            window.location.href = "utama.php?id=' . $encryptedId . '"; // Kirim ID pengguna yang dienkripsi ke halaman utama.php
                        }, 2500); // Redirect ke utama.php setelah 2.5 detik
                    </script>';
            exit();
        }
    }
    ?>

    <script>
        AOS.init();
    </script>
</body>

</html>