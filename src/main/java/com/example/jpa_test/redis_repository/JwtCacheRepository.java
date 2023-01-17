package com.example.jpa_test.redis_repository;

import java.time.Duration;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
@RequiredArgsConstructor
public class JwtCacheRepository {
    //레디스에서 제공하는 기능들을 간편하게 호출할 수 있게 만들어주는 클래스다.

    private final RedisTemplate<String, String> template;
    //임시로 1주일이라고 하자.
    private static final Duration JWT_DURATION = Duration.ofDays(7);

    public void setJwt(String jwt, Long userPKId){
        String key = getKey(userPKId);
        template.opsForValue().set(key, jwt, JWT_DURATION);
        log.info("레디스에 유저 jwt 셋팅 완료.");
    }

    public String getJwt(Long userPKId){
        String key = getKey(userPKId);
        String jwt = template.opsForValue().get(key);
        log.info("레디스로부터 유저 키 가져오기 완료.");
        return jwt;
    }




    private String getKey(Long userPKId){
        return "USER:JWT:" + userPKId;
    }

}