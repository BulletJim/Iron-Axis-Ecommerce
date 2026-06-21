document.addEventListener("DOMContentLoaded", function() {
        const toast = document.getElementById("toast-message");
        if (toast) {
            setTimeout(() => {
                toast.classList.add("hide-toast");
                // Rimuove l'elemento dal DOM dopo l'animazione per evitare che blocchi i click
                setTimeout(() => toast.remove(), 500); 
            }, 4000);
        }
    });