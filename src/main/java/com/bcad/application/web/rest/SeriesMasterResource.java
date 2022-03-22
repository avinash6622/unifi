package com.bcad.application.web.rest;


import com.bcad.application.domain.AIFClientMaster;
import com.bcad.application.domain.SeriesMaster;
import com.bcad.application.repository.AIFClientMasterRepository;
import com.bcad.application.repository.SeriesMasterRepository;
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
public class SeriesMasterResource {

    private final Logger log = LoggerFactory.getLogger(SeriesMasterResource.class);

    private final SeriesMasterRepository seriesMasterRepository;

    public SeriesMasterResource(SeriesMasterRepository seriesMasterRepository) {
        this.seriesMasterRepository = seriesMasterRepository;
    }

    @PostMapping("/series-master")
    @Timed
    public ResponseEntity<SeriesMaster> createSeriesMaster(@RequestBody SeriesMaster seriesMaster) throws URISyntaxException {
        log.debug("REST request to save SeriesMaster : {}", seriesMaster);

        SeriesMaster result =seriesMasterRepository.save(seriesMaster);

        return ResponseEntity.created(new URI("/api/series-master/" + result.getId().toString()))
            .headers(HeaderUtil.createAlert("A SeriesMaster is created with identifier " + result.getId().toString(), result.getId().toString()))
            .body(result);

    }


    @GetMapping("/series-master/{id}")
    @Timed
    public Optional<SeriesMaster> getSeriesMastert(@PathVariable Long id) {
        Optional<SeriesMaster> result = seriesMasterRepository.findById(id);
        return result;

    }

    @PutMapping("/series-master")
    @Timed
    public ResponseEntity<SeriesMaster> updateSeriesMaster(@RequestBody SeriesMaster seriesMaster)
        throws URISyntaxException {

        SeriesMaster result = seriesMasterRepository.save(seriesMaster);

        return ResponseEntity.created(new URI("/api/series-master/" + result.getId().toString()))
            .headers(HeaderUtil.createAlert("A SeriesMaster is created with identifier " + result.getId().toString(), result.getId().toString()))
            .body(result);

    }

    @DeleteMapping("/series-master/{id}")
    @Timed
    public ResponseEntity<SeriesMaster> deleteSeriesMaster(@PathVariable Long id) {
        log.debug("REST request to delete SeriesMaster: {}", id);
        seriesMasterRepository.deleteById(id);
        return ResponseEntity.ok().headers(HeaderUtil.createAlert("A SeriesMaster is deleted with identifier " + id, id.toString())).build();

    }

    @GetMapping("/series-masters")
    @Timed
    public ResponseEntity<List<SeriesMaster>> getAllSeriesMaster(Pageable pageable) {
        final Page<SeriesMaster> page = seriesMasterRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/series-masters/");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    @GetMapping("/series-master-all")
    @Timed
    public List<SeriesMaster> getAllSeriesMasters() {
        List<SeriesMaster>  seriesMasters= seriesMasterRepository.findAll();
        System.out.println(seriesMasters.size());
        return seriesMasters;
    }

     @PostMapping("/series-masters-search")
     @Timed
     public SeriesMaster postSeriesMaster(@RequestBody SeriesMaster seriesMaster) {
        SeriesMaster result = seriesMasterRepository.findBySeriesCode(seriesMaster.getSeriesCode());
     return result;
     }
}
