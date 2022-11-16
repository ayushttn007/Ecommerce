package com.Ecomm.Ecommerce.repos;

import com.Ecomm.Ecommerce.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepo extends JpaRepository<Role,Long> {

public Role findByAuthority(String authority);


}