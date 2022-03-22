package com.bcad.application.repository;

import java.util.List;

import com.bcad.application.domain.DistributorOption;
import com.bcad.application.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DistributorOptionRepository extends JpaRepository<DistributorOption, Long> {
List<DistributorOption> findByProduct(Product product);
DistributorOption findByOptionName(String optionName);
}
