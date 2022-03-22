package com.bcad.application.service;

import com.bcad.application.domain.*;
import com.bcad.application.repository.*;
import com.bcad.application.service.dto.*;
import java.io.*;
import java.util.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional
public class ClientManagementService {
    private final ClientManagementRepository clientManagementRepository;

    public ClientManagementService(ClientManagementRepository clientManagementRepository) {
        this.clientManagementRepository = clientManagementRepository;
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

    public int updateClientCode() {
        System.out.println("File processing...");
        List<ClientManagement> clients = clientManagementRepository.findClientList();
        System.out.println("ClientsLength... " + clients.size());
        int counter = 0;
        List<ClientManagementDTO> clientDTOs = new ArrayList();
        try {
            clientDTOs = readExcelFile();
            System.out.println(clientDTOs.size());
            for (int i = 0; i < clients.size(); i++) {
                String dbClientCode = clients.get(i).getClientCode();
                String dbClientName = clients.get(i).getClientName();
                for (int j = 0; j < clientDTOs.size(); j++) {
                    String fileClientCode = clientDTOs.get(j).getClientCode();
                    String fileClientName = clientDTOs.get(j).getClientName();
                    String fileFullClientCode = clientDTOs.get(j).getFullClientCode();
                    if (dbClientCode.equals(fileClientCode) && dbClientName.equals(fileClientName)) {
                        System.out.println("Client... " + dbClientCode + " : " + fileFullClientCode);
                        clients.get(i).setClientCode(fileFullClientCode);
                        clientManagementRepository.save(clients.get(i));
                        counter++;
                    }
                }
            }


        } catch (Exception e) {
            System.out.println("File Error..." + e.getMessage());
        }
        return counter;
    }

    public List<ClientManagementDTO> readExcelFile() throws IOException {
        System.out.println("File reading...");
        FileInputStream fis = new FileInputStream("/Users/senthilkumar/Desktop/Workspace/Indium/bcad_client.xlsx");
        XSSFWorkbook workbook = new XSSFWorkbook(fis);
        Sheet sheet = workbook.getSheetAt(0);
        Iterator<Row> rowIterator = sheet.iterator();
        boolean result = false;
        Cell nextCell1 = null;
        Cell nextCell2 = null;
        String inputStr1 = "";
        String inputStr2 = "";
        List<ClientManagementDTO> client = new ArrayList();
        while (rowIterator.hasNext()) {
            Row row = rowIterator.next();
            Iterator<Cell> cellIterator = row.cellIterator();
            int iPhysNumOfCells = row.getPhysicalNumberOfCells();
            ClientManagementDTO cli = new ClientManagementDTO();
            String sCode = "";
            while (cellIterator.hasNext()) {
                Cell cell = cellIterator.next();
                nextCell1 = row.getCell(0, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
                nextCell2 = row.getCell(1, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
                inputStr1 = getCellValue(nextCell1).toString().trim();
                inputStr2 = getCellValue(nextCell2).toString().trim();
                String sSplits[] = inputStr1.split("(?<=\\D)(?=\\d)|(?<=\\d)(?=\\D)");
                sCode = sSplits[0].toString();
            }
            cli.setClientCode(sCode);
            cli.setFullClientCode(inputStr1);
            cli.setClientName(inputStr2);
            System.out.println("sCode... " + sCode);
            client.add(cli);
        }
        return client;
    }
}
