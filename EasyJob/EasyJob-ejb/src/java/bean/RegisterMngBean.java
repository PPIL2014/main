/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bean;

import javax.ejb.Stateless;
import javax.ejb.LocalBean;

/**
 *
 * @author Yann
 */
@Stateless
public class RegisterMngBean {

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    public String submitForm(){
        return "toto.xhtml";
    }
}
