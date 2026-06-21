document.addEventListener("DOMContentLoaded", function(){
	
    const productData = document.getElementById("product-data");
    let productVariants = [];
    let contextPath = "";
	
    if(productData){
        try{
            productVariants = JSON.parse(productData.dataset.variants);
            /* CORRETTO: contecontextpath -> contextpath */
            contextPath = productData.dataset.contextpath; 
        } catch(e){
            console.error("productData parsing error", e);
        }
    } 
	
    const flavourMenu = document.getElementById("flavourMenu");
    const sizeMenu = document.getElementById("sizeMenu");
    const priceDisplay = document.getElementById("product-price");
    const stockDisplay = document.getElementById("stock-status");
    const productImage = document.getElementById("main-product-image");
    const nutrTableImage = document.getElementById("nutritional-table-img");
    const addToCartBtn = document.getElementById("add-to-cart-btn");
    const skuInput = document.getElementById("selected-sku");
	
    function UpdateProductVariant(){
		
        if(!productVariants || productVariants.length == 0) return;
		
        const selectedFlavour = flavourMenu ? flavourMenu.value : null;
        const selectedSize = sizeMenu ? sizeMenu.value : null;
		
        let matchedVariant = productVariants.find(v => 
            (flavourMenu ? v.flavour === selectedFlavour : true) &&
            (sizeMenu ? v.size === selectedSize : true)
        );
		
        if(!matchedVariant){
            matchedVariant = productVariants.find(v =>
                flavourMenu ? v.flavour === selectedFlavour : true
            );
        }
		
        if(matchedVariant){
            if(skuInput) skuInput.value = matchedVariant.sku;
            if(priceDisplay) priceDisplay.textContent = matchedVariant.price.toFixed(2);
			
            if(productImage && matchedVariant.urlImage && matchedVariant.urlImage.trim() != null){
                productImage.src = contextPath + "/" + matchedVariant.urlImage;
            }
			
            if(nutrTableImage){
                if(productImage && matchedVariant.nutrTableUrl && matchedVariant.nutrTableUrl.trim() != null){
                    nutrTableImage.src = contextPath + "/" + matchedVariant.nutrTableUrl;
                    nutrTableImage.classList.remove("d-none");
                } else{
                    nutrTableImage.classList.add("d-none");
                }
            }
			
            if(matchedVariant.quantity > 0){
                stockDisplay.textContent = "Disponibile";
                stockDisplay.classList.remove("stock-out");
                stockDisplay.classList.add("stock-available");
				
                if(addToCartBtn){
                    addToCartBtn.disabled = false;
                    addToCartBtn.style.backgroundColor = "var(--primary-color)";
                    addToCartBtn.style.color = "#fff";
                    addToCartBtn.textContent = "Aggiungi Al Carrello";
                }
            } else{
                stockDisplay.textContent = "Non Disponibile";
                stockDisplay.classList.remove("stock-available");
                stockDisplay.classList.add("stock-out");
				
                if(addToCartBtn){
                    addToCartBtn.disabled = true;
                    addToCartBtn.style.backgroundColor = "var(--border-color)";
                    addToCartBtn.style.color = "var(--text-dark)";
                    addToCartBtn.textContent = "Esaurito";
                }
            }	
        }
    }
	
    if(flavourMenu) flavourMenu.addEventListener("change", UpdateProductVariant);
    if(sizeMenu) sizeMenu.addEventListener("change", UpdateProductVariant);
	
    UpdateProductVariant();
	
    const btnTabDesc = document.getElementById("btn-tab-desc");
    const btnTabRev = document.getElementById("btn-tab-rev");
    const contentDesc = document.getElementById("tab-content-desc");
    const contentRev = document.getElementById("tab-content-rev");

    if (btnTabDesc && btnTabRev && contentDesc && contentRev) {

        btnTabDesc.addEventListener("click", function() {
            contentDesc.classList.add("active");
            contentRev.classList.remove("active");

            btnTabDesc.classList.add("active");
            btnTabRev.classList.remove("active");
        });

        btnTabRev.addEventListener("click", function() {
            contentRev.classList.add("active");
            contentDesc.classList.remove("active");

            btnTabRev.classList.add("active");
            btnTabDesc.classList.remove("active");
        });
    }
	
	const minusBtn = document.querySelector(".minus-btn");
	    const plusBtn = document.querySelector(".plus-btn");
	    const qtyInput = document.getElementById("quantity-input");

	    if (minusBtn && plusBtn && qtyInput) {
	        minusBtn.addEventListener("click", function() {
	            let currentValue = parseInt(qtyInput.value);
	            let min = parseInt(qtyInput.min) || 1;
	            if (currentValue > min) {
	                qtyInput.value = currentValue - 1;
	            }
	        });

	        plusBtn.addEventListener("click", function() {
	            let currentValue = parseInt(qtyInput.value);
	            let max = parseInt(qtyInput.max) || 10;
	            if (currentValue < max) {
	                qtyInput.value = currentValue + 1;
	            }
	        });
	    }
	
});

