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
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.persistence.Basic;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * The SHADER_DEMO table entity class. Encapsulates all data pertaining to a demo.
 * @author Reuben Steenekamp
 */
@Entity
@Table(name = "SHADER_DEMO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ShaderDemo.search", query = "SELECT s FROM ShaderDemo s WHERE (LOWER(s.title) LIKE CONCAT('%',LOWER(:q),'%') OR EXISTS (SELECT t FROM s.tags t WHERE t in :qWs)) AND (s.creator = :account OR s.availability > 0)"),
    @NamedQuery(name = "ShaderDemo.findAll", query = "SELECT s FROM ShaderDemo s"),
    @NamedQuery(name = "ShaderDemo.findById", query = "SELECT s FROM ShaderDemo s WHERE s.id = :id"),
    @NamedQuery(name = "ShaderDemo.findByTitle", query = "SELECT s FROM ShaderDemo s WHERE s.title = :title"),
    @NamedQuery(name = "ShaderDemo.findByDateCreated", query = "SELECT s FROM ShaderDemo s WHERE s.dateCreated = :dateCreated"),
    @NamedQuery(name = "ShaderDemo.findByCode", query = "SELECT s FROM ShaderDemo s WHERE s.code = :code"),
    @NamedQuery(name = "ShaderDemo.findByAvailability", query = "SELECT s FROM ShaderDemo s WHERE s.availability = :availability")})
public class ShaderDemo implements Serializable {
    private static final long serialVersionUID = 1L;
    public static final int PRIVATE = 0;
    public static final int PUBLIC = 1;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "TITLE")
    private String title;
    @Basic(optional = false)
    @Column(name = "DATE_CREATED")
    @Temporal(TemporalType.DATE)
    private Date dateCreated;
    @Basic(optional = false)
    @Column(name = "CODE")
    private String code;
    @Basic(optional = false)
    @Column(name = "AVAILABILITY")
    private int availability;
    @ManyToMany(mappedBy = "delights", fetch = FetchType.EAGER)
    private Set<Account> delighted;
    @ManyToMany(mappedBy = "visits", fetch = FetchType.EAGER)
    private Set<Account> visitors;
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
            name="TAG",
            joinColumns=@JoinColumn(name="SHADER_DEMO")
    )
    @Column(name="TAG_NAME")
    private Set<String> tags;
    @JoinColumn(name = "CREATOR", referencedColumnName = "ID")
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private Account creator;

    public ShaderDemo() {
    }

    public ShaderDemo(Integer id) {
        this.id = id;
    }

    public ShaderDemo(Integer id, String title, Date dateCreated, String code, int availability) {
        this.id = id;
        this.title = title;
        this.dateCreated = dateCreated;
        this.code = code;
        this.availability = availability;
    }

    

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ShaderDemo)) {
            return false;
        }
        ShaderDemo other = (ShaderDemo) object;
        if ((this.getId() == null && other.getId() != null) || (this.getId() != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return getTitle();
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
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * @param title the title to set
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * @return the creation date
     */
    public Date getDateCreated() {
        return dateCreated;
    }

    /**
     * @param dateCreated the creation date to set
     */
    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    /**
     * @return the code
     */
    public String getCode() {
        return code;
    }

    /**
     * @param code the code to set
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * @return the availability
     */
    public int getAvailability() {
        return availability;
    }

    /**
     * @param availability the availability to set
     */
    public void setAvailability(int availability) {
        this.availability = availability;
    }

    /**
     * @return the set of accounts delighted by the shader
     */
    public Set<Account> getDelighted() {
        return delighted;
    }

    /**
     * @param delighted the set of accounts delighted by the shader to set
     */
    public void setDelighted(Set<Account> delighted) {
        this.delighted = delighted;
    }

    /**
     * @return the visitors to the shader
     */
    public Set<Account> getVisitors() {
        return visitors;
    }

    /**
     * @param visitors the visitors to set
     */
    public void setVisitors(Set<Account> visitors) {
        this.visitors = visitors;
    }

    /**
     * @return the tags
     */
    public Set<String> getTags() {
        return tags;
    }

    /**
     * @param tags the tags to set
     */
    public void setTags(Set<String> tags) {
        this.tags = tags;
    }

    /**
     * @return the creator
     */
    public Account getCreator() {
        return creator;
    }

    /**
     * @param creator the creator to set
     */
    public void setCreator(Account creator) {
        this.creator = creator;
    }
    
}
