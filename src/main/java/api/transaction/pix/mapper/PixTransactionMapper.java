package api.transaction.pix.mapper;

import api.transaction.pix.dto.PixTransactionRequest;
import api.transaction.pix.dto.PixTransactionResponse;
import api.transaction.pix.entity.PixKey;
import api.transaction.pix.entity.PixTransaction;
import api.transaction.pix.entity.User;
import api.transaction.pix.enums.TransactionStatus;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class PixTransactionMapper {

    public PixTransactionResponse toResponse(PixTransaction pixTransaction, User receiver, PixKey receiverKey){
        return new PixTransactionResponse(
                pixTransaction.getId(),
                pixTransaction.getAmount(),
                receiver.getName(),
                receiverKey.getKey(),
                pixTransaction.getStatus(),
                pixTransaction.getCreatedAt()
        );
    }

    public PixTransaction toEntity(User senderUser, User receiverUser, PixTransactionRequest request,PixKey receiverKey){
        PixTransaction pixTransaction = new PixTransaction();
        pixTransaction.setSender(senderUser);
        pixTransaction.setReceiver(receiverUser);
        pixTransaction.setAmount(request.amount());
        pixTransaction.setReceiverKey(receiverKey);
        pixTransaction.setStatus(TransactionStatus.CREATED);
        pixTransaction.setCreatedAt(LocalDateTime.now());
        return pixTransaction;
    }
}
