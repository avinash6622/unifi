package com.bcad.application.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name="role_name_master")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class RoleNameMaster implements Serializable {

    @Id
    @JsonIgnore
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="role_name")
    private String roleName;

    @Column(name="role_create")
    private Boolean roleCreate;

    @Column(name="role_edit")
    private Boolean roleEdit;

    @Column(name="role_view")
    private Boolean roleView;

    @Column(name="role_delete")
    private Boolean roleDelete;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id")
    @JsonBackReference
    private Role roles;

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

    public Role getRoles() {
        return roles;
    }

    public RoleNameMaster roles(Role role) {
        this.roles = role;
        return this;
    }

    public void setRoles(Role roles) {
        this.roles = roles;
    }

    public Boolean getRoleCreate() {
        return roleCreate;
    }

    public void setRoleCreate(Boolean roleCreate) {
        this.roleCreate = roleCreate;
    }

    public Boolean getRoleEdit() {
        return roleEdit;
    }

    public void setRoleEdit(Boolean roleEdit) {
        this.roleEdit = roleEdit;
    }

    public Boolean getRoleView() {
        return roleView;
    }

    public void setRoleView(Boolean roleView) {
        this.roleView = roleView;
    }

    public Boolean getRoleDelete() {
        return roleDelete;
    }

    public void setRoleDelete(Boolean roleDelete) {
        this.roleDelete = roleDelete;
    }

}
