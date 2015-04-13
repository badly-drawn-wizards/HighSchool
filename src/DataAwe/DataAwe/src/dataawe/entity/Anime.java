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
package dataawe.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Reuben Steenekamp
 */
@Entity
@Table(name = "ANIME")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Anime.findAll", query = "SELECT a FROM Anime a"),
    @NamedQuery(name = "Anime.findById", query = "SELECT a FROM Anime a WHERE a.id = :id"),
    @NamedQuery(name = "Anime.findByTitle", query = "SELECT a FROM Anime a WHERE a.title = :title"),
    @NamedQuery(name = "Anime.findByAired", query = "SELECT a FROM Anime a WHERE a.aired = :aired"),
    @NamedQuery(name = "Anime.findByDubbed", query = "SELECT a FROM Anime a WHERE a.dubbed = :dubbed"),
    @NamedQuery(name = "Anime.findByRating", query = "SELECT a FROM Anime a WHERE a.rating = :rating")})
public class Anime implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "TITLE")
    private String title = "";
    @Basic(optional = false)
    @Column(name = "AIRED")
    @Temporal(TemporalType.DATE)
    private Date aired = null;
    @Basic(optional = false)
    @Column(name = "DUBBED")
    private boolean dubbed = false;
    @Basic(optional = false)
    @Column(name = "RATING")
    private float rating = 0f;
    @JoinColumn(name = "GENRE_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private Genre genre = null;

    public Anime() {
    }

    public Anime(Integer id) {
        this.id = id;
    }

    public Anime(Integer id, String title, Date aired, boolean dubbed, float rating) {
        this.id = id;
        this.title = title;
        this.aired = aired;
        this.dubbed = dubbed;
        this.rating = rating;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getAired() {
        return aired;
    }

    public void setAired(Date aired) {
        this.aired = aired;
    }

    public boolean getDubbed() {
        return dubbed;
    }

    public void setDubbed(boolean dubbed) {
        this.dubbed = dubbed;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public Genre getGenre() {
        return genre;
    }

    public void setGenre(Genre genre) {
        this.genre = genre;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Anime)) {
            return false;
        }
        Anime other = (Anime) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return getTitle();
    }
    
}
