package edu.service;

import edu.model.dto.request.MentorRequest;
import edu.model.dto.response.MentorResponse;

import java.util.List;

public interface MentorService {
    List<MentorResponse> getAllMentors(String role, Long currentUserId);
    MentorResponse getMentorById(Long id, String role, Long currentUserId);
    MentorResponse createMentor(MentorRequest request, String role);
    MentorResponse updateMentor(Long id, MentorRequest request, String role, Long currentUserId);
}
