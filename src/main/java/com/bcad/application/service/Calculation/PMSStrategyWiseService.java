package com.bcad.application.service.Calculation;

import com.bcad.application.bean.PMSStrategyBean;
import com.bcad.application.config.PropertiesUtils;
import com.bcad.application.domain.*;
import com.bcad.application.repository.*;
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
import javax.persistence.Query;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
//@Transactional(noRollbackFor = Exception.class)
public class PMSStrategyWiseService {

    List<DistributorPMSBean> distributorPMSBeans = new ArrayList<DistributorPMSBean>();
    List<CumulativePmsAifBean> cumulativePmsAifBeans = new ArrayList<CumulativePmsAifBean>();

    private final ReportGenerationRepository reportGenerationRepository;
    private final ProfitShareRepository profitShareRepository;
    private final PMSNavRepository pmsNavRepository;
    private final DistributorMasterRepository distributorMasterRepository;
    private final PMSClientMasterRepository pmsClientMasterRepository;
    private final ReportBrokeragePMSRepository reportBrokeragePMSRepository;
    private final ReportProfitShareRepository reportProfitShareRepository;
    private final CommissionDefinitionRepository commissionDefinitionRepository;
    private final ProductRepository productRepository;
    private final InvestmentMasterRepository investmentMasterRepository;
    private final PMSAndAIFReportService pmsAndAIFReportService;
    private final ClientFeeCommissionRepository clientFeeCommissionRepository;

    public PMSStrategyWiseService(ReportGenerationRepository reportGenerationRepository, ProfitShareRepository profitShareRepository, PMSNavRepository pmsNavRepository,
                                  DistributorMasterRepository distributorMasterRepository, PMSClientMasterRepository pmsClientMasterRepository,
                                  ReportBrokeragePMSRepository reportBrokeragePMSRepository, ReportProfitShareRepository reportProfitShareRepository,
                                  CommissionDefinitionRepository commissionDefinitionRepository, ProductRepository productRepository,
                                  InvestmentMasterRepository investmentMasterRepository, PMSAndAIFReportService pmsAndAIFReportService,ClientFeeCommissionRepository clientFeeCommissionRepository) {
        this.reportGenerationRepository = reportGenerationRepository;
        this.profitShareRepository = profitShareRepository;
        this.pmsNavRepository = pmsNavRepository;
        this.distributorMasterRepository = distributorMasterRepository;
        this.pmsClientMasterRepository = pmsClientMasterRepository;
        this.reportBrokeragePMSRepository = reportBrokeragePMSRepository;
        this.reportProfitShareRepository = reportProfitShareRepository;
        this.commissionDefinitionRepository = commissionDefinitionRepository;
        this.productRepository = productRepository;
        this.investmentMasterRepository = investmentMasterRepository;
        this.pmsAndAIFReportService = pmsAndAIFReportService;
        this.clientFeeCommissionRepository=clientFeeCommissionRepository;
    }

    @PersistenceContext
    EntityManager entityManager;

    PropertiesConfiguration prop = new PropertiesUtils().getProperties("application.properties");
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    DateFormat firstDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");

    private final Logger log = LoggerFactory.getLogger(PMSAndAIFReportService.class);

