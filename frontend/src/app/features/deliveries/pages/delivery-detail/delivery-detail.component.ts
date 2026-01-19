import { Component, inject, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { DeliveryService } from '../../../../core/services/delivery.service';
import { CourierService } from '../../../../core/services/courier.service';
import { AuthService } from '../../../../core/services/auth.service';
import { DeliveryDetails, DeliveryStatus, CourierResponse } from '../../../../core/models/delivery.model';

@Component({
  selector: 'app-delivery-detail',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './delivery-detail.component.html'
})
export class DeliveryDetailComponent implements OnInit {
  deliveryService = inject(DeliveryService);
  courierService = inject(CourierService);
  authService = inject(AuthService);
  route = inject(ActivatedRoute);
  router = inject(Router);

  isSender(): boolean {
    const user = this.authService.getStoredUser();
    return user?.role === 'ROLE_SENDER';
  }

  isCourier(): boolean {
    const user = this.authService.getStoredUser();
    return user?.role === 'ROLE_COURIER';
  }

  isManager(): boolean {
    const user = this.authService.getStoredUser();
    return user?.role === 'ROLE_MANAGER' || user?.role === 'ROLE_ADMIN';
  }

  canUpdateStatus(): boolean {
    return !this.isSender();
  }

  canAssignCouriers(): boolean {
    return this.isManager();
  }

  delivery = signal<DeliveryDetails | null>(null);
  loading = signal(false);
  error = signal<string | null>(null);
  success = signal<string | null>(null);
  newStatus: string = ''; 
  availableCouriers = signal<CourierResponse[]>([]);
  selectedCollectingCourier: number | null = null;
  selectedShippingCourier: number | null = null;
  
  statusOptions = Object.values(DeliveryStatus);

  ngOnInit() {
    const id = this.route.snapshot.paramMap.get('id');
    if (id && !isNaN(+id)) {
      this.loadDelivery(+id);
    } else {
      this.error.set('ID de livraison invalide');
      this.loading.set(false);
    }
  }

  loadCouriersForZone(zoneId: number | null | undefined) {
    if (!zoneId || !this.canAssignCouriers()) {
      return;
    }

    const currentCouriers = this.availableCouriers();
    if (currentCouriers.length > 0 && currentCouriers[0].zone?.id === zoneId) {
      return; // Already loaded
    }

    this.courierService.getCouriersByZone(zoneId, 0, 100).subscribe({
      next: (res) => {
        if (res?.success && res.data) {
          this.availableCouriers.set(res.data.content || []);
        }
      },
      error: () => {
        console.error('Failed to load couriers for zone');
      }
    });
  }

  onCollectingCourierSelectOpen() {
    const delivery = this.delivery();
    if (delivery?.pickupZone?.id) {
      this.loadCouriersForZone(delivery.pickupZone.id);
    }
  }

  onShippingCourierSelectOpen() {
    const delivery = this.delivery();
    if (delivery?.shippingZone?.id) {
      this.loadCouriersForZone(delivery.shippingZone.id);
    }
  }

  loadDelivery(id: number) {
    this.loading.set(true);
    this.error.set(null);

    this.deliveryService.getDeliveryById(id).subscribe({
      next: (res) => {
        if (res?.success && res.data) {
          this.delivery.set(res.data);
          this.newStatus = res.data.status;
          this.selectedCollectingCourier = res.data.collectingCourier?.id || null;
          this.selectedShippingCourier = res.data.shippingCourier?.id || null;
        } else {
          this.error.set('Livraison non trouvée');
        }
        this.loading.set(false);
      },
      error: () => {
        this.error.set('Erreur de chargement');
        this.loading.set(false);
      }
    });
  }

  updateStatus() {
    const d = this.delivery();
    if (!d || !this.newStatus) return;

    this.loading.set(true);
    this.error.set(null);
    this.success.set(null);

    this.deliveryService.updateDeliveryStatus(d.id, { status: this.newStatus as DeliveryStatus }).subscribe({
      next: (res) => {
        if (res?.success && res.data) {
          this.delivery.set(res.data);
          this.success.set('Statut mis à jour');
          setTimeout(() => this.success.set(null), 3000);
        }
        this.loading.set(false);
      },
      error: () => {
        this.error.set('Erreur de mise à jour');
        this.loading.set(false);
      }
    });
  }

  assignCollectingCourier() {
    const d = this.delivery();
    if (!d || !this.selectedCollectingCourier) return;

    this.loading.set(true);
    this.error.set(null);
    this.success.set(null);

    this.deliveryService.assignCollectingCourier(d.id, { courierId: this.selectedCollectingCourier }).subscribe({
      next: (res) => {
        if (res?.success && res.data) {
          this.delivery.set(res.data);
          this.success.set('Livreur de collecte assigné avec succès');
          setTimeout(() => this.success.set(null), 3000);
        }
        this.loading.set(false);
      },
      error: (err) => {
        this.error.set(err.error?.message || 'Erreur lors de l\'assignation du livreur de collecte');
        this.loading.set(false);
      }
    });
  }

  assignShippingCourier() {
    const d = this.delivery();
    if (!d || !this.selectedShippingCourier) return;

    this.loading.set(true);
    this.error.set(null);
    this.success.set(null);

    this.deliveryService.assignShippingCourier(d.id, { courierId: this.selectedShippingCourier }).subscribe({
      next: (res) => {
        if (res?.success && res.data) {
          this.delivery.set(res.data);
          this.success.set('Livreur de livraison assigné avec succès');
          setTimeout(() => this.success.set(null), 3000);
        }
        this.loading.set(false);
      },
      error: (err) => {
        this.error.set(err.error?.message || 'Erreur lors de l\'assignation du livreur de livraison');
        this.loading.set(false);
      }
    });
  }

  getCourierName(courierId: number | null): string {
    if (!courierId) return '';
    const courier = this.availableCouriers().find(c => c.id === courierId);
    return courier ? `${courier.firstName} ${courier.lastName}` : '';
  }

  goBack() {
    if (this.isSender()) {
      this.router.navigate(['/deliveries/my-deliveries']);
    } else if (this.isCourier()) {
      this.router.navigate(['/dashboard/courier']);
    } else {
      this.router.navigate(['/deliveries']);
    }
  }

  getStatusClass(status: string): string {
    const classes: Record<string, string> = {
      'CREATED': 'bg-blue-100 text-blue-800',
      'COLLECTED': 'bg-yellow-100 text-yellow-800',
      'IN_STOCK': 'bg-purple-100 text-purple-800',
      'IN_TRANSIT': 'bg-indigo-100 text-indigo-800',
      'DELIVERED': 'bg-green-100 text-green-800'
    };
    return classes[status] || 'bg-gray-100 text-gray-800';
  }

  getPriorityClass(priority: string): string {
    const classes: Record<string, string> = {
      'LOW': 'bg-gray-100 text-gray-800',
      'MEDIUM': 'bg-yellow-100 text-yellow-800',
      'HIGH': 'bg-red-100 text-red-800'
    };
    return classes[priority] || 'bg-gray-100 text-gray-800';
  }

  getStatusLabel(status: string): string {
    const labels: Record<string, string> = {
      'CREATED': 'Créé',
      'COLLECTED': 'Collecté',
      'IN_STOCK': 'En stock',
      'IN_TRANSIT': 'En transit',
      'DELIVERED': 'Livré'
    };
    return labels[status] || status;
  }

  getPriorityLabel(priority: string): string {
    const labels: Record<string, string> = {
      'LOW': 'Basse',
      'MEDIUM': 'Moyenne',
      'HIGH': 'Haute'
    };
    return labels[priority] || priority;
  }

  formatDate(date: string): string {
    return new Date(date).toLocaleDateString('fr-FR', {
      year: 'numeric',
      month: 'long',
      day: 'numeric',
      hour: '2-digit',
      minute: '2-digit'
    });
  }
}