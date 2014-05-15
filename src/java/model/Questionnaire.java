/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package model;

import java.io.Serializable;
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
public class Questionnaire implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String nom;
    /**
     * 
     * @element-type Question
     */
    @OneToMany
    private Collection<Question>  questions;
    
    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public Collection<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(Collection<Question> questions) {
        this.questions = questions;
    }

    public void ajouterQuestion(Question question) {
        boolean b = false;
        for(Question q : this.getQuestions()){
            if(q.getId() == question.getId()){
                q = question;
                b = true;
            }
        }
        
        if(!b){
            this.questions.add(question);
        }
    }

    public void retirerQuestion(Question question) {
        Iterator<Question> iter = this.getQuestions().iterator();
        while(iter.hasNext()){
            if(iter.next().getId() == question.getId()){
                iter.remove();
            }
        }    
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
        if (!(object instanceof Questionnaire)) {
            return false;
        }
        Questionnaire other = (Questionnaire) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ul.dateroulette.entity.Questionnaire[ id=" + id + " ]";
    }

    public Question getQuestion(int countRep) {
        return (Question)this.questions.toArray()[countRep];
    }
    
    public void retirerQuestion(long id) {
        Iterator<Question> iter = this.getQuestions().iterator();
        while(iter.hasNext()){
            if(iter.next().getId() == id){
                iter.remove();
            }
        }        
    }
}
