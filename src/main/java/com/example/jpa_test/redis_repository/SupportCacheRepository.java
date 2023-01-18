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
    private final RedisTemplate<String, Long> template;

    //임시로 3일이라 하자.
    private static final Duration SUPPORT_NUMBER_DURATION = Duration.ofDays(3);
    private static final Duration NANO_DURATION = Duration.ofNanos(1L);


    //응원 숫자를 레디스로부터 가져온다.
    public Long getSupportNumber(Long publicTodoPKId){
        String key = getKey(publicTodoPKId);
        Long value = template.opsForValue().get(key);
        log.info("레디스로부터 응원 숫자 가져오기 완료.");
        return value;
    }


    //최초의 응원에 대하여 캐싱한다.
    public void setInitialSupport(Long publicTodoPKId){
        String key = getKey(publicTodoPKId);
        template.opsForValue().set(key, 1L, SUPPORT_NUMBER_DURATION);
        log.info("레디스에 응원숫자 최초 셋팅 완료.");
    }

    //응원 숫자를 += 1 한다.
    public void plusOneSupport(Long publicTodoPKId){
        String key = getKey(publicTodoPKId);

        Long prevNumber = template.opsForValue().getAndExpire(key, NANO_DURATION);

        prevNumber++;

        log.info("응원 추가 후 응원 숫자 : " + prevNumber);

        template.opsForValue().set(key, prevNumber, SUPPORT_NUMBER_DURATION);
        log.info("레디스에 응원숫자 +=1 누적 플러스 완료.");
    }

    //응원 숫자를 -= 1 한다.
    public void minusOneSupport(Long publicTodoPKId){
        String key = getKey(publicTodoPKId);

        Long prevNumber = template.opsForValue().getAndExpire(key, NANO_DURATION);

        prevNumber--;

        log.info("응원 취소 후 응원 숫자 : " + prevNumber);

        template.opsForValue().set(key, prevNumber, SUPPORT_NUMBER_DURATION);
        log.info("레디스에 응원숫자 -=1 누적 마이너스 완료.");
    }




    private String getKey(Long publicTodoPKId){
        return "TODO:SUPPORT:" + publicTodoPKId;
    }
}
