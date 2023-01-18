package com.example.jpa_test.config;

import io.lettuce.core.RedisURI;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
@EnableRedisRepositories
@RequiredArgsConstructor
public class RedisConfiguration {


    //임포트 할 때, 멀티플 초이스가 가능한데 그 중에서 딴게 아니라
    //import org.springframework.boot.autoconfigure.data.redis.RedisProperties;로 임포트 해야 한다.
    //여기서 정의한 프로퍼티는 application.yml에서 셋팅한 레디스 관련 설정 정보를 담고 있다.
    //RedisProperties 클래스 파일을 보면,
    //@ConfigurationProperties(prefix = "spring.redis")라고 써 있는 것을 볼 수 있는데,
    //이 경우, application.yml 파일에서 spring.redis~~ 라고 입력해서 설정값들을 셋팅해주면 이 클래스파일에 그 정보가 저장되는 것이다.
    private final RedisProperties redisProperties;

    @Bean
    public RedisConnectionFactory redisConnectionFactory(){
        /*RedisURI redisURI = RedisURI.create(redisProperties.getUrl());
        org.springframework.data.redis.connection.RedisConfiguration configuration = LettuceConnectionFactory.createRedisConfiguration(redisURI);*/
        LettuceConnectionFactory factory = new LettuceConnectionFactory("여기에 레디스 엔드포인트 직접 넣으라", 6379);
        factory.afterPropertiesSet();
        return factory;
    }

    //유저의 jwt 토큰을 저장해 둘 레디스 템플릿을 빈으로 만들어준다.
    @Bean
    @Primary
    public RedisTemplate<String, String> makeUserJwtRedisTemplate(RedisConnectionFactory redisConnectionFactory){
        //템플릿을 만들어서 설정값을 붙여준다.
        RedisTemplate<String, String> redisTemplate = new RedisTemplate<>();

        //레디스 템플릿은 레디스 오퍼레이션 기능을 추상화하여 제공하는 클래스다.
        //오펴레이션 기능이 작동하기 위해서는 연결 url 등의 세부 정보를 설정해줘야 한다.
        redisTemplate.setConnectionFactory(redisConnectionFactory);

        //시리얼라리저를 설정해줘야 한다.
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        //'값' 타입이 무엇인지에 따라서 시리얼라이저가 달라져야 한다.
        // 값 타입이 User라는 엔티티 클래스일 경우, new Jackson2JsonRedisSerializer<User>(User.class) 이렇게 넣어줘야 한다.
        redisTemplate.setValueSerializer(new StringRedisSerializer());
        return redisTemplate;
    }


    //특정 유저가 팔로우하고 있는 다른 유저들의 주키 아이디 값을 리스트 형태로 캐시하는 것에 쓰는 레디스 템플릿을 만든다.
    //Long 타입을 레디스에서 별도로 제공하지 않으므로 값 타입을 Object 타입으로 정의한다.
    //저장되는 값 타입이 Object일지라도, 레디스템플릿 자치에서 리스트 동작을 지원하기 때문에 값 부분 시리얼라이저가
    //리스트 관련 시리얼라이저일 필요는 없다.
    /*@Bean
    @Primary
    public RedisTemplate<String, Long> makeStringObjectListRedisTemplate(RedisConnectionFactory redisConnectionFactory){
        RedisTemplate<String, Long> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        //Object는 객체타입이므로 new Jackson2JsonRedisSerializer<Long>(Long.class)를 사용한다.
        redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<>(Long.class));
        return redisTemplate;
    }*/



    /*@Bean
    @Primary
    public RedisTemplate<String, Object> makeStringRedisTemplate() {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory());
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<>(String.class));
        return redisTemplate;
    }*/


}
















