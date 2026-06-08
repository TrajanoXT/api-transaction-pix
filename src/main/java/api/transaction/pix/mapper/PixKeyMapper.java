package api.transaction.pix.mapper;

import api.transaction.pix.dto.PixKeyRequest;
import api.transaction.pix.dto.PixKeyResponse;
import api.transaction.pix.entity.PixKey;
import api.transaction.pix.entity.User;
import api.transaction.pix.enums.PixKeyType;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class PixKeyMapper {
    public PixKeyResponse toResponse(PixKey pixKey, User owner) {
        return new PixKeyResponse(
                pixKey.getId(),
                pixKey.getKey(),
                pixKey.getType(),
                owner.getId(),
                owner.getName()
        );
    }
}
