package edu.controller;

import lombok.RequiredArgsConstructor;
import edu.model.dto.request.StudentRequest;
import edu.model.dto.response.ApiDataResponse;
import edu.model.dto.response.StudentResponse;
import edu.model.entity.Users;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import edu.repo.UserRepository;
import edu.service.StudentService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/students")
@RequiredArgsConstructor
public class StudentController {

    private final StudentService studentService;
    private final UserRepository userRepository;

    // 10 - ADMIN, MENTOR
    @GetMapping
    public ResponseEntity<ApiDataResponse<List<StudentResponse>>> getAllStudents() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Users currentUser = getCurrentUser(auth);

        String role = currentUser != null ? extractPrimaryRole(currentUser) : null;
        Long currentUserId = currentUser != null ? currentUser.getUserId() : null;

        List<StudentResponse> data = studentService.getAllStudents(currentUserId, role);
        return ResponseEntity.ok(new ApiDataResponse<>(true, "Lấy danh sách sinh viên thành công", data, null, HttpStatus.OK));
    }

    // 11 - ADMIN, MENTOR, STUDENT
    @GetMapping("/{student_id}")
    public ResponseEntity<ApiDataResponse<StudentResponse>> getStudentById(@PathVariable("student_id") Long studentId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Users currentUser = getCurrentUser(auth);

        String role = currentUser != null ? extractPrimaryRole(currentUser) : null;
        Long currentUserId = currentUser != null ? currentUser.getUserId() : null;

        StudentResponse resp = studentService.getStudentById(studentId, role, currentUserId);
        return ResponseEntity.ok(new ApiDataResponse<>(true, "Lấy thông tin sinh viên thành công", resp, null, HttpStatus.OK));
    }

    // 12 - ADMIN (securityFilterChain bảo đảm), nhưng ta vẫn validate userId trong body
    @PostMapping
    public ResponseEntity<ApiDataResponse<StudentResponse>> createStudent(@RequestBody StudentRequest request) {
        // request phải chứa userId liên kết với Users đã tồn tại
        Long userId = request.getUserId();
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiDataResponse<>(false, "userId là bắt buộc để liên kết Student với User", null, "MISSING_USER_ID", HttpStatus.BAD_REQUEST));
        }

        Optional<Users> maybeUser = userRepository.findById(userId);
        if (maybeUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiDataResponse<>(false, "User không tồn tại", null, "USER_NOT_FOUND", HttpStatus.BAD_REQUEST));
        }

        Users user = maybeUser.get();
        // kiểm tra user có role STUDENT
        boolean isStudentRole = user.getRoles().stream()
                .anyMatch(r -> "STUDENT".equalsIgnoreCase(r.getRoleName()));
        if (!isStudentRole) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiDataResponse<>(false, "User không có role STUDENT", null, "INVALID_ROLE", HttpStatus.BAD_REQUEST));
        }

        // chuyển DTO -> entity với user đã load
        StudentResponse created;
        try {
            created = studentService.createStudent(request.toEntity(user));
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiDataResponse<>(false, "Tạo Student thất bại: " + ex.getMessage(), null, "CREATE_FAIL", HttpStatus.INTERNAL_SERVER_ERROR));
        }

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiDataResponse<>(true, "Tạo sinh viên thành công", created, null, HttpStatus.CREATED));
    }

    // 13 - ADMIN, STUDENT (STUDENT chỉ update của mình)
    @PutMapping("/{student_id}")
    public ResponseEntity<ApiDataResponse<StudentResponse>> updateStudent(
            @PathVariable("student_id") Long studentId,
            @RequestBody StudentRequest request) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Users currentUser = getCurrentUser(auth);
        String role = currentUser != null ? extractPrimaryRole(currentUser) : null;
        Long currentUserId = currentUser != null ? currentUser.getUserId() : null;

        // Khi update, cần Users liên kết cho toEntity(); StudentId == userId (MapsId)
        // Lấy user liên kết theo studentId
        Optional<Users> maybeUser = userRepository.findById(studentId);
        if (maybeUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiDataResponse<>(false, "User liên kết Student không tồn tại", null, "USER_NOT_FOUND", HttpStatus.NOT_FOUND));
        }
        Users linkedUser = maybeUser.get();

        // build Student entity from DTO + linkedUser
        try {
            // đảm bảo toEntity không override studentId; service sẽ dùng id param để tìm và update
            var entity = request.toEntity(linkedUser);
            // gọi service (service kiểm quyền: STUDENT chỉ update của mình)
            StudentResponse updated = studentService.updateStudent(studentId, entity, role, currentUserId);
            return ResponseEntity.ok(new ApiDataResponse<>(true, "Cập nhật thành công", updated, null, HttpStatus.OK));
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new ApiDataResponse<>(false, "Không có quyền hoặc lỗi: " + ex.getMessage(), null, "FORBIDDEN", HttpStatus.FORBIDDEN));
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiDataResponse<>(false, "Cập nhật thất bại: " + ex.getMessage(), null, "UPDATE_FAIL", HttpStatus.INTERNAL_SERVER_ERROR));
        }
    }

    // --- helper ---
    private Users getCurrentUser(Authentication auth) {
        if (auth == null) return null;
        String username = auth.getName();
        return userRepository.findByUsername(username).orElse(null);
    }

    private String extractPrimaryRole(Users user) {
        if (user == null || user.getRoles() == null || user.getRoles().isEmpty()) return null;
        return user.getRoles().get(0).getRoleName(); // nếu cần priority, thay đổi ở đây
    }
}
