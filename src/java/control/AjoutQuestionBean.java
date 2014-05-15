/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package control;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;
import javax.inject.Named;
import javax.enterprise.context.Dependent;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;
import model.Choix;
import model.Question;
import model.QuestionOuverte;
import model.QuestionQCM;
import model.Questionnaire;
import model.ReponseOuverte;

/**
 *
 * @author thibaut
 */
@ManagedBean
@RequestScoped
public class AjoutQuestionBean {

    @PersistenceContext
    private EntityManager em;

    @Resource
    private UserTransaction ut;

    private String intitule;
    private String choix;
    private ArrayList<String> listchoix = new ArrayList<String>();
    private boolean unique = false;
    private List<Question> questModif = new ArrayList<Question>();

    /**
     * Creates a new instance of AjoutQuestionBean
     */
    public AjoutQuestionBean() {
    }

    public String getIntitule() {
        return intitule;
    }

    public void setIntitule(String intitule) {
        this.intitule = intitule;
    }

    public String getChoix() {
        return choix;
    }

    public void setChoix(String choix) {
        this.choix = choix;
        listchoix.add(choix);
    }

    public void creerQuestionOuverte() throws NotSupportedException, SystemException, RollbackException, HeuristicMixedException, HeuristicRollbackException, IOException {
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        boolean deja = false;
        Questionnaire quest = em.find(Questionnaire.class, (long) Integer.parseInt(request.getParameter("id")));
        Query jQuery = em.createQuery("Select q From Question q");
        List<Question> rList = jQuery.getResultList();

        for (Question q : rList) {
            if (q.getQuestion().equals(intitule)) {
                deja = true;
            }
        }

        if (!deja) {
            QuestionOuverte nouvelle = new QuestionOuverte();
            nouvelle.setQuestion(intitule);
            nouvelle.setType("textarea");
            nouvelle.ajouterQuestionnaire(quest);
            quest.ajouterQuestion(nouvelle);
            ut.begin();
            em.merge(nouvelle);
            em.merge(quest);
            ut.commit();
        }
        
        FacesContext.getCurrentInstance().getExternalContext().redirect("questionnaire-modif.xhtml?id=" + request.getParameter("id"));

    }
    
    public void creerQuestionQCM() throws NotSupportedException, SystemException, RollbackException, SecurityException, HeuristicMixedException, HeuristicRollbackException, IOException {
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        boolean deja = false;
        
        //Récupération du questionnaire
        Questionnaire quest = em.find(Questionnaire.class, (long) Integer.parseInt(request.getParameter("id")));
        
        //Récupération des questions pour vérifier si elle existe déja
        Query jQuery = em.createQuery("Select q From Question q");
        List<Question> rList = jQuery.getResultList();

        QuestionQCM ques = null;
        for (Question q : rList) {
            if (q.getQuestion().equals(intitule) && q instanceof QuestionQCM && q.getType().equals("selectmany")) {
                ques = (QuestionQCM) q;
            }
        }
        
        if(ques == null){
            ques = new QuestionQCM();
            ques.setQuestion(intitule);
            if(this.isUnique()){
                ques.setType("selectone");
            }else{
                ques.setType("selectmany");
            }          
            ques.ajouterQuestionnaire(quest);
            quest.ajouterQuestion(ques);
        }

        
        ArrayList<Choix> alc = new ArrayList<Choix>();
        Query choixQuery = em.createQuery("Select c From Choix c where c.question = :ques");
        choixQuery.setParameter("ques", ques);
        List<Choix> listChoix = choixQuery.getResultList();
        for(String s : listchoix){
            boolean b = false;
            if(!s.equals("")){
                for(Choix c : listChoix){
                    if(c.getReponse().equals(s)){
                        b = true;
                        alc.add(c);
                    }
                }
                if(!b){
                Choix ch = new Choix();
                ch.setReponse(s);
                ch.setQuestion(ques);
                alc.add(ch);
            }
            }

        }
        
        ques.setChoix(alc);
        ut.begin();
        em.merge(ques);
        em.merge(quest);
        ut.commit();
        
         FacesContext.getCurrentInstance().getExternalContext().redirect("questionnaire-modif.xhtml?id=" + request.getParameter("id"));

    }
    
    
    public void supprimerQuestion(long id) throws NotSupportedException, SystemException, RollbackException, HeuristicMixedException, HeuristicRollbackException{
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();

        //Récupération du questionnaire
        Questionnaire quest = em.find(Questionnaire.class, (long) Integer.parseInt(request.getParameter("id")));
        Question q = em.find(Question.class,id);
        q.retirerQuestionnaire((long) Integer.parseInt(request.getParameter("id")));
        quest.retirerQuestion(id);
        ut.begin();
        em.merge(q);
        em.merge(quest);
        ut.commit(); 
    }
    
    public void modifier(){
        
    }

    public boolean isUnique() {
        return unique;
    }

    public void setUnique(boolean unique) {
        this.unique = unique;
    }   
}
