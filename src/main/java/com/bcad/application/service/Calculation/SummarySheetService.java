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
public class SummarySheetService {

    private final ReportsDistributorFeeRepository reportsDistributorFeeRepository;
    private final FeeTrailUpfrontTransRepository feeTrailUpfrontTransRepository;
    private final BCADDistributorFeeRepository bcadDistributorFeeRepository;
    private final GenericPayTrailUpfrontRepository genericPayTrailUpfrontRepository;
    private final ProductRepository productRepository;
    private final AIFDistributorFeeRepository aifDistributorFeeRepository;
    //private final ReportBcadMonthlyCalculationRepository reportBcadMonthlyCalculationRepository;

    List<NewCumulativeFee> cumulativeFees= new ArrayList<>();

    public SummarySheetService(ReportsDistributorFeeRepository reportsDistributorFeeRepository, FeeTrailUpfrontTransRepository feeTrailUpfrontTransRepository,
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
            Double aifBlendTotal=0.0;

            /*BCAD Summary Report*/
            DateFormat dateForm = new SimpleDateFormat("yyyy-MM-dd");
            String sFrom = dateForm.format(reportGeneration.getStartDate());
            String sTo = dateForm.format(reportGeneration.getToDate());


            /*Other Than BCAD Summary Report*/
            Product productAIF2 = productRepository.findByProductName("AIF2");
            Product productBlend = productRepository.findByProductName("AIF Blend");
            System.out.println("Final Date ------->"+minimumDate(reportGeneration.getToDate()));
            String sAifBlendDate=dateForm.format(minimumDate(reportGeneration.getToDate()));
            AIFDistributorFee aifDistributorFee=aifDistributorFeeRepository.getMonthsCalculation(productBlend.getId(),distributorMaster.getId(),sAifBlendDate);
            //String aifBlendStart=dateForm.format(openingDate(reportGeneration.getStartDate()));
            AIFDistributorFee aifDistributorOpen=aifDistributorFeeRepository.getMonthsCalculation(productBlend.getId(),distributorMaster.getId(),sFrom);


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
            CellStyle csDFBold = StyleUtil.getStyleBoldDataFormat(workBook);

            csDFBold.setBorderLeft(BorderStyle.THIN);
            csDFBold.setBorderRight(BorderStyle.THIN);

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

            CellStyle csDFBoldFour = csDFBold;

            csDFBoldFour.setBorderBottom(BorderStyle.THIN);
            csDFBoldFour.setBorderTop(BorderStyle.THIN);
            csDFBoldFour.setBorderLeft(BorderStyle.THIN);
            csDFBoldFour.setBorderRight(BorderStyle.THIN);

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
            headingBRSBookRow.getCell(0).setCellStyle(cs);

            HSSFCell cell = headingBRSBookRow.createCell(1);
            cell.setCellValue("Amount");
            cell.setCellStyle(cs);

           /* iRowNo = sheetSummary.getLastRowNum() + 1;
            headingBRSBookRow = sheetSummary.createRow(iRowNo);
            headingBRSBookRow.createCell(0).setCellValue(prop.getString("pms.marketing.fee"));
            sheetSummary.autoSizeColumn(0);
            headingBRSBookRow.getCell(0).setCellStyle(csPlanLeftRight);*/
            Float totalAllProduct=0f;

           /* cell = headingBRSBookRow.createCell(1);
            cell.setCellValue((cumulativePms.getBrokerageTotal()));
            totalAllProduct+=(cumulativePms.getBrokerageTotal());
            cell.setCellStyle(csDF);
            cell.setCellType(CellType.NUMERIC);*/

            iRowNo = sheetSummary.getLastRowNum() + 1;
            headingBRSBookRow = sheetSummary.createRow(iRowNo);
            cell = headingBRSBookRow.createCell(0);
            sheetSummary.autoSizeColumn(0);
            cell.setCellValue(prop.getString("pms.adv.fee"));
            cell.setCellStyle(csPlanLeftRight);

            cell = headingBRSBookRow.createCell(1);
            cell.setCellValue((cumulativePms.getPmsTotal()));
            totalAllProduct+=(cumulativePms.getPmsTotal());
            cell.setCellStyle(csDF);
            cell.setCellType(CellType.NUMERIC);

            iRowNo = sheetSummary.getLastRowNum() + 1;
            headingBRSBookRow = sheetSummary.createRow(iRowNo);
            cell = headingBRSBookRow.createCell(0);
            sheetSummary.autoSizeColumn(0);
            cell.setCellValue(prop.getString("pms.profit.fee"));
            cell.setCellStyle(csPlanLeftRight);

            cell = headingBRSBookRow.createCell(1);
            cell.setCellValue((cumulativePms.getProfitTotal()));
            totalAllProduct+=(cumulativePms.getProfitTotal());
            cell.setCellStyle(csDF);
            cell.setCellType(CellType.NUMERIC);

            iRowNo = sheetSummary.getLastRowNum() + 1;
            headingBRSBookRow = sheetSummary.createRow(iRowNo);
            cell = headingBRSBookRow.createCell(0);
            sheetSummary.autoSizeColumn(0);
            cell.setCellValue(prop.getString("bcad.option1.upfront"));
            cell.setCellStyle(csPlanLeftRight);

            cell = headingBRSBookRow.createCell(1);
            cell.setCellValue((cumulativeAIFSeriesBCAD.getBCADUpfrontValue().floatValue()));
            totalAllProduct+=(cumulativeAIFSeriesBCAD.getBCADUpfrontValue().floatValue());
            cell.setCellStyle(csDF);

            iRowNo = sheetSummary.getLastRowNum() + 1;
            headingBRSBookRow = sheetSummary.createRow(iRowNo);
            cell = headingBRSBookRow.createCell(0);
            sheetSummary.autoSizeColumn(0);
            cell.setCellValue(prop.getString("bcad.option1.trail.fee"));
            cell.setCellStyle(csPlanLeftRight);

            cell = headingBRSBookRow.createCell(1);
            cell.setCellValue((cumulativeAIFSeriesBCAD.getBCADDistOption1().floatValue()));
            totalAllProduct+=(cumulativeAIFSeriesBCAD.getBCADDistOption1().floatValue());
            cell.setCellStyle(csDF);

            iRowNo = sheetSummary.getLastRowNum() + 1;
            headingBRSBookRow = sheetSummary.createRow(iRowNo);
            cell = headingBRSBookRow.createCell(0);
            sheetSummary.autoSizeColumn(0);
            cell.setCellValue(prop.getString("bcad.option2.trail.fee"));
            cell.setCellStyle(csPlanLeftRight);

            cell = headingBRSBookRow.createCell(1);
            cell.setCellValue((cumulativeAIFSeriesBCAD.getBCADDistOption2().floatValue()));
            totalAllProduct+=(cumulativeAIFSeriesBCAD.getBCADDistOption2().floatValue());
            cell.setCellStyle(csDF);

            iRowNo = sheetSummary.getLastRowNum() + 2;
            headingBRSBookRow = sheetSummary.createRow(iRowNo);
            cell = headingBRSBookRow.createCell(0);
            sheetSummary.autoSizeColumn(0);
            cell.setCellValue(prop.getString("aif.fee"));
            cell.setCellStyle(csPlanLeftRight);

            cell = headingBRSBookRow.createCell(1);
            cell.setCellValue((Float.parseFloat(cumulativeAif.getAifTotal())));
            totalAllProduct+=(Float.parseFloat(cumulativeAif.getAifTotal()));
            cell.setCellStyle(csDF);
            cell.setCellType(CellType.NUMERIC);

            iRowNo = sheetSummary.getLastRowNum() + 1;
            headingBRSBookRow = sheetSummary.createRow(iRowNo);
            cell = headingBRSBookRow.createCell(0);
            sheetSummary.autoSizeColumn(0);
            cell.setCellValue(prop.getString("aif2.fee"));
            cell.setCellStyle(csPlanLeftRight);

            cell = headingBRSBookRow.createCell(1);
            cell.setCellValue((cumulativeAIFSeriesBCAD.getAIFGreenValue().floatValue()));
            totalAllProduct+=(cumulativeAIFSeriesBCAD.getAIFGreenValue().floatValue());
            headingBRSBookRow.getCell(1).setCellStyle(csDF);

            iRowNo = sheetSummary.getLastRowNum() + 1;
            headingBRSBookRow = sheetSummary.createRow(iRowNo);
            cell = headingBRSBookRow.createCell(0);
            sheetSummary.autoSizeColumn(0);
            cell.setCellValue(prop.getString("aifblend.fee"));
            cell.setCellStyle(csPlanLeftRight);

            cell = headingBRSBookRow.createCell(1);
            if(aifDistributorFee.getClosingBal()>0){
            cell.setCellValue((aifDistributorFee.getClosingBal().floatValue()));
            totalAllProduct+=(aifDistributorFee.getClosingBal().floatValue());}
            headingBRSBookRow.getCell(1).setCellStyle(csDF);

            iRowNo = sheetSummary.getLastRowNum() + 1;
            headingBRSBookRow = sheetSummary.createRow(iRowNo);
            cell = headingBRSBookRow.createCell(0);
            sheetSummary.autoSizeColumn(0);
            cell.setCellValue(prop.getString("total.upfront"));
            cell.setCellStyle(cs);

            cell = headingBRSBookRow.createCell(1);
            cell.setCellValue((totalAllProduct));
            cell.setCellStyle(csDFBoldFour);
            cell.setCellType(CellType.NUMERIC);

            iRowNo = sheetSummary.getLastRowNum() + 1;
            headingBRSBookRow = sheetSummary.createRow(iRowNo);
            cell = headingBRSBookRow.createCell(0);
            sheetSummary.autoSizeColumn(0);
            cell.setCellValue("");
            cell.setCellStyle(cs);

            cell = headingBRSBookRow.createCell(1);
            cell.setCellValue("");
            cell.setCellStyle(csDFBoldFour);

            iRowNo = sheetSummary.getLastRowNum() + 1;
            headingBRSBookRow = sheetSummary.createRow(iRowNo);
            cell = headingBRSBookRow.createCell(0);
            sheetSummary.autoSizeColumn(0);
            cell.setCellValue("Rounded Off");
            cell.setCellStyle(cs);

            cell = headingBRSBookRow.createCell(1);
            cell.setCellValue(MathUtil.getRoundOffValue(totalAllProduct));
            cell.setCellStyle(csDFBoldFour);
            cell.setCellType(CellType.NUMERIC);


            distFee = sheetSummary.createRow(sheetSummary.getLastRowNum() + 4);
            distFee.createCell(0).setCellValue("BCAD - Option 1");
            distFee.getCell(0).setCellStyle(csPlainNoBorder);

            distFee.createCell(3).setCellValue("BCAD - Option 2");
            distFee.getCell(3).setCellStyle(csPlainNoBorder);

            distFee = sheetSummary.createRow(sheetSummary.getLastRowNum() + 1);
            distFee.createCell(0).setCellValue("Fixed Management Fee Only");
            distFee.getCell(0).setCellStyle(csPlainNoBorder);

            distFee.createCell(3).setCellValue("Management Fee + Performance Fee");
            distFee.getCell(3).setCellStyle(csPlainNoBorder);
            sheetSummary.addMergedRegion(new CellRangeAddress(20, 20, 3, 4));

            iRowNo = sheetSummary.getLastRowNum() + 1;
            headingBRSBookRow = sheetSummary.createRow(iRowNo);
            headingBRSBookRow.createCell(0).setCellValue("Particulars");
            headingBRSBookRow.getCell(0).setCellStyle(cs);

            cell = headingBRSBookRow.createCell(1);
            cell.setCellValue("Amount");
            cell.setCellStyle(cs);

            headingBRSBookRow.createCell(3).setCellValue("Particulars");
            headingBRSBookRow.getCell(3).setCellStyle(cs);

            cell = headingBRSBookRow.createCell(4);
            cell.setCellValue("Amount");
            cell.setCellStyle(cs);

            iRowNo = sheetSummary.getLastRowNum() + 1;
            headingBRSBookRow = sheetSummary.createRow(iRowNo);
            headingBRSBookRow.createCell(0).setCellValue(prop.getString("bcad.total.trail.option1"));
            headingBRSBookRow.getCell(0).setCellStyle(cs);
            sheetSummary.autoSizeColumn(0);

            Double upfrontOption1 =0.0;
            cell = headingBRSBookRow.createCell(1);
            cell.setCellValue((cumulativeAIFSeriesBCAD.getDistributorShare1().floatValue()));
            upfrontOption1=cumulativeAIFSeriesBCAD.getDistributorShare1();
            headingBRSBookRow.getCell(1).setCellStyle(csDF);

            headingBRSBookRow.createCell(3).setCellValue(prop.getString("bcad.trail.fee.period"));
            headingBRSBookRow.getCell(3).setCellStyle(cs);

            cell = headingBRSBookRow.createCell(4);
            cell.setCellValue((cumulativeAIFSeriesBCAD.getDistributorShare2().floatValue()));
            headingBRSBookRow.getCell(4).setCellStyle(csDF);

            iRowNo = sheetSummary.getLastRowNum() + 1;
            headingBRSBookRow = sheetSummary.createRow(iRowNo);
            headingBRSBookRow.createCell(0).setCellValue(prop.getString("bcad.adj.against.upfront"));
            headingBRSBookRow.getCell(0).setCellStyle(cs);
            sheetSummary.autoSizeColumn(0);

            cell = headingBRSBookRow.createCell(1);
            cell.setCellValue(-(cumulativeAIFSeriesBCAD.getAgainstUpfrontFee().floatValue()));
            upfrontOption1-=cumulativeAIFSeriesBCAD.getAgainstUpfrontFee().floatValue();
            headingBRSBookRow.getCell(1).setCellStyle(csDF);

            headingBRSBookRow.createCell(3).setCellValue(prop.getString("total.upfront"));
            headingBRSBookRow.getCell(3).setCellStyle(cs);

            cell = headingBRSBookRow.createCell(4);
            cell.setCellValue((cumulativeAIFSeriesBCAD.getDistributorShare2().floatValue()));
            headingBRSBookRow.getCell(4).setCellStyle(csDFBoldFour);

            iRowNo = sheetSummary.getLastRowNum() + 1;
            headingBRSBookRow = sheetSummary.createRow(iRowNo);
            headingBRSBookRow.createCell(0).setCellValue(prop.getString("trail.balance.payable"));
            headingBRSBookRow.getCell(0).setCellStyle(cs);
            sheetSummary.autoSizeColumn(0);

            cell = headingBRSBookRow.createCell(1);
            cell.setCellValue((upfrontOption1.floatValue()));
            headingBRSBookRow.getCell(1).setCellStyle(csDFBoldFour);

            distFee = sheetSummary.createRow(sheetSummary.getLastRowNum() + 3);
            distFee.createCell(0).setCellValue("BCAD - Upfront Fee & Adjustment");
            distFee.getCell(0).setCellStyle(csPlainNoBorder);

            iRowNo = sheetSummary.getLastRowNum() + 1;
            headingBRSBookRow = sheetSummary.createRow(iRowNo);
            headingBRSBookRow.createCell(0).setCellValue("Particulars");
            headingBRSBookRow.getCell(0).setCellStyle(cs);

            cell = headingBRSBookRow.createCell(1);
            cell.setCellValue("Amount");
            cell.setCellStyle(cs);

            iRowNo = sheetSummary.getLastRowNum() + 1;
            headingBRSBookRow = sheetSummary.createRow(iRowNo);
            headingBRSBookRow.createCell(0).setCellValue(prop.getString("bcad.option1.previous"));
            csPlanLeftRight.setWrapText(true);
            headingBRSBookRow.getCell(0).setCellStyle(csPlanLeftRight);

            Double bcadOption1=0.0;
            cell = headingBRSBookRow.createCell(1);
            cell.setCellValue((cumulativeAIFSeriesBCAD.getCarryOverForward().floatValue()));
            bcadOption1=cumulativeAIFSeriesBCAD.getCarryOverForward();
            headingBRSBookRow.getCell(1).setCellStyle(csDF);

            iRowNo = sheetSummary.getLastRowNum() + 1;
            headingBRSBookRow = sheetSummary.createRow(iRowNo);
            headingBRSBookRow.createCell(0).setCellValue(prop.getString("bcad.add.referral"));
            headingBRSBookRow.getCell(0).setCellStyle(csPlanLeftRight);

            cell = headingBRSBookRow.createCell(1);
            cell.setCellValue((cumulativeAIFSeriesBCAD.getBCADUpfrontValue().floatValue()));
            bcadOption1+=cumulativeAIFSeriesBCAD.getBCADUpfrontValue();
            headingBRSBookRow.getCell(1).setCellStyle(csDF);

            iRowNo = sheetSummary.getLastRowNum() + 1;
            headingBRSBookRow = sheetSummary.createRow(iRowNo);
            headingBRSBookRow.createCell(0).setCellValue("Total");
            headingBRSBookRow.getCell(0).setCellStyle(csPlanLeftRight);

            cell = headingBRSBookRow.createCell(1);
            cell.setCellValue((bcadOption1.floatValue()));
            headingBRSBookRow.getCell(1).setCellStyle(csDF);

            iRowNo = sheetSummary.getLastRowNum() + 1;
            headingBRSBookRow = sheetSummary.createRow(iRowNo);
            headingBRSBookRow.createCell(0).setCellValue(prop.getString("bcad.less.adjustments.option1"));
            headingBRSBookRow.getCell(0).setCellStyle(csPlanLeftRight);

            cell = headingBRSBookRow.createCell(1);
            cell.setCellValue(-(cumulativeAIFSeriesBCAD.getAgainstUpfrontOriginal().floatValue()));
            headingBRSBookRow.getCell(1).setCellStyle(csDF);

            iRowNo = sheetSummary.getLastRowNum() + 1;
            headingBRSBookRow = sheetSummary.createRow(iRowNo);
            headingBRSBookRow.createCell(0).setCellValue(prop.getString("bcad.balance.carried.over"));
            headingBRSBookRow.getCell(0).setCellStyle(csDFBoldFour);

            Double option1Balance=bcadOption1-cumulativeAIFSeriesBCAD.getAgainstUpfrontOriginal();
            cell = headingBRSBookRow.createCell(1);
            cell.setCellValue((option1Balance.floatValue()));
            headingBRSBookRow.getCell(1).setCellStyle(csDFBoldFour);

            iRowNo = sheetSummary.getLastRowNum() + 4;
            headingBRSBookRow = sheetSummary.createRow(iRowNo);
            headingBRSBookRow.createCell(0).setCellValue(prop.getString("aif.blend.upfront.fee"));
            headingBRSBookRow.getCell(0).setCellStyle(csPlainNoBorder);

            iRowNo = sheetSummary.getLastRowNum() + 1;
            headingBRSBookRow = sheetSummary.createRow(iRowNo);
            headingBRSBookRow.createCell(0).setCellValue("Particulars");
            headingBRSBookRow.getCell(0).setCellStyle(cs);

            cell = headingBRSBookRow.createCell(1);
            cell.setCellValue("Amount");
            cell.setCellStyle(cs);

            iRowNo = sheetSummary.getLastRowNum() + 1;
            headingBRSBookRow = sheetSummary.createRow(iRowNo);
            headingBRSBookRow.createCell(0).setCellValue(prop.getString("aif.blend.referral.fund"));
            headingBRSBookRow.getCell(0).setCellStyle(csPlanLeftRight);

            cell = headingBRSBookRow.createCell(1);
            cell.setCellValue((aifDistributorOpen.getOpeningBalFee()));
            aifBlendTotal=aifDistributorOpen.getOpeningBalFee().doubleValue();
            cell.setCellStyle(csDF);

            iRowNo = sheetSummary.getLastRowNum() + 1;
            headingBRSBookRow = sheetSummary.createRow(iRowNo);
            headingBRSBookRow.createCell(0).setCellValue(prop.getString("aif.blend.less.adjust.trail"));
            headingBRSBookRow.getCell(0).setCellStyle(cs);

            cell = headingBRSBookRow.createCell(1);
            cell.setCellValue("");
            cell.setCellStyle(cs);

            iRowNo = sheetSummary.getLastRowNum() + 1;
            headingBRSBookRow = sheetSummary.createRow(iRowNo);
            headingBRSBookRow.createCell(0).setCellValue(sMonthStart + " to " + sMonthEnd);
            headingBRSBookRow.getCell(0).setCellStyle(csPlanLeftRight);

            cell = headingBRSBookRow.createCell(1);
            cell.setCellValue((cumulativeAIFSeriesBCAD.getAIFBlendValue().floatValue()));
            aifBlendTotal+=cumulativeAIFSeriesBCAD.getAIFBlendValue().floatValue();
            cell.setCellStyle(csDF);

            iRowNo = sheetSummary.getLastRowNum() + 1;
            headingBRSBookRow = sheetSummary.createRow(iRowNo);
            headingBRSBookRow.createCell(0).setCellValue(prop.getString("aif.blend.upfront.carried.over"));
            headingBRSBookRow.getCell(0).setCellStyle(csDFBoldFour);

            cell = headingBRSBookRow.createCell(1);
            cell.setCellValue((aifBlendTotal.floatValue()));
            cell.setCellStyle(csDFBoldFour);



            sheetSummary.setDefaultColumnWidth(16);

            //sheetSummary.setColumnWidth(0, 25);


        } catch (Exception e) {
            System.out.println(e);
        }
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

    private Date minimumDate(Date startDate) {
        Calendar calendar = null;

        calendar = Calendar.getInstance();
        calendar.setTime(startDate);
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
        return calendar.getTime();
    }

    private Date openingDate(Date startDate) {
        Calendar calendar = null;

        calendar = Calendar.getInstance();
        calendar.setTime(startDate);
        calendar.set(Calendar.MONTH,-1);
        return calendar.getTime();
    }

}
