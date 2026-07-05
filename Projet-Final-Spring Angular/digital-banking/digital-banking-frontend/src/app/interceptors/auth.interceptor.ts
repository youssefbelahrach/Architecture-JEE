import { HttpEvent, HttpHandler, HttpInterceptor, HttpRequest } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { AuthService } from '../services/auth.service';

/**
 * Ajoute l'en-tête "Authorization: Bearer <token>" à toutes les requêtes,
 * sauf à la requête de login.
 */
@Injectable()
export class AuthInterceptor implements HttpInterceptor {

  constructor(private authService: AuthService) {}

  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    if (req.url.includes('/login')) {
      return next.handle(req);
    }
    const token = this.authService.accessToken || window.localStorage.getItem('jwt-token');
    if (token) {
      const authReq = req.clone({
        setHeaders: { Authorization: 'Bearer ' + token }
      });
      return next.handle(authReq);
    }
    return next.handle(req);
  }
}
