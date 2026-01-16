export enum DeliveryStatus {
  CREATED = 'CREATED',
  COLLECTED = 'COLLECTED',
  IN_STOCK = 'IN_STOCK',
  IN_TRANSIT = 'IN_TRANSIT',
  DELIVERED = 'DELIVERED'
}

export enum DeliveryPriority {
  LOW = 'LOW',
  MEDIUM = 'MEDIUM',
  HIGH = 'HIGH'
}

export interface Delivery {
  id: number;
  description: string;
  destinationCity: string;
  trackingNumber: string;
  weight: number;
  status: DeliveryStatus;
  priority: DeliveryPriority;
  recipientName: string;
  senderName: string;
  pickupZoneName: string;
  shippingZoneName: string;
  collectingCourierName: string | null;
  shippingCourierName: string | null;
  createdAt: string;
  updatedAt: string;
}

export interface DeliveryDetails {
  id: number;
  description: string;
  destinationCity?: string | null;
  weight: number;
  status: string;
  priority: string;
  trackingNumber: string;
  sender: SenderResponse;
  recipient: RecipientResponse;
  collectingCourier: CourierResponse | null;
  shippingCourier: CourierResponse | null;
  zone?: ZoneResponse | null;
  products: ProductInDelivery[];
  history: DeliveryHistory[];
  createdAt: string;
  updatedAt: string;
}

export interface DeliveryTracking {
  id: string;
  description: string;
  currentStatus: DeliveryStatus;
  trackingNumber: string;
  destinationCity: string;
  recipientName: string;
  senderName: string;
  collectingCourierName: string | null;
  shippingCourierName: string | null;
  lastUpdate: string;
}

export interface CreateDeliveryRequest {
  description: string;
  destinationCity: string;
  weight: number;
  priority?: string; // Optional, can be empty string, "LOW", "MEDIUM", or "HIGH"
  pickupAddress: string;
  pickupPostalCode: string;
  shippingAddress: string;
  shippingPostalCode: string;
  recipientData: RecipientRequest;
  products?: DeliveryProductRequest[]; // Optional list of products
}

export interface RecipientRequest {
  name: string;
  email: string;
  phone: string;
}

export interface DeliveryProductRequest {
  productId: number; // Long in backend
  quantity: number; // Integer in backend
}

export interface SearchDeliveryRequest {
  searchTerm?: string;
  status?: DeliveryStatus;
  priority?: DeliveryPriority;
  pickupZoneId?: number;
  deliveryZoneId?: number;
  city?: string;
  courierId?: string;
  senderId?: string;
  phone?: string;
  page?: number;
  size?: number;
  sortBy?: string;
  sortOrder?: 'ASC' | 'DESC';
}

export interface UpdateDeliveryStatusRequest {
  status: DeliveryStatus;
}

export interface AssignDeliveryRequest {
  courierId: number;
}

export interface UpdatePersonRequest {
  lastName: string;
  firstName: string;
  email: string;
  phone: string;
  address: string;
}

export interface CreatePersonRequest {
  lastName: string;
  firstName: string;
  email: string;
  password: string;
  phone: string;
  address: string;
}

export interface SenderResponse {
  id: string; // API returns String, but we use number for requests
  lastName: string;
  firstName: string;
  email: string;
  phone: string;
  totalDeliveriesSent?: number;
  activeDeliveries?: number;
}

export interface RecipientResponse {
  id: string;
  firstName?: string;
  lastName?: string;
  name?: string; // Fallback for Recipient which has name field
  email: string;
  phone: string;
  totalDeliveriesReceived?: number;
  pendingDeliveries?: number;
}

export interface CourierResponse {
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

export interface ZoneResponse {
  id: number;
  name: string;
  code: string;
  active: boolean;
  totalPostalCodes: number;
  postalCodes: string[];
  createdAt: string;
  updatedAt: string;
}

export interface ProductInDelivery {
  id: number;
  productId: number;
  productName: string;
  quantity: number;
  unitPrice: number;
  totalPrice: number;
}

export interface DeliveryHistory {
  id: string;
  status: DeliveryStatus;
  comment?: string | null; // API uses 'comment' not 'note'
  updatedAt: string; // API uses 'updatedAt' not 'createdAt'
}

