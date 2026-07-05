import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { catchError, Observable, throwError } from 'rxjs';
import { AccountHistory } from '../model/account.model';
import { AccountsService } from '../services/accounts.service';

@Component({
  selector: 'app-accounts',
  templateUrl: './accounts.component.html'
})
export class AccountsComponent implements OnInit {

  accountFormGroup!: FormGroup;
  operationFormGroup!: FormGroup;
  currentPage: number = 0;
  pageSize: number = 5;
  accountObservable!: Observable<AccountHistory>;
  errorMessage!: string;

  constructor(private fb: FormBuilder, private accountService: AccountsService) {}

  ngOnInit(): void {
    this.accountFormGroup = this.fb.group({
      accountId: this.fb.control('')
    });
    this.operationFormGroup = this.fb.group({
      operationType: this.fb.control(null),
      amount: this.fb.control(0),
      description: this.fb.control(null),
      accountDestination: this.fb.control(null)
    });
  }

  handleSearchAccount(): void {
    const accountId: string = this.accountFormGroup.value.accountId;
    this.accountObservable = this.accountService.getAccount(accountId, this.currentPage, this.pageSize).pipe(
      catchError(err => {
        this.errorMessage = err.message;
        return throwError(() => err);
      })
    );
  }

  gotoPage(page: number): void {
    this.currentPage = page;
    this.handleSearchAccount();
  }

  handleAccountOperation(): void {
    const accountId: string = this.accountFormGroup.value.accountId;
    const operationType = this.operationFormGroup.value.operationType;
    const amount: number = this.operationFormGroup.value.amount;
    const description: string = this.operationFormGroup.value.description;
    const accountDestination: string = this.operationFormGroup.value.accountDestination;

    if (operationType === 'DEBIT') {
      this.accountService.debit(accountId, amount, description).subscribe({
        next: () => this.onOperationSuccess("Débit effectué."),
        error: err => this.errorMessage = err.error
      });
    } else if (operationType === 'CREDIT') {
      this.accountService.credit(accountId, amount, description).subscribe({
        next: () => this.onOperationSuccess("Crédit effectué."),
        error: err => this.errorMessage = err.error
      });
    } else if (operationType === 'TRANSFER') {
      this.accountService.transfer(accountId, accountDestination, amount).subscribe({
        next: () => this.onOperationSuccess("Virement effectué."),
        error: err => this.errorMessage = err.error
      });
    }
  }

  private onOperationSuccess(message: string): void {
    alert(message);
    this.operationFormGroup.reset();
    this.handleSearchAccount(); // rafraîchit le solde et l'historique
  }
}
