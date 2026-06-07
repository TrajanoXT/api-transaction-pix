package api.transaction.pix.service;

import api.transaction.pix.dto.CreateUserRequest;
import api.transaction.pix.dto.UserResponseDto;
import api.transaction.pix.entity.User;
import api.transaction.pix.exception.CpfAlreadyExistsException;
import api.transaction.pix.exception.InvalidCpfException;
import api.transaction.pix.exception.UserNotFoundException;
import api.transaction.pix.mapper.UserMapper;
import api.transaction.pix.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

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

    public UserResponseDto createUser(CreateUserRequest createUserRequest) {
        String cleanCpf = clearCpf(createUserRequest.cpf());
        if(userRepository.existsByCpf(cleanCpf)) throw  new CpfAlreadyExistsException("CPF already exists");
        if(!valid(cleanCpf)){
            throw new InvalidCpfException("Invalid CPF [Brazilian taxpayer ID number]");
        }
        User user = userMapper.toEntity(createUserRequest);
        userRepository.save(user);
        return userMapper.toResponse(user);
    }

    public List<UserResponseDto> listUsers() {
        return userRepository.findAll()
                .stream()
                .map(user -> new UserResponseDto(user.getName(), user.getCpf(), user.getBalance(), user.getId()))
                .toList();
    }
    public UserResponseDto home(UUID sender) {
        User user =userRepository.findById(sender)
                .orElseThrow(()->new UserNotFoundException("User not found"));
        return new UserResponseDto(
                user.getName(),
                user.getCpf(),
                user.getBalance(),
                user.getId()
        );
    }
    public String getTotalBalance() {
        BigDecimal balance=userRepository.getTotalBalance();
        DecimalFormat  df = new DecimalFormat("#,##0.00");
        return df.format(balance);
    }
}
