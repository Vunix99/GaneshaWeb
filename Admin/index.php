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
    <title>Login Admin DIUR</title>
    <link rel="shortcut icon" href="images/logo-mini.png" type="image/x-icon" />
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
    <!-- Tambahkan script SweetAlert -->
    <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
    <style>
        body,
        html {
            height: 100%;
            margin: 0;
            overflow: hidden;
            font-family: 'Rubik', sans-serif;
        }

        .bg-image {
            background-image: url('images/bg.png');
            height: 100%;
            background-size: cover;
            background-position: center;
            position: fixed;
            width: 100%;
            z-index: -1;
            opacity: 0;
            animation: fadeIn 1s ease-in-out forwards;
        }

        .card-container {
            display: flex;
            align-items: center;
            justify-content: center;
            height: 100vh;
            opacity: 0;
            animation: fadeIn 1s ease-in-out 0.5s forwards;
        }

        .card {
            width: 30%;
            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
            position: relative;
            padding: 20px;
            /* Tambahkan padding */
        }

        .form-group {
            position: relative;
        }

        .input-group {
            position: relative;
        }

        .form-group label,
        .input-group label {
            opacity: 0.15;
            transition: opacity 0.3s ease-in-out;
            position: absolute;
            top: 50%;
            transform: translateY(-50%);
            padding: 0 10px;
            pointer-events: yes;
        }

        .form-group input,
        .input-group input {
            padding-left: 10px;
        }

        .form-group input:focus+label,
        .form-group input:not(:placeholder-shown)+label,
        .input-group input:focus+label,
        .input-group input:not(:placeholder-shown)+label {
            opacity: 0;
        }

        .input-group-text {
            cursor: pointer;
            position: absolute;
            right: 10px;
            top: 50%;
            transform: translateY(-50%);
        }

        .form-group button,
        .form-group a {
            width: 100%;
            margin-top: 10px;
        }

        @keyframes fadeIn {
            from {
                opacity: 0;
            }

            to {
                opacity: 1;
            }
        }

        @media (max-width: 576px) {
            .card {
                width: 80%;
            }

            .card img {
                max-width: 100%;
            }

            .card h2 {
                font-size: 1.2rem;
            }

            .form-group input,
            .input-group input {
                width: 100%;
            }
        }

        @media (max-width: 350px) {
            .card {
                width: 100%;
            }
        }

        @media (min-width: 577px) and (max-width: 768px) {
            .card {
                width: 60%;
            }
        }

        .card img {
            width: 100%;
        }
    </style>
</head>

<body>
    <!-- Container untuk menampilkan gambar -->
    <div class="bg-image"></div>

    <!-- Container untuk card dan form -->
    <div class="card-container d-flex align-items-center justify-content-center">
        <div class="card p-4 shadow">
            <!-- Tempatkan elemen img sebelum elemen h2 -->
            <img src="images/logo.png" alt="Logo" style="width: 200px; height: auto; display: block; margin: auto;"><br>
            <h2 class="mb-4 text-left small">LOGIN ADMIN</h2>
            <form action="<?php echo $_SERVER['PHP_SELF']; ?>" method="POST">
                <div class="form-group">
                    <input type="text" class="form-control form-control-empty" id="username" name="username" required placeholder="Username">
                    <label for="username">Username</label>
                </div>
                <div class="input-group">
                    <input type="password" class="form-control form-control-empty" id="password" name="password" required placeholder="Password">
                    <label for="password">Password</label>
                    <span class="input-group-text" onclick="togglePassword('password', 'togglePasswordBtn')">
                        <i class="fa fa-eye" aria-hidden="true" id="togglePasswordBtn"></i>
                    </span>
                </div><br><br>

                <div class="d-flex justify-content-between mt-3">
                    <button type="submit" class="btn btn-success text-left" name="submit">Masuk</button>
                    <br class="d-md-none" />
                    <span class="align-self-center"> Atau </span>
                    <br class="d-md-none" />
                    <a href="reg-admin.php" class="btn btn-primary text-right">Daftar</a>
                </div>
            </form>
        </div>
    </div>

    <script src="https://code.jquery.com/jquery-3.2.1.slim.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.12.9/umd/popper.min.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/js/bootstrap.min.js"></script>
    <script>
        // Fungsi untuk menampilkan SweetAlert
        function showSweetAlert(icon, title, text) {
            Swal.fire({
                icon: icon,
                title: title,
                text: text,
                showConfirmButton: false,
                timer: 1500
            });
        }
    </script>
    <script>
        function togglePassword(inputId, buttonId) {
            var passwordInput = document.getElementById(inputId);
            var toggleButton = document.getElementById(buttonId);

            if (!passwordInput || !toggleButton) {
                console.error('Element not found');
                return;
            }

            if (passwordInput.type === 'password') {
                passwordInput.type = 'text';
                toggleButton.classList.remove('fa-eye');
                toggleButton.classList.add('fa-eye-slash');
            } else {
                passwordInput.type = 'password';
                toggleButton.classList.remove('fa-eye-slash');
                toggleButton.classList.add('fa-eye');
            }

            // Additional checks for other elements
            var otherInputId = (inputId === 'password') ? 'keypass' : 'password';
            var otherToggleButtonId = (inputId === 'password') ? 'toggleKeypassBtn' : 'togglePasswordBtn';

            var otherPasswordInput = document.getElementById(otherInputId);
            var otherToggleButton = document.getElementById(otherToggleButtonId);

            if (otherToggleButton) {
                otherToggleButton.classList.remove('fa-eye-slash');
                otherToggleButton.classList.add('fa-eye');
            }
        }
    </script>
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
    if ($_SERVER['REQUEST_METHOD'] == 'POST' && isset($_POST['submit'])) {
        $username = $_POST['username'];
        $password = $_POST['password'];

        // Konfigurasi curl
        $endpoint = 'https://asia-south1.gcp.data.mongodb-api.com/app/application-0-dlgbn/endpoint/loginAdmin'; // Ganti dengan URL endpoint yang benar
        $url = $endpoint . '?username=' . urlencode($username) . '&password=' . urlencode($password);

        $ch = curl_init();
        curl_setopt($ch, CURLOPT_URL, $url);
        curl_setopt($ch, CURLOPT_RETURNTRANSFER, 1);
        $result = curl_exec($ch);
        curl_close($ch);

        if ($result === FALSE) {
            echo '<script>
            showSweetAlert("error", "Login Gagal", "Gagal melakukan permintaan HTTP");
            </script>';
        } else {
            $resultArray = json_decode($result, true);

            if ($resultArray['success'] === true) {
                // Enkripsi ID pengguna sebelum mengirimkannya
                $encrypted_id = encryptId($resultArray['id']);

                $_SESSION['token'] = $resultArray['id'];

                echo '<script>
                    showSweetAlert("success", "Login Berhasil", "Login berhasil!");
                    setTimeout(function() {
                        window.location.href = "admin_dashboard.php?id=' . $encrypted_id . '"; // Kirim ID pengguna yang dienkripsi ke halaman utama.php
                    }, 1500); // Redirect ke admin_dahsboard.php setelah 1.5 detik
                </script>';
                // Alihkan langsung ke halaman admin.php menggunakan tag meta
                echo '<meta http-equiv="refresh" content="1.5;url=admin_dashboard.php?id=' . $encrypted_id . '">';
            } else {
                echo '<script>
                    showSweetAlert("error", "Login Gagal", "' . $resultArray['message'] . '");
                </script>';
            }
        }
    }
    ?>
</body>

</html>