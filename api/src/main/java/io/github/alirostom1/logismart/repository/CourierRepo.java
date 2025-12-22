package io.github.alirostom1.logismart.repository;

import io.github.alirostom1.logismart.model.entity.Courier;
import io.github.alirostom1.logismart.model.entity.Zone;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourierRepo extends JpaRepository<Courier,Long>{
    boolean existsByPhone(String phone);
    boolean existsByEmail(String email);
    boolean existsByPhoneAndIdNot(String phone,Long id);
    boolean existsByEmailAndIdNot(String email,Long id);



    Page<Courier> findCouriersByZone(Zone zone, Pageable pageable);
}
