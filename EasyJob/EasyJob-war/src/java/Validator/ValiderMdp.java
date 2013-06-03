/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Validator;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

/**
 *
 * @author celie
 */

@FacesValidator("validerMdp")
public class ValiderMdp implements Validator {

    @Override
    public void validate(FacesContext context, UIComponent component, Object value)
            throws ValidatorException {
        
        String mdp = String.valueOf(value);
        boolean valid = mdp.contains("0") || mdp.contains("1") || mdp.contains("2") || mdp.contains("3") || mdp.contains("4") 
               || mdp.contains("5") || mdp.contains("6") || mdp.contains("7") || mdp.contains("8") || mdp.contains("9");
        
        if (!valid) {
            FacesMessage message = new FacesMessage(
                    FacesMessage.SEVERITY_ERROR, "mot de pas incorrect",
                    "Le mot de passe doit comporter au moins un chiffre.");
            throw new ValidatorException(message);
        }
    }
    
}
