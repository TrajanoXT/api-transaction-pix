package api.transaction.pix.controller;

import api.transaction.pix.dto.PixTransactionRequest;
import api.transaction.pix.dto.PixTransactionResponse;
import api.transaction.pix.service.PixTransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/transaction")
@RequiredArgsConstructor
public class PixTransactionController {
    private final PixTransactionService pixTransactionService;

    @PostMapping("/{uuid}")
    public ResponseEntity<PixTransactionResponse> transaction(@PathVariable UUID senderId,
                                                              @RequestBody PixTransactionRequest pixTransactionRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(pixTransactionService.transfer(senderId,pixTransactionRequest));
    }
}
