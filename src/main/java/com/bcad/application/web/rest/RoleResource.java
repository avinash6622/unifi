package com.bcad.application.web.rest;

import com.bcad.application.domain.DistributorOption;
import com.bcad.application.domain.Role;
import com.bcad.application.repository.RoleRepository;
import com.bcad.application.web.rest.util.HeaderUtil;
import com.bcad.application.web.rest.util.PaginationUtil;
import com.codahale.metrics.annotation.Timed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/api")
public class RoleResource {
    private final Logger log = LoggerFactory.getLogger(DistributorOption.class);
    private final RoleRepository roleRepository;

    public RoleResource(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @PostMapping("/role")
    @Timed
    public ResponseEntity<Role> createRole(@RequestBody Role role)
        throws URISyntaxException {
        Role result = roleRepository.save(role);
        return ResponseEntity.created(new URI("/api/role/" + result.getId().toString())).headers(HeaderUtil
            .createAlert("A role is created with identifier " + result.getId().toString(), result.getId().toString()))
            .body(result);
    }


    @GetMapping("/role/{id}")
    @Timed
    public Optional<Role> getRoleID(@PathVariable Long id) throws Exception {
        Optional<Role> result = roleRepository.findById(id);

        return result;

    }
    @PutMapping("/role")
    @Timed
    public ResponseEntity<Role> updateRole(@RequestBody Role role)
        throws URISyntaxException {

        Role result = roleRepository.save(role);

        return ResponseEntity.created(new URI("/api/role/" + result.getId().toString())).headers(HeaderUtil
            .createAlert("A user is created with identifier " + result.getId().toString(), result.getId().toString()))
            .body(result);
    }

    @DeleteMapping("/role/{id}")
    @Timed
    public ResponseEntity<Role> deleteRole(@PathVariable Long id) {
        log.debug("REST request to delete Role: {}", id);
        Optional<Role> result = roleRepository.findById(id);
        roleRepository.deleteById(id);
        return ResponseEntity.ok().headers(HeaderUtil.createAlert("A user is deleted with identifier " + id, id.toString())).build();
    }

    @GetMapping("/roles")
    @Timed
    public ResponseEntity<List<Role>> getAllRole(Pageable pageable) {
        final Page<Role> page = roleRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/role/");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    @GetMapping("/role-all")
    @Timed
    public List<Role> getRole() {
        return roleRepository.findAll();
    }

    @PostMapping("/role-search")
    @Timed
    public Role postDistributor(@RequestBody Role role) {
        Role result = roleRepository.findByRoleName(role.getRoleName());
        return result;
    
    } 
}

