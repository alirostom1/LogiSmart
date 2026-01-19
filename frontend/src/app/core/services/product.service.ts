import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../../environment';
import { ApiResponse } from '../models/user.model';
import { ProductResponse, CreateProductRequest } from '../models/delivery.model';

@Injectable({
  providedIn: 'root'
})
export class ProductService {
  private http = inject(HttpClient);
  private apiUrl = environment.apiUrl;

  getMyProducts(page: number = 0, size: number = 100): Observable<ApiResponse<{ content: ProductResponse[]; totalElements: number; totalPages: number; number: number; size: number }>> {
    let params = new HttpParams();
    params = params.set('page', page.toString());
    params = params.set('size', size.toString());

    return this.http.get<ApiResponse<{ content: ProductResponse[]; totalElements: number; totalPages: number; number: number; size: number }>>(
      `${this.apiUrl}products/my-products`,
      { params }
    );
  }

  getProductById(id: number): Observable<ApiResponse<ProductResponse>> {
    return this.http.get<ApiResponse<ProductResponse>>(
      `${this.apiUrl}products/${id}`
    );
  }

  createProduct(request: CreateProductRequest): Observable<ApiResponse<ProductResponse>> {
    return this.http.post<ApiResponse<ProductResponse>>(
      `${this.apiUrl}products`,
      request
    );
  }

  updateProduct(id: number, request: CreateProductRequest): Observable<ApiResponse<ProductResponse>> {
    return this.http.put<ApiResponse<ProductResponse>>(
      `${this.apiUrl}products/${id}`,
      request
    );
  }

  deleteProduct(id: number): Observable<ApiResponse<void>> {
    return this.http.delete<ApiResponse<void>>(
      `${this.apiUrl}products/${id}`
    );
  }
}

