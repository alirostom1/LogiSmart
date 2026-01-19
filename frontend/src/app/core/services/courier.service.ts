import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../../environment';
import { ApiResponse } from '../models/user.model';
import { CourierResponse, CreateCourierRequest, UpdateCourierRequest } from '../models/delivery.model';

@Injectable({
  providedIn: 'root'
})
export class CourierService {
  private http = inject(HttpClient);
  private apiUrl = environment.apiUrl;

  getAllCouriers(page: number = 0, size: number = 100): Observable<ApiResponse<{ content: CourierResponse[]; totalElements: number; totalPages: number; number: number; size: number }>> {
    let params = new HttpParams();
    params = params.set('page', page.toString());
    params = params.set('size', size.toString());

    return this.http.get<ApiResponse<{ content: CourierResponse[]; totalElements: number; totalPages: number; number: number; size: number }>>(
      `${this.apiUrl}couriers`,
      { params }
    );
  }

  getCourierById(id: number): Observable<ApiResponse<CourierResponse>> {
    return this.http.get<ApiResponse<CourierResponse>>(
      `${this.apiUrl}couriers/${id}`
    );
  }

  createCourier(request: CreateCourierRequest): Observable<ApiResponse<CourierResponse>> {
    return this.http.post<ApiResponse<CourierResponse>>(
      `${this.apiUrl}couriers`,
      request
    );
  }

  updateCourier(id: number, request: UpdateCourierRequest): Observable<ApiResponse<CourierResponse>> {
    return this.http.put<ApiResponse<CourierResponse>>(
      `${this.apiUrl}couriers/${id}`,
      request
    );
  }

  deleteCourier(id: number): Observable<ApiResponse<void>> {
    return this.http.delete<ApiResponse<void>>(
      `${this.apiUrl}couriers/${id}`
    );
  }

  getCouriersByZone(zoneId: number, page: number = 0, size: number = 100): Observable<ApiResponse<{ content: CourierResponse[]; totalElements: number; totalPages: number; number: number; size: number }>> {
    let params = new HttpParams();
    params = params.set('page', page.toString());
    params = params.set('size', size.toString());

    return this.http.get<ApiResponse<{ content: CourierResponse[]; totalElements: number; totalPages: number; number: number; size: number }>>(
      `${this.apiUrl}couriers/zone/${zoneId}`,
      { params }
    );
  }
}

