import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { CustomersComponent } from './customers/customers.component';
import { NewCustomerComponent } from './new-customer/new-customer.component';
import { AccountsComponent } from './accounts/accounts.component';
import { LoginComponent } from './login/login.component';
import { NotAuthorizedComponent } from './not-authorized/not-authorized.component';
import { authenticationGuard } from './guards/authentication.guard';
import { authorizationGuard } from './guards/authorization.guard';

const routes: Routes = [
  { path: '', redirectTo: '/customers', pathMatch: 'full' },
  { path: 'login', component: LoginComponent },
  { path: 'not-authorized', component: NotAuthorizedComponent },

  // Accessibles à tout utilisateur authentifié
  { path: 'customers', component: CustomersComponent, canActivate: [authenticationGuard] },
  { path: 'accounts', component: AccountsComponent, canActivate: [authenticationGuard] },
  { path: 'customer-accounts/:id', component: AccountsComponent, canActivate: [authenticationGuard] },

  // Réservé aux administrateurs
  {
    path: 'new-customer',
    component: NewCustomerComponent,
    canActivate: [authenticationGuard, authorizationGuard],
    data: { role: 'SCOPE_ADMIN' }
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {}
