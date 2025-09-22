package edu.service.imp;

import lombok.RequiredArgsConstructor;
import edu.model.dto.response.StudentResponse;
import edu.model.entity.RoleType;
import edu.model.entity.Student;
import edu.model.entity.Users;

import org.springframework.beans.factory.annotation.Autowired;
import edu.repo.StudentRepository;
import edu.repo.UserRepository;
import edu.service.StudentService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService {
@Autowired
     StudentRepository studentRepository;
@Autowired
     UserRepository userRepository;

    @Override
    public List<StudentResponse> getAllStudents(Long mentorId, String role) {
        // nên mình giữ tạm để khớp interface: chỉ ADMIN mới có thể xem toàn bộ
        if (!RoleType.ADMIN.name().equals(role)) {
            throw new RuntimeException("Bạn không có quyền xem danh sách sinh viên.");
        }
        return studentRepository.findAll()
                .stream()
                .map(StudentResponse::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public StudentResponse getStudentById(Long id, String role, Long currentUserId) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Không tìm thấy sinh viên với ID: " + id));

        if (RoleType.STUDENT.name().equals(role) && !student.getStudentId().equals(currentUserId)) {
            throw new RuntimeException("Bạn chỉ được xem thông tin của chính mình.");
        }

        // ADMIN và MENTOR được phép xem
        return StudentResponse.fromEntity(student);
    }

    @Override
    public StudentResponse createStudent(Student student) {
        // check user tồn tại và có role STUDENT
        Long userId = student.getStudentId();
        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy user với ID: " + userId));

        if (user.getRoles().stream().noneMatch(r -> r.getRoleName().equals(RoleType.STUDENT.name()))) {
            throw new RuntimeException("User không có role STUDENT để liên kết.");
        }

        // gắn lại User cho Student
        student.setUser(user);

        Student saved = studentRepository.save(student);
        return StudentResponse.fromEntity(saved);
    }

    @Override
    public StudentResponse updateStudent(Long id, Student student, String role, Long currentUserId) {
        Student existing = studentRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Không tìm thấy sinh viên với ID: " + id));

        // Nếu là STUDENT thì chỉ được cập nhật của chính mình
        if (RoleType.STUDENT.name().equals(role) && !existing.getStudentId().equals(currentUserId)) {
            throw new RuntimeException("Bạn chỉ được cập nhật thông tin của chính mình.");
        }

        // ADMIN thì có thể cập nhật tất cả
        existing.setStudentCode(student.getStudentCode());
        existing.setMajor(student.getMajor());
        existing.setClassName(student.getClassName());
        existing.setDateOfBirth(student.getDateOfBirth());
        existing.setAddress(student.getAddress());

        Student updated = studentRepository.save(existing);
        return StudentResponse.fromEntity(updated);
    }
}
