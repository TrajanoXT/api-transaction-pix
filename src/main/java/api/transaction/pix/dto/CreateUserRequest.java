package api.transaction.pix.dto;

import org.springframework.context.annotation.Bean;

import java.util.UUID;


public record CreateUserRequest(
        String name,
        String cpf
) {}
