package api.transaction.pix.dto;

import java.util.UUID;

public record PixKeyRequest(
        String key,
        String type,
        UUID ownerId
){}