package com.cons.reporteya.service;

import com.cons.reporteya.entity.User;
import com.cons.reporteya.entity.VerificationToken;
import com.cons.reporteya.repository.UserRepository;
import com.cons.reporteya.repository.VerificationTokenRepository;
import com.cons.reporteya.validator.UserValidator;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;

import java.util.Objects;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final VerificationTokenRepository verificationTokenRepository;
    private final UserValidator userValidator;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository,
                       VerificationTokenRepository verificationTokenRepository,
                       UserValidator userValidator,
                       PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.verificationTokenRepository = verificationTokenRepository;
        this.userValidator = userValidator;
        this.passwordEncoder = passwordEncoder;
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }

    public User save(User user) {
        return userRepository.save(user);
    }

    /**
     * Sets an encrypted password to the user and saves it.
     * @param user
     * @return
     */
    public User register(User user){
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return save(user);
    }

    /**
     * Checks if all the fields of a user exist.
     * @param user
     * @return
     */
    public boolean checkCredentialsExistance(User user){
        return Objects.nonNull(user) &
                Objects.nonNull(user.getEmail()) &
                Objects.nonNull(user.getDate_of_birth()) &
                Objects.nonNull(user.getPassword()) &
                Objects.nonNull(user.getPasswordConfirmation());
    }

    /**
     * Checks if a user can register or not.
     * It will add errors if it can't.
     * @param user
     * @param errors
     */
    public void checkCredentialsRegistration(User user,
                                             Errors errors){
        if (!checkCredentialsExistance(user)) return;

        if (Objects.nonNull(findByEmail(user.getEmail())))
            errors.rejectValue(
                    "email",
                    "Match"
            );

        userValidator.validate(user, errors);
    }

    public User findUserByToken(String token){
        return verificationTokenRepository.findByToken(token).getUser();
    }

    public VerificationToken getVerificationToken(String token) {
        return verificationTokenRepository.findByToken(token);
    }

    public void createVerificationToken(User user, String token) {
        VerificationToken myToken = new VerificationToken(
                null,
                token,
                user,
                VerificationToken.calculateExpiryDate(60 * 24)
        );
        verificationTokenRepository.save(myToken);
    }

    public boolean canCreateAReport(User user){
        return user.getReports().size() < 3;
    }
}
