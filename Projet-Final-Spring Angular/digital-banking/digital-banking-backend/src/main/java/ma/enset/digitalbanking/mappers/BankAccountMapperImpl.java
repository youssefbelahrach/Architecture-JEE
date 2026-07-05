package ma.enset.digitalbanking.mappers;

import ma.enset.digitalbanking.dtos.*;
import ma.enset.digitalbanking.entities.*;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

/**
 * Convertit les entités JPA en DTOs (et inversement).
 * On utilise BeanUtils.copyProperties pour copier les champs de même nom.
 */
@Service
public class BankAccountMapperImpl {

    public CustomerDTO fromCustomer(Customer customer) {
        CustomerDTO customerDTO = new CustomerDTO();
        BeanUtils.copyProperties(customer, customerDTO);
        return customerDTO;
    }

    public Customer fromCustomerDTO(CustomerDTO customerDTO) {
        Customer customer = new Customer();
        BeanUtils.copyProperties(customerDTO, customer);
        return customer;
    }

    public SavingBankAccountDTO fromSavingBankAccount(SavingAccount savingAccount) {
        SavingBankAccountDTO dto = new SavingBankAccountDTO();
        BeanUtils.copyProperties(savingAccount, dto);
        dto.setCustomerDTO(fromCustomer(savingAccount.getCustomer()));
        dto.setType(savingAccount.getClass().getSimpleName()); // "SavingAccount"
        return dto;
    }

    public SavingAccount fromSavingBankAccountDTO(SavingBankAccountDTO dto) {
        SavingAccount savingAccount = new SavingAccount();
        BeanUtils.copyProperties(dto, savingAccount);
        savingAccount.setCustomer(fromCustomerDTO(dto.getCustomerDTO()));
        return savingAccount;
    }

    public CurrentBankAccountDTO fromCurrentBankAccount(CurrentAccount currentAccount) {
        CurrentBankAccountDTO dto = new CurrentBankAccountDTO();
        BeanUtils.copyProperties(currentAccount, dto);
        dto.setCustomerDTO(fromCustomer(currentAccount.getCustomer()));
        dto.setType(currentAccount.getClass().getSimpleName()); // "CurrentAccount"
        return dto;
    }

    public CurrentAccount fromCurrentBankAccountDTO(CurrentBankAccountDTO dto) {
        CurrentAccount currentAccount = new CurrentAccount();
        BeanUtils.copyProperties(dto, currentAccount);
        currentAccount.setCustomer(fromCustomerDTO(dto.getCustomerDTO()));
        return currentAccount;
    }

    public AccountOperationDTO fromAccountOperation(AccountOperation accountOperation) {
        AccountOperationDTO dto = new AccountOperationDTO();
        BeanUtils.copyProperties(accountOperation, dto);
        return dto;
    }
}
