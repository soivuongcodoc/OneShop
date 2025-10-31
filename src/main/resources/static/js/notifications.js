// Notification dropdown functionality
class NotificationManager {
    constructor() {
        this.notificationBell = document.querySelector('.notification-bell');
        this.notificationDropdown = document.querySelector('.notification-dropdown');
        this.notificationList = document.querySelector('.notification-list');
        this.badge = document.querySelector('.notification-badge');
        this.markAllReadBtn = document.querySelector('.mark-all-read');
        
        if (this.notificationBell) {
            this.init();
        }
    }

    init() {
        // Load notification count on page load
        this.updateNotificationCount();
        
        // Load notifications when hovering over bell
        this.notificationBell.addEventListener('mouseenter', () => {
            this.loadNotifications();
        });

        // Mark all as read button
        if (this.markAllReadBtn) {
            this.markAllReadBtn.addEventListener('click', () => {
                this.markAllAsRead();
            });
        }

        // Close dropdown when clicking outside
        document.addEventListener('click', (e) => {
            if (!this.notificationBell.contains(e.target)) {
                this.notificationDropdown?.classList.remove('show');
            }
        });

        // Toggle dropdown on click
        this.notificationBell.addEventListener('click', (e) => {
            e.stopPropagation();
            this.notificationDropdown?.classList.toggle('show');
            this.loadNotifications();
        });

        // Refresh count every 30 seconds
        setInterval(() => this.updateNotificationCount(), 30000);
    }

    async updateNotificationCount() {
        try {
            const response = await fetch('/api/notifications/count', {
                headers: {
                    'Authorization': 'Bearer ' + localStorage.getItem('token')
                }
            });
            
            if (response.ok) {
                const data = await response.json();
                const count = data.count;
                
                if (count > 0) {
                    this.badge.textContent = count > 99 ? '99+' : count;
                    this.badge.style.display = 'flex';
                } else {
                    this.badge.style.display = 'none';
                }
            }
        } catch (error) {
            console.error('Error fetching notification count:', error);
        }
    }

    async loadNotifications() {
        try {
            const response = await fetch('/api/notifications', {
                headers: {
                    'Authorization': 'Bearer ' + localStorage.getItem('token')
                }
            });
            
            if (response.ok) {
                const notifications = await response.json();
                this.renderNotifications(notifications);
            }
        } catch (error) {
            console.error('Error loading notifications:', error);
        }
    }

    renderNotifications(notifications) {
        if (!this.notificationList) return;

        if (notifications.length === 0) {
            this.notificationList.innerHTML = `
                <div class="notification-empty">
                    <i class="bi bi-bell-slash"></i>
                    <p>Không có thông báo</p>
                </div>
            `;
            return;
        }

        this.notificationList.innerHTML = notifications.map(notif => {
            const readClass = notif.read ? 'read' : 'unread';
            const icon = this.getNotificationIcon(notif.type);
            const link = this.getNotificationLink(notif);
            const timeAgo = this.formatTimeAgo(notif.createdAt);

            return `
                <div class="notification-item ${readClass}" data-id="${notif.id}" data-link="${link}">
                    <div class="notification-icon ${notif.type.toLowerCase()}">
                        <i class="bi ${icon}"></i>
                    </div>
                    <div class="notification-content">
                        <p class="notification-message">${notif.message}</p>
                        <span class="notification-time">${timeAgo}</span>
                    </div>
                    ${!notif.read ? '<span class="unread-dot"></span>' : ''}
                </div>
            `;
        }).join('');

        // Add click handlers
        this.notificationList.querySelectorAll('.notification-item').forEach(item => {
            item.addEventListener('click', () => this.handleNotificationClick(item));
        });
    }

    getNotificationIcon(type) {
        const icons = {
            'NEW_ORDER': 'bi-cart-check',
            'ORDER_CONFIRMED': 'bi-check-circle',
            'ORDER_CANCELLED': 'bi-x-circle',
            'PRODUCT_DELETED': 'bi-trash'
        };
        return icons[type] || 'bi-bell';
    }

    getNotificationLink(notif) {
        switch (notif.type) {
            case 'NEW_ORDER':
                return `/vendor/orders/${notif.orderId}`;
            case 'ORDER_CONFIRMED':
            case 'ORDER_CANCELLED':
                return `/user/orders/${notif.orderId}`;
            case 'PRODUCT_DELETED':
                return `/vendor/products`;
            default:
                return '#';
        }
    }

    async handleNotificationClick(item) {
        const notifId = item.dataset.id;
        const link = item.dataset.link;

        // Mark as read
        if (item.classList.contains('unread')) {
            await this.markAsRead(notifId);
        }

        // Navigate to link
        if (link && link !== '#') {
            window.location.href = link;
        }
    }

    async markAsRead(notifId) {
        try {
            const response = await fetch(`/api/notifications/${notifId}/read`, {
                method: 'PUT',
                headers: {
                    'Authorization': 'Bearer ' + localStorage.getItem('token')
                }
            });
            
            if (response.ok) {
                this.updateNotificationCount();
            }
        } catch (error) {
            console.error('Error marking notification as read:', error);
        }
    }

    async markAllAsRead() {
        try {
            const response = await fetch('/api/notifications/read-all', {
                method: 'PUT',
                headers: {
                    'Authorization': 'Bearer ' + localStorage.getItem('token')
                }
            });
            
            if (response.ok) {
                this.loadNotifications();
                this.updateNotificationCount();
            }
        } catch (error) {
            console.error('Error marking all as read:', error);
        }
    }

    formatTimeAgo(dateString) {
        const date = new Date(dateString);
        const now = new Date();
        const seconds = Math.floor((now - date) / 1000);

        if (seconds < 60) return 'Vừa xong';
        if (seconds < 3600) return Math.floor(seconds / 60) + ' phút trước';
        if (seconds < 86400) return Math.floor(seconds / 3600) + ' giờ trước';
        if (seconds < 604800) return Math.floor(seconds / 86400) + ' ngày trước';
        
        return date.toLocaleDateString('vi-VN');
    }
}

// Initialize notification manager when DOM is ready
document.addEventListener('DOMContentLoaded', () => {
    new NotificationManager();
});
