document.addEventListener("DOMContentLoaded", () => {
    const categorySelect = document.getElementById("filter-category");
    const maxPriceInput = document.getElementById("filter-max-price");
    const sortBySelect = document.getElementById("filter-sort");
    const availableCheckbox = document.getElementById("filter-available");
    const clearFiltersBtn = document.getElementById("btn-clear-filters");

    const landingSections = document.getElementById("landing-sections-wrapper");
    const searchResultsWrapper = document.getElementById("search-results-wrapper");
    const ajaxContainer = document.getElementById("products-ajax-container");

    const sampleForm = document.querySelector(".product-cart-form");
	
	const priceDisplay = document.getElementById("product-price");
	
    const contextPath = sampleForm 
        ? sampleForm.getAttribute("action").split("/CartServlet")[0] 
        : "";

    async function applyFilters() {
        const categoryId = categorySelect.value;
        const maxPrice = maxPriceInput.value;
        const sortBy = sortBySelect.value;
        const onlyAvailable = availableCheckbox.checked;

        if (!categoryId && !maxPrice && sortBy === "default" && !onlyAvailable) {
            if (landingSections) landingSections.classList.remove("initially-hidden");
            if (searchResultsWrapper) searchResultsWrapper.classList.add("initially-hidden");
            return;
        }

        if (landingSections) landingSections.classList.add("initially-hidden");
        if (searchResultsWrapper) searchResultsWrapper.classList.remove("initially-hidden");
		
        const params = new URLSearchParams();
        if (categoryId) params.append("categoryId", categoryId);
        if (maxPrice) params.append("maxPrice", maxPrice);
        if (sortBy) params.append("sortBy", sortBy);
        if (onlyAvailable) params.append("onlyAvailable", onlyAvailable);
        params.append("ajax", "true");

		const url = `${contextPath}/CatalogServlet?${params.toString()}`;
		
		console.log("Sending AJAX Request to: ", url);
		
        try {
            const response = await fetch(url);
            
            if (!response.ok) {
                throw new Error("Network response error");
            }
            
            const html = await response.text();
            ajaxContainer.innerHTML = html;
            
            const counter = document.getElementById("results-counter");
            const itemsFound = ajaxContainer.querySelectorAll(".product-card").length;
            if (counter) {
                counter.innerText = `${itemsFound} prodotti trovati`;
            }
        } catch (error) {
            console.error(error);
        }
    }

    if (categorySelect) categorySelect.addEventListener("change", applyFilters);
    if (maxPriceInput) maxPriceInput.addEventListener("input", applyFilters);
    if (sortBySelect) sortBySelect.addEventListener("change", applyFilters);
    if (availableCheckbox) availableCheckbox.addEventListener("change", applyFilters);

    if (clearFiltersBtn) {
        clearFiltersBtn.addEventListener("click", () => {
            categorySelect.value = "";
            maxPriceInput.value = "";
            sortBySelect.value = "default";
            availableCheckbox.checked = false;
            applyFilters();
        });
    }
});