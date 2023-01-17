package com.example.jpa_test.redis_repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.deser.DurationDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.DurationSerializer;
import java.io.Serializable;
import java.time.Duration;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
@RequiredArgsConstructor
@MappedSuperclass
@EntityListeners(value = {AuditingEntityListener.class})
public class SupportCacheRepository implements Serializable {

    private static final long serialVersionUID = 1L;
    private final RedisTemplate<String, Long> template;

    //임시로 3일이라 하자.
    @JsonSerialize(using = DurationSerializer.class)
    @JsonDeserialize(using = DurationDeserializer.class)
    private static final Duration SUPPORT_NUMBER_DURATION = Duration.ofDays(3);


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

        Long prevNumber = template.opsForValue().get(key);

        prevNumber++;

        template.opsForValue().set(key, prevNumber, SUPPORT_NUMBER_DURATION);
        log.info("레디스에 응원숫자 +=1 누적 플러스 완료.");
    }

    //응원 숫자를 -= 1 한다.
    public void minusOneSupport(Long publicTodoPKId){
        String key = getKey(publicTodoPKId);

        Long prevNumber = template.opsForValue().get(key);

        prevNumber--;

        template.opsForValue().set(key, prevNumber, SUPPORT_NUMBER_DURATION);
        log.info("레디스에 응원숫자 -=1 누적 마이너스 완료.");
    }




    private String getKey(Long publicTodoPKId){
        return "TODO:SUPPORT:" + publicTodoPKId;
    }
}
