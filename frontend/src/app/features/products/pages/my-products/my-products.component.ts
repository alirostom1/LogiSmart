import { Component, inject, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ProductService } from '../../../../core/services/product.service';
import { ProductResponse, CreateProductRequest } from '../../../../core/models/delivery.model';

@Component({
  selector: 'app-my-products',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './my-products.component.html'
})
export class MyProductsComponent implements OnInit {
  productService = inject(ProductService);

  products = signal<ProductResponse[]>([]);
  allProducts = signal<ProductResponse[]>([]);
  filteredProducts = signal<ProductResponse[]>([]);
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
  selectedProduct = signal<ProductResponse | null>(null);

  // Form data
  productForm: CreateProductRequest = {
    name: '',
    category: '',
    unitPrice: 0
  };

  ngOnInit() {
    this.loadProducts();
  }

  loadProducts() {
    this.loading.set(true);
    this.error.set(null);

    this.productService.getMyProducts(0, 1000).subscribe({
      next: (res) => {
        if (res?.success && res.data) {
          this.allProducts.set(res.data.content || []);
          this.applyFilter();
        }
        this.loading.set(false);
      },
      error: (err) => {
        this.error.set(err.error?.message || 'Erreur de chargement des produits');
        this.loading.set(false);
      }
    });
  }

  applyFilter() {
    const term = this.searchTerm().toLowerCase().trim();
    
    if (!term) {
      this.filteredProducts.set(this.allProducts());
    } else {
      const filtered = this.allProducts().filter(product => 
        product.name.toLowerCase().includes(term) ||
        (product.category && product.category.toLowerCase().includes(term))
      );
      this.filteredProducts.set(filtered);
    }
    
    this.totalElements.set(this.filteredProducts().length);
    this.totalPages.set(Math.ceil(this.filteredProducts().length / this.pageSize));
    this.updateDisplayedProducts();
  }

  updateDisplayedProducts() {
    const start = this.currentPage() * this.pageSize;
    const end = start + this.pageSize;
    this.products.set(this.filteredProducts().slice(start, end));
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
      this.updateDisplayedProducts();
    }
  }

  openCreateModal() {
    this.productForm = {
      name: '',
      category: '',
      unitPrice: 0
    };
    this.showCreateModal.set(true);
    this.error.set(null);
    this.success.set(null);
  }

  openEditModal(product: ProductResponse) {
    this.selectedProduct.set(product);
    this.productForm = {
      name: product.name,
      category: product.category || '',
      unitPrice: product.unitPrice
    };
    this.showEditModal.set(true);
    this.error.set(null);
    this.success.set(null);
  }

  closeModals() {
    this.showCreateModal.set(false);
    this.showEditModal.set(false);
    this.selectedProduct.set(null);
  }

  createProduct() {
    if (!this.isFormValid()) {
      this.error.set('Veuillez remplir tous les champs requis');
      return;
    }

    this.loading.set(true);
    this.error.set(null);
    this.success.set(null);

    const request: CreateProductRequest = {
      name: this.productForm.name,
      category: this.productForm.category || undefined,
      unitPrice: this.productForm.unitPrice
    };

    this.productService.createProduct(request).subscribe({
      next: () => {
        this.success.set('Produit créé avec succès');
        this.closeModals();
        this.loadProducts();
        setTimeout(() => this.success.set(null), 3000);
      },
      error: (err) => {
        this.error.set(err.error?.message || 'Erreur lors de la création du produit');
        this.closeModals();
        this.loading.set(false);
      }
    });
  }

  updateProduct() {
    const product = this.selectedProduct();
    if (!product || !this.isFormValid()) {
      this.error.set('Veuillez remplir tous les champs requis');
      return;
    }

    this.loading.set(true);
    this.error.set(null);
    this.success.set(null);

    const request: CreateProductRequest = {
      name: this.productForm.name,
      category: this.productForm.category || undefined,
      unitPrice: this.productForm.unitPrice
    };

    this.productService.updateProduct(product.id, request).subscribe({
      next: () => {
        this.success.set('Produit mis à jour avec succès');
        this.closeModals();
        this.loadProducts();
        setTimeout(() => this.success.set(null), 3000);
      },
      error: (err) => {
        this.error.set(err.error?.message || 'Erreur lors de la mise à jour du produit');
        this.closeModals();
        this.loading.set(false);
      }
    });
  }

  deleteProduct(id: number) {
    if (!confirm('Êtes-vous sûr de vouloir supprimer ce produit ?')) {
      return;
    }

    this.loading.set(true);
    this.error.set(null);

    this.productService.deleteProduct(id).subscribe({
      next: () => {
        this.success.set('Produit supprimé avec succès');
        this.loadProducts();
        setTimeout(() => this.success.set(null), 3000);
      },
      error: (err) => {
        this.error.set(err.error?.message || 'Erreur lors de la suppression');
        this.loading.set(false);
      }
    });
  }

  isFormValid(): boolean {
    return !!(this.productForm.name && this.productForm.unitPrice > 0);
  }

  formatPrice(price: number): string {
    return new Intl.NumberFormat('fr-FR', {
      style: 'currency',
      currency: 'EUR'
    }).format(price);
  }
}

