import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { BehaviorSubject, Observable, tap } from 'rxjs';
import { Router } from '@angular/router';
import { environment } from '../../../../environment';
import { AuthData, AuthResponse, LoginRequest, RegisterRequest } from '../models/user.model';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private http = inject(HttpClient);
  private router = inject(Router);
  private apiUrl = environment.apiUrl;

  private currentUserSubject = new BehaviorSubject<AuthData | null>(this.getStoredUser());
  currentUser$ = this.currentUserSubject.asObservable();

  login(credentials: LoginRequest): Observable<AuthResponse> {
    return this.http.post<AuthResponse>(
      `${this.apiUrl}${environment.auth.login}`,
      credentials
    ).pipe(
      tap(response => {
        if (response.success && response.data) {
          localStorage.setItem('access_token', response.data.tokenPair.accessToken);
          localStorage.setItem('refresh_token', response.data.tokenPair.refreshToken);
          localStorage.setItem('user', JSON.stringify(response.data));
          this.currentUserSubject.next(response.data);
        }
      })
    );
  }

  refresh(refreshToken: string): Observable<AuthResponse> {
    return this.http.post<AuthResponse>(
      `${this.apiUrl}${environment.auth.refresh}`,
      { refreshToken }
    ).pipe(
      tap(response => {
        if (response.success && response.data) {
          localStorage.setItem('access_token', response.data.tokenPair.accessToken);
          localStorage.setItem('refresh_token', response.data.tokenPair.refreshToken);
          localStorage.setItem('user', JSON.stringify(response.data));
          this.currentUserSubject.next(response.data);
        }
      })
    );
  }

  logout(): void {
    localStorage.clear();
    this.currentUserSubject.next(null);
    this.router.navigate(['/auth/login']);
  }

  getAccessToken(): string | null {
    return localStorage.getItem('access_token');
  }

  getRefreshToken(): string | null {
    return localStorage.getItem('refresh_token');
  }

  getStoredUser(): AuthData | null {
    const userStr = localStorage.getItem('user');
    return userStr ? JSON.parse(userStr) : null;
  }

  isAuthenticated(): boolean {
    return !!this.getAccessToken();
  }

  register(request: RegisterRequest): Observable<AuthResponse> {
    return this.http.post<AuthResponse>(
      `${this.apiUrl}${environment.auth.register}`,
      request
    ).pipe(
      tap(response => {
        if (response.success && response.data) {
          localStorage.setItem('access_token', response.data.tokenPair.accessToken);
          localStorage.setItem('refresh_token', response.data.tokenPair.refreshToken);
          localStorage.setItem('user', JSON.stringify(response.data));
          this.currentUserSubject.next(response.data);
        }
      })
    );
  }

  redirectBasedOnRole(role: string): void {
    if (role === 'ROLE_MANAGER' || role === 'ROLE_ADMIN') {
      this.router.navigate(['/dashboard']);
    } else if (role === 'ROLE_SENDER') {
      console.log('Redirecting to sender deliveries');
      this.router.navigate(['/my-deliveries']);
    } else if (role === 'ROLE_COURIER') {
      this.router.navigate(['/my-deliveries']);
    } else {
      this.router.navigate(['/dashboard']);
    }
  }

  get currentUserValue(): AuthData | null {
    return this.currentUserSubject.value;
  }
}