/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package control;

import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import model.Utilisateur;

/**
 *
 * @author Faouz
 */
@ManagedBean
@ViewScoped
public class AutoCompleteClientBean {
    
    @ManagedProperty("#{sessionBean.utilisateur}")
    public Utilisateur user;
    
    private Utilisateur selectedClient;

    
   
    public List<Utilisateur> complete(String query) {  
        System.out.println(query);
        return user.getContactsByName(query);  
    } 
 
    public Utilisateur getSelectedClient() {
        return selectedClient;
    }

    public Utilisateur getUser() {
        return user;
    }

    public void setUser(Utilisateur user) {
        this.user = user;
    }
    
    public void setSelectedClient(Utilisateur selectedClient) {
        this.selectedClient = selectedClient;
    }
 
    public String submit()
    {
        return "MessagerieAsynchrone.xhtml?pseudo="+selectedClient.getPseudo();
    }
}