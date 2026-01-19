import { Component, inject, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { CourierService } from '../../../../core/services/courier.service';
import { ZoneService } from '../../../../core/services/zone.service';
import { CourierResponse, ZoneResponse, CreateCourierRequest, UpdateCourierRequest } from '../../../../core/models/delivery.model';

@Component({
  selector: 'app-courier-list',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './courier-list.component.html'
})
export class CourierListComponent implements OnInit {
  courierService = inject(CourierService);
  zoneService = inject(ZoneService);
  router = inject(Router);

  couriers = signal<CourierResponse[]>([]);
  allCouriers = signal<CourierResponse[]>([]);
  filteredCouriers = signal<CourierResponse[]>([]);
  zones = signal<ZoneResponse[]>([]);
  loading = signal(false);
  error = signal<string | null>(null);
  success = signal<string | null>(null);

  currentPage = signal(0);
  pageSize = 10;
  totalElements = signal(0);
  totalPages = signal(0);

  searchTerm = signal('');
  showCreateModal = signal(false);
  showEditModal = signal(false);
  selectedCourier = signal<CourierResponse | null>(null);

  // Form data - separate forms for create and update
  createForm: CreateCourierRequest = {
    lastName: '',
    firstName: '',
    email: '',
    password: '',
    vehicle: '',
    phone: '',
    zoneId: 0
  };

  updateForm: UpdateCourierRequest = {
    lastName: '',
    firstName: '',
    vehicle: '',
    email: '',
    phoneNumber: '',
    zoneId: 0
  };

  ngOnInit() {
    this.loadCouriers();
    this.loadZones();
  }

  loadCouriers() {
    this.loading.set(true);
    this.error.set(null);

    this.courierService.getAllCouriers(0, 1000).subscribe({
      next: (res) => {
        if (res?.success && res.data) {
          this.allCouriers.set(res.data.content || []);
          this.applyFilter();
        }
        this.loading.set(false);
      },
      error: (err) => {
        this.error.set(err.error?.message || 'Erreur de chargement des livreurs');
        this.loading.set(false);
      }
    });
  }

  loadZones() {
    this.zoneService.getAllZones(0, 100).subscribe({
      next: (res) => {
        if (res?.success && res.data) {
          this.zones.set(res.data.content || []);
        }
      },
      error: () => {
        console.error('Failed to load zones');
      }
    });
  }

  applyFilter() {
    const term = this.searchTerm().toLowerCase().trim();
    
    if (!term) {
      this.filteredCouriers.set(this.allCouriers());
    } else {
      const filtered = this.allCouriers().filter(courier => 
        `${courier.firstName} ${courier.lastName}`.toLowerCase().includes(term) ||
        courier.email.toLowerCase().includes(term) ||
        courier.phoneNumber.toLowerCase().includes(term) ||
        courier.vehicle.toLowerCase().includes(term)
      );
      this.filteredCouriers.set(filtered);
    }
    
    this.totalElements.set(this.filteredCouriers().length);
    this.totalPages.set(Math.ceil(this.filteredCouriers().length / this.pageSize));
    this.updateDisplayedCouriers();
  }

  updateDisplayedCouriers() {
    const start = this.currentPage() * this.pageSize;
    const end = start + this.pageSize;
    this.couriers.set(this.filteredCouriers().slice(start, end));
  }

  onSearch() {
    this.currentPage.set(0);
    this.applyFilter();
  }

  onReset() {
    this.searchTerm.set('');
    this.currentPage.set(0);
    this.applyFilter();
  }

  onPageChange(page: number) {
    if (page >= 0 && page < this.totalPages()) {
      this.currentPage.set(page);
      this.updateDisplayedCouriers();
    }
  }

  openCreateModal() {
    this.createForm = {
      lastName: '',
      firstName: '',
      email: '',
      password: '',
      vehicle: '',
      phone: '',
      zoneId: 0
    };
    this.showCreateModal.set(true);
    this.error.set(null);
    this.success.set(null);
  }

  openEditModal(courier: CourierResponse) {
    this.selectedCourier.set(courier);
    this.updateForm = {
      lastName: courier.lastName,
      firstName: courier.firstName,
      vehicle: courier.vehicle,
      email: courier.email,
      phoneNumber: courier.phoneNumber,
      zoneId: courier.zone?.id || 0
    };
    this.showEditModal.set(true);
    this.error.set(null);
    this.success.set(null);
  }

  closeModals() {
    this.showCreateModal.set(false);
    this.showEditModal.set(false);
    this.selectedCourier.set(null);
  }

  createCourier() {
    if (!this.isCreateFormValid()) {
      this.error.set('Veuillez remplir tous les champs requis');
      return;
    }

    this.loading.set(true);
    this.error.set(null);
    this.success.set(null);

    this.courierService.createCourier(this.createForm).subscribe({
      next: () => {
        this.success.set('Livreur créé avec succès');
        this.closeModals();
        this.loadCouriers();
        setTimeout(() => this.success.set(null), 3000);
      },
      error: (err) => {
        this.error.set(err.error?.message || 'Erreur lors de la création du livreur');
        this.closeModals();
        this.loading.set(false);
      }
    });
  }

  updateCourier() {
    const courier = this.selectedCourier();
    if (!courier || !this.isUpdateFormValid()) {
      this.error.set('Veuillez remplir tous les champs requis');
      return;
    }

    this.loading.set(true);
    this.error.set(null);
    this.success.set(null);

    this.courierService.updateCourier(courier.id, this.updateForm).subscribe({
      next: () => {
        this.success.set('Livreur mis à jour avec succès');
        this.closeModals();
        this.loadCouriers();
        setTimeout(() => this.success.set(null), 3000);
      },
      error: (err) => {
        this.error.set(err.error?.message || 'Erreur lors de la mise à jour du livreur');
        this.closeModals();
        this.loading.set(false);
      }
    });
  }

  deleteCourier(id: number) {
    if (!confirm('Êtes-vous sûr de vouloir supprimer ce livreur ?')) {
      return;
    }

    this.loading.set(true);
    this.error.set(null);

    this.courierService.deleteCourier(id).subscribe({
      next: () => {
        this.success.set('Livreur supprimé avec succès');
        this.loadCouriers();
        setTimeout(() => this.success.set(null), 3000);
      },
      error: (err) => {
        this.error.set(err.error?.message || 'Erreur lors de la suppression');
        this.loading.set(false);
      }
    });
  }

  isCreateFormValid(): boolean {
    return !!(this.createForm.lastName && this.createForm.firstName && this.createForm.email && 
              this.createForm.password && this.createForm.vehicle && this.createForm.phone && 
              this.createForm.zoneId);
  }

  isUpdateFormValid(): boolean {
    return !!(this.updateForm.lastName && this.updateForm.firstName && this.updateForm.email && 
              this.updateForm.vehicle && this.updateForm.phoneNumber && this.updateForm.zoneId);
  }

  formatDate(date: string): string {
    return new Date(date).toLocaleDateString('fr-FR', {
      year: 'numeric',
      month: 'short',
      day: 'numeric'
    });
  }
}
