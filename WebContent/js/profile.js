document.addEventListener("DOMContentLoaded", function() {

    const dobInputDisplay = document.getElementById("birthDateDisplay");
    
    const dobHidden = document.getElementById("birthDate");
    
    const dobError = document.getElementById("dobError");
    const form = document.getElementById("profileForm");

    if (dobInputDisplay) {
        dobInputDisplay.addEventListener("input", function(e) {
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
                    dobInputDisplay.classList.add("input-error");
                    dobHidden.value = ""; // Blocco l'invio
                } else {
                    dobError.textContent = "";
                    dobInputDisplay.classList.remove("input-error");
                    
                    const parts = value.split("/");
                    dobHidden.value = parts[2] + "-" + parts[1] + "-" + parts[0];
                }
            }
			else if (value.length > 0 && value.length < 10) {
                dobError.textContent = "Completa la data inserendo anche l'anno.";
                dobInputDisplay.classList.add("input-error");
                dobHidden.value = ""; 
            } 
			else {
                dobError.textContent = "";
                dobInputDisplay.classList.remove("input-error");
                dobHidden.value = "";
            }
        });
    }

    if (form) {
		
        form.addEventListener("submit", function(e) {
			
            if (dobInputDisplay && (dobInputDisplay.classList.contains("input-error") || (dobInputDisplay.value.length > 0 && dobInputDisplay.value.length < 10))) {
                e.preventDefault();
                dobError.textContent = "Correggi gli errori nella data prima di salvare.";
                dobInputDisplay.focus();
            }
        });
    }
});