package org.ozea.recommend.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.ozea.assessment.dto.AssessmentResultDto;
import org.ozea.product.dto.ProductDto;
import org.ozea.user.domain.User;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("RecommendResponseDto 단위 테스트")
class RecommendResponseDtoTest {

    @Test
    @DisplayName("from() - AssessmentResultDto와 제품 리스트로 RecommendResponseDto를 생성한다")
    void from_WithValidInputs_CreatesRecommendResponseDto() {
        // Given
        User user = User.builder()
                .id(1L)
                .email("test@example.com")
                .name("테스트 사용자")
                .build();

        AssessmentResultDto assessmentResult = AssessmentResultDto.builder()
                .id(1L)
                .user(user)
                .type("RISK_TOLERANCE")
                .resultCode("CONSERVATIVE")
                .score(30)
                .createdAt(LocalDateTime.now())
                .build();

        List<ProductDto> products = Arrays.asList(
                createProduct(1L, "안전 예금", "LOW"),
                createProduct(2L, "저위험 펀드", "LOW")
        );

        // When
        RecommendResponseDto result = RecommendResponseDto.from(products, assessmentResult);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getType()).isEqualTo("RISK_TOLERANCE");
        assertThat(result.getResultCode()).isEqualTo("CONSERVATIVE");
        assertThat(result.getScore()).isEqualTo(30);
        assertThat(result.getProducts()).hasSize(2);
        assertThat(result.getProducts()).containsExactlyElementsOf(products);
    }

    @Test
    @DisplayName("from() - 빈 제품 리스트로도 정상적으로 생성된다")
    void from_WithEmptyProductList_CreatesRecommendResponseDto() {
        // Given
        User user = User.builder()
                .id(1L)
                .email("test@example.com")
                .build();

        AssessmentResultDto assessmentResult = AssessmentResultDto.builder()
                .id(1L)
                .user(user)
                .type("RISK_TOLERANCE")
                .resultCode("MODERATE")
                .score(50)
                .createdAt(LocalDateTime.now())
                .build();

        List<ProductDto> emptyProducts = Collections.emptyList();

        // When
        RecommendResponseDto result = RecommendResponseDto.from(emptyProducts, assessmentResult);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getType()).isEqualTo("RISK_TOLERANCE");
        assertThat(result.getResultCode()).isEqualTo("MODERATE");
        assertThat(result.getScore()).isEqualTo(50);
        assertThat(result.getProducts()).isEmpty();
    }

    @Test
    @DisplayName("from() - AGGRESSIVE 결과 코드로 RecommendResponseDto를 생성한다")
    void from_WithAggressiveResultCode_CreatesRecommendResponseDto() {
        // Given
        User user = User.builder()
                .id(1L)
                .email("aggressive@example.com")
                .build();

        AssessmentResultDto assessmentResult = AssessmentResultDto.builder()
                .id(2L)
                .user(user)
                .type("RISK_TOLERANCE")
                .resultCode("AGGRESSIVE")
                .score(85)
                .createdAt(LocalDateTime.now())
                .build();

        List<ProductDto> products = Arrays.asList(
                createProduct(3L, "고위험 펀드", "HIGH"),
                createProduct(4L, "주식형 펀드", "HIGH")
        );

        // When
        RecommendResponseDto result = RecommendResponseDto.from(products, assessmentResult);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getType()).isEqualTo("RISK_TOLERANCE");
        assertThat(result.getResultCode()).isEqualTo("AGGRESSIVE");
        assertThat(result.getScore()).isEqualTo(85);
        assertThat(result.getProducts()).hasSize(2);
    }

    @Test
    @DisplayName("guest() - 게스트 사용자용 RecommendResponseDto를 생성한다")
    void guest_WithProducts_CreatesGuestRecommendResponseDto() {
        // Given
        List<ProductDto> products = Arrays.asList(
                createProduct(1L, "중위험 예금", "MEDIUM"),
                createProduct(2L, "중위험 펀드", "MEDIUM"),
                createProduct(3L, "균형 펀드", "MEDIUM")
        );

        // When
        RecommendResponseDto result = RecommendResponseDto.guest(products);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getType()).isEqualTo("GUEST");
        assertThat(result.getResultCode()).isEqualTo("DEFAULT");
        assertThat(result.getScore()).isNull();
        assertThat(result.getProducts()).hasSize(3);
        assertThat(result.getProducts()).containsExactlyElementsOf(products);
    }

    @Test
    @DisplayName("guest() - 빈 제품 리스트로 게스트용 응답을 생성한다")
    void guest_WithEmptyProductList_CreatesGuestRecommendResponseDto() {
        // Given
        List<ProductDto> emptyProducts = new ArrayList<>();

        // When
        RecommendResponseDto result = RecommendResponseDto.guest(emptyProducts);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getType()).isEqualTo("GUEST");
        assertThat(result.getResultCode()).isEqualTo("DEFAULT");
        assertThat(result.getScore()).isNull();
        assertThat(result.getProducts()).isEmpty();
    }

    @Test
    @DisplayName("guest() - null 제품 리스트로도 생성이 가능하다")
    void guest_WithNullProductList_CreatesGuestRecommendResponseDto() {
        // When
        RecommendResponseDto result = RecommendResponseDto.guest(null);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getType()).isEqualTo("GUEST");
        assertThat(result.getResultCode()).isEqualTo("DEFAULT");
        assertThat(result.getScore()).isNull();
        assertThat(result.getProducts()).isNull();
    }

    @Test
    @DisplayName("builder를 사용한 수동 생성이 정상적으로 동작한다")
    void builder_ManualCreation_WorksCorrectly() {
        // Given & When
        List<ProductDto> products = Arrays.asList(createProduct(1L, "테스트 상품", "MEDIUM"));
        
        RecommendResponseDto result = RecommendResponseDto.builder()
                .type("CUSTOM_TYPE")
                .resultCode("CUSTOM_CODE")
                .score(42)
                .products(products)
                .build();

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getType()).isEqualTo("CUSTOM_TYPE");
        assertThat(result.getResultCode()).isEqualTo("CUSTOM_CODE");
        assertThat(result.getScore()).isEqualTo(42);
        assertThat(result.getProducts()).hasSize(1);
    }

    @Test
    @DisplayName("from()과 guest()로 생성된 객체는 서로 다른 특성을 갖는다")
    void from_AndGuest_ProduceDifferentCharacteristics() {
        // Given
        User user = User.builder().id(1L).email("test@example.com").build();
        AssessmentResultDto assessmentResult = AssessmentResultDto.builder()
                .id(1L)
                .user(user)
                .type("RISK_TOLERANCE")
                .resultCode("CONSERVATIVE")
                .score(25)
                .createdAt(LocalDateTime.now())
                .build();

        List<ProductDto> products = Arrays.asList(createProduct(1L, "상품1", "LOW"));

        // When
        RecommendResponseDto fromResult = RecommendResponseDto.from(products, assessmentResult);
        RecommendResponseDto guestResult = RecommendResponseDto.guest(products);

        // Then
        assertThat(fromResult.getType()).isNotEqualTo(guestResult.getType());
        assertThat(fromResult.getResultCode()).isNotEqualTo(guestResult.getResultCode());
        assertThat(fromResult.getScore()).isNotNull();
        assertThat(guestResult.getScore()).isNull();
    }

    // Helper method
    private ProductDto createProduct(Long id, String name, String riskLevel) {
        return ProductDto.builder()
                .id(id)
                .name(name)
                .category("DEPOSIT")
                .riskLevel(riskLevel)
                .interestRate(new BigDecimal("3.5"))
                .minBalance(new BigDecimal("1000000"))
                .description("테스트 상품 설명")
                .build();
    }
}