package edu.service;

import edu.model.dto.response.StudentResponse;
import edu.model.entity.Student;

import java.util.List;

public interface StudentService {
    List<StudentResponse> getAllStudents(Long mentorId, String role);
    StudentResponse getStudentById(Long id, String role, Long currentUserId);
    StudentResponse createStudent(Student student);
    StudentResponse updateStudent(Long id, Student student, String role, Long currentUserId);
}
