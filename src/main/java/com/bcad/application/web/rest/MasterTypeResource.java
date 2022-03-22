package com.bcad.application.web.rest;

import com.bcad.application.domain.MasterType;
import com.bcad.application.repository.MasterTypeRepository;
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

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/api")
public class MasterTypeResource {

    private final Logger log = LoggerFactory.getLogger(MasterTypeResource.class);

    private final MasterTypeRepository masterTypeRepository;

    public MasterTypeResource(MasterTypeRepository masterTypeRepository) {
        this.masterTypeRepository= masterTypeRepository;
    }

    @PostMapping("/master-type")
    @Timed
    public ResponseEntity<MasterType> createMasterType(@RequestBody MasterType masterType) throws URISyntaxException {
        log.debug("REST request to save MasterType : {}", masterType);

        MasterType result = masterTypeRepository.save(masterType);

        return ResponseEntity.created(new URI("/api/master-type/" + result.getId().toString()))
            .headers(HeaderUtil.createAlert( "A MasterType is created with identifier " + result.getId().toString(), result.getId().toString()))
            .body(result);
    }

    @GetMapping("/master-types")
    @Timed
    public ResponseEntity<List<MasterType>> getAllMasterType(Pageable pageable) {
        final Page<MasterType> page = masterTypeRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/mastertype");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    @GetMapping("/master-type/{id}")
    @Timed
    public Optional<MasterType> getLocation(@PathVariable Long id){
        Optional<MasterType> result = masterTypeRepository.findById(id);
        return result;

    }
}
