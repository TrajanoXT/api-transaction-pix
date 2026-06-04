package api.transaction.pix.controller;

import api.transaction.pix.dto.PixTransactionRequest;
import api.transaction.pix.dto.PixTransactionResponse;
import api.transaction.pix.dto.UserResponseDto;
import api.transaction.pix.service.PixTransactionService;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/transaction")
@RequiredArgsConstructor
public class PixTransactionController {
    private final PixTransactionService pixTransactionService;

    @PostMapping("/{senderId}/transfer")
    public ResponseEntity<PixTransactionResponse> transfer(
            @PathVariable UUID senderId,
            @RequestBody PixTransactionRequest request){
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(pixTransactionService.transfer(senderId, request));
    }
    @PostMapping("/{transactionId}/reverse")
    public ResponseEntity<Void> reverse(@PathVariable UUID transactionId){
        pixTransactionService.reverse(transactionId);
        return ResponseEntity.ok().build();
    }
    @PostMapping("/{transactionId}/fail")
    public ResponseEntity<Void> fail(@PathVariable UUID transactionId,
                                     @RequestParam String reason){
        pixTransactionService.fail(transactionId, reason);
        return ResponseEntity.ok().build();
    }

}
