package api.transaction.pix.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record UserResponseDto(
        String name,
        String cpf,
        BigDecimal balance,
        UUID id
){}
