<?php
if (session_status() == PHP_SESSION_NONE) {
    session_start(); // Mulai sesi jika belum ada
}

// Mengekstrak parameter 'id' dari URL
$userId = isset($_GET['id']) ? $_GET['id'] : '';

function encryptId($userId)
{
    $iv = '12345678901' . str_repeat("\0", 5);
    $encryptedUserId = base64_encode(openssl_encrypt($userId, 'aes-128-cbc', 'secret_key', 0, $iv));
    return $encryptedUserId;
}

?>

<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Lengkapi Pendaftaran</title>
    <link rel="shortcut icon" href="images/logo-mini.png" type="image/x-icon" />
    <link rel="stylesheet" type="text/css" href="style/register_finishing.css">
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

        // Mengekstrak parameter 'id' dari URL
        var urlParams = new URLSearchParams(window.location.search);
        var userId = urlParams.get('id');

        // Menampilkan userId ke konsol (untuk tujuan debug)
        console.log('UserId:', userId);

        // Memeriksa apakah userId valid sebelum melakukan fetch
        if (userId) {
            // Menggunakan userId untuk mendapatkan security key
            getSecurityKey(userId);
        } else {
            console.error('UserId tidak valid');
        }

        function getSecurityKey(userId) {
            const url = `https://asia-south1.gcp.data.mongodb-api.com/app/application-0-dlgbn/endpoint/getUserProfile?id=${userId}`;

            fetch(url)
                .then(response => {
                    if (!response.ok) {
                        throw new Error(`HTTP error! Status: ${response.status}`);
                    }
                    return response.json();
                })
                .then(data => {
                    const securityKey = data.security_key || 'Security Key Not Found';
                    document.getElementById('security_key').innerText = securityKey;
                })
                .catch(error => {
                    console.error('Fetch error:', error);
                });
        }

        function copyToClipboard() {
            const securityKey = document.getElementById('security_key').innerText;
            const textarea = document.createElement('textarea');
            textarea.value = securityKey;
            document.body.appendChild(textarea);
            textarea.select();
            document.execCommand('copy');
            document.body.removeChild(textarea);
            showSweetAlert('success', 'Berhasil Copy', 'Security key berhasil di-copy silahkan taruh pada penyimpanan/note anda');

            // Aktifkan tombol "Selesaikan Pendaftaran"
            document.getElementById('submitBtn').classList.remove('disabled');
            document.getElementById('submitBtn').removeAttribute('disabled');
        }

        function submitForm() {
            if (document.getElementById('submitBtn').classList.contains('disabled')) {
                // Menggunakan SweetAlert untuk menampilkan pesan
                Swal.fire({
                    icon: 'error',
                    title: 'Gagal',
                    text: 'Harap klik tombol copy to clipboard sebelum menyelesaikan pendaftaran.'
                });
                return false;
            }

            // Lakukan validasi formulir atau tindakan tambahan di sini
            var age = document.getElementById('age').value;
            var gender = document.getElementById('gender').value;
            var weight = document.getElementById('weight').value;
            var height = document.getElementById('height').value;
            var activity = document.getElementById('activity').value;

            if (age === '' || gender === '' || weight === '' || height === '' || activity === '') {
                // Menggunakan SweetAlert untuk menampilkan pesan
                Swal.fire({
                    icon: 'error',
                    title: 'Gagal',
                    text: 'Harap lengkapi semua field sebelum mengirim formulir.'
                });
                return false; // Menghentikan pengiriman formulir
            }

            // Jika validasi berhasil, ubah tipe tombol menjadi "submit" dan submit formulir
            document.getElementById('submitBtn').type = 'submit';
            document.getElementById('submitBtn').removeAttribute('disabled');
            document.getElementById('submitBtn').classList.remove('disabled');
        }
    </script>
</head>

<body style="background: url('images/bg-register.png') center center fixed; background-size: cover;">
    <div class="container" data-aos="fade-up" data-aos-duration="1200">
        <div class="image-container">
            <h2 class="form-title">Lengkapi pendaftaran-mu</h2>
            <img src="images/alert.png" alt="Fill Your Data">
        </div>
        <div class="form-container">
            <form id="profileForm" method="post" onsubmit="return submitForm()">
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
                    <h4 style="color: red; margin-bottom:0;">Harap copy terlabih dahulu security-key anda</h4>
                    <h4>"Security key ini akan digunakan ketika lupa password"</h4>
                    <h3>Security Key: <br><span id="security_key"></span></h3>
                    <button type="button" onclick="copyToClipboard()">Copy to Clipboard</button>
                </div>

                <!-- Tombol Selesaikan Pendaftaran -->
                <button type="submit" id="submitBtn" class="disabled" disabled onclick="submitForm()">Selesaikan Pendaftaran!</button>

            </form>

            <?php
            if (isset($_POST['age']) && isset($_POST['gender']) && isset($_POST['weight']) && isset($_POST['height']) && isset($_POST['activity'])) {
                $age = $_POST['age'];
                $gender = $_POST['gender'];
                $weight = $_POST['weight'];
                $height = $_POST['height'];
                $activity = $_POST['activity'];

                $url = 'https://asia-south1.gcp.data.mongodb-api.com/app/application-0-dlgbn/endpoint/updateProfile?id=' . $userId . '&age=' . $age . '&gender=' . $gender . '&weight=' . $weight . '&height=' . $height . '&activity=' . $activity;

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
                    // Handle success
                    $_SESSION['token'] = $userId;
                    $encryptedId = encryptId($userId);
                    echo '<script>
                        showSweetAlert("success", "Registrasi Berhasil", "Selamat, Akun telah diregistrasi!");
                        setTimeout(function() {
                            window.location.href = "utama.php?id=' . $encryptedId . '"; // Kirim ID pengguna yang dienkripsi ke halaman utama.php
                        }, 2500); // Redirect ke utama.php setelah 2.5 detik
                    </script>';
                    exit();
                }
            }
            ?>
        </div>
    </div>
    <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
    <script>
        AOS.init();
    </script>
</body>

</html>