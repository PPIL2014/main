/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import model.Contact.Type;

/**
 *
 * @author Romain
 */
@Entity
public class FAQ implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    
    private String questionFAQ;
    
    private String reponseFAQ;
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getQuestionFAQ() {
        return questionFAQ;
    }

    public void setQuestionFAQ(String questionFAQ) {
        this.questionFAQ = questionFAQ;
    }

    public String getReponseFAQ() {
        return reponseFAQ;
    }

    public void setReponseFAQ(String reponseFAQ) {
        this.reponseFAQ = reponseFAQ;
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
        if (!(object instanceof FAQ)) {
            return false;
        }
        FAQ other = (FAQ) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "model.FAQ[ id=" + id + " ]";
    }
    
}
