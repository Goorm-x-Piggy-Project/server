package com.piggymetrics.notification.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.piggymetrics.notification.domain.Frequency;
import com.piggymetrics.notification.domain.NotificationSettings;
import com.piggymetrics.notification.domain.NotificationType;
import com.piggymetrics.notification.domain.Recipient;
import com.piggymetrics.notification.service.RecipientService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.security.Principal;
import java.util.Map;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * RecipientController의 REST API 동작을 검증.
 */
class RecipientControllerTest {

    @InjectMocks
    private RecipientController recipientController; // 테스트할 컨트롤러 클래스

    @Mock
    private RecipientService recipientService; // 의존성 주입을 위한 Mock 객체

    private MockMvc mockMvc; // MockMvc를 사용하여 HTTP 요청 테스트
    private ObjectMapper objectMapper; // JSON 직렬화 및 역직렬화를 위한 ObjectMapper

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(recipientController).build();
        this.objectMapper = new ObjectMapper();
    }

    /**
     * 현재 사용자의 알림 설정을 조회하는 API가 정상적으로 동작하는지 확인.
     */
    @Test
    @WithMockUser(username = "test-user")
    void shouldGetCurrentNotificationsSettings() throws Exception {
        // Given
        Principal principal = () -> "test-user";
        Recipient recipient = getMockRecipient();
        when(recipientService.findByAccountName("test-user")).thenReturn(recipient);

        // When & Then
        mockMvc.perform(get("/recipients/current").principal(principal))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accountName").value("test-user"))
                .andExpect(jsonPath("$.email").value("test@test.com"));
    }

    /**
     * 알림 설정 조회 시 수신자가 존재하지 않을 경우 404 응답을 반환하는지 확인.
     */
    @Test
    @WithMockUser(username = "test-user")
    void shouldReturnNotFoundWhenRecipientNotExists() throws Exception {
        // Given
        Principal principal = () -> "test-user";
        when(recipientService.findByAccountName("test-user")).thenReturn(null);

        // When & Then
        mockMvc.perform(get("/recipients/current").principal(principal))
                .andExpect(status().isNotFound());
    }

    /**
     * 현재 사용자의 알림 설정 저장 API가 정상적으로 동작하는지 확인.
     */
    @Test
    @WithMockUser(username = "test-user")
    void shouldSaveNotificationSettings() throws Exception {
        // Given
        NotificationSettings settings = NotificationSettings.builder()
                .active(true)
                .frequency(Frequency.WEEKLY)
                .build();

        // 필요 시 ObjectMapper 설정
        objectMapper.registerModule(new JavaTimeModule());  // Date 타입 처리

        Recipient recipient = getMockRecipient();
        when(recipientService.updateNotificationSettings("test-user", NotificationType.REMIND, settings))
                .thenReturn(recipient);

        // When & Then
        mockMvc.perform(put("/recipients/current/REMIND")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(settings)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.accountName").value("test-user"))
                .andExpect(jsonPath("$.email").value("test@test.com"));
    }

    /**
     * 저장 시 잘못된 알림 유형이 전달되었을 때 400 응답을 반환하는지 확인.
     */
    @Test
    @WithMockUser(username = "test-user")
    void shouldReturnBadRequestForInvalidNotificationType() throws Exception {
        // Given
        NotificationSettings settings = NotificationSettings.builder()
                .active(true)
                .frequency(Frequency.WEEKLY)
                .build();

        // When & Then
        mockMvc.perform(put("/recipients/current/INVALID")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(settings)))
                .andExpect(status().isBadRequest());
    }

    /**
     * Principal이 null일 때 400 응답을 반환하는지 확인.
     */
    @Test
    void shouldReturnBadRequestWhenPrincipalIsNull() throws Exception {
        mockMvc.perform(get("/recipients/current")
                        .principal(() -> null))  // Principal을 null로 설정
                .andExpect(status().isBadRequest());
    }

    /**
     * 수신자가 없는 경우에도 저장 API가 404를 반환하는지 확인.
     */
    @Test
    @WithMockUser(username = "test-user")
    void shouldReturnNotFoundWhenSavingNotificationSettingsForNonExistingRecipient() throws Exception {
        // Given
        NotificationSettings settings = NotificationSettings.builder()
                .active(true)
                .frequency(Frequency.WEEKLY)
                .build();

        when(recipientService.updateNotificationSettings("test-user", NotificationType.REMIND, settings))
                .thenReturn(null);

        // When & Then
        mockMvc.perform(put("/recipients/current/REMIND")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(settings)))
                .andExpect(status().isNotFound());
    }

    /**
     * Mock Recipient 객체 생성.
     */
    private Recipient getMockRecipient() {
        NotificationSettings remindSettings = NotificationSettings.builder()
                .active(true)
                .frequency(Frequency.WEEKLY)
                .build();

        NotificationSettings backupSettings = NotificationSettings.builder()
                .active(false)
                .frequency(Frequency.MONTHLY)
                .build();

        return Recipient.builder()
                .accountName("test-user")
                .email("test@test.com")
                .scheduledNotifications(Map.of(
                        NotificationType.REMIND, remindSettings,
                        NotificationType.BACKUP, backupSettings
                ))
                .build();
    }
}
