package com.bcad.application.service;

import com.bcad.application.config.PropertiesUtils;
import com.bcad.application.domain.*;
import com.bcad.application.service.dto.ClientInvestmentDateDTO;
import com.bcad.application.repository.*;
import com.bcad.application.web.rest.errors.ClientNotFoundException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.*;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.URISyntaxException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class PMSUploadService {

    @PersistenceContext
    EntityManager entityManager;

    private byte[] fileStream;
    private String fileName;
    private boolean enableSavePMSNav =true;
    private Boolean enablePMSDownload=true;
    private List<MasterLogBean> masterLogBean=new ArrayList<>();
    private String statusCode;
    private Boolean enableSave;

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

    public List<MasterLogBean> getMasterLogBean() {
        return masterLogBean;
    }

    public void setMasterLogBean(List<MasterLogBean> masterLogBean) {
        this.masterLogBean = masterLogBean;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public Boolean getEnableSave() {
        return enableSave;
    }

    public void setEnableSave(Boolean enableSave) {
        this.enableSave = enableSave;
    }

    private final FileUploadRepository fileUploadRepository;
    private final FileTypeRepository fileTypeRepository;
    private final MakerBrokerageRepository makerBrokerageRepository;
    private final PMSClientMasterRepository pmsClientMasterRepository;
    private final MakerProfitShareRepository makerProfitShareRepository;
    private final InvestmentMasterRepository investmentMasterRepository;
    private final MakerPMSNavRepository makerPMSNavRepository;
    private final PMSService pmsService;
    private final UploadMasterFilesRepository uploadMasterFilesRepository;
    private final ProductRepository productRepository;
    private final BCADMakerPMSNavRepository bcadMakerPMSNavRepository;
    private final ClientManagementRepository clientManagementRepository;
    private final MasterTypeRepository masterTypeRepository;
    private final BCADUploadService bcadUploadService;
    private final BCADMakerProfitShareRepository bcadMakerProfitShareRepository;
    private final CommissionDefinitionRepository commissionDefinitionRepository;
    private final MakerCapitalTransactionRepository makerCapitalTransactionRepository;
    private final CapitalTransactionRepository capitalTransactionRepository;
    private final PMSNavRepository pmsNavRepository;
    private final BCADPMSNavRepository bcadpmsNavRepository;
    private final RelationshipManagerRepository relationshipManagerRepository;
    private final DistributorMasterRepository distributorMasterRepository;
    private final ClientFeeCommissionRepository clientFeeCommissionRepository ;
    private final ClientCommissionRepository clientCommissionRepository;
    private final AIF2ManagementFeeRepository aif2ManagementFeeRepository;
    private final AIF2SeriesMasterRepository aif2SeriesMasterRepository;


    public PMSUploadService(FileUploadRepository fileUploadRepository, FileTypeRepository fileTypeRepository, MakerBrokerageRepository makerBrokerageRepository, PMSClientMasterRepository pmsClientMasterRepository
        , MakerProfitShareRepository makerProfitShareRepository, InvestmentMasterRepository investmentMasterRepository, MakerPMSNavRepository makerPMSNavRepository, PMSService pmsService,
                            UploadMasterFilesRepository uploadMasterFilesRepository, ProductRepository productRepository, BCADMakerPMSNavRepository bcadMakerPMSNavRepository,
                            ClientManagementRepository clientManagementRepository, MasterTypeRepository masterTypeRepository, BCADUploadService bcadUploadService,
                            BCADMakerProfitShareRepository bcadMakerProfitShareRepository, CommissionDefinitionRepository commissionDefinitionRepository,
                            MakerCapitalTransactionRepository makerCapitalTransactionRepository, CapitalTransactionRepository capitalTransactionRepository,
                            PMSNavRepository pmsNavRepository, BCADPMSNavRepository bcadpmsNavRepository, RelationshipManagerRepository relationshipManagerRepository, DistributorMasterRepository distributorMasterRepository, ClientFeeCommissionRepository clientFeeCommissionRepository,
                            ClientCommissionRepository clientCommissionRepository, AIF2ManagementFeeRepository aif2ManagementFeeRepository,AIF2SeriesMasterRepository aif2SeriesMasterRepository) {
        this.fileUploadRepository = fileUploadRepository;
        this.fileTypeRepository = fileTypeRepository;
        this.makerBrokerageRepository = makerBrokerageRepository;
        this.pmsClientMasterRepository = pmsClientMasterRepository;
        this.makerProfitShareRepository = makerProfitShareRepository;
        this.investmentMasterRepository = investmentMasterRepository;
        this.makerPMSNavRepository = makerPMSNavRepository;
        this.pmsService = pmsService;
        this.uploadMasterFilesRepository = uploadMasterFilesRepository;
        this.productRepository = productRepository;
        this.bcadMakerPMSNavRepository = bcadMakerPMSNavRepository;
        this.clientManagementRepository = clientManagementRepository;
        this.masterTypeRepository= masterTypeRepository;
        this.bcadUploadService=bcadUploadService;
        this.bcadMakerProfitShareRepository = bcadMakerProfitShareRepository;
        this.commissionDefinitionRepository =commissionDefinitionRepository;
        this.makerCapitalTransactionRepository = makerCapitalTransactionRepository;
        this.capitalTransactionRepository = capitalTransactionRepository;
        this.pmsNavRepository = pmsNavRepository;
        this.bcadpmsNavRepository = bcadpmsNavRepository;
        this.relationshipManagerRepository = relationshipManagerRepository;
        this.distributorMasterRepository = distributorMasterRepository;
        this.clientFeeCommissionRepository = clientFeeCommissionRepository;
        this.clientCommissionRepository = clientCommissionRepository;
        this.aif2ManagementFeeRepository=aif2ManagementFeeRepository;
        this.aif2SeriesMasterRepository=aif2SeriesMasterRepository;
    }

    PropertiesConfiguration prop = new PropertiesUtils().getProperties("application.properties");
    private final Logger log = LoggerFactory.getLogger(PMSUploadService.class);

    public FileUpload uploadPMSFile(Date startDate, Date endDate, String fileType, MultipartFile multipartFile) throws IOException,Exception {
        masterLogBean = new ArrayList<>();

        enableSavePMSNav = true;
        enablePMSDownload = true;
        DateFormat dateFormat = new SimpleDateFormat("MMM yyyy");
        String sStartTime = dateFormat.format(startDate);
        String sEndTime = dateFormat.format(endDate);
        String sFinal = sStartTime + "_to_" + sEndTime;

        String sDate = " " + sStartTime;
        System.out.println(multipartFile.getOriginalFilename());

        setFileName(multipartFile.getOriginalFilename());

        String sLoc = fileType.concat(sDate);
        fileStream = IOUtils.toByteArray(multipartFile.getInputStream());
        FileUpload fileUpload = null;

        if (fileType.contains(prop.getString("file.fee.brokerage"))) {

            String sFilesDirectory = prop.getString("fee.file.folder") + "DFA Backup\\\\"
                + prop.getString("file.fee.brokerage") + "\\\\" + sFinal;
            File dirFiles = new File(sFilesDirectory);
            dirFiles.mkdirs();
            if (fileName.contains(sLoc)) {
                File sFile = new File(dirFiles, fileName);
                try {
                    fileUpload = fileUploadRepository.findByFileLocationAndIsDeleted(sFile.getPath().toString(), 0);
                } catch (Exception e) {
                    System.out.println(e);
                }
                if (fileUpload == null) {
                    writeFile(fileStream, sFile);
                    System.out.println("File path" + sFile.getPath());
                    List<MakerBrokerage> list = readBrokerageFile(sFile.getPath(), startDate, endDate);


                    if (list.isEmpty() != true) {
                        FileType fileTypeDO = fileTypeRepository.findByFileType("Brokerage");

                        fileUpload = new FileUpload();
                        fileUpload.setFileLocation(sFile.getPath());
                        fileUpload.setFileType(fileTypeDO);
                        fileUpload.setIsDeleted(0);
                        fileUpload.setUploadApproved((long) 0);
                        fileUploadRepository.save(fileUpload);
                        System.out.println(list.size());
                        for (MakerBrokerage makerBrokerage : list) {
                            makerBrokerage.setFileUpload(fileUpload);
                            makerBrokerage.setIsDeleted(0);
                            makerBrokerageRepository.save(makerBrokerage);

                        }

                    }
                } else {
                    fileUpload = new FileUpload();
                    fileUpload.setCode("409");
                    System.out.println(fileUpload);
                    return fileUpload;

                   /* UnifiJsfUtils
                        .addErrorMessage("Uploaded file already exists, Delete previous file and try again");*/
                }
            } else {
                fileUpload = new FileUpload();
                fileUpload.setCode("400");
                System.out.println(fileUpload);
                return fileUpload;

                // UnifiJsfUtils.addErrorMessage("Uploaded file is not in correct month");
            }
        }
        if (fileType.contains(prop.getString("file.fee.profit"))) {
            String loggedUser;

            String sFilesDirectory = prop.getString("fee.file.folder") + "DFA Backup\\\\"
                + prop.getString("file.fee.profit") + "\\\\" + sFinal;
            File dirFiles = new File(sFilesDirectory);

            dirFiles.mkdirs();
            if (fileName.contains(sLoc)) {
                File sFile = new File(dirFiles, fileName);
                try {
                    fileUpload = fileUploadRepository.findByFileLocationAndIsDeleted(sFile.getPath().toString(), 0);
                } catch (Exception e) {
                    System.out.println(e);
                }
                if (fileUpload == null) {
                    writeFile(fileStream, sFile);
                    System.out.println("File path" + sFile.getPath());
                    List<MakerProfitShare> list = readProfitShareFile(sFile.getPath(), startDate, endDate);

                    if (list.isEmpty() != true) {
                        FileType fileType1 = fileTypeRepository.findByFileType("Profit Share");
                        MasterType masterType = masterTypeRepository.findByFileName("Profit Share");
                        Product product1 = productRepository.findByProductName("BCAD");

                        fileUpload = new FileUpload();
                        fileUpload.setFileLocation(sFile.getPath());
                        fileUpload.setFileType(fileType1);
                        fileUpload.setIsDeleted(0);
                        fileUpload.setUploadApproved((long) 0);
                        fileUploadRepository.save(fileUpload);

                        UploadMasterFiles uploadMasterFiles = new UploadMasterFiles();
                        uploadMasterFiles.setFileLocation(sFile.getPath());
                        uploadMasterFiles.setMasterType(masterType);
                        uploadMasterFiles.setIsDeleted(0);
                        uploadMasterFiles.setUploadApproved(0);
                        uploadMasterFiles.setFeeFromDate(startDate);
                        uploadMasterFiles.setFeeToDate(endDate);
                        uploadMasterFiles.setProduct(product1);
                        uploadMasterFilesRepository.save(uploadMasterFiles);

                        Set<MakerProfitShare> hs = new HashSet<>();
                        hs.addAll(list);
                        list.clear();
                        list.addAll(hs);

                        for (MakerProfitShare makerProfitShare : list) {
                            Integer sFlag = 0;
                            if (makerProfitShare.getPmsClientMaster().getDistributorMaster() != null && makerProfitShare.getProductName().equals("BCAD")) {
                                Product product = productRepository.findByProductName("BCAD");
                                List<CommissionDefinition> checkOption3 = commissionDefinitionRepository.findOption3Commission(
                                    makerProfitShare.getPmsClientMaster().getDistributorMaster().getId(), product.getId());
                                if (checkOption3 != null)
                                    sFlag = 1;
                            }
                            if (!makerProfitShare.getProductName().equals("BCAD") || sFlag == 1) {
                                makerProfitShare.setFileUpload(fileUpload);
                                makerProfitShare.setIsDeleted(0);
                                makerProfitShare.setStartDate(startDate);
                                makerProfitShare.setEndDate(endDate);
                                makerProfitShareRepository.save(makerProfitShare);
                            }
                            if (makerProfitShare.getProductName().equals("BCAD") && sFlag == 0) {
                                BCADMakerProfitShare bcadMakerProfitShare = new BCADMakerProfitShare();
                                ClientManagement clientManagement = clientManagementRepository.findByClientCode(makerProfitShare.getPmsClientMaster().getClientCode());
                                bcadMakerProfitShare.setClientManagement(clientManagement);
                                bcadMakerProfitShare.setProfitShareIncome(makerProfitShare.getProfitShareIncome());
                                bcadMakerProfitShare.setReceiptDate(makerProfitShare.getReceiptDate());
                                bcadMakerProfitShare.setFileUpload(uploadMasterFiles);
                                bcadMakerProfitShare.setStartDate(startDate);
                                bcadMakerProfitShare.setEndDate(endDate);
                                bcadMakerProfitShare.setIsDeleted(0);
                                bcadMakerProfitShareRepository.save(bcadMakerProfitShare);
                            }

                        }

                    }
                } else {
                    fileUpload = new FileUpload();
                    fileUpload.setCode("409");
                    System.out.println(fileUpload);
                    return fileUpload;
                   /* UnifiJsfUtils
                        .addErrorMessage("Uploaded file already exists, Delete previous file and try again");*/
                }
            } else {
                fileUpload = new FileUpload();
                fileUpload.setCode("400");
                System.out.println(fileUpload);
                return fileUpload;

                // UnifiJsfUtils.addErrorMessage("Uploaded file is not in correct month");
            }
        }

        if (fileType.contains(prop.getString("file.fee.pms"))) {

            String sFilesDirectory = prop.getString("fee.file.folder") + "DFA Backup\\\\"
                + prop.getString("file.fee.pms") + "\\\\" + sFinal;
            File dirFiles = new File(sFilesDirectory);
            dirFiles.mkdirs();
            if (fileName.contains(sLoc)) {
                File sFile = new File(dirFiles, fileName);
                try {
                    fileUpload = fileUploadRepository.findByFileLocationAndIsDeleted(sFile.getPath().toString(), 0);
                } catch (Exception e) {
                    System.out.println(e);
                }
                if (fileUpload == null) {
                    statusCode = "";
                    writeFile(fileStream, sFile);
                    System.out.println("File path" + sFile.getPath());
                    List<MakerPMSNav> list = readPmsNavFile(sFile.getPath(), startDate, endDate);
                    /**c
                     * save when client validation done
                     */
                    if (enableSavePMSNav) {

                        if (list.isEmpty() != true) {
                            FileType fileType2 = fileTypeRepository.findByFileType(prop.getString("file.fee.pms"));

                            fileUpload = new FileUpload();
                            fileUpload.setFileLocation(sFile.getPath());
                            fileUpload.setFileType(fileType2);
                            fileUpload.setIsDeleted(0);
                            fileUpload.setUploadApproved((long) 0);
                            fileUploadRepository.save(fileUpload);

                            MasterType masterType = masterTypeRepository.findByFileName(prop.getString("file.fee.pms"));
                            Product product1 = productRepository.findByProductName("BCAD");

                            UploadMasterFiles uploadMasterFiles = new UploadMasterFiles();
                            uploadMasterFiles.setFileLocation(sFile.getPath());
                            uploadMasterFiles.setMasterType(masterType);
                            uploadMasterFiles.setIsDeleted(0);
                            uploadMasterFiles.setUploadApproved(0);
                            uploadMasterFiles.setFeeFromDate(startDate);
                            uploadMasterFiles.setFeeToDate(endDate);
                            uploadMasterFiles.setProduct(product1);
                            uploadMasterFilesRepository.save(uploadMasterFiles);
                            for (MakerPMSNav makerPMSNav : list) {
                                Integer sFlag = 0;
                                Product product = productRepository.findByProductName("BCAD");
                                if (makerPMSNav.getPmsClientMaster().getDistributorMaster() != null && makerPMSNav.getStrategyName().equals("BCAD")) {
                                    List<CommissionDefinition> checkOption3 = commissionDefinitionRepository.findOption3Commission(
                                        makerPMSNav.getPmsClientMaster().getDistributorMaster().getId(), product.getId());
                                    if (checkOption3 != null)
                                        sFlag = 1;
                                }
                                if (!makerPMSNav.getStrategyName().equals("BCAD") || sFlag == 1) {
                                    makerPMSNav.setFileUpload(fileUpload);
                                    System.out.println("Start date---" + makerPMSNav.getSelectedStartDate());
                                    makerPMSNav.setIsDeleted(0);
                                    makerPMSNavRepository.save(makerPMSNav);
                                }
                                if (makerPMSNav.getStrategyName().equals("BCAD") && sFlag == 0) {
                                    BCADMakerPMSNav bcadMakerPMSNav = new BCADMakerPMSNav();
                                    ClientManagement clientManagement = clientManagementRepository.findByClientCode(makerPMSNav.getPmsClientMaster().getClientCode());
                                    bcadMakerPMSNav.setClientManagement(clientManagement);
                                    bcadMakerPMSNav.setCodeScheme(makerPMSNav.getCodeScheme());
                                    bcadMakerPMSNav.setStartDate(makerPMSNav.getStartDate());
                                    bcadMakerPMSNav.setEndDate(makerPMSNav.getEndDate());
                                    bcadMakerPMSNav.setNoDays(makerPMSNav.getNoDays());
                                    bcadMakerPMSNav.setPercentageComm(makerPMSNav.getPercentageComm());
                                    bcadMakerPMSNav.setGrossPmsNav(makerPMSNav.getGrossPmsNav());
                                    bcadMakerPMSNav.setMarginValue(makerPMSNav.getMarginValue());
                                    bcadMakerPMSNav.setNetPmsNav(makerPMSNav.getNetPmsNav());
                                    bcadMakerPMSNav.setCalcPmsNav(makerPMSNav.getCalcPmsNav());
                                    bcadMakerPMSNav.setInvestMode(makerPMSNav.getInvestMode());
                                    bcadMakerPMSNav.setNoMonth(makerPMSNav.getNoMonth());
                                    bcadMakerPMSNav.setNoYear(makerPMSNav.getNoYear());
                                    System.out.println("Start Date--" + startDate);
                                    bcadMakerPMSNav.setSelectedStartDate(makerPMSNav.getSelectedStartDate());
                                    bcadMakerPMSNav.setFileUpload(uploadMasterFiles);
                                    System.out.println("Start date---" + bcadMakerPMSNav.getSelectedStartDate());
                                    bcadMakerPMSNav.setIsDeleted(0);
                                    bcadMakerPMSNavRepository.save(bcadMakerPMSNav);
                                }
                            }


                        }
                    }
                    if (list.isEmpty() == true && statusCode.equals("410")) {
                        fileUpload = new FileUpload();
                        fileUpload.setCode("410");
                        System.out.println(fileUpload);
                        return fileUpload;

                    }
                    if (enableSavePMSNav == false) {
                        fileUpload = new FileUpload();
                        fileUpload.setCode("470");
                        fileUpload.setStatus(prop.getString("fee.file.folder") + "DFA Backup\\" +
                            prop.getString("bulk.upload.not") + "\\" + fileName);
                        System.out.println("pmscheck");
                        masterCreate(masterLogBean);
                        setEnablePMSDownload(false);
                    }
                } else {
                    fileUpload = new FileUpload();
                    fileUpload.setCode("409");
                    System.out.println(fileUpload);
                    return fileUpload;
                    //UnifiJsfUtils .addErrorMessage("Uploaded file already exists, Delete previous file and try again");
                }
            } else {
                fileUpload = new FileUpload();
                fileUpload.setCode("400");
                System.out.println(fileUpload);
                return fileUpload;
                // UnifiJsfUtils.addErrorMessage("Uploaded file is not in correct month");
            }
        }
        if (fileType.contains(prop.getString("nav.profitshare.file"))) {

            String sFilesDirectory = prop.getString("fee.file.folder") + "DFA Backup\\\\"
                + prop.getString("nav.profitshare.file") + "\\\\" + sFinal;
          /*  String sFilesDirectory = prop.getString("fee.file.folder") + "DFA Backup\\\\"
                + prop.getString("nav.profitshare.file") + "////" + sFinal;*/
            File dirFiles = new File(sFilesDirectory);
            dirFiles.mkdirs();
            if (fileName.contains(sLoc)) {
                File sFile = new File(dirFiles, fileName);
                try {
                	fileUpload = fileUploadRepository.findByFileLocationAndIsDeleted(sFile.getPath().toString(), 0);
                } catch (Exception e) {
                    System.out.println(e);
                }
                if (fileUpload == null) {
                    writeFile(fileStream, sFile);
                    System.out.println("File path" + sFile.getPath());
//                    List<MakerPMSNav> list = readNewPmsNavFile(sFile.getPath(), startDate, endDate);
                    FileUploadFeePMS fileUploadFeePMSList = readNewPmsNavFile(sFile.getPath(), startDate, endDate);
                    List<MakerPMSNav> makerPMSNavList =fileUploadFeePMSList.getListMakerPmsNavDOs();
                    List<BCADMakerPMSNav> bcadMakerPMSNavList =fileUploadFeePMSList.getListBCADMakerPMSNavDOs();
                    List<MakerProfitShare> listMakerProfitShare = fileUploadFeePMSList.getListMakerProfitShare();
                    List<BCADMakerProfitShare> listBCADMakerProfitShare = fileUploadFeePMSList.getListBCADMakerProfitShare();
                    if (enableSavePMSNav) {

//                        if (list.isEmpty() != true) {
                    	if (fileUploadFeePMSList.getListMakerPmsNavDOs().size() > 0 || fileUploadFeePMSList.getListBCADMakerPMSNavDOs().size() > 0 ) {
                            // PMS Nav upload storage
                            FileType fileType2 = fileTypeRepository.findByFileType(prop.getString("file.fee.pms"));

                            fileUpload = new FileUpload();
                            fileUpload.setFileLocation(sFile.getPath());
                            fileUpload.setFileType(fileType2);
                            fileUpload.setIsDeleted(0);
                            fileUpload.setUploadApproved((long) 0);
                            fileUploadRepository.save(fileUpload);

                            MasterType masterType = masterTypeRepository.findByFileName(prop.getString("file.fee.pms"));
                            Product product1 = productRepository.findByProductName("BCAD");

                            UploadMasterFiles uploadMasterFiles = new UploadMasterFiles();
                            uploadMasterFiles.setFileLocation(sFile.getPath());
                            uploadMasterFiles.setMasterType(masterType);
                            uploadMasterFiles.setIsDeleted(0);
                            uploadMasterFiles.setUploadApproved(0);
                            uploadMasterFiles.setFeeFromDate(startDate);
                            uploadMasterFiles.setFeeToDate(endDate);
                            uploadMasterFiles.setProduct(product1);
                            uploadMasterFilesRepository.save(uploadMasterFiles);

                            //Profit share upload storage
                            FileType fileType1 = fileTypeRepository.findByFileType("Profit Share");
                            masterType = masterTypeRepository.findByFileName("Profit Share");
                            //product1 = productRepository.findByProductName("BCAD");

                            FileUpload fileUpload1 = new FileUpload();
                            fileUpload1.setFileLocation(sFile.getPath());
                            fileUpload1.setFileType(fileType1);
                            fileUpload1.setIsDeleted(0);
                            fileUpload1.setUploadApproved((long) 0);
                            fileUploadRepository.save(fileUpload1);

                            UploadMasterFiles uploadMasterFiles1 = new UploadMasterFiles();
                            uploadMasterFiles1.setFileLocation(sFile.getPath());
                            uploadMasterFiles1.setMasterType(masterType);
                            uploadMasterFiles1.setIsDeleted(0);
                            uploadMasterFiles1.setUploadApproved(0);
                            uploadMasterFiles1.setFeeFromDate(startDate);
                            uploadMasterFiles1.setFeeToDate(endDate);
                            uploadMasterFiles1.setProduct(product1);
                            uploadMasterFilesRepository.save(uploadMasterFiles);

                            if(makerPMSNavList != null) {
                            	for (MakerPMSNav makerPMSNav :makerPMSNavList) {
                            		MakerPMSNav makerPMSNavInsert = new MakerPMSNav();
                            		makerPMSNavInsert.setPmsClientMaster(makerPMSNav.getPmsClientMaster());
                            		makerPMSNavInsert.setStrategyName(makerPMSNav.getStrategyName());
                            		makerPMSNavInsert.setCodeScheme(makerPMSNav.getCodeScheme());
                            		makerPMSNavInsert.setCalcPmsNav(makerPMSNav.getCalcPmsNav());
                            		makerPMSNavInsert.setInvestmentMaster(makerPMSNav.getInvestmentMaster());
                            		makerPMSNavInsert.setSelectedStartDate(makerPMSNav.getSelectedStartDate());
                            		makerPMSNavInsert.setFeeType(makerPMSNav.getFeeType());
                            		makerPMSNavInsert.setStrategyName(makerPMSNav.getStrategyName());
                            		makerPMSNavInsert.setFileUpload(fileUpload);
                            		makerPMSNavInsert.setStartDate(startDate);
                            		makerPMSNavInsert.setEndDate(endDate);
                            		makerPMSNavInsert.setIsDeleted(0);
                            		makerPMSNavRepository.save(makerPMSNavInsert);
                            	}
                            }

                            if(bcadMakerPMSNavList != null) {
                            	for (BCADMakerPMSNav bcadMakerPMSNav :bcadMakerPMSNavList) {
                            		BCADMakerPMSNav bcadMakerPMSNavInsert = new BCADMakerPMSNav();
                            		bcadMakerPMSNavInsert.setClientManagement(bcadMakerPMSNav.getClientManagement());
                            		bcadMakerPMSNavInsert.setCodeScheme(bcadMakerPMSNav.getCodeScheme());
                            		bcadMakerPMSNavInsert.setCalcPmsNav(bcadMakerPMSNav.getCalcPmsNav());
                            		bcadMakerPMSNavInsert.setSelectedStartDate(bcadMakerPMSNav.getSelectedStartDate());
                            		bcadMakerPMSNavInsert.setInvestmentMaster(bcadMakerPMSNav.getInvestmentMaster());
                            		bcadMakerPMSNavInsert.setStartDate(bcadMakerPMSNav.getStartDate());
                            		bcadMakerPMSNavInsert.setEndDate(bcadMakerPMSNav.getEndDate());
                            		bcadMakerPMSNavInsert.setFileUpload(uploadMasterFiles);
                            		bcadMakerPMSNavInsert.setIsDeleted(0);
                            		bcadMakerPMSNavRepository.save(bcadMakerPMSNavInsert);
                            	}
                            }

                            if(listMakerProfitShare != null) {
                            	for (MakerProfitShare makerProfitShare :listMakerProfitShare) {
                            		MakerProfitShare makerProfitShareInsert = new MakerProfitShare();
                            		makerProfitShareInsert.setFileUpload(fileUpload1);
                            		makerProfitShareInsert.setPmsClientMaster(makerProfitShare.getPmsClientMaster());
                            		makerProfitShareInsert.setInvestmentMaster(makerProfitShare.getInvestmentMaster());
                            		makerProfitShareInsert.setProfitShareIncome(makerProfitShare.getProfitShareIncome());
                            		makerProfitShareInsert.setReceiptDate(makerProfitShare.getReceiptDate());
                            		makerProfitShareInsert.setIsDeleted(0);
                            		makerProfitShareInsert.setStartDate(makerProfitShare.getStartDate());
                            		makerProfitShareInsert.setEndDate(makerProfitShare.getEndDate());
                            		makerProfitShareRepository.save(makerProfitShareInsert);
                            	}
                            }

                            if(listBCADMakerProfitShare != null) {
                            	for (BCADMakerProfitShare bcadMakerProfitShare :listBCADMakerProfitShare) {
                            		BCADMakerProfitShare bcadMakerProfitShareInsert = new BCADMakerProfitShare();
                            		bcadMakerProfitShareInsert.setClientManagement(bcadMakerProfitShare.getClientManagement());
                            		bcadMakerProfitShareInsert.setProfitShareIncome(bcadMakerProfitShare.getProfitShareIncome());
                            		bcadMakerProfitShareInsert.setReceiptDate(bcadMakerProfitShare.getReceiptDate());
                            		bcadMakerProfitShareInsert.setFileUpload(uploadMasterFiles1);
                            		bcadMakerProfitShareInsert.setStartDate(bcadMakerProfitShare.getStartDate());
                            		bcadMakerProfitShareInsert.setEndDate(bcadMakerProfitShare.getEndDate());
                            		bcadMakerProfitShareInsert.setIsDeleted(0);
                            		bcadMakerProfitShareRepository.save(bcadMakerProfitShareInsert);
                            	}
                            }

//                            for (MakerPMSNav makerPMSNav : list) {
//                                Integer sFlag = 0;
//                                if (makerPMSNav.getFeeType().contains("Management")) {
//                                    //Product product = productRepository.findByProductName("BCAD");
//                                    if (makerPMSNav.getPmsClientMaster().getDistributorMaster() != null && makerPMSNav.getStrategyName().equals("BCAD")) {
//                                        List<CommissionDefinition> checkOption3 = commissionDefinitionRepository.findOption3Commission(
//                                            makerPMSNav.getPmsClientMaster().getDistributorMaster().getId(), product1.getId());
//                                        if (checkOption3 != null && checkOption3.size()!=0)
//                                            sFlag = 1;
//                                    }
//                                    if (!makerPMSNav.getStrategyName().equals("BCAD") || sFlag == 1) {
//                                        makerPMSNav.setFileUpload(fileUpload);
//                                        makerPMSNav.setStartDate(startDate);
//                                        makerPMSNav.setEndDate(endDate);
//                                        //System.out.println("Start date---" + makerPMSNav.getSelectedStartDate());
//                                        makerPMSNav.setIsDeleted(0);
//                                        makerPMSNavRepository.save(makerPMSNav);
//                                    }
//                                    if (makerPMSNav.getStrategyName().equals("BCAD") && sFlag == 0) {
//                                        BCADMakerPMSNav bcadMakerPMSNav = new BCADMakerPMSNav();
//                                        System.out.println(makerPMSNav.getPmsClientMaster().getClientCode());
//                                        ClientManagement clientManagement = clientManagementRepository.findByClientCode(makerPMSNav.getPmsClientMaster().getClientCode());
//                                        bcadMakerPMSNav.setClientManagement(clientManagement);
//                                        bcadMakerPMSNav.setCodeScheme(makerPMSNav.getCodeScheme());
//                                        bcadMakerPMSNav.setCalcPmsNav(makerPMSNav.getCalcPmsNav());
//                                        bcadMakerPMSNav.setSelectedStartDate(makerPMSNav.getSelectedStartDate());
//                                        bcadMakerPMSNav.setInvestmentMaster(makerPMSNav.getInvestmentMaster());
//                                        bcadMakerPMSNav.setFileUpload(uploadMasterFiles);
//                                        bcadMakerPMSNav.setStartDate(startDate);
//                                        bcadMakerPMSNav.setEndDate(endDate);
//                                        //System.out.println("Start date---" + bcadMakerPMSNav.getSelectedStartDate());
//                                        bcadMakerPMSNav.setIsDeleted(0);
//                                        bcadMakerPMSNavRepository.save(bcadMakerPMSNav);
//                                    }
//                                } else {
//                                    MakerProfitShare makerProfitShare = new MakerProfitShare();
//                                    sFlag = 0;
//                                    if (makerPMSNav.getPmsClientMaster().getDistributorMaster() != null && makerPMSNav.getStrategyName().equals("BCAD")) {
//                                        Product product = productRepository.findByProductName("BCAD");
//                                        List<CommissionDefinition> checkOption3 = commissionDefinitionRepository.findOption3Commission(
//                                            makerPMSNav.getPmsClientMaster().getDistributorMaster().getId(), product.getId());
//                                        if (checkOption3 != null)
//                                            sFlag = 1;
//                                    }
//                                    // InvestmentMaster investmentMaster = investmentMasterRepository.findByInvestmentName(makerPMSNav.getStrategyName());
//                                    if (!makerPMSNav.getStrategyName().equals("BCAD") || sFlag == 1) {
//                                        makerProfitShare.setFileUpload(fileUpload1);
//                                        makerProfitShare.setPmsClientMaster(makerPMSNav.getPmsClientMaster());
//                                        makerProfitShare.setInvestmentMaster(makerPMSNav.getInvestmentMaster());
//                                        makerProfitShare.setProfitShareIncome(makerPMSNav.getCalcPmsNav());
//                                        makerProfitShare.setReceiptDate(makerPMSNav.getSelectedStartDate());
//                                        makerProfitShare.setIsDeleted(0);
//                                        makerProfitShare.setStartDate(startDate);
//                                        makerProfitShare.setInvestmentDate(makerPMSNav.getInvestmentDate());
//                                        makerProfitShare.setEndDate(endDate);
//                                        makerProfitShareRepository.save(makerProfitShare);
//                                    }
//                                    if (makerPMSNav.getStrategyName().equals("BCAD") && sFlag == 0) {
//                                        BCADMakerProfitShare bcadMakerProfitShare = new BCADMakerProfitShare();
//                                        ClientManagement clientManagement = clientManagementRepository.findByClientCode(makerPMSNav.getPmsClientMaster().getClientCode());
//                                        bcadMakerProfitShare.setClientManagement(clientManagement);
//                                        bcadMakerProfitShare.setProfitShareIncome(makerPMSNav.getCalcPmsNav());
//                                        //makerProfitShare.setInvestmentMaster(makerPMSNav.getInvestmentMaster());
//                                        bcadMakerProfitShare.setReceiptDate(makerPMSNav.getSelectedStartDate());
//                                        bcadMakerProfitShare.setFileUpload(uploadMasterFiles1);
//                                        bcadMakerProfitShare.setStartDate(startDate);
//                                        bcadMakerProfitShare.setEndDate(endDate);
//                                        bcadMakerProfitShare.setIsDeleted(0);
//                                        bcadMakerProfitShareRepository.save(bcadMakerProfitShare);
//                                    }
//
//                                }
//                            }


                        }
                    }
                    if (enableSavePMSNav == false) {
                        fileUpload = new FileUpload();
                        fileUpload.setCode("470");
                        fileUpload.setStatus(prop.getString("fee.file.folder") + "DFA Backup\\" +
                            prop.getString("bulk.upload.not") + "\\" + fileName);
                        System.out.println("pmscheck");
                        masterCreate(masterLogBean);
                        setEnablePMSDownload(false);
                    }


                } else {
                    fileUpload = new FileUpload();
                    fileUpload.setCode("409");
                    System.out.println(fileUpload);
                    return fileUpload;
                    //UnifiJsfUtils .addErrorMessage("Uploaded file already exists, Delete previous file and try again");
                }
            } else {
                fileUpload = new FileUpload();
                fileUpload.setCode("400");
                System.out.println(fileUpload);
                return fileUpload;
                // UnifiJsfUtils.addErrorMessage("Uploaded file is not in correct month");
            }
        }
        // Capital Transactions
        if (fileType.contains(prop.getString("pms.investment.transaction"))) {
            String loggedUser;

            String sFilesDirectory = prop.getString("fee.file.folder") + "DFA Backup\\\\"
                + prop.getString("file.fee.profit") + "\\\\" + sFinal;
            /*String sFilesDirectory = prop.getString("fee.file.folder") + "DFA Backup////"
                + prop.getString("pms.investment.transaction") + "////" + sFinal;
          */  File dirFiles = new File(sFilesDirectory);

            dirFiles.mkdirs();
            if (fileName.contains(sLoc)) {
                File sFile = new File(dirFiles, fileName);
                try {
                    fileUpload = fileUploadRepository.findByFileLocationAndIsDeleted(sFile.getPath().toString(), 0);
                } catch (Exception e) {
                    System.out.println(e);
                }
                if (fileUpload == null) {
                    writeFile(fileStream, sFile);
                    System.out.println("File path" + sFile.getPath());
                    List<MakerCapitalTransaction> list = readCapitalTransaction(sFile.getPath(), startDate, endDate);
                    if (enableSavePMSNav){
                    if (list.isEmpty() != true) {
                        FileType fileType1 = fileTypeRepository.findByFileType("Capital Transaction");
                        fileUpload = new FileUpload();
                        fileUpload.setFileLocation(sFile.getPath());
                        fileUpload.setFileType(fileType1);
                        fileUpload.setIsDeleted(0);
                        fileUpload.setUploadApproved((long) 0);
                        fileUploadRepository.save(fileUpload);

                        Set<MakerCapitalTransaction> hs = new HashSet<>();
                        hs.addAll(list);
                        list.clear();
                        list.addAll(hs);

                        for (MakerCapitalTransaction makerCapitalTransaction : list) {
                            makerCapitalTransaction.setFileUpload(fileUpload);
                            makerCapitalTransaction.setStartDate(startDate);
                            makerCapitalTransaction.setEndDate(endDate);
                            makerCapitalTransaction.setIsDeleted(0);
                            makerCapitalTransactionRepository.save(makerCapitalTransaction);
                        }

                    }}
                    if (enableSavePMSNav == false) {
                        fileUpload = new FileUpload();
                        fileUpload.setCode("470");
                        fileUpload.setStatus(prop.getString("fee.file.folder") + "DFA Backup\\" +
                            prop.getString("bulk.upload.not") + "\\" + fileName);


                        System.out.println("pmscheck");
                        masterCreate(masterLogBean);
                        setEnablePMSDownload(false);
                    }
            } else {
                fileUpload = new FileUpload();
                fileUpload.setCode("409");
                System.out.println(fileUpload);
                return fileUpload;
                   /* UnifiJsfUtils
                        .addErrorMessage("Uploaded file already exists, Delete previous file and try again");*/
            }
        }
        else {
            fileUpload = new FileUpload();
            fileUpload.setCode("400");
            System.out.println(fileUpload);
            return fileUpload;

            // UnifiJsfUtils.addErrorMessage("Uploaded file is not in correct month");
        }

        }

        System.out.println(fileUpload);
        return fileUpload;

    }

    public PMSClientMaster uploadPMSDetailFile( MultipartFile multipartFile) throws IOException,Exception {
        fileStream = IOUtils.toByteArray(multipartFile.getInputStream());
        int index = multipartFile.getOriginalFilename().length();
        System.out.println("File Details"+multipartFile.getOriginalFilename());
        setFileName(multipartFile.getOriginalFilename().substring(0, index ));

        String sFilesDirectory = prop.getString("fee.file.folder") + "DFA Backup\\"+
            prop.getString("bulk.upload.not");
        File dirFiles = new File(sFilesDirectory);
        dirFiles.mkdirs();

        File sFile = new File(dirFiles, fileName);

        writeFile(fileStream, sFile);
        System.out.println("File completed");
        PMSClientMaster  list = readPMSNewDetailsFile(sFile.getPath());

        return list;
    }

    private PMSClientMaster readPMSNewDetailsFile(String path ) throws IOException, URISyntaxException {

        // Excel to database
        List<PMSClientMaster> listMakerBrokerageDOs = new ArrayList<>();
        PMSClientMaster pmsClientMaster = new PMSClientMaster();
        List<MasterLogBean> masterLogBean=new ArrayList<>();
        MasterLogBean masterLogs = new MasterLogBean();
        setEnableSave(true);
        masterLogBean = new ArrayList<>();
        try {
            FileInputStream inputStream = new FileInputStream(path);
            Workbook workbook = new HSSFWorkbook(inputStream);
            Sheet mySheet = workbook.getSheetAt(0);

            Iterator<Row> rowIterator = mySheet.iterator();

            FileUpload fileUpload = null;
            int cellIdx = 0;int rowNu=0;String distName = null; String rName = null;String dName= null; String code= null;  String codeSplit = null;String codeSplit1 = null;
            while (rowIterator.hasNext()) {
                pmsClientMaster = new PMSClientMaster();
                Row row = rowIterator.next();
                if(rowNu>0) {
                    int schemeId = 0 ; int rId =0; int distId =0;
                    int id = (int) row.getCell(0).getNumericCellValue();
                    pmsClientMaster.setClientId(id);

                    String name = row.getCell(1).getStringCellValue();
                    pmsClientMaster.setClientName(name);

                    code = row.getCell(2).getStringCellValue();
                    pmsClientMaster.setClientCode(code);

                    System.out.println(row.getCell(5).getDateCellValue());
                    Date sDate= row.getCell(5).getDateCellValue();
//                    Cell cell1 = row.getCell(5);
//                    cell1.setCellType(CellType.STRING);
//                    String sDate = cell1.getStringCellValue();
//                    DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
//                    Date dStart = dateFormat.parse(sDate);
                    pmsClientMaster.setAccountOpenDate(sDate);

                    DateFormat firstDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.s");
                    String count= firstDateFormat.parse(prop.getString("pms.strategy.date")).after(sDate) ? "OLD": "NEW";

                    pmsClientMaster.setSlab(count);
                    pmsClientMaster.setSlab(count);
                    String schemeName = row.getCell(52).getStringCellValue();
                    if(code.startsWith("U") && schemeName.equalsIgnoreCase("UNIFI AIF Umbrella Blend Fund - 2")){
                        System.out.println(" code inside if"+code);



                        if (!schemeName.equals("")) {
                            InvestmentMaster investmentMaster = investmentMasterRepository.findByInvestmentName(schemeName);
                            if (investmentMaster == null)
                                schemeId++;
                            else
                                pmsClientMaster.setInvestmentMaster(investmentMaster);
                        }

                        String rsname = row.getCell(58).getStringCellValue();
                        String output[] = rsname.split("\\(");
                        rName =output[0];
                        System.out.println("rname"+rName);
                        if (!rName.equals("")) {
                            RelationshipManager relationshipManager = relationshipManagerRepository.findByRelationName(rName);
                            if (relationshipManager == null)
                                rId++;
                            else
                                pmsClientMaster.setRelationshipManager(relationshipManager);
                        }

                        String intermediaryNameDistributor = row.getCell(50).getStringCellValue();
                        String arrayName[] = intermediaryNameDistributor.split("[_-]");
                        dName = arrayName[0];
                        if (!intermediaryNameDistributor.equals("")) {
//                                System.out.println("dist name"+dName);
////                                System.out.println("dist name sub"+dName.indexOf("("));
//                                System.out.println("after dist name"+dName.substring(0,dName.indexOf("(")));
                            String dNameSplit[] = dName.split(" \\(AIF");
                            dName =dNameSplit[0];
                            System.out.println("dName"+dName);
                            DistributorMaster distributorMaster = distributorMasterRepository.findByDistributorName(dName);
                            if (distributorMaster == null)
                                distId++;
                            else
                                pmsClientMaster.setDistributorMaster(distributorMaster);
                        }

                        if ((!schemeName.equals("")) && (schemeId == 1) || ((!rName.equals("")) && (rId ==1)) || ((!intermediaryNameDistributor.equals("")) && (distId ==1)) )  {
                            System.out.println("if ");
                            masterLogs = new MasterLogBean();
                            masterLogs.setClientCode(code);
                            masterLogs.setClientName(name);
                            masterLogs.setStrategyName(schemeName);
                            masterLogs.setRmName(rName);
                            masterLogs.setDistName(dName);
                            System.out.println("schem name"+schemeId);
                            System.out.println("rId"+rId);
                            System.out.println("distId"+distId);

                            if (distId == 1)
                                masterLogs.settStatus("Distributor Not Found");
                            if (rId == 1)
                                masterLogs.settStatus("RM Not Found");
                            if (schemeId == 1)
                                masterLogs.settStatus("Scheme Not Found");
                            if (schemeId == 1 && rId == 1 && distId ==1)
                                masterLogs.settStatus("Scheme,RM and Distributor NOT Found");
                            if (schemeId == 1 && rId != 1 && distId ==1)
                                masterLogs.settStatus("Scheme and Distributor Not Found");
                            if (schemeId == 1 && rId == 1 && distId !=1)
                                masterLogs.settStatus("Scheme and RM Not Found");
                            if (schemeId != 1 && rId == 1 && distId ==1)
                                masterLogs.settStatus("Distributor and RM Not Found");

                            masterLogBean.add(masterLogs);
                            setEnableSave(false);
                        }
                        else {
                            System.out.println("else ");

                            PMSClientMaster pmsClientMaster2 = pmsClientMasterRepository.findByClientCode(code);
                            if(pmsClientMaster2 != null) {
                                pmsClientMaster.setId(pmsClientMaster2.getId());
                                pmsClientMaster.setSchemeName(schemeName);
                                pmsClientMaster.setRelmgrName(rName);
                                pmsClientMaster.setIntermediaryNameDistributor(dName);
                                listMakerBrokerageDOs.add(pmsClientMaster);
                            }
                            else {
                                pmsClientMaster.setSchemeName(schemeName);
                                pmsClientMaster.setRelmgrName(rName);
                                pmsClientMaster.setIntermediaryNameDistributor(dName);
                                listMakerBrokerageDOs.add(pmsClientMaster);
                            }
                        }


                    }

                    else {
                        if (!schemeName.equals("")) {
                            InvestmentMaster investmentMaster = investmentMasterRepository.findByInvestmentName(schemeName);
                            if (investmentMaster == null)
                                schemeId++;
                            else
                                pmsClientMaster.setInvestmentMaster(investmentMaster);
                        }

                        String rsname = row.getCell(58).getStringCellValue();
                        String output[] = rsname.split("\\(");
                        rName =output[0];
                        if (!rName.equals("")) {
                            RelationshipManager relationshipManager = relationshipManagerRepository.findByRelationName(rName);
                            if (relationshipManager == null)
                                rId++;
                            else
                                pmsClientMaster.setRelationshipManager(relationshipManager);
                        }

                        String intermediaryNameDistributor = row.getCell(50).getStringCellValue();
                        String arrayName[] = intermediaryNameDistributor.split("[_-]");
                        dName = arrayName[0];
                        if (!intermediaryNameDistributor.equals("")) {
                            DistributorMaster distributorMaster = distributorMasterRepository.findByDistributorName(dName);
                            if (distributorMaster == null)
                                distId++;
                            else
                                pmsClientMaster.setDistributorMaster(distributorMaster);
                        }

                        if ((!schemeName.equals("")) && (schemeId == 1) || ((!rName.equals("")) && (rId ==1)) || ((!intermediaryNameDistributor.equals("")) && (distId ==1)) )  {
                            masterLogs = new MasterLogBean();
                            masterLogs.setClientCode(code);
                            masterLogs.setClientName(name);
                            masterLogs.setStrategyName(schemeName);
                            masterLogs.setRmName(rName);
                            masterLogs.setDistName(dName);

                            if (distId == 1)
                                masterLogs.settStatus("Distributor Not Found");
                            if (rId == 1)
                                masterLogs.settStatus("RM Not Found");
                            if (schemeId == 1)
                                masterLogs.settStatus("Scheme Not Found");
                            if (schemeId == 1 && rId == 1 && distId ==1)
                                masterLogs.settStatus("Scheme,RM and Distributor NOT Found");
                            if (schemeId == 1 && rId != 1 && distId ==1)
                                masterLogs.settStatus("Scheme and Distributor Not Found");
                            if (schemeId == 1 && rId == 1 && distId !=1)
                                masterLogs.settStatus("Scheme and RM Not Found");
                            if (schemeId != 1 && rId == 1 && distId ==1)
                                masterLogs.settStatus("Distributor and RM Not Found");

                            masterLogBean.add(masterLogs);
                            setEnableSave(false);
                        }
                        else {
                            PMSClientMaster pmsClientMaster2 = pmsClientMasterRepository.findByClientCode(code);
                            if(pmsClientMaster2 != null) {
                                pmsClientMaster.setId(pmsClientMaster2.getId());
                                pmsClientMaster.setSchemeName(schemeName);
                                pmsClientMaster.setRelmgrName(rName);
                                pmsClientMaster.setIntermediaryNameDistributor(dName);
                                listMakerBrokerageDOs.add(pmsClientMaster);
                            }
                            else {
                                pmsClientMaster.setSchemeName(schemeName);
                                pmsClientMaster.setRelmgrName(rName);
                                pmsClientMaster.setIntermediaryNameDistributor(dName);
                                listMakerBrokerageDOs.add(pmsClientMaster);
                            }
                        }
                    }
                    cellIdx++;
                }
                rowNu++;
            }


            if(getEnableSave()==false){
                if (masterLogBean.size() != 0) {
                    pmsClientMaster.setCode("450");
                    pmsClientMaster.setStatus(prop.getString("fee.file.folder") + "DFA Backup\\"+
                        prop.getString("bulk.upload.not")+ "\\"+fileName);
                    pmsMasterCreate(masterLogBean);
                    listMakerBrokerageDOs= new ArrayList<>();
                }
            }

            if(getEnableSave()==true){
                pmsClientMaster.setCode("420");
                if (listMakerBrokerageDOs.size() != 0) {
                    pmsClientMaster=saveClient(listMakerBrokerageDOs);
                }
            }

            // workBook.close();
        } catch (Exception exception) {
            log.error("Exception at DataUploadEditForm Brokerage File Format" + exception);
        }
        return pmsClientMaster;
    }

    private void pmsMasterCreate(List<MasterLogBean> masterLogBean) throws IOException {
        try {
            String sFilesDirectory = prop.getString("fee.file.folder") + "DFA Backup\\"
                + prop.getString("bulk.upload.not");
            File dirFiles = new File(sFilesDirectory);
            dirFiles.mkdirs();

            String file = sFilesDirectory + "\\\\" + fileName;


            fos = new FileOutputStream(file);
            workBook = new HSSFWorkbook();

            String reportDate = "RM_SchemeName";
            HSSFSheet sheet = workBook.createSheet(reportDate);
            CellStyle cs = workBook.createCellStyle();
            CellStyle csRight = workBook.createCellStyle();

            HSSFFont fFont = workBook.createFont();
            fFont.setBold(true);
            cs.setFont(fFont);

            csRight.setAlignment(HorizontalAlignment.RIGHT);


            int iRowNo = sheet.getLastRowNum() + 1;
            HSSFRow headingBRSBookRow = sheet.createRow(iRowNo);
            headingBRSBookRow.createCell(0).setCellValue("Client Code");
            headingBRSBookRow.getCell(0).setCellStyle(cs);
            sheet.autoSizeColumn(0);
            headingBRSBookRow.createCell(1).setCellValue("Client Name");
            headingBRSBookRow.getCell(1).setCellStyle(cs);
            sheet.autoSizeColumn(1);
            headingBRSBookRow.createCell(2).setCellValue("Scheme Name");
            headingBRSBookRow.getCell(2).setCellStyle(cs);
            sheet.autoSizeColumn(2);
            headingBRSBookRow.createCell(3).setCellValue("RM Name");
            headingBRSBookRow.getCell(3).setCellStyle(cs);
            sheet.autoSizeColumn(3);
            headingBRSBookRow.createCell(4).setCellValue("Distributor Name");
            headingBRSBookRow.getCell(4).setCellStyle(cs);
            sheet.autoSizeColumn(4);
            headingBRSBookRow.createCell(5).setCellValue("Status");
            headingBRSBookRow.getCell(5).setCellStyle(cs);
            sheet.autoSizeColumn(5);
            iRowNo = sheet.getLastRowNum() + 2;

            for (MasterLogBean bean : masterLogBean) {
                HSSFRow row = sheet.createRow(iRowNo);

                row.createCell(0).setCellValue(bean.getClientCode());
                sheet.autoSizeColumn(0);
                row.createCell(1).setCellValue(bean.getClientName());
                sheet.autoSizeColumn(1);
                row.createCell(2).setCellValue(bean.getStrategyName());
                sheet.autoSizeColumn(2);
                row.createCell(3).setCellValue(bean.getRmName());
                sheet.autoSizeColumn(3);
                row.createCell(4).setCellValue(bean.getDistName());
                sheet.autoSizeColumn(4);
                row.createCell(5).setCellValue(bean.gettStatus());
                sheet.autoSizeColumn(5);
                iRowNo++;
            }
            System.out.println("masterlog file uploaded");
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

    private void CommissionDefCreate(List<MasterLogBean> masterLogBean) throws IOException {
        try {
            String sFilesDirectory = prop.getString("fee.file.folder") + "DFA Backup\\"
                + prop.getString("bulk.upload.not");
            File dirFiles = new File(sFilesDirectory);
            dirFiles.mkdirs();

            String file = sFilesDirectory + "\\" + fileName;


            fos = new FileOutputStream(file);
            workBook = new HSSFWorkbook();

            String reportDate = "Commission_Definition";
            HSSFSheet sheet = workBook.createSheet(reportDate);
            CellStyle cs = workBook.createCellStyle();
            CellStyle csRight = workBook.createCellStyle();

            HSSFFont fFont = workBook.createFont();
            fFont.setBold(true);
            cs.setFont(fFont);

            csRight.setAlignment(HorizontalAlignment.RIGHT);


            int iRowNo = sheet.getLastRowNum() + 1;
            HSSFRow headingBRSBookRow = sheet.createRow(iRowNo);
            headingBRSBookRow.createCell(0).setCellValue("Distributor Name");
            headingBRSBookRow.getCell(0).setCellStyle(cs);
            sheet.autoSizeColumn(0);
            headingBRSBookRow.createCell(1).setCellValue("Invest Name");
            headingBRSBookRow.getCell(1).setCellStyle(cs);
            sheet.autoSizeColumn(1);
            headingBRSBookRow.createCell(2).setCellValue("Invest Status");
            headingBRSBookRow.getCell(2).setCellStyle(cs);
            sheet.autoSizeColumn(2);
            headingBRSBookRow.createCell(3).setCellValue("Status");
            headingBRSBookRow.getCell(3).setCellStyle(cs);
            sheet.autoSizeColumn(3);

            iRowNo = sheet.getLastRowNum() + 2;

            for (MasterLogBean bean : masterLogBean) {
                HSSFRow row = sheet.createRow(iRowNo);

                row.createCell(0).setCellValue(bean.getDistName());
                sheet.autoSizeColumn(0);
                row.createCell(1).setCellValue(bean.getStrategyName());
                sheet.autoSizeColumn(1);
                row.createCell(2).setCellValue(bean.getStrategyStatus());
                sheet.autoSizeColumn(2);
                row.createCell(3).setCellValue(bean.gettStatus());
                sheet.autoSizeColumn(3);

                iRowNo++;
            }
            System.out.println("commission Definition file uploaded");
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

    private void CommissionDefCreateMutiple(List<MasterLogBean> masterLogBean,List<MasterLogBean> masterLogBeanList) throws IOException {
        try {
            String sFilesDirectory = prop.getString("fee.file.folder") + "DFA Backup\\"
                + prop.getString("bulk.upload.not");
            File dirFiles = new File(sFilesDirectory);
            dirFiles.mkdirs();

            String file = sFilesDirectory + "\\" + fileName;


            fos = new FileOutputStream(file);
            workBook = new HSSFWorkbook();

            String reportDate = "Commission_Definition";
            String reportDates = "Commission";
            HSSFSheet sheet1 = workBook.createSheet(reportDate);
            HSSFSheet sheet2 = workBook.createSheet(reportDates);
            CellStyle cs = workBook.createCellStyle();
            CellStyle csRight = workBook.createCellStyle();

            HSSFFont fFont = workBook.createFont();
            fFont.setBold(true);
            cs.setFont(fFont);

            csRight.setAlignment(HorizontalAlignment.RIGHT);


            int iRowNo = sheet1.getLastRowNum() + 1;
            HSSFRow headingBRSBookRow = sheet1.createRow(iRowNo);
            headingBRSBookRow.createCell(0).setCellValue("Distributor Name");
            headingBRSBookRow.getCell(0).setCellStyle(cs);
            sheet1.autoSizeColumn(0);
            headingBRSBookRow.createCell(1).setCellValue("Invest Name");
            headingBRSBookRow.getCell(1).setCellStyle(cs);
            sheet1.autoSizeColumn(1);
            headingBRSBookRow.createCell(2).setCellValue("Invest Status");
            headingBRSBookRow.getCell(2).setCellStyle(cs);
            sheet1.autoSizeColumn(2);
            headingBRSBookRow.createCell(3).setCellValue("Status");
            headingBRSBookRow.getCell(3).setCellStyle(cs);
            sheet1.autoSizeColumn(3);

            iRowNo = sheet1.getLastRowNum() + 2;

            for (MasterLogBean bean : masterLogBean) {
                HSSFRow row = sheet1.createRow(iRowNo);

                row.createCell(0).setCellValue(bean.getDistName());
                sheet1.autoSizeColumn(0);
                row.createCell(1).setCellValue(bean.getStrategyName());
                sheet1.autoSizeColumn(1);
                row.createCell(2).setCellValue(bean.getStrategyStatus());
                sheet1.autoSizeColumn(2);
                row.createCell(3).setCellValue(bean.gettStatus());
                sheet1.autoSizeColumn(3);

                iRowNo++;
            }

            int iRowNo1 = sheet2.getLastRowNum() + 1;
            HSSFRow headingBRSBookRows = sheet2.createRow(iRowNo1);
            headingBRSBookRows.createCell(0).setCellValue("Distributor Name");
            headingBRSBookRows.getCell(0).setCellStyle(cs);
            sheet2.autoSizeColumn(0);
            headingBRSBookRows.createCell(1).setCellValue("Invest Name");
            headingBRSBookRows.getCell(1).setCellStyle(cs);
            sheet2.autoSizeColumn(1);
            headingBRSBookRows.createCell(2).setCellValue("Invest Status");
            headingBRSBookRows.getCell(2).setCellStyle(cs);
            sheet2.autoSizeColumn(2);
            headingBRSBookRows.createCell(3).setCellValue("Status");
            headingBRSBookRows.getCell(3).setCellStyle(cs);
            sheet2.autoSizeColumn(3);

            iRowNo1 = sheet2.getLastRowNum() + 2;

            for (MasterLogBean bean : masterLogBeanList) {
                HSSFRow row = sheet2.createRow(iRowNo1);

                row.createCell(0).setCellValue(bean.getDistName());
                sheet2.autoSizeColumn(0);
                row.createCell(1).setCellValue(bean.getStrategyName());
                sheet2.autoSizeColumn(1);
                row.createCell(2).setCellValue(bean.getStrategyStatus());
                sheet2.autoSizeColumn(2);
                row.createCell(3).setCellValue(bean.gettStatus());
                sheet2.autoSizeColumn(3);

                iRowNo1++;
            }
            System.out.println("commission Definition file uploaded");
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


    private PMSClientMaster saveClient(List<PMSClientMaster> pmsClientMasterList) throws IOException {
        MasterLogBean masterLogs = null;
        PMSClientMaster pmsClientMaster = new PMSClientMaster();
        ClientManagement clientManagement = new ClientManagement();
        List<MasterLogBean> masterLogBeanList = new ArrayList<>();
        List<MasterLogBean> masterLogBean=new ArrayList<>();
        for(PMSClientMaster pmsClientMasterDO:pmsClientMasterList){
            long id=0L;
            String name =pmsClientMasterDO.getSchemeName();
            String code=pmsClientMasterDO.getClientCode();
            System.out.println(name);
            if(code.equals("75004BCAD"))
            System.out.println(code);
            Product product = productRepository.findByProductName(pmsClientMasterDO.getSchemeName());
            List<CommissionDefinition> commissionDefinitionList=null;
            CommissionDefinition commissionDefinition=null;
            if(pmsClientMasterDO.getDistributorMaster()!=null)  {
                commissionDefinitionList= commissionDefinitionRepository.findOption3Commission(pmsClientMasterDO.getDistributorMaster().getId(), product.getId());
                int invest = (pmsClientMasterDO.getSlab().equals("OLD") ? 0 : 1);
                 commissionDefinition = commissionDefinitionRepository.getPMSInvestmentDateCalc(pmsClientMasterDO.getDistributorMaster().getId(), invest, product.getId());
            }
            // add BCAD type in client management table
            // not equal to opton 3 in bcad_dist_opt3

            List<Integer> option3=commissionDefinitionRepository.Option3Check(pmsClientMasterDO.getDistributorMaster().getId());
            if(pmsClientMasterDO.getSchemeName().equals("BCAD") &&(option3 == null || option3.size()==0) && (commissionDefinitionList==null || commissionDefinitionList.size()==0)) {
                String sSplits[] = pmsClientMasterDO.getClientCode().split("(?<=\\D)(?=\\d)|(?<=\\d)(?=\\D)");
                String sCode = sSplits[0];

                ClientManagement clientManagement1=clientManagementRepository.findByClientCode(sCode);
                if(clientManagement1==null)
                    clientManagement1=clientManagementRepository.findByClientCode(pmsClientMasterDO.getClientCode());

                ClientManagement result= new ClientManagement();
                if(clientManagement1 !=  null){
                    clientManagement1.setClientCode(pmsClientMasterDO.getClientCode());
                    clientManagement1.setClientName(pmsClientMasterDO.getClientName());
                    clientManagement1.setRelationshipManager(pmsClientMasterDO.getRelationshipManager());
                    clientManagement1.setDistributorMaster(pmsClientMasterDO.getDistributorMaster());
                    clientManagement1.setAccountopendate(pmsClientMasterDO.getAccountOpenDate());
                    clientManagement1.setSlab(pmsClientMasterDO.getSlab());
                    clientManagement1.setClientId(pmsClientMasterDO.getClientId());
                    clientManagementRepository.save(clientManagement1);

                }
                else {
                	ClientManagement clientManagementOne = new ClientManagement();
                	clientManagementOne.setClientCode(pmsClientMasterDO.getClientCode());
                	clientManagementOne.setClientName(pmsClientMasterDO.getClientName());
                	clientManagementOne.setRelationshipManager(pmsClientMasterDO.getRelationshipManager());
                	clientManagementOne.setDistributorMaster(pmsClientMasterDO.getDistributorMaster());
                	clientManagementOne.setAccountopendate(pmsClientMasterDO.getAccountOpenDate());
                    clientManagementOne.setSlab(pmsClientMasterDO.getSlab());
                    clientManagementOne.setClientId(pmsClientMasterDO.getClientId());
                    Product product1 = productRepository.findByProductName("BCAD");
                	clientManagementOne.setProduct(product1);
                    clientManagementRepository.save(clientManagementOne);
                }
                ClientCommission clientCommission=clientCommissionRepository.findbcadClientMaster(result.getId());
                if(clientManagement1.getDistributorMaster()!=null){
                 if(clientCommission==null) {
                    clientCommission=new ClientCommission();
                    clientCommission.setClientId(clientManagementRepository.findByClientCode(pmsClientMasterDO.getClientCode()));
                    clientCommission.setNavComm(commissionDefinition.getNavComm());
                    clientCommission.setProfitComm(commissionDefinition.getProfitComm());
                    clientCommission.setUpdateRequired(0);
                    clientCommissionRepository.save(clientCommission);
                }
                 else{
                     clientCommission.setClientId(clientManagementRepository.findByClientCode(pmsClientMasterDO.getClientCode()));
                     clientCommission.setNavComm(commissionDefinition.getNavComm());
                     clientCommission.setProfitComm(commissionDefinition.getProfitComm());
                     clientCommissionRepository.save(clientCommission);

                 }}
            }
            else if(pmsClientMasterDO.getSchemeName().equals("UNIFI AIF Umbrella Blend Fund - 2")) {
                String sSplits[] = pmsClientMasterDO.getClientCode().split("(?<=\\D)(?=\\d)|(?<=\\d)(?=\\D)");
                String sCode = sSplits[0];

                ClientManagement clientManagement1 = clientManagementRepository.findByClientCode(pmsClientMasterDO.getClientCode());
                if (clientManagement1 == null)
                    clientManagement1 = clientManagementRepository.findByClientCode(pmsClientMasterDO.getClientCode());

                ClientManagement result = new ClientManagement();
                if (clientManagement1 == null) {
                    System.out.println("client"+pmsClientMasterDO.getClientName());
                    result.setClientCode(pmsClientMasterDO.getClientCode());
                    result.setClientName(pmsClientMasterDO.getClientName());
                    result.setRelationshipManager(pmsClientMasterDO.getRelationshipManager());
                    result.setDistributorMaster(pmsClientMasterDO.getDistributorMaster());
                    result.setAccountopendate(pmsClientMasterDO.getAccountOpenDate());
                    result.setSlab(pmsClientMasterDO.getSlab());
                    result.setClientId(pmsClientMasterDO.getClientId());
                    Product product1 = productRepository.findByProductName("UNIFI AIF Umbrella Blend Fund - 2");
                    result.setProduct(product1);
                    clientManagementRepository.save(result);

                }
            }
            //Other than BCAD & option 3
            else {
                PMSClientMaster pmsClientMaster1= pmsClientMasterRepository.findByClientCode(pmsClientMasterDO.getClientCode());
                if(pmsClientMaster1==null) {
                    PMSClientMaster result = pmsClientMasterRepository.save(pmsClientMasterDO);
                    if (result.getDistributorMaster() != null) {

                            if (product.equals("BCAD")) {
                                //int invest = (pmsClientMasterDO.getSlab().equals("OLD") ?0:1);
                                //commissionDefinitionList= commissionDefinitionRepository.findOption3Commission(distributorMaster.getId(),product.getId());
                                //CommissionDefinition commissionDefinition = commissionDefinitionRepository.getPMSInvestmentDateCalc(distributorMaster.getId(),invest,product.getId());
                                if (commissionDefinition == null && (commissionDefinitionList == null || commissionDefinitionList.size()==0)) {
                                    masterLogs = new MasterLogBean();
                                    masterLogs.setDistName(result.getDistributorMaster().getDistName());
                                    masterLogs.setStrategyName(result.getInvestmentMaster().getInvestmentName());
                                    masterLogs.setStrategyStatus(result.getSlab());
                                    masterLogs.settStatus("Commission definition not Found");
                                    masterLogBean.add(masterLogs);
                                    setEnableSave(false);
                                } }
                            else if(commissionDefinition == null && (commissionDefinitionList == null || commissionDefinitionList.size()==0)) {
                                      /*
                                     // Its not required
                                      clientManagement.setClientName(result.getClientName());
                                       clientManagement.setRelationshipManager(result.getRelationshipManager());
                                       cliex`x`ntManagement.setDistributorMaster(result.getDistributorMaster());
                                       clientManagementRepository.save(clientManagement);*/

                                masterLogs = new MasterLogBean();
                                masterLogs.setDistName(result.getDistributorMaster().getDistName());
                                masterLogs.setStrategyName(result.getInvestmentMaster().getInvestmentName());
                                masterLogs.setStrategyStatus(result.getSlab());
                                masterLogs.settStatus("Commission definition not Found");
                                masterLogBean.add(masterLogs);
                                setEnableSave(false);
                                }
                            else{
                                ClientFeeCommission clientFeeCommission = new ClientFeeCommission();
                                clientFeeCommission.setPmsClientMaster(result);
                                clientFeeCommission.setNavComm(commissionDefinition.getNavComm());
                                clientFeeCommission.setProfitComm(commissionDefinition.getProfitComm());
                                clientFeeCommission.setUpdateRequired(0);
                                clientFeeCommissionRepository.save(clientFeeCommission);
                            }


                            }
                   /* else {

                                result=pmsClientMasterRepository.save(pmsClientMasterDO);
                                 id = result.getId();
                                ClientFeeCommission clientFeeCommission1 =clientFeeCommissionRepository.findPmsClientMaster(id);
                                if(clientFeeCommission1 != null){
                                    clientFeeCommission1.setPmsClientMaster(result);
                                    clientFeeCommission1.setNavComm(commissionDefinition.getNavComm());
                                    clientFeeCommission1.setProfitComm(commissionDefinition.getProfitComm());
                                    clientFeeCommissionRepository.save(clientFeeCommission1);
                                }
                                else{
                                    ClientFeeCommission clientFeeCommission = new ClientFeeCommission();
                                    clientFeeCommission.setPmsClientMaster(result);
                                    clientFeeCommission1.setNavComm(commissionDefinition.getNavComm());
                                    clientFeeCommission1.setProfitComm(commissionDefinition.getProfitComm());
                                    clientFeeCommissionRepository.save(clientFeeCommission);
                                }
                            }*/
                    }
                else {
                    pmsClientMasterDO.setId(pmsClientMaster1.getId());
                    PMSClientMaster result1= pmsClientMasterRepository.save(pmsClientMasterDO);
                    id =result1.getId();

                        if (commissionDefinition == null) {
                            masterLogs = new MasterLogBean();
                            masterLogs.setDistName(result1.getDistributorMaster().getDistName());
                            masterLogs.setStrategyName(result1.getInvestmentMaster().getInvestmentName());
                            masterLogs.setStrategyStatus(result1.getSlab());
                            masterLogs.settStatus("Commission definition not Found");
                            masterLogBeanList.add(masterLogs);
                            setEnableSave(false);

                        }
                        else {
                            ClientFeeCommission clientFeeCommission = clientFeeCommissionRepository.findPmsClientMaster(id);
                            if (clientFeeCommission != null) {
                                clientFeeCommission.setPmsClientMaster(result1);
                                clientFeeCommission.setNavComm(commissionDefinition.getNavComm());
                                clientFeeCommission.setProfitComm(commissionDefinition.getProfitComm());
                                clientFeeCommission.setUpdateRequired(0);
                                clientFeeCommissionRepository.save(clientFeeCommission);
                            }
                            else {
                                ClientFeeCommission clientFeeCommission1 = new ClientFeeCommission();
                                clientFeeCommission1.setPmsClientMaster(result1);
                                clientFeeCommission1.setNavComm(commissionDefinition.getNavComm());
                                clientFeeCommission1.setProfitComm(commissionDefinition.getProfitComm());
                                clientFeeCommission1.setUpdateRequired(0);
                                clientFeeCommissionRepository.save(clientFeeCommission1);
                            }
                        }


                }

            }
        }
        if(getEnableSave()==false){
            if (masterLogBean.size() != 0 || masterLogBeanList.size() !=0) {
                pmsClientMaster.setCode("450");
               pmsClientMaster.setStatus(prop.getString("fee.file.folder") + "DFA Backup\\"+
                    prop.getString("bulk.upload.not")+ "\\"+fileName);
               // CommissionDefCreate(masterLogBean);
                CommissionDefCreateMutiple(masterLogBean,masterLogBeanList);

            }
        }
        if(getEnableSave()==true){
            pmsClientMaster.setCode("420");
        }
        return pmsClientMaster;
    }

    private List<MakerCapitalTransaction> readCapitalTransaction(String path, Date startDate, Date endDate) throws IOException, InvalidFormatException, ParseException {
        FileInputStream fis = new FileInputStream(path);
        Workbook workbook = WorkbookFactory.create(fis);

        Sheet sheet = workbook.getSheetAt(0);
        DataFormatter dataFormatter = new DataFormatter();
        int iPutNxtDetailsToDB = 0;
        Calendar cal = Calendar.getInstance();
        cal.setTime(endDate);
        int year = cal.get(Calendar.YEAR);
        System.out.println(year);
        int month = cal.get(Calendar.MONTH) + 1;
        DateFormat df3 = new SimpleDateFormat("MM");
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String sMonth = df3.format(startDate);
        System.out.println(sMonth);
        Iterator<Row> rowIterator = sheet.iterator();
        List<MakerCapitalTransaction> listMakerCapitalsDOs = new ArrayList<MakerCapitalTransaction>();
        MasterLogBean masterLog= new MasterLogBean();
        MakerCapitalTransaction makerCapitals = new MakerCapitalTransaction();
        Object dateObj = null;
        int iTotRowInserted = 0;
        boolean result = false;
        boolean sResult = false;
        String investMode = "";
        MasterLogBean masterLogs=new MasterLogBean();
        int i=0;
        while (rowIterator.hasNext()) {
            masterLog= new MasterLogBean();
            makerCapitals = new MakerCapitalTransaction();
            Row row = rowIterator.next();
            if(i>=1){
                Iterator<Cell> cellIterator = row.cellIterator();
                String codeScheme = "";
                String sCode = "";
                Integer clientCode = null;
                PMSClientMaster pmsClientMaster = null;
                InvestmentMaster investmentMaster = null;
                String strategyName="";
                Date dTransDate= new Date();
                String feeType="";
                Double credit=0.0;
                Double debit=0.0;
                Date transDate = null;

                int cellCount=0;
                while (cellIterator.hasNext()) {
                    Cell cell = cellIterator.next();
                    System.out.println(cell.getColumnIndex()+"----"+cell.getCellTypeEnum());
                    //String cellValue = dataFormatter.formatCellValue(cell);
                    codeScheme=row.getCell(2).getStringCellValue();
                    String sSplits[] = codeScheme.split("(?<=\\D)(?=\\d)|(?<=\\d)(?=\\D)");
                    sCode = sSplits[0];
                    clientCode = new Double(sCode).intValue();
                    pmsClientMaster =pmsClientMasterRepository.findByClientCode(clientCode.toString());
                    if(pmsClientMaster==null)
                        result = true;
                    transDate= dateFormat.parse(row.getCell(4).getStringCellValue());
                    feeType=row.getCell(6).getStringCellValue();
                    credit=row.getCell(7).getNumericCellValue();
                    debit = row.getCell(8).getNumericCellValue();
                    strategyName=row.getCell(13).getStringCellValue();
                    investmentMaster = investmentMasterRepository.findByInvestmentName(strategyName);
                    if(investmentMaster==null)
                        result = true;
                    cellCount++;
                    break;
                }
                if(result){
                    if (feeType.contains("Cash Deposits") || feeType.contains("Security in") || feeType.contains("Cash Withdrawal")
                        || feeType.contains("Security out")){
                        if(pmsClientMaster==null){
                            masterLog.setClientCode(clientCode.toString());
                            masterLog.settStatus("Client Not Found");
                        }
                        String finalStrategyName = strategyName;
                        List<MasterLogBean> bank = masterLogBean.stream().filter((x -> (x.getStrategyName().equals(finalStrategyName)))).collect(Collectors.toList());

                        if(investmentMaster==null && bank.size()==0){
                            masterLog.setStrategyName(strategyName);
                            masterLog.setStrategyStatus("Stragey Not found");
                        }
                        if(pmsClientMaster==null || (investmentMaster==null && bank.size()==0))
                            masterLogBean.add(masterLog);
                        setEnableSavePMSNav(false);
                    }
                    result=false;
                }
                else {
                    System.out.println(pmsClientMaster);
                    makerCapitals.setPmsClientMaster(pmsClientMaster);
                    makerCapitals.setClientCodeScheme(codeScheme);
                    makerCapitals.setTransDate(transDate);
                    makerCapitals.setInvestmentMaster(investmentMaster);
                    makerCapitals.setTransDesc(feeType);
                    makerCapitals.setCreditAmount(credit);
                    makerCapitals.setDebitAmount(debit);
                    if (makerCapitals.getTransDesc().contains("Cash Deposits") || makerCapitals.getTransDesc().contains("Security in") ||
                        makerCapitals.getTransDesc().contains("Cash Withdrawal") || makerCapitals.getTransDesc().contains("Security out"))
                        listMakerCapitalsDOs.add(makerCapitals);
                }
            }
            i++;

        }



        return listMakerCapitalsDOs;

    }

    private FileUploadFeePMS readNewPmsNavFile(String path, Date startDate, Date endDate) throws IOException, InvalidFormatException, ParseException {

        List<CapitalTransaction> capitalTransactions=capitalTransactionRepository.findByStartDateAndEndDateAndIntDeleted(startDate,endDate,0);

        FileInputStream fis = new FileInputStream(path);
        Workbook workbook = WorkbookFactory.create(fis);

        Sheet sheet = workbook.getSheetAt(0);
        DataFormatter dataFormatter = new DataFormatter();
        int iPutNxtDetailsToDB = 0;
        Calendar cal = Calendar.getInstance();
        cal.setTime(endDate);
        int year = cal.get(Calendar.YEAR);
        System.out.println(year);
        int month = cal.get(Calendar.MONTH) + 1;
        DateFormat df3 = new SimpleDateFormat("MM");
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String sMonth = df3.format(startDate);
        System.out.println(sMonth);
        Iterator<Row> rowIterator = sheet.iterator();
        FileUploadFeePMS fileUploadFeePMS = new FileUploadFeePMS();
        MasterLogBean masterLog= new MasterLogBean();
        //MakerPMSNav makerPMSNav = null;
        BCADMakerPMSNav bcadMakerPMSNav = null;
        MakerProfitShare makerProfitShare = null;
        BCADMakerProfitShare bcadMakerProfitShare = null;
        List<MakerPMSNav> listMakerPmsNavDOs = new ArrayList<MakerPMSNav>();
        List<BCADMakerPMSNav> listBCADMakerPMSNavDOs = new ArrayList<BCADMakerPMSNav>();
        List<MakerProfitShare> listMakerProfitShare = new ArrayList<MakerProfitShare>();
        List<BCADMakerProfitShare> listBCADMakerProfitShare = new ArrayList<BCADMakerProfitShare>();
        //MasterLogBean masterLog= new MasterLogBean();
        MakerPMSNav makerPMSNav = new MakerPMSNav();
        Object dateObj = null;
        int iTotRowInserted = 0;
        boolean result = false;
        boolean pmsClientResult= false,clientManagementBcadResult= false;
        boolean sResult = false;
        String investMode = "";
        MasterLogBean masterLogs=new MasterLogBean();
        int i=0;
        while (rowIterator.hasNext()) {
            masterLog= new MasterLogBean();
            makerPMSNav = new MakerPMSNav();
            Row row = rowIterator.next();
            if(i>=1){
            Iterator<Cell> cellIterator = row.cellIterator();
            String codeScheme = "";
            String sCode = "";
            Integer clientCode = null;
            PMSClientMaster pmsClientMaster = null;
            InvestmentMaster investmentMaster = null;
            ClientManagement clientManagement = null;
            String strategyName="";
            Date dTransDate= new Date();
            String feeType="";
            Double pmsNav=0.0;

            int cellCount=0;
            while (cellIterator.hasNext()) {
                Cell cell = cellIterator.next();
                System.out.println(cell.getColumnIndex()+"----"+row.getCell(2)+"==="+row.getCell(2).getCellTypeEnum());

                codeScheme=row.getCell(2).getStringCellValue();
//                String sSplits[] = codeScheme.split("(?<=\\D)(?=\\d)|(?<=\\d)(?=\\D)");
//                sCode = sSplits[0];
//                clientCode = new Double(sCode).intValue();
                pmsClientMaster = pmsClientMasterRepository.findByClientCode(codeScheme);
                Product productItem = productRepository.findByProductName("BCAD");
                List<CommissionDefinition> option3Check = new ArrayList<>();
                if(strategyName.equalsIgnoreCase("BCAD")) {
                    option3Check=  commissionDefinitionRepository.findOption3Commission(
                        clientManagement.getDistributorMaster().getId(), productItem.getId());
                }
//                pmsClientMaster =pmsClientMasterRepository.findByClientCode(clientCode.toString());
//                if(pmsClientMaster==null)
//                    result = true;

                if(pmsClientMaster==null && (option3Check.isEmpty() && option3Check.size()==0)) {
                	strategyName=row.getCell(3).getStringCellValue();
                	if(strategyName.equalsIgnoreCase("BCAD")) {
                		clientManagement = clientManagementRepository.findByClientCode(codeScheme);
                        System.out.println(codeScheme);
                		if(clientManagement !=null) {
                		/*	List<CommissionDefinition> option3Check = commissionDefinitionRepository.findOption3Commission(
                					clientManagement.getDistributorMaster().getId(), productItem.getId());
                		*/	if(option3Check.isEmpty() && option3Check.size()==0) {
                				clientManagementBcadResult =true;
                			}
                			else {
                				pmsClientResult = true;
                			}
                		}
                		else {
                			result = true;

                		}
                	}
                }
                else {
                	pmsClientResult = true;
                }
              strategyName=row.getCell(3).getStringCellValue();
              investmentMaster = investmentMasterRepository.findByInvestmentName(strategyName);
              if(investmentMaster==null)
                  result = true;
                dTransDate = row.getCell(8).getDateCellValue();
                feeType = row.getCell(9).getStringCellValue();
                pmsNav = row.getCell(11).getNumericCellValue();
                cellCount++;
                break;
            }
            if(result){
                if (feeType.contains("Management") || feeType.contains("Performance")){
                if((pmsClientMaster==null) && (clientManagement==null)){
                    masterLog.setClientCode(codeScheme);
                    masterLog.settStatus("Client Not Found");
                }
                    String finalStrategyName = strategyName;
                    List<MasterLogBean> bank = new ArrayList<>();
                try {
                    bank = masterLogBean.stream().filter((x -> (x.getStrategyName().equals(finalStrategyName)))).collect(Collectors.toList());
                }
                catch(Exception e){
                    System.out.println(e);
                    bank = new ArrayList<>();
                }
                    if(investmentMaster==null && bank.size()==0){
                    masterLog.setStrategyName(strategyName);
                    masterLog.setStrategyStatus("Stragey Not found");
                }
                    if(pmsClientMaster==null || (investmentMaster==null && bank.size()==0))
                masterLogBean.add(masterLog);
                setEnableSavePMSNav(false);
                }
                result=false;
            }
            else {
            	if(pmsClientResult) {
            		System.out.println(pmsClientMaster);
            		if (feeType.contains("Management")){
            			makerPMSNav = new MakerPMSNav();
                		makerPMSNav.setPmsClientMaster(pmsClientMaster);
                		makerPMSNav.setStrategyName(strategyName);
                		makerPMSNav.setCodeScheme(codeScheme);
                		makerPMSNav.setCalcPmsNav(pmsNav.floatValue());
                		makerPMSNav.setInvestmentMaster(investmentMaster);
                		makerPMSNav.setSelectedStartDate(dTransDate);
                		makerPMSNav.setFeeType(feeType);

//            			String finalCodeScheme = codeScheme;
//            			PMSClientMaster finalPmsClientMaster = pmsClientMaster;
//            			InvestmentMaster finalInvestmentMaster = investmentMaster;
////            			List<CapitalTransaction> capitalTransaction = capitalTransactions.stream().filter((x -> (x.getClientCodeScheme().equals(finalCodeScheme) &&
////            					x.getPmsClientMaster().equals(finalPmsClientMaster) && (x.getTransDesc().equals("Cash Deposits") || x.getTransDesc().equals("Security in")))
////            					)).collect(Collectors.toList());
//            			System.out.println(capitalTransaction.toString());
//            			Date pmsInvestment=null;
//            			if(capitalTransaction.size()>0){
//            				makerPMSNav.setInvestmentMaster(capitalTransaction.get(0).getInvestmentMaster());
//            				System.out.println(codeScheme);
//            				pmsInvestment=pmsNavRepository.getPMSNavInvestmentInitialDate(capitalTransaction.get(0).getClientCodeScheme(),startDate);
//            				if(pmsInvestment==null){
//            					System.out.println(investmentMaster.getInvestmentName()+pmsClientMaster.getClientCode());
//            					//makerPMSNav.setInvestmentMaster(capitalTransaction.get(0).getInvestmentMaster());
//            					makerPMSNav.setInvestmentDate(capitalTransaction.get(0).getTransDate());}
//            				else{
//            					makerPMSNav.setInvestmentDate(pmsInvestment);
//            				}}
//            			else{
//            				pmsInvestment=pmsNavRepository.getPMSNavInvestmentInitialDate(codeScheme,startDate);
//            				if(pmsInvestment==null)
//            					pmsInvestment=pmsNavRepository.getPMSNavInvestmentOldDate(codeScheme,startDate);
//            				if(pmsInvestment!=null){
//            					/*investmentMaster=investmentMasterRepository.findByInvestmentName(strategyName);
//                            makerPMSNav.setInvestmentMaster(investmentMaster);*/
//            					makerPMSNav.setInvestmentDate(pmsInvestment);}
//
//            			}

            			makerPMSNav.setStrategyName(strategyName);
            			listMakerPmsNavDOs.add(makerPMSNav);
            			fileUploadFeePMS.setListMakerPmsNavDOs(listMakerPmsNavDOs);
            			}
            		else if(feeType.contains("Performance")){
            				makerProfitShare = new MakerProfitShare();
            				makerProfitShare.setPmsClientMaster(pmsClientMaster);
            				makerProfitShare.setInvestmentMaster(investmentMaster);
            				makerProfitShare.setProfitShareIncome(pmsNav.floatValue());
            				makerProfitShare.setReceiptDate(dTransDate);
            				makerProfitShare.setIsDeleted(0);
            				makerProfitShare.setStartDate(startDate);
            				makerProfitShare.setEndDate(endDate);
            				listMakerProfitShare.add(makerProfitShare);
            				fileUploadFeePMS.setListMakerProfitShare(listMakerProfitShare);
            			}
            		pmsClientResult = false;
            	}
            	else if(clientManagementBcadResult) {
            		if (feeType.contains("Management")){
            			bcadMakerPMSNav = new BCADMakerPMSNav();
            			bcadMakerPMSNav.setClientManagement(clientManagement);
                        bcadMakerPMSNav.setCodeScheme(codeScheme);
                        bcadMakerPMSNav.setCalcPmsNav(pmsNav.floatValue());
                        bcadMakerPMSNav.setSelectedStartDate(dTransDate);
                        bcadMakerPMSNav.setInvestmentMaster(investmentMaster);
                        bcadMakerPMSNav.setStartDate(startDate);
                        bcadMakerPMSNav.setEndDate(endDate);
                        bcadMakerPMSNav.setIsDeleted(0);
                        listBCADMakerPMSNavDOs.add(bcadMakerPMSNav);
                        fileUploadFeePMS.setListBCADMakerPMSNavDOs(listBCADMakerPMSNavDOs);
            		}
            		else if(feeType.contains("Performance")){
            		  bcadMakerProfitShare = new BCADMakerProfitShare();
            		  bcadMakerProfitShare.setClientManagement(clientManagement);
                      bcadMakerProfitShare.setProfitShareIncome(pmsNav.floatValue());
                      bcadMakerProfitShare.setReceiptDate(dTransDate);
//                    bcadMakerProfitShare.setFileUpload(uploadMasterFiles1);
                      bcadMakerProfitShare.setStartDate(startDate);
                      bcadMakerProfitShare.setEndDate(endDate);
                      bcadMakerProfitShare.setIsDeleted(0);
                      listBCADMakerProfitShare.add(bcadMakerProfitShare);
                      fileUploadFeePMS.setListBCADMakerProfitShare(listBCADMakerProfitShare);
            		}
            		clientManagementBcadResult =false;
            	}
            }
            }
            i++;

        }



        return fileUploadFeePMS;
    }

    private List<MakerPMSNav> readPmsNavFile(String path, Date startDate, Date endDate)throws IOException {

        List<CapitalTransaction> capitalTransactions=capitalTransactionRepository.findByStartDateAndEndDateAndIntDeleted(startDate,endDate,0);

        Integer sOut = 0;

        log.info("In DataUploadEditForm readPmsNavFile() method");
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
        List<MakerPMSNav> listMakerPmsNavDOs = new ArrayList<MakerPMSNav>();
        MakerPMSNav makerPMSNav = new MakerPMSNav();
        Object dateObj = null;
        int iTotRowInserted = 0;
        boolean result = false;
        boolean sResult = false;
        String investMode = "";
        MasterLogBean masterLogs=new MasterLogBean();
        while (rowIterator.hasNext()) {
            masterLogs=new MasterLogBean();
            makerPMSNav = new MakerPMSNav();
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
            PMSClientMaster pmsClientMaster = null;
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
                                            pmsClientMaster =pmsClientMasterRepository.findByClientCode(clientCode.toString());

                                             if(pmsClientMaster==null)
                                                 result = true;
                                        } else {
                                            sBreak = 1;
                                            break;

                                        }
                                        if ((sBreak == 0) && (!codeScheme.contains(prop.getString("pms.noenter")))) {
                                            // sScheme =
                                            // row.getCell(2).getStringCellValue();
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
                                       // UnifiJsfUtils.addErrorMessage("Client Not found " + clientCode);
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
                            if (result) {

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
                    if ((nDays != 0)) {
                        String sDate = "/" + month + "/" + year;
                        String tDate="/" + sMonth + "/" + year;
                        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                        String dStart = dateFormat.format(dStartDate);
                        String dEnd = dateFormat.format(dEndDate);
                        // String dStart=dStartDate.toString();
                        // String dEnd=dEndDate.toString();
                        if ((nDays != 0) && ((dStart.contains(sDate) && dEnd.contains(sDate))||((dStart.contains(tDate) && dEnd.contains(tDate))))) {


                            makerPMSNav.setPmsClientMaster(pmsClientMaster);
                            makerPMSNav.setCodeScheme(codeScheme);
                            makerPMSNav.setStartDate(dStartDate);
                            makerPMSNav.setEndDate(dEndDate);
                            makerPMSNav.setNoDays(nDays);
                            makerPMSNav.setPercentageComm(perComm);
                            makerPMSNav.setGrossPmsNav(grossPms);
                            makerPMSNav.setMarginValue(pMargin);
                            makerPMSNav.setNetPmsNav(netPms);
                            makerPMSNav.setCalcPmsNav(calcPms);
                            makerPMSNav.setInvestMode(investMode);
                            makerPMSNav.setNoMonth(month);
                            makerPMSNav.setNoYear(year);
                            System.out.println("Start Date--"+startDate);
                            makerPMSNav.setSelectedStartDate(startDate);
                            System.out.println(strategyName);
                            InvestmentMaster investmentMaster=investmentMasterRepository.findByInvestmentName(strategyName);
                            PMSClientMaster finalPmsClientMaster = pmsClientMaster;
                            String finalCodeScheme = codeScheme;
                            List<CapitalTransaction> capitalTransaction = capitalTransactions.stream().filter((x -> (x.getClientCodeScheme().equals(finalCodeScheme) &&
                                x.getPmsClientMaster().equals(finalPmsClientMaster) && (x.getTransDesc().equals("Cash Deposits") || x.getTransDesc().equals("Security in")))
                            )).collect(Collectors.toList());
                            System.out.println(capitalTransaction.toString());

                            if(capitalTransaction.size()>0){
                                makerPMSNav.setInvestmentMaster(capitalTransaction.get(0).getInvestmentMaster());
                                System.out.println(codeScheme);
                                Date pmsInvestment=pmsNavRepository.getPMSNavInvestment(codeScheme);
                                if(pmsInvestment==null){
                                System.out.println(investmentMaster.getInvestmentName()+pmsClientMaster.getClientCode());
                                makerPMSNav.setInvestmentDate(capitalTransaction.get(0).getTransDate());
                                makerPMSNav.setInvestmentMaster(capitalTransaction.get(0).getInvestmentMaster());}
                            else{
                                makerPMSNav.setInvestmentDate(pmsInvestment);
                                }}
                            else{
                              Date pmsInvestment=pmsNavRepository.getPMSNavInvestmentOldDate(codeScheme,startDate);
                                if(pmsInvestment!=null){
                                    if(strategyName.equals("AL"))
                                        investmentMaster = investmentMasterRepository.findByInvestmentName("Alternate/EAF");
                                    if(strategyName.equals("APJTR"))
                                        investmentMaster = investmentMasterRepository.findByInvestmentName("APJ TRANS");
                                    if(strategyName.equals("BLN"))
                                        investmentMaster = investmentMasterRepository.findByInvestmentName("BLENDED- RANGOLI");
                                    if(strategyName.equals("BLN2"))
                                        investmentMaster = investmentMasterRepository.findByInvestmentName("BLENDED2");
                                    if(strategyName.equals("BLN3"))
                                        investmentMaster = investmentMasterRepository.findByInvestmentName("BLENDED3");
                                    if(strategyName.equals("EAF"))
                                        investmentMaster = investmentMasterRepository.findByInvestmentName("Event Arbitrage");
                                    if(strategyName.equals("EQS"))
                                        investmentMaster = investmentMasterRepository.findByInvestmentName("Equal Weight Large Cap");
                                    if(strategyName.equals("FF"))
                                        investmentMaster = investmentMasterRepository.findByInvestmentName("Fund of Funds");
                                    if(strategyName.equals("GRN"))
                                        investmentMaster = investmentMasterRepository.findByInvestmentName("GREEN FUND");
                                    if(strategyName.equals("GRN2"))
                                        investmentMaster = investmentMasterRepository.findByInvestmentName("GREEN FUND 2");
                                    if(strategyName.equals("HC"))
                                        investmentMaster = investmentMasterRepository.findByInvestmentName("Holdco");
                                    if(strategyName.equals("HC2"))
                                        investmentMaster = investmentMasterRepository.findByInvestmentName("HOLDCO2");
                                    if(strategyName.equals("IS"))
                                        investmentMaster = investmentMasterRepository.findByInvestmentName("Insider Shadow");
                                    if(strategyName.equals("ISN"))
                                        investmentMaster = investmentMasterRepository.findByInvestmentName("INSIDER SHADOW NEW");
                                    if(strategyName.equals("OT"))
                                        investmentMaster = investmentMasterRepository.findByInvestmentName("Others");
                                    if(strategyName.equals("OT1"))
                                        investmentMaster = investmentMasterRepository.findByInvestmentName("Others1");
                                    if(strategyName.equals("RR"))
                                        investmentMaster = investmentMasterRepository.findByInvestmentName("Realty & Related");
                                    if(strategyName.equals("S0"))
                                        investmentMaster = investmentMasterRepository.findByInvestmentName("SpinOff");
                                    if(strategyName.equals("SON"))
                                        investmentMaster = investmentMasterRepository.findByInvestmentName("SPINOFF NEW");
                                    if(strategyName.equals("SON2"))
                                        investmentMaster = investmentMasterRepository.findByInvestmentName("SPINOFF NEW2");
                                    if(strategyName.equals("SP"))
                                        investmentMaster = investmentMasterRepository.findByInvestmentName("Special Opportunities");
                                    if(strategyName.equals("TR"))
                                        investmentMaster = investmentMasterRepository.findByInvestmentName("Trend");

                                    makerPMSNav.setInvestmentMaster(investmentMaster);
                                    makerPMSNav.setInvestmentDate(pmsInvestment);}
                            }
                            makerPMSNav.setStrategyName(strategyName);

                            listMakerPmsNavDOs.add(makerPMSNav);
                        }

                        else {
                            statusCode="410";
                           // UnifiJsfUtils.addErrorMessage("File is not containing the same month");
                            listMakerPmsNavDOs = new ArrayList<MakerPMSNav>();
                            break;
                        }

                    }
                }
            }
        }



        return listMakerPmsNavDOs;

    }

    private List<MakerProfitShare> readProfitShareFile(String path, Date startDate, Date endDate)throws Exception {

        log.info("In DataUploadEditForm readProfitShareFile() method");
        FileInputStream inputStream = new FileInputStream(path);
        Workbook workbook = new XSSFWorkbook(inputStream);
        Sheet firstSheet = workbook.getSheetAt(0);
        Iterator<Row> iterator = firstSheet.iterator();

        List<MakerProfitShare> listMakerProfitShares = new ArrayList<MakerProfitShare>();
        // OnboardingInitiationDO onboardingInitiation = null;
        MakerProfitShare makerProfitShare = null;
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
            makerProfitShare = new MakerProfitShare();
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
                            try {
                                inputStr = getCellValue(nextCell).toString().trim();
                                clientCode = new Double(inputStr).intValue();
                                PMSClientMaster pmsClientMaster = pmsClientMasterRepository.findByClientCode(clientCode.toString());
                                if(pmsClientMaster==null)
                                {
                                    result = true;
                                    break row;
                                }
                                else
                                {
                                makerProfitShare.setPmsClientMaster(pmsClientMaster);
                                break;}
                            } catch (NoResultException exception) {
                                log.error("Exception at DataUploadEditForm Profit share Client not found" + clientCode);
                                //UnifiJsfUtils.addErrorMessage("Client Not found " + clientCode);
                                result = true;
                                break row;

                            }
                        case 2:
                            try {
                                inputStr = getCellValue(nextCell).toString().trim();
                                InvestmentMaster investmentMaster = investmentMasterRepository.findByInvestmentName(inputStr);
                                if(investmentMaster==null){
                                    result = true;
                                    break row;
                                }
                                else{
                                System.out.println("inves " + investmentMaster);
                                makerProfitShare.setInvestmentMaster(investmentMaster);
                                makerProfitShare.setProductName(inputStr);
                                break;}
                            } catch (NoResultException exception) {
                                log.error("Exception at DataUploadEditForm InvestmentNot Found" + inputStr);
                              //  UnifiJsfUtils.addErrorMessage("Investment Not found " + inputStr);
                                result = true;
                                break row;

                            }

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
            listMakerProfitShares = new ArrayList<MakerProfitShare>();
        }
        workbook.close();
        inputStream.close();
        return listMakerProfitShares;


    }

    private List<MakerBrokerage> readBrokerageFile(String sFile, Date startDate, Date endDate) throws IOException, URISyntaxException {

        // Excel to database
        int iPhysNumOfCells;
        FileInputStream fis = new FileInputStream(sFile);
        XSSFWorkbook workbook = new XSSFWorkbook(fis);

        XSSFSheet sheet = workbook.getSheetAt(0);
        int iPutNxtDetailsToDB = 0;

        Iterator<Row> rowIterator = sheet.iterator();

        List<MakerBrokerage> listMakerBrokerageDOs = new ArrayList<MakerBrokerage>();
        MakerBrokerage makerBrokerage = null;
        int iTotRowInserted = 0;
        boolean result = false;

        while (rowIterator.hasNext()) {
            makerBrokerage = new MakerBrokerage();
            Row row = rowIterator.next();
            Row row2 = row;
            iTotRowInserted++;
            Iterator<Cell> cellIterator = row.cellIterator();

            int iPos = 0;
            int iTotConToMeet = 1;
            int iTotConMet = 0;
            int iConCheckNow = 0;
            String sConCheck = "";
            iPhysNumOfCells = row.getPhysicalNumberOfCells();

            // System.out.println("Blank"+iPhysNumOfCells);
            while (cellIterator.hasNext() && iPutNxtDetailsToDB == 0) {
                Cell cell = cellIterator.next();

                if (iPutNxtDetailsToDB == 0) {
                    if (iPos == 0)
                        sConCheck = "Party";
                    /*
                     * if (iPos == 1) sConCheck = "Square Brokerage"; if (iPos
                     * == 2) sConCheck = "Square Turnover In Rs."; if (iPos ==
                     * 3) sConCheck = "Transaction Description"; if (iPos == 4)
                     * sConCheck = "Running Balance";
                     */

                    if (cell.getStringCellValue().trim().equals(sConCheck))
                        iTotConMet++;

                    iPos++;
                    if (iTotConToMeet == iTotConMet) {
                        iPutNxtDetailsToDB = 1;
                        iConCheckNow = 1;
                    }
                }
            }
            String sCode = "";
            Integer bCode = 0;
            float bAmount = 0.0f;
            Integer clientCode = null;

            if (iPhysNumOfCells != 0) {

                if (iPutNxtDetailsToDB == 1 && iConCheckNow == 0) {

                    switch (row.getCell(0).getCellTypeEnum()) {

                        case STRING:
                            try {
                                sCode = row.getCell(0).getStringCellValue();
                                if (sCode.contains(prop.getString("file.fee.symbol"))) {
                                    String sSplits[] = sCode.split("\\[");
                                    sCode = sSplits[1].replaceAll("]", "");
                                    clientCode = new Double(sCode).intValue();
                                    PMSClientMaster clientMaster = pmsClientMasterRepository.findByClientCode(clientCode.toString());
                                    if(clientMaster==null){
                                        log.error("Exception at DataUploadEditForm Brokerage client not found" + clientCode);
                                        result = true;
                                        throw new ClientNotFoundException(clientCode);
                                   }
                                    else
                                    makerBrokerage.setPmsClientMaster(clientMaster);
                                }
                                System.out.println(clientCode);
                                break;
                            } catch (Exception exception) {
                                log.error("Exception at DataUploadEditForm Brokerage client not found" + clientCode);
                                result = true;
                                break;
                            }
                        default:

                    }

                    while (cellIterator.hasNext()) {
                        Cell cell = cellIterator.next();
                        cell.setCellType(CellType.STRING);
                    }
                    // System.out.println(dDate);
                    if (!sCode.contains(prop.getString("file.brokerage.nenter"))) {
                        try {

                            String dTranType = row.getCell(15).getStringCellValue();
                            bAmount = Float.parseFloat(dTranType);
                            makerBrokerage.setBrokerageAmount(bAmount);
                            makerBrokerage.setStartDate(startDate);
                            makerBrokerage.setEndDate(endDate);
                            // System.out.println(bAmount);
                            listMakerBrokerageDOs.add(makerBrokerage);

                        } catch (Exception e) {
                            //UnifiJsfUtils.addErrorMessage("Sorry,Please check the Input format of file Uploaded");
                            log.error("Exception at DataUploadEditForm Brokerage File Format" + e);
                            // result = true;
                            break;
                        }
                    } else {
                        iConCheckNow = 1;
                        break;
                    }
                }

            } else {
                break;
            }

        }
        if (result) {
            listMakerBrokerageDOs = new ArrayList<MakerBrokerage>();
        }
        return listMakerBrokerageDOs;


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

    FileOutputStream fos = null;
    HSSFWorkbook workBook = null;
    public void masterCreate(List<MasterLogBean> masterLogBean) throws FileNotFoundException{
        try{


            String sFilesDirectory = prop.getString("fee.file.folder") + "DFA Backup\\"
                + prop.getString("bulk.upload.not");
            File dirFiles = new File(sFilesDirectory);
            dirFiles.mkdirs();

            String file =sFilesDirectory+"/"+fileName;
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


            int iRowNo=sheet.getLastRowNum();
            HSSFRow headingBRSBookRow = sheet.createRow(iRowNo);
            headingBRSBookRow.createCell(0).setCellValue("Client Code");
            headingBRSBookRow.getCell(0).setCellStyle(cs);
            sheet.autoSizeColumn(0);
          /*  headingBRSBookRow.createCell(1).setCellValue("Client Name");
            headingBRSBookRow.getCell(1).setCellStyle(cs);
            sheet.autoSizeColumn(1);*/
            headingBRSBookRow.createCell(1).setCellValue("Status");
            headingBRSBookRow.getCell(1).setCellStyle(cs);
            sheet.autoSizeColumn(2);
            headingBRSBookRow.createCell(2).setCellValue("Strategy Name");
            headingBRSBookRow.getCell(2).setCellStyle(cs);
            sheet.autoSizeColumn(2);
            headingBRSBookRow.createCell(3).setCellValue("Strategy Status");
            headingBRSBookRow.getCell(3).setCellStyle(cs);
            sheet.autoSizeColumn(2);

            iRowNo=sheet.getLastRowNum()+2;

            Set<MasterLogBean> hs = new HashSet<>();
            hs.addAll(masterLogBean);
            masterLogBean.clear();
            masterLogBean.addAll(hs);

            for(MasterLogBean bean:masterLogBean){
                HSSFRow row = sheet.createRow(iRowNo);

                row.createCell(0).setCellValue(bean.getClientCode());
                sheet.autoSizeColumn(0);
                /*row.createCell(1).setCellValue(bean.getClientCode());
                sheet.autoSizeColumn(1);*/
                row.createCell(1).setCellValue(bean.gettStatus());
                sheet.autoSizeColumn(1);
                row.createCell(2).setCellValue(bean.getStrategyName());
                sheet.autoSizeColumn(2);
                row.createCell(3).setCellValue(bean.getStrategyStatus());
                sheet.autoSizeColumn(3);
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

    public Optional<FileUpload> approveFile(Optional<FileUpload> result) {

        if(result.get().getFileType().getFileType().equals("Brokerage")){

            try {
                Query query=entityManager.createNativeQuery("insert into fee_brokerage select * from maker_fee_brokerage mb where mb.fileuploaded_id="+result.get().getId());
                query.executeUpdate();

                query=entityManager.createNativeQuery("update file_upload f set f.upload_approved=1 where f.id="+result.get().getId());
                query.executeUpdate();
            }
            catch (PersistenceException cve) {
                cve.printStackTrace();
            }
            catch (Exception e) {
                e.printStackTrace();
            }}
        if(result.get().getFileType().getFileType().equals("Profit Share")){
            try {
                //String hql="insert into fee_pms_profit_share select * from maker_fee_pms_profit_share mps where mps.fileuploaded_id=?1";
                Query query=entityManager.createNativeQuery("insert into fee_pms_profit_share select * from maker_fee_pms_profit_share mps where mps.fileuploaded_id="+ result.get().getId());
                query.executeUpdate();

               query=entityManager.createNativeQuery("update file_upload f set f.upload_approved=1 where f.id="+result.get().getId());
               query.executeUpdate();

                MasterType masterType=masterTypeRepository.findByFileName(result.get().getFileType().getFileType());
                UploadMasterFiles uploadMasterFiles= uploadMasterFilesRepository.findByFileLocationAndIsDeletedAndMasterType(result.get().getFileLocation(),0,masterType);
                if(uploadMasterFiles!=null) {

                    query = entityManager.createNativeQuery("insert into bcad_profit_share select * from bcad_maker_profit_share mp where mp.fileuploaded_id=" + uploadMasterFiles.getId());
                    query.executeUpdate();

                    query = entityManager.createNativeQuery("update bcad_upload_master_files f set f.int_approved=1 where f.id=" + uploadMasterFiles.getId());
                    query.executeUpdate();
                }
            }
            catch (PersistenceException cve) {
                cve.printStackTrace();
            }
            catch (Exception e) {
                e.printStackTrace();
            }}
        if(result.get().getFileType().getFileType().equals("PMS NAV")){
            try {
                Query query=entityManager.createNativeQuery("insert into fee_pms_nav select * from maker_fee_pms_nav mp where mp.fileuploaded_id="+result.get().getId());
                query.executeUpdate();

                query=entityManager.createNativeQuery("update file_upload f set f.upload_approved=1 where f.id="+ result.get().getId());
                query.executeUpdate();

                MasterType masterType=masterTypeRepository.findByFileName(result.get().getFileType().getFileType());
                UploadMasterFiles uploadMasterFiles= uploadMasterFilesRepository.findByFileLocationAndIsDeletedAndMasterType(result.get().getFileLocation(),0,masterType);

                query=entityManager.createNativeQuery("insert into bcad_fee_pms_nav select * from bcad_maker_fee_pms_nav mp where mp.fileuploaded_id="+uploadMasterFiles.getId());
                query.executeUpdate();

                query=entityManager.createNativeQuery("update bcad_upload_master_files f set f.int_approved=1 where f.id="+ uploadMasterFiles.getId());
                query.executeUpdate();
            }
            catch (PersistenceException cve) {
                cve.printStackTrace();
            }
            catch (Exception e) {
                e.printStackTrace();
            }}
        if(result.get().getFileType().getFileType().equals("Capital Transaction")){

            try {
                Query query=entityManager.createNativeQuery("insert into capital_transactions select * from maker_capital_transactions mb where mb.file_upload_id="+result.get().getId());
                query.executeUpdate();

                query=entityManager.createNativeQuery("update file_upload f set f.upload_approved=1 where f.id="+result.get().getId());
                query.executeUpdate();
            }
            catch (PersistenceException cve) {
                cve.printStackTrace();
            }
            catch (Exception e) {
                e.printStackTrace();
            }}
            return result;

    }

    public Integer deleteFile(Long id) {
        log.info("In FileSearchForm delete() method");
        int result=0;
        int rProfit=0;
        int rDist=0;
        Date pDate=null;
        String loggedUser;
        Optional<FileUpload> file =fileUploadRepository.findById(id);
        if(file.get().getFileType().getFileType().equals("Profit Share"))
        {
            pDate=pmsService.onStartDate(file.get());
            result=pmsService.deleteProfitShare(file.get());
            file.get().setIsDeleted(1);
            fileUploadRepository.save(file.get());
            MasterType masterType=masterTypeRepository.findByFileName(file.get().getFileType().getFileType());
            UploadMasterFiles uploadMasterFiles= uploadMasterFilesRepository.findByFileLocationAndIsDeletedAndMasterType(file.get().getFileLocation(),0,masterType);
            bcadUploadService.deleteProfitFile(uploadMasterFiles);
            System.out.println("--------"+result);
        }
        if(file.get().getFileType().getFileType().equals("Brokerage"))        {

            pDate=pmsService.onBrokerageStartDate(file.get());
            System.out.println("Report raised flag"+"---------");
            result=pmsService.deleteBrokerage(file.get());
            file.get().setIsDeleted(1);
            fileUploadRepository.save(file.get());
            System.out.println("--------"+result);
        }
        if(file.get().getFileType().getFileType().equals("PMS NAV")){
            pDate=pmsService.onPMSStartDate(file.get());
            System.out.println("Report raised flag"+"---------");
            result=pmsService.deletePmsNav(file.get());
            file.get().setIsDeleted(1);
            fileUploadRepository.save(file.get());
            MasterType masterType=masterTypeRepository.findByFileName(file.get().getFileType().getFileType());
            UploadMasterFiles uploadMasterFiles= uploadMasterFilesRepository.findByFileLocationAndIsDeletedAndMasterType(file.get().getFileLocation(),0,masterType);
            bcadUploadService.deletePMSFile(uploadMasterFiles);
            System.out.println("--------"+result);

        }
       /* if(file.get().getFileType().getFileType().equals("Capital Transaction"))        {

            pDate=pmsService.onInvestmentStartDate(file.get());
            System.out.println("Report raised flag"+"---------");
            result=pmsService.deleteInvestments(file.get());
            file.get().setIsDeleted(1);
            fileUploadRepository.save(file.get());
            System.out.println("--------"+result);
        }*/
return result;

    }

    public void deletePMSFile(FileUpload fileUpload) {
        System.out.println(fileUpload + "idcheck");

        Query query=entityManager.createNativeQuery("update fee_pms_nav f set f.int_deleted=1 where f.fileuploaded_id="+fileUpload.getId());
        query.executeUpdate();
    }

    public void deleteBrokerageFile(FileUpload fileUpload) {

        Query query=entityManager.createNativeQuery("update fee_brokerage f set f.int_deleted=1 where f.fileuploaded_id="+fileUpload.getId());
        query.executeUpdate();
    }

    public void deleteProfitFile(FileUpload fileUpload) {
        Query query=entityManager.createNativeQuery("update fee_pms_profit_share f set f.int_deleted=1 where f.fileuploaded_id="+fileUpload.getId());
        query.executeUpdate();
    }

    public void deleteInvestment(FileUpload fileUpload) {
        Query query = entityManager.createNativeQuery("update capital_transactions p set p.int_deleted=1 where p.file_upload_id=" + fileUpload.getId());
        query.executeUpdate();
    }

    public int getPMSClients() {
        List<PMSNav> result = pmsNavRepository.findByUploadId();
        int counter = 0;
        List<ClientInvestmentDateDTO> clientList = new ArrayList();
        try {
            clientList = readExcelFile();
            for (int i = 0; i < result.size(); i++) {
                String code = result.get(i).getCodeScheme();
                for (int j = 0; j < clientList.size(); j++) {
                    if (clientList.get(j).getClientCode().equals(code)) {
                        result.get(i).setInvestmentDate(clientList.get(j).getInvestmentDate());
                         System.out.println("code : " + code + " : " + clientList.get(j).getClientCode() + " : " + clientList.get(j).getInvestmentDate());
                        pmsNavRepository.save(result.get(i));
                        counter++;
                    }
                }
            }
            System.out.println("Total records matched... " + counter);
            System.out.println("File rows count... " + clientList.size());
        } catch (Exception e) {
            System.out.println("File Not Found...." + e.getMessage());
        }

        return counter;
    }

    public List<ClientInvestmentDateDTO> readExcelFile() throws IOException {
        FileInputStream fis = new FileInputStream("/Users/indiumsoftware/Desktop/client-investment date.xlsx");
        XSSFWorkbook workbook = new XSSFWorkbook(fis);
        Sheet sheet = workbook.getSheetAt(0);
        Iterator<Row> rowIterator = sheet.iterator();
        boolean result = false;
        String inputStr1 = "";
        String inputStr3 ="";
        Date inputStr2 = null; Cell nextCell1 = null;
        Cell nextCell2,nextCell3 = null;

        List<ClientInvestmentDateDTO> client = new ArrayList();
        while (rowIterator.hasNext()) {
            Row row = rowIterator.next();
            Iterator<Cell> cellIterator = row.cellIterator();
            int iPhysNumOfCells = row.getPhysicalNumberOfCells();
            ClientInvestmentDateDTO cli = new ClientInvestmentDateDTO();
            while (cellIterator.hasNext()) {
                Cell cell = cellIterator.next();
                nextCell1 = row.getCell(0, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
                nextCell2 = row.getCell(1, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
                nextCell3 = row.getCell(2, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
                inputStr1 = getCellValue(nextCell1).toString().trim();
                inputStr3 = getCellValue(nextCell3).toString().trim();

                if(nextCell1.getCellType() == HSSFCell.CELL_TYPE_NUMERIC){

                    inputStr1 = String.valueOf(row.getCell(0).getNumericCellValue());
                    inputStr1= StringUtils.substring(inputStr1, 0, inputStr1.length() - 2);
                }
                if(nextCell1.getCellType() == HSSFCell.CELL_TYPE_STRING) {

                    String sSplits[] = inputStr1.split("(?<=\\D)(?=\\d)|(?<=\\d)(?=\\D)");
                    inputStr1 = sSplits[0];
                }

                if (nextCell2.getCellType() == HSSFCell.CELL_TYPE_NUMERIC) {
                    inputStr2 = row.getCell(1).getDateCellValue();
                }
            }
            cli.setClientCode(inputStr1);
            cli.setInvestmentDate(inputStr2);
            cli.setSchemeName(inputStr3);
            client.add(cli);
        }
        return client;
    }

    public int getBCADClients() {
        List<BCADPMSNav> result = bcadpmsNavRepository.getBCADDate();
        int counter = 0;
        List<ClientInvestmentDateDTO> clientList = new ArrayList();
        try {
            clientList = readExcelFile();
            for (BCADPMSNav bcadpmsNav : result) {
                ClientInvestmentDateDTO clientInvestmentDateDTO = clientList.stream().filter(x -> (x.getClientCode().equals(bcadpmsNav.getCodeScheme())))
                    .findAny().orElse(null);
                if(clientInvestmentDateDTO!=null){
                    bcadpmsNav.setInvestmentDate(clientInvestmentDateDTO.getInvestmentDate());
                    bcadpmsNavRepository.save(bcadpmsNav);
                }


            }
            System.out.println("Total records matched... " + counter);
            System.out.println("File rows count... " + clientList.size());
        } catch (Exception e) {
            System.out.println("File Not Found...." + e.getMessage());
        }

        return counter;
    }



    // Upload management fee
    public String uploadUmbrellaManagementFile( MultipartFile multipartFile,Long productId,Date startDate, Date endDate) throws IOException,Exception {

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String sStartTime = dateFormat.format(startDate);
        String sEndTime = dateFormat.format(endDate);
        List<AIF2ManagementFee> aif2ManagementFee=aif2ManagementFeeRepository.findAIF2Managements(sStartTime,sEndTime,productId);
        if(aif2ManagementFee !=null || aif2ManagementFee.size() !=0)
        {
            System.out.println("ibside null");
            return "409";
        }

        fileStream = IOUtils.toByteArray(multipartFile.getInputStream());
        int index = multipartFile.getOriginalFilename().length();
        System.out.println("File Details"+multipartFile.getOriginalFilename());
        setFileName(multipartFile.getOriginalFilename().substring(0, index ));

        String sFilesDirectory = prop.getString("fee.file.folder") + "DFA Backup\\"+
            prop.getString("bulk.upload.not");
        File dirFiles = new File(sFilesDirectory);
        dirFiles.mkdirs();

        File sFile = new File(dirFiles, fileName);

        writeFile(fileStream, sFile);
        System.out.println("File completed");
        String  list = readUmbrellaManagementFile(sFile.getPath(),productId,startDate,endDate);

        return list;
    }


    private String readUmbrellaManagementFile(String path, Long productId,Date startDate, Date endDate ) throws IOException, URISyntaxException {
        try {
            FileInputStream inputStream = new FileInputStream(path);
            Workbook workbook = new HSSFWorkbook(inputStream);
            Sheet mySheet = workbook.getSheetAt(0);

            Iterator<Row> rowIterator = mySheet.iterator();

            FileUpload fileUpload = null;
            int cellIdx = 0;
            int rowNu = 0;
            String distName = null;
            String rName = null;
            String dName = null;
            String code = null;
            String codeSplit = null;
            String codeSplit1 = null;
            while (rowIterator.hasNext()) {
                AIF2ManagementFee aif2ManagementFee = new AIF2ManagementFee();
                Row row = rowIterator.next();
                if (rowNu > 1) {
                    int schemeId = 0;
                    int rId = 0;
                    int distId = 0;
                    String monthYear =  row.getCell(0).getStringCellValue();
                    String[] splitMonth= monthYear.split("-");
                    System.out.println("month year"+splitMonth[0]);


                    String series =  row.getCell(0).getStringCellValue();
                    System.out.println("series"+series);

                    AIF2SeriesMaster aif2SeriesMaster=aif2SeriesMasterRepository.findByClassType(series);
                    aif2ManagementFee.setAif2SeriesMaster(aif2SeriesMaster);


                    Product product=productRepository.findById(productId).get();
//                    aif2ManagementFee.setMonthYear(splitMonth[0]);
                    aif2ManagementFee.setProduct(product);

                    Double name = row.getCell(1).getNumericCellValue();
                    aif2ManagementFee.setUnits(name.floatValue());
                    aif2ManagementFee.setMonthYear(startDate);

                    aif2ManagementFeeRepository.save(aif2ManagementFee);
                }
                rowNu++;

            }
            return "200";

        }
        catch (Exception exception) {

            log.error("Exception at DataUploadEditForm Brokerage File Format" + exception);
            return "450";

        }

    }

        private String uploadUmbrellaManagementFee(List<AIF2ManagementFee> aif2ManagementFee)
    {
        List<AIF2ManagementFee> result = aif2ManagementFeeRepository.saveAll(aif2ManagementFee);
        return null;


    }

}
