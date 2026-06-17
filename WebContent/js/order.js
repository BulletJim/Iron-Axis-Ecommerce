document.addEventListener("DOMContentLoaded", function() {
    const toggleBtn = document.getElementById("toggleAddressFormBtn");
    const cancelBtn = document.getElementById("cancelAddressFormBtn");
    const formContainer = document.getElementById("inlineAddressFormContainer");
    const ajaxForm = document.getElementById("ajaxAddAddressForm");
    const errorDiv = document.getElementById("addressFormError");

    if (toggleBtn) {
        toggleBtn.addEventListener("click", () => {
            formContainer.classList.remove("is-hidden");
            toggleBtn.classList.add("is-hidden");
        });
    }

    if (cancelBtn) {
        cancelBtn.addEventListener("click", () => {
            formContainer.classList.add("is-hidden");
            if (toggleBtn) toggleBtn.classList.remove("is-hidden");
            
            ajaxForm.reset();
            errorDiv.classList.add("is-hidden");
        });
    }

    if (ajaxForm) {
        ajaxForm.addEventListener("submit", async function(e) {
            e.preventDefault();
            errorDiv.classList.add("is-hidden");
			
            const submitBtn = ajaxForm.querySelector('button[type="submit"]');
            const originalBtnText = submitBtn.textContent;

            submitBtn.disabled = true;
            submitBtn.textContent = "Salvataggio...";

            const formData = new URLSearchParams(new FormData(ajaxForm));

            try {
                const response = await fetch("<%= request.getContextPath() %>/AddressServlet", {
                    method: "POST",
                    headers: {
                        "Content-Type": "application/x-www-form-urlencoded"
                    },
                    body: formData.toString()
                });

                if (response.ok) {
                    window.location.reload();
                } else {
                    const errorText = await response.text();
                    throw new Error(errorText || "Internal Server Error");
                }

            } catch (error) {
                errorDiv.textContent = "Errore durante il salvataggio: " + error.message;
                errorDiv.classList.remove("is-hidden");

                submitBtn.disabled = false;
                submitBtn.textContent = originalBtnText;
            }
        });
    }
	
});