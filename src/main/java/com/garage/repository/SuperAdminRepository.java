package com.garage.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.garage.entity.SuperAdmin;

import net.bytebuddy.dynamic.DynamicType.Builder.FieldDefinition.Optional;
@Repository
public interface SuperAdminRepository extends CrudRepository<SuperAdmin, Integer> {
SuperAdmin findByIdAndActive(int id,byte active);
java.util.Optional<SuperAdmin> findByEmailAndActive(String email,byte active);
SuperAdmin findByEmailAndRole(String email,String role);
SuperAdmin findByEmail(String email);
}
