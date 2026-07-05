import { Component, OnInit } from '@angular/core';
import { AuthService } from './services/auth.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html'
})
export class AppComponent implements OnInit {
  title = 'digital-banking-frontend';

  constructor(private authService: AuthService) {}

  ngOnInit(): void {
    // au chargement de l'appli, on tente de restaurer la session
    this.authService.loadJwtTokenFromLocalStorage();
  }
}
