package api.transaction.pix.service;

import api.transaction.pix.dto.PixTransactionRequest;
import api.transaction.pix.dto.PixTransactionResponse;
import api.transaction.pix.dto.UserResponseDto;
import api.transaction.pix.entity.PixKey;
import api.transaction.pix.entity.PixTransaction;
import api.transaction.pix.entity.User;
import api.transaction.pix.enums.TransactionStatus;
import api.transaction.pix.exception.*;
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
    private final PixKeyRepository pixKeyRepository;
    private final UserRepository userRepository;

    @Transactional
    public PixTransactionResponse transfer(UUID senderId,PixTransactionRequest request){
        if (request.amount()==null||request.amount().compareTo(BigDecimal.ZERO)<=0){
            throw new InvalidAmountException("Invalid amount");
        }
        User senderUser = userRepository.findByIdWithLock(senderId)
                .orElseThrow(()->new UserNotFoundException("User not found"));
        PixKey receiverKey = pixKeyRepository.findByKey(request.key())
                .orElseThrow(()->new PixKeyNotFoundException("Key not found"));
        User receiverUser = userRepository.findByIdWithLock(receiverKey.getOwner().getId())
                .orElseThrow(()->new UserNotFoundException("Receiver not found"));
        if (senderUser.getId().equals(receiverUser.getId())){
            throw new SelfTransferException("Cannot transfer self transfer");
        }
        if (senderUser.getBalance().compareTo(request.amount())<0){
            throw new InsufficientBalanceException("Insufficient balance");
        }

        PixTransaction pixTransaction = new PixTransaction();
        pixTransaction.setSender(senderUser);
        pixTransaction.setReceiver(receiverUser);
        pixTransaction.setAmount(request.amount());
        pixTransaction.setReceiverKey(receiverKey);
        pixTransaction.setStatus(TransactionStatus.CREATED);
        pixTransaction.setCreatedAt(LocalDateTime.now());
        pixTransactionRepository.save(pixTransaction);

        pixTransaction.setStatus(pixTransaction.getStatus().transitionTo(TransactionStatus.AUTHORIZED));
        pixTransaction.setAuthorizedAt(LocalDateTime.now());
        pixTransactionRepository.save(pixTransaction);

        pixTransaction.setStatus(pixTransaction.getStatus().transitionTo(TransactionStatus.PROCESSING));
        pixTransaction.setProcessingAt(LocalDateTime.now());
        pixTransactionRepository.save(pixTransaction);

        senderUser.setBalance(senderUser.getBalance().subtract(request.amount()));
        receiverUser.setBalance(receiverUser.getBalance().add(request.amount()));
        userRepository.save(senderUser);
        userRepository.save(receiverUser);

        pixTransaction.setStatus(pixTransaction.getStatus().transitionTo(TransactionStatus.COMPLETED));
        pixTransaction.setCompletedAt(LocalDateTime.now());
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
    @Transactional
    public void fail(UUID transactionId,String reason){
        PixTransaction tx = pixTransactionRepository.findById(transactionId)
                .orElseThrow(()->new RuntimeException("Transaction Not found"));
        tx.setStatus(tx.getStatus().transitionTo(TransactionStatus.FAILED));
        tx.setFailedAt(LocalDateTime.now());
        tx.setFailReason(reason);
        pixTransactionRepository.save(tx);
    }
    @Transactional
    public void reverse(UUID transactionId){
        PixTransaction tx = pixTransactionRepository.findById(transactionId)
                .orElseThrow(()->new RuntimeException("Transaction Not found"));
        User sender = userRepository.findByIdWithLock(tx.getSender().getId())
                .orElseThrow(()->new UserNotFoundException("User not found"));
        User receiver = userRepository.findByIdWithLock(tx.getReceiver().getId())
                .orElseThrow(()->new UserNotFoundException("Receiver not found"));
        tx.setStatus(tx.getStatus().transitionTo(TransactionStatus.REVERSED));
        tx.setReversedAt(LocalDateTime.now());
        sender.setBalance(sender.getBalance().add(tx.getAmount()));
        receiver.setBalance(receiver.getBalance().subtract(tx.getAmount()));
        pixTransactionRepository.save(tx);
        userRepository.save(sender);
        userRepository.save(receiver);
    }

}
