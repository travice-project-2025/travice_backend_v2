package com.gmg.travice.feature.login.service;

import com.gmg.travice.domain.user.dto.UserDTO;
import com.gmg.travice.domain.user.entity.LoginType;
import com.gmg.travice.domain.user.entity.User;
import com.gmg.travice.domain.user.repository.UserRepository;
import com.gmg.travice.feature.login.oAuth.provider.GoogleResponse;
import com.gmg.travice.feature.login.oAuth.provider.KakaoResponse;
import com.gmg.travice.feature.login.oAuth.provider.NaverResponse;
import com.gmg.travice.feature.login.oAuth.provider.OAuth2Response;
import com.gmg.travice.feature.login.oAuth.user.*;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    // DB 접근
    private final UserRepository userRepository;

    public CustomOAuth2UserService(UserRepository userRepository) {

        this.userRepository = userRepository;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        System.out.println("CustomOAuth2UserService Start");


        OAuth2User oAuth2User = super.loadUser(userRequest);

        System.out.println(oAuth2User.getAttributes());
        // 로그인한 서비스 종류 롹인
        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        // 객체 선언
        OAuth2Response oAuth2Response = null;
        if (registrationId.equals("naver")) {

            oAuth2Response = new NaverResponse(oAuth2User.getAttributes());
        }
        else if (registrationId.equals("google")) {

            oAuth2Response = new GoogleResponse(oAuth2User.getAttributes());
        } else if (registrationId.equals("kakao")) {
            oAuth2Response = new KakaoResponse(oAuth2User.getAttributes());
        } else {
            throw new OAuth2AuthenticationException("Invalid registration id " + registrationId);
        }

        String loginType = oAuth2Response.getProvider();
        String token = oAuth2Response.getProviderId();

        User existData = userRepository.findByToken(token);

        if (existData == null) {

            User user = new User();
            user.setEmail(oAuth2Response.getEmail());
            user.setLoginType(LoginType.valueOf(oAuth2Response.getProvider()));
            user.setToken(oAuth2Response.getProviderId());
            user.setName(oAuth2Response.getName());
            user.setCreatedAt(LocalDateTime.now());
            userRepository.save(user);

            UserDTO userDTO = new UserDTO();
            userDTO.setName(oAuth2Response.getName());
            userDTO.setToken(token);
            userDTO.setEmail(oAuth2Response.getEmail());

            return new CustomOAuth2User(userDTO);
        }
        else {
            UserDTO userDTO = new UserDTO();
            userDTO.setName(existData.getName());
            userDTO.setToken(token);
            userDTO.setEmail(existData.getEmail());

            return new CustomOAuth2User(userDTO);

        }
    }
}
