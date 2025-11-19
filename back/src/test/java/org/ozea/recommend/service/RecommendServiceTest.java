package org.ozea.recommend.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.ozea.product.dto.ProductDto;
import org.ozea.recommend.dto.RecommendResponseDto;
import org.ozea.user.domain.User;
import org.ozea.user.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("RecommendService 단위 테스트")
class RecommendServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private Recommender recommender;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private RecommendService recommendService;

    private User testUser;
    private RecommendResponseDto mockRecommendResponse;

    @BeforeEach
    void setUp() {
        testUser = User.builder()
                .id(1L)
                .email("test@example.com")
                .name("테스트 사용자")
                .role("USER")
                .build();

        List<ProductDto> mockProducts = Arrays.asList(
                createProduct(1L, "테스트 상품1", "MEDIUM"),
                createProduct(2L, "테스트 상품2", "MEDIUM")
        );

        mockRecommendResponse = RecommendResponseDto.builder()
                .type("RISK_TOLERANCE")
                .resultCode("MODERATE")
                .score(50)
                .products(mockProducts)
                .build();

        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    @DisplayName("recommend() - 인증된 사용자에게 개인화된 추천을 제공한다")
    void recommend_WithAuthenticatedUser_ReturnsPersonalizedRecommendation() {
        // Given
        String email = "test@example.com";
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.isAuthenticated()).thenReturn(true);
        when(authentication.getPrincipal()).thenReturn(email);
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(testUser));
        when(recommender.recommendFor(testUser)).thenReturn(mockRecommendResponse);

        // When
        RecommendResponseDto result = recommendService.recommend();

        // Then
        assertThat(result).isNotNull();
        assertThat(result).isEqualTo(mockRecommendResponse);

        verify(userRepository).findByEmail(email);
        verify(recommender).recommendFor(testUser);
        verify(recommender, never()).recommendFor(null);
    }

    @Test
    @DisplayName("recommend() - null Authentication인 경우 게스트 추천을 제공한다")
    void recommend_WithNullAuthentication_ReturnsGuestRecommendation() {
        // Given
        when(securityContext.getAuthentication()).thenReturn(null);
        
        RecommendResponseDto guestResponse = RecommendResponseDto.builder()
                .type("GUEST")
                .resultCode("DEFAULT")
                .score(null)
                .products(Arrays.asList(createProduct(1L, "게스트 상품", "MEDIUM")))
                .build();
        
        when(recommender.recommendFor(null)).thenReturn(guestResponse);

        // When
        RecommendResponseDto result = recommendService.recommend();

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getType()).isEqualTo("GUEST");
        assertThat(result.getScore()).isNull();

        verify(recommender).recommendFor(null);
        verify(userRepository, never()).findByEmail(any());
    }

    @Test
    @DisplayName("recommend() - 인증되지 않은 사용자는 게스트 추천을 받는다")
    void recommend_WithUnauthenticatedUser_ReturnsGuestRecommendation() {
        // Given
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.isAuthenticated()).thenReturn(false);
        
        RecommendResponseDto guestResponse = RecommendResponseDto.guest(
                Arrays.asList(createProduct(1L, "게스트 상품", "MEDIUM"))
        );
        
        when(recommender.recommendFor(null)).thenReturn(guestResponse);

        // When
        RecommendResponseDto result = recommendService.recommend();

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getType()).isEqualTo("GUEST");

        verify(recommender).recommendFor(null);
        verify(userRepository, never()).findByEmail(any());
    }

    @Test
    @DisplayName("recommend() - anonymousUser는 게스트 추천을 받는다")
    void recommend_WithAnonymousUser_ReturnsGuestRecommendation() {
        // Given
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.isAuthenticated()).thenReturn(true);
        when(authentication.getPrincipal()).thenReturn("anonymousUser");
        
        RecommendResponseDto guestResponse = RecommendResponseDto.guest(
                Arrays.asList(createProduct(1L, "게스트 상품", "MEDIUM"))
        );
        
        when(recommender.recommendFor(null)).thenReturn(guestResponse);

        // When
        RecommendResponseDto result = recommendService.recommend();

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getType()).isEqualTo("GUEST");

        verify(recommender).recommendFor(null);
        verify(userRepository, never()).findByEmail(any());
    }

    @Test
    @DisplayName("recommend() - 존재하지 않는 사용자 이메일은 예외를 발생시킨다")
    void recommend_WithNonExistentUser_ThrowsException() {
        // Given
        String email = "nonexistent@example.com";
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.isAuthenticated()).thenReturn(true);
        when(authentication.getPrincipal()).thenReturn(email);
        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> recommendService.recommend())
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("유저를 찾을 수 없습니다");

        verify(userRepository).findByEmail(email);
        verify(recommender, never()).recommendFor(any());
    }

    @Test
    @DisplayName("recommend() - Principal이 String이 아닌 경우를 처리한다")
    void recommend_WithNonStringPrincipal_HandlesSafely() {
        // Given
        Object nonStringPrincipal = new Object();
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.isAuthenticated()).thenReturn(true);
        when(authentication.getPrincipal()).thenReturn(nonStringPrincipal);

        // When & Then
        // String으로 캐스팅하려 할 때 ClassCastException 발생
        assertThatThrownBy(() -> recommendService.recommend())
                .isInstanceOf(ClassCastException.class);

        verify(recommender, never()).recommendFor(any());
    }

    @Test
    @DisplayName("recommend() - 여러 사용자가 순차적으로 추천을 요청할 수 있다")
    void recommend_MultipleUsers_HandlesSequentially() {
        // Given - User 1
        String email1 = "user1@example.com";
        User user1 = User.builder().id(1L).email(email1).name("사용자1").build();
        RecommendResponseDto response1 = RecommendResponseDto.builder()
                .type("RISK_TOLERANCE")
                .resultCode("CONSERVATIVE")
                .score(30)
                .products(Arrays.asList(createProduct(1L, "상품1", "LOW")))
                .build();

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.isAuthenticated()).thenReturn(true);
        when(authentication.getPrincipal()).thenReturn(email1);
        when(userRepository.findByEmail(email1)).thenReturn(Optional.of(user1));
        when(recommender.recommendFor(user1)).thenReturn(response1);

        // When - User 1 request
        RecommendResponseDto result1 = recommendService.recommend();

        // Then - User 1
        assertThat(result1.getResultCode()).isEqualTo("CONSERVATIVE");
        verify(recommender).recommendFor(user1);

        // Given - User 2
        String email2 = "user2@example.com";
        User user2 = User.builder().id(2L).email(email2).name("사용자2").build();
        RecommendResponseDto response2 = RecommendResponseDto.builder()
                .type("RISK_TOLERANCE")
                .resultCode("AGGRESSIVE")
                .score(85)
                .products(Arrays.asList(createProduct(2L, "상품2", "HIGH")))
                .build();

        when(authentication.getPrincipal()).thenReturn(email2);
        when(userRepository.findByEmail(email2)).thenReturn(Optional.of(user2));
        when(recommender.recommendFor(user2)).thenReturn(response2);

        // When - User 2 request
        RecommendResponseDto result2 = recommendService.recommend();

        // Then - User 2
        assertThat(result2.getResultCode()).isEqualTo("AGGRESSIVE");
        verify(recommender).recommendFor(user2);
    }

    @Test
    @DisplayName("recommend() - 같은 사용자가 여러 번 요청해도 정상 동작한다")
    void recommend_SameUserMultipleTimes_WorksCorrectly() {
        // Given
        String email = "test@example.com";
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.isAuthenticated()).thenReturn(true);
        when(authentication.getPrincipal()).thenReturn(email);
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(testUser));
        when(recommender.recommendFor(testUser)).thenReturn(mockRecommendResponse);

        // When
        RecommendResponseDto result1 = recommendService.recommend();
        RecommendResponseDto result2 = recommendService.recommend();
        RecommendResponseDto result3 = recommendService.recommend();

        // Then
        assertThat(result1).isEqualTo(mockRecommendResponse);
        assertThat(result2).isEqualTo(mockRecommendResponse);
        assertThat(result3).isEqualTo(mockRecommendResponse);

        verify(userRepository, times(3)).findByEmail(email);
        verify(recommender, times(3)).recommendFor(testUser);
    }

    @Test
    @DisplayName("recommend() - 빈 이메일 문자열도 처리한다")
    void recommend_WithEmptyEmailString_HandlesGracefully() {
        // Given
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.isAuthenticated()).thenReturn(true);
        when(authentication.getPrincipal()).thenReturn("");
        when(userRepository.findByEmail("")).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> recommendService.recommend())
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("유저를 찾을 수 없습니다");

        verify(userRepository).findByEmail("");
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