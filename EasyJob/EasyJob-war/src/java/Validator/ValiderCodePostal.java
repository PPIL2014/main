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
 * @author Yann
 */
@FacesValidator("validerCodePostal")
public class ValiderCodePostal implements Validator{
    
    @Override
    public void validate(FacesContext context, UIComponent component, Object value)
            throws ValidatorException {
        boolean b=false;
        try{

            if(value!=null){
                int i=Integer.parseInt(((String)(value)));
                b=true;
            }

        }catch(Exception e){
            
        }
        
        if(!b){
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erreur",
                    "Code postal non valide");
            throw new ValidatorException(message);
            
        }

    }

}