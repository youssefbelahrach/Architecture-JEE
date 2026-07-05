package ma.enset.digitalbanking;

import ma.enset.digitalbanking.dtos.BankAccountDTO;
import ma.enset.digitalbanking.dtos.CurrentBankAccountDTO;
import ma.enset.digitalbanking.dtos.CustomerDTO;
import ma.enset.digitalbanking.dtos.SavingBankAccountDTO;
import ma.enset.digitalbanking.exceptions.BalanceNotSufficientException;
import ma.enset.digitalbanking.exceptions.BankAccountNotFoundException;
import ma.enset.digitalbanking.exceptions.CustomerNotFoundException;
import ma.enset.digitalbanking.services.BankAccountService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.stream.Stream;

@SpringBootApplication
public class DigitalBankingApplication {

    public static void main(String[] args) {
        SpringApplication.run(DigitalBankingApplication.class, args);
    }

    /**
     * Jeu de données de test, construit via la couche service
     * (donc en passant par les DTOs, les mappers et la logique métier).
     * À désactiver quand la base contient déjà des données réelles.
     */
    @Bean
    CommandLineRunner commandLineRunner(BankAccountService bankAccountService) {
        return args -> {

            // 1) Création de clients
            Stream.of("Hassan", "Imane", "Mohamed").forEach(name -> {
                CustomerDTO customer = new CustomerDTO();
                customer.setName(name);
                customer.setEmail(name.toLowerCase() + "@gmail.com");
                bankAccountService.saveCustomer(customer);
            });

            // 2) Deux comptes (courant + épargne) par client
            bankAccountService.listCustomers().forEach(customer -> {
                try {
                    bankAccountService.saveCurrentBankAccount(Math.random() * 90000, 9000, customer.getId());
                    bankAccountService.saveSavingBankAccount(Math.random() * 120000, 5.5, customer.getId());
                } catch (CustomerNotFoundException e) {
                    e.printStackTrace();
                }
            });

            // 3) Opérations de crédit et de débit sur chaque compte
            for (BankAccountDTO bankAccount : bankAccountService.bankAccountList()) {
                String accountId = (bankAccount instanceof SavingBankAccountDTO sa)
                        ? sa.getId()
                        : ((CurrentBankAccountDTO) bankAccount).getId();
                for (int i = 0; i < 10; i++) {
                    try {
                        bankAccountService.credit(accountId, 10000 + Math.random() * 120000, "Crédit de test");
                        bankAccountService.debit(accountId, 1000 + Math.random() * 9000, "Débit de test");
                    } catch (BankAccountNotFoundException | BalanceNotSufficientException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
    }
}
