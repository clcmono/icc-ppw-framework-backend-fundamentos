package ec.edu.ups.icc.fundamentos01.users.repository;

import ec.edu.ups.icc.fundamentos01.users.models.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    Optional<UserEntity> findById(Long id);

    Optional<UserEntity> findByEmail(String email);

    boolean existsByEmail(String email);
}