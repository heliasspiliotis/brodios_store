import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
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
  // Μεταβλητές Πλοήγησης
  currentPage: string = 'home';
  selectedCategory: string = 'all';

  // Δεδομένα
  products: any[] = [];

  // ΑΥΤΑ ΕΛΕΙΠΑΝ ΚΑΙ ΕΒΓΑΖΕ ΤΟ ΛΑΘΟΣ:
  selectedProduct: any = null; // Το προϊόν που βλέπουμε
  selectedSize: any = null;    // Το μέγεθος που διάλεξε ο πελάτης

  constructor(private http: HttpClient) {}

  ngOnInit() {
    // Φόρτωση προϊόντων από τη Java
    this.http.get<any[]>('http://localhost:8080/api/products').subscribe({
      next: (data) => this.products = data,
      error: (err) => console.error('Error fetching products:', err)
    });
  }

  // Αλλαγή Σελίδας
  changePage(page: string) {
    this.currentPage = page;
    // Αν φύγουμε από τα details, καθαρίζουμε την επιλογή
    if (page === 'store') {
      this.selectedCategory = 'all';
      this.selectedProduct = null;
    }
  }

  // Φίλτρο Κατηγορίας
  filterCategory(category: string) {
    this.selectedCategory = category;
  }

  // --- ΝΕΕΣ ΣΥΝΑΡΤΗΣΕΙΣ ΓΙΑ ΤΟ DETAILS PAGE ---

  // 1. Άνοιγμα προϊόντος
  openProduct(product: any) {
    console.log('Opening product:', product.name); // Για έλεγχο
    this.selectedProduct = product;
    this.selectedSize = null; // Reset το μέγεθος
    this.currentPage = 'details';
  }

  // 2. Επιλογή Μεγέθους
  selectSize(variant: any) {
    this.selectedSize = variant;
  }

  // 3. Προσθήκη στο καλάθι
  addToCart() {
    if (!this.selectedSize) {
      alert('Παρακαλώ επιλέξτε μέγεθος!');
      return;
    }
    alert(`Προστέθηκε στο καλάθι: ${this.selectedProduct.name} (Μέγεθος: ${this.selectedSize.sizeName})`);
  }

  // --- ΒΟΗΘΗΤΙΚΑ ---

  // Φιλτράρισμα λίστας
  get filteredProducts() {
    if (this.selectedCategory === 'all') {
      return this.products;
    }
    return this.products.filter(p => p.category?.name === this.selectedCategory);
  }
}
