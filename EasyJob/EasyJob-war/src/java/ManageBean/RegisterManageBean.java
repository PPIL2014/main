package ManageBean;




import business.CandidatEJB;
import business.EmployeurEJB;
import java.io.Serializable;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


/**
 *
 * @author Yann
 */

@Named(value="connexion")
//@ManagedBean(name="connexion")
@SessionScoped
public class RegisterManageBean implements Serializable{
   
    //@Produces
    private String login,password;
    //@Produces
    private String rat;

    /*@Inject
    private CandidatEJB candidatEJB;
    @Inject
    private EmployeurEJB employeurEJB;*/
    
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
