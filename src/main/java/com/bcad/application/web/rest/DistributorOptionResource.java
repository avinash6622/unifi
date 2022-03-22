package com.bcad.application.web.rest;

import com.bcad.application.domain.DistributorOption;
import com.bcad.application.repository.DistributorOptionRepository;
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
public class DistributorOptionResource {

    private final Logger log = LoggerFactory.getLogger(DistributorOption.class);

    private final DistributorOptionRepository distributorOptionRepository;

    public DistributorOptionResource(DistributorOptionRepository distributorOptionRepository) {
        this.distributorOptionRepository = distributorOptionRepository;
    }

    @PostMapping("/option")
    @Timed
    public ResponseEntity<DistributorOption> createOption(@RequestBody DistributorOption distributorOption) throws URISyntaxException {
        log.debug("REST request to save DistributorOption : {}", distributorOption);

        DistributorOption result = distributorOptionRepository.save(distributorOption);

        return ResponseEntity.created(new URI("/api/option/" + result.getId().toString()))
            .headers(HeaderUtil.createAlert("A option is created with identifier " + result.getId().toString(), result.getId().toString()))
            .body(result);

    }

    // @GetMapping("/option")
    // @Timed
    // public List<DistributorOption> getAllOption() {
    //     List<DistributorOption> DistributorOption = distributorOptionRepository.findAll();
    //     return DistributorOption;
    // }

    @GetMapping("/option/{id}")
    @Timed
    public Optional<DistributorOption> getOption(@PathVariable Long id) {
        Optional<DistributorOption> result = distributorOptionRepository.findById(id);
        return result;

    }

    @PutMapping("/option")
    @Timed
    public ResponseEntity<DistributorOption> updateOption(@RequestBody DistributorOption DistributorOption)
        throws URISyntaxException {

        DistributorOption result = distributorOptionRepository.save(DistributorOption);

        return ResponseEntity.created(new URI("/api/option/" + result.getId().toString()))
            .headers(HeaderUtil.createAlert("A option is created with identifier " + result.getId().toString(), result.getId().toString()))
            .body(result);

    }

    @DeleteMapping("/option/{id}")
    @Timed
    public ResponseEntity<DistributorOption> deleteOption(@PathVariable Long id) {
        log.debug("REST request to delete Location: {}", id);
        Optional<DistributorOption> result = distributorOptionRepository.findById(id);
        distributorOptionRepository.deleteById(id);
        return ResponseEntity.ok().headers(HeaderUtil.createAlert("A option is deleted with identifier " + id, id.toString())).build();

    }

   @GetMapping("/options")
   @Timed
   public ResponseEntity<List<DistributorOption>> getAllOption(Pageable pageable) {
       final Page<DistributorOption> page = distributorOptionRepository.findAll(pageable);
       HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/option/");
       return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
   }
}
