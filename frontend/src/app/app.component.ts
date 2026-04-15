import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { BusService, Booking } from './bus.service';
import { TranslationService } from './translation.service';
import { AuthService, User } from './auth.service';
import { ThemeService } from './theme.service';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit {
  activeTab: 'home' | 'book' | 'list' | 'auth' = 'home';
  isSidebarOpen: boolean = false;

  // Theme & Auth
  currentUser: User | null = null;
  authMode: 'login' | 'signup' = 'login';
  authData = { username: 'admin', password: 'admin123', email: '' };

  // Search
  search = { from: '', to: '', date: new Date().toISOString().split('T')[0] };
  availableBuses = [
    { id: 1, name: 'Saffron Express', type: 'AC Sleeper', time: '10:00 PM', price: '₹1200' },
    { id: 2, name: 'Green Travels', type: 'Luxury Seater', time: '08:30 PM', price: '₹800' }
  ];
  selectedBus: any = null;

  // Booking Form
  travelDate: string = this.search.date;
  mobileNumber: string = '';
  selectedSeats: string[] = [];
  bookedSeats: string[] = [];

  // List View
  listDate: string = new Date().toISOString().split('T')[0];
  bookings: Booking[] = [];

  // Confirmation
  showConfirmation: boolean = false;
  lastBooking?: Booking;

  // Chatbot
  showChat: boolean = false;
  chatMessages: { text: string, isBot: boolean }[] = [
    { text: "Namaste! I am your Go Bus AI Buddy. How can I help you today?", isBot: true }
  ];
  userInput: string = '';

  rows = Array.from({ length: 15 }, (_, i) => i + 1);
  cols = ['A', 'B', 'C', 'D'];

  constructor(
    private busService: BusService,
    public ts: TranslationService,
    public auth: AuthService,
    public theme: ThemeService
  ) { }

  ngOnInit() {
    this.auth.currentUser$.subscribe(user => this.currentUser = user);
    this.refreshBookedSeats();
  }

  t(key: string): string { return this.ts.translate(key); }
  changeLang(lang: string) { this.ts.setLanguage(lang); }
  changeTheme(t: string) { this.theme.setTheme(t); }

  // Auth Actions
  handleAuth() {
    if (this.authMode === 'login') {
      this.auth.login({ username: this.authData.username, password: this.authData.password }).subscribe({
        next: () => { this.activeTab = 'home'; this.authData = { username: '', password: '', email: '' }; },
        error: (err) => alert("Login failed: " + (err.error?.error || "Invalid credentials"))
      });
    } else {
      this.auth.signup(this.authData).subscribe({
        next: () => { alert("Signup successful! Please login."); this.authMode = 'login'; },
        error: (err) => alert("Signup failed: " + (err.error?.error || "Error"))
      });
    }
  }

  logout() {
    this.auth.logout();
    this.activeTab = 'home';
  }

  // Search Actions
  handleBookClick(bus: any) {
    if (this.auth.getCurrentUser()) {
      this.selectedBus = bus;
      this.activeTab = 'book';
    } else {
      this.activeTab = 'auth';
    }
  }

  onSearch() {
    if (!this.search.from || !this.search.to) {
      alert("Please enter both From and To locations.");
      return;
    }
    // Simulate finding buses
    this.selectedBus = null;
  }

  selectBus(bus: any) {
    this.selectedBus = bus;
    this.travelDate = this.search.date;
    this.activeTab = 'book';
    this.refreshBookedSeats();
  }

  // Booking Logic (Existing)
  refreshBookedSeats() {
    this.busService.getBookings(this.travelDate).subscribe(data => {
      this.bookedSeats = data.flatMap(b => b.seats);
    });
  }

  toggleSeat(seatId: string) {
    if (this.isBooked(seatId)) return;
    const index = this.selectedSeats.indexOf(seatId);
    if (index > -1) this.selectedSeats.splice(index, 1);
    else {
      if (this.selectedSeats.length >= 6) return;
      this.selectedSeats.push(seatId);
    }
  }

  isBooked(seatId: string) { return this.bookedSeats.includes(seatId); }
  isSelected(seatId: string) { return this.selectedSeats.includes(seatId); }

  bookTickets() {
    if (!this.auth.isLoggedIn()) {
      alert("Please login to book tickets.");
      this.activeTab = 'auth';
      return;
    }
    if (!this.mobileNumber || this.mobileNumber.length !== 10) {
      alert("Please enter a valid 10-digit mobile number.");
      return;
    }
    const booking: Booking = {
      travelDate: this.travelDate,
      mobileNumber: this.mobileNumber || '9999999999',
      seats: [...this.selectedSeats],
      boarded: false
    };
    this.busService.bookSeats(booking).subscribe({
      next: (res) => {
        this.lastBooking = res;
        this.showConfirmation = true;
        this.selectedSeats = [];
        this.refreshBookedSeats();
      },
      error: (err) => alert(err.error?.error || "Booking failed.")
    });
  }

  // Tracking & Share
  loadBookings() { this.busService.getBookings(this.listDate).subscribe(data => this.bookings = data); }
  markBoarded(booking: Booking) {
    if (booking.id) {
      this.busService.markAsBoarded(booking.id).subscribe({
        next: (res) => {
          booking.boarded = true;
          alert("Passenger boarded successfully!");
        },
        error: (err) => {
          console.error(err);
          alert("Error marking as boarded: " + (err.error?.error || "Unknown error"));
        }
      });
    }
  }
  shareWhatsApp(booking?: Booking) {
    const b = booking || this.lastBooking;
    if (!b) return;
    const text = `Booking Confirmed! ID: ${b.id}, Date: ${b.travelDate}, Seats: ${b.seats.join(', ')}`;
    window.open(`https://wa.me/91${b.mobileNumber}?text=${encodeURIComponent(text)}`);
  }

  toggleChat() { this.showChat = !this.showChat; }
  closeConfirmation() { this.showConfirmation = false; }

  scrollToSearch() {
    const el = document.getElementById('search-section');
    if (el) el.scrollIntoView({ behavior: 'smooth' });
  }

  // Chatbot
  sendMessage() {
    if (!this.userInput.trim()) return;
    this.chatMessages.push({ text: this.userInput, isBot: false });
    const input = this.userInput.toLowerCase();
    this.userInput = '';

    // Domain filtering
    const busKeywords = ['bus', 'ticket', 'seat', 'route', 'book', 'time', 'schedule', 'price', 'fare', 'pay', 'cancel', 'status', 'manifest', 'boarding', 'track', 'hyd', 'vja', 'vskp', 'blr', 'travel', 'journey'];
    const isBusRelated = busKeywords.some(key => input.includes(key));

    setTimeout(() => {
      let reply = "";
      if (!isBusRelated) {
        reply = "I'm your Bus Buddy! 🚌 I specialize only in bus-related queries like bookings, routes, and schedules. How can I help you with your journey?";
      } else if (input.includes('price') || input.includes('fare')) {
        reply = "Our premium bus tickets start at ₹850. Prices vary based on the route and bus type.";
      } else if (input.includes('book') || input.includes('ticket')) {
        reply = "You can book tickets by selecting a bus from the dashboard and choosing your seats!";
      } else if (input.includes('status') || input.includes('track')) {
        reply = "You can track your boarding status in the 'Manifest' section after logging in.";
      } else {
        reply = "That sounds like something I can help with! Could you provide more details about your bus travel query?";
      }
      this.chatMessages.push({ text: reply, isBot: true });
    }, 800);
  }
}
