package api.transaction.pix.service;

import api.transaction.pix.dto.PixTransactionRequest;
import api.transaction.pix.dto.PixTransactionResponse;
import api.transaction.pix.entity.PixKey;
import api.transaction.pix.entity.PixTransaction;
import api.transaction.pix.entity.User;
import api.transaction.pix.enums.TransactionStatus;
import api.transaction.pix.exception.CpfAlreadyExistsException;
import api.transaction.pix.repository.PixKeyRepository;
import api.transaction.pix.repository.PixTransactionRepository;
import api.transaction.pix.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PixTransactionService {
    private final PixTransactionRepository pixTransactionRepository;
    private final UserRepository userRepository;
    private final PixKeyRepository pixKeyRepository;

    @Transactional
    public PixTransactionResponse transfer(UUID sender, PixTransactionRequest request) {
        User senderUser = userRepository.findById(sender)
                .orElseThrow(() -> new CpfAlreadyExistsException("User not found"));

        PixKey receiverKey = pixKeyRepository.findByKeyValue(request.key())
                .orElseThrow(()->new CpfAlreadyExistsException("Key not found"));

        User receiverUser = receiverKey.getOwner();

        if (senderUser.getId().equals(receiverUser.getId())) {
            throw new IllegalArgumentException("You cannot transfer to yourself");
        }

        if (request.amount() == null ||
                request.amount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Invalid amount");
        }

        if (senderUser.getBalance().compareTo(request.amount()) < 0) {
            throw new IllegalArgumentException("Insufficient balance");
        }

        senderUser.setBalance(
                senderUser.getBalance().subtract(request.amount()));

        receiverUser.setBalance(
                receiverUser.getBalance().add(request.amount()));

        userRepository.save(senderUser);
        userRepository.save(receiverUser);


        PixTransaction pixTransaction = new PixTransaction();
        pixTransaction.setSender(senderUser);
        pixTransaction.setReceiver(receiverUser);
        pixTransaction.setAmount(request.amount());
        pixTransaction.setStatus(TransactionStatus.AUTHORIZED);
        pixTransaction.setCreatedAt(LocalDateTime.now());
        pixTransaction.setReceiverKey(receiverKey);
        pixTransactionRepository.save(pixTransaction);

        return new PixTransactionResponse(
                pixTransaction.getId(),
                pixTransaction.getAmount(),
                receiverUser.getName(),
                receiverKey.getKey(),
                pixTransaction.getStatus(),
                pixTransaction.getCreatedAt()
        );
    }
}
