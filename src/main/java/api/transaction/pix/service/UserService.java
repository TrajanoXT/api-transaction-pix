package api.transaction.pix.service;

import api.transaction.pix.dto.CreateUserRequest;
import api.transaction.pix.entity.User;
import api.transaction.pix.repository.UserRepository;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public static String clearCpf(String cpf) {
        if (cpf == null) return null;
        return cpf.replaceAll("[^0-9]", "");
    }
    
    public static boolean valid(String cpf) {
        String cpfClear = clearCpf(cpf);
        if (cpfClear == null || cpfClear.length() != 11 || cpfClear.matches("(\\d)\\1{10}")) {
            return false;
        }

        try {
            int sum = 0;
            int weight = 10;
            for (int i = 0; i < 9; i++) {
                sum += (cpfClear.charAt(i) - '0') * weight--;
            }
            int resto = 11 - (sum % 11);
            char digit1 = (resto == 10 || resto == 11) ? '0' : (char) (resto + '0');
            
            sum = 0;
            weight = 11;
            for (int i = 0; i < 10; i++) {
                sum += (cpfClear.charAt(i) - '0') * weight--;
            }
            resto = 11 - (sum % 11);
            char digit2 = (resto == 10 || resto == 11) ? '0' : (char) (resto + '0');
            
            return digit1 == cpfClear.charAt(9) && digit2 == cpfClear.charAt(10);

        } catch (Exception e) {
            return false;
        }
    }

    public User createUser(CreateUserRequest createUserRequest) {
        User user = new User();
        if(valid(createUserRequest.cpf())==false){
            throw new IllegalArgumentException("Cpf invalid");
        }
        user.setCpf(createUserRequest.cpf());
        user.setName(createUserRequest.name());
        user.setBalance(BigDecimal.ZERO);
        userRepository.save(user);
        return user;
    }

}
