package api.transaction.pix.mapper;

import api.transaction.pix.dto.PixKeyResponse;
import api.transaction.pix.entity.PixKey;
import api.transaction.pix.entity.User;
import org.springframework.stereotype.Component;

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
