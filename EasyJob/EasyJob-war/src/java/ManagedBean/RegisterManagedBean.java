package ManagedBean;





import interfaces.CandidatLocal;
import interfaces.EmployeurLocal;
import java.io.Serializable;
import javax.annotation.PostConstruct;

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
public class RegisterManagedBean implements Serializable{

    private String login;
    private String password;
    
    private Employeur employeur;
    private Candidat candidat;
    /**
     * Le champs membre est à vrai si c'est un candidat qui est conecté et à faux si c'est un employeur
     */
    private boolean isConnect;



    @Inject
    private CandidatLocal candidatEJB;
    @Inject
    private EmployeurLocal employeurEJB;
    
    /**
     * Creates a new instance of RegisterManagedBean
     */
    public RegisterManagedBean() {
    }
    
    @PostConstruct
    public void initialisation() {
        isConnect = false;
    }
    
    public String accueilPage() {
        String next = "";
        if(candidat!=null) {
            next="profilCand";
        } else if (employeur!=null) {
            next = "profilEmpl";
        }
        return next;
    }
        
    public String profil() {
        String next ="";
        if(candidat!=null) {
            next = "donneesCand.xhtml";
        } else if(employeur!=null){
            next = "donneesEmpl.xhtml";
        }
        return next;
    }
    
    
    public String connexion(){
        String next = "connexion";
        if(login!=null && password!=null) {
                if(candidatEJB.loginCandidat(login, password)){                 
                    setCandidat(candidatEJB.getCandidatByMail(login));
                    next = "profilCand";
                }else if (employeurEJB.loginEmployeur(login, password)){
                    setEmployeur(employeurEJB.getEmployeurByMail(login));
                    next = "profilEmpl";
                } else {
                    setEmployeur(null);
                    setCandidat(null);
                }
        } else {
             setEmployeur(null);
             setCandidat(null);   
        }
           
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

    /**
     * @return the isConnect
     */
    public boolean isIsConnect() {
        return isConnect;
    }

    /**
     * @param isConnect the isConnect to set
     */
    public void setIsConnect(boolean isConnect) {
        this.isConnect = isConnect;
    }


}
