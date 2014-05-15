/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package control;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import javax.annotation.Resource;
import javax.inject.Named;
import javax.enterprise.context.Dependent;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.ViewScoped;
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
public class ModifierQuestionBean {

    @PersistenceContext
    private EntityManager em;

    @Resource
    private UserTransaction ut;

    private String intitule;
    private String choix = "";
    private ArrayList<String> listchoix = new ArrayList<String>();
    
    private boolean effacer = false;
    private boolean type;
    private boolean modifier = false;
    private List<Choix> listechoix;
    private List<Choix> choixdelete = new ArrayList<Choix>();
    private List<Choix> listnewchoix = new ArrayList<Choix>();

    /**
     * Creates a new instance of AjoutQuestionBean
     */
    public ModifierQuestionBean() {
    }

    public String getIntitule() {
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        return em.find(Question.class, (long)Integer.parseInt(request.getParameter("id"))).getQuestion();
    }

    public void setIntitule(String intitule) {
        this.intitule = intitule;
    }

    public String getChoix() {
        return choix;
    }

    public void setChoix(String choix) {
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        Question q = em.find(Question.class, (long)Integer.parseInt(request.getParameter("id")));
        if(!choix.equals("")){
            Choix c = new Choix();
            c.setReponse(choix);
            c.setQuestion((QuestionQCM) q);
            listnewchoix.add(c);
        }

    }

    public void modifier() throws NotSupportedException, SystemException, RollbackException, HeuristicMixedException, HeuristicRollbackException, IOException{
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        Question q = em.find(Question.class, (long)Integer.parseInt(request.getParameter("id")));
        q.setQuestion(intitule);
        
        if(this.modifier){
            if(q.getType().equals("selectone")){
                q.setType("selectmany");
            }else if(q.getType().equals("selectmany")){
                q.setType("selectone");
            }
        }

        ut.begin();
        if(!this.listnewchoix.isEmpty()){
            for(Choix c : this.listnewchoix){
                em.merge(c);
                ((QuestionQCM) q).ajouterChoix(c);
            }
            //Query query = em.createQuery("DELETE FROM Country c WHERE c.population < :p");
           // int deletedCount = query.setParameter(p, 100000).executeUpdate();
        }
        

        em.merge(q);
        ut.commit();
        
        FacesContext.getCurrentInstance().getExternalContext().redirect("questionnaire-modif.xhtml?id=" + request.getParameter("quest"));

    }
    
    public String getType(){
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        return em.find(Question.class, (long)Integer.parseInt(request.getParameter("id"))).getType();
    }
    
    public void setType(){
     
    }
    
    public boolean getEffacer(){
        return this.effacer;
    }
    
    public void setEffacer(boolean b){

        this.effacer = b;
    }

    public List<Choix> getListechoix() {       
       HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
       Question q = em.find(Question.class, (long)Integer.parseInt(request.getParameter("id")));
       return (List<Choix>) ((QuestionQCM) q).getChoix();
    }
    
    public void supprimerChoix() throws NotSupportedException, SystemException, RollbackException, HeuristicMixedException, HeuristicRollbackException{
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        Question q = em.find(Question.class, (long)Integer.parseInt(request.getParameter("id")));
        Choix c = em.find(Choix.class, (long)Integer.parseInt(request.getParameter("idchoix")));
    
        if(!estSup(c.getId())){
            c.retirerQuestion();
            ((QuestionQCM) q).retirerChoix(c); 
            this.choixdelete.add(c);
        }

        ut.begin();
        em.merge(c);
        em.merge(q);
        ut.commit();
    }
    
    public void setListechoix(List<Choix> listechoix) {
        //this.listechoix = listechoix;
    }
    
    public boolean estSup(long id){
        for(Choix c : this.choixdelete){
            if(id == c.getId()){
                return true;
            }
        }
        return false;
    }
    
    public void annulerSupprimerChoix(){
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        long id = (long)Integer.parseInt(request.getParameter("idchoix"));
        
        Iterator<Choix> iter = this.choixdelete.iterator();
        while(iter.hasNext()){
            if(iter.next().getId() == id){
                iter.remove();
            }
        } 
    }

    public List<Choix> getListnewchoix() {
        return listnewchoix;
    }

    public void setListnewchoix(List<Choix> listnewchoix) {
        this.listnewchoix = listnewchoix;
    }

    public boolean isModifier() {
        return modifier;
    }

    public void setModifier(boolean modifier) {
        this.modifier = modifier;
    }
    
    
}
