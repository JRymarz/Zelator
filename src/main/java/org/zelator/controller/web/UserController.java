package org.zelator.controller.web;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.zelator.dto.LoginRequest;
import org.zelator.dto.UserCookieDto;
import org.zelator.dto.UserDto;
import org.zelator.entity.User;
import org.zelator.service.UserService;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:3000/", allowCredentials = "true")
@RequiredArgsConstructor
public class UserController {


    private final UserService userService;


    @PostMapping("/login")
    @CrossOrigin
    public ResponseEntity<String> login(@RequestBody LoginRequest loginRequest, HttpSession session, HttpServletRequest request) {
        try {
            boolean isAuthenticated = userService.authenticate(loginRequest.getEmail(), loginRequest.getPassword());

            User user = userService.getByEmail(loginRequest.getEmail());

            System.out.println(isAuthenticated);
            if(isAuthenticated) {
                if(User.Role.Member.equals(user.getRole())) {
                    return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Brak dostępu. Wypróbuj aplikacje mobilną.");
                }

                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(user.getEmail(), null, AuthorityUtils.createAuthorityList(user.getRole().name()));
                SecurityContextHolder.getContext().setAuthentication(authentication);
                SecurityContextHolder.getContext().setAuthentication(authentication);

                session.setAttribute("user", user);
                request.getSession().setAttribute("SPRING_SECURITY_CONTEXT", SecurityContextHolder.getContext());
                System.out.println(user.getEmail());
                return ResponseEntity.ok("Zalogowano poprawnie.");
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Niepoprawne hasło lub email.");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Napotkano nieznany błąd.");
        }
    }


    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpSession session) {
        System.out.println("logout");
        session.invalidate();
        SecurityContextHolder.clearContext();
        return ResponseEntity.ok("Wylogowano pomyślnie.");
    }


    @GetMapping("/current-user")
    @CrossOrigin
    public ResponseEntity<?> getCurrentUser(HttpSession session){
        User user = (User) session.getAttribute("user");

        if(user == null) {
            System.out.println("No user");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Nie jesteś zalogowany.");
        }

        UserCookieDto userCookieDto = new UserCookieDto();
        userCookieDto.setId(user.getId());
        userCookieDto.setRole(user.getRole().name());
        userCookieDto.setEmail(user.getEmail());
        userCookieDto.setFirstName(user.getFirstName());
        userCookieDto.setLastName(user.getLastName());

        if(user.getGroup() != null) {
            UserCookieDto.GroupDto groupDto = new UserCookieDto.GroupDto();
            groupDto.setId(user.getGroup().getId());
            groupDto.setName(user.getGroup().getName());

            userCookieDto.setGroup(groupDto);
        }

        System.out.println(userCookieDto.getRole());

        return ResponseEntity.ok(userCookieDto);
    }


    @PostMapping("/create-user")
    @CrossOrigin
    @PreAuthorize("hasAuthority('Zelator')")
    public ResponseEntity<String> createUser(@RequestBody UserDto userDto) {
        try {
            userService.createUser(userDto);
            return ResponseEntity.ok("Konto użytkownika zostało utworzone.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Nie udało się utworzyć nowego konta.");
        }
    }


    @GetMapping("/members")
    @CrossOrigin
    public ResponseEntity<Page<UserCookieDto>> getMembers(
            @RequestParam(required = false) Long groupId,
            @RequestParam(required = false) Boolean hasGroup,
            @RequestParam(required = false) String firstName,
            @RequestParam(required = false) String lastName,
            Pageable pageable) {

        Page<User> members = userService.getFilteredMembers(pageable, groupId, hasGroup, firstName, lastName);

        Page<UserCookieDto> memberDtos = members.map(user -> {
            UserCookieDto userCookieDto = new UserCookieDto();
            userCookieDto.setId(user.getId());
            userCookieDto.setEmail(user.getEmail());
            userCookieDto.setRole(user.getRole().name());
            userCookieDto.setFirstName(user.getFirstName());
            userCookieDto.setLastName(user.getLastName());

            if(user.getGroup() != null) {
                UserCookieDto.GroupDto groupDto = new UserCookieDto.GroupDto();
                groupDto.setName(user.getGroup().getName());
                groupDto.setId(user.getGroup().getId());

                userCookieDto.setGroup(groupDto);
            }

            return userCookieDto;
        });

        return ResponseEntity.ok(memberDtos);
    }


    @PatchMapping("/members/{memberId}/assign")
    @CrossOrigin
    public ResponseEntity<?> assignMemberToGroup(@PathVariable Long memberId, @RequestBody Long groupId) {
        System.out.println("MemberId: " + memberId + ", groupId: " + groupId);
        try {
            userService.assignMemberToGroup(memberId, groupId);
            return ResponseEntity.ok("Członek został przypisany do grupy.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Błąd przypisywania członka do grupy.");
        }
    }

}
