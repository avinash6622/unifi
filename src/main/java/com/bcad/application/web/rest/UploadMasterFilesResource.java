package com.bcad.application.web.rest;

import com.bcad.application.domain.BCADPMSNav;
import com.bcad.application.domain.FileUploadUpfront;
import com.bcad.application.domain.Product;
import com.bcad.application.domain.UploadMasterFiles;
import com.bcad.application.repository.FileUploadUpfrontRepository;
import com.bcad.application.repository.UploadMasterFilesRepository;
import com.bcad.application.service.BCADUploadService;
import com.bcad.application.service.UpfrontUploadService;
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
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.springframework.core.io.FileSystemResource;

@RestController
@RequestMapping("/api")
public class UploadMasterFilesResource {

    private final Logger log = LoggerFactory.getLogger(UploadMasterFilesResource.class);

    private final UploadMasterFilesRepository uploadMasterFilesRepository;
    private final BCADUploadService bcadUploadService;
    private final FileUploadUpfrontRepository fileUploadUpfrontRepository;
    private final UpfrontUploadService upfrontUploadService;


    public UploadMasterFilesResource(UploadMasterFilesRepository uploadMasterFilesRepository,BCADUploadService bcadUploadService,
                                     FileUploadUpfrontRepository fileUploadUpfrontRepository,UpfrontUploadService upfrontUploadService) {
        this.uploadMasterFilesRepository = uploadMasterFilesRepository;
        this.bcadUploadService = bcadUploadService;
        this.fileUploadUpfrontRepository = fileUploadUpfrontRepository;
        this.upfrontUploadService = upfrontUploadService;
    }
    private String enableDownload = "";
    public String getEnableDownload() {
        return enableDownload;
    }

    public void setEnableDownload(String enableDownload) {
        this.enableDownload = enableDownload;
    }

    @PostMapping("/uploadmaster")
    @Timed
    public ResponseEntity<?> createUploadMasterFiles(@RequestParam @DateTimeFormat(pattern = "dd-MM-yyyy") Date startDate, @RequestParam @DateTimeFormat(pattern = "dd-MM-yyyy") Date endDate,
                                                                     @RequestParam String fileName,
                                                                     @RequestParam(name = "fileUpload") MultipartFile multipartFile,@RequestParam String product) throws Exception {
        log.debug("REST request to save UploadMasterFiles : {}");

        UploadMasterFiles result = bcadUploadService.uploadFiles(startDate,endDate,fileName,multipartFile,product);

        if(result.getCode() != null) {
            if(result.getCode().equals("470")){
                setEnableDownload(result.getStatus());
                JsonObject apierror = new JsonObject();
                apierror = new JsonObject();
                apierror.addProperty("error", "470");
                apierror.addProperty("message", "file not uploaded");
                return ResponseEntity.ok(apierror.toString());
            }
            if(result.getCode().equals("450")){
                setEnableDownload(result.getStatus());
                JsonObject apierror = new JsonObject();
                apierror = new JsonObject();
                apierror.addProperty("error", "450");
                apierror.addProperty("message", "file not uploaded");
                return ResponseEntity.ok(apierror.toString());
            }
            if(result.getCode().equals("409")){
                JsonObject apierror = new JsonObject();
                apierror = new JsonObject();
                apierror.addProperty("error", "409");
                apierror.addProperty("message", "file already exist for this selected date");
                return ResponseEntity.ok(apierror.toString());
            }
            if(result.getCode().equals("202")){
                JsonObject apierror = new JsonObject();
                apierror = new JsonObject();
                apierror.addProperty("error", "202");
                apierror.addProperty("message", "Uploaded file already exists, Delete previous file and try again");
                return ResponseEntity.ok(apierror.toString());
            }
            if(result.getCode().equals("400")){
                JsonObject apierror = new JsonObject();
                apierror = new JsonObject();
                apierror.addProperty("error", "400");
                apierror.addProperty("message", "Uploaded file is not in correct month");
                return ResponseEntity.ok(apierror.toString());
            }
            if(result.getCode().equals("204")){
                JsonObject apierror = new JsonObject();
                apierror = new JsonObject();
                apierror.addProperty("error", "204");
                apierror.addProperty("message", "Client Code Not Found");
                return ResponseEntity.ok(apierror.toString());
            }
        }


        return ResponseEntity.created(new URI("/api/uploadmaster/" + result.getId().toString()))
            .headers(HeaderUtil.createAlert( "A Location is created with identifier " + result.getId().toString(), result.getId().toString()))
            .body(result);
    }

    @GetMapping("/uploadmaster/{id}")
    @Timed
    public Optional<UploadMasterFiles> getUploadMasterFiles(@PathVariable Long id){
        Optional<UploadMasterFiles> result = uploadMasterFilesRepository.findById(id);
        result = bcadUploadService.approveFile(result);

        FileUploadUpfront resultUpfront = fileUploadUpfrontRepository.findByStroreFileLocationAndIsDeleted(result.get().getFileLocation(),0);
        resultUpfront = upfrontUploadService.approveFile(resultUpfront);
        return result;

    }
    @GetMapping("/uploadmasters")
    @Timed
    public ResponseEntity<List<UploadMasterFiles>> getALLUploadMasterFiles(Pageable pageable) {
        final Page<UploadMasterFiles> page = uploadMasterFilesRepository.findByIsDeleted(0,pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/uploadmaster");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }
    @DeleteMapping("/uploadmaster/{id}")
    @Timed
    public ResponseEntity<UploadMasterFiles> deleteUploadMasterFiles(@PathVariable Long id) {
        log.debug("REST request to delete UploadMasterFiles: {}", id);
        Optional<UploadMasterFiles> result = uploadMasterFilesRepository.findById(id);
        if(result.get().getMasterType().getFileName().equals("PMS NAV"))
        {
            bcadUploadService.deletePMSFile(result.get());

        }
        if(result.get().getMasterType().getFileName().equals("Upfront"))
        {
            bcadUploadService.deleteUpfrontFile(result.get());
            FileUploadUpfront fileUploadUpfront= fileUploadUpfrontRepository.findByStroreFileLocationAndIsDeleted(result.get().getFileLocation(),0);
            Integer result1=upfrontUploadService.deleteBCADFile(fileUploadUpfront.getId(),result.get().getFeeFromDate(),result.get().getFeeToDate());
        }
        if(result.get().getMasterType().getFileName().equals("Profit Share"))
        {
            bcadUploadService.deleteProfitFile(result.get());
        }
        result.get().setIsDeleted(1);
        uploadMasterFilesRepository.save(result.get());
        return ResponseEntity.ok().headers(HeaderUtil.createAlert("A user is deleted with identifier " + id, id.toString())).build();
    }

    @GetMapping("/download-error-upfront")
    @Timed
    public ResponseEntity downloadUpfrontErrorStatus() throws Exception {
        System.out.println(enableDownload);
        String filePath = enableDownload;

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

    @GetMapping("/download-error-pms")
    @Timed
    public ResponseEntity downloadPmsErrorStatus() throws Exception {
        System.out.println(enableDownload);
        String filePath = enableDownload;

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
