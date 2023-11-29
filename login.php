<?php
if (session_status() == PHP_SESSION_NONE) {
    session_start(); // Mulai sesi jika belum ada
}
?>
<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>DIUR - Log In</title>
    <link rel="stylesheet" href="style/login.css" type="text/css" />
    <!-- Google fonts -->
    <link rel="preconnect" href="https://fonts.googleapis.com" />
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin />
    <link href="https://fonts.googleapis.com/css2?family=Poppins:ital,wght@0,100;0,200;0,300;0,400;0,500;0,600;0,700;0,800;0,900;1,100;1,200;1,300;1,400;1,500;1,600;1,700;1,800;1,900&display=swap" rel="stylesheet" />
    <!--==Using-Font-Awesome=====================-->
    <link rel="stylesheet" href="https://pro.fontawesome.com/releases/v5.10.0/css/all.css" integrity="sha384-AYmEC3Yw5cVb3ZcuHtOA93w35dYTsvhLPVnYs9eStHfGJvOvKxVfELGroGkvsg+p" crossorigin="anonymous" />
    <link rel="shortcut icon" href="images/logo-mini.png" type="image/x-icon" />

    <!--==Using-boxicons=====================-->
    <link href="https://unpkg.com/boxicons@2.1.4/css/boxicons.min.css" rel="stylesheet" />

    <!---Sweet Alert-->
    <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>

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
</head>

