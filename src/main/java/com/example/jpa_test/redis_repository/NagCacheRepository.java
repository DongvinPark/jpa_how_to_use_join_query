package com.example.jpa_test.redis_repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
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
public class NagCacheRepository implements Serializable {

    private static final long serialVersionUID = 1L;

    private final RedisTemplate<String, Object> template;

    //이 부분이 문제다. 레디스 캐시에만 잔소리/서포트를 기록하면 서비스가 운영될수록
    //캐시서버에 기록이 계속 누적되는 것이다.
    //때문에 누적된 캐시기록들을 1일 단위로 매일 새벽 2~6시마다 어제의 캐시 기록들을 DB에 영구저장하는 것이 필요할 것이다.
    @JsonSerialize(using = DurationSerializer.class)
    @JsonDeserialize(using = DurationDeserializer.class)
    private static final Duration NAG_NUMBER_DURATION = Duration.ofDays(3);


    //잔소리 숫자를 레디스로부터 가져온다.
    public Long getSupportNumber(Long publicTodoPKId){
        String key = getKey(publicTodoPKId);
        Long value = (Long) template.opsForValue().get(key);
        log.info("레디스로부터 잔소리 숫자 가져오기 완료");
        return value;
    }


    //최초의 잔소리에 대하여 캐싱한다.
    public void setInitialNag(Long publicTodoPKId) throws JsonProcessingException {

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        String durationString = objectMapper.writeValueAsString(NAG_NUMBER_DURATION);

        String key = getKey(publicTodoPKId);
        template.opsForValue().set(key, 1L, NAG_NUMBER_DURATION);
        log.info("레디스에 잔소리숫자 최초 셋팅 완료.");
    }

    //잔소리 숫자를 += 1 한다.
    public void plusOneNag(Long publicTodoPKId){
        String key = getKey(publicTodoPKId);

        Long prevNumber = (Long) template.opsForValue().get(key);

        prevNumber++;

        template.opsForValue().set(key, prevNumber, NAG_NUMBER_DURATION);
        log.info("레디스에 잔소리숫자 +=1 누적 플러스 완료.");
    }

    //잔소리 숫자를 -= 1 한다.
    public void minusOneNag(Long publicTodoPKId){
        String key = getKey(publicTodoPKId);

        Long prevNumber = (Long) template.opsForValue().get(key);

        prevNumber--;

        template.opsForValue().set(key, prevNumber, NAG_NUMBER_DURATION);
        log.info("레디스에 잔소리숫자 -=1 누적 마이너스 완료.");
    }




    private String getKey(Long publicTodoPKId){
        return "TODO:NAG:" + publicTodoPKId;
    }

}
