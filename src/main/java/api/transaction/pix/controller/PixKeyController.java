package api.transaction.pix.controller;

import api.transaction.pix.dto.PixKeyRequest;
import api.transaction.pix.dto.PixKeyResponse;
import api.transaction.pix.service.PixKeyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/pix-key")
@RequiredArgsConstructor
public class PixKeyController {
    private final PixKeyService pixKeyService;

    @PostMapping
    public ResponseEntity<PixKeyResponse> savePixKey(@RequestBody PixKeyRequest pixKeyRequest){

        return ResponseEntity.status(HttpStatus.CREATED).body(pixKeyService.createPixKey(pixKeyRequest));
    }

}
