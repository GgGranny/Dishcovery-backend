package com.dishcovery.backend.service;


import ch.qos.logback.core.testUtil.RandomUtil;
import com.dishcovery.backend.components.RecipeComponent;
import com.dishcovery.backend.dto.LoginDto;
import com.dishcovery.backend.dto.UserBioDto;
import com.dishcovery.backend.dto.UserDto;
import com.dishcovery.backend.dto.UserResponseDto;
import com.dishcovery.backend.model.Recipe;
import com.dishcovery.backend.model.RefreshToken;
import com.dishcovery.backend.model.Token;
import com.dishcovery.backend.model.Users;
import com.dishcovery.backend.repo.RefreshTokenRepo;
import com.dishcovery.backend.repo.TokenRepo;
import com.dishcovery.backend.repo.UserRepo;
import com.dishcovery.backend.response.MyResponseHandler;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserServicesImp {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private MailService mailService;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private JWTService jwtService;

    @Autowired
    private RefreshTokenImpl refreshTokenService;

    @Autowired
    private SubscriptionImple subscriptionImple;

    private static final String DEFAULT_PROFILE_IMAGE = "https://cdn-icons-png.flaticon.com/512/3135/3135715.png";
    private TokenRepo tokenRepo;

    @Autowired
    private RefreshTokenRepo refreshTokenRepo;

    @Autowired
    private RecipeComponent recipeComponent;

    @Value("${file.user.profile}")
    private String profileFolder;
    @PostConstruct
    public void init() throws IOException {
        File file = new File(profileFolder);
        if(!file.exists()) {
            Files.createDirectory(Paths.get(profileFolder));
        }
    }

    public Map<String, String> registerUser(Users user) {
        Map<String, String>  message = new HashMap<>();
        if(!usernameExists(user.getUsername())) {
            message.put("message", "username alredy taken");
            message.put("status", "400");
            return message;
        }

        if(!emailExists(user.getEmail())) {
            message.put("message", "Email alredy taken");
            message.put("status", "400");
            return message;
        }
        user.setEnabled(false);
        user.setProfilePicture(DEFAULT_PROFILE_IMAGE);
        user.setRole("USER");
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        userRepo.save(user);
        message.put("message", "Please confirm your email");
        message.put("status", "200");

        //Save the token
        Token token = new Token(user,tokenService.generateToken(), LocalDateTime.now().plusMinutes(2), LocalDateTime.now());
        System.out.println("token:" + token);
        tokenService.saveToken(token);

        //send mail
        mailService.sendMail(user.getEmail(), token.getToken());
        return message;
    }

    //check if username already exists
    private boolean usernameExists(String username) {
        Users userPresent = userRepo.findByUsername(username);
        if(userPresent == null) {
            return true;
        }
        return false;
    }

    //check if email already exists
    private boolean emailExists(String email) {
        Users userPresent = userRepo.findByEmail(email);
        if(userPresent == null) {
            return true;
        }
        return false;
    }

    public  Map<String, String> verify(LoginDto userDto) {
        Map<String, String> response = new HashMap<>();
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(userDto.getUsername(), userDto.getPassword())
            );
            if (authentication.isAuthenticated()) {
                String token = jwtService.generateToken(userDto.getUsername());
                Users u = userRepo.findByUsername(userDto.getUsername());

                // Subscribe as free user
                if(!subscriptionImple.isUserSubscribed(u.getId())) {
                    subscriptionImple.subscribeAsFreeUser(u.getUsername());
                }
                RefreshToken oldRefreshToken = refreshTokenRepo.findByUser(u);
                if(oldRefreshToken != null ) {
                    response.put("refreshToken", oldRefreshToken.getToken());
                    response.put("token", token);
                    response.put("user_id", String.valueOf(u.getId()));
                    return response;
                }
                String refreshToken = refreshTokenService.createRefreshToken(u.getId()).getToken();
                response.put("token", token);
                response.put("refreshToken", refreshToken);
                return response;
            }

        }catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }


    public ResponseEntity<Object> verifyEmailAndSendOTP(String email) {
        try {
            Users user = userRepo.findByEmail(email);
            if(user == null) {
                throw new IllegalArgumentException("No such user found");
            }

            //Generate OTP token and save it
            Token token = new Token(user,tokenService.generateToken(), LocalDateTime.now().plusMinutes(2), LocalDateTime.now());
            tokenService.saveToken(token);

            // send otp to recover change password
            Map<String, String> msg =new HashMap<>();
            msg.put("expires_at", "2min");
            mailService.sendMail(user.getEmail(), token.getToken());
            return MyResponseHandler.responseBuilder(HttpStatus.OK, "An OTP is sent to your Email", msg);
        }catch(Exception e) {
            throw new IllegalArgumentException("Please verify your email", e);
        }
    }

    public boolean updateUserPassword(String token, String newPassword) {

        Map<String, Object> response = new HashMap<>();

        //check if the token exists
        Token tokenOpt = tokenService.getTokenByToken(token)
                .orElseThrow(() -> new IllegalArgumentException("Token not found"));

        // check if the token is expired
        LocalDateTime expireTime = tokenOpt.getExpiresAt();
        if(expireTime.isBefore(LocalDateTime.now())){
            throw new IllegalArgumentException("Token is expired");
        }

        //check if token is confirmed and if not confirm it
        if(tokenOpt.getConfirmAt() != null) {
            throw new IllegalArgumentException("Token is already confirmed");
        }

        // update users new password
        Users user = tokenOpt.getUser();
        user.setPassword(bCryptPasswordEncoder.encode(newPassword));
        userRepo.save(user);

        response.put("status", HttpStatus.OK);
        return true;
    }

    public String uploadUserProfile(MultipartFile profile, int userId) throws IOException{
        if(profile == null ) {
            throw new NullPointerException("profile is null");
        }
        if(!profile.getContentType().startsWith("image/")) {
            return null;
        }
        String randomUUID = UUID.randomUUID().toString();
        String fileName = profile.getOriginalFilename().trim();
        fileName = randomUUID.concat(profile.getOriginalFilename());
        Path path = Paths.get(profileFolder,fileName);

        Users u = userRepo.findById(userId)
                .orElseThrow(()-> new RuntimeException("user not found for id: "+userId));

        //delete the exisisting path
        if (u.getProfilePicture() != null && !u.getProfilePicture().startsWith("http")) {
            Path pathToDelete = Paths.get(u.getProfilePicture());
            if (Files.exists(pathToDelete)) {
                Files.delete(pathToDelete);
            }
        }

        byte[] bytes = profile.getBytes();
        InputStream inputStream = profile.getInputStream();
        Files.copy(inputStream,path, StandardCopyOption.REPLACE_EXISTING);
        String url = path.toString();
        u.setProfilePicture(url);
        userRepo.save(u);
        return url;
    }

    public String fetchProfile(int id) throws IOException {
        Users user = userRepo.findById(id)
                .orElseThrow(()-> new RuntimeException("no such user found"));
        String userProfile = user.getProfilePicture();
        if(userProfile.startsWith("https")) return userProfile;
        Path path = Paths.get(user.getProfilePicture());
        Resource resource = new FileSystemResource(path);
        byte[] bytes = resource.getInputStream().readAllBytes();
        String profile = Base64.getEncoder().encodeToString(bytes);
        return profile;
    }


    public UserResponseDto fetchUser(int userId) throws IOException {
        Users user = userRepo.findById(userId)
                .orElseThrow(()-> new RuntimeException("User not found for this id"));
        String profile = fetchProfile(userId);
        int recipeCount = recipeComponent.countRecipe(userId);
        UserResponseDto response = new UserResponseDto(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                profile,
                user.getRole(),
                user.getEnabled(),
                user.getDisplayName(),
                user.getBio(),
                recipeCount,
                user.getFollowersCount() != null ? user.getFollowersCount(): 0,
                user.getCuisinePreferences()
        );
        return response;
    }

    public Users updateUserBio(UserBioDto userBioDto, int userId) {

        Users user = userRepo.findById(userId)
                .orElseThrow(() -> new RuntimeException("No such user found"));

        user.setBio(userBioDto.getBio());
        user.setDisplayName(userBioDto.getDisplayName());

        // Safe cuisine parsing
        if (userBioDto.getCuisinePreferences() != null) {
            List<String> preferredCuisines = Arrays.stream(
                            userBioDto.getCuisinePreferences().split(","))
                    .map(String::trim)
                    .filter(s -> !s.isEmpty())
                    .collect(Collectors.toList());

            user.setCuisinePreferences(preferredCuisines);
        }

        return userRepo.save(user);
    }

}

