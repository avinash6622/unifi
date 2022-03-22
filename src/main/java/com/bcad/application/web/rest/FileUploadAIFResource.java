package com.bcad.application.web.rest;

import com.bcad.application.domain.FileUpload;
import com.bcad.application.domain.FileUploadAIF;
import com.bcad.application.domain.PMSClientMaster;
import com.bcad.application.repository.FileUploadAIFRepository;
import com.bcad.application.service.AIFUploadService;
import com.bcad.application.web.rest.util.HeaderUtil;
import com.bcad.application.web.rest.util.PaginationUtil;
import com.codahale.metrics.annotation.Timed;
import com.google.gson.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class FileUploadAIFResource {

    private final Logger log = LoggerFactory.getLogger(FileUploadAIFResource.class);

    private final FileUploadAIFRepository fileUploadAIFRepository;
    private final AIFUploadService aifUploadService;

    public FileUploadAIFResource(FileUploadAIFRepository fileUploadAIFRepository,AIFUploadService aifUploadService) {
        this.fileUploadAIFRepository = fileUploadAIFRepository;
        this.aifUploadService = aifUploadService;
    }

    @PostMapping("/aif-file-upload")
    @Timed
    public ResponseEntity<?> createAIFFileUpload(@RequestParam @DateTimeFormat(pattern = "dd-MM-yyyy") Date startDate, @RequestParam @DateTimeFormat(pattern = "dd-MM-yyyy") Date endDate,
                                                      @RequestParam String fileName,
                                                      @RequestParam(name = "fileUpload") MultipartFile multipartFile)
        throws URISyntaxException, MissingServletRequestParameterException, IOException,Exception {

        FileUploadAIF result = aifUploadService.uploadAIFFile(startDate,endDate,fileName,multipartFile);

        if(result.getCode() != null) {
            if(result.getCode().equals("409")){
                JsonObject apierror = new JsonObject();
                apierror = new JsonObject();
                apierror.addProperty("error", "409");
                apierror.addProperty("message", "Series Master already existed");
                return ResponseEntity.ok(apierror.toString());
            }
            if(result.getCode().equals("412")){
                JsonObject apierror = new JsonObject();
                apierror = new JsonObject();
                apierror.addProperty("error", "412");
                apierror.addProperty("message", "Management Fee already exists for this month");
                return ResponseEntity.ok(apierror.toString());
            }

            if(result.getCode().equals("410")){
                JsonObject apierror = new JsonObject();
                apierror = new JsonObject();
                apierror.addProperty("error", "410");
                apierror.addProperty("message", result.getStatus());
                return ResponseEntity.ok(apierror.toString());
            }
            if(result.getCode().equals("400")){
                JsonObject apierror = new JsonObject();
                apierror = new JsonObject();
                apierror.addProperty("error", "400");
                apierror.addProperty("message", "Selected Date is not in the same month");
                return ResponseEntity.ok(apierror.toString());
            }
            if(result.getCode().equals("401")){
                JsonObject apierror = new JsonObject();
                apierror = new JsonObject();
                apierror.addProperty("error", "401");
                apierror.addProperty("message", "File is not containing in the same month");
                return ResponseEntity.ok(apierror.toString());
            }
        }

        return ResponseEntity.created(new URI("/api/aif-file-upload" + result.getId().toString()))
            .headers(HeaderUtil.createAlert( "A Location is created with identifier " + result.getId().toString(), result.getId().toString()))
            .body(result);
    }

    @GetMapping("/approve-aif-file/{id}")
    @Timed
    public Optional<FileUploadAIF> getAIFFileUpload(@PathVariable Long id) {
        Optional<FileUploadAIF> result = fileUploadAIFRepository.findById(id);
        result = aifUploadService.approveFile(result);
        return result;

    }

    @GetMapping("/aif-file-uploads")
    @Timed
    public ResponseEntity<List<FileUploadAIF>> getAllAIFFileupload(Pageable pageable) {
        int i =0;
        final Page<FileUploadAIF> page = fileUploadAIFRepository.findByIsDeleted(i,pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page,"/api/aif-file-uploads");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    @DeleteMapping("/aif-file-upload/{id}")
    @Timed
    public ResponseEntity<FileUploadAIF> deleteAIFFile(@PathVariable Long id) {
        log.debug("REST request to delete FileUpload: {}", id);
        Integer result=aifUploadService.deleteFile(id);
        return ResponseEntity.ok().headers(HeaderUtil.createAlert("A PMSClient is deleted with identifier " + id, id.toString())).build();

    }
}
