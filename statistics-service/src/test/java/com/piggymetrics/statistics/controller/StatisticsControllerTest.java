//package com.piggymetrics.statistics.controller;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.google.common.collect.ImmutableList;
//import com.piggymetrics.statistics.domain.Account;
//import com.piggymetrics.statistics.domain.Currency;
//import com.piggymetrics.statistics.domain.Item;
//import com.piggymetrics.statistics.domain.Saving;
//import com.piggymetrics.statistics.domain.TimePeriod;
//import com.piggymetrics.statistics.domain.timeseries.DataPoint;
//import com.piggymetrics.statistics.domain.timeseries.DataPointId;
//import com.piggymetrics.statistics.service.StatisticsService;
//import com.sun.security.auth.UserPrincipal;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.http.MediaType;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.setup.MockMvcBuilders;
//
//import java.math.BigDecimal;
//import java.util.Date;
//
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.ArgumentMatchers.anyString;
//import static org.mockito.Mockito.verify;
//import static org.mockito.Mockito.when;
//import static org.mockito.MockitoAnnotations.initMocks;
//import static org.mockito.internal.verification.VerificationModeFactory.times;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
//import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//@ExtendWith(MockitoExtension.class)
//@SpringBootTest
//public class StatisticsControllerTest {
//
//    private static final ObjectMapper mapper = new ObjectMapper();
//
//    @Mock
//    private StatisticsService statisticsService;
//
//    private MockMvc mockMvc;
//
//    @BeforeEach
//    public void setup() {
//        StatisticsController statisticsController = new StatisticsController(statisticsService);
//        this.mockMvc = MockMvcBuilders.standaloneSetup(statisticsController).build();
//    }
//
//    @Test
//    public void shouldGetStatisticsByAccountName() throws Exception {
//
//        final DataPoint dataPoint = DataPoint.builder()
//                .id(new DataPointId("test", new Date()))
//                .build();
//
//        when(statisticsService.findByAccountName(dataPoint.getId().getAccount()))
//                .thenReturn(ImmutableList.of(dataPoint));
//
//        mockMvc.perform(get("/api/v1/statistics/test").principal(new UserPrincipal(dataPoint.getId().getAccount())))
//                .andDo(print())
//                .andExpect(jsonPath("$[0].id.account").value(dataPoint.getId().getAccount()))
//                .andExpect(status().isOk());
//    }
//
//    @Test
//    public void shouldGetCurrentAccountStatistics() throws Exception {
//
//        final DataPoint dataPoint = DataPoint.builder()
//                .id(new DataPointId("test", new Date()))
//                .build();
//
//        when(statisticsService.findByAccountName(dataPoint.getId().getAccount()))
//                .thenReturn(ImmutableList.of(dataPoint));
//
//        mockMvc.perform(get("/api/v1/statistics/current").principal(new UserPrincipal(dataPoint.getId().getAccount())))
//                .andExpect(jsonPath("$[0].id.account").value(dataPoint.getId().getAccount()))
//                .andExpect(status().isOk());
//    }
//
//    @Test
//    public void shouldSaveAccountStatistics() throws Exception {
//
//        Saving saving = Saving.builder()
//                .amount(new BigDecimal(1500))
//                .currency(Currency.USD)
//                .interest(new BigDecimal("3.32"))
//                .deposit(true)
//                .capitalization(false)
//                .build();
//
//        Item grocery = Item.builder()
//                .title("Grocery")
//                .amount(new BigDecimal(10))
//                .currency(Currency.USD)
//                .period(TimePeriod.DAY)
//                .build();
//
//        Item salary = Item.builder()
//                .title("Salary")
//                .amount(new BigDecimal(9100))
//                .currency(Currency.USD)
//                .period(TimePeriod.MONTH)
//                .build();
//
//
//        final Account account = new Account();
//        account.setSaving(saving);
//        account.setExpenses(ImmutableList.of(grocery));
//        account.setIncomes(ImmutableList.of(salary));
//
//        String json = mapper.writeValueAsString(account);
//
//        mockMvc.perform(put("/api/v1/statistics/test").contentType(MediaType.APPLICATION_JSON).content(json))
//                .andExpect(status().isOk());
//
//        verify(statisticsService, times(1)).save(anyString(), any(Account.class));
//    }
//}