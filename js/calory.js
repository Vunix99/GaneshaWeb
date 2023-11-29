document.addEventListener("DOMContentLoaded", function () {
    const form = document.querySelector("form");
    form.addEventListener("submit", function (e) {
      e.preventDefault();
      const usia = parseInt(form.elements[0].value);
      const tinggi = parseInt(form.elements[1].value);
      const berat = parseInt(form.elements[2].value);
      const gender = document.querySelector(
        'input[name="gender"]:checked'
      ).value;
      let bmr;
  
      if (gender === "male") {
        bmr = 66 + 13.7 * berat + 5 * tinggi - 6.8 * usia;
      } else {
        bmr = 655 + 9.6 * berat + 1.8 * tinggi - 4.7 * usia;
      }
  
      document.getElementById("kalori_ideal").textContent = `${bmr} kalori/hari`;
    });
  });
  