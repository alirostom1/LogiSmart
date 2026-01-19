import { Component, inject, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ZoneService } from '../../../../core/services/zone.service';
import { ZoneResponse, CreateZoneRequest, CreateZoneWithPostalCodesRequest, PostalCodesRequest } from '../../../../core/models/delivery.model';

@Component({
  selector: 'app-zone-list',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './zone-list.component.html'
})
export class ZoneListComponent implements OnInit {
  zoneService = inject(ZoneService);

  zones = signal<ZoneResponse[]>([]);
  allZones = signal<ZoneResponse[]>([]);
  filteredZones = signal<ZoneResponse[]>([]);
  loading = signal(false);
  error = signal<string | null>(null);
  success = signal<string | null>(null);

  currentPage = signal(0);
  pageSize = 10;
  totalElements = signal(0);
  totalPages = signal(0);

  searchTerm = signal('');
  showCreateModal = signal(false);
  showCreateWithPostalCodesModal = signal(false);
  showEditModal = signal(false);
  showAddPostalCodesModal = signal(false);
  showRemovePostalCodesModal = signal(false);
  selectedZone = signal<ZoneResponse | null>(null);

  // Form data
  zoneForm: CreateZoneRequest = {
    name: '',
    code: ''
  };

  zoneWithPostalCodesForm: CreateZoneWithPostalCodesRequest = {
    name: '',
    code: '',
    postalCodes: []
  };

  postalCodesInput = signal('');
  postalCodesToRemove = signal<string[]>([]);

  ngOnInit() {
    this.loadZones();
  }

  loadZones() {
    this.loading.set(true);
    this.error.set(null);

    this.zoneService.getAllZones(0, 1000).subscribe({
      next: (res) => {
        if (res?.success && res.data) {
          this.allZones.set(res.data.content || []);
          this.applyFilter();
        }
        this.loading.set(false);
      },
      error: (err) => {
        this.error.set(err.error?.message || 'Erreur de chargement des zones');
        this.loading.set(false);
      }
    });
  }

  applyFilter() {
    const term = this.searchTerm().toLowerCase().trim();
    
    if (!term) {
      this.filteredZones.set(this.allZones());
    } else {
      const filtered = this.allZones().filter(zone => 
        zone.name.toLowerCase().includes(term) ||
        zone.code.toLowerCase().includes(term) ||
        zone.postalCodes.some(pc => pc.toLowerCase().includes(term))
      );
      this.filteredZones.set(filtered);
    }
    
    this.totalElements.set(this.filteredZones().length);
    this.totalPages.set(Math.ceil(this.filteredZones().length / this.pageSize));
    this.updateDisplayedZones();
  }

