package com.bcad.application.web.rest;


import com.bcad.application.domain.TransactionReport;
import com.bcad.application.repository.TransactionReportRepository;
import com.bcad.application.service.TransactionReportService;
import com.bcad.application.web.rest.util.HeaderUtil;
import com.codahale.metrics.annotation.Timed;
import com.google.gson.JsonObject;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.net.URI;
import java.util.Date;

@RestController
@RequestMapping("/api")
public class TransactionReportResource {

    private final Logger log = LoggerFactory.getLogger(DistributorMasterResource.class);
    private final TransactionReportRepository transactionReportRepository;
    private final TransactionReportService transactionReportService;

    public TransactionReportResource(TransactionReportRepository transactionReportRepository,TransactionReportService transactionReportService)
    {
        this.transactionReportRepository=transactionReportRepository;
        this.transactionReportService=transactionReportService;
    }

    @PostMapping("/upload/transactionReport")
    public ResponseEntity<?> createUploadMasterFiles(@RequestParam @DateTimeFormat(pattern = "dd-MM-yyyy") Date startDate,
                                                     @RequestParam @DateTimeFormat(pattern = "dd-MM-yyyy") Date endDate,
                                                     @RequestParam String fileName,
                                                     @RequestParam(name = "fileUpload") MultipartFile multipartFile)
        throws Exception {
        TransactionReport transactionReport=transactionReportService.TransactionReportFileUpload(startDate,endDate,fileName,multipartFile);
        if (transactionReport.getCode() != null) {
            if (transactionReport.getCode().equals("450")) {
                JsonObject apierror = new JsonObject();
                apierror = new JsonObject();
                apierror.addProperty("error", "400");
                apierror.addProperty("message", "file not uploaded");
                return ResponseEntity.ok(apierror.toString());
            }
            if (transactionReport.getCode().equals("200")) {
                JsonObject apierror = new JsonObject();
                apierror = new JsonObject();
                apierror.addProperty("message", "file uploaded");
                return ResponseEntity.ok(apierror.toString());
            }
            if (transactionReport.getCode().equals("409")) {
                JsonObject apierror = new JsonObject();
                apierror = new JsonObject();
                apierror.addProperty("message", "File Already exists");
                return ResponseEntity.ok(apierror.toString());
            }
        }
        return ResponseEntity.created(new URI("/api/file-upload/" + transactionReport.getCode().toString()))
            .headers(HeaderUtil.createAlert("A transactionUpload is created with identifier " + transactionReport.getId().toString(),
                transactionReport.getId().toString()))
            .body(transactionReport);

    }

}
