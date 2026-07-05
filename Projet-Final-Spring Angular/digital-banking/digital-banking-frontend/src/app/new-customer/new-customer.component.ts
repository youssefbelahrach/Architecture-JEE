import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { Customer } from '../model/customer.model';
import { CustomerService } from '../services/customer.service';

@Component({
  selector: 'app-new-customer',
  templateUrl: './new-customer.component.html'
})
export class NewCustomerComponent implements OnInit {

  newCustomerFormGroup!: FormGroup;

  constructor(
    private fb: FormBuilder,
    private customerService: CustomerService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.newCustomerFormGroup = this.fb.group({
      name: this.fb.control(null, [Validators.required, Validators.minLength(3)]),
      email: this.fb.control(null, [Validators.required, Validators.email])
    });
  }

  handleSaveCustomer(): void {
    const customer: Customer = this.newCustomerFormGroup.value;
    this.customerService.saveCustomer(customer).subscribe({
      next: () => {
        alert("Client enregistré avec succès.");
        this.newCustomerFormGroup.reset();
        this.router.navigateByUrl("/customers");
      },
      error: err => console.log(err)
    });
  }
}
