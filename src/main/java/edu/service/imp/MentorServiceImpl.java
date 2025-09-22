package edu.service.imp;

import lombok.RequiredArgsConstructor;
import edu.model.dto.request.MentorRequest;
import edu.model.dto.response.MentorResponse;
import edu.model.entity.Mentor;
import edu.model.entity.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import edu.repo.MentorRepository;
import edu.repo.UserRepository;
import edu.service.MentorService;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MentorServiceImpl implements MentorService {
    @Autowired
    private final MentorRepository mentorRepository;
    @Autowired
    private final UserRepository userRepository;

    @Override
    public List<MentorResponse> getAllMentors(String role, Long currentUserId) {
        List<Mentor> mentors = mentorRepository.findAll();

        // STUDENT chỉ được xem thông tin chung (không có email)
        if ("STUDENT".equals(role)) {
            return mentors.stream()
                    .map(m -> MentorResponse.builder()
                            .mentorId(m.getMentorId())
                            .fullName(m.getUser().getFullName())
                            .department(m.getDepartment())
                            .academicRank(m.getAcademicRank())
                            .build())
                    .collect(Collectors.toList());
        }

        // ADMIN xem đầy đủ
        return mentors.stream().map(MentorResponse::fromEntity).collect(Collectors.toList());
    }

    @Override
    public MentorResponse getMentorById(Long id, String role, Long currentUserId) {
        Mentor mentor = mentorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Mentor not found"));

        if ("MENTOR".equals(role) && !mentor.getMentorId().equals(currentUserId)) {
            throw new AccessDeniedException("Bạn chỉ có thể xem thông tin của chính mình");
        }

        if ("STUDENT".equals(role)) {
            return MentorResponse.builder()
                    .mentorId(mentor.getMentorId())
                    .fullName(mentor.getUser().getFullName())
                    .department(mentor.getDepartment())
                    .academicRank(mentor.getAcademicRank())
                    .build();
        }

        return MentorResponse.fromEntity(mentor);
    }

    @Override
    public MentorResponse createMentor(MentorRequest request, String role) {
        if (!role.equals("ADMIN")) {
            throw new AccessDeniedException("Bạn không có quyền tạo mentor");
        }

        // Tìm user
        Users user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User không tồn tại"));

        if ((user.getRoles().stream().anyMatch(r -> r.getRoleName().contains("MENTOR") || r.getRoleName().contains("ADMIN")))) {
            throw new RuntimeException("Đối tượng không thể tạo mentor");
        }

        // Tạo mentor mới
        Mentor mentor = Mentor.builder()
                .user(user)
                .department(request.getDepartment())
                .academicRank(request.getAcademicRank())
                .build();

        Mentor saved = mentorRepository.save(mentor);

        return MentorResponse.fromEntity(saved);
    }

    @Override
    public MentorResponse updateMentor(Long id, MentorRequest request, String role, Long currentUserId) {

        Mentor mentor = mentorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Mentor not found"));

        if ("MENTOR".equals(role) && !mentor.getMentorId().equals(currentUserId)) {
            throw new AccessDeniedException("Bạn chỉ có thể cập nhật thông tin của chính mình");
        }

        mentor.setDepartment(request.getDepartment());
        mentor.setAcademicRank(request.getAcademicRank());

        return MentorResponse.fromEntity(mentorRepository.save(mentor));
    }
}
