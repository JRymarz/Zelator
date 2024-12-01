package org.zelator.service;


import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.zelator.dto.UserDto;
import org.zelator.entity.User;
import org.zelator.repository.UserRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {


    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;


    public boolean authenticate(String email, String password) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Użytkownik z takim emailem nie istnieje."));

        if(!bCryptPasswordEncoder.matches(password, user.getPassword())) {
//            throw new BadCredentialsException("Hasło jest nie prawidłowe.");
            return false;
        }

        return true;
    }


    public User getByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Użytkownik z takim emailem nie istnieje."));
    }


    public void createZelator(UserDto userDto) {
        if(userRepository.existsByEmail(userDto.getEmail())) {
            throw new IllegalArgumentException("Użytkownik o podanym adresie email już istnieje.");
        }

        User user = new User();
        user.setEmail(userDto.getEmail());
        user.setPassword(bCryptPasswordEncoder.encode(userDto.getPassword()));
        user.setRole(User.Role.Zelator);
        user.setActive(true);
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());

        userRepository.save(user);
    }

}
