import { ZoneResponse } from './delivery.model';

export interface Courier {
  id: number;
  lastName: string;
  firstName: string;
  vehicle: string;
  email: string;
  phoneNumber: string;
  zone: ZoneResponse | null;
  createdAt: string;
  updatedAt: string;
}

export interface CreateCourierRequest {
  lastName: string;
  firstName: string;
  email: string;
  password: string;
  vehicle: string;
  phone: string;
  zoneId: number;
}

export interface UpdateCourierRequest {
  lastName: string;
  firstName: string;
  vehicle: string;
  email: string;
  phoneNumber: string;
  zoneId: number;
}

export interface CourierWithDeliveries extends Courier {
  collectingDeliveriesCount: number;
  shippingDeliveriesCount: number;
}

