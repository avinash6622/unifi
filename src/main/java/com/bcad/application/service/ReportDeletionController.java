package com.bcad.application.service;

import com.bcad.application.domain.*;
import com.bcad.application.repository.*;
import com.codahale.metrics.annotation.Timed;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URISyntaxException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api")
public class ReportDeletionController {

    private final ReportsDistributorFeeRepository reportsDistributorFeeRepository;
    private final ReportGenerationRepository reportGenerationRepository;
    private final ReportAIFCalcRepository reportAIFCalcRepository;
    private final ReportAIFQuarterFourRepository reportAIFQuarterFourRepository;
    private final ReportAIFQuarterFourPerRepository reportAIFQuarterFourPerRepository;
    private final ReportAIFQuarterFourFinancialRepository reportAIFQuarterFourFinancialRepository;
    private final ReportAIFQuarterFourManageRepository reportAIFQuarterFourManageRepository;
    private final ReportAIFQuarterFourPerformanceRepository reportAIFQuarterFourPerformanceRepository;
    private final ReportBrokeragePMSRepository reportBrokeragePMSRepository;
    private final ReportProfitShareRepository reportProfitShareRepository;
    private final ReportBcadMonthlyCalculationRepository reportBcadMonthlyCalculationRepository;
    private final AIFBlendMonthlyCalculationRepository aifBlendMonthlyCalculationRepository;
    private final AIF2MonthlyCalculationRepository aif2MonthlyCalculationRepository;
    private final DistributorMasterRepository distributorMasterRepository;
    private final AIFDistributorFeeRepository aifDistributorFeeRepository;
    private final BCADDistributorFeeRepository bcadDistributorFeeRepository;
    private final AifUmbrellaCalculationRepository aifUmbrellaCalculationRepository;

    public ReportDeletionController(ReportsDistributorFeeRepository reportsDistributorFeeRepository, ReportGenerationRepository reportGenerationRepository, ReportAIFCalcRepository reportAIFCalcRepository,
                                    ReportAIFQuarterFourRepository reportAIFQuarterFourRepository, ReportAIFQuarterFourPerRepository reportAIFQuarterFourPerRepository,
                                    ReportAIFQuarterFourFinancialRepository reportAIFQuarterFourFinancialRepository, ReportAIFQuarterFourManageRepository reportAIFQuarterFourManageRepository,
                                    ReportAIFQuarterFourPerformanceRepository reportAIFQuarterFourPerformanceRepository, ReportBrokeragePMSRepository reportBrokeragePMSRepository, ReportProfitShareRepository reportProfitShareRepository,
                                    ReportBcadMonthlyCalculationRepository reportBcadMonthlyCalculationRepository, AIFBlendMonthlyCalculationRepository aifBlendMonthlyCalculationRepository,
                                    AIF2MonthlyCalculationRepository aif2MonthlyCalculationRepository,DistributorMasterRepository distributorMasterRepository,
                                    AIFDistributorFeeRepository aifDistributorFeeRepository,BCADDistributorFeeRepository bcadDistributorFeeRepository,AifUmbrellaCalculationRepository aifUmbrellaCalculationRepository) {
        this.reportsDistributorFeeRepository = reportsDistributorFeeRepository;
        this.reportGenerationRepository = reportGenerationRepository;
        this.reportAIFCalcRepository = reportAIFCalcRepository;
        this.reportAIFQuarterFourRepository = reportAIFQuarterFourRepository;
        this.reportAIFQuarterFourPerRepository = reportAIFQuarterFourPerRepository;
        this.reportAIFQuarterFourFinancialRepository = reportAIFQuarterFourFinancialRepository;
        this.reportAIFQuarterFourManageRepository = reportAIFQuarterFourManageRepository;
        this.reportAIFQuarterFourPerformanceRepository = reportAIFQuarterFourPerformanceRepository;
        this.reportBrokeragePMSRepository = reportBrokeragePMSRepository;
        this.reportProfitShareRepository = reportProfitShareRepository;
        this.reportBcadMonthlyCalculationRepository = reportBcadMonthlyCalculationRepository;
        this.aifBlendMonthlyCalculationRepository = aifBlendMonthlyCalculationRepository;
        this.aif2MonthlyCalculationRepository = aif2MonthlyCalculationRepository;
        this.distributorMasterRepository = distributorMasterRepository;
        this.aifDistributorFeeRepository = aifDistributorFeeRepository;
        this.bcadDistributorFeeRepository=bcadDistributorFeeRepository;
        this.aifUmbrellaCalculationRepository=aifUmbrellaCalculationRepository;
    }

