package com.ewd.service;

import com.ewd.exception.UserAlreadyExist;
import com.ewd.repository.SignUpEmailRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.ewd.Constants.ConfigurationConstant;
import com.ewd.dto.LoginRequest;
import com.ewd.dto.TokenResponse;
import com.ewd.entity.User;
import com.ewd.repository.UserRepository;
import com.ewd.utils.JwtUtils;
import lombok.extern.slf4j.Slf4j;

import java.util.UUID;
import java.util.List;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpServletRequest;

@Service
@Slf4j
public class AuthService implements UserDetailsService {


//    private final JwtUtils jwtUtils;
//
//    private final UserRepository userRepository;
//
//    private final PasswordEncoder bcryptPasswordEncoder;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder bcryptPasswordEncoder;

    @Autowired
    private SignUpEmailRepo signUpEmailRepo;

    @Autowired
    private JavaMailSender mailSender;

    public AuthService(JwtUtils jwtUtils, UserRepository userRepository, PasswordEncoder bcryptPasswordEncoder) {
        this.jwtUtils = jwtUtils;
        this.userRepository = userRepository;
        this.bcryptPasswordEncoder = bcryptPasswordEncoder;
    }


//    public boolean validateToken(String jwtToken){
//        log.info("Entered into validateToken method");
//        try {
//
//            if (jwtToken != null && jwtToken.startsWith("Bearer ")) {
//                String token = jwtToken.substring(7);
//                if (token != null && !(jwtUtils.istokenExpired(token))) {
//                    userDetailsService.loadUserByUsername(jwtUtils.extractName(token));
//                    log.info("Token validate successfully");
//                    return true;
//                }
//            }
//        }
//
//        catch(SignatureException e){
//            log.error("Error with jwt signature");
//            throw e;
//        }
//        catch(InsufficientAuthenticationException e){
//            log.error("Authentication Fail");
//            throw e;
//        }
//        catch (Exception e){
//            log.error("Exception occur");
//            throw e;
//        }
//        log.info("Token invalid");
//        log.info("Existing from validateToken method");
//        return false;
//    }

    public TokenResponse login(LoginRequest user) {
        log.info("Entering into login method");

        // Fetch user from the database
        User verifiedUser = userRepository.findByUserName(user.getUserName())
                .orElseThrow(() -> new UsernameNotFoundException(ConfigurationConstant.USER_NOT_FOUND));

        // Check if user is activated
        if (!"activated".equalsIgnoreCase(verifiedUser.getStatus())) {
            log.warn("User is not activated: {}", verifiedUser.getUsername());
            throw new BadCredentialsException("User is not activated");
        }

        // Validate password
        if (!bcryptPasswordEncoder.matches(user.getPassword(), verifiedUser.getPassword())) {
            log.error("BAD CREDENTIALS - Invalid Password");
            throw new BadCredentialsException("Invalid Password");
        }

        // Generate JWT Token
        String token = jwtUtils.genrateToken(verifiedUser, false);

        log.info("Exiting from login method");
        return new TokenResponse(token);
    }



    public String registerUser(User user, HttpServletRequest request){
        log.info("Entered into registerUser method");
        try {
            if (userRepository.existsByUserName(user.getUsername())) {
                log.error("Error: User already exist");
                throw new UserAlreadyExist("This Username already registered");
            }
            user.setPassword(bcryptPasswordEncoder.encode(user.getPassword()));
            user.setStatus("pending");
            user.setActivationToken(UUID.randomUUID().toString());

            userRepository.save(user);

            sendActivationEmailToManager(user, request);

            log.info("User details saved successfully");
            log.info("Existing from registerUser method");
            return ConfigurationConstant.USER_SAVED;
        }catch (UserAlreadyExist e){
            log.warn("Exception handing inside service");
            throw e;
        }catch (Exception e){
            throw e;
        }
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return this.userRepository.findByUserName(username).get();
    }

    public void sendActivationEmailToManager(User user, HttpServletRequest request) {
        List<String> managerEmail = signUpEmailRepo.findSignUpEmail();
        log.info("managerEmail:   {} ",managerEmail);
        if (managerEmail == null || managerEmail.isEmpty()) {

            log.warn("Manager email not configured, skipping manager notification");
            return;
        }

        String subject = "New User Activation Pending";
        
     // Build dynamic base URL
        String baseUrl = request.getScheme() + "://" + request.getServerName() + 
                         ":" + request.getServerPort() + request.getContextPath();
        log.info("baseUrl:   {} ",baseUrl);

        // Replace with your actual server URL
//        String baseUrl = "http://localhost:8080/eidiko-workorder-integration-service/api/auth/activate";
        
//        String acceptLink = baseUrl + "?token=" + user.getActivationToken() + "&action=accept";
//        String rejectLink = baseUrl + "?token=" + user.getActivationToken() + "&action=reject";
        
        String acceptLink = baseUrl + "/api/auth/activate?token=" + user.getActivationToken() + "&action=accept";
        String rejectLink = baseUrl + "/api/auth/activate?token=" + user.getActivationToken() + "&action=reject";

        log.info("acceptLink:   {} ",acceptLink);
        String body = "<p>A new user has registered and requires activation.</p>" +
                "<p><strong>User:</strong> " + user.getUsername() + "</p>" +
                "<p><strong>Email:</strong> " + user.getEmail() + "</p>" +
                "<p>Please review and take action:</p>" +
                "<p>" +
                "<a href='" + acceptLink + "' style='padding: 10px 20px; background-color: green; color: white; text-decoration: none; margin-right: 10px;'>Accept</a>" +
                "<a href='" + rejectLink + "' style='padding: 10px 20px; background-color: red; color: white; text-decoration: none;'>Reject</a>" +
                "</p>";

        for (String email : managerEmail) {
        	log.info("managerEmail1:   {} ",managerEmail);
            try {
                MimeMessage message = mailSender.createMimeMessage();
                MimeMessageHelper helper = new MimeMessageHelper(message, true);
                helper.setTo(email);
                helper.setSubject(subject);
                helper.setText(body, true);
                mailSender.send(message);
                log.info("Activation email sent to manager: {}", email);
            } catch (MessagingException e) {
                log.error("Failed to send activation email to manager {}: {}", email, e.getMessage());
            }
        }
    }


    public String processActivation(String token, String action) {
        log.info("Entered into processActivation method token: {}, action: {}", token, action);

        User user = userRepository.findByActivationToken(token)
                .orElseThrow(() -> new UsernameNotFoundException("Invalid activation token"));

        if ("accept".equalsIgnoreCase(action)) {
            user.setStatus("activated");
            user.setActivationToken(null); // Invalidate token
            userRepository.save(user);
            return "User activated successfully";
        } else if ("reject".equalsIgnoreCase(action)) {
            userRepository.delete(user);
            return "User registration rejected and deleted";
        } else {
            throw new IllegalArgumentException("Invalid action");
        }
    }
}
