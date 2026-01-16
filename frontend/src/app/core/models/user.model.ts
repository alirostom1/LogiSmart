export enum UserRole {
  ROLE_MANAGER = 'ROLE_MANAGER',
  ROLE_COURIER = 'ROLE_COURIER',
  ROLE_SENDER = 'ROLE_SENDER',
  ROLE_ADMIN = 'ROLE_ADMIN'
}

export interface LoginRequest {
  email: string;
  password: string;
}

export interface RegisterRequest {
  firstName: string;
  lastName: string;
  email: string;
  password: string;
  phone: string;
}

export interface TokenPair {
  accessToken: string;
  refreshToken: string;
  tokenType: string;
  accessTokenExpiresIn: number;
  refreshTokenExpiresIn: number;
}

export interface PersonResponse {
  id: string;
  lastName: string;
  firstName: string;
  email: string;
  phone: string;
}

export interface AuthData {
  tokenPair: TokenPair;
  personResponse: PersonResponse;
  role: UserRole;
}

export interface AuthResponse {
  success: boolean;
  message: string;
  data: AuthData;
  timestamp: number;
}

export interface ApiResponse<T> {
  success: boolean;
  message?: string;
  data?: T;
  timestamp?: number;
}

export interface RefreshRequest {
  refreshToken: string;
}