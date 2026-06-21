document.addEventListener("DOMContentLoaded", () => {
    
    const quantityControls = document.querySelectorAll('.quantity-controls');
    
    quantityControls.forEach(control => {
        const minusBtn = control.querySelector('.minus-btn');
        const plusBtn = control.querySelector('.plus-btn');
        const input = control.querySelector('.ajax-quantity');

        if (minusBtn && plusBtn && input) {
            minusBtn.addEventListener('click', () => {
                let currentValue = parseInt(input.value);
                let min = parseInt(input.min) || 1;
                if (currentValue > min) {
                    input.value = currentValue - 1;
                    input.dispatchEvent(new Event('change'));
                }
            });

            plusBtn.addEventListener('click', () => {
                let currentValue = parseInt(input.value);
                let max = parseInt(input.max) || 99;
                if (currentValue < max) {
                    input.value = currentValue + 1;
                    input.dispatchEvent(new Event('change'));
                }
            });
        }
    });

    const quantityInputs = document.querySelectorAll('.ajax-quantity');
    
    quantityInputs.forEach(input => {
        input.addEventListener('change', async function() {
            const sku = this.getAttribute('data-sku');
            const price = parseFloat(this.getAttribute('data-price'));
            let newQuantity = parseInt(this.value);
            
            if (newQuantity < 1 || isNaN(newQuantity)) {
                newQuantity = 1;
                this.value = 1;
            }

            const formData = new URLSearchParams();
            formData.append('action', 'updateQuantity');
            formData.append('productSku', sku);
            formData.append('quantity', newQuantity);
            formData.append('ajax', 'true'); 

            try {
                const response = await fetch(`CartServlet`, {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/x-www-form-urlencoded',
                    },
                    body: formData.toString()
                });
                
                if (!response.ok) {
                    throw new Error(`HTTP Error: ${response.status}`);
                }

                const data = await response.json();

                if (data.status === 'success') {
                    const newItemTotal = price * newQuantity;
                    document.getElementById('subtotal-' + sku).innerText = newItemTotal.toFixed(2);

                    let grandTotal = 0.0;
                    document.querySelectorAll('.item-subtotal').forEach(subElement => {
                        grandTotal += parseFloat(subElement.innerText);
                    });
                    document.getElementById('cart-grand-total').innerText = grandTotal.toFixed(2);
                }
            } catch (error) {
                console.error('cart updating error:', error);
            }
        });
    });
});