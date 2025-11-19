package org.ozea.recommend.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.ozea.assessment.dto.AssessmentResultDto;
import org.ozea.assessment.repository.AssessmentRepository;
import org.ozea.product.dto.ProductDto;
import org.ozea.product.repository.ProductRepository;
import org.ozea.recommend.dto.RecommendResponseDto;
import org.ozea.user.domain.User;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("RuleBasedRecommender 단위 테스트")
class RuleBasedRecommenderTest {

    @Mock
    private AssessmentRepository assessmentRepository;

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private RuleBasedRecommender ruleBasedRecommender;

    private User testUser;
    private List<ProductDto> lowRiskProducts;
    private List<ProductDto> mediumRiskProducts;
    private List<ProductDto> highRiskProducts;

    @BeforeEach
    void setUp() {
        testUser = User.builder()
                .id(1L)
                .email("test@example.com")
                .name("테스트 사용자")
                .build();

        lowRiskProducts = Arrays.asList(
                createProduct(1L, "안전 예금", "LOW", "3.0"),
                createProduct(2L, "저위험 펀드", "LOW", "3.5")
        );

        mediumRiskProducts = Arrays.asList(
                createProduct(3L, "중위험 펀드", "MEDIUM", "5.0"),
                createProduct(4L, "균형 펀드", "MEDIUM", "5.5")
        );

        highRiskProducts = Arrays.asList(
                createProduct(5L, "고위험 펀드", "HIGH", "8.0"),
                createProduct(6L, "주식형 펀드", "HIGH", "9.0")
        );
    }

    @Test
    @DisplayName("recommendFor() - null 사용자에게는 MEDIUM 리스크 제품을 추천한다 (게스트)")
    void recommendFor_WithNullUser_ReturnsGuestRecommendation() {
        // Given
        when(productRepository.findByRiskLevel("MEDIUM")).thenReturn(mediumRiskProducts);

        // When
        RecommendResponseDto result = ruleBasedRecommender.recommendFor(null);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getType()).isEqualTo("GUEST");
        assertThat(result.getResultCode()).isEqualTo("DEFAULT");
        assertThat(result.getScore()).isNull();
        assertThat(result.getProducts()).hasSize(2);
        assertThat(result.getProducts()).containsExactlyElementsOf(mediumRiskProducts);

