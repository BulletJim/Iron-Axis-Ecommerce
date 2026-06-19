document.addEventListener("DOMContentLoaded", () => {
    
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