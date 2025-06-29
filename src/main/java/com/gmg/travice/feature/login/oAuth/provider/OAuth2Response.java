package com.gmg.travice.feature.login.oAuth.provider;

public interface OAuth2Response {
    // 제공자 이름 (naver, google, kakao)
    String getProvider();
    // 각 사용자의 번호(id)
    String getProviderId();
    // 사용자의 이메일
    String getEmail();
    // 사용자의 이름
    String getName();
}
