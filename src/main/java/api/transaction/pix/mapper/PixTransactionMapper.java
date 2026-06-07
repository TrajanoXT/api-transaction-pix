package api.transaction.pix.mapper;

import api.transaction.pix.dto.PixTransactionResponse;
import api.transaction.pix.entity.PixTransaction;
import org.springframework.stereotype.Component;

@Component
public class PixTransactionMapper {

    public PixTransactionResponse toResponse(PixTransaction pixTransaction){
        return new PixTransactionResponse(
                pixTransaction.getId(),
                pixTransaction.getAmount(),
                receiverUser.getName(),
                receiverKey.getKey(),
                pixTransaction.getStatus(),
                pixTransaction.getCreatedAt()
        )
    }
}