        verify(productRepository).findByRiskLevel("MEDIUM");
        verify(assessmentRepository, never()).findTopByUserAndTypeOrderByCreatedAtDesc(any(), any());
    }

    @Test
    @DisplayName("recommendFor() - CONSERVATIVE 성향 사용자에게 LOW 리스크 제품을 추천한다")
    void recommendFor_WithConservativeUser_ReturnsLowRiskProducts() {
        // Given
        AssessmentResultDto conservativeResult = createAssessmentResult(
                1L, testUser, "CONSERVATIVE", 25);

        when(assessmentRepository.findTopByUserAndTypeOrderByCreatedAtDesc(testUser, "RISK_TOLERANCE"))
                .thenReturn(Optional.of(conservativeResult));
        when(productRepository.findByRiskLevel("LOW")).thenReturn(lowRiskProducts);

        // When
        RecommendResponseDto result = ruleBasedRecommender.recommendFor(testUser);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getType()).isEqualTo("RISK_TOLERANCE");
        assertThat(result.getResultCode()).isEqualTo("CONSERVATIVE");
        assertThat(result.getScore()).isEqualTo(25);
        assertThat(result.getProducts()).hasSize(2);
        assertThat(result.getProducts()).containsExactlyElementsOf(lowRiskProducts);

        verify(assessmentRepository).findTopByUserAndTypeOrderByCreatedAtDesc(testUser, "RISK_TOLERANCE");
        verify(productRepository).findByRiskLevel("LOW");
    }

    @Test
    @DisplayName("recommendFor() - AGGRESSIVE 성향 사용자에게 HIGH 리스크 제품을 추천한다")
    void recommendFor_WithAggressiveUser_ReturnsHighRiskProducts() {
        // Given
        AssessmentResultDto aggressiveResult = createAssessmentResult(
                2L, testUser, "AGGRESSIVE", 85);

        when(assessmentRepository.findTopByUserAndTypeOrderByCreatedAtDesc(testUser, "RISK_TOLERANCE"))
                .thenReturn(Optional.of(aggressiveResult));
        when(productRepository.findByRiskLevel("HIGH")).thenReturn(highRiskProducts);

        // When
        RecommendResponseDto result = ruleBasedRecommender.recommendFor(testUser);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getType()).isEqualTo("RISK_TOLERANCE");
        assertThat(result.getResultCode()).isEqualTo("AGGRESSIVE");
        assertThat(result.getScore()).isEqualTo(85);
        assertThat(result.getProducts()).hasSize(2);
        assertThat(result.getProducts()).containsExactlyElementsOf(highRiskProducts);

        verify(assessmentRepository).findTopByUserAndTypeOrderByCreatedAtDesc(testUser, "RISK_TOLERANCE");
        verify(productRepository).findByRiskLevel("HIGH");
    }

    @Test
    @DisplayName("recommendFor() - MODERATE 성향 사용자에게 MEDIUM 리스크 제품을 추천한다")
    void recommendFor_WithModerateUser_ReturnsMediumRiskProducts() {
        // Given
        AssessmentResultDto moderateResult = createAssessmentResult(
                3L, testUser, "MODERATE", 55);

        when(assessmentRepository.findTopByUserAndTypeOrderByCreatedAtDesc(testUser, "RISK_TOLERANCE"))
                .thenReturn(Optional.of(moderateResult));
        when(productRepository.findByRiskLevel("MEDIUM")).thenReturn(mediumRiskProducts);

        // When
        RecommendResponseDto result = ruleBasedRecommender.recommendFor(testUser);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getType()).isEqualTo("RISK_TOLERANCE");
        assertThat(result.getResultCode()).isEqualTo("MODERATE");
        assertThat(result.getScore()).isEqualTo(55);
        assertThat(result.getProducts()).hasSize(2);
        assertThat(result.getProducts()).containsExactlyElementsOf(mediumRiskProducts);

        verify(assessmentRepository).findTopByUserAndTypeOrderByCreatedAtDesc(testUser, "RISK_TOLERANCE");
        verify(productRepository).findByRiskLevel("MEDIUM");
    }

    @Test
    @DisplayName("recommendFor() - 알 수 없는 성향 코드는 MEDIUM으로 매핑한다")
    void recommendFor_WithUnknownResultCode_ReturnsMediumRiskProducts() {
        // Given
        AssessmentResultDto unknownResult = createAssessmentResult(
                4L, testUser, "UNKNOWN_CODE", 50);

        when(assessmentRepository.findTopByUserAndTypeOrderByCreatedAtDesc(testUser, "RISK_TOLERANCE"))
                .thenReturn(Optional.of(unknownResult));
        when(productRepository.findByRiskLevel("MEDIUM")).thenReturn(mediumRiskProducts);

        // When
        RecommendResponseDto result = ruleBasedRecommender.recommendFor(testUser);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getProducts()).containsExactlyElementsOf(mediumRiskProducts);

        verify(productRepository).findByRiskLevel("MEDIUM");
    }

    @Test
    @DisplayName("recommendFor() - 평가 결과가 없는 사용자는 예외를 발생시킨다")
    void recommendFor_WithNoAssessmentResult_ThrowsException() {
        // Given
        when(assessmentRepository.findTopByUserAndTypeOrderByCreatedAtDesc(testUser, "RISK_TOLERANCE"))
                .thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> ruleBasedRecommender.recommendFor(testUser))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("위험 성향 평가 결과가 없습니다.");

        verify(assessmentRepository).findTopByUserAndTypeOrderByCreatedAtDesc(testUser, "RISK_TOLERANCE");
        verify(productRepository, never()).findByRiskLevel(any());
    }

    @Test
    @DisplayName("recommendFor() - 빈 제품 리스트를 반환하는 경우도 처리한다")
    void recommendFor_WithEmptyProductList_ReturnsEmptyRecommendation() {
        // Given
        AssessmentResultDto conservativeResult = createAssessmentResult(
                1L, testUser, "CONSERVATIVE", 20);

        when(assessmentRepository.findTopByUserAndTypeOrderByCreatedAtDesc(testUser, "RISK_TOLERANCE"))
                .thenReturn(Optional.of(conservativeResult));
        when(productRepository.findByRiskLevel("LOW")).thenReturn(Collections.emptyList());

        // When
        RecommendResponseDto result = ruleBasedRecommender.recommendFor(testUser);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getProducts()).isEmpty();

        verify(productRepository).findByRiskLevel("LOW");
    }

    @Test
    @DisplayName("recommendFor() - 게스트 사용자의 경우 빈 제품 리스트도 반환 가능하다")
    void recommendFor_GuestWithEmptyProducts_ReturnsEmptyGuestRecommendation() {
        // Given
        when(productRepository.findByRiskLevel("MEDIUM")).thenReturn(Collections.emptyList());

        // When
        RecommendResponseDto result = ruleBasedRecommender.recommendFor(null);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getType()).isEqualTo("GUEST");
        assertThat(result.getProducts()).isEmpty();
    }

    @Test
    @DisplayName("mapRisk() - 모든 결과 코드가 올바르게 매핑된다")
    void mapRisk_AllResultCodes_MappedCorrectly() {
        // Given
        AssessmentResultDto conservativeResult = createAssessmentResult(1L, testUser, "CONSERVATIVE", 20);
        AssessmentResultDto aggressiveResult = createAssessmentResult(2L, testUser, "AGGRESSIVE", 80);
        AssessmentResultDto moderateResult = createAssessmentResult(3L, testUser, "MODERATE", 50);
        AssessmentResultDto customResult = createAssessmentResult(4L, testUser, "CUSTOM", 40);

        when(assessmentRepository.findTopByUserAndTypeOrderByCreatedAtDesc(eq(testUser), eq("RISK_TOLERANCE")))
                .thenReturn(Optional.of(conservativeResult))
                .thenReturn(Optional.of(aggressiveResult))
                .thenReturn(Optional.of(moderateResult))
                .thenReturn(Optional.of(customResult));

        when(productRepository.findByRiskLevel("LOW")).thenReturn(lowRiskProducts);
        when(productRepository.findByRiskLevel("HIGH")).thenReturn(highRiskProducts);
        when(productRepository.findByRiskLevel("MEDIUM")).thenReturn(mediumRiskProducts);

        // When & Then - CONSERVATIVE -> LOW
        ruleBasedRecommender.recommendFor(testUser);
        verify(productRepository).findByRiskLevel("LOW");

        // AGGRESSIVE -> HIGH
        ruleBasedRecommender.recommendFor(testUser);
        verify(productRepository).findByRiskLevel("HIGH");

        // MODERATE -> MEDIUM
        ruleBasedRecommender.recommendFor(testUser);
        verify(productRepository, times(1)).findByRiskLevel("MEDIUM");

        // CUSTOM (default) -> MEDIUM
        ruleBasedRecommender.recommendFor(testUser);
        verify(productRepository, times(2)).findByRiskLevel("MEDIUM");
    }

    @Test
    @DisplayName("recommendFor() - 대소문자가 다른 결과 코드는 매핑되지 않는다")
    void recommendFor_WithDifferentCaseResultCode_UseDefaultMapping() {
        // Given
        AssessmentResultDto lowerCaseResult = createAssessmentResult(
                1L, testUser, "conservative", 25);

        when(assessmentRepository.findTopByUserAndTypeOrderByCreatedAtDesc(testUser, "RISK_TOLERANCE"))
                .thenReturn(Optional.of(lowerCaseResult));
        when(productRepository.findByRiskLevel("MEDIUM")).thenReturn(mediumRiskProducts);

        // When
        RecommendResponseDto result = ruleBasedRecommender.recommendFor(testUser);

        // Then - default case로 MEDIUM 매핑
        assertThat(result.getProducts()).containsExactlyElementsOf(mediumRiskProducts);
        verify(productRepository).findByRiskLevel("MEDIUM");
        verify(productRepository, never()).findByRiskLevel("LOW");
    }

    // Helper methods
    private ProductDto createProduct(Long id, String name, String riskLevel, String interestRate) {
        return ProductDto.builder()
                .id(id)
                .name(name)
                .category("FUND")
                .riskLevel(riskLevel)
                .interestRate(new BigDecimal(interestRate))
                .minBalance(new BigDecimal("1000000"))
                .description(name + " 설명")
                .build();
    }

    private AssessmentResultDto createAssessmentResult(Long id, User user, String resultCode, Integer score) {
        return AssessmentResultDto.builder()
                .id(id)
                .user(user)
                .type("RISK_TOLERANCE")
                .resultCode(resultCode)
                .score(score)
                .createdAt(LocalDateTime.now())
                .build();
    }
}