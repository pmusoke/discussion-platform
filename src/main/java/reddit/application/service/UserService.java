package reddit.application.service;

import java.util.List;

import reddit.application.dto.RegistrationDto;
import reddit.application.dto.UserEntityDto;
import reddit.application.models.UserEntity;

public interface UserService {
    void saveUser(RegistrationDto registrationDto);

    UserEntity findByEmail(String email);

    UserEntity findByUsername(String username);

    UserEntity findById(Long id);

    void updateUser(UserEntityDto userEntityDto);

    List<UserEntity> searchUsers(String query);


    

}
