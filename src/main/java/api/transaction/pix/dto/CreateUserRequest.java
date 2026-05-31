package api.transaction.pix.dto;

import org.springframework.context.annotation.Bean;


public record CreateUserRequest(
        String name,
        String cpf  ) {}
