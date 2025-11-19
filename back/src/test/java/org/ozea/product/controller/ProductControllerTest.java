package org.ozea.product.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.ozea.product.dto.ProductDto;
import org.ozea.product.service.ProductService;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("ProductController 단위 테스트")
class ProductControllerTest {

    @Mock
    private ProductService productService;

    @InjectMocks
    private ProductController productController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(productController).build();
    }

    @Test
    @DisplayName("GET /api/products - 모든 제품 목록을 조회한다")
    void list_ReturnsAllProducts() throws Exception {
        // Given
        List<ProductDto> products = Arrays.asList(
                createProduct(1L, "안전 예금", "LOW", "DEPOSIT", "3.0"),
                createProduct(2L, "중위험 펀드", "MEDIUM", "FUND", "5.0"),
                createProduct(3L, "고위험 펀드", "HIGH", "FUND", "8.0")
        );

        when(productService.findAll()).thenReturn(products);

        // When & Then
        mockMvc.perform(get("/api/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(3))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("안전 예금"))
                .andExpect(jsonPath("$[0].riskLevel").value("LOW"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].name").value("중위험 펀드"))
                .andExpect(jsonPath("$[1].riskLevel").value("MEDIUM"))
                .andExpect(jsonPath("$[2].id").value(3))
                .andExpect(jsonPath("$[2].name").value("고위험 펀드"))
                .andExpect(jsonPath("$[2].riskLevel").value("HIGH"));

        verify(productService, times(1)).findAll();
    }

    @Test
    @DisplayName("GET /api/products - 빈 제품 목록을 반환한다")
    void list_ReturnsEmptyList() throws Exception {
        // Given
        when(productService.findAll()).thenReturn(Collections.emptyList());

        // When & Then
        mockMvc.perform(get("/api/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());

        verify(productService, times(1)).findAll();
    }

    @Test
    @DisplayName("GET /api/products - 단일 제품만 있는 경우")
    void list_ReturnsSingleProduct() throws Exception {
        // Given
        List<ProductDto> products = Collections.singletonList(
                createProduct(1L, "유일한 상품", "MEDIUM", "FUND", "5.5")
        );

        when(productService.findAll()).thenReturn(products);

        // When & Then
        mockMvc.perform(get("/api/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].name").value("유일한 상품"));

        verify(productService).findAll();
    }

    @Test
    @DisplayName("GET /api/products - 제품의 모든 필드가 올바르게 반환된다")
    void list_ReturnsAllProductFields() throws Exception {
        // Given
        ProductDto product = ProductDto.builder()
                .id(1L)
                .name("테스트 펀드")
                .category("FUND")
                .riskLevel("MEDIUM")
                .interestRate(new BigDecimal("5.75"))
                .minBalance(new BigDecimal("1000000"))
                .description("테스트 펀드 상세 설명")
                .build();

        when(productService.findAll()).thenReturn(Collections.singletonList(product));

        // When & Then
        mockMvc.perform(get("/api/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("테스트 펀드"))
                .andExpect(jsonPath("$[0].category").value("FUND"))
                .andExpect(jsonPath("$[0].riskLevel").value("MEDIUM"))
                .andExpect(jsonPath("$[0].interestRate").value(5.75))
                .andExpect(jsonPath("$[0].minBalance").value(1000000))
                .andExpect(jsonPath("$[0].description").value("테스트 펀드 상세 설명"));

        verify(productService).findAll();
    }

    @Test
    @DisplayName("GET /api/products - 다양한 카테고리의 제품들을 조회한다")
    void list_ReturnsDifferentCategories() throws Exception {
        // Given
        List<ProductDto> products = Arrays.asList(
                createProduct(1L, "정기 예금", "LOW", "DEPOSIT", "3.0"),
                createProduct(2L, "채권형 펀드", "MEDIUM", "FUND", "5.0"),
                createProduct(3L, "주식", "HIGH", "STOCK", "0.0"),
                createProduct(4L, "저축보험", "LOW", "INSURANCE", "3.5")
        );

        when(productService.findAll()).thenReturn(products);

        // When & Then
        mockMvc.perform(get("/api/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(4))
                .andExpect(jsonPath("$[0].category").value("DEPOSIT"))
                .andExpect(jsonPath("$[1].category").value("FUND"))
                .andExpect(jsonPath("$[2].category").value("STOCK"))
                .andExpect(jsonPath("$[3].category").value("INSURANCE"));

        verify(productService).findAll();
    }

    @Test
    @DisplayName("GET /api/products - 여러 번 호출해도 정상 동작한다")
    void list_MultipleRequests_WorksCorrectly() throws Exception {
        // Given
        List<ProductDto> products = Collections.singletonList(
                createProduct(1L, "테스트 상품", "MEDIUM", "FUND", "5.0")
        );

        when(productService.findAll()).thenReturn(products);

        // When & Then - 3번 호출
        for (int i = 0; i < 3; i++) {
            mockMvc.perform(get("/api/products"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.length()").value(1));
        }

        verify(productService, times(3)).findAll();
    }

    @Test
    @DisplayName("GET /api/products - 서비스에서 예외 발생시 전파된다")
    void list_WhenServiceThrowsException_PropagatesException() throws Exception {
        // Given
        when(productService.findAll()).thenThrow(new RuntimeException("데이터베이스 오류"));

        // When & Then
        mockMvc.perform(get("/api/products"))
                .andExpect(status().is5xxServerError());

        verify(productService).findAll();
    }

    @Test
    @DisplayName("GET /api/products - 큰 제품 목록도 처리할 수 있다")
    void list_HandlesLargeProductList() throws Exception {
        // Given - 100개의 제품 생성
        List<ProductDto> largeProductList = Arrays.asList(
                new ProductDto[100]
        );
        for (int i = 0; i < 100; i++) {
            largeProductList.set(i, createProduct(
                    (long) (i + 1),
                    "상품" + (i + 1),
                    i % 3 == 0 ? "LOW" : i % 3 == 1 ? "MEDIUM" : "HIGH",
                    "FUND",
                    String.valueOf(3.0 + i * 0.1)
            ));
        }

        when(productService.findAll()).thenReturn(largeProductList);

        // When & Then
        mockMvc.perform(get("/api/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(100));

        verify(productService).findAll();
    }

    // Helper method
    private ProductDto createProduct(Long id, String name, String riskLevel, 
                                     String category, String interestRate) {
        return ProductDto.builder()
                .id(id)
                .name(name)
                .category(category)
                .riskLevel(riskLevel)
                .interestRate(new BigDecimal(interestRate))
                .minBalance(new BigDecimal("1000000"))
                .description(name + " 설명")
                .build();
    }
}