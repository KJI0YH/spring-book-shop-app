package com.example.mybookshopapp.services;

import com.example.mybookshopapp.data.Role2UserEntity;
import com.example.mybookshopapp.data.RoleEntity;
import com.example.mybookshopapp.data.UserEntity;
import com.example.mybookshopapp.errors.RoleDoesNotExistsException;
import com.example.mybookshopapp.repositories.Role2UserRepository;
import com.example.mybookshopapp.repositories.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class RoleService {

    private final RoleRepository roleRepository;
    private final Role2UserRepository role2UserRepository;

    public RoleEntity getRoleByName(String name) {
        return roleRepository.findRoleEntityByNameIgnoreCase(name);
    }

    public int getNumberOfUsersByRole(RoleEntity role) {
        return role2UserRepository.countAllByRoleId(role.getId());
    }

    public Role2UserEntity addRoleToUser(String roleName, UserEntity user) throws RoleDoesNotExistsException {
        RoleEntity role = getRoleByName(roleName);
        if (role == null)
            throw new RoleDoesNotExistsException("Role with name " + roleName + " does not exists");
        Role2UserEntity role2user = new Role2UserEntity();
        role2user.setRole(role);
        role2user.setUser(user);
        return role2UserRepository.save(role2user);
    }
}
