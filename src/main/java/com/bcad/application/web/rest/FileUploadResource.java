package com.bcad.application.web.rest;

import com.bcad.application.domain.*;
import com.bcad.application.repository.*;
import com.bcad.application.service.BCADUploadService;
import com.bcad.application.service.PMSUploadService;
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

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class FileUploadResource {

    private final Logger log = LoggerFactory.getLogger(FileUploadResource.class);

    private final FileUploadRepository fileUploadRepository;
    private PMSUploadService pmsUploadService;
    private MakerBrokerageRepository makerBrokerageRepository;
    private UploadMasterFilesRepository uploadMasterFilesRepository;
    private BCADUploadService bcadUploadService;
    private MasterTypeRepository masterTypeRepository;
    private MakerPMSNavRepository makerPMSNavRepository;
    private PMSNavRepository pmsNavRepository;
    private PMSClientMasterRepository pmsClientMasterRepository;

    public FileUploadResource(FileUploadRepository fileUploadRepository, PMSUploadService pmsUploadService,
                              MakerBrokerageRepository makerBrokerageRepository,UploadMasterFilesRepository uploadMasterFilesRepository,
                              BCADUploadService bcadUploadService,MasterTypeRepository masterTypeRepository,
                              MakerPMSNavRepository makerPMSNavRepository,PMSNavRepository pmsNavRepository,PMSClientMasterRepository pmsClientMasterRepository) {
        this.fileUploadRepository = fileUploadRepository;
        this.pmsUploadService = pmsUploadService;
        this.makerBrokerageRepository = makerBrokerageRepository;
        this.uploadMasterFilesRepository = uploadMasterFilesRepository;
        this.bcadUploadService = bcadUploadService;
        this.masterTypeRepository = masterTypeRepository;
        this.makerPMSNavRepository = makerPMSNavRepository;
        this.pmsNavRepository = pmsNavRepository;
        this.pmsClientMasterRepository = pmsClientMasterRepository;
    }

    private String enableDownload = "";
    public String getEnableDownload() {
        return enableDownload;
    }

    public void setEnableDownload(String enableDownload) {
        this.enableDownload = enableDownload;
    }

    @PostMapping("/file-upload")
    @Timed
    public ResponseEntity<?> createFileUpload(@RequestParam @DateTimeFormat(pattern = "dd-MM-yyyy") Date startDate, @RequestParam @DateTimeFormat(pattern = "dd-MM-yyyy") Date endDate,
                                                   @RequestParam String fileName,
                                                   @RequestParam(name = "fileUpload") MultipartFile multipartFile)
        throws URISyntaxException, MissingServletRequestParameterException, IOException,Exception {

        FileUpload result = pmsUploadService.uploadPMSFile(startDate,endDate,fileName,multipartFile);
        System.out.println(result + "pmsuploading");

        if(result.getCode() != null) {
            if(result.getCode().equals("470")){
                setEnableDownload(result.getStatus());
                JsonObject apierror = new JsonObject();
                apierror = new JsonObject();
                apierror.addProperty("error", "470");
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
            if(result.getCode().equals("400")){
                JsonObject apierror = new JsonObject();
                apierror = new JsonObject();
                apierror.addProperty("error", "400");
                apierror.addProperty("message", "Uploaded file is not in correct month");
                return ResponseEntity.ok(apierror.toString());
            }

            if(result.getCode().equals("410")){
                JsonObject apierror = new JsonObject();
                apierror = new JsonObject();
                apierror.addProperty("error", "410");
                apierror.addProperty("message", "File is not containing the same month");
                return ResponseEntity.ok(apierror.toString());
            }
        }

        return ResponseEntity.created(new URI("/api/file-upload/" + result.getId().toString()))
            .headers(HeaderUtil.createAlert("A FileUpload is created with identifier " + result.getId().toString(), result.getId().toString()))
             .body(result);
    }

    @PostMapping("/pms-file-upload")
    @Timed
    public ResponseEntity<?>  createPMSFileUpload(@RequestParam(name = "fileUpload") MultipartFile multipartFile)
        throws URISyntaxException, MissingServletRequestParameterException, IOException,Exception {

        PMSClientMaster result = pmsUploadService.uploadPMSDetailFile(multipartFile);
        if (result.getCode() != null) {
            if (result.getCode().equals("450")) {
                setEnableDownload(result.getStatus());
                JsonObject apierror = new JsonObject();
                apierror = new JsonObject();
                apierror.addProperty("error", "450");
                apierror.addProperty("message", "file not uploaded");
                return ResponseEntity.ok(apierror.toString());
            }
            if (result.getCode().equals("420")) {
                setEnableDownload(result.getStatus());
                JsonObject apierror = new JsonObject();
                apierror = new JsonObject();
                apierror.addProperty("message", "file uploaded");
                return ResponseEntity.ok(apierror.toString());
            }
        }
        return null;
//        return ResponseEntity.created(new URI("/api/pms-file-upload/" + result.getId().toString()))
//            .headers(HeaderUtil.createAlert("A Client Master Upload is created with identifier " + result.getId().toString(),
//                result.getId().toString()))
//            .body(result);

    }

    @PostMapping("/pms-fee-update")
    @Timed
    public String pmsFeeUpdate(@RequestParam(name = "fileUpload") FileUpload fileUpload)
        throws URISyntaxException, MissingServletRequestParameterException, IOException,Exception {

        List<PMSNav> pmsNavList = pmsNavRepository.findByFileUpload(fileUpload);
        if(pmsNavList.size() !=0)
            for (PMSNav pmsNav :pmsNavList){
                String code = pmsNav.getCodeScheme();
                System.out.println(code);
                PMSClientMaster pmsClientMaster = pmsClientMasterRepository.findByClientCode(code);
                System.out.println();
                if(pmsClientMaster != null){
                    pmsNav.setPmsClientMaster(pmsClientMaster);
                    pmsNavRepository.save(pmsNav);
                }
                else {
                    System.out.println("code is not there");
                }
            }

        return "Uploaded File";

    }

    @GetMapping("/approve-file/{id}")
    @Timed
    public Optional<FileUpload> getFileUpload(@PathVariable Long id) {
        Optional<FileUpload> result = fileUploadRepository.findById(id);
        result = pmsUploadService.approveFile(result);
        return result;

    }

    @GetMapping("/file-uploads")
    @Timed
    public ResponseEntity<List<FileUpload>> getAllFileupload(Pageable pageable) {
               int i =0;
              final Page<FileUpload> page = fileUploadRepository.findByIsDeleted(i,pageable);
               HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page,"/api/file-uploads");
               return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    @DeleteMapping("/file-upload/{id}")
    @Timed
    public ResponseEntity<FileUpload> deleteFileupload(@PathVariable Long id) {
//        log.debug("REST request to delete FileUpload: {}", id);
//        Integer result=pmsUploadService.deleteFile(id);
        log.debug("REST request to delete UploadMasterFiles: {}", id);
        Optional<FileUpload> result = fileUploadRepository.findById(id);
        System.out.println(result.toString() + "result");
        System.out.println(result.toString().length());
        System.out.println("test *****");
        System.out.println(result.get().getFileType().getFileType() +"&&&&&&&");

        if(result.get().getFileType().getFileType().equals("PMS NAV"))
        {
            pmsUploadService.deletePMSFile(result.get());
            MasterType masterType=masterTypeRepository.findByFileName(result.get().getFileType().getFileType());
            UploadMasterFiles uploadMasterFiles= uploadMasterFilesRepository.findByFileLocationAndIsDeletedAndMasterType(result.get().getFileLocation(),0,masterType);
            bcadUploadService.deletePMSFile(uploadMasterFiles);
            uploadMasterFiles.setIsDeleted(1);
            uploadMasterFilesRepository.save(uploadMasterFiles);


        }
        if(result.get().getFileType().getFileType().equals("Brokerage"))
        {
            pmsUploadService.deleteBrokerageFile(result.get());
        }
        if(result.get().getFileType().getFileType().equals("Profit Share"))
        {
            pmsUploadService.deleteProfitFile(result.get());
            MasterType masterType=masterTypeRepository.findByFileName(result.get().getFileType().getFileType());

            UploadMasterFiles uploadMasterFiles= uploadMasterFilesRepository.findByFileLocationAndIsDeletedAndMasterType(result.get().getFileLocation(),0,masterType);
            if(uploadMasterFiles!=null) {
                bcadUploadService.deleteProfitFile(uploadMasterFiles);
                uploadMasterFiles.setIsDeleted(1);
                uploadMasterFilesRepository.save(uploadMasterFiles);
            }
        }
        if(result.get().getFileType().getFileType().equals("Capital Transaction"))
        {
            pmsUploadService.deleteInvestment(result.get());
        }
        result.get().setIsDeleted(1);
        fileUploadRepository.save(result.get());
        return ResponseEntity.ok().headers(HeaderUtil.createAlert("A File is deleted with identifier " + id, id.toString())).build();
    }

    @GetMapping("/download-error-pmsupload")
    @Timed
    public ResponseEntity downloadPmsErrorStatus() throws Exception {
        System.out.println("enable doanload"+enableDownload);
        String filePath = enableDownload;

        File file = new File(filePath);
        System.out.println("file path"+file.getName());
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

    @GetMapping("/insert-investment")
    @Timed
    public String investmentID()throws Exception{

        List<MakerPMSNav> makerPMSNavs = makerPMSNavRepository.getDuplicateDate();
        Optional<FileUpload> fileUpload=fileUploadRepository.findById((long)227);
        for(MakerPMSNav makerPMSNav:makerPMSNavs){
            List<PMSNav> pmsNav = pmsNavRepository.findByFileUploadAndCodeScheme(fileUpload.get(),makerPMSNav.getCodeScheme());
            pmsNav.get(0).setInvestmentMaster(makerPMSNav.getInvestmentMaster());
            pmsNav.get(0).setInvestmentDate(makerPMSNav.getInvestmentDate());
            pmsNavRepository.save(pmsNav.get(0));
        }
        return null;

    }

    @PostMapping("/upload/umbrella/managementFee")
    @Timed
    public ResponseEntity<?>  uploadUmbrellaManagementFee(@RequestParam(name = "fileUpload") MultipartFile multipartFile,@RequestParam ("productId") Long productId,
                                                          @RequestParam ("startDate")  @DateTimeFormat(pattern = "dd-MM-yyyy") Date startDate, @RequestParam ("endDate") @DateTimeFormat(pattern = "dd-MM-yyyy") Date endDate)
        throws URISyntaxException, MissingServletRequestParameterException, IOException,Exception {
        String result = pmsUploadService.uploadUmbrellaManagementFile(multipartFile, productId,startDate,endDate);
        if (result != null) {
            if (result.equals("450")) {
                JsonObject apierror = new JsonObject();
                apierror = new JsonObject();
                apierror.addProperty("error", "450");
                apierror.addProperty("message", "file not uploaded");
                return ResponseEntity.ok(apierror.toString());
            }
            if (result.equals("200")) {
                JsonObject apierror = new JsonObject();
                apierror = new JsonObject();
                apierror.addProperty("message", "file uploaded");
                return ResponseEntity.ok(apierror.toString());
            }
            if(result.equals("409")){
                JsonObject apierror = new JsonObject();
                apierror = new JsonObject();
                apierror.addProperty("error", "409");
                apierror.addProperty("message", "file already exist for this selected date");
                return ResponseEntity.ok(apierror.toString());
            }
        }
        return null;
    }
}
