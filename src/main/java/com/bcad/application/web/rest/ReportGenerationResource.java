package com.bcad.application.web.rest;

import com.bcad.application.config.PropertiesUtils;
import com.bcad.application.domain.*;
import com.bcad.application.repository.DistributorMasterRepository;
import com.bcad.application.repository.ProfitShareRepository;
import com.bcad.application.repository.ReportGenerationRepository;
import com.bcad.application.service.Calculation.PMSAndAIFReportService;
import com.bcad.application.service.Calculation.ReportGenerationService;
import com.codahale.metrics.annotation.Timed;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class ReportGenerationResource {

    private final Logger log = LoggerFactory.getLogger(AuthorityResource.class);
    private List<String> fileDownload = new ArrayList<>();

    private final ReportGenerationRepository reportGenerationRepository;
    private final ReportGenerationService reportGenerationService;
    private final DistributorMasterRepository distributorMasterRepository;
    private final PMSAndAIFReportService pmsAndAIFReportService;
    private final ProfitShareRepository profitShareRepository;

    public ReportGenerationResource(ReportGenerationRepository reportGenerationRepository, ReportGenerationService reportGenerationService,
                                    DistributorMasterRepository distributorMasterRepository, PMSAndAIFReportService pmsAndAIFReportService,
                                    ProfitShareRepository profitShareRepository) {
        this.reportGenerationRepository = reportGenerationRepository;
        this.reportGenerationService = reportGenerationService;
        this.distributorMasterRepository = distributorMasterRepository;
        this.pmsAndAIFReportService = pmsAndAIFReportService;
        this.profitShareRepository = profitShareRepository;
    }

    PropertiesConfiguration prop = new PropertiesUtils().getProperties("application.properties");
    List<String> fileDownloadRM = new ArrayList<>();

    //
//    @PostMapping("/report-generate")
//    @Timed
//    public ResponseEntity<ReportGeneration> reportCreated(@RequestBody ReportGeneration reportGeneration) throws URISyntaxException,Exception {
//        log.debug("REST request to save User : {}", reportGeneration);
//        reportGenerationService.bcadReport(reportGeneration);
//
//        ReportGeneration result = null;
//
//        return ResponseEntity.created(new URI("/api/report-generate/" + result.getDistributorMaster().getDistName()))
//            .headers(HeaderUtil.createAlert( "A Report Generate is created with identifier " + result.getDistributorMaster().getDistName(), result.getDistributorMaster().getDistName()))
//            .body(result);
//
//    }
    @PostMapping("/report-generates")
    @Timed
    public String reportGenerationCreation(@RequestBody ReportGeneration reportGeneration) throws URISyntaxException, Exception {
        log.debug("REST request to save User : {}", reportGeneration.toString());
        if (reportGeneration.getDistributorMaster() != null) {
            fileDownload = new ArrayList<>();
            List<ReportBcadMonthlyCalculation> distValue = new ArrayList<>();
            List<AIF2MonthlyCalculation> aif2DistValue = new ArrayList<>();
            List<AIFBlendMonthlyCalculation> aifBlendDistValue = new ArrayList<>();
            List<AIFUmbrella> umbrellaAIF = new ArrayList<>();

            for (DistributorMaster dm : reportGeneration.getDistributorMaster()) {
                Optional<DistributorMaster> distributorMaster = distributorMasterRepository.findById(dm.getId());
                reportGeneration.setDistributorMaster1(distributorMaster.get());
                distValue = reportGenerationService.bcadReport(reportGeneration);
                aif2DistValue = reportGenerationService.aif2Report(reportGeneration);
                aifBlendDistValue = reportGenerationService.aifBlendReport(reportGeneration);
                umbrellaAIF = reportGenerationService.aifUmbrella2Report(reportGeneration);

                reportGenerationService.generateReports(distValue, aif2DistValue, aifBlendDistValue, umbrellaAIF,distributorMaster.get(), reportGeneration, fileDownload);
            }

            if (reportGeneration.getRelationshipManager() == null) {
                reportGenerationService.compressZip(fileDownload, reportGeneration);
            }
        }
        if (reportGeneration.getRelationshipManager() != null) {
            fileDownloadRM = new ArrayList<>();
            for (RelationshipManager rm : reportGeneration.getRelationshipManager()) {
                String sFilesDirectory = prop.getString("fee.file.folder") + "DFA Backup\\\\" + prop.getString("file.bcad.output") + "\\\\"
                    + prop.getString("rm.generate.folder") + "\\\\" + rm.getRmName();
                FileUtils.deleteDirectory(new File(sFilesDirectory));
                reportGeneration.setRelationManage(rm);
                List<DistributorMaster> distributorMasters = distributorMasterRepository.findRelationshipManager(rm.getId());
                if (distributorMasters.size() != 0) {
                    fileDownloadRM.add(sFilesDirectory);
                }
                for (DistributorMaster dm : distributorMasters) {
                    reportGeneration.setDistributorMaster1(dm);
                    List<ReportBcadMonthlyCalculation> distValue = reportGenerationService.bcadReport(reportGeneration);
                    List<AIF2MonthlyCalculation> aif2DistValue = reportGenerationService.aif2Report(reportGeneration);
                    List<AIFBlendMonthlyCalculation> aifBlendDistValue = reportGenerationService.aifBlendReport(reportGeneration);
                    List<AIFUmbrella> aifUmbrellas= reportGenerationService.aifUmbrella2Report(reportGeneration);
                    reportGenerationService.generateReports(distValue, aif2DistValue, aifBlendDistValue, aifUmbrellas,dm, reportGeneration, fileDownload);
                }

                reportGenerationService.DirectoryZip(fileDownloadRM, reportGeneration);
            }
        }
        return "Generated";

    }

    @GetMapping("/download")
    @ResponseBody
    public ResponseEntity downloadData() throws Exception {
        System.out.println(fileDownload);
        String filePath = prop.getString("fee.file.folder") + "DFA Backup\\\\BCAD Zip Files\\\\FileDownload.zip";

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

    @GetMapping("/downloadRm")
    @ResponseBody
    public ResponseEntity downloadRmData() throws Exception {
        System.out.println(fileDownload);
        String filePath = prop.getString("fee.file.folder") + "DFA Backup\\\\BCAD Zip RmFiles\\\\RM Report\\\\rm.zip";
        File file = new File(filePath);
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

    /* @PostMapping("/profit_share")
     @Timed
     public List<ProfitShare> getResult(@RequestBody DistributorMaster distributorMaster, @RequestParam String startDte, @RequestParam String endDate){
         List<ProfitShare> result = profitShareRepository.getProfitShareByDistBetween(distributorMaster.getId(),startDte,endDate);
         return result;
     }*/
    @GetMapping("excel-pdf")
    public String converExcelToPDF() {
        return null;
    }
}


