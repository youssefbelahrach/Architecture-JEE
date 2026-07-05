import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Customer } from '../model/customer.model';
import { environment } from '../../environments/environment';

@Injectable({ providedIn: 'root' })
export class CustomerService {

  backendHost: string = environment.backendHost;

  constructor(private http: HttpClient) {}

  getCustomers(): Observable<Array<Customer>> {
    return this.http.get<Array<Customer>>(this.backendHost + "/customers");
  }

  searchCustomers(keyword: string): Observable<Array<Customer>> {
    return this.http.get<Array<Customer>>(this.backendHost + "/customers/search?keyword=" + keyword);
  }

  saveCustomer(customer: Customer): Observable<Customer> {
    return this.http.post<Customer>(this.backendHost + "/customers", customer);
  }

  deleteCustomer(id: number): Observable<void> {
    return this.http.delete<void>(this.backendHost + "/customers/" + id);
  }
}
