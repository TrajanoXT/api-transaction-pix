package api.transaction.pix.dto;

public record PixKeyRequest(
        String key,
        String type
){}