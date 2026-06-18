function switchTab(panelId) {
    // Nasconde tutti i pannelli dei moduli/tabelle
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