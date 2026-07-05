import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { Router } from '@angular/router';
import { catchError, map, Observable, throwError } from 'rxjs';
import { Customer } from '../model/customer.model';
import { CustomerService } from '../services/customer.service';

@Component({
  selector: 'app-customers',
  templateUrl: './customers.component.html'
})
export class CustomersComponent implements OnInit {

  customers!: Observable<Array<Customer>>;
  errorMessage!: string;
  searchFormGroup!: FormGroup;

  constructor(
    private customerService: CustomerService,
    private fb: FormBuilder,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.searchFormGroup = this.fb.group({ keyword: this.fb.control("") });
    this.handleSearchCustomers();
  }

  handleSearchCustomers(): void {
    const keyword = this.searchFormGroup.value.keyword;
    this.customers = this.customerService.searchCustomers(keyword).pipe(
      catchError(err => {
        this.errorMessage = err.message;
        return throwError(() => err);
      })
    );
  }

  handleDeleteCustomer(customer: Customer): void {
    if (!confirm("Voulez-vous vraiment supprimer ce client ?")) return;
    this.customerService.deleteCustomer(customer.id).subscribe({
      next: () => {
        // retire le client de la liste sans recharger tout le tableau
        this.customers = this.customers.pipe(
          map(data => data.filter(c => c.id != customer.id))
        );
      },
      error: err => console.log(err)
    });
  }

  handleCustomerAccounts(customer: Customer): void {
    this.router.navigateByUrl("/customer-accounts/" + customer.id, { state: customer });
  }
}
