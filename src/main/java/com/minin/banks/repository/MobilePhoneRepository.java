package com.minin.banks.repository;

import com.minin.banks.models.MobilePhone;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface MobilePhoneRepository extends JpaRepository<MobilePhone, UUID> {

    Optional<MobilePhone> findByMobilePhone(String mobilePhone);

    boolean existsByMobilePhone(String mobilePhone);

}
