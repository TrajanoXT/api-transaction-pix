package api.transaction.pix.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @Column(unique = true)
    private String cpf;

    private BigDecimal balance;
}
