import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HttpClient } from '@angular/common/http';
import { HttpClientModule } from '@angular/common/http';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [CommonModule, HttpClientModule, FormsModule],
  templateUrl: './app.html',
  styleUrls: ['./app.css']
})
export class AppComponent implements OnInit {
  // --- ΠΛΟΗΓΗΣΗ & ΔΕΔΟΜΕΝΑ ---
  currentPage: string = 'home';
  selectedCategory: string = 'all';
  products: any[] = [];
  cart: any[] = [];
  selectedProduct: any = null;
  selectedSize: any = null;

  // --- LOGIN SYSTEM (ΜΕΤΑΒΛΗΤΕΣ) ---
  isLoggedIn: boolean = false;
  currentUserData: any = null;

  // Φόρμες εισαγωγής
  loginForm = { username: '', password: '' };
  registerForm = { username: '', email: '', password: '' };

  // --- CHECKOUT SYSTEM (ΜΕΤΑΒΛΗΤΕΣ) ---
  customer = {
    name: '',
    address: '',
    phone: '',
    email: ''
  };

  constructor(private http: HttpClient) {}

  ngOnInit() {
    this.http.get<any[]>('http://localhost:8080/api/products').subscribe({
      next: (data) => this.products = data,
      error: (err) => console.error('Error fetching products:', err)
    });
  }

  // --- ΠΛΟΗΓΗΣΗ ---
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

  // --- LOGIC ΠΡΟΪΟΝΤΩΝ ---
  openProduct(product: any) {
    console.log('Product data:', product);
    this.selectedProduct = product;
    this.selectedSize = null;
    this.currentPage = 'details';
  }

  selectSize(variant: any) {
    this.selectedSize = variant;
  }

  get filteredProducts() {
    if (this.selectedCategory === 'all') {
      return this.products;
    }
    return this.products.filter(p => p.category?.name === this.selectedCategory);
  }

  // --- LOGIC ΚΑΛΑΘΙΟΥ ---
  addToCart() {
    if (!this.selectedSize) {
      alert('Παρακαλώ επιλέξτε μέγεθος!');
      return;
    }
    const item = {
      product: this.selectedProduct,
      size: this.selectedSize
    };
    this.cart.push(item);
    alert(`Προστέθηκε: ${this.selectedProduct.name} (${this.selectedSize.size})`);
  }

  removeFromCart(index: number) {
    this.cart.splice(index, 1);
  }

  getTotalPrice() {
    return this.cart.reduce((total, item) => total + item.product.basePrice, 0);
  }

  // --- LOGIC LOGIN / REGISTER ---

  // 1. ΠΡΑΓΜΑΤΙΚΗ ΣΥΝΔΕΣΗ (Μιλάει με τη Java)
    performLogin() {
      const url = 'http://localhost:8080/api/auth/login';

      this.http.post(url, this.loginForm).subscribe({
        next: (response: any) => {
          // ΕΠΙΤΥΧΙΑ: Η Java απάντησε σωστά
          this.isLoggedIn = true;

          // Αποθηκεύουμε τα στοιχεία που ήρθαν από τη βάση
          this.currentUserData = response;

          // Γεμίζουμε αυτόματα τη φόρμα παραγγελίας με τα στοιχεία του χρήστη!
          this.customer.name = response.username;
          this.customer.address = response.address;
          this.customer.phone = response.phone;
          this.customer.email = response.email;

          alert('Καλώς ήρθατε ' + response.username + '!');
          this.changePage('home');
        },
        error: (err) => {
          // ΑΠΟΤΥΧΙΑ: Λάθος κωδικός
          console.error(err);
          alert('Λάθος όνομα χρήστη ή κωδικός!');
        }
      });
    }

    // 2. ΠΡΑΓΜΑΤΙΚΗ ΕΓΓΡΑΦΗ
    performRegister() {
      const url = 'http://localhost:8080/api/auth/register';

      // Στέλνουμε όλο το αντικείμενο (με διεύθυνση και τηλέφωνο)
      this.http.post(url, this.registerForm, { responseType: 'text' }).subscribe({
        next: (response) => {
          alert('Η εγγραφή πέτυχε! Τώρα κάντε είσοδο.');
          this.changePage('login');
        },
        error: (err) => {
          console.error(err);
          alert('Η εγγραφή απέτυχε. Δοκιμάστε άλλο username/email.');
        }
      });
    }

    // 3. ΑΠΟΣΥΝΔΕΣΗ
    logout() {
      this.isLoggedIn = false;
      this.currentUserData = null;
      // Καθαρίζουμε τη φόρμα παραγγελίας
      this.customer = { name: '', address: '', phone: '', email: '' };

      alert('Αποσυνδεθήκατε.');
      this.changePage('home');
    }

  // --- LOGIC CHECKOUT (ΤΑΜΕΙΟ) ---

  goToCheckout() {
    if (this.cart.length === 0) {
      alert('Το καλάθι είναι άδειο!');
      return;
    }
    // Υποχρεωτική σύνδεση για αγορά

    if (!this.isLoggedIn) {
      alert('Πρέπει να συνδεθείτε για να ολοκληρώσετε την αγορά!');
      this.changePage('login');
      return;
    }

    this.currentPage = 'checkout';
  }

  submitOrder() {
    if (!this.customer.name || !this.customer.address || !this.customer.phone) {
      alert('Παρακαλώ συμπληρώστε όλα τα στοιχεία αποστολής!');
      return;
    }

    console.log('Νέα παραγγελία:', {
      customer: this.customer,
      items: this.cart,
      total: this.getTotalPrice()
    });

    alert('Η παραγγελία καταχωρήθηκε επιτυχώς! Ευχαριστούμε.');

    // Reset μετά την αγορά
    this.cart = [];
    this.customer = { name: '', address: '', phone: '', email: '' };
    this.currentPage = 'home';
  }
}
