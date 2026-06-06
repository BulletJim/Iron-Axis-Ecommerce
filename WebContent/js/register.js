const regForm = document.getElementById("registerForm");
const emailInput = document.getElementById("email");
const passwordInput = document.getElementById("password");
const confPasswordInput = document.getElementById("confirmPassword");

const emailError = document.getElementById("emailError");
const passwordError = document.getElementById("passwordError");
const confPasswordError = document.getElementById("confirmPasswordError");

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
        const passwordRegex = /^(?=.*[A-Za-z])(?=.*\d)[A-Za-z\d]{8,25}$/;
        
        if (passwordValue.length === 0) {
            passwordError.textContent = "";
            passwordInput.classList.remove("input-error");
        } else if (!passwordRegex.test(passwordValue)) {
            passwordError.textContent = "Usa 8- 25caratteri, con almeno una lettera e un numero.";
            passwordInput.classList.add("input-error");
        } else {
            passwordError.textContent = "";
            passwordInput.classList.remove("input-error");
        }

        if (confPasswordInput && confPasswordInput.value.length > 0) {
            confPasswordInput.dispatchEvent(new Event("input"));
        }
    });
}

if (confPasswordInput) {
    confPasswordInput.addEventListener("input", function() {
        const confPasswordValue = confPasswordInput.value;
        const passwordValue = passwordInput.value;

        if (confPasswordValue.length === 0) {
            confPasswordError.textContent = "";
            confPasswordInput.classList.remove("input-error");
        } else if (confPasswordValue !== passwordValue) {
            confPasswordError.textContent = "Le password non coincidono.";
            confPasswordInput.classList.add("input-error");
        } else {
            confPasswordError.textContent = "";
            confPasswordInput.classList.remove("input-error");
        }
    });
}

// 4. CONTROLLO FINALE AL CLICK SU "REGISTRATI"
if (regForm) {
    regForm.addEventListener("submit", function(event) {
        if (emailInput) emailInput.dispatchEvent(new Event("input"));
        if (passwordInput) passwordInput.dispatchEvent(new Event("input"));
        if (confPasswordInput) confPasswordInput.dispatchEvent(new Event("input"));

        const isEmailInvalid = emailInput.classList.contains("input-error") || emailInput.value.trim() === "";
        const isPasswordInvalid = passwordInput.classList.contains("input-error") || passwordInput.value === "";
        const isConfPasswordInvalid = confPasswordInput.classList.contains("input-error") || confPasswordInput.value === "";

        if (isEmailInvalid || isPasswordInvalid || isConfPasswordInvalid) {
            event.preventDefault();
        }
    });
}