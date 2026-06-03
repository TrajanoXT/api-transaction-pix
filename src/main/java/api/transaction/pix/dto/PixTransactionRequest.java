package api.transaction.pix.dto;

import java.math.BigDecimal;

public record PixTransactionRequest(
        BigDecimal amount,
        String key
){}
