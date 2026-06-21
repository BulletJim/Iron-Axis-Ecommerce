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
        const errorSpan = document.getElementById("orderFilterError");
        const startDateInput = document.getElementById("startDate");
        const endDateInput = document.getElementById("endDate");
        const customerInput = document.getElementById("customerQuery");

        function formatData(e) {
            let val = e.target.value.replace(/\D/g, '');
            if (val.length > 2 && val.length <= 4) {
                val = val.substring(0, 2) + '/' + val.substring(2);
            } else if (val.length > 4) {
                val = val.substring(0, 2) + '/' + val.substring(2, 4) + '/' + val.substring(4, 8);
            }
            e.target.value = val;
        }

        function parseDate(dateStr) {
            const parts = dateStr.split('/');
            if (parts.length === 3) {
                return new Date(parts[2], parts[1] - 1, parts[0]);
            }
            return null;
        }

        function validateDates() {
            errorSpan.textContent = ""; 
            startDateInput.style.borderColor = "";
            endDateInput.style.borderColor = "";
            
            if (startDateInput.value.length === 10 && endDateInput.value.length === 10) {
                const dStart = parseDate(startDateInput.value);
                const dEnd = parseDate(endDateInput.value);

                if (dStart && dEnd && dStart > dEnd) {
                    errorSpan.textContent = "Errore: La data di inizio non può essere successiva alla data di fine.";
                    startDateInput.style.borderColor = "var(--error-color)";
                    endDateInput.style.borderColor = "var(--error-color)";
                    return false;
                }
            }
            return true;
        }

        startDateInput.addEventListener("input", formatData);
        endDateInput.addEventListener("input", formatData);

        startDateInput.addEventListener("change", validateDates);
        endDateInput.addEventListener("change", validateDates);

        orderForm.addEventListener("submit", function(event) {
            customerInput.style.borderColor = "";
            
            if (!validateDates()) {
                startDateInput.focus(); 
                event.preventDefault(); 
            }
        });
        
        const formInputs = orderForm.querySelectorAll('input');
		
        formInputs.forEach(input => {
            input.addEventListener('focus', function() {
                this.style.boxShadow = "0 0 5px var(--primary-color)";
            });
            input.addEventListener('blur', function() {
                this.style.boxShadow = "";
            });
        });
    }
});