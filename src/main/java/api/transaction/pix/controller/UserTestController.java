package api.transaction.pix.controller;

import api.transaction.pix.entity.User;
import api.transaction.pix.service.DepositService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.UUID;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserTestController {
    private final DepositService depositService;

    @PatchMapping("/balance/{userId}")
    public ResponseEntity<User> addBalance(@PathVariable UUID userId,
                                           @RequestParam BigDecimal amount) {
        return ResponseEntity.ok(depositService.addBalance(userId, amount));
    }
}
