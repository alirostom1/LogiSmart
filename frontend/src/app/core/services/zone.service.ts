import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../../environment';
import { ApiResponse } from '../models/user.model';
import { ZoneResponse, CreateZoneRequest, CreateZoneWithPostalCodesRequest, PostalCodesRequest } from '../models/delivery.model';

@Injectable({
  providedIn: 'root'
})
export class ZoneService {
  private http = inject(HttpClient);
  private apiUrl = environment.apiUrl;

  getAllZones(page: number = 0, size: number = 100): Observable<ApiResponse<{ content: ZoneResponse[]; totalElements: number; totalPages: number; number: number; size: number }>> {
    let params = new HttpParams();
    params = params.set('page', page.toString());
    params = params.set('size', size.toString());

    return this.http.get<ApiResponse<{ content: ZoneResponse[]; totalElements: number; totalPages: number; number: number; size: number }>>(
      `${this.apiUrl}zones`,
      { params }
    );
  }

  getZoneById(id: number): Observable<ApiResponse<ZoneResponse>> {
    return this.http.get<ApiResponse<ZoneResponse>>(
      `${this.apiUrl}zones/${id}`
    );
  }

  createZone(request: CreateZoneRequest): Observable<ApiResponse<ZoneResponse>> {
    return this.http.post<ApiResponse<ZoneResponse>>(
      `${this.apiUrl}zones`,
      request
    );
  }

  updateZone(id: number, request: CreateZoneRequest): Observable<ApiResponse<ZoneResponse>> {
    return this.http.put<ApiResponse<ZoneResponse>>(
      `${this.apiUrl}zones/${id}`,
      request
    );
  }

  deleteZone(id: number): Observable<ApiResponse<void>> {
    return this.http.delete<ApiResponse<void>>(
      `${this.apiUrl}zones/${id}`
    );
  }

  createZoneWithPostalCodes(request: CreateZoneWithPostalCodesRequest): Observable<ApiResponse<ZoneResponse>> {
    return this.http.post<ApiResponse<ZoneResponse>>(
      `${this.apiUrl}zones/with-postal-codes`,
      request
    );
  }

  addPostalCodesToZone(zoneId: number, request: PostalCodesRequest): Observable<ApiResponse<void>> {
    return this.http.post<ApiResponse<void>>(
      `${this.apiUrl}zones/${zoneId}/postal-codes`,
      request
    );
  }

  removePostalCodesFromZone(zoneId: number, request: PostalCodesRequest): Observable<ApiResponse<void>> {
    return this.http.request<ApiResponse<void>>(
      'DELETE',
      `${this.apiUrl}zones/${zoneId}/postal-codes`,
      { body: request }
    );
  }
}

