package com.bcad.application.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "rm_master")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class RelationshipManager extends AbstractAuditingEntity implements Serializable{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "rm_name")
    private String rmName;

    @ManyToOne
    @JoinColumn(name="location_id")
    private Location location;

    @JsonProperty
    @ManyToMany (fetch = FetchType.EAGER)
    @JoinTable(name = "rm_subrm_map",
        joinColumns = @JoinColumn(name = "rm_id", referencedColumnName = "id"),
        inverseJoinColumns = @JoinColumn(name = "sub_rm_id", referencedColumnName = "id"))
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<SubRM> subRMS = new HashSet<>();

    /*@ManyToMany(mappedBy = "relationshipManagers",fetch = FetchType.LAZY)
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<CommissionDefinition> relationshipManagerMap = new HashSet<>();*/

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRmName() {
        return rmName;
    }

    public void setRmName(String rmName) {
        this.rmName = rmName;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Set<SubRM> getSubRMS() {
        return subRMS;
    }

    public void setSubRMS(Set<SubRM> subRMS) {
        this.subRMS = subRMS;
    }

  /*  public RelationshipManager relationshipManagerMap(Set<CommissionDefinition> commissionDefinitions) {
        this.relationshipManagerMap = commissionDefinitions;
        return this;
    }

    public RelationshipManager addRelationshipManagerMap(CommissionDefinition commissionDefinition) {
        this.relationshipManagerMap.add(commissionDefinition);
        commissionDefinition.getRelationshipManagers().add(this);
        return this;
    }

    public RelationshipManager removeRelationshipManagerMap(CommissionDefinition commissionDefinition) {
        this.relationshipManagerMap.remove(commissionDefinition);
        commissionDefinition.getRelationshipManagers().remove(this);
        return this;
    }*/

   /* @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RelationshipManager that = (RelationshipManager) o;
        return Objects.equals(id, that.id) &&
            Objects.equals(rmName, that.rmName) &&
            Objects.equals(location, that.location) &&
            Objects.equals(subRMS, that.subRMS) &&
            Objects.equals(relationshipManagerMap, that.relationshipManagerMap);
    }*/

   /* @Override
    public int hashCode() {
        return Objects.hash(id, rmName, location, subRMS, relationshipManagerMap);
    }*/

   /*@Override
    public String toString() {
        return "RelationshipManager{" +
            "id=" + id +
            ", rmName='" + rmName + '\'' +
            ", location=" + location +
            ", subRMS=" + subRMS +
            ", relationshipManagerMap=" + relationshipManagerMap +
            '}';
    }*/
}

