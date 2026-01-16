import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable, timeout } from 'rxjs';
import { environment } from '../../../../environment';
import { ApiResponse } from '../models/user.model';
import {
  Delivery,
  DeliveryDetails,
  DeliveryTracking,
  CreateDeliveryRequest,
  SearchDeliveryRequest,
  UpdateDeliveryStatusRequest,
  AssignDeliveryRequest
} from '../models/delivery.model';

@Injectable({
  providedIn: 'root'
})
export class DeliveryService {
  private http = inject(HttpClient);
  private apiUrl = environment.apiUrl;

  createDelivery(request: CreateDeliveryRequest): Observable<ApiResponse<DeliveryDetails>> {
    return this.http.post<ApiResponse<DeliveryDetails>>(
      `${this.apiUrl}deliveries`,
      request
    );
  }

  getDeliveryById(id: number): Observable<ApiResponse<DeliveryDetails>> {
    return this.http.get<ApiResponse<DeliveryDetails>>(
      `${this.apiUrl}deliveries/${id}`
    );
  }

  getDeliveryByTrackingNumber(trackingNumber: string): Observable<ApiResponse<DeliveryTracking>> {
    return this.http.get<ApiResponse<DeliveryTracking>>(
      `${this.apiUrl}deliveries/tracking/${trackingNumber}`
    );
  }

  searchDeliveries(request: SearchDeliveryRequest): Observable<ApiResponse<{ content: Delivery[]; totalElements: number; totalPages: number; number: number; size: number }>> {
    let params = new HttpParams();
    
    // Add pagination params
    if (request.page !== undefined) params = params.set('page', request.page.toString());
    if (request.size !== undefined) params = params.set('size', request.size.toString());
    if (request.sortBy) params = params.set('sort', `${request.sortBy},${request.sortOrder || 'DESC'}`);
    
    // Build request body - only include search criteria, not pagination
    const requestBody: any = {};
    if (request.searchTerm) requestBody.searchTerm = request.searchTerm;
    if (request.status) requestBody.status = String(request.status); // Convert enum to string
    if (request.priority) requestBody.priority = String(request.priority); // Convert enum to string
    if (request.pickupZoneId) requestBody.pickupZoneId = request.pickupZoneId;
    if (request.deliveryZoneId) requestBody.deliveryZoneId = request.deliveryZoneId;
    if (request.city) requestBody.city = request.city;
    if (request.courierId) requestBody.courierId = request.courierId;
    if (request.senderId) requestBody.senderId = request.senderId;
    if (request.phone) requestBody.phone = request.phone;

    console.log('Searching deliveries with:', { requestBody, params: params.toString() });

    return this.http.post<ApiResponse<{ content: Delivery[]; totalElements: number; totalPages: number; number: number; size: number }>>(
      `${this.apiUrl}deliveries/search`,
      requestBody,
      { params }
    ).pipe(
      timeout(30000) // 30 second timeout
    );
  }

  updateDeliveryStatus(id: number, request: UpdateDeliveryStatusRequest): Observable<ApiResponse<DeliveryDetails>> {
    return this.http.patch<ApiResponse<DeliveryDetails>>(
      `${this.apiUrl}deliveries/${id}/status`,
      request
    );
  }

  deleteDelivery(id: number): Observable<ApiResponse<void>> {
    return this.http.delete<ApiResponse<void>>(
      `${this.apiUrl}deliveries/${id}`
    );
  }

  assignDelivery(id: number, courierId: number): Observable<ApiResponse<DeliveryDetails>> {
    return this.http.post<ApiResponse<DeliveryDetails>>(
      `${this.apiUrl}deliveries/${id}/assign`,
      { courierId }
    );
  }

  assignCollectingCourier(id: number, request: AssignDeliveryRequest): Observable<ApiResponse<DeliveryDetails>> {
    return this.http.patch<ApiResponse<DeliveryDetails>>(
      `${this.apiUrl}deliveries/${id}/assign-collecting-courier`,
      request
    );
  }

  assignShippingCourier(id: number, request: AssignDeliveryRequest): Observable<ApiResponse<DeliveryDetails>> {
    return this.http.patch<ApiResponse<DeliveryDetails>>(
      `${this.apiUrl}deliveries/${id}/assign-shipping-courier`,
      request
    );
  }
}
