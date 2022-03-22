package com.bcad.application.web.rest;

import com.bcad.application.domain.InvestmentMaster;
import com.bcad.application.domain.Product;
import com.bcad.application.repository.InvestmentMasterRepository;
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
public class InvestmentMasterResource {

    private final Logger log = LoggerFactory.getLogger(InvestmentMasterResource.class);

    private final InvestmentMasterRepository investmentMasterRepository;
    private final ProductRepository productRepository;

    public InvestmentMasterResource(InvestmentMasterRepository investmentMasterRepository, ProductRepository productRepository) {
        this.investmentMasterRepository = investmentMasterRepository;
        this.productRepository =  productRepository;
    }

    @PostMapping("/invest-master")
    @Timed
    public ResponseEntity<InvestmentMaster> createInvestmentMaster(@RequestBody InvestmentMaster investmentMaster) throws URISyntaxException {
        log.debug("REST request to save SeriesMaster : {}", investmentMaster);

        InvestmentMaster result = investmentMasterRepository.save(investmentMaster);
        Product product=new Product();
        product.setProductName(investmentMaster.getInvestmentName());
        productRepository.save(product);

        return ResponseEntity.created(new URI("/api/invest-master/" + result.getId().toString()))
            .headers(HeaderUtil.createAlert("A InvestmentMaster is created with identifier " + result.getId().toString(), result.getId().toString()))
            .body(result);

    }


    @GetMapping("/invest-master/{id}")
    @Timed
    public Optional<InvestmentMaster> getInvestmentMastert(@PathVariable Long id) {
        Optional<InvestmentMaster> result = investmentMasterRepository.findById(id);
        return result;

    }

    @PutMapping("/invest-master")
    @Timed
    public ResponseEntity<InvestmentMaster> updateInvestmentMaster(@RequestBody InvestmentMaster investmentMaster)
        throws URISyntaxException {
        Optional<InvestmentMaster> invest = investmentMasterRepository.findById(investmentMaster.getId());
        Product product = productRepository.findByProductName(invest.get().getInvestmentName());
        product.setProductName(investmentMaster.getInvestmentName());
        productRepository.save(product);

        InvestmentMaster result = investmentMasterRepository.save(investmentMaster);

        return ResponseEntity.created(new URI("/api/invest-master/" + result.getId().toString()))
            .headers(HeaderUtil.createAlert("A InvestmentMaster is created with identifier " + result.getId().toString(), result.getId().toString()))
            .body(result);

    }

    @DeleteMapping("/invest-master/{id}")
    @Timed
    public ResponseEntity<InvestmentMaster> deleteInvestmentMaster(@PathVariable Long id) {
        log.debug("REST request to delete InvestmentMaster: {}", id);
        Optional<InvestmentMaster> invest = investmentMasterRepository.findById(id);
        Product product = productRepository.findByProductName(invest.get().getInvestmentName());
       productRepository.deleteById(product.getId());
        investmentMasterRepository.deleteById(id);

        return ResponseEntity.ok().headers(HeaderUtil.createAlert("A InvestmentMaster is deleted with identifier " + id, id.toString())).build();

    }

    @GetMapping("/invest-masters")
    @Timed
    public ResponseEntity<List<InvestmentMaster>> getAllInvestmentMaster(Pageable pageable) {
        final Page<InvestmentMaster> page = investmentMasterRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/invest-masters/");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    @PostMapping("/invest-search")
    @Timed
    public InvestmentMaster postInvestmentMaster(@RequestBody InvestmentMaster investmentMaster) {
    InvestmentMaster result=investmentMasterRepository.findByInvestmentName(investmentMaster.getInvestmentName());
        return result;

    }
}