    public void generateDistributorReports(DistributorMaster dm, ReportGeneration reportGeneration, HSSFSheet sheetSummary, HSSFSheet sheetPMS, HSSFSheet sheetAIF,
                                           String sStartTime, String sEndTime, HSSFWorkbook workBook, CumulativeAIFSeriesBCAD cumulativeAIFSeriesBCAD) throws ParseException {


        String sStartDate = sdf.format(reportGeneration.getStartDate());
        String sToDate = sdf.format(reportGeneration.getToDate());
        List<ReportGeneration> reportFees = reportGenerationRepository.findDistributorReport(0, sStartDate, sToDate,
            dm.getId());

        int sFlag=0;

        DistributorMaster distributorMaster = distributorMasterRepository.findByDistributorName(dm.getDistName());
        Product productBCAD=productRepository.findByProductName("BCAD");

        List<Long> productIds=pmsNavRepository.getDistributorStrategy(dm.getId(),sStartDate,sToDate);

        if(productIds==null || productIds.size()==0)
            productIds.add(0,0L);
        if(productIds.contains((Integer)1)){
            productIds.remove(0);
        sFlag=1;}
        List<CommissionDefinition> commissionDefinition = commissionDefinitionRepository.getPMSStrategyCommission(productIds, dm.getId());
        if(sFlag==1) {
            List<CommissionDefinition> commissionDefinitionBCAD = commissionDefinitionRepository.getPMSOption3StrategyCommission(productBCAD.getId(), dm.getId());
            if(commissionDefinitionBCAD!=null)
                commissionDefinition.addAll(commissionDefinitionBCAD);
        }

        List<PMSStrategyBean> pmsStrategyBeans=new ArrayList<>();
            distributorPMSBeans = new ArrayList<DistributorPMSBean>();
         cumulativePmsAifBeans = new ArrayList<CumulativePmsAifBean>();

        if (reportFees.size() == 0) {
            distributorPMSBeans = new ArrayList<DistributorPMSBean>();
            cumulativePmsAifBeans = new ArrayList<CumulativePmsAifBean>();
            List<PMSClientMaster> pmsClientMasters = pmsClientMasterRepository.findByDistributorMaster(dm);
            PMSStrategyBean pmsStrategyBean;

            for (PMSClientMaster pmsClientMaster : pmsClientMasters) {
                     DistributorPMSBean bean = null;
                List<PMSNav> pmsNavs=getAllCodeSchemen(pmsClientMaster.getId(),sStartDate,sToDate);
                if(pmsNavs.size()!=0) {
                    pmsStrategyBean = new PMSStrategyBean();
                    pmsStrategyBean.setClientCode(pmsClientMaster.getClientCode());
                    pmsStrategyBean.setClientName(pmsClientMaster.getClientName());
                    Float pmsNav=0.0f;
                    Float distShare=0.0f;
                   for(PMSNav pms: pmsNavs) {
                       bean = new DistributorPMSBean();
                       bean.setClientCode(pmsClientMaster.getClientCode());
                       bean.setClientName(pmsClientMaster.getClientName());
                       bean.setClientCodeScheme(pms.getCodeScheme());
                       pmsNav+=pms.getCalcPmsNav();
                       bean.setPmsNavFee(pms.getCalcPmsNav());
                       bean.setStrategyName(pms.getInvestmentMaster().getInvestmentName());
                      // Integer pmsInvest = pms.getPmsClientMaster().getSlab().equals("OLD") ?0 :1;
                       //Integer pmsInvest = (firstDateFormat.parse(prop.getString("pms.strategy.date")).after(pms.getPmsClientMaster().getAccountOpenDate())) ? 0: 1;
                       Product productId=productRepository.findByProductName(pms.getInvestmentMaster().getInvestmentName());
/* Recently Commented  */
                     /*  CommissionDefinition commissionDefinition1=new CommissionDefinition();
                      if(!productId.getProductName().equals("BCAD"))
                     commissionDefinition1 = commissionDefinitionRepository.getPMSInvestmentDateCalc(dm.getId(),pmsInvest,productId.getId());
                    else
                          commissionDefinition1 = commissionDefinitionRepository.getBCADInvestmentDateCalc(dm.getId(),pmsInvest,productId.getId());
                     */
                       ClientFeeCommission clientFeeCommission = new ClientFeeCommission();

                       clientFeeCommission = clientFeeCommissionRepository.findByPmsClientMaster(pmsClientMaster);


                       //Float sDistShare= (pms.getCalcPmsNav()*clientFeeCommission.getNavComm())/100;
                       Float sDistShare= (pms.getCalcPmsNav()*clientFeeCommission.getNavComm())/100;


                       bean.setPmsDistShare(sDistShare);
                       distShare+=sDistShare;
                       distributorPMSBeans.add(bean);
                   }
                    pmsStrategyBean.setPmsNavFee(pmsNav);
                    pmsStrategyBean.setDistShare(distShare);
                    pmsStrategyBeans.add(pmsStrategyBean);
                }
            }
            writePMSStrategyWise(distributorPMSBeans, dm, cumulativePmsAifBeans, sheetSummary, sheetPMS, sheetAIF, reportGeneration, workBook,cumulativeAIFSeriesBCAD,pmsStrategyBeans);

        }
        if (reportFees.isEmpty() == false &&  !distributorMaster.getDistModelType().equals("")) {
            cumulativePmsAifBeans = new ArrayList<CumulativePmsAifBean>();
            System.out.println(reportFees.size());
           writeExistReportPMS(reportFees, cumulativePmsAifBeans, distributorMaster, sheetSummary, sheetPMS, sheetAIF, reportGeneration, workBook, commissionDefinition, cumulativeAIFSeriesBCAD);
        }


    }



