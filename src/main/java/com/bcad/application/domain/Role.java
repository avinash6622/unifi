package com.bcad.application.domain;

import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

@Entity
@Table(name="role_master")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Role extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="role_name")
    private String roleName;

    @OneToMany(mappedBy = "roles",cascade={CascadeType.ALL},orphanRemoval=true)
    @LazyCollection(LazyCollectionOption.FALSE)
    @JsonManagedReference
    private List<RoleNameMaster> roleNameMasters;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public List<RoleNameMaster> getRoleNameMasters() {
        return roleNameMasters;
    }

    public void setRoleNameMasters(List<RoleNameMaster> roleNameMasters) {
        this.roleNameMasters = roleNameMasters;
    }

    /*public Role roleNameMasters(List<RoleNameMaster> roleNameMasters) {
        this.roleNameMasters = roleNameMasters;
        return this;
    }

    public Role addRoleNameMaster(RoleNameMaster roleNameMaster) {
        this.roleNameMasters.add(roleNameMaster);
        roleNameMaster.setRoles(this);
        return this;
    }

    public Role removeRoleNameMaster(RoleNameMaster roleNameMaster) {
        this.roleNameMasters.remove(roleNameMaster);
        roleNameMaster.setRoles(null);
        return this;
    }*/

}

