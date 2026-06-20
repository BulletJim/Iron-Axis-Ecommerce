document.addEventListener('DOMContentLoaded', () => {
    const searchBox = document.getElementById('searchBox');
    const searchSuggestions = document.getElementById('searchSuggestions');

    if (searchBox && searchSuggestions) {
        
        searchBox.addEventListener('input', function() {
            const query = this.value.trim();

            if (query.length < 3) {
                searchSuggestions.style.display = 'none';
                searchSuggestions.innerHTML = '';
                return;
            }

            fetch(`SearchBarServlet?search=${encodeURIComponent(query)}`)
                .then(response => response.json())
                .then(products => {
                    searchSuggestions.innerHTML = ''; 

                    // Se non ci sono prodotti pertinenti, nascondi il menu
                    if (products.length === 0) {
                        searchSuggestions.style.display = 'none';
                        return;
                    }

                    products.forEach(product => {
                        const suggestionDiv = document.createElement('div');
                        suggestionDiv.className = 'suggestion-item';
                        
                        suggestionDiv.innerHTML = `
                            <i class="fas fa-search search-icon-ajax"></i> 
                            <span class="search-text-ajax">${product.name}</span>
                        `;

                        suggestionDiv.addEventListener('click', function() {  
                            searchBox.value = product.name;
                            searchSuggestions.style.display = 'none';
                            window.location.href = `ProductServlet?id=${product.id}`;
                        });

                        searchSuggestions.appendChild(suggestionDiv);
                    });

                    searchSuggestions.style.display = 'block'; 
                })
                .catch(error => console.error('Errore durante la chiamata AJAX dei suggerimenti:', error));
        });

        document.addEventListener('click', function(e) {    
            if (!searchBox.contains(e.target) && !searchSuggestions.contains(e.target)) {
                searchSuggestions.style.display = 'none';
            }
        });
    }
});