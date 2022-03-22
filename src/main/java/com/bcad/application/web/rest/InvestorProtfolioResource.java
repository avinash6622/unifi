package com.bcad.application.web.rest;

import com.bcad.application.domain.InvestorProtfolio;
import com.bcad.application.domain.SeriesMaster;
import com.bcad.application.repository.InvestorProtfolioRepository;
import com.bcad.application.repository.SeriesMasterRepository;
import com.codahale.metrics.annotation.Timed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URISyntaxException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class InvestorProtfolioResource {

    private final Logger log = LoggerFactory.getLogger(InvestorProtfolioResource.class);

    private final InvestorProtfolioRepository investorProtfolioRepository;
    private final SeriesMasterRepository seriesMasterRepository;

    public InvestorProtfolioResource(InvestorProtfolioRepository investorProtfolioRepository, SeriesMasterRepository seriesMasterRepository) {
        this.investorProtfolioRepository = investorProtfolioRepository;
        this.seriesMasterRepository = seriesMasterRepository;
    }

    @GetMapping("/investor-series/{id}")
    @Timed
    public Optional<SeriesMaster> getSeriesMasterName(@PathVariable Long id) {
        Optional<SeriesMaster> result = seriesMasterRepository.findById(id);
        return result;
    }

    @PostMapping("/aif-subscription")
    @Timed
    public ResponseEntity<InvestorProtfolio> create(@RequestBody List<InvestorProtfolio> investorProtfolios) throws URISyntaxException {
        log.debug("REST request to save AIF2ManagementFee : {}", investorProtfolios);
        String monthYr;
        DateFormat monthYrformat;
        Float noOfUnits = 0f;
        monthYrformat = new SimpleDateFormat("MMM yyyy");
        for (InvestorProtfolio investorProtfolio : investorProtfolios) {
            monthYr = monthYrformat.format(investorProtfolio.getSubscriptionDate());
            InvestorProtfolio selectedInvestorProtfolio = investorProtfolioRepository.findByMonthYrAndAifClientMaster(monthYr, investorProtfolio.getAifClientMaster());

            if (selectedInvestorProtfolio != null) {
                noOfUnits = selectedInvestorProtfolio.getClosingUnits() + investorProtfolio.getNoOfUnits();
                selectedInvestorProtfolio.setClosingUnits(noOfUnits);
                selectedInvestorProtfolio.setNoOfUnits(noOfUnits);
                investorProtfolioRepository.save(selectedInvestorProtfolio);

            } else {
                investorProtfolio.setMonthYr(monthYr);
                investorProtfolio.setClosingUnits(investorProtfolio.getNoOfUnits());
                if (investorProtfolio.getAifClientMaster().getDistributorMaster() != null) {
                    investorProtfolio.setDistributorMaster(investorProtfolio.getAifClientMaster().getDistributorMaster());
                }
                investorProtfolioRepository.save(investorProtfolio);
            }

        }
        return null;

    }
}
