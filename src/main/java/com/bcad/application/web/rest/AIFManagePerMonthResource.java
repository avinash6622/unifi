package com.bcad.application.web.rest;

import com.bcad.application.domain.AIFClientMaster;
import com.bcad.application.domain.AIFManagePerMonth;
import com.bcad.application.repository.AIFClientMasterRepository;
import com.bcad.application.repository.AIFManagePerMonthRepository;
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
public class AIFManagePerMonthResource {

    private final Logger log = LoggerFactory.getLogger(AIFClientMasterResource.class);

    private final AIFManagePerMonthRepository aifManagePerMonthRepository;

    public AIFManagePerMonthResource(AIFManagePerMonthRepository aifManagePerMonthRepository) {
        this.aifManagePerMonthRepository = aifManagePerMonthRepository;
    }

    @PostMapping("/aif-manage")
    @Timed
    public ResponseEntity<AIFManagePerMonth> createAIFManage(@RequestBody AIFManagePerMonth aifManagePerMonth) throws URISyntaxException {
        log.debug("REST request to save DistributorOption : {}", aifManagePerMonth);

        AIFManagePerMonth result =aifManagePerMonthRepository.save(aifManagePerMonth);

        return ResponseEntity.created(new URI("/api/aif-manage/" + result.getSeriesMaster().getSeriesName()))
            .headers(HeaderUtil.createAlert("A AIFManage is created with identifier " + result.getSeriesMaster().getSeriesName(), result.getSeriesMaster().getSeriesName()))
            .body(result);

    }


    @GetMapping("/aif-manage/{id}")
    @Timed
    public Optional<AIFManagePerMonth> getAIFManage(@PathVariable Long id) {
        Optional<AIFManagePerMonth> result = aifManagePerMonthRepository.findById(id);
        return result;

    }

    @PutMapping("/aif-manage")
    @Timed
    public ResponseEntity<AIFManagePerMonth> updateAIFManage(@RequestBody AIFManagePerMonth aifManagePerMonth)
        throws URISyntaxException {

        AIFManagePerMonth result = aifManagePerMonthRepository.save(aifManagePerMonth);

        return ResponseEntity.created(new URI("/api/aif-manage/" + result.getSeriesMaster().getSeriesName()))
            .headers(HeaderUtil.createAlert("A AIFClient is created with identifier " + result.getSeriesMaster().getSeriesName(), result.getSeriesMaster().getSeriesName()))
            .body(result);

    }

    @DeleteMapping("/aif-manage/{id}")
    @Timed
    public ResponseEntity<AIFManagePerMonth> deleteAIFManage(@PathVariable Long id) {
        log.debug("REST request to delete AIFManage: {}", id);
        aifManagePerMonthRepository.deleteById(id);
        return ResponseEntity.ok().headers(HeaderUtil.createAlert("A AIFManage is deleted with identifier " + id, id.toString())).build();

    }

    @GetMapping("/aif-manges")
    @Timed
    public ResponseEntity<List<AIFManagePerMonth>> getAllAIFManage(Pageable pageable) {
        final Page<AIFManagePerMonth> page = aifManagePerMonthRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/aif-manages/");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }
}
