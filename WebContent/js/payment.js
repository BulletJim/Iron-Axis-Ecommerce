document.addEventListener('DOMContentLoaded', () => {
    const methodOptions = document.querySelectorAll('.method-option');
    const creditCardFields = document.getElementById('creditCardFields');
    const paymentForm = document.getElementById('paymentForm');

    methodOptions.forEach(option => {
        option.addEventListener('click', () => {
            methodOptions.forEach(opt => opt.classList.remove('active'));
            option.classList.add('active');
            const radio = option.querySelector('input[type="radio"]');
            radio.checked = true;

            if (radio.value === 'PayPal') {
                creditCardFields.classList.add('is-hidden');
            } else {
                creditCardFields.classList.remove('is-hidden');
            }
        });
    });

    const cardNumberInput = document.getElementById('cardNumber');
    const expDateInput = document.getElementById('expDate');
    const cvvInput = document.getElementById('cvv');

    if(cardNumberInput) {
        cardNumberInput.addEventListener('input', (e) => {
            let value = e.target.value.replace(/\D/g, '');
            let formattedValue = '';
            for (let i = 0; i < value.length; i++) {
                if (i > 0 && i % 4 === 0) formattedValue += ' ';
                formattedValue += value[i];
            }
            e.target.value = formattedValue;
        });
    }

    if(expDateInput) {
        expDateInput.addEventListener('input', (e) => {
            let value = e.target.value.replace(/\D/g, '');
            if (value.length > 2) {
                value = value.substring(0, 2) + '/' + value.substring(2, 4);
            }
            e.target.value = value;
        });
    }
    
    if(cvvInput) {
        cvvInput.addEventListener('input', (e) => {
            e.target.value = e.target.value.replace(/\D/g, '');
        });
    }

    if(paymentForm) {
        paymentForm.addEventListener('submit', (e) => {
            const isCardSelected = document.querySelector('input[name="paymentMethod"]:checked').value === 'Carta di Credito';
            
            if (isCardSelected) {
                let isValid = true;
                
                const cardHolder = document.getElementById('cardHolder');
                const errHolder = document.getElementById('errHolder');
                if (cardHolder.value.trim().length < 3) {
                    errHolder.style.display = 'block';
                    isValid = false;
                } else {
                    errHolder.style.display = 'none';
                }

                const cardNumber = document.getElementById('cardNumber');
                const errCard = document.getElementById('errCard');
                if (cardNumber.value.replace(/\s/g, '').length !== 16) {
                    errCard.style.display = 'block';
                    isValid = false;
                } else {
                    errCard.style.display = 'none';
                }

                const expDate = document.getElementById('expDate');
                const errExp = document.getElementById('errExp');
                if (expDate.value.length !== 5) {
                    errExp.style.display = 'block';
                    isValid = false;
                } else {
                    errExp.style.display = 'none';
                }

                const cvv = document.getElementById('cvv');
                const errCvv = document.getElementById('errCvv');
                if (cvv.value.length !== 3) {
                    errCvv.style.display = 'block';
                    isValid = false;
                } else {
                    errCvv.style.display = 'none';
                }

                if (!isValid) {
                    e.preventDefault();
                }
            }
        });
    }
});