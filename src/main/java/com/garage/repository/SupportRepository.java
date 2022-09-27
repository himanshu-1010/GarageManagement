package com.garage.repository;
import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.garage.entity.Support;
@Repository
public interface SupportRepository extends CrudRepository<Support, Integer> {
 List<Support> findByCustomerId(int customerId);
 List<Support> findByActive(byte active);
 Support findByIdAndActive(int id,byte active);
}
