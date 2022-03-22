package com.bcad.application.web.rest;

import com.bcad.application.domain.*;
import com.bcad.application.repository.InvestorProtfolioRepository;
import com.bcad.application.repository.SeriesMasterMonthRepository;
import com.bcad.application.repository.SeriesMasterRepository;
import com.bcad.application.service.AIFSubRedUploadService;
import com.codahale.metrics.annotation.Timed;
import com.google.gson.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.io.IOException;
import java.net.URISyntaxException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping("/api")
public class AIFRedemptionMasterResource {

    @PersistenceContext
    EntityManager em;

    private final Logger log = LoggerFactory.getLogger(AIFRedemptionMasterResource.class);

    private final AIFSubRedUploadService aifSubRedUploadService;
    private final SeriesMasterMonthRepository seriesMasterMonthRepository;
    private final InvestorProtfolioRepository investorProtfolioRepository;
    private final SeriesMasterRepository seriesMasterRepository;

    public AIFRedemptionMasterResource(AIFSubRedUploadService aifSubRedUploadService, SeriesMasterMonthRepository seriesMasterMonthRepository,
                                       InvestorProtfolioRepository investorProtfolioRepository,SeriesMasterRepository seriesMasterRepository) {
        this.aifSubRedUploadService = aifSubRedUploadService;
        this.seriesMasterMonthRepository = seriesMasterMonthRepository;
        this.investorProtfolioRepository = investorProtfolioRepository;
        this.seriesMasterRepository = seriesMasterRepository;
    }

    @PostMapping("/sub-file-upload")
    @Timed
    public ResponseEntity<?> createClient(@RequestParam @DateTimeFormat(pattern = "dd-MMM-yyyy") Date redemptionDate,
                                                      @RequestParam String fileName,
                                                      @RequestParam(name = "fileUpload") MultipartFile multipartFile)
        throws URISyntaxException, MissingServletRequestParameterException, IOException, Exception {

        FileUploadAIF result = aifSubRedUploadService.fileSubUpload(redemptionDate, fileName, multipartFile);

        if(result.getCode() != null) {
                JsonObject apierror = new JsonObject();
                apierror = new JsonObject();
                apierror.addProperty("error", result.getCode());
                apierror.addProperty("message", result.getStatus());
                return ResponseEntity.ok(apierror.toString());
            }

        return null;
    }

    @PostMapping("/change-series")
    @Timed
    public Float selectSeries(@RequestParam @DateTimeFormat(pattern = "dd-MMM-yyyy") Date redemptionDate,
                              @RequestBody SeriesMaster seriesMaster) throws IOException, Exception {
        Date result;
        String monthYr;
        GregorianCalendar calendar;
        DateFormat monthYrformat;

        calendar = new GregorianCalendar();
        calendar.setTime(redemptionDate);
        calendar.add(Calendar.MONTH, -1);
        result = calendar.getTime();
        monthYrformat = new SimpleDateFormat("MMM yyyy");
        monthYr = monthYrformat.format(result);

        SeriesMasterMonth seriesNavAsOn = seriesMasterMonthRepository.findBySeriesMasterAndMonthYearAndIsDeleted(seriesMaster, monthYr, 0);
        if (seriesNavAsOn == null) {
            log.error("please upload Nav value");
        }
        return seriesNavAsOn.getNavValue();
    }

    @GetMapping("redemption-clients/{id}")
    @Timed
    public List<AIFClientMaster> getAIFClients(@PathVariable Long id) {
        Query q=em.createNativeQuery("select * from aif_client_master where id in(select client_id FROM investor_portfolio where series_id="+id+")",AIFClientMaster.class);
        List<AIFClientMaster> clientMasters=q.getResultList();
        return clientMasters;
    }

    @PostMapping("redemption-units")
    @Timed
    public InvestorProtfolio getRedemptionClients(@RequestBody AIFClientSeries aifClientSeries){

        InvestorProtfolio result = investorProtfolioRepository.findBySeriesMasterAndAifClientMaster(aifClientSeries.getSeriesMaster(),aifClientSeries.getAifClientMaster());

        return result;
    }

    @PostMapping("redemptions")
    @Timed
    public InvestorProtfolio getRedemptionClients(@RequestBody List<InvestorProtfolio> investorProtfolios){

        AIFRedemptionMaster aifRedemptionMaster;
        List<InvestorProtfolio> investView;

        DateFormat dateFormat = new SimpleDateFormat("MMM yyyy");
        String sMonth = dateFormat.format(investorProtfolios.get(0).getSubscriptionDate());
        aifSubRedUploadService.validateRedemption(investorProtfolios,investorProtfolios.get(0).getSubscriptionDate());

        return null;
    }
}
