package com.kamillo.task.scheduler.order.integration;

import com.kamillo.task.scheduler.order.infra.GeneratedSeatsRepo;
import com.kamillo.task.scheduler.order.infra.PostgresSeats;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInstance;
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
import java.util.stream.IntStream;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(
        locations = "classpath:application-integration.yml")
@Testcontainers
@ActiveProfiles("integration")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class BaseIT <T> {

    protected static final String ORDER_ID_PARAM = "orderId";

    @Autowired
    private GeneratedSeatsRepo seatsRepo;

    @Autowired
    private TestRestTemplate restTemplate;

    @BeforeEach
    public void setup() {
        IntStream.range(1, 3).forEach(
            row -> IntStream.range(1, 3).forEach(
                number -> seatsRepo.save(PostgresSeats.builder().row(row).number(number).free(true).build())
            )
        );
    }

    public BaseIT() {
    }

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
