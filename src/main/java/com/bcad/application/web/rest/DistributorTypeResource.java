package com.bcad.application.web.rest;

import com.bcad.application.domain.DistributorType;
import com.bcad.application.domain.DistributorType;
import com.bcad.application.repository.DistributorTypeRepository;
import com.bcad.application.repository.DistributorTypeRepository;
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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class DistributorTypeResource {
    private final Logger log = LoggerFactory.getLogger(DistributorTypeResource.class);

    private final DistributorTypeRepository distributorTypeRepository;

    public DistributorTypeResource(DistributorTypeRepository distributorTypeRepository) {
        this.distributorTypeRepository = distributorTypeRepository;
    }

    @PostMapping("/distributor-type")
    @Timed
    public ResponseEntity<DistributorType> createDistributorType(@RequestBody DistributorType distributorType) throws URISyntaxException {
        log.debug("REST request to save DistributorType : {}", distributorType);

        DistributorType result = distributorTypeRepository.save(distributorType);

        return ResponseEntity.created(new URI("/api/distributor-type/" + result.getId().toString()))
            .headers(HeaderUtil.createAlert( "A Distributor Type is created with identifier " + result.getId().toString(), result.getId().toString()))
            .body(result);

    }

    @GetMapping("/distributor-type/{id}")
    @Timed
    public Optional<DistributorType> getDistributorType(@PathVariable Long id){
        Optional<DistributorType> result = distributorTypeRepository.findById(id);
        return result;
    
    }
    @PutMapping("/distributor-type")
    @Timed
    public ResponseEntity<DistributorType> updateDistributorType(@RequestBody DistributorType distributorType)
            throws URISyntaxException {

                DistributorType result = distributorTypeRepository.save(distributorType);

                return ResponseEntity.created(new URI("/api/distributor-type/" + result.getId().toString()))
                .headers(HeaderUtil.createAlert( "A DistributorType is created with identifier " + result.getId().toString(), result.getId().toString()))
                .body(result);
    }
    @DeleteMapping("/distributor-type/{id}")
    @Timed
    public ResponseEntity<DistributorType> deleteDistributorType(@PathVariable Long id) {
    log.debug("REST request to delete DistributorMaster: {}", id);
     Optional<DistributorType> result=distributorTypeRepository.findById(id);
     distributorTypeRepository.deleteById(id);
    return ResponseEntity.ok().headers(HeaderUtil.createAlert( "A user is deleted with identifier " + id,id.toString())).build();
                }

    @GetMapping("/distributor-types")
    @Timed
    public ResponseEntity<List<DistributorType>> getAllDistributorType(Pageable pageable) {
    final Page<DistributorType> page = distributorTypeRepository.findAll(pageable);
                    HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/distributor-type/");
                    return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
                }

     @PostMapping("/Dist-type-search")
     @Timed
     public DistributorType postAIF(@RequestBody DistributorType distributorType) {
        DistributorType result = distributorTypeRepository.findByDistTypeName(distributorType.getDistTypeName());
     return result;
                
      }        
 }


