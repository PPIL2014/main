package ManageBean;





import interfaces.CandidatLocal;
import interfaces.EmployeurLocal;
import java.io.Serializable;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
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

@Named(value="connexionBean")
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
    
    private String rat;

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
        return "faces/connexion.xhtml";
    }
    
    public String doConnectEmployeur(){
        membre = 2;
        return "faces/connexion.xhtml";
    }
    
    public String connexion(){
        String next = "connexion";
        if(login!=null && password!=null) {
            if(membre==1) {
                if(candidatEJB.loginCandidat(login, password)){
                    setCandidat(candidatEJB.getCandidatByMail(login));
                    next = "profilCand";
                }else{
                    /*FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erreur d'identitfiants", "Invalid credentials");
                    FacesContext.getCurrentInstance().addMessage(null, msg);*/
                    setCandidat(null);
                }
            }            
            
        }
        //System.out.println(candidat);
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


    public String getRat(){
        setRat("rat");
        return rat;
    }

    /**
     * @param rat the rat to set
     */
    public void setRat(String rat) {
        this.rat = rat;
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
