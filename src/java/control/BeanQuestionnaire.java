/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package control;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.annotation.Resource;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
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
import  model.ReponseOuverte;
import model.ReponseQCM;
import model.Utilisateur;

/**
 *
 * @author Thomas
 */
@ManagedBean
@RequestScoped
public class BeanQuestionnaire {
    @PersistenceContext 
    private EntityManager em;

    @Resource 
    private UserTransaction ut;
    public String newQuest;
    public String reponse = "";
    public List<String> reponsesQcm = new ArrayList<String>();
    public int i = 0;
    public String nomQuest;
    public Questionnaire q;
    public int idQuest;
    public int countRep = 0;
    public String pseudo = null;
    public Utilisateur util;
    private ArrayList<ReponseQCM> repQCM = new ArrayList<ReponseQCM>();
    private ArrayList<ReponseOuverte> repOuvertes = new ArrayList<ReponseOuverte>();
    public int nbQuestionsQuestionnaire;
public boolean vide = false;

    /**
     * Creates a new instance of BeanQuestionnaire
     */
    public BeanQuestionnaire() {

    }
    
    public void ajout() throws IOException, NotSupportedException, SystemException, RollbackException, HeuristicRollbackException, HeuristicMixedException {
        ut.begin();
        for(ReponseOuverte ro : repOuvertes){
            util.ajouterReponseOuverte(ro);
            em.merge(ro);
            if(ro.getId() == null){

            }
        }
        for(ReponseQCM rq : repQCM){
                if(rq.getType().equals("selectone")){
                   if(rq.getReponses().toArray()[0] == null ){ 
			util.retirerReponse(rq); 	 	
                    } 
                }else{ 	 	
	                util.ajouterReponseQCM(rq); 	 	
		} 
            em.merge(rq);
            if(rq.getId() == null){
            }
        }
        em.merge(util);
        ut.commit();
       HttpServletRequest request = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
       this.pseudo = request.getParameter("pseudo");

        FacesContext.getCurrentInstance().getExternalContext().redirect("liste-questionnaire.xhtml");
    }

    public List<String> getReponsesQcm() {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpSession session = (HttpSession) context.getExternalContext().getSession(false);
        Utilisateur utilisateurSession = (Utilisateur)em.find(Utilisateur.class,(String)session.getAttribute("pseudoUtilisateur")) ;
        this.pseudo = utilisateurSession.getPseudo();
        
        HttpServletRequest request = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
        Questionnaire q = em.find(Questionnaire.class,(long)Integer.parseInt(request.getParameter("id")));
        if(this.countRep >= q.getQuestions().size()){
            this.countRep = 0;
        }
        Question ques = q.getQuestion(this.countRep);

        Query jQuery2 = em.createQuery("Select q.reponsesQCM From Utilisateur q Where q.pseudo = :pseudo");
        jQuery2.setParameter("pseudo", this.pseudo);
        

        List<ReponseQCM> rListq = jQuery2.getResultList();
        ReponseQCM rq = null;
        
        for(ReponseQCM qcm : rListq){
            if(qcm != null && qcm.getQuestion().getId() == ques.getId()){
                rq = qcm;
            }
        }
        this.countRep++;
        List<String> ls = new ArrayList<String>();
        
        if(rq != null){
            for(Choix c : rq.getReponses()){
                ls.add((c.getId().toString()));
            }
            return ls;
        }
        return ls;
    }

    public void setReponsesQcm(List<String> s) throws NotSupportedException, SystemException, RollbackException, HeuristicMixedException, HeuristicRollbackException {
        //Récuperation questionnaire grace au parametre
        HttpServletRequest request = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
        q = em.find(Questionnaire.class,(long)Integer.parseInt(request.getParameter("id")));
        
        //récuperation de la question
        QuestionQCM qcm = (QuestionQCM) q.getQuestions().toArray()[i];

        Query jQuery = em.createQuery("Select q.reponsesQCM From Utilisateur q Where q.pseudo = :pseudo");
        jQuery.setParameter("pseudo", request.getParameter("pseudo"));
        List<ReponseQCM> rList = jQuery.getResultList();
        ReponseQCM rq = null;
        for(ReponseQCM rep : rList){
            if(rep  != null && rep.getQuestion().getId() == qcm.getId()){
                rq = rep;
            }
        }
        
        if(rq == null){
            rq = new ReponseQCM();
            rq.setType("selectmany");
        }
       
        Collection<Choix> cc = new ArrayList<Choix>();
        
        for(int j = 0; j < s.size() ; j++){
            Long id = (long) Integer.parseInt(s.get(j));
            Choix c = em.find(Choix.class, id);
            cc.add(c);
        }
        
        
        rq.setReponses(cc);
        rq.setQuestion(qcm);
        if(util == null){
            util = em.find(Utilisateur.class,request.getParameter("pseudo"));
        }
        
        this.repQCM.add(rq);
        i++;
        this.reponsesQcm = s;
    }

    public String getReponse() {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpSession session = (HttpSession) context.getExternalContext().getSession(false);
        Utilisateur utilisateurSession = (Utilisateur)em.find(Utilisateur.class,(String)session.getAttribute("pseudoUtilisateur")) ;
        this.pseudo = utilisateurSession.getPseudo();
        
        HttpServletRequest request = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
        Questionnaire q = em.find(Questionnaire.class,(long)Integer.parseInt(request.getParameter("id")));
        if(this.countRep >= q.getQuestions().size()){
            this.countRep = 0;
        }
        Question ques = q.getQuestion(this.countRep);
        Query jQuery = em.createQuery("Select q.reponsesOuvertes From Utilisateur q Where q.pseudo = :pseudo");
        jQuery.setParameter("pseudo", this.pseudo);
        Query jQuery2 = em.createQuery("Select q.reponsesQCM From Utilisateur q Where q.pseudo = :pseudo");
        jQuery2.setParameter("pseudo", this.pseudo);
        
        List<ReponseOuverte> rListo = jQuery.getResultList();

        List<ReponseQCM> rListq = jQuery2.getResultList();

        ReponseOuverte ro = null;
        ReponseQCM rq = null;
        for(ReponseOuverte qo : rListo){
            if(qo != null && qo.getQuestion().getId() == ques.getId()){

                ro = qo;
            }
        }
        
        for(ReponseQCM qcm : rListq){
            if(qcm != null && qcm.getQuestion().getId() == ques.getId()){

                rq = qcm;
            }
        }
        this.countRep++;
        
        if(ro == null && rq != null){
            return ((Choix)rq.getReponses().toArray()[0]).getId().toString();
        }else if(ro != null){
            return ro.getReponse();
        }else{
            return "";
        }
    }

    public void setReponse(String rep) throws NotSupportedException, SystemException, RollbackException, HeuristicMixedException, HeuristicRollbackException {


        if(rep!=null){
            HttpServletRequest request = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
            q = em.find(Questionnaire.class,(long)Integer.parseInt(request.getParameter("id")));

            Question qu = (Question) q.getQuestions().toArray()[i];
            if(qu instanceof QuestionOuverte){
                boolean nouv = false;
                Query jQuery = em.createQuery("Select q.reponsesOuvertes From Utilisateur q Where q.pseudo = :pseudo");
                jQuery.setParameter("pseudo", request.getParameter("pseudo"));
                List<ReponseOuverte> rList = jQuery.getResultList();
                ReponseOuverte ro = null;
                for(ReponseOuverte reponse : rList){
                    if(reponse  != null && reponse.getQuestion().getId() == qu.getId()){
                        ro = reponse;
                        nouv = false;
                    }
                }

                if(ro == null){
                    ro = new ReponseOuverte();
                    nouv = true;
                }
                
                ro.setReponse(rep);
                ro.setQuestion((QuestionOuverte) qu);
                if(util == null){
                    util = em.find(Utilisateur.class,request.getParameter("pseudo"));
                }

                this.repOuvertes.add(ro);         
            }else{ // instanceof QuestionQCM
                Query jQuery = em.createQuery("Select q.reponsesQCM From Utilisateur q Where q.pseudo = :pseudo");
                jQuery.setParameter("pseudo", request.getParameter("pseudo"));
                List<ReponseQCM> rList = jQuery.getResultList();
                ReponseQCM rq = null;
                for(ReponseQCM reponse : rList){
                    if(reponse  != null && reponse.getQuestion().getId() == qu.getId()){
                        rq = reponse;
                    }
                }

                if(rq == null){
                    rq = new ReponseQCM();
                    rq.setType("selectone");
                }
                Long id = (long) Integer.parseInt(rep);
                Choix c = em.find(Choix.class, id);
                //ReponseQCM rq = new ReponseQCM();
                Collection<Choix> cc = new ArrayList<Choix>();
                cc.add(c);
                rq.setReponses(cc);
                rq.setQuestion((QuestionQCM) qu);
                if(util == null){
                    util = em.find(Utilisateur.class,request.getParameter("pseudo"));
                }
                this.repQCM.add(rq);
  
            }
            this.reponse = rep;
        }
        i++;
    }    
    
    public List<Question> getQuestion() {

       Query jQuery = em.createQuery("Select q From Questionnaire q Where q.id = :id");
       HttpServletRequest request = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();

       jQuery.setParameter("id", Integer.parseInt(request.getParameter("id")));
       List<Questionnaire> rList = jQuery.getResultList();
       jQuery = em.createQuery("Select q From Question q Where q.questionnaires = :q");
       jQuery.setParameter("q", rList.get(0));
       List<Question> rListe = jQuery.getResultList();
       return rListe;
    }
    
    public List<Choix> getChoix(int idQuest){

       Query question = em.createQuery("SELECT a FROM Question a WHERE a.id = :id");
       question.setParameter("id", idQuest);
       if(question.getResultList().get(0) instanceof QuestionQCM){
            Query jQuery = em.createQuery("SELECT a FROM Choix a WHERE a.question = :q");
            jQuery.setParameter("q", question.getResultList().get(0));
            List<Choix> rList = jQuery.getResultList();
       return rList;
       }
       
       return null;
    }

    public int getIdQuest() {
        HttpServletRequest request = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
        return Integer.parseInt(request.getParameter("id"));
    }

    public void setIdQuest(int idQuest) throws IOException {
        this.idQuest = idQuest;

        FacesContext.getCurrentInstance().getExternalContext().redirect("questionnaire.xhtml?id="+idQuest);
    }
    
    public List<Questionnaire> getQuests()throws NotSupportedException, SystemException, RollbackException, HeuristicMixedException, HeuristicRollbackException{
       Query jQuery = em.createQuery("Select q From Questionnaire q");
       List<Questionnaire> rList = jQuery.getResultList();
       return rList;
    }
    
    public void rediriger() throws IOException{
        FacesContext.getCurrentInstance().getExternalContext().redirect("liste-questionnaire.xhtml");
    }
    
    public void redirigerliste(AjaxBehaviorEvent event) throws IOException{

        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        ec.redirect(ec.getRequestContextPath() + "liste-questionnaire.xhtml");
    }

    public String getPseudo() {
        return pseudo;
    }

    
    public void setPseudo(String pseudo) {
        this.pseudo = pseudo;
    }
    
    public int getNbQuestionsQuestionnaire(long id){
         Questionnaire q = em.find(Questionnaire.class,id);
         return q.getQuestions().size();
    }
    
    public int getNbQuestionsRepondues(long id){
        FacesContext context = FacesContext.getCurrentInstance();
        HttpSession session = (HttpSession) context.getExternalContext().getSession(false);
        HttpServletRequest request = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
        System.out.println((String)session.getAttribute("pseudoUtilisateur"));
        Utilisateur user = (Utilisateur)em.find(Utilisateur.class,(String)session.getAttribute("pseudoUtilisateur")) ;
        Questionnaire q = (Questionnaire)em.find(Questionnaire.class,id);
        
        int i = 0;
        for(ReponseOuverte ro : user.getReponsesOuvertes()){
            for(Questionnaire qs : ro.getQuestion().getQuestionnaires()){
                if(qs.getId() == id && !ro.getReponse().equals("")){
                    i++;
                }
            }
        }
        
        for(ReponseQCM rq : user.getReponsesQCM()){
            for(Questionnaire qst : rq.getQuestion().getQuestionnaires()){
                if(qst.getId() == id && !rq.getReponses().isEmpty()){
                    if(((Choix)rq.getReponses().toArray()[0]).getReponse().equals("")){
                        
                    }else{
                        i++;
                    }
                }
            }
        }
        return i;
    }
    
    public String getNomQuestionnaire(){
       HttpServletRequest request = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
       Questionnaire qr = (Questionnaire)em.find(Questionnaire.class,(long)Integer.parseInt(request.getParameter("id")));
       return qr.getNom();
    }

	public void supprimer() throws IOException, NotSupportedException, SystemException, RollbackException, HeuristicMixedException, HeuristicRollbackException{
       HttpServletRequest request = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
       Questionnaire qr = (Questionnaire)em.find(Questionnaire.class,(long)Integer.parseInt(request.getParameter("id")));
       
       ut.begin();
      for(Question q : qr.getQuestions()){
          q.retirerQuestionnaire(qr.getId());
          em.merge(q);
      }
      qr.setQuestions(null);   
      em.remove(em.merge(qr));
      ut.commit();
      FacesContext.getCurrentInstance().getExternalContext().redirect("liste-questionnaire-modif.xhtml");

    }

    public String getNewQuest() {
        return newQuest;
    }

    public void setNewQuest(String newQuest) {
        this.newQuest = newQuest;
    }
    
    public void creerQuestionnaire() throws NotSupportedException, SystemException, RollbackException, HeuristicRollbackException, HeuristicMixedException, IOException{
        Questionnaire q = new Questionnaire();
        q.setNom(this.newQuest);
        
        ut.begin();
        em.merge(q);
        ut.commit();
        
         FacesContext.getCurrentInstance().getExternalContext().redirect("liste-questionnaire-modif.xhtml");
    }
}