    @PostMapping("/report-deletion")
    @Timed
    public ResponseEntity<?> deleteReports(@RequestBody ReportDeletion reportDeletion) throws URISyntaxException, ParseException {

        if (reportDeletion.getDistributorMasterList() != null) {
           for(DistributorMaster dm :reportDeletion.getDistributorMasterList()){
              deleteProductReports(dm,reportDeletion);
           }
        }

        if (reportDeletion.getRelationshipManagerList()!= null) {

           for(RelationshipManager rm : reportDeletion.getRelationshipManagerList()){
               List<DistributorMaster> distributorMasters = distributorMasterRepository.findRelationshipManager(rm.getId());
               for(DistributorMaster dm :reportDeletion.getDistributorMasterList()){
                   deleteProductReports(dm,reportDeletion);
               }
           }
        }


        return null;

    }

    private void deleteProductReports(DistributorMaster dm, ReportDeletion reportDeletion) throws ParseException {

        DateFormat dateForm = new SimpleDateFormat("yyyy-MM-dd");
        DateFormat dateFormat = new SimpleDateFormat("MMM YYYY");
        String sFrom = dateForm.format(reportDeletion.getStartDate());
        String sTo = dateForm.format(reportDeletion.getEndDate());

        List<String> aifDate = new ArrayList<String>();
        String from="";
        Calendar beginCalendar = Calendar.getInstance();
        Calendar finishCalendar = Calendar.getInstance();

        beginCalendar.setTime(reportDeletion.getStartDate());
        finishCalendar.setTime(reportDeletion.getEndDate());
        System.out.println(beginCalendar.getTime());
        System.out.println(finishCalendar.getTime());

        while (!beginCalendar.after(finishCalendar)) {
            from = dateFormat.format(beginCalendar.getTime());
            aifDate.add(from);
            beginCalendar.add(Calendar.MONTH, 1);

        }
        List<ReportGeneration> reportGeneration = reportGenerationRepository.findDistributorReport(0,sFrom,sTo,dm.getId());
        for(ReportGeneration reports : reportGeneration){
            if(reports.getReportType().equals("Brokerage PMS")){
                reportBrokeragePMSRepository.deleteBrokerage(reports.getId());
            }
            if(reports.getReportType().equals("Profit Share")){
                reportProfitShareRepository.deleteProfit(reports.getId());
            }
            if(reports.getReportType().equals("AIF")){
                reportAIFCalcRepository.deleteAIF(reports.getId());
            }
            reportGenerationRepository.delete(reports);
        }
             reportAIFQuarterFourRepository.deleteAIF(dm.getId(),aifDate);
             reportAIFQuarterFourPerRepository.deleteAIF(dm.getId(),aifDate);
             reportAIFQuarterFourFinancialRepository.deleteAIF(dm.getId(),aifDate);
             reportAIFQuarterFourManageRepository.deleteAIF(dm.getId(),aifDate);
             reportAIFQuarterFourPerformanceRepository.deleteAIF(dm.getId(),aifDate);
             reportsDistributorFeeRepository.deleteReports(sFrom,sTo,dm.getId());
             reportBcadMonthlyCalculationRepository.findDeleteReports(sFrom, sTo, dm.getId());
             aif2MonthlyCalculationRepository.findDeleteReports(sFrom,sTo,dm.getId());
             aifBlendMonthlyCalculationRepository.findDeleteReports(sFrom,sTo,dm.getId());
             bcadDistributorFeeRepository.findDeleteReports(sFrom,sTo,dm.getId());
             aifDistributorFeeRepository.findDeleteReports(sFrom,sTo,dm.getId());
             aifUmbrellaCalculationRepository.findUmbrellaDeleteReports(sFrom,sTo,dm.getId());
    }


}
