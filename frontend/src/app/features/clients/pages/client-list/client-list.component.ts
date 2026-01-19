import { Component, inject, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { ClientService } from '../../../../core/services/client.service';
import { SenderResponse } from '../../../../core/models/delivery.model';

@Component({
  selector: 'app-client-list',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './client-list.component.html'
})
export class ClientListComponent implements OnInit {
  clientService = inject(ClientService);
  router = inject(Router);

  clients = signal<SenderResponse[]>([]);
  allClients = signal<SenderResponse[]>([]);
  filteredClients = signal<SenderResponse[]>([]);
  loading = signal(false);
  error = signal<string | null>(null);

  currentPage = signal(0);
  pageSize = 10;
  totalElements = signal(0);
  totalPages = signal(0);

  searchTerm = signal('');

  ngOnInit() {
    this.loadClients();
  }

  loadClients() {
    this.loading.set(true);
    this.error.set(null);

    this.clientService.getAllClients(0, 1000).subscribe({
      next: (res) => {
        if (res?.success && res.data) {
          this.allClients.set(res.data.content || []);
          this.applyFilter();
        }
        this.loading.set(false);
      },
      error: (err) => {
        this.error.set(err.error?.message || 'Erreur de chargement des clients');
        this.loading.set(false);
      }
    });
  }

  applyFilter() {
    const term = this.searchTerm().toLowerCase().trim();
    
    if (!term) {
      this.filteredClients.set(this.allClients());
    } else {
      const filtered = this.allClients().filter(client => 
        `${client.firstName} ${client.lastName}`.toLowerCase().includes(term) ||
        client.email.toLowerCase().includes(term)
      );
      this.filteredClients.set(filtered);
    }
    
    // Update pagination
    this.totalElements.set(this.filteredClients().length);
    this.totalPages.set(Math.ceil(this.filteredClients().length / this.pageSize));
    this.updateDisplayedClients();
  }

  updateDisplayedClients() {
    const start = this.currentPage() * this.pageSize;
    const end = start + this.pageSize;
    this.clients.set(this.filteredClients().slice(start, end));
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
      this.updateDisplayedClients();
    }
  }

  deleteClient(id: string) {
    if (!confirm('Êtes-vous sûr de vouloir supprimer ce client ?')) {
      return;
    }

    this.loading.set(true);
    this.error.set(null);

    this.clientService.deleteClient(id).subscribe({
      next: () => {
        this.loadClients();
      },
      error: (err) => {
        this.error.set('Erreur lors de la suppression');
        this.loading.set(false);
      }
    });
  }

  formatDate(date: string): string {
    return new Date(date).toLocaleDateString('fr-FR', {
      year: 'numeric',
      month: 'short',
      day: 'numeric'
    });
  }
}
