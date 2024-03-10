package com.minin.banks.repository;

import com.minin.banks.models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

    Optional<User> findByUsername(String username);

    Page<User> findAllByBirthDateAfter(Date birthDate, Pageable pageable);

    Page<User> findAllBySurnameLike(String like, Pageable pageable);

    Page<User> findAllBySurnameLikeAndNameLike(String like1, String like2, Pageable pageable);

    Page<User> findAllBySurnameLikeAndNameLikeAndPatronymicLike(String like1, String like2, String like3, Pageable pageable);

    boolean existsByUsername(String username);

}
