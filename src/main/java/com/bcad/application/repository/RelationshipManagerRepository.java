package com.bcad.application.repository;

import com.bcad.application.domain.RelationshipManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import javax.management.relation.Relation;

@Repository
public interface RelationshipManagerRepository extends JpaRepository<RelationshipManager,Long> {

    String RELATIONS_ID = "relationsById";

    @EntityGraph(attributePaths = "subRMS")
    Optional<RelationshipManager> findById(Long id);

    @EntityGraph(attributePaths = "subRMS")
    Page<RelationshipManager> findAllByOrderByIdAsc(Pageable pageable);

    List<RelationshipManager> findByRmName(String rmName);

    @Query(value="select * from rm_master where rm_name=:rmName",nativeQuery = true)
    RelationshipManager findByRelationName(@Param("rmName") String rmName);

}
