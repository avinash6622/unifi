package com.bcad.application.service;

import com.bcad.application.config.PropertiesUtils;
import com.bcad.application.domain.*;
import com.bcad.application.repository.*;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.io.IOUtils;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
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

import javax.persistence.*;
import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
@Transactional
public class BCADUploadService {

    private byte[] fileStream;
    private String fileName;
    private boolean enableSavePMSNav =true;
    private Boolean enablePMSDownload=true;
    private List<MasterLogBean> masterLogBean=new ArrayList<>();
    private List<UpfrontLogBean> upfrontLogBeans= new ArrayList<>();
    List<BcadMakerUpfrontMaster> bcadMakerUpfrontMasterList = new ArrayList<BcadMakerUpfrontMaster>();
    List<MakerUpfrontMaster> listMakerUpfrontMasterDOs = new ArrayList<>();

    private List<String> fileDownload = new ArrayList<String>();
    private String statusCode;

    @PersistenceContext
    EntityManager entityManager;

    public byte[] getFileStream() {
        return fileStream;
    }

    public void setFileStream(byte[] fileStream) {
        this.fileStream = fileStream;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public boolean isEnableSavePMSNav() {
        return enableSavePMSNav;
    }

    public void setEnableSavePMSNav(boolean enableSavePMSNav) {
        this.enableSavePMSNav = enableSavePMSNav;
    }

    public Boolean getEnablePMSDownload() {
        return enablePMSDownload;
    }

    public void setEnablePMSDownload(Boolean enablePMSDownload) {
        this.enablePMSDownload = enablePMSDownload;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    private final BCADMakerPMSNavRepository bcadMakerPMSNavRepository;
    private final UploadMasterFilesRepository uploadMasterFilesRepository;
    private final MasterTypeRepository masterTypeRepository;
    private final ClientManagementRepository clientManagementRepository;
    private final ProductRepository productRepository;
    private final BcadUpfrontMasterRepository bcadUpfrontMasterRepository;
    private final BcadMakerUpfrontMasterRepository bcadMakerUpfrontMasterRepository;
    private final BCADMakerProfitShareRepository bcadMakerProfitShareRepository;
    private final BCADProfitShareRepository bcadProfitShareRepository;
    private final UpfrontMasterRepository upfrontMasterRepository;
    private final PMSClientMasterRepository pmsClientMasterRepository;
    private final InvestmentMasterRepository investmentMasterRepository;
    private final FileTypeRepository fileTypeRepository;
    private final FileUploadUpfrontRepository fileUploadUpfrontRepository;
    private final MakerUpfrontMasterRepository makerUpfrontMasterRepository;
    private final CommissionDefinitionRepository commissionDefinitionRepository;

    public BCADUploadService(BCADMakerPMSNavRepository bcadMakerPMSNavRepository, UploadMasterFilesRepository uploadMasterFilesRepository,
                             MasterTypeRepository masterTypeRepository,ClientManagementRepository clientManagementRepository,
                             ProductRepository productRepository,BcadUpfrontMasterRepository bcadUpfrontMasterRepository,
                             BcadMakerUpfrontMasterRepository bcadMakerUpfrontMasterRepository,BCADMakerProfitShareRepository bcadMakerProfitShareRepository,
                             BCADProfitShareRepository bcadProfitShareRepository,UpfrontMasterRepository upfrontMasterRepository,
                             PMSClientMasterRepository pmsClientMasterRepository,InvestmentMasterRepository investmentMasterRepository,FileTypeRepository fileTypeRepository,
                             FileUploadUpfrontRepository fileUploadUpfrontRepository,MakerUpfrontMasterRepository makerUpfrontMasterRepository,
                             CommissionDefinitionRepository commissionDefinitionRepository) {
        this.bcadMakerPMSNavRepository = bcadMakerPMSNavRepository;
        this.uploadMasterFilesRepository = uploadMasterFilesRepository;
        this.masterTypeRepository = masterTypeRepository;
        this.clientManagementRepository = clientManagementRepository;
        this.productRepository = productRepository;
        this.bcadUpfrontMasterRepository = bcadUpfrontMasterRepository;
        this.bcadMakerUpfrontMasterRepository = bcadMakerUpfrontMasterRepository;
        this.bcadMakerProfitShareRepository = bcadMakerProfitShareRepository;
        this.bcadProfitShareRepository = bcadProfitShareRepository;
        this.upfrontMasterRepository = upfrontMasterRepository;
        this.pmsClientMasterRepository = pmsClientMasterRepository;
        this.investmentMasterRepository = investmentMasterRepository;
        this.fileTypeRepository = fileTypeRepository;
        this.fileUploadUpfrontRepository = fileUploadUpfrontRepository;
        this.makerUpfrontMasterRepository = makerUpfrontMasterRepository;
        this.commissionDefinitionRepository = commissionDefinitionRepository;
    }

    PropertiesConfiguration prop = new PropertiesUtils().getProperties("application.properties");
    private final Logger log = LoggerFactory.getLogger(BCADUploadService.class);

    public UploadMasterFiles uploadFiles(Date startDate, Date endDate, String fileType, MultipartFile multipartFile, String product) throws Exception {
        masterLogBean=new ArrayList<>();
        enableSavePMSNav =true;
        enablePMSDownload=true;
        DateFormat dateFormat = new SimpleDateFormat("MMM yyyy");
        String sStartTime = dateFormat.format(startDate);
        String sEndTime = dateFormat.format(endDate);
        String sFinal = sStartTime + "_to_" + sEndTime;

        String sDate = " " + sStartTime;

        String sLoc = fileName.concat(sDate);
        setFileName(multipartFile.getOriginalFilename());
        fileStream = IOUtils.toByteArray(multipartFile.getInputStream());

        UploadMasterFiles uploadMasterFiles = new UploadMasterFiles();
        List<BcadUpfrontMaster> upfrontValidate= new ArrayList<>();
        List<UpfrontMaster> upfrontValidatePMS = new ArrayList<UpfrontMaster>();

        if (fileName.contains(prop.getString("file.fee.pms")) && product.equals("BCAD")) {
            {
                String sFilesDirectory = prop.getString("fee.file.folder") + "DFA Backup\\\\"
                    + product+"\\\\"+prop.getString("file.fee.pms") + "\\\\" + sFinal;
                File dirFiles = new File(sFilesDirectory);
                dirFiles.mkdirs();
                MasterType masterType1= masterTypeRepository.findByFileName(fileName);
                if (fileName.contains(sLoc)) {
                    File sFile = new File(dirFiles, fileName);
                    try {
                        uploadMasterFiles = uploadMasterFilesRepository.findByFileLocationAndIsDeletedAndMasterType(sFile.getPath().toString(), 0,masterType1);
                         } catch (Exception e) {
                        System.out.println(e);
                    }
                    if (uploadMasterFiles == null) {
                        statusCode="";
                        writeFile(fileStream, sFile);
                        System.out.println("File path" + sFile.getPath());
                        List<BCADMakerPMSNav> list = readBCADPmsNavFile(sFile.getPath(), startDate, endDate,product);
                        /**
                         * save when client validation done
                         */
                        if (enableSavePMSNav) {

                                MasterType masterType = masterTypeRepository.findByFileName(prop.getString("file.fee.pms"));
                                Product product1 = productRepository.findByProductName(product);

                                uploadMasterFiles = new UploadMasterFiles();
                                uploadMasterFiles.setFileLocation(sFile.getPath());
                                uploadMasterFiles.setMasterType(masterType);
                                uploadMasterFiles.setIsDeleted(0);
                                uploadMasterFiles.setUploadApproved(0);
                                uploadMasterFiles.setFeeFromDate(startDate);
                                uploadMasterFiles.setFeeToDate(endDate);
                                uploadMasterFiles.setProduct(product1);
                                uploadMasterFilesRepository.save(uploadMasterFiles);
                                for (BCADMakerPMSNav bcadMakerPMSNav : list) {
                                    bcadMakerPMSNav.setFileUpload(uploadMasterFiles);
                                    System.out.println("Start date---" + bcadMakerPMSNav.getSelectedStartDate());
                                    bcadMakerPMSNav.setIsDeleted(0);
                                    bcadMakerPMSNavRepository.save(bcadMakerPMSNav);
                                }
                                // int result = pmsNavRepository.onInsertPmsNav(fileDO);
                                // UnifiJsfUtils.addInfoMessage("Uploaded");


                        }
                        if (enableSavePMSNav == false) {
                            masterCreate(masterLogBean);
                            setEnablePMSDownload(false);
                            System.out.println("checking");
                            uploadMasterFiles = new UploadMasterFiles();
                            uploadMasterFiles.setCode("470");
                            uploadMasterFiles.setStatus(prop.getString("fee.file.folder") + "DFA Backup\\\\"+
                                prop.getString("bulk.upload.not")+ "\\\\"+fileName);
                           // return uploadMasterFiles;
                        }
                    } else {
                        System.out.println("errorr");
                        log.error("file already exist for this selected date");
                        uploadMasterFiles.setCode("409");
                        System.out.println(uploadMasterFiles);
                        return uploadMasterFiles;
                    }
                } else {
                    System.out.println("errorr");
                    uploadMasterFiles.setCode("400");
                    System.out.println(uploadMasterFiles);
                    return uploadMasterFiles;
                    // UnifiJsfUtils.addErrorMessage("Uploaded file is not in correct month");
                }
            }
        }
        if(fileName.contains(prop.getString("fee.upfront.file.name"))&& product.equals("BCAD"))
        {
            upfrontValidate=bcadUpfrontMasterRepository.findByTransDateBetweenAndIsDeleted(startDate,endDate,0);
            upfrontValidatePMS = upfrontMasterRepository.findByTransDateBetweenAndIsDeleted(startDate, endDate, 0);
            String sPath="";
            if(upfrontValidate.isEmpty()==true && upfrontValidatePMS.isEmpty()==true)
            {
                String sFilesDirectory = prop.getString("fee.file.folder") + "DFA Backup\\\\BCAD\\\\"
                    + prop.getString("fee.upfront.file.name") + "\\\\" + sFinal;
                File dirFiles = new File(sFilesDirectory);
                dirFiles.mkdirs();


                File sFile = new File(dirFiles, fileName);
                sPath =sFile.toString();
                    writeFile(fileStream, sFile);
                uploadMasterFiles = readUprontFile(sFile,startDate,endDate);

            }else{
                System.out.println("errorr");
                log.error("file already exist for this selected date");
                uploadMasterFiles.setStatus("File Already Exist");
                uploadMasterFiles.setCode("409");
                System.out.println(uploadMasterFiles);
                return uploadMasterFiles;

            }
            if(uploadMasterFiles.getCode() == null)
            uploadMasterFiles= uploadMasterFilesRepository.findByFileLocationAndIsDeleted(sPath,0);
        }

        if (fileName.contains(prop.getString("file.fee.profit"))&& product.equals("BCAD")) {
            String loggedUser;

            String sFilesDirectory = prop.getString("fee.file.folder") + "DFA Backup\\\\BCAD\\\\"
                + prop.getString("file.fee.profit") + "\\\\" + sFinal;
            File dirFiles = new File(sFilesDirectory);

            dirFiles.mkdirs();
            MasterType masterType1= masterTypeRepository.findByFileName(fileName);
            if (fileName.contains(sLoc)) {
                File sFile = new File(dirFiles, fileName);

                    uploadMasterFiles = uploadMasterFilesRepository.findByFileLocationAndIsDeletedAndMasterType(sFile.getPath().toString(), 0,masterType1);

                if (uploadMasterFiles == null) {
                    writeFile(fileStream, sFile);
                    System.out.println("File path" + sFile.getPath());
                    List<BCADMakerProfitShare> list = readProfitShareFile(sFile.getPath(), startDate,endDate,product);

                    if (list.isEmpty() != true) {
                        MasterType masterType = masterTypeRepository.findByFileName("Profit Share");
                        Product product1 = productRepository.findByProductName(product);

                        uploadMasterFiles = new UploadMasterFiles();
                        uploadMasterFiles.setFileLocation(sFile.getPath());
                        uploadMasterFiles.setMasterType(masterType);
                        uploadMasterFiles.setIsDeleted(0);
                        uploadMasterFiles.setUploadApproved(0);
                        uploadMasterFiles.setFeeFromDate(startDate);
                        uploadMasterFiles.setFeeToDate(endDate);
                        uploadMasterFiles.setProduct(product1);
                        uploadMasterFilesRepository.save(uploadMasterFiles);

                        for (BCADMakerProfitShare bcadMakerProfitShare : list) {
                           // if(bcadMakerProfitShare.getProductName().equals("BCAD")) {
                                bcadMakerProfitShare.setFileUpload(uploadMasterFiles);
                                bcadMakerProfitShare.setStartDate(startDate);
                                bcadMakerProfitShare.setEndDate(endDate);
                                bcadMakerProfitShare.setIsDeleted(0);
                                bcadMakerProfitShareRepository.save(bcadMakerProfitShare);
                           // }
                        }
                        log.info("Uploaded");

                    }
                } else {
                    System.out.println("errorr");
                    uploadMasterFiles.setCode("202");
                    System.out.println(uploadMasterFiles);
                    return uploadMasterFiles;
                    // log.error("Uploaded file already exists, Delete previous file and try again");
                }
            } else {
                System.out.println("errorr");
                uploadMasterFiles.setCode("400");
                System.out.println(uploadMasterFiles);
                return uploadMasterFiles;
                // log.error("Uploaded file is not in correct month");
            }
        }


        return uploadMasterFiles;
    }

    private List<BCADMakerProfitShare> readProfitShareFile(String filePath, Date startDate, Date endDate, String product) throws Exception {

        log.info("In DataUploadEditForm readProfitShareFile() method");
        FileInputStream inputStream = new FileInputStream(filePath);
        Workbook workbook = new XSSFWorkbook(inputStream);
        Sheet firstSheet = workbook.getSheetAt(0);
        Iterator<Row> iterator = firstSheet.iterator();

        List<BCADMakerProfitShare> listMakerProfitShares = new ArrayList<BCADMakerProfitShare>();
        // OnboardingInitiationDO onboardingInitiation = null;
        BCADMakerProfitShare makerProfitShare = null;
        Row nextRow = null;
        // Iterator<Cell> cellIterator = null;
        Cell nextCell = null;
        int i = 0, rowNum = 0;
        Integer clientCode = null;
        boolean result = false;
        Object dateObj = null;
        Integer physicalCellNos = null;
        String inputStr = null, showMsg = null, experience = null;
        Calendar cal = Calendar.getInstance();
        cal.setTime(startDate);
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH) + 1;
        DateFormat df3 = new SimpleDateFormat("MM");
        String sMonth = df3.format(startDate);
        String sValid = "/" + month + "/" + year;
        String tValid="/" + sMonth + "/" + year;

        row: while (iterator.hasNext()) {
            nextRow = iterator.next();
            physicalCellNos = nextRow.getPhysicalNumberOfCells();
            System.out.println("physical--------------->" + physicalCellNos);
            makerProfitShare = new BCADMakerProfitShare();
            if (i >= 4) {
                // cellIterator = nextRow.cellIterator();
                rowNum = i + 1;
                column: for (int cn = 0; cn < 19; cn++) {
                    result = false;
                    nextCell = nextRow.getCell(cn, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);

                    /* else { */
                    switch (cn) {
                        case 0: // candidate First name

                            inputStr = getCellValue(nextCell).toString().trim();
                            if (inputStr.equals("Total")) {
                                break row;
                            } else {
                                System.out.println(inputStr);
                                break;
                            }

                        case 1: // client

                            inputStr = getCellValue(nextCell).toString().trim();
                            clientCode = new Double(inputStr).intValue();
                            ClientManagement clientMasterDO = clientManagementRepository.findByClientCode(clientCode.toString());
                            if(clientMasterDO==null) {
                                log.error("Exception at DataUploadEditForm Profit share Client not found" + clientCode);
                                // log.error("Client Not found " + clientCode);
                                result = true;
                                break row;
                            }
                              else{
                                makerProfitShare.setClientManagement(clientMasterDO);
                                break;}

                        case 2:
                                inputStr =getCellValue(nextCell).toString().trim();
                                //makerProfitShare.setProductName(inputStr);
                                break;

                        case 4:

                            inputStr = getCellValue(nextCell).toString().trim();
                            System.out.println(inputStr);
                            makerProfitShare.setProfitShareIncome(Float.valueOf(inputStr));
                            break;

                        case 5:
                            dateObj = getCellValue(nextCell);
                            inputStr = dateObj.toString().trim();
                            Date dob = org.apache.poi.ss.usermodel.DateUtil.getJavaDate((Double) dateObj);
                            System.out.println("DOB" + dob);
                            DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                            String sStartTime = dateFormat.format(dob);
                            String dConDob = sStartTime.toString();
                            if ((dConDob.contains(sValid)) ||(dConDob.contains(tValid)))
                                makerProfitShare.setReceiptDate(dob);
                            else {
                                result = true;
                            }
                            break;

                    }
                    listMakerProfitShares.add(makerProfitShare);

                }

            }
            i++;

        }
        if (result) {
            listMakerProfitShares = new ArrayList<BCADMakerProfitShare>();
        }
        workbook.close();
        inputStream.close();
        return listMakerProfitShares;


    }

    private Object getCellValue(Cell cell) {
        switch (cell.getCellTypeEnum()) {
            case STRING:
                return cell.getStringCellValue();

            case BOOLEAN:
                return cell.getBooleanCellValue();

            case NUMERIC:
                return cell.getNumericCellValue();
            case BLANK:
                return cell.getStringCellValue();

        }

        return null;
    }

    private UploadMasterFiles readUprontFile(File sFile, Date startDate, Date endDate) throws IOException {
        UploadMasterFiles uploadUpfrontMasterFiles = new UploadMasterFiles();


        log.info("In DataUploadEditForm readPmsNavFile() method");
        // Excel to database

        int iPhysNumOfCells;
        FileInputStream fis = new FileInputStream(sFile);
        HSSFWorkbook workbook = new HSSFWorkbook(fis);

        bcadMakerUpfrontMasterList = new ArrayList<BcadMakerUpfrontMaster>();
        listMakerUpfrontMasterDOs = new ArrayList<MakerUpfrontMaster>();

        upfrontLogBeans= new ArrayList<UpfrontLogBean>();

        HSSFSheet sheet = workbook.getSheetAt(0);
        int iPutNxtDetailsToDB = 0;
        Iterator<Row> rowIterator = sheet.iterator();
        BcadMakerUpfrontMaster bcadMakerUpfrontMaster = new BcadMakerUpfrontMaster();
        MakerUpfrontMaster makerUpfrontMaster = new MakerUpfrontMaster();
        Object dateObj = null;
        int iTotRowInserted = 0;
        boolean result = false;
        boolean sResult = false;
        int sFlag=0;
        int sSkip=0;
        UpfrontLogBean upfrontLog=new UpfrontLogBean();
        while (rowIterator.hasNext()) {
            upfrontLog=new UpfrontLogBean();
            bcadMakerUpfrontMaster = new BcadMakerUpfrontMaster();
            makerUpfrontMaster = new MakerUpfrontMaster();
            Row row = rowIterator.next();
            Row row2 = row;
            iTotRowInserted++;
            Iterator<Cell> cellIterator = row.cellIterator();
            sFlag=0;

            int iPos = 0;
            int iTotConToMeet = 7;
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
                        sConCheck = "Trans. Date";
                    if (iPos == 2)
                        sConCheck = "Additional Fund";
                    if (iPos == 3)
                        sConCheck = "Capital Payout";
                    if (iPos == 4)
                        sConCheck = "  Profit Payout";
                    if (iPos == 5)
                        sConCheck = "Profits Renewed";
                    if (iPos == 6)
                        sConCheck = "Inter a/c Transfers";
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
            String sCode="";
            String sName="";
            String investName="";
            String distRm = "";
            Date transDate = null;
            Float initFund = 0.0f;
            Float addFund = 0.0f;
            Float capitalPay = 0.0f;
            Float profitPay = 0.0f;
            Integer clientCode = 0;
            Float profitRenewed=0.0f;
            Float interACTransfers=0.0f;

            ClientManagement clientManagement = null;
            DistributorMaster distributorMaster = null;
            RelationshipManager relationshipManager = null;
            InvestmentMaster investmentMaster=null;
            DataFormatter formatter = new DataFormatter();
            if (iPhysNumOfCells != 0 && iPhysNumOfCells != 7) {

                if (iPutNxtDetailsToDB == 1 && iConCheckNow == 0) {

                    while (cellIterator.hasNext()) {
                        Cell cell = cellIterator.next();
                        // System.out.println("Cell
                        // Type->"+cell.getCellType());
                        switch (cell.getCellTypeEnum()) {


                                case STRING:
                                        if(sSkip==0) {
                                            System.out.println(row.getCell(0).getStringCellValue());
                                            sName = row.getCell(0).getStringCellValue();
                                            sCodeScheme = row.getCell(1).getStringCellValue();
                                            investName = row.getCell(2).getStringCellValue();
                                            distRm = row.getCell(3).getStringCellValue();
                                            transDate = row.getCell(4).getDateCellValue();
                                        }

                                    break;
                                case NUMERIC:
                                   if(sSkip==0){
                                        initFund = (float) row.getCell(5).getNumericCellValue();
                                        //formatter.formatCellValue(cellTemp).toString()
                                        if (row.getCell(6) != null) {
                                            addFund = (float) row.getCell(6).getNumericCellValue();
                                            capitalPay = (float) row.getCell(7).getNumericCellValue();
                                            profitPay = (float) row.getCell(8).getNumericCellValue();
                                            profitRenewed = (float) row.getCell(9).getNumericCellValue();
                                            interACTransfers = (float) row.getCell(10).getNumericCellValue();
                                        }
                                        else
                                            sSkip=1;}

                                    break;
                                default:

                        }
                    }

                    try {

                        String sSplits[] = sCodeScheme.split("(?<=\\D)(?=\\d)|(?<=\\d)(?=\\D)");
                        sCode = sSplits[0];
                        if(!sCode.equals("")) {
                            clientCode = new Double(sCode).intValue();
                            clientManagement = clientManagementRepository.findByClientCode(clientCode.toString());

                            if (investName.contains("#")) {
                                investName = investName.replace("#", "");
                            }
                            if(investName.equals("BCAD") && clientManagement.getDistributorMaster()!=null){
                                Product product = productRepository.findByProductName("BCAD");
                              List<CommissionDefinition> checkOption3=commissionDefinitionRepository.findOption3Commission(
                                  clientManagement.getDistributorMaster().getId(),product.getId());
                              if(checkOption3!=null)
                                  sFlag=1;

                            }
                            if (investName.equals("BCAD") && sFlag==0)
                            {
                            bcadMakerUpfrontMaster.setClientCode(sCodeScheme);
                            bcadMakerUpfrontMaster.setClientName(sName);
                            bcadMakerUpfrontMaster.setTransDate(transDate);
                            bcadMakerUpfrontMaster.setInitialFund(initFund);
                            bcadMakerUpfrontMaster.setAdditionalFund(addFund);
                            bcadMakerUpfrontMaster.setCapitalPayout(capitalPay);
                            bcadMakerUpfrontMaster.setProfitPayout(profitPay);
                            bcadMakerUpfrontMaster.setProfitRenewed(profitRenewed);
                            bcadMakerUpfrontMaster.setInterACTransfers(interACTransfers);

                            if (clientManagement != null) {
                                bcadMakerUpfrontMaster.setClientManagement(clientManagement);
                                bcadMakerUpfrontMaster.setDistributorMaster(clientManagement.getDistributorMaster());
                                bcadMakerUpfrontMaster.setRelationshipManagerMaster(clientManagement.getRelationshipManager());
                                bcadMakerUpfrontMasterList.add(bcadMakerUpfrontMaster);
                            }
                            else{
                                upfrontLog.setClientCode(clientCode);
                                upfrontLog.setInvestName(investName);
                                upfrontLog.setsStatus("Client Not Found");
                                uploadUpfrontMasterFiles.setCode("450");
                                uploadUpfrontMasterFiles.setStatus(prop.getString("fee.file.folder") + "DFA Backup\\\\"+
                                    prop.getString("bulk.upload.not")+ "\\\\"+fileName);
                                upfrontLogBeans.add(upfrontLog);
                                setEnableSavePMSNav(false);

                            }

                        }
                        else{
                            PMSClientMaster clientMaster = pmsClientMasterRepository.findByClientCode(clientCode.toString());
                            InvestmentMaster investmentMaster1= investmentMasterRepository.findByInvestmentName(investName);
                            if(clientMaster!=null && investmentMaster1!=null){
                                makerUpfrontMaster.setClientCode(sCodeScheme);
                                makerUpfrontMaster.setClientName(sName);
                                makerUpfrontMaster.setInvestmentMaster(investmentMaster1);
                                makerUpfrontMaster.setTransDate(transDate);
                                makerUpfrontMaster.setInitialFund(initFund);
                                makerUpfrontMaster.setAdditionalFund(addFund);
                                makerUpfrontMaster.setCapitalPayout(capitalPay);
                                makerUpfrontMaster.setProfitPayout(profitPay);
                                makerUpfrontMaster.setPmsClientMaster(clientMaster);
                                makerUpfrontMaster.setDistributorMaster(clientMaster.getDistributorMaster());
                                makerUpfrontMaster.setRelationshipManagerMaster(clientMaster.getRelationshipManager());
                                listMakerUpfrontMasterDOs.add(makerUpfrontMaster);
                            }
                            else{
                                upfrontLog.setClientCode(clientCode);
                                upfrontLog.setInvestName(investName);
                                if (clientMaster == null) {
                                    upfrontLog.setsStatus("Client Not Found");
                                } else
                                    upfrontLog.setsStatus("Investment Not Found");
                                uploadUpfrontMasterFiles.setCode("450");
                                uploadUpfrontMasterFiles.setStatus(prop.getString("fee.file.folder") + "DFA Backup\\\\"+
                                    prop.getString("bulk.upload.not")+ "\\\\"+fileName);
                                upfrontLogBeans.add(upfrontLog);
                                setEnableSavePMSNav(false);
                            }

                            }
                        }

                    }

                    catch (Exception exception) {
                        System.out.println(exception);
                    }


                }

            }

        }
        saveClient(sFile,startDate,endDate,uploadUpfrontMasterFiles);
        if (enableSavePMSNav == false) {
            setEnablePMSDownload(false);
        }

        return uploadUpfrontMasterFiles;
    }

    private void saveClient(File sFile, Date startDate, Date endDate, UploadMasterFiles uploadMasterFiles) {
            try {
                FileUploadUpfront fileUploadUpfront = new FileUploadUpfront();
                MakerUpfrontMaster upfrontMaster;

                BcadMakerUpfrontMaster bcadMakerUpfrontMaster;
                Set<DistributorMaster> distributorMasters;
                Float pmsCommission,aifCommission,commisionAmt;
                List<BcadMakerUpfrontMaster> masterDOs;
                List<MakerUpfrontMaster> masterMakerDOs;
                pmsCommission=prop.getFloat("fee.upfront.pms.commission");
                aifCommission=prop.getFloat("fee.upfront.aif.commission");
                masterDOs=new ArrayList<BcadMakerUpfrontMaster>();
                masterMakerDOs=new ArrayList<MakerUpfrontMaster>();
                if (enableSavePMSNav) {
                    if (bcadMakerUpfrontMasterList.isEmpty() != true) {
                        uploadMasterFiles=saveUploadedFile(sFile.getPath(),startDate,endDate);
                        distributorMasters=new HashSet<DistributorMaster>();
                        for (BcadMakerUpfrontMaster bean: bcadMakerUpfrontMasterList) {
                            if(bean.getDistributorMaster()!=null){
                                distributorMasters.add(bean.getDistributorMaster());
                            }

                            bcadMakerUpfrontMaster = new BcadMakerUpfrontMaster();
                            bcadMakerUpfrontMaster.setClientCode(bean.getClientCode());
                            bcadMakerUpfrontMaster.setClientName(bean.getClientName());
                            bcadMakerUpfrontMaster.setDistributorMaster(bean.getDistributorMaster());
                            bcadMakerUpfrontMaster.setRelationshipManagerMaster(bean.getRelationshipManagerMaster());
                            bcadMakerUpfrontMaster.setTransDate(bean.getTransDate());
                            bcadMakerUpfrontMaster.setStartDate(startDate);
                            bcadMakerUpfrontMaster.setEndDate(endDate);
                            bcadMakerUpfrontMaster.setIsDeleted(0);
                            bcadMakerUpfrontMaster.setUploadMasterFiles(uploadMasterFiles);
                            bcadMakerUpfrontMaster.setInitialFund(bean.getInitialFund());
                            bcadMakerUpfrontMaster.setAdditionalFund(bean.getAdditionalFund());
                            bcadMakerUpfrontMaster.setCapitalPayout(bean.getCapitalPayout());
                            bcadMakerUpfrontMaster.setProfitPayout(bean.getProfitPayout());
                            bcadMakerUpfrontMaster.setClientManagement(bean.getClientManagement());
                            bcadMakerUpfrontMaster.setProfitRenewed(bean.getProfitRenewed());
                            bcadMakerUpfrontMaster.setInterACTransfers(bean.getInterACTransfers());
                            masterDOs.add(bcadMakerUpfrontMaster);

                            bcadMakerUpfrontMasterRepository.save(bcadMakerUpfrontMaster);
                        }



                    }
                    if(listMakerUpfrontMasterDOs.isEmpty() != true){

                        fileUploadUpfront = saveUploadedUpfrontFile(sFile.getPath(),startDate,endDate);
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
                                masterMakerDOs.add(upfrontMaster);
                            }
                            makerUpfrontMasterRepository.save(upfrontMaster);
                        }
                    }

                }
                if (upfrontLogBeans.isEmpty() == false) {
                    HashSet<UpfrontLogBean> hs = new HashSet<>();
                    hs.addAll(upfrontLogBeans);
                    upfrontLogBeans.clear();
                    upfrontLogBeans.addAll(hs);
                    upfrontCreate(upfrontLogBeans);
                    log.error("Upfront file is not uploaded");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

    private FileUploadUpfront saveUploadedUpfrontFile(String path, Date startDate, Date endDate) {
            String loggedUser;
            FileUploadUpfront fileUploadUpfront;
            FileType fileType;

            fileUploadUpfront = new FileUploadUpfront();
            fileType = fileTypeRepository.findByFileType("Upfront");

            fileUploadUpfront = new FileUploadUpfront();
            fileUploadUpfront.setStroreFileLocation(path);
            fileUploadUpfront.setFileType(fileType);
            fileUploadUpfront.setUploadApproved(0);
            fileUploadUpfront.setIsDeleted(0);
            fileUploadUpfront=fileUploadUpfrontRepository.save(fileUploadUpfront);
            return fileUploadUpfront;



    }

    XSSFWorkbook workBookUpfront = null;
    private void upfrontCreate(List<UpfrontLogBean> upfrontLogBeans) {

        try {

            String sFilesDirectory = prop.getString("fee.file.folder") + "DFA Backup\\\\"
                + prop.getString("bulk.upload.not");
            File dirFiles = new File(sFilesDirectory);
            dirFiles.mkdirs();

            String file = sFilesDirectory + "\\\\" + fileName;
            //fileDownload.add(file);
            System.out.println(file);

            fos = new FileOutputStream(file);
            workBookUpfront = new XSSFWorkbook();

            String reportDate = "Upfront";
            XSSFSheet sheet = workBookUpfront.createSheet(reportDate);
            CellStyle cs = workBookUpfront.createCellStyle();
            CellStyle csRight = workBookUpfront.createCellStyle();

            XSSFFont fFont = workBookUpfront.createFont();
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
            workBookUpfront.write(fos);

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
                workBookUpfront.close();
            } catch (IOException e) {
                System.out.println(e);
                // TODO Auto-generated catch block

            }
        }


    }

    private UploadMasterFiles saveUploadedFile(String path, Date startDate, Date endDate) {
        String loggedUser;
        UploadMasterFiles uploadMasterFiles;
        MasterType masterType = masterTypeRepository.findByFileName("Upfront");
        Product product = productRepository.findByProductName("BCAD");

        uploadMasterFiles=new UploadMasterFiles();

        uploadMasterFiles.setFileLocation(path);
        uploadMasterFiles.setMasterType(masterType);
        uploadMasterFiles.setIsDeleted(0);
        uploadMasterFiles.setProduct(product);
        uploadMasterFiles.setFeeFromDate(startDate);
        uploadMasterFiles.setFeeToDate(endDate);
        uploadMasterFiles.setUploadApproved(0);
        uploadMasterFilesRepository.save(uploadMasterFiles);
        return uploadMasterFiles;
    }

    FileOutputStream fos = null;
    HSSFWorkbook workBook = null;
    private void masterCreate(List<MasterLogBean> masterLogBean) {
        try{

            String sFilesDirectory = prop.getString("fee.file.folder") + "DFA Backup\\\\"
                + prop.getString("bulk.upload.not");
            File dirFiles = new File(sFilesDirectory);
            dirFiles.mkdirs();

            String file =sFilesDirectory+"\\\\"+fileName;
            // fileDownload.add(file);

            fos= new FileOutputStream(file);
            workBook = new HSSFWorkbook();

            String reportDate = "Client";
            HSSFSheet sheet = workBook.createSheet(reportDate);
            CellStyle cs= workBook.createCellStyle();
            CellStyle csRight= workBook.createCellStyle();

            HSSFFont fFont=workBook.createFont();
            fFont.setBold(true);
            cs.setFont(fFont);

            csRight.setAlignment(HorizontalAlignment.RIGHT);


            int iRowNo=sheet.getLastRowNum()+1;
            HSSFRow headingBRSBookRow = sheet.createRow(iRowNo);
            headingBRSBookRow.createCell(0).setCellValue("Client Code");
            headingBRSBookRow.getCell(0).setCellStyle(cs);
            sheet.autoSizeColumn(0);
            headingBRSBookRow.createCell(1).setCellValue("Client Name");
            headingBRSBookRow.getCell(1).setCellStyle(cs);
            sheet.autoSizeColumn(1);
            headingBRSBookRow.createCell(2).setCellValue("Status");
            headingBRSBookRow.getCell(2).setCellStyle(cs);
            sheet.autoSizeColumn(2);

            iRowNo=sheet.getLastRowNum()+2;

            Set<MasterLogBean> hs = new HashSet<>();
            hs.addAll(masterLogBean);
            masterLogBean.clear();
            masterLogBean.addAll(hs);

            for(MasterLogBean bean:masterLogBean){
                HSSFRow row = sheet.createRow(iRowNo);

                row.createCell(0).setCellValue(bean.getClientName());
                sheet.autoSizeColumn(0);
                row.createCell(1).setCellValue(bean.getClientCode());
                sheet.autoSizeColumn(1);
                row.createCell(2).setCellValue(bean.gettStatus());
                sheet.autoSizeColumn(2);
                iRowNo++;
            }
            workBook.write(fos);
        }
        catch(Exception e)
        {
            System.out.println(e);
        }
        finally
        {
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

    private List<BCADMakerPMSNav> readBCADPmsNavFile(String path, Date startDate, Date endDate, String product) throws IOException {
        UploadMasterFiles uploadPmsnavMasterFiles = new UploadMasterFiles();
        Integer sOut = 0;
        log.info("In BCADUploadService readPmsNavFile() method");
        // Excel to database
        int iPhysNumOfCells;
        FileInputStream fis = new FileInputStream(path);
        HSSFWorkbook workbook = new HSSFWorkbook(fis);

        HSSFSheet sheet = workbook.getSheetAt(0);
        int iPutNxtDetailsToDB = 0;
        Calendar cal = Calendar.getInstance();
        cal.setTime(endDate);
        int year = cal.get(Calendar.YEAR);
        System.out.println(year);
        int month = cal.get(Calendar.MONTH) + 1;
        DateFormat df3 = new SimpleDateFormat("MM");
        String sMonth = df3.format(startDate);
        System.out.println(sMonth);
        Iterator<Row> rowIterator = sheet.iterator();
        List<BCADMakerPMSNav> listBCADMakerPMSNavs = new ArrayList<BCADMakerPMSNav>();
        BCADMakerPMSNav bcadMakerPMSNav = new BCADMakerPMSNav();
        Object dateObj = null;
        int iTotRowInserted = 0;
        boolean result = false;
        boolean sResult = false;
        String investMode = "";
        MasterLogBean masterLogs = new MasterLogBean();
        while (rowIterator.hasNext()) {
            masterLogs=new MasterLogBean();
            bcadMakerPMSNav = new BCADMakerPMSNav();
            Row row = rowIterator.next();
            Row row2 = row;
            iTotRowInserted++;
            Iterator<Cell> cellIterator = row.cellIterator();

            int iPos = 0;
            int iTotConToMeet = 1;
            int sTotConToMeet = 2;
            int iTotConMet = 0;
            int iConCheckNow = 0;
            String sConCheck = "";
            iPhysNumOfCells = row.getPhysicalNumberOfCells();

            int sMode = 0;

            while (cellIterator.hasNext() && iPutNxtDetailsToDB == 0) {
                Cell cell = cellIterator.next();

                if (iPutNxtDetailsToDB == 0) {
                    if (iPos == 0)
                        sConCheck = "Investment Mode : Discretionary";

                    if (cell.getStringCellValue().trim().equals(sConCheck))
                        iTotConMet++;

                    iPos++;

                    if (iTotConToMeet == iTotConMet) {
                        investMode = cell.getStringCellValue();
                        String[] sSpilt = investMode.split("\\:");
                        investMode = sSpilt[1];
                        iPutNxtDetailsToDB = 1;
                        iConCheckNow = 1;
                    }

                    System.out.println(investMode);
                }
            }
            Integer iPutNonDiscretionary = 0;
            String sCode = "";
            Integer bCode = 0;
            String codeScheme = "";
            String sScheme = "";
            String distRm = "";
            String strategyName="";
            Integer dCode = 0;
            Integer rCode = 0;
            Date dStartDate = null;
            Date dEndDate = null;
            Integer nDays = 0;
            Float perComm = 0.0f;
            Float grossPms = 0.0f;
            Float pMargin = 0.0f;
            Float netPms = 0.0f;
            Float calcPms = 0.0f;
            float bAmount = 0.0f;
            Integer clientCode = null;
            int sBreak = 0;
            ClientManagement clientManagement = null;
            DistributorMaster distributorMaster = null;
            RelationshipManager relationshipManager = null;
            // System.out.println(iPhysNumOfCells);
            if (sOut != 1) {
                if (iPhysNumOfCells != 0 && iPhysNumOfCells != 7) {

                    if (iPutNxtDetailsToDB == 1 && iConCheckNow == 0) {

                        while (cellIterator.hasNext()) {
                            Cell cell = cellIterator.next();
                            // System.out.println("Cell
                            // Type->"+cell.getCellType());
                            switch (cell.getCellTypeEnum()) {
                                case STRING:
                                    try {

                                        codeScheme = row.getCell(0).getStringCellValue();
                                        // System.out.println("invest"+codeScheme);
                                        if (codeScheme.contains(prop.getString("pms.noenter"))) {
                                            sOut = 1;
                                            break;
                                        }
                                        if (codeScheme.equals(prop.getString("pms.fee.nondiscrete"))) {
                                            investMode = row.getCell(0).getStringCellValue();
                                            String[] sSpilt = investMode.split("\\:");
                                            investMode = sSpilt[1];
                                            // System.out.println(investMode);
                                            sBreak = 1;
                                            break;
                                        }

                                        if (!codeScheme.contains("Total") && sBreak == 0) {
                                            String sSplits[] = codeScheme.split("(?<=\\D)(?=\\d)|(?<=\\d)(?=\\D)");
                                            sCode = sSplits[0];
                                            clientCode = new Double(sCode).intValue();
                                            clientManagement =clientManagementRepository.findByClientCode(clientCode.toString());

                                            if(clientManagement==null){
                                                result = true;}
                                        } else {
                                            sBreak = 1;
                                            break;

                                        }
                                        if ((sBreak == 0) && (!codeScheme.contains(prop.getString("pms.noenter")))) {
                                            // sScheme =
                                            System.out.println(row.getCell(2).getStringCellValue());
                                            if(row.getCell(2).getStringCellValue().equals("BCAD"))
                                                strategyName=row.getCell(2).getStringCellValue();

                                            if (iPhysNumOfCells != 15) {
                                                distRm = row.getCell(3).getStringCellValue();

                                            }

                                            if (distributorMaster == null) {
                                                // sResult=true;
                                                sBreak = 0;


                                            }

                                        }

                                    } catch (Exception exception) {

                                        result = true;
                                        log.error("Exception at DataUploadEditForm readPMSNavFile" + exception);
                                    }

                                    break;
                                case NUMERIC:
                                    try {
                                        if ((sBreak == 0) && (!codeScheme.contains(prop.getString("pms.noenter")))) {
                                            nDays = (int) row.getCell(6).getNumericCellValue();

                                            perComm = (float) row.getCell(7).getNumericCellValue();

                                            grossPms = (float) row.getCell(8).getNumericCellValue();

                                            pMargin = (float) row.getCell(9).getNumericCellValue();

                                            netPms = (float) row.getCell(10).getNumericCellValue();

                                            calcPms = (float) row.getCell(11).getNumericCellValue();

                                            // if (investMode.contains("
                                            // Non-Discretionary")) {
                                            dStartDate = row.getCell(4).getDateCellValue();
                                            System.out.println(dStartDate);
                                            dEndDate = row.getCell(5).getDateCellValue();
                                            System.out.println(dEndDate);

                                            // }

                                        }
                                    } catch (Exception exception) {
                                        log.error("Exception at DataUploadEditForm PMS Nav, client not found" + clientCode);
                                        result = true;
                                        sBreak = 1;
                                        break;
                                    }

                                    break;
                                default:
                                    try {
                                        if (sBreak == 0) {
                                            dStartDate = row.getCell(4).getDateCellValue();
                                            System.out.println(dStartDate);
                                            dEndDate = row.getCell(5).getDateCellValue();
                                            System.out.println(dEndDate);
                                        }
                                    } catch (Exception e) {
                                        log.error("Exception at DataUploadEditForm readPMSNavFile" + e);
                                        System.out.println(e);
                                    }
                            }
                            /**
                             * adding client not exist validation
                             */
                            if (result && strategyName.equals("BCAD")) {

                                masterLogs.setClientCode(codeScheme.toString());
                                masterLogs.setClientName(row.getCell(1).getStringCellValue());
                                masterLogs.settStatus("Client Not Found");
                                masterLogBean.add(masterLogs);
                                //UnifiJsfUtils.addErrorMessage("Client Does not exist Code "+clientCode.toString() +"Name"+row.getCell(1).getStringCellValue());
                                setEnableSavePMSNav(false);

                            }
                            result = false;
                        }

                    }
                    if ((nDays != 0) && result == false) {
                        String sDate = "/" + month + "/" + year;
                        String tDate="/" + sMonth + "/" + year;
                        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                        String dStart = dateFormat.format(dStartDate);
                        String dEnd = dateFormat.format(dEndDate);
                        // String dStart=dStartDate.toString();
                        // String dEnd=dEndDate.toString();
                        if ((nDays != 0) && ((dStart.contains(sDate) && dEnd.contains(sDate))||((dStart.contains(tDate) && dEnd.contains(tDate))))) {
                            if(strategyName.equals("BCAD")) {
                                bcadMakerPMSNav.setClientManagement(clientManagement);
                                bcadMakerPMSNav.setCodeScheme(codeScheme);
                                bcadMakerPMSNav.setStartDate(dStartDate);
                                bcadMakerPMSNav.setEndDate(dEndDate);
                                bcadMakerPMSNav.setNoDays(nDays);
                                bcadMakerPMSNav.setPercentageComm(perComm);
                                bcadMakerPMSNav.setGrossPmsNav(grossPms);
                                bcadMakerPMSNav.setMarginValue(pMargin);
                                bcadMakerPMSNav.setNetPmsNav(netPms);
                                bcadMakerPMSNav.setCalcPmsNav(calcPms);
                                bcadMakerPMSNav.setInvestMode(investMode);
                                bcadMakerPMSNav.setNoMonth(month);
                                bcadMakerPMSNav.setNoYear(year);
                                System.out.println("Start Date--" + startDate);
                                bcadMakerPMSNav.setSelectedStartDate(startDate);

                                listBCADMakerPMSNavs.add(bcadMakerPMSNav);
                            }
                        }

                        else {
                            statusCode="410";
                            // UnifiJsfUtils.addErrorMessage("File is not containing the same month");
                            listBCADMakerPMSNavs = new ArrayList<BCADMakerPMSNav>();
                            break;
                        }

                    }
                }
            }
        }



        return listBCADMakerPMSNavs;

    }
    public  Optional<UploadMasterFiles> approveFile(Optional<UploadMasterFiles> result) {
        if(result.get().getMasterType().getFileName().equals("Upfront")) {

            try {

                String hql = "insert into bcad_upfront_master select * from bcad_maker_upfront_master mu where mu.file_uploaded_id=" + result.get().getId();
                Query query = entityManager.createNativeQuery(hql);
                query.executeUpdate();


                String hqlUpdate = "update `bcad_upload_master_files` f set f.int_approved=1 where f.id=" + result.get().getId();
                query = entityManager.createNativeQuery(hqlUpdate);
                query.executeUpdate();
            } catch (PersistenceException cve) {

                cve.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if(result.get().getMasterType().getFileName().equals("PMS NAV"))
        {
            try {
                Query query=entityManager.createNativeQuery("insert into bcad_fee_pms_nav select * from bcad_maker_fee_pms_nav mp where mp.fileuploaded_id="+result.get().getId());
                query.executeUpdate();

                query=entityManager.createNativeQuery("update bcad_upload_master_files f set f.int_approved=1 where f.id="+ result.get().getId());
                query.executeUpdate();
            }
            catch (PersistenceException cve) {
                cve.printStackTrace();
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
        if(result.get().getMasterType().getFileName().equals("Profit Share"))
        {
            try {
                Query query=entityManager.createNativeQuery("insert into bcad_profit_share select * from bcad_maker_profit_share mp where mp.fileuploaded_id="+result.get().getId());
                query.executeUpdate();

                query=entityManager.createNativeQuery("update bcad_upload_master_files f set f.int_approved=1 where f.id="+ result.get().getId());
                query.executeUpdate();
            }
            catch (PersistenceException cve) {
                cve.printStackTrace();
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    private void writeFile( byte[] fileStream, File sFile) throws IOException {

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

    public void deletePMSFile(UploadMasterFiles uploadMasterFiles)
    {
        System.out.println("TEST @@@@@");

        Query query=entityManager.createNativeQuery("update bcad_fee_pms_nav f set f.int_deleted=1 where f.fileuploaded_id="+uploadMasterFiles.getId());
        query.executeUpdate();
    }

    public void deleteUpfrontFile(UploadMasterFiles uploadMasterFiles) {

        Query query=entityManager.createNativeQuery("update bcad_upfront_master f set f.int_deleted=1 where f.file_uploaded_id="+uploadMasterFiles.getId());
        query.executeUpdate();
    }

    public void deleteProfitFile(UploadMasterFiles uploadMasterFiles) {
        Query query=entityManager.createNativeQuery("update bcad_profit_share f set f.int_deleted=1 where f.fileuploaded_id="+uploadMasterFiles.getId());
        query.executeUpdate();
    }
}
