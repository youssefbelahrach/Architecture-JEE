import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { AccountHistory } from '../model/account.model';
import { environment } from '../../environments/environment';

@Injectable({ providedIn: 'root' })
export class AccountsService {

  backendHost: string = environment.backendHost;

  constructor(private http: HttpClient) {}

  getAccount(accountId: string, page: number, size: number): Observable<AccountHistory> {
    return this.http.get<AccountHistory>(
      this.backendHost + "/accounts/" + accountId + "/pageOperations?page=" + page + "&size=" + size
    );
  }

  debit(accountId: string, amount: number, description: string): Observable<any> {
    const data = { accountId, amount, description };
    return this.http.post(this.backendHost + "/accounts/debit", data);
  }

  credit(accountId: string, amount: number, description: string): Observable<any> {
    const data = { accountId, amount, description };
    return this.http.post(this.backendHost + "/accounts/credit", data);
  }

  transfer(accountSource: string, accountDestination: string, amount: number): Observable<any> {
    const data = { accountSource, accountDestination, amount };
    return this.http.post(this.backendHost + "/accounts/transfer", data);
  }
}
