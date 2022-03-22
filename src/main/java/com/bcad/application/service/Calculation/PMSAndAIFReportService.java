package com.bcad.application.service.Calculation;

import com.bcad.application.config.PropertiesUtils;
import com.bcad.application.domain.*;
import com.bcad.application.repository.*;
import com.bcad.application.service.InvestorViewService;
import com.bcad.application.service.PMSService;
import com.bcad.application.web.rest.util.StyleUtil;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
@Transactional(noRollbackFor = Exception.class)
public class PMSAndAIFReportService {

    @PersistenceContext
    EntityManager entityManager;

    private final Logger log = LoggerFactory.getLogger(PMSAndAIFReportService.class);

    List<DistributorPMSBean> distributorPMSBeans = new ArrayList<DistributorPMSBean>();
    List<CumulativePmsAifBean> cumulativePmsAifBeans = new ArrayList<CumulativePmsAifBean>();
    private List<ReportAifDistBean> reportAifDistBean;
    private List<ReportQFourBean> reportQFourBean;
    private List<ReportQFourFinancialBean> reportQFourFinancialBean;
    private List<ReportQFourPerBean> reportQFourPerBean;
    private List<ReportQFourManageBean> reportQFourManageBean;
    private List<ReportQFourPerformanceBean> reportQFourPerformanceBean;


    private final ReportGenerationRepository reportGenerationRepository;
    private final BrokerageRepository brokerageRepository;

    private final ProfitShareRepository profitShareRepository;
    private final PMSNavRepository pmsNavRepository;
    private final DistributorMasterRepository distributorMasterRepository;
    private final PMSClientMasterRepository pmsClientMasterRepository;
    private final UpfrontMasterRepository upfrontMasterRepository;
    private final ClientFeeCommissionRepository clientFeeCommissionRepository;
    private final ReportBrokeragePMSRepository reportBrokeragePMSRepository;
    private final ReportProfitShareRepository reportProfitShareRepository;
    private final ReportsDistributorFeeRepository reportsDistributorFeeRepository;
    private final ReportAIFCalcRepository reportAIFCalcRepository;
    private final ReportAIFQuarterFourRepository reportAIFQuarterFourRepository;
    private final ReportAIFQuarterFourFinancialRepository reportAIFQuarterFourFinancialRepository;
    private final ReportAIFQuarterFourPerRepository reportAIFQuarterFourPerRepository;
    private final ReportAIFQuarterFourManageRepository reportAIFQuarterFourManageRepository;
    private final ReportAIFQuarterFourPerformanceRepository reportAIFQuarterFourPerformanceRepository;
    private final AIFUpdatedUnitsRepository aifUpdatedUnitsRepository;
    private final InvestorViewService investorViewService;
    private final InvestorViewRepository investorViewRepository;
    private final AIFManagePerMonthRepository aifManagePerMonthRepository;
    private final FeeTrailUpfrontTransRepository feeTrailUpfrontTransRepository;
    private final DistributorOpeningBalRepository distributorOpeningBalRepository;
    private final AIFClientMasterRepository aifClientMasterRepository;
    private final SeriesMasterRepository seriesMasterRepository;
    private final FeeTrailUpfrontTransService feeTrailUpfrontTransService;
    private final PMSService pmsService;
    private final CommissionDefinitionRepository commissionDefinitionRepository;
    private final ProductRepository productRepository;
    private final SummarySheetService summarySheetService;

    public PMSAndAIFReportService(ReportGenerationRepository reportGenerationRepository, BrokerageRepository brokerageRepository, ProfitShareRepository profitShareRepository,
                                  PMSNavRepository pmsNavRepository, DistributorMasterRepository distributorMasterRepository,
                                  PMSClientMasterRepository pmsClientMasterRepository, UpfrontMasterRepository upfrontMasterRepository,
                                  ClientFeeCommissionRepository clientFeeCommissionRepository, ReportBrokeragePMSRepository reportBrokeragePMSRepository,
                                  ReportProfitShareRepository reportProfitShareRepository,
                                  ReportsDistributorFeeRepository reportsDistributorFeeRepository, ReportAIFCalcRepository reportAIFCalcRepository,
                                  ReportAIFQuarterFourRepository reportAIFQuarterFourRepository, ReportAIFQuarterFourFinancialRepository reportAIFQuarterFourFinancialRepository,
                                  ReportAIFQuarterFourPerRepository reportAIFQuarterFourPerRepository, ReportAIFQuarterFourManageRepository reportAIFQuarterFourManageRepository,
                                  ReportAIFQuarterFourPerformanceRepository reportAIFQuarterFourPerformanceRepository, AIFUpdatedUnitsRepository aifUpdatedUnitsRepository,
                                  InvestorViewService investorViewService, InvestorViewRepository investorViewRepository, AIFManagePerMonthRepository aifManagePerMonthRepository,
                                  FeeTrailUpfrontTransRepository feeTrailUpfrontTransRepository, DistributorOpeningBalRepository distributorOpeningBalRepository,
                                  AIFClientMasterRepository aifClientMasterRepository, SeriesMasterRepository seriesMasterRepository, FeeTrailUpfrontTransService feeTrailUpfrontTransService,
                                  PMSService pmsService, CommissionDefinitionRepository commissionDefinitionRepository, ProductRepository productRepository,
                                  SummarySheetService summarySheetService) {
        this.reportGenerationRepository = reportGenerationRepository;
        this.brokerageRepository = brokerageRepository;
        this.profitShareRepository = profitShareRepository;
        this.pmsNavRepository = pmsNavRepository;
        this.distributorMasterRepository = distributorMasterRepository;
        this.pmsClientMasterRepository = pmsClientMasterRepository;
        this.upfrontMasterRepository = upfrontMasterRepository;
        this.clientFeeCommissionRepository = clientFeeCommissionRepository;
        this.reportBrokeragePMSRepository = reportBrokeragePMSRepository;
        this.reportProfitShareRepository = reportProfitShareRepository;
        this.reportsDistributorFeeRepository = reportsDistributorFeeRepository;
        this.reportAIFCalcRepository = reportAIFCalcRepository;
        this.reportAIFQuarterFourRepository = reportAIFQuarterFourRepository;
        this.reportAIFQuarterFourFinancialRepository = reportAIFQuarterFourFinancialRepository;
        this.reportAIFQuarterFourPerRepository = reportAIFQuarterFourPerRepository;
        this.reportAIFQuarterFourManageRepository = reportAIFQuarterFourManageRepository;
        this.reportAIFQuarterFourPerformanceRepository = reportAIFQuarterFourPerformanceRepository;
        this.aifUpdatedUnitsRepository = aifUpdatedUnitsRepository;
        this.investorViewService = investorViewService;
        this.investorViewRepository = investorViewRepository;
        this.aifManagePerMonthRepository = aifManagePerMonthRepository;
        this.feeTrailUpfrontTransRepository = feeTrailUpfrontTransRepository;
        this.distributorOpeningBalRepository = distributorOpeningBalRepository;
        this.aifClientMasterRepository = aifClientMasterRepository;
        this.seriesMasterRepository = seriesMasterRepository;
        this.feeTrailUpfrontTransService = feeTrailUpfrontTransService;
        this.pmsService = pmsService;
        this.commissionDefinitionRepository = commissionDefinitionRepository;
        this.productRepository = productRepository;
        this.summarySheetService = summarySheetService;
    }

    PropertiesConfiguration prop = new PropertiesUtils().getProperties("application.properties");

    public void generateDistributorReports(DistributorMaster dm, ReportGeneration reportGeneration, HSSFSheet sheetSummary, HSSFSheet sheetPMS, HSSFSheet sheetAIF, String sStartTime, String sEndTime, HSSFWorkbook workBook, CumulativeAIFSeriesBCAD cumulativeAIFSeriesBCAD) throws Exception {
        Float pmsComm = 0.0f;
        String modelType = "";
        Integer sFlag=0;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String sStartDate = sdf.format(reportGeneration.getStartDate());
        String sToDate = sdf.format(reportGeneration.getToDate());
        List<ReportGeneration> reportFees = reportGenerationRepository.findDistributorReport(0, sStartDate, sToDate,
            dm.getId());

        //DistributorMaster distributorMaster = distributorMasterRepository.findByDistributorName(dm.getDistName());
        Product product = productRepository.findByProductName("PMS");
        Product productBCAD = productRepository.findByProductName("BCAD");
        CommissionDefinition commissionDefinition = commissionDefinitionRepository.findCommissions(dm.getId(), product.getId());
        List<CommissionDefinition> checkOption3=commissionDefinitionRepository.findOption3Commission(
            dm.getId(),productBCAD.getId());
        if(checkOption3!=null)
                sFlag=1;
        ArrayList<HSSFSheet> sheetArrayList=new ArrayList<>();

        if (reportFees.size() == 0 && commissionDefinition.getNavComm() != 0.0f && !dm.getDistModelType().equals("")) {
            distributorPMSBeans = new ArrayList<DistributorPMSBean>();
            cumulativePmsAifBeans = new ArrayList<CumulativePmsAifBean>();
            List<PMSClientMaster> pmsClientMasters = pmsClientMasterRepository.findByDistributorMaster(dm);

            for (PMSClientMaster pmsClientMaster : pmsClientMasters) {
                DistributorPMSBean bean = null;

                Double brokage = brokerageRepository.getPeriodBrokerage(sStartDate, sToDate, pmsClientMaster.getId());

                if (brokage != null) {
                    bean = new DistributorPMSBean();
                    bean.setClientCode(pmsClientMaster.getClientCode());
                    bean.setClientName(pmsClientMaster.getClientName());
                    bean.setTotal(Float.valueOf(brokage.toString()));
                    bean.setPmsClientMaster(pmsClientMaster);
                    bean.setClientFeeCommission(clientFeeCommissionRepository.findByPmsClientMaster(bean.getPmsClientMaster()));

                    Double pmsNav = pmsNavRepository.getPMSFeeBeforeDate(pmsClientMaster.getId(),sStartDate, sToDate);
                    if(pmsNav!= null)
                        bean.setPmsFee(pmsNav.toString());
                    /*
                     // Merge all the strategy code
                    List<PMSNav> pmsNavs = pmsNavRepository.findByPmsClientMasterAndStartDateBetweenAndIsDeleted(pmsClientMaster, reportGeneration.getStartDate(), reportGeneration.getToDate(), 0);

                    if (pmsNavs.isEmpty() != true) {
                        Float fee = 0.0f;
                        for (PMSNav pmsNav : pmsNavs) {
                            fee += pmsNav.getCalcPmsNav();
                        }
                        bean.setPmsFee(fee.toString());
                    }*/ else {
                        bean.setPmsFee("0");
                    }
                    if (dm.getDistModelType().equals("Upfront")) {
                        List<UpfrontMaster> upfrontMasters = upfrontMasterRepository.findByPmsClientMasterAndTransDateBetweenAndIsDeleted(pmsClientMaster,
                            reportGeneration.getStartDate(), reportGeneration.getToDate(), 0);
                        List<Date> transDates = upfrontMasterRepository.getTrans(pmsClientMaster.getId(), reportGeneration.getStartDate(), reportGeneration.getToDate());
                        System.out.println(transDates);
                        Float sInitial = 0.0f;
                        Float sAdditional = 0.0f;
                        Float totCorpus = 0.0f;
                        Float totShareCorpus = 0.0f;
                        String transtDates = "";

                        if (upfrontMasters.isEmpty() != true) {
                            for (UpfrontMaster upfrontMaster : upfrontMasters) {
                                sInitial += upfrontMaster.getInitialFund();
                                sAdditional += upfrontMaster.getAdditionalFund();
                            }
                            totCorpus = sInitial + sAdditional;
                            totShareCorpus = totCorpus / 100;

                            DateFormat df = new SimpleDateFormat("dd/MM/yyyy");

                            if (transDates.size() != 0) {
                                for (Date dates : transDates) {
                                    String dTrans = df.format(dates);
                                    transtDates = transtDates + dTrans.concat(" & ");
                                }
                                transtDates = transtDates.substring(0, transtDates.length() - 2);
                                System.out.println(transtDates);
                            }

                        }
                        bean.setInitialFund(sInitial);
                        bean.setAddFund(sAdditional);
                        bean.setTotCorpus(totCorpus);
                        bean.setTotShareCorpus(totShareCorpus);
                        bean.setTransDate(transtDates);

                    }
                    distributorPMSBeans.add(bean);
                }
                if (brokage == null) {
                    /**
                     * adding when nav available brokerage not available
                     */
                    Double pmsNav = pmsNavRepository.getPMSFeeBeforeDate(pmsClientMaster.getId(),sStartDate, sToDate);
                    //Strategy Before Date
                    //List<PMSNav> pmsNavs = pmsNavRepository.findByPmsClientMasterAndStartDateBetweenAndIsDeleted(pmsClientMaster, reportGeneration.getStartDate(), reportGeneration.getToDate(), 0);
                    if (pmsNav!=null) {
                        bean = new DistributorPMSBean();
                        bean.setClientCode(pmsClientMaster.getClientCode());
                        bean.setClientName(pmsClientMaster.getClientName());
                        bean.setTotal(0f);
                        bean.setPmsClientMaster(pmsClientMaster);
                        bean.setClientFeeCommission(clientFeeCommissionRepository.findByPmsClientMaster(bean.getPmsClientMaster()));

                       /* Float fee = 0.0f;
                        for (PMSNav pmsNav : pmsNavs) {
                            fee += pmsNav.getCalcPmsNav();
                        }*/
                        bean.setPmsFee(pmsNav.toString());
                        // Upfront calculation
                        if (dm.getDistModelType().equals("Upfront")) {
                            List<UpfrontMaster> upfrontMasters = upfrontMasterRepository.findByPmsClientMasterAndTransDateBetweenAndIsDeleted(pmsClientMaster,
                                reportGeneration.getStartDate(), reportGeneration.getToDate(), 0);
                            List<Date> transDates = upfrontMasterRepository.getTrans(pmsClientMaster.getId(), reportGeneration.getStartDate(), reportGeneration.getToDate());
                            System.out.println(transDates);
                            Float sInitial = 0.0f;
                            Float sAdditional = 0.0f;
                            Float totCorpus = 0.0f;
                            Float totShareCorpus = 0.0f;
                            String transtDates = "";
                            if (upfrontMasters.isEmpty() != true) {

                                for (UpfrontMaster upfrontMaster : upfrontMasters) {
                                    sInitial += upfrontMaster.getInitialFund();
                                    sAdditional += upfrontMaster.getAdditionalFund();
                                }
                                totCorpus = sInitial + sAdditional;
                                totShareCorpus = totCorpus / 100;

                                DateFormat df = new SimpleDateFormat("dd/MM/yyyy");

                                if (transDates.size() != 0) {
                                    for (Date dates : transDates) {
                                        String dTrans = df.format(dates);
                                        transtDates = dTrans.concat(" &");
                                    }
                                    transtDates = transtDates.substring(0, transtDates.length() - 2);
                                    System.out.println(transtDates);
                                }
                            }
                            bean.setInitialFund(sInitial);
                            bean.setAddFund(sAdditional);
                            bean.setTotCorpus(totCorpus);
                            bean.setTotShareCorpus(totShareCorpus);
                            bean.setTransDate(transtDates);
                        }

                        distributorPMSBeans.add(bean);
                    }
                }


            }
            writeBrokerageAndPMS(distributorPMSBeans, dm, cumulativePmsAifBeans, sheetSummary, sheetPMS, sheetAIF, reportGeneration, workBook, commissionDefinition, cumulativeAIFSeriesBCAD);

        }
        if (reportFees.isEmpty() == false &&  !dm.getDistModelType().equals("")) {
            cumulativePmsAifBeans = new ArrayList<CumulativePmsAifBean>();
            System.out.println(reportFees.size());
            writeExistReportPMS(reportFees, cumulativePmsAifBeans, dm, sheetSummary, sheetPMS, sheetAIF, reportGeneration, workBook, commissionDefinition, cumulativeAIFSeriesBCAD);
        }


    }

