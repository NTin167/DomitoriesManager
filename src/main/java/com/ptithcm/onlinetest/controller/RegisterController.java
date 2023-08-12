package com.ptithcm.onlinetest.controller;

import com.ptithcm.onlinetest.model.User;
import com.ptithcm.onlinetest.model.VerificationToken;
import com.ptithcm.onlinetest.payload.dto.PasswordDto;
import com.ptithcm.onlinetest.payload.request.LoginRequest;
import com.ptithcm.onlinetest.payload.request.SignUpRequest;
import com.ptithcm.onlinetest.payload.response.UserResponse;
import com.ptithcm.onlinetest.registration.OnRegistrationCompleteEvent;
import com.ptithcm.onlinetest.repository.UserRepository;
import com.ptithcm.onlinetest.repository.VerificationTokenRepository;
import com.ptithcm.onlinetest.security.JwtTokenProvider;
import com.ptithcm.onlinetest.security.UserSecurityService;
import com.ptithcm.onlinetest.service.UserService;
import com.ptithcm.onlinetest.util.GenericResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.MessageSource;
import org.springframework.core.env.Environment;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.ValidationException;
import java.util.Locale;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/auth")
public class RegisterController {
    private final Logger LOGGER = LoggerFactory.getLogger(getClass());

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserService userService;

    @Autowired
    VerificationTokenRepository tokenRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    JwtTokenProvider tokenProvider;

    @Autowired
    ApplicationEventPublisher eventPublisher;

    @Autowired
    JavaMailSender mailSender;

    @Autowired
    Environment env;

    @Autowired
    MessageSource messages;

    @Autowired
    UserSecurityService userSecurityService;

    @PostMapping("/registration")
    public GenericResponse registerUserAccount(@Valid @RequestBody SignUpRequest signUpRequest, HttpServletRequest request) {
        System.out.println(signUpRequest.toString());
        LOGGER.debug("Registering user account with information: {}", signUpRequest);
        if(userRepository.existsByEmail(signUpRequest.getEmail())) {
//            throw new UserAlreadyExistException("There is an account with that email address: " + signUpRequest.getEmail());
            return new GenericResponse("There is an account with that email address: " + signUpRequest.getEmail());
        }
        if(userRepository.existsByUsername(signUpRequest.getUserName())) {
//            throw new UserAlreadyExistException("There is an account with that  username" + signUpRequest.getUserName());
            return new GenericResponse("There is an account with that  student code: " + signUpRequest.getUserName());
        }
        User registered = userService.registerNewUserAccount(signUpRequest);
        eventPublisher.publishEvent(new OnRegistrationCompleteEvent(getAppUrl(request), request.getLocale(), registered));
        return new GenericResponse("success");
    }

    @GetMapping("/resendRegistrationToken")
    public GenericResponse resendRegistrationToken(final HttpServletRequest request, @RequestParam("token") final String existingToken) {
        final VerificationToken newToken = userService.generateNewVerificationToken(existingToken);
        final User user = userService.getUser(newToken.getToken());
        mailSender.send(constructResendVerificationTokenEmail(getAppUrl(request), request.getLocale(), newToken, user));
        return new GenericResponse(messages.getMessage("message.resendToken", null, request.getLocale()));
    }

//    @PostMapping("/user/savePassword")
//    public GenericResponse savePassword(final Locale locale, @Valid PasswordDto passwordDto) {
//
////        final String result = securityUserService.validatePasswordResetToken(passwordDto.getToken());
////
////        if(result != null) {
////            return new GenericResponse(messages.getMessage("auth.message." + result, null, locale));
////        }
//
//        Optional<User> user = userService.getUserByPasswordResetToken(passwordDto.getToken());
//        if(user.isPresent()) {
//            userService.changeUserPassword(user.get(), passwordDto.getNewPassword());
//            return new GenericResponse(messages.getMessage("message.resetPasswordSuc", null, locale));
//        } else {
//            return new GenericResponse(messages.getMessage("auth.message.invalid", null, locale));
//        }
//    }

    // Change user password
    @PostMapping("/user/updatePassword")
    public GenericResponse changeUserPassword(final Locale locale, @Valid PasswordDto passwordDto) {
        final User user = userService.findUserByEmail(((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getEmail());
        if (!userService.checkIfValidOldPassword(user, passwordDto.getOldPassword())) {
            throw new ValidationException();
        }
        userService.changeUserPassword(user, passwordDto.getNewPassword());
        return new GenericResponse(messages.getMessage("message.updatePassword", null, locale));
    }

    @GetMapping("/registrationConfirm")
    public GenericResponse registrationConfirm(@Param("token") String token) {
        final String result = userService.validateVerificationToken(token);
        if (result.equals("valid")) {
            if(userService.verifyRegistration(token) == true) {
                return new GenericResponse("success");
            }
        }
        return new GenericResponse("failure");
    }


    @PostMapping("/resetPassword")
    public GenericResponse resetPassword(HttpServletRequest request,
                                         @RequestParam("email") String email) {
        User user = userService.findUserByEmail(email);

        if(user == null) {
            return new GenericResponse("Username not found");
        }
        String token = UUID.randomUUID().toString();
        userService.createPasswordResetTokenForUser(user, token);
        mailSender.send(constructResetTokenEmail(getAppUrl(request), request.getLocale(), token, user));
        return new GenericResponse(messages.getMessage("message.resetPasswordEmail", null, request.getLocale()));
    }

    @GetMapping("/changePassword")
    public GenericResponse changePassword(@RequestParam("token") String token) {
        String result = userSecurityService.validatePasswordResetToken(token);
        if(result != null) {
            return new GenericResponse("false");
        }
        else {
            return new GenericResponse("success");
        }
    }

    @PostMapping("/user/savePassword")
    public ResponseEntity<?> savePassword(@Valid @RequestBody PasswordDto passwordDto) {
        String result = userSecurityService.validatePasswordResetToken(passwordDto.getToken());
        System.out.println(result);
        if(result != null) {
            return ResponseEntity.ok("invalid Token");
        }
        Optional<User> user = userService.getUserByPasswordResetToken(passwordDto.getToken());

        if(user.isPresent()) {
            userService.changeUserPassword(user.get(), passwordDto.getNewPassword());
            return ResponseEntity.ok("Password updated successfully");
        }
        else {
            return ResponseEntity.ok("Password updated unsuccessfully");
        }

    }


    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getUsernameOrEmail(), loginRequest.getPassword()
                    )
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);

            String jwt = tokenProvider.generateToken(authentication);
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            User currentUser = userRepository.findByUsername(userDetails.getUsername()).get();

            return ResponseEntity.ok(new UserResponse(currentUser.getId(), jwt, userDetails.getUsername(), userDetails.getAuthorities()));
        } catch (Exception e){
            System.out.println(e);
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Lỗi đăng nhập");

    }


//    ---------------------------------------------------------------------
//    NON API
    private String getAppUrl(HttpServletRequest request) {
        return "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
    }

    private SimpleMailMessage constructEmail(String subject, String body, User user) {
        final SimpleMailMessage email = new SimpleMailMessage();
        email.setSubject(subject);
        email.setText(body);
        email.setTo(user.getEmail());
        email.setFrom(env.getProperty("support.email"));
        return email;
    }
    private SimpleMailMessage constructResendVerificationTokenEmail(final String contextPath, final Locale locale, final VerificationToken newToken, final User user) {
        final String confirmationUrl = contextPath + "/registrationConfirm.html?token=" + newToken.getToken();
        final String message = messages.getMessage("message.resendToken", null, locale);
        return constructEmail("Resend Registration Token", message + " \r\n" + confirmationUrl, user);
    }

    private SimpleMailMessage constructResetTokenEmail(final String contextPath, final Locale locale, final String token, final User user) {
        final String url = contextPath + "/api/auth/changePassword?token=" + token;
        final String message = messages.getMessage("message.resetPassword", null, locale);
        return constructEmail("Reset Password", message + " \r\n" + url, user);
    }

}
