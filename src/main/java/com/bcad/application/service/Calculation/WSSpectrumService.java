package com.bcad.application.service.Calculation;

import com.bcad.application.bean.WSBean;
import com.bcad.application.config.PropertiesUtils;
import com.bcad.application.domain.*;
import com.bcad.application.repository.*;
import com.bcad.application.web.rest.util.StyleUtil;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class WSSpectrumService {

    private final PMSStrategyWiseService pmsStrategyWiseService;
    private final ProductRepository productRepository;
    private final CommissionDefinitionRepository commissionDefinitionRepository;
    private final ProfitShareRepository profitShareRepository;
    private final BCADPMSNavRepository bcadpmsNavRepository;
    private final BCADProfitShareRepository bcadProfitShareRepository;
    private final ClientManagementRepository clientManagementRepository;
    private final PMSClientMasterRepository pmsClientMasterRepository;

    public WSSpectrumService(PMSStrategyWiseService pmsStrategyWiseService, ProductRepository productRepository,
                             CommissionDefinitionRepository commissionDefinitionRepository, ProfitShareRepository profitShareRepository,
                             BCADPMSNavRepository bcadpmsNavRepository,BCADProfitShareRepository bcadProfitShareRepository,
                             ClientManagementRepository clientManagementRepository, PMSClientMasterRepository pmsClientMasterRepository) {
        this.pmsStrategyWiseService = pmsStrategyWiseService;
        this.productRepository = productRepository;
        this.commissionDefinitionRepository = commissionDefinitionRepository;
        this.profitShareRepository =profitShareRepository;
        this.bcadpmsNavRepository = bcadpmsNavRepository;
        this.bcadProfitShareRepository = bcadProfitShareRepository;
        this.clientManagementRepository = clientManagementRepository;
        this.pmsClientMasterRepository = pmsClientMasterRepository;
    }

    PropertiesConfiguration prop = new PropertiesUtils().getProperties("application.properties");
    DateFormat firstDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    SimpleDateFormat sdfDate = new SimpleDateFormat("dd/MM/yyyy");
    public List<WSBean> generateWealthSpeactrum(List<PMSClientMaster> pmsClientMasters, Date startDate, Date endDate, String comments) throws ParseException {
        String sStartDate = sdf.format(startDate);
        String sToDate = sdf.format(endDate);
        String wsDate = sdfDate.format(endDate);
        List<WSBean> wsBeanList = new ArrayList<>();
        WSBean wsBean =new WSBean();

        Float sDistShare = 0.0f;
        String sCode = "";
        for (PMSClientMaster pm : pmsClientMasters) {

                List<PMSNav> pmsNavs = pmsStrategyWiseService.getAllCodeSchemen(pm.getId(), sStartDate, sToDate);

                if (pmsNavs.size() != 0) {
                    for (PMSNav pms : pmsNavs) {
                        wsBean = new WSBean();

                        System.out.println(pms.getCodeScheme());
                        String sSplits[] = pms.getCodeScheme().split("(?<=\\D)(?=\\d)|(?<=\\d)(?=\\D)");
                        sCode = sSplits[0];
                        Integer clientCode = new Double(sCode).intValue();
                        PMSClientMaster pmsClientMaster = pmsClientMasterRepository.findByClientCode(clientCode.toString());
                        wsBean.setClientCode(pms.getCodeScheme());
                        wsBean.setDateMonth(wsDate);

                        Integer pmsInvest = (firstDateFormat.parse(prop.getString("pms.strategy.date")).after(pms.getInvestmentDate())) ? 0 : 1;
                        Product productId = productRepository.findByProductName(pms.getInvestmentMaster().getInvestmentName());
                        Float pmsProfit = profitShareRepository.getProfitShare(pm.getId(), pms.getInvestmentMaster().getId(), sStartDate, sToDate);
                       if(pmsProfit!=null)
                           System.out.println(pmsProfit);
                        pmsProfit = (pmsProfit == null) ? 0.0f : pmsProfit;
                        CommissionDefinition commissionDefinition1 = new CommissionDefinition();
                        if (pmsClientMaster.getDistributorMaster() != null) {
                            if (!productId.getProductName().equals("BCAD")) {
                                commissionDefinition1 = commissionDefinitionRepository.getPMSInvestmentDateCalc(pmsClientMaster.getDistributorMaster().getId(), pmsInvest, productId.getId());
                                sDistShare = (pms.getCalcPmsNav() * commissionDefinition1.getNavComm()) / 100;
                                sDistShare += (pmsProfit * commissionDefinition1.getProfitComm()) / 100;
                            } else {
                                commissionDefinition1 = commissionDefinitionRepository.getBCADInvestmentDateCalc(pmsClientMaster.getDistributorMaster().getId(), pmsInvest, productId.getId());
                                sDistShare = (pms.getCalcPmsNav() * commissionDefinition1.getNavComm()) / 100;
                                sDistShare += (pmsProfit * commissionDefinition1.getProfitComm()) / 100;
                            }
                            wsBean.setPmsNav(sDistShare);
                            wsBean.setComments(comments);
                            wsBeanList.add(wsBean);
                        } else {
                            wsBean.setPmsNav(0.0f);
                            wsBean.setComments(comments);
                            wsBeanList.add(wsBean);
                        }


                    }

                }

                ClientManagement clientManagement = clientManagementRepository.findByClientCode(pm.getClientCode());

                List<BCADPMSNav> calculation = bcadpmsNavRepository.findByClientManagementAndSelectedStartDateBetweenAndIsDeleted(clientManagement, startDate, endDate, 0);
                BCADProfitShare performanceCalc = bcadProfitShareRepository.findByClientManagementAndReceiptDateBetweenAndIsDeleted(clientManagement, startDate, endDate, 0);
                if (calculation.size() != 0) {
                    wsBean = new WSBean();
                    Float navManagement = 0f;
                    Float performanceFee = 0f;
                    wsBean.setClientCode(calculation.get(0).getCodeScheme());
                    if (clientManagement.getDistributorMaster() != null) {
                        CommissionDefinition commissionDefinition = commissionDefinitionRepository.findCommissions(clientManagement.getDistributorMaster().getId(), 1L);
                        Float distributorCommission = commissionDefinition.getDistributorComm() / 100;
                        for (BCADPMSNav nav : calculation) {
                            navManagement += nav.getCalcPmsNav();

                        }

                        if (performanceCalc != null)
                            performanceFee = (float) performanceCalc.getProfitShareIncome();


                        wsBean.setPmsNav((navManagement + performanceFee) * distributorCommission);
                        wsBean.setDateMonth(wsDate);
                        wsBean.setComments(comments);
                        wsBeanList.add(wsBean);
                    } else {
                        wsBean.setPmsNav(0f);
                        wsBean.setDateMonth(wsDate);
                        wsBean.setComments(comments);
                        wsBeanList.add(wsBean);

                    }
                }


        }
        return wsBeanList;
    }

    FileOutputStream fos = null;
    HSSFWorkbook workBook = null;
    public void generateClientReport(List<WSBean> wsBeanList) throws FileNotFoundException {
        String file = "";
        String filePath = prop.getString("fee.file.folder") + "DFA Backup/" +
            prop.getString("file.bcad.output");
        workBook = new HSSFWorkbook();
        File dirFiles = new File(filePath);
        dirFiles.mkdirs();
        file = filePath + "/" +   "Client Report " +  ".xls";
        fos = new FileOutputStream(file);
try {

    HSSFFont defaultFont = workBook.createFont();
    defaultFont.setFontHeightInPoints((short) 11);
    defaultFont.setFontName("Calibri");

    CellStyle cs = workBook.createCellStyle();

    CellStyle csDF = StyleUtil.getStyleDataFormat(workBook);

    csDF.setFont(defaultFont);

    HSSFFont fFont = workBook.createFont();
    fFont.setFontHeightInPoints((short) 11);
    fFont.setFontName("Calibri");
    fFont.setBold(true);
    cs.setFont(fFont);

    HSSFFont fFontNoBold = workBook.createFont();
    fFontNoBold.setFontHeightInPoints((short) 11);
    fFontNoBold.setFontName("Calibri");

    CellStyle csHorVerCenter = workBook.createCellStyle();
    CellStyle csPlanLeftRight = workBook.createCellStyle();

    // Format sheet
    csHorVerCenter.setAlignment(HorizontalAlignment.CENTER);
    csHorVerCenter.setVerticalAlignment(VerticalAlignment.CENTER);
    csHorVerCenter.setFont(fFont);

    csPlanLeftRight.setFont(fFontNoBold);

    DecimalFormat decimalFormat = new DecimalFormat("#.0000");
    HSSFSheet sheet = workBook.createSheet("Client");
    int iRowNo = sheet.getLastRowNum() + 1;
    HSSFRow headingBRSBookRow = sheet.createRow(iRowNo);
    headingBRSBookRow.setHeightInPoints(25);
    headingBRSBookRow.createCell(0).setCellValue("ClientId");
    headingBRSBookRow.getCell(0).setCellStyle(csHorVerCenter);
    sheet.autoSizeColumn(0);

    headingBRSBookRow.createCell(1).setCellValue("Trandate");
    headingBRSBookRow.getCell(1).setCellStyle(csHorVerCenter);
    sheet.autoSizeColumn(1);

    headingBRSBookRow.createCell(2).setCellValue("Incentiveamt");
    headingBRSBookRow.getCell(2).setCellStyle(csHorVerCenter);
    sheet.autoSizeColumn(2);

    headingBRSBookRow.createCell(3).setCellValue("Remarks");
    headingBRSBookRow.getCell(3).setCellStyle(csHorVerCenter);
    sheet.autoSizeColumn(3);


    iRowNo = sheet.getLastRowNum() + 1;
    for (WSBean wsBean : wsBeanList) {
        String sUcpl ="";
        headingBRSBookRow = sheet.createRow(iRowNo);

        HSSFCell cellTotal = headingBRSBookRow.createCell(0);
        cellTotal.setCellValue(wsBean.getClientCode());
        cellTotal.setCellStyle(csPlanLeftRight);
        //sheet.autoSizeColumn(0);

        cellTotal = headingBRSBookRow.createCell(1);
        cellTotal.setCellValue((wsBean.getDateMonth()));
        cellTotal.setCellStyle(csPlanLeftRight);
        //sheet.autoSizeColumn(1);

        sUcpl = decimalFormat.format(wsBean.getPmsNav());
        cellTotal = headingBRSBookRow.createCell(2);
        cellTotal.setCellValue(Double.parseDouble(sUcpl));
        cellTotal.setCellStyle(csDF);
        cellTotal.setCellType(CellType.NUMERIC);
        //sheet.autoSizeColumn(2);


        cellTotal = headingBRSBookRow.createCell(3);
        cellTotal.setCellValue((wsBean.getComments()));
        cellTotal.setCellStyle(csPlanLeftRight);
        //sheet.autoSizeColumn(3);

        iRowNo++;
    }
    workBook.write(fos);
}catch (Exception e) {
    System.out.println(e);

} finally {
    try {
        fos.flush();
    } catch (IOException e) {
        System.out.println(e);

    }
    try {
        fos.close();
    } catch (IOException e) {

    }
    try {
        workBook.close();

    } catch (IOException e) {
        System.out.println(e);


    }
}
    }
}
