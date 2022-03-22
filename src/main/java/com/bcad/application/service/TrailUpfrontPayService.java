package com.bcad.application.service;

import com.bcad.application.config.PropertiesUtils;
import com.bcad.application.domain.*;
import com.bcad.application.repository.GenericPayTrailUpfrontRepository;
import com.bcad.application.repository.TrailupfrontpayRepository;
import com.bcad.application.web.rest.util.StyleUtil;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.io.FileUtils;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.openxml4j.opc.internal.ZipHelper;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.stereotype.Service;

import java.io.*;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Service
public class TrailUpfrontPayService {

    private final TrailupfrontpayRepository trailupfrontpayRepository;
    private final GenericPayTrailUpfrontRepository genericPayTrailUpfrontRepository;

    public TrailUpfrontPayService(TrailupfrontpayRepository trailupfrontpayRepository,
                                  GenericPayTrailUpfrontRepository genericPayTrailUpfrontRepository) {
        this.trailupfrontpayRepository = trailupfrontpayRepository;
        this.genericPayTrailUpfrontRepository = genericPayTrailUpfrontRepository;
    }

    //List<String> fileDownload = new ArrayList<>();

    PropertiesConfiguration prop = new PropertiesUtils().getProperties("application.properties");

    public void trailUpfrontPaid(Date startDate, Date endDate, RelationshipManager relationshipManager, DistributorMaster distributorMaster, List<String> fileDownload) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String payStart  = sdf.format(startDate);
        String payEnd  = sdf.format(endDate);

        List<PaymentTrailUpfrontBean> paymentTrailUpfrontBeans = new ArrayList<PaymentTrailUpfrontBean>();
        PaymentTrailUpfrontBean bean = null;

        List<Trailupfrontpay> trailupfrontpays = trailupfrontpayRepository.getTrailUpfrontPay(distributorMaster.getId(), payStart,
            payEnd);
        List<GenericPayTrailUpfront> genericPayTrailUpfronts = genericPayTrailUpfrontRepository.getTrailUpfrontPay(distributorMaster.getId(), payStart,
            payEnd);

        for (Trailupfrontpay pay : trailupfrontpays) {
            bean = new PaymentTrailUpfrontBean();

            bean.setBankName(pay.getBankName());
            bean.setChequeDate(pay.getChequeDate());
            bean.setChequeNo(pay.getChequeNo());
            bean.setPayAmount(pay.getPaymentAmount());
            bean.setPayDate(pay.getPaymentDate());
            bean.setPayType(pay.getPaymentType());
            bean.setProductName("AIF & PMS");
            paymentTrailUpfrontBeans.add(bean);

        }
        for(GenericPayTrailUpfront genericPay : genericPayTrailUpfronts){
            bean = new PaymentTrailUpfrontBean();

            bean.setBankName(genericPay.getBankName());
            bean.setChequeDate(genericPay.getChequeDate());
            bean.setChequeNo(genericPay.getChequeNo());
            bean.setPayAmount(genericPay.getPayAmount());
            bean.setPayDate(genericPay.getPaymentDate());
            bean.setPayType(genericPay.getPayType());
            bean.setProductName(genericPay.getProduct().getProductName());
            paymentTrailUpfrontBeans.add(bean);

        }
        paymentExcel(paymentTrailUpfrontBeans,distributorMaster, relationshipManager,startDate,endDate,fileDownload);
    }

    FileOutputStream fos = null;
    HSSFWorkbook workBook = null;
    private void paymentExcel(List<PaymentTrailUpfrontBean> paymentTrailUpfrontBeans, DistributorMaster distributorMaster,
                              RelationshipManager relationshipManager, Date startDate, Date endDate, List<String> fileDownload) {

        try {

            DateFormat dateFormat = new SimpleDateFormat("MMM yyyy");
            String sStartTime = dateFormat.format(startDate);
            String sEndTime = dateFormat.format(endDate);
            String sFinal = sStartTime + "_to_" + sEndTime;
            String file = "";
            if (relationshipManager != null) {
                String sFilesDirectory = prop.getString("fee.file.folder") + "DFA Backup\\\\"
                    + prop.getString("rm.paid.amt") + "\\\\" + relationshipManager.getRmName();
                 FileUtils.deleteDirectory(new File(sFilesDirectory));
                File dirFiles = new File(sFilesDirectory);

                dirFiles.mkdirs();
                file = sFilesDirectory + "\\\\" + distributorMaster.getDistName() + " " + sStartTime + "_to_" + sEndTime
                    + ".xls";
                fileDownload.add(prop.getString("fee.file.folder") + "DFA Backup\\\\" + prop.getString("rm.paid.amt"));
            } else {
                String sFilesDirectory = prop.getString("fee.file.folder") + "DFA Backup\\\\"
                    + prop.getString("pay.trail.file") + "\\\\" + sFinal;
                File dirFiles = new File(sFilesDirectory);
                dirFiles.mkdirs();
                file = sFilesDirectory + "\\\\" + distributorMaster.getDistName() + " Payments" + " " + sStartTime + "_to_" + sEndTime
                    + ".xls";
                fileDownload.add(file);
            }

            fos = new FileOutputStream(file);
            workBook = new HSSFWorkbook();

            String reportDate = dateFormat.format(startDate);
            HSSFSheet sheet = workBook.createSheet("Payment");
            Float sAmount = 0f;

            sheet.setZoom(90);
            sheet.setDisplayGridlines(false);

            HSSFFont defaultFont = workBook.createFont();
            defaultFont.setFontHeightInPoints((short) 11);
            defaultFont.setFontName("Calibri");

            CellStyle cs = workBook.createCellStyle();
            CellStyle csRight = workBook.createCellStyle();
            CellStyle csRightLeftRight = workBook.createCellStyle();

            // Format sheet
            CellStyle csHorVerCenter = workBook.createCellStyle();
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
            // csHorVerCenter.setFont(defaultFont);
            csDF.setFont(defaultFont);
            csPlain.setFont(defaultFont);
            // csDFBold.setFont(defaultFont);
            csPerc.setFont(defaultFont);

            csPlainNoBorder.setFont(fFont);
            csPlainNoBorder.setBorderBottom(BorderStyle.NONE);
            csPlainNoBorder.setBorderTop(BorderStyle.NONE);
            csPlainNoBorder.setBorderLeft(BorderStyle.NONE);
            csPlainNoBorder.setBorderRight(BorderStyle.NONE);

            HSSFRow rowUserName = sheet.createRow(sheet.getLastRowNum() + 1);
            rowUserName.createCell(0).setCellValue(distributorMaster.getDistName().toUpperCase());
            rowUserName.getCell(0).setCellStyle(csPlainNoBorder);// cs

            HSSFRow durationFrom = sheet.createRow(sheet.getLastRowNum() + 1);
            durationFrom.createCell(0).setCellValue("PAYMENT FOR " + sStartTime + "-" + sEndTime);
            durationFrom.getCell(0).setCellStyle(csPlainNoBorder);// cs
            // Format sheet
            sheet.addMergedRegion(new CellRangeAddress(1, 1, 0, 3));
            sheet.addMergedRegion(new CellRangeAddress(2, 2, 0, 3));

            sheet.setDefaultColumnWidth(18);

            sheet.createFreezePane(0, 6);

            int iRowNo = sheet.getLastRowNum() + 3;
            HSSFRow headingBRSBookRow = sheet.createRow(iRowNo);
            // Format sheet
            headingBRSBookRow.setHeightInPoints(30);

            HSSFCell cellTemp = headingBRSBookRow.createCell(0);
            cellTemp.setCellValue(prop.getString("pay.pay.date"));
            cellTemp.setCellStyle(csHorVerCenter);

            // sheet.autoSizeColumn(0);
            headingBRSBookRow.createCell(1).setCellValue(prop.getString("pay.cheque.no"));
            headingBRSBookRow.getCell(1).setCellStyle(csHorVerCenter);
            // sheet.autoSizeColumn(1);
            headingBRSBookRow.createCell(2).setCellValue(prop.getString("pay.bank.name"));
            headingBRSBookRow.getCell(2).setCellStyle(csHorVerCenter);
            // sheet.autoSizeColumn(2);
            headingBRSBookRow.createCell(3).setCellValue(prop.getString("pay.cheque.date"));
            headingBRSBookRow.getCell(3).setCellStyle(csHorVerCenter);
            // sheet.autoSizeColumn(3);
            headingBRSBookRow.createCell(4).setCellValue(prop.getString("pay.pay.type"));
            headingBRSBookRow.getCell(4).setCellStyle(csHorVerCenter);

            headingBRSBookRow.createCell(5).setCellValue(prop.getString("pay.product.name"));
            headingBRSBookRow.getCell(5).setCellStyle(csHorVerCenter);
            // sheet.autoSizeColumn(4);
            headingBRSBookRow.createCell(6).setCellValue(prop.getString("pay.pay.amount"));
            headingBRSBookRow.getCell(6).setCellStyle(csHorVerCenter);
            /*
             * iRowNo = sheet.getLastRowNum() + 1; HSSFRow blankRow =
             * sheet.createRow(iRowNo);
             */

            iRowNo = sheet.getLastRowNum() + 1;

            DecimalFormat decimalFormat = new DecimalFormat("#.0000");
            for (PaymentTrailUpfrontBean bean : paymentTrailUpfrontBeans) {
                HSSFRow row = sheet.createRow(iRowNo);

                String sPayDate = "";
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

                sPayDate = sdf.format(bean.getPayDate());

                HSSFCell cellReceipt = row.createCell(0);
                csRight.setAlignment(HorizontalAlignment.RIGHT);
                csRightLeftRight = csRight;
                csRightLeftRight.setBorderLeft(BorderStyle.THIN);
                csRightLeftRight.setBorderRight(BorderStyle.THIN);
                cellReceipt.setCellStyle(csRightLeftRight);
                cellReceipt.setCellValue(sPayDate);

                // sheet.autoSizeColumn(0);
                HSSFCell cell = row.createCell(1);
                // cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
                cell.setCellValue(bean.getChequeNo());
                cell.setCellStyle(csPlanLeftRight);

                cell = row.createCell(2);
                cell.setCellStyle(csDF);
                cell.setCellValue(bean.getBankName());

                String sCheque = "";
                sCheque = sdf.format(bean.getChequeDate());
                cell = row.createCell(3);
                csRight.setAlignment(HorizontalAlignment.RIGHT);
                csRightLeftRight = csRight;
                csRightLeftRight.setBorderLeft(BorderStyle.THIN);
                csRightLeftRight.setBorderRight(BorderStyle.THIN);
                cell.setCellStyle(csRightLeftRight);
                cell.setCellValue(sCheque);

                cell = row.createCell(4);
                cell.setCellValue(bean.getPayType());
                cell.setCellStyle(csDF);

                cell = row.createCell(5);
                cell.setCellValue(bean.getProductName());
                cell.setCellStyle(csDF);

                cell = row.createCell(6);
                cell.setCellValue((double) (bean.getPayAmount()));
                cell.setCellType(CellType.NUMERIC);
                cell.setCellStyle(csDF);
                sAmount += bean.getPayAmount();


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

            HSSFRow row = sheet.createRow(sheet.getLastRowNum() + 1);
            row.createCell(0).setCellValue("TOTAL");
            row.getCell(0).setCellStyle(csFour);
            // sheet.autoSizeColumn(0);

            HSSFCell cell = row.createCell(1);
            cell.setCellValue("");
            cell.setCellStyle(csDFBoldFour);

            cell = row.createCell(2);
            cell.setCellValue("");
            cell.setCellStyle(csDFBoldFour);

            cell = row.createCell(3);
            cell.setCellValue("");// sTotal
            cell.setCellStyle(csDFBoldFour);

            cell = row.createCell(4);
            cell.setCellValue("");
            cell.setCellStyle(csDFBoldFour);

            cell = row.createCell(5);
            cell.setCellValue("");
            cell.setCellStyle(csDFBoldFour);

            cell = row.createCell(6);
            cell.setCellValue((double) sAmount);// sMarkup
            cell.setCellType(CellType.NUMERIC);
            cell.setCellStyle(csDFBoldFour);

            workBook.write(fos);
            // }



        } catch (Exception e) {
            System.out.println(e);
        }

        finally {
            try {
                fos.flush();
            } catch (IOException e) {
                System.out.println(e);
                // TODO Auto-generated catch block

            }
            try {
                fos.close();
            } catch (IOException e) {
                System.out.println(e);
                // TODO Auto-generated catch block

            }
            try {
                workBook.close();
            } catch (IOException e) {
                System.out.println(e);
                // TODO Auto-generated catch block

            }
        }


    }

    public void downloadPayment(List<String> fileDownload, ViewPaymentBean viewPaymentBean) throws IOException {


        DateFormat dateFormat = new SimpleDateFormat("MMM yyyy");
        String sStartTime = dateFormat.format(viewPaymentBean.getStartDate());
        String sEndTime = dateFormat.format(viewPaymentBean.getEndDate());

        String sFinal = sStartTime + "_to_" + sEndTime;
        String sFilesDirectory = "";
        if (viewPaymentBean.getDistributorMasterList()==null) {

            sFilesDirectory = prop.getString("fee.file.folder") + "DFA Backup\\\\" + prop.getString("rm.paid.amt.zip");
            File dirFiles = new File(sFilesDirectory);
            dirFiles.delete();
            dirFiles.mkdirs();
        }
       /* if (viewPaymentBean.getDistributorMasterList().isEmpty() == false && viewPaymentBean.getRelationshipManagerList().isEmpty() == false) {
            sFilesDirectory = prop.getString("fee.file.folder") + "DFA Backup\\\\" + prop.getString("rm.paid.amt.zip")
                + "\\\\" + sFinal;
            File dirFiles = new File(sFilesDirectory);
            dirFiles.mkdirs();
        }*/ else {

            sFilesDirectory = prop.getString("fee.file.folder") + "DFA Backup\\\\"
                + prop.getString("pay.trail.file.zip");
            FileUtils.deleteDirectory(new File(sFilesDirectory));
            File dirFiles = new File(sFilesDirectory);
            dirFiles.mkdirs();
        }

       /* Long lTemp = Calendar.getInstance().getTimeInMillis();
        String sSetId = lTemp.toString();*/

        String file = sFilesDirectory + "\\\\Filedownload.zip";

        String filename = "";
        if (viewPaymentBean.getRelationshipManagerList()==null) {
            filename = file;
            byte[] buffer = new byte[1024];

            FileOutputStream fos = new FileOutputStream(file);
            ZipOutputStream zos = new ZipOutputStream(fos);
            int j = fileDownload.size();
            for (int i = 0; i < j; i++) {
                String sFile = fileDownload.get(i).toString();
                File srcFile = new File(sFile);
                FileInputStream fis = new FileInputStream(srcFile);
                zos.putNextEntry(new ZipEntry(srcFile.getName()));
                int length;
                while ((length = fis.read(buffer)) > 0) {
                    zos.write(buffer, 0, length);
                }
                zos.closeEntry();
                fis.close();
            }
            zos.close();
        }
        if (viewPaymentBean.getDistributorMasterList()==null) {
            ZipHelper zippy = new ZipHelper();
            filename = file;
            zippy.zipDir(fileDownload.get(0).toString(), file);
        }
        if (viewPaymentBean.getDistributorMasterList()!=null && viewPaymentBean.getRelationshipManagerList()!=null) {
            ZipHelper zippy = new ZipHelper();
            filename = file;
            zippy.zipDir(fileDownload.get(0).toString(), file);
        }
      /*  InputStream inputStream = new FileInputStream(filename);

        dFile = new DefaultStreamedContent(inputStream, "zip", "file-Download -" + sSetId + ".zip");

        System.out.println("file Download");*/


    }
    class ZipHelper {
        ZipOutputStream zip = null;
        FileOutputStream fW = null;

        public void zipDir(String dirName, String nameZipFile) throws IOException {

            fW = new FileOutputStream(nameZipFile);
            zip = new ZipOutputStream(fW);

            addFolderToZip("", dirName, zip);
            zip.close();
            fW.close();
        }

        private void addFolderToZip(String path, String srcFolder, ZipOutputStream zip) throws IOException {
            File folder = new File(srcFolder);
            if (folder.list().length == 0) {
                addFileToZip(path, srcFolder, zip, true);
            } else {
                for (String fileName : folder.list()) {
                    if (path.equals("")) {
                        addFileToZip(folder.getName(), srcFolder + "/" + fileName, zip, false);
                    } else {
                        addFileToZip(path + "/" + folder.getName(), srcFolder + "/" + fileName, zip, false);
                    }
                }
            }
        }

        private void addFileToZip(String path, String srcFile, ZipOutputStream zip, boolean flag) throws IOException {
            File folder = new File(srcFile);
            if (flag) {
                zip.putNextEntry(new ZipEntry(path + "/" + folder.getName() + "/"));
            } else {
                if (folder.isDirectory()) {
                    addFolderToZip(path, srcFile, zip);
                } else {
                    byte[] buf = new byte[1024];
                    int len;
                    FileInputStream in = new FileInputStream(srcFile);
                    zip.putNextEntry(new ZipEntry(path + "/" + folder.getName()));
                    while ((len = in.read(buf)) > 0) {
                        zip.write(buf, 0, len);
                    }
                }
            }
        }

    }
}
