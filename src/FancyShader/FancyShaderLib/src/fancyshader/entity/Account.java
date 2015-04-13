/* 
 * Copyright (C) 2014 Reuben Steenekamp
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package fancyshader.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 * The ACCOUNT table entity class. Encapsulates data pertaining to a user account.
 * @author Reuben Steenekamp
 */
@Entity
@Table(name = "ACCOUNT")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Account.findAll", query = "SELECT a FROM Account a"),
    @NamedQuery(name = "Account.findById", query = "SELECT a FROM Account a WHERE a.id = :id"),
    @NamedQuery(name = "Account.findByUsername", query = "SELECT a FROM Account a WHERE a.username = :username"),
    @NamedQuery(name = "Account.findByDateCreated", query = "SELECT a FROM Account a WHERE a.dateCreated = :dateCreated")})
public class Account implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "USERNAME")
    private String username;
    @Basic(optional = false)
    @Column(name = "DATE_CREATED")
    @Temporal(TemporalType.DATE)
    private Date dateCreated;
    @JoinTable(name = "DELIGHTFUL", joinColumns = {
        @JoinColumn(name = "ACCOUNT", referencedColumnName = "ID")}, inverseJoinColumns = {
        @JoinColumn(name = "SHADER_DEMO", referencedColumnName = "ID")})
    @ManyToMany(fetch = FetchType.EAGER)
    private Set<ShaderDemo> delights;
    @JoinTable(name = "VISIT", joinColumns = {
        @JoinColumn(name = "ACCOUNT", referencedColumnName = "ID")}, inverseJoinColumns = {
        @JoinColumn(name = "SHADER_DEMO", referencedColumnName = "ID")})
    @ManyToMany(fetch = FetchType.EAGER)
    private Set<ShaderDemo> visits;
    @Basic(optional = false)
    @Column(name ="AUTH_DETAIL")
    private int authDetail;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "creator", fetch = FetchType.EAGER)
    private Set<ShaderDemo> creations;

    public Account() {
    }

    public Account(Integer id) {
        this.id = id;
    }

    public Account(Integer id, String username, Date dateCreated) {
        this.id = id;
        this.username = username;
        this.dateCreated = dateCreated;
    }

    

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (getId() != null ? getId().hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Account)) {
            return false;
        }
        Account other = (Account) object;
        if ((this.getId() == null && other.getId() != null) || (this.getId() != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "fancyshader.entity.Account[ id=" + getId() + " ]";
    }

    /**
     * @return the id
     */
    public Integer getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * @param username the username to set
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * @return the date created
     */
    public Date getDateCreated() {
        return dateCreated;
    }

    /**
     * @param dateCreated the date created to set
     */
    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    /**
     * @return the set of demos delighted by
     */
    public Set<ShaderDemo> getDelights() {
        return delights;
    }

    /**
     * @param delights the set of demos delighted by to set
     */
    public void setDelights(Set<ShaderDemo> delights) {
        this.delights = delights;
    }

    /**
     * @return the set of demos visited
     */
    public Set<ShaderDemo> getVisits() {
        return visits;
    }

    /**
     * @param visits the set of demos visited to set
     */
    public void setVisits(Set<ShaderDemo> visits) {
        this.visits = visits;
    }

    /**
     * @return the AuthDetail id
     */
    public int getAuthDetail() {
        return authDetail;
    }

    /**
     * @param authDetail the AuthDetail id to set
     */
    public void setAuthDetail(int authDetail) {
        this.authDetail = authDetail;
    }

    /**
     * @return the set of demos created by the account
     */
    public Set<ShaderDemo> getCreations() {
        return creations;
    }

    /**
     * @param creations the set of demos created by the account to set
     */
    public void setCreations(Set<ShaderDemo> creations) {
        this.creations = creations;
    }
    
}
