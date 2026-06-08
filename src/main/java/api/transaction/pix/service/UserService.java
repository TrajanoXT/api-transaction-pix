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
import java.util.List;
import java.util.UUID;
import static api.transaction.pix.utils.ClearCpf.clearCpf;
import static api.transaction.pix.utils.Valid.valid;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

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
                .map(userMapper::toResponse)
                .toList();
    }
    public UserResponseDto home(UUID sender) {
        User user =userRepository.findById(sender)
                .orElseThrow(()->new UserNotFoundException("User not found"));
        return userMapper.toResponse(user);
    }
}
