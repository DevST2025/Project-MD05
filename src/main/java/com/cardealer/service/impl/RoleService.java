package com.cardealer.service.impl;

import com.cardealer.model.entity.Role;
import com.cardealer.repository.IRoleRepository;
import com.cardealer.service.IRoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoleService implements IRoleService {
    private final IRoleRepository roleRepository;
    @Override
    public List<Role> findAll() {
        return roleRepository.findAll();
    }
}
