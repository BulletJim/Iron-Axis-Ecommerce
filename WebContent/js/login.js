const loginForm = document.getElementById("loginForm");
const emailInput = document.getElementById("email");
const passwordInput = document.getElementById("password");
const emailError = document.getElementById("emailError");
const passwordError = document.getElementById("passwordError");


if (emailInput) {
    emailInput.addEventListener("input", function() {
        const emailValue = emailInput.value.trim();
        const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
        
        if (emailValue.length === 0) {
            emailError.textContent = "";
            emailInput.classList.remove("input-error");
        } else if (!emailRegex.test(emailValue)) {
            emailError.textContent = "Inserisci un formato email valido.";
            emailInput.classList.add("input-error");
        } else {
            emailError.textContent = "";
            emailInput.classList.remove("input-error");
        }
    });
}

if (passwordInput) {
    passwordInput.addEventListener("input", function() {
        const passwordValue = passwordInput.value;
        
        if (passwordValue.length === 0) {
            passwordError.textContent = "";
            passwordInput.classList.remove("input-error");
        } else if (passwordValue.length < 8) {
            passwordError.textContent = "La password deve avere almeno 8 caratteri.";
            passwordInput.classList.add("input-error");
        } else {
            passwordError.textContent = "";
            passwordInput.classList.remove("input-error");
        }
    });
}


if (loginForm) {
    loginForm.addEventListener("submit", function(event) {
        emailInput.dispatchEvent(new Event("input"));
        passwordInput.dispatchEvent(new Event("input"));

        const isEmailInvalid = emailInput.classList.contains("input-error") || emailInput.value.trim() === "";
        const isPasswordInvalid = passwordInput.classList.contains("input-error") || passwordInput.value === "";

        if (isEmailInvalid || isPasswordInvalid) {
            event.preventDefault();
        }
    });
}