package com.bcad.application.service;

import com.bcad.application.config.PropertiesUtils;
import com.bcad.application.domain.*;
import com.bcad.application.repository.*;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.io.IOUtils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


@Service
@Transactional
public class TransactionReportService {
    private byte[] fileStream;

    PropertiesConfiguration prop = new PropertiesUtils().getProperties("application.properties");
    private String fileName;

    private final TransactionReportRepository transactionReportRepository;
    private final AIF2InvestmentsRepository aif2InvestmentsRepository;
    private final AIF2SeriesMasterRepository aif2SeriesMasterRepository;
    private final ProductRepository productRepository;
    private final ClientManagementRepository clientManagementRepository;
    private final UmbrellaReportsRepository umbrellaReportsRepository;

    public TransactionReportService(TransactionReportRepository transactionReportRepository, AIF2InvestmentsRepository aif2InvestmentsRepository, AIF2SeriesMasterRepository aif2SeriesMasterRepository,
                                    ProductRepository productRepository, ClientManagementRepository clientManagementRepository,UmbrellaReportsRepository umbrellaReportsRepository) {
        this.transactionReportRepository = transactionReportRepository;
        this.aif2InvestmentsRepository = aif2InvestmentsRepository;
        this.aif2SeriesMasterRepository = aif2SeriesMasterRepository;
        this.productRepository = productRepository;
        this.clientManagementRepository = clientManagementRepository;
        this.umbrellaReportsRepository=umbrellaReportsRepository;
    }


    public TransactionReport TransactionReportFileUpload(Date startDate, Date endDate, String fileType, MultipartFile multipartFile) throws IOException, ParseException {

        List<TransactionReport> transactionReport = new ArrayList<>();
        System.out.println("startDate" + startDate);


        TransactionReport transactionReportV = new TransactionReport();
        fileStream = IOUtils.toByteArray(multipartFile.getInputStream());
        int index = multipartFile.getOriginalFilename().length();
        String fileOriginalName = multipartFile.getOriginalFilename();
        DateFormat dateFormat = new SimpleDateFormat("MMM yyyy");
        String sStartTime = dateFormat.format(startDate);
        System.out.println("sstarttime" + sStartTime);

        String sDate = " " + sStartTime;
        String month = sStartTime.substring(0, sStartTime.indexOf(" "));
        System.out.println("month" + month);

        System.out.println("sDate" + sDate);


        Date date = new SimpleDateFormat("MMM").parse(month);//put your month name here
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int monthNumber = 0;
        monthNumber = cal.get(Calendar.MONTH);
        monthNumber = monthNumber + 1;

        String[] splited = sStartTime.split(" ");


        String monthYear = splited[1] + "-" + "0" + monthNumber;

        String sLoc = fileType.concat(sDate);


        fileStream = IOUtils.toByteArray(multipartFile.getInputStream());
        FileUpload fileUpload = null;

        String sFilesDirectory = prop.getString("fee.file.folder") + "DFA Backup\\\\" +
            prop.getString("bulk.upload.not");
        File dirFiles = new File(sFilesDirectory);
        dirFiles.mkdirs();

        File sFile = new File(dirFiles, fileOriginalName);
        System.out.println("File completed" + sFile);


        // file and date check
//        TransactionReport transactionReportCheck = transactionReportRepository.findByFileLocationAndIsDeleted(sFile.getPath().toString(), 0);

        List<TransactionReport> transactionReportDateCheck = transactionReportRepository.getByDate(monthYear);
        if (transactionReportDateCheck.size() == 0)
        {

                if (fileOriginalName.contains(sDate)) {


                    writeFile(fileStream, sFile);
                    System.out.println("File completed");
                    transactionReport = readTransactionReportFile(sFile.getPath());
                    if (transactionReport.size() != 0) {
                        for (TransactionReport transactionReport1 : transactionReport) {

                            List<UmbrellaReports> umbrellaReportList = umbrellaReportsRepository.getClientDetails(transactionReport1.getWsAccountCode());
                            if(umbrellaReportList.size()==0)
                            {
                                UmbrellaReports umbrellaReports= new UmbrellaReports();
                                umbrellaReports.setClientName(transactionReport1.getClientName());
                                umbrellaReports.setCode(transactionReport1.getCode());
                                umbrellaReports.setFileLocation(transactionReport1.getFileLocation());
                                umbrellaReports.setIsDeleted(transactionReport1.getIsDeleted());
                                umbrellaReports.setNetAmount(transactionReport1.getNetAmount());
                                umbrellaReports.setWsAccountCode(transactionReport1.getWsAccountCode());
                                umbrellaReports.setWsClientCode(transactionReport1.getWsClientCode());
                                umbrellaReports.setQuantity(transactionReport1.getQuantity());
                                umbrellaReports.setRate(transactionReport1.getRate());
                                umbrellaReports.setSecurityCode(transactionReport1.getSecurityCode());
                                umbrellaReports.setSecurityName(transactionReport1.getSecurityName());
                                umbrellaReports.setTranDate(transactionReport1.getTranDate());
                                umbrellaReportsRepository.save(umbrellaReports);
                            }
                            else

                            {
                                UmbrellaReports umbrellaReports = new UmbrellaReports();
                                double netAmount=0d;
                                float quantity=0f;
                                float rate =0f;
                                System.out.println("umbrella size "+umbrellaReportList.size());
                                for(UmbrellaReports umbrellaReports1:umbrellaReportList) {
                                    System.out.println("umbrella reports  " + umbrellaReports1.getNetAmount()+"/n"+umbrellaReports.getWsAccountCode());
                                }


                                for(int i=0;i<umbrellaReportList.size();i++) {

                                     netAmount = netAmount+transactionReport1.getNetAmount() + umbrellaReportList.get(i).getNetAmount();
                                     quantity = quantity+transactionReport1.getQuantity() + umbrellaReportList.get(i).getQuantity();
                                     rate = rate +transactionReport1.getRate() + umbrellaReportList.get(i).getRate();


                                }
                                umbrellaReports.setClientName(transactionReport1.getClientName());
                                umbrellaReports.setCode(transactionReport1.getCode());
                                umbrellaReports.setFileLocation(transactionReport1.getFileLocation());
                                umbrellaReports.setIsDeleted(transactionReport1.getIsDeleted());
                                umbrellaReports.setWsAccountCode(transactionReport1.getWsAccountCode());
                                umbrellaReports.setWsClientCode(transactionReport1.getWsClientCode());
                                umbrellaReports.setNetAmount(netAmount);
                                umbrellaReports.setQuantity(quantity);
                                umbrellaReports.setRate(rate);
                                umbrellaReports.setSecurityCode(transactionReport1.getSecurityCode());
                                umbrellaReports.setSecurityName(transactionReport1.getSecurityName());
                                umbrellaReports.setTranDate(transactionReport1.getTranDate());

                                umbrellaReportsRepository.save(umbrellaReports);

                            }




                            transactionReportRepository.save(transactionReport1);
                            AIF2Investments aif2Investments = new AIF2Investments();
                            AIF2SeriesMaster aif2SeriesMaster1 = new AIF2SeriesMaster();

                            Product product = productRepository.findByProductName("UNIFI AIF Umbrella Blend Fund - 2");
                            AIF2SeriesMaster aif2SeriesMaster = aif2SeriesMasterRepository.findByClassTypeAndProduct(transactionReport1.getSeries(), product);
                            if (aif2SeriesMaster == null) {
                                aif2SeriesMaster.setClassType(transactionReport1.getSeries());
                                aif2SeriesMaster = aif2SeriesMasterRepository.save(aif2SeriesMaster);
                            }
                            ClientManagement clientManagement1 = clientManagementRepository.findByClientCode(transactionReport1.getWsAccountCode());
                            aif2Investments.setNoOfUnits(transactionReport1.getQuantity());
                            aif2Investments.setUnitPrice(transactionReport1.getRate());
                            aif2Investments.setProduct(product);
                            aif2Investments.setAif2SeriesMaster(aif2SeriesMaster);
                            aif2Investments.setTotOfUnits((transactionReport1.getNetAmount()).floatValue());
                            aif2Investments.setClientManagement(clientManagement1);
                            aif2InvestmentsRepository.save(aif2Investments);
                            transactionReport1.setSeriesId(aif2SeriesMaster.getId());
                            transactionReportRepository.save(transactionReport1);
                        }
                    }
                    transactionReportV.setCode("200");
                    return transactionReportV;
                } else {
                    transactionReportV.setCode("400");
                    System.out.println(fileUpload);
                    return transactionReportV;
                }

        } else {
            transactionReportV.setCode("409");
            System.out.println("trqns" + transactionReportV);
            return transactionReportV;

        }

    }

    public List<TransactionReport> readTransactionReportFile(String path) {
        System.out.println("REEEEEE" + path);

        List<TransactionReport> transactionReportList = new ArrayList<>();
        try {
            FileInputStream inputStream = new FileInputStream(path);
            XSSFWorkbook workbook = new XSSFWorkbook(inputStream);

            XSSFSheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rowIterator = sheet.iterator();
            System.out.println("EEEE");

            int cellIdx = 0;
            int rowNu = 0;
            String distName = null;
            String rName = null;
            String dName = null;
            String code = null;
            String codeSplit = null;
            String codeSplit1 = null;

            while (rowIterator.hasNext()) {
                TransactionReport transactionReport1 = new TransactionReport();
                Row row = rowIterator.next();
                System.out.println("      rowNo" + rowNu);
                if (rowNu > 0) {
                    System.out.println("orndfdf" + rowNu);

                    int schemeId = 0;
                    int rId = 0;
                    int distId = 0;

                    //Ws client id
//                    System.out.println("row.getCell(0).getStringCellValue()"+row.getCell(0).getStringCellValue());
                    Integer wClientId = (int) row.getCell(0).getNumericCellValue();
                    System.out.println("test" + wClientId);
                    transactionReport1.setWsClientCode(wClientId);

                    //Ws client ACC
                    String wClientAccCode = row.getCell(1).getStringCellValue();
                    transactionReport1.setWsAccountCode(wClientAccCode);

                    // Client Name
                    String name = row.getCell(2).getStringCellValue();
                    transactionReport1.setClientName(name);
                    String securityCode1 = row.getCell(10).getStringCellValue();

                    //tran date
                    Date tranDate = row.getCell(3).getDateCellValue();

                    transactionReport1.setTranDate(tranDate);


                    // Security code
                    String securityCode = row.getCell(10).getStringCellValue();
                    transactionReport1.setSecurityCode(securityCode);


                    // Security Name
                    String securityName = row.getCell(11).getStringCellValue();
                    transactionReport1.setSecurityName(securityName);


                    // quantity
                    Float quantity = (float) row.getCell(19).getNumericCellValue();
                    transactionReport1.setQuantity(quantity);

                    // rate
                    Float rate = (float) row.getCell(20).getNumericCellValue();
                    transactionReport1.setRate(rate);


                    // Net amount

//                System.out.println("security Name "+securityName.sp("",2));
                    String output[] = securityName.split("Class");
                   // String rName1[] = output[1].split("-");
                    String className = output[1];

                    System.out.println("class name" + className);
                    String series = "Class" + className;
                    System.out.println("rname lsit" + series);

                    Double netAmount = (double) row.getCell(24).getNumericCellValue();
                    transactionReport1.setNetAmount(netAmount);
                    transactionReport1.setSeries(series);
                    transactionReport1.setCode("200");
                    transactionReportList.add(transactionReport1);

                    System.out.println("row " + rowNu);
                }
                rowNu++;


            }
        } catch (Exception e) {

        }

        return transactionReportList;
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

}
