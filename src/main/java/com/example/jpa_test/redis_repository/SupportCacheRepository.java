package com.example.jpa_test.redis_repository;

import java.time.Duration;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
@RequiredArgsConstructor
public class SupportCacheRepository {
    private final RedisTemplate<String, String> template;

    //임시로 3일이라 하자.
    private static final Duration SUPPORT_NUMBER_DURATION = Duration.ofDays(3);


    //응원 숫자를 레디스로부터 가져온다.
    public Long getSupportNumber(Long publicTodoPKId){
        String key = getKey(publicTodoPKId);
        String value = template.opsForValue().get(key);
        log.info("레디스로부터 응원 숫자 가져오기 완료.");
        return Long.parseLong(value);
    }


    //최초의 응원에 대하여 캐싱한다.
    public void setInitialSupport(Long publicTodoPKId){
        String key = getKey(publicTodoPKId);
        template.opsForValue().set(key, "1", SUPPORT_NUMBER_DURATION);
        log.info("레디스에 응원숫자 최초 셋팅 완료.");
    }

    //응원 숫자를 += 1 한다.
    public void plusOneSupport(Long publicTodoPKId){
        log.info("응원 추가 진입");

        String key = getKey(publicTodoPKId);

        String prevNumber = template.opsForValue().get(key);

        System.out.println("응원 += 1 직전 숫자 : " + prevNumber);

        template.opsForValue().increment(key);

        log.info("응원 추가 후 응원 숫자 : " + template.opsForValue().get(key));

        log.info("레디스에 응원숫자 +=1 누적 플러스 완료.");
    }

    //응원 숫자를 -= 1 한다.
    public void minusOneSupport(Long publicTodoPKId){
        log.info("응원 감소 진입");

        String key = getKey(publicTodoPKId);

        String prevNumber = template.opsForValue().get(key);

        System.out.println("응원 += 1 직전 숫자 : " + prevNumber);

        template.opsForValue().decrement(key);

        log.info("응원 감소 후 응원 숫자 : " + template.opsForValue().get(key));

        log.info("레디스에 응원숫자 -=1 누적 플러스 완료.");
    }




    private String getKey(Long publicTodoPKId){
        return "TODO:SUPPORT:" + publicTodoPKId;
    }
}
