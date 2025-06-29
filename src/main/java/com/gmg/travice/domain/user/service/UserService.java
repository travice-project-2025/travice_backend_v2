package com.gmg.travice.domain.user.service;

import com.gmg.travice.domain.user.entity.User;
import jakarta.servlet.http.HttpServletRequest;

public interface UserService {
    User findUserByJWTToken(HttpServletRequest request);
    User updateUser(User user);
    User findById(Long id);
}
