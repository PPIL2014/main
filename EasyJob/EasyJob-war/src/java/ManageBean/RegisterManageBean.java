package ManageBean;





import interfaces.CandidatLocal;
import interfaces.EmployeurLocal;
import java.io.Serializable;
import javax.enterprise.context.SessionScoped;
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
//@ManagedBean(name="connexion")
@SessionScoped
public class RegisterManageBean implements Serializable{
   
    //@Produces
    private String login,password;
    
    private Employeur employeur;
    
    private Candidat candidat;
    
    //@Produces
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
    
    
    
    public String connexion(){
      //  System.out.println(conexionMngBean.connexionValidator(getLogin(), getPassword()));
        return "connexion";
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

}
