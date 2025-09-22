package edu.controller;

import lombok.RequiredArgsConstructor;
import edu.model.dto.request.MentorRequest;
import edu.model.dto.response.ApiDataResponse;
import edu.model.dto.response.MentorResponse;
import edu.model.entity.Users;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import edu.service.MentorService;

import java.util.List;

@RestController
@RequestMapping("/api/mentors")
@RequiredArgsConstructor
public class MentorController {

    private final MentorService mentorService;

    // Lấy danh sách tất cả mentors
    @GetMapping
    public ResponseEntity<ApiDataResponse<List<MentorResponse>>> getAllMentors() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Users user = (Users) authentication.getPrincipal();
        String role = authentication.getAuthorities().iterator().next().getAuthority();
        Long currentUserId = user.getUserId();

        return ResponseEntity.ok(
                new ApiDataResponse<>(
                        true,
                        "Danh sách mentors",
                        mentorService.getAllMentors(role, currentUserId),
                        null,
                        null
                )
        );
    }

    // Lấy mentor theo ID
    @GetMapping("/{mentor_id}")
    public ResponseEntity<ApiDataResponse<MentorResponse>> getMentorById(@PathVariable("mentor_id") Long mentorId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Users user = (Users) authentication.getPrincipal();
        String role = authentication.getAuthorities().iterator().next().getAuthority();
        Long currentUserId = user.getUserId();

        return ResponseEntity.ok(
                new ApiDataResponse<>(
                        true,
                        "Thông tin mentor",
                        mentorService.getMentorById(mentorId, role, currentUserId),
                        null,
                        null
                )
        );
    }

    // Tạo mentor mới (ADMIN)
    @PostMapping
    public ResponseEntity<ApiDataResponse<MentorResponse>> createMentor(@RequestBody MentorRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String role = authentication.getAuthorities().iterator().next().getAuthority();

        return ResponseEntity.ok(
                new ApiDataResponse<>(
                        true,
                        "Tạo mentor thành công",
                        mentorService.createMentor(request, role),
                        null,
                        null
                )
        );
    }


    // Cập nhật mentor (ADMIN hoặc chính mentor đó)
    @PutMapping("/{mentor_id}")
    public ResponseEntity<ApiDataResponse<MentorResponse>> updateMentor(
            @PathVariable("mentor_id") Long mentorId,
            @RequestBody MentorRequest request) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String role = authentication.getAuthorities().iterator().next().getAuthority();
        Long currentUserId = ((Users) authentication.getPrincipal()).getUserId();

        return ResponseEntity.ok(
                new ApiDataResponse<>(
                        true,
                        "Cập nhật mentor thành công",
                        mentorService.updateMentor(mentorId, request, role, currentUserId),
                        null,
                        null
                )
        );
    }
}
