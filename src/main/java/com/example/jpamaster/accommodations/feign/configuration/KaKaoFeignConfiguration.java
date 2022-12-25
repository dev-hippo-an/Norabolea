package com.example.jpamaster.accommodations.feign.configuration;

import feign.RequestInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KaKaoFeignConfiguration {

    /**
     * @Configuration을 붙이고 @Bean으로 빈을 등록하게 되면 Feign Client는 빈으로 등록되어 있는 @ReqeustInterceptor을 찾아 모든 feign 인터페이스에 적용시키게 된다.
     * 반드시 @Configuration을 붙여줘야 한다 ㅎㅎ 꼭 붙여줘야 하는 이유는 스프링 내부 동작 원리로, 좀 더 공부해보자.
     * 만약, 모든 feign 에 적용시키고 싶지 않다면 적용시키고 싶은 feignClient에 config를 특정 config class로 적용시키면 된다.
     * @return
     */
    @Bean
    public RequestInterceptor requestInterceptor() {
        return requestTemplate -> requestTemplate.header("Authorization", "KakaoAK 1f5b9a84c7d8338b6ab29ce3065408b2");
    }
}
