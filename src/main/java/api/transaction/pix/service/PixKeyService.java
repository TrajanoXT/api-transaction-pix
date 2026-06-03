package api.transaction.pix.service;

import api.transaction.pix.dto.PixKeyRequest;
import api.transaction.pix.entity.PixKey;
import api.transaction.pix.entity.User;
import api.transaction.pix.enums.PixKeyType;
import api.transaction.pix.exception.InvalidPixKeyException;
import api.transaction.pix.exception.UserNotFoundException;
import api.transaction.pix.repository.PixKeyRepository;
import api.transaction.pix.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class PixKeyService {
    private final PixKeyRepository pixKeyRepository;
    private final UserRepository userRepository;

    private static final String REGEX_CPF = "^\\d{11}$";
    private static final String REGEX_EMAIL = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$";
    private static final String REGEX_TELEFONE = "^\\+[1-9]\\d{10,14}$";


    public PixKey createPixKey(PixKeyRequest dto) {

        User owner = userRepository.findById(dto.ownerId())
                .orElseThrow(() -> new UserNotFoundException("Owner not found"));

        boolean isRandom = "RANDOM".equals(dto.type());

        if (!isRandom && (dto.key() == null || dto.key().isBlank())) {
            throw new InvalidPixKeyException("Key cannot be null or empty");
        }
        String input = dto.key();
        PixKey pixKey = new PixKey();

        if (isRandom) {
            pixKey.setType(PixKeyType.RANDOM);
            pixKey.setKey(UUID.randomUUID().toString());
            pixKey.setOwner(owner);
        }else {
            String digitsOnly = input.replaceAll("[\\.\\-]", "");

            if (Pattern.matches(REGEX_CPF, digitsOnly)) {
                pixKey.setType(PixKeyType.CPF);
                pixKey.setKey(digitsOnly);
                pixKey.setOwner(owner);
            }else if (Pattern.matches(REGEX_EMAIL, input)) {
                pixKey.setType(PixKeyType.EMAIL);
                pixKey.setKey(input);
                pixKey.setOwner(owner);
            }else if (Pattern.matches(REGEX_TELEFONE, input)) {
                pixKey.setType(PixKeyType.PHONE);
                pixKey.setKey(input);
                pixKey.setOwner(owner);
            }else {
                throw new InvalidPixKeyException("Invalid key format or type mismatch");
            }
        }
        pixKeyRepository.save(pixKey);
        return pixKey;
    }
}
