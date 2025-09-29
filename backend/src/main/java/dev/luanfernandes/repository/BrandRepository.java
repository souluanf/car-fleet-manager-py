package dev.luanfernandes.repository;

import dev.luanfernandes.domain.entity.Brand;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface BrandRepository extends JpaRepository<Brand, UUID> {
    boolean existsByName(String brandName);

    @Query("SELECT b FROM Brand b WHERE LOWER(b.name) = LOWER(:brandName)")
    Optional<Brand> findByNameIgnoreCase(String brandName);
}
