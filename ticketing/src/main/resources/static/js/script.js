/**
 * IT Support Ticket System - Custom JavaScript
 */

document.addEventListener('DOMContentLoaded', function() {
    // Initialize tooltips
    initializeTooltips();
    
    // Initialize popovers
    initializePopovers();
    
    // Auto-dismiss alerts after 5 seconds
    setupAlertDismissal();
    
    // Setup confirmation dialogs
    setupConfirmationDialogs();
    
    // Add active class to current nav item
    highlightCurrentNavItem();
    
    // Form validation
    setupFormValidation();
    
    // Status update dropdowns
    setupStatusChangeHandlers();
});

/**
 * Initialize Bootstrap tooltips
 */
function initializeTooltips() {
    const tooltipTriggerList = [].slice.call(document.querySelectorAll('[data-bs-toggle="tooltip"]'));
    tooltipTriggerList.forEach(function(tooltipTriggerEl) {
        new bootstrap.Tooltip(tooltipTriggerEl);
    });
}

/**
 * Initialize Bootstrap popovers
 */
function initializePopovers() {
    const popoverTriggerList = [].slice.call(document.querySelectorAll('[data-bs-toggle="popover"]'));
    popoverTriggerList.forEach(function(popoverTriggerEl) {
        new bootstrap.Popover(popoverTriggerEl);
    });
}

/**
 * Auto-dismiss alerts after delay
 */
function setupAlertDismissal() {
    const alerts = document.querySelectorAll('.alert:not(.alert-permanent)');
    alerts.forEach(function(alert) {
        setTimeout(function() {
            const bsAlert = new bootstrap.Alert(alert);
            bsAlert.close();
        }, 5000); // 5 seconds
    });
}

/**
 * Setup confirmation dialogs for delete actions
 */
function setupConfirmationDialogs() {
    const deleteButtons = document.querySelectorAll('[data-confirm]');
    deleteButtons.forEach(function(button) {
        button.addEventListener('click', function(e) {
            if (!confirm(this.dataset.confirm || 'Are you sure you want to perform this action?')) {
                e.preventDefault();
            }
        });
    });
}

/**
 * Highlight current nav item based on URL
 */
function highlightCurrentNavItem() {
    const currentPath = window.location.pathname;
    const navLinks = document.querySelectorAll('.navbar-nav .nav-link');
    
    navLinks.forEach(function(link) {
        const href = link.getAttribute('href');
        
        // Remove all active classes first
        link.classList.remove('active');
        
        // Special case for dashboard
        if (href === '/ticketing/dashboard' && currentPath === '/ticketing/dashboard') {
            link.classList.add('active');
        }
        // Special case for tickets
        else if (href === '/ticketing/tickets' && 
            (currentPath === '/ticketing/tickets' || 
             currentPath.startsWith('/ticketing/tickets/'))) {
            link.classList.add('active');
        }
        // Special case for users
        else if (href === '/ticketing/users' && 
            (currentPath === '/ticketing/users' || 
             currentPath.startsWith('/ticketing/users/'))) {
            link.classList.add('active');
        }
        // Exact match for other pages
        else if (href === currentPath) {
            link.classList.add('active');
        }
    });
}

/**
 * Setup form validation
 */
function setupFormValidation() {
    const forms = document.querySelectorAll('.needs-validation');
    
    Array.from(forms).forEach(function(form) {
        form.addEventListener('submit', function(event) {
            if (!form.checkValidity()) {
                event.preventDefault();
                event.stopPropagation();
            }
            
            form.classList.add('was-validated');
        }, false);
    });
    
    // Password confirmation validation
    const passwordField = document.getElementById('password');
    const confirmPasswordField = document.getElementById('confirmPassword');
    
    if (passwordField && confirmPasswordField) {
        confirmPasswordField.addEventListener('input', function() {
            if (passwordField.value !== confirmPasswordField.value) {
                confirmPasswordField.setCustomValidity("Passwords don't match");
            } else {
                confirmPasswordField.setCustomValidity('');
            }
        });
        
        passwordField.addEventListener('input', function() {
            if (confirmPasswordField.value) {
                if (passwordField.value !== confirmPasswordField.value) {
                    confirmPasswordField.setCustomValidity("Passwords don't match");
                } else {
                    confirmPasswordField.setCustomValidity('');
                }
            }
        });
    }
}

/**
 * Setup status change handlers for tickets
 */
function setupStatusChangeHandlers() {
    const statusDropdowns = document.querySelectorAll('.status-change-dropdown');
    
    statusDropdowns.forEach(function(dropdown) {
        dropdown.addEventListener('change', function() {
            if (this.dataset.autoSubmit === 'true') {
                this.closest('form').submit();
            }
        });
    });
}

/**
 * Format date as human-readable relative time
 * @param {string} dateString - ISO date string
 * @returns {string} Human readable relative time
 */
function formatRelativeTime(dateString) {
    const date = new Date(dateString);
    const now = new Date();
    const diffMs = now - date;
    const diffSecs = Math.floor(diffMs / 1000);
    const diffMins = Math.floor(diffSecs / 60);
    const diffHours = Math.floor(diffMins / 60);
    const diffDays = Math.floor(diffHours / 24);
    
    if (diffSecs < 60) {
        return 'just now';
    } else if (diffMins < 60) {
        return `${diffMins} minute${diffMins > 1 ? 's' : ''} ago`;
    } else if (diffHours < 24) {
        return `${diffHours} hour${diffHours > 1 ? 's' : ''} ago`;
    } else if (diffDays < 7) {
        return `${diffDays} day${diffDays > 1 ? 's' : ''} ago`;
    } else {
        return date.toLocaleDateString();
    }
}

/**
 * Create a chart for ticket statistics
 * @param {string} elementId - Canvas element ID
 * @param {string} type - Chart type ('bar', 'doughnut', etc.)
 * @param {Array} labels - Chart labels
 * @param {Array} data - Chart data values
 * @param {Array} colors - Chart colors
 */
function createChart(elementId, type, labels, data, colors) {
    const ctx = document.getElementById(elementId);
    
    if (!ctx) return;
    
    new Chart(ctx, {
        type: type,
        data: {
            labels: labels,
            datasets: [{
                data: data,
                backgroundColor: colors,
                borderWidth: 1
            }]
        },
        options: {
            responsive: true,
            maintainAspectRatio: false,
            plugins: {
                legend: {
                    position: 'bottom'
                }
            }
        }
    });
}