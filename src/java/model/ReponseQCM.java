/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

/**
 *
 * @author thomas
 */
@Entity
public class ReponseQCM implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private QuestionQCM question;
    private String type;
    /**
   * 
   * @element-type Choix
   */
    @OneToMany
    private Collection<Choix>  reponses = new ArrayList<Choix>();
    
    public Collection<Choix> getReponses() {
        return reponses;
    }

    public void setReponses(Collection<Choix> reponses) {
        this.reponses = reponses;
    }
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
        if (!(object instanceof ReponseQCM)) {
            return false;
        }
        ReponseQCM other = (ReponseQCM) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ul.dateroulette.entity.ReponseQCM[ id=" + id + " ]";
    }

    public Question getQuestion() {
        return this.question;
    }

    public void setQuestion(QuestionQCM qcm) {
        this.question = qcm;
    }
    
    public String getType(){
        return this.type;
    }
    
    public void setType(String s){
        this.type = s;
    }
    
}
