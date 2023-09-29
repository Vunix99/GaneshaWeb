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

document.addEventListener("DOMContentLoaded", function () {
  const burgerMenu = document.querySelector(".burger-menu");
  const nav = document.querySelector(".nav");

  burgerMenu.addEventListener("click", function () {
    nav.classList.toggle("active");
    burgerMenu.classList.toggle("active");
  });

  const darkModeButton = document.getElementById("dark-mode");
  const lightModeButton = document.getElementById("light-mode");
  const navbarItems = document.querySelectorAll(".nav li a");
  const headings = document.querySelectorAll("h1, h2, h3, h4, h5, h6");
  const downloadApp = document.getElementById("download-app");
  const logoImages = document.querySelectorAll(".logo, .logo_diur");

  darkModeButton.addEventListener("click", () => {
    document.body.classList.toggle("dark__mode");

    if (document.body.classList.contains("dark__mode")) {
      const footer = document.querySelector("footer");
      footer.style.backgroundColor = "#202020";

      document.body.style.backgroundColor = "#333333";

      for (const navbarItem of navbarItems) {
        navbarItem.style.color = "white";
      }

      for (const heading of headings) {
        heading.style.color = "white";
      }

      const searchBanner = document.getElementById("search-banner");

      if (searchBanner) {
        searchBanner.style.backgroundColor = "#474747";
      }

      if (downloadApp) {
        downloadApp.style.backgroundColor = "#474747";

        const downloadAppTextElements = downloadApp.querySelectorAll("*");
        for (const element of downloadAppTextElements) {
          element.style.color = "white";
        }
      }

      for (const logoImage of logoImages) {
        logoImage.src = "images/logo-med-green.png";
      }

      // Mengganti ikon dari bulan ke matahari
      darkModeButton.querySelector("i.bx-moon").classList.remove("bx-moon");
      darkModeButton.querySelector("i").classList.remove("bx-moon");
      darkModeButton.querySelector("i").classList.add("bx-sun");

      if (lightModeButton) {
        // Mengganti ikon dari matahari ke bulan pada lightModeButton (jika ada)
        lightModeButton.querySelector("i.bx-sun").classList.remove("bx-sun");
        lightModeButton.querySelector("i").classList.remove("bx-sun");
        lightModeButton.querySelector("i").classList.add("bx-moon");
      }

      // Mengganti latar belakang navbar dalam mode gelap
      if (nav) {
        nav.style.backgroundColor = "#333333";
        // tambahkan properti lain sesuai kebutuhan
      }
    } else {
      const footer = document.querySelector("footer");
      footer.style.backgroundColor = "";

      document.body.style.backgroundColor = "";

      for (const navbarItem of navbarItems) {
        navbarItem.style.color = "";
      }

      for (const heading of headings) {
        heading.style.color = "";
      }

      const searchBanner = document.getElementById("search-banner");

      if (searchBanner) {
        searchBanner.style.backgroundColor = "";
      }

      if (downloadApp) {
        downloadApp.style.backgroundColor = "";

        const downloadAppTextElements = downloadApp.querySelectorAll("*");
        for (const element of downloadAppTextElements) {
          element.style.color = "";
        }
      }

      for (const logoImage of logoImages) {
        logoImage.src = "images/logo-med.png";
      }

      // Mengganti ikon dari matahari ke bulan
      darkModeButton.querySelector("i.bx-sun").classList.remove("bx-sun");
      darkModeButton.querySelector("i").classList.remove("bx-sun");
      darkModeButton.querySelector("i").classList.add("bx-moon");

      if (lightModeButton) {
        // Mengganti ikon dari bulan ke matahari pada lightModeButton (jika ada)
        lightModeButton.querySelector("i.bx-moon").classList.remove("bx-moon");
        lightModeButton.querySelector("i").classList.remove("bx-moon");
        lightModeButton.querySelector("i").classList.add("bx-sun");
      }

      // Mengembalikan latar belakang navbar ke mode terang
      if (nav) {
        nav.style.backgroundColor = ""; // Hapus latar belakang yang ada
      }
    }
  });
});
// Login
const sign_in_btn = document.querySelector("#sign-in-btn");
const sign_up_btn = document.querySelector("#sign-up-btn");
const container = document.querySelector(".container");

sign_up_btn.addEventListener("click", () => {
  container.classList.add("sign-up-mode");
});

sign_in_btn.addEventListener("click", () => {
  container.classList.remove("sign-up-mode");
});

