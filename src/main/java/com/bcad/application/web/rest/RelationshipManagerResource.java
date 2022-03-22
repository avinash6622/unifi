package com.bcad.application.web.rest;

import com.bcad.application.domain.RelationshipManager;
import com.bcad.application.repository.RelationshipManagerRepository;
import com.bcad.application.web.rest.util.HeaderUtil;
import com.bcad.application.web.rest.util.PaginationUtil;
import com.codahale.metrics.annotation.Timed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class RelationshipManagerResource {
    private final Logger log = LoggerFactory.getLogger(RelationshipManagerResource.class);

    private final RelationshipManagerRepository relationshipManagerRepository;

    public RelationshipManagerResource(RelationshipManagerRepository relationshipManagerRepository) {
        this.relationshipManagerRepository = relationshipManagerRepository;
    }

    @PostMapping("/rm")
    @Timed
    public ResponseEntity<RelationshipManager> createRM(@RequestBody RelationshipManager relationshipManager) throws URISyntaxException {
        log.debug("REST request to save RelationshipManager : {}", relationshipManager);

        RelationshipManager result = relationshipManagerRepository.save(relationshipManager);

        return ResponseEntity.created(new URI("/api/rm/" + result.getId().toString()))
            .headers(HeaderUtil.createAlert("A RelationshipManager is created with identifier " + result.getId().toString(), result.getId().toString()))
            .body(result);

    }

    @PutMapping("/rm")
    @Timed
    public ResponseEntity<RelationshipManager> updateRelation(@RequestBody RelationshipManager relationshipManager) throws URISyntaxException {
        log.debug("REST request to update User : {}", relationshipManager);

        RelationshipManager updateRelationManager = relationshipManagerRepository.save(relationshipManager);

        return ResponseEntity.created(new URI("/api/rm/" + updateRelationManager.getId().toString()))
            .headers(HeaderUtil.createAlert("A Distributor is updated with identifier" + updateRelationManager.getId().toString(), updateRelationManager.getId().toString()))
            .body(updateRelationManager);
    }

    @GetMapping("/rm/{id}")
    @Timed
    @Transactional
    public Optional<RelationshipManager> getRM(@PathVariable Long id) {
        log.debug("REST request to get User : {}", id);
        Optional<RelationshipManager> result = relationshipManagerRepository.findById(id);
        return result;
    }

    @GetMapping("/rms")
    @Timed
    public ResponseEntity<List<RelationshipManager>> getAllRelations(Pageable pageable) {
        final Page<RelationshipManager> page = relationshipManagerRepository.findAllByOrderByIdAsc(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/rms");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    @PostMapping("/rm-search")
    @Timed
    public List<RelationshipManager> postRM(@RequestBody RelationshipManager relationshipManager) {
    List<RelationshipManager> result=relationshipManagerRepository.findByRmName(relationshipManager.getRmName());
        return result;
    
    } 

    @DeleteMapping("/rm/{id}")
    @Timed
    public ResponseEntity<RelationshipManager> deleteRelationshipManager(@PathVariable Long id) {
        log.debug("REST request to delete RelationshipManager: {}", id);
        Optional<RelationshipManager> result=relationshipManagerRepository.findById(id);
        relationshipManagerRepository.deleteById(id);
        return ResponseEntity.ok().headers(HeaderUtil.createAlert( "A user is deleted with identifier " + id,id.toString())).build();
    }
}
