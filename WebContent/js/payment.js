document.addEventListener("DOMContentLoaded", function () {
	
    const paymentForm = document.getElementById("paymentForm");
	
    if(!paymentForm)
		 return;

    const methodOptions = document.querySelectorAll(".method-option");
    const creditCardFields = document.getElementById("creditCardFields");
    
    const cardHolder = document.getElementById("cardHolder");
    const cardNumber = document.getElementById("cardNumber");
    const expDate = document.getElementById("expDate");
    const cvv = document.getElementById("cvv");

    const errHolder = document.getElementById("errHolder");
    const errCard = document.getElementById("errCard");
    const errExp = document.getElementById("errExp");
    const errCvv = document.getElementById("errCvv");

    methodOptions.forEach(option => {
		
        option.addEventListener("click", function () {
            methodOptions.forEach(opt => opt.classList.remove("active"));
            this.classList.add("active");
			
            const radioBtn = this.querySelector("input[type='radio']");
            radioBtn.checked = true;

            if (radioBtn.value === "PayPal") {
                creditCardFields.style.display = "none";
            } 
			else {
                creditCardFields.style.display = "block";
                cardHolder.focus();
            }
        });
    });

    paymentForm.addEventListener("submit", function (event) {
        const selectedMethod = document.querySelector("input[name='paymentMethod']:checked").value;
        
        if (selectedMethod === "PayPal"){
            return; 
        }

        let isValid = true;

        errHolder.style.display = "none";
        errCard.style.display = "none";
        errExp.style.display = "none";
        errCvv.style.display = "none";

        const holderValue = cardHolder.value.trim();
        const holderRegex = /^[A-Za-zÀ-ù\s]{3,}\s[A-Za-zÀ-ù\s]{2,}$/;
		
        if (!holderRegex.test(holderValue)){
			
            errHolder.style.display = "block";
            cardHolder.focus();
            isValid = false;
        }

        const cardValue = cardNumber.value.trim();
        const cardRegex = /^\d{16}$/;
		
        if (!cardRegex.test(cardValue)) {
            if (isValid) {
                errCard.style.display = "block";
                cardNumber.focus();
            }
			 else {
                errCard.style.display = "block";
            }
            isValid = false;
        }

        const expValue = expDate.value.trim();
        const expRegex = /^(0[1-9]|1[0-2])\/([0-9]{2})$/;
		
        if (!expRegex.test(expValue)){
            if (isValid) {
                errExp.style.display = "block";
                expDate.focus();
            } 
			else {
                errExp.style.display = "block";
            }
            isValid = false;
        } 
		else {
            const matches = expValue.match(expRegex);
            const month = parseInt(matches[1], 10);
            const year = parseInt("20" + matches[2], 10);
            
            const now = new Date();
            const currentMonth = now.getMonth() + 1; 
            const currentYear = now.getFullYear();

            if (year < currentYear || (year === currentYear && month < currentMonth)){
				
                if (isValid){
                    errExp.innerHTML = "La carta di credito inserita risulta scaduta!";
                    errExp.style.display = "block";
                    expDate.focus();
                } 
				else {
                    errExp.innerHTML = "La carta di credito inserita risulta scaduta!";
                    errExp.style.display = "block";
                }
                isValid = false;
            }
        }

        const cvvValue = cvv.value.trim();
        const cvvRegex = /^\d{3}$/;
		
        if (!cvvRegex.test(cvvValue)) {
            if (isValid) {
                errCvv.style.display = "block";
                cvv.focus();
            } 
			else {
                errCvv.style.display = "block";
            }
            isValid = false;
        }

        if (!isValid) {
            event.preventDefault();
        }
    });

    if(expDate) {
		
        expDate.addEventListener("input", function () {
            let cleanInput = this.value.replace(/\D/g, ''); 
			
            if (cleanInput.length > 2) {
                this.value = cleanInput.substring(0, 2) + '/' + cleanInput.substring(2, 4);
            }
			 else {
                this.value = cleanInput;
            }
        });
    }
});