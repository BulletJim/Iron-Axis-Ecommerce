document.addEventListener("DOMContentLoaded", function(){
	
	const productData = document.getElementById("product-data");
	let productVariants = [];
	let contextPath = "";
	
	if(productData){
		
		try{
			productVariants = JSON.parse(productData.dataset.variants);
			contextPath = productData.dataset.contecontextpath;
		} catch(e){
			consolr.error("productData parsing error", e);
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
		
		// Find exact variant
		let matchedVariant = productVariants.find(v => 
			(flavourMenu ? v.flavour === selectedFlavour : true) &&
			(sizeMenu ? v.size === selectedSize : true)
		);
		
		//Taste Fallback
		if(!matchedVariant){
			matchedVariant = productVariants.find(v =>
				flavourMenu ? v.flavour === selectedFlavour : true
			);
		}
		
		
		if(matchedVariant){
			if(skuInput) skuInput.value = matchedVariant.sku;
			if(priceDisplay) priceDisplay.textContent = matchedVariant.price.toFixed(2);
			
			//Loading product image
			if(productImage && matchedVariant.urlImage && matchedVariant.urlImage.trim() != null){
				productImage.src = contextPath + "/" + matchedVariant.urlImage;
			}
			
			//Loading Nutritional Table
			if(nutrTableImage){
				if(productImage && matchedVariant.nutrTableUrl && matchedVariant.nutrTableUrl.trim() != null){
					nutrTableImage.src = contextPath + "/" + matchedVariant.nutrTableUrl;
					nutrTableImage.classList.remove("d-none");
				} else{
					nutrTableImage.classList.add("d-none");
				}
			}
			
			// In stock - not in stock
			if(matchedVariant.quantity > 0){
				stockDisplay.textContent = "Disponibile";
				stockDisplay.classList.remove("stock-out");
				stockDisplay.classList.add("stock-available");
				
				if(addToCartBtn){
					addToCartBtn.disabled= false;
					addToCartBtn.style.backgroundColor = "#e44d26";
					addToCartBtn.style.color = "white";
					addToCartBtn.textContent = "Aggiungi Al Carrello";
				}
			} else{
				stockDisplay.textContent = "Non Disponibile";
				stockDisplay.classList.remove("stock-available");
				stockDisplay.classList.add("stock-out");
				
				if(addToCartBtn){
					addToCartBtn.disabled = true;
					addToCartBtn.style.backgroundColor = 'rgba(0, 0, 0, 0.1)';
					addToCartBtn.style.color = "black";
					addToCartBtn.textContent = "Esaurito";
				}
			}	
		}
	}
	
	// First time loading the page
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
	
});