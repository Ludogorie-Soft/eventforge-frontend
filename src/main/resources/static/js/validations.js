document.getElementById('name').addEventListener('input', function () {
    this.setCustomValidity('');
    if (!this.checkValidity()) {
      this.setCustomValidity("Името на организацията трябва да е между 5 и 30 символа!");
    }
});

document.getElementById('password').addEventListener('input', function () {
    this.setCustomValidity('');
    if (!this.checkValidity()) {
      this.setCustomValidity("Паролата трябва да е дълга поне 8 знака и да съдържа поне една цифра, една малка буква, една главна буква, един специален знак и без интервали!");
    }
});

document.getElementById('facebook').addEventListener('input', function () {
    this.setCustomValidity('');
    if (!this.checkValidity()) {
      this.setCustomValidity("Моля въведете валиден фейсбук линк (www.facebook.com/...)");
    }
});

document.getElementById('purpose').addEventListener('input', function () {
    this.setCustomValidity('');
    if (!this.checkValidity()) {
      this.setCustomValidity("Обоснованието трябва да е поне 15 символа!");
    }
});
document.getElementById('fullname').addEventListener('input', function () {
    this.setCustomValidity('');
    if (!this.checkValidity()) {
      this.setCustomValidity("Името трябва трябва да съдържа поне 8 символа! Моля използвайте само тирета,букви на кирилица или латиница");
    }
});document.getElementById('phone').addEventListener('input', function () {
    this.setCustomValidity('');
    if (!this.checkValidity()) {
      this.setCustomValidity("Невалиден телефонен номер.Телефонният номер трябва да съдържа между 10 и 13 цифри, позволени са само цифри и знакът +");
    }
});

document.getElementById('registerForm').addEventListener('submit', function (event) {
    event.preventDefault();
});
