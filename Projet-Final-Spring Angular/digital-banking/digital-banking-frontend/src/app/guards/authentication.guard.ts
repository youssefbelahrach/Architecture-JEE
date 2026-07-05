import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { AuthService } from '../services/auth.service';

/** Bloque l'accès aux routes si l'utilisateur n'est pas connecté. */
export const authenticationGuard: CanActivateFn = (route, state) => {
  const authService = inject(AuthService);
  const router = inject(Router);
  if (authService.isAuthenticated) {
    return true;
  }
  router.navigateByUrl('/login');
  return false;
};
