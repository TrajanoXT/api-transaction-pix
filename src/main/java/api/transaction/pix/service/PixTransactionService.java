package api.transaction.pix.service;

import api.transaction.pix.dto.PixTransactionRequest;
import api.transaction.pix.dto.PixTransactionResponse;
import api.transaction.pix.entity.PixKey;
import api.transaction.pix.entity.PixTransaction;
import api.transaction.pix.entity.User;
import api.transaction.pix.enums.TransactionStatus;
import api.transaction.pix.exception.*;
import api.transaction.pix.repository.PixKeyRepository;
import api.transaction.pix.repository.PixTransactionRepository;
import api.transaction.pix.repository.UserRepository;
import org.springframework.transaction.annotation.Transactional;
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
        if (request.amount() == null || request.amount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidAmountException("Invalid amount");
        }
        User senderUser = userRepository.findByIdWithLock(sender)
                .orElseThrow(() -> new UserNotFoundException("User not found"));


        PixKey receiverKey = pixKeyRepository.findByKey(request.key())
                .orElseThrow(()->new PixKeyNotFoundException("Key not found"));

        User receiverUser = userRepository.findByIdWithLock(receiverKey.getOwner().getId())
                .orElseThrow(()->new UserNotFoundException("Receiver not found"));

        if (senderUser.getId().equals(receiverUser.getId())) {
            throw new SelfTransferException("You cannot transfer to yourself");
        }

        if (senderUser.getBalance().compareTo(request.amount()) < 0) {
            throw new InsufficientBalanceException("Insufficient balance");
        }

        senderUser.setBalance(
                senderUser.getBalance().subtract(request.amount()));

        receiverUser.setBalance(
                receiverUser.getBalance().add(request.amount()));

        PixTransaction pixTransaction = new PixTransaction();
        pixTransaction.setSender(senderUser);
        pixTransaction.setReceiver(receiverUser);
        pixTransaction.setAmount(request.amount());
        pixTransaction.setStatus(TransactionStatus.AUTHORIZED);
        pixTransaction.setCreatedAt(LocalDateTime.now());
        pixTransaction.setReceiverKey(receiverKey);
        pixTransactionRepository.save(pixTransaction);
        userRepository.save(senderUser);
        userRepository.save(receiverUser);

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
