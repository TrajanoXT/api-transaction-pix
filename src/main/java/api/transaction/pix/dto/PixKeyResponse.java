package api.transaction.pix.dto;

import api.transaction.pix.enums.PixKeyType;
import java.util.UUID;

public record PixKeyResponse (
    UUID id,
    String key,
    PixKeyType type,
    UUID ownerId,
    String ownerName
){}
