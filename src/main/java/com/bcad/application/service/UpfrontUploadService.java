package com.bcad.application.service;

import com.bcad.application.config.PropertiesUtils;
import com.bcad.application.domain.*;
import com.bcad.application.repository.*;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.io.IOUtils;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
@Transactional
public class UpfrontUploadService {

    @PersistenceContext
    EntityManager entityManager;

    private List<MakerUpfrontMaster> listMakerUpfrontMasterDOs = new ArrayList<>();
    private List<UpfrontLogBean> upfrontLogBeans = new ArrayList<>();

    private final UpfrontMasterRepository upfrontMasterRepository;
    private final PMSClientMasterRepository pmsClientMasterRepository;
    private final AIFClientMasterRepository aifClientMasterRepository;
    private final MakerUpfrontMasterRepository makerUpfrontMasterRepository;
    private final InvestmentMasterRepository investmentMasterRepository;
    private final FileUploadUpfrontRepository fileUploadUpfrontRepository;
    private final FileTypeRepository fileTypeRepository;
    private final FeeTrailUpfrontTransRepository feeTrailUpfrontTransRepository;

    public UpfrontUploadService(UpfrontMasterRepository upfrontMasterRepository, PMSClientMasterRepository pmsClientMasterRepository,
                                AIFClientMasterRepository aifClientMasterRepository, MakerUpfrontMasterRepository makerUpfrontMasterRepository, InvestmentMasterRepository investmentMasterRepository,
                                FileUploadUpfrontRepository fileUploadUpfrontRepository, FileTypeRepository fileTypeRepository,FeeTrailUpfrontTransRepository feeTrailUpfrontTransRepository) {
        this.upfrontMasterRepository = upfrontMasterRepository;
        this.pmsClientMasterRepository = pmsClientMasterRepository;
        this.aifClientMasterRepository = aifClientMasterRepository;
        this.makerUpfrontMasterRepository = makerUpfrontMasterRepository;
        this.investmentMasterRepository = investmentMasterRepository;
        this.fileUploadUpfrontRepository = fileUploadUpfrontRepository;
        this.fileTypeRepository = fileTypeRepository;
        this.feeTrailUpfrontTransRepository = feeTrailUpfrontTransRepository;
    }

    private byte[] fileStream;
    private boolean enableSave = true;
    private boolean enableDownload = true;
    private String fileName;
    private List<String> fileDownload = new ArrayList<String>();

    public byte[] getFileStream() {
        return fileStream;
    }

    public void setFileStream(byte[] fileStream) {
        this.fileStream = fileStream;
    }

    public boolean isEnableSave() {
        return enableSave;
    }

    public void setEnableSave(boolean enableSave) {
        this.enableSave = enableSave;
    }

    public boolean isEnableDownload() {
        return enableDownload;
    }

    public void setEnableDownload(boolean enableDownload) {
        this.enableDownload = enableDownload;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    PropertiesConfiguration prop = new PropertiesUtils().getProperties("application.properties");
    private final Logger log = LoggerFactory.getLogger(UpfrontUploadService.class);

    public FileUploadUpfront uploadUpfrontFile(Date startDate, Date endDate, String fileType, MultipartFile multipartFile) throws IOException {

        FileUploadUpfront fileUploadUpfront = new FileUploadUpfront();
        DateFormat dateFormat = new SimpleDateFormat("MMM yyyy");
        String sStartTime = dateFormat.format(startDate);
        String sEndTime = dateFormat.format(endDate);
        String sFinal = sStartTime + "_to_" + sEndTime;
        setFileName(multipartFile.getOriginalFilename());
        String sPath="";

        List<UpfrontMaster> upfrontValidate = new ArrayList<UpfrontMaster>();
        if (fileType.contains(prop.getString("fee.upfront.file.name"))) {
            try {
                upfrontValidate = upfrontMasterRepository.findByTransDateBetweenAndIsDeleted(startDate, endDate, 0);
            } catch (Exception e) {
                System.out.println(e);
            }
            if (upfrontValidate.size() == 0) {
                String sFilesDirectory = prop.getString("fee.file.folder") + "DFA Backup\\\\"
                    + prop.getString("fee.upfront.file.name") + "\\\\" + sFinal;
                File dirFiles = new File(sFilesDirectory);
                dirFiles.mkdirs();
                fileStream = IOUtils.toByteArray(multipartFile.getInputStream());
                File sFile = new File(dirFiles, fileName);
                sPath=sFile.toString();
                writeFile(fileStream, sFile);
                fileUploadUpfront = readUprontFile(sFile, startDate, endDate);

            } else {
                fileUploadUpfront.setCode("202");
                System.out.println(fileUploadUpfront);
                return fileUploadUpfront;
                // UnifiJsfUtils.addErrorMessage("file already exist for this selected date");

            }

            if(fileUploadUpfront.getCode() == null)
                fileUploadUpfront= fileUploadUpfrontRepository.findByStroreFileLocationAndIsDeleted(sPath,0);


        }

        System.out.println(fileUploadUpfront.toString() + "good");
        return fileUploadUpfront;

    }

    private FileUploadUpfront readUprontFile(File sFile, Date startDate, Date endDate) throws IOException {
        enableSave =true;
        enableDownload=true;

        log.info("In DataUploadEditForm readPmsNavFile() method");
        // Excel to database
        FileUploadUpfront fileUploadUpfront = new FileUploadUpfront();
        int iPhysNumOfCells;
        FileInputStream fis = new FileInputStream(sFile);
        HSSFWorkbook workbook = new HSSFWorkbook(fis);

        HSSFSheet sheet = workbook.getSheetAt(0);
        int iPutNxtDetailsToDB = 0;
        Iterator<Row> rowIterator = sheet.iterator();
        listMakerUpfrontMasterDOs = new ArrayList<MakerUpfrontMaster>();
        upfrontLogBeans = new ArrayList<>();
        MakerUpfrontMaster makerUpfrontMaster = new MakerUpfrontMaster();
        Object dateObj = null;
        int iTotRowInserted = 0;
        boolean result = false;
        boolean sResult = false;
        UpfrontLogBean upfrontLog = new UpfrontLogBean();
        while (rowIterator.hasNext()) {
            upfrontLog = new UpfrontLogBean();
            makerUpfrontMaster = new MakerUpfrontMaster();
            Row row = rowIterator.next();
            Row row2 = row;
            iTotRowInserted++;
            Iterator<Cell> cellIterator = row.cellIterator();

            int iPos = 0;
            int iTotConToMeet = 8;
            int iTotConMet = 0;
            int iConCheckNow = 0;
            String sConCheck = "";
            iPhysNumOfCells = row.getPhysicalNumberOfCells();

            int sMode = 0;

            // System.out.println("Blank"+iPhysNumOfCells);
            while (cellIterator.hasNext() && iPutNxtDetailsToDB == 0) {
                Cell cell = cellIterator.next();

                if (iPutNxtDetailsToDB == 0) {
                    if (iPos == 0)
                        sConCheck = "Strategy";
                    if (iPos == 1)
                        sConCheck = "Code";
                    if (iPos == 2)
                        sConCheck = "Strategy";
                    if (iPos == 3)
                        sConCheck = "RM";
                    if (iPos == 4)
                        sConCheck = "Trans. Date";
                    if (iPos == 5)
                        sConCheck = "Intial Fund";
                    if (iPos == 6)
                        sConCheck = "Additional Fund";
                    if (iPos == 7)
                        sConCheck = "Capital Payout";
                    if (iPos == 8)
                        sConCheck = "  Profit Payout";
                    if (cell.getStringCellValue().trim().equals(sConCheck.trim()))
                        iTotConMet++;

                    iPos++;

                    if (iTotConToMeet == iTotConMet) {
                        iPutNxtDetailsToDB = 1;
                        iConCheckNow = 1;
                    }
                }
            }
            String sCodeScheme = "";
            String sCode = "";
            String sName = "";
            String investName = "";
            String distRm = "";
            Date transDate = null;
            Float initFund = 0.0f;
            Float addFund = 0.0f;
            Float capitalPay = 0.0f;
            Float profitPay = 0.0f;
            Integer clientCode = 0;
            PMSClientMaster clientMaster = null;
            AIFClientMaster aifClientMaster = null;
            DistributorMaster distributorMaster = null;
            RelationshipManager relationshipManager = null;
            InvestmentMaster investmentMaster = null;
            DataFormatter formatter = new DataFormatter();
            if (iPhysNumOfCells != 0 && iPhysNumOfCells != 7) {

                if (iPutNxtDetailsToDB == 1 && iConCheckNow == 0) {

                    while (cellIterator.hasNext()) {
                        Cell cell = cellIterator.next();

                        switch (cell.getCellTypeEnum()) {
                            case STRING:

                                sName = row.getCell(0).getStringCellValue();
                                sCodeScheme = row.getCell(1).getStringCellValue();
                                investName = row.getCell(2).getStringCellValue();
                                distRm = row.getCell(3).getStringCellValue();
                                transDate = row.getCell(4).getDateCellValue();
                                break;
                            case NUMERIC:
                                initFund = (float) row.getCell(5).getNumericCellValue();

                                addFund = (float) row.getCell(6).getNumericCellValue();
                                capitalPay = (float) row.getCell(7).getNumericCellValue();
                                profitPay = (float) row.getCell(8).getNumericCellValue();
                                break;
                            default:

                        }
                    }


                    try {

                        String sSplits[] = sCodeScheme.split("(?<=\\D)(?=\\d)|(?<=\\d)(?=\\D)");
                        sCode = sSplits[0];
                        if (!sCode.equals("")) {
                            clientCode = new Double(sCode).intValue();
                            clientMaster = pmsClientMasterRepository.findByClientCode(clientCode.toString());
                            if (clientMaster == null) {
                                aifClientMaster = aifClientMasterRepository.findByClientCode(clientCode.toString());
                            }
                            if (investName.contains("#")) {
                                investName = investName.replace("#", "");
                            }
                            investmentMaster = investmentMasterRepository.findByInvestmentName(investName);

                            if (clientMaster != null || aifClientMaster != null){
                                // navValue=Float.parseFloat(sValue);
                                makerUpfrontMaster.setClientCode(sCodeScheme);
                            makerUpfrontMaster.setClientName(sName);
                            makerUpfrontMaster.setInvestmentMaster(investmentMaster);
                            makerUpfrontMaster.setTransDate(transDate);
                            makerUpfrontMaster.setInitialFund(initFund);
                            makerUpfrontMaster.setAdditionalFund(addFund);
                            makerUpfrontMaster.setCapitalPayout(capitalPay);
                            makerUpfrontMaster.setProfitPayout(profitPay);
                            makerUpfrontMaster.setPmsClientMaster(clientMaster);
                            System.out.println(clientMaster.getClientCode());
                            //	upfrontMasterDO.setFileUploadUpfrontDO(fileUploadUpfrontDO);
                            if (clientMaster != null) {
                                makerUpfrontMaster.setDistributorMaster(clientMaster.getDistributorMaster());
                                makerUpfrontMaster.setRelationshipManagerMaster(clientMaster.getRelationshipManager());
                            } else
                                makerUpfrontMaster.setDistributorMaster(aifClientMaster.getDistributorMaster());
                            listMakerUpfrontMasterDOs.add(makerUpfrontMaster);}
                            else{
                                upfrontLog.setClientCode(clientCode);
                                upfrontLog.setInvestName(investName);
                                if (clientMaster == null && aifClientMaster == null) {
                                    upfrontLog.setsStatus("Client Not Found");
                                } else
                                    upfrontLog.setsStatus("Investment Not Found");
                                upfrontLogBeans.add(upfrontLog);
                                setEnableSave(false);
                            }
                        }

                    } catch (Exception exception) {
                        upfrontLog.setClientCode(clientCode);
                        upfrontLog.setInvestName(investName);
                        if (clientMaster == null && aifClientMaster == null) {
                            upfrontLog.setsStatus("Client Not Found");
                        } else
                            upfrontLog.setsStatus("Investment Not Found");
                        upfrontLogBeans.add(upfrontLog);
                        setEnableSave(false);
                    }


                }

            }

        }
       saveClient(sFile, startDate, endDate);
        if (enableSave == false) {
            fileUploadUpfront.setCode("470");
            fileUploadUpfront.setStatus(prop.getString("fee.file.folder") + "DFA Backup\\\\"+
                prop.getString("bulk.upload.not")+ "\\\\"+fileName);
            System.out.println("upfrontCheck" + fileUploadUpfront);
            setEnableDownload(false);
        }

        return fileUploadUpfront;
    }

    private void saveClient(File sFile, Date startDate, Date endDate) {
        try {
            FileUploadUpfront fileUploadUpfront = new FileUploadUpfront();
            MakerUpfrontMaster upfrontMaster;
            Set<DistributorMaster> distributorMasters;
            Float pmsCommission, aifCommission, commisionAmt;
            List<MakerUpfrontMaster> masterDOs;
            pmsCommission = prop.getFloat("fee.upfront.pms.commission");
            aifCommission = prop.getFloat("fee.upfront.aif.commission");
            masterDOs = new ArrayList<MakerUpfrontMaster>();
            if (enableSave) {

                if (listMakerUpfrontMasterDOs.isEmpty() != true) {
                    fileUploadUpfront = saveUploadedFile(sFile.getPath());
                    distributorMasters = new HashSet<DistributorMaster>();
                    for (MakerUpfrontMaster bean : listMakerUpfrontMasterDOs) {
                        if (bean.getDistributorMaster() != null) {
                            distributorMasters.add(bean.getDistributorMaster());
                        }
                        upfrontMaster = new MakerUpfrontMaster();
                        upfrontMaster.setClientCode(bean.getClientCode());
                        upfrontMaster.setClientName(bean.getClientName());
                        upfrontMaster.setDistributorMaster(bean.getDistributorMaster());
                        upfrontMaster.setRelationshipManagerMaster(bean.getRelationshipManagerMaster());
                        upfrontMaster.setInvestmentMaster(bean.getInvestmentMaster());
                        upfrontMaster.setTransDate(bean.getTransDate());
                        upfrontMaster.setStartDate(startDate);
                        upfrontMaster.setEndDate(endDate);
                        upfrontMaster.setIsDeleted(0);
                        upfrontMaster.setFileUploadUpfront(fileUploadUpfront);
                        upfrontMaster.setInitialFund(bean.getInitialFund());
                        upfrontMaster.setAdditionalFund(bean.getAdditionalFund());
                        upfrontMaster.setCapitalPayout(bean.getCapitalPayout());
                        upfrontMaster.setProfitPayout(bean.getProfitPayout());
                        upfrontMaster.setPmsClientMaster(bean.getPmsClientMaster());
                        if (bean.getInitialFund() > 0) {
                            commisionAmt = bean.getInitialFund() * (pmsCommission / 100);
                            upfrontMaster.setCommissionAmount(commisionAmt);
                        } else {
                            commisionAmt = bean.getAdditionalFund() * (pmsCommission / 100);
                            upfrontMaster.setCommissionAmount(commisionAmt);
                        }
                        if (bean.getDistributorMaster() != null)
                            System.out.println(bean.getDistributorMaster());
                        if ((bean.getDistributorMaster() != null) &&
                            (bean.getDistributorMaster().getDistModelType().equals("Upfront"))) {
                            masterDOs.add(upfrontMaster);
                        }
                        makerUpfrontMasterRepository.save(upfrontMaster);
                    }
                }


               /* RequestContext context = RequestContext.getCurrentInstance();
                context.execute("PF('status').show();");*/
            }
            if (upfrontLogBeans.isEmpty() == false) {
                Set<UpfrontLogBean> hs = new HashSet<>();
                hs.addAll(upfrontLogBeans);
                upfrontLogBeans.clear();
                upfrontLogBeans.addAll(hs);
                upfrontCreate(upfrontLogBeans);
                log.error("Upfront file is not uploaded");
                //UnifiJsfUtils.addErrorMessage("Upfront file is not uploaded");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    FileOutputStream fos = null;
    XSSFWorkbook workBook = null;

    private void upfrontCreate(List<UpfrontLogBean> upfrontLogBeans) {
        try {

            String sFilesDirectory = prop.getString("fee.file.folder") + "DFA Backup\\\\"
                + prop.getString("bulk.upload.not");
            File dirFiles = new File(sFilesDirectory);
            dirFiles.mkdirs();

            String file = sFilesDirectory + "\\\\" + fileName;
            fileDownload.add(file);
            System.out.println(file);

            fos = new FileOutputStream(file);
            workBook = new XSSFWorkbook();

            String reportDate = "Upfront";
            XSSFSheet sheet = workBook.createSheet(reportDate);
            CellStyle cs = workBook.createCellStyle();
            CellStyle csRight = workBook.createCellStyle();

            XSSFFont fFont = workBook.createFont();
            fFont.setBold(true);
            cs.setFont(fFont);

            csRight.setAlignment(HorizontalAlignment.RIGHT);

            int iRowNo = sheet.getLastRowNum() + 1;
            XSSFRow headingBRSBookRow = sheet.createRow(iRowNo);
            headingBRSBookRow.createCell(0).setCellValue("Client Code");
            headingBRSBookRow.getCell(0).setCellStyle(cs);
            sheet.autoSizeColumn(0);
            headingBRSBookRow.createCell(1).setCellValue("Investment Name");
            headingBRSBookRow.getCell(1).setCellStyle(cs);
            sheet.autoSizeColumn(1);
            headingBRSBookRow.createCell(2).setCellValue("Status");
            headingBRSBookRow.getCell(2).setCellStyle(cs);
            sheet.autoSizeColumn(2);


            iRowNo = sheet.getLastRowNum() + 1;

            for (UpfrontLogBean bean : upfrontLogBeans) {
                XSSFRow row = sheet.createRow(iRowNo);

                row.createCell(0).setCellValue(bean.getClientCode());
                sheet.autoSizeColumn(0);
                System.out.println(bean.getClientCode());
                row.createCell(1).setCellValue(bean.getInvestName());
                sheet.autoSizeColumn(1);
                System.out.println(bean.getsStatus());
                row.createCell(2).setCellValue(bean.getsStatus());
                sheet.autoSizeColumn(2);
                iRowNo++;
            }
            workBook.write(fos);

        } catch (Exception e) {
            System.out.println(e);
        } finally {
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

    private void writeFile(byte[] fileStream, File sFile) throws IOException {
        InputStream in;

        System.out.println(sFile);
        in = new ByteArrayInputStream(fileStream);
        OutputStream out = new FileOutputStream(sFile);
        byte buf[] = new byte[1024];
        int len;
        while ((len = in.read(buf)) > 0) {
            out.write(buf, 0, len);
        }
        in.close();
        out.flush();
        out.close();
        System.out.println("File Uploading is Completed");
    }

    public FileUploadUpfront saveUploadedFile(String filePath) {
        String loggedUser;
        FileUploadUpfront fileUploadUpfront;
        FileType fileType;

        fileUploadUpfront = new FileUploadUpfront();
        fileType = fileTypeRepository.findByFileType("Upfront");

        fileUploadUpfront = new FileUploadUpfront();
        fileUploadUpfront.setStroreFileLocation(filePath);
        fileUploadUpfront.setFileType(fileType);
        fileUploadUpfront.setUploadApproved(0);
        fileUploadUpfront.setIsDeleted(0);
        fileUploadUpfront=fileUploadUpfrontRepository.save(fileUploadUpfront);
        return fileUploadUpfront;


    }

    public FileUploadUpfront approveFile(FileUploadUpfront fileUploadUpfront) {

        fileUploadUpfront = approveUpfrontUpload(fileUploadUpfront);

        return fileUploadUpfront;
    }

    private FileUploadUpfront approveUpfrontUpload(FileUploadUpfront fileUploadUpfront) {

        List<MakerUpfrontMaster> masterDOsTemp;
        List<MakerUpfrontMaster> masterDOs = new ArrayList<MakerUpfrontMaster>();

        int result = 0;
        try {
            Query query = entityManager.createNativeQuery("insert into upfront_master select * from maker_upfront_master mu where mu.file_uploaded_id=" + fileUploadUpfront.getId());
            query.executeUpdate();

            masterDOsTemp = makerUpfrontMasterRepository.findByFileUploadUpfront(fileUploadUpfront);

            for (MakerUpfrontMaster oneBean : masterDOsTemp) {

                if ((oneBean.getDistributorMaster()!= null) && ((oneBean.getDistributorMaster().getDistModelType().equals("Upfront")
                    && !oneBean.getInvestmentMaster().getInvestmentName().equals("BCAD")) || (oneBean.getInvestmentMaster().getInvestmentName().equals("BCAD")))) {
                    System.out.println(oneBean.getDistributorMaster().getId() + oneBean.getId());
                    masterDOs.add(oneBean);
                }
            }

            updateTrailUpfront(masterDOs);

            query = entityManager.createNativeQuery("update file_upload_upfront f set f.upload_approved=1 where f.id=" + fileUploadUpfront.getId());
            result=query.executeUpdate();
        } catch (Exception e) {
            result = 99992;
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return fileUploadUpfront;

    }

    private void updateTrailUpfront(List<MakerUpfrontMaster> masterDOs) {
        FeeTrailUpfrontTrans feeTrailUpfrontTrans;
        System.out.println(masterDOs);

        for (MakerUpfrontMaster upfrontMasterDO : masterDOs) {
            feeTrailUpfrontTrans = new FeeTrailUpfrontTrans();
            feeTrailUpfrontTrans.setDistributoMaster(upfrontMasterDO.getDistributorMaster());
            feeTrailUpfrontTrans.setTransDate(upfrontMasterDO.getTransDate());
            feeTrailUpfrontTrans.setPadiAmount(0f);
            feeTrailUpfrontTrans.setDetailsUpdatedFlag(0);

            if (upfrontMasterDO.getCommissionAmount() != null) {
                feeTrailUpfrontTrans.setUpfrontAmount(-upfrontMasterDO.getCommissionAmount());
                feeTrailUpfrontTrans.setPayableAmount(upfrontMasterDO.getCommissionAmount());
            }
            feeTrailUpfrontTransRepository.save(feeTrailUpfrontTrans);

        }

    }

    public Integer deleteFile(Long id) {
        int result = 0;
        Optional<FileUploadUpfront> fileUploadUpfront=fileUploadUpfrontRepository.findById(id);
        result=deleteUpfrontMaster(fileUploadUpfront.get());
        fileUploadUpfront.get().setIsDeleted(1);
        fileUploadUpfrontRepository.save( fileUploadUpfront.get());

        return result;
    }
    public int deleteUpfrontMaster(FileUploadUpfront fileUploadUpfront){
        int result=0;
        Query query=entityManager.createNativeQuery("update upfront_master f set f.int_deleted=1 where f.file_uploaded_id="+fileUploadUpfront.getId());
        result=query.executeUpdate();


        return result;

    }

    public Integer deleteBCADFile(Long id, Date feeFromDate, Date feeToDate) {

        int result = 0;
        Optional<FileUploadUpfront> fileUploadUpfront=fileUploadUpfrontRepository.findById(id);
        result=deleteUpfrontBCADMaster(fileUploadUpfront.get(),feeFromDate,feeToDate);
        fileUploadUpfront.get().setIsDeleted(1);
        fileUploadUpfrontRepository.save( fileUploadUpfront.get());

        return result;
    }

    private int deleteUpfrontBCADMaster(FileUploadUpfront fileUploadUpfront,Date feeFromDate,Date feeToDate) {
        int result=0;
        Query query=entityManager.createNativeQuery("update upfront_master f set f.int_deleted=1 where f.file_uploaded_id="+fileUploadUpfront.getId());
        result=query.executeUpdate();

        DateFormat dateForm = new SimpleDateFormat("yyyy-MM-dd");
        String sFrom = dateForm.format(feeFromDate);
        String sTo = dateForm.format(feeToDate);

        query=entityManager.createNativeQuery("update fee_trail_upfront_trans f set f.int_details_updated_flag=1 where f.trans_date between '"+sFrom+"' and '"+sTo+"'");
        result=query.executeUpdate();

        return result;
    }
}
