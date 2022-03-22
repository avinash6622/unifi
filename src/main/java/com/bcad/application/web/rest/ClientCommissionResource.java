package com.bcad.application.web.rest;

import com.bcad.application.domain.*;
import com.bcad.application.repository.ClientCommissionRepository;
import com.bcad.application.repository.ClientManagementRepository;
import com.bcad.application.repository.CommissionDefinitionRepository;
import com.bcad.application.web.rest.util.HeaderUtil;
import com.bcad.application.web.rest.util.PaginationUtil;
import com.codahale.metrics.annotation.Timed;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class ClientCommissionResource {

    private final Logger log = LoggerFactory.getLogger(ClientCommissionResource.class);


    private final ClientManagementRepository clientManagementRepository;
    private final ClientCommissionRepository clientCommissionRepository;
    private final CommissionDefinitionRepository commissionDefinitionRepository;

    public ClientCommissionResource(ClientManagementRepository clientManagementRepository, ClientCommissionRepository clientCommissionRepository, CommissionDefinitionRepository commissionDefinitionRepository) {
        this.clientManagementRepository = clientManagementRepository;
        this.clientCommissionRepository = clientCommissionRepository;
        this.commissionDefinitionRepository = commissionDefinitionRepository;
    }

    @PersistenceContext
    EntityManager entityManager;

    @PostMapping("/client_code")
    @Timed
    public ResponseEntity<?> createClientCommission(@RequestParam(name = "clientCommission") MultipartFile multipartFile)
        throws URISyntaxException, MissingServletRequestParameterException, IOException, Exception {

        //List<ClientCommission> clientCommissionList = new ArrayList<ClientCommission>();
        List<String> sheetdata = new ArrayList();
        ClientManagement clientManagement = new ClientManagement();
        HSSFWorkbook hwb = new HSSFWorkbook(multipartFile.getInputStream());
        HSSFSheet sheet = hwb.getSheetAt(0);
        HSSFRow row;
        int i = 0;
        String clientcode = null;
        int rownu = 0;
        Iterator rows = sheet.rowIterator();
        while (rows.hasNext()) {
            row = (HSSFRow) rows.next();

            Iterator cells = row.cellIterator();
            if (rownu > 0) {
                List<String> data = new ArrayList();

                while (cells.hasNext()) {

                    HSSFCell cell = (HSSFCell) cells.next();
                    cell.setCellType(CellType.STRING);
                    System.out.println("cell type " + cell.getCellTypeEnum());
                    String clientCode = cell.getStringCellValue();

                    sheetdata.add(clientCode);

                }
            }
            rownu++;
        }

        for (String cm : sheetdata) {
            String sSplits[] = cm.split("(?<=\\D)(?=\\d)|(?<=\\d)(?=\\D)");
            String sCode = sSplits[0];

            clientManagement = clientManagementRepository.findByClientCode(sCode);
            System.out.println("client mngment:" + clientManagement);
            if (clientManagement != null) {
                clientManagement.setClientCode(cm);
                clientManagementRepository.save(clientManagement);
            }
            else{
                System.out.println("client codes not yet updated"+ cm);
            }

        }


       /* else{


            if (clientManagement != null) {
                if (clientManagement.getClientCode() != null) {
                    System.out.println("cm:" + cm);
                    clientManagement.setClientCode(cm);
                    clientManagementRepository.save(clientManagement);

                }
            }
*/
        System.out.println(clientManagement.getClientCode());
        return ResponseEntity.created(new URI("/api/client_code/" + clientManagement.getId().toString()))
            .headers(HeaderUtil.createAlert("A Clientmanagement is created with identifier " + clientManagement.getId().toString(), clientManagement.getId().toString()))
            .body(clientManagement);
    }


    @PostMapping("/client_commission")
    @Timed
    public ResponseEntity<?> createClientCommissionUpdate(@RequestParam(name = "clientCommissionUpdate") MultipartFile multipartFile)
        throws URISyntaxException, MissingServletRequestParameterException, IOException, Exception {

        //List<ClientCommission> clientCommissionList = new ArrayList<ClientCommission>();
        List<String> sheetdata = new ArrayList();
        ClientManagement clientManagement = new ClientManagement();
        ClientCommission clientCommission = new ClientCommission();
        HSSFWorkbook hwb = new HSSFWorkbook(multipartFile.getInputStream());
        HSSFSheet sheet = hwb.getSheetAt(0);
        HSSFRow row;
        int i = 0;
        String clientcode = null;
        int rownu = 0;
        Iterator rows = sheet.rowIterator();
        while (rows.hasNext()) {
            row = (HSSFRow) rows.next();

            Iterator cells = row.cellIterator();
            if (rownu > 0) {
                List<String> data = new ArrayList();

                while (cells.hasNext()) {

                    HSSFCell cell = (HSSFCell) cells.next();
                    cell.setCellType(CellType.STRING);
                    System.out.println("cell type " + cell.getCellTypeEnum());
                    String clientCode = cell.getStringCellValue();

                    sheetdata.add(clientCode);

                }
            }
            rownu++;
        }
        List<Long> datas = new ArrayList();
        List<String> datalist = new ArrayList();
        List<String> datalists = new ArrayList();
        long distId = 0l;
        for (String cm : sheetdata) {
            String code = cm;
            clientManagement = clientManagementRepository.findByClientCode(cm);
            if (clientManagement != null) {
//                clientCommission.setClientId(clientManagement);
//                clientCommission.setProfitComm((float) 40);
//                clientCommission.setNavComm((float) 40);
//                clientCommissionRepository.save(clientCommission);
                if (clientManagement.getDistributorMaster() != null && clientManagement.getProduct() != null) {
                    List<CommissionDefinition> commissionDefinitionList = commissionDefinitionRepository.findOption3Commission(distId, clientManagement.getProduct().getId());
                    if (commissionDefinitionList != null) {
                        System.out.println("clientcode" + clientManagement.getClientCode());
                        int invest = (clientManagement.getSlab().equals("OLD") ? 0 : 1);
                        CommissionDefinition commissionDefinition = commissionDefinitionRepository.getPMSInvestmentDateCalc(clientManagement.getDistributorMaster().getId(), invest, clientManagement.getProduct().getId());
                        if (commissionDefinition != null) {
                            clientCommission.setNavComm(commissionDefinition.getNavComm());
                            clientCommission.setProfitComm(commissionDefinition.getProfitComm());
                            clientCommission.setClientId(clientManagement);
                            System.out.println("upload data");
                            clientCommissionRepository.save(clientCommission);

                        }
                    }
                }
            }
        }


        return ResponseEntity.created(new URI("/api/client_commission" + clientCommission.getId().toString()))
            .headers(HeaderUtil.createAlert("A Clientmanagement is created with identifier " + clientCommission.getId().toString(), clientCommission.getId().toString()))
            .body(clientCommission);
    }

    @PostMapping("/client-comm")
    @Timed
    public ResponseEntity<ClientCommission> createClientFeeCommission(@RequestBody ClientCommission clientCommission) throws URISyntaxException {
        log.debug("REST request to save ClientFeeCommission : {}", clientCommission);

        ClientCommission result = clientCommissionRepository.save(clientCommission);

        return ResponseEntity.created(new URI("/api/client-comm/" + result.getClientId().getClientName()))
            .headers(HeaderUtil.createAlert("A ClientFeeCommission is created with identifier " + result.getClientId().getClientName(), result.getClientId().getClientName()))
            .body(result);

    }

    @GetMapping("/client-comm/{id}")
    @Timed
    public Optional<ClientCommission> getClientCommission(@PathVariable Long id) {
        Optional<ClientCommission> result = clientCommissionRepository.findById(id);
        return result;

    }

    @PutMapping("/client-comm")
    @Timed
    public ResponseEntity<ClientCommission> updateClientCommission(@RequestBody ClientCommission clientCommission)
        throws URISyntaxException {

        ClientCommission result = clientCommissionRepository.save(clientCommission);

        return ResponseEntity.created(new URI("/api/client-comm/" + result.getId().toString())).headers(HeaderUtil
            .createAlert("A user is created with identifier " + result.getId().toString(), result.getId().toString()))
            .body(result);
    }


    @DeleteMapping("/client-comm/{id}")
    @Timed
    public ResponseEntity<ClientCommission> deleteClientCommission(@PathVariable Long id) {
        log.debug("REST request to delete AIFClientMaster: {}", id);
        clientCommissionRepository.deleteById(id);
        return ResponseEntity.ok().headers(HeaderUtil.createAlert("A AIFClient is deleted with identifier " + id, id.toString())).build();

    }

    @GetMapping("/client-comm")
    @Timed
    public ResponseEntity<List<ClientCommission>> getAllClientCommission(Pageable pageable) {
        final Page<ClientCommission> page = clientCommissionRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api//client-comm/");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    @PostMapping("/clientComm-search-all")
    @Timed
    public List<ClientCommission> searchClient(@RequestBody ClientManagementSearch clientManagementSearch) {
        String sql = "";
        List<String> clientIds = new ArrayList<>();
        if (clientManagementSearch.getDistMasterId().size() != 0) {
            List<Integer> nCheck = clientManagementRepository.findDistributorCMId(clientManagementSearch.getDistMasterId());
          /*  Query query = entityManager.createNativeQuery("select distinct(id) from pms_client_master where " +
                "dist_id in (" + StringUtils.join(clientManagementSearch.getDistMasterId(), ',') + ")");
            List<Integer> nCheck=query.getResultList();*/
            for (int i = 0; i < nCheck.size(); ++i) {
                clientIds.add(nCheck.get(i).toString());
            }
            //clientIds.clear();
            //   clientIds= Lists.transform(nCheck, number -> (long)(number));
            System.out.println(clientIds);
        }


        if ((clientManagementSearch.getClientCodes().size() != 0) ||
            (clientIds.size() != 0)) {
            if ((clientIds.size() != 0 && clientManagementSearch.getClientCodes().size() == 0) ||
                clientIds.size() == 0 && clientManagementSearch.getClientCodes().size() != 0) {
                clientManagementSearch.getClientCodes().addAll(clientIds);
            } else {
                List<String> intersect = clientManagementSearch.getClientCodes().stream()
                    .filter(clientIds::contains)
                    .collect(Collectors.toList());
                clientManagementSearch.getClientCodes().clear();
                clientManagementSearch.getClientCodes().addAll(intersect);
            }
            sql = "client_id in(" + StringUtils.join(clientManagementSearch.getClientCodes(), ',') + ")";
        }

        Query query = entityManager.createNativeQuery("select * from bcad_client_commision where " + sql, ClientCommission.class);

        List<ClientCommission> result = query.getResultList();
        //System.out.println(result);
        return result;

    }

    @GetMapping("/client-commission-all")
    @Timed
    public List<ClientCommission> getAllClientCommission() {
        List<ClientCommission>  clientCommission= clientCommissionRepository.findAll();
        return clientCommission;
    }

}





