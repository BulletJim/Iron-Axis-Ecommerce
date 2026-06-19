const regForm = document.getElementById("registerForm");
const emailInput = document.getElementById("email");
const passwordInput = document.getElementById("password");
const confPasswordInput = document.getElementById("confirmPassword");
const phoneInput = document.getElementById("phoneNumber");
const phoneError = document.getElementById("phoneError");

const emailError = document.getElementById("emailError");
const passwordError = document.getElementById("passwordError");
const confPasswordError = document.getElementById("confirmPasswordError");

const dobInput = document.getElementById("dob");
const dobError = document.getElementById("dobError");

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

document.addEventListener("DOMContentLoaded", function() {
    document.addEventListener("input", function(event) {
        
        if (event.target && event.target.name === "phoneNumber") {
            const phoneInput = event.target;
            const originalValue = phoneInput.value;

            let sanitizedValue = originalValue.replace(/[^0-9]/g, '');

            sanitizedValue = sanitizedValue.replace(/(?!^)\+/g, '');


            if (originalValue !== sanitizedValue) {
                phoneInput.value = sanitizedValue;
                phoneInput.classList.add("input-error");

                setTimeout(() => {
                    phoneInput.classList.remove("input-error");
                }, 500);

            } else {
                phoneInput.classList.remove("input-error");
            }
        }
    });

});


if (dobInput) {
    dobInput.addEventListener("input", function(e) {

        let value = e.target.value.replace(/\D/g, ''); 
        
        if (value.length > 8) {
            value = value.slice(0, 8);
        }
        
        if (value.length > 4) {
            value = value.replace(/(\d{2})(\d{2})(\d+)/, '$1/$2/$3');
        } else if (value.length > 2) {
            value = value.replace(/(\d{2})(\d+)/, '$1/$2');
        }
        
        e.target.value = value;
        
        if (value.length === 10) {
            const dateRegex = /^(0[1-9]|[12][0-9]|3[01])\/(0[1-9]|1[0-2])\/\d{4}$/;
            
            if (!dateRegex.test(value)) {
                dobError.textContent = "Data non valida. Usa un formato GG/MM/AAAA corretto.";
                dobInput.classList.add("input-error");
            } else {
                dobError.textContent = "";
                dobInput.classList.remove("input-error");
            }
        } else if (value.length > 0 && value.length < 10) {
            dobError.textContent = "Completa la data inserendo anche l'anno.";
            dobInput.classList.add("input-error");
        } else {
            dobError.textContent = "";
            dobInput.classList.remove("input-error");
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
            passwordError.textContent = "Usa 8-25 caratteri, con almeno una lettera e un numero.";
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

document.addEventListener("DOMContentLoaded", function() {
    
    document.addEventListener("input", function(event) {
        if (event.target.classList.contains("addr-prov")) {
            let val = event.target.value.replace(/[^A-Za-z]/g, ''); 
            event.target.value = val.substring(0, 2).toUpperCase();
        }
        if (event.target.classList.contains("addr-zip-code")) {
            let val = event.target.value.replace(/\D/g, ''); 
            event.target.value = val.substring(0, 5);
        }
    });

    const itiInstances = [];

    function initPhoneField(inputElement) {
        const iti = window.intlTelInput(inputElement, {
            utilsScript: "https://cdnjs.cloudflare.com/ajax/libs/intl-tel-input/18.2.1/js/utils.js",
            initialCountry: "it",
            preferredCountries: ["it", "us", "gb"]
        });
        
        itiInstances.push({ input: inputElement, iti: iti });

        const errorSpan = inputElement.closest('.form-group').querySelector('.phone-error');

        const validate = () => {
            if (inputElement.value.trim() === "") {
                if(errorSpan) errorSpan.textContent = "";
                inputElement.classList.remove("input-error");
                return true;
            }
            if (iti.isValidNumber()) {
                if(errorSpan) errorSpan.textContent = "";
                inputElement.classList.remove("input-error");
                return true;
            } else {
                if(errorSpan) errorSpan.textContent = "Numero non valido.";
                inputElement.classList.add("input-error");
                return false;
            }
        };

        inputElement.addEventListener('blur', validate);
        inputElement.addEventListener('countrychange', validate);
    }

    document.querySelectorAll(".phone-input").forEach(initPhoneField);

    const addPhoneBtn = document.getElementById("add-phone-btn");
    const phonesContainer = document.getElementById("phones-container");

    if (addPhoneBtn && phonesContainer) {
        addPhoneBtn.addEventListener("click", function() {
            const row = document.createElement("div");
            row.classList.add("phone-row");
            row.innerHTML = `
                <select name="phoneType" required>
                    <option value="MOBILE">Cellulare</option>
                    <option value="HOME">Casa</option>
                    <option value="WORK">Lavoro</option>
                </select>
                <input type="tel" name="phoneNumber" class="phone-input" placeholder="Inserisci il tuo numero" required>
                <button type="button" class="btn-remove" title="Rimuovi">-</button>
            `;
            phonesContainer.appendChild(row);
            
            const newInput = row.querySelector('.phone-input');
            initPhoneField(newInput);
        });
    }

    const addAddressBtn = document.getElementById("add-address-btn");
    const addressesContainer = document.getElementById("addresses-container");

    if (addAddressBtn && addressesContainer) {
        addAddressBtn.addEventListener("click", function() {
            const row = document.createElement("div");
            row.classList.add("address-group");
            row.innerHTML = `
                <div class="input-row">
                    <input type="text" name="street" placeholder="Via/Piazza" class="addr-street input-flex-1" required>
                    <input type="number" name="streetNumber" placeholder="N°" class="addr-civic input-civic" min="0" onkeydown="if(event.key === '-') event.preventDefault()" required>
                </div>
                <div class="input-row">
                    <input type="text" name="city" placeholder="Città" class="addr-city input-flex-1" required>
                    <input type="text" name="prov" placeholder="Prov" class="addr-prov input-prov" required>
                </div>
                <div class="input-row">
                    <input type="text" name="zipCode" placeholder="CAP" class="addr-zip-code input-zip-code" required>
                    <input type="text" name="country" placeholder="Nazione" class="addr-country input-flex-1" required>
                </div>
                <button type="button" class="btn-remove" title="Rimuovi">-</button>
            `;
            addressesContainer.appendChild(row);
        });
    }

    document.addEventListener("click", function(e) {
        if (e.target.classList.contains("btn-remove")) {
            const phoneRow = e.target.closest(".phone-row");
            if (phoneRow) {
                const inputToRemove = phoneRow.querySelector('.phone-input');
                const index = itiInstances.findIndex(obj => obj.input === inputToRemove);
                if (index > -1) itiInstances.splice(index, 1);
                
                phoneRow.remove();
            }

            const addressGroup = e.target.closest(".address-group");
            if (addressGroup) {
                addressGroup.remove();
            }
        }
    });

    if (regForm) {
        regForm.addEventListener("submit", function(e) {
            let allPhonesValid = true;

            itiInstances.forEach(instance => {
                if (instance.input.value.trim() !== "" && !instance.iti.isValidNumber()) {
                    allPhonesValid = false;
                    instance.input.classList.add("input-error");
                } else if (instance.iti.isValidNumber()) {
                    instance.input.value = instance.iti.getNumber();
                }
            });

            if (!allPhonesValid) {
                e.preventDefault();
                document.querySelector('.phone-error').textContent = "Correggi i numeri di telefono evidenziati.";
            }
        });
    }
});