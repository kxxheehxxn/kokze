package org.ozea.recommend.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.ozea.product.dto.ProductDto;
import org.ozea.recommend.dto.RecommendResponseDto;
import org.ozea.recommend.service.RecommendService;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("RecommendController 단위 테스트")
class RecommendControllerTest {

    @Mock
    private RecommendService recommendService;

    @InjectMocks
    private RecommendController recommendController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(recommendController).build();
    }

    @Test
    @DisplayName("GET /api/recommend - 추천 결과를 정상적으로 반환한다")
    void recommend_ReturnsRecommendationSuccessfully() throws Exception {
        // Given
        RecommendResponseDto mockResponse = RecommendResponseDto.builder()
                .type("RISK_TOLERANCE")
                .resultCode("MODERATE")
                .score(50)
                .products(Arrays.asList(
                        createProduct(1L, "중위험 펀드", "MEDIUM"),
                        createProduct(2L, "균형 펀드", "MEDIUM")
                ))
                .build();

        when(recommendService.recommend()).thenReturn(mockResponse);

        // When & Then
        mockMvc.perform(get("/api/recommend"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.type").value("RISK_TOLERANCE"))
                .andExpect(jsonPath("$.resultCode").value("MODERATE"))
                .andExpect(jsonPath("$.score").value(50))
                .andExpect(jsonPath("$.products").isArray())
                .andExpect(jsonPath("$.products.length()").value(2))
                .andExpect(jsonPath("$.products[0].name").value("중위험 펀드"))
                .andExpect(jsonPath("$.products[1].name").value("균형 펀드"));

        verify(recommendService, times(1)).recommend();
    }

    @Test
    @DisplayName("GET /api/recommend - 게스트 사용자에게 추천을 제공한다")
    void recommend_ForGuest_ReturnsGuestRecommendation() throws Exception {
        // Given
        RecommendResponseDto guestResponse = RecommendResponseDto.guest(
                Arrays.asList(createProduct(1L, "게스트 추천 상품", "MEDIUM"))
        );

        when(recommendService.recommend()).thenReturn(guestResponse);

        // When & Then
        mockMvc.perform(get("/api/recommend"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.type").value("GUEST"))
                .andExpect(jsonPath("$.resultCode").value("DEFAULT"))
                .andExpect(jsonPath("$.score").isEmpty())
                .andExpect(jsonPath("$.products").isArray())
                .andExpect(jsonPath("$.products[0].name").value("게스트 추천 상품"));

        verify(recommendService, times(1)).recommend();
    }

    @Test
    @DisplayName("GET /api/recommend - 빈 제품 리스트도 정상 반환한다")
    void recommend_WithEmptyProducts_ReturnsEmptyList() throws Exception {
        // Given
        RecommendResponseDto emptyResponse = RecommendResponseDto.builder()
                .type("RISK_TOLERANCE")
                .resultCode("CONSERVATIVE")
                .score(25)
                .products(Collections.emptyList())
                .build();

        when(recommendService.recommend()).thenReturn(emptyResponse);

        // When & Then
        mockMvc.perform(get("/api/recommend"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.type").value("RISK_TOLERANCE"))
                .andExpect(jsonPath("$.products").isArray())
                .andExpect(jsonPath("$.products").isEmpty());

        verify(recommendService, times(1)).recommend();
    }

    @Test
    @DisplayName("GET /api/recommend - CONSERVATIVE 사용자 추천")
    void recommend_ForConservativeUser_ReturnsLowRiskProducts() throws Exception {
        // Given
        RecommendResponseDto conservativeResponse = RecommendResponseDto.builder()
                .type("RISK_TOLERANCE")
                .resultCode("CONSERVATIVE")
                .score(20)
                .products(Arrays.asList(
                        createProduct(1L, "안전 예금", "LOW"),
                        createProduct(2L, "저위험 펀드", "LOW")
                ))
                .build();

        when(recommendService.recommend()).thenReturn(conservativeResponse);

        // When & Then
        mockMvc.perform(get("/api/recommend"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.resultCode").value("CONSERVATIVE"))
                .andExpect(jsonPath("$.score").value(20))
                .andExpect(jsonPath("$.products[0].riskLevel").value("LOW"))
                .andExpect(jsonPath("$.products[1].riskLevel").value("LOW"));

        verify(recommendService).recommend();
    }

    @Test
    @DisplayName("GET /api/recommend - AGGRESSIVE 사용자 추천")
    void recommend_ForAggressiveUser_ReturnsHighRiskProducts() throws Exception {
        // Given
        RecommendResponseDto aggressiveResponse = RecommendResponseDto.builder()
                .type("RISK_TOLERANCE")
                .resultCode("AGGRESSIVE")
                .score(90)
                .products(Arrays.asList(
                        createProduct(3L, "고위험 펀드", "HIGH"),
                        createProduct(4L, "주식형 펀드", "HIGH")
                ))
                .build();

        when(recommendService.recommend()).thenReturn(aggressiveResponse);

        // When & Then
        mockMvc.perform(get("/api/recommend"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.resultCode").value("AGGRESSIVE"))
                .andExpect(jsonPath("$.score").value(90))
                .andExpect(jsonPath("$.products[0].riskLevel").value("HIGH"))
                .andExpect(jsonPath("$.products[1].riskLevel").value("HIGH"));

        verify(recommendService).recommend();
    }

    @Test
    @DisplayName("GET /api/recommend - 서비스에서 예외 발생시 전파된다")
    void recommend_WhenServiceThrowsException_PropagatesException() throws Exception {
        // Given
        when(recommendService.recommend())
                .thenThrow(new IllegalStateException("위험 성향 평가 결과가 없습니다."));

        // When & Then
        mockMvc.perform(get("/api/recommend"))
                .andExpect(status().is5xxServerError());

        verify(recommendService).recommend();
    }

    @Test
    @DisplayName("GET /api/recommend - 여러 번 호출해도 정상 동작한다")
    void recommend_MultipleRequests_WorksCorrectly() throws Exception {
        // Given
        RecommendResponseDto mockResponse = RecommendResponseDto.builder()
                .type("RISK_TOLERANCE")
                .resultCode("MODERATE")
                .score(50)
                .products(Arrays.asList(createProduct(1L, "상품", "MEDIUM")))
                .build();

        when(recommendService.recommend()).thenReturn(mockResponse);

        // When & Then - 3번 호출
        for (int i = 0; i < 3; i++) {
            mockMvc.perform(get("/api/recommend"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.resultCode").value("MODERATE"));
        }

        verify(recommendService, times(3)).recommend();
    }

    // Helper method
    private ProductDto createProduct(Long id, String name, String riskLevel) {
        return ProductDto.builder()
                .id(id)
                .name(name)
                .category("FUND")
                .riskLevel(riskLevel)
                .interestRate(new BigDecimal("5.0"))
                .minBalance(new BigDecimal("1000000"))
                .description(name + " 설명")
                .build();
    }
}