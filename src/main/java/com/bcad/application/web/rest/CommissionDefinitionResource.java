package com.bcad.application.web.rest;

import com.bcad.application.domain.*;
import com.bcad.application.repository.*;
import com.bcad.application.web.rest.util.HeaderUtil;
import com.bcad.application.web.rest.util.PaginationUtil;
import com.codahale.metrics.annotation.Timed;
import com.google.gson.JsonObject;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class CommissionDefinitionResource {

    private final Logger log = LoggerFactory.getLogger(CommissionDefinition.class);

    private final CommissionDefinitionRepository commissionDefinitionRepository;
    private final DistributorMasterRepository distributorMasterRepository;
    private final RelationshipManagerRepository relationshipManagerRepository;
    private final PMSClientMasterRepository pmsClientMasterRepository;
    private final ClientFeeCommissionRepository clientFeeCommissionRepository;
    private final ProductRepository productRepository;
    private final ClientCommissionRepository clientCommissionRepository;
    private final ClientManagementRepository clientManagementRepository;
    private final InvestmentMasterRepository investmentMasterRepository;

    public CommissionDefinitionResource(CommissionDefinitionRepository commissionDefinitionRepository,DistributorMasterRepository distributorMasterRepository,
                                        RelationshipManagerRepository relationshipManagerRepository,PMSClientMasterRepository pmsClientMasterRepository,
                                        ClientFeeCommissionRepository clientFeeCommissionRepository,ProductRepository productRepository,
                                        ClientCommissionRepository clientCommissionRepository,ClientManagementRepository clientManagementRepository,
                                        InvestmentMasterRepository investmentMasterRepository) {
        this.commissionDefinitionRepository = commissionDefinitionRepository;
        this.distributorMasterRepository = distributorMasterRepository;
        this.relationshipManagerRepository = relationshipManagerRepository;
        this.pmsClientMasterRepository = pmsClientMasterRepository;
        this.clientFeeCommissionRepository = clientFeeCommissionRepository;
        this.productRepository = productRepository;
        this.clientCommissionRepository = clientCommissionRepository;
        this.clientManagementRepository = clientManagementRepository;
        this.investmentMasterRepository = investmentMasterRepository;
    }

    @PostMapping("/commission-def")
    @Timed
    public ResponseEntity<CommissionDefinition> createCommission(@RequestBody CommissionDefinition commissionDefinition) throws URISyntaxException {
        log.debug("REST request to save CommissionDefinition : {}", commissionDefinition);
        System.out.println(commissionDefinition);

        CommissionDefinition result = commissionDefinitionRepository.save(commissionDefinition);
        if(result.getBcadPMS()!=null){

       if(result.getProduct().getProductName().equals("BCAD") && result.getDistributorMasters().isEmpty()==false
        && result.getBcadPMS() ==1){

          for(DistributorMaster distributorMaster : result.getDistributorMasters()){
              distributorMaster=distributorMasterRepository.findByDistributorName(distributorMaster.getDistName());
              InvestmentMaster investmentMaster = investmentMasterRepository.findByInvestmentName(result.getProduct().getProductName());

              String slab=(result.getPmsInvest()==0) ? "OLD" :"NEW";
              List<PMSClientMaster> pmsClientMasters = pmsClientMasterRepository.findDistributor(distributorMaster.getId(),investmentMaster.getId(),slab);
              for(PMSClientMaster pmsClientMaster :pmsClientMasters) {
                  ClientFeeCommission clientFeeCommission = clientFeeCommissionRepository.findPmsClientMaster(pmsClientMaster.getId());
                if(clientFeeCommission!=null) {
                    clientFeeCommission.setProfitComm(result.getProfitComm());
                    clientFeeCommission.setNavComm(result.getNavComm());
                    clientFeeCommissionRepository.save(clientFeeCommission);
                }
              }
          }
        }

        else if(result.getProduct().getProductName().equals("BCAD") && result.getDistributorMasters().isEmpty()==false
            && result.getBcadPMS() ==0) {

            for (DistributorMaster distributorMaster : result.getDistributorMasters()) {
                distributorMaster = distributorMasterRepository.findByDistributorName(distributorMaster.getDistName());
                String slab = (result.getPmsInvest() == 0) ? "OLD" : "NEW";
                List<ClientManagement> clientManagements = clientManagementRepository.findDistributor(distributorMaster.getId(), result.getProduct().getId(), slab);
                for (ClientManagement pmsClientMaster : clientManagements) {
                    ClientCommission clientCommission = clientCommissionRepository.findbcadClientMaster(pmsClientMaster.getId());
                    if(clientCommission !=null) {
                        clientCommission.setProfitComm(result.getProfitComm());
                        clientCommission.setNavComm(result.getNavComm());
                        clientCommissionRepository.save(clientCommission);
                    }
                }
            }
        }
        else{
           for(DistributorMaster distributorMaster : result.getDistributorMasters()){
               distributorMaster=distributorMasterRepository.findByDistributorName(distributorMaster.getDistName());
               InvestmentMaster investmentMaster = investmentMasterRepository.findByInvestmentName(result.getProduct().getProductName());

               String slab=(result.getPmsInvest()==0) ? "OLD" :"NEW";
               List<PMSClientMaster> pmsClientMasters = pmsClientMasterRepository.findDistributor(distributorMaster.getId(),investmentMaster.getId(),slab);
               for(PMSClientMaster pmsClientMaster :pmsClientMasters) {
                   ClientFeeCommission clientFeeCommission = clientFeeCommissionRepository.findPmsClientMaster(pmsClientMaster.getId());
                    if(clientFeeCommission!=null) {
                    clientFeeCommission.setProfitComm(result.getProfitComm());
                    clientFeeCommission.setNavComm(result.getNavComm());
                    clientFeeCommissionRepository.save(clientFeeCommission);
                    }
               }
           }

       }
        }

        return ResponseEntity.created(new URI("/api/commission-def/" + result.getId().toString()))
            .headers(HeaderUtil.createAlert("A CommissionDefinition is created with identifier " + result.getId().toString(), result.getId().toString()))
            .body(result);

    }

   @GetMapping("/commission-defs")
   @Timed
   public ResponseEntity<List<CommissionDefinition>> getAllCommission(Pageable pageable) {
       final Page<CommissionDefinition> page = commissionDefinitionRepository.findAll(pageable);
       HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/commission-def/");
       return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
   }

   @PutMapping("/commission-def")
   @Timed
   public ResponseEntity<?> updateCommission(@RequestBody CommissionDefinition commissionDefinition)
       throws URISyntaxException, ParseException {
        Integer sFlag=0;
        if(commissionDefinition.getBcadPMS()!=null){
            if(commissionDefinition.getBcadPMS()==1)
                sFlag=1;
        }

    if(sFlag==1 && commissionDefinition.getProduct().getProductName().equals("BCAD")){
    List<Long> idList = new ArrayList<>();
    idList = commissionDefinition.getDistributorMasterOption().stream().map(DistributorMaster::getId).collect(Collectors.toList());
    if (idList.size() != 0) {

        List<Integer> commissionDefinitions = commissionDefinitionRepository.findDistributorDuplicationnOption3(commissionDefinition.getProduct().getId(), idList, commissionDefinition.getId(),commissionDefinition.getPmsInvest());
        if (commissionDefinitions.size() != 0) {
            JsonObject apierror = new JsonObject();
            apierror = new JsonObject();
            apierror.addProperty("error", "470");
            String distributorNot = "";
            for (Integer distId : commissionDefinitions) {
                Optional<DistributorMaster> distributorMaster = distributorMasterRepository.findById((long) distId);
                distributorNot = distributorNot.concat(distributorMaster.get().getDistName() + ",");
            }
            apierror.addProperty("Message", distributorNot + " is already mapped with another Commission Definition");
            return ResponseEntity.ok(apierror.toString());
        }

    }

}
else if(commissionDefinition.getProduct().getProductName().equals("AIF")
     || commissionDefinition.getProduct().getProductName().equals("AIF2") || commissionDefinition.getProduct().getProductName().equals("AIF Blend")){
    List<Long> idList = new ArrayList<>();
    idList = commissionDefinition.getDistributorMasters().stream().map(DistributorMaster::getId).collect(Collectors.toList());
    if (idList.size() != 0) {
        List<Integer> commissionDefinitions = commissionDefinitionRepository.findDistributorDuplications(commissionDefinition.getProduct().getId(), idList, commissionDefinition.getId());
        if (commissionDefinitions.size() != 0) {
            JsonObject apierror = new JsonObject();
            apierror = new JsonObject();
            apierror.addProperty("error", "470");
            String distributorNot = "";
            for (Integer distId : commissionDefinitions) {
                Optional<DistributorMaster> distributorMaster = distributorMasterRepository.findById((long) distId);
                distributorNot = distributorNot.concat(distributorMaster.get().getDistName() + ",");
            }
            apierror.addProperty("Message", distributorNot + " is already mapped with another Commission Definition");
            return ResponseEntity.ok(apierror.toString());
        }
    }
}
else{
        List<Long> idList = new ArrayList<>();
        idList = commissionDefinition.getDistributorMasters().stream().map(DistributorMaster::getId).collect(Collectors.toList());
        if (idList.size() != 0) {
            List<Integer> commissionDefinitions = commissionDefinitionRepository.findDistributorPMSDuplications(commissionDefinition.getProduct().getId(),commissionDefinition.getPmsInvest(), idList, commissionDefinition.getId());
            if (commissionDefinitions.size() != 0) {
                JsonObject apierror = new JsonObject();
                apierror = new JsonObject();
                apierror.addProperty("error", "470");
                String distributorNot = "";
                for (Integer distId : commissionDefinitions) {
                    Optional<DistributorMaster> distributorMaster = distributorMasterRepository.findById((long) distId);
                    distributorNot = distributorNot.concat(distributorMaster.get().getDistName() + ",");
                }
                apierror.addProperty("Message", distributorNot + " is already mapped with another Commission Definition");
                return ResponseEntity.ok(apierror.toString());
            }
        }
    }

        CommissionDefinition result = commissionDefinitionRepository.save(commissionDefinition);

       if(result.getProduct().getProductName().equals("BCAD") && result.getDistributorMasters().isEmpty()==false
           && result.getBcadPMS() ==1){

           for(DistributorMaster distributorMaster : result.getDistributorMasters()){
               distributorMaster=distributorMasterRepository.findByDistributorName(distributorMaster.getDistName());
               InvestmentMaster investmentMaster = investmentMasterRepository.findByInvestmentName(result.getProduct().getProductName());

               String slab=(result.getPmsInvest()==0) ? "OLD" :"NEW";
               List<PMSClientMaster> pmsClientMasters = pmsClientMasterRepository.findDistributor(distributorMaster.getId(),investmentMaster.getId(),slab);
               for(PMSClientMaster pmsClientMaster :pmsClientMasters) {
                   ClientFeeCommission clientFeeCommission = clientFeeCommissionRepository.findPmsClientMaster(pmsClientMaster.getId());
                    if(clientFeeCommission !=null){
                   clientFeeCommission.setProfitComm(result.getProfitComm());
                   clientFeeCommission.setNavComm(result.getNavComm());
                   clientFeeCommissionRepository.save(clientFeeCommission);}

           }
       }}

       else if(result.getProduct().getProductName().equals("BCAD") && result.getDistributorMasters().isEmpty()==false
           && result.getBcadPMS() ==0) {

           for (DistributorMaster distributorMaster : result.getDistributorMasters()) {
               distributorMaster = distributorMasterRepository.findByDistributorName(distributorMaster.getDistName());
               String slab = (result.getPmsInvest() == 0) ? "OLD" : "NEW";
               List<ClientManagement> clientManagements = clientManagementRepository.findDistributor(distributorMaster.getId(), result.getProduct().getId(), slab);
               for (ClientManagement pmsClientMaster : clientManagements) {
                   ClientCommission clientCommission = clientCommissionRepository.findbcadClientMaster(pmsClientMaster.getId());
                   if(clientCommission!=null){
                   clientCommission.setProfitComm(result.getProfitComm());
                   clientCommission.setNavComm(result.getNavComm());
                   clientCommissionRepository.save(clientCommission);
               }}
           }
       }
       else{
           for(DistributorMaster distributorMaster : result.getDistributorMasters()){
               distributorMaster=distributorMasterRepository.findByDistributorName(distributorMaster.getDistName());
               InvestmentMaster investmentMaster = investmentMasterRepository.findByInvestmentName(result.getProduct().getProductName());

               String slab=(result.getPmsInvest()==0) ? "OLD" :"NEW";
               List<PMSClientMaster> pmsClientMasters = pmsClientMasterRepository.findDistributor(distributorMaster.getId(),investmentMaster.getId(),slab);
               for(PMSClientMaster pmsClientMaster :pmsClientMasters) {
                   ClientFeeCommission clientFeeCommission = clientFeeCommissionRepository.findPmsClientMaster(pmsClientMaster.getId());
                   if(clientFeeCommission!=null) {
                       clientFeeCommission.setProfitComm(result.getProfitComm());
                       clientFeeCommission.setNavComm(result.getNavComm());
                       clientFeeCommissionRepository.save(clientFeeCommission);
                   }
               }
           }

       }


       return ResponseEntity.created(new URI("/api/commission-def/" + result.getId().toString()))
           .headers(HeaderUtil.createAlert("A CommissionDefinition is created with identifier " + result.getId().toString(), result.getId().toString()))
           .body(result);

   }

   @GetMapping("/commission-def/{id}")
   @Timed
   public Optional<CommissionDefinition> getCommission(@PathVariable Long id) {
       Optional<CommissionDefinition> result = commissionDefinitionRepository.findById(id);
       return result;

   }

   @DeleteMapping("/commission-def/{id}")
   @Timed
   public ResponseEntity<CommissionDefinition> deleteCommission(@PathVariable Long id) {
       log.debug("REST request to delete Location: {}", id);
       Optional<CommissionDefinition> result= commissionDefinitionRepository.findById(id);

        commissionDefinitionRepository.deleteById(id);
       return ResponseEntity.ok().headers(HeaderUtil.createAlert( "A user is deleted with identifier " + id,id.toString())).build();
   }

    @GetMapping("/commission-def/dist-master")
    @Timed
    public List<DistributorMaster> getDistributors() {
        List<DistributorMaster> result =distributorMasterRepository.findCommissionDefinition();
        return result;
    }
    @GetMapping("/commission-def/product/{id}")
    @Timed
    public List<DistributorMaster> getProductDistributors(@PathVariable Long id) {
        List<DistributorMaster> result =distributorMasterRepository.findProductMappings(id);
        return result;
    }

    @GetMapping("/commission-def/option3/{id}/{invest}")
    @Timed
    public List<DistributorMaster> getProductOptio3(@PathVariable Long id,@PathVariable Long invest) {
        List<DistributorMaster> result = distributorMasterRepository.findProductOption3Mappings(id,invest);
        return result;
    }

    @GetMapping("/commission-def/product/{id}/{invest}")
    @Timed
    public List<DistributorMaster> getPMSDistributors(@PathVariable Long id,@PathVariable Long invest) {
        List<DistributorMaster> result =distributorMasterRepository.findPMSMappings(id,invest);
        return result;
    }

}
