// user-profile
$(document).ready(function () {
  $(".action").on("click", function () {
    $(".pengguna").toggleClass("active");
  });
});

// Ambil elemen navbar
const navbar = document.getElementById("navbar");

// Tambahkan event listener untuk mengikuti scroll
window.addEventListener("scroll", () => {
  if (window.pageYOffset > 0) {
    navbar.classList.add("bg-white");
  } else {
    navbar.classList.remove("bg-white");
  }
});

//Burger Navbar
document.addEventListener('DOMContentLoaded', function() {
  const burgerMenu = document.querySelector('.burger-menu');
  const nav = document.querySelector('.nav');

  burgerMenu.addEventListener('click', function() {
    burgerMenu.classList.toggle('active');
    nav.classList.toggle('active');
  });
});


// Login
const sign_in_btn = document.querySelector("#sign-in-btn");
const sign_up_btn = document.querySelector("#sign-up-btn");
const container = document.querySelector(".container");

// Tambahkan kelas "sign-up-mode" pada elemen .container saat halaman dimuat
container.classList.add("sign-up-mode");

sign_up_btn.addEventListener("click", () => {
  container.classList.add("sign-up-mode");
});

sign_in_btn.addEventListener("click", () => {
  container.classList.remove("sign-up-mode");
});

// Memeriksa apakah sesi sudah dimulai sebelum memulainya
if (typeof window !== 'undefined' && window.sessionStorage && !sessionStorage.getItem('session_started')) {
  sessionStorage.setItem('session_started', '1');
}
