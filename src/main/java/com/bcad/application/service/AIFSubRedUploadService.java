package com.bcad.application.service;

import com.bcad.application.config.PropertiesUtils;
import com.bcad.application.domain.*;
import com.bcad.application.repository.*;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.io.IOUtils;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class AIFSubRedUploadService {

    private byte[] fileStream;
    private String fileName;
    private Boolean enableSave = true;
    private boolean validateSave;

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

    public Boolean getEnableSave() {
        return enableSave;
    }

    public void setEnableSave(Boolean enableSave) {
        this.enableSave = enableSave;
    }

    public boolean isValidateSave() {
        return validateSave;
    }

    public void setValidateSave(boolean validateSave) {
        this.validateSave = validateSave;
    }

    private final AIFClientMasterRepository aifClientMasterRepository;
    private final InvestorProtfolioRepository investorProtfolioRepository;
    private final SeriesMasterRepository seriesMasterRepository;
    private final SeriesMasterMonthRepository seriesMasterMonthRepository;
    private final AIFRedemptionMasterRepository aifRedemptionMasterRepository;
    private final InvestorViewService investorViewService;
    private final AIFUpdatedUnitsRepository aifUpdatedUnitsRepository;
    private final InvestorViewRepository investorViewRepository;

    public AIFSubRedUploadService(AIFClientMasterRepository aifClientMasterRepository, InvestorProtfolioRepository investorProtfolioRepository,
                                  SeriesMasterRepository seriesMasterRepository,SeriesMasterMonthRepository seriesMasterMonthRepository,
                                  AIFRedemptionMasterRepository aifRedemptionMasterRepository, InvestorViewService investorViewService,
                                  AIFUpdatedUnitsRepository aifUpdatedUnitsRepository,InvestorViewRepository investorViewRepository) {
        this.aifClientMasterRepository = aifClientMasterRepository;
        this.investorProtfolioRepository = investorProtfolioRepository;
        this.seriesMasterRepository = seriesMasterRepository;
        this.seriesMasterMonthRepository = seriesMasterMonthRepository;
        this.aifRedemptionMasterRepository = aifRedemptionMasterRepository;
        this.investorViewService = investorViewService;
        this.aifUpdatedUnitsRepository= aifUpdatedUnitsRepository;
        this.investorViewRepository = investorViewRepository;
    }

    PropertiesConfiguration prop = new PropertiesUtils().getProperties("application.properties");
    private final Logger log = LoggerFactory.getLogger(AIFSubRedUploadService.class);
    private List<InvestorProtfolio> investorProtfolios= new ArrayList<InvestorProtfolio>();
    private List<InvestorProtfolio> investorUpdateInsertions= new ArrayList<InvestorProtfolio>();
    private List<InvestorProtfolio> investorForAllDOs = new ArrayList<>();

    public FileUploadAIF fileSubUpload(Date redemptionDate, String sFileType, MultipartFile multipartFile) throws IOException {

        investorProtfolios= new ArrayList<InvestorProtfolio>();
        investorUpdateInsertions= new ArrayList<InvestorProtfolio>();
        investorForAllDOs = new ArrayList<>();
        FileUploadAIF fileUploadAIF= new FileUploadAIF();

        DateFormat dateFormat = new SimpleDateFormat("MMM yyyy");
        String sStartTime = dateFormat.format(redemptionDate);
        String sFinal = sStartTime + "_to_" + sStartTime;

        if (sFileType.contains(prop.getString("sub.file.upload"))) {

            String sFilesDirectory = prop.getString("fee.file.folder") + "DFA Backup\\\\"
                + prop.getString("sub.file.upload") + "\\\\" + sFinal;
            File dirFiles = new File(sFilesDirectory);
            dirFiles.mkdirs();
            fileStream = IOUtils.toByteArray(multipartFile.getInputStream());
            setFileName(multipartFile.getOriginalFilename());
            File sFile = new File(dirFiles, fileName);
            writeFile(fileStream, sFile);
            fileUploadAIF = readSubscriptionFile(sFile, sStartTime);


        }
        if(sFileType.contains(prop.getString("red.file.upload")))
        {
            String sFilesDirectory = prop.getString("fee.file.folder") + "DFA Backup\\\\"
                + prop.getString("red.file.upload") + "\\\\" + sFinal;
            File dirFiles = new File(sFilesDirectory);
            dirFiles.mkdirs();
            fileStream = IOUtils.toByteArray(multipartFile.getInputStream());
            setFileName(multipartFile.getOriginalFilename());
            File sFile = new File(dirFiles, fileName);
            writeFile(fileStream, sFile);
            fileUploadAIF =  readRedemptionFile(sFile,sStartTime,redemptionDate);
        }

        return fileUploadAIF;
    }

    private FileUploadAIF readRedemptionFile(File sFile, String sStartTime, Date redemptionDate) throws IOException {

        FileUploadAIF uploadAIF = new FileUploadAIF();
        int iPhysNumOfCells;
        FileInputStream fis = new FileInputStream(sFile);
        HSSFWorkbook workbook = new HSSFWorkbook(fis);

        HSSFSheet sheet = workbook.getSheetAt(0);
        int iPutNxtDetailsToDB = 0;
        Iterator<Row> rowIterator = sheet.iterator();

        int iTotRowInserted = 0;
        InvestorProtfolio investorProtfolio =new InvestorProtfolio();
        while (rowIterator.hasNext()) {
            investorProtfolio=new InvestorProtfolio();

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

            // System.out.println("Blank"+iPhysNumOfCells);
            while (cellIterator.hasNext() && iPutNxtDetailsToDB == 0) {
                Cell cell = cellIterator.next();

                if (iPutNxtDetailsToDB == 0) {
                    if (iPos == 0)
                        sConCheck = "Client Code";
                    if (iPos == 1)
                        sConCheck = "Client Name";
                    if (iPos == 2)
                        sConCheck = "Series";
                    if (iPos == 3)
                        sConCheck = "Redemption Units";

                    if (cell.getStringCellValue().trim().equals(sConCheck))
                        iTotConMet++;

                    iPos++;

                    if (iTotConToMeet == iTotConMet) {
                        iPutNxtDetailsToDB = 1;
                        iConCheckNow = 1;
                    }
                }
            }
            Integer clientCode = 0;
            String sName="";
            String sSeries="";
            Float sUnits=0f;
            Float validateUnits=0f;

            AIFClientMaster aifClientMaster=null;
            DistributorMaster distributorMaster = null;
            SeriesMaster seriesMaster=null;
            SeriesMasterMonth selectedSeriesMasterMonthDO=null;
            InvestorProtfolio investor=null;

            InvestmentMaster investmentMaster=null;
            DataFormatter formatter = new DataFormatter();
            if (iPhysNumOfCells != 0 && iPhysNumOfCells != 3) {

                if (iPutNxtDetailsToDB == 1 && iConCheckNow == 0) {

                    while (cellIterator.hasNext()) {
                        Cell cell = cellIterator.next();
                        // System.out.println("Cell
                        // Type->"+cell.getCellType());
                        switch (cell.getCellTypeEnum()) {
                            case STRING:
                                sName = row.getCell(1).getStringCellValue();
                                sSeries = row.getCell(2).getStringCellValue();
                                break;
                            case NUMERIC:
                                sUnits =(float) row.getCell(3).getNumericCellValue();
                                clientCode = (int) row.getCell(0).getNumericCellValue();
                                break;
                            default:

                        }
                    }

                    try {

                        aifClientMaster = aifClientMasterRepository.findByClientCode(clientCode.toString());
                        seriesMaster=seriesMasterRepository.findBySeriesCode(sSeries);
                        investorProtfolio=investorProtfolioRepository.findBySeriesMasterAndAifClientMaster(seriesMaster,aifClientMaster);
                        String monthYr;

                        monthYr = getMonthYr(redemptionDate);
                        System.out.println(seriesMaster);
                        selectedSeriesMasterMonthDO = seriesMasterMonthRepository.findBySeriesMasterAndMonthYearAndIsDeleted(seriesMaster,monthYr,0);

                        if (selectedSeriesMasterMonthDO == null) {
                            log.error("please upload Nav value");
                            uploadAIF.setCode("320");
                            uploadAIF.setStatus("please upload Nav value");
                            return  uploadAIF;
                        }
                        investorProtfolio.setAifClientMaster(aifClientMaster);
                        investorProtfolio.setSeriesMaster(seriesMaster);
                        investorProtfolio.setNoOfRedemptingUnit(sUnits);
                        investorProtfolio.setClosingUnits(investorProtfolio.getClosingUnits());
                        System.out.println(selectedSeriesMasterMonthDO);
                        investorProtfolio.setNavUnits(selectedSeriesMasterMonthDO.getNavValue());
                        investorProtfolios.add(investorProtfolio);


                    if(aifClientMaster==null ||seriesMaster==null ) {
                        if (aifClientMaster == null){
                            log.error("Client Code not Found" + clientCode);
                            uploadAIF.setCode("321");
                            uploadAIF.setStatus("Client Code not Found : " + clientCode);
                            return uploadAIF;}

                        if (seriesMaster == null && aifClientMaster != null){

                            log.error("Series code not Found"+sSeries);
                            uploadAIF.setCode("322");
                            uploadAIF.setStatus("Series Code not Found : " + sSeries);
                            return uploadAIF;
                        }
                            log.error("Series code not Found" + sSeries);
                        setEnableSave(false);
                    }
                    }
                    catch (Exception exception) {

                        if (aifClientMaster==null) {
                            log.error("Client Code not Found" + clientCode);
                            uploadAIF.setCode("321");
                            uploadAIF.setStatus("Client Code not Found : " + clientCode);
                            return uploadAIF;
                        }

                        if(seriesMaster==null && aifClientMaster!=null){
                            log.error("Series code not Found"+sSeries);
                            uploadAIF.setCode("322");
                            uploadAIF.setStatus("Series Code not Found : " + sSeries);
                            return uploadAIF;
                        }
                        setEnableSave(false);
                    }


                }


            }


        }
        uploadAIF = validateRedemption(investorProtfolios,redemptionDate);
        return uploadAIF;
    }

    private String getMonthYr(Date redemptionDate) {
            Date result;
            String monthYr;
            GregorianCalendar calendar;
            DateFormat monthYrformat;

            calendar = new GregorianCalendar();
            calendar.setTime(redemptionDate);
            calendar.add(Calendar.MONTH, -1);
            result = calendar.getTime();
            monthYrformat = new SimpleDateFormat("MMM yyyy");
            monthYr = monthYrformat.format(result);
            return monthYr;


    }
    public FileUploadAIF validateRedemption(List<InvestorProtfolio> investorProtfolios, Date redemptionDate) {
        List<InvestorProtfolio> tempInvestorProtfolios;
        FileUploadAIF uploadAIF = new FileUploadAIF();
        Float costOfRedemptingUnits = 0f;
        validateSave = true;
        try {
            tempInvestorProtfolios = new ArrayList<InvestorProtfolio>();
            for (InvestorProtfolio investorProtfolioDO : investorProtfolios) {
                if (investorProtfolioDO.getNoOfRedemptingUnit() != 0f) {
                    if ((investorProtfolioDO.getClosingUnits() >= investorProtfolioDO.getNoOfRedemptingUnit()) ||
                        ((investorProtfolioDO.getClosingUnits()+1) >= investorProtfolioDO.getNoOfRedemptingUnit())) {
                        costOfRedemptingUnits = investorProtfolioDO.getNoOfRedemptingUnit()
                            * investorProtfolioDO.getNavAsOn();
                        investorProtfolioDO.setCostOfRedemptingUnit(costOfRedemptingUnits);
                        tempInvestorProtfolios.add(investorProtfolioDO);
                    }

                    else {
                        log.error("You can not redempt more than available unit");
                        uploadAIF.setCode("322");
                        uploadAIF.setStatus("You can not redempt more than available unit");
                        validateSave = false;
                        return uploadAIF;
                    }
                }
            }
           uploadAIF = saveRedemption(investorProtfolios,redemptionDate);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return uploadAIF;
    }
    List<InvestorView> investorCheckMonth = new ArrayList<>();
    List<AIFUpdatedUnits> investView;
    List<AIFUpdatedUnits> updateViewSeries;

    private FileUploadAIF saveRedemption(List<InvestorProtfolio> investorProtfolios, Date redemptionDate) {
        FileUploadAIF uploadAIF= new FileUploadAIF();
        AIFRedemptionMaster aifRedemptionMaster;
        List<InvestorProtfolio> investViewDO;
        String loggedUser;
        DateFormat dateFormat = new SimpleDateFormat("MMM yyyy");
        String sMonth = dateFormat.format(redemptionDate);
        //loggedUser = (String) log.getSession().getAttribute("logged");
        try {

            validateInvestView(sMonth);
            if (validateSave) {
                for (InvestorProtfolio investorProtfolio : investorProtfolios) {
                    InvestorProtfolio investorProtfolio1 = investorProtfolioRepository.findBySeriesMasterAndAifClientMaster(investorProtfolio.getSeriesMaster(),
                        investorProtfolio.getAifClientMaster());
                    aifRedemptionMaster = new AIFRedemptionMaster();
                    aifRedemptionMaster.setRedemptionDate(redemptionDate);
                    aifRedemptionMaster.setAifClientMaster(investorProtfolio.getAifClientMaster());
                    aifRedemptionMaster.setSeriesMaster(investorProtfolio.getSeriesMaster());
                    aifRedemptionMaster.setNoRedempedUnit(investorProtfolio.getNoOfRedemptingUnit());
                    aifRedemptionMaster.setNoOfUnitHold(investorProtfolio.getClosingUnits());
                    aifRedemptionMaster.setClosingUnit(
                        investorProtfolio.getClosingUnits() - investorProtfolio.getNoOfRedemptingUnit());
                    aifRedemptionMaster.setTotalCostOfUnit(
                        investorProtfolio.getClosingUnits() * investorProtfolio.getNavAsOn());
                    aifRedemptionMaster.setTotalCostRedemped(
                        investorProtfolio.getNoOfRedemptingUnit() * investorProtfolio.getNavAsOn());
                    aifRedemptionMaster.setTotalClosing(
                        aifRedemptionMaster.getClosingUnit() * investorProtfolio.getNavAsOn());
                    investorProtfolio1.setClosingUnits(aifRedemptionMaster.getClosingUnit());
                    investorProtfolioRepository.save(investorProtfolio1);
                    if (investorProtfolio.getNoOfRedemptingUnit() != 0.0f) {
                        aifRedemptionMasterRepository.save(aifRedemptionMaster);
                        aifSeriesCreation(aifRedemptionMaster,redemptionDate);
                        try {
                            investorCheckMonth = investorViewRepository.findByMonthYear(sMonth);
                            updateViewSeries = new ArrayList<>();
                            if (investorCheckMonth.isEmpty() == false) {
                                updateViewSeries = investorViewService.updateSeries(sMonth,
                                    investorProtfolio.getSeriesMaster());
                            }
                            if (investorCheckMonth.isEmpty() == true) {
                                investView = new ArrayList<>();
                                investView = investorViewService.seriesCalcInvestor(sMonth);
                            }
                        } catch (Exception e) {
                            System.out.println(e);
                        }
                    }
                }
                uploadAIF.setCode("400");
                uploadAIF.setStatus("Uploaded Successfully");


            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e);
        }


        return uploadAIF;
    }
    List<AIFUpdatedUnits> aifUpdatedMonth = new ArrayList<>();
    private void aifSeriesCreation(AIFRedemptionMaster aifRedemptionMaster, Date redemptionDate) {

        investorForAllDOs = new ArrayList<InvestorProtfolio>();
        investorForAllDOs = investorProtfolioRepository.findAll();
        AIFUpdatedUnits aifUpdatedUnitsDO;
        DateFormat dateFormat = new SimpleDateFormat("MMM yyyy");
        String sMonth = dateFormat.format(redemptionDate);
        try {
            aifUpdatedMonth = aifUpdatedUnitsRepository.findByMonthYear(sMonth);
        } catch (Exception e) {
            System.out.println(e);
        }
        if (aifUpdatedMonth.isEmpty() == true) {
            for (InvestorProtfolio investorProtfolioDO : investorForAllDOs) {
                aifUpdatedUnitsDO = new AIFUpdatedUnits();
                aifUpdatedUnitsDO.setAifClientMaster(investorProtfolioDO.getAifClientMaster());
                aifUpdatedUnitsDO.setSeriesMaster(investorProtfolioDO.getSeriesMaster());
                aifUpdatedUnitsDO.setDistributorMaster(investorProtfolioDO.getDistributorMaster());
                aifUpdatedUnitsDO.setTotRemUnits(investorProtfolioDO.getClosingUnits());
                aifUpdatedUnitsDO.setMonthYear(sMonth);
                aifUpdatedUnitsRepository.save(aifUpdatedUnitsDO);
            }

        } else {
            aifUpdatedUnitsDO = aifUpdatedUnitsRepository.findByMonthYearAndAifClientMasterAndSeriesMaster(sMonth,
                aifRedemptionMaster.getAifClientMaster(),aifRedemptionMaster.getSeriesMaster());
            aifUpdatedUnitsDO.setTotRemUnits(aifRedemptionMaster.getClosingUnit());
            aifUpdatedUnitsRepository.save(aifUpdatedUnitsDO);
        }


    }

    List<AIFUpdatedUnits> validateInvest;
    List<AIFUpdatedUnits> validateUpdatedUnits=new ArrayList<AIFUpdatedUnits>();

    private void validateInvestView(String sMonth) {

        try {
            AIFUpdatedUnits aifUpdatedUnits;

            String mon = investorViewService.getInvestorMonth();
            System.out.println(mon);
            DateFormat formater = new SimpleDateFormat("MMM yyyy");

            Calendar beginCalendar = Calendar.getInstance();
            Calendar finishCalendar = Calendar.getInstance();
            beginCalendar.setTime(formater.parse(mon));
            finishCalendar.setTime(formater.parse(sMonth));
            while (beginCalendar.before(finishCalendar)) {
                /*
                 * String date = names.format(beginCalendar.getTime()); String
                 * dates = year.format(beginCalendar.getTime());
                 */
                String vMonth = formater.format(beginCalendar.getTime());
                validateUpdatedUnits = aifUpdatedUnitsRepository.findByMonthYear(vMonth);
                if (validateUpdatedUnits.isEmpty() == true) {
                    investorUpdateInsertions = new ArrayList<InvestorProtfolio>();
                    investorUpdateInsertions = investorProtfolioRepository.findAll();
                    for (InvestorProtfolio investorProtfolioDO : investorUpdateInsertions) {
                        aifUpdatedUnits = new AIFUpdatedUnits();
                        aifUpdatedUnits.setAifClientMaster(investorProtfolioDO.getAifClientMaster());
                        aifUpdatedUnits.setSeriesMaster(investorProtfolioDO.getSeriesMaster());
                        aifUpdatedUnits.setDistributorMaster(investorProtfolioDO.getDistributorMaster());
                        aifUpdatedUnits.setTotRemUnits(investorProtfolioDO.getClosingUnits());
                        aifUpdatedUnits.setMonthYear(vMonth);
                        aifUpdatedUnitsRepository.save(aifUpdatedUnits);
                    }

                    validateInvest = new ArrayList<>();
                    validateInvest = investorViewService.seriesCalcInvestor(sMonth);

                }
                beginCalendar.add(Calendar.MONTH, 1);
                // validateRedempt
            }
        } catch (Exception e) {
            System.out.println(e);
        }


    }

    private FileUploadAIF readSubscriptionFile(File sFile, String sStartTime) throws FileNotFoundException, IOException {
        enableSave=true;

        FileUploadAIF uploadAIF=new FileUploadAIF();
        int iPhysNumOfCells;
        FileInputStream fis = new FileInputStream(sFile);
        HSSFWorkbook workbook = new HSSFWorkbook(fis);

        HSSFSheet sheet = workbook.getSheetAt(0);
        int iPutNxtDetailsToDB = 0;
        Iterator<Row> rowIterator = sheet.iterator();

        int iTotRowInserted = 0;
        InvestorProtfolio investorProtfolioDO = new InvestorProtfolio();
        while (rowIterator.hasNext()) {
            investorProtfolioDO = new InvestorProtfolio();

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

            // System.out.println("Blank"+iPhysNumOfCells);
            while (cellIterator.hasNext() && iPutNxtDetailsToDB == 0) {
                Cell cell = cellIterator.next();

                if (iPutNxtDetailsToDB == 0) {
                    if (iPos == 0)
                        sConCheck = "Client Code";
                    if (iPos == 1)
                        sConCheck = "Client Name";
                    if (iPos == 2)
                        sConCheck = "Series";
                    if (iPos == 3)
                        sConCheck = "Units";

                    if (cell.getStringCellValue().trim().equals(sConCheck))
                        iTotConMet++;

                    iPos++;

                    if (iTotConToMeet == iTotConMet) {
                        iPutNxtDetailsToDB = 1;
                        iConCheckNow = 1;
                    }
                }
            }
            Integer clientCode = 0;
            String sName = "";
            String sSeries = "";
            Float sUnits = 0f;
            Float validateUnits = 0f;

            AIFClientMaster aifClientMaster = null;
            DistributorMaster distributorMasterDO = null;
            SeriesMaster seriesMaster = null;

            InvestmentMaster investmentMaster = null;
            DataFormatter formatter = new DataFormatter();
            if (iPhysNumOfCells != 0 && iPhysNumOfCells != 3) {

                if (iPutNxtDetailsToDB == 1 && iConCheckNow == 0) {

                    while (cellIterator.hasNext()) {
                        Cell cell = cellIterator.next();
                        switch (cell.getCellTypeEnum()) {
                            case STRING:
                                sName = row.getCell(1).getStringCellValue();
                                sSeries = row.getCell(2).getStringCellValue();
                                break;
                            case NUMERIC:
                                sUnits = (float) row.getCell(3).getNumericCellValue();
                                clientCode = (int) row.getCell(0).getNumericCellValue();

                                break;
                            default:

                        }
                    }

                    try {

                        aifClientMaster = aifClientMasterRepository.findByClientCode(clientCode.toString());
                        seriesMaster = seriesMasterRepository.findBySeriesCode(sSeries);
                        if(aifClientMaster != null && seriesMaster!=null ) {
                            if (seriesMaster.getInitPerCost() > 0.0) {
                                validateUnits = sUnits * seriesMaster.getInitPerCost();
                                if (validateUnits <= 0) {
                                    //UnifiJsfUtils.addErrorMessage("No of units is zero");
                                    FileUploadAIF fileUploadAIF = new FileUploadAIF();
                                    fileUploadAIF.setCode("230");
                                    fileUploadAIF.setStatus("No of units is zero");
                                    setEnableSave(false);
                                    return fileUploadAIF;
                                }
                                investorProtfolioDO.setAifClientMaster(aifClientMaster);
                                investorProtfolioDO.setSeriesMaster(seriesMaster);
                                if (aifClientMaster.getDistributorMaster() != null)
                                    investorProtfolioDO.setDistributorMaster(aifClientMaster.getDistributorMaster());
                                investorProtfolioDO.setMonthYr(sStartTime);
                                investorProtfolioDO.setNavAsOn(seriesMaster.getInitPerCost());
                                investorProtfolioDO.setNoOfUnits(sUnits);
                                investorProtfolios.add(investorProtfolioDO);

                            } else {
                                log.error("Initial Per unit cost should not be 0");
                                // UnifiJsfUtils.addErrorMessage("Initial Per unit cost should not be 0");
                                FileUploadAIF fileUploadAIF = new FileUploadAIF();
                                fileUploadAIF.setCode("230");
                                fileUploadAIF.setStatus("Initial Per unit cost should not be 0");
                                return fileUploadAIF;
                            }
                        }
                        else{
                            FileUploadAIF fileUploadAIF= new FileUploadAIF();
                            fileUploadAIF.setCode("210");

                            if (aifClientMaster == null){
                                log.error("Client Code not Found" + clientCode);
                                fileUploadAIF.setStatus("Client Code Not found : "+clientCode);
                                setEnableSave(false);
                                return fileUploadAIF;
                            }

                            if (seriesMaster == null){
                                fileUploadAIF.setStatus("Series Not found : "+sSeries);
                                setEnableSave(false);
                                return fileUploadAIF;
                            }
                        }
                    } catch (Exception exception) {
                        FileUploadAIF fileUploadAIF= new FileUploadAIF();
                        fileUploadAIF.setCode("210");

                        if (aifClientMaster == null){
                            log.error("Client Code not Found" + clientCode);
                            fileUploadAIF.setStatus("Client Code Not found : "+clientCode);
                            setEnableSave(false);
                            return fileUploadAIF;
                        }

                        if (seriesMaster == null){
                            fileUploadAIF.setStatus("Series Not found : "+sSeries);
                            setEnableSave(false);
                            return fileUploadAIF;
                           }
                        System.out.println(fileUploadAIF);


                    }


                }

            }

        }
        uploadAIF= saveClient(sFile, sStartTime);


        return uploadAIF;
    }

    private FileUploadAIF saveClient(File sFile, String sStartTime) {
        FileUploadAIF uploadAIF=new FileUploadAIF();

        if(enableSave)
        {
            if(investorProtfolios.isEmpty()==false)
            {
                Float noOfUnits=0f;
                InvestorProtfolio investDO;
                for (InvestorProtfolio bean : investorProtfolios) {
                    investDO=new InvestorProtfolio();
                    investDO=investorProtfolioRepository.findByMonthYrAndAifClientMaster(sStartTime,bean.getAifClientMaster());
                    if(investDO!=null){
                        noOfUnits=investDO.getClosingUnits()+bean.getNoOfUnits();
                        investDO.setClosingUnits(noOfUnits);
                        investDO.setNoOfUnits(noOfUnits);
                        investDO.setSeriesMaster(bean.getSeriesMaster());
                        investorProtfolioRepository.save(investDO);

                    }else{
                        bean.setMonthYr(sStartTime);
                        bean.setSeriesMaster(bean.getSeriesMaster());
                        bean.setClosingUnits(bean.getNoOfUnits());
                        if(bean.getAifClientMaster().getDistributorMaster()!=null){
                            bean.setDistributorMaster(bean.getAifClientMaster().getDistributorMaster());
                        }
                        investorProtfolioRepository.save(bean);
                    }
                }
                uploadAIF.setCode("400");
                uploadAIF.setStatus("Uploaded Successfully");

            }
        }

    return uploadAIF;
    }

    private void writeFile(byte[] fileStream, File sFile) throws FileNotFoundException, IOException {
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
}
