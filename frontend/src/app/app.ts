import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common'; // Απαραίτητο για τα *ngIf και *ngFor
import { HttpClient } from '@angular/common/http';
import { HttpClientModule } from '@angular/common/http';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [CommonModule, HttpClientModule],
  templateUrl: './app.html',
  styleUrls: ['./app.css']
})
export class AppComponent implements OnInit {
  // Μεταβλητές για την πλοήγηση
  currentPage: string = 'home'; // Ξεκινάμε στην αρχική
  selectedCategory: string = 'all'; // Επιλεγμένη κατηγορία στο Store

  products: any[] = [];

  constructor(private http: HttpClient) {}

  ngOnInit() {
    // Φορτώνουμε τα προϊόντα μόλις ανοίξει η σελίδα
    this.http.get<any[]>('http://localhost:8080/api/products').subscribe({
      next: (data) => this.products = data,
      error: (err) => console.error('Σφάλμα:', err)
    });
  }

  // Συνάρτηση για αλλαγή σελίδας (Home, Store, About)
  changePage(page: string) {
    this.currentPage = page;
    // Αν πάμε στο Store, να δείχνει όλα τα προϊόντα αρχικά
    if (page === 'store') {
      this.selectedCategory = 'all';
    }
  }

  // Συνάρτηση για φιλτράρισμα κατηγορίας
  filterCategory(category: string) {
    this.selectedCategory = category;
  }

  // Αυτό επιστρέφει μόνο τα προϊόντα της επιλεγμένης κατηγορίας
  get filteredProducts() {
    if (this.selectedCategory === 'all') {
      return this.products;
    }
    // Φιλτράρισμα με βάση το όνομα της κατηγορίας (Kits ή Jackets)
    return this.products.filter(p => p.category?.name === this.selectedCategory);
  }
}