    public List<PMSNav> getAllCodeSchemen(Long clientId,String startDate,String endDate) throws ParseException {
        List<PMSNav> pmsNavList = new ArrayList<>();
        Query query=entityManager.createNativeQuery("select client_code_scheme ,sum(calc_pms_nav),investment_id,investment_date from fee_pms_nav "+
            " where pms_client_id=?1 and start_date between ?2 and ?3 and int_deleted=0 group by client_code_scheme,investment_id,investment_date order by client_code_scheme asc");
        query.setParameter("1", clientId);
        query.setParameter("2", startDate);
        query.setParameter("3", endDate);

        List<?> result = query.getResultList();

        for(int i=0; i<result.size(); i++) {
            Object[] row = (Object[]) result.get(i);

            PMSNav pmsNav=new PMSNav();
            pmsNav.setCodeScheme(row[0].toString());
            String noOf=row[1].toString();
            pmsNav.setCalcPmsNav(Float.parseFloat(noOf));
            System.out.println(row[0].toString()+"-----"+row[2].toString());
            Optional<InvestmentMaster> investmentMaster =investmentMasterRepository.findById(Long.valueOf((row[2].toString())));
            pmsNav.setInvestmentMaster(investmentMaster.get());
            PMSClientMaster pmsClientMaster=pmsClientMasterRepository.findByClientCode(row[0].toString());
            //pmsNav.setInvestmentDate(sdf.parse(row[3].toString()));
            System.out.println(pmsClientMaster.getAccountOpenDate());
            pmsNav.setInvestmentDate(pmsClientMaster.getAccountOpenDate());
            pmsNavList.add(pmsNav);
        }
        return pmsNavList;
    }

