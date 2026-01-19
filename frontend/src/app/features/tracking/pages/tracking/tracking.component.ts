import { Component, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { DeliveryService } from '../../../../core/services/delivery.service';
import { DeliveryTracking, DeliveryStatus } from '../../../../core/models/delivery.model';

@Component({
  selector: 'app-tracking',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './tracking.component.html'
})
export class TrackingComponent {
  deliveryService = inject(DeliveryService);

  trackingNumber = '';
  loading = signal(false);
  error = signal<string | null>(null);
  tracking = signal<DeliveryTracking | null>(null);

  search() {
    if (!this.trackingNumber.trim()) {
      this.error.set('Veuillez entrer un numéro de suivi');
      return;
    }

    this.loading.set(true);
    this.error.set(null);
    this.tracking.set(null);

    this.deliveryService.getDeliveryByTrackingNumber(this.trackingNumber.trim()).subscribe({
      next: (res) => {
        if (res?.success && res.data) {
          this.tracking.set(res.data);
        } else {
          this.error.set('Livraison non trouvée');
        }
        this.loading.set(false);
      },
      error: (err) => {
        if (err.status === 404) {
          this.error.set('Aucune livraison trouvée avec ce numéro de suivi');
        } else {
          this.error.set('Erreur lors de la recherche. Veuillez réessayer.');
        }
        this.loading.set(false);
      }
    });
  }

  getStatusClass(status: DeliveryStatus | string): string {
    const statusStr = String(status);
    const classes: Record<string, string> = {
      'CREATED': 'bg-blue-100 text-blue-800',
      'COLLECTED': 'bg-yellow-100 text-yellow-800',
      'IN_STOCK': 'bg-purple-100 text-purple-800',
      'IN_TRANSIT': 'bg-indigo-100 text-indigo-800',
      'DELIVERED': 'bg-green-100 text-green-800',
      'FAILED': 'bg-red-100 text-red-800',
      'CANCELLED': 'bg-gray-100 text-gray-800'
    };
    return classes[statusStr] || 'bg-gray-100 text-gray-800';
  }

  getStatusLabel(status: DeliveryStatus | string): string {
    const statusStr = String(status);
    const labels: Record<string, string> = {
      'CREATED': 'Créé',
      'COLLECTED': 'Collecté',
      'IN_STOCK': 'En stock',
      'IN_TRANSIT': 'En transit',
      'DELIVERED': 'Livré',
      'FAILED': 'Échoué',
      'CANCELLED': 'Annulé'
    };
    return labels[statusStr] || statusStr;
  }

  formatDate(dateString: string): string {
    return new Date(dateString).toLocaleDateString('fr-FR', {
      year: 'numeric',
      month: 'long',
      day: 'numeric',
      hour: '2-digit',
      minute: '2-digit'
    });
  }
}

