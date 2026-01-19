import { Component, inject, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule, Router } from '@angular/router';
import { DeliveryService } from '../../../../core/services/delivery.service';
import { AuthService } from '../../../../core/services/auth.service';
import { DeliveryStatus, Delivery, DeliveryPriority } from '../../../../core/models/delivery.model';

@Component({
  selector: 'app-courier-dashboard',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './courier-dashboard.component.html'
})
export class CourierDashboardComponent implements OnInit {
  deliveryService = inject(DeliveryService);
  authService = inject(AuthService);
  router = inject(Router);

  loading = signal(false);
  error = signal<string | null>(null);
  success = signal<string | null>(null);
  
  stats = signal({
    total: 0,
    created: 0,
    collected: 0,
    inStock: 0,
    inTransit: 0,
    delivered: 0
  });

  deliveries = signal<Delivery[]>([]);
  recentDeliveries = signal<Delivery[]>([]);

  currentPage = signal(0);
  pageSize = 10;
  totalElements = signal(0);
  totalPages = signal(0);

  ngOnInit() {
    if (!this.authService.isAuthenticated()) {
      this.router.navigate(['/auth/login']);
      return;
    }
    this.loadDeliveries();
  }

  loadDeliveries() {
    this.loading.set(true);
    this.error.set(null);

    this.deliveryService.getMyDeliveries(this.currentPage(), this.pageSize).subscribe({
      next: (res) => {
        if (res?.success && res.data) {
          const deliveries = res.data.content || [];
          this.deliveries.set(deliveries);
          this.recentDeliveries.set(deliveries.slice(0, 5));
          this.totalElements.set(res.data.totalElements || 0);
          this.totalPages.set(res.data.totalPages || 0);
          this.currentPage.set(res.data.number || 0);
          
          this.stats.set({
            total: res.data.totalElements || 0,
            created: deliveries.filter(d => d.status === DeliveryStatus.CREATED).length,
            collected: deliveries.filter(d => d.status === DeliveryStatus.COLLECTED).length,
            inStock: deliveries.filter(d => d.status === DeliveryStatus.IN_STOCK).length,
            inTransit: deliveries.filter(d => d.status === DeliveryStatus.IN_TRANSIT).length,
            delivered: deliveries.filter(d => d.status === DeliveryStatus.DELIVERED).length
          });
        }
        this.loading.set(false);
      },
      error: () => {
        this.error.set('Erreur de chargement des livraisons');
        this.loading.set(false);
      }
    });
  }

  onPageChange(page: number) {
    if (page >= 0 && page < this.totalPages()) {
      this.currentPage.set(page);
      this.loadDeliveries();
    }
  }

  updateStatus(deliveryId: number, newStatus: DeliveryStatus) {
    this.loading.set(true);
    this.error.set(null);
    this.success.set(null);

    this.deliveryService.updateDeliveryStatus(deliveryId, { status: newStatus }).subscribe({
      next: () => {
        this.success.set('Statut mis à jour avec succès');
        this.loadDeliveries();
        setTimeout(() => this.success.set(null), 3000);
      },
      error: (err) => {
        this.error.set(err.error?.message || 'Erreur lors de la mise à jour du statut');
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

  getPriorityClass(priority: DeliveryPriority): string {
    const classes: Record<DeliveryPriority, string> = {
      [DeliveryPriority.LOW]: 'bg-gray-100 text-gray-800',
      [DeliveryPriority.MEDIUM]: 'bg-yellow-100 text-yellow-800',
      [DeliveryPriority.HIGH]: 'bg-red-100 text-red-800'
    };
    return classes[priority] || 'bg-gray-100 text-gray-800';
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

  getPriorityLabel(priority: DeliveryPriority): string {
    const labels: Record<DeliveryPriority, string> = {
      [DeliveryPriority.LOW]: 'Basse',
      [DeliveryPriority.MEDIUM]: 'Moyenne',
      [DeliveryPriority.HIGH]: 'Haute'
    };
    return labels[priority] || priority;
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

  getCourierName(): string {
    const user = this.authService.getStoredUser();
    if (user?.personResponse) {
      return `${user.personResponse.firstName} ${user.personResponse.lastName}`;
    }
    return 'Livreur';
  }

  getNextStatus(currentStatus: DeliveryStatus): DeliveryStatus | null {
    const statusFlow: Record<DeliveryStatus, DeliveryStatus | null> = {
      [DeliveryStatus.CREATED]: DeliveryStatus.COLLECTED,
      [DeliveryStatus.COLLECTED]: DeliveryStatus.IN_STOCK,
      [DeliveryStatus.IN_STOCK]: DeliveryStatus.IN_TRANSIT,
      [DeliveryStatus.IN_TRANSIT]: DeliveryStatus.DELIVERED,
      [DeliveryStatus.DELIVERED]: null
    };
    return statusFlow[currentStatus] || null;
  }

  canUpdateStatus(status: DeliveryStatus): boolean {
    return status !== DeliveryStatus.DELIVERED;
  }
}

