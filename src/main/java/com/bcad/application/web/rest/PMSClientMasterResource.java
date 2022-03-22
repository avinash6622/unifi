package com.bcad.application.web.rest;

import com.bcad.application.config.PropertiesUtils;
import com.bcad.application.domain.*;
import com.bcad.application.repository.*;
import com.bcad.application.security.SecurityUtils;
import com.bcad.application.web.rest.util.HeaderUtil;
import com.bcad.application.web.rest.util.PaginationUtil;
import com.codahale.metrics.annotation.Timed;
import com.google.gson.JsonObject;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class PMSClientMasterResource {

    private final Logger log = LoggerFactory.getLogger(PMSClientMasterResource.class);

    private final PMSClientMasterRepository pmsClientMasterRepository;
    private final UserResource userResource;
    private final ClientFeeCommissionRepository clientFeeCommissionRepository;
    private final DistributorMasterRepository distributorMasterRepository;
    private final CommissionDefinitionRepository commisssionDefinitionRepository;
    private final ProductRepository productRepository;
    private final InvestmentMasterRepository investmentMasterRepository;

    public PMSClientMasterResource(PMSClientMasterRepository pmsClientMasterRepository,UserResource userResource,
         ClientFeeCommissionRepository clientFeeCommissionRepository,DistributorMasterRepository distributorMasterRepository,
                                   CommissionDefinitionRepository commisssionDefinitionRepository,ProductRepository productRepository,
                                   InvestmentMasterRepository investmentMasterRepository) {
        this.pmsClientMasterRepository = pmsClientMasterRepository;
        this.userResource=userResource;
        this.clientFeeCommissionRepository = clientFeeCommissionRepository;
        this.distributorMasterRepository = distributorMasterRepository;
        this.commisssionDefinitionRepository = commisssionDefinitionRepository;
        this.productRepository = productRepository;
        this.investmentMasterRepository = investmentMasterRepository;
    }

    @PersistenceContext
    EntityManager entityManager;

    DateFormat firstDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");
    PropertiesConfiguration prop = new PropertiesUtils().getProperties("application.properties");


    @PostMapping("/pms-client")
    @Timed
    public ResponseEntity<?> createPMSClient(@RequestBody PMSClientMaster pmsClientMaster) throws URISyntaxException, ParseException {
        log.debug("REST request to save DistributorOption : {}", pmsClientMaster);
        CommissionDefinition commissionDefinition = new CommissionDefinition();

        if(pmsClientMaster.getDistributorMaster()!=null){
            Integer pmsInvest = (firstDateFormat.parse(prop.getString("pms.strategy.date")).after(pmsClientMaster.getAccountOpenDate())) ? 0 : 1;
            Product product = productRepository.findByProductName(pmsClientMaster.getInvestmentMaster().getInvestmentName());
            if(product.getProductName().equals("BCAD")) {
                commissionDefinition = commisssionDefinitionRepository.getBCADInvestmentDateCalc(pmsClientMaster.getDistributorMaster().getId(), pmsInvest, product.getId());
            } else{
                commissionDefinition=commisssionDefinitionRepository.getPMSInvestmentDateCalc(pmsClientMaster.getDistributorMaster().getId(),pmsInvest,product.getId());}
            if(commissionDefinition==null){
                JsonObject apierror = new JsonObject();
                apierror = new JsonObject();
                apierror.addProperty("error", "470");
                apierror.addProperty("Message",product.getProductName() +"is not mapped with Commission Definition. " +
                    "Add in the CD and again create client");
                return ResponseEntity.ok(apierror.toString());

        }}

        PMSClientMaster result =pmsClientMasterRepository.save(pmsClientMaster);
        if(result.getDistributorMaster()!=null){
            ClientFeeCommission clientFeeCommission = new ClientFeeCommission();
            clientFeeCommission.setPmsClientMaster(result);
            clientFeeCommission.setNavComm(commissionDefinition.getNavComm());
            clientFeeCommission.setProfitComm(commissionDefinition.getProfitComm());
            clientFeeCommission.setUpdateRequired(0);
            clientFeeCommissionRepository.save(clientFeeCommission);

        }

        return ResponseEntity.created(new URI("/api/pms-client/" + result.getId().toString()))
            .headers(HeaderUtil.createAlert("A PMSClient is created with identifier " + result.getId().toString(), result.getId().toString()))
            .body(result);

    }


    @GetMapping("/pms-client/{id}")
    @Timed
    public Optional<PMSClientMaster> getPMSClient(@PathVariable Long id) {
        Optional<PMSClientMaster> result = pmsClientMasterRepository.findById(id);
        return result;

    }

    @PutMapping("/pms-client")
    @Timed
    public ResponseEntity<?> updatePMSClient(@RequestBody PMSClientMaster pmsClientMaster)
        throws URISyntaxException, ParseException {
        CommissionDefinition commissionDefinition = new CommissionDefinition();
        if(pmsClientMaster.getDistributorMaster()!=null){
            Integer pmsInvest = (firstDateFormat.parse(prop.getString("pms.strategy.date")).after(pmsClientMaster.getAccountOpenDate())) ? 0 : 1;
            DistributorMaster distributorMaster = distributorMasterRepository.findByDistributorName(pmsClientMaster.getDistributorMaster().getDistName());
            Product product = productRepository.findByProductName(pmsClientMaster.getInvestmentMaster().getInvestmentName());
            if(product.getProductName().equals("BCAD")) {
                commissionDefinition = commisssionDefinitionRepository.getBCADInvestmentDateCalc(distributorMaster.getId(), pmsInvest, product.getId());
            } else{
                commissionDefinition=commisssionDefinitionRepository.getPMSInvestmentDateCalc(distributorMaster.getId(),pmsInvest,product.getId());}
            if(commissionDefinition==null){
                JsonObject apierror = new JsonObject();
                apierror = new JsonObject();
                apierror.addProperty("error", "470");
                apierror.addProperty("Message",product.getProductName() +"is not mapped with Commission Definition. " +
                    "Add in the CD and again create client");
                return ResponseEntity.ok(apierror.toString());

            }}

        PMSClientMaster result = pmsClientMasterRepository.save(pmsClientMaster);

        if(result.getDistributorMaster()!=null){
            ClientFeeCommission clientFeeCommission =clientFeeCommissionRepository.findPmsClientMaster(result.getId());
             if(clientFeeCommission!=null){
                 if(clientFeeCommission.getUpdateRequired()==0){
                 clientFeeCommission.setNavComm(commissionDefinition.getNavComm());
                clientFeeCommission.setProfitComm(commissionDefinition.getProfitComm());
                clientFeeCommissionRepository.save(clientFeeCommission);}}
                else{
                   clientFeeCommission = new ClientFeeCommission();
                   clientFeeCommission.setPmsClientMaster(result);
                   clientFeeCommission.setNavComm(commissionDefinition.getNavComm());
                   clientFeeCommission.setProfitComm(commissionDefinition.getProfitComm());
                   clientFeeCommission.setUpdateRequired(0);
                   clientFeeCommissionRepository.save(clientFeeCommission);

               }

        }

        return ResponseEntity.created(new URI("/api/pms-client/" + result.getId().toString()))
            .headers(HeaderUtil.createAlert("A PMSClient is created with identifier " + result.getId().toString(), result.getId().toString()))
            .body(result);

    }

    @DeleteMapping("/pms-client/{id}")
    @Timed
    public ResponseEntity<PMSClientMaster> deletePMSClient(@PathVariable Long id) {
        log.debug("REST request to delete PMSClientMaster: {}", id);
        ClientFeeCommission clientFeeCommission = clientFeeCommissionRepository.findPmsClientMaster(id);
        if(clientFeeCommission!=null)
        clientFeeCommissionRepository.deleteById(clientFeeCommission.getId());

        pmsClientMasterRepository.deleteById(id);
        return ResponseEntity.ok().headers(HeaderUtil.createAlert("A PMSClient is deleted with identifier " + id, id.toString())).build();

    }

    @GetMapping("/pms-clients")
    @Timed
    public ResponseEntity<List<PMSClientMaster>> getAllPMS(Pageable pageable) {
        Optional<User> user=userResource.getCurrentUse(SecurityUtils.getCurrentUserLogin().get());
        Page<PMSClientMaster> page=null;
        if(user.get().getDistributorMaster()!=null){
        page = pmsClientMasterRepository.findByDistributorMaster(user.get().getDistributorMaster(),pageable);}
       else if(user.get().getRelationshipManager()!=null){
        page = pmsClientMasterRepository.findByRelationshipManager(user.get().getRelationshipManager(),pageable);}
            else {
       page = pmsClientMasterRepository.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/pms-clients/");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    @PostMapping("/PMS-search")
    @Timed
    public List<PMSClientMaster> postPMS(@RequestBody PMSClientMaster pmsClientMaster) {
        List<PMSClientMaster> result = pmsClientMasterRepository.findClientCode(pmsClientMaster.getClientCode());
        return result;

    }

    @PostMapping("/PMS-search-all")
    @Timed
    public List<PMSClientMaster> searchClient(@RequestBody ClientManagementSearch clientManagementSearch) {
        String sql="";
        if(clientManagementSearch.getClientCodes()!=null && clientManagementSearch.getClientCodes().size()!=0)
            sql="client_code in("+ String.join(",",clientManagementSearch.getClientCodes().stream().map(clientCode -> ("'" + clientCode + "'")).collect(Collectors.toList()))+")";
        if(clientManagementSearch.getClientNames()!=null && clientManagementSearch.getClientNames().size()!=0)
            sql=(!sql.equals("")?sql.concat(" and client_name in("+String.join(",", clientManagementSearch.getClientNames().stream()
                .map(name -> ("'" + name + "'")).collect(Collectors.toList()))+")"):"client_name in" +
                "("+String.join(",", clientManagementSearch.getClientNames().stream()
                .map(name -> ("'" + name + "'"))
                .collect(Collectors.toList()))+")");
        if(clientManagementSearch.getDistMasterId()!=null && clientManagementSearch.getDistMasterId().size()!=0)
            sql=(!sql.equals("")? sql.concat(" and dist_id in("+StringUtils.join(clientManagementSearch.getDistMasterId(),',')+")") :
                " dist_id in("+StringUtils.join(clientManagementSearch.getDistMasterId(),',')+")");

        if(clientManagementSearch.getRmId()!=null && clientManagementSearch.getRmId().size()!=0)
            sql=(!sql.equals("")? sql.concat(" and rm_id in("+StringUtils.join(clientManagementSearch.getRmId(),',')+")"):
                " rm_id in("+StringUtils.join(clientManagementSearch.getRmId(),',')+")");

        Query query=entityManager.createNativeQuery("select * from pms_client_master where "+sql,PMSClientMaster.class);

        List<PMSClientMaster> result = query.getResultList();
        System.out.println(result);
        return result;

    }

    @GetMapping ("/pms-client")
    @Timed
    public List<PMSClientMaster> getpms() {

                List<PMSClientMaster> result=pmsClientMasterRepository.findAll();


        return result;
    }


    @GetMapping("/pms-clients-all")
    @Timed
    public List<PMSClientMaster> getAllPMSClients() {
        List<PMSClientMaster>  pmsClientMasters= pmsClientMasterRepository.findAll();

        return pmsClientMasters;
    }

}
