package api.transaction.pix.service;

import api.transaction.pix.entity.User;
import api.transaction.pix.exception.CpfAlreadyExistsException;
import api.transaction.pix.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DepositService {
    private final UserRepository userRepository;

    public User addBalance(UUID uuid, BigDecimal amount) {
        User user = userRepository.findById(uuid).orElseThrow(()->new CpfAlreadyExistsException("User not found"));
        user.setBalance(user.getBalance() == null
                ? amount
                : user.getBalance().add(amount));
        return userRepository.save(user);
    }
}
