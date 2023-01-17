package com.example.jpa_test.redis_repository;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
@RequiredArgsConstructor
public class FolloweeCacheRepository {

    private final RedisTemplate<String, Object> template;

    //이것은 값이 큰 편이므로 캐시에서 저장되는 유효기간을 짧게 설정한다.
    private static final Duration LIST_DURATION = Duration.ofDays(1);


    //최초로 로그인 했다. DB로부터 특정 유저가 팔로우한 사람들의 주키 아이디 값 리스트를 받아서 레디스에 저장한다.
    //리스트에 더하는 작업은 5000번을 초과할 수 없다.
    public void setFolloweeList(List<Long> pkIdList, Long userPKId){
        String key = getKey(userPKId);
        for(Long id : pkIdList){
            template.opsForList().rightPush(key, id, LIST_DURATION);
        }
        log.info("레디스에 팔로이 리스트 캐시 완료.");
    }


    //레디스로부터 특정 유저가 팔로우한 사람들의 리스트를 가져온다.
    public List<Long> getFolloweeList(Long userPKId){
        String key = getKey(userPKId);
        long size = (Long) template.opsForList().size(key);

        List<Long> resultList = new ArrayList<>();
        for(Object longObject : Objects.requireNonNull(template.opsForList().range(key, 0, size))){
            resultList.add( (Long) longObject );
        }

        log.info("레디스로부터 팔로우 리스트 가져오기 완료.");
        return resultList;
    }


    //새롭게 팔로우 또는 언팔로우한 사람에 대한 처리는 하지 않는다. 어차피 Duration이 지나면 캐시 값은 소멸하고,
    //나중에 새롭게 캐시값 만들 때 팔로우 또는 언팔로우한 결과가 반영될 것이기 때문이다.


    private String getKey(Long userPKId){
        return "FOLLOWEE:PKIDLIST:" + userPKId;
    }

}
