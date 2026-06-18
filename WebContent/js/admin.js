document.addEventListener("DOMContentLoaded", function() {
    const adminForm = document.getElementById("adminProductForm");

    if (adminForm) {
        adminForm.addEventListener("submit", function(event) {
            let isValid = true;
            let firstInvalidField = null;

            document.querySelectorAll(".error-inline").forEach(el => el.textContent = "");

            const nameInput = document.getElementById("name");
            const nameRegex = /^[a-zA-Z0-9\s\-_]{3,100}$/;
			
            if (!nameRegex.test(nameInput.value.trim())) {
                document.getElementById("nameError").textContent = "Inserisci un nome valido (minimo 3 caratteri alfanumerici).";
                isValid = false;
				
                if (!firstInvalidField) firstInvalidField = nameInput;
            }

         
            const categoryInput = document.getElementById("categoryId");
			
            if (categoryInput.value <= 0 || isNaN(categoryInput.value)) {
                document.getElementById("categoryError").textContent = "Seleziona un ID categoria valido.";
                isValid = false;
				
                if (!firstInvalidField) firstInvalidField = categoryInput;
            }

            const descInput = document.getElementById("description");
			
            if (descInput.value.trim().length < 10) {
                document.getElementById("descError").textContent = "La descrizione deve contenere almeno 10 caratteri.";
                isValid = false;
                if (!firstInvalidField) firstInvalidField = descInput;
            }

            if (!isValid) {
                event.preventDefault();
            }
        });
    }
});

function confirmDelete(event, formElement) {
    event.preventDefault();
	
    if (confirm("Sei assolutamente sicuro di voler rimuovere questo prodotto? Lo storico ordini non verrà alterato.")) {
        formElement.submit(); 
    }
}