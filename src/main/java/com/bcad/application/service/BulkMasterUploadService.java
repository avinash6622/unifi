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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import javax.persistence.*;
import org.springframework.transaction.annotation.Transactional;
import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class BulkMasterUploadService {

    @PersistenceContext
    EntityManager entityManager;

    private final DistributorMasterRepository distributorMasterRepository;
    private final DistributorTypeRepository distributorTypeRepository;
    private final DistributorOptionRepository distributorOptionRepository;
    private final RelationshipManagerRepository relationshipManagerRepository;
    private final ProductRepository productRepository;
    private final ClientManagementRepository clientManagementRepository;
    private final PMSClientMasterRepository pmsClientMasterRepository;
    private final CommissionDefinitionRepository commissionDefinitionRepository;
    private final ClientFeeCommissionRepository clientFeeCommissionRepository;

    public BulkMasterUploadService(DistributorMasterRepository distributorMasterRepository, DistributorTypeRepository distributorTypeRepository, DistributorOptionRepository distributorOptionRepository, RelationshipManagerRepository relationshipManagerRepository, ProductRepository productRepository,
                                   ClientManagementRepository clientManagementRepository,PMSClientMasterRepository pmsClientMasterRepository,CommissionDefinitionRepository commissionDefinitionRepository,
                                   ClientFeeCommissionRepository clientFeeCommissionRepository) {
        this.distributorMasterRepository = distributorMasterRepository;
        this.distributorTypeRepository = distributorTypeRepository;
        this.distributorOptionRepository = distributorOptionRepository;
        this.relationshipManagerRepository = relationshipManagerRepository;
        this.productRepository = productRepository;
        this.clientManagementRepository = clientManagementRepository;
        this.pmsClientMasterRepository = pmsClientMasterRepository;
        this.commissionDefinitionRepository = commissionDefinitionRepository;
        this.clientFeeCommissionRepository =clientFeeCommissionRepository;
    }

    private byte[] fileStream;
    private String fileName;
    private Boolean enableSave=true;
    List<MasterLogBean> masterLogBean= new ArrayList<>();
    private ClientManagement clientManagement= new ClientManagement();


    public Boolean getEnableSave() {
        return enableSave;
    }

    public void setEnableSave(Boolean enableSave) {
        this.enableSave = enableSave;
    }

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


    PropertiesConfiguration prop = new PropertiesUtils().getProperties("application.properties");
    private final Logger log = LoggerFactory.getLogger(BulkMasterUploadService.class);

    public ClientManagement uploadMasterFiles(String fileType, MultipartFile multipartFile) throws IOException, Exception {

        int index = multipartFile.getOriginalFilename().lastIndexOf(".");
        setFileName(multipartFile.getOriginalFilename().substring(0, index ));
        //setFileName(multipartFile.getOriginalFilename());
        clientManagement= new ClientManagement();

        fileStream = IOUtils.toByteArray(multipartFile.getInputStream());
        DistributorMaster distMaster;
        DistributorMaster checkName = new DistributorMaster();
        enableSave=true;
        masterLogBean= new ArrayList<>();

        if (fileType.contains(prop.getString("bulk.upload.distributor"))) {
            String sFilesDirectory = prop.getString("fee.file.folder") + "DFA Backup\\\\"
                + prop.getString("bulk.upload.folder");
            File dirFiles = new File(sFilesDirectory);
            dirFiles.mkdirs();

            File sFile = new File(dirFiles, fileName);

            writeFile(fileStream, sFile);
            System.out.println("File completed");

            int iPhysNumOfCells;
            FileInputStream fis = new FileInputStream(sFile.getPath());
            HSSFWorkbook workbook = new HSSFWorkbook(fis);

            HSSFSheet sheet = workbook.getSheetAt(0);
            int iPutNxtDetailsToDB = 0;
            int iTotRowInserted = 0;
            Iterator<Row> rowIterator = sheet.iterator();

            while (rowIterator.hasNext()) {
                distMaster = new DistributorMaster();
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
                            sConCheck = "Distributor Name";
                        if (iPos == 1)
                            sConCheck = "Distributor Type";

                        if (cell.getStringCellValue().trim().equals(sConCheck))
                            iTotConMet++;

                        iPos++;

                        if (iTotConToMeet == iTotConMet) {
                            iPutNxtDetailsToDB = 1;
                            iConCheckNow = 1;
                        }
                    }
                }

                String dName = "";
                String dType = "";
                if (iPhysNumOfCells == 2) {

                    if (iPutNxtDetailsToDB == 1 && iConCheckNow == 0) {

                        while (cellIterator.hasNext()) {
                            Cell cell = cellIterator.next();
                            //System.out.println("Cell Type->"+cell.getCellType());
                            switch (cell.getCellTypeEnum()) {
                                case STRING:
                                    try {
                                        dName = row.getCell(0).getStringCellValue();
                                        dType = row.getCell(1).getStringCellValue();
                                        break;
                                    } catch (NoResultException exception) {
                                        System.out.println(exception);
                                    }
                                default:
                            }
                        }
                        checkName = distributorMasterRepository.findByDistributorName(dName);
                        DistributorType distributorType = distributorTypeRepository.findByDistTypeName(dType);
                        if (checkName != null) {
                            checkName.setDistName(dName);
                            checkName.setDistributorType(distributorType);
                            distributorMasterRepository.save(checkName);
                            /*Query query = entityManager.createNativeQuery("update `dist_master` f set f.dist_name="+dName+" and f.dist_type_id="+distributorType.getId()+" where f.id="+checkName.getId());
                            query.executeUpdate();*/

                        } else {
                            distMaster.setDistName(dName);
                            distMaster.setDistributorType(distributorType);
                            distributorMasterRepository.save(distMaster);
                        }


                    }
                }
            }

        }

        if (fileType.contains(prop.getString("bulk.upload.client"))) {
            String sFilesDirectory = prop.getString("fee.file.folder") + "DFA Backup\\\\"
                + prop.getString("bulk.upload.folder");
            File dirFiles = new File(sFilesDirectory);
            dirFiles.mkdirs();
            File sFile = new File(dirFiles, fileName);

            /*String startDate=fileName.substring(fileName.length() - 8);
            DateFormat dateFormat = new SimpleDateFormat("MMM yyyy");
            String sStartTime = dateFormat.format(startDate);
            DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date dDate = sdf.parse(sStartTime);*/
            //System.out.println(dDate);
            writeFile(fileStream, sFile);
            List<ClientManagement> clientManagementList = new ArrayList<>();


            int iPhysNumOfCells;
            FileInputStream fis = new FileInputStream(sFile.getPath());
            HSSFWorkbook workbook = new HSSFWorkbook(fis);

            HSSFSheet sheet = workbook.getSheetAt(0);
            int iPutNxtDetailsToDB = 0;
            int iTotRowInserted = 0;
            Iterator<Row> rowIterator = sheet.iterator();
            ClientManagement clientMasterDO = new ClientManagement();

            MasterLogBean masterLogs = new MasterLogBean();
            while (rowIterator.hasNext()) {
                clientMasterDO = new ClientManagement();
                masterLogs = new MasterLogBean();
                Row row = rowIterator.next();
                Row row2 = row;
                iTotRowInserted++;
                Iterator<Cell> cellIterator = row.cellIterator();

                int iPos = 0;
                int iTotConToMeet = 4;

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
                            sConCheck = "Client Code";
                        if (iPos == 1)
                            sConCheck = "Client Name";
                        if (iPos == 2)
                            sConCheck = "RM Name";
                        if (iPos == 3)
                            sConCheck = "Distributor Name";

                        if (cell.getStringCellValue().trim().equals(sConCheck))
                            iTotConMet++;

                        iPos++;

                        if (iTotConToMeet == iTotConMet) {
                            iPutNxtDetailsToDB = 1;
                            iConCheckNow = 1;
                        }
                    }
                }

                String cCode = "";
                String cName = "";
                String cdist = "";
                String cRm = "";
                String product="";
                String sOption="";
                ClientManagement clientMast = new ClientManagement();
                PMSClientMaster pmsClientMaster = new PMSClientMaster();
                RelationshipManager rmMasterDO = new RelationshipManager();
                DistributorMaster distributorMasterDO = new DistributorMaster();
                Product productMaster = new Product();
                DistributorOption distributorOption = new DistributorOption();
                int sDist = 0;
                int sRm = 0;

               // if (iPhysNumOfCells == 6) {

                    if (iPutNxtDetailsToDB == 1 && iConCheckNow == 0) {

                        while (cellIterator.hasNext()) {
                            Cell cell = cellIterator.next();
                            cell.setCellType(CellType.STRING);
                        }
                        cCode = row.getCell(0).getStringCellValue();
                        cName = row.getCell(1).getStringCellValue();
                        if(row.getCell(2)!=null)
                        cRm = row.getCell(2).getStringCellValue();
                        if(row.getCell(3)!=null)
                        cdist = row.getCell(3).getStringCellValue();
                        if(row.getCell(4)!=null)
                        product = row.getCell(4).getStringCellValue();
                        if(row.getCell(5)!=null)
                        sOption = row.getCell(5).getStringCellValue();

                        clientMasterDO.setClientCode(cCode);
                        clientMasterDO.setClientName(cName);
                        if(!sOption.equals("")) {
                            clientMasterDO.setDistributorOption(distributorOptionRepository.findByOptionName(sOption.trim()));
                        }
                        clientMasterDO.setProduct(productRepository.findByProductName(product.trim()));

                        if (!cdist.equals("")) {
                            distributorMasterDO = distributorMasterRepository.findByDistributorName(cdist.trim());
                            if (distributorMasterDO == null) {
                                sDist++;
                            } else
                                clientMasterDO.setDistributorMaster(distributorMasterDO);
                        }
                        if (!cRm.equals("")) {
                            rmMasterDO = relationshipManagerRepository.findByRelationName(cRm.trim());
                            if (rmMasterDO == null)
                                sRm++;
                            else
                                clientMasterDO.setRelationshipManager(rmMasterDO);
                        }
                        if(!product.equals("PMS")){
                        clientMast = clientManagementRepository.findByClientCode(cCode);
                        if(clientMast==null)
                        clientManagementList.add(clientMasterDO);
                        }
                        if(product.equals("PMS")){
                            pmsClientMaster=pmsClientMasterRepository.findByClientCode(cCode);
                            if(pmsClientMaster==null)
                            clientManagementList.add(clientMasterDO);}
                        else
                            log.error("Client Code already existed");
                        //clientMasterRepository.save(clientMasterDO);

                        if (((!cdist.equals("")) && (sDist == 1)) || ((!cRm.equals("")) && (sRm == 1))) {

                            log.error
                                ("Distributor Not found " + cdist + ", this " + cCode + " Client code is not saved");
                            masterLogs.setClientCode(cCode);
                            masterLogs.setClientName(cName);
                            masterLogs.setDistName(cdist);
                            masterLogs.setRmName(cRm);
                            if (sDist == 1)
                                masterLogs.settStatus("Distributor Not Found");
                            if (sRm == 1)
                                masterLogs.settStatus("RM Not Found");
                            if (sDist == 1 && sRm == 1)
                                masterLogs.settStatus("RM & Distributor Not Found");
                            masterLogBean.add(masterLogs);
                            setEnableSave(false);
                        }
                    }
                }


          //  }
           saveClient(clientManagementList);
            if(getEnableSave()==false){
                clientManagement.setCode("450");
                clientManagement.setStatus(prop.getString("fee.file.folder") + "DFA Backup\\\\"+
                    prop.getString("bulk.upload.not")+ "\\\\"+fileName);
            }
            if(getEnableSave()==true){
                clientManagement.setCode("420");
            }
        }
        return clientManagement;
    }

    private void saveClient(List<ClientManagement> clientManagementList) throws IOException {
        PMSClientMaster pmsClientMaster;
        if(enableSave){
            for(ClientManagement clientMasterDO:clientManagementList){
                if(!clientMasterDO.getProduct().getProductName().equals("PMS"))
                clientManagementRepository.save(clientMasterDO);
                if(clientMasterDO.getProduct().getProductName().equals("PMS")){
                    Product productBCAD=productRepository.findByProductName("BCAD");
                    clientMasterDO.setProduct(productBCAD);
                    ClientManagement clientManagement=clientManagementRepository.findByClientCode(clientMasterDO.getClientCode());
                    if(clientManagement==null)
                    clientManagementRepository.save(clientMasterDO);
                    PMSClientMaster pmsClientMaster1= pmsClientMasterRepository.findByClientCode(clientMasterDO.getClientCode());
                   if(pmsClientMaster1==null) {
                       pmsClientMaster = new PMSClientMaster();
                       pmsClientMaster.setClientCode(clientMasterDO.getClientCode());
                       pmsClientMaster.setClientName(clientMasterDO.getClientName());
                       pmsClientMaster.setDistributorMaster(clientMasterDO.getDistributorMaster());
                       pmsClientMaster.setRelationshipManager(clientMasterDO.getRelationshipManager());
                       PMSClientMaster result = pmsClientMasterRepository.save(pmsClientMaster);
                       if (result.getDistributorMaster() != null) {
                           DistributorMaster distributorMaster = distributorMasterRepository.findByDistributorName(result.getDistributorMaster().getDistName());
                           Product product = productRepository.findByProductName("PMS");
                           CommissionDefinition commissionDefinition = commissionDefinitionRepository.findCommissions(distributorMaster.getId(), product.getId());

                           ClientFeeCommission clientFeeCommission = new ClientFeeCommission();
                           clientFeeCommission.setPmsClientMaster(result);
                           clientFeeCommission.setBrokerageComm(commissionDefinition.getBrokerageComm());
                           clientFeeCommission.setNavComm(commissionDefinition.getNavComm());
                           clientFeeCommission.setProfitComm(commissionDefinition.getProfitComm());
                           clientFeeCommissionRepository.save(clientFeeCommission);

                       }
                   }
                }
            }
        }
        else{
            masterCreate(masterLogBean);
        }

    }


    FileOutputStream fos = null;
    HSSFWorkbook workBook = null;
    private void masterCreate(List<MasterLogBean> masterLogBean) throws IOException {
        String sFilesDirectory = prop.getString("fee.file.folder") + "DFA Backup\\\\"
            + prop.getString("bulk.upload.not");
        File dirFiles = new File(sFilesDirectory);
        dirFiles.mkdirs();

        String file =sFilesDirectory+"\\\\"+fileName;


        fos= new FileOutputStream(file);
        workBook = new HSSFWorkbook();

        String reportDate = "Distributor";
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
        headingBRSBookRow.createCell(2).setCellValue("Distributor Name");
        headingBRSBookRow.getCell(2).setCellStyle(cs);
        sheet.autoSizeColumn(2);
        headingBRSBookRow.createCell(3).setCellValue("RM Name");
        headingBRSBookRow.getCell(3).setCellStyle(cs);
        sheet.autoSizeColumn(3);

        iRowNo=sheet.getLastRowNum()+2;

        for(MasterLogBean bean:masterLogBean){
            HSSFRow row = sheet.createRow(iRowNo);

            row.createCell(0).setCellValue(bean.getClientName());
            sheet.autoSizeColumn(0);
            row.createCell(1).setCellValue(bean.getClientCode());
            sheet.autoSizeColumn(1);
            row.createCell(2).setCellValue(bean.getDistName());
            sheet.autoSizeColumn(2);
            row.createCell(3).setCellValue(bean.getRmName());
            sheet.autoSizeColumn(3);
            row.createCell(4).setCellValue(bean.gettStatus());
            sheet.autoSizeColumn(4);
            iRowNo++;
        }
        workBook.write(fos);

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

   /* public void uploadTemporaryFiles(String distMaster, MultipartFile multipartFile) throws Exception{

            FileInputStream fis = new FileInputStream("C:\\Users\\girija noah\\Desktop\\Client Options.xls");
            HSSFWorkbook workbook = new HSSFWorkbook(fis);
            HSSFSheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rowIterator = sheet.iterator();

            // Create a DataFormatter to format and get each cell's value as String
            DataFormatter dataFormatter = new DataFormatter();
            // System.out.println("\n\nIterating over Rows and Columns using Iterator\n");

            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                int rowNum = row.getRowNum();
                String code = "";
                String clientName = "";
                // System.out.println(rowNum);
                // System.out.println(sheet.getPhysicalNumberOfRows());

                // Now let's iterate over the columns of the current row
                Iterator<Cell> cellIterator = row.cellIterator();

                while (cellIterator.hasNext()) {
                    Cell cell = cellIterator.next();
                    cell.setCellType(CellType.STRING);
                }
                code = row.getCell(0).getStringCellValue();
                clientName = row.getCell(1).getStringCellValue();

               ClientManagement clientUpdate=clientManagementRepository.findByClientCode(code);
               if(clientUpdate!=null){
                   DistributorOption disType=distributorOptionRepository.findByOptionName(clientName);
                   clientUpdate.setDistributorOption(disType);
                   clientManagementRepository.save(clientUpdate);

                }
                System.out.println(code +"Client ----->");
            }


            workbook.close();


    }*/
}




























