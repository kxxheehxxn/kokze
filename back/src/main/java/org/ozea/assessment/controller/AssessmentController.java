package org.ozea.assessment.controller;

import lombok.RequiredArgsConstructor;
import org.ozea.assessment.dto.AssessmentAnswerDto;
import org.ozea.assessment.dto.AssessmentResultDto;
import org.ozea.assessment.service.AssessmentService;
import org.ozea.user.domain.User;
import org.ozea.user.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/assessment")
@RequiredArgsConstructor
public class AssessmentController {

    private final AssessmentService assessmentService;
    private final UserRepository userRepository;

    @PostMapping("/submit")
    public AssessmentResultDto submit(@RequestBody AssessmentAnswerDto dto) {
        User user = getCurrentUser();

        return assessmentService.evaluate(dto, user);
    }

    @GetMapping("/me")
    public AssessmentResultDto myResult() {
        User user = getCurrentUser();

        return assessmentService.getLatestResultFor(user);
    }

    private User getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if(auth == null || !auth.isAuthenticated() || "anonymousUser".equals(auth.getPrincipal())){
            throw new IllegalArgumentException("로그인이 필요합니다");
        }

        String email = (String) auth.getPrincipal();

        return userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalStateException("유저를 찾을 수 없습니다."));
    }
}