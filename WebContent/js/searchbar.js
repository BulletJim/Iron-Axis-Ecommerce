document.addEventListener('DOMContentLoaded', () => {
	
	const searchBox = document.getElementById('searchBox');
	const searchSuggestions = document.getElementById('searchSuggestions');

	    if (searchBox && searchSuggestions) {
	        
	        searchBox.addEventListener('input', function() {
	            const query = this.value.trim();

	            if (query.length === 0) {
	                searchSuggestions.style.display = 'none';
	                searchSuggestions.innerHTML = '';
	                return;
	            }

	          
	            fetch(`SearchBarServlet?search=${encodeURIComponent(query)}`)   //La chiamata asincrona a searchBarServlet
	                .then(response => response.json())
	                .then(products => {
	                    searchSuggestions.innerHTML = ''; 

	                    if (products.length === 0) {
	                        searchSuggestions.style.display = 'none';
							
	                        return;
	                    }

	                    products.forEach(product => {
							
	                        const suggestionDiv = document.createElement('div');
							
	                        suggestionDiv.className = 'suggestion-item';
	                        suggestionDiv.textContent = product.name;

	                        
	                        suggestionDiv.addEventListener('click', function() {  //Se si va a cliccare sul suggerimento autocompleta e reindirizza
	                           
								 searchBox.value = product.name;
	                            searchSuggestions.style.display = 'none';
	                            window.location.href = `ProductServlet?id=${product.id}`;
	                        });

	                        searchSuggestions.appendChild(suggestionDiv);
	                    });

	                    searchSuggestions.style.display = 'block'; 
	                })
	                .catch(error => console.error('Errore durante il recupero dei suggerimenti:', error));
	        });

	        
	        document.addEventListener('click', function(e) {    //Se si clicca al di fuori del menù a tendina esso si chiude
				
	            if (!searchBox.contains(e.target) && !searchSuggestions.contains(e.target)) {
	                searchSuggestions.style.display = 'none';
	            }
	        });
	    }
	});