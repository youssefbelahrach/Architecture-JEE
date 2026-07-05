package ma.enset.digitalbanking.services;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ma.enset.digitalbanking.dtos.*;
import ma.enset.digitalbanking.entities.*;
import ma.enset.digitalbanking.enums.AccountStatus;
import ma.enset.digitalbanking.enums.OperationType;
import ma.enset.digitalbanking.exceptions.BalanceNotSufficientException;
import ma.enset.digitalbanking.exceptions.BankAccountNotFoundException;
import ma.enset.digitalbanking.exceptions.CustomerNotFoundException;
import ma.enset.digitalbanking.mappers.BankAccountMapperImpl;
import ma.enset.digitalbanking.repositories.AccountOperationRepository;
import ma.enset.digitalbanking.repositories.BankAccountRepository;
import ma.enset.digitalbanking.repositories.CustomerRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
@AllArgsConstructor
@Slf4j
public class BankAccountServiceImpl implements BankAccountService {

    private CustomerRepository customerRepository;
    private BankAccountRepository bankAccountRepository;
    private AccountOperationRepository accountOperationRepository;
    private BankAccountMapperImpl dtoMapper;

    // ============================ CLIENTS ============================

    @Override
    public CustomerDTO saveCustomer(CustomerDTO customerDTO) {
        log.info("Enregistrement d'un nouveau client");
        Customer customer = dtoMapper.fromCustomerDTO(customerDTO);
        Customer savedCustomer = customerRepository.save(customer);
        return dtoMapper.fromCustomer(savedCustomer);
    }

    @Override
    public List<CustomerDTO> listCustomers() {
        return customerRepository.findAll()
                .stream()
                .map(dtoMapper::fromCustomer)
                .collect(Collectors.toList());
    }

    @Override
    public CustomerDTO getCustomer(Long customerId) throws CustomerNotFoundException {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new CustomerNotFoundException("Client introuvable"));
        return dtoMapper.fromCustomer(customer);
    }

    @Override
    public CustomerDTO updateCustomer(CustomerDTO customerDTO) {
        log.info("Mise à jour d'un client");
        Customer customer = dtoMapper.fromCustomerDTO(customerDTO);
        Customer savedCustomer = customerRepository.save(customer);
        return dtoMapper.fromCustomer(savedCustomer);
    }

    @Override
    public void deleteCustomer(Long customerId) {
        customerRepository.deleteById(customerId);
    }

    @Override
    public List<CustomerDTO> searchCustomers(String keyword) {
        return customerRepository.findByNameContainingIgnoreCase(keyword)
                .stream()
                .map(dtoMapper::fromCustomer)
                .collect(Collectors.toList());
    }

    // ============================ COMPTES ============================

    @Override
    public CurrentBankAccountDTO saveCurrentBankAccount(double initialBalance, double overDraft, Long customerId)
            throws CustomerNotFoundException {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new CustomerNotFoundException("Client introuvable"));
        CurrentAccount currentAccount = new CurrentAccount();
        currentAccount.setId(UUID.randomUUID().toString());
        currentAccount.setCreatedAt(new Date());
        currentAccount.setBalance(initialBalance);
        currentAccount.setOverDraft(overDraft);
        currentAccount.setStatus(AccountStatus.CREATED);
        currentAccount.setCurrency("MAD");
        currentAccount.setCustomer(customer);
        CurrentAccount savedAccount = bankAccountRepository.save(currentAccount);
        return dtoMapper.fromCurrentBankAccount(savedAccount);
    }

    @Override
    public SavingBankAccountDTO saveSavingBankAccount(double initialBalance, double interestRate, Long customerId)
            throws CustomerNotFoundException {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new CustomerNotFoundException("Client introuvable"));
        SavingAccount savingAccount = new SavingAccount();
        savingAccount.setId(UUID.randomUUID().toString());
        savingAccount.setCreatedAt(new Date());
        savingAccount.setBalance(initialBalance);
        savingAccount.setInterestRate(interestRate);
        savingAccount.setStatus(AccountStatus.CREATED);
        savingAccount.setCurrency("MAD");
        savingAccount.setCustomer(customer);
        SavingAccount savedAccount = bankAccountRepository.save(savingAccount);
        return dtoMapper.fromSavingBankAccount(savedAccount);
    }

    @Override
    public List<BankAccountDTO> bankAccountList() {
        List<BankAccount> bankAccounts = bankAccountRepository.findAll();
        return bankAccounts.stream().map(this::toBankAccountDTO).collect(Collectors.toList());
    }

    @Override
    public BankAccountDTO getBankAccount(String accountId) throws BankAccountNotFoundException {
        BankAccount bankAccount = bankAccountRepository.findById(accountId)
                .orElseThrow(() -> new BankAccountNotFoundException("Compte introuvable"));
        return toBankAccountDTO(bankAccount);
    }

    /** Convertit un compte vers le bon sous-type de DTO selon sa classe réelle. */
    private BankAccountDTO toBankAccountDTO(BankAccount bankAccount) {
        if (bankAccount instanceof SavingAccount savingAccount) {
            return dtoMapper.fromSavingBankAccount(savingAccount);
        } else {
            return dtoMapper.fromCurrentBankAccount((CurrentAccount) bankAccount);
        }
    }

    // ============================ OPERATIONS ============================

    @Override
    public void debit(String accountId, double amount, String description)
            throws BankAccountNotFoundException, BalanceNotSufficientException {
        BankAccount bankAccount = bankAccountRepository.findById(accountId)
                .orElseThrow(() -> new BankAccountNotFoundException("Compte introuvable"));
        if (bankAccount.getBalance() < amount) {
            throw new BalanceNotSufficientException("Solde insuffisant");
        }
        AccountOperation operation = new AccountOperation();
        operation.setType(OperationType.DEBIT);
        operation.setAmount(amount);
        operation.setDescription(description);
        operation.setOperationDate(new Date());
        operation.setBankAccount(bankAccount);
        accountOperationRepository.save(operation);
        bankAccount.setBalance(bankAccount.getBalance() - amount);
        bankAccountRepository.save(bankAccount);
    }

    @Override
    public void credit(String accountId, double amount, String description)
            throws BankAccountNotFoundException {
        BankAccount bankAccount = bankAccountRepository.findById(accountId)
                .orElseThrow(() -> new BankAccountNotFoundException("Compte introuvable"));
        AccountOperation operation = new AccountOperation();
        operation.setType(OperationType.CREDIT);
        operation.setAmount(amount);
        operation.setDescription(description);
        operation.setOperationDate(new Date());
        operation.setBankAccount(bankAccount);
        accountOperationRepository.save(operation);
        bankAccount.setBalance(bankAccount.getBalance() + amount);
        bankAccountRepository.save(bankAccount);
    }

    @Override
    public void transfer(String accountIdSource, String accountIdDestination, double amount)
            throws BankAccountNotFoundException, BalanceNotSufficientException {
        // Un virement = un débit du compte source + un crédit du compte destination.
        // La méthode étant @Transactional, si le crédit échoue, le débit est annulé (rollback).
        debit(accountIdSource, amount, "Virement vers " + accountIdDestination);
        credit(accountIdDestination, amount, "Virement depuis " + accountIdSource);
    }

    // ============================ HISTORIQUE ============================

    @Override
    public List<AccountOperationDTO> accountHistory(String accountId) {
        return accountOperationRepository.findByBankAccountId(accountId)
                .stream()
                .map(dtoMapper::fromAccountOperation)
                .collect(Collectors.toList());
    }

    @Override
    public AccountHistoryDTO getAccountHistory(String accountId, int page, int size)
            throws BankAccountNotFoundException {
        BankAccount bankAccount = bankAccountRepository.findById(accountId)
                .orElseThrow(() -> new BankAccountNotFoundException("Compte introuvable"));
        Page<AccountOperation> accountOperations =
                accountOperationRepository.findByBankAccountId(accountId, PageRequest.of(page, size));

        AccountHistoryDTO dto = new AccountHistoryDTO();
        List<AccountOperationDTO> operationDTOS = accountOperations.getContent()
                .stream()
                .map(dtoMapper::fromAccountOperation)
                .collect(Collectors.toList());
        dto.setAccountOperationDTOS(operationDTOS);
        dto.setAccountId(bankAccount.getId());
        dto.setBalance(bankAccount.getBalance());
        dto.setCurrentPage(page);
        dto.setPageSize(size);
        dto.setTotalPages(accountOperations.getTotalPages());
        return dto;
    }
}
