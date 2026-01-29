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
  currentPage: string = 'home';
  selectedCategory: string = 'all';

  products: any[] = [];

  // --- ΝΕΟ: Η λίστα που κρατάει τα ψώνια ---
  cart: any[] = [];

  selectedProduct: any = null;
  selectedSize: any = null;

  constructor(private http: HttpClient) {}

  ngOnInit() {
    this.http.get<any[]>('http://localhost:8080/api/products').subscribe({
      next: (data) => this.products = data,
      error: (err) => console.error('Error fetching products:', err)
    });
  }

  changePage(page: string) {
    this.currentPage = page;
    if (page === 'store') {
      this.selectedCategory = 'all';
      this.selectedProduct = null;
    }
  }

  filterCategory(category: string) {
    this.selectedCategory = category;
  }

  openProduct(product: any) {
    console.log('Product data:', product);
    this.selectedProduct = product;
    this.selectedSize = null;
    this.currentPage = 'details';
  }

  selectSize(variant: any) {
    this.selectedSize = variant;
  }

  // Η συνάρτηση που βάζει το προϊόν στο καλάθι
  addToCart() {
    if (!this.selectedSize) {
      alert('Παρακαλώ επιλέξτε μέγεθος!');
      return;
    }

    // 1. Δημιουργούμε το αντικείμενο για το καλάθι
    const item = {
      product: this.selectedProduct,
      size: this.selectedSize
    };

    // 2. Το βάζουμε στη λίστα
    this.cart.push(item);

    // 3.  Εμφανίζουμε μήνυμα  επιστρέφουμε στο κατάστημα
    alert(`Προστέθηκε: ${this.selectedProduct.name} (${this.selectedSize.size})`);

  }

  get filteredProducts() {
    if (this.selectedCategory === 'all') {
      return this.products;
    }
    return this.products.filter(p => p.category?.name === this.selectedCategory);
  }
}
