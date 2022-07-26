package com.kamillo.task.scheduler.integration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Map;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(
        locations = "classpath:application-integration.yml")
@Testcontainers
@ActiveProfiles("integration")
public class BaseIT <T> {

    @Autowired
    private TestRestTemplate restTemplate;

    public ResponseEntity<T> post(String path, Class<T> clazz, Map<String, String> params, T body){
        HttpEntity<T> requestEntity = new HttpEntity<>(body, new HttpHeaders());
        MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();
        params.forEach(parameters::add);
        String uri = UriComponentsBuilder
                .fromPath(path)
                .queryParams(parameters)
                .build().toUriString();
        return restTemplate.postForEntity(uri, requestEntity, clazz);
    }

}
