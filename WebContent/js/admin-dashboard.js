function switchTab(panelId) {
    document.querySelectorAll('.hub-panel').forEach(panel => {
        panel.classList.remove('active');
    });
    
    document.querySelectorAll('.hub-btn').forEach(btn => {
        btn.classList.remove('active');
    });
    
    const targetPanel = document.getElementById(panelId);
	
    if (targetPanel) {
        targetPanel.classList.add('active');
    }
    
    const targetBtn = Array.from(document.querySelectorAll('.hub-btn')).find(btn => 
        btn.getAttribute('onclick') && btn.getAttribute('onclick').includes(panelId)
    );
	
    if (targetBtn) {
        targetBtn.classList.add('active');
    }
}

function filterTableByCategory() {
    const selectedFilter = document.getElementById("tableCategoryFilter").value;
    const rows = document.querySelectorAll(".product-row");
    
    rows.forEach(row => {
        const rowCategoryId = row.getAttribute("data-category-id");
        
        if (selectedFilter === "all" || rowCategoryId === selectedFilter) {
            row.style.display = "";
        } else {
            row.style.display = "none"; 
        }
    });
}

document.addEventListener("DOMContentLoaded", function() {
    const activePanelId = document.body.getAttribute("data-active-panel");
	
    if (activePanelId) {
        switchTab(activePanelId);
    }

    const orderForm = document.getElementById("orderFilterForm");

    if (orderForm) {
        orderForm.addEventListener("submit", function(event) {
            let isValid = true;
            const errorSpan = document.getElementById("orderFilterError");
            errorSpan.textContent = ""; 
            
            const startDateInput = document.getElementById("startDate");
            const endDateInput = document.getElementById("endDate");
            const customerInput = document.getElementById("customerQuery");
            
            startDateInput.style.borderColor = "";
            endDateInput.style.borderColor = "";
            customerInput.style.borderColor = "";

            if (startDateInput.value && endDateInput.value) {
                if (new Date(startDateInput.value) > new Date(endDateInput.value)) {
                    errorSpan.textContent = "Errore: La data di inizio non può essere successiva alla data di fine.";
                    startDateInput.style.borderColor = "red";
                    endDateInput.style.borderColor = "red";
                    startDateInput.focus(); 
                    isValid = false;
                }
            }

            if (!isValid) {
                event.preventDefault(); 
            }
        });
        
        const formInputs = orderForm.querySelectorAll('input');
		
        formInputs.forEach(input => {
            input.addEventListener('focus', function() {
                this.style.boxShadow = "0 0 5px #ff7b00";
            });
            input.addEventListener('blur', function() {
                this.style.boxShadow = "";
            });
        });
    }
});