import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface Booking {
  id?: string;
  travelDate: string;
  mobileNumber: string;
  seats: string[];
  boarded: boolean;
}

@Injectable({
  providedIn: 'root'
})
export class BusService {
  private apiUrl = 'http://localhost:8080/api/bookings';

  constructor(private http: HttpClient) {}

  bookSeats(booking: Booking): Observable<Booking> {
    return this.http.post<Booking>(this.apiUrl, booking);
  }

  getBookings(date: string): Observable<Booking[]> {
    return this.http.get<Booking[]>(`${this.apiUrl}?date=${date}`);
  }

  markAsBoarded(id: string): Observable<Booking> {
    return this.http.patch<Booking>(`${this.apiUrl}/${id}/board`, {});
  }
}
