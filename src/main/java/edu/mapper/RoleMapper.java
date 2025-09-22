package edu.mapper;

import edu.model.entity.Role;
import edu.model.entity.RoleType;
import edu.repo.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class RoleMapper {
    private final RoleRepository roleRepository;

    public Set<Role> mapToSetRole(Set<String> roles) {
        Set<Role> roleSet = new HashSet<>();
        if (roles == null || roles.isEmpty()) {
            roleSet.add(roleRepository.findByRoleName(String.valueOf(RoleType.STUDENT))
                    .orElseThrow(() -> new NoSuchElementException("Không tồn tại role STUDENT")));
        } else {
            roles.forEach(role -> {
                roleSet.add(roleRepository.findByRoleName(String.valueOf(RoleType.valueOf(role.toUpperCase())))
                        .orElseThrow(() -> new NoSuchElementException("Không tồn tại role :" + role)));
            });
        }
        return roleSet;
    }
}