package com.example.jpa_test.redis_repository;

import java.time.Duration;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
@RequiredArgsConstructor
public class NagCacheRepository {

    private final RedisTemplate<String, Object> template;

    //이 부분이 문제다. 레디스 캐시에만 잔소리/서포트를 기록하면 서비스가 운영될수록
    //캐시서버에 기록이 계속 누적되는 것이다.
    //때문에 누적된 캐시기록들을 1일 단위로 매일 새벽 2~6시마다 어제의 캐시 기록들을 DB에 영구저장하는 것이 필요할 것이다.
    private static final Duration SUPPORT_NUMBER_DURATION = Duration.ofDays(3);


    //잔소리 숫자를 레디스로부터 가져온다.
    public Long getSupportNumber(Long publicTodoPKId){
        String key = getKey(publicTodoPKId);
        Long value = (Long) template.opsForValue().get(key);
        log.info("레디스로부터 잔소리 숫자 가져오기 완료");
        return value;
    }


    //최초의 잔소리에 대하여 캐싱한다.
    public void setInitialNag(Long publicTodoPKId){
        String key = getKey(publicTodoPKId);
        template.opsForValue().set(key, 1L, SUPPORT_NUMBER_DURATION);
        log.info("레디스에 잔소리숫자 최초 셋팅 완료.");
    }

    //잔소리 숫자를 += 1 한다.
    public void plusOneNag(Long publicTodoPKId){
        String key = getKey(publicTodoPKId);

        Long prevNumber = (Long) template.opsForValue().get(key);

        prevNumber++;

        template.opsForValue().set(key, prevNumber, SUPPORT_NUMBER_DURATION);
        log.info("레디스에 잔소리숫자 +=1 누적 플러스 완료.");
    }

    //잔소리 숫자를 -= 1 한다.
    public void minusOneNag(Long publicTodoPKId){
        String key = getKey(publicTodoPKId);

        Long prevNumber = (Long) template.opsForValue().get(key);

        prevNumber--;

        template.opsForValue().set(key, prevNumber, SUPPORT_NUMBER_DURATION);
        log.info("레디스에 잔소리숫자 -=1 누적 마이너스 완료.");
    }




    private String getKey(Long publicTodoPKId){
        return "TODO:NAG:" + publicTodoPKId;
    }

}
