package com.bcad.application.repository;

import com.bcad.application.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product,Long> {

    Product findByProductName(String product);

    @Query(value = "FROM Product where productName in(?1)")
    List<Product> getAIF2andAIF_Blend(List<String> productName);
}
