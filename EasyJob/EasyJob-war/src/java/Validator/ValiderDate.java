/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Validator;

import java.util.Date;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;



/**
 *
 * @author Yann
 */
@FacesValidator("validerDate")
public class ValiderDate implements Validator{
    
    @Override
    public void validate(FacesContext context, UIComponent component, Object value)
            throws ValidatorException {
        if(value!=null){
            Date date=(Date)value;
            Date actuel=new Date();
            if(date.before(actuel)){
                FacesMessage message = new FacesMessage(
                    FacesMessage.SEVERITY_ERROR, "Erreur",
                    "Date non valide");
                throw new ValidatorException(message);
            }
        }

    }

}