import { Component, inject, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { DeliveryService } from '../../../../core/services/delivery.service';
import { ProductService } from '../../../../core/services/product.service';
import { CreateDeliveryRequest, DeliveryProductRequest, ProductResponse, DeliveryPriority } from '../../../../core/models/delivery.model';

@Component({
  selector: 'app-create-delivery',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './create-delivery.component.html'
})
export class CreateDeliveryComponent implements OnInit {
  deliveryService = inject(DeliveryService);
  productService = inject(ProductService);
  router = inject(Router);

  loading = signal(false);
  error = signal<string | null>(null);
  success = signal<string | null>(null);

  // Form data
  deliveryForm: CreateDeliveryRequest = {
    description: '',
    weight: 0,
    priority: 'MEDIUM',
    pickupAddress: '',
    pickupPostalCode: '',
    shippingAddress: '',
    shippingPostalCode: '',
    recipientData: {
      name: '',
      email: '',
      phone: ''
    },
    products: []
  };

  availableProducts = signal<ProductResponse[]>([]);
  selectedProducts: Array<{ product: ProductResponse | null; quantity: number }> = [];
  priorityOptions = ['LOW', 'MEDIUM', 'HIGH'];

  ngOnInit() {
    this.loadProducts();
    this.selectedProducts.push({ product: null, quantity: 1 });
  }

  loadProducts() {
    this.productService.getMyProducts(0, 1000).subscribe({
      next: (res) => {
        if (res?.success && res.data) {
          this.availableProducts.set(res.data.content || []);
        }
      },
      error: () => {
        console.error('Failed to load products');
      }
    });
  }

  addProductRow() {
    this.selectedProducts.push({ product: null, quantity: 1 });
  }

  removeProductRow(index: number) {
    this.selectedProducts.splice(index, 1);
  }

  createDelivery() {
    if (!this.isFormValid()) {
      this.error.set('Veuillez remplir tous les champs requis');
      return;
    }

    this.loading.set(true);
    this.error.set(null);
    this.success.set(null);

    // Build products array from selected products
    const products: DeliveryProductRequest[] = this.selectedProducts
      .filter(sp => sp.product && sp.quantity > 0)
      .map(sp => ({
        productId: sp.product!.id,
        quantity: sp.quantity
      }));

    const request: CreateDeliveryRequest = {
      ...this.deliveryForm,
      products: products.length > 0 ? products : undefined,
      priority: this.deliveryForm.priority || 'MEDIUM'
    };

    this.deliveryService.createDelivery(request).subscribe({
      next: (res) => {
        if (res?.success && res.data) {
          this.success.set('Livraison créée avec succès');
          setTimeout(() => {
            if (res.data) {
              this.router.navigate(['/deliveries', res.data.id]);
            }
          }, 2000);
        }
        this.loading.set(false);
      },
      error: (err) => {
        this.error.set(err.error?.message || 'Erreur lors de la création de la livraison');
        this.loading.set(false);
      }
    });
  }

  isFormValid(): boolean {
    return !!(
      this.deliveryForm.description &&
      this.deliveryForm.weight > 0 &&
      this.deliveryForm.pickupAddress &&
      this.deliveryForm.pickupPostalCode &&
      this.deliveryForm.shippingAddress &&
      this.deliveryForm.shippingPostalCode &&
      this.deliveryForm.recipientData.email &&
      this.deliveryForm.recipientData.phone
    );
  }

  goBack() {
    this.router.navigate(['/dashboard']);
  }
}

