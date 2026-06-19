document.addEventListener('DOMContentLoaded', () => {
    const methodOptions = document.querySelectorAll('.method-option');
    const creditCardFields = document.getElementById('creditCardFields');
    const paymentForm = document.getElementById('paymentForm');
    const btnSubmit = document.querySelector('.btn-submit');
    
    let isAmexGlobal = false;

    // Gestione Selezione Metodo
    methodOptions.forEach(option => {
        option.addEventListener('click', () => {
            methodOptions.forEach(opt => opt.classList.remove('active'));
            option.classList.add('active');
            const radio = option.querySelector('input[type="radio"]');
            radio.checked = true;

            if (radio.value === 'PayPal') {
                creditCardFields.classList.add('is-hidden');
                btnSubmit.innerHTML = '<i class="fa-brands fa-paypal"></i> Paga con PayPal';
				btnSubmit.style.backgroundColor = '#0070BA';
            } else if (radio.value === 'ApplePay'){
                creditCardFields.classList.add('is-hidden');
                btnSubmit.innerHTML = '<i class="fa-brands fa-apple"></i> Paga con Apple Pay';
				btnSubmit.style.backgroundColor = '#000000';
            } else {
                creditCardFields.classList.remove('is-hidden');
                btnSubmit.innerHTML = '<i class="fa-solid fa-shield-halved"></i> Completa l\'acquisto';
				btnSubmit.style.backgroundColor = 'var(--primary-color)';
            }
        });
    });

    // Funzione Algoritmo di Luhn
    function luhnCheck(cardNumber) {
        let sum = 0;
        let isSecond = false;
        for (let i = cardNumber.length - 1; i >= 0; i--) {
            let digit = parseInt(cardNumber.charAt(i), 10);
            if (isSecond) {
                digit *= 2;
                if (digit > 9) digit -= 9;
            }
            sum += digit;
            isSecond = !isSecond;
        }
        return (sum % 10) === 0;
    }

    const cardNumberInput = document.getElementById('cardNumber');
    const cardIcon = document.getElementById('cardIcon');
    const expDateInput = document.getElementById('expDate');
    const cvvInput = document.getElementById('cvv');
    const cardMessage = document.getElementById('errCard');

    // Gestione Numero Carta
    if(cardNumberInput) {
        cardNumberInput.addEventListener('input', (e) => {
            let value = e.target.value.replace(/\D/g, '');
            let formattedValue = '';
            
            // 1. Identificazione Circuito
            let isVisa = value.startsWith('4');
            let isMastercard = /^5[1-5]/.test(value) || /^2[2-7]/.test(value);
            let isAmex = /^3[47]/.test(value);
            isAmexGlobal = isAmex;

            if (cvvInput) {
                if (isAmex) {
                    cvvInput.setAttribute('maxlength', '4');
                    cvvInput.placeholder = '1234';
                } else {
                    cvvInput.setAttribute('maxlength', '3');
                    cvvInput.placeholder = '123';
                    if (cvvInput.value.length > 3) {
                        cvvInput.value = cvvInput.value.substring(0, 3);
                    }
                }
            }
			
            // 2. Cambio Icona
            if (isVisa) {
                cardIcon.className = 'fa-brands fa-cc-visa';
                cardIcon.style.color = '#1a1f71';
            } else if (isMastercard) {
                cardIcon.className = 'fa-brands fa-cc-mastercard';
                cardIcon.style.color = '#eb001b';
            } else if (isAmex) {
                cardIcon.className = 'fa-brands fa-cc-amex';
                cardIcon.style.color = '#2e77bc';
            } else {
                cardIcon.className = 'fa-regular fa-credit-card';
                cardIcon.style.color = '';
            }
            
            // 3. Formattazione
            if (isAmex) {
                // Formato Amex (4-6-5)
                let parts = [];
                if (value.length > 0) parts.push(value.substring(0, 4));
                if (value.length > 4) parts.push(value.substring(4, 10));
                if (value.length > 10) parts.push(value.substring(10, 15));
                formattedValue = parts.join(' ');
            } else {
                // Formato Classico (4-4-4-4)
                for (let i = 0; i < value.length; i++) {
                    if (i > 0 && i % 4 === 0) formattedValue += ' ';
                    formattedValue += value[i];
                }
            }
            e.target.value = formattedValue;

            // 4. Validazione Real-Time
            let targetLength = isAmex ? 15 : 16;

            if (value.length === 0) {
                cardMessage.style.display = 'none';
            } else if (value.length === targetLength) {
                if (luhnCheck(value)) {
                    cardMessage.style.display = 'none'; 
                } else {
                    cardMessage.textContent = "Numero carta non valido.";
                    cardMessage.style.display = 'block';
                }
            } else if (value.length > targetLength) {
                cardMessage.textContent = "Il numero di carta inserito è troppo lungo.";
                cardMessage.style.display = 'block';
            } else {
                if (value.length >= 13 && (isVisa || isMastercard || isAmex)) {
                    cardMessage.textContent = `Questo circuito richiede esattamente ${targetLength} cifre.`;
                    cardMessage.style.display = 'block';
                } else {
                    cardMessage.style.display = 'none';
                }
            }
        });
    }

    // Gestione Scadenza
    if(expDateInput) {
        expDateInput.addEventListener('input', (e) => {
            let value = e.target.value.replace(/\D/g, '');
            if (value.length > 2) {
                value = value.substring(0, 2) + '/' + value.substring(2, 4);
            }
            e.target.value = value;
        });
    }
    
    // Gestione CVV
    if(cvvInput) {
        cvvInput.addEventListener('input', (e) => {
            e.target.value = e.target.value.replace(/\D/g, '');
        });
    }

    // Validazione Finale (Submit)
    if(paymentForm) {
        paymentForm.addEventListener('submit', (e) => {
            const isCardSelected = document.querySelector('input[name="paymentMethod"]:checked').value === 'Carta di Credito';
            
            if (isCardSelected) {
                let isValid = true;
                
                // Controllo Titolare
                const cardHolder = document.getElementById('cardHolder');
                const errHolder = document.getElementById('errHolder');
                if (cardHolder.value.trim().length < 3) {
                    errHolder.style.display = 'block';
                    isValid = false;
                } else {
                    errHolder.style.display = 'none';
                }

                // Controllo Numero Carta
                const cardNumber = document.getElementById('cardNumber');
                const errCard = document.getElementById('errCard');
                let cleanNumber = cardNumber.value.replace(/\s/g, '');
                let requiredLength = isAmexGlobal ? 15 : 16;
                
                if (cleanNumber.length !== requiredLength || !luhnCheck(cleanNumber)) {
                    errCard.textContent = "Inserisci un numero di carta valido.";
                    errCard.style.display = 'block';
                    isValid = false;
                } else {
                    errCard.style.display = 'none';
                }

                // Controllo Scadenza
                const expDate = document.getElementById('expDate');
                const errExp = document.getElementById('errExp');
                if (expDate.value.length !== 5) {
                    errExp.style.display = 'block';
                    isValid = false;
                } else {
                    errExp.style.display = 'none';
                }

                // Controllo CVV
                const cvv = document.getElementById('cvv');
                const errCvv = document.getElementById('errCvv');
                let requiredCvvLength = isAmexGlobal ? 4 : 3;
                
                if (cvv.value.length !== requiredCvvLength) {
                    errCvv.textContent = `Il CVV deve essere di ${requiredCvvLength} cifre.`;
                    errCvv.style.display = 'block';
                    isValid = false;
                } else {
                    errCvv.style.display = 'none';
                }

                // Blocca l'invio se ci sono errori
                if (!isValid) {
                    e.preventDefault();
                }
            }
        });
    }
});