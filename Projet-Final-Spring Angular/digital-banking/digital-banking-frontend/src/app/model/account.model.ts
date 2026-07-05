export interface AccountOperation {
  id: number;
  operationDate: Date;
  amount: number;
  type: string;        // "DEBIT" ou "CREDIT"
  description: string;
}

// Correspond à AccountHistoryDTO côté backend
export interface AccountHistory {
  accountId: string;
  balance: number;
  currentPage: number;
  totalPages: number;
  pageSize: number;
  accountOperationDTOS: AccountOperation[];
}
