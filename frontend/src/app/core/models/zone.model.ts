export interface Zone {
  id: number;
  name: string;
  code: string;
  active: boolean;
  totalPostalCodes: number;
  postalCodes: string[];
  createdAt: string;
  updatedAt: string;
}

export interface CreateZoneRequest {
  name: string;
  code: string;
  active: boolean;
  postalCodes: string[];
}

export interface UpdateZoneRequest {
  name: string;
  code: string;
  active: boolean;
  postalCodes: string[];
}

