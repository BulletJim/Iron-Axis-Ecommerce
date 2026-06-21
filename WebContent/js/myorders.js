function toggleDetails(rowId) {
    const row = document.getElementById(rowId);
    if (row.style.display === 'none' || row.style.display === '') {
        row.style.display = 'table-row';
    }
	 else {
        row.style.display = 'none';
    }
}