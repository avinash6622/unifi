package com.bcad.application.web.rest;

import com.bcad.application.domain.AIF2SeriesMaster;
import com.bcad.application.domain.Product;
import com.bcad.application.repository.AIF2SeriesMasterRepository;
import com.bcad.application.repository.ProductRepository;
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
public class AIF2SeriesMasterResource {

    private final Logger log = LoggerFactory.getLogger(AIF2SeriesMasterResource.class);

    private final AIF2SeriesMasterRepository aif2SeriesMasterRepository;
    private final ProductRepository productRepository;

    public AIF2SeriesMasterResource(AIF2SeriesMasterRepository aif2SeriesMasterRepository,ProductRepository productRepository) {
        this.aif2SeriesMasterRepository = aif2SeriesMasterRepository;
        this.productRepository = productRepository;
    }

    @PostMapping("/aif2-series")
    @Timed
    public ResponseEntity<AIF2SeriesMaster> create(@RequestBody AIF2SeriesMaster aif2SeriesMaster) throws URISyntaxException {
        log.debug("REST request to save Aif2SeriesMaser : {}", aif2SeriesMaster);

        AIF2SeriesMaster result = aif2SeriesMasterRepository.save(aif2SeriesMaster);

        return (ResponseEntity<AIF2SeriesMaster>) ResponseEntity.created(new URI("/api/aif2-series/" + result.getId().toString()))
            .headers(HeaderUtil.createAlert( "A Aif2SeriesMaster is created with identifier " + result.getId().toString(), result.getId().toString()))
            .body(result);

    }
    @GetMapping("/aif2-series-product/{id}")
    @Timed
    public List<AIF2SeriesMaster> getProducts(@PathVariable Long id) throws URISyntaxException {
        Optional<Product> product = productRepository.findById(id);
     List<AIF2SeriesMaster> result=aif2SeriesMasterRepository.findByProduct(product.get());
     return result;



    }
    @GetMapping("/aif2-series/{id}")
    @Timed
    public Optional<AIF2SeriesMaster> getaif2seriesmaster(@PathVariable Long id){
        Optional<AIF2SeriesMaster> result = aif2SeriesMasterRepository.findById(id);
        return result;

    }
    @PutMapping("/aif2-series")
    @Timed
    public ResponseEntity<AIF2SeriesMaster> updateaif2seriesmaster(@RequestBody AIF2SeriesMaster aif2SeriesMaster)
        throws URISyntaxException {

        AIF2SeriesMaster result = aif2SeriesMasterRepository.save(aif2SeriesMaster);

        return (ResponseEntity<AIF2SeriesMaster>) ResponseEntity.created(new URI("/api/aif2-series/" + result.getId().toString()))
            .headers(HeaderUtil.createAlert( "A Aif2SeriesMaster is created with identifier " + result.getId().toString(), result.getId().toString()))
            .body(result);

    }

    @DeleteMapping("/aif2-series/{id}")
    @Timed
    public ResponseEntity<AIF2SeriesMaster> deleteaif2seriesmaster(@PathVariable Long id) {
        log.debug("REST request to delete AIF2SeriesMaster: {}", id);
        Optional<AIF2SeriesMaster> result= aif2SeriesMasterRepository.findById(id);
        aif2SeriesMasterRepository.deleteById(id);
        return ResponseEntity.ok().headers(HeaderUtil.createAlert( "A Aif2SeriesMaster is deleted with identifier " + id,id.toString())).build();
    }

    @GetMapping("/aif2-seriess")
    @Timed
    public ResponseEntity<List<AIF2SeriesMaster>> getAllAif2SeriesMasters(Pageable pageable) {
        final Page<AIF2SeriesMaster> page = aif2SeriesMasterRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/aif2-seriess");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }


}
