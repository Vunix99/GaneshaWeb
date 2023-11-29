<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Daftar Admin DIUR</title>
    <link rel="shortcut icon" href="images/logo-mini.png" type="image/x-icon" />
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
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

    <!-- Tambahkan script SweetAlert -->
    <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
</head>

<body>
    <div class="bg-image"></div>

    <div class="card-container d-flex align-items-center justify-content-center">
        <div class="card p-4 shadow">
            <img src="images/logo.png" alt="Logo" style="width: 200px; height: auto; display: block; margin: auto;"><br>
            <h2 class="mb-4 text-left small">DAFTAR ADMIN</h2>
            <form id="registrationForm" action="" method="post">
                <div class="form-group">
                    <input type="email" class="form-control form-control-empty" id="email" name="email" required placeholder="Email">
                    <label for="email">Email</label>
                </div>
                <div class="form-group">
                    <input type="text" class="form-control form-control-empty" id="username" name="username" required placeholder="Username">
                    <label for="username">Username</label>
                </div>
                <div class="form-group">
                    <input type="password" class="form-control form-control-empty" id="password" name="password" required placeholder="Password">
                    <label for="password">Password</label>
                    <span class="input-group-text" onclick="togglePassword('password', 'togglePasswordBtn')">
                        <i class="fa fa-eye" aria-hidden="true" id="togglePasswordBtn"></i>
                    </span>
                </div>
                <div class="form-group">
                    <input type="password" class="form-control form-control-empty" id="keypass" name="keypass" required placeholder="Keypass">
                    <label for="keypass">Keypass</label>
                    <span class="input-group-text" onclick="togglePassword('keypass', 'toggleKeypassBtn')">
                        <i class="fa fa-eye" aria-hidden="true" id="toggleKeypassBtn"></i>
                    </span>
                </div><br><br>

                <div class="d-flex justify-content-between mt-3">
                    <button type="submit" class="btn btn-success text-left" name="daftar">Daftar</button>
                    <br class="d-md-none" />
                    <span class="align-self-center"> Atau </span>
                    <br class="d-md-none" />
                    <a href="index.php" class="btn btn-primary text-right">Masuk</a>
                </div>
            </form>
        </div>
    </div>
    <?php
    if ($_SERVER['REQUEST_METHOD'] === 'POST' && isset($_POST['daftar'])) {
        // Form is submitted, process the data

        // Make sure to validate and sanitize input data before using it
        $email = isset($_POST['email']) ? $_POST['email'] : '';
        $username = isset($_POST['username']) ? $_POST['username'] : '';
        $password = isset($_POST['password']) ? $_POST['password'] : '';
        $keypass = isset($_POST['keypass']) ? $_POST['keypass'] : '';

        // Add more validation if needed

        // Validate keypass
        if ($keypass !== 'ganesha') {
            // Menggunakan SweetAlert untuk menampilkan pesan kesalahan
            echo '<script>';
            echo 'Swal.fire({
            icon: "error",
            title: "Kesalahan KeyPass",
            text: "Keypass yang dimasukkan tidak valid"
        });';
            echo '</script>';
        } else {
            // Replace this with your actual MongoDB function URL
            $functionUrl = 'https://asia-south1.gcp.data.mongodb-api.com/app/application-0-dlgbn/endpoint/registAdmin';

            // Data to be sent to the MongoDB function
            $data = array(
                'username' => $username,
                'email' => $email,
                'password' => $password
            );

            // Set up cURL
            $ch = curl_init($functionUrl);
            curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
            curl_setopt($ch, CURLOPT_CUSTOMREQUEST, 'POST');
            curl_setopt($ch, CURLOPT_HTTPHEADER, array(
                'Content-Type: application/json'
            ));
            curl_setopt($ch, CURLOPT_POSTFIELDS, json_encode($data));

            // Execute cURL and get the response
            $response = curl_exec($ch);

            // Check for cURL errors and HTTP status code
            $httpCode = curl_getinfo($ch, CURLINFO_HTTP_CODE);
            if (curl_errno($ch)) {
                // Menggunakan SweetAlert untuk menampilkan pesan error cURL
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

                if ($result && isset($result['status'])) {
                    if ($result['status'] === 'success') {
                        // Menggunakan SweetAlert untuk menampilkan pesan sukses
                        echo '<script>';
                        echo 'Swal.fire({
                        icon: "success",
                        title: "Registrasi Berhasil",
                        text: "Data berhasil disimpan, silahkan login",
                        showConfirmButton: false,
                        timer: 1500
                    });
                    setTimeout(function() {
                        window.location.href = "index.php"; // Redirect ke index.php setelah 1.5 detik
                    }, 1500);';
                        echo '</script>';
                    } elseif ($result['status'] === 'failed' && $result['reason'] === 'UsernameAlreadyExists') {
                        // Menggunakan SweetAlert untuk menampilkan pesan ketika username sudah terdaftar
                        echo '<script>';
                        echo 'Swal.fire({
                        icon: "error",
                        title: "Pendaftaran Gagal",
                        text: "Maaf, username telah terdaftar"
                    });';
                        echo '</script>';
                    } else {
                        // Registration failed for other reasons
                        $errorMessage = $result['message'] ?? 'Terjadi kesalahan';
                        // Menggunakan SweetAlert untuk menampilkan pesan kegagalan
                        echo '<script>';
                        echo 'Swal.fire({
                        icon: "error",
                        title: "Registrasi Gagal",
                        text: "' . $errorMessage . '"
                    });';
                        echo '</script>';
                    }
                }
            }

            // Close cURL
            curl_close($ch);
        }
    }
    ?>
    <script src="https://code.jquery.com/jquery-3.2.1.slim.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.12.9/umd/popper.min.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/js/bootstrap.min.js"></script>
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


</body>

</html>