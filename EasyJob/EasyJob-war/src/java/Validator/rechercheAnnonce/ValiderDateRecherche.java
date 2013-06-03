/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Validator.rechercheAnnonce;

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
@FacesValidator("validerDateRecherche")
public class ValiderDateRecherche implements Validator{
    
    @Override
    public void validate(FacesContext context, UIComponent component, Object value)
            throws ValidatorException {
        if(value!=null){
            Date date=(Date)value;
            Date actuel=new Date();
            if(date.after(actuel)){
                FacesMessage message = new FacesMessage();
                message.setSeverity(FacesMessage.SEVERITY_ERROR);
                message.setSummary("Date non valide.");
                message.setDetail("Date future");
                context.addMessage("recherAnnonce:datecreation", message);
                throw new ValidatorException(message);
            }
        }
    }

}