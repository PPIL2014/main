package ManagedBean;





import interfaces.CandidatLocal;
import interfaces.EmployeurLocal;
import java.io.Serializable;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

import persistence.Candidat;
import persistence.Employeur;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


/**
 *
 * @author Yann
 */

@ManagedBean(name="connexionBean")
@SessionScoped
public class RegisterManageBean implements Serializable{

    private String login;
    private String password;
    
    private Employeur employeur;
    private Candidat candidat;
    /**
     * Le champs membre est à 1 si c'est un candidat qui est conecté et à 2 si c'est un employeur
     */
    private int membre;


    @Inject
    private CandidatLocal candidatEJB;
    @Inject
    private EmployeurLocal employeurEJB;
    
    /**
     * Creates a new instance of RegisterManageBean
     */
    public RegisterManageBean() {
    }
    
    
    
    public String doConnectCandidat(){
        membre = 1;
        System.out.println(" ConnectCandidat  ");
        return "faces/connexion.xhtml";
    }
    
    public String doConnectEmployeur(){
        membre = 2;
        System.out.println(" ConnectEmployeur  ");
        return "faces/connexion.xhtml";
    }
    
    public String connexion(){
        String next = "connexion";
        if(login!=null && password!=null) {
            if(membre==1) {
                System.out.println("BOUCLE 1");
                if(candidatEJB.loginCandidat(login, password)){
                    setCandidat(candidatEJB.getCandidatByMail(login));
                    next = "profilCand";
                }else{
                    /*FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erreur d'identitfiants", "Invalid credentials");
                    FacesContext.getCurrentInstance().addMessage(null, msg);*/
                    setCandidat(null);
                }
            }else if(membre==2) {
                System.out.println("BOUCLE 2");
                if(employeurEJB.loginEmployeur(login, password)){
                    setEmployeur(employeurEJB.getEmployeurByMail(login));
                    next = "profilEmpl";
                }else{
                    setEmployeur(null);
                }
            }else{
                next="connexion";
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,"Sample info message", "PrimeFaces rocks!")); 
            }
            
        }
        System.out.println(next);
        System.out.println("Candidat = "+candidat);
        System.out.println("Employeur = "+employeur);
      //  System.out.println(conexionMngBean.connexionValidator(getLogin(), getPassword()));
        return next;
    }

    /**
     * @return the login
     */
    public String getLogin() {
        return login;
    }

    /**
     * @param login the login to set
     */
    public void setLogin(String login) {
        this.login = login;
    }

    /**
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }


 


    /**
     * @return the membre
     */
    public int getMembre() {
        return membre;
    }

    /**
     * @param membre the membre to set
     */
    public void setMembre(int membre) {
        this.membre = membre;
    }

    /**
     * @return the employeur
     */
    public Employeur getEmployeur() {
        return employeur;
    }

    /**
     * @param employeur the employeur to set
     */
    public void setEmployeur(Employeur employeur) {
        this.employeur = employeur;
    }

    /**
     * @return the candidat
     */
    public Candidat getCandidat() {
        return candidat;
    }

    /**
     * @param candidat the candidat to set
     */
    public void setCandidat(Candidat candidat) {
        this.candidat = candidat;
    }

}
