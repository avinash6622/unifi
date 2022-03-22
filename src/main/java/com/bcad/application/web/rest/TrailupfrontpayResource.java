package com.bcad.application.web.rest;

import com.bcad.application.config.PropertiesUtils;
import com.bcad.application.domain.*;
import com.bcad.application.repository.*;
import com.bcad.application.security.SecurityUtils;
import com.bcad.application.service.TrailUpfrontPayService;
import com.bcad.application.web.rest.UserResource;
import com.bcad.application.web.rest.util.HeaderUtil;
import com.codahale.metrics.annotation.Timed;
import com.google.gson.JsonObject;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.FileSystemResource;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class TrailupfrontpayResource {

    private final Logger log = LoggerFactory.getLogger(TrailupfrontpayResource.class);

    private final TrailupfrontpayRepository trailupfrontpayRepository;
    private final FeeTrailUpfrontTransRepository feeTrailUpfrontTransRepository;
    private final DistributorMasterRepository distributorMasterRepository;
    private final UserResource userResource;
    private final TrailUpfrontPayService trailUpfrontPayService;
    private final GenericPayTrailUpfrontRepository genericPayTrailUpfrontRepository;
    private final ProductRepository productRepository;

    List<String> fileDownload = new ArrayList<>();
    PropertiesConfiguration prop = new PropertiesUtils().getProperties("application.properties");


    public TrailupfrontpayResource(TrailupfrontpayRepository  trailupfrontpayRepository,FeeTrailUpfrontTransRepository feeTrailUpfrontTransRepository,
                                   DistributorMasterRepository distributorMasterRepository,UserResource userResource,TrailUpfrontPayService trailUpfrontPayService,
                                   GenericPayTrailUpfrontRepository genericPayTrailUpfrontRepository,ProductRepository productRepository) {
        this.trailupfrontpayRepository = trailupfrontpayRepository;
        this.feeTrailUpfrontTransRepository = feeTrailUpfrontTransRepository;
        this.distributorMasterRepository = distributorMasterRepository;
        this.userResource = userResource;
        this.trailUpfrontPayService = trailUpfrontPayService;
        this.genericPayTrailUpfrontRepository=genericPayTrailUpfrontRepository;
        this.productRepository = productRepository;
    }

    @PostMapping("/trailupfrontpay")
    @Timed
    public ResponseEntity<?> createTrailupfront(@RequestBody Trailupfrontpay trailupfrontpay) throws URISyntaxException {
        log.debug("REST request to save Trailupfront : {}", trailupfrontpay);

        System.out.println(trailupfrontpay.toString());
        Trailupfrontpay  result1 = new Trailupfrontpay();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String payDate  = sdf.format(trailupfrontpay.getPaymentDate());

        if(trailupfrontpay.getProductName().equals("AIF & PMS"))
        {
        Trailupfrontpay result = trailupfrontpayRepository.findPaymentRepeation(payDate,
                                      trailupfrontpay.getPaymentAmount(),trailupfrontpay.getDistributorMaster().getId());
        if(result==null){
           updatePaymentTransaction(trailupfrontpay);
           result1 =trailupfrontpayRepository.save(trailupfrontpay);
        }
        if(result!= null) {
                JsonObject apierror = new JsonObject();
                apierror = new JsonObject();
                apierror.addProperty("error", "420");
                apierror.addProperty("message", "Already Payment done");
                return ResponseEntity.ok(apierror.toString());

        }

        return ResponseEntity.created(new URI("/api/trailupfrontpay/" + result1.getId().toString()))
            .headers(HeaderUtil.createAlert( "A Trailupfrontpay is created with identifier " + result1.getId().toString(), result1.getId().toString()))
            .body(result);}
            else{
                GenericPayTrailUpfront genericPayTrailUpfront=new GenericPayTrailUpfront();
                GenericPayTrailUpfront result2=new GenericPayTrailUpfront();
                Product product = productRepository.findByProductName(trailupfrontpay.getProductName());

            GenericPayTrailUpfront result = genericPayTrailUpfrontRepository.findPaymentRepeation(payDate,
                trailupfrontpay.getPaymentAmount(),trailupfrontpay.getDistributorMaster().getId(),product.getId());

            if(result==null){
                genericPayTrailUpfront.setBankName(trailupfrontpay.getBankName());
                genericPayTrailUpfront.setChequeDate(trailupfrontpay.getChequeDate());
                genericPayTrailUpfront.setChequeNo(trailupfrontpay.getChequeNo());
                genericPayTrailUpfront.setPaymentDate(trailupfrontpay.getPaymentDate());
                genericPayTrailUpfront.setDistributorMaster(trailupfrontpay.getDistributorMaster());
                genericPayTrailUpfront.setPayAmount(trailupfrontpay.getPaymentAmount());
                genericPayTrailUpfront.setPayType(trailupfrontpay.getPaymentType());
                genericPayTrailUpfront.setProduct(product);
                result2 =genericPayTrailUpfrontRepository.save(genericPayTrailUpfront);
            }
            if(result!= null) {
                JsonObject apierror = new JsonObject();
                apierror = new JsonObject();
                apierror.addProperty("error", "420");
                apierror.addProperty("message", "Already Payment done");
                return ResponseEntity.ok(apierror.toString());

            }

            return ResponseEntity.created(new URI("/api/trailupfrontpay/" + result2.getId().toString()))
                .headers(HeaderUtil.createAlert( "A Trailupfrontpay is created with identifier " + result2.getId().toString(), result2.getId().toString()))
                .body(result);
        }
    }

    private void updateProductTransaction(Trailupfrontpay trailupfrontpay) {
    }

    @PostMapping("/trailupfrontpay-repeat")
    @Timed
    public ResponseEntity<?> repeatTrailupfront(@RequestBody Trailupfrontpay trailupfrontpay) throws URISyntaxException {
        log.debug("REST request to save Trailupfront : {}", trailupfrontpay);
        GenericPayTrailUpfront genericPayTrailUpfront= new GenericPayTrailUpfront();


    if(trailupfrontpay.getProductName().equals("AIF & PMS")){
        updatePaymentTransaction(trailupfrontpay);
        Trailupfrontpay result = trailupfrontpayRepository.save(trailupfrontpay);
        return ResponseEntity.created(new URI("/api/trailupfrontpay/" + result.getId().toString()))
            .headers(HeaderUtil.createAlert( "A Trailupfrontpay is created with identifier " + result.getId().toString(), result.getId().toString()))
            .body(result);}
        else{
        Product product=productRepository.findByProductName(trailupfrontpay.getProductName());
     genericPayTrailUpfront.setBankName(trailupfrontpay.getBankName());
     genericPayTrailUpfront.setChequeDate(trailupfrontpay.getChequeDate());
     genericPayTrailUpfront.setChequeNo(trailupfrontpay.getChequeNo());
     genericPayTrailUpfront.setDistributorMaster(trailupfrontpay.getDistributorMaster());
     genericPayTrailUpfront.setPaymentDate(trailupfrontpay.getPaymentDate());
     genericPayTrailUpfront.setPayAmount(trailupfrontpay.getPaymentAmount());
     genericPayTrailUpfront.setPayType(trailupfrontpay.getPaymentType());
     genericPayTrailUpfront.setProduct(product);
        genericPayTrailUpfront =genericPayTrailUpfrontRepository.save(genericPayTrailUpfront);
        return ResponseEntity.created(new URI("/api/trailupfrontpay/" + genericPayTrailUpfront.getId().toString()))
            .headers(HeaderUtil.createAlert( "A Trailupfrontpay is created with identifier " + genericPayTrailUpfront.getId().toString(), genericPayTrailUpfront.getId().toString()))
            .body(genericPayTrailUpfront);
 }
    }

        private void updatePaymentTransaction(Trailupfrontpay trailupfrontpay) {
        FeeTrailUpfrontTrans feeTrailUpfrontTrans = new FeeTrailUpfrontTrans();
        feeTrailUpfrontTrans = new FeeTrailUpfrontTrans();
        feeTrailUpfrontTrans.setDistributoMaster(trailupfrontpay.getDistributorMaster());
        feeTrailUpfrontTrans.setTransDate(trailupfrontpay.getChequeDate());
        feeTrailUpfrontTrans.setPadiAmount(trailupfrontpay.getPaymentAmount());
        feeTrailUpfrontTrans.setTrailOpeningBalance(0f);
        feeTrailUpfrontTrans.setTrailClosingBalance(0f);
        feeTrailUpfrontTrans.setUpfrontOpeningBalance(0f);
        feeTrailUpfrontTrans.setUpfrontClosingBalance(0f);
        feeTrailUpfrontTrans.setUpfrontAmount(0f);
        feeTrailUpfrontTrans.setDetailsUpdatedFlag(0);
        feeTrailUpfrontTrans.setTrailAmount(0f);
        feeTrailUpfrontTrans.setPayableAmount(0f);
        feeTrailUpfrontTransRepository.save(feeTrailUpfrontTrans);
    }

    @GetMapping("/payment/dist-masters")
    @Timed
    public List<DistributorMaster> getDistributors() {
        List<DistributorMaster> distributorMasters=new ArrayList<>();
        Optional<User> user = userResource.getCurrentUse(SecurityUtils.getCurrentUserLogin().get());
        if(user.get().getDistributorMaster()!=null)
        {
            distributorMasters = distributorMasterRepository.findByDistName(user.get().getDistributorMaster().getDistName());
        }
        if(user.get().getRelationshipManager() == null && user.get().getDistributorMaster() ==null){
            distributorMasters = distributorMasterRepository.findAll();

        }
        return distributorMasters;
    }

    @PostMapping("/download-payment")
    @Timed
    public ResponseEntity<?> downloadPayment(@RequestBody ViewPaymentBean viewPaymentBean) throws IOException {
        fileDownload = new ArrayList<>();
        if(viewPaymentBean.getRelationshipManagerList()!=null){
       for(RelationshipManager relationshipManager : viewPaymentBean.getRelationshipManagerList()){
           List<DistributorMaster> distributorMasterList = distributorMasterRepository.findRelationshipManager(relationshipManager.getId());
           for(DistributorMaster distributorMaster :distributorMasterList){
               trailUpfrontPayService.trailUpfrontPaid(viewPaymentBean.getStartDate(), viewPaymentBean.getEndDate(), relationshipManager, distributorMaster,fileDownload);
           }
           trailUpfrontPayService.downloadPayment(fileDownload,viewPaymentBean);
       }

   }
   if(viewPaymentBean.getDistributorMasterList()!=null){
       RelationshipManager relationshipManager = null;
       for(DistributorMaster distributorMaster :viewPaymentBean.getDistributorMasterList()){
           trailUpfrontPayService.trailUpfrontPaid(viewPaymentBean.getStartDate(), viewPaymentBean.getEndDate(), relationshipManager, distributorMaster,fileDownload);
       }
       trailUpfrontPayService.downloadPayment(fileDownload,viewPaymentBean);
   }

       return null;
    }

    @GetMapping("/payment-dist")
    @ResponseBody
    public ResponseEntity downloadDistributor() throws Exception {
        System.out.println(fileDownload);
        String filePath = prop.getString("fee.file.folder") + "DFA Backup\\\\"
            + prop.getString("pay.trail.file.zip")+"\\\\"+"FileDownload.zip";

        File file = new File(filePath);
        System.out.println(file.getName());
        if (file.exists()) {
            return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=" + file.getName())
                .contentLength(file.length())
                .lastModified(file.lastModified())
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(new FileSystemResource(file));
        }

        return null;
    }

    @GetMapping("/payment-rm")
    @ResponseBody
    public ResponseEntity downloadRelationManager() throws Exception {
        System.out.println(fileDownload);
        String filePath = prop.getString("fee.file.folder") + "DFA Backup\\\\" + prop.getString("rm.paid.amt.zip")
            +"\\\\"+"FileDownload.zip";
        File file = new File(filePath);
        System.out.println(file.getName());
        if (file.exists()) {
            return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=" + file.getName())
                .contentLength(file.length())
                .lastModified(file.lastModified())
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(new FileSystemResource(file));
        }

        return null;
    }


}
