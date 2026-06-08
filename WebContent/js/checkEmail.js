document.addEventListener("DOMContentLoaded", function() {

    const emailInput = document.querySelector("input[type='email']");
    const submitBtn = document.querySelector("button[type='submit']");
    const emailError = document.getElementById("emailError");

    if (emailInput) {

        emailInput.addEventListener("blur", async function() {

            const emailValue = emailInput.value.trim();

            if (emailValue == "") {
                return;
            }

            try {

                const params = new URLSearchParams();
                params.append("email", emailValue);

                const response = await fetch(`CheckEmailServlet?${params.toString()}`);

                if (!response.ok) {
                    throw new Error("Connection Error");
                }

                const data = await response.json();

                if (data.emailExists) {

                    emailInput.classList.add("input-error");
                    if (emailError) emailError.textContent = "Questa email già esiste";

                    submitBtn.disabled = true;
                    submitBtn.style.opacity = "0.5";
                    submitBtn.style.cursor = "not-allowed";

                } else {

                    emailInput.classList.remove("input-error");
                    if (emailError) emailError.textContent = "";

                    submitBtn.disabled = false;
                    submitBtn.style.opacity = "1";
                    submitBtn.style.cursor = "pointer";
                }

            } catch (error) {
                console.log("AJAX ERROR: ", error);
            }

        });
    }
});