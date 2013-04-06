/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import javax.inject.Named;
import javax.faces.bean.SessionScoped;

/**
 *
 * @author Yann
 */
@Named(value = "rmb")
@SessionScoped
public class RegisterManageBeans {
    private String totos="2eux";
    /**
     * Creates a new instance of RegisterManageBeans
     */
    public RegisterManageBeans() {
    }
    
    
    
    public String register(){
        return "toto";
    }

    /**
     * @return the totos
     */
    public String getTotos() {
        return totos;
    }

    /**
     * @param totos the totos to set
     */
    public void setTotos(String totos) {
        this.totos = totos;
    }
}
