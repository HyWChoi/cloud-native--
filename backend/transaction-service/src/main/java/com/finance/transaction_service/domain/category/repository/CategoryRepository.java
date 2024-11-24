package com.finance.transaction_service.domain.category.repository;

import com.finance.transaction_service.domain.category.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    List<Category> findByProfileIdAndDeletedAtIsNull(Long profileId);

   Optional<Category> findByIdAndProfileId(Long categoryId, Long profileId);
}
