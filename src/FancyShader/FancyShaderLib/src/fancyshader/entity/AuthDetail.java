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
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * The AUTH_DETAIL table entity class. Contains the hashed passwords and salts for the accounts. They are stored 
 * are stored on the one-to-one join table to avoid leaks of password data through the service layer.
 * @author Reuben Steenekamp
 */
@Entity
@Table(name = "AUTH_DETAIL")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "AuthDetail.findAll", query = "SELECT a FROM AuthDetail a"),
    @NamedQuery(name = "AuthDetail.findById", query = "SELECT a FROM AuthDetail a WHERE a.id = :id"),
    @NamedQuery(name = "AuthDetail.findByHashcode", query = "SELECT a FROM AuthDetail a WHERE a.hashcode = :hashcode"),
    @NamedQuery(name = "AuthDetail.findBySalt", query = "SELECT a FROM AuthDetail a WHERE a.salt = :salt")})
public class AuthDetail implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "HASHCODE")
    private String hashcode;
    @Basic(optional = false)
    @Column(name = "SALT")
    private String salt;
    

    public AuthDetail() {
    }

    public AuthDetail(Integer id) {
        this.id = id;
    }

    public AuthDetail(Integer id, String hashcode, String salt) {
        this.id = id;
        this.hashcode = hashcode;
        this.salt = salt;
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
        if (!(object instanceof AuthDetail)) {
            return false;
        }
        AuthDetail other = (AuthDetail) object;
        if ((this.getId() == null && other.getId() != null) || (this.getId() != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "fancyshader.entity.AuthDetail[ id=" + getId() + " ]";
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
     * @return the password hashcode
     */
    public String getHashcode() {
        return hashcode;
    }

    /**
     * @param hashcode the password hashcode to set
     */
    public void setHashcode(String hashcode) {
        this.hashcode = hashcode;
    }

    /**
     * @return the salt
     */
    public String getSalt() {
        return salt;
    }

    /**
     * @param salt the salt to set
     */
    public void setSalt(String salt) {
        this.salt = salt;
    }
    
}
