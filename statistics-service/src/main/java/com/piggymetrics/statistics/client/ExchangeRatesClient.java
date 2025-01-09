package com.piggymetrics.statistics.client;

import com.piggymetrics.statistics.domain.ExchangeRate;
import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Primary;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(
        url = "https://api.manana.kr/exchange",
        name = "rates-client",
        fallback = ExchangeRatesClientFallback.class
)
@Primary
public interface ExchangeRatesClient {

    @GetMapping("/rate.json") // URL의 엔드포인트만 지정
    List<ExchangeRate> getRates(); // 매개변수가 필요 없음

}
