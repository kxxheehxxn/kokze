package org.ozea.recommend.dto;

import lombok.Data;

import java.util.List;

@Data
public class RecommendResponseDto {
    private List<String> productIds;
}