package com.gmg.travice.domain.user.service;

import com.gmg.travice.domain.user.entity.User;
import com.gmg.travice.domain.user.repository.UserRepository;
import com.gmg.travice.feature.login.jwt.JWTUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final JWTUtil jwtUtil;

    @Override
    public User findUserByJWTToken(HttpServletRequest request) {
        String JWTtoken = null;
        User user = null;
        Cookie[] cookies = request.getCookies();

        if(cookies != null) {
            for(Cookie cookie : cookies) {
                if(cookie.getName().equals("JWT-TOKEN")) {
                    JWTtoken = cookie.getValue();
                    user = userRepository.findByToken(jwtUtil.getJWTToken(JWTtoken));
                    return user;
                }
            }
        }

        return null;
    }

    @Override
    public User updateUser(User user) {
        if(user == null){
            throw new IllegalArgumentException("user is null");
        }

        if(user.getId() == null){
            throw new IllegalArgumentException("user id is null");
        }

        return userRepository.save(user);
    }

    @Override
    public User findById(Long id) {
        return userRepository.findById(id).orElse(null);
    }
}
