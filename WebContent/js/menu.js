document.addEventListener('DOMContentLoaded', () => {
    
    const sidebar = document.getElementById('sidebar'); 
    const hamburgerBtn = document.getElementById('menuToggle'); 
    const closeBtn = document.getElementById('sidebar-close-btn'); 
    const backdrop = document.getElementById('sidebar-backdrop');  
    
    const openSidebar = () => {
        if (sidebar && backdrop) {
            sidebar.classList.add('sidebar-open');
            backdrop.classList.add('backdrop-open');
            
            document.body.style.overflow = 'hidden';     // Blocca lo scroll della pagina sottostante
        }
    };

    const closeSidebar = () => {
        if (sidebar && backdrop) {
            sidebar.classList.remove('sidebar-open');
            backdrop.classList.remove('backdrop-open');
            
            document.body.style.overflow = '';      // Ripristina lo scroll
        }
    };
    
    if (hamburgerBtn) {
        hamburgerBtn.addEventListener('click', openSidebar);    // Apertura tramite l'icona hamburger
    }

    if (closeBtn) {
        closeBtn.addEventListener('click', closeSidebar);     // Chiusura tramite la "X" interna
    }

    if (backdrop) {
        backdrop.addEventListener('click', closeSidebar);   // Chiusura cliccando sul velo scuro
    }

    document.addEventListener('keydown', (e) => {
        if (e.key === 'Escape' && sidebar && sidebar.classList.contains('sidebar-open')) {      // Chiusura premendo il tasto ESC sulla tastiera
            closeSidebar();
        }
    });

    const submenuToggles = document.querySelectorAll('.submenu-toggle');

    submenuToggles.forEach(toggle => {
        toggle.addEventListener('click', function(e) {
            
            if (window.innerWidth < 992) {
                e.preventDefault();
                
                const parentLi = this.parentElement;
                parentLi.classList.toggle('submenu-open');

                submenuToggles.forEach(otherToggle => {
                    if (otherToggle !== this) {
                        otherToggle.parentElement.classList.remove('submenu-open');
                    }
                });
            }
        });
    });
});