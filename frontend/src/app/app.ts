import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HttpClient, HttpHeaders } from '@angular/common/http';
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

  // --- LOGIN SYSTEM ---
  isLoggedIn: boolean = false;
  currentUserData: any = null;

  // Φόρμες
  loginForm = { username: '', password: '' };
  registerForm = { username: '', email: '', password: '', address: '', phone: '' };

  // --- CHECKOUT SYSTEM ---
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
    window.scrollTo(0, 0);

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
      variant: this.selectedSize,
      quantity: 1
    };

    this.cart.push(item);
    alert(`Προστέθηκε: ${this.selectedProduct.name} (${this.selectedSize.size})`);
    this.changePage('store');
  }

  removeFromCart(index: number) {
    this.cart.splice(index, 1);
  }

  getTotalPrice() {
    return this.cart.reduce((total, item) => total + (item.product.basePrice * item.quantity), 0);
  }

  // --- LOGIC CHECKOUT ---
  goToCheckout() {
    if (this.cart.length === 0) {
      alert('Το καλάθι είναι άδειο!');
      return;
    }
    if (!this.isLoggedIn) {
      alert('Πρέπει να συνδεθείτε για να ολοκληρώσετε την αγορά!');
      this.changePage('login');
      return;
    }
    this.currentPage = 'checkout';
  }

  // --- LOGIN / REGISTER ---

  performLogin() {
    const url = 'http://localhost:8080/api/auth/login';

    this.http.post(url, this.loginForm).subscribe({
      next: (response: any) => {
        this.isLoggedIn = true;
        this.currentUserData = response;

        // Γεμίζουμε τα πεδία του Checkout αυτόματα
        this.customer.name = response.username;
        this.customer.address = response.address || '';
        this.customer.phone = response.phone || '';
        this.customer.email = response.email;

        alert('Καλώς ήρθατε ' + response.username + '!');
        this.changePage('home');
      },
      error: (err) => {
        console.error(err);
        alert('Λάθος όνομα χρήστη ή κωδικός!');
      }
    });
  }

  performRegister() {
    const url = 'http://localhost:8080/api/auth/register';
    this.http.post(url, this.registerForm, { responseType: 'text' }).subscribe({
      next: (response) => {
        alert('Η εγγραφή πέτυχε! Τώρα κάντε είσοδο.');
        this.changePage('login');
      },
      error: (err) => {
        console.error(err);
        alert('Η εγγραφή απέτυχε. Δοκιμάστε άλλο username.');
      }
    });
  }

  logout() {
    this.isLoggedIn = false;
    this.currentUserData = null;
    this.cart = [];
    this.customer = { name: '', address: '', phone: '', email: '' };
    alert('Αποσυνδεθήκατε.');
    this.changePage('home');
  }

  // --- ΟΛΟΚΛΗΡΩΣΗ ΑΓΟΡΑΣ ΜΕ TOKEN ---
  submitOrder() {
      // 1. Έλεγχοι
      if (!this.isLoggedIn || !this.currentUserData) {
        alert('Πρέπει να συνδεθείτε!');
        this.changePage('login');
        return;
      }

      console.log('Requesting Checkout...');

      // 2. Εύρεση Token
      const token = this.currentUserData.token;

      if (!token) {
          alert("Σφάλμα: Δεν βρέθηκε Token! Κάντε Logout και ξανά Login.");
          return;
      }

      // 3. Προετοιμασία Headers
      const headers = new HttpHeaders().set('Authorization', 'Bearer ' + token);

      // 4. Αποστολή στο ΥΠΑΡΧΟΝ endpoint της Java
      const url = 'http://localhost:8080/api/orders/create';

      this.http.post(url, orderRequest, { headers: headers }).subscribe({
        next: (response) => {
          alert('Η παραγγελία ολοκληρώθηκε επιτυχώς!');
          this.cart = [];
          this.changePage('home');
        },
        error: (err) => {
          console.error('Order Error:', err);
          if (err.status === 401) {
              alert('Το Token έληξε. Κάντε ξανά Login.');
              this.logout();
          } else {
              alert('Σφάλμα: ' + (err.error?.message || err.message));
          }
        }
      });
    }

}
