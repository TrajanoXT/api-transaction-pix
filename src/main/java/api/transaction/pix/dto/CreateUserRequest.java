package api.transaction.pix.dto;

public record CreateUserRequest(
        String name,
        String cpf
) {}
