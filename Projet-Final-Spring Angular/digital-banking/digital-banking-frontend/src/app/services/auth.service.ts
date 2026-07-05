import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';

@Injectable({ providedIn: 'root' })
export class AuthService {

  isAuthenticated: boolean = false;
  username: string = '';
  roles: string[] = [];
  accessToken: string = '';

  constructor(private http: HttpClient, private router: Router) {}

  /** Envoie les identifiants au backend (format x-www-form-urlencoded). */
  public login(username: string, password: string): Observable<any> {
    const options = {
      headers: new HttpHeaders().set('Content-Type', 'application/x-www-form-urlencoded')
    };
    const params = new HttpParams()
      .set('username', username)
      .set('password', password);
    return this.http.post(environment.authHost + '/login', params, options);
  }

  /** Charge le profil à partir de la réponse du login. */
  loadProfile(data: any): void {
    this.accessToken = data['access-token'];
    const decoded: any = this.decodeToken(this.accessToken);
    this.isAuthenticated = true;
    this.username = decoded.sub;
    // le claim "scope" contient les rôles séparés par un espace (ex: "SCOPE_USER SCOPE_ADMIN")
    this.roles = decoded.scope ? decoded.scope.split(' ') : [];
    window.localStorage.setItem('jwt-token', this.accessToken);
  }

  /** Décode la partie payload (base64) d'un JWT. */
  private decodeToken(token: string): any {
    const payload = token.split('.')[1];
    return JSON.parse(atob(payload));
  }

  hasRole(role: string): boolean {
    return this.roles.includes(role);
  }

  logout(): void {
    this.isAuthenticated = false;
    this.accessToken = '';
    this.username = '';
    this.roles = [];
    window.localStorage.removeItem('jwt-token');
    this.router.navigateByUrl('/login');
  }

  /** Restaure la session au rechargement de la page si le token est encore valide. */
  loadJwtTokenFromLocalStorage(): void {
    const token = window.localStorage.getItem('jwt-token');
    if (token) {
      const decoded: any = this.decodeToken(token);
      const now = Math.floor(Date.now() / 1000);
      if (decoded.exp > now) {
        this.loadProfile({ 'access-token': token });
      } else {
        this.logout(); // token expiré
      }
    }
  }
}
