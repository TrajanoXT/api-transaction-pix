package api.transaction.pix.dto;

import java.math.BigDecimal;

public record PixTransferRequest(
        Long senderId,
        String receiverKey,
        BigDecimal amount
) {}
