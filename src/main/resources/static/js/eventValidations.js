document.getElementById('name').addEventListener('input', function () {
    this.setCustomValidity('');
    if (!this.checkValidity()) {
      this.setCustomValidity("Името на събитието трябва да е между 5 и 30 символа!");
    }
});

document.getElementById('description').addEventListener('input', function () {
    this.setCustomValidity('');
    if (!this.checkValidity()) {
      this.setCustomValidity("Описанието трява да е между 5 и 255 символа!");
    }
});

document.getElementById('address').addEventListener('input', function () {
    this.setCustomValidity('');
    if (!this.checkValidity()) {
      this.setCustomValidity("Полето трява да съдържа поне 5 символа!");
    }
});

document.getElementById('eventCategories').addEventListener('input', function () {
    this.setCustomValidity('');
    if (!this.checkValidity()) {
      this.setCustomValidity("Моля използвайте само букви (латиница , кирилица) и запетаи!");
    }
});
document.getElementById('price').addEventListener('input', function () {
    this.setCustomValidity('');
    if (!this.checkValidity()) {
      this.setCustomValidity("Цената на събитието трябва да бъде най-малко 0!");
    }
});
document.getElementById('minAge').addEventListener('input', function () {
    this.setCustomValidity('');
    if (!this.checkValidity()) {
      this.setCustomValidity("Полето може да бъде с най-малка стойност 0!");
    }
});
document.getElementById('maxAge').addEventListener('input', function () {
    this.setCustomValidity('');
    if (!this.checkValidity()) {
      this.setCustomValidity("Полето може да бъде с най-малка стойност 0!");
    }
});

document.getElementById('registerForm').addEventListener('submit', function (event) {
    event.preventDefault();
});