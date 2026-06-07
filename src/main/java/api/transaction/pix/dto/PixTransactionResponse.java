package api.transaction.pix.dto;

import api.transaction.pix.enums.TransactionStatus;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record PixTransactionResponse(
        UUID transactionId,
        BigDecimal amount,
        String receiverName,
        String receiverKey,
        TransactionStatus status,
        LocalDateTime createdAt
){}
