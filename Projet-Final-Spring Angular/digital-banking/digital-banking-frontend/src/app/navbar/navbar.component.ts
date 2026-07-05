import { Component } from '@angular/core';
import { AuthService } from '../services/auth.service';

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html'
})
export class NavbarComponent {
  // public : utilisé directement dans le template
  constructor(public authService: AuthService) {}

  handleLogout(): void {
    this.authService.logout();
  }
}
