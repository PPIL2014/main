/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

/**
 *
 * @author Romain
 */
@Entity
public class FAQ implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @Id
    @Column(length=700)
    private String questionFAQ;
    
    @Column(length=2500)
    private String reponseFAQ;
    

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
    public String toString() {
        return "FAQ{" + "questionFAQ=" + questionFAQ + ", reponseFAQ=" + reponseFAQ + '}';
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 59 * hash + Objects.hashCode(this.questionFAQ);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final FAQ other = (FAQ) obj;
        if (!Objects.equals(this.questionFAQ, other.questionFAQ)) {
            return false;
        }
        return true;
    }
    
    
    

}
