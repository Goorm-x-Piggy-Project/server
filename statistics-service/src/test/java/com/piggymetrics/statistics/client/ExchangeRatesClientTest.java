//package com.piggymetrics.statistics.client;
//
//import com.piggymetrics.statistics.service.ExchangeRatesService;
//import java.math.BigDecimal;
//import java.time.LocalDate;
//import java.util.List;
//import java.util.Map;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.cloud.openfeign.EnableFeignClients;
//import org.springframework.context.annotation.Import;
//import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
//
//@SpringBootTest(properties = "de.flapdoodle.mongodb.embedded.version=5.0.5")
//public class ExchangeRatesClientTest {
//
//    @Autowired
//    private ExchangeRatesService exchangeRatesService;
//
//    @MockBean
//    private ClientRegistrationRepository clientRegistrationRepository; // 더미 빈 주입
//
//    @Test
//    void testGetExchangeRates() {
//        Map<String, BigDecimal> rates = exchangeRatesService.getExchangeRates();
//        System.out.println(rates);
//    }
//
//}