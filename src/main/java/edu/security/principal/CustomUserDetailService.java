package edu.security.principal;

import edu.model.entity.Role;
import edu.model.entity.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import edu.repo.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CustomUserDetailService implements UserDetailsService {
    @Autowired
    UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Users users = userRepository.findByUsername(username).orElseThrow (()->new UsernameNotFoundException("khong ton tai username"+ username));

     return CustomUserDetails.builder()
             .username(users.getUsername())
             .password(users.getPasswordHash())
             .fullName(users.getFullName())
             .email(users.getEmail())
             .phone(users.getPhoneNumber())
             .status(users.getIsActive())
             .authorities(mapToGrandAuthorities(users.getRoles()))
             .createdAt(users.getCreatedAt())
             .updatedAt(users.getUpdatedAt())
             .build();
    }

    private List<GrantedAuthority> mapToGrandAuthorities(List<Role> roles) {
        return roles.stream().map(role -> new SimpleGrantedAuthority(role.getRoleName())).collect(Collectors.toList());
    }
}
