/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ManagedBean;

import interfaces.AnnonceLocal;
import interfaces.CandidatLocal;
import interfaces.EmployeurLocal;
import java.io.IOException;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import javax.inject.Inject;
import javax.servlet.http.HttpSession;
import persistence.Candidat;
import persistence.Employeur;

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
    
    /**
     * page de la recherche
     */
    private String recherche;
    
    @Inject
    private CandidatLocal candidatEJB;
    @Inject
    private EmployeurLocal employeurEJB;
     @Inject
    private AnnonceLocal annonceEJB;

    
    @PostConstruct
    public void initialisation() {
        recherche = "";
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
            next = "donneesCand";
        } else if(employeur!=null){
            next = "donneesEmpl";
        }
        return next;
    }
    
     public String suggestion() {
        String next = "";
        if(candidat!=null) {
            next="consulterSuggestionAnnonce";
        } else if (employeur!=null) {
            next = "consulterSuggestionCandidat";
        }
        return next;
    }
    
    public String notification() {
        String next = "";
        if(candidat!=null) {
            next="consulterNotificationCand";
        } else if (employeur!=null) {
            next = "consulterNotificationEmp";
        }
        return next;
    }
    
    public String listeSuivi() {
        String next = null;
        if(candidat!=null)
            next =  "listeSuiviCandidat";
        else if (employeur!=null)
            next = "listeSuiviEmployeur";
        return next;
    }
    
    public String connexion(){
        String next = "accueil";
        if(login!=null && password!=null) {
            if(candidatEJB.loginCandidat(login, password)){                 
                setCandidat(candidatEJB.getCandidatByMail(login));
                setEmployeur(null);
                isConnect = true;
                next = "profilCand";
            }else if (employeurEJB.loginEmployeur(login, password)){
                setEmployeur(employeurEJB.getEmployeurByMail(login));
                setCandidat(null);
                isConnect = true;
                next = "profilEmpl";
            } else {
                setEmployeur(null);
                setCandidat(null);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"Erreur", "Identifiants incorrects"));  
            }
        } else {
             setEmployeur(null);
             setCandidat(null);  
             FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"Erreur", "Identifiants incorrects"));  
        }
           
        return next;
    }

    /**
     * permet de se deconnecter
     * @return 
     */
    public String deconnexion(){
        this.isConnect=false;
        this.candidat=null;
        this.employeur=null;
        HttpSession ses = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
        if(ses!=null){
            ses.invalidate();
        }
        return "accueil";
    }
    
       public void changedValeur(ValueChangeEvent e){
        recherche=((String)(e.getNewValue()));
        
        try {
            FacesContext.getCurrentInstance().getExternalContext().redirect(recherchePage());
        } catch (IOException ex) {
            Logger.getLogger(RegisterManagedBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        setRecherche("");
    }
       
    public int nbCandidats() {
        return candidatEJB.getNbCandidatsInscrits();
    }
    
    public int nbAnnonces() {
        return annonceEJB.getNbAnnoncesPostees();
    }
    
    public int nbEmployeurs() {
        return employeurEJB.getNbEmployeursInscrits();
    }
    
    public String recherchePage() {
        return recherche;
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
        return this.candidat!=null||this.employeur!=null;
    }

    /**
     * @param isConnect the isConnect to set
     */
    public void setIsConnect(boolean isConnect) {
        this.isConnect = isConnect;
    }

    /**
     * @return the recherche
     */
    public String getRecherche() {
        return recherche;
    }

    /**
     * @param recherche the recherche to set
     */
    public void setRecherche(String recherche) {
        this.recherche = recherche;
    }

    
}
