package api.transaction.pix.dto;

import java.math.BigDecimal;

public record DepositRequest(
        BigDecimal amount
) {
}