    private void writePMSStrategyWise(List<DistributorPMSBean> distributorPMSBeans, DistributorMaster distributorMaster, List<CumulativePmsAifBean> cumulativePmsAifBeans, HSSFSheet sheetSummary,
                                      HSSFSheet sheetPMS, HSSFSheet sheetAIF, ReportGeneration reportGeneration, HSSFWorkbook workBook, CumulativeAIFSeriesBCAD cumulativeAIFSeriesBCAD, List<PMSStrategyBean> pmsStrategyBeans) {

        ReportBrokeragePMS reportBrokeragePMS;

        CumulativePmsAifBean cumulativePmsAifBean = new CumulativePmsAifBean();

        DateFormat dateFormat = new SimpleDateFormat("MMM yyyy");
        DateFormat dateFormatp = new SimpleDateFormat("yyyy-MM-dd");
        String sStartTime = dateFormat.format(reportGeneration.getStartDate());
        String sEndTime = dateFormat.format(reportGeneration.getToDate());
        String pStartTime = dateFormat.format(reportGeneration.getStartDate());
        String pEndTime = dateFormat.format(reportGeneration.getToDate());
        String sFinal = sStartTime + "_to_" + sEndTime;

        ReportGeneration reportFeeBrokerage = new ReportGeneration();
        reportFeeBrokerage.setStartDate(reportGeneration.getStartDate());
        reportFeeBrokerage.setToDate(reportGeneration.getToDate());
        reportFeeBrokerage.setDetailsUpdatedFlag(0);
        reportFeeBrokerage.setDistributorMaster1(distributorMaster);
        reportFeeBrokerage.setReportType("Brokerage PMS");
        reportFeeBrokerage = reportGenerationRepository.save(reportFeeBrokerage);

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

        csDF.setFont(defaultFont);
        csPlain.setFont(defaultFont);

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


        sheetPMS.createFreezePane(0, 6);

        int iRowNo = sheetPMS.getLastRowNum() + 3;
        HSSFRow headingBRSBookRow = sheetPMS.createRow(iRowNo);
        // Format sheet
        headingBRSBookRow.setHeightInPoints(30);
        HSSFCell cellTemp = headingBRSBookRow.createCell(0);

        cellTemp.setCellValue(prop.getString("pms.cell.client_name"));
        cellTemp.setCellStyle(csHorVerCenter);

        headingBRSBookRow.createCell(1).setCellValue(prop.getString("pms.cell.client_code"));
        headingBRSBookRow.getCell(1).setCellStyle(csHorVerCenter);

        headingBRSBookRow.createCell(2).setCellValue(prop.getString("pms.cell.pms"));
        headingBRSBookRow.getCell(2).setCellStyle(csHorVerCenter);

        headingBRSBookRow.createCell(3).setCellValue(prop.getString("pms.cell.net_payable"));
        headingBRSBookRow.getCell(3).setCellStyle(csHorVerCenter);

        iRowNo = sheetPMS.getLastRowNum() + 1;

        HSSFRow blankRow = sheetPMS.createRow(iRowNo);

        int iTotCell = 0;
        HSSFCell blankCell = null;

        int iMaxAllowedCell = 3;

        while (iTotCell < iMaxAllowedCell || iTotCell == iMaxAllowedCell) {
            blankCell = blankRow.createCell(iTotCell);
            blankCell.setCellValue("");
            blankCell.setCellStyle(csPlanLeftRight);
            iTotCell++;
        }

        iRowNo = sheetPMS.getLastRowNum() + 1;

        float sAdv = 0f;
        Float fTotFee = 0f;
        DecimalFormat decimalFormat = new DecimalFormat("#.0000");
        for (DistributorPMSBean bean : distributorPMSBeans) {

            reportBrokeragePMS = new ReportBrokeragePMS();
            HSSFRow row = sheetPMS.createRow(iRowNo);
            HSSFCell cell = row.createCell(0);
            cell.setCellValue(bean.getClientName());
            cell.setCellStyle(csPlanLeftRight);

            cell = row.createCell(1);
           // cell.setCellType(CellType.NUMERIC);
            cell.setCellValue(bean.getClientCodeScheme());
            cell.setCellStyle(csPlanLeftRight);

              cell = row.createCell(2);
              cell.setCellValue(bean.getPmsNavFee());
              cell.setCellType(CellType.NUMERIC);
              cell.setCellStyle(csDF);

              Float dTotal = bean.getPmsNavFee();
              fTotFee += dTotal;

              double rRounds = 0.0;

              rRounds = (double) Math.round(bean.getPmsDistShare());
              sAdv += (float) rRounds;

              cell = row.createCell(3);
              cell.setCellValue(rRounds);
              cell.setCellType(CellType.NUMERIC);
              cell.setCellStyle(csDF);

                reportBrokeragePMS.setClientName(bean.getClientName());
                reportBrokeragePMS.setClientCode(bean.getClientCodeScheme());
                reportBrokeragePMS.setDistributorMaster(distributorMaster);
                reportBrokeragePMS.setPmsFee(Float.valueOf(bean.getPmsNavFee()));
                reportBrokeragePMS.setAdvisoryPay((float) rRounds);
                reportBrokeragePMS.setReportGeneration(reportFeeBrokerage);
                reportBrokeragePMSRepository.save(reportBrokeragePMS);

                iRowNo++;



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
        cell.setCellValue((fTotFee));
        cell.setCellType(CellType.NUMERIC);
        cell.setCellStyle(csDFBoldFour);

        cell = row.createCell(3);
        cell.setCellValue((sAdv));//sMarkup
        cell.setCellType(CellType.NUMERIC);
        cell.setCellStyle(csDFBoldFour);

        cumulativePmsAifBean.setPmsName(prop.getString("pms.adv.fee"));
        cumulativePmsAifBean.setPmsTotal(sAdv);

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

        iRowNo = sheetPMS.getLastRowNum() + 1;
        headingBRSBookRow = sheetPMS.createRow(iRowNo);
        headingBRSBookRow.setHeightInPoints(45);//40
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
/* Recently Commented  */
                    Integer pmsInvest = profitShare.getPmsClientMaster().getSlab().equals("OLD") ?0 :1;
                // Integer pmsInvest = (firstDateFormat.parse(prop.getString("pms.strategy.date")).after(profitShare.getInvestmentDate())) ? 0: 1;
                    Product productId=productRepository.findByProductName(profitShare.getInvestmentMaster().getInvestmentName());
/* Recently Commented  */
                 // CommissionDefinition commissionDefinition1 = commissionDefinitionRepository.getPMSInvestmentDateCalc(distributorMaster.getId(),pmsInvest,productId.getId());
                    ClientFeeCommission clientFeeCommission = new ClientFeeCommission();
                    clientFeeCommission = clientFeeCommissionRepository.findByPmsClientMaster(profitShare.getPmsClientMaster());

                    cell = row.createCell(0);
                    cell.setCellValue(profitShare.getPmsClientMaster().getClientName());
                    cell.setCellStyle(csPlanLeftRight);

                    reportProfitShare.setClientName(profitShare.getPmsClientMaster().getClientName());

                    cell = row.createCell(1);
                    cell.setCellType(CellType.NUMERIC);
                    cell.setCellStyle(csPlanLeftRight);
                    cell.setCellValue(profitShare.getPmsClientMaster().getClientCode());
                    reportProfitShare.setClientCode(profitShare.getPmsClientMaster().getClientCode());

                    cell = row.createCell(2);

                    cell.setCellValue(income);
                    reportProfitShare.setProfitFee(income.floatValue());
                    cell.setCellType(CellType.NUMERIC);
                    cell.setCellStyle(csDF);

                    pShare += profitShare.getProfitShareIncome();
                    cell = row.createCell(3);
/* Recently Commented  */
                    cell.setCellValue(clientFeeCommission.getProfitComm() / 100);// +
                    //"%"
                   reportProfitShare.setProfitComm(clientFeeCommission.getProfitComm());
                 //   cell.setCellValue(clientFeeCommission.getProfitComm() / 100);
                    reportProfitShare.setProfitComm(clientFeeCommission.getProfitComm());
                    cell.setCellType(CellType.NUMERIC);
                    cell.setCellStyle(csPerc);

                    Float x = income.floatValue();

                    String sReceipts = "";

                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
/* Recently Commented  */
                      Float y = clientFeeCommission.getProfitComm();
                      //  Float y = clientFeeCommission.getProfitComm();
                        Float num = (float) ((x * y) / 100);
                        double rRound = (double) Math.round(num);

                        cell = row.createCell(4);
                        cell.setCellValue(rRound);
                        reportProfitShare.setFeePay((float) rRound);
                        cell.setCellType(CellType.NUMERIC);
                        cell.setCellStyle(csDF);

                        pBeyond += (float) rRound;

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

                        // One Blank Column
                        cell = row.createCell(7);
                        cell.setCellValue(profitShare.getInvestmentMaster().getInvestmentName());
                        cell.setCellStyle(csRightLeftRight);

                        reportProfitShare.setStrategyName(profitShare.getInvestmentMaster().getInvestmentName());
                        reportProfitShare.setReportGeneration(reportGenerationProfit);
                        reportProfitShare.setDistributorMaster(distributorMaster);
                        reportProfitShareRepository.save(reportProfitShare);
                        iRowNo++;

                }
            }
        } catch (Exception e) {
            System.out.println(e);
        }

        // }
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

        cell = row.createCell(7);
        cell.setCellValue("");
        cell.setCellStyle(csDFBoldFour);


        cumulativePmsAifBean.setProfitName(prop.getString("pms.profit.fee"));
        cumulativePmsAifBean.setProfitTotal(pBeyond);
        cumulativePmsAifBeans.add(cumulativePmsAifBean);

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
        pmsAndAIFReportService.aifFeeCalculation(distributorMaster, sheetAIF, sFinal, cumulativePmsAifBeans, sheetSummary, aifDate, sq4, sq4f, sq4j, reportGeneration, workBook, cumulativeAIFSeriesBCAD);
    }

    private void writeExistReportPMS(List<ReportGeneration> reportFees, List<CumulativePmsAifBean> cumulativePmsAifBeans, DistributorMaster distributorMaster, HSSFSheet sheetSummary, HSSFSheet sheetPMS, HSSFSheet sheetAIF, ReportGeneration reportGeneration, HSSFWorkbook workBook, List<CommissionDefinition> commissionDefinition, CumulativeAIFSeriesBCAD cumulativeAIFSeriesBCAD) {

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

            int sCount = 0;

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

            sheetPMS.createFreezePane(0, 6);

            int iRowNo = sheetPMS.getLastRowNum() + 3;
            HSSFRow headingBRSBookRow = sheetPMS.createRow(iRowNo);
            // Format sheet
            headingBRSBookRow.setHeightInPoints(30);
            HSSFCell cellTemp = headingBRSBookRow.createCell(0);

            cellTemp.setCellValue(prop.getString("pms.cell.client_name"));
            cellTemp.setCellStyle(csHorVerCenter);

            headingBRSBookRow.createCell(1).setCellValue(prop.getString("pms.cell.client_code"));
            headingBRSBookRow.getCell(1).setCellStyle(csHorVerCenter);

            headingBRSBookRow.createCell(2).setCellValue(prop.getString("pms.cell.pms"));
            headingBRSBookRow.getCell(2).setCellStyle(csHorVerCenter);

            headingBRSBookRow.createCell(3).setCellValue(prop.getString("pms.cell.net_payable"));
            headingBRSBookRow.getCell(3).setCellStyle(csHorVerCenter);

            iRowNo = sheetPMS.getLastRowNum() + 1;
            HSSFRow blankRow = sheetPMS.createRow(iRowNo);

            int iTotCell = 0;
            HSSFCell blankCell = null;

            int iMaxAllowedCell = 3;

            while (iTotCell < iMaxAllowedCell || iTotCell == iMaxAllowedCell) {
                blankCell = blankRow.createCell(iTotCell);
                blankCell.setCellValue("");
                blankCell.setCellStyle(csPlanLeftRight);
                iTotCell++;
            }

            iRowNo = sheetPMS.getLastRowNum() + 1;

            float sPms = 0f;
            float sAdv = 0f;

            List<ReportBrokeragePMS> reportBrokeragePMSs = reportBrokeragePMSRepository.findByReportGeneration(reportGenerationExist);
            DecimalFormat decimalFormat = new DecimalFormat("#.0000");
            for (ReportBrokeragePMS bean : reportBrokeragePMSs) {
                HSSFRow row = sheetPMS.createRow(iRowNo);

                HSSFCell cell = row.createCell(0);
                cell.setCellValue(bean.getClientName());
                cell.setCellStyle(csPlanLeftRight);

                cell = row.createCell(1);
               // cell.setCellType(CellType.NUMERIC);
                cell.setCellValue(bean.getClientCode());
                cell.setCellStyle(csPlanLeftRight);

                cell = row.createCell(2);
                cell.setCellValue(bean.getPmsFee());
                cell.setCellType(CellType.NUMERIC);
                cell.setCellStyle(csDF);
                // cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
                sPms += bean.getPmsFee();

                double rRounds = 0.0;
                System.out.println(bean.getPmsFee());

                rRounds = (double) Math.round(bean.getAdvisoryPay());
                sAdv += (float) rRounds;

                cell = row.createCell(3);
                cell.setCellValue(rRounds);
                cell.setCellType(CellType.NUMERIC);
                cell.setCellStyle(csDF);

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
            cell.setCellValue((sPms));
            cell.setCellType(CellType.NUMERIC);
            cell.setCellStyle(csDFBoldFour);

            cell = row.createCell(3);
            cell.setCellValue((sAdv));//sMarkup
            cell.setCellType(CellType.NUMERIC);
            cell.setCellStyle(csDFBoldFour);

            cumulativePmsAifBean.setPmsName(prop.getString("pms.adv.fee"));
            cumulativePmsAifBean.setPmsTotal(sAdv);

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
                pmsAndAIFReportService.reportExistAIFGeneration(sStartTime, sEndTime, distributorMaster, sheetAIF, sFinal, cumulativePmsAifBeans, sheetSummary, reportFees,
                    reportGeneration, workBook, cumulativeAIFSeriesBCAD);
            if (sq4 != 0 && sq4f == 0 && sq4j == 0)
                pmsAndAIFReportService.reportAifQuarterFourGeneration(sStartTime, sEndTime, distributorMaster, sheetAIF, sFinal, cumulativePmsAifBeans, sheetSummary, reportGeneration, workBook, cumulativeAIFSeriesBCAD);
            if (sq4f != 0 && sq4j == 0 && reportGeneration.getAifCalculation().equals("Both"))
                pmsAndAIFReportService.reportAifQuarterFourFinancialGeneration(sStartTime, sEndTime, distributorMaster, sheetAIF, sFinal, cumulativePmsAifBeans, sheetSummary, workBook, reportGeneration, cumulativeAIFSeriesBCAD);
            if (sq4f != 0 && sq4j == 0 && reportGeneration.getAifCalculation().equals("Management")) {
                pmsAndAIFReportService.reportAifQuarterFourManageGeneration(sStartTime, sEndTime, distributorMaster, sheetAIF, sFinal, cumulativePmsAifBeans,
                    sheetSummary, workBook, reportGeneration, cumulativeAIFSeriesBCAD);
            }
            if (sq4f != 0 && sq4j == 0 && reportGeneration.getAifCalculation().equals("Performance")) {
                pmsAndAIFReportService.reportAifQuarterFourPerformanceGeneration(sStartTime, sEndTime, distributorMaster, sheetAIF, sFinal, cumulativePmsAifBeans,
                    sheetSummary, workBook, reportGeneration, cumulativeAIFSeriesBCAD);
            }

            if (sq4j != 0)
                pmsAndAIFReportService.reportAifQuarterFourPerGeneration(sStartTime, sEndTime, distributorMaster, sheetAIF, sFinal, cumulativePmsAifBeans, sheetSummary, workBook, reportGeneration, cumulativeAIFSeriesBCAD);


        } catch (Exception e) {
            e.printStackTrace();
        }

    }


}