  updateDisplayedZones() {
    const start = this.currentPage() * this.pageSize;
    const end = start + this.pageSize;
    this.zones.set(this.filteredZones().slice(start, end));
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
      this.updateDisplayedZones();
    }
  }

  openCreateModal() {
    this.zoneForm = {
      name: '',
      code: ''
    };
    this.showCreateModal.set(true);
    this.error.set(null);
    this.success.set(null);
  }

  openCreateWithPostalCodesModal() {
    this.zoneWithPostalCodesForm = {
      name: '',
      code: '',
      postalCodes: []
    };
    this.postalCodesInput.set('');
    this.showCreateWithPostalCodesModal.set(true);
    this.error.set(null);
    this.success.set(null);
  }

  openAddPostalCodesModal(zone: ZoneResponse) {
    this.selectedZone.set(zone);
    this.postalCodesInput.set('');
    this.showAddPostalCodesModal.set(true);
    this.error.set(null);
    this.success.set(null);
  }

  openRemovePostalCodesModal(zone: ZoneResponse) {
    this.selectedZone.set(zone);
    this.postalCodesToRemove.set([]);
    this.showRemovePostalCodesModal.set(true);
    this.error.set(null);
    this.success.set(null);
  }

  openEditModal(zone: ZoneResponse) {
    this.selectedZone.set(zone);
    this.zoneForm = {
      name: zone.name,
      code: zone.code
    };
    this.showEditModal.set(true);
    this.error.set(null);
    this.success.set(null);
  }

  closeModals() {
    this.showCreateModal.set(false);
    this.showCreateWithPostalCodesModal.set(false);
    this.showEditModal.set(false);
    this.showAddPostalCodesModal.set(false);
    this.showRemovePostalCodesModal.set(false);
    this.selectedZone.set(null);
  }

  parsePostalCodes(input: string): string[] {
    return input
      .split(/[,\n]/)
      .map(pc => pc.trim())
      .filter(pc => pc.length > 0);
  }

  createZone() {
    if (!this.isFormValid()) {
      this.error.set('Veuillez remplir tous les champs requis');
      return;
    }

    this.loading.set(true);
    this.error.set(null);
    this.success.set(null);

    this.zoneService.createZone(this.zoneForm).subscribe({
      next: () => {
        this.success.set('Zone créée avec succès');
        this.closeModals();
        this.loadZones();
        setTimeout(() => this.success.set(null), 3000);
      },
      error: (err) => {
        this.error.set(err.error?.message || 'Erreur lors de la création de la zone');
        this.closeModals();
        this.loading.set(false);
      }
    });
  }

  updateZone() {
    const zone = this.selectedZone();
    if (!zone || !this.isFormValid()) {
      this.error.set('Veuillez remplir tous les champs requis');
      return;
    }

    this.loading.set(true);
    this.error.set(null);
    this.success.set(null);

    this.zoneService.updateZone(zone.id, this.zoneForm).subscribe({
      next: () => {
        this.success.set('Zone mise à jour avec succès');
        this.closeModals();
        this.loadZones();
        setTimeout(() => this.success.set(null), 3000);
      },
      error: (err) => {
        this.error.set(err.error?.message || 'Erreur lors de la mise à jour de la zone');
        this.closeModals();
        this.loading.set(false);
      }
    });
  }

  deleteZone(id: number) {
    if (!confirm('Êtes-vous sûr de vouloir supprimer cette zone ?')) {
      return;
    }

    this.loading.set(true);
    this.error.set(null);

    this.zoneService.deleteZone(id).subscribe({
      next: () => {
        this.success.set('Zone supprimée avec succès');
        this.loadZones();
        setTimeout(() => this.success.set(null), 3000);
      },
      error: (err) => {
        this.error.set(err.error?.message || 'Erreur lors de la suppression');
        this.loading.set(false);
      }
    });
  }

  isFormValid(): boolean {
    return !!(this.zoneForm.name && this.zoneForm.code);
  }

  formatDate(date: string): string {
    return new Date(date).toLocaleDateString('fr-FR', {
      year: 'numeric',
      month: 'short',
      day: 'numeric'
    });
  }

  createZoneWithPostalCodes() {
    const postalCodes = this.parsePostalCodes(this.postalCodesInput());
    if (!this.zoneWithPostalCodesForm.name || !this.zoneWithPostalCodesForm.code || postalCodes.length === 0) {
      this.error.set('Veuillez remplir tous les champs requis et ajouter au moins un code postal');
      return;
    }

    this.loading.set(true);
    this.error.set(null);
    this.success.set(null);

    const request: CreateZoneWithPostalCodesRequest = {
      ...this.zoneWithPostalCodesForm,
      postalCodes
    };

    this.zoneService.createZoneWithPostalCodes(request).subscribe({
      next: () => {
        this.success.set('Zone créée avec succès avec codes postaux');
        this.closeModals();
        this.loadZones();
        setTimeout(() => this.success.set(null), 3000);
      },
      error: (err) => {
        this.error.set(err.error?.message || 'Erreur lors de la création de la zone');
        this.closeModals();
        this.loading.set(false);
      }
    });
  }

  addPostalCodes() {
    const zone = this.selectedZone();
    if (!zone) return;

    const postalCodes = this.parsePostalCodes(this.postalCodesInput());
    if (postalCodes.length === 0) {
      this.error.set('Veuillez entrer au moins un code postal');
      return;
    }

    this.loading.set(true);
    this.error.set(null);
    this.success.set(null);

    this.zoneService.addPostalCodesToZone(zone.id, { postalCodes }).subscribe({
      next: () => {
        this.success.set('Codes postaux ajoutés avec succès');
        this.closeModals();
        this.loadZones();
        setTimeout(() => this.success.set(null), 3000);
      },
      error: (err) => {
        this.error.set(err.error?.message || 'Erreur lors de l\'ajout des codes postaux');
        this.closeModals();
        this.loading.set(false);
      }
    });
  }

  removePostalCodes() {
    const zone = this.selectedZone();
    if (!zone || this.postalCodesToRemove().length === 0) {
      this.error.set('Veuillez sélectionner au moins un code postal à supprimer');
      return;
    }

    this.loading.set(true);
    this.error.set(null);
    this.success.set(null);

    this.zoneService.removePostalCodesFromZone(zone.id, { postalCodes: this.postalCodesToRemove() }).subscribe({
      next: () => {
        this.success.set('Codes postaux supprimés avec succès');
        this.closeModals();
        this.loadZones();
        setTimeout(() => this.success.set(null), 3000);
      },
      error: (err) => {
        this.error.set(err.error?.message || 'Erreur lors de la suppression des codes postaux');
        this.closeModals();
        this.loading.set(false);
      }
    });
  }

  togglePostalCodeForRemoval(postalCode: string) {
    const current = this.postalCodesToRemove();
    if (current.includes(postalCode)) {
      this.postalCodesToRemove.set(current.filter(pc => pc !== postalCode));
    } else {
      this.postalCodesToRemove.set([...current, postalCode]);
    }
  }

  getPostalCodesDisplay(zone: ZoneResponse): string {
    if (!zone.postalCodes || zone.postalCodes.length === 0) {
      return 'Aucun code postal';
    }
    if (zone.postalCodes.length <= 5) {
      return zone.postalCodes.join(', ');
    }
    return `${zone.postalCodes.slice(0, 5).join(', ')}... (+${zone.postalCodes.length - 5})`;
  }
}
