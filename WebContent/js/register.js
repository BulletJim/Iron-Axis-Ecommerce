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

document.addEventListener("DOMContentLoaded", function(){
    
    const phonesContainer = document.getElementById("phones-container");
    const addPhoneBtn = document.getElementById("add-phone-btn");
    
    const addressesContainer = document.getElementById("addresses-container");
    const addAddressBtn = document.getElementById("add-address-btn");
    
    function initPhoneInput(inputElement){
        return window.intlTelInput(inputElement, {
            initialCountry: "it",
            preferredCountries: ["it", "us", "gb", "fr", "de", "es"],
            utilsScript: "https://cdnjs.cloudflare.com/ajax/libs/intl-tel-input/18.2.1/js/utils.js"
        });
    }
    
    const defaultPhoneInput = document.querySelector(".phone-row .phone-number");
    if(defaultPhoneInput){
        initPhoneInput(defaultPhoneInput);
    }
    
    if(addPhoneBtn){
        addPhoneBtn.addEventListener("click", function(){
            const newRow = document.createElement("div");
            newRow.className = "dynamic-row phone-row";
            
            newRow.innerHTML = `
			<select name="phoneType" class="input-phone-type" required>
				<option value="MOBILE">Cellulare</option>
			    <option value="HOME">Casa</option>
			    <option value="WORK">Lavoro</option>
			</select>
			<input type="tel" name="phoneNumber" class="phone-number" placeholder="Inserisci il tuo numero" required>
            <button type="button" class="btn-remove">-</button>
            `;
            
            phonesContainer.appendChild(newRow);
            
            const newInput = newRow.querySelector(".phone-number");
            initPhoneInput(newInput);
        });
    }
    
    if (addAddressBtn) {
        addAddressBtn.addEventListener("click", function() {
            const row = document.createElement("div");
            row.className = "address-group";
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
                phoneRow.remove();
            }

            const addressGroup = e.target.closest(".address-group");
            if (addressGroup) {
                addressGroup.remove();
            }
        }
    });
});