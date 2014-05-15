/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
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
public class Question implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String question;
    private String type;

    /**
     * 
     * @element-type Questionnaire
     */
    @OneToMany
    private Collection<Questionnaire>  questionnaires = new ArrayList<Questionnaire>();
    

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public Collection<Questionnaire> getQuestionnaires() {
        return questionnaires;
    }

    public void setQuestionnaires(Collection<Questionnaire> questionnaires) {
        this.questionnaires = questionnaires;
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
        if (!(object instanceof Question)) {
            return false;
        }
        Question other = (Question) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ul.dateroulette.entity.Question[ id=" + id + " ]";
    }

public void ajouterQuestionnaire(Questionnaire quest) {
        boolean b = false;
       // if(){
            for(Questionnaire q : this.questionnaires){
                if(q.getId() == quest.getId()){
                    q = quest;
                    b = true;
                }
            }
       // }
        if(!b){
            this.questionnaires.add(quest);
        }
    }

    public void retirerQuestionnaire(long l) {
        Iterator<Questionnaire> iter = this.getQuestionnaires().iterator();
        while(iter.hasNext()){
            if(iter.next().getId() == l){
                iter.remove();
            }
        } 
    }
    
}
