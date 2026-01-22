import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Product } from '../models/product';

@Injectable({
  providedIn: 'root'
})
export class ProductService {

  // Η διεύθυνση του Java Backend
  private apiUrl = 'http://localhost:8080/api/products';

  constructor(private http: HttpClient) { }

  // Μέθοδος για να φέρουμε όλα τα προϊόντα
  getProducts(): Observable<Product[]> {
    return this.http.get<Product[]>(this.apiUrl);
  }
}
