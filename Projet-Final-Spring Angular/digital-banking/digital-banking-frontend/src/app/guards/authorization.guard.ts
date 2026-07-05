import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { AuthService } from '../services/auth.service';

/**
 * Bloque l'accès si l'utilisateur n'a pas le rôle requis.
 * Le rôle attendu est passé dans les data de la route : data: { role: 'SCOPE_ADMIN' }.
 */
export const authorizationGuard: CanActivateFn = (route, state) => {
  const authService = inject(AuthService);
  const router = inject(Router);
  const requiredRole = route.data['role'];
  if (authService.hasRole(requiredRole)) {
    return true;
  }
  router.navigateByUrl('/not-authorized');
  return false;
};
