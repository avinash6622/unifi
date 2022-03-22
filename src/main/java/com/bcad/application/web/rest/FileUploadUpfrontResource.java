package com.bcad.application.web.rest;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import com.bcad.application.domain.FileUploadAIF;
import com.bcad.application.domain.FileUploadUpfront;
import com.bcad.application.repository.FileTypeRepository;
import com.bcad.application.repository.FileUploadUpfrontRepository;
import com.bcad.application.service.UpfrontUploadService;
import com.bcad.application.web.rest.util.HeaderUtil;
import com.bcad.application.web.rest.util.PaginationUtil;
import com.codahale.metrics.annotation.Timed;

import com.google.gson.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.FileSystemResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api")
public class FileUploadUpfrontResource {

    private final Logger log = LoggerFactory.getLogger(FileUploadUpfrontResource.class);

    private final FileUploadUpfrontRepository fileUploadUpfrontRepository;
    private final UpfrontUploadService upfrontUploadService;

    private String enableDownload = "";
    public String getEnableDownload() {
        return enableDownload;
    }
    public void setEnableDownload(String enableDownload) {
        this.enableDownload = enableDownload;
    }


    public FileUploadUpfrontResource(FileUploadUpfrontRepository fileUploadUpfrontRepository, UpfrontUploadService upfrontUploadService) {
        this.fileUploadUpfrontRepository = fileUploadUpfrontRepository;
        this.upfrontUploadService = upfrontUploadService;
    }

 @PostMapping("/file-upload-upfront")
 @Timed
 public ResponseEntity<?> createUpfrontFileupload(@RequestParam @DateTimeFormat(pattern = "dd-MM-yyyy") Date startDate, @RequestParam @DateTimeFormat(pattern = "dd-MM-yyyy") Date endDate,
                                                @RequestParam String fileName,
                                                @RequestParam(name = "fileUpload") MultipartFile multipartFile)
     throws URISyntaxException, MissingServletRequestParameterException, IOException,Exception {

     FileUploadUpfront result = upfrontUploadService.uploadUpfrontFile(startDate, endDate, fileName, multipartFile);
     if(result.getCode() != null) {
         if (result.getCode().equals("202")) {
             JsonObject apierror = new JsonObject();
             apierror = new JsonObject();
             apierror.addProperty("error", "202");
             apierror.addProperty("message", "Uploaded file already exists, Delete previous file and try again");
             return ResponseEntity.ok(apierror.toString());
         }

         if (result.getCode().equals("470")) {
             setEnableDownload(result.getStatus());
             JsonObject apierror = new JsonObject();
             apierror = new JsonObject();
             apierror.addProperty("error", "470");
             apierror.addProperty("message", "file not uploaded");
             return ResponseEntity.ok(apierror.toString());
         }
     }
     return ResponseEntity.created(new URI("/api/file-upload-upfront" + result.getId().toString()))
         .headers(HeaderUtil.createAlert( "A Location is created with identifier " + result.getId().toString(), result.getId().toString()))
         .body(result);
 }
    @GetMapping("/approve-upfront-file/{id}")
    @Timed
    public FileUploadUpfront getApproveFileUpload(@PathVariable Long id) {
        FileUploadUpfront result1=new FileUploadUpfront();
        Optional<FileUploadUpfront> result = fileUploadUpfrontRepository.findById(id);
        result1 = upfrontUploadService.approveFile(result.get());
        return result1;

    }

    @GetMapping("/file-upload-upfronts")
    @Timed
    public ResponseEntity<List<FileUploadUpfront>> getAllUpfrontFileupload(Pageable pageable) {
        int i =0;
        final Page<FileUploadUpfront> page = fileUploadUpfrontRepository.findByIsDeleted(i,pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page,"/api/file-upload-upfronts");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    @DeleteMapping("/file-upload-upfront/{id}")
    @Timed
    public ResponseEntity<FileUploadUpfront> deleteUpfrontFile(@PathVariable Long id) {
        log.debug("REST request to delete FileUpload: {}", id);
        Integer result=upfrontUploadService.deleteFile(id);
        return ResponseEntity.ok().headers(HeaderUtil.createAlert("A PMSClient is deleted with identifier " + id, id.toString())).build();

    }

    @GetMapping("/download-pms-error-upfront")
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

}

