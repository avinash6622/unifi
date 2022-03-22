package com.bcad.application.service.Calculation;

import com.bcad.application.config.PropertiesUtils;
import com.bcad.application.domain.*;
import com.bcad.application.repository.*;
import com.bcad.application.web.rest.util.MathUtil;
import com.bcad.application.web.rest.util.StyleUtil;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFRegionUtil;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class CumulativeFeeService {

    private final ReportsDistributorFeeRepository reportsDistributorFeeRepository;
    private final FeeTrailUpfrontTransRepository feeTrailUpfrontTransRepository;
    private final BCADDistributorFeeRepository bcadDistributorFeeRepository;
    private final GenericPayTrailUpfrontRepository genericPayTrailUpfrontRepository;
    private final ProductRepository productRepository;
    private final AIFDistributorFeeRepository aifDistributorFeeRepository;
    //private final ReportBcadMonthlyCalculationRepository reportBcadMonthlyCalculationRepository;

    List<NewCumulativeFee> cumulativeFees= new ArrayList<>();

    public CumulativeFeeService(ReportsDistributorFeeRepository reportsDistributorFeeRepository, FeeTrailUpfrontTransRepository feeTrailUpfrontTransRepository,
                                BCADDistributorFeeRepository bcadDistributorFeeRepository,GenericPayTrailUpfrontRepository genericPayTrailUpfrontRepository,
                                ProductRepository productRepository, AIFDistributorFeeRepository aifDistributorFeeRepository) {
        this.reportsDistributorFeeRepository = reportsDistributorFeeRepository;
        this.feeTrailUpfrontTransRepository = feeTrailUpfrontTransRepository;
        this.bcadDistributorFeeRepository = bcadDistributorFeeRepository;
        this.genericPayTrailUpfrontRepository= genericPayTrailUpfrontRepository;
        this.productRepository=productRepository;
        this.aifDistributorFeeRepository= aifDistributorFeeRepository;
    }
    PropertiesConfiguration prop = new PropertiesUtils().getProperties("application.properties");

    public void cumulativeFee(List<CumulativePmsAifBean> cumulativePmsAifBeans, String sMonthStart, String sMonthEnd, String distName, HSSFSheet sheetSummary,
                              String sFinal, CumulativeDistributorReportBean cumulativeDistributorReportBean, Double totalClosing, Double currentPayable,
                              Double totalTrail, Double totalUpfront, DistributorMaster distributorMaster, ReportGeneration reportGeneration, HSSFWorkbook workBook, CumulativeAIFSeriesBCAD cumulativeAIFSeriesBCAD) {

        try {
            cumulativeFees= new ArrayList<>();
            cumulativeFees = narrationAdded();
            Double currentUpfront, openingUpfront;
            Double upfront, trail, payable, paid = 0d;
            ReportsDistributorFee distributorFee;
            ReportsDistributorFee openingDistributorFee;
            Integer sMonth=monthCalculate(reportGeneration.getStartDate());
            Integer sYEar=getYear(reportGeneration.getToDate());

            distributorFee = reportsDistributorFeeRepository.getCurrentTrans(distributorMaster.getId(), monthCalculate(reportGeneration.getToDate()), getYear(reportGeneration.getToDate()));
            openingDistributorFee = reportsDistributorFeeRepository.getCurrentTrans(distributorMaster.getId(),
                monthCalculate(reportGeneration.getStartDate()), getYear(reportGeneration.getStartDate()));

            upfront = reportsDistributorFeeRepository.getUpfront(distributorMaster.getId(), reportGeneration.getStartDate(), reportGeneration.getToDate());
            trail = reportsDistributorFeeRepository.getTrail(distributorMaster.getId(), reportGeneration.getStartDate(), reportGeneration.getToDate());
            payable = reportsDistributorFeeRepository.getPayable(distributorMaster.getId(), reportGeneration.getToDate());
            paid = reportsDistributorFeeRepository.getPaid(distributorMaster.getId(), reportGeneration.getToDate());
            currentUpfront = feeTrailUpfrontTransRepository.getCurrentUpfront(distributorMaster.getId(), reportGeneration.getStartDate(), reportGeneration.getToDate());
            openingUpfront = feeTrailUpfrontTransRepository.getTotalUpfront(distributorMaster.getId(), reportGeneration.getStartDate());

            /*BCAD Summary Report*/
            DateFormat dateForm = new SimpleDateFormat("yyyy-MM-dd");
            String sFrom = dateForm.format(reportGeneration.getStartDate());
            String sTo = dateForm.format(reportGeneration.getToDate());
           BCADDistributorFee bcadDistributorFee=bcadDistributorFeeRepository.getMonthlyFee(sFrom,reportGeneration.getDistributorMaster1().getId());
             Float currentUpfrontPeriod =genericPayTrailUpfrontRepository.getProductPaid(sFrom,sTo,(long)1,"Upfront",reportGeneration.getDistributorMaster1().getId());
            Float currentTrailPeriod =genericPayTrailUpfrontRepository.getProductPaid(sFrom,sTo,(long)1,"Trail",reportGeneration.getDistributorMaster1().getId());
            Float distributorOption1=bcadDistributorFeeRepository.getDistributorOption1(sFrom,sTo,reportGeneration.getDistributorMaster1().getId());
            Float distributorOption2=bcadDistributorFeeRepository.getDistributorOption2(sFrom,sTo,reportGeneration.getDistributorMaster1().getId());

            BCADDistributorFee bcadDistributorClose=bcadDistributorFeeRepository.getMonthlyFeeBefore(sTo,reportGeneration.getDistributorMaster1().getId());

            /*Other Than BCAD Summary Report*/
            Double pmsUpfront=reportsDistributorFeeRepository.getClosedUpfront(distributorMaster.getId(), reportGeneration.getStartDate());
            pmsUpfront = (pmsUpfront==null) ?0:pmsUpfront;
            cumulativeFees.get(0).setPmsUpfront(pmsUpfront);
            cumulativeFees.get(1).setPmsUpfront(upfront);
            cumulativeFees.get(2).setPmsUpfront(upfront+pmsUpfront);
            cumulativeFees.get(3).setPmsUpfront(upfront+pmsUpfront-trail);
            cumulativeFees.get(4).setPmsUpfront(upfront+pmsUpfront-trail);
            ReportsDistributorFee openingOtherIncome=reportsDistributorFeeRepository.findCurrentOpeningBalance(sFrom,
                reportGeneration.getDistributorMaster1().getId());
            cumulativeFees.get(5).setOtherIncome((double)openingOtherIncome.getOpeningBal());
            cumulativeFees.get(6).setOtherIncome((double)trail);
            cumulativeFees.get(7).setOtherIncome(upfront-trail);
            cumulativeFees.get(8).setOtherIncome(openingOtherIncome.getOpeningBal()+trail+(upfront-trail));
            Double currentPaid=reportsDistributorFeeRepository.getCurrentPaid(reportGeneration.getDistributorMaster1().getId(),reportGeneration.getStartDate(),
                reportGeneration.getToDate());
            currentPaid=(currentPaid==null) ?0.0 :currentPaid;
            cumulativeFees.get(9).setOtherIncome(currentPaid);
            cumulativeFees.get(10).setOtherIncome(cumulativeFees.get(8).getOtherIncome()-currentPaid);

            Product productAIF2 = productRepository.findByProductName("AIF2");
            Product productBlend = productRepository.findByProductName("AIF Blend");
            AIFDistributorFee aifDistributorFeeAIF2=aifDistributorFeeRepository.getMonthsCalculation(productAIF2.getId(),reportGeneration.getDistributorMaster1().getId(),sFrom);
            Float payAIF2=genericPayTrailUpfrontRepository.getProductPaid(sFrom,sTo,productAIF2.getId(),"Trail",reportGeneration.getDistributorMaster1().getId());
           Float openingAIF2=(aifDistributorFeeAIF2 ==null) ?0f : aifDistributorFeeAIF2.getOpeningBalFee();
           openingAIF2=(openingAIF2==null)?0f:openingAIF2;
            cumulativeFees.get(5).setAifGreen((double)openingAIF2);
            cumulativeFees.get(6).setAifGreen(cumulativeAIFSeriesBCAD.getAIFGreenValue());
            cumulativeFees.get(8).setAifGreen((double)openingAIF2+cumulativeAIFSeriesBCAD.getAIFGreenValue());
           payAIF2=(payAIF2==null)?0f:payAIF2;
            cumulativeFees.get(9).setAifGreen((double)payAIF2);
           cumulativeFees.get(10).setAifGreen((double)openingAIF2+cumulativeAIFSeriesBCAD.getAIFGreenValue()-(double)payAIF2);

            AIFDistributorFee aifDistributorFeeBlend=aifDistributorFeeRepository.getMonthsCalculation(productBlend.getId(),reportGeneration.getDistributorMaster1().getId(),sFrom);
            Float payBlendUpfront=genericPayTrailUpfrontRepository.getProductPaid(sFrom,sTo,productBlend.getId(),"Upfront",reportGeneration.getDistributorMaster1().getId());
            Float payBlendTrail=genericPayTrailUpfrontRepository.getProductPaid(sFrom,sTo,productBlend.getId(),"Trail",reportGeneration.getDistributorMaster1().getId());
            payBlendUpfront=(payBlendUpfront==null)?0f:payBlendUpfront;
            cumulativeFees.get(1).setAifBlendUpfront((double)payBlendUpfront);
            cumulativeFees.get(2).setAifBlendUpfront((double)payBlendUpfront);
            Float openingAIFBlend=aifDistributorFeeBlend.getOpeningBalFee();
            openingAIFBlend=(openingAIFBlend==null)?0f:openingAIFBlend;
            cumulativeFees.get(5).setAifBlendTrail((double)openingAIFBlend);
            cumulativeFees.get(6).setAifBlendTrail(cumulativeAIFSeriesBCAD.getAIFBlendValue());
            cumulativeFees.get(8).setAifBlendTrail((double)openingAIFBlend+cumulativeAIFSeriesBCAD.getAIFBlendValue());
            cumulativeFees.get(8).setAifBlendUpfront((double)payBlendUpfront);
            payBlendTrail=(payBlendTrail==null)?0f:payBlendTrail;
            cumulativeFees.get(9).setAifBlendTrail((double)payBlendTrail);
            cumulativeFees.get(10).setAifBlendTrail((double)openingAIFBlend+cumulativeAIFSeriesBCAD.getAIFBlendValue()-(double)payBlendTrail);


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
            cs.setWrapText(true);

            csRight.setAlignment(HorizontalAlignment.RIGHT);

            // Data Format
            CellStyle csDF = StyleUtil.getStyleDataFormat(workBook);
            CellStyle csPlainNoBorder = workBook.createCellStyle();
            CellStyle csHorVerCenter = workBook.createCellStyle();
            CellStyle csPlanLeftRight = workBook.createCellStyle();

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

            csHorVerCenter.setAlignment(HorizontalAlignment.CENTER);
            csHorVerCenter.setVerticalAlignment(VerticalAlignment.CENTER);
            csHorVerCenter.setFont(fFont);
            csHorVerCenter.setWrapText(true);

            csHorVerCenter.setBorderTop(BorderStyle.THIN);
            csHorVerCenter.setBorderBottom(BorderStyle.THIN);
            csHorVerCenter.setBorderLeft(BorderStyle.THIN);
            csHorVerCenter.setBorderRight(BorderStyle.THIN);

            HSSFFont fFontNoBold = workBook.createFont();
            fFontNoBold.setFontHeightInPoints((short) 11);
            fFontNoBold.setFontName("Calibri");

            csPlanLeftRight.setBorderLeft(BorderStyle.THIN);
            csPlanLeftRight.setBorderRight(BorderStyle.THIN);
            csPlanLeftRight.setBorderBottom(BorderStyle.THIN);
            csPlanLeftRight.setBorderTop(BorderStyle.THIN);
            csPlanLeftRight.setFont(fFontNoBold);

            HSSFRow rowUserName = sheetSummary.createRow(sheetSummary.getLastRowNum() + 1);
            rowUserName.createCell(0).setCellValue(distName.toUpperCase());
            rowUserName.getCell(0).setCellStyle(csPlainNoBorder);

            HSSFRow durationFrom = sheetSummary.createRow(sheetSummary.getLastRowNum() + 1);
            durationFrom.createCell(0).setCellValue("FEES PAYABALE FOR " + sMonthStart + "-" + sMonthEnd);
            durationFrom.getCell(0).setCellStyle(csPlainNoBorder);

            HSSFRow distFee = sheetSummary.createRow(sheetSummary.getLastRowNum() + 1);
            distFee.createCell(0).setCellValue("Distribution Fee Details - for the Period");
            distFee.getCell(0).setCellStyle(csPlainNoBorder);

            // Format sheet
            sheetSummary.addMergedRegion(new CellRangeAddress(1, 1, 0, 3));
            sheetSummary.addMergedRegion(new CellRangeAddress(2, 2, 0, 3));
            sheetSummary.addMergedRegion(new CellRangeAddress(3, 3, 0, 3));

            csDF.setFont(fValues);

            int iRowNo = sheetSummary.getLastRowNum() + 1;
            HSSFRow headingBRSBookRow = sheetSummary.createRow(iRowNo);
            headingBRSBookRow.createCell(0).setCellValue("Particulars");
            headingBRSBookRow.getCell(0).setCellStyle(csHorVerCenter);
            CellRangeAddress cellRangeAddress =new CellRangeAddress(4, 5, 0, 0);
            sheetSummary.addMergedRegion(cellRangeAddress);
            setBordersForMergedCells(cellRangeAddress,sheetSummary,workBook);

            HSSFCell cell = headingBRSBookRow.createCell(1);
            cell.setCellValue("Other Income");
            cell.setCellStyle(csHorVerCenter);
            sheetSummary.autoSizeColumn(1);
            cellRangeAddress =new CellRangeAddress(4, 5, 1, 1);
            sheetSummary.addMergedRegion(cellRangeAddress);
            setBordersForMergedCells(cellRangeAddress,sheetSummary,workBook);

            headingBRSBookRow = sheetSummary.getRow(4);
            cell = headingBRSBookRow.createCell(2);
            cell.setCellValue("PMS NAV Fee");
            cell.setCellStyle(csHorVerCenter);
            sheetSummary.autoSizeColumn(2);
            cellRangeAddress =new CellRangeAddress(4, 4, 2, 3);
            sheetSummary.addMergedRegion(cellRangeAddress);
            setBordersForMergedCells(cellRangeAddress,sheetSummary,workBook);

            headingBRSBookRow = sheetSummary.getRow(5);
            cell = headingBRSBookRow.createCell(2);
            cell.setCellValue("Referral");
            cell.setCellStyle(csHorVerCenter);

            headingBRSBookRow = sheetSummary.getRow(5);
            cell = headingBRSBookRow.createCell(3);
            cell.setCellValue("Trail");
            cell.setCellStyle(csHorVerCenter);

            headingBRSBookRow = sheetSummary.getRow(4);
            cell = headingBRSBookRow.createCell(4);
            cell.setCellValue("PMS Profit Share");
            cell.setCellStyle(csHorVerCenter);
            cellRangeAddress =new CellRangeAddress(4, 5, 4, 4);
            sheetSummary.addMergedRegion(cellRangeAddress);
            setBordersForMergedCells(cellRangeAddress,sheetSummary,workBook);

            cell = headingBRSBookRow.createCell(5);
            cell.setCellValue("PMS BCAD");
            cell.setCellStyle(csHorVerCenter);
            cellRangeAddress =new CellRangeAddress(4, 4, 5, 7);
            sheetSummary.addMergedRegion(cellRangeAddress);
            setBordersForMergedCells(cellRangeAddress,sheetSummary,workBook);

            headingBRSBookRow = sheetSummary.getRow(5);
            cell = headingBRSBookRow.createCell(5);
            cell.setCellValue("Referral");
            cell.setCellStyle(csHorVerCenter);

            cell = headingBRSBookRow.createCell(6);
            cell.setCellValue("Net Trail                  (Option 1)");
            cell.setCellStyle(csHorVerCenter);

            cell = headingBRSBookRow.createCell(7);
            cell.setCellValue("Trail  (Option 2)");
            cell.setCellStyle(csHorVerCenter);

            headingBRSBookRow= sheetSummary.getRow(4);
            cell = headingBRSBookRow.createCell(8);
            cell.setCellValue("AIF HYF  (Trail)");
            cell.setCellStyle(csHorVerCenter);
            cellRangeAddress =new CellRangeAddress(4, 5, 8, 8);
            sheetSummary.addMergedRegion(cellRangeAddress);
            setBordersForMergedCells(cellRangeAddress,sheetSummary,workBook);

            headingBRSBookRow= sheetSummary.getRow(4);
            cell = headingBRSBookRow.createCell(9);
            cell.setCellValue("AIF Green");
            cell.setCellStyle(csHorVerCenter);
            cellRangeAddress =new CellRangeAddress(4, 5, 9, 9);
            sheetSummary.addMergedRegion(cellRangeAddress);
            setBordersForMergedCells(cellRangeAddress,sheetSummary,workBook);

            cell = headingBRSBookRow.createCell(10);
            cell.setCellValue("AIF Blend");
            cell.setCellStyle(csHorVerCenter);
            cellRangeAddress =new CellRangeAddress(4, 4, 10, 11);
            sheetSummary.addMergedRegion(cellRangeAddress);
            setBordersForMergedCells(cellRangeAddress,sheetSummary,workBook);
            System.out.println(sheetSummary.getLastRowNum());

            headingBRSBookRow = sheetSummary.getRow(5);
            cell = headingBRSBookRow.createCell(10);
            cell.setCellValue("Referral");
            cell.setCellStyle(csHorVerCenter);

            cell = headingBRSBookRow.createCell(11);
            cell.setCellValue("Trail");
            cell.setCellStyle(csHorVerCenter);

            headingBRSBookRow= sheetSummary.getRow(4);
            cell = headingBRSBookRow.createCell(12);
            cell.setCellValue("Total");
            cell.setCellStyle(csHorVerCenter);
            cellRangeAddress =new CellRangeAddress(4, 5, 12, 12);
            sheetSummary.addMergedRegion(cellRangeAddress);
            setBordersForMergedCells(cellRangeAddress,sheetSummary,workBook);

            iRowNo = sheetSummary.getLastRowNum() + 1;
            headingBRSBookRow = sheetSummary.createRow(iRowNo);
            headingBRSBookRow.createCell(0).setCellValue("Fee for the Period - Month/Quarter/Year");
            //sheetSummary.autoSizeColumn(0);
            headingBRSBookRow.getCell(0).setCellStyle(cs);
            Float totalAllProduct=0f;

            cell = headingBRSBookRow.createCell(1);
            cell.setCellValue(MathUtil.getRoundOffValue(cumulativePms.getBrokerageTotal()));
            totalAllProduct+=cumulativePms.getBrokerageTotal();
            cell.setCellStyle(csDF);
            cell.setCellType(CellType.NUMERIC);

            cell = headingBRSBookRow.createCell(2);
            cell.setCellValue(MathUtil.getRoundOffValue(-upfront.floatValue()));
            totalAllProduct+=-upfront.floatValue();
            cell.setCellStyle(csDF);

            cell = headingBRSBookRow.createCell(3);
            cell.setCellValue(MathUtil.getRoundOffValue(cumulativePms.getPmsTotal()));
            totalAllProduct+=cumulativePms.getPmsTotal();
            cell.setCellStyle(csDF);
            cell.setCellType(CellType.NUMERIC);

            cell = headingBRSBookRow.createCell(4);
            cell.setCellValue(MathUtil.getRoundOffValue(cumulativePms.getProfitTotal()));
            totalAllProduct+=cumulativePms.getProfitTotal();
            cell.setCellStyle(csDF);
            cell.setCellType(CellType.NUMERIC);

            cell = headingBRSBookRow.createCell(5);
            cell.setCellValue(MathUtil.getRoundOffValue(cumulativeAIFSeriesBCAD.getBCADUpfrontValue().floatValue()));
            totalAllProduct+=cumulativeAIFSeriesBCAD.getBCADUpfrontValue().floatValue();
            cell.setCellStyle(csDF);

            cell = headingBRSBookRow.createCell(6);
            cell.setCellValue(MathUtil.getRoundOffValue(cumulativeAIFSeriesBCAD.getBCADDistOption1().floatValue()));
            totalAllProduct+=cumulativeAIFSeriesBCAD.getBCADDistOption1().floatValue();
            cell.setCellStyle(csDF);

            cell = headingBRSBookRow.createCell(7);
            cell.setCellValue(MathUtil.getRoundOffValue(cumulativeAIFSeriesBCAD.getBCADDistOption2().floatValue()));
            totalAllProduct+=cumulativeAIFSeriesBCAD.getBCADDistOption2().floatValue();
            cell.setCellStyle(csDF);

            cell = headingBRSBookRow.createCell(8);
            cell.setCellValue(MathUtil.getRoundOffValue(Float.parseFloat(cumulativeAif.getAifTotal())));
            totalAllProduct+=Float.parseFloat(cumulativeAif.getAifTotal());
            cell.setCellStyle(csDF);
            cell.setCellType(CellType.NUMERIC);

            cell = headingBRSBookRow.createCell(9);
            cell.setCellValue(MathUtil.getRoundOffValue(cumulativeAIFSeriesBCAD.getAIFGreenValue().floatValue()));
            totalAllProduct+=cumulativeAIFSeriesBCAD.getAIFGreenValue().floatValue();
            headingBRSBookRow.getCell(9).setCellStyle(csDF);

            cell = headingBRSBookRow.createCell(10);
            cell.setCellValue(MathUtil.getRoundOffValue(payBlendUpfront));
            cell.setCellStyle(csDF);
            cell.setCellType(CellType.NUMERIC);

            cell = headingBRSBookRow.createCell(11);
            cell.setCellValue(MathUtil.getRoundOffValue(cumulativeAIFSeriesBCAD.getAIFBlendValue().floatValue()));
            totalAllProduct+=cumulativeAIFSeriesBCAD.getAIFBlendValue().floatValue();
            headingBRSBookRow.getCell(11).setCellStyle(csDF);

            cell = headingBRSBookRow.createCell(12);
            cell.setCellValue(MathUtil.getRoundOffValue(totalAllProduct));
            cell.setCellStyle(csDF);
            cell.setCellType(CellType.NUMERIC);


            durationFrom = sheetSummary.createRow(sheetSummary.getLastRowNum() + 2);
            durationFrom.createCell(0).setCellValue("Summary");
            durationFrom.getCell(0).setCellStyle(csPlainNoBorder);

            durationFrom = sheetSummary.createRow(sheetSummary.getLastRowNum() + 1);
            durationFrom.createCell(0).setCellValue("Fees (Other than BCAD)");
            durationFrom.getCell(0).setCellStyle(csPlainNoBorder);

            iRowNo = sheetSummary.getLastRowNum() + 1;
            headingBRSBookRow = sheetSummary.createRow(iRowNo);
            headingBRSBookRow.createCell(0).setCellValue("Particulars         ");
            headingBRSBookRow.getCell(0).setCellStyle(csHorVerCenter);
            sheetSummary.autoSizeColumn(0);
            cellRangeAddress =new CellRangeAddress(10, 11, 0, 0);
            sheetSummary.addMergedRegion(cellRangeAddress);
            setBordersForMergedCells(cellRangeAddress,sheetSummary,workBook);

            cell = headingBRSBookRow.createCell(1);
            cell.setCellValue("Other Income");
            cell.setCellStyle(csHorVerCenter);
            sheetSummary.autoSizeColumn(1);
            cellRangeAddress =new CellRangeAddress(10, 11, 1, 1);
            sheetSummary.addMergedRegion(cellRangeAddress);
            setBordersForMergedCells(cellRangeAddress,sheetSummary,workBook);

            headingBRSBookRow = sheetSummary.getRow(10);
            cell = headingBRSBookRow.createCell(2);
            cell.setCellValue("PMS NAV Fee");
            cell.setCellStyle(csHorVerCenter);
            sheetSummary.autoSizeColumn(2);
            cellRangeAddress =new CellRangeAddress(10, 10, 2, 3);
            sheetSummary.addMergedRegion(cellRangeAddress);
            setBordersForMergedCells(cellRangeAddress,sheetSummary,workBook);

            headingBRSBookRow = sheetSummary.getRow(11);
            cell = headingBRSBookRow.createCell(2);
            cell.setCellValue("Referral");
            cell.setCellStyle(csHorVerCenter);

            //headingBRSBookRow = sheetSummary.getRow(5);
            cell = headingBRSBookRow.createCell(3);
            cell.setCellValue("Trail");
            cell.setCellStyle(csHorVerCenter);

            headingBRSBookRow = sheetSummary.getRow(10);
            cell = headingBRSBookRow.createCell(4);
            cell.setCellValue("PMS Profit Share");
            cell.setCellStyle(csHorVerCenter);
            cellRangeAddress =new CellRangeAddress(10, 11, 4, 4);
            sheetSummary.addMergedRegion(cellRangeAddress);
            setBordersForMergedCells(cellRangeAddress,sheetSummary,workBook);

            headingBRSBookRow= sheetSummary.getRow(10);
            cell = headingBRSBookRow.createCell(5);
            cell.setCellValue("AIF HYF");
            cell.setCellStyle(csHorVerCenter);
            cellRangeAddress =new CellRangeAddress(10, 11, 5, 5);
            sheetSummary.addMergedRegion(cellRangeAddress);
            setBordersForMergedCells(cellRangeAddress,sheetSummary,workBook);

            headingBRSBookRow= sheetSummary.getRow(10);
            cell = headingBRSBookRow.createCell(6);
            cell.setCellValue("AIF Green");
            cell.setCellStyle(csHorVerCenter);
            cellRangeAddress =new CellRangeAddress(10, 11, 6, 6);
            sheetSummary.addMergedRegion(cellRangeAddress);
            setBordersForMergedCells(cellRangeAddress,sheetSummary,workBook);

            cell = headingBRSBookRow.createCell(7);
            cell.setCellValue("AIF Blend");
            cell.setCellStyle(csHorVerCenter);
            cellRangeAddress =new CellRangeAddress(10, 10, 7, 8);
            sheetSummary.addMergedRegion(cellRangeAddress);
            setBordersForMergedCells(cellRangeAddress,sheetSummary,workBook);
            System.out.println(sheetSummary.getLastRowNum());

            headingBRSBookRow = sheetSummary.getRow(11);
            cell = headingBRSBookRow.createCell(7);
            cell.setCellValue("Upfront");
            cell.setCellStyle(csHorVerCenter);

            cell = headingBRSBookRow.createCell(8);
            cell.setCellValue("Trail");
            cell.setCellStyle(csHorVerCenter);

            headingBRSBookRow= sheetSummary.getRow(10);
            cell = headingBRSBookRow.createCell(9);
            cell.setCellValue("Total");
            cell.setCellStyle(csHorVerCenter);
            cellRangeAddress =new CellRangeAddress(10, 11, 9, 9);
            sheetSummary.addMergedRegion(cellRangeAddress);
            setBordersForMergedCells(cellRangeAddress,sheetSummary,workBook);
            String name ="";
            for(NewCumulativeFee newC: cumulativeFees){
                if(!name.equals(newC.getFeeType())){
                iRowNo = sheetSummary.getLastRowNum() + 1;
                headingBRSBookRow = sheetSummary.createRow(iRowNo);
                headingBRSBookRow.createCell(0).setCellValue(newC.getFeeType());
               // sheetSummary.autoSizeColumn(0);
                headingBRSBookRow.getCell(0).setCellStyle(cs);
                name=newC.getFeeType();}

                iRowNo = sheetSummary.getLastRowNum() + 1;
                headingBRSBookRow = sheetSummary.createRow(iRowNo);
                headingBRSBookRow.createCell(0).setCellValue(newC.getNarration());
                //sheetSummary.autoSizeColumn(0);
                headingBRSBookRow.getCell(0).setCellStyle(cs);

                cell = headingBRSBookRow.createCell(1);
                if(newC.getOtherIncome()!=null)
                cell.setCellValue(MathUtil.getRoundOffValue(newC.getOtherIncome().floatValue()));
                cell.setCellStyle(csDF);

                cell = headingBRSBookRow.createCell(2);
                if(newC.getPmsUpfront()!=null)
                    cell.setCellValue(MathUtil.getRoundOffValue(newC.getPmsUpfront().floatValue()));
                cell.setCellStyle(csDF);

                cell = headingBRSBookRow.createCell(3);
                cell.setCellValue("");
                cell.setCellStyle(csDF);

                cell = headingBRSBookRow.createCell(4);
                cell.setCellValue("");
                cell.setCellStyle(csDF);

                cell = headingBRSBookRow.createCell(5);
                cell.setCellValue("");
                cell.setCellStyle(csDF);

                cell = headingBRSBookRow.createCell(6);
                if(newC.getAifGreen()!=null)
                    cell.setCellValue(MathUtil.getRoundOffValue(newC.getAifGreen().floatValue()));
                cell.setCellStyle(csDF);

                cell = headingBRSBookRow.createCell(7);
                if(newC.getAifBlendUpfront()!=null)
                    cell.setCellValue(MathUtil.getRoundOffValue(newC.getAifBlendUpfront().floatValue()));
                cell.setCellStyle(csDF);

                cell = headingBRSBookRow.createCell(8);
                if(newC.getAifBlendTrail()!=null)
                    cell.setCellValue(MathUtil.getRoundOffValue(newC.getAifBlendTrail().floatValue()));
                cell.setCellStyle(csDF);

                cell = headingBRSBookRow.createCell(9);
                cell.setCellValue("");
                cell.setCellStyle(csDF);

            }

            headingBRSBookRow = sheetSummary.createRow(sheetSummary.getLastRowNum() + 3);
            headingBRSBookRow.createCell(0).setCellValue(prop.getString("bcad.option1.narration"));
            headingBRSBookRow.getCell(0).setCellStyle(csPlainNoBorder);

            cell = headingBRSBookRow.createCell(3);
            cell.setCellValue(prop.getString("bcad.option2.narration"));
            cell.setCellStyle(csPlainNoBorder);

            headingBRSBookRow = sheetSummary.createRow(sheetSummary.getLastRowNum() + 1);
            headingBRSBookRow.createCell(0).setCellValue(prop.getString("bcad.option1.management"));
            headingBRSBookRow.getCell(0).setCellStyle(csPlainNoBorder);

            cell = headingBRSBookRow.createCell(3);
            cell.setCellValue(prop.getString("bcad.option2.manage.performance"));
            cell.setCellStyle(csPlainNoBorder);
            cellRangeAddress =new CellRangeAddress(29, 29, 3, 4);
            sheetSummary.addMergedRegion(cellRangeAddress);

            headingBRSBookRow = sheetSummary.createRow(sheetSummary.getLastRowNum() + 1);
            headingBRSBookRow.createCell(0).setCellValue(prop.getString("bcad.options.particulars"));
            headingBRSBookRow.getCell(0).setCellStyle(cs);

            cell = headingBRSBookRow.createCell(1);
            cell.setCellValue(prop.getString("bcad.options.amt"));
            cell.setCellStyle(cs);

            cell = headingBRSBookRow.createCell(3);
            cell.setCellValue(prop.getString("bcad.options.particulars"));
            cell.setCellStyle(cs);

            cell = headingBRSBookRow.createCell(4);
            cell.setCellValue(prop.getString("bcad.options.amt"));
            cell.setCellStyle(cs);

            headingBRSBookRow = sheetSummary.createRow(sheetSummary.getLastRowNum() + 1);
            headingBRSBookRow.createCell(0).setCellValue(prop.getString("bcad.option1.previous"));
            headingBRSBookRow.getCell(0).setCellStyle(cs);

            cell = headingBRSBookRow.createCell(1);
            if(bcadDistributorFee.getOpeningBalOption1()==null)
                bcadDistributorFee.setOpeningBalOption1(0f);
            cell.setCellValue(MathUtil.getRoundOffValue(bcadDistributorFee.getOpeningBalOption1()));
            cell.setCellStyle(csDF);

            cell = headingBRSBookRow.createCell(3);
            cell.setCellValue(prop.getString("bcad.trail.forward"));
            cell.setCellStyle(cs);

            cell = headingBRSBookRow.createCell(4);
            if(bcadDistributorFee.getOpeningBalOption2()==null)
                bcadDistributorFee.setOpeningBalOption2(0f);
            cell.setCellValue(MathUtil.getRoundOffValue(bcadDistributorFee.getOpeningBalOption1()));
            cell.setCellStyle(csDF);

            headingBRSBookRow = sheetSummary.createRow(sheetSummary.getLastRowNum() + 1);
            headingBRSBookRow.createCell(0).setCellValue(prop.getString("bcad.add.referral"));
            headingBRSBookRow.getCell(0).setCellStyle(cs);

            cell = headingBRSBookRow.createCell(1);
            if(currentUpfrontPeriod!=null)
            cell.setCellValue(MathUtil.getRoundOffValue(currentUpfrontPeriod));
            cell.setCellStyle(csDF);

            cell = headingBRSBookRow.createCell(3);
            cell.setCellValue(prop.getString("bcad.trail.fee.period"));
            cell.setCellStyle(cs);

            cell = headingBRSBookRow.createCell(4);
            if(distributorOption2==null)
                distributorOption2=0f;
                cell.setCellValue(MathUtil.getRoundOffValue(distributorOption2));
            cell.setCellStyle(csDF);

            headingBRSBookRow = sheetSummary.createRow(sheetSummary.getLastRowNum() + 1);
            headingBRSBookRow.createCell(0).setCellValue("Total");
            headingBRSBookRow.getCell(0).setCellStyle(cs);

            cell = headingBRSBookRow.createCell(1);
            if(currentUpfrontPeriod==null)
                currentUpfrontPeriod=0f;
            cell.setCellValue(MathUtil.getRoundOffValue(bcadDistributorFee.getOpeningBalOption1()+currentUpfrontPeriod));
            cell.setCellStyle(csDF);

            cell = headingBRSBookRow.createCell(3);
            cell.setCellValue("Total");
            cell.setCellStyle(cs);

            cell = headingBRSBookRow.createCell(4);
            if(bcadDistributorFee.getOpeningBalOption2()==null)
                bcadDistributorFee.setOpeningBalOption2(0f);
            if(currentTrailPeriod==null)
                currentTrailPeriod=0f;
            cell.setCellValue(MathUtil.getRoundOffValue(bcadDistributorFee.getOpeningBalOption2()+distributorOption2));
            cell.setCellStyle(csDF);

            headingBRSBookRow = sheetSummary.createRow(sheetSummary.getLastRowNum() + 1);
            headingBRSBookRow.createCell(0).setCellValue(prop.getString("bcad.less.adjustments.option1"));
            headingBRSBookRow.getCell(0).setCellStyle(cs);

            cell = headingBRSBookRow.createCell(1);
            if(distributorOption1!=null)
            cell.setCellValue(MathUtil.getRoundOffValue(distributorOption1));
            cell.setCellStyle(csDF);

            cell = headingBRSBookRow.createCell(3);
            cell.setCellValue(prop.getString("bcad.less.payments"));
            cell.setCellStyle(cs);

            cell = headingBRSBookRow.createCell(4);
            if(currentTrailPeriod!=null)
            cell.setCellValue(MathUtil.getRoundOffValue(currentTrailPeriod));
            cell.setCellStyle(csDF);

            headingBRSBookRow = sheetSummary.createRow(sheetSummary.getLastRowNum() + 1);
            headingBRSBookRow.createCell(0).setCellValue(prop.getString("bcad.closing.bal.referral"));
            headingBRSBookRow.getCell(0).setCellStyle(cs);

            cell = headingBRSBookRow.createCell(1);
            if(bcadDistributorClose.getClosingBalOption1()!=null){
                if(bcadDistributorClose.getClosingBalOption1().floatValue()<0){
                    Float sValue=MathUtil.getRoundOffValue(-bcadDistributorClose.getClosingBalOption1().floatValue());
            cell.setCellValue(-sValue);}
            else
                    cell.setCellValue(MathUtil.getRoundOffValue(bcadDistributorClose.getClosingBalOption1().floatValue()));
            }
            cell.setCellStyle(csDF);

            cell = headingBRSBookRow.createCell(3);
            cell.setCellValue(prop.getString("bcad.option2.bal.payable"));
            cell.setCellStyle(cs);

            cell = headingBRSBookRow.createCell(4);

            if(bcadDistributorClose.getTrailPayableOption2()!=null){
                if(bcadDistributorClose.getTrailPayableOption2()<0){
                    Float sValue=MathUtil.getRoundOffValue(-bcadDistributorClose.getTrailPayableOption2().floatValue());
               cell.setCellValue(-sValue);}
            else
                    cell.setCellValue(MathUtil.getRoundOffValue(bcadDistributorClose.getTrailPayableOption2().floatValue()));}
            cell.setCellStyle(csDF);

            sheetSummary.setDefaultColumnWidth(16);

           //sheetSummary.setColumnWidth(0, 25);


        } catch (Exception e) {
            System.out.println(e);
        }
    }

    private List<NewCumulativeFee> narrationAdded() {
        NewCumulativeFee newCumulativeFee=new NewCumulativeFee();
        List<NewCumulativeFee> addNarrations = new ArrayList<>();

        newCumulativeFee.setNarration(prop.getString("opening.bal.previous"));
        newCumulativeFee.setFeeType("Upfront Fee");
        addNarrations.add(newCumulativeFee);

        newCumulativeFee=new NewCumulativeFee();
        newCumulativeFee.setNarration(prop.getString("referral.fee.current"));
        newCumulativeFee.setFeeType("Upfront Fee");
        addNarrations.add(newCumulativeFee);

        newCumulativeFee=new NewCumulativeFee();
        newCumulativeFee.setNarration(prop.getString("total.upfront"));
        newCumulativeFee.setFeeType("Upfront Fee");
        addNarrations.add(newCumulativeFee);

        newCumulativeFee=new NewCumulativeFee();
        newCumulativeFee.setNarration(prop.getString("adjustment.trail.current"));
        newCumulativeFee.setFeeType("Upfront Fee");
        addNarrations.add(newCumulativeFee);

        newCumulativeFee=new NewCumulativeFee();
        newCumulativeFee.setNarration(prop.getString("closing.bal.next.period"));
        newCumulativeFee.setFeeType("Upfront Fee");
        addNarrations.add(newCumulativeFee);

        newCumulativeFee=new NewCumulativeFee();
        newCumulativeFee.setNarration(prop.getString("opening.bal.current"));
        newCumulativeFee.setFeeType("Trail Fee");
        addNarrations.add(newCumulativeFee);

        newCumulativeFee=new NewCumulativeFee();
        newCumulativeFee.setNarration(prop.getString("fee.current"));
        newCumulativeFee.setFeeType("Trail Fee");
        addNarrations.add(newCumulativeFee);

       /* newCumulativeFee=new NewCumulativeFee();
        newCumulativeFee.setNarration(prop.getString("less.amount"));
        newCumulativeFee.setFeeType("Trail Fee");
        addNarrations.add(newCumulativeFee);*/

        newCumulativeFee=new NewCumulativeFee();
        newCumulativeFee.setNarration(prop.getString("less.amount")+prop.getString("trail.fee.adjusted.upfront"));
        newCumulativeFee.setFeeType("Trail Fee");
        addNarrations.add(newCumulativeFee);

        newCumulativeFee=new NewCumulativeFee();
        newCumulativeFee.setNarration(prop.getString("net.trail.fee"));
        newCumulativeFee.setFeeType("Trail Fee");
        addNarrations.add(newCumulativeFee);

        newCumulativeFee=new NewCumulativeFee();
        newCumulativeFee.setNarration(prop.getString("payments.made"));
        newCumulativeFee.setFeeType("Trail Fee");
        addNarrations.add(newCumulativeFee);

        newCumulativeFee=new NewCumulativeFee();
        newCumulativeFee.setNarration(prop.getString("trail.balance.payable"));
        newCumulativeFee.setFeeType("Trail Fee");
        addNarrations.add(newCumulativeFee);

        return addNarrations;
    }


    private void setBordersForMergedCells(CellRangeAddress cellRangeAddress, HSSFSheet sheetSummary, HSSFWorkbook workBook) {
        HSSFRegionUtil.setBorderTop(1,cellRangeAddress,sheetSummary,workBook);
        HSSFRegionUtil.setBorderBottom(1,cellRangeAddress,sheetSummary,workBook);
        HSSFRegionUtil.setBorderLeft(1,cellRangeAddress,sheetSummary,workBook);
        HSSFRegionUtil.setBorderRight(1,cellRangeAddress,sheetSummary,workBook);
    }

    private Integer monthCalculate(Date startDate) {
        Calendar calendar = null;

        calendar = Calendar.getInstance();
        calendar.setTime(startDate);
        return calendar.get(Calendar.MONTH) + 1;
    }
    private Integer getYear(Date startDate) {
        Calendar calendar = null;

        calendar = Calendar.getInstance();
        calendar.setTime(startDate);
        return calendar.get(Calendar.YEAR);

    }

}
