import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../../environment';
import { ApiResponse } from '../models/user.model';
import { SenderResponse, UpdatePersonRequest } from '../models/delivery.model';

@Injectable({
  providedIn: 'root'
})
export class ClientService {
  private http = inject(HttpClient);
  private apiUrl = environment.apiUrl;

  getAllClients(page: number = 0, size: number = 10): Observable<ApiResponse<{ content: SenderResponse[]; totalElements: number; totalPages: number; number: number; size: number }>> {
    let params = new HttpParams();
    params = params.set('page', page.toString());
    params = params.set('size', size.toString());

    return this.http.get<ApiResponse<{ content: SenderResponse[]; totalElements: number; totalPages: number; number: number; size: number }>>(
      `${this.apiUrl}persons/senders`,
      { params }
    );
  }

  getClientById(id: string | number): Observable<ApiResponse<SenderResponse>> {
    return this.http.get<ApiResponse<SenderResponse>>(
      `${this.apiUrl}persons/sender/${id}`
    );
  }

  updateClient(id: string | number, request: UpdatePersonRequest): Observable<ApiResponse<SenderResponse>> {
    return this.http.put<ApiResponse<SenderResponse>>(
      `${this.apiUrl}persons/sender/${id}`,
      request
    );
  }

  deleteClient(id: string | number): Observable<ApiResponse<void>> {
    return this.http.delete<ApiResponse<void>>(
      `${this.apiUrl}persons/sender/${id}`
    );
  }
}

