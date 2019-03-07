package com.restaurant.management.repository;

import com.restaurant.management.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    Optional<Product> findProductById(Long id);

    Optional<Product> findProductsByUniqueId(String uniqueId);

    Optional<Product> findProductByUniqueId(String id);

    boolean existsByName(String name);

    boolean existsByUniqueId(String uniqueId);

    Optional<Product> findProductByName(String name);

    List<Product> findProductsByName(String name);

    List<Product> findProductsByName(List<String> name);

    List<Product> findAllByIsArchivedIsFalse();

    List<Product> findAllByIsArchivedIsTrue();

}
