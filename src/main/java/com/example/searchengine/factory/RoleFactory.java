package com.example.searchengine.factory;

import com.example.searchengine.entity.Role;
import com.example.searchengine.exception.RoleNotFoundException;
import com.example.searchengine.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class RoleFactory {
    @Autowired
    private RoleRepository roleRepository;
    private RoleFactory() {
    }
  
    public Role getAdminRole() throws RoleNotFoundException{
        Optional<Role> oRole = roleRepository.findByName("admin");
        if(oRole.isPresent()){
            return oRole.get();
        }
        throw new RoleNotFoundException("Admin role not found");
    }

    public Role getUserRole() throws RoleNotFoundException{
        Optional<Role> oRole = roleRepository.findByName("user");
        if(oRole.isPresent()){
            return oRole.get();
        }
        throw new RoleNotFoundException("User role not found");
    }
}