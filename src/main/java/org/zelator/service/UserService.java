package org.zelator.service;


import jakarta.persistence.EntityNotFoundException;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.zelator.dto.UserDto;
import org.zelator.entity.Group;
import org.zelator.entity.Mystery;
import org.zelator.entity.User;
import org.zelator.repository.GroupRepository;
import org.zelator.repository.MysteryRepository;
import org.zelator.repository.UserRepository;
import org.zelator.specification.UserSpecifications;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {


    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final GroupRepository groupRepository;
    private final MysteryRepository mysteryRepository;


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


    public void createUser(UserDto userDto) {
        if(userRepository.existsByEmail(userDto.getEmail())) {
            throw new IllegalArgumentException("Użytkownik o podanym adresie email już istnieje.");
        }

        User user = new User();
        user.setEmail(userDto.getEmail());
        user.setPassword(bCryptPasswordEncoder.encode(userDto.getPassword()));
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setRole(User.Role.Member);
        user.setActive(true);

        userRepository.save(user);
    }


    public User getCurrentUser() {
        System.out.println("Wewnatrz getCurrent");
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        System.out.println("Po pobraniu princnipal");
        System.out.println(principal);

        if(principal instanceof String) {
            String email = (String) principal;
            System.out.println("Email zalogowanego " + email);

            return userRepository.findByEmail(email)
                    .orElseThrow(() -> new UsernameNotFoundException("Nie znaleziono zalogowanego użytkownika."));
        } else {
            throw new IllegalStateException("Błąd w uwierzytelnieniu.");
        }
    }


    public Page<User> getFilteredMembers(Pageable pageable, Long groupId, Boolean hasGroup, String firstName, String lastName) {
        Specification<User> spec = Specification.where(UserSpecifications.isMember());

        if(groupId != null) {
            spec = spec.and(UserSpecifications.belongsToGroup(groupId));
        }
        if(hasGroup != null) {
            spec = spec.and(UserSpecifications.hasGroup(hasGroup));
        }
        if(firstName != null && !firstName.isEmpty()) {
            spec = spec.and(UserSpecifications.hasFirstName(firstName));
        }
        if(lastName != null && !lastName.isEmpty()) {
            spec = spec.and(UserSpecifications.hasLastName(lastName));
        }

        return userRepository.findAll(spec, pageable);
    }


    public void assignMemberToGroup(Long memberId, Long groupId) {
        User user = userRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("Użytkownik nie istnieje."));

        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new IllegalArgumentException("Grupa nie istnieje."));

        user.setGroup(group);

        userRepository.save(user);
    }


    public void assignMystery(Long memberId, Long mysteryId) {
        User member = userRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("Nie znaleziono użytkownika."));

        Mystery mystery = mysteryRepository.findById(mysteryId)
                .orElseThrow(() -> new IllegalArgumentException("Nie znaleziono tajemnicy."));

        member.setMystery(mystery);
        userRepository.save(member);
    }


    public void removeMember(Long memberId) {
        User user = userRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("Użytkownik nie został znaleziony."));

        if(user.getGroup().getLeader().getId() == user.getId())
            throw new IllegalArgumentException("Zelator nie może usunąc siebie z róży.");

        user.setGroup(null);
        user.setMystery(null);

        userRepository.save(user);
    }


    public User getById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Nie znaleziono użytkownika."));
    }

}
