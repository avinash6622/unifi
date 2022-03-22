package com.bcad.application.web.rest;

import com.bcad.application.domain.*;
import com.bcad.application.repository.DistributorMasterRepository;
import com.bcad.application.security.SecurityUtils;
import com.bcad.application.service.BulkMasterUploadService;
import com.bcad.application.web.rest.util.HeaderUtil;
import com.bcad.application.web.rest.util.PaginationUtil;
import com.codahale.metrics.annotation.Timed;
import org.apache.commons.lang3.StringUtils;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class DistributorMasterResource {

    private final Logger log = LoggerFactory.getLogger(DistributorMasterResource.class);

    private final DistributorMasterRepository distributorMasterRepository;
    private final UserResource userResource;
    private final BulkMasterUploadService bulkMasterUploadService;

    public DistributorMasterResource(DistributorMasterRepository distributorMasterRepository, UserResource userResource,
                                     BulkMasterUploadService bulkMasterUploadService) {
        this.distributorMasterRepository = distributorMasterRepository;
        this.userResource = userResource;
        this.bulkMasterUploadService = bulkMasterUploadService;
    }

    @PersistenceContext
    EntityManager entityManager;

    @PostMapping("/distributor")
    @Timed
    public ResponseEntity<DistributorMaster> createDistributor(@RequestBody DistributorMaster distributorMaster) throws URISyntaxException {
        log.debug("REST request to save User : {}", distributorMaster);

        DistributorMaster result = distributorMasterRepository.save(distributorMaster);

        return ResponseEntity.created(new URI("/api/distributor/" + result.getId().toString()))
            .headers(HeaderUtil.createAlert("A Distributor is created with identifier " + result.getId().toString(), result.getId().toString()))
            .body(result);
    }

    @PutMapping("/distributor")
    @Timed
    public ResponseEntity<DistributorMaster> updateDistributor(@RequestBody DistributorMaster distributorMaster)
        throws URISyntaxException {

        DistributorMaster result = distributorMasterRepository.save(distributorMaster);

        return ResponseEntity.created(new URI("/api/distributor/" + result.getId().toString()))
            .headers(HeaderUtil.createEntityUpdateAlert("A Distributor is updated with identifier" + result.getId().toString(), result.getId().toString()))
            .body(result);

    }

    @GetMapping("/distributor/{id}")
    @Timed
    public Optional<DistributorMaster> getDistributorMaster(@PathVariable Long id) {
        Optional<DistributorMaster> result = distributorMasterRepository.findById(id);
        return result;

    }

    @DeleteMapping("/distributor/{id}")
    @Timed
    public ResponseEntity<DistributorMaster> deleteDistributorMaster(@PathVariable Long id) {
        log.debug("REST request to delete DistributorMaster: {}", id);
        Optional<DistributorMaster> result = distributorMasterRepository.findById(id);
        distributorMasterRepository.deleteById(id);
        return ResponseEntity.ok().headers(HeaderUtil.createAlert("A DistributorMaster is deleted with identifier " + id, id.toString())).build();
    }

    @GetMapping("/distributors")
    @Timed
    public ResponseEntity<List<DistributorMaster>> getAllDistributor(Pageable pageable) {
        Optional<User> user = userResource.getCurrentUse(SecurityUtils.getCurrentUserLogin().get());
        Page<DistributorMaster> page = null;
        if (user.get().getRelationshipManager() != null) {
            page = distributorMasterRepository.findByRelationshipManager(user.get().getRelationshipManager(), pageable);
        } else {
            page = distributorMasterRepository.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/distributor/");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }


    @GetMapping("/distributors-all")
    @Timed
    public List<DistributorMaster> getAllDistributors() {
        List<DistributorMaster>  distributorMasters= distributorMasterRepository.findAll();
        System.out.println(distributorMasters.size());
        return distributorMasters;
    }

    @PostMapping("/dist-master-search")
    @Timed
    public List<DistributorMaster> postDistributor(@RequestBody DistributorMaster distributorMaster) {
      /*  List<String> distName = new ArrayList<>();
        List<Long> rm = new ArrayList<>();
        List<String> typeModel = new ArrayList<>();
        if(distributorName==null || distributorName.size()==0)
            distName.add(0);*/

        List<DistributorMaster> result = distributorMasterRepository.findByDistName(distributorMaster.getDistName());
        return result;

    }

    @PostMapping("/bulkupload")
    @Timed
    public ResponseEntity<DistributorMaster> bulkDistributorMaster(@RequestParam(name = "distName") String distMaster,
                                                                   @RequestParam(name = "fileUpload") MultipartFile multipartFile)
        throws URISyntaxException, MissingServletRequestParameterException, IOException, Exception {

        bulkMasterUploadService.uploadMasterFiles(distMaster, multipartFile);

        return null;
    }

    @GetMapping("/report/dist-masters")
    @Timed
    public List<DistributorMaster> getDistributors() {
        List<DistributorMaster> distributorMasters=new ArrayList<>();
        Optional<User> user = userResource.getCurrentUse(SecurityUtils.getCurrentUserLogin().get());
        if (user.get().getRelationshipManager() != null) {
            distributorMasters =distributorMasterRepository.findRelationshipManager(user.get().getRelationshipManager().getId());
        }
        if(user.get().getDistributorMaster()!=null)
        {
            distributorMasters = distributorMasterRepository.findByDistName(user.get().getDistributorMaster().getDistName());
        }
        if(user.get().getRelationshipManager() == null && user.get().getDistributorMaster() ==null){
            distributorMasters = distributorMasterRepository.findAll();
        }
        return distributorMasters;
    }
    @PostMapping("/dist-search-all")
    @Timed
    public List<DistributorMaster> searchDistributor(@RequestBody ClientManagementSearch clientManagementSearch) {
        String sql="";
        if(clientManagementSearch.getDistType()!=null && clientManagementSearch.getDistType().size()!=0)
            sql="dist_type_id in("+ StringUtils.join(clientManagementSearch.getDistType(),',')+")";
        if(clientManagementSearch.getClientNames()!=null && clientManagementSearch.getClientNames().size()!=0)
            sql=(!sql.equals("")?sql.concat(" and dist_name in("+String.join(",", clientManagementSearch.getClientNames().stream()
                .map(name -> ("'" + name + "'")).collect(Collectors.toList()))+")"):"dist_name in" +
                "("+String.join(",", clientManagementSearch.getClientNames().stream()
                .map(name -> ("'" + name + "'"))
                .collect(Collectors.toList()))+")");
        if(clientManagementSearch.getDistModelType()!=null && clientManagementSearch.getDistModelType().size()!=0)
            sql=(!sql.equals("")?sql.concat(" and dist_model_type in("+String.join(",", clientManagementSearch.getDistModelType().stream()
                .map(name -> ("'" + name + "'")).collect(Collectors.toList()))+")"):"dist_model_type in" +
                "("+String.join(",", clientManagementSearch.getDistModelType().stream()
                .map(name -> ("'" + name + "'"))
                .collect(Collectors.toList()))+")");
        if(clientManagementSearch.getRmId()!=null && clientManagementSearch.getRmId().size()!=0)
            sql=(!sql.equals("")? sql.concat(" and rm_id in("+StringUtils.join(clientManagementSearch.getRmId(),',')+")"):
                " rm_id in("+StringUtils.join(clientManagementSearch.getRmId(),',')+")");

        Query query=entityManager.createNativeQuery("select * from dist_master where "+sql,DistributorMaster.class);

        List<DistributorMaster> result = query.getResultList();
        System.out.println(result);
        return result;

    }

}
