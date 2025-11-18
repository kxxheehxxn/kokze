package org.ozea.assessment.repository;

import org.ozea.assessment.dto.AssessmentResultDto;
import org.ozea.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AssessmentRepository extends JpaRepository<AssessmentResultDto, Long> {
    Optional<AssessmentResultDto> findTopByUserAndTypeOrderByCreatedAtDesc(User user, String type);

}
