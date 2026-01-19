import { Component, inject, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { DeliveryService } from '../../../../core/services/delivery.service';
import { AuthService } from '../../../../core/services/auth.service';
import { Delivery, DeliveryStatus, DeliveryPriority } from '../../../../core/models/delivery.model';

@Component({
  selector: 'app-my-deliveries',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './my-deliveries.component.html'
})
export class MyDeliveriesComponent implements OnInit {
  deliveryService = inject(DeliveryService);
  authService = inject(AuthService);

  deliveries = signal<Delivery[]>([]);
  loading = signal(false);
  error = signal<string | null>(null);

  currentPage = signal(0);
  pageSize = 10;
  totalElements = signal(0);
  totalPages = signal(0);

  ngOnInit() {
    this.loadDeliveries();
  }

  loadDeliveries() {
    this.loading.set(true);
    this.error.set(null);

    this.deliveryService.getMyDeliveries(this.currentPage(), this.pageSize).subscribe({
      next: (res) => {
        if (res?.success && res.data) {
          this.deliveries.set(res.data.content || []);
          this.totalElements.set(res.data.totalElements || 0);
          this.totalPages.set(res.data.totalPages || 0);
          this.currentPage.set(res.data.number || 0);
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

  formatDate(date: string): string {
    return new Date(date).toLocaleDateString('fr-FR', {
      year: 'numeric',
      month: 'short',
      day: 'numeric',
      hour: '2-digit',
      minute: '2-digit'
    });
  }
}

