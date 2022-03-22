package com.bcad.application.service;

import com.bcad.application.config.PropertiesUtils;
import com.bcad.application.domain.*;
import com.bcad.application.repository.*;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.io.IOUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
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
public class AIFUploadService {

    @PersistenceContext
    EntityManager entityManager;

    private final InvestorProtfolioRepository investorProtfolioRepository;
    private final AIFClientMasterRepository aifClientMasterRepository;
    private final SeriesMasterRepository seriesMasterRepository;
    private final AIFUpdatedUnitsRepository aifUpdatedUnitsRepository;
    private final InvestorViewService investorViewService;
    private final FileUploadAIFRepository fileUploadAIFRepository;
    private final FileTypeRepository fileTypeRepository;
    private final MakerSeriesMonthRepository makerSeriesMonthRepository;
    private final MakerAIFManagePerMonthRepository makerAIFManagePerMonthRepository;


    public AIFUploadService(InvestorProtfolioRepository investorProtfolioRepository, AIFClientMasterRepository aifClientMasterRepository,
                            SeriesMasterRepository seriesMasterRepository, AIFUpdatedUnitsRepository aifUpdatedUnitsRepository,
                            InvestorViewService investorViewService, FileUploadAIFRepository fileUploadAIFRepository,
                            FileTypeRepository fileTypeRepository, MakerSeriesMonthRepository makerSeriesMonthRepository,
                            MakerAIFManagePerMonthRepository makerAIFManagePerMonthRepository) {
        this.investorProtfolioRepository = investorProtfolioRepository;
        this.aifClientMasterRepository = aifClientMasterRepository;
        this.seriesMasterRepository = seriesMasterRepository;
        this.aifUpdatedUnitsRepository = aifUpdatedUnitsRepository;
        this.investorViewService = investorViewService;
        this.fileUploadAIFRepository = fileUploadAIFRepository;
        this.fileTypeRepository = fileTypeRepository;
        this.makerSeriesMonthRepository = makerSeriesMonthRepository;
        this.makerAIFManagePerMonthRepository=makerAIFManagePerMonthRepository;
    }

    private byte[] fileStream;
    private String fileName;
    private List<InvestorProtfolio> investorProtfolios = new ArrayList<InvestorProtfolio>();
    private List<InvestorLogBean> investorLogBeans = new ArrayList<InvestorLogBean>();
    private List<MakerSeriesMasterMonth> seriesMasterMonths = new ArrayList<>();
    private boolean enableSave = true;
    private Boolean enableDownload = true;
    private List<String> fileDownload = new ArrayList<String>();
    private List<MakerAIFManagePerMonth> makerAIFManagePerMonths=new ArrayList<>();

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

    public List<InvestorProtfolio> getInvestorProtfolios() {
        return investorProtfolios;
    }

    public void setInvestorProtfolios(List<InvestorProtfolio> investorProtfolios) {
        this.investorProtfolios = investorProtfolios;
    }

    public List<InvestorLogBean> getInvestorLogBeans() {
        return investorLogBeans;
    }

    public void setInvestorLogBeans(List<InvestorLogBean> investorLogBeans) {
        this.investorLogBeans = investorLogBeans;
    }

    public boolean isEnableSave() {
        return enableSave;
    }

    public void setEnableSave(boolean enableSave) {
        this.enableSave = enableSave;
    }

    public Boolean getEnableDownload() {
        return enableDownload;
    }

    public void setEnableDownload(Boolean enableDownload) {
        this.enableDownload = enableDownload;
    }

    public List<String> getFileDownload() {
        return fileDownload;
    }

    public void setFileDownload(List<String> fileDownload) {
        this.fileDownload = fileDownload;
    }

    public List<MakerSeriesMasterMonth> getSeriesMasterMonths() {
        return seriesMasterMonths;
    }

    public void setSeriesMasterMonths(List<MakerSeriesMasterMonth> seriesMasterMonths) {
        this.seriesMasterMonths = seriesMasterMonths;
    }

    PropertiesConfiguration prop = new PropertiesUtils().getProperties("application.properties");
    private final Logger log = LoggerFactory.getLogger(AIFUploadService.class);

    public FileUploadAIF uploadAIFFile(Date startDate, Date endDate, String fileType, MultipartFile multipartFile) throws IOException, Exception{
    DateFormat dateFormat = new SimpleDateFormat("MMM yyyy");
    String sStartTime = dateFormat.format(startDate);
    String sEndTime = dateFormat.format(endDate);
    String sFinal = sStartTime + "_to_" + sEndTime;

    String sDate = " " + sStartTime;
    String sPath="";

    int index = multipartFile.getOriginalFilename().lastIndexOf(".");
    setFileName(multipartFile.getOriginalFilename().substring(0, index ));

    fileStream = IOUtils.toByteArray(multipartFile.getInputStream());

    List<InvestorProtfolio> investorValidate = new ArrayList<InvestorProtfolio>();
    List<AIFManagePerMonth> managementValidate;
    FileUploadAIF fileUploadAIF = new FileUploadAIF();
    enableSave=true;
    seriesMasterMonths = new ArrayList<>();
    makerAIFManagePerMonths = new ArrayList<>();
    investorProtfolios = new ArrayList<InvestorProtfolio>();
    investorLogBeans = new ArrayList<InvestorLogBean>();
    seriesMasterMonths = new ArrayList<>();
    enableDownload = true;
    fileDownload = new ArrayList<String>();
    makerAIFManagePerMonths=new ArrayList<>();

        if (fileType.contains(prop.getString("file.series.month"))) {
        String sFilesDirectory = prop.getString("fee.file.folder") + "DFA Backup\\\\"
            + prop.getString("file.series.type") + "\\\\" + sFinal;

        File dirFiles = new File(sFilesDirectory);
        dirFiles.mkdirs();
        File sFile = new File(dirFiles, multipartFile.getOriginalFilename());
        sPath=sFile.toString();


        fileUploadAIF = fileUploadAIFRepository.findByFileLocationAndIsDeleted(sFile.getPath().toString(), 0);

        if (fileUploadAIF == null) {
            /*fileUploadAifDO=saveUploadSeriesFileName(sFile);*/

            writeFile(fileStream, sFile);
            System.out.println("File completed");

            int iPhysNumOfCells;
            FileInputStream fis = new FileInputStream(sFile.getPath());
            XSSFWorkbook workbook = new XSSFWorkbook(fis);

            XSSFSheet sheet = workbook.getSheetAt(0);
            int iPutNxtDetailsToDB = 0;
            int iTotRowInserted = 0;
            Iterator<Row> rowIterator = sheet.iterator();
            MakerSeriesMasterMonth seriesMasterMonth = new MakerSeriesMasterMonth();
            while (rowIterator.hasNext()) {
                seriesMasterMonth = new MakerSeriesMasterMonth();
                Row row = rowIterator.next();
                Row row2 = row;
                iTotRowInserted++;
                Iterator<Cell> cellIterator = row.cellIterator();

                int iPos = 0;
                int iTotConToMeet = 2;

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
                            sConCheck = "Particulars";
                        if (iPos == 1)
                            sConCheck = "Post-Tax NAV";

                        if (cell.getStringCellValue().trim().equals(sConCheck))
                            iTotConMet++;

                        iPos++;

                        if (iTotConToMeet == iTotConMet) {
                            iPutNxtDetailsToDB = 1;
                            iConCheckNow = 1;
                        }
                    }
                }

                String dCode = "";
                Float navValue = 0.0f;
                String sValue = "";
                SeriesMaster seriesMaster = null;

                if (iPhysNumOfCells == 2) {

                    if (iPutNxtDetailsToDB == 1 && iConCheckNow == 0) {

                        while (cellIterator.hasNext()) {
                            Cell cell = cellIterator.next();
                            // System.out.println("Cell
                            // Type->"+cell.getCellType());
                            switch (cell.getCellTypeEnum()) {
                                case STRING:
                                    try {
                                        dCode = row.getCell(0).getStringCellValue();
                                        break;
                                    } catch (NoResultException exception) {

                                    }

                                default:
                                    navValue = (float) row.getCell(1).getNumericCellValue();
                            }
                        }
                        try {
                            seriesMaster = seriesMasterRepository.findBySeriesCode(dCode.toString());
                            if (seriesMaster != null) {
                                // navValue=Float.parseFloat(sValue);
                                seriesMasterMonth.setSeriesMaster(seriesMaster);
                                seriesMasterMonth.setNavValue(navValue);
                                seriesMasterMonth.setIsDeleted(0);
                                //seriesMasterMonthDO.setFileUploadAifDO(fileUploadAifDO);
                                seriesMasterMonth.setMonthYear(sStartTime);
                                seriesMasterMonths.add(seriesMasterMonth);
                            } else {
                                log.error("Series not found");
                                //UnifiJsfUtils.addErrorMessage("Series Master already existed ");
                                fileUploadAIF= new FileUploadAIF();
                                fileUploadAIF.setCode("410");
                                System.out.println(fileUploadAIF);
                                fileUploadAIF.setStatus("Series Not found"+dCode);
                                setEnableSave(false);
                                return fileUploadAIF;

                            }

                        } catch (Exception exception) {
                            System.out.println(exception);
                            log.error("Series Not found " + dCode);
                            //UnifiJsfUtils.addErrorMessage("Series Not found " + dCode);
                            setEnableSave(false);
                        }

                    }
                }

                //log.info("Successfully uploaded");

            }
            saveClient(sFile, startDate, endDate,multipartFile.getOriginalFilename(),investorProtfolios);
        } else {
            log.error("Series Master already existed");
            //UnifiJsfUtils.addErrorMessage("Series Master already existed ");
            fileUploadAIF.setCode("409");
            System.out.println(fileUploadAIF);
            return fileUploadAIF;
        }
        if(fileUploadAIF==null)
            fileUploadAIF=fileUploadAIFRepository.findByFileLocationAndIsDeleted(sPath,0);
    }

        if (fileType.contains(prop.getString("file.investor.type"))) {
        try {
            investorValidate = investorProtfolioRepository.findByMonthYr(sStartTime);
        } catch (Exception e) {
            System.out.println(e);
        }
        if (investorValidate.isEmpty() == true) {

            String sFilesDirectory = prop.getString("fee.file.folder") + "DFA Backup\\\\"
                + prop.getString("file.investor.type") + "\\\\" + sFinal;
            File dirFiles = new File(sFilesDirectory);
            dirFiles.mkdirs();
            File sFile = new File(dirFiles, multipartFile.getOriginalFilename());
            writeFile(fileStream, sFile);
            System.out.println("File completed");
            int iPhysNumOfCells;
            FileInputStream fis = new FileInputStream(sFile.getPath());
            XSSFWorkbook workbook = new XSSFWorkbook(fis);

            XSSFSheet sheet = workbook.getSheetAt(0);
            int iPutNxtDetailsToDB = 0;
            int iTotRowInserted = 0;
            Iterator<Row> rowIterator = sheet.iterator();
            InvestorProtfolio investorProtfolio = new InvestorProtfolio();
            InvestorLogBean investorLogBean = new InvestorLogBean();
            while (rowIterator.hasNext()) {
                investorProtfolio = new InvestorProtfolio();
                investorLogBean = new InvestorLogBean();
                Row row = rowIterator.next();
                Row row2 = row;
                iTotRowInserted++;
                Iterator<Cell> cellIterator = row.cellIterator();

                int iPos = 0;
                int iTotConToMeet = 10;

                int iTotConMet = 0;
                int iConCheckNow = 0;
                String sConCheck = "";
                iPhysNumOfCells = row.getPhysicalNumberOfCells();

                int sMode = 0;

                System.out.println("Blank" + iPhysNumOfCells);
                while (cellIterator.hasNext() && iPutNxtDetailsToDB == 0) {
                    Cell cell = cellIterator.next();

                    if (iPutNxtDetailsToDB == 0) {
                        if (iPos == 0)
                            sConCheck = "Sl No";
                        if (iPos == 1)
                            sConCheck = "Client Code";
                        if (iPos == 2)
                            sConCheck = "Client Name";
                        if (iPos == 3)
                            sConCheck = "RM";
                        if (iPos == 4)
                            sConCheck = "Dsitributor Name";
                        if (iPos == 5)
                            sConCheck = "Class of Units";
                        if (iPos == 6)
                            sConCheck = "Series";
                        if (iPos == 7)
                            sConCheck = "Cost of Investment";
                        if (iPos == 8)
                            sConCheck = "NAV per Unit at Cost";
                        if (iPos == 9)
                            sConCheck = "No of Units";
                        /*
                         * if (iPos == 10) sConCheck =
                         * "Portfolio Value - NAV";
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

                String dCode = "";
                //DistributorMasterDO distId = null;
                Date dSeries = null;
                String tSeriesName = "";
                Integer sClient = 0;
                Float navValue = 0.0f;
                Float costOfInvest = 0.0f;
                Float navPerUnitCost = 0.0f;
                Float noOfUnits = 0.0f;
                Float navAsOn = 0.0f;
                Float portfolioVal = 0.0f;
                String sValue = "";
                AIFClientMaster aifClientMaster = null;
                SeriesMaster seriesMaster = null;

                if (iPhysNumOfCells == 12) {

                    if (iPutNxtDetailsToDB == 1 && iConCheckNow == 0) {

                        while (cellIterator.hasNext()) {
                            Cell cell = cellIterator.next();
                            // System.out.println("Cell
                            // Type->"+cell.getCellType());
                            switch (cell.getCellTypeEnum()) {
                                case STRING:
                                    try {
                                        dCode = row.getCell(5).getStringCellValue();
                                        dSeries = row.getCell(6).getDateCellValue();
                                        break;
                                    } catch (NoResultException exception) {
                                        System.out.println(exception);

                                    }

                                default:
                                    sClient = (int) row.getCell(1).getNumericCellValue();
                                    costOfInvest = (float) row.getCell(7).getNumericCellValue();
                                    navPerUnitCost = (float) row.getCell(8).getNumericCellValue();
                                    noOfUnits = (float) row.getCell(9).getNumericCellValue();
                                    navAsOn = (float) row.getCell(10).getNumericCellValue();
                                    portfolioVal = (float) row.getCell(11).getNumericCellValue();
                            }
                        }
                        try {
                            if (sClient != 0) {
                                aifClientMaster = aifClientMasterRepository.findByClientCode(sClient.toString());

                                String sSeries = dateFormat.format(dSeries);
                                tSeriesName = dCode + " (" + sSeries + ")";
                                // tSeriesName =
                                // tSeriesName.replace("-", " ");
                                seriesMaster = seriesMasterRepository.findBySeriesCode(tSeriesName.toString());
                                if (aifClientMaster != null && seriesMaster != null) {
                                    // navValue=Float.parseFloat(sValue);
                                    investorProtfolio.setAifClientMaster(aifClientMaster);
                                    investorProtfolio.setSeriesMaster(seriesMaster);
                                    investorProtfolio.setMonthYr(sStartTime);
                                    investorProtfolio.setCostInvestment(costOfInvest);
                                    investorProtfolio.setNavUnits(navPerUnitCost);
                                    investorProtfolio.setNoOfUnits(noOfUnits);
                                    investorProtfolio.setDistributorMaster(aifClientMaster.getDistributorMaster());
                                    investorProtfolio.setNavAsOn(navAsOn);
                                    investorProtfolio.setProtfolioValue(portfolioVal);
                                    investorProtfolio.setClosingUnits(noOfUnits);
                                    investorProtfolios.add(investorProtfolio);
                                } else {
                                    investorLogBean.setClientCode(sClient);
                                    investorLogBean.setSeriesName(tSeriesName);
                                    if (aifClientMaster == null)
                                        investorLogBean.setStatus("Client Not Found");
                                    else
                                        investorLogBean.setStatus("Series Not Found");
                                    investorLogBeans.add(investorLogBean);
                                    // UnifiJsfUtils.addErrorMessage("Series Not
                                    // found " + tSeriesName+"Client name not
                                    // found"+sClient);
                                    setEnableSave(false);
                                }
                            } else {
                                break;
                            }
                        } catch (Exception exception) {
                            System.out.println(exception);
                            investorLogBean.setClientCode(sClient);
                            investorLogBean.setSeriesName(tSeriesName);
                            if (aifClientMaster == null)
                                investorLogBean.setStatus("Client Not Found");
                            else
                                investorLogBean.setStatus("Series Not Found");
                            investorLogBeans.add(investorLogBean);
                            // UnifiJsfUtils.addErrorMessage("Series Not
                            // found " + tSeriesName+"Client name not
                            // found"+sClient);
                            setEnableSave(false);

                        }

                    }
                }

                /*
                 * RequestContext context =
                 * RequestContext.getCurrentInstance();
                 * context.execute("PF('status').show();");
                 */

            }
            saveClient(sFile, startDate, endDate,multipartFile.getOriginalFilename(),investorProtfolios);
        } else {
            log.error("Investor Portfolio already exists for this month");
        }
        if (enableSave == false) {
            setEnableDownload(false);
        }
    }
        if (fileType.contains(prop.getString("file.aif.management"))) {

        DateFormat df1 = new SimpleDateFormat("MM");
        String sMonth = df1.format(startDate);
        DateFormat df2 = new SimpleDateFormat("YYYY");
        String sYear = df2.format(startDate);
        managementValidate = new ArrayList<>();
        managementValidate = investorViewService.managMonth(sMonth, sYear);
        System.out.println(managementValidate.size());
        String sFilesDirectory = prop.getString("fee.file.folder") + "DFA Backup\\\\"
            + prop.getString("file.aif.management") + "\\\\" + sFinal;
        File dirFiles = new File(sFilesDirectory);
        dirFiles.mkdirs();
        File sFile = new File(dirFiles, multipartFile.getOriginalFilename());
        sPath=sFile.toString();
        fileUploadAIF = fileUploadAIFRepository.findByFileLocationAndIsDeleted(sFile.getPath(), 0);

        if (fileUploadAIF == null) {

            writeFile(fileStream, sFile);
            System.out.println("File completed");
            int iPhysNumOfCells;
            FileInputStream fis = new FileInputStream(sFile.getPath());
            XSSFWorkbook workbook = new XSSFWorkbook(fis);

            XSSFSheet sheet = workbook.getSheetAt(0);
            int iPutNxtDetailsToDB = 0;
            int iTotRowInserted = 0;
            Iterator<Row> rowIterator = sheet.iterator();
            MakerAIFManagePerMonth makerAIFManagePerMonth = new MakerAIFManagePerMonth();

            while (rowIterator.hasNext()) {
                makerAIFManagePerMonth = new MakerAIFManagePerMonth();
                Row row = rowIterator.next();
                Row row2 = row;
                iTotRowInserted++;
                Iterator<Cell> cellIterator = row.cellIterator();

                int iPos = 0;
                int iTotConToMeet = 3;

                int iTotConMet = 0;
                int iConCheckNow = 0;
                String sConCheck = "";
                iPhysNumOfCells = row.getPhysicalNumberOfCells();

                int sMode = 0;

                System.out.println("Blank" + iPhysNumOfCells);
                while (cellIterator.hasNext() && iPutNxtDetailsToDB == 0) {
                    Cell cell = cellIterator.next();

                    if (iPutNxtDetailsToDB == 0) {
                        if (iPos == 0)
                            sConCheck = "Particulars";
                        if (iPos == 1)
                            sConCheck = "Management Fee";
                        if (iPos == 2)
                            sConCheck = "Performance Fee";

                        if (cell.getStringCellValue().trim().equals(sConCheck))
                            iTotConMet++;

                        iPos++;

                        if (iTotConToMeet == iTotConMet) {
                            iPutNxtDetailsToDB = 1;
                            iConCheckNow = 1;
                        }
                    }
                }

                String tSeriesName = "";
                Float managValue = 0.0f;
                Float performValue = 0.0f;
                SeriesMaster seriesMaster = null;

                if (iPhysNumOfCells == 3) {

                    if (iPutNxtDetailsToDB == 1 && iConCheckNow == 0) {

                        while (cellIterator.hasNext()) {
                            Cell cell = cellIterator.next();
                            switch (cell.getCellTypeEnum()) {
                                case STRING:
                                    try {
                                        tSeriesName = row.getCell(0).getStringCellValue();
                                        break;
                                    } catch (NoResultException exception) {
                                        System.out.println(exception);

                                    }

                                default:

                                    managValue = (float) row.getCell(1).getNumericCellValue();
                                    performValue = (float) row.getCell(2).getNumericCellValue();
                            }
                        }
                        if (!(tSeriesName.trim()).equals("Total")) {
                            seriesMaster = seriesMasterRepository.findBySeriesCode(tSeriesName.toString());
                        }
                        if (seriesMaster == null && (!tSeriesName.trim().equals("Total"))) {
                            log.error("Series Type Not found " + tSeriesName);
                            setEnableSave(false);
                        }

                        if ((!tSeriesName.equals("Total")) && (seriesMaster != null)) {
                            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                            String asOndate = df.format(startDate);
                            Date asDate = df.parse(asOndate);
                            System.out.println(asOndate);

                            makerAIFManagePerMonth.setSeriesMaster(seriesMaster);
                            makerAIFManagePerMonth.setManagementFee(managValue);
                            makerAIFManagePerMonth.setPerformanceFee(performValue);
                            makerAIFManagePerMonth.setIsDeleted(0);
                            //aifMasterFeeDO.setFileUploadAifDO(uploadAifDO);
                            makerAIFManagePerMonth.setAsOnDate(asDate);

                            makerAIFManagePerMonths.add(makerAIFManagePerMonth);
                        } else {
                            break;
                        }

                    }
                }
                /*
                 * RequestContext context =
                 * RequestContext.getCurrentInstance();
                 * context.execute("PF('status').show();");
                 */
            }

            saveClient(sFile, startDate, endDate,multipartFile.getOriginalFilename(), investorProtfolios);

        } else {
            log.error("Management Fee already exists for this month");
            fileUploadAIF.setCode("412");
            System.out.println(fileUploadAIF);
            return fileUploadAIF;
        }
        if(fileUploadAIF==null)
            fileUploadAIF=fileUploadAIFRepository.findByFileLocationAndIsDeleted(sPath,0);
    }

        return fileUploadAIF;
    }

    public void saveClient(File sFile, Date startDate, Date endDate, String sFileName, List<InvestorProtfolio> investorProtfolios) {
        try {
            FileUploadAIF fileUploadAIF;
            AIFUpdatedUnits aifUpdatedUnits;
            List<InvestorProtfolio> investView;
            if (enableSave) {
                if (seriesMasterMonths.isEmpty() != true) {
                    fileUploadAIF = saveUploadSeriesFileName(sFile);
                    for (MakerSeriesMasterMonth seriesMasterMonth : seriesMasterMonths) {
                        seriesMasterMonth.setFileUploadAIF(fileUploadAIF);
                        makerSeriesMonthRepository.save(seriesMasterMonth);
                    }
                }
                if (this.investorProtfolios.isEmpty() != true) {
                    for (InvestorProtfolio investorProtfolio : this.investorProtfolios) {
                        aifUpdatedUnits = new AIFUpdatedUnits();
                        investorProtfolioRepository.save(investorProtfolio);
                        aifUpdatedUnits.setAifClientMaster(investorProtfolio.getAifClientMaster());
                        aifUpdatedUnits.setSeriesMaster(investorProtfolio.getSeriesMaster());
                        aifUpdatedUnits.setDistributorMaster(investorProtfolio.getDistributorMaster());
                        aifUpdatedUnits.setTotRemUnits(investorProtfolio.getClosingUnits());
                        aifUpdatedUnits.setMonthYear(investorProtfolio.getMonthYr());
                        aifUpdatedUnitsRepository.save(aifUpdatedUnits);
                    }

                    DateFormat dateFormat = new SimpleDateFormat("MMM yyyy");
                    String sMonth = dateFormat.format(startDate);
                    investView = new ArrayList<>();
                    investView = investorViewService.seriesCalc(sMonth);
                    System.out.println("Complete");
                }
               if (makerAIFManagePerMonths.isEmpty() != true) {
                    fileUploadAIF=saveUploadFeeFileName(sFile);
                    for (MakerAIFManagePerMonth makerAIFManagePerMonth : makerAIFManagePerMonths) {
                        makerAIFManagePerMonth.setFileUploadAIF(fileUploadAIF);
                        makerAIFManagePerMonthRepository.save(makerAIFManagePerMonth);
                    }
                }
                log.info("Successfully uploaded");
            }
            if (investorLogBeans.isEmpty() == false) {
                investorCreate(investorLogBeans,sFileName);
                log.error("Investor Portfolio is not uploaded");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private FileUploadAIF saveUploadFeeFileName(File sFile) {
        FileType fileType;
        FileUploadAIF fileUploadAIF;
        fileUploadAIF = new FileUploadAIF();
        fileType = fileTypeRepository.findByFileType("Management Performance Fee");
        if (fileType != null) {
            fileUploadAIF.setFileType(fileType);
        }
        fileUploadAIF.setIsDeleted(0);
        fileUploadAIF.setFileLocation(sFile.getPath());
        fileUploadAIF.setUploadApproved((long)0);
        // fileUploadAifDO.set
        fileUploadAIFRepository.save(fileUploadAIF);
        return fileUploadAIF;

    }

    FileOutputStream fos = null;
    XSSFWorkbook workBook = null;

    private void investorCreate(List<InvestorLogBean> investorLogBeans2,String sFileName) {

        try {

            String sFilesDirectory = prop.getString("fee.file.folder") + "DFA Backup\\\\"
                + prop.getString("bulk.upload.not");
            File dirFiles = new File(sFilesDirectory);
            dirFiles.mkdirs();

            String file = sFilesDirectory + "\\\\" + sFileName;
            fileDownload.add(file);
            System.out.println(file);

            fos = new FileOutputStream(file);
            workBook = new XSSFWorkbook();

            String reportDate = "Investor";
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
            headingBRSBookRow.createCell(1).setCellValue("Series Name");
            headingBRSBookRow.getCell(1).setCellStyle(cs);
            sheet.autoSizeColumn(1);
            headingBRSBookRow.createCell(2).setCellValue("Distributor Name");
            headingBRSBookRow.getCell(2).setCellStyle(cs);
            sheet.autoSizeColumn(2);
            headingBRSBookRow.createCell(3).setCellValue("Status");
            headingBRSBookRow.getCell(3).setCellStyle(cs);
            sheet.autoSizeColumn(3);

            iRowNo = sheet.getLastRowNum() + 1;

            for (InvestorLogBean bean : investorLogBeans) {
                XSSFRow row = sheet.createRow(iRowNo);

                row.createCell(0).setCellValue(bean.getClientCode());
                sheet.autoSizeColumn(0);
                System.out.println(bean.getClientCode());
                row.createCell(1).setCellValue(bean.getSeriesName());
                sheet.autoSizeColumn(1);
                System.out.println(bean.getSeriesName());
                row.createCell(2).setCellValue(bean.getDistName());
                sheet.autoSizeColumn(2);
                System.out.println(bean.getDistName());
                row.createCell(3).setCellValue(bean.getStatus());
                sheet.autoSizeColumn(3);
                System.out.println(bean.getStatus());
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

    public FileUploadAIF saveUploadSeriesFileName(File file) {
        FileType fileType;
        FileUploadAIF fileUploadAIF;
        fileUploadAIF = new FileUploadAIF();
        fileType = fileTypeRepository.findByFileType("Series Master");
        if (fileType != null) {
            fileUploadAIF.setFileType(fileType);
        }
        fileUploadAIF.setIsDeleted(0);
        fileUploadAIF.setFileLocation(file.getPath());
        fileUploadAIF.setUploadApproved((long) 0);
        // fileUploadAifDO.set
        fileUploadAIFRepository.save(fileUploadAIF);
        return fileUploadAIF;

    }

    public Optional<FileUploadAIF> approveFile(Optional<FileUploadAIF> result) {
        if (result.get().getFileType().getFileType().equals("Series Master")) {

            try {
                Query query = entityManager.createNativeQuery("insert into series_monthwise select * from maker_series_monthwise ms where ms.file_upload_aif_id=" + result.get().getId());
                query.executeUpdate();

                query = entityManager.createNativeQuery("update file_upload_aif f set f.upload_approved=1 where f.id=" + result.get().getId());
                query.executeUpdate();

            } catch (PersistenceException cve) {
                cve.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (result.get().getFileType().getFileType().equals("Management Performance Fee")) {
            try {
                Query query = entityManager.createNativeQuery("insert into fee_mange_perf_month select * from maker_fee_mange_perf_month mf where mf.file_upload_aif_id=" + result.get().getId());
                query.executeUpdate();

                query = entityManager.createNativeQuery("update file_upload_aif f set f.upload_approved=1 where f.id=" + result.get().getId());
                query.executeUpdate();

            } catch (PersistenceException cve) {
                cve.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    public int deleteFile(Long id) {
        int result = 0;
        Optional<FileUploadAIF> file = fileUploadAIFRepository.findById(id);
        System.out.println(file);
        if (file.get().getFileType().getFileType().equals("Series Master")) {
            result = investorViewService.deleteSeriesMonth(file);
            file.get().setIsDeleted(1);
            fileUploadAIFRepository.save(file.get());
            System.out.println("--------" + result);
            log.info("deleted successfully");
        }
        if (file.get().getFileType().getFileType().equals("Management Performance Fee")) {

            result = investorViewService.deleteManagePerformance(file.get().getId());
            file.get().setIsDeleted(1);
            fileUploadAIFRepository.save(file.get());
            System.out.println("--------" + result);
            log.info("deleted successfully");
        }
return result;
    }
}
