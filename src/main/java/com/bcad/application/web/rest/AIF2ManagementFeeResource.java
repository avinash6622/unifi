package com.bcad.application.web.rest;

import com.bcad.application.domain.AIF2ManagementFee;
import com.bcad.application.repository.AIF2ManagementFeeRepository;
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
public class AIF2ManagementFeeResource {

    private final Logger log = LoggerFactory.getLogger(AIF2ManagementFeeResource.class);

    private final AIF2ManagementFeeRepository aif2ManagementFeeRepository;

    public AIF2ManagementFeeResource(AIF2ManagementFeeRepository aif2ManagementFeeRepository) {
        this.aif2ManagementFeeRepository = aif2ManagementFeeRepository;
    }

    @PostMapping("/aif2-management")
    @Timed
    public ResponseEntity<AIF2ManagementFee> create(@RequestBody AIF2ManagementFee aif2ManagementFee) throws URISyntaxException {
        log.debug("REST request to save AIF2ManagementFee : {}", aif2ManagementFee);

        AIF2ManagementFee result = aif2ManagementFeeRepository.save(aif2ManagementFee);

        return (ResponseEntity<AIF2ManagementFee>) ResponseEntity.created(new URI("/api/aif2-management/" + result.getId().toString()))
            .headers(HeaderUtil.createAlert( "A aif2ManagementFee is created with identifier " + result.getId().toString(), result.getId().toString()))
            .body(result);

    }
    @GetMapping("/aif2-management/{id}")
    @Timed
    public Optional<AIF2ManagementFee> getaif2ManagementFee(@PathVariable Long id){
        Optional<AIF2ManagementFee> result = aif2ManagementFeeRepository.findById(id);
        return result;

    }
    @PutMapping("/aif2-management")
    @Timed
    public ResponseEntity<AIF2ManagementFee> updateaif2ManagementFee(@RequestBody AIF2ManagementFee aif2ManagementFee)
        throws URISyntaxException {

        AIF2ManagementFee result = aif2ManagementFeeRepository.save(aif2ManagementFee);

        return (ResponseEntity<AIF2ManagementFee>) ResponseEntity.created(new URI("/api/aif2-management/" + result.getId().toString()))
            .headers(HeaderUtil.createAlert( "A aif2ManagementFee is created with identifier " + result.getId().toString(), result.getId().toString()))
            .body(result);

    }

    @DeleteMapping("/aif2-management/{id}")
    @Timed
    public ResponseEntity<AIF2ManagementFee> deleteaif2ManagementFee(@PathVariable Long id) {
        log.debug("REST request to delete AIF2ManagementFee: {}", id);
        Optional<AIF2ManagementFee> result= aif2ManagementFeeRepository.findById(id);
        aif2ManagementFeeRepository.deleteById(id);
        return ResponseEntity.ok().headers(HeaderUtil.createAlert( "A aif2ManagementFee is deleted with identifier " + id,id.toString())).build();
    }

    @GetMapping("/aif2-managements")
    @Timed
    public ResponseEntity<List<AIF2ManagementFee>> getAllaif2ManagementFees(Pageable pageable) {
        final Page<AIF2ManagementFee> page = aif2ManagementFeeRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/aif2-managements");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }


}
