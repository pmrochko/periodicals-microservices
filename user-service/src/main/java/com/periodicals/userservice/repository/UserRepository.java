package com.periodicals.userservice.repository;

import com.periodicals.userservice.model.entity.User;
import com.periodicals.userservice.model.enums.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String username);
    boolean existsByEmail(String email);
    boolean existsByPhone(String phoneNumber);

    @Transactional
    @Modifying
    @Query("update User u set u.email = ?1, " +
                             "u.userRole = ?2, " +
                             "u.name = ?3, " +
                             "u.surname = ?4, " +
                             "u.phone = ?5, " +
                             "u.address = ?6, " +
                             "u.access = ?7" +
            " where u.id = ?8")
    void updateUser(String email,
                    UserRole userRole,
                    String name,
                    String surname,
                    String phone,
                    String address,
                    Boolean access,
                    Long id);

}