<body>
    <div class="container">
        <div class="forms-container">
            <div class="signin-signup">
                <form action="<?php echo $_SERVER['PHP_SELF']; ?>" method="POST" class="sign-in-form">
                    <h2 class="title">Sign in</h2>
                    <div class="input-field">
                        <i class="fas fa-user"></i>
                        <input type="text" name="username" placeholder="Username" />
                    </div>
                    <div class="input-field">
                        <i class="fas fa-lock"></i>
                        <input type="password" name="password" placeholder="Password" />
                    </div>
                    <div class="button-field">
                        <input type="button" name="forget-password" value="Forget-password" id="forgetPasswordBtn" class="btnforget" />
                        <input type="submit" name="login" value="Login" class="btn solid" />
                    </div>
                </form>

                <!-- Modal lupa password -->
                <div id="forgetPasswordModal" class="modal">
                    <div class="modal-content">
                        <span class="close" onclick="closeforgetPasswordModal()">&times;</span>
                        <form id="forgetPasswordForm">
                            <label for="username">Username:</label>
                            <input type="text" id="username" name="username" required style="width:100%; padding:10px; margin:5px 0; box-sizing:border-box;">

                            <label for="security_key">Security Key:</label>
                            <input type="text" id="security_key" name="security_key" required>

                            <label for="newPassword">Password Baru:</label>
                            <input type="password" id="newPassword" name="newPassword" required>

                            <label for="confirmNewPassword">Konfirmasi Password Baru:</label>
                            <input type="password" id="confirmNewPassword" name="confirmNewPassword" required>

                            <button type="button" id="btnSubmitForget" onclick="submitforgetPasswordForm()">Ubah Password</button>
                        </form>
                    </div>
                </div>

                <form action="<?php echo $_SERVER['PHP_SELF']; ?>" method="POST" class="sign-up-form">
                    <h2 class="title">Sign up</h2>
                    <div class="input-field">
                        <i class="fas fa-user"></i>
                        <input type="text" name="username" placeholder="Username" />
                    </div>
                    <div class="input-field">
                        <i class="fas fa-envelope"></i>
                        <input type="email" name="email" placeholder="Email" />
                    </div>
                    <div class="input-field">
                        <i class="fas fa-lock"></i>
                        <input type="password" name="password" placeholder="Password" />
                    </div>
                    <input type="submit" name="signup" class="btn" value="Sign up" />
                </form>
            </div>
        </div>

        <div class="panels-container">
            <div class="panel left-panel">
                <div class="content">
                    <h3>Pendatang baru ?</h3>
                    <p>
                        Daftarkan diri anda pada sistem kami, untuk mendapatkan pengalaman
                        yang baru dalam menjalankan diet untuk hidup yang sehat!
                    </p>
                    <button class="btn transparent" id="sign-up-btn">Sign up</button>
                </div>
                <img src="images/login.svg" class="image" alt="" />
            </div>
            <div class="panel right-panel">
                <div class="content">
                    <h3>Telah terdaftar ?</h3>
                    <p>
                        Silahkan melakukan login untuk melanjutkan progress harian dari
                        diet sayur mu di DIUR!
                    </p>
                    <button class="btn transparent" id="sign-in-btn">Sign in</button>
                </div>
                <img src="images/login.svg" class="image" alt="" />
            </div>
        </div>
    </div>

    <?php
    function encryptId($id)
    {
        $iv = '12345678901' . str_repeat("\0", 5); // Tambahkan 5 karakter nol ke IV
        $encryptedId = base64_encode(openssl_encrypt($id, 'aes-128-cbc', 'secret_key', 0, $iv));
        return $encryptedId;
    }

    // Fungsi untuk mengenkripsi ID pada proses sign up
    function encryptSignUpId($id)
    {
        $iv = '12345678901' . str_repeat("\0", 5); // Tambahkan 5 karakter nol ke IV
        $encryptedId = base64_encode(openssl_encrypt($id, 'aes-128-cbc', 'sign_up_secret_key', 0, $iv));
        return $encryptedId;
    }

    // Pemrosesan login
    if ($_SERVER['REQUEST_METHOD'] == 'POST') {
        if (isset($_POST['login'])) {
            $username = $_POST['username'];
            $password = $_POST['password'];

            // Konfigurasi permintaan HTTP
            $ch = curl_init();
            $url = 'https://asia-south1.gcp.data.mongodb-api.com/app/application-0-dlgbn/endpoint/loginUser';
            $url .= '?username=' . urlencode($username) . '&password=' . urlencode($password);
            curl_setopt($ch, CURLOPT_URL, $url);
            curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);

            // Eksekusi permintaan HTTP
            $response = curl_exec($ch);

            if ($response === FALSE) {
                echo '<script>
                    showSweetAlert("error", "Login Gagal", "Gagal melakukan permintaan HTTP");
                </script>';
            } else {
                $result = json_decode($response, true);
                if ($result['success'] === true) {
                    // Enkripsi ID pengguna sebelum mengirimkannya
                    $encrypted_id = encryptId($result['id']);

                    $_SESSION['token'] = $result['id'];

                    echo '<script>
                        showSweetAlert("success", "Login Berhasil", "Login berhasil!");
                        setTimeout(function() {
                            window.location.href = "utama.php?id=' . $encrypted_id . '"; // Kirim ID pengguna yang dienkripsi ke halaman utama.php
                        }, 1500); // Redirect ke utama.php setelah 1.5 detik
                    </script>';
                } else {
                    echo '<script>
                        showSweetAlert("error", "Login Gagal", "Username/Password tidak valid");
                    </script>';
                }
            }

            // Tutup koneksi cURL
            curl_close($ch);
        } elseif (isset($_POST['signup'])) {
            // Tangani proses pendaftaran di sini
            $username = $_POST['username'];
            $email = $_POST['email'];
            $password = $_POST['password'];

            // Data untuk dikirim ke API signup
            $data = [
                'username' => $username,
                'email' => $email,
                'password' => $password
            ];

            // Konfigurasi permintaan HTTP
            $ch = curl_init();
            curl_setopt($ch, CURLOPT_URL, 'https://asia-south1.gcp.data.mongodb-api.com/app/application-0-dlgbn/endpoint/registUser');
            curl_setopt($ch, CURLOPT_POST, 1);
            curl_setopt($ch, CURLOPT_POSTFIELDS, json_encode($data));
            curl_setopt($ch, CURLOPT_HTTPHEADER, ['Content-Type: application/json']);
            curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);

            // Eksekusi permintaan HTTP
            $response = curl_exec($ch);

            if ($response === FALSE) {
                echo '<script>
                    showSweetAlert("error", "Registrasi Gagal", "Gagal melakukan permintaan HTTP");
                </script>';
            } else {
                $result = json_decode($response, true);
                if ($result['status'] === "success") {
                    // Enkripsi ID pengguna sebelum mengirimkannya
                    $userId = $result['_id'];
                    echo '<script>
                        showSweetAlert("success", "Registrasi Berhasil", "Registrasi berhasil!");
                        setTimeout(function() {
                            window.location.href = "register_finishing.php?id=' . $userId . '"; // Arahkan pengguna ke register_finishing.php setelah 1.5 detik
                        }, 1500);
                    </script>';
                } else if ($result['status'] === "failed" && $result['message'] === "Pendaftaran gagal, username telah ada") {
                    echo '<script>
                        showSweetAlert("error", "Registrasi Gagal", "Pendaftaran gagal, Username telah tersedia pada sistem. Silahkan memilih Username lain.");
                    </script>';
                } else {
                    echo '<script>
                        showSweetAlert("error", "Registrasi Gagal", "Registrasi gagal. Silakan coba lagi.");
                    </script>';
                }
            }


            // Tutup koneksi cURL
            curl_close($ch);
        }
    }
    ?>

    <script>
        function showforgetPasswordModal() {
            var modal = document.getElementById('forgetPasswordModal');
            modal.style.display = 'block';
        }

        // Event handler untuk tombol "Ganti Password"
        document.getElementById('forgetPasswordBtn').addEventListener('click', function() {
            showforgetPasswordModal();
        });

        // Fungsi untuk menutup modal ganti password
        function closeforgetPasswordModal() {
            var modal = document.getElementById('forgetPasswordModal');
            modal.style.display = 'none';
        }

        function submitforgetPasswordForm() {
            const username = document.getElementById('username').value;
            const security_key = document.getElementById('security_key').value;
            const newPassword = document.getElementById('newPassword').value;
            const confirmNewPassword = document.getElementById('confirmNewPassword').value;

            // Perform validation and submit the form as needed

            // Ensure proper validation of new password and confirmation
            if (newPassword !== confirmNewPassword) {
                showSweetAlert("error", "Gagal Mengubah Password", "Konfirmasi password baru tidak sesuai");
                return;
            }

            const forgetPasswordUrl = `https://asia-south1.gcp.data.mongodb-api.com/app/application-0-dlgbn/endpoint/lupaPassword?username=${username}&security_key=${security_key}&new_password=${newPassword}`;

            const requestBody = {
                method: 'PUT',
                headers: {
                    'Content-Type': 'application/json',
                },
            };

            fetch(forgetPasswordUrl, requestBody)
                .then(response => {
                    if (!response.ok) {
                        throw new Error(`HTTP error! Status: ${response.status}`);
                    }
                    return response.json();
                })
                .then(data => {
                    if (data.status === "success") {
                        showSweetAlert("success", "Password Berhasil Diubah", "Password Anda telah diubah dengan sukses!");
                        closeforgetPasswordModal();
                    } else {
                        showSweetAlert("error", "Gagal Mengubah Password", data.message || "Terjadi kesalahan");
                    }
                })
                .catch(error => {
                    console.error('Fetch error:', error);
                    showSweetAlert("error", "Error", "Terjadi kesalahan saat mengubah password");
                });
        }
    </script>
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.6.0/jquery.min.js"></script>
    <script src="js/script.js" type="text/javascript"></script>
</body>

</html>