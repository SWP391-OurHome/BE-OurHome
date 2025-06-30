package com.javaweb.service.impl;

import com.javaweb.model.GoogleLoginResponse;
import com.javaweb.model.GoogleUserInfo;
import com.javaweb.repository.entity.OTPEntity;
import com.javaweb.repository.entity.RoleEntity;
import com.javaweb.repository.entity.UserEntity;
import com.javaweb.repository.impl.OTPRepositoryImpl;
import com.javaweb.repository.impl.UserRepositoryImpl;
import com.javaweb.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;


import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

import static com.mysql.cj.conf.PropertyKey.logger;

@Service
public class GoogleAuthService {
    @Value("${google.client.id}")
    private String clientId;

    @Value("${google.client.secret}")
    private String clientSecret;

    @Value("${google.redirect.uri}")
    private String redirectUri;

    @Autowired
    private UserRepositoryImpl userRepo;

    @Autowired
    private OTPRepositoryImpl OTPRepo;

    @Autowired
    private EmailService emailService;

    @Autowired
    private JwtUtil jwtUtil;

    private String getAccessToken(String code) {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("code", code);
        map.add("client_id", clientId);
        map.add("client_secret", clientSecret);
        map.add("redirect_uri", redirectUri);
        map.add("grant_type", "authorization_code");

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);

        ResponseEntity<Map> response = restTemplate.postForEntity("https://oauth2.googleapis.com/token", request, Map.class);
        return (String) response.getBody().get("access_token");
    }

    private GoogleUserInfo getUserInfo(String accessToken) {
        String userInfoEndpoint = "https://www.googleapis.com/oauth2/v1/userinfo?alt=json&access_token=" + accessToken;
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.getForObject(userInfoEndpoint, GoogleUserInfo.class);
    }


    public GoogleUserInfo authenticateWithGoogle(String code) {
        String accessToken = getAccessToken(code);
        return getUserInfo(accessToken);
    }

    public GoogleLoginResponse findOrCreateGoogleUser(GoogleUserInfo userInfo) {
        Optional<UserEntity> userOpt = userRepo.findByEmail(userInfo.getEmail());

        UserEntity user;
        if (userOpt.isPresent()) {
            user = userOpt.get();
        } else {
            user = new UserEntity();
            user.setEmail(userInfo.getEmail());
            user.setFirstName(userInfo.getFirstName());
            user.setLastName(userInfo.getLastName());

            RoleEntity defaultRole = new RoleEntity();
            defaultRole.setRoleId(2); // Hoặc lấy từ roleRepo nếu cần
            user.setRole(defaultRole);

            userRepo.save(user);
        }

        // ✅ Tạo JWT token (giả sử method JwtUtil.generateToken(email, role) đã tồn tại)
        String token = jwtUtil.generateToken(user.getEmail(), user.getRole().getRoleName());

        GoogleLoginResponse response = new GoogleLoginResponse();
        response.setUserId(user.getUserId());
        response.setSuccess(true);
        response.setFirstName(user.getFirstName());
        response.setLastName(user.getLastName());
        response.setPicture(user.getImgPath());
        response.setEmail(user.getEmail());
        response.setRole(user.getRole().getRoleName());
        response.setBirthday(user.getBirthday());
        response.setToken(token);

        return response;
    }

    @Transactional
    public boolean sendOtpToEmail(String email) {
        if (userRepo.existsByEmail(email)) {
            // 2. Tạo mã OTP 6 chữ số
            String otp = String.format("%06d", new Random().nextInt(1_000_000));

            // 3. Xoá mã OTP cũ (nếu có)
            OTPRepo.deleteByEmail(email);

            // 4. Lưu OTP mới
            OTPEntity otpCode = new OTPEntity();
            otpCode.setEmail(email);
            otpCode.setOtpCode(otp);
            otpCode.setExpiredAt(LocalDateTime.now().plusMinutes(60));
            OTPRepo.save(otpCode);

            // 5. Gửi email OTP
            String subject = "Your Account Verification Code";
            String body = String.format("Your OTP code is: %s", otp);
            System.out.println("Sending OTP to email: " + email + " | OTP: " + otp);
            emailService.sendEmail(email, subject, body);
            return true;
        }else{
            return false;
        }
    }

    @Transactional
    public boolean verifyOtp(String email, String otp) {
        Optional<OTPEntity> latestOtp = OTPRepo.findLatestOtpByEmail(email);
        if(!latestOtp.isPresent()){
            return false;
        }
        OTPEntity otpEntity  = latestOtp.get();
        return otpEntity.getOtpCode().equals(otp);
    }
}
