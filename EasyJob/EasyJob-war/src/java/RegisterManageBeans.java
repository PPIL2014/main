

import business.ConnexionSessionBean;
import java.io.Serializable;
import javax.enterprise.context.SessionScoped;
import javax.enterprise.inject.Produces;
import javax.faces.bean.ViewScoped;
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

@Named
@ViewScoped
public class RegisterManageBeans implements Serializable{
    @Produces
    private String login,password;
    @Produces
    private String rat;

    @Inject
    private ConnexionSessionBean conexionSnBean;
    /**
     * Creates a new instance of RegisterManageBeans
     */
    public RegisterManageBeans() {
       // conexionMngBean=new ConnexionMngBean();
    }
    
    
    
    public String connexion(){
      //  System.out.println(conexionMngBean.connexionValidator(getLogin(), getPassword()));
        return "toto";
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
