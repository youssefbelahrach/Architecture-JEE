package ma.enset.digitalbanking.dtos;

import lombok.Data;

/** DTO exposé par l'API pour un client. */
@Data
public class CustomerDTO {
    private Long id;
    private String name;
    private String email;
}
