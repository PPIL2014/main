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

@FacesValidator("validerSecu")
public class ValiderSecu implements Validator {
    
    @Override
    public void validate(FacesContext context, UIComponent component, Object value)
            throws ValidatorException {
        
        String secu = String.valueOf(value);
        boolean valid = secu.startsWith("1") || secu.startsWith("2");
        
        if (!valid) {
            FacesMessage message = new FacesMessage(
                    FacesMessage.SEVERITY_ERROR, "numéro de sécu invalide",
                    "Le numéro de sécu n'est pas valide.");
            throw new ValidatorException(message);
        }
    }
}