    private void writeBrokerageAndPMS(List<DistributorPMSBean> distributorPMSBeans, DistributorMaster distributorMaster, List<CumulativePmsAifBean> cumulativePmsAifBeans, HSSFSheet sheetSummary, HSSFSheet sheetPMS, HSSFSheet sheetAIF, ReportGeneration reportGeneration, HSSFWorkbook workBook, CommissionDefinition commissionDefinition, CumulativeAIFSeriesBCAD cumulativeAIFSeriesBCAD) {

        ReportBrokeragePMS reportBrokeragePMS;

        CumulativePmsAifBean cumulativePmsAifBean = new CumulativePmsAifBean();

        DateFormat dateFormat = new SimpleDateFormat("MMM yyyy");
        String sStartTime = dateFormat.format(reportGeneration.getStartDate());
        String sEndTime = dateFormat.format(reportGeneration.getToDate());
        String sFinal = sStartTime + "_to_" + sEndTime;

        ReportGeneration reportFeeBrokerage = new ReportGeneration();
        reportFeeBrokerage.setStartDate(reportGeneration.getStartDate());
        reportFeeBrokerage.setToDate(reportGeneration.getToDate());
        reportFeeBrokerage.setDetailsUpdatedFlag(0);
        reportFeeBrokerage.setDistributorMaster1(distributorMaster);
        reportFeeBrokerage.setReportType("Brokerage PMS");
        reportFeeBrokerage = reportGenerationRepository.save(reportFeeBrokerage);

        Integer sFlag=0;
        Product productBCAD = productRepository.findByProductName("BCAD");
        List<CommissionDefinition> checkOption3=commissionDefinitionRepository.findOption3Commission(
            distributorMaster.getId(),productBCAD.getId());
        if(checkOption3!=null)
            sFlag=1;

        HSSFFont defaultFont = workBook.createFont();
        defaultFont.setFontHeightInPoints((short) 11);
        defaultFont.setFontName("Calibri");

        CellStyle cs = workBook.createCellStyle();
        CellStyle csRight = workBook.createCellStyle();
        CellStyle csRightLeftRight = workBook.createCellStyle();
        CellStyle csHorVerCenter = workBook.createCellStyle();
        CellStyle csHorVerDate = workBook.createCellStyle();
        CellStyle csPlanLeftRight = workBook.createCellStyle();
        CellStyle csPlainNoBorder = workBook.createCellStyle();
        CellStyle csPercNoBorder = workBook.createCellStyle();

        HSSFFont fFont = workBook.createFont();
        fFont.setFontHeightInPoints((short) 11);
        fFont.setFontName("Calibri");
        fFont.setBold(true);
        cs.setFont(fFont);

        HSSFFont fFontNoBold = workBook.createFont();
        fFontNoBold.setFontHeightInPoints((short) 11);
        fFontNoBold.setFontName("Calibri");

        csRight.setAlignment(HorizontalAlignment.LEFT);
        // Format sheet
        csHorVerCenter.setAlignment(HorizontalAlignment.CENTER);
        csHorVerCenter.setVerticalAlignment(VerticalAlignment.CENTER);
        csHorVerCenter.setFont(fFont);
        csHorVerCenter.setWrapText(true);

        csHorVerCenter.setBorderTop(BorderStyle.THIN);
        csHorVerCenter.setBorderBottom(BorderStyle.THIN);
        csHorVerCenter.setBorderLeft(BorderStyle.THIN);
        csHorVerCenter.setBorderRight(BorderStyle.THIN);

        csHorVerDate.setAlignment(HorizontalAlignment.CENTER);
        csHorVerDate.setVerticalAlignment(VerticalAlignment.CENTER);
        csHorVerDate.setFont(defaultFont);
        csHorVerDate.setWrapText(true);

        csPlanLeftRight.setBorderLeft(BorderStyle.THIN);
        csPlanLeftRight.setBorderRight(BorderStyle.THIN);
        csPlanLeftRight.setFont(fFontNoBold);
        // Data Format
        DataFormat df = workBook.createDataFormat();
        DataFormat dfPerc = workBook.createDataFormat();
        /*
         * CellStyle csDF = workBook.createCellStyle();
         * csDF.setDataFormat(df.getFormat("0.0000"));
         */

        CellStyle csDF = StyleUtil.getStyleDataFormat(workBook);

        CellStyle csPlain = StyleUtil.getStyleDataFormat(workBook);

        CellStyle csDFBold = StyleUtil.getStyleBoldDataFormat(workBook);

        CellStyle csPerc = StyleUtil.getStylePercDataFormat(workBook);

        csDF.setBorderLeft(BorderStyle.THIN);
        csDF.setBorderRight(BorderStyle.THIN);

        csPlain.setBorderLeft(BorderStyle.THIN);
        csPlain.setBorderRight(BorderStyle.THIN);

        csDFBold.setBorderLeft(BorderStyle.THIN);
        csDFBold.setBorderRight(BorderStyle.THIN);

        csPercNoBorder = csPerc;

        csPerc.setBorderLeft(BorderStyle.THIN);
        csPerc.setBorderRight(BorderStyle.THIN);

        csPercNoBorder.setBorderLeft(BorderStyle.NONE);
        csPercNoBorder.setBorderRight(BorderStyle.NONE);

        csRight.setFont(defaultFont);
        //csHorVerCenter.setFont(defaultFont);
        csDF.setFont(defaultFont);
        csPlain.setFont(defaultFont);
        //csDFBold.setFont(defaultFont);
        csPerc.setFont(defaultFont);

        csPlainNoBorder.setFont(fFont);
        csPlainNoBorder.setBorderBottom(BorderStyle.NONE);
        csPlainNoBorder.setBorderTop(BorderStyle.NONE);
        csPlainNoBorder.setBorderLeft(BorderStyle.NONE);
        csPlainNoBorder.setBorderRight(BorderStyle.NONE);

        HSSFRow rowUserName = sheetPMS.createRow(sheetPMS.getLastRowNum() + 1);
        rowUserName.createCell(0).setCellValue(distributorMaster.getDistName().toUpperCase());
        rowUserName.getCell(0).setCellStyle(csPlainNoBorder);//cs

        HSSFRow durationFrom = sheetPMS.createRow(sheetPMS.getLastRowNum() + 1);
        durationFrom.createCell(0).setCellValue("FEES PAYABALE FOR " + sStartTime + "-" + sEndTime);
        durationFrom.getCell(0).setCellStyle(csPlainNoBorder);//cs
        // Format sheet
        sheetPMS.addMergedRegion(new CellRangeAddress(1, 1, 0, 3));
        sheetPMS.addMergedRegion(new CellRangeAddress(2, 2, 0, 3));

        int iDistCommLineNum = sheetPMS.getLastRowNum() + 1;
        boolean bIsCommissionSet = false;
        HSSFRow distComm = sheetPMS.createRow(iDistCommLineNum);
        distComm.createCell(0).setCellValue("Distributor Commission");
        distComm.getCell(0).setCellStyle(csPlainNoBorder);//cs


        sheetPMS.createFreezePane(0, 7);

        int iRowNo = sheetPMS.getLastRowNum() + 3;
        HSSFRow headingBRSBookRow = sheetPMS.createRow(iRowNo);
        // Format sheet
        headingBRSBookRow.setHeightInPoints(30);
        HSSFCell cellTemp = headingBRSBookRow.createCell(0);

        cellTemp.setCellValue(prop.getString("pms.cell.client_name"));
        cellTemp.setCellStyle(csHorVerCenter);

        // sheet.autoSizeColumn(0);
        headingBRSBookRow.createCell(1).setCellValue(prop.getString("pms.cell.client_code"));
        headingBRSBookRow.getCell(1).setCellStyle(csHorVerCenter);
        // sheet.autoSizeColumn(1);
        headingBRSBookRow.createCell(2).setCellValue(prop.getString("pms.cell.brokerage"));
        headingBRSBookRow.getCell(2).setCellStyle(csHorVerCenter);
        // sheet.autoSizeColumn(2);
        headingBRSBookRow.createCell(3).setCellValue(prop.getString("pms.cell.brokerage.fee"));
        headingBRSBookRow.getCell(3).setCellStyle(csHorVerCenter);
        headingBRSBookRow.createCell(4).setCellValue(prop.getString("pms.cell.pms"));
        headingBRSBookRow.getCell(4).setCellStyle(csHorVerCenter);
        // sheet.autoSizeColumn(3);
        headingBRSBookRow.createCell(5).setCellValue(prop.getString("pms.cell.net_payable"));
        headingBRSBookRow.getCell(5).setCellStyle(csHorVerCenter);

        if (distributorMaster.getDistModelType().equals("Upfront")) {

            headingBRSBookRow.createCell(7).setCellValue(prop.getString("pms.cell.trans_dt"));
            headingBRSBookRow.getCell(7).setCellStyle(csHorVerCenter);

            headingBRSBookRow.createCell(8).setCellValue(prop.getString("pms.cell.init_fund"));
            headingBRSBookRow.getCell(8).setCellStyle(csHorVerCenter);

            headingBRSBookRow.createCell(9).setCellValue(prop.getString("pms.cell.add_fund"));
            headingBRSBookRow.getCell(9).setCellStyle(csHorVerCenter);

            headingBRSBookRow.createCell(10).setCellValue(prop.getString("pms.cell.tot_corp"));
            headingBRSBookRow.getCell(10).setCellStyle(csHorVerCenter);

            headingBRSBookRow.createCell(11).setCellValue(prop.getString("pms.cell.tot_corp_share"));
            headingBRSBookRow.getCell(11).setCellStyle(csHorVerCenter);
        }

        iRowNo = sheetPMS.getLastRowNum() + 1;
        //	System.out.println(iRowNo);
        HSSFRow blankRow = sheetPMS.createRow(iRowNo);

        int iTotCell = 0;
        HSSFCell blankCell = null;

        int iMaxAllowedCell = 0;

        if (distributorMaster.getDistModelType().equals("Upfront"))
            iMaxAllowedCell = 11;
        else
            iMaxAllowedCell = 6;
        while (iTotCell < iMaxAllowedCell || iTotCell == iMaxAllowedCell) {
            blankCell = blankRow.createCell(iTotCell);
            blankCell.setCellValue("");
            blankCell.setCellStyle(csPlanLeftRight);
            iTotCell++;
        }

        iRowNo = sheetPMS.getLastRowNum() + 1;
        Float sFee = 0f;
        float sTotal = 0f;
        String sUcpl = "";
        String tPms = "";
        float sMarkup = 0f;
        float sAdvisory = 0f;
        float sPms = 0f;
        float tInitial = 0f;
        float tAdd = 0f;
        float tTot = 0f;
        float tToShare = 0f;
        float sAdv = 0f;
        double rounded = 0.0;
        Float fTotFee = 0f;
        DecimalFormat decimalFormat = new DecimalFormat("#.0000");
        for (DistributorPMSBean bean : distributorPMSBeans) {

            reportBrokeragePMS = new ReportBrokeragePMS();
            HSSFRow row = sheetPMS.createRow(iRowNo);
            System.out.println(bean.getPmsClientMaster().getClientCode());
            HSSFCell cell = row.createCell(0);
            cell.setCellValue(bean.getClientName());
            cell.setCellStyle(csPlanLeftRight);

            // sheet.autoSizeColumn(0);

            cell = row.createCell(1);
            cell.setCellType(CellType.NUMERIC);
            cell.setCellValue(Double.parseDouble(bean.getClientCode()));
            //		cell.setCellStyle(csPlain);
            cell.setCellStyle(csPlanLeftRight);
            // cell.setCellStyle(csDF);

            // sheet.autoSizeColumn(1);
            if (bean.getTotal() != 0) {
                sUcpl = decimalFormat.format(bean.getTotal());
                // System.out.println(Float.parseFloat(sUcpl));
                cell = row.createCell(2);
                cell.setCellType(CellType.NUMERIC);
                cell.setCellStyle(csDF);
                cell.setCellValue(bean.getTotal());

                sFee += bean.getTotal();
                // sheet.autoSizeColumn(2);
                Float myValue = bean.getTotal();
                // sTotal+=myValue;
                rounded = (double) Math.round(myValue);
                sTotal += (float) rounded;

            } else {
                sUcpl = decimalFormat.format(bean.getTotal());
                cell = row.createCell(2);
                cell.setCellValue(0);
                cell.setCellType(CellType.NUMERIC);
                cell.setCellStyle(csDF);

            }

            if (bIsCommissionSet == false) {
                distComm.createCell(1).setCellValue(bean.getClientFeeCommission().getNavComm() / 100);
                distComm.getCell(1).setCellStyle(csPercNoBorder);
                distComm.getCell(1).setCellType(CellType.NUMERIC);
                bIsCommissionSet = true;
            }

            // sheet.autoSizeColumn(4);
            Float x = bean.getTotal();

            if (bean.getClientFeeCommission() != null) {
                Float y = bean.getClientFeeCommission().getNavComm();
                Float b = bean.getClientFeeCommission().getBrokerageComm();

                // Float y = 33f;
                Float num = (float) ((x * b) / 100);
                double rRound = (double) Math.round(num);
                sMarkup += (float) rRound;
                if (rRound != 0) {
                    cell = row.createCell(3);
                    cell.setCellValue(rRound);
                    cell.setCellType(CellType.NUMERIC);
                    cell.setCellStyle(csDF);

                } else {
                    cell = row.createCell(3);
                    cell.setCellValue(0);
                    cell.setCellType(CellType.NUMERIC);
                    cell.setCellStyle(csDF);

                }
                // tPms=decimalFormat.format();
                if (bean.getPmsFee().equals("0")) {
                    //cell = row.createCell(7);
                    cell = row.createCell(4);
                    cell.setCellValue(Double.parseDouble(bean.getPmsFee()));
                    cell.setCellType(CellType.NUMERIC);
                    cell.setCellStyle(csDF);

                } else {

                    //cell = row.createCell(7);
                    cell = row.createCell(4);
                    cell.setCellValue(Double.parseDouble(bean.getPmsFee()));
                    cell.setCellType(CellType.NUMERIC);
                    cell.setCellStyle(csDF);

                }

                Double dTotal = Double.parseDouble(bean.getPmsFee());
                fTotFee += dTotal.floatValue();

                double rRounds = 0.0;
                if (!bean.getPmsFee().equals("-")) {
                    String pPms = bean.getPmsFee();
                    Float amount = Float.parseFloat(pPms);


                    // Float pPer = 33f;
						/*Float pValue = (float) ((amount * y) / 100);
						rRounds = (double) Math.round(pValue);
						sAdv += (float) rRounds;*/

                    Float fPercForDist = (Float.parseFloat(bean.getPmsFee()) * y) / 100;
                    rRounds = (double) Math.round(fPercForDist);
                    sAdv += (float) rRounds;

                    //	cell = row.createCell(9);
                    cell = row.createCell(5);
                    cell.setCellValue(rRounds);
                    cell.setCellType(CellType.NUMERIC);
                    cell.setCellStyle(csDF);
                    // sheet.autoSizeColumn(9);
                } else {
                    cell = row.createCell(5);
                    cell.setCellValue(0);
                    cell.setCellType(CellType.NUMERIC);
                    cell.setCellStyle(csDF);

                    // sheet.autoSizeColumn(9);
                }


                if (distributorMaster.getDistModelType().equals("Upfront")) {
                    cell = row.createCell(7);//10
                    //csRight.setAlignment(CellStyle.ALIGN_RIGHT);
                    cell.setCellStyle(csHorVerDate);
                    cell.setCellValue(bean.getTransDate());

                    cell = row.createCell(8);//11
                    cell.setCellType(CellType.NUMERIC);
                    cell.setCellStyle(csDF);
                    cell.setCellValue(bean.getInitialFund());
                    tInitial += bean.getInitialFund();

                    cell = row.createCell(9);//12
                    cell.setCellType(CellType.NUMERIC);
                    cell.setCellStyle(csDF);
                    cell.setCellValue(bean.getAddFund());
                    tAdd += bean.getAddFund();

                    cell = row.createCell(10);//13
                    cell.setCellType(CellType.NUMERIC);
                    cell.setCellStyle(csDF);
                    cell.setCellValue(bean.getTotCorpus());
                    tTot += bean.getTotCorpus();

                    cell = row.createCell(11);//14
                    cell.setCellType(CellType.NUMERIC);
                    cell.setCellStyle(csDF);
                    cell.setCellValue(bean.getTotShareCorpus());
                    tToShare += bean.getTotShareCorpus();
                }
                reportBrokeragePMS.setClientName(bean.getClientName());
                reportBrokeragePMS.setClientCode(bean.getClientCode());
                reportBrokeragePMS.setDistributorMaster(distributorMaster);
                if (bean.getTotal() != 0) {
                    reportBrokeragePMS.setBrokeFee(bean.getTotal());
                    reportBrokeragePMS.setBrokeTotal((float) rounded);
                    reportBrokeragePMS.setBrokeComm(bean.getClientFeeCommission().getBrokerageComm());
                    reportBrokeragePMS.setMarketingPay((float) rRound);
                    if (distributorMaster.getDistModelType().equals("Upfront")) {
                        reportBrokeragePMS.setTransDate(bean.getTransDate());
                        reportBrokeragePMS.setInitialCorpus(bean.getInitialFund());
                        reportBrokeragePMS.setAddCorpus(bean.getAddFund());
                        reportBrokeragePMS.setTotalCorpus(bean.getTotCorpus());
                        reportBrokeragePMS.setToShareCorpus(bean.getTotShareCorpus());
                    }
                } else {
                    reportBrokeragePMS.setBrokeFee(0f);
                    reportBrokeragePMS.setBrokeTotal(0f);
                    reportBrokeragePMS.setBrokeComm(bean.getClientFeeCommission().getBrokerageComm());
                    reportBrokeragePMS.setMarketingPay(0f);
                    if (distributorMaster.getDistModelType().equals("Upfront")) {
                        reportBrokeragePMS.setTransDate(bean.getTransDate());
                        reportBrokeragePMS.setInitialCorpus(bean.getInitialFund());
                        reportBrokeragePMS.setAddCorpus(bean.getAddFund());
                        reportBrokeragePMS.setTotalCorpus(bean.getTotCorpus());
                        reportBrokeragePMS.setToShareCorpus(bean.getTotShareCorpus());
                    }
                }
                if (!bean.getPmsFee().equals("0")) {
                    reportBrokeragePMS.setPmsFee(Float.valueOf(bean.getPmsFee()));
                    reportBrokeragePMS.setPmsComm(bean.getClientFeeCommission().getNavComm());
                    reportBrokeragePMS.setAdvisoryPay((float) rRounds);
                    reportBrokeragePMS.setReportGeneration(reportFeeBrokerage);
                    reportBrokeragePMSRepository.save(reportBrokeragePMS);
                } else {
                    reportBrokeragePMS.setPmsFee(0f);
                    reportBrokeragePMS.setPmsComm(bean.getClientFeeCommission().getNavComm());
                    reportBrokeragePMS.setAdvisoryPay((float) rRounds);
                    reportBrokeragePMS.setReportGeneration(reportFeeBrokerage);
                    reportBrokeragePMSRepository.save(reportBrokeragePMS);
                }

                iRowNo++;
            } else {
                //log.error("commision not available");

            }


        }

        CellStyle csFour = cs;
        CellStyle csDFBoldFour = csDFBold;

        csFour.setBorderBottom(BorderStyle.THIN);
        csFour.setBorderTop(BorderStyle.THIN);
        csFour.setBorderLeft(BorderStyle.THIN);
        csFour.setBorderRight(BorderStyle.THIN);

        csDFBoldFour.setBorderBottom(BorderStyle.THIN);
        csDFBoldFour.setBorderTop(BorderStyle.THIN);
        csDFBoldFour.setBorderLeft(BorderStyle.THIN);
        csDFBoldFour.setBorderRight(BorderStyle.THIN);


        HSSFRow row = sheetPMS.createRow(sheetPMS.getLastRowNum() + 1);
        row.createCell(0).setCellValue("GRAND TOTALS");
        row.getCell(0).setCellStyle(csFour);
        // sheet.autoSizeColumn(0);

        HSSFCell cell = row.createCell(1);
        cell.setCellValue("");
        cell.setCellStyle(csDFBoldFour);

        cell = row.createCell(2);
        cell.setCellValue((sFee));
        cell.setCellType(CellType.NUMERIC);
        cell.setCellStyle(csDFBoldFour);

        // sheet.autoSizeColumn(2);

        cell = row.createCell(3);
        cell.setCellValue((sMarkup));//sTotal
        cell.setCellType(CellType.NUMERIC);
        cell.setCellStyle(csDFBoldFour);
        // sheet.autoSizeColumn(3);

        cell = row.createCell(4);
        cell.setCellValue((fTotFee));
        cell.setCellType(CellType.NUMERIC);
        cell.setCellStyle(csDFBoldFour);

        cell = row.createCell(5);
        cell.setCellValue((sAdv));//sMarkup
        cell.setCellType(CellType.NUMERIC);
        cell.setCellStyle(csDFBoldFour);

        cumulativePmsAifBean.setBrokerageName(prop.getString("pms.marketing.fee"));
        cumulativePmsAifBean.setBrokerageTotal(sMarkup);
        // sheet.autoSizeColumn(5);

        cell = row.createCell(6);
        cell.setCellValue("");
        cell.setCellStyle(csDFBoldFour);
        //following is a dummy loop can be removed after verification
        if (distributorMaster.getDistModelType().equals("Upfront")) {
            cell = row.createCell(7);
            cell.setCellValue("");//sPms
            cell.setCellStyle(csDFBoldFour);
            //	cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
            // sheet.autoSizeColumn(7);

            cell = row.createCell(8);
            cell.setCellValue("");
            cell.setCellStyle(csDFBoldFour);

            cell = row.createCell(9);
            cell.setCellValue(sAdv);
            cell.setCellStyle(csDFBoldFour);
            cell.setCellType(CellType.NUMERIC);

            cell = row.createCell(10);
            cell.setCellValue("");
            cell.setCellStyle(csDFBoldFour);
        }
        if (distributorMaster.getDistModelType().equals("Upfront")) {
            //cell = row.createCell(11);
            cell = row.createCell(8);
            cell.setCellValue(tInitial);
            cell.setCellStyle(csDFBoldFour);
            cell.setCellType(CellType.NUMERIC);

            cell = row.createCell(9);
            //cell = row.createCell(12);
            cell.setCellValue(tAdd);
            cell.setCellStyle(csDFBoldFour);
            cell.setCellType(CellType.NUMERIC);

            cell = row.createCell(10);
            //cell = row.createCell(13);
            cell.setCellValue(tTot);
            cell.setCellStyle(csDFBoldFour);
            cell.setCellType(CellType.NUMERIC);

            cell = row.createCell(11);
            //cell = row.createCell(14);
            cell.setCellValue((tToShare));
            cell.setCellStyle(csDFBoldFour);
            cell.setCellType(CellType.NUMERIC);
        }

        cumulativePmsAifBean.setPmsName(prop.getString("pms.adv.fee"));
        cumulativePmsAifBean.setPmsTotal(sAdv);
        // sheet.autoSizeColumn(9);

        iRowNo = sheetPMS.getLastRowNum() + 2;

        CellStyle wrapText = workBook.createCellStyle();
        wrapText.setWrapText(true);

        ReportGeneration reportGenerationProfit = new ReportGeneration();
        reportGenerationProfit.setStartDate(reportGeneration.getStartDate());
        reportGenerationProfit.setToDate(reportGeneration.getToDate());
        reportGenerationProfit.setDetailsUpdatedFlag(0);
        reportGenerationProfit.setDistributorMaster1(distributorMaster);
        reportGenerationProfit.setReportType("Profit Share");
        reportGenerationProfit = reportGenerationRepository.save(reportGenerationProfit);

        headingBRSBookRow = sheetPMS.createRow(iRowNo);
        headingBRSBookRow.createCell(0).setCellValue("Profit Share");
        headingBRSBookRow.getCell(0).setCellStyle(csPlainNoBorder);//cs
        // sheet.autoSizeColumn(0);
        iRowNo = sheetPMS.getLastRowNum() + 1;
        headingBRSBookRow = sheetPMS.createRow(iRowNo);
        headingBRSBookRow.setHeightInPoints(45);//40
        headingBRSBookRow.createCell(0).setCellValue("Client Name");
        headingBRSBookRow.getCell(0).setCellStyle(csHorVerCenter);
        // sheet.autoSizeColumn(0);
        headingBRSBookRow.createCell(1).setCellValue("Code");
        headingBRSBookRow.getCell(1).setCellStyle(csHorVerCenter);
        // sheet.autoSizeColumn(1);
        headingBRSBookRow.createCell(2).setCellValue("Profit Share Fee Income");
        headingBRSBookRow.getCell(2).setCellStyle(csHorVerCenter);
        // sheet.autoSizeColumn(2);
        headingBRSBookRow.createCell(3).setCellValue("%");
        headingBRSBookRow.getCell(3).setCellStyle(csHorVerCenter);
        // sheet.autoSizeColumn(3);
        headingBRSBookRow.createCell(4).setCellValue("Fee Payable");
        headingBRSBookRow.getCell(4).setCellStyle(csHorVerCenter);
        // sheet.autoSizeColumn(4);
        headingBRSBookRow.createCell(5).setCellValue("Date Of Amount Receipts ");
        headingBRSBookRow.getCell(5).setCellStyle(csHorVerCenter);
        // sheet.autoSizeColumn(5);
        headingBRSBookRow.createCell(7).setCellValue("PMS Strategy");
        headingBRSBookRow.getCell(7).setCellStyle(csHorVerCenter);
        // sheet.autoSizeColumn(6);
        iRowNo = sheetPMS.getLastRowNum() + 1;
        float pShare = 0f;
        float pBeyond = 0f;

        sheetPMS.setDefaultColumnWidth(12);
        sheetPMS.autoSizeColumn(0);

        sheetPMS.autoSizeColumn(6);
        sheetPMS.setColumnWidth(6, 0);

        try {

            List<ProfitShare> profitShares = profitShareRepository.getProfitShareByDistBetween(distributorMaster.getId(), reportGeneration.getStartDate(),
                reportGeneration.getToDate());

            String sIncome = "";
            ReportProfitShare reportProfitShare;
            if (profitShares.isEmpty() != true) {
                for (ProfitShare profitShare : profitShares) {

                    reportProfitShare = new ReportProfitShare();

                    Double income = Double.valueOf(profitShare.getProfitShareIncome());
                    sIncome = decimalFormat.format(income);
                    System.out.println(income);
                    row = sheetPMS.createRow(iRowNo);
                    reportProfitShare.setClientFeeCommission(clientFeeCommissionRepository.findByPmsClientMaster(profitShare.getPmsClientMaster()));
                    cell = row.createCell(0);
                    cell.setCellValue(profitShare.getPmsClientMaster().getClientName());
                    cell.setCellStyle(csPlanLeftRight);

                    reportProfitShare.setClientName(profitShare.getPmsClientMaster().getClientName());
                    // sheet.autoSizeColumn(0);
                    cell = row.createCell(1);
                    cell.setCellType(CellType.NUMERIC);
                    cell.setCellStyle(csPlanLeftRight);
                    cell.setCellValue(Double.parseDouble(profitShare.getPmsClientMaster().getClientCode()));
                    reportProfitShare.setClientCode(profitShare.getPmsClientMaster().getClientCode());
                    // sheet.autoSizeColumn(1);
                    cell = row.createCell(2);
                    // cell.setCellValue(Double.parseDouble(sIncome));
                    cell.setCellValue(income);
                    reportProfitShare.setProfitFee(income.floatValue());
                    cell.setCellType(CellType.NUMERIC);
                    cell.setCellStyle(csDF);
                    // sheet.autoSizeColumn(2);
                    pShare += profitShare.getProfitShareIncome();
                    cell = row.createCell(3);
                    cell.setCellValue(reportProfitShare.getClientFeeCommission().getProfitComm() / 100);// +
                    // "%"
                    reportProfitShare.setProfitComm(reportProfitShare.getClientFeeCommission().getProfitComm());
                    cell.setCellType(CellType.NUMERIC);
                    cell.setCellStyle(csPerc);
                    // sheet.autoSizeColumn(3);
                    Float x = income.floatValue();
                    // Float y = 33f;
                    String sReceipts = "";
                    if (reportProfitShare.getClientFeeCommission() != null) {
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                        Float y = reportProfitShare.getClientFeeCommission().getProfitComm();
                        Float num = (float) ((x * y) / 100);
                        double rRound = (double) Math.round(num);

                        cell = row.createCell(4);
                        cell.setCellValue(rRound);
                        reportProfitShare.setFeePay((float) rRound);
                        cell.setCellType(CellType.NUMERIC);
                        cell.setCellStyle(csDF);

                        pBeyond += (float) rRound;
                        // sheet.autoSizeColumn(4);
                        sReceipts = sdf.format(profitShare.getReceiptDate());
                        System.out.println(sReceipts);
                        HSSFCell cellReceipt = row.createCell(5);
                        csRight.setAlignment(HorizontalAlignment.RIGHT);
                        csRightLeftRight = csRight;
                        csRightLeftRight.setBorderLeft(BorderStyle.THIN);
                        csRightLeftRight.setBorderRight(BorderStyle.THIN);
                        cellReceipt.setCellStyle(csRightLeftRight);
                        cellReceipt.setCellValue(sReceipts);

                        reportProfitShare.setReceiptDate(profitShare.getReceiptDate());
                        // sheet.autoSizeColumn(5);
                        // One Blank Column
                        // row.createCell(6).setCellValue(profitShareDO.getInvestmentMasterDO().getInvestName());
                        cell = row.createCell(7);
                        cell.setCellValue(profitShare.getInvestmentMaster().getInvestmentName());
                        cell.setCellStyle(csRightLeftRight);

                        // sheet.autoSizeColumn(6);
                        reportProfitShare.setStrategyName(profitShare.getInvestmentMaster().getInvestmentName());
                        reportProfitShare.setReportGeneration(reportGenerationProfit);
                        reportProfitShare.setDistributorMaster(distributorMaster);
                        reportProfitShareRepository.save(reportProfitShare);
                        iRowNo++;
                    } else {
                        log.error("Commision not exists");

                    }
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        }

        // }
        row = sheetPMS.createRow(sheetPMS.getLastRowNum() + 1);
        row.createCell(0).setCellValue("TOTAL");
        row.getCell(0).setCellStyle(csFour);
        // sheet.autoSizeColumn(0);

        cell = row.createCell(1);
        cell.setCellValue("");
        cell.setCellStyle(csDFBoldFour);

        cell = row.createCell(2);
        cell.setCellValue(pShare);
        cell.setCellStyle(csDFBoldFour);
        cell.setCellType(CellType.NUMERIC);

        cell = row.createCell(3);
        cell.setCellValue("");
        cell.setCellStyle(csDFBoldFour);

        // sheet.autoSizeColumn(2);
        cell = row.createCell(4);
        cell.setCellValue((pBeyond));
        cell.setCellStyle(csDFBoldFour);
        cell.setCellType(CellType.NUMERIC);

        cell = row.createCell(5);
        cell.setCellValue("");
        cell.setCellStyle(csDFBoldFour);

        cell = row.createCell(6);
        cell.setCellValue("");
        cell.setCellStyle(csDFBoldFour);

        cell = row.createCell(7);
        cell.setCellValue("");
        cell.setCellStyle(csDFBoldFour);

        cumulativePmsAifBean.setProfitName(prop.getString("pms.profit.fee"));
        cumulativePmsAifBean.setProfitTotal(pBeyond);
        cumulativePmsAifBeans.add(cumulativePmsAifBean);
        // sheet.autoSizeColumn(4);
        Calendar beginCalendar = Calendar.getInstance();
        Calendar finishCalendar = Calendar.getInstance();
        try {
            beginCalendar.setTime(dateFormat.parse(sStartTime));
            finishCalendar.setTime(dateFormat.parse(sEndTime));

        } catch (ParseException e) {
            e.printStackTrace();
        }
        int sq4 = 0;
        int sq4f = 0;
        int sq4j = 0;
        List<String> aifDate = new ArrayList<String>();
        while (!beginCalendar.after(finishCalendar)) {
            // add one month to date per loop
            String from = dateFormat.format(beginCalendar.getTime());
            String fStart = dateFormat.format(reportGeneration.getStartDate());
            String fEnd = dateFormat.format(reportGeneration.getToDate());
            aifDate.add(from);
            if (fStart.contains(prop.getString("fee.fourth.quarter.month3")) && fEnd.contains(prop.getString("fee.fourth.quarter.month"))) {
                sq4f++;
            }
            if (fStart.contains(prop.getString("fee.fourth.quarter.month")) && fEnd.contains(prop.getString("fee.fourth.quarter.month"))) {
                sq4++;
            }
            if (fStart.contains(prop.getString("fee.fourth.quarter.month1")) && fEnd.contains(prop.getString("fee.fourth.quarter.month"))) {
                sq4j++;
            }
            beginCalendar.add(Calendar.MONTH, 1);
        }
        aifFeeCalculation(distributorMaster, sheetAIF, sFinal, cumulativePmsAifBeans, sheetSummary, aifDate, sq4, sq4f, sq4j, reportGeneration, workBook, cumulativeAIFSeriesBCAD);
    }

    List<InvestorProtfolio> investView;

    public void aifFeeCalculation(DistributorMaster distributorMaster, HSSFSheet sheetAIF, String sFinal, List<CumulativePmsAifBean> cumulativePmsAifBeans, HSSFSheet sheetSummary, List<String> aifDate, int sq4, int sq4f, int sq4j,
                                   ReportGeneration reportGeneration, HSSFWorkbook workBook, CumulativeAIFSeriesBCAD cumulativeAIFSeriesBCAD) {
        Product product = productRepository.findByProductName("AIF");
        CommissionDefinition commissionDefinition = commissionDefinitionRepository.findCommissions(distributorMaster.getId(), product.getId());
        ReportsDistributorFee reportsDistributorFee;

        DateFormat dateFormat = new SimpleDateFormat("MMM yyyy");
        DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String sMonthStart = dateFormat.format(reportGeneration.getStartDate());

        String sMonthEnd = dateFormat.format(reportGeneration.getToDate());
        String from = "";

        investView = new ArrayList<>();
        Calendar beginCalendar = Calendar.getInstance();
        Calendar finishCalendar = Calendar.getInstance();

        Float percentage, mangementFee, performanceFee, total, commision;
        Float rPercentage, rMangementFee, rPerformanceFee, rCommision;
        Float distCommision = 0.0f;

        ReportAifDistBean reportAifDistBeans = new ReportAifDistBean();
        ReportQFourBean reportQFourBeans = new ReportQFourBean();
        ReportQFourFinancialBean reportQFourFinancialBeans = new ReportQFourFinancialBean();
        ReportQFourPerBean reportQFourPerBeans = new ReportQFourPerBean();
        ReportQFourManageBean reportQFourManageBeans = new ReportQFourManageBean();
        ReportQFourPerformanceBean reportQFourPerformanceBeans = new ReportQFourPerformanceBean();

        try {
            // for (DistributorMasterDO distributorMasterDO : masterDO) {
            distCommision = commissionDefinition.getDistributorComm();
            try {
                beginCalendar.setTime(dateFormat.parse(sMonthStart));
                finishCalendar.setTime(dateFormat.parse(sMonthEnd));

            } catch (ParseException e) {
                e.printStackTrace();
            }
            reportAifDistBean = new ArrayList<ReportAifDistBean>();
            List<ReportAifDistBean> aifUpdated = investorViewService.getDistFee(distributorMaster, aifDate);
            System.out.println(aifUpdated.size());
            if (aifUpdated.isEmpty() == false) {
                for (ReportAifDistBean aifUpdatedUnits : aifUpdated) {
                    reportAifDistBeans = new ReportAifDistBean();
                    Double investorUnits = investorViewRepository.getInvestorSum(aifUpdatedUnits.getSeriesMaster().getId(), aifDate);
                    if (investorUnits != 0.0) {
                        reportAifDistBeans.setClientCode(aifUpdatedUnits.getClientCode());
                        reportAifDistBeans.setClientName(aifUpdatedUnits.getClientName());
                        reportAifDistBeans.setSeriesName(aifUpdatedUnits.getSeriesName());
                        reportAifDistBeans.setDistComm(commissionDefinition.getDistributorComm());
                        Double manageFee = aifManagePerMonthRepository.getManage(aifUpdatedUnits.getSeriesMaster().getId(), reportGeneration.getStartDate(), reportGeneration.getToDate());
                        reportAifDistBeans.setManageFee(Float.valueOf(manageFee.toString()));
                        Double performFee = aifManagePerMonthRepository.getPerform(aifUpdatedUnits.getSeriesMaster().getId(), reportGeneration.getStartDate(), reportGeneration.getToDate());
                        reportAifDistBeans.setPerformFee(Float.valueOf(performFee.toString()));
                        reportAifDistBeans.setSumOfUnitSeries(Float.valueOf(investorUnits.toString()));
                        reportAifDistBeans.setTotNoOfUnits(aifUpdatedUnits.getTotNoOfUnits());

                        percentage = (roundOff(aifUpdatedUnits.getTotNoOfUnits())
                            / roundOff(Float.valueOf(investorUnits.toString()))) * 100;
                        rPercentage = roundOff(percentage);
                        mangementFee = (rPercentage / 100) * roundOff(Float.valueOf(manageFee.toString()));

                        rMangementFee = roundOff(mangementFee);
                        reportAifDistBeans.setManageFeeCharged(rMangementFee);
                        // total = rMangementFee;
                        commision = (distCommision / 100) * rMangementFee;
                        rCommision = roundOff(commision);
                        reportAifDistBeans.setsCommission(rCommision);
                        reportAifDistBean.add(reportAifDistBeans);
                    }

                }

            }

            while (!beginCalendar.after(finishCalendar)) {
                reportQFourBean = new ArrayList<ReportQFourBean>();
                reportQFourFinancialBean = new ArrayList<ReportQFourFinancialBean>();
                reportQFourPerBean = new ArrayList<ReportQFourPerBean>();
                reportQFourManageBean = new ArrayList<ReportQFourManageBean>();
                reportQFourPerformanceBean = new ArrayList<ReportQFourPerformanceBean>();

                // add one month to date per loop
                from = dateFormat.format(beginCalendar.getTime());

                Date sStart = dateFormat.parse(from);
                String startDate = sdf.format(beginCalendar.getTime());
                Date dDate = sdf.parse(startDate);
                Float reportAif = 0f;
                Float reportAifManag = 0f;
                Float reportAifPer = 0f;
                Float fPercentage, frPercentage, fMangementFee, fPerformanceFee, frMangementFee, frPerformanceFee,
                    fTotal, fCommision, frCommision;
                Float jmPercentage, jmrPercentage, jmMangementFee, jmPerformanceFee, jmrMangementFee, jmrPerformanceFee,
                    jmTotal, jmCommision, jmrCommision;
                Float manPercentage, manrPercentage, manMangementFee, manPerformanceFee, manrMangementFee, manrPerformanceFee,
                    manTotal, manCommision, manrCommision;
                Float perfPercentage, perfrPercentage, perfMangementFee, perfPerformanceFee, perfrMangementFee, perfrPerformanceFee,
                    perfTotal, perfCommision, perfrCommision;
                List<AIFUpdatedUnits> aifUpdatedUnitsList = aifUpdatedUnitsRepository.findByDistributorMasterAndMonthYear(distributorMaster, from);
                for (AIFUpdatedUnits aifUpdatedUnits : aifUpdatedUnitsList) {

                    reportQFourBeans = new ReportQFourBean();
                    reportQFourFinancialBeans = new ReportQFourFinancialBean();
                    reportQFourPerBeans = new ReportQFourPerBean();
                    reportQFourManageBeans = new ReportQFourManageBean();
                    reportQFourPerformanceBeans = new ReportQFourPerformanceBean();
                    InvestorView investorViewDO = investorViewRepository.findByMonthYearAndSeriesMaster(from, aifUpdatedUnits.getSeriesMaster());
                    if (investorViewDO.getNoOfUnits() != 0.0) {
                        AIFManagePerMonth aifManagePerMonth = getParticularSeriesFee(aifUpdatedUnits.getSeriesMaster(),
                            sStart, reportGeneration.getStartDate(), reportGeneration.getToDate());
                        reportQFourBeans.setClientCode(aifUpdatedUnits.getAifClientMaster().getClientCode());
                        reportQFourFinancialBeans.setClientCode(aifUpdatedUnits.getAifClientMaster().getClientCode());
                        reportQFourPerBeans.setClientCode(aifUpdatedUnits.getAifClientMaster().getClientCode());
                        reportQFourManageBeans.setClientCode(aifUpdatedUnits.getAifClientMaster().getClientCode());
                        reportQFourPerformanceBeans.setClientCode(aifUpdatedUnits.getAifClientMaster().getClientCode());

                        reportQFourFinancialBeans.setClientName(aifUpdatedUnits.getAifClientMaster().getClientName());
                        reportQFourBeans.setClientName(aifUpdatedUnits.getAifClientMaster().getClientName());
                        reportQFourPerBeans.setClientName(aifUpdatedUnits.getAifClientMaster().getClientName());
                        reportQFourManageBeans.setClientName(aifUpdatedUnits.getAifClientMaster().getClientName());
                        reportQFourPerformanceBeans.setClientName(aifUpdatedUnits.getAifClientMaster().getClientName());

                        reportQFourBeans.setSeriesName(aifUpdatedUnits.getSeriesMaster().getSeriesCode());
                        reportQFourFinancialBeans.setSeriesName(aifUpdatedUnits.getSeriesMaster().getSeriesCode());
                        reportQFourPerBeans.setSeriesName(aifUpdatedUnits.getSeriesMaster().getSeriesCode());
                        reportQFourManageBeans.setSeriesName(aifUpdatedUnits.getSeriesMaster().getSeriesCode());
                        reportQFourPerformanceBeans.setSeriesName(aifUpdatedUnits.getSeriesMaster().getSeriesCode());

                        if (from.contains(prop.getString("fee.fourth.quarter.month")))
                            mangementFee = roundOff(aifManagePerMonth.getManagementFee());
                        else
                            mangementFee = 0.0f;
                        if ((from.contains(prop.getString("fee.fourth.quarter.month"))) || (from.contains(prop.getString("fee.fourth.quarter.month1"))) ||
                            (from.contains(prop.getString("fee.fourth.quarter.month2"))))
                            jmMangementFee = roundOff(aifManagePerMonth.getManagementFee());
                        else
                            jmMangementFee = 0.0f;

                        fMangementFee = roundOff(aifManagePerMonth.getManagementFee());
                        manMangementFee = roundOff(aifManagePerMonth.getManagementFee());

                        //jmMangementFee=roundOff(aifMasterFeeDO.getMangeFee());
                        reportQFourBeans.setqFourManage(mangementFee);
                        reportQFourFinancialBeans.setqFourManage(fMangementFee);
                        reportQFourPerBeans.setqFourManage(jmMangementFee);
                        reportQFourManageBeans.setqFourManage(manMangementFee);
                        reportQFourPerformanceBeans.setqFourManage(0.0f);

                        performanceFee = roundOff(aifManagePerMonth.getPerformanceFee());
                        fPerformanceFee = roundOff(aifManagePerMonth.getPerformanceFee());
                        jmPerformanceFee = roundOff(aifManagePerMonth.getPerformanceFee());

                        reportQFourBeans.setqFourPerform(performanceFee);
                        reportQFourFinancialBeans.setqFourPerform(fPerformanceFee);
                        reportQFourPerBeans.setqFourPerform(jmPerformanceFee);
                        reportQFourManageBeans.setqFourPerform(0.0f);
                        reportQFourPerformanceBeans.setqFourPerform(fPerformanceFee);

                        reportQFourBeans.setSumOfUnitSeries(roundOff(roundOff(investorViewDO.getNoOfUnits())));
                        reportQFourFinancialBeans.setSumOfUnitSeries(roundOff(roundOff(investorViewDO.getNoOfUnits())));
                        reportQFourPerBeans.setSumOfUnitSeries(roundOff(roundOff(investorViewDO.getNoOfUnits())));
                        reportQFourManageBeans.setSumOfUnitSeries(roundOff(roundOff(investorViewDO.getNoOfUnits())));
                        reportQFourPerformanceBeans.setSumOfUnitSeries(roundOff(roundOff(investorViewDO.getNoOfUnits())));

                        reportQFourBeans.setTotNoOfUnits(aifUpdatedUnits.getTotRemUnits());
                        reportQFourFinancialBeans.setTotNoOfUnits(aifUpdatedUnits.getTotRemUnits());
                        reportQFourPerBeans.setTotNoOfUnits(aifUpdatedUnits.getTotRemUnits());
                        reportQFourManageBeans.setTotNoOfUnits(aifUpdatedUnits.getTotRemUnits());
                        reportQFourPerformanceBeans.setTotNoOfUnits(aifUpdatedUnits.getTotRemUnits());

                        percentage = (roundOff(aifUpdatedUnits.getTotRemUnits())
                            / roundOff(investorViewDO.getNoOfUnits())) * 100;

                        rPercentage = roundOff(percentage);

                        mangementFee = (rPercentage / 100) * roundOff(mangementFee);
                        performanceFee = (percentage / 100) * roundOff(aifManagePerMonth.getPerformanceFee());
                        rMangementFee = roundOff(mangementFee);
                        rPerformanceFee = roundOff(performanceFee);
                        total = (rMangementFee + rPerformanceFee);

                        /*Financial year template*/
                        fMangementFee = (rPercentage / 100) * roundOff(fMangementFee);
                        fPerformanceFee = (percentage / 100) * roundOff(aifManagePerMonth.getPerformanceFee());
                        frMangementFee = roundOff(fMangementFee);
                        frPerformanceFee = roundOff(fPerformanceFee);
                        fTotal = (frMangementFee + frPerformanceFee);

                        /*Financial Jan - Mar template*/
                        jmMangementFee = (rPercentage / 100) * roundOff(jmMangementFee);
                        jmPerformanceFee = (percentage / 100) * roundOff(aifManagePerMonth.getPerformanceFee());
                        jmrMangementFee = roundOff(jmMangementFee);
                        jmrPerformanceFee = roundOff(jmPerformanceFee);
                        jmTotal = (jmrMangementFee + jmrPerformanceFee);

                        /*Financial - Management alone*/
                        manMangementFee = (rPercentage / 100) * roundOff(manMangementFee);
                        manrMangementFee = roundOff(manMangementFee);
                        manTotal = (manrMangementFee + 0.0f);

                        /*Financial - Performance alone*/
                        perfPerformanceFee = (percentage / 100) * roundOff(aifManagePerMonth.getPerformanceFee());
                        perfrPerformanceFee = roundOff(perfPerformanceFee);
                        perfTotal = (perfrPerformanceFee + 0.0f);

                        reportQFourBeans.setClientFeeCharged(total);
                        reportQFourFinancialBeans.setClientFeeCharged(fTotal);
                        reportQFourPerBeans.setClientFeeCharged(jmTotal);
                        reportQFourManageBeans.setClientFeeCharged(manTotal);
                        reportQFourPerformanceBeans.setClientFeeCharged(perfTotal);

                        commision = (distCommision / 100) * total;
                        rCommision = roundOff(commision);

                        /*Financial year commission*/
                        fCommision = (distCommision / 100) * fTotal;
                        frCommision = roundOff(fCommision);

                        /*Financial Jan - Mar commission*/
                        jmCommision = (distCommision / 100) * jmTotal;
                        jmrCommision = roundOff(jmCommision);

                        /*Financial Management commission*/
                        manCommision = (distCommision / 100) * manTotal;
                        manrCommision = roundOff(manCommision);

                        /*Financial Performance alone*/
                        perfCommision = (distCommision / 100) * perfTotal;
                        perfrCommision = roundOff(perfCommision);

                        reportQFourBeans.setCommDue(rCommision);
                        reportQFourFinancialBeans.setCommDue(frCommision);
                        reportQFourPerBeans.setCommDue(jmrCommision);
                        reportQFourManageBeans.setCommDue(manrCommision);
                        reportQFourPerformanceBeans.setCommDue(perfrCommision);

                        reportQFourBean.add(reportQFourBeans);
                        reportQFourFinancialBean.add(reportQFourFinancialBeans);
                        reportQFourPerBean.add(reportQFourPerBeans);
                        reportQFourManageBean.add(reportQFourManageBeans);
                        reportQFourPerformanceBean.add(reportQFourPerformanceBeans);

                        Float mangementFee1 = roundOff(aifManagePerMonth.getManagementFee());
                        Float performanceFee1 = roundOff(aifManagePerMonth.getPerformanceFee());

                        Float percentage1 = (roundOff(aifUpdatedUnits.getTotRemUnits())
                            / roundOff(investorViewDO.getNoOfUnits())) * 100;
                        Float rPercentage1 = roundOff(percentage1);

                        mangementFee1 = (rPercentage1 / 100) * roundOff(aifManagePerMonth.getManagementFee());
                        performanceFee1 = (percentage1 / 100) * roundOff(aifManagePerMonth.getPerformanceFee());
                        //Float total1 = roundOff(mangementFee1+performanceFee1)* (rPercentage1 / 100);
                        Float rMangementFee1 = roundOff(mangementFee1);
                        Float rPerformanceFee1 = roundOff(performanceFee1);
                        Float total1 = rMangementFee1 + rPerformanceFee1;

                        Float commision1 = (distCommision / 100) * total1;

                        reportAif += commision1;
                        Float commManag = (distCommision / 100) * rMangementFee1;
                        reportAifManag += commManag;

                        Float commPerf = (distCommision / 100) * rPerformanceFee1;
                        reportAifPer += commPerf;
                    }

                }
                //Not required for the current Multiple strategy scenario
               // reportsDistributorFee = reportsDistributorFeeRepository.findCurrentOpeningBalance(startDate, distributorMaster.getId());
               /* if (reportsDistributorFee == null) {
                    distributorFee(reportAif, distributorMaster, dDate, reportAifManag, reportAifPer, reportGeneration);
                }*/
                /*Financial Mar -Mar template*/
                List<ReportAIFQuarterFour> reportAifQuarterFour = reportAIFQuarterFourRepository.findByDistributorMasterAndMnthYear(distributorMaster, from);

                if (reportAifQuarterFour.isEmpty() == true) {
                    ReportAIFQuarterFour reports;
                    AIFClientMaster client = null;
                    SeriesMaster seriesName = null;
                    InvestorView investorView = null;
                    for (ReportQFourBean beans : reportQFourBean) {
                        Float clientFee = 0.0f;
                        Float commDue = 0.0f;
                        reports = new ReportAIFQuarterFour();
                        System.out.println(beans.getClientCode());
                        client = aifClientMasterRepository.findByClientCode(beans.getClientCode());
                        reports.setAifClientMaster(client);
                        reports.setDistributorMaster(distributorMaster);
                        seriesName = seriesMasterRepository.findBySeriesCode(beans.getSeriesName());
                        reports.setSeriesMaster(seriesName);
                        reports.setMnthYear(from);
                        reports.setManageFee(beans.getqFourManage());

                        investorView = investorViewRepository.findByMonthYearAndSeriesMaster(from, seriesName);
                        System.out.println(investorView.getNoOfUnits());
                        reports.setTotIndUnits(beans.getTotNoOfUnits());
                        reports.setPerformFee(beans.getqFourPerform());
                        reports.setTotSeriesUnits(beans.getSumOfUnitSeries());


                        System.out.println(
                            ((reports.getManageFee() + beans.getqFourPerform()) / (beans.getSumOfUnitSeries()))
                                * beans.getTotNoOfUnits());
                        reports.setTotClientFee(beans.getClientFeeCharged());
                        reports.setMnthYear(from);
                        reports.setCommPerc(distributorMaster.getAifCommission());
                        reports.setCommDue(beans.getCommDue());
                        reportAIFQuarterFourRepository.save(reports);
                    }

                }
                /*Financial AIF Apr - Mar template*/
                List<ReportAIFQuarterFourFinancial> reportAifQuarterFourFinancial = reportAIFQuarterFourFinancialRepository.findByDistributorMasterAndMnthYear(distributorMaster, from);

                if (reportAifQuarterFourFinancial.isEmpty() == true) {
                    ReportAIFQuarterFourFinancial reports;
                    AIFClientMaster client = null;
                    SeriesMaster seriesName = null;
                    InvestorView investorView = null;
                    for (ReportQFourFinancialBean beans : reportQFourFinancialBean) {
                        Float clientFee = 0.0f;
                        Float commDue = 0.0f;
                        reports = new ReportAIFQuarterFourFinancial();
                        System.out.println(beans.getClientCode());
                        client = aifClientMasterRepository.findByClientCode(beans.getClientCode());
                        reports.setAifClientMaster(client);
                        reports.setDistributorMaster(distributorMaster);
                        seriesName = seriesMasterRepository.findBySeriesCode(beans.getSeriesName());
                        reports.setSeriesMaster(seriesName);
                        reports.setMnthYear(from);
                        reports.setManageFee(beans.getqFourManage());

                        investorView = investorViewRepository.findByMonthYearAndSeriesMaster(from, seriesName);
                        System.out.println(investorView.getNoOfUnits());
                        reports.setTotIndUnits(beans.getTotNoOfUnits());
                        reports.setPerformFee(beans.getqFourPerform());
                        reports.setTotSeriesUnits(beans.getSumOfUnitSeries());
                        System.out.println(
                            ((reports.getManageFee() + beans.getqFourPerform()) / (beans.getSumOfUnitSeries()))
                                * beans.getTotNoOfUnits());
                        reports.setTotClntFee(beans.getClientFeeCharged());
                        reports.setMnthYear(from);
                        reports.setCommPerc(distributorMaster.getAifCommission());
                        reports.setCommDue(beans.getCommDue());
                        reportAIFQuarterFourFinancialRepository.save(reports);

                    }

                }
                /*Financial Jan - Mar template*/
                List<ReportAIFQuarterFourPer> reportAifQuarterFourPer = reportAIFQuarterFourPerRepository.findByDistributorMasterAndMnthYear(distributorMaster, from);

                if (reportAifQuarterFourPer.isEmpty() == true) {
                    ReportAIFQuarterFourPer reports;
                    AIFClientMaster client = null;
                    SeriesMaster seriesName = null;
                    InvestorView investorView = null;
                    for (ReportQFourPerBean beans : reportQFourPerBean) {
                        Float clientFee = 0.0f;
                        Float commDue = 0.0f;
                        reports = new ReportAIFQuarterFourPer();
                        System.out.println(beans.getClientCode());
                        client = aifClientMasterRepository.findByClientCode(beans.getClientCode());
                        reports.setAifClientMaster(client);
                        reports.setDistributorMaster(distributorMaster);
                        seriesName = seriesMasterRepository.findBySeriesCode(beans.getSeriesName());
                        reports.setSeriesMaster(seriesName);
                        reports.setMnthYear(from);
                        reports.setManageFee(beans.getqFourManage());

                        investorView = investorViewRepository.findByMonthYearAndSeriesMaster(from, seriesName);
                        System.out.println(investorView.getNoOfUnits());
                        reports.setTotIndUnits(beans.getTotNoOfUnits());
                        reports.setPerformFee(beans.getqFourPerform());
                        reports.setTotSeriesUnits(beans.getSumOfUnitSeries());

                        System.out.println(
                            ((reports.getManageFee() + beans.getqFourPerform()) / (beans.getSumOfUnitSeries()))
                                * beans.getTotNoOfUnits());
                        reports.setTotClntFee(beans.getClientFeeCharged());
                        reports.setMnthYear(from);
                        reports.setCommPerc(distributorMaster.getAifCommission());
                        reports.setCommDue(beans.getCommDue());
                        reportAIFQuarterFourPerRepository.save(reports);

                    }

                }
                /*Financial AIF Management alone template*/
                List<ReportAIFQuarterFourManage> reportAIFQuarterFourManages = reportAIFQuarterFourManageRepository.findByDistributorMasterAndMnthYear(distributorMaster, from);
                if (reportAIFQuarterFourManages.isEmpty() == true) {
                    ReportAIFQuarterFourManage reports;
                    AIFClientMaster client = null;
                    SeriesMaster seriesName = null;
                    InvestorView investorView = null;
                    for (ReportQFourManageBean beans : reportQFourManageBean) {
                        Float clientFee = 0.0f;
                        Float commDue = 0.0f;
                        reports = new ReportAIFQuarterFourManage();
                        System.out.println(beans.getClientCode());
                        client = aifClientMasterRepository.findByClientCode(beans.getClientCode());
                        reports.setAifClientMaster(client);
                        reports.setDistributorMaster(distributorMaster);
                        seriesName = seriesMasterRepository.findBySeriesCode(beans.getSeriesName());
                        reports.setSeriesMaster(seriesName);
                        reports.setMnthYear(from);
                        reports.setManageFee(beans.getqFourManage());
                        investorView = investorViewRepository.findByMonthYearAndSeriesMaster(from, seriesName);
                        System.out.println(investorView.getNoOfUnits());
                        reports.setTotIndUnits(beans.getTotNoOfUnits());
                        reports.setPerformFee(beans.getqFourPerform());
                        reports.setTotSeriesUnits(beans.getSumOfUnitSeries());

                        System.out.println(
                            ((reports.getManageFee() + beans.getqFourPerform()) / (beans.getSumOfUnitSeries()))
                                * beans.getTotNoOfUnits());
                        reports.setTotClientFee(beans.getClientFeeCharged());
                        reports.setMnthYear(from);
                        reports.setCommPerc(distributorMaster.getAifCommission());
                        reports.setCommDue(beans.getCommDue());
                        reportAIFQuarterFourManageRepository.save(reports);

                    }

                }

                /*Financial AIF Performance alone template*/
                List<ReportAIFQuarterFourPerformance> reportAIFQuarterFourPerformances = reportAIFQuarterFourPerformanceRepository.findByDistributorMasterAndMnthYear(distributorMaster, from);
                if (reportAIFQuarterFourPerformances.isEmpty() == true) {
                    ReportAIFQuarterFourPerformance reports;
                    AIFClientMaster client = null;
                    SeriesMaster seriesName = null;
                    InvestorView investorView = null;
                    for (ReportQFourPerformanceBean beans : reportQFourPerformanceBean) {
                        Float clientFee = 0.0f;
                        Float commDue = 0.0f;
                        reports = new ReportAIFQuarterFourPerformance();
                        System.out.println(beans.getClientCode());
                        client = aifClientMasterRepository.findByClientCode(beans.getClientCode());
                        reports.setAifClientMaster(client);
                        reports.setDistributorMaster(distributorMaster);
                        seriesName = seriesMasterRepository.findBySeriesCode(beans.getSeriesName());
                        reports.setSeriesMaster(seriesName);
                        reports.setMnthYear(from);
                        reports.setManageFee(beans.getqFourManage());

                        investorView = investorViewRepository.findByMonthYearAndSeriesMaster(from, seriesName);

                        System.out.println(investorView.getNoOfUnits());
                        reports.setTotIndUnits(beans.getTotNoOfUnits());
                        reports.setPerformFee(beans.getqFourPerform());
                        reports.setTotSeriesUnits(beans.getSumOfUnitSeries());


                        System.out.println(
                            ((reports.getManageFee() + beans.getqFourPerform()) / (beans.getSumOfUnitSeries()))
                                * beans.getTotNoOfUnits());
                        reports.setTotClientFee(beans.getClientFeeCharged());
                        reports.setMnthYear(from);
                        reports.setCommPerc(distributorMaster.getAifCommission());
                        reports.setCommDue(beans.getCommDue());
                        reportAIFQuarterFourPerformanceRepository.save(reports);

                    }

                }

                beginCalendar.add(Calendar.MONTH, 1);
            }

            if (sq4 == 0 && sq4f == 0 && sq4j == 0) {

                reportAifGeneration(reportAifDistBean, sMonthStart, sMonthEnd,
                    distributorMaster, sheetAIF, sFinal, cumulativePmsAifBeans, sheetSummary, reportGeneration, workBook, commissionDefinition, cumulativeAIFSeriesBCAD);

            }

            if (sq4 != 0 && sq4f == 0 && sq4j == 0) {
                reportAifQuarterFourGeneration(sMonthStart, sMonthEnd, distributorMaster, sheetAIF, sFinal, cumulativePmsAifBeans,
                    sheetSummary, reportGeneration, workBook, cumulativeAIFSeriesBCAD);
            }
            if (sq4f != 0 && sq4j == 0 && reportGeneration.getAifCalculation().equals("Both")) {
                reportAifQuarterFourFinancialGeneration(sMonthStart,
                    sMonthEnd, distributorMaster, sheetAIF, sFinal, cumulativePmsAifBeans,
                    sheetSummary, workBook, reportGeneration, cumulativeAIFSeriesBCAD);
            }
            if (sq4f != 0 && sq4j == 0 && reportGeneration.getAifCalculation().equals("Management")) {
                reportAifQuarterFourManageGeneration(sMonthStart,
                    sMonthEnd, distributorMaster, sheetAIF, sFinal, cumulativePmsAifBeans,
                    sheetSummary, workBook, reportGeneration, cumulativeAIFSeriesBCAD);
            }
            if (sq4f != 0 && sq4j == 0 && reportGeneration.getAifCalculation().equals("Performance")) {
                reportAifQuarterFourPerformanceGeneration(sMonthStart,
                    sMonthEnd, distributorMaster, sheetAIF, sFinal, cumulativePmsAifBeans,
                    sheetSummary, workBook, reportGeneration, cumulativeAIFSeriesBCAD);
            }
            if (sq4j != 0) {
                reportAifQuarterFourPerGeneration(sMonthStart,
                    sMonthEnd, distributorMaster, sheetAIF, sFinal, cumulativePmsAifBeans,
                    sheetSummary, workBook, reportGeneration, cumulativeAIFSeriesBCAD);
            }

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e);
        }

        System.out.println("Combination" + cumulativePmsAifBeans.size());

    }

    public void reportAifQuarterFourPerGeneration(String sMonthStart, String sMonthEnd, DistributorMaster distributorMaster, HSSFSheet sheetAIF, String sFinal, List<CumulativePmsAifBean> cumulativePmsAifBeans, HSSFSheet sheetSummary, HSSFWorkbook workBook, ReportGeneration reportGeneration, CumulativeAIFSeriesBCAD cumulativeAIFSeriesBCAD) {

        CumulativeDistributorReportBean cumulativeDistributorReportBean = null;
        BigDecimal closing, payableClosing;
        Float tempClosing, tempPayableClosing;
        Float pmsCommission, aifCommission;
        pmsCommission = prop.getFloat("fee.upfront.pms.commission");
        aifCommission = prop.getFloat("fee.upfront.aif.commission");
        Calendar beginCalendar = Calendar.getInstance();
        Calendar finishCalendar = Calendar.getInstance();
        List<String> mnthYr = new ArrayList<>();
        List<ReportAIFQuarterFourPer> reportAifQuarterFourPers = new ArrayList<ReportAIFQuarterFourPer>();

        String from = "";

        DateFormat dateFormat = new SimpleDateFormat("MMM yyyy");
        DateFormat dateMonth = new SimpleDateFormat("yyyy-MM");

        try {

            try {

                beginCalendar.setTime(dateFormat.parse(sMonthStart));
                finishCalendar.setTime(dateFormat.parse(sMonthEnd));
                int year = beginCalendar.get(Calendar.YEAR);
                int sMonth = 04;
                beginCalendar.setTime(dateMonth.parse(year - 1 + "-" + sMonth));

            } catch (ParseException e) {
                e.printStackTrace();
            }
            while (!beginCalendar.after(finishCalendar)) {
                reportQFourPerBean = new ArrayList<ReportQFourPerBean>();
                // add one month to date per loop
                from = dateFormat.format(beginCalendar.getTime());
                mnthYr.add(from);
                beginCalendar.add(Calendar.MONTH, 1);
            }
            sheetAIF.createFreezePane(0, 6);

            CumulativePmsAifBean cumulativePmsAifBean = new CumulativePmsAifBean();
            Float sTotal = 0.0f;
            reportAifQuarterFourPers = reportAIFQuarterFourPerRepository.managQFour(distributorMaster.getId(), mnthYr);

            HSSFFont fFont = workBook.createFont();

            fFont.setBold(true);
            fFont.setFontHeightInPoints((short) 11);
            fFont.setFontName("Calibri");

            HSSFFont defaultFont = workBook.createFont();
            defaultFont.setFontHeightInPoints((short) 11);
            defaultFont.setFontName("Calibri");


            HSSFFont fFontNoBold = workBook.createFont();
            fFontNoBold.setFontHeightInPoints((short) 11);
            fFontNoBold.setFontName("Calibri");

            CellStyle cs = workBook.createCellStyle();
            CellStyle csRight = workBook.createCellStyle();
            CellStyle csPlainNoBorder = workBook.createCellStyle();
            CellStyle csPlanLeftRight = workBook.createCellStyle();
            CellStyle csPlanLeftRightWrap = workBook.createCellStyle();
            CellStyle csMergedCellHeader = cs;

            CellStyle csSecond = workBook.createCellStyle();
            csSecond.setFont(fFontNoBold);
            csSecond.setBorderLeft(BorderStyle.THIN);
            csSecond.setBorderRight(BorderStyle.THIN);

            cs.setFont(fFont);

            CellStyle csMergedCellBody = workBook.createCellStyle();
            csMergedCellBody.setFont(fFontNoBold);
            csMergedCellBody.setBorderLeft(BorderStyle.THIN);
            csMergedCellBody.setBorderRight(BorderStyle.THIN);
            csMergedCellBody.setBorderTop(BorderStyle.NONE);
            csMergedCellBody.setBorderBottom(BorderStyle.NONE);
            csMergedCellBody.setWrapText(true);


            csPlainNoBorder.setFont(fFont);
            csPlainNoBorder.setBorderBottom(BorderStyle.NONE);
            csPlainNoBorder.setBorderTop(BorderStyle.NONE);
            csPlainNoBorder.setBorderLeft(BorderStyle.NONE);
            csPlainNoBorder.setBorderRight(BorderStyle.NONE);


            csPlanLeftRight.cloneStyleFrom(csPlainNoBorder);//HEre//cs
            csPlanLeftRightWrap.cloneStyleFrom(csPlanLeftRight);
            csPlanLeftRightWrap.setWrapText(true);


            csRight.setAlignment(HorizontalAlignment.RIGHT);

            // Format sheet
            CellStyle csHorVerCenter = workBook.createCellStyle();

            csRight.setAlignment(HorizontalAlignment.LEFT);
            // Format sheet
            csHorVerCenter.setAlignment(HorizontalAlignment.CENTER);
            csHorVerCenter.setVerticalAlignment(VerticalAlignment.CENTER);
            csHorVerCenter.setFont(fFont);
            csHorVerCenter.setWrapText(true);

            csMergedCellHeader.setAlignment(HorizontalAlignment.CENTER);
            csMergedCellHeader.setVerticalAlignment(VerticalAlignment.CENTER);

            csHorVerCenter.setBorderTop(BorderStyle.THIN);
            csHorVerCenter.setBorderBottom(BorderStyle.THIN);
            csHorVerCenter.setBorderLeft(BorderStyle.THIN);
            csHorVerCenter.setBorderRight(BorderStyle.THIN);

            csPlanLeftRight.setBorderLeft(BorderStyle.THIN);
            csPlanLeftRight.setBorderRight(BorderStyle.THIN);
            csPlanLeftRight.setFont(fFontNoBold);

            CellStyle csDF = StyleUtil.getStyleDataFormat(workBook);

            CellStyle csDFBold = StyleUtil.getStyleBoldDataFormat(workBook);

            CellStyle csPerc = StyleUtil.getStylePercDataFormat(workBook);

            csDF.setBorderLeft(BorderStyle.THIN);
            csDF.setBorderRight(BorderStyle.THIN);
            csDF.setFont(defaultFont);

            HSSFRow rowUserName = sheetAIF.createRow(sheetAIF.getLastRowNum() + 1);
            rowUserName.createCell(0).setCellValue(distributorMaster.getDistName().trim().toUpperCase());
            rowUserName.getCell(0).setCellStyle(csPlainNoBorder);

            HSSFRow durationFrom = sheetAIF.createRow(sheetAIF.getLastRowNum() + 1);
            durationFrom.createCell(0).setCellValue("FEES PAYABALE FOR " + sMonthStart + "-" + sMonthEnd);
            durationFrom.getCell(0).setCellStyle(csPlainNoBorder);

            // Format sheet
            sheetAIF.addMergedRegion(new CellRangeAddress(1, 1, 0, 3));
            sheetAIF.addMergedRegion(new CellRangeAddress(2, 2, 0, 3));

            int iRowNo = sheetAIF.getLastRowNum() + 3;
            HSSFRow headingBRSBookRow = sheetAIF.createRow(iRowNo);
            headingBRSBookRow.setHeightInPoints(30);

            sheetAIF.addMergedRegion(new CellRangeAddress(5, 5, 0, 2));

            headingBRSBookRow.createCell(0).setCellValue(prop.getString("aif.cell.client_name"));
            headingBRSBookRow.getCell(0).setCellStyle(csMergedCellHeader);

            headingBRSBookRow.createCell(1).setCellValue("");
            headingBRSBookRow.createCell(2).setCellValue("");
            headingBRSBookRow.getCell(1).setCellStyle(csMergedCellHeader);
            headingBRSBookRow.getCell(2).setCellStyle(csMergedCellHeader);


            headingBRSBookRow.createCell(3).setCellValue(prop.getString("aif.cell.series.name"));
            headingBRSBookRow.getCell(3).setCellStyle(csHorVerCenter);

            headingBRSBookRow.createCell(4).setCellValue("Month");
            headingBRSBookRow.getCell(4).setCellStyle(csHorVerCenter);

            headingBRSBookRow.createCell(5).setCellValue(prop.getString("aif.cell.manage.fee"));
            headingBRSBookRow.getCell(5).setCellStyle(csHorVerCenter);

            headingBRSBookRow.createCell(6).setCellValue(prop.getString("aif.cell.perform.fee"));
            headingBRSBookRow.getCell(6).setCellStyle(csHorVerCenter);

            headingBRSBookRow.createCell(7).setCellValue(prop.getString("aif.cell.sum.series"));
            headingBRSBookRow.getCell(7).setCellStyle(csHorVerCenter);

            headingBRSBookRow.createCell(8).setCellValue(prop.getString("aif.cell.tot.units"));
            headingBRSBookRow.getCell(8).setCellStyle(csHorVerCenter);

            headingBRSBookRow.createCell(9).setCellValue(prop.getString("aif.cell.manage.fee.charge"));
            headingBRSBookRow.getCell(9).setCellStyle(csHorVerCenter);

            headingBRSBookRow.createCell(10).setCellValue(prop.getString("aif.cell.total.comm"));
            headingBRSBookRow.getCell(10).setCellStyle(csHorVerCenter);
            // sheet.autoSizeColumn(7);

            iRowNo = sheetAIF.getLastRowNum() + 1;

            HSSFRow blankRow = sheetAIF.createRow(iRowNo);

            int iTotCell = 0;
            HSSFCell blankCell = null;
            //sheet.addMergedRegion(new CellRangeAddress(iRowNo, iRowNo, 0, 1));
            CellStyle csPlainLeft = csPlanLeftRight;
            csPlainLeft.setBorderRight(BorderStyle.NONE);

            CellStyle csPlainRight = csPlanLeftRight;
            csPlainRight.setBorderLeft(BorderStyle.NONE);

            sheetAIF.addMergedRegion(new CellRangeAddress(iRowNo, iRowNo, 0, 2));
            CellStyle csPlainNoBottom = cs;
            csPlainNoBottom.setBorderBottom(BorderStyle.NONE);
            while (iTotCell < 9 || iTotCell == 9) {

                blankCell = blankRow.createCell(iTotCell);
                blankCell.setCellValue("");

                if (iTotCell != 0 && iTotCell != 1 && iTotCell != 2)
                    blankCell.setCellStyle(csPlainNoBottom);//cs
                iTotCell++;
            }

            iRowNo = sheetAIF.getLastRowNum() + 1;

            DecimalFormat decimalFormat = new DecimalFormat("#.0000");
            String sManage = "";
            String sPerform = "";
            String sSum = "";
            String sNoOf = "";
            String sFeeCharged = "";
            String sCommission = "";
            for (ReportAIFQuarterFourPer bean : reportAifQuarterFourPers) {
                sheetAIF.addMergedRegion(new CellRangeAddress(iRowNo, iRowNo, 0, 2));
                HSSFRow row = sheetAIF.createRow(iRowNo);

                row.createCell(0).setCellValue(bean.getAifClientMaster().getClientName());
                row.getCell(0).setCellStyle(csMergedCellBody);

                row.createCell(1).setCellValue("");

                row.createCell(2).setCellValue("");

                row.createCell(3).setCellValue(bean.getSeriesMaster().getSeriesCode());
                row.getCell(3).setCellStyle(csSecond);

                row.createCell(4).setCellValue(bean.getMnthYear());
                row.getCell(4).setCellStyle(csSecond);


                sManage = decimalFormat.format(bean.getManageFee());
                HSSFCell cell = row.createCell(5);
                cell.setCellType(CellType.NUMERIC);
                cell.setCellValue(Double.parseDouble(sManage));
                cell.setCellStyle(csDF);


                sPerform = decimalFormat.format(bean.getPerformFee());
                cell = row.createCell(6);
                cell.setCellType(CellType.NUMERIC);
                cell.setCellValue(Double.parseDouble(sPerform));
                cell.setCellStyle(csDF);


                sSum = decimalFormat.format(bean.getTotSeriesUnits());
                cell = row.createCell(7);
                cell.setCellType(CellType.NUMERIC);
                cell.setCellStyle(csDF);
                cell.setCellValue(Double.parseDouble(sSum));


                sNoOf = decimalFormat.format(bean.getTotIndUnits());
                cell = row.createCell(8);
                cell.setCellType(CellType.NUMERIC);
                cell.setCellStyle(csDF);
                cell.setCellValue(Double.parseDouble(sNoOf));

                sFeeCharged = decimalFormat.format(bean.getTotClntFee());
                cell = row.createCell(9);
                cell.setCellType(CellType.NUMERIC);
                cell.setCellStyle(csDF);
                cell.setCellValue(Double.parseDouble(sFeeCharged));

                sCommission = decimalFormat.format(bean.getCommDue());
                cell = row.createCell(10);
                cell.setCellValue(Double.parseDouble(sCommission));
                cell.setCellType(CellType.NUMERIC);
                cell.setCellStyle(csDF);


                sTotal += bean.getCommDue();
                iRowNo++;
            }
            String aifTotal = "";
            int iCurrRow = sheetAIF.getLastRowNum() + 1;
            HSSFRow row = sheetAIF.createRow(iCurrRow);

            sheetAIF.addMergedRegion(new CellRangeAddress(iCurrRow, iCurrRow, 0, 2));
            CellStyle csFour = cs;
            CellStyle csDFBoldFour = csDFBold;

            csFour.setBorderBottom(BorderStyle.THIN);
            csFour.setBorderTop(BorderStyle.THIN);
            csFour.setBorderLeft(BorderStyle.THIN);
            csFour.setBorderRight(BorderStyle.THIN);

            csDFBoldFour.setBorderBottom(BorderStyle.THIN);
            csDFBoldFour.setBorderTop(BorderStyle.THIN);
            csDFBoldFour.setBorderLeft(BorderStyle.THIN);
            csDFBoldFour.setBorderRight(BorderStyle.THIN);


            HSSFCell totCell = null;
            iTotCell = 0;
            while (iTotCell < 8 || iTotCell == 8) {
                totCell = row.createCell(iTotCell);
                totCell.setCellValue("");
                totCell.setCellStyle(csDFBoldFour);
                iTotCell++;
            }


            HSSFCell cell = row.createCell(9);
            cell.setCellValue("TOTAL");
            cell.setCellStyle(csFour);


            aifTotal = decimalFormat.format(sTotal);
            cell = row.createCell(10);
            cell.setCellValue(sTotal);
            cell.setCellStyle(csDFBoldFour);

            sheetAIF.setDefaultColumnWidth(18);
            sheetAIF.autoSizeColumn(1);
            sheetAIF.autoSizeColumn(2);
            cumulativePmsAifBean.setAifName("Total AIF");
            cumulativePmsAifBean.setAifTotal(aifTotal);
            cumulativePmsAifBeans.add(cumulativePmsAifBean);

            Double totalTrail = 0.0;
            Double totalUpfront = 0.0;
            Double totalClosing = 0.0;
            Double payable = 0.0;
            Double currentPayable = 0d;
            if (distributorMaster.getDistModelType().equals("Upfront")) {
                cumulativeDistributorReportBean = feeTrailUpfrontTransService.getDistributorCurrentReport(distributorMaster,
                    pmsCommission, reportGeneration.getStartDate(), reportGeneration.getToDate());

                closing = cumulativeDistributorReportBean.getCurrentUpfrontAmt()
                    .add(cumulativeDistributorReportBean.getCurrentTrailAmt());
                tempClosing = Float.valueOf(cumulativeDistributorReportBean.getCurrentUpfrontAmt().floatValue())
                    + Float.valueOf(cumulativeDistributorReportBean.getCurrentTrailAmt().floatValue());
                totalTrail = feeTrailUpfrontTransRepository.getTotalTrail(distributorMaster.getId(), reportGeneration.getToDate());
                totalUpfront = feeTrailUpfrontTransRepository.getTotalUpfront(distributorMaster.getId(), reportGeneration.getToDate());
                totalClosing = totalUpfront + totalTrail;
                payable = feeTrailUpfrontTransRepository.getTotalPayable(distributorMaster.getId(), reportGeneration.getToDate());
                Double paid = feeTrailUpfrontTransRepository.getTotalPaid(distributorMaster.getId(), reportGeneration.getToDate());

                if (totalClosing > 0) {
                    currentPayable = (totalClosing + payable) - paid;
                } else {
                    currentPayable = payable - paid;
                }
                BigDecimal totalPayable = cumulativeDistributorReportBean.getCurrentTotalPayableAmt()
                    .subtract(cumulativeDistributorReportBean.getCurrentPaidAmount());
            }
            summarySheetService.cumulativeFee(cumulativePmsAifBeans, sMonthStart, sMonthEnd, distributorMaster.getDistName().trim(), sheetSummary, sFinal,
                cumulativeDistributorReportBean, totalClosing, currentPayable, totalTrail, totalUpfront, distributorMaster, reportGeneration, workBook, cumulativeAIFSeriesBCAD);


        } catch (Exception e) {
            System.out.println(e);
        }


    }

    public void reportAifQuarterFourPerformanceGeneration(String sMonthStart, String sMonthEnd, DistributorMaster distributorMaster, HSSFSheet sheetAIF, String sFinal, List<CumulativePmsAifBean> cumulativePmsAifBeans, HSSFSheet sheetSummary, HSSFWorkbook workBook, ReportGeneration reportGeneration, CumulativeAIFSeriesBCAD cumulativeAIFSeriesBCAD) {

        CumulativeDistributorReportBean cumulativeDistributorReportBean = null;
        BigDecimal closing, payableClosing;
        Float tempClosing, tempPayableClosing;
        Float pmsCommission, aifCommission;
        pmsCommission = prop.getFloat("fee.upfront.pms.commission");
        aifCommission = prop.getFloat("fee.upfront.aif.commission");
        Calendar beginCalendar = Calendar.getInstance();
        Calendar finishCalendar = Calendar.getInstance();
        List<String> mnthYr = new ArrayList<>();
        List<ReportAIFQuarterFourPerformance> reportAifQuarterFourPerformances = new ArrayList<ReportAIFQuarterFourPerformance>();

        String from = "";

        DateFormat dateFormat = new SimpleDateFormat("MMM yyyy");
        DateFormat dateMonth = new SimpleDateFormat("yyyy-MM");

        try {

            try {

                beginCalendar.setTime(dateFormat.parse(sMonthStart));
                finishCalendar.setTime(dateFormat.parse(sMonthEnd));
                int year = beginCalendar.get(Calendar.YEAR);
                int sMonth = 04;
                beginCalendar.setTime(dateMonth.parse(year + "-" + sMonth));

            } catch (ParseException e) {
                e.printStackTrace();
            }
            while (!beginCalendar.after(finishCalendar)) {
                reportQFourPerformanceBean = new ArrayList<ReportQFourPerformanceBean>();
                // add one month to date per loop
                from = dateFormat.format(beginCalendar.getTime());
                mnthYr.add(from);
                beginCalendar.add(Calendar.MONTH, 1);
            }
            sheetAIF.createFreezePane(0, 6);

            CumulativePmsAifBean cumulativePmsAifBean = new CumulativePmsAifBean();
            Float sTotal = 0.0f;
            reportAifQuarterFourPerformances = reportAIFQuarterFourPerformanceRepository.perfQFour(distributorMaster.getId(), mnthYr);

            HSSFFont fFont = workBook.createFont();

            fFont.setBold(true);
            fFont.setFontHeightInPoints((short) 11);
            fFont.setFontName("Calibri");

            HSSFFont defaultFont = workBook.createFont();
            defaultFont.setFontHeightInPoints((short) 11);
            defaultFont.setFontName("Calibri");


            HSSFFont fFontNoBold = workBook.createFont();
            fFontNoBold.setFontHeightInPoints((short) 11);
            fFontNoBold.setFontName("Calibri");

            CellStyle cs = workBook.createCellStyle();
            CellStyle csRight = workBook.createCellStyle();
            CellStyle csPlainNoBorder = workBook.createCellStyle();
            CellStyle csPlanLeftRight = workBook.createCellStyle();
            CellStyle csPlanLeftRightWrap = workBook.createCellStyle();
            CellStyle csMergedCellHeader = cs;

            CellStyle csSecond = workBook.createCellStyle();
            csSecond.setFont(fFontNoBold);
            csSecond.setBorderLeft(BorderStyle.THIN);
            csSecond.setBorderRight(BorderStyle.THIN);

            cs.setFont(fFont);

            CellStyle csMergedCellBody = workBook.createCellStyle();
            csMergedCellBody.setFont(fFontNoBold);
            csMergedCellBody.setBorderLeft(BorderStyle.THIN);
            csMergedCellBody.setBorderRight(BorderStyle.THIN);
            csMergedCellBody.setBorderTop(BorderStyle.NONE);
            csMergedCellBody.setBorderBottom(BorderStyle.NONE);
            csMergedCellBody.setWrapText(true);

            csPlainNoBorder.setFont(fFont);
            csPlainNoBorder.setBorderBottom(BorderStyle.NONE);
            csPlainNoBorder.setBorderTop(BorderStyle.NONE);
            csPlainNoBorder.setBorderLeft(BorderStyle.NONE);
            csPlainNoBorder.setBorderRight(BorderStyle.NONE);

            csPlanLeftRight.cloneStyleFrom(csPlainNoBorder);//HEre//cs
            csPlanLeftRightWrap.cloneStyleFrom(csPlanLeftRight);
            csPlanLeftRightWrap.setWrapText(true);

            csRight.setAlignment(HorizontalAlignment.RIGHT);

            // Format sheet
            CellStyle csHorVerCenter = workBook.createCellStyle();

            csRight.setAlignment(HorizontalAlignment.LEFT);
            // Format sheet
            csHorVerCenter.setAlignment(HorizontalAlignment.CENTER);
            csHorVerCenter.setVerticalAlignment(VerticalAlignment.CENTER);
            csHorVerCenter.setFont(fFont);
            csHorVerCenter.setWrapText(true);

            csMergedCellHeader.setAlignment(HorizontalAlignment.CENTER);
            csMergedCellHeader.setVerticalAlignment(VerticalAlignment.CENTER);

            csHorVerCenter.setBorderTop(BorderStyle.THIN);
            csHorVerCenter.setBorderBottom(BorderStyle.THIN);
            csHorVerCenter.setBorderLeft(BorderStyle.THIN);
            csHorVerCenter.setBorderRight(BorderStyle.THIN);

            csPlanLeftRight.setBorderLeft(BorderStyle.THIN);
            csPlanLeftRight.setBorderRight(BorderStyle.THIN);
            csPlanLeftRight.setFont(fFontNoBold);

            CellStyle csDF = StyleUtil.getStyleDataFormat(workBook);

            CellStyle csDFBold = StyleUtil.getStyleBoldDataFormat(workBook);

            CellStyle csPerc = StyleUtil.getStylePercDataFormat(workBook);

            csDF.setBorderLeft(BorderStyle.THIN);
            csDF.setBorderRight(BorderStyle.THIN);
            csDF.setFont(defaultFont);

            HSSFRow rowUserName = sheetAIF.createRow(sheetAIF.getLastRowNum() + 1);
            rowUserName.createCell(0).setCellValue(distributorMaster.getDistName().trim().toUpperCase());
            rowUserName.getCell(0).setCellStyle(csPlainNoBorder);

            HSSFRow durationFrom = sheetAIF.createRow(sheetAIF.getLastRowNum() + 1);
            durationFrom.createCell(0).setCellValue("FEES PAYABALE FOR " + sMonthStart + "-" + sMonthEnd);
            durationFrom.getCell(0).setCellStyle(csPlainNoBorder);

            // Format sheet
            sheetAIF.addMergedRegion(new CellRangeAddress(1, 1, 0, 3));
            sheetAIF.addMergedRegion(new CellRangeAddress(2, 2, 0, 3));

            int iRowNo = sheetAIF.getLastRowNum() + 3;
            HSSFRow headingBRSBookRow = sheetAIF.createRow(iRowNo);
            headingBRSBookRow.setHeightInPoints(30);

            sheetAIF.addMergedRegion(new CellRangeAddress(5, 5, 0, 2));

            headingBRSBookRow.createCell(0).setCellValue(prop.getString("aif.cell.client_name"));
            headingBRSBookRow.getCell(0).setCellStyle(csMergedCellHeader);

            headingBRSBookRow.createCell(1).setCellValue("");
            headingBRSBookRow.createCell(2).setCellValue("");
            headingBRSBookRow.getCell(1).setCellStyle(csMergedCellHeader);
            headingBRSBookRow.getCell(2).setCellStyle(csMergedCellHeader);


            headingBRSBookRow.createCell(3).setCellValue(prop.getString("aif.cell.series.name"));
            headingBRSBookRow.getCell(3).setCellStyle(csHorVerCenter);

            headingBRSBookRow.createCell(4).setCellValue("Month");
            headingBRSBookRow.getCell(4).setCellStyle(csHorVerCenter);

            headingBRSBookRow.createCell(5).setCellValue(prop.getString("aif.cell.manage.fee"));
            headingBRSBookRow.getCell(5).setCellStyle(csHorVerCenter);

            headingBRSBookRow.createCell(6).setCellValue(prop.getString("aif.cell.perform.fee"));
            headingBRSBookRow.getCell(6).setCellStyle(csHorVerCenter);

            headingBRSBookRow.createCell(7).setCellValue(prop.getString("aif.cell.sum.series"));
            headingBRSBookRow.getCell(7).setCellStyle(csHorVerCenter);

            headingBRSBookRow.createCell(8).setCellValue(prop.getString("aif.cell.tot.units"));
            headingBRSBookRow.getCell(8).setCellStyle(csHorVerCenter);

            headingBRSBookRow.createCell(9).setCellValue(prop.getString("aif.cell.manage.fee.charge"));
            headingBRSBookRow.getCell(9).setCellStyle(csHorVerCenter);

            headingBRSBookRow.createCell(10).setCellValue(prop.getString("aif.cell.total.comm"));
            headingBRSBookRow.getCell(10).setCellStyle(csHorVerCenter);


            iRowNo = sheetAIF.getLastRowNum() + 1;

            HSSFRow blankRow = sheetAIF.createRow(iRowNo);

            int iTotCell = 0;
            HSSFCell blankCell = null;
            //sheet.addMergedRegion(new CellRangeAddress(iRowNo, iRowNo, 0, 1));
            CellStyle csPlainLeft = csPlanLeftRight;
            csPlainLeft.setBorderRight(BorderStyle.NONE);

            CellStyle csPlainRight = csPlanLeftRight;
            csPlainRight.setBorderLeft(BorderStyle.NONE);

            sheetAIF.addMergedRegion(new CellRangeAddress(iRowNo, iRowNo, 0, 2));
            CellStyle csPlainNoBottom = cs;
            csPlainNoBottom.setBorderBottom(BorderStyle.NONE);
            while (iTotCell < 9 || iTotCell == 9) {

                blankCell = blankRow.createCell(iTotCell);
                blankCell.setCellValue("");

                if (iTotCell != 0 && iTotCell != 1 && iTotCell != 2)
                    blankCell.setCellStyle(csPlainNoBottom);//cs
                iTotCell++;
            }

            iRowNo = sheetAIF.getLastRowNum() + 1;

            DecimalFormat decimalFormat = new DecimalFormat("#.0000");
            String sManage = "";
            String sPerform = "";
            String sSum = "";
            String sNoOf = "";
            String sFeeCharged = "";
            String sCommission = "";
            for (ReportAIFQuarterFourPerformance bean : reportAifQuarterFourPerformances) {
                sheetAIF.addMergedRegion(new CellRangeAddress(iRowNo, iRowNo, 0, 2));
                HSSFRow row = sheetAIF.createRow(iRowNo);

                row.createCell(0).setCellValue(bean.getAifClientMaster().getClientName());
                row.getCell(0).setCellStyle(csMergedCellBody);

                row.createCell(1).setCellValue("");

                row.createCell(2).setCellValue("");

                row.createCell(3).setCellValue(bean.getSeriesMaster().getSeriesCode());
                row.getCell(3).setCellStyle(csSecond);

                row.createCell(4).setCellValue(bean.getMnthYear());
                row.getCell(4).setCellStyle(csSecond);


                sManage = decimalFormat.format(bean.getManageFee());
                HSSFCell cell = row.createCell(5);
                cell.setCellType(CellType.NUMERIC);
                cell.setCellValue(Double.parseDouble(sManage));
                cell.setCellStyle(csDF);

                sPerform = decimalFormat.format(bean.getPerformFee());
                cell = row.createCell(6);
                cell.setCellType(CellType.NUMERIC);
                cell.setCellValue(Double.parseDouble(sPerform));
                cell.setCellStyle(csDF);

                sSum = decimalFormat.format(bean.getTotSeriesUnits());
                cell = row.createCell(7);
                cell.setCellType(CellType.NUMERIC);
                cell.setCellStyle(csDF);
                cell.setCellValue(Double.parseDouble(sSum));


                sNoOf = decimalFormat.format(bean.getTotIndUnits());
                cell = row.createCell(8);
                cell.setCellType(CellType.NUMERIC);
                cell.setCellStyle(csDF);
                cell.setCellValue(Double.parseDouble(sNoOf));

                sFeeCharged = decimalFormat.format(bean.getTotClientFee());
                cell = row.createCell(9);
                cell.setCellType(CellType.NUMERIC);
                cell.setCellStyle(csDF);
                cell.setCellValue(Double.parseDouble(sFeeCharged));

                sCommission = decimalFormat.format(bean.getCommDue());
                cell = row.createCell(10);
                cell.setCellValue(Double.parseDouble(sCommission));
                cell.setCellType(CellType.NUMERIC);
                cell.setCellStyle(csDF);


                sTotal += bean.getCommDue();
                iRowNo++;
            }
            String aifTotal = "";
            int iCurrRow = sheetAIF.getLastRowNum() + 1;
            HSSFRow row = sheetAIF.createRow(iCurrRow);

            sheetAIF.addMergedRegion(new CellRangeAddress(iCurrRow, iCurrRow, 0, 2));
            CellStyle csFour = cs;
            CellStyle csDFBoldFour = csDFBold;

            csFour.setBorderBottom(BorderStyle.THIN);
            csFour.setBorderTop(BorderStyle.THIN);
            csFour.setBorderLeft(BorderStyle.THIN);
            csFour.setBorderRight(BorderStyle.THIN);

            csDFBoldFour.setBorderBottom(BorderStyle.THIN);
            csDFBoldFour.setBorderTop(BorderStyle.THIN);
            csDFBoldFour.setBorderLeft(BorderStyle.THIN);
            csDFBoldFour.setBorderRight(BorderStyle.THIN);


            HSSFCell totCell = null;
            iTotCell = 0;
            while (iTotCell < 8 || iTotCell == 8) {
                totCell = row.createCell(iTotCell);
                totCell.setCellValue("");
                totCell.setCellStyle(csDFBoldFour);
                iTotCell++;
            }


            HSSFCell cell = row.createCell(9);
            cell.setCellValue("TOTAL");
            cell.setCellStyle(csFour);

            aifTotal = decimalFormat.format(sTotal);
            cell = row.createCell(10);
            cell.setCellValue(sTotal);
            cell.setCellStyle(csDFBoldFour);

            sheetAIF.setDefaultColumnWidth(18);
            sheetAIF.autoSizeColumn(1);
            sheetAIF.autoSizeColumn(2);
            cumulativePmsAifBean.setAifName("Total AIF");
            cumulativePmsAifBean.setAifTotal(aifTotal);
            cumulativePmsAifBeans.add(cumulativePmsAifBean);

            Double totalTrail = 0.0;
            Double totalUpfront = 0.0;
            Double totalClosing = 0.0;
            Double payable = 0.0;
            Double currentPayable = 0d;
            if (distributorMaster.getDistModelType().equals("Upfront")) {
                cumulativeDistributorReportBean = feeTrailUpfrontTransService.getDistributorCurrentReport(distributorMaster,
                    pmsCommission, reportGeneration.getStartDate(), reportGeneration.getToDate());

                closing = cumulativeDistributorReportBean.getCurrentUpfrontAmt()
                    .add(cumulativeDistributorReportBean.getCurrentTrailAmt());
                tempClosing = Float.valueOf(cumulativeDistributorReportBean.getCurrentUpfrontAmt().floatValue())
                    + Float.valueOf(cumulativeDistributorReportBean.getCurrentTrailAmt().floatValue());
                totalTrail = feeTrailUpfrontTransRepository.getTotalTrail(distributorMaster.getId(), reportGeneration.getToDate());
                totalUpfront = feeTrailUpfrontTransRepository.getTotalUpfront(distributorMaster.getId(), reportGeneration.getToDate());
                totalClosing = totalUpfront + totalTrail;
                payable = feeTrailUpfrontTransRepository.getTotalPayable(distributorMaster.getId(), reportGeneration.getToDate());
                Double paid = feeTrailUpfrontTransRepository.getTotalPaid(distributorMaster.getId(), reportGeneration.getToDate());

                if (totalClosing > 0) {
                    currentPayable = (totalClosing + payable) - paid;
                } else {
                    currentPayable = payable - paid;
                }
                BigDecimal totalPayable = cumulativeDistributorReportBean.getCurrentTotalPayableAmt()
                    .subtract(cumulativeDistributorReportBean.getCurrentPaidAmount());
            }
            summarySheetService.cumulativeFee(cumulativePmsAifBeans, sMonthStart, sMonthEnd, distributorMaster.getDistName().trim(), sheetSummary, sFinal,
                cumulativeDistributorReportBean, totalClosing, currentPayable, totalTrail, totalUpfront, distributorMaster, reportGeneration, workBook, cumulativeAIFSeriesBCAD);


        } catch (Exception e) {
            System.out.println(e);
        }

    }

    public void reportAifQuarterFourManageGeneration(String sMonthStart, String sMonthEnd, DistributorMaster distributorMaster, HSSFSheet sheetAIF, String sFinal, List<CumulativePmsAifBean> cumulativePmsAifBeans, HSSFSheet sheetSummary, HSSFWorkbook workBook, ReportGeneration reportGeneration, CumulativeAIFSeriesBCAD cumulativeAIFSeriesBCAD) {

        CumulativeDistributorReportBean cumulativeDistributorReportBean = null;
        BigDecimal closing, payableClosing;
        Float tempClosing, tempPayableClosing;
        Float pmsCommission, aifCommission;
        pmsCommission = prop.getFloat("fee.upfront.pms.commission");
        aifCommission = prop.getFloat("fee.upfront.aif.commission");
        Calendar beginCalendar = Calendar.getInstance();
        Calendar finishCalendar = Calendar.getInstance();
        List<String> mnthYr = new ArrayList<>();
        List<ReportAIFQuarterFourManage> reportAIFQuarterFourManages = new ArrayList<ReportAIFQuarterFourManage>();

        String from = "";

        DateFormat dateFormat = new SimpleDateFormat("MMM yyyy");
        DateFormat dateMonth = new SimpleDateFormat("yyyy-MM");

        try {

            try {

                beginCalendar.setTime(dateFormat.parse(sMonthStart));
                finishCalendar.setTime(dateFormat.parse(sMonthEnd));
                int year = beginCalendar.get(Calendar.YEAR);
                int sMonth = 04;
                beginCalendar.setTime(dateMonth.parse(year + "-" + sMonth));

            } catch (ParseException e) {
                e.printStackTrace();
            }
            while (!beginCalendar.after(finishCalendar)) {
                reportQFourManageBean = new ArrayList<ReportQFourManageBean>();
                // add one month to date per loop
                from = dateFormat.format(beginCalendar.getTime());
                mnthYr.add(from);
                beginCalendar.add(Calendar.MONTH, 1);
            }
            sheetAIF.createFreezePane(0, 6);

            CumulativePmsAifBean cumulativePmsAifBean = new CumulativePmsAifBean();
            Float sTotal = 0.0f;
            reportAIFQuarterFourManages = reportAIFQuarterFourManageRepository.managQFour(distributorMaster.getId(), mnthYr);

            HSSFFont fFont = workBook.createFont();

            fFont.setBold(true);
            fFont.setFontHeightInPoints((short) 11);
            fFont.setFontName("Calibri");

            HSSFFont defaultFont = workBook.createFont();
            defaultFont.setFontHeightInPoints((short) 11);
            defaultFont.setFontName("Calibri");


            HSSFFont fFontNoBold = workBook.createFont();
            fFontNoBold.setFontHeightInPoints((short) 11);
            fFontNoBold.setFontName("Calibri");

            CellStyle cs = workBook.createCellStyle();
            CellStyle csRight = workBook.createCellStyle();
            CellStyle csPlainNoBorder = workBook.createCellStyle();
            CellStyle csPlanLeftRight = workBook.createCellStyle();
            CellStyle csPlanLeftRightWrap = workBook.createCellStyle();
            CellStyle csMergedCellHeader = cs;

            CellStyle csSecond = workBook.createCellStyle();
            csSecond.setFont(fFontNoBold);
            csSecond.setBorderLeft(BorderStyle.THIN);
            csSecond.setBorderRight(BorderStyle.THIN);

            cs.setFont(fFont);

            CellStyle csMergedCellBody = workBook.createCellStyle();
            csMergedCellBody.setFont(fFontNoBold);
            csMergedCellBody.setBorderLeft(BorderStyle.THIN);
            csMergedCellBody.setBorderRight(BorderStyle.THIN);
            csMergedCellBody.setBorderTop(BorderStyle.NONE);
            csMergedCellBody.setBorderBottom(BorderStyle.NONE);
            csMergedCellBody.setWrapText(true);


            csPlainNoBorder.setFont(fFont);
            csPlainNoBorder.setBorderBottom(BorderStyle.NONE);
            csPlainNoBorder.setBorderTop(BorderStyle.NONE);
            csPlainNoBorder.setBorderLeft(BorderStyle.NONE);
            csPlainNoBorder.setBorderRight(BorderStyle.NONE);


            csPlanLeftRight.cloneStyleFrom(csPlainNoBorder);//HEre//cs
            csPlanLeftRightWrap.cloneStyleFrom(csPlanLeftRight);
            csPlanLeftRightWrap.setWrapText(true);


            csRight.setAlignment(HorizontalAlignment.RIGHT);

            // Format sheet
            CellStyle csHorVerCenter = workBook.createCellStyle();

            csRight.setAlignment(HorizontalAlignment.LEFT);
            // Format sheet
            csHorVerCenter.setAlignment(HorizontalAlignment.CENTER);
            csHorVerCenter.setVerticalAlignment(VerticalAlignment.CENTER);
            csHorVerCenter.setFont(fFont);
            csHorVerCenter.setWrapText(true);

            csMergedCellHeader.setAlignment(HorizontalAlignment.CENTER);
            csMergedCellHeader.setVerticalAlignment(VerticalAlignment.CENTER);

            csHorVerCenter.setBorderTop(BorderStyle.THIN);
            csHorVerCenter.setBorderBottom(BorderStyle.THIN);
            csHorVerCenter.setBorderLeft(BorderStyle.THIN);
            csHorVerCenter.setBorderRight(BorderStyle.THIN);

            csPlanLeftRight.setBorderLeft(BorderStyle.THIN);
            csPlanLeftRight.setBorderRight(BorderStyle.THIN);
            csPlanLeftRight.setFont(fFontNoBold);

            CellStyle csDF = StyleUtil.getStyleDataFormat(workBook);

            CellStyle csDFBold = StyleUtil.getStyleBoldDataFormat(workBook);

            CellStyle csPerc = StyleUtil.getStylePercDataFormat(workBook);

            csDF.setBorderLeft(BorderStyle.THIN);
            csDF.setBorderRight(BorderStyle.THIN);
            csDF.setFont(defaultFont);

            HSSFRow rowUserName = sheetAIF.createRow(sheetAIF.getLastRowNum() + 1);
            rowUserName.createCell(0).setCellValue(distributorMaster.getDistName().trim().toUpperCase());
            rowUserName.getCell(0).setCellStyle(csPlainNoBorder);

            HSSFRow durationFrom = sheetAIF.createRow(sheetAIF.getLastRowNum() + 1);
            durationFrom.createCell(0).setCellValue("FEES PAYABALE FOR " + sMonthStart + "-" + sMonthEnd);
            durationFrom.getCell(0).setCellStyle(csPlainNoBorder);

            // Format sheet
            sheetAIF.addMergedRegion(new CellRangeAddress(1, 1, 0, 3));
            sheetAIF.addMergedRegion(new CellRangeAddress(2, 2, 0, 3));

            int iRowNo = sheetAIF.getLastRowNum() + 3;
            HSSFRow headingBRSBookRow = sheetAIF.createRow(iRowNo);
            headingBRSBookRow.setHeightInPoints(30);

            sheetAIF.addMergedRegion(new CellRangeAddress(5, 5, 0, 2));

            headingBRSBookRow.createCell(0).setCellValue(prop.getString("aif.cell.client_name"));
            headingBRSBookRow.getCell(0).setCellStyle(csMergedCellHeader);

            headingBRSBookRow.createCell(1).setCellValue("");
            headingBRSBookRow.createCell(2).setCellValue("");
            headingBRSBookRow.getCell(1).setCellStyle(csMergedCellHeader);
            headingBRSBookRow.getCell(2).setCellStyle(csMergedCellHeader);

            headingBRSBookRow.createCell(3).setCellValue(prop.getString("aif.cell.series.name"));
            headingBRSBookRow.getCell(3).setCellStyle(csHorVerCenter);

            headingBRSBookRow.createCell(4).setCellValue("Month");
            headingBRSBookRow.getCell(4).setCellStyle(csHorVerCenter);

            headingBRSBookRow.createCell(5).setCellValue(prop.getString("aif.cell.manage.fee"));
            headingBRSBookRow.getCell(5).setCellStyle(csHorVerCenter);

            headingBRSBookRow.createCell(6).setCellValue(prop.getString("aif.cell.perform.fee"));
            headingBRSBookRow.getCell(6).setCellStyle(csHorVerCenter);

            headingBRSBookRow.createCell(7).setCellValue(prop.getString("aif.cell.sum.series"));
            headingBRSBookRow.getCell(7).setCellStyle(csHorVerCenter);

            headingBRSBookRow.createCell(8).setCellValue(prop.getString("aif.cell.tot.units"));
            headingBRSBookRow.getCell(8).setCellStyle(csHorVerCenter);

            headingBRSBookRow.createCell(9).setCellValue(prop.getString("aif.cell.manage.fee.charge"));
            headingBRSBookRow.getCell(9).setCellStyle(csHorVerCenter);

            headingBRSBookRow.createCell(10).setCellValue(prop.getString("aif.cell.total.comm"));
            headingBRSBookRow.getCell(10).setCellStyle(csHorVerCenter);


            iRowNo = sheetAIF.getLastRowNum() + 1;

            HSSFRow blankRow = sheetAIF.createRow(iRowNo);

            int iTotCell = 0;
            HSSFCell blankCell = null;
            //sheet.addMergedRegion(new CellRangeAddress(iRowNo, iRowNo, 0, 1));
            CellStyle csPlainLeft = csPlanLeftRight;
            csPlainLeft.setBorderRight(BorderStyle.NONE);

            CellStyle csPlainRight = csPlanLeftRight;
            csPlainRight.setBorderLeft(BorderStyle.NONE);

            sheetAIF.addMergedRegion(new CellRangeAddress(iRowNo, iRowNo, 0, 2));
            CellStyle csPlainNoBottom = cs;
            csPlainNoBottom.setBorderBottom(BorderStyle.NONE);
            while (iTotCell < 9 || iTotCell == 9) {

                blankCell = blankRow.createCell(iTotCell);
                blankCell.setCellValue("");

                if (iTotCell != 0 && iTotCell != 1 && iTotCell != 2)
                    blankCell.setCellStyle(csPlainNoBottom);//cs
                iTotCell++;
            }

            iRowNo = sheetAIF.getLastRowNum() + 1;

            DecimalFormat decimalFormat = new DecimalFormat("#.0000");
            String sManage = "";
            String sPerform = "";
            String sSum = "";
            String sNoOf = "";
            String sFeeCharged = "";
            String sCommission = "";
            for (ReportAIFQuarterFourManage bean : reportAIFQuarterFourManages) {
                sheetAIF.addMergedRegion(new CellRangeAddress(iRowNo, iRowNo, 0, 2));
                HSSFRow row = sheetAIF.createRow(iRowNo);

                row.createCell(0).setCellValue(bean.getAifClientMaster().getClientName());
                row.getCell(0).setCellStyle(csMergedCellBody);

                row.createCell(1).setCellValue("");

                row.createCell(2).setCellValue("");

                row.createCell(3).setCellValue(bean.getSeriesMaster().getSeriesCode());
                row.getCell(3).setCellStyle(csSecond);

                row.createCell(4).setCellValue(bean.getMnthYear());
                row.getCell(4).setCellStyle(csSecond);

                sManage = decimalFormat.format(bean.getManageFee());
                HSSFCell cell = row.createCell(5);
                cell.setCellType(CellType.NUMERIC);
                cell.setCellValue(Double.parseDouble(sManage));
                cell.setCellStyle(csDF);

                sPerform = decimalFormat.format(bean.getPerformFee());
                cell = row.createCell(6);
                cell.setCellType(CellType.NUMERIC);
                cell.setCellValue(Double.parseDouble(sPerform));
                cell.setCellStyle(csDF);

                sSum = decimalFormat.format(bean.getTotSeriesUnits());
                cell = row.createCell(7);
                cell.setCellType(CellType.NUMERIC);
                cell.setCellStyle(csDF);
                cell.setCellValue(Double.parseDouble(sSum));

                sNoOf = decimalFormat.format(bean.getTotIndUnits());
                cell = row.createCell(8);
                cell.setCellType(CellType.NUMERIC);
                cell.setCellStyle(csDF);
                cell.setCellValue(Double.parseDouble(sNoOf));


                sFeeCharged = decimalFormat.format(bean.getTotClientFee());
                cell = row.createCell(9);
                cell.setCellType(CellType.NUMERIC);
                cell.setCellStyle(csDF);
                cell.setCellValue(Double.parseDouble(sFeeCharged));

                sCommission = decimalFormat.format(bean.getCommDue());
                cell = row.createCell(10);
                cell.setCellValue(Double.parseDouble(sCommission));
                cell.setCellType(CellType.NUMERIC);
                cell.setCellStyle(csDF);

                sTotal += bean.getCommDue();
                iRowNo++;
            }
            String aifTotal = "";
            int iCurrRow = sheetAIF.getLastRowNum() + 1;
            HSSFRow row = sheetAIF.createRow(iCurrRow);

            sheetAIF.addMergedRegion(new CellRangeAddress(iCurrRow, iCurrRow, 0, 2));
            CellStyle csFour = cs;
            CellStyle csDFBoldFour = csDFBold;

            csFour.setBorderBottom(BorderStyle.THIN);
            csFour.setBorderTop(BorderStyle.THIN);
            csFour.setBorderLeft(BorderStyle.THIN);
            csFour.setBorderRight(BorderStyle.THIN);

            csDFBoldFour.setBorderBottom(BorderStyle.THIN);
            csDFBoldFour.setBorderTop(BorderStyle.THIN);
            csDFBoldFour.setBorderLeft(BorderStyle.THIN);
            csDFBoldFour.setBorderRight(BorderStyle.THIN);


            HSSFCell totCell = null;
            iTotCell = 0;
            while (iTotCell < 8 || iTotCell == 8) {
                totCell = row.createCell(iTotCell);
                totCell.setCellValue("");
                totCell.setCellStyle(csDFBoldFour);
                iTotCell++;
            }


            HSSFCell cell = row.createCell(9);
            cell.setCellValue("TOTAL");
            cell.setCellStyle(csFour);

            aifTotal = decimalFormat.format(sTotal);
            cell = row.createCell(10);
            cell.setCellValue(sTotal);
            cell.setCellStyle(csDFBoldFour);

            sheetAIF.setDefaultColumnWidth(18);
            sheetAIF.autoSizeColumn(1);
            sheetAIF.autoSizeColumn(2);
            cumulativePmsAifBean.setAifName("Total AIF");
            cumulativePmsAifBean.setAifTotal(aifTotal);
            cumulativePmsAifBeans.add(cumulativePmsAifBean);

            Double totalTrail = 0.0;
            Double totalUpfront = 0.0;
            Double totalClosing = 0.0;
            Double payable = 0.0;
            Double currentPayable = 0d;
            if (distributorMaster.getDistModelType().equals("Upfront")) {
                cumulativeDistributorReportBean = feeTrailUpfrontTransService.getDistributorCurrentReport(distributorMaster,
                    pmsCommission, reportGeneration.getStartDate(), reportGeneration.getToDate());

                closing = cumulativeDistributorReportBean.getCurrentUpfrontAmt()
                    .add(cumulativeDistributorReportBean.getCurrentTrailAmt());
                tempClosing = Float.valueOf(cumulativeDistributorReportBean.getCurrentUpfrontAmt().floatValue())
                    + Float.valueOf(cumulativeDistributorReportBean.getCurrentTrailAmt().floatValue());
                totalTrail = feeTrailUpfrontTransRepository.getTotalTrail(distributorMaster.getId(), reportGeneration.getToDate());
                totalUpfront = feeTrailUpfrontTransRepository.getTotalUpfront(distributorMaster.getId(), reportGeneration.getToDate());
                totalClosing = totalUpfront + totalTrail;
                payable = feeTrailUpfrontTransRepository.getTotalPayable(distributorMaster.getId(), reportGeneration.getToDate());
                Double paid = feeTrailUpfrontTransRepository.getTotalPaid(distributorMaster.getId(), reportGeneration.getToDate());

                if (totalClosing > 0) {
                    currentPayable = (totalClosing + payable) - paid;
                } else {
                    currentPayable = payable - paid;
                }
                BigDecimal totalPayable = cumulativeDistributorReportBean.getCurrentTotalPayableAmt()
                    .subtract(cumulativeDistributorReportBean.getCurrentPaidAmount());
            }
            summarySheetService.cumulativeFee(cumulativePmsAifBeans, sMonthStart, sMonthEnd, distributorMaster.getDistName().trim(), sheetSummary, sFinal,
                cumulativeDistributorReportBean, totalClosing, currentPayable, totalTrail, totalUpfront, distributorMaster, reportGeneration, workBook, cumulativeAIFSeriesBCAD);


        } catch (Exception e) {
            System.out.println(e);
        }


    }

    public void reportAifQuarterFourFinancialGeneration(String sMonthStart, String sMonthEnd, DistributorMaster distributorMaster,
                                                         HSSFSheet sheetAIF, String sFinal, List<CumulativePmsAifBean> cumulativePmsAifBeans, HSSFSheet sheetSummary, HSSFWorkbook workBook,
                                                         ReportGeneration reportGeneration, CumulativeAIFSeriesBCAD cumulativeAIFSeriesBCAD) {

        CumulativeDistributorReportBean cumulativeDistributorReportBean = null;
        BigDecimal closing, payableClosing;
        Float tempClosing, tempPayableClosing;
        Float pmsCommission, aifCommission;
        pmsCommission = prop.getFloat("fee.upfront.pms.commission");
        aifCommission = prop.getFloat("fee.upfront.aif.commission");
        Calendar beginCalendar = Calendar.getInstance();
        Calendar finishCalendar = Calendar.getInstance();
        List<String> mnthYr = new ArrayList<>();
        List<ReportAIFQuarterFourFinancial> reportAifQuarterFourFinancials = new ArrayList<ReportAIFQuarterFourFinancial>();

        String from = "";

        DateFormat dateFormat = new SimpleDateFormat("MMM yyyy");
        DateFormat dateMonth = new SimpleDateFormat("yyyy-MM");

        try {

            try {

                beginCalendar.setTime(dateFormat.parse(sMonthStart));
                finishCalendar.setTime(dateFormat.parse(sMonthEnd));
                int year = beginCalendar.get(Calendar.YEAR);
                int sMonth = 04;
                beginCalendar.setTime(dateMonth.parse(year + "-" + sMonth));

            } catch (ParseException e) {
                e.printStackTrace();
            }
            while (!beginCalendar.after(finishCalendar)) {
                reportQFourFinancialBean = new ArrayList<ReportQFourFinancialBean>();
                // add one month to date per loop
                from = dateFormat.format(beginCalendar.getTime());
                mnthYr.add(from);
                beginCalendar.add(Calendar.MONTH, 1);
            }
            sheetAIF.createFreezePane(0, 6);

            CumulativePmsAifBean cumulativePmsAifBean = new CumulativePmsAifBean();
            Float sTotal = 0.0f;
            reportAifQuarterFourFinancials = reportAIFQuarterFourFinancialRepository.managQFour(distributorMaster.getId(), mnthYr);

            HSSFFont fFont = workBook.createFont();

            fFont.setBold(true);
            fFont.setFontHeightInPoints((short) 11);
            fFont.setFontName("Calibri");

            HSSFFont defaultFont = workBook.createFont();
            defaultFont.setFontHeightInPoints((short) 11);
            defaultFont.setFontName("Calibri");


            HSSFFont fFontNoBold = workBook.createFont();
            fFontNoBold.setFontHeightInPoints((short) 11);
            fFontNoBold.setFontName("Calibri");

            CellStyle cs = workBook.createCellStyle();
            CellStyle csRight = workBook.createCellStyle();
            CellStyle csPlainNoBorder = workBook.createCellStyle();
            CellStyle csPlanLeftRight = workBook.createCellStyle();
            CellStyle csPlanLeftRightWrap = workBook.createCellStyle();
            CellStyle csMergedCellHeader = cs;

            CellStyle csSecond = workBook.createCellStyle();
            csSecond.setFont(fFontNoBold);
            csSecond.setBorderLeft(BorderStyle.THIN);
            csSecond.setBorderRight(BorderStyle.THIN);

            cs.setFont(fFont);

            CellStyle csMergedCellBody = workBook.createCellStyle();
            csMergedCellBody.setFont(fFontNoBold);
            csMergedCellBody.setBorderLeft(BorderStyle.THIN);
            csMergedCellBody.setBorderRight(BorderStyle.THIN);
            csMergedCellBody.setBorderTop(BorderStyle.NONE);
            csMergedCellBody.setBorderBottom(BorderStyle.NONE);
            csMergedCellBody.setWrapText(true);


            csPlainNoBorder.setFont(fFont);
            csPlainNoBorder.setBorderBottom(BorderStyle.NONE);
            csPlainNoBorder.setBorderTop(BorderStyle.NONE);
            csPlainNoBorder.setBorderLeft(BorderStyle.NONE);
            csPlainNoBorder.setBorderRight(BorderStyle.NONE);


            csPlanLeftRight.cloneStyleFrom(csPlainNoBorder);//HEre//cs
            csPlanLeftRightWrap.cloneStyleFrom(csPlanLeftRight);
            csPlanLeftRightWrap.setWrapText(true);


            csRight.setAlignment(HorizontalAlignment.RIGHT);

            // Format sheet
            CellStyle csHorVerCenter = workBook.createCellStyle();

            csRight.setAlignment(HorizontalAlignment.LEFT);
            // Format sheet
            csHorVerCenter.setAlignment(HorizontalAlignment.CENTER);
            csHorVerCenter.setVerticalAlignment(VerticalAlignment.CENTER);
            csHorVerCenter.setFont(fFont);
            csHorVerCenter.setWrapText(true);

            csMergedCellHeader.setAlignment(HorizontalAlignment.CENTER);
            csMergedCellHeader.setVerticalAlignment(VerticalAlignment.CENTER);

            csHorVerCenter.setBorderTop(BorderStyle.THIN);
            csHorVerCenter.setBorderBottom(BorderStyle.THIN);
            csHorVerCenter.setBorderLeft(BorderStyle.THIN);
            csHorVerCenter.setBorderRight(BorderStyle.THIN);

            csPlanLeftRight.setBorderLeft(BorderStyle.THIN);
            csPlanLeftRight.setBorderRight(BorderStyle.THIN);
            csPlanLeftRight.setFont(fFontNoBold);

            CellStyle csDF = StyleUtil.getStyleDataFormat(workBook);

            CellStyle csDFBold = StyleUtil.getStyleBoldDataFormat(workBook);

            CellStyle csPerc = StyleUtil.getStylePercDataFormat(workBook);

            csDF.setBorderLeft(BorderStyle.THIN);
            csDF.setBorderRight(BorderStyle.THIN);
            csDF.setFont(defaultFont);

            HSSFRow rowUserName = sheetAIF.createRow(sheetAIF.getLastRowNum() + 1);
            rowUserName.createCell(0).setCellValue(distributorMaster.getDistName().trim().toUpperCase());
            rowUserName.getCell(0).setCellStyle(csPlainNoBorder);

            HSSFRow durationFrom = sheetAIF.createRow(sheetAIF.getLastRowNum() + 1);
            durationFrom.createCell(0).setCellValue("FEES PAYABALE FOR " + sMonthStart + "-" + sMonthEnd);
            durationFrom.getCell(0).setCellStyle(csPlainNoBorder);

            // Format sheet
            sheetAIF.addMergedRegion(new CellRangeAddress(1, 1, 0, 3));
            sheetAIF.addMergedRegion(new CellRangeAddress(2, 2, 0, 3));

            /*
             * HSSFRow rowUserName = sheet.createRow(sheet.getLastRowNum() + 1);
             * rowUserName.createCell(1).setCellValue("As on " + sFinal);
             * rowUserName.getCell(1).setCellStyle(cs); sheet.autoSizeColumn(1);
             */

            int iRowNo = sheetAIF.getLastRowNum() + 3;
            HSSFRow headingBRSBookRow = sheetAIF.createRow(iRowNo);
            headingBRSBookRow.setHeightInPoints(30);

            sheetAIF.addMergedRegion(new CellRangeAddress(5, 5, 0, 2));

            headingBRSBookRow.createCell(0).setCellValue(prop.getString("aif.cell.client_name"));
            headingBRSBookRow.getCell(0).setCellStyle(csMergedCellHeader);

            headingBRSBookRow.createCell(1).setCellValue("");
            headingBRSBookRow.createCell(2).setCellValue("");
            headingBRSBookRow.getCell(1).setCellStyle(csMergedCellHeader);
            headingBRSBookRow.getCell(2).setCellStyle(csMergedCellHeader);

            headingBRSBookRow.createCell(3).setCellValue(prop.getString("aif.cell.series.name"));
            headingBRSBookRow.getCell(3).setCellStyle(csHorVerCenter);

            headingBRSBookRow.createCell(4).setCellValue("Month");
            headingBRSBookRow.getCell(4).setCellStyle(csHorVerCenter);

            headingBRSBookRow.createCell(5).setCellValue(prop.getString("aif.cell.manage.fee"));
            headingBRSBookRow.getCell(5).setCellStyle(csHorVerCenter);

            headingBRSBookRow.createCell(6).setCellValue(prop.getString("aif.cell.perform.fee"));
            headingBRSBookRow.getCell(6).setCellStyle(csHorVerCenter);

            headingBRSBookRow.createCell(7).setCellValue(prop.getString("aif.cell.sum.series"));
            headingBRSBookRow.getCell(7).setCellStyle(csHorVerCenter);

            headingBRSBookRow.createCell(8).setCellValue(prop.getString("aif.cell.tot.units"));
            headingBRSBookRow.getCell(8).setCellStyle(csHorVerCenter);

            headingBRSBookRow.createCell(9).setCellValue(prop.getString("aif.cell.manage.fee.charge"));
            headingBRSBookRow.getCell(9).setCellStyle(csHorVerCenter);

            headingBRSBookRow.createCell(10).setCellValue(prop.getString("aif.cell.total.comm"));
            headingBRSBookRow.getCell(10).setCellStyle(csHorVerCenter);


            iRowNo = sheetAIF.getLastRowNum() + 1;

            HSSFRow blankRow = sheetAIF.createRow(iRowNo);

            int iTotCell = 0;
            HSSFCell blankCell = null;

            CellStyle csPlainLeft = csPlanLeftRight;
            csPlainLeft.setBorderRight(BorderStyle.NONE);

            CellStyle csPlainRight = csPlanLeftRight;
            csPlainRight.setBorderLeft(BorderStyle.NONE);

            sheetAIF.addMergedRegion(new CellRangeAddress(iRowNo, iRowNo, 0, 2));
            CellStyle csPlainNoBottom = cs;
            csPlainNoBottom.setBorderBottom(BorderStyle.NONE);
            while (iTotCell < 9 || iTotCell == 9) {

                blankCell = blankRow.createCell(iTotCell);
                blankCell.setCellValue("");

                if (iTotCell != 0 && iTotCell != 1 && iTotCell != 2)
                    blankCell.setCellStyle(csPlainNoBottom);//cs
                iTotCell++;
            }

            iRowNo = sheetAIF.getLastRowNum() + 1;

            DecimalFormat decimalFormat = new DecimalFormat("#.0000");
            String sManage = "";
            String sPerform = "";
            String sSum = "";
            String sNoOf = "";
            String sFeeCharged = "";
            String sCommission = "";
            for (ReportAIFQuarterFourFinancial bean : reportAifQuarterFourFinancials) {
                sheetAIF.addMergedRegion(new CellRangeAddress(iRowNo, iRowNo, 0, 2));
                HSSFRow row = sheetAIF.createRow(iRowNo);

                row.createCell(0).setCellValue(bean.getAifClientMaster().getClientName());
                row.getCell(0).setCellStyle(csMergedCellBody);

                row.createCell(1).setCellValue("");

                row.createCell(2).setCellValue("");

                row.createCell(3).setCellValue(bean.getSeriesMaster().getSeriesCode());
                row.getCell(3).setCellStyle(csSecond);

                row.createCell(4).setCellValue(bean.getMnthYear());
                row.getCell(4).setCellStyle(csSecond);


                sManage = decimalFormat.format(bean.getManageFee());
                HSSFCell cell = row.createCell(5);
                cell.setCellType(CellType.NUMERIC);
                cell.setCellValue(Double.parseDouble(sManage));
                cell.setCellStyle(csDF);


                sPerform = decimalFormat.format(bean.getPerformFee());
                cell = row.createCell(6);
                cell.setCellType(CellType.NUMERIC);
                cell.setCellValue(Double.parseDouble(sPerform));
                cell.setCellStyle(csDF);


                sSum = decimalFormat.format(bean.getTotSeriesUnits());
                cell = row.createCell(7);
                cell.setCellType(CellType.NUMERIC);
                cell.setCellStyle(csDF);
                cell.setCellValue(Double.parseDouble(sSum));


                sNoOf = decimalFormat.format(bean.getTotIndUnits());
                cell = row.createCell(8);
                cell.setCellType(CellType.NUMERIC);
                cell.setCellStyle(csDF);
                cell.setCellValue(Double.parseDouble(sNoOf));


                sFeeCharged = decimalFormat.format(bean.getTotClntFee());
                cell = row.createCell(9);
                cell.setCellType(CellType.NUMERIC);
                cell.setCellStyle(csDF);
                cell.setCellValue(Double.parseDouble(sFeeCharged));

                sCommission = decimalFormat.format(bean.getCommDue());
                cell = row.createCell(10);
                cell.setCellValue(Double.parseDouble(sCommission));
                cell.setCellType(CellType.NUMERIC);
                cell.setCellStyle(csDF);


                sTotal += bean.getCommDue();
                iRowNo++;
            }
            String aifTotal = "";
            int iCurrRow = sheetAIF.getLastRowNum() + 1;
            HSSFRow row = sheetAIF.createRow(iCurrRow);

            sheetAIF.addMergedRegion(new CellRangeAddress(iCurrRow, iCurrRow, 0, 2));
            CellStyle csFour = cs;
            CellStyle csDFBoldFour = csDFBold;

            csFour.setBorderBottom(BorderStyle.THIN);
            csFour.setBorderTop(BorderStyle.THIN);
            csFour.setBorderLeft(BorderStyle.THIN);
            csFour.setBorderRight(BorderStyle.THIN);

            csDFBoldFour.setBorderBottom(BorderStyle.THIN);
            csDFBoldFour.setBorderTop(BorderStyle.THIN);
            csDFBoldFour.setBorderLeft(BorderStyle.THIN);
            csDFBoldFour.setBorderRight(BorderStyle.THIN);


            HSSFCell totCell = null;
            iTotCell = 0;
            while (iTotCell < 8 || iTotCell == 8) {
                totCell = row.createCell(iTotCell);
                totCell.setCellValue("");
                totCell.setCellStyle(csDFBoldFour);
                iTotCell++;
            }


            HSSFCell cell = row.createCell(9);
            cell.setCellValue("TOTAL");
            cell.setCellStyle(csFour);

            // sheet.autoSizeColumn(6);
            aifTotal = decimalFormat.format(sTotal);
            cell = row.createCell(10);
            cell.setCellValue(sTotal);
            cell.setCellStyle(csDFBoldFour);

            sheetAIF.setDefaultColumnWidth(18);
            sheetAIF.autoSizeColumn(1);
            sheetAIF.autoSizeColumn(2);
            cumulativePmsAifBean.setAifName("Total AIF");
            cumulativePmsAifBean.setAifTotal(aifTotal);
            cumulativePmsAifBeans.add(cumulativePmsAifBean);

            Double totalTrail = 0.0;
            Double totalUpfront = 0.0;
            Double totalClosing = 0.0;
            Double payable = 0.0;
            Double currentPayable = 0d;
            if (distributorMaster.getDistModelType().equals("Upfront")) {
                cumulativeDistributorReportBean = feeTrailUpfrontTransService.getDistributorCurrentReport(distributorMaster,
                    pmsCommission, reportGeneration.getStartDate(), reportGeneration.getToDate());

                closing = cumulativeDistributorReportBean.getCurrentUpfrontAmt()
                    .add(cumulativeDistributorReportBean.getCurrentTrailAmt());
                tempClosing = Float.valueOf(cumulativeDistributorReportBean.getCurrentUpfrontAmt().floatValue())
                    + Float.valueOf(cumulativeDistributorReportBean.getCurrentTrailAmt().floatValue());
                totalTrail = feeTrailUpfrontTransRepository.getTotalTrail(distributorMaster.getId(), reportGeneration.getToDate());
                totalUpfront = feeTrailUpfrontTransRepository.getTotalUpfront(distributorMaster.getId(), reportGeneration.getToDate());
                totalClosing = totalUpfront + totalTrail;
                payable = feeTrailUpfrontTransRepository.getTotalPayable(distributorMaster.getId(), reportGeneration.getToDate());
                Double paid = feeTrailUpfrontTransRepository.getTotalPaid(distributorMaster.getId(), reportGeneration.getToDate());

                if (totalClosing > 0) {
                    currentPayable = (totalClosing + payable) - paid;
                } else {
                    currentPayable = payable - paid;
                }
                BigDecimal totalPayable = cumulativeDistributorReportBean.getCurrentTotalPayableAmt()
                    .subtract(cumulativeDistributorReportBean.getCurrentPaidAmount());
            }
            summarySheetService.cumulativeFee(cumulativePmsAifBeans, sMonthStart, sMonthEnd, distributorMaster.getDistName().trim(), sheetSummary, sFinal,
                cumulativeDistributorReportBean, totalClosing, currentPayable, totalTrail, totalUpfront, distributorMaster, reportGeneration, workBook, cumulativeAIFSeriesBCAD);


        } catch (Exception e) {
            System.out.println(e);
        }

    }

    public void reportAifQuarterFourGeneration(String sMonthStart, String sMonthEnd, DistributorMaster distributorMaster, HSSFSheet sheetAIF, String sFinal, List<CumulativePmsAifBean> cumulativePmsAifBeans, HSSFSheet sheetSummary, ReportGeneration reportGeneration, HSSFWorkbook workBook, CumulativeAIFSeriesBCAD cumulativeAIFSeriesBCAD) {

        CumulativeDistributorReportBean cumulativeDistributorReportBean = null;
        BigDecimal closing, payableClosing;
        Float tempClosing, tempPayableClosing;
        Float pmsCommission, aifCommission;
        pmsCommission = prop.getFloat("fee.upfront.pms.commission");
        aifCommission = prop.getFloat("fee.upfront.aif.commission");
        Calendar beginCalendar = Calendar.getInstance();
        Calendar finishCalendar = Calendar.getInstance();
        List<String> mnthYr = new ArrayList<>();
        List<ReportAIFQuarterFour> reportAIFQuarterFours = new ArrayList<ReportAIFQuarterFour>();

        String from = "";

        DateFormat dateFormat = new SimpleDateFormat("MMM yyyy");
        DateFormat dateMonth = new SimpleDateFormat("yyyy-MM");

        try {

            try {

                beginCalendar.setTime(dateFormat.parse(sMonthStart));
                finishCalendar.setTime(dateFormat.parse(sMonthEnd));
                System.out.println(beginCalendar.getTime());
                System.out.println(finishCalendar.getTime());

                int year = beginCalendar.get(Calendar.YEAR);
                int sMonth = 04;
                System.out.println(dateMonth.parse(year - 1 + "-" + sMonth));
                beginCalendar.setTime(dateMonth.parse(year - 1 + "-" + sMonth));

            } catch (ParseException e) {
                e.printStackTrace();
            }


            while (!beginCalendar.after(finishCalendar)) {
                reportQFourBean = new ArrayList<ReportQFourBean>();
                // add one month to date per loop
                from = dateFormat.format(beginCalendar.getTime());
                mnthYr.add(from);
                beginCalendar.add(Calendar.MONTH, 1);
            }
            sheetAIF.createFreezePane(0, 6);

            CumulativePmsAifBean cumulativePmsAifBean = new CumulativePmsAifBean();
            Float sTotal = 0.0f;
            reportAIFQuarterFours = reportAIFQuarterFourRepository.managQFour(distributorMaster.getId(), mnthYr);

            HSSFFont fFont = workBook.createFont();

            fFont.setBold(true);
            fFont.setFontHeightInPoints((short) 11);
            fFont.setFontName("Calibri");

            HSSFFont defaultFont = workBook.createFont();
            defaultFont.setFontHeightInPoints((short) 11);
            defaultFont.setFontName("Calibri");


            HSSFFont fFontNoBold = workBook.createFont();
            fFontNoBold.setFontHeightInPoints((short) 11);
            fFontNoBold.setFontName("Calibri");

            CellStyle cs = workBook.createCellStyle();
            CellStyle csRight = workBook.createCellStyle();
            CellStyle csPlainNoBorder = workBook.createCellStyle();
            CellStyle csPlanLeftRight = workBook.createCellStyle();
            CellStyle csPlanLeftRightWrap = workBook.createCellStyle();
            CellStyle csMergedCellHeader = cs;

            CellStyle csSecond = workBook.createCellStyle();
            csSecond.setFont(fFontNoBold);
            csSecond.setBorderLeft(BorderStyle.THIN);
            csSecond.setBorderRight(BorderStyle.THIN);

            cs.setFont(fFont);

            CellStyle csMergedCellBody = workBook.createCellStyle();
            csMergedCellBody.setFont(fFontNoBold);
            csMergedCellBody.setBorderLeft(BorderStyle.THIN);
            csMergedCellBody.setBorderRight(BorderStyle.THIN);
            csMergedCellBody.setBorderTop(BorderStyle.NONE);
            csMergedCellBody.setBorderBottom(BorderStyle.NONE);
            csMergedCellBody.setWrapText(true);


            csPlainNoBorder.setFont(fFont);
            csPlainNoBorder.setBorderBottom(BorderStyle.NONE);
            csPlainNoBorder.setBorderTop(BorderStyle.NONE);
            csPlainNoBorder.setBorderLeft(BorderStyle.NONE);
            csPlainNoBorder.setBorderRight(BorderStyle.NONE);


            csPlanLeftRight.cloneStyleFrom(csPlainNoBorder);//HEre//cs
            csPlanLeftRightWrap.cloneStyleFrom(csPlanLeftRight);
            csPlanLeftRightWrap.setWrapText(true);


            csRight.setAlignment(HorizontalAlignment.RIGHT);

            // Format sheet
            CellStyle csHorVerCenter = workBook.createCellStyle();

            csRight.setAlignment(HorizontalAlignment.LEFT);
            // Format sheet
            csHorVerCenter.setAlignment(HorizontalAlignment.CENTER);
            csHorVerCenter.setVerticalAlignment(VerticalAlignment.CENTER);
            csHorVerCenter.setFont(fFont);
            csHorVerCenter.setWrapText(true);

            csMergedCellHeader.setAlignment(HorizontalAlignment.CENTER);
            csMergedCellHeader.setVerticalAlignment(VerticalAlignment.CENTER);

            csHorVerCenter.setBorderTop(BorderStyle.THIN);
            csHorVerCenter.setBorderBottom(BorderStyle.THIN);
            csHorVerCenter.setBorderLeft(BorderStyle.THIN);
            csHorVerCenter.setBorderRight(BorderStyle.THIN);

            csPlanLeftRight.setBorderLeft(BorderStyle.THIN);
            csPlanLeftRight.setBorderRight(BorderStyle.THIN);
            csPlanLeftRight.setFont(fFontNoBold);

            CellStyle csDF = StyleUtil.getStyleDataFormat(workBook);

            CellStyle csDFBold = StyleUtil.getStyleBoldDataFormat(workBook);

            CellStyle csPerc = StyleUtil.getStylePercDataFormat(workBook);

            csDF.setBorderLeft(BorderStyle.THIN);
            csDF.setBorderRight(BorderStyle.THIN);
            csDF.setFont(defaultFont);

            HSSFRow rowUserName = sheetAIF.createRow(sheetAIF.getLastRowNum() + 1);
            rowUserName.createCell(0).setCellValue(distributorMaster.getDistName().trim().toUpperCase());
            rowUserName.getCell(0).setCellStyle(csPlainNoBorder);

            HSSFRow durationFrom = sheetAIF.createRow(sheetAIF.getLastRowNum() + 1);
            durationFrom.createCell(0).setCellValue("FEES PAYABALE FOR " + sMonthStart + "-" + sMonthEnd);
            durationFrom.getCell(0).setCellStyle(csPlainNoBorder);

            // Format sheet
            sheetAIF.addMergedRegion(new CellRangeAddress(1, 1, 0, 3));
            sheetAIF.addMergedRegion(new CellRangeAddress(2, 2, 0, 3));

            int iRowNo = sheetAIF.getLastRowNum() + 3;
            HSSFRow headingBRSBookRow = sheetAIF.createRow(iRowNo);
            headingBRSBookRow.setHeightInPoints(30);

            sheetAIF.addMergedRegion(new CellRangeAddress(5, 5, 0, 2));

            headingBRSBookRow.createCell(0).setCellValue(prop.getString("aif.cell.client_name"));
            headingBRSBookRow.getCell(0).setCellStyle(csMergedCellHeader);

            headingBRSBookRow.createCell(1).setCellValue("");
            headingBRSBookRow.createCell(2).setCellValue("");
            headingBRSBookRow.getCell(1).setCellStyle(csMergedCellHeader);
            headingBRSBookRow.getCell(2).setCellStyle(csMergedCellHeader);


            headingBRSBookRow.createCell(3).setCellValue(prop.getString("aif.cell.series.name"));
            headingBRSBookRow.getCell(3).setCellStyle(csHorVerCenter);

            headingBRSBookRow.createCell(4).setCellValue("Month");
            headingBRSBookRow.getCell(4).setCellStyle(csHorVerCenter);

            headingBRSBookRow.createCell(5).setCellValue(prop.getString("aif.cell.manage.fee"));
            headingBRSBookRow.getCell(5).setCellStyle(csHorVerCenter);

            headingBRSBookRow.createCell(6).setCellValue(prop.getString("aif.cell.perform.fee"));
            headingBRSBookRow.getCell(6).setCellStyle(csHorVerCenter);

            headingBRSBookRow.createCell(7).setCellValue(prop.getString("aif.cell.sum.series"));
            headingBRSBookRow.getCell(7).setCellStyle(csHorVerCenter);

            headingBRSBookRow.createCell(8).setCellValue(prop.getString("aif.cell.tot.units"));
            headingBRSBookRow.getCell(8).setCellStyle(csHorVerCenter);

            headingBRSBookRow.createCell(9).setCellValue(prop.getString("aif.cell.manage.fee.charge"));
            headingBRSBookRow.getCell(9).setCellStyle(csHorVerCenter);

            headingBRSBookRow.createCell(10).setCellValue(prop.getString("aif.cell.total.comm"));
            headingBRSBookRow.getCell(10).setCellStyle(csHorVerCenter);


            iRowNo = sheetAIF.getLastRowNum() + 1;

            HSSFRow blankRow = sheetAIF.createRow(iRowNo);

            int iTotCell = 0;
            HSSFCell blankCell = null;
            //sheet.addMergedRegion(new CellRangeAddress(iRowNo, iRowNo, 0, 1));
            CellStyle csPlainLeft = csPlanLeftRight;
            csPlainLeft.setBorderRight(BorderStyle.NONE);

            CellStyle csPlainRight = csPlanLeftRight;
            csPlainRight.setBorderLeft(BorderStyle.NONE);

            sheetAIF.addMergedRegion(new CellRangeAddress(iRowNo, iRowNo, 0, 2));
            CellStyle csPlainNoBottom = cs;
            csPlainNoBottom.setBorderBottom(BorderStyle.NONE);
            while (iTotCell < 9 || iTotCell == 9) {

                blankCell = blankRow.createCell(iTotCell);
                blankCell.setCellValue("");

                if (iTotCell != 0 && iTotCell != 1 && iTotCell != 2)
                    blankCell.setCellStyle(csPlainNoBottom);//cs
                iTotCell++;
            }

            iRowNo = sheetAIF.getLastRowNum() + 1;

            DecimalFormat decimalFormat = new DecimalFormat("#.0000");
            String sManage = "";
            String sPerform = "";
            String sSum = "";
            String sNoOf = "";
            String sFeeCharged = "";
            String sCommission = "";
            for (ReportAIFQuarterFour bean : reportAIFQuarterFours) {
                sheetAIF.addMergedRegion(new CellRangeAddress(iRowNo, iRowNo, 0, 2));
                HSSFRow row = sheetAIF.createRow(iRowNo);

                row.createCell(0).setCellValue(bean.getAifClientMaster().getClientName());
                row.getCell(0).setCellStyle(csMergedCellBody);

                row.createCell(1).setCellValue("");

                row.createCell(2).setCellValue("");

                row.createCell(3).setCellValue(bean.getSeriesMaster().getSeriesCode());
                row.getCell(3).setCellStyle(csSecond);

                row.createCell(4).setCellValue(bean.getMnthYear());
                row.getCell(4).setCellStyle(csSecond);


                sManage = decimalFormat.format(bean.getManageFee());
                HSSFCell cell = row.createCell(5);
                cell.setCellType(CellType.NUMERIC);
                cell.setCellValue(Double.parseDouble(sManage));
                cell.setCellStyle(csDF);

                sPerform = decimalFormat.format(bean.getPerformFee());
                cell = row.createCell(6);
                cell.setCellType(CellType.NUMERIC);
                cell.setCellValue(Double.parseDouble(sPerform));
                cell.setCellStyle(csDF);

                sSum = decimalFormat.format(bean.getTotSeriesUnits());
                cell = row.createCell(7);
                cell.setCellType(CellType.NUMERIC);
                cell.setCellStyle(csDF);
                cell.setCellValue(Double.parseDouble(sSum));

                sNoOf = decimalFormat.format(bean.getTotIndUnits());
                cell = row.createCell(8);
                cell.setCellType(CellType.NUMERIC);
                cell.setCellStyle(csDF);
                cell.setCellValue(Double.parseDouble(sNoOf));

                sFeeCharged = decimalFormat.format(bean.getTotClientFee());
                cell = row.createCell(9);
                cell.setCellType(CellType.NUMERIC);
                cell.setCellStyle(csDF);
                cell.setCellValue(Double.parseDouble(sFeeCharged));

                sCommission = decimalFormat.format(bean.getCommDue());
                cell = row.createCell(10);
                cell.setCellValue(Double.parseDouble(sCommission));
                cell.setCellType(CellType.NUMERIC);
                cell.setCellStyle(csDF);


                sTotal += bean.getCommDue();
                iRowNo++;
            }
            String aifTotal = "";
            int iCurrRow = sheetAIF.getLastRowNum() + 1;
            HSSFRow row = sheetAIF.createRow(iCurrRow);

            sheetAIF.addMergedRegion(new CellRangeAddress(iCurrRow, iCurrRow, 0, 2));
            CellStyle csFour = cs;
            CellStyle csDFBoldFour = csDFBold;

            csFour.setBorderBottom(BorderStyle.THIN);
            csFour.setBorderTop(BorderStyle.THIN);
            csFour.setBorderLeft(BorderStyle.THIN);
            csFour.setBorderRight(BorderStyle.THIN);

            csDFBoldFour.setBorderBottom(BorderStyle.THIN);
            csDFBoldFour.setBorderTop(BorderStyle.THIN);
            csDFBoldFour.setBorderLeft(BorderStyle.THIN);
            csDFBoldFour.setBorderRight(BorderStyle.THIN);


            HSSFCell totCell = null;
            iTotCell = 0;
            while (iTotCell < 8 || iTotCell == 8) {
                totCell = row.createCell(iTotCell);
                totCell.setCellValue("");
                totCell.setCellStyle(csDFBoldFour);
                iTotCell++;
            }


            HSSFCell cell = row.createCell(9);
            cell.setCellValue("TOTAL");
            cell.setCellStyle(csFour);

            // sheet.autoSizeColumn(6);
            aifTotal = decimalFormat.format(sTotal);
            cell = row.createCell(10);
            cell.setCellValue(sTotal);
            cell.setCellStyle(csDFBoldFour);

            sheetAIF.setDefaultColumnWidth(18);
            sheetAIF.autoSizeColumn(1);
            sheetAIF.autoSizeColumn(2);
            cumulativePmsAifBean.setAifName("Total AIF");
            cumulativePmsAifBean.setAifTotal(aifTotal);
            cumulativePmsAifBeans.add(cumulativePmsAifBean);

            Double totalTrail = 0.0;
            Double totalUpfront = 0.0;
            Double totalClosing = 0.0;
            Double payable = 0.0;
            Double currentPayable = 0d;
            if (distributorMaster.getDistModelType().equals("Upfront")) {
                cumulativeDistributorReportBean = feeTrailUpfrontTransService.getDistributorCurrentReport(distributorMaster,
                    pmsCommission, reportGeneration.getStartDate(), reportGeneration.getToDate());

                closing = cumulativeDistributorReportBean.getCurrentUpfrontAmt()
                    .add(cumulativeDistributorReportBean.getCurrentTrailAmt());
                tempClosing = Float.valueOf(cumulativeDistributorReportBean.getCurrentUpfrontAmt().floatValue())
                    + Float.valueOf(cumulativeDistributorReportBean.getCurrentTrailAmt().floatValue());
                totalTrail = feeTrailUpfrontTransRepository.getTotalTrail(distributorMaster.getId(), reportGeneration.getStartDate());
                totalUpfront = feeTrailUpfrontTransRepository.getTotalUpfront(distributorMaster.getId(), reportGeneration.getStartDate());
                totalClosing = totalUpfront + totalTrail;
                payable = feeTrailUpfrontTransRepository.getTotalPayable(distributorMaster.getId(), reportGeneration.getStartDate());
                Double paid = feeTrailUpfrontTransRepository.getTotalPaid(distributorMaster.getId(), reportGeneration.getStartDate());

                if (totalClosing > 0) {
                    currentPayable = (totalClosing + payable) - paid;
                } else {
                    currentPayable = payable - paid;
                }
                BigDecimal totalPayable = cumulativeDistributorReportBean.getCurrentTotalPayableAmt()
                    .subtract(cumulativeDistributorReportBean.getCurrentPaidAmount());
            }
            summarySheetService.cumulativeFee(cumulativePmsAifBeans, sMonthStart, sMonthEnd, distributorMaster.getDistName().trim(), sheetSummary, sFinal,
                cumulativeDistributorReportBean, totalClosing, currentPayable, totalTrail, totalUpfront, distributorMaster, reportGeneration, workBook, cumulativeAIFSeriesBCAD);


        } catch (Exception e) {
            System.out.println(e);
        }


    }

    private void reportAifGeneration(List<ReportAifDistBean> reportAifDistBean, String sMonthStart, String sMonthEnd, DistributorMaster distributorMaster,
                                     HSSFSheet sheetAIF, String sFinal, List<CumulativePmsAifBean> cumulativePmsAifBeans, HSSFSheet sheetSummary,
                                     ReportGeneration reportGeneration, HSSFWorkbook workBook, CommissionDefinition commissionDefinition, CumulativeAIFSeriesBCAD cumulativeAIFSeriesBCAD) {

        CumulativeDistributorReportBean cumulativeDistributorReportBean = null;
        BigDecimal closing, payableClosing;
        Float tempClosing, tempPayableClosing;
        Float pmsCommission, aifCommission;
        pmsCommission = prop.getFloat("fee.upfront.pms.commission");
        aifCommission = prop.getFloat("fee.upfront.aif.commission");
        try {

            sheetAIF.createFreezePane(0, 6);

            ReportGeneration reportFeeAIF = new ReportGeneration();
            reportFeeAIF.setStartDate(reportGeneration.getStartDate());
            reportFeeAIF.setToDate(reportGeneration.getToDate());
            reportFeeAIF.setDetailsUpdatedFlag(0);
            reportFeeAIF.setDistributorMaster1(distributorMaster);
            reportFeeAIF.setReportType("AIF");
            reportFeeAIF = reportGenerationRepository.save(reportFeeAIF);
            ReportAIFCalc reportAIFCalc;
            ReportAIFQuarterFour reportAIFQuarterFour;
            CumulativePmsAifBean cumulativePmsAifBean = new CumulativePmsAifBean();
            Float sTotal = 0.0f;
            HSSFFont fFont = workBook.createFont();

            fFont.setBold(true);
            fFont.setFontHeightInPoints((short) 11);
            fFont.setFontName("Calibri");

            HSSFFont defaultFont = workBook.createFont();
            defaultFont.setFontHeightInPoints((short) 11);
            defaultFont.setFontName("Calibri");


            HSSFFont fFontNoBold = workBook.createFont();
            fFontNoBold.setFontHeightInPoints((short) 11);
            fFontNoBold.setFontName("Calibri");


            CellStyle cs = workBook.createCellStyle();
            CellStyle csRight = workBook.createCellStyle();
            CellStyle csPlainNoBorder = workBook.createCellStyle();
            CellStyle csPlanLeftRight = workBook.createCellStyle();
            CellStyle csPlanLeftRightWrap = workBook.createCellStyle();
            CellStyle csMergedCellHeader = cs;

            CellStyle csSecond = workBook.createCellStyle();
            csSecond.setFont(fFontNoBold);
            csSecond.setBorderLeft(BorderStyle.THIN);
            csSecond.setBorderRight(BorderStyle.THIN);

            cs.setFont(fFont);

            CellStyle csMergedCellBody = workBook.createCellStyle();
            csMergedCellBody.setFont(fFontNoBold);
            csMergedCellBody.setBorderLeft(BorderStyle.THIN);
            csMergedCellBody.setBorderRight(BorderStyle.THIN);
            csMergedCellBody.setBorderTop(BorderStyle.NONE);
            csMergedCellBody.setBorderBottom(BorderStyle.NONE);
            csMergedCellBody.setWrapText(true);


            csPlainNoBorder.setFont(fFont);
            csPlainNoBorder.setBorderBottom(BorderStyle.NONE);
            csPlainNoBorder.setBorderTop(BorderStyle.NONE);
            csPlainNoBorder.setBorderLeft(BorderStyle.NONE);
            csPlainNoBorder.setBorderRight(BorderStyle.NONE);

			/*csPlanLeftRight = csPlainNoBorder;//HEre//cs

			csPlanLeftRightWrap = csPlanLeftRight;*/

            csPlanLeftRight.cloneStyleFrom(csPlainNoBorder);//HEre//cs
            csPlanLeftRightWrap.cloneStyleFrom(csPlanLeftRight);
            csPlanLeftRightWrap.setWrapText(true);


            csRight.setAlignment(HorizontalAlignment.RIGHT);

            // Format sheet
            CellStyle csHorVerCenter = workBook.createCellStyle();

            csRight.setAlignment(HorizontalAlignment.LEFT);
            // Format sheet
            csHorVerCenter.setAlignment(HorizontalAlignment.CENTER);
            csHorVerCenter.setVerticalAlignment(VerticalAlignment.CENTER);
            csHorVerCenter.setFont(fFont);
            csHorVerCenter.setWrapText(true);

            csMergedCellHeader.setAlignment(HorizontalAlignment.CENTER);
            csMergedCellHeader.setVerticalAlignment(VerticalAlignment.CENTER);

            csHorVerCenter.setBorderTop(BorderStyle.THIN);
            csHorVerCenter.setBorderBottom(BorderStyle.THIN);
            csHorVerCenter.setBorderLeft(BorderStyle.THIN);
            csHorVerCenter.setBorderRight(BorderStyle.THIN);

            csPlanLeftRight.setBorderLeft(BorderStyle.THIN);
            csPlanLeftRight.setBorderRight(BorderStyle.THIN);
            csPlanLeftRight.setFont(fFontNoBold);


            CellStyle csDF = StyleUtil.getStyleDataFormat(workBook);

            CellStyle csDFBold = StyleUtil.getStyleBoldDataFormat(workBook);

            CellStyle csPerc = StyleUtil.getStylePercDataFormat(workBook);

            csDF.setBorderLeft(BorderStyle.THIN);
            csDF.setBorderRight(BorderStyle.THIN);
            csDF.setFont(defaultFont);

            HSSFRow rowUserName = sheetAIF.createRow(sheetAIF.getLastRowNum() + 1);
            rowUserName.createCell(0).setCellValue(distributorMaster.getDistName().trim().toUpperCase());
            rowUserName.getCell(0).setCellStyle(csPlainNoBorder);//cs

            HSSFRow durationFrom = sheetAIF.createRow(sheetAIF.getLastRowNum() + 1);
            durationFrom.createCell(0).setCellValue("FEES PAYABALE FOR " + sMonthStart + "-" + sMonthEnd);
            durationFrom.getCell(0).setCellStyle(csPlainNoBorder);//cs

            // Format sheet
            sheetAIF.addMergedRegion(new CellRangeAddress(1, 1, 0, 3));
            sheetAIF.addMergedRegion(new CellRangeAddress(2, 2, 0, 3));

            int iRowNo = sheetAIF.getLastRowNum() + 3;
            HSSFRow headingBRSBookRow = sheetAIF.createRow(iRowNo);
            headingBRSBookRow.setHeightInPoints(30);

            sheetAIF.addMergedRegion(new CellRangeAddress(5, 5, 0, 2));

            headingBRSBookRow.createCell(0).setCellValue(prop.getString("aif.cell.client_name"));
            headingBRSBookRow.getCell(0).setCellStyle(csMergedCellHeader);//csHorVerCenter

            headingBRSBookRow.createCell(1).setCellValue("");
            headingBRSBookRow.createCell(2).setCellValue("");
            headingBRSBookRow.getCell(1).setCellStyle(csMergedCellHeader);
            headingBRSBookRow.getCell(2).setCellStyle(csMergedCellHeader);

            // sheet.autoSizeColumn(1);
            headingBRSBookRow.createCell(3).setCellValue(prop.getString("aif.cell.series.name"));
            headingBRSBookRow.getCell(3).setCellStyle(csHorVerCenter);
            // sheet.autoSizeColumn(2);
            headingBRSBookRow.createCell(4).setCellValue(prop.getString("aif.cell.manage.fee"));
            headingBRSBookRow.getCell(4).setCellStyle(csHorVerCenter);
            // sheet.autoSizeColumn(3);
            headingBRSBookRow.createCell(5).setCellValue(prop.getString("aif.cell.perform.fee"));
            headingBRSBookRow.getCell(5).setCellStyle(csHorVerCenter);
            // sheet.autoSizeColumn(4);
            headingBRSBookRow.createCell(6).setCellValue(prop.getString("aif.cell.sum.series"));
            headingBRSBookRow.getCell(6).setCellStyle(csHorVerCenter);
            // sheet.autoSizeColumn(5);
            headingBRSBookRow.createCell(7).setCellValue(prop.getString("aif.cell.tot.units"));
            headingBRSBookRow.getCell(7).setCellStyle(csHorVerCenter);
            // sheet.autoSizeColumn(6);
            headingBRSBookRow.createCell(8).setCellValue(prop.getString("aif.cell.manage.fee.charge"));
            headingBRSBookRow.getCell(8).setCellStyle(csHorVerCenter);
            // sheet.autoSizeColumn(7);
            headingBRSBookRow.createCell(9).setCellValue(prop.getString("aif.cell.total.comm"));
            headingBRSBookRow.getCell(9).setCellStyle(csHorVerCenter);
            // sheet.autoSizeColumn(7);

            iRowNo = sheetAIF.getLastRowNum() + 1;

            HSSFRow blankRow = sheetAIF.createRow(iRowNo);

            int iTotCell = 0;
            HSSFCell blankCell = null;
            //sheet.addMergedRegion(new CellRangeAddress(iRowNo, iRowNo, 0, 1));
            CellStyle csPlainLeft = csPlanLeftRight;
            csPlainLeft.setBorderRight(BorderStyle.NONE);

            CellStyle csPlainRight = csPlanLeftRight;
            csPlainRight.setBorderLeft(BorderStyle.NONE);

            sheetAIF.addMergedRegion(new CellRangeAddress(iRowNo, iRowNo, 0, 2));
            CellStyle csPlainNoBottom = cs;
            csPlainNoBottom.setBorderBottom(BorderStyle.NONE);
            while (iTotCell < 9 || iTotCell == 9) {

                blankCell = blankRow.createCell(iTotCell);
                blankCell.setCellValue("");

                if (iTotCell != 0 && iTotCell != 1 && iTotCell != 2)
                    blankCell.setCellStyle(csPlainNoBottom);//cs
                iTotCell++;
            }

            iRowNo = sheetAIF.getLastRowNum() + 1;

            DecimalFormat decimalFormat = new DecimalFormat("#.0000");
            String sManage = "";
            String sPerform = "";
            String sCommission = "";
            for (ReportAifDistBean bean : reportAifDistBean) {
                sheetAIF.addMergedRegion(new CellRangeAddress(iRowNo, iRowNo, 0, 2));
                reportAIFCalc = new ReportAIFCalc();
                HSSFRow row = sheetAIF.createRow(iRowNo);

                reportAIFCalc.setClientCode(bean.getClientCode());

                row.createCell(0).setCellValue(bean.getClientName());
                row.getCell(0).setCellStyle(csMergedCellBody);//csPlanLeftRightWrap//csMergedCellBody

                reportAIFCalc.setClientName(bean.getClientName());

                row.createCell(1).setCellValue("");
                //row.getCell(1).setCellStyle(csPlainLeft);

                row.createCell(2).setCellValue("");
                //	row.getCell(2).setCellStyle(csPlainRight);

                row.createCell(3).setCellValue(bean.getSeriesName());
                row.getCell(3).setCellStyle(csSecond);//csPlanLeftRight

                reportAIFCalc.setSeriesName(bean.getSeriesName());

                HSSFCell cell = row.createCell(4);
                cell.setCellValue(bean.getManageFee());
                reportAIFCalc.setManageFee(bean.getManageFee());
                cell.setCellType(CellType.NUMERIC);
                cell.setCellStyle(csDF);


                cell = row.createCell(5);
                cell.setCellType(CellType.NUMERIC);
                cell.setCellStyle(csDF);
                cell.setCellValue(0);

                reportAIFCalc.setPerformFee(bean.getPerformFee());

                cell = row.createCell(6);
                cell.setCellType(CellType.NUMERIC);
                cell.setCellStyle(csDF);
                cell.setCellValue(bean.getSumOfUnitSeries());
                reportAIFCalc.setTotSumOfSeriesUnits(bean.getSumOfUnitSeries());

                cell = row.createCell(7);
                cell.setCellValue(bean.getTotNoOfUnits());
                cell.setCellType(CellType.NUMERIC);
                cell.setCellStyle(csDF);
                reportAIFCalc.setTotSumOfIndivUnits(bean.getTotNoOfUnits());

                cell = row.createCell(8);
                cell.setCellValue(bean.getManageFeeCharged());
                cell.setCellType(CellType.NUMERIC);
                cell.setCellStyle(csDF);
                reportAIFCalc.setMngFeeCharged(bean.getManageFeeCharged());

                cell = row.createCell(9);
                cell.setCellValue(bean.getsCommission());
                cell.setCellType(CellType.NUMERIC);
                cell.setCellStyle(csDF);
                // row.createCell(7).setCellValue(sCommission);
                reportAIFCalc.setCommDue(bean.getsCommission());
                reportAIFCalc.setAifComm(commissionDefinition.getDistributorComm());
                reportAIFCalc.setDistributorMaster(distributorMaster);
                reportAIFCalc.setReportGeneration(reportFeeAIF);
                reportAIFCalcRepository.save(reportAIFCalc);
                sTotal += bean.getsCommission();
                iRowNo++;
            }
            String aifTotal = "";
            int iCurrRow = sheetAIF.getLastRowNum() + 1;
            HSSFRow row = sheetAIF.createRow(iCurrRow);

            sheetAIF.addMergedRegion(new CellRangeAddress(iCurrRow, iCurrRow, 0, 2));
            CellStyle csFour = cs;
            CellStyle csDFBoldFour = csDFBold;

            csFour.setBorderBottom(BorderStyle.THIN);
            csFour.setBorderTop(BorderStyle.THIN);
            csFour.setBorderLeft(BorderStyle.THIN);
            csFour.setBorderRight(BorderStyle.THIN);

            csDFBoldFour.setBorderBottom(BorderStyle.THIN);
            csDFBoldFour.setBorderTop(BorderStyle.THIN);
            csDFBoldFour.setBorderLeft(BorderStyle.THIN);
            csDFBoldFour.setBorderRight(BorderStyle.THIN);


            HSSFCell totCell = null;
            iTotCell = 0;
            while (iTotCell < 7 || iTotCell == 7) {
                totCell = row.createCell(iTotCell);
                totCell.setCellValue("");
                totCell.setCellStyle(csDFBoldFour);
                iTotCell++;
            }

            HSSFCell cell = row.createCell(8);
            cell.setCellValue("TOTAL");
            cell.setCellStyle(csFour);//cs

            aifTotal = decimalFormat.format(sTotal);
            cell = row.createCell(9);
            cell.setCellValue((sTotal));
            cell.setCellStyle(csDFBoldFour);//csDFBold

            sheetAIF.setDefaultColumnWidth(18);

            cumulativePmsAifBean.setAifName(prop.getString("aif.total.fee"));
            cumulativePmsAifBean.setAifTotal(aifTotal);
            cumulativePmsAifBeans.add(cumulativePmsAifBean);

            Double totalTrail = 0.0;
            Double totalUpfront = 0.0;
            Double totalClosing = 0.0;
            Double payable = 0.0;
            Double currentPayable = 0d;
            // not required for Multiple strategy sheets - Upfront will not be after Apr 2020

           /* if (distributorMaster.getDistModelType().equals("Upfront")) {
                cumulativeDistributorReportBean = feeTrailUpfrontTransService.getDistributorCurrentReport(distributorMaster,
                    pmsCommission, reportGeneration.getStartDate(), reportGeneration.getToDate());

                closing = cumulativeDistributorReportBean.getCurrentUpfrontAmt()
                    .add(cumulativeDistributorReportBean.getCurrentTrailAmt());
                tempClosing = Float.valueOf(cumulativeDistributorReportBean.getCurrentUpfrontAmt().floatValue())
                    + Float.valueOf(cumulativeDistributorReportBean.getCurrentTrailAmt().floatValue());
                totalTrail = feeTrailUpfrontTransRepository.getTotalTrail(distributorMaster.getId(), reportGeneration.getToDate());
                totalTrail=totalTrail==null ?0.0f :totalTrail;
                totalUpfront = feeTrailUpfrontTransRepository.getTotalUpfront(distributorMaster.getId(), reportGeneration.getToDate());
                totalUpfront=totalUpfront==null ?0.0f :totalUpfront;
                totalClosing = totalUpfront + totalTrail;
                payable = feeTrailUpfrontTransRepository.getTotalPayable(distributorMaster.getId(), reportGeneration.getToDate());
                payable = payable==null ?0.0f :payable;
                Double paid = feeTrailUpfrontTransRepository.getTotalPaid(distributorMaster.getId(), reportGeneration.getToDate());
                paid = paid==null ?0.0f :paid;

                if (totalClosing > 0) {
                    currentPayable = (totalClosing + payable) - paid;
                } else {
                    currentPayable = payable - paid;
                }
                BigDecimal totalPayable = cumulativeDistributorReportBean.getCurrentTotalPayableAmt()
                    .subtract(cumulativeDistributorReportBean.getCurrentPaidAmount());
            }*/
            summarySheetService.cumulativeFee(cumulativePmsAifBeans, sMonthStart, sMonthEnd, distributorMaster.getDistName().trim(),
                sheetSummary, sFinal, cumulativeDistributorReportBean, totalClosing, currentPayable, totalTrail,
                totalUpfront, distributorMaster, reportGeneration, workBook, cumulativeAIFSeriesBCAD);


        } catch (Exception e) {
            System.out.println(e);
        }


    }

    private void cumulativeFeeExist(List<CumulativePmsAifBean> cumulativePmsAifBeans, String sMonthStart, String sMonthEnd, String distName, HSSFSheet sheetSummary,
                                    String sFinal, CumulativeDistributorReportBean cumulativeDistributorReportBean, Double totalClosing, Double currentPayable,
                                    Double totalTrail, Double totalUpfront, DistributorMaster distributorMaster, ReportGeneration reportGeneration, HSSFWorkbook workBook, CumulativeAIFSeriesBCAD cumulativeAIFSeriesBCAD) {

        try {
            Double currentUpfront, openingUpfront;
            Double upfront, trail, payable, paid = 0d;
            ReportsDistributorFee distributorFee;
            ReportsDistributorFee openingDistributorFee;
            Integer sMonth = monthCalculate(reportGeneration.getStartDate());
            Integer sYEar = getYear(reportGeneration.getToDate());
            System.out.println(sMonth + sYEar);


            distributorFee = reportsDistributorFeeRepository.getCurrentTrans(distributorMaster.getId(), monthCalculate(reportGeneration.getToDate()), getYear(reportGeneration.getToDate()));
            openingDistributorFee = reportsDistributorFeeRepository.getCurrentTrans(distributorMaster.getId(),
                monthCalculate(reportGeneration.getStartDate()), getYear(reportGeneration.getStartDate()));

            upfront = reportsDistributorFeeRepository.getUpfront(distributorMaster.getId(), reportGeneration.getStartDate(), reportGeneration.getToDate());
            trail = reportsDistributorFeeRepository.getTrail(distributorMaster.getId(), reportGeneration.getStartDate(), reportGeneration.getToDate());
            payable = reportsDistributorFeeRepository.getPayable(distributorMaster.getId(), reportGeneration.getToDate());
            paid = reportsDistributorFeeRepository.getPaid(distributorMaster.getId(), reportGeneration.getToDate());
            currentUpfront = feeTrailUpfrontTransRepository.getCurrentUpfront(distributorMaster.getId(), reportGeneration.getStartDate(), reportGeneration.getToDate());
            openingUpfront = feeTrailUpfrontTransRepository.getTotalUpfront(distributorMaster.getId(), reportGeneration.getStartDate());
            CumulativePmsAifBean cumulativePms = new CumulativePmsAifBean();
            CumulativePmsAifBean cumulativeAif = new CumulativePmsAifBean();
            cumulativePms = cumulativePmsAifBeans.get(0);
            cumulativeAif = cumulativePmsAifBeans.get(1);
            Float sTotal = 0.0f;
            CellStyle cs = workBook.createCellStyle();
            CellStyle csRight = workBook.createCellStyle();

            HSSFFont fFont = workBook.createFont();
            fFont.setBold(true);
            fFont.setFontHeightInPoints((short) 11);
            fFont.setFontName("Calibri");
            cs.setFont(fFont);

            csRight.setAlignment(HorizontalAlignment.RIGHT);

            // Data Format
            CellStyle csDF = StyleUtil.getStyleDataFormat(workBook);
            CellStyle csPlainNoBorder = workBook.createCellStyle();

            csDF.setBorderTop(BorderStyle.THIN);
            csDF.setBorderBottom(BorderStyle.THIN);
            csDF.setBorderLeft(BorderStyle.THIN);
            csDF.setBorderRight(BorderStyle.THIN);

            cs.setBorderTop(BorderStyle.THIN);
            cs.setBorderBottom(BorderStyle.THIN);
            cs.setBorderLeft(BorderStyle.THIN);
            cs.setBorderRight(BorderStyle.THIN);

            HSSFFont fValues = workBook.createFont();
            fValues.setFontHeightInPoints((short) 11);
            fValues.setFontName("Calibri");


            csPlainNoBorder.setFont(fFont);//fValues
            csPlainNoBorder.setBorderBottom(BorderStyle.NONE);
            csPlainNoBorder.setBorderTop(BorderStyle.NONE);
            csPlainNoBorder.setBorderLeft(BorderStyle.NONE);
            csPlainNoBorder.setBorderRight(BorderStyle.NONE);

            HSSFRow rowUserName = sheetSummary.createRow(sheetSummary.getLastRowNum() + 1);
            rowUserName.createCell(0).setCellValue(distName.toUpperCase());
            rowUserName.getCell(0).setCellStyle(csPlainNoBorder);

            HSSFRow durationFrom = sheetSummary.createRow(sheetSummary.getLastRowNum() + 1);
            durationFrom.createCell(0).setCellValue("FEES PAYABALE FOR " + sMonthStart + "-" + sMonthEnd);
            durationFrom.getCell(0).setCellStyle(csPlainNoBorder);

            HSSFRow distFee = sheetSummary.createRow(sheetSummary.getLastRowNum() + 1);
            distFee.createCell(0).setCellValue("Distribution Fee");
            distFee.getCell(0).setCellStyle(csPlainNoBorder);

            // Format sheet
            sheetSummary.addMergedRegion(new CellRangeAddress(1, 1, 0, 3));
            sheetSummary.addMergedRegion(new CellRangeAddress(2, 2, 0, 3));
            sheetSummary.addMergedRegion(new CellRangeAddress(3, 3, 0, 3));

            csDF.setFont(fValues);

            int iRowNo = sheetSummary.getLastRowNum() + 3;
            HSSFRow headingBRSBookRow = sheetSummary.createRow(iRowNo);
            headingBRSBookRow.createCell(0).setCellValue(cumulativePms.getBrokerageName());
            headingBRSBookRow.getCell(0).setCellStyle(cs);
            sheetSummary.autoSizeColumn(0);

            HSSFCell cell = headingBRSBookRow.createCell(1);
            cell.setCellValue((cumulativePms.getBrokerageTotal()));
            cell.setCellStyle(csDF);
            cell.setCellType(CellType.NUMERIC);
            sTotal += cumulativePms.getBrokerageTotal();

            iRowNo = sheetSummary.getLastRowNum() + 1;
            headingBRSBookRow = sheetSummary.createRow(iRowNo);
            headingBRSBookRow.createCell(0).setCellValue(cumulativePms.getPmsName());
            headingBRSBookRow.getCell(0).setCellStyle(cs);

            cell = headingBRSBookRow.createCell(1);
            cell.setCellValue((cumulativePms.getPmsTotal()));
            cell.setCellStyle(csDF);
            cell.setCellType(CellType.NUMERIC);
            sTotal += cumulativePms.getPmsTotal();

            iRowNo = sheetSummary.getLastRowNum() + 1;
            headingBRSBookRow = sheetSummary.createRow(iRowNo);
            headingBRSBookRow.createCell(0).setCellValue(cumulativePms.getProfitName());
            headingBRSBookRow.getCell(0).setCellStyle(cs);
            sheetSummary.autoSizeColumn(0);
            cell = headingBRSBookRow.createCell(1);
            cell.setCellValue((cumulativePms.getProfitTotal()));
            cell.setCellStyle(csDF);
            cell.setCellType(CellType.NUMERIC);
            sTotal += cumulativePms.getProfitTotal();

            iRowNo = sheetSummary.getLastRowNum() + 1;
            headingBRSBookRow = sheetSummary.createRow(iRowNo);
            headingBRSBookRow.createCell(0).setCellValue(cumulativeAif.getAifName());
            headingBRSBookRow.getCell(0).setCellStyle(cs);

            cell = headingBRSBookRow.createCell(1);
            cell.setCellValue((Float.parseFloat(cumulativeAif.getAifTotal())));
            sTotal += Float.parseFloat(cumulativeAif.getAifTotal());
            cell.setCellStyle(csDF);
            cell.setCellType(CellType.NUMERIC);


            iRowNo = sheetSummary.getLastRowNum() + 1;
            headingBRSBookRow = sheetSummary.createRow(iRowNo);
            headingBRSBookRow.createCell(0).setCellValue(prop.getString("pms.total.fee"));
            headingBRSBookRow.getCell(0).setCellStyle(cs);

            cell = headingBRSBookRow.createCell(1);
            cell.setCellValue((sTotal));
            cell.setCellStyle(csDF);
            cell.setCellType(CellType.NUMERIC);

            sheetSummary.setDefaultColumnWidth(16);

            if (distributorMaster.getDistModelType().equals("Upfront")) {
                iRowNo = sheetSummary.getLastRowNum() + 2;
                headingBRSBookRow = sheetSummary.createRow(iRowNo);
                headingBRSBookRow.createCell(0).setCellValue(prop.getString("cum.open.bal"));
                Float currentTrail = sTotal;
                headingBRSBookRow.getCell(0).setCellStyle(cs);
                sheetSummary.autoSizeColumn(0);
                headingBRSBookRow.createCell(1).setCellValue((openingDistributorFee.getOpeningBal()));
                headingBRSBookRow.getCell(1).setCellStyle(csDF);
                sheetSummary.autoSizeColumn(1);

                iRowNo = sheetSummary.getLastRowNum() + 1;
                headingBRSBookRow = sheetSummary.createRow(iRowNo);
                headingBRSBookRow.createCell(0).setCellValue(prop.getString("trail.amt.fee"));
                headingBRSBookRow.getCell(0).setCellStyle(cs);
                sheetSummary.autoSizeColumn(0);
                headingBRSBookRow.createCell(1).setCellValue((sTotal));
                headingBRSBookRow.getCell(1).setCellStyle(csDF);
                sheetSummary.autoSizeColumn(1);
                iRowNo = sheetSummary.getLastRowNum() + 1;
                headingBRSBookRow = sheetSummary.createRow(iRowNo);
                headingBRSBookRow.createCell(0).setCellValue(prop.getString("upfront.amt.fee"));
                headingBRSBookRow.getCell(0).setCellStyle(cs);
                sheetSummary.autoSizeColumn(0);
                headingBRSBookRow.createCell(1).setCellValue((upfront.floatValue()));
                headingBRSBookRow.getCell(1).setCellStyle(csDF);
                sheetSummary.autoSizeColumn(1);

                iRowNo = sheetSummary.getLastRowNum() + 1;
                headingBRSBookRow = sheetSummary.createRow(iRowNo);
                headingBRSBookRow.createCell(0).setCellValue(prop.getString("paid.amt"));
                headingBRSBookRow.getCell(0).setCellStyle(cs);
                sheetSummary.autoSizeColumn(0);
                headingBRSBookRow.createCell(1).setCellValue(((float) (double) paid));
                headingBRSBookRow.getCell(1).setCellStyle(csDF);
                sheetSummary.autoSizeColumn(1);

                iRowNo = sheetSummary.getLastRowNum() + 1;
                headingBRSBookRow = sheetSummary.createRow(iRowNo);
                headingBRSBookRow.createCell(0).setCellValue(prop.getString("payable.amt"));
                headingBRSBookRow.getCell(0).setCellStyle(cs);
                sheetSummary.autoSizeColumn(0);

                Float fAmount = (float) ((openingDistributorFee.getOpeningBal() + sTotal) - (double) paid);
                headingBRSBookRow.createCell(1).setCellValue((fAmount));

                headingBRSBookRow.getCell(1).setCellStyle(csDF);
                sheetSummary.autoSizeColumn(1);

            }
            //Payment alone
            if (distributorMaster.getDistModelType().equals("Trail")) {
                iRowNo = sheetSummary.getLastRowNum() + 2;
                headingBRSBookRow = sheetSummary.createRow(iRowNo);
                headingBRSBookRow.createCell(0).setCellValue(prop.getString("cum.open.bal"));
                Float currentTrail = sTotal;
                headingBRSBookRow.getCell(0).setCellStyle(cs);
                sheetSummary.autoSizeColumn(0);
                headingBRSBookRow.createCell(1).setCellValue((openingDistributorFee.getOpeningBal()));
                headingBRSBookRow.getCell(1).setCellStyle(csDF);
                sheetSummary.autoSizeColumn(1);


                iRowNo = sheetSummary.getLastRowNum() + 1;
                headingBRSBookRow = sheetSummary.createRow(iRowNo);
                headingBRSBookRow.createCell(0).setCellValue(prop.getString("paid.amt"));
                headingBRSBookRow.getCell(0).setCellStyle(cs);
                sheetSummary.autoSizeColumn(0);
                headingBRSBookRow.createCell(1).setCellValue(((float) (double) paid));
                headingBRSBookRow.getCell(1).setCellStyle(csDF);
                sheetSummary.autoSizeColumn(1);

                iRowNo = sheetSummary.getLastRowNum() + 1;
                headingBRSBookRow = sheetSummary.createRow(iRowNo);
                headingBRSBookRow.createCell(0).setCellValue(prop.getString("payable.amt"));
                headingBRSBookRow.getCell(0).setCellStyle(cs);
                sheetSummary.autoSizeColumn(0);
                double tPay = 0.0;
                tPay = (double) ((distributorFee.getClosingBal() + payable) - paid);

                Float fPay = (float) (tPay);
                headingBRSBookRow.createCell(1).setCellValue((fPay));

                headingBRSBookRow.getCell(1).setCellStyle(csDF);

                sheetSummary.autoSizeColumn(1);
                iRowNo = sheetSummary.getLastRowNum() + 1;
                headingBRSBookRow = sheetSummary.createRow(iRowNo);
                headingBRSBookRow.createCell(0).setCellValue(prop.getString("trail.alone.close"));
                headingBRSBookRow.getCell(0).setCellStyle(cs);
                sheetSummary.autoSizeColumn(0);
                headingBRSBookRow.createCell(1).setCellValue(0);
                headingBRSBookRow.getCell(1).setCellStyle(csDF);
                sheetSummary.autoSizeColumn(1);
            }

            iRowNo = sheetSummary.getLastRowNum() + 2;
            headingBRSBookRow = sheetSummary.createRow(iRowNo);
            headingBRSBookRow.createCell(0).setCellValue(prop.getString("bcad.distributor.share"));
            headingBRSBookRow.getCell(0).setCellStyle(cs);
            sheetSummary.autoSizeColumn(0);
            headingBRSBookRow.createCell(1).setCellValue((cumulativeAIFSeriesBCAD.getBCADDistValue().floatValue()));
            headingBRSBookRow.getCell(1).setCellStyle(csDF);
            sheetSummary.autoSizeColumn(1);

            iRowNo = sheetSummary.getLastRowNum() + 1;
            headingBRSBookRow = sheetSummary.createRow(iRowNo);
            headingBRSBookRow.createCell(0).setCellValue(prop.getString("bcad.trail.share"));
            headingBRSBookRow.getCell(0).setCellStyle(cs);
            sheetSummary.autoSizeColumn(0);
            headingBRSBookRow.createCell(1).setCellValue((cumulativeAIFSeriesBCAD.getBCADTrailValue().floatValue()));
            headingBRSBookRow.getCell(1).setCellStyle(csDF);
            sheetSummary.autoSizeColumn(1);

            iRowNo = sheetSummary.getLastRowNum() + 1;
            headingBRSBookRow = sheetSummary.createRow(iRowNo);
            headingBRSBookRow.createCell(0).setCellValue(prop.getString("bcad.upfront.share"));
            headingBRSBookRow.getCell(0).setCellStyle(cs);
            sheetSummary.autoSizeColumn(0);
            headingBRSBookRow.createCell(1).setCellValue((cumulativeAIFSeriesBCAD.getBCADUpfrontValue().floatValue()));
            headingBRSBookRow.getCell(1).setCellStyle(csDF);
            sheetSummary.autoSizeColumn(1);

            iRowNo = sheetSummary.getLastRowNum() + 1;
            headingBRSBookRow = sheetSummary.createRow(iRowNo);
            headingBRSBookRow.createCell(0).setCellValue(prop.getString("aif2.total.fee"));
            headingBRSBookRow.getCell(0).setCellStyle(cs);
            sheetSummary.autoSizeColumn(0);
            headingBRSBookRow.createCell(1).setCellValue((cumulativeAIFSeriesBCAD.getAIFGreenValue().floatValue()));
            headingBRSBookRow.getCell(1).setCellStyle(csDF);
            sheetSummary.autoSizeColumn(1);

            iRowNo = sheetSummary.getLastRowNum() + 1;
            headingBRSBookRow = sheetSummary.createRow(iRowNo);
            headingBRSBookRow.createCell(0).setCellValue(prop.getString("aif.blend.total"));
            headingBRSBookRow.getCell(0).setCellStyle(cs);
            sheetSummary.autoSizeColumn(0);
            headingBRSBookRow.createCell(1).setCellValue((cumulativeAIFSeriesBCAD.getAIFBlendValue().floatValue()));
            headingBRSBookRow.getCell(1).setCellStyle(csDF);
            sheetSummary.autoSizeColumn(1);


        } catch (Exception e) {
            System.out.println(e);
        }
    }


    private void distributorFee(Float reportAif, DistributorMaster distributorMaster, Date dDate, Float reportAifManag, Float reportAifPer, ReportGeneration reportGeneration) throws ParseException {


        java.sql.Date eDate = new java.sql.Date(reportGeneration.getToDate().getTime());
        DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String previossDate = sdf.format(dDate);
        List<PMSNav> pmsNavs = new ArrayList<>();


        List<DistributorPMSBean> distributorPMSBeans;
        List<PMSClientMaster> pmsClientMasters = pmsClientMasterRepository.findByDistributorMaster(distributorMaster);

        distributorPMSBeans = new ArrayList<DistributorPMSBean>();
        for (PMSClientMaster pmsClientMaster : pmsClientMasters) {
            DistributorPMSBean bean = null;
            Double pmsNav = null;


            Double brokage = brokerageRepository.getPeriodBrokerageMonthVice(previossDate, pmsClientMaster.getId());

            // System.out.println("bro" + brokage);

            if (brokage != null) {
                bean = new DistributorPMSBean();
                bean.setClientCode(pmsClientMaster.getClientCode());
                bean.setClientName(pmsClientMaster.getClientName());
                bean.setTotal(Float.valueOf(brokage.toString()));
                bean.setPmsClientMaster(pmsClientMaster);

                pmsNav = pmsNavRepository.getPMSFeeMonthWise(pmsClientMaster.getId(), previossDate);
                if (pmsNav!=null) {
                    /* Float fee = 0.0f;
                   for (PMSNav pmsNav : pmsNavs) {
                        fee += pmsNav.getCalcPmsNav();
                    }*/
                    bean.setPmsFee(pmsNav.toString());
                } else {
                    bean.setPmsFee("0");
                }
                distributorPMSBeans.add(bean);
            }
            if (pmsNav==null) {
                /**
                 * adding when nav available brokerage not available
                 */
                pmsNav = pmsNavRepository.getPMSFeeMonthWise(pmsClientMaster.getId(), previossDate);
                if (pmsNav!=null) {
                    bean = new DistributorPMSBean();
                    bean.setClientCode(pmsClientMaster.getClientCode());
                    bean.setClientName(pmsClientMaster.getClientName());
                    bean.setTotal(0f);
                    bean.setPmsClientMaster(pmsClientMaster);
                   /* Float fee = 0.0f;
                    for (PMSNav pmsNav : pmsNavs) {
                        fee += pmsNav.getCalcPmsNav();
                    }*/
                    bean.setPmsFee(pmsNav.toString());
                    distributorPMSBeans.add(bean);

                }

            }
        }
        updateMonthViceNew(distributorPMSBeans, distributorMaster, dDate, reportGeneration, reportAif, reportAifManag, reportAifPer);


    }

    private void updateMonthViceNew(List<DistributorPMSBean> distributorPMSBeans, DistributorMaster distributorMaster, Date dDate, ReportGeneration reportGeneration, Float reportAif, Float reportAifManag, Float reportAifPer) throws ParseException {

        BigDecimal closing, payableClosing;
        Float tempClosing, tempPayableClosing, pmsCommission, monthViceUpfront, closingBal = 0f;
        Integer month = 0, year = 0, bmonth = 0, byear = 0;
        String months = "";

        ClientFeeCommission clientFeeCommission;
        CumulativeDistributorReportBean cumulativeDistributorReportBean = null;
        ReportsDistributorFee reportsDistributorFee;
        pmsCommission = prop.getFloat("fee.upfront.pms.commission");
        Float marketingFee = 0.0f, profitSharFee = 0.0f, pmsNavFee = 0.0f, totalTrail = 0.0f;
        for (DistributorPMSBean distributorPMSBean : distributorPMSBeans) {
            clientFeeCommission = clientFeeCommissionRepository.findByPmsClientMaster(distributorPMSBean.getPmsClientMaster());
            if (clientFeeCommission.getBrokerageComm() != null) {
                marketingFee += (clientFeeCommission.getBrokerageComm() / 100) * (distributorPMSBean.getTotal());
                pmsNavFee += (clientFeeCommission.getNavComm() / 100) * (Float.valueOf(distributorPMSBean.getPmsFee()));
                System.out.println(pmsNavFee);
            }

            System.out.println(distributorPMSBean.getClientName() + "broker" + distributorPMSBean.getTotal());

        }

        /**
         * profit share month vice
         */
        List<ProfitShare> profitShares = profitShareRepository.getProfitShareByDist(distributorMaster.getId(), dDate);
        for (ProfitShare profitShare : profitShares) {
            clientFeeCommission = clientFeeCommissionRepository.findByPmsClientMaster(profitShare.getPmsClientMaster());
            profitSharFee += (clientFeeCommission.getProfitComm() / 100) * (profitShare.getProfitShareIncome());

        }

        /**
         * temporay removing aif for upfront calculation
         */
        // totalTrail=marketingFee+pmsNavFee+profitSharFee+aifFee;

        month = monthCalculate(dDate);
        year = getYear(dDate);
        Double monthUpfront = 0d, monthPayable = 0d, monthPaid = 0d;
        monthUpfront = feeTrailUpfrontTransRepository.getTotalUpfrontMonthVice(distributorMaster.getId(), month, year);
        monthPayable = feeTrailUpfrontTransRepository.getTotalPayableMonthVice(distributorMaster.getId(), month, year);
        monthPaid = feeTrailUpfrontTransRepository.getTotalPaidMonthVice(distributorMaster.getId(), month, year);

        Date previousDate = getPreviousMonth(dDate);
        DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String previossDate = sdf.format(previousDate);
        ReportsDistributorFee feeDO = reportsDistributorFeeRepository.findCurrentOpeningBalance(previossDate, distributorMaster.getId());

        totalTrail = marketingFee + pmsNavFee + profitSharFee;
        reportsDistributorFee = new ReportsDistributorFee();
        reportsDistributorFee.setMarketingFee(marketingFee);
        reportsDistributorFee.setDistributorMaster(distributorMaster);
        reportsDistributorFee.setAifTotal(reportAif);
        reportsDistributorFee.setAifManagTotal(reportAifManag);
        reportsDistributorFee.setAifPerfTotal(reportAifPer);
        reportsDistributorFee.setAdvisoryFee(0f);
        reportsDistributorFee.setPmsTotal(0f);
        reportsDistributorFee.setProfitShareFee(profitSharFee);
        reportsDistributorFee.setDetailsUpdatedFlag(0);
        reportsDistributorFee.setStartDate(dDate);
        reportsDistributorFee.setAdvisoryFee(pmsNavFee);
        reportsDistributorFee.setPmsTotal(totalTrail);
        if (monthPayable != null) {
            reportsDistributorFee.setPayableAmount(monthPayable.floatValue());
        } else {
            reportsDistributorFee.setPayableAmount(0f);
        }
        if (monthPaid != null) {
            reportsDistributorFee.setPaidAmount(monthPaid.floatValue());
        } else {
            reportsDistributorFee.setPaidAmount(0f);
        }

        reportsDistributorFee.setUpfrontAmount(0f);

        if (monthUpfront != null) {
            monthViceUpfront = Float.valueOf(monthUpfront.floatValue());
            reportsDistributorFee.setUpfrontAmount(monthViceUpfront);
        }
        Double quarterFour = 0.0;
        if (feeDO != null) {
            reportsDistributorFee.setOpeningBal(feeDO.getClosingBal());
            if (monthUpfront != null) {
                months = formatMonth(month);
                if (!months.equals(prop.getString("fee.fourth.quarter.month")))
                    closingBal = feeDO.getClosingBal() + Float.valueOf(totalTrail + reportAifManag + monthUpfront.floatValue());
                if (months.equals(prop.getString("fee.fourth.quarter.month"))) {

                    String sStart = previousYear(reportGeneration.getToDate());
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                    Date sStartDate = format.parse(sStart);

                    quarterFour = reportsDistributorFeeRepository.getQuarter(distributorMaster.getId(), sStartDate, reportGeneration.getToDate());
                    closingBal = feeDO.getClosingBal() + Float.valueOf(totalTrail + monthUpfront.floatValue() + reportAifManag + quarterFour.floatValue());
                }
                reportsDistributorFee.setClosingBal(closingBal);

            } else {
                if (!months.equals(prop.getString("fee.fourth.quarter.month")))
                    closingBal = feeDO.getClosingBal() + Float.valueOf(totalTrail) + reportAifManag;
                reportsDistributorFee.setClosingBal(closingBal);
                if (months.equals(prop.getString("fee.fourth.quarter.month"))) {

                    String sStart = previousYear(reportGeneration.getToDate());
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                    Date sStartDate = format.parse(sStart);

                    quarterFour = reportsDistributorFeeRepository.getQuarter(distributorMaster.getId(), sStartDate, reportGeneration.getToDate());
                    closingBal = feeDO.getClosingBal() + Float.valueOf(totalTrail) + reportAifManag + quarterFour.floatValue();
                    reportsDistributorFee.setClosingBal(closingBal);
                }

            }

        } else {

            DistributorOpeningBal distributorOpeningBal = null;
            if (!months.equals(prop.getString("fee.fourth.quarter.month"))) {
                distributorOpeningBal = distributorOpeningBalRepository.findByDistributorMaster(distributorMaster);
                System.out.println(distributorOpeningBal);
                Float openBal = 0f;
                if (distributorOpeningBal != null)
                    openBal = distributorOpeningBal.getOpeningBal();
                reportsDistributorFee.setOpeningBal(openBal);
                if (monthUpfront != null) {
                    reportsDistributorFee
                        .setClosingBal(Float.valueOf(totalTrail + reportAifManag + monthUpfront.floatValue() + (openBal)));
                } else {
                    reportsDistributorFee.setClosingBal(Float.valueOf(totalTrail + reportAifManag + (openBal)));
                }
            }
            if (months.equals(prop.getString("fee.fourth.quarter.month"))) {
                distributorOpeningBal = distributorOpeningBalRepository.findByDistributorMaster(distributorMaster);
                System.out.println(distributorOpeningBal);
                Float openBal = 0f;
                if (distributorOpeningBal != null)
                    openBal = distributorOpeningBal.getOpeningBal();
                reportsDistributorFee.setOpeningBal(openBal);
                String sStart = previousYear(reportGeneration.getToDate());
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                Date sStartDate = format.parse(sStart);

                quarterFour = reportsDistributorFeeRepository.getQuarter(distributorMaster.getId(), sStartDate, reportGeneration.getToDate());
                if (monthUpfront != null) {
                    reportsDistributorFee
                        .setClosingBal(Float.valueOf(totalTrail + reportAifManag + monthUpfront.floatValue() + (openBal)) + quarterFour.floatValue());
                } else {
                    reportsDistributorFee.setClosingBal(Float.valueOf(totalTrail + reportAifManag + (openBal)) + quarterFour.floatValue());
                }
            }
        }

        reportsDistributorFeeRepository.save(reportsDistributorFee);

        System.out.println("brokerage " + marketingFee);

    }

    private void writeExistReportPMS(List<ReportGeneration> reportFees, List<CumulativePmsAifBean> cumulativePmsAifBeans, DistributorMaster distributorMaster, HSSFSheet sheetSummary, HSSFSheet sheetPMS, HSSFSheet sheetAIF, ReportGeneration reportGeneration, HSSFWorkbook workBook, CommissionDefinition commissionDefinition, CumulativeAIFSeriesBCAD cumulativeAIFSeriesBCAD) {

        try {
            DateFormat dateFormat = new SimpleDateFormat("MMM yyyy");
            String sStartTime = dateFormat.format(reportGeneration.getStartDate());
            String sEndTime = dateFormat.format(reportGeneration.getToDate());
            String sFinal = sStartTime + "_to_" + sEndTime;

            ReportGeneration reportGenerationExist;
            reportGenerationExist = reportFees.get(0);
            System.out.println(reportFees);
            CumulativePmsAifBean cumulativePmsAifBean = new CumulativePmsAifBean();
            ReportBrokeragePMS reportBrokeragePMS;
            String fName = "";

            Integer sFlag=0;
            Product productBCAD = productRepository.findByProductName("BCAD");
            List<CommissionDefinition> checkOption3=commissionDefinitionRepository.findOption3Commission(
                distributorMaster.getId(),productBCAD.getId());
            if(checkOption3!=null)
                sFlag=1;

            int sCount = 0;

          /*  HSSFSheet sheet2 = workBook.createSheet("Summary");
            HSSFSheet sheet = workBook.createSheet("PMS");
            HSSFSheet sheet1 = workBook.createSheet("AIF");*/

            sheetPMS.setZoom(90);
            sheetAIF.setZoom(90);
            sheetSummary.setZoom(90);
            sheetPMS.setDisplayGridlines(false);
            sheetAIF.setDisplayGridlines(false);
            sheetSummary.setDisplayGridlines(false);

            HSSFFont defaultFont = workBook.createFont();
            defaultFont.setFontHeightInPoints((short) 11);
            defaultFont.setFontName("Calibri");

            CellStyle cs = workBook.createCellStyle();
            CellStyle csRight = workBook.createCellStyle();
            CellStyle csRightLeftRight = workBook.createCellStyle();

            // Format sheet
            CellStyle csHorVerCenter = workBook.createCellStyle();
            CellStyle csHorVerDate = workBook.createCellStyle();
            CellStyle csPlanLeftRight = workBook.createCellStyle();
            CellStyle csPlainNoBorder = workBook.createCellStyle();
            CellStyle csPercNoBorder = workBook.createCellStyle();

            HSSFFont fFont = workBook.createFont();
            fFont.setFontHeightInPoints((short) 11);
            fFont.setFontName("Calibri");
            fFont.setBold(true);
            cs.setFont(fFont);

            HSSFFont fFontNoBold = workBook.createFont();
            fFontNoBold.setFontHeightInPoints((short) 11);
            fFontNoBold.setFontName("Calibri");

            csRight.setAlignment(HorizontalAlignment.LEFT);
            // Format sheet
            csHorVerCenter.setAlignment(HorizontalAlignment.CENTER);
            csHorVerCenter.setVerticalAlignment(VerticalAlignment.CENTER);
            csHorVerCenter.setFont(fFont);
            csHorVerCenter.setWrapText(true);

            csHorVerCenter.setBorderTop(BorderStyle.THIN);
            csHorVerCenter.setBorderBottom(BorderStyle.THIN);
            csHorVerCenter.setBorderLeft(BorderStyle.THIN);
            csHorVerCenter.setBorderRight(BorderStyle.THIN);

            csHorVerDate.setAlignment(HorizontalAlignment.CENTER);
            csHorVerDate.setVerticalAlignment(VerticalAlignment.CENTER);
            csHorVerDate.setFont(defaultFont);
            csHorVerDate.setWrapText(true);


            csPlanLeftRight.setBorderLeft(BorderStyle.THIN);
            csPlanLeftRight.setBorderRight(BorderStyle.THIN);
            csPlanLeftRight.setFont(fFontNoBold);
            // Data Format

            CellStyle csDF = StyleUtil.getStyleDataFormat(workBook);

            CellStyle csPlain = StyleUtil.getStyleDataFormat(workBook);

            CellStyle csDFBold = StyleUtil.getStyleBoldDataFormat(workBook);

            CellStyle csPerc = StyleUtil.getStylePercDataFormat(workBook);

            csDF.setBorderLeft(BorderStyle.THIN);
            csDF.setBorderRight(BorderStyle.THIN);

            csPlain.setBorderLeft(BorderStyle.THIN);
            csPlain.setBorderRight(BorderStyle.THIN);

            csDFBold.setBorderLeft(BorderStyle.THIN);
            csDFBold.setBorderRight(BorderStyle.THIN);

            csPercNoBorder = csPerc;

            csPerc.setBorderLeft(BorderStyle.THIN);
            csPerc.setBorderRight(BorderStyle.THIN);

            csPercNoBorder.setBorderLeft(BorderStyle.NONE);
            csPercNoBorder.setBorderRight(BorderStyle.NONE);

            csRight.setFont(defaultFont);
            //csHorVerCenter.setFont(defaultFont);
            csDF.setFont(defaultFont);
            csPlain.setFont(defaultFont);
            //csDFBold.setFont(defaultFont);
            csPerc.setFont(defaultFont);

            csPlainNoBorder.setFont(fFont);
            csPlainNoBorder.setBorderBottom(BorderStyle.NONE);
            csPlainNoBorder.setBorderTop(BorderStyle.NONE);
            csPlainNoBorder.setBorderLeft(BorderStyle.NONE);
            csPlainNoBorder.setBorderRight(BorderStyle.NONE);

            HSSFRow rowUserName = sheetPMS.createRow(sheetPMS.getLastRowNum() + 1);
            rowUserName.createCell(0).setCellValue(distributorMaster.getDistName().toUpperCase());
            rowUserName.getCell(0).setCellStyle(csPlainNoBorder);//cs

            HSSFRow durationFrom = sheetPMS.createRow(sheetPMS.getLastRowNum() + 1);
            durationFrom.createCell(0).setCellValue("FEES PAYABALE FOR " + sStartTime + "-" + sEndTime);
            durationFrom.getCell(0).setCellStyle(csPlainNoBorder);//cs
            // Format sheet
            sheetPMS.addMergedRegion(new CellRangeAddress(1, 1, 0, 3));
            sheetPMS.addMergedRegion(new CellRangeAddress(2, 2, 0, 3));

            int iDistCommLineNum = sheetPMS.getLastRowNum() + 1;
            boolean bIsCommissionSet = false;
            HSSFRow distComm = sheetPMS.createRow(iDistCommLineNum);
            distComm.createCell(0).setCellValue("Distributor Commission");
            distComm.getCell(0).setCellStyle(csPlainNoBorder);//cs


            sheetPMS.createFreezePane(0, 7);

            int iRowNo = sheetPMS.getLastRowNum() + 3;
            HSSFRow headingBRSBookRow = sheetPMS.createRow(iRowNo);
            // Format sheet
            headingBRSBookRow.setHeightInPoints(30);
            HSSFCell cellTemp = headingBRSBookRow.createCell(0);

            cellTemp.setCellValue(prop.getString("pms.cell.client_name"));
            cellTemp.setCellStyle(csHorVerCenter);

            headingBRSBookRow.createCell(1).setCellValue(prop.getString("pms.cell.client_code"));
            headingBRSBookRow.getCell(1).setCellStyle(csHorVerCenter);

            headingBRSBookRow.createCell(2).setCellValue(prop.getString("pms.cell.brokerage"));
            headingBRSBookRow.getCell(2).setCellStyle(csHorVerCenter);

            headingBRSBookRow.createCell(3).setCellValue(prop.getString("pms.cell.brokerage.fee"));
            headingBRSBookRow.getCell(3).setCellStyle(csHorVerCenter);

            headingBRSBookRow.createCell(4).setCellValue(prop.getString("pms.cell.pms"));
            headingBRSBookRow.getCell(4).setCellStyle(csHorVerCenter);

            headingBRSBookRow.createCell(5).setCellValue(prop.getString("pms.cell.net_payable"));
            headingBRSBookRow.getCell(5).setCellStyle(csHorVerCenter);

            if (distributorMaster.getDistModelType().equals("Upfront")) {

                headingBRSBookRow.createCell(7).setCellValue(prop.getString("pms.cell.trans_dt"));
                headingBRSBookRow.getCell(7).setCellStyle(csHorVerCenter);

                headingBRSBookRow.createCell(8).setCellValue(prop.getString("pms.cell.init_fund"));
                headingBRSBookRow.getCell(8).setCellStyle(csHorVerCenter);

                headingBRSBookRow.createCell(9).setCellValue(prop.getString("pms.cell.add_fund"));
                headingBRSBookRow.getCell(9).setCellStyle(csHorVerCenter);

                headingBRSBookRow.createCell(10).setCellValue(prop.getString("pms.cell.tot_corp"));
                headingBRSBookRow.getCell(10).setCellStyle(csHorVerCenter);

                headingBRSBookRow.createCell(11).setCellValue(prop.getString("pms.cell.tot_corp_share"));
                headingBRSBookRow.getCell(11).setCellStyle(csHorVerCenter);
            }

            iRowNo = sheetPMS.getLastRowNum() + 1;
            HSSFRow blankRow = sheetPMS.createRow(iRowNo);

            int iTotCell = 0;
            HSSFCell blankCell = null;

            int iMaxAllowedCell = 0;

            if (distributorMaster.getDistModelType().equals("Upfront"))
                iMaxAllowedCell = 11;
            else
                iMaxAllowedCell = 6;
            while (iTotCell < iMaxAllowedCell || iTotCell == iMaxAllowedCell) {
                blankCell = blankRow.createCell(iTotCell);
                blankCell.setCellValue("");
                blankCell.setCellStyle(csPlanLeftRight);
                iTotCell++;
            }

            iRowNo = sheetPMS.getLastRowNum() + 1;
            Float sFee = 0f;
            float sTotal = 0f;
            String sUcpl = "";
            String tPms = "";
            float sMarkup = 0f;
            float sAdvisory = 0f;
            float sPms = 0f;
            float sAdv = 0f;
            float tInitial = 0f;
            float tAdd = 0f;
            float tTot = 0f;
            float tToShare = 0f;
            double rounded = 0.0;
            Float fTotFee = 0f;
            List<ReportBrokeragePMS> reportBrokeragePMSs = reportBrokeragePMSRepository.findByReportGeneration(reportGenerationExist);
            DecimalFormat decimalFormat = new DecimalFormat("#.0000");
            for (ReportBrokeragePMS bean : reportBrokeragePMSs) {
                HSSFRow row = sheetPMS.createRow(iRowNo);

                HSSFCell cell = row.createCell(0);
                cell.setCellValue(bean.getClientName());
                cell.setCellStyle(csPlanLeftRight);

                cell = row.createCell(1);
                cell.setCellType(CellType.NUMERIC);
                cell.setCellValue(Double.parseDouble(bean.getClientCode()));
                cell.setCellStyle(csPlanLeftRight);

                System.out.println(bean.getBrokeFee());
                sFee += bean.getBrokeFee();
                sUcpl = decimalFormat.format(bean.getBrokeFee());
                cell = row.createCell(2);
                cell.setCellType(CellType.NUMERIC);
                cell.setCellStyle(csDF);
                cell.setCellValue(Double.parseDouble(sUcpl));

                Float myValue = bean.getBrokeTotal();
                rounded = (double) Math.round(myValue);
                sTotal += (float) rounded;

                if (bIsCommissionSet == false) {
                    distComm.createCell(1).setCellValue(commissionDefinition.getNavComm() / 100);
                    distComm.getCell(1).setCellStyle(csPercNoBorder);
                    distComm.getCell(1).setCellType(CellType.NUMERIC);
                    bIsCommissionSet = true;
                }

                double rRound = (double) Math.round(bean.getMarketingPay());
                sMarkup += (float) rRound;

                cell = row.createCell(3);
                cell.setCellValue(bean.getMarketingPay());
                cell.setCellType(CellType.NUMERIC);
                cell.setCellStyle(csDF);

                cell = row.createCell(4);
                cell.setCellValue(bean.getPmsFee());
                cell.setCellType(CellType.NUMERIC);
                cell.setCellStyle(csDF);
                // cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
                sPms += bean.getPmsFee();
                Float totPms = (float) (bean.getPmsFee() * commissionDefinition.getNavComm()) / 100;
                double pRound = (double) Math.round(totPms);
                sAdvisory += (float) pRound;

                Double dTotal = (double) bean.getPmsFee();
                fTotFee += dTotal.floatValue();

                double rRounds = 0.0;
                System.out.println(bean.getPmsFee());

                rRounds = (double) Math.round(bean.getAdvisoryPay());
                sAdv += (float) rRounds;

                cell = row.createCell(5);
                cell.setCellValue(rRounds);
                cell.setCellType(CellType.NUMERIC);
                cell.setCellStyle(csDF);
                if (distributorMaster.getDistModelType().equals("Upfront")) {
                    cell = row.createCell(7);
                    //csRight.setAlignment(CellStyle.ALIGN_RIGHT);
                    cell.setCellStyle(csHorVerDate);
                    cell.setCellValue(bean.getTransDate());

                    cell = row.createCell(8);
                    cell.setCellType(CellType.NUMERIC);
                    cell.setCellStyle(csDF);
                    cell.setCellValue(bean.getInitialCorpus());
                    tInitial += bean.getInitialCorpus();

                    cell = row.createCell(9);
                    cell.setCellType(CellType.NUMERIC);
                    cell.setCellStyle(csDF);
                    cell.setCellValue(bean.getAddCorpus());
                    tAdd += bean.getAddCorpus();

                    cell = row.createCell(10);
                    cell.setCellType(CellType.NUMERIC);
                    cell.setCellStyle(csDF);
                    cell.setCellValue(bean.getTotalCorpus());
                    tTot += bean.getTotalCorpus();

                    cell = row.createCell(11);
                    cell.setCellType(CellType.NUMERIC);
                    cell.setCellStyle(csDF);
                    cell.setCellValue(bean.getToShareCorpus());
                    tToShare += bean.getToShareCorpus();
                }
                iRowNo++;

                System.out.println("code " + bean.getClientCode() + "name " + bean.getClientName() + "brok "
                    + bean.getBrokeTotal() + "pms " + bean.getPmsFee());

            }

            CellStyle csFour = cs;
            CellStyle csDFBoldFour = csDFBold;

            csFour.setBorderBottom(BorderStyle.THIN);
            csFour.setBorderTop(BorderStyle.THIN);
            csFour.setBorderLeft(BorderStyle.THIN);
            csFour.setBorderRight(BorderStyle.THIN);

            csDFBoldFour.setBorderBottom(BorderStyle.THIN);
            csDFBoldFour.setBorderTop(BorderStyle.THIN);
            csDFBoldFour.setBorderLeft(BorderStyle.THIN);
            csDFBoldFour.setBorderRight(BorderStyle.THIN);


            HSSFRow row = sheetPMS.createRow(sheetPMS.getLastRowNum() + 1);
            row.createCell(0).setCellValue("GRAND TOTALS");
            row.getCell(0).setCellStyle(csFour);


            HSSFCell cell = row.createCell(1);
            cell.setCellValue("");
            cell.setCellStyle(csDFBoldFour);

            cell = row.createCell(2);
            cell.setCellValue((sFee));
            cell.setCellType(CellType.NUMERIC);
            cell.setCellStyle(csDFBoldFour);


            cell = row.createCell(3);
            cell.setCellValue((sMarkup));//sTotal
            cell.setCellType(CellType.NUMERIC);
            cell.setCellStyle(csDFBoldFour);

            cell = row.createCell(4);
            cell.setCellValue((fTotFee));
            cell.setCellType(CellType.NUMERIC);
            cell.setCellStyle(csDFBoldFour);

            cell = row.createCell(5);
            cell.setCellValue((sAdv));//sMarkup
            cell.setCellType(CellType.NUMERIC);
            cell.setCellStyle(csDFBoldFour);

            cumulativePmsAifBean.setBrokerageName(prop.getString("pms.marketing.fee"));
            cumulativePmsAifBean.setBrokerageTotal(sMarkup);

            cell = row.createCell(6);
            cell.setCellValue("");
            cell.setCellStyle(csDFBoldFour);

            cumulativePmsAifBean.setPmsName(prop.getString("pms.adv.fee"));
            cumulativePmsAifBean.setPmsTotal(sAdvisory);

            //following is a dummy loop can be removed after verification
            if (distributorMaster.getDistModelType().equals("Upfront")) {
                cell = row.createCell(7);
                cell.setCellValue("");//sPms
                cell.setCellStyle(csDFBoldFour);

                cell = row.createCell(8);
                cell.setCellValue("");
                cell.setCellStyle(csDFBoldFour);

                cell = row.createCell(9);
                cell.setCellValue(sAdv);
                cell.setCellStyle(csDFBoldFour);
                cell.setCellType(CellType.NUMERIC);

                cell = row.createCell(10);
                cell.setCellValue("");
                cell.setCellStyle(csDFBoldFour);
            }
            if (distributorMaster.getDistModelType().equals("Upfront")) {
                cell = row.createCell(8);
                cell.setCellValue((tInitial));
                cell.setCellStyle(csDFBoldFour);
                cell.setCellType(CellType.NUMERIC);

                cell = row.createCell(9);
                cell.setCellValue((tAdd));
                cell.setCellStyle(csDFBoldFour);
                cell.setCellType(CellType.NUMERIC);

                cell = row.createCell(10);
                cell.setCellValue((tTot));
                cell.setCellStyle(csDFBoldFour);
                cell.setCellType(CellType.NUMERIC);

                cell = row.createCell(11);
                cell.setCellValue((tToShare));
                cell.setCellStyle(csDFBoldFour);
                cell.setCellType(CellType.NUMERIC);
            }
            iRowNo = sheetPMS.getLastRowNum() + 2;

            CellStyle wrapText = workBook.createCellStyle();
            wrapText.setWrapText(true);

            headingBRSBookRow = sheetPMS.createRow(iRowNo);
            headingBRSBookRow.createCell(0).setCellValue("Profit Share");
            headingBRSBookRow.getCell(0).setCellStyle(csPlainNoBorder);

            iRowNo = sheetPMS.getLastRowNum() + 1;
            headingBRSBookRow = sheetPMS.createRow(iRowNo);
            headingBRSBookRow.setHeightInPoints(45);
            headingBRSBookRow.createCell(0).setCellValue("Client Name");
            headingBRSBookRow.getCell(0).setCellStyle(csHorVerCenter);

            headingBRSBookRow.createCell(1).setCellValue("Code");
            headingBRSBookRow.getCell(1).setCellStyle(csHorVerCenter);

            headingBRSBookRow.createCell(2).setCellValue("Profit Share Fee Income");
            headingBRSBookRow.getCell(2).setCellStyle(csHorVerCenter);

            headingBRSBookRow.createCell(3).setCellValue("%");
            headingBRSBookRow.getCell(3).setCellStyle(csHorVerCenter);

            headingBRSBookRow.createCell(4).setCellValue("Fee Payable");
            headingBRSBookRow.getCell(4).setCellStyle(csHorVerCenter);

            headingBRSBookRow.createCell(5).setCellValue("Date Of Amount Receipts ");
            headingBRSBookRow.getCell(5).setCellStyle(csHorVerCenter);

            headingBRSBookRow.createCell(7).setCellValue("PMS Strategy");
            headingBRSBookRow.getCell(7).setCellStyle(csHorVerCenter);

            iRowNo = sheetPMS.getLastRowNum() + 1;
            float pShare = 0f;
            float pBeyond = 0f;

            sheetPMS.setDefaultColumnWidth(12);
            sheetPMS.autoSizeColumn(0);
            sheetPMS.autoSizeColumn(6);
            sheetPMS.setColumnWidth(6, 0);

            reportGenerationExist = reportFees.get(1);
            List<ReportProfitShare> reportProfitShares = reportProfitShareRepository.findByReportGeneration(reportGenerationExist);

            if (reportProfitShares.isEmpty() != true) {

                for (ReportProfitShare bean : reportProfitShares) {
                    row = sheetPMS.createRow(iRowNo);
                    cell = row.createCell(0);
                    cell.setCellValue(bean.getClientName());
                    cell.setCellStyle(csPlanLeftRight);

                    cell = row.createCell(1);
                    cell.setCellType(CellType.NUMERIC);
                    cell.setCellStyle(csPlanLeftRight);
                    cell.setCellValue(Double.parseDouble(bean.getClientCode()));

                    cell = row.createCell(2);
                    cell.setCellValue(bean.getProfitFee());
                    cell.setCellType(CellType.NUMERIC);
                    cell.setCellStyle(csDF);

                    pShare += bean.getProfitFee();
                    cell = row.createCell(3);
                    cell.setCellValue(bean.getProfitComm() / 100);// + "%"
                    cell.setCellStyle(csPerc);
                    cell.setCellType(CellType.NUMERIC);

                    String sReceipts = "";
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

                    double rRound = (double) Math.round(bean.getFeePay());
                    cell = row.createCell(4);
                    cell.setCellValue(rRound);
                    cell.setCellStyle(csDF);
                    cell.setCellType(CellType.NUMERIC);

                    pBeyond += (float) rRound;

                    sReceipts = sdf.format(bean.getReceiptDate());

                    HSSFCell cellReceipt = row.createCell(5);
                    csRight.setAlignment(HorizontalAlignment.RIGHT);
                    csRightLeftRight = csRight;
                    csRightLeftRight.setBorderLeft(BorderStyle.THIN);
                    csRightLeftRight.setBorderRight(BorderStyle.THIN);
                    cellReceipt.setCellStyle(csRightLeftRight);
                    cellReceipt.setCellValue(sReceipts);

                    cell = row.createCell(7);
                    cell.setCellValue(bean.getStrategyName());
                    cell.setCellStyle(csRightLeftRight);

                    iRowNo++;

                }
            }
            row = sheetPMS.createRow(sheetPMS.getLastRowNum() + 1);
            row.createCell(0).setCellValue("TOTAL");
            row.getCell(0).setCellStyle(csFour);

            cell = row.createCell(1);
            cell.setCellValue("");
            cell.setCellStyle(csDFBoldFour);

            cell = row.createCell(2);
            cell.setCellValue(pShare);
            cell.setCellStyle(csDFBoldFour);
            cell.setCellType(CellType.NUMERIC);

            cell = row.createCell(3);
            cell.setCellValue("");
            cell.setCellStyle(csDFBoldFour);

            cell = row.createCell(4);
            cell.setCellValue((pBeyond));
            cell.setCellStyle(csDFBoldFour);
            cell.setCellType(CellType.NUMERIC);

            cell = row.createCell(5);
            cell.setCellValue("");
            cell.setCellStyle(csDFBoldFour);

            cell = row.createCell(6);
            cell.setCellValue("");
            cell.setCellStyle(csDFBoldFour);

            cell = row.createCell(7);
            cell.setCellValue("");
            cell.setCellStyle(csDFBoldFour);

            cumulativePmsAifBean.setProfitName(prop.getString("pms.profit.fee"));
            cumulativePmsAifBean.setProfitTotal(pBeyond);
            cumulativePmsAifBeans.add(cumulativePmsAifBean);

            try {
                DateFormat sDate = new SimpleDateFormat("yyyy-MM-dd");
                String sStartValidate = dateFormat.format(reportGeneration.getStartDate());
                String sEndValidate = dateFormat.format(reportGeneration.getToDate());

                Calendar beginCalendar = Calendar.getInstance();
                Calendar finishCalendar = Calendar.getInstance();

                try {
                    beginCalendar.setTime(dateFormat.parse(sStartValidate));
                    finishCalendar.setTime(dateFormat.parse(sEndValidate));

                } catch (ParseException e) {
                    e.printStackTrace();
                }


            } catch (Exception e) {
                System.out.println(e);
            }

            Calendar beginCal = Calendar.getInstance();
            Calendar finishCal = Calendar.getInstance();
            try {
                beginCal.setTime(dateFormat.parse(sStartTime));
                finishCal.setTime(dateFormat.parse(sEndTime));

            } catch (ParseException e) {
                e.printStackTrace();
            }
            int sq4 = 0;
            int sq4f = 0;
            int sq4j = 0;
            List<String> aifDate = new ArrayList<String>();
            while (!beginCal.after(finishCal)) {
                // add one month to date per loop
                String from = dateFormat.format(beginCal.getTime());
                String fStart = dateFormat.format(reportGeneration.getStartDate());
                String fEnd = dateFormat.format(reportGeneration.getToDate());
                aifDate.add(from);
                if (fStart.contains(prop.getString("fee.fourth.quarter.month3")) && fEnd.contains(prop.getString("fee.fourth.quarter.month"))) {
                    sq4f++;
                }
                if (fStart.contains(prop.getString("fee.fourth.quarter.month")) && fEnd.contains(prop.getString("fee.fourth.quarter.month"))) {
                    sq4++;
                }
                if (fStart.contains(prop.getString("fee.fourth.quarter.month1")) && fEnd.contains(prop.getString("fee.fourth.quarter.month"))) {
                    sq4j++;
                }
                beginCal.add(Calendar.MONTH, 1);
            }
            if (sq4 == 0 && sq4f == 0 && sq4j == 0)
                reportExistAIFGeneration(sStartTime, sEndTime, distributorMaster, sheetAIF, sFinal, cumulativePmsAifBeans, sheetSummary, reportFees,
                    reportGeneration, workBook, cumulativeAIFSeriesBCAD);
            if (sq4 != 0 && sq4f == 0 && sq4j == 0)
                reportAifQuarterFourGeneration(sStartTime, sEndTime, distributorMaster, sheetAIF, sFinal, cumulativePmsAifBeans, sheetSummary, reportGeneration, workBook, cumulativeAIFSeriesBCAD);
            if (sq4f != 0 && sq4j == 0 && reportGeneration.getAifCalculation().equals("Both"))
                reportAifQuarterFourFinancialGeneration(sStartTime, sEndTime, distributorMaster, sheetAIF, sFinal, cumulativePmsAifBeans, sheetSummary, workBook, reportGeneration, cumulativeAIFSeriesBCAD);
            if (sq4f != 0 && sq4j == 0 && reportGeneration.getAifCalculation().equals("Management")) {
                reportAifQuarterFourManageGeneration(sStartTime, sEndTime, distributorMaster, sheetAIF, sFinal, cumulativePmsAifBeans,
                    sheetSummary, workBook, reportGeneration, cumulativeAIFSeriesBCAD);
            }
            if (sq4f != 0 && sq4j == 0 && reportGeneration.getAifCalculation().equals("Performance")) {
                reportAifQuarterFourPerformanceGeneration(sStartTime, sEndTime, distributorMaster, sheetAIF, sFinal, cumulativePmsAifBeans,
                    sheetSummary, workBook, reportGeneration, cumulativeAIFSeriesBCAD);
            }

            if (sq4j != 0)
                reportAifQuarterFourPerGeneration(sStartTime, sEndTime, distributorMaster, sheetAIF, sFinal, cumulativePmsAifBeans, sheetSummary, workBook, reportGeneration, cumulativeAIFSeriesBCAD);


        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void reportExistAIFGeneration(String sStartTime, String sEndTime, DistributorMaster distributorMaster,
                                          HSSFSheet sheetAIF, String sFinal, List<CumulativePmsAifBean> cumulativePmsAifBeans, HSSFSheet sheetSummary, List<ReportGeneration> reportFees, ReportGeneration reportGeneration, HSSFWorkbook workBook, CumulativeAIFSeriesBCAD cumulativeAIFSeriesBCAD) {

        try {
            CumulativeDistributorReportBean cumulativeDistributorReportBean = null;
            BigDecimal closing, payableClosing;
            Float tempClosing, tempPayableClosing;
            Float pmsCommission, aifCommission;
            pmsCommission = prop.getFloat("fee.upfront.pms.commission");
            aifCommission = prop.getFloat("fee.upfront.aif.commission");

            sheetAIF.createFreezePane(0, 6);

            ReportGeneration reportGenerationExist;
            reportGenerationExist = reportFees.get(2);

            System.out.println("AIF report----" + reportGenerationExist);
            List<ReportAIFCalc> reportAIFCalcs = reportAIFCalcRepository.findByReportGeneration(reportGenerationExist);

            CumulativePmsAifBean cumulativePmsAifBean = new CumulativePmsAifBean();
            Float sTotal = 0.0f;
            HSSFFont fFont = workBook.createFont();

            fFont.setBold(true);
            fFont.setFontHeightInPoints((short) 11);
            fFont.setFontName("Calibri");

            HSSFFont defaultFont = workBook.createFont();
            defaultFont.setFontHeightInPoints((short) 11);
            defaultFont.setFontName("Calibri");


            HSSFFont fFontNoBold = workBook.createFont();
            fFontNoBold.setFontHeightInPoints((short) 11);
            fFontNoBold.setFontName("Calibri");

            CellStyle cs = workBook.createCellStyle();
            CellStyle csRight = workBook.createCellStyle();
            CellStyle csPlainNoBorder = workBook.createCellStyle();
            CellStyle csPlanLeftRight = workBook.createCellStyle();
            CellStyle csPlanLeftRightWrap = workBook.createCellStyle();
            CellStyle csMergedCellHeader = cs;

            CellStyle csSecond = workBook.createCellStyle();
            csSecond.setFont(fFontNoBold);
            csSecond.setBorderLeft(BorderStyle.THIN);
            csSecond.setBorderRight(BorderStyle.THIN);

            cs.setFont(fFont);

            CellStyle csMergedCellBody = workBook.createCellStyle();
            csMergedCellBody.setFont(fFontNoBold);
            csMergedCellBody.setBorderLeft(BorderStyle.THIN);
            csMergedCellBody.setBorderRight(BorderStyle.THIN);
            csMergedCellBody.setBorderTop(BorderStyle.NONE);
            csMergedCellBody.setBorderBottom(BorderStyle.NONE);
            csMergedCellBody.setWrapText(true);

            csPlainNoBorder.setFont(fFont);
            csPlainNoBorder.setBorderBottom(BorderStyle.NONE);
            csPlainNoBorder.setBorderTop(BorderStyle.NONE);
            csPlainNoBorder.setBorderLeft(BorderStyle.NONE);
            csPlainNoBorder.setBorderRight(BorderStyle.NONE);

            csPlanLeftRight.cloneStyleFrom(csPlainNoBorder);//HEre//cs
            csPlanLeftRightWrap.cloneStyleFrom(csPlanLeftRight);
            csPlanLeftRightWrap.setWrapText(true);

            csRight.setAlignment(HorizontalAlignment.RIGHT);

            CellStyle csHorVerCenter = workBook.createCellStyle();

            csRight.setAlignment(HorizontalAlignment.LEFT);
            // Format sheet
            csHorVerCenter.setAlignment(HorizontalAlignment.CENTER);
            csHorVerCenter.setVerticalAlignment(VerticalAlignment.CENTER);
            csHorVerCenter.setFont(fFont);
            csHorVerCenter.setWrapText(true);

            csMergedCellHeader.setAlignment(HorizontalAlignment.CENTER);
            csMergedCellHeader.setVerticalAlignment(VerticalAlignment.CENTER);

            csHorVerCenter.setBorderTop(BorderStyle.THIN);
            csHorVerCenter.setBorderBottom(BorderStyle.THIN);
            csHorVerCenter.setBorderLeft(BorderStyle.THIN);
            csHorVerCenter.setBorderRight(BorderStyle.THIN);

            csPlanLeftRight.setBorderLeft(BorderStyle.THIN);
            csPlanLeftRight.setBorderRight(BorderStyle.THIN);
            csPlanLeftRight.setFont(fFontNoBold);

            CellStyle csDF = StyleUtil.getStyleDataFormat(workBook);

            CellStyle csDFBold = StyleUtil.getStyleBoldDataFormat(workBook);

            CellStyle csPerc = StyleUtil.getStylePercDataFormat(workBook);

            csDF.setBorderLeft(BorderStyle.THIN);
            csDF.setBorderRight(BorderStyle.THIN);
            csDF.setFont(defaultFont);

            HSSFRow rowUserName = sheetAIF.createRow(sheetAIF.getLastRowNum() + 1);
            rowUserName.createCell(0).setCellValue(distributorMaster.getDistName().trim().toUpperCase());
            rowUserName.getCell(0).setCellStyle(csPlainNoBorder);

            HSSFRow durationFrom = sheetAIF.createRow(sheetAIF.getLastRowNum() + 1);
            durationFrom.createCell(0).setCellValue("FEES PAYABALE FOR " + sStartTime + " - " + sEndTime);
            durationFrom.getCell(0).setCellStyle(csPlainNoBorder);

            // Format sheet
            sheetAIF.addMergedRegion(new CellRangeAddress(1, 1, 0, 3));
            sheetAIF.addMergedRegion(new CellRangeAddress(2, 2, 0, 3));

            int iRowNo = sheetAIF.getLastRowNum() + 3;
            HSSFRow headingBRSBookRow = sheetAIF.createRow(iRowNo);
            headingBRSBookRow.setHeightInPoints(30);

            sheetAIF.addMergedRegion(new CellRangeAddress(5, 5, 0, 2));

            headingBRSBookRow.createCell(0).setCellValue(prop.getString("aif.cell.client_name"));
            headingBRSBookRow.getCell(0).setCellStyle(csMergedCellHeader);

            headingBRSBookRow.createCell(1).setCellValue("");
            headingBRSBookRow.createCell(2).setCellValue("");
            headingBRSBookRow.getCell(1).setCellStyle(csMergedCellHeader);
            headingBRSBookRow.getCell(2).setCellStyle(csMergedCellHeader);

            // sheet.autoSizeColumn(0);
            headingBRSBookRow.createCell(3).setCellValue(prop.getString("aif.cell.series.name"));
            headingBRSBookRow.getCell(3).setCellStyle(csHorVerCenter);
            // sheet.autoSizeColumn(1);
            headingBRSBookRow.createCell(4).setCellValue(prop.getString("aif.cell.manage.fee"));
            headingBRSBookRow.getCell(4).setCellStyle(csHorVerCenter);
            // sheet.autoSizeColumn(2);
            headingBRSBookRow.createCell(5).setCellValue(prop.getString("aif.cell.perform.fee"));
            headingBRSBookRow.getCell(5).setCellStyle(csHorVerCenter);
            // sheet.autoSizeColumn(3);
            headingBRSBookRow.createCell(6).setCellValue(prop.getString("aif.cell.sum.series"));
            headingBRSBookRow.getCell(6).setCellStyle(csHorVerCenter);
            // sheet.autoSizeColumn(4);
            headingBRSBookRow.createCell(7).setCellValue(prop.getString("aif.cell.tot.units"));
            headingBRSBookRow.getCell(7).setCellStyle(csHorVerCenter);
            // sheet.autoSizeColumn(5);
            headingBRSBookRow.createCell(8).setCellValue(prop.getString("aif.cell.manage.fee.charge"));
            headingBRSBookRow.getCell(8).setCellStyle(csHorVerCenter);
            // sheet.autoSizeColumn(6);
            headingBRSBookRow.createCell(9).setCellValue(prop.getString("aif.cell.total.comm"));
            headingBRSBookRow.getCell(9).setCellStyle(csHorVerCenter);
            // sheet.autoSizeColumn(7);

            iRowNo = sheetAIF.getLastRowNum() + 1;

            HSSFRow blankRow = sheetAIF.createRow(iRowNo);

            int iTotCell = 0;
            HSSFCell blankCell = null;

            CellStyle csPlainLeft = csPlanLeftRight;
            csPlainLeft.setBorderRight(BorderStyle.NONE);

            CellStyle csPlainRight = csPlanLeftRight;
            csPlainRight.setBorderLeft(BorderStyle.NONE);

            sheetAIF.addMergedRegion(new CellRangeAddress(iRowNo, iRowNo, 0, 2));
            CellStyle csPlainNoBottom = cs;
            csPlainNoBottom.setBorderBottom(BorderStyle.NONE);
            while (iTotCell < 9 || iTotCell == 9) {

                blankCell = blankRow.createCell(iTotCell);
                blankCell.setCellValue("");

                if (iTotCell != 0 && iTotCell != 1 && iTotCell != 2)
                    blankCell.setCellStyle(csPlainNoBottom);//cs
                iTotCell++;
            }

            iRowNo = sheetAIF.getLastRowNum() + 1;

            DecimalFormat decimalFormat = new DecimalFormat("#.0000");
            if (reportAIFCalcs.isEmpty() != true) {
                for (ReportAIFCalc bean : reportAIFCalcs) {
                    sheetAIF.addMergedRegion(new CellRangeAddress(iRowNo, iRowNo, 0, 2));
                    HSSFRow row = sheetAIF.createRow(iRowNo);

                    row.createCell(0).setCellValue(bean.getClientName());
                    row.getCell(0).setCellStyle(csMergedCellBody);//csPlanLeftRightWrap//csMergedCellBody

                    row.createCell(1).setCellValue("");

                    row.createCell(2).setCellValue("");

                    row.createCell(3).setCellValue(bean.getSeriesName());
                    row.getCell(3).setCellStyle(csSecond);//csPlanLeftRight

                    HSSFCell cell = row.createCell(4);
                    cell.setCellValue(bean.getManageFee());
                    cell.setCellType(CellType.NUMERIC);
                    cell.setCellStyle(csDF);

                    cell = row.createCell(5);
                    cell.setCellType(CellType.NUMERIC);
                    cell.setCellStyle(csDF);
                    cell.setCellValue(0);

                    cell = row.createCell(6);
                    cell.setCellType(CellType.NUMERIC);
                    cell.setCellStyle(csDF);
                    cell.setCellValue(bean.getTotSumOfSeriesUnits());

                    cell = row.createCell(7);
                    cell.setCellValue(bean.getTotSumOfIndivUnits());
                    cell.setCellType(CellType.NUMERIC);
                    cell.setCellStyle(csDF);

                    cell = row.createCell(8);
                    cell.setCellValue(bean.getMngFeeCharged());
                    cell.setCellType(CellType.NUMERIC);
                    cell.setCellStyle(csDF);

                    cell = row.createCell(9);
                    cell.setCellValue(bean.getCommDue());
                    cell.setCellType(CellType.NUMERIC);
                    cell.setCellStyle(csDF);

                    sTotal += bean.getCommDue();
                    iRowNo++;
                }
            }
            String aifTotal = "";
            int iCurrRow = sheetAIF.getLastRowNum() + 1;
            HSSFRow row = sheetAIF.createRow(iCurrRow);

            sheetAIF.addMergedRegion(new CellRangeAddress(iCurrRow, iCurrRow, 0, 2));
            CellStyle csFour = cs;
            CellStyle csDFBoldFour = csDFBold;

            csFour.setBorderBottom(BorderStyle.THIN);
            csFour.setBorderTop(BorderStyle.THIN);
            csFour.setBorderLeft(BorderStyle.THIN);
            csFour.setBorderRight(BorderStyle.THIN);

            csDFBoldFour.setBorderBottom(BorderStyle.THIN);
            csDFBoldFour.setBorderTop(BorderStyle.THIN);
            csDFBoldFour.setBorderLeft(BorderStyle.THIN);
            csDFBoldFour.setBorderRight(BorderStyle.THIN);


            HSSFCell totCell = null;
            iTotCell = 0;
            while (iTotCell < 7 || iTotCell == 7) {
                totCell = row.createCell(iTotCell);
                totCell.setCellValue("");
                totCell.setCellStyle(csDFBoldFour);
                iTotCell++;
            }

            HSSFCell cell = row.createCell(8);
            cell.setCellValue("TOTAL");
            cell.setCellStyle(csFour);

            aifTotal = decimalFormat.format(sTotal);
            cell = row.createCell(9);
            cell.setCellValue((sTotal));
            cell.setCellStyle(csDFBoldFour);

            sheetAIF.setDefaultColumnWidth(18);

            cumulativePmsAifBean.setAifName(prop.getString("aif.total.fee"));

            cumulativePmsAifBean.setAifTotal(aifTotal);
            cumulativePmsAifBeans.add(cumulativePmsAifBean);

            Double totalTrail = 0.0;
            Double totalUpfront = 0.0;
            Double totalClosing = 0.0;
            Double payable = 0.0;
            Double currentPayable = 0d;
            if (distributorMaster.getDistModelType().equals("Upfront")) {
                cumulativeDistributorReportBean = feeTrailUpfrontTransService
                    .getDistributorCurrentReport(distributorMaster, pmsCommission, reportGeneration.getStartDate(), reportGeneration.getToDate());

                closing = cumulativeDistributorReportBean.getCurrentUpfrontAmt()
                    .add(cumulativeDistributorReportBean.getCurrentTrailAmt());
                tempClosing = Float.valueOf(cumulativeDistributorReportBean.getCurrentUpfrontAmt().floatValue())
                    + Float.valueOf(cumulativeDistributorReportBean.getCurrentTrailAmt().floatValue());
                totalTrail = feeTrailUpfrontTransRepository.getTotalTrail(distributorMaster.getId(), reportGeneration.getToDate());
                totalUpfront = feeTrailUpfrontTransRepository.getTotalUpfront(distributorMaster.getId(), reportGeneration.getToDate());
                totalTrail=(totalTrail==null)?0f:totalTrail;
                totalUpfront=(totalUpfront==null)?0f:totalUpfront;
                totalClosing = totalUpfront + totalTrail;
                payable = feeTrailUpfrontTransRepository.getTotalPayable(distributorMaster.getId(), reportGeneration.getToDate());
                Double paid = feeTrailUpfrontTransRepository.getTotalPaid(distributorMaster.getId(), reportGeneration.getToDate());
                payable=(payable==null)?0f:payable;
                paid=(paid==null)?0f:paid;
                if (totalClosing > 0) {
                    currentPayable = (totalClosing + payable) - paid;
                } else {
                    currentPayable = payable - paid;
                }
                BigDecimal totalPayable = cumulativeDistributorReportBean.getCurrentTotalPayableAmt()
                    .subtract(cumulativeDistributorReportBean.getCurrentPaidAmount());
            }
            summarySheetService.cumulativeFee(cumulativePmsAifBeans, sStartTime, sEndTime, distributorMaster.getDistName().trim(),
                sheetSummary, sFinal, cumulativeDistributorReportBean, totalClosing, currentPayable, totalTrail,
                totalUpfront, distributorMaster, reportGeneration, workBook, cumulativeAIFSeriesBCAD);

            // workBook.write(fos);

        } catch (Exception e) {
            e.printStackTrace();
        }


    }


    private Date getPreviousMonth(Date sDate) {
        Calendar calendar = null;

        calendar = Calendar.getInstance();
        calendar.setTime(sDate);
        calendar.add(Calendar.MONTH, -1);
        Date startDate = calendar.getTime();
        return startDate;

    }

    private Integer getYear(Date startDate) {
        Calendar calendar = null;

        calendar = Calendar.getInstance();
        calendar.setTime(startDate);
        return calendar.get(Calendar.YEAR);

    }

    private Integer monthCalculate(Date startDate) {
        Calendar calendar = null;

        calendar = Calendar.getInstance();
        calendar.setTime(startDate);
        return calendar.get(Calendar.MONTH) + 1;
    }

    private String formatMonth(Integer month) {
        DateFormat formatter = new SimpleDateFormat("MMM");
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.MONTH, month - 1);
        // System.out.println(formatMonth);
        return formatter.format(calendar.getTime());
    }

    private String previousYear(Date endDate) {
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();
        cal.setTime(endDate);
        cal.add(Calendar.YEAR, -1);
        cal.add(Calendar.MONTH, 1);
        return formatter.format(cal.getTime());
    }

    private AIFManagePerMonth getParticularSeriesFee(SeriesMaster seriesMaster, Date start, Date startDate, Date sStart) {

        AIFManagePerMonth aifManagePerMonth;
        String month, year;
        DateFormat monthformat, yearformat;

        monthformat = new SimpleDateFormat("MM");
        yearformat = new SimpleDateFormat("yyyy");
        month = monthformat.format(start);
        year = yearformat.format(start);
        aifManagePerMonth = aifManagePerMonthRepository.getFeeSeriesViceAsOn(seriesMaster.getId(), Integer.valueOf(year),
            Integer.valueOf(month));

        return aifManagePerMonth;

    }

    public float roundOff(float value) {
        BigDecimal decimal = new BigDecimal(value);
        float roundOffVale = decimal.setScale(4, BigDecimal.ROUND_HALF_UP).floatValue();
        return roundOffVale;
    }


}
