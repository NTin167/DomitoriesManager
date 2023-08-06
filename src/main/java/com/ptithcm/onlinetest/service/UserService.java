package com.ptithcm.onlinetest.service;

import com.ptithcm.onlinetest.exception.AppException;
import com.ptithcm.onlinetest.model.*;
import com.ptithcm.onlinetest.payload.request.SignUpRequest;
import com.ptithcm.onlinetest.repository.PasswordResetTokenRepository;
import com.ptithcm.onlinetest.repository.RoleRepository;
import com.ptithcm.onlinetest.repository.UserRepository;
import com.ptithcm.onlinetest.repository.VerificationTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class UserService implements IUserService{

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    VerificationTokenRepository tokenRepository;

    @Autowired
    PasswordResetTokenRepository resetTokenRepository;

    public static final String TOKEN_INVALID = "invalidToken";
    public static final String TOKEN_EXPIRED = "expired";
    public static final String TOKEN_VALID = "valid";

    @Override
    public User registerNewUserAccount(SignUpRequest signUpRequest) {
        User user = new User();
        user.setUsername(signUpRequest.getUserName());
        user.setFirstName(signUpRequest.getFirstName());
        user.setLastName(signUpRequest.getLastName());
        user.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));
        user.setEmail(signUpRequest.getEmail());
        Role userRole = roleRepository.findByName(RoleName.ROLE_USER).orElseThrow(() -> new AppException("User Role not set."));
        user.setRoles(Collections.singleton(userRole));
        return userRepository.save(user);
    }

    @Override
    public User getUser(String verificationToken) {
        VerificationToken token = tokenRepository.findByToken(verificationToken);
        if(token != null) {
            return token.getUser();
        }
        return null;
    }

    @Override
    public void saveRegisteredUser(User user) {
        userRepository.save(user);
    }

    @Override
    public void deleteUser(User user) {
        final VerificationToken verificationToken = tokenRepository.findByUser(user);

        if(verificationToken != null) {
            tokenRepository.delete(verificationToken);
        }

        final PasswordResetToken passwordResetToken = resetTokenRepository.findByUser(user);

        if(resetTokenRepository != null) {
            resetTokenRepository.delete(passwordResetToken);
        }

        userRepository.delete(user);
    }

    @Override
    public void createVerificationTokenForUser(User user, String token) {
        final VerificationToken myToken = new VerificationToken(token, user);
        tokenRepository.save(myToken);
    }

    @Override
    public VerificationToken getVerificationToken(String VerificationToken) {
        return tokenRepository.findByToken(VerificationToken);
    }

    @Override
    public VerificationToken generateNewVerificationToken(String token) {
        VerificationToken verificationToken = tokenRepository.findByToken(token);
        verificationToken.updateToken(UUID.randomUUID().toString());
        verificationToken = tokenRepository.save(verificationToken);
        return verificationToken;
    }

    @Override
    public void createPasswordResetTokenForUser(User user, String token) {
        final PasswordResetToken passwordResetToken = new PasswordResetToken(token, user);
        resetTokenRepository.save(passwordResetToken);
    }

    @Override
    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public PasswordResetToken getPasswordResetToken(String token) {
        return resetTokenRepository.findByToken(token);
    }

    @Override
    public Optional<User> getUserByPasswordResetToken(String token) {
        return Optional.ofNullable(resetTokenRepository.findByToken(token).getUser());
    }

    @Override
    public Optional<User> getUserByID(long id) {
        return userRepository.findById(id);
    }

    @Override
    public void changeUserPassword(User user, String password) {
        user.setPassword(passwordEncoder.encode(password));
        userRepository.save(user);
    }

    @Override
    public boolean checkIfValidOldPassword(User user, String oldPassword) {
        return passwordEncoder.matches(oldPassword, user.getPassword());
    }

    @Override
    public String validateVerificationToken(String token) {
        VerificationToken verificationToken = tokenRepository.findByToken(token);
        if(verificationToken == null) {
            return TOKEN_INVALID;
        }
        final User user = verificationToken.getUser();

        final Calendar calendar = Calendar.getInstance();
        if(verificationToken.getExpiryDate().getTime() - calendar.getTime().getTime() <= 0) {
            tokenRepository.delete(verificationToken);
            return TOKEN_EXPIRED;
        }

        user.setEnabled(false);
        userRepository.save(user);
        return TOKEN_VALID;
    }

    @Override
    public boolean verifyRegistration(String verifyCode) {
        if(tokenRepository.findByToken(verifyCode)!= null) {
            System.out.println("access");
        }
        User user = tokenRepository.findByToken(verifyCode).getUser();
        if(user.getEnable() == false)
        {
            user.setEnabled(true);
            userRepository.save(user);
            return true;
        }
        return false;
    }
}
