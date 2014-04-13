/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package control;
import javax.faces.bean.ManagedProperty;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import model.Utilisateur;

/**
 *
 * @author Faouz
 */


@FacesConverter("clientConverter")
public class ClientConverter implements Converter {
    
    @ManagedProperty("#{sessionBean.dest2}")
    public Utilisateur dest;
    @ManagedProperty("#{sessionBean.dest2}")
    public Utilisateur dest2;
    @ManagedProperty("#{sessionBean.dest2}")
    public Utilisateur dest3;
    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        
        if(value.equals(dest.getPseudo())){
            return dest;
        }
        else if(value.equals(dest2.getPseudo())){
            return dest2;
        }
        else{
            return dest3;
        }
        
    }
 
    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        System.out.println("Value2 : "+value);
        if (value == null || value.equals("")) {  
            return "";  
        } else {  
            return String.valueOf(((Utilisateur) value).getPseudo());  
        }  
    }
}