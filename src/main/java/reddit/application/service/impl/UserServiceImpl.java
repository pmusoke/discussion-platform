package reddit.application.service.impl;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import reddit.application.dto.RegistrationDto;
import reddit.application.dto.UserEntityDto;
import reddit.application.models.Role;
import reddit.application.models.UserEntity;
import reddit.application.repository.RoleRepository;
import reddit.application.repository.UserRepository;
import reddit.application.service.UserService;

@Service
public class UserServiceImpl implements UserService {
    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private PasswordEncoder passwordEncoder;


    @Autowired
    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository,
            PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void saveUser(RegistrationDto registrationDto) {
        UserEntity user = new UserEntity();
        user.setUsername(registrationDto.getUsername());
        user.setEmail(registrationDto.getEmail());
        user.setPassword(passwordEncoder.encode(registrationDto.getPassword()));

        if (registrationDto.getPhotoUrl() == "") {
            registrationDto.setPhotoUrl("https://img.magnific.com/premium-vector/vector-flat-illustration-grayscale-avatar-user-profile-person-icon-gender-neutral-silhouette-profile-picture-suitable-social-media-profiles-icons-screensavers-as-templatex9xa_719432-2210.jpg?semt=ais_hybrid&w=740&q=80");
        }
        
        user.setPhotoUrl(registrationDto.getPhotoUrl());


        Role role = roleRepository.findByName("USER");
        user.setRoles(Arrays.asList(role));
        userRepository.save(user);
    }

    @Override
    public UserEntity findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public UserEntity findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public void updateUser(UserEntityDto userEntityDto) {
        UserEntity user = userRepository.findById(userEntityDto.getId()).get();

        user.setEmail(userEntityDto.getEmail());

        if (userEntityDto.getPhotoUrl() == "") {
            userEntityDto.setPhotoUrl("https://img.magnific.com/premium-vector/vector-flat-illustration-grayscale-avatar-user-profile-person-icon-gender-neutral-silhouette-profile-picture-suitable-social-media-profiles-icons-screensavers-as-templatex9xa_719432-2210.jpg?semt=ais_hybrid&w=740&q=80");
        }
        user.setPhotoUrl(userEntityDto.getPhotoUrl());

        userRepository.save(user);
    }

    @Override
    public UserEntity findById(Long id) {
        UserEntity user = userRepository.findById(id).get();
        return user;
    }

    @Override
    public List<UserEntity> searchUsers(String query) {
        List<UserEntity> users = userRepository.searchUsers(query);
        return users;
    }



    

}
