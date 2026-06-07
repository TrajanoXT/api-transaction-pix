package api.transaction.pix.mapper;

import api.transaction.pix.dto.CreateUserRequest;
import api.transaction.pix.dto.UserResponseDto;
import api.transaction.pix.entity.User;
import api.transaction.pix.service.UserService;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class UserMapper {

    public User toEntity(CreateUserRequest createUserRequest) {
        User user = new User();
        user.setCpf(UserService.clearCpf(createUserRequest.cpf()));
        user.setName(createUserRequest.name());
        user.setBalance(BigDecimal.ZERO);
        return user;
    }

    public UserResponseDto toResponse(User user){
        return new UserResponseDto(
                user.getName(),
                user.getCpf(),
                user.getBalance(),
                user.getId()
        );
    }
}
