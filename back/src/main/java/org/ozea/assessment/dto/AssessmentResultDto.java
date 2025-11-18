package org.ozea.assessment.dto;

import jakarta.persistence.*;
import lombok.*;
import org.ozea.user.domain.User;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "assessment_results")
public class AssessmentResultDto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false, length = 30)
    private String type;

    @Column(nullable = false, length = 30)
    private String resultCode;

    @Column(nullable = false, length = 30)
    private Integer score;

    @Column(nullable = false)
    private LocalDateTime createdAt;
}