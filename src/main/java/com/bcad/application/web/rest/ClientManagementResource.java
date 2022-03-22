package com.bcad.application.web.rest;

import com.bcad.application.config.PropertiesUtils;
import com.bcad.application.domain.*;
import com.bcad.application.repository.*;
import com.bcad.application.security.SecurityUtils;
import com.bcad.application.service.BulkMasterUploadService;
import com.bcad.application.service.ClientManagementService;
import com.bcad.application.web.rest.util.HeaderUtil;
import com.bcad.application.web.rest.util.PaginationUtil;
import com.codahale.metrics.annotation.Timed;
import com.google.gson.JsonObject;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.io.FileSystemResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;




@RestController
@RequestMapping("/api")
public class ClientManagementResource {

    private final ClientManagementRepository clientManagementRepository;
    private final DistributorOptionRepository distributorOptionRepository;
    private final UserResource userResource;
    private final ProductRepository productRepository;
    private final BulkMasterUploadService bulkMasterUploadService;
    private final ClientManagementService clientManagementService;
    private final ClientCommissionRepository clientCommissionRepository;
    private final CommissionDefinitionRepository commissionDefinitionRepository;

    private String enableDownload = "";

    public ClientManagementResource(ClientManagementRepository clientManagementRepository, UserResource userResource,
            DistributorOptionRepository distributorOptionRepository, ProductRepository productRepository,
            BulkMasterUploadService bulkMasterUploadService,ClientCommissionRepository clientCommissionRepository,
            ClientManagementService clientManagementService,CommissionDefinitionRepository commissionDefinitionRepository) {
        this.clientManagementRepository = clientManagementRepository;
        this.userResource = userResource;
        this.distributorOptionRepository = distributorOptionRepository;
        this.productRepository = productRepository;
        this.bulkMasterUploadService = bulkMasterUploadService;
        this.clientCommissionRepository = clientCommissionRepository;
        this.clientManagementService = clientManagementService;
        this.commissionDefinitionRepository = commissionDefinitionRepository;
    }

    @PersistenceContext
    EntityManager entityManager;

    DateFormat firstDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");
    PropertiesConfiguration prop = new PropertiesUtils().getProperties("application.properties");


    public String getEnableDownload() {
        return enableDownload;
    }

    public void setEnableDownload(String enableDownload) {
        this.enableDownload = enableDownload;
    }

    @PostMapping("/client")
    @Timed
    public ResponseEntity<?> createClient(@RequestBody ClientManagement clientManagement)
        throws URISyntaxException, ParseException {
        CommissionDefinition commissionDefinition=new CommissionDefinition();

        if(clientManagement.getDistributorMaster()!=null){
            Integer pmsInvest = (firstDateFormat.parse(prop.getString("pms.strategy.date")).after(clientManagement.getAccountopendate())) ? 0 : 1;
            if(clientManagement.getProduct().getProductName().equals("BCAD")) {
                commissionDefinition = commissionDefinitionRepository.getBCADInvestmentDateCalc(clientManagement.getDistributorMaster().getId(), pmsInvest, clientManagement.getProduct().getId());
            }
            if(commissionDefinition==null){
                JsonObject apierror = new JsonObject();
                apierror = new JsonObject();
                apierror.addProperty("error", "470");
                apierror.addProperty("Message",clientManagement.getProduct().getProductName() +"is not mapped with Commission Definition. " +
                    "Add in the CD and again create client");
                return ResponseEntity.ok(apierror.toString());

            }}

        ClientManagement result = clientManagementRepository.save(clientManagement);
        if(result.getDistributorMaster()!=null){
            ClientCommission clientCommission = new ClientCommission();
            clientCommission.setClientId(result);
            clientCommission.setNavComm(commissionDefinition.getNavComm());
            clientCommission.setProfitComm(commissionDefinition.getProfitComm());
            clientCommission.setUpdateRequired(0);
            clientCommissionRepository.save(clientCommission);

        }


        return ResponseEntity.created(new URI("/api/client/" + result.getId().toString()))
                .headers(HeaderUtil.createAlert("A user is created with identifier " + result.getId().toString(),
                        result.getId().toString()))
                .body(result);
    }

    @GetMapping("/clients")
    @Timed
    public ResponseEntity<List<ClientManagement>> getAllClients(Pageable pageable) {
        Optional<User> user = userResource.getCurrentUse(SecurityUtils.getCurrentUserLogin().get());
        Page<ClientManagement> page = null;
        if (user.get().getDistributorMaster() != null) {
            page = clientManagementRepository.findByDistributorMaster(user.get().getDistributorMaster(), pageable);
        } else if (user.get().getRelationshipManager() != null) {
            page = clientManagementRepository.findByRelationshipManager(user.get().getRelationshipManager(), pageable);
        } else {
            page = clientManagementRepository.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/clients/");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    @PostMapping("/clientSearch")
    @Timed
    public ResponseEntity<List<ClientManagement>> getAllClientsSearch(
            @RequestBody List<RelationshipManager> relationshipManagers) {
        List<ClientManagement> result = new ArrayList<>();
        for (RelationshipManager rel : relationshipManagers) {
            result.addAll(clientManagementRepository.findByRelationshipManager(rel));
        }

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/clients/{id}")
    @Timed
    public Optional<ClientManagement> getClient(@PathVariable Long id) {
        Optional<ClientManagement> result = clientManagementRepository.findById(id);
        return result;

    }

    @PutMapping("/client")
    @Timed
    public ResponseEntity<?> updateClient(@RequestBody ClientManagement clientManagement)
        throws URISyntaxException, ParseException {
        CommissionDefinition commissionDefinition= new CommissionDefinition();
        if(clientManagement.getDistributorMaster()!=null){
            Integer pmsInvest = (firstDateFormat.parse(prop.getString("pms.strategy.date")).after(clientManagement.getAccountopendate())) ? 0 : 1;
            if(clientManagement.getProduct().getProductName().equals("BCAD")) {
                commissionDefinition = commissionDefinitionRepository.getBCADInvestmentDateCalc(clientManagement.getDistributorMaster().getId(), pmsInvest, clientManagement.getProduct().getId());
            }
            if(commissionDefinition==null){
                JsonObject apierror = new JsonObject();
                apierror = new JsonObject();
                apierror.addProperty("error", "470");
                apierror.addProperty("Message",clientManagement.getProduct().getProductName() +"is not mapped with Commission Definition. " +
                    "Add in the CD and again create client");
                return ResponseEntity.ok(apierror.toString());

            }}

        ClientManagement result = clientManagementRepository.save(clientManagement);
        if(result.getDistributorMaster()!=null){
            ClientCommission clientCommission =clientCommissionRepository.findClient(result.getClientId());
            if(clientCommission==null) {
                 clientCommission = new ClientCommission();
                clientCommission.setClientId(result);
                clientCommission.setNavComm(commissionDefinition.getNavComm());
                clientCommission.setProfitComm(commissionDefinition.getProfitComm());
                clientCommissionRepository.save(clientCommission);
            }
            else{
                if(clientCommission.getUpdateRequired()==0){
                    clientCommission.setNavComm(commissionDefinition.getNavComm());
                    clientCommission.setProfitComm(commissionDefinition.getProfitComm());
                    clientCommissionRepository.save(clientCommission);

                }
            }


        }

        return ResponseEntity.created(new URI("/api/client/" + result.getId().toString()))
                .headers(HeaderUtil.createAlert("A user is created with identifier " + result.getId().toString(),
                        result.getId().toString()))
                .body(result);
    }

    @DeleteMapping("/client/{id}")
    @Timed
    public Optional<ClientManagement> deleteClient(@PathVariable Long id) {
        clientManagementRepository.deleteById(id);
        return null;
    }

    @GetMapping("/client-product/{product}")
    @Timed
    public List<DistributorOption> Client(@PathVariable Long product) {
        Optional<Product> products = productRepository.findById(product);
        List<DistributorOption> result = distributorOptionRepository.findByProduct(products.get());
        return result;
    }

    @PostMapping("/client-search")
    @Timed
    public List<ClientManagement> postClient(@RequestBody ClientManagement clientManagement) {
        List<ClientManagement> result = clientManagementRepository.findClientCode(clientManagement.getClientCode());
        return result;

    }

    @GetMapping("/client")
    @Timed
    public List<ClientManagement> getclient() {
         Optional<User>  user=userResource.getCurrentUse(SecurityUtils.getCurrentUserLogin().get());
        // if(user.get().getDistributorMaster()!=null){
        // List<ClientManagement>
        // result=clientManagementRepository.findByDistributorMaster(user.get().getDistributorMaster());}
        // else if(user.get().getRelationshipManager()!=null){
        // List<ClientManagement> result=
        // clientManagementRepository.findByRelationshipManager(user.get().getRelationshipManager());}
        // else {
        List<ClientManagement> result = clientManagementRepository.findAll();
        // }
        return result;
    }

    @PostMapping("/clientbulkupload")
    @Timed
    public ResponseEntity<?> bulkClientManagement(@RequestParam(name = "distName") String distMaster,
            @RequestParam(name = "fileUpload") MultipartFile multipartFile)
            throws URISyntaxException, MissingServletRequestParameterException, IOException, Exception {

        ClientManagement result = bulkMasterUploadService.uploadMasterFiles(distMaster, multipartFile);

         System.out.println(result + "news");
        if (result.getCode() != null) {
            if (result.getCode().equals("450")) {
                setEnableDownload(result.getStatus());
                JsonObject apierror = new JsonObject();
                apierror = new JsonObject();
                apierror.addProperty("error", "450");
                apierror.addProperty("message", "file not uploaded");
                return ResponseEntity.ok(apierror.toString());
            }
            if (result.getCode().equals("420")) {
                setEnableDownload(result.getStatus());
                JsonObject apierror = new JsonObject();
                apierror = new JsonObject();
                apierror.addProperty("error", "420");
                apierror.addProperty("message", "file uploaded");
                return ResponseEntity.ok(apierror.toString());
            }
        }
        return ResponseEntity.created(new URI("/api/clientbulkupload/" + result.getId().toString()))
                .headers(HeaderUtil.createAlert("A Bulk Upload is created with identifier " + result.getId().toString(),
                        result.getId().toString()))
                .body(result);
    }

    @GetMapping("/download-bulk-client")
    @Timed
    public ResponseEntity downloadClient() throws Exception {
        System.out.println(enableDownload);
        String filePath = enableDownload;

        File file = new File(filePath);
        System.out.println(file.getName());
        if (file.exists()) {
            return ResponseEntity.ok().header("Content-Disposition", "attachment; filename=" + file.getName())
                    .contentLength(file.length()).lastModified(file.lastModified())
                    .contentType(MediaType.APPLICATION_OCTET_STREAM).body(new FileSystemResource(file));
        }

        return null;
    }

    @GetMapping("/clients-all")
    @Timed
    public List<ClientManagement> getAllClientMasters() {
        List<ClientManagement>  clientManagements= clientManagementRepository.findAll();
        System.out.println(clientManagements.size());
        return clientManagements;
    }

    /*
     * @PostMapping("/temporary")
     *
     * @Timed public ResponseEntity<ClientManagement>
     * bulkClientOptionst(@RequestParam(name = "distName") String distMaster,
     *
     * @RequestParam(name = "fileUpload") MultipartFile multipartFile) throws
     * URISyntaxException, MissingServletRequestParameterException, IOException,
     * Exception {
     *
     * bulkMasterUploadService.uploadTemporaryFiles(distMaster, multipartFile);
     *
     * return null; }
     */

    @PostMapping("/client-search-all")
    @Timed
    public List<ClientManagement> searchClient(@RequestBody ClientManagementSearch clientManagementSearch) {
        String sql="";
        if(clientManagementSearch.getClientCodes()!=null && clientManagementSearch.getClientCodes().size()!=0)
            sql="client_code in("+String.join(",",clientManagementSearch.getClientCodes().stream() .map(clientCode -> ("'" + clientCode + "'")).collect(Collectors.toList()))+")";
        if(clientManagementSearch.getClientNames()!=null && clientManagementSearch.getClientNames().size()!=0)
            sql=(!sql.equals("")?sql.concat(" and client_name in("+String.join(",", clientManagementSearch.getClientNames().stream()
                .map(name -> ("'" + name + "'")).collect(Collectors.toList()))+")"):"client_name in" +
                "("+String.join(",", clientManagementSearch.getClientNames().stream()
                .map(name -> ("'" + name + "'"))
                .collect(Collectors.toList()))+")");
        if(clientManagementSearch.getDistMasterId()!=null && clientManagementSearch.getDistMasterId().size()!=0)
            sql=(!sql.equals("")? sql.concat(" and dist_master_id in("+StringUtils.join(clientManagementSearch.getDistMasterId(),',')+")") :
                " dist_master_id in("+StringUtils.join(clientManagementSearch.getDistMasterId(),',')+")");

        if(clientManagementSearch.getRmId()!=null && clientManagementSearch.getRmId().size()!=0)
            sql=(!sql.equals("")? sql.concat(" and rm_id in("+StringUtils.join(clientManagementSearch.getRmId(),',')+")"):
                " rm_id in("+StringUtils.join(clientManagementSearch.getRmId(),',')+")");

      Query query=entityManager.createNativeQuery("select * from bcad_client_master_cd where "+sql,ClientManagement.class);

        List<ClientManagement> result = query.getResultList();
        System.out.println(result);
        return result;

    }

    @GetMapping("/clients-mapping")
    public String clientMapping() throws IOException {
        int records = 0;
        try {
            records = clientManagementService.updateClientCode();
            System.out.println(records);
        } catch (Exception e) {
            System.out.println("Not processed");
        }


        return records + " updated";
    }
}
