package com.bcad.application.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "sub_rm")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class SubRM extends AbstractAuditingEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="sub_name")
    private String subName;


    @ManyToMany(mappedBy = "subRMS",fetch = FetchType.LAZY)
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<RelationshipManager> subRMMap = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSubName() {
        return subName;
    }

    public void setSubName(String subName) {
        this.subName = subName;
    }

    public Set<RelationshipManager> getSubRMMap() {
        return subRMMap;
    }

    public void setSubRMMap(Set<RelationshipManager> subRMMap) {
        this.subRMMap = subRMMap;
    }

    public SubRM subRMMap(Set<RelationshipManager> relationshipManagers) {
        this.subRMMap = relationshipManagers;
        return this;
    }

    public SubRM addSubRMMap(RelationshipManager relationshipManager) {
        this.subRMMap.add(relationshipManager);
        relationshipManager.getSubRMS().add(this);
        return this;
    }

    public SubRM removeSubRMMap(RelationshipManager relationshipManager) {
        this.subRMMap.remove(relationshipManager);
        relationshipManager.getSubRMS().remove(this);
        return this;
    }
}
