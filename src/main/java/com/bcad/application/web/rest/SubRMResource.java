package com.bcad.application.web.rest;
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
import org.springframework.web.bind.annotation.*;

import com.bcad.application.domain.SubRM;
import com.bcad.application.repository.SubRMRepository;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class SubRMResource {

    private final Logger log = LoggerFactory.getLogger(SubRMResource.class);

    private final SubRMRepository subRMRepository;

    public SubRMResource(SubRMRepository subRMRepository) {
        this.subRMRepository = subRMRepository;
    }

    @PostMapping("/sub-rm")
    @Timed
    public ResponseEntity<SubRM> createSubRM(@RequestBody SubRM subRM) throws URISyntaxException {
        log.debug("REST request to save User : {}", subRM);

        SubRM result = subRMRepository.save(subRM);

        return ResponseEntity.created(new URI("/api/sub-rm/" + result.getId().toString()))
            .headers(HeaderUtil.createAlert( "A SubRM is created with identifier " + result.getId().toString(), result.getId().toString()))
            .body(result);

    }

    // @GetMapping("/sub-rm")
    // @Timed
    // public List<SubRM> getAllUsers() {
    //     List<SubRM> subrmList = subRMRepository.findAll();
    //     return subrmList;
    // }

    @GetMapping("/sub-rm")
    @Timed
    public ResponseEntity<List<SubRM>> getAllSubRM(Pageable pageable) {
        final Page<SubRM> page = subRMRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/subrms");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }
    @GetMapping("/sub-rm/{id}")
    @Timed
    public Optional<SubRM> getSubRM(@PathVariable Long id){
        Optional<SubRM> result = subRMRepository.findById(id);
        return result;

    }
    @PutMapping("/sub-rm")
    @Timed
    public ResponseEntity<SubRM> updateSubRM(@RequestBody SubRM SubRM)
            throws URISyntaxException {

                SubRM result = subRMRepository.save(SubRM);

                return ResponseEntity.created(new URI("/api/rm/" + result.getId().toString()))
                .headers(HeaderUtil.createAlert( "A Sub RelationshipManager is created with identifier " + result.getId().toString(), result.getId().toString()))
                .body(result);
    }
    @DeleteMapping("/sub-rm/{id}")
    @Timed
    public ResponseEntity<SubRM> deleteSubRM(@PathVariable Long id) {
        log.debug("REST request to delete SubRM: {}", id);
        Optional<SubRM> result = subRMRepository.findById(id);
         subRMRepository.deleteById(id);
        return ResponseEntity.ok().headers(HeaderUtil.createAlert("A SubRM is deleted with identifier " + id, id.toString())).build();

    }

    @PostMapping("/sub-rm-search")
    @Timed
    public List<SubRM> postSubRM(@RequestBody SubRM subRM) {
    List<SubRM> result=subRMRepository.findBySubName(subRM.getSubName());
        return result;
    
    } 
}
