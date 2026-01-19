import { Component, inject, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule, Router } from '@angular/router';
import { DeliveryService } from '../../../../core/services/delivery.service';
import { AuthService } from '../../../../core/services/auth.service';
import { DeliveryStatus, Delivery } from '../../../../core/models/delivery.model';

@Component({
  selector: 'app-sender-dashboard',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './sender-dashboard.component.html'
})
export class SenderDashboardComponent implements OnInit {
  deliveryService = inject(DeliveryService);
  authService = inject(AuthService);
  router = inject(Router);

  loading = signal(false);
  error = signal<string | null>(null);
  
  stats = signal({
    total: 0,
    created: 0,
    collected: 0,
    inStock: 0,
    inTransit: 0,
    delivered: 0
  });

  recentDeliveries = signal<Delivery[]>([]);

  ngOnInit() {
    if (!this.authService.isAuthenticated()) {
      this.router.navigate(['/auth/login']);
      return;
    }
    this.loadStats();
  }

  loadStats() {
    this.loading.set(true);
    this.error.set(null);

    this.deliveryService.getMyDeliveries(0, 1000)
      .subscribe({
        next: (res) => {
          if (res?.success && res.data) {
            const deliveries = res.data.content || [];
            this.stats.set({
              total: res.data.totalElements || 0,
              created: deliveries.filter(d => d.status === DeliveryStatus.CREATED).length,
              collected: deliveries.filter(d => d.status === DeliveryStatus.COLLECTED).length,
              inStock: deliveries.filter(d => d.status === DeliveryStatus.IN_STOCK).length,
              inTransit: deliveries.filter(d => d.status === DeliveryStatus.IN_TRANSIT).length,
              delivered: deliveries.filter(d => d.status === DeliveryStatus.DELIVERED).length
            });
            this.recentDeliveries.set(deliveries.slice(0, 5));
          }
          this.loading.set(false);
        },
        error: () => {
          this.error.set('Erreur de chargement');
          this.loading.set(false);
        }
      });
  }

  getStatusClass(status: DeliveryStatus): string {
    const classes: Record<DeliveryStatus, string> = {
      [DeliveryStatus.CREATED]: 'bg-blue-100 text-blue-800',
      [DeliveryStatus.COLLECTED]: 'bg-yellow-100 text-yellow-800',
      [DeliveryStatus.IN_STOCK]: 'bg-purple-100 text-purple-800',
      [DeliveryStatus.IN_TRANSIT]: 'bg-indigo-100 text-indigo-800',
      [DeliveryStatus.DELIVERED]: 'bg-green-100 text-green-800'
    };
    return classes[status] || 'bg-gray-100 text-gray-800';
  }

  getStatusLabel(status: DeliveryStatus): string {
    const labels: Record<DeliveryStatus, string> = {
      [DeliveryStatus.CREATED]: 'Créé',
      [DeliveryStatus.COLLECTED]: 'Collecté',
      [DeliveryStatus.IN_STOCK]: 'En stock',
      [DeliveryStatus.IN_TRANSIT]: 'En transit',
      [DeliveryStatus.DELIVERED]: 'Livré'
    };
    return labels[status] || status;
  }

  formatDate(dateString: string): string {
    return new Date(dateString).toLocaleDateString('fr-FR', {
      year: 'numeric',
      month: 'short',
      day: 'numeric',
      hour: '2-digit',
      minute: '2-digit'
    });
  }

  getSenderName(): string {
    const user = this.authService.getStoredUser();
    if (user?.personResponse) {
      return `${user.personResponse.firstName} ${user.personResponse.lastName}`;
    }
    return 'Utilisateur';
  }
}

