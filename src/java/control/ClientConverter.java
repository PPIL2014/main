/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package control;
import javax.annotation.Resource;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;
import model.Utilisateur;

/**
 *
 * @author Faouz
 */


@FacesConverter("clientConverter")
public class ClientConverter implements Converter {
    
    @PersistenceContext(unitName = "DateRoulettePU")
    private EntityManager em;
    @Resource
    private UserTransaction ut;
    
    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        return getUser(value);
    }
 
    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        
        if (value == null || value.equals("")) {  
            return "";  
        } else {  
            return String.valueOf(((Utilisateur) value).getPseudo());  
        }  
    }
    
    /**
     * Méthode permettant de récupérer un utilisateur à partir de son id
     * @param pseudo le pseudo de l'utilisateur
     * @return l'utilsiateur associé à cet id
     */
    public Utilisateur getUser(String pseudo) {
        Utilisateur results = null;
        try{
            ut.begin();
            Query q = em.createQuery("SELECT u FROM Utilisateur u WHERE u.pseudo=:pse");
            q.setParameter("pse", pseudo);
            results = (Utilisateur) q.getSingleResult();
            
            ut.commit();
        }catch(NotSupportedException | SystemException | RollbackException | 
                HeuristicMixedException | HeuristicRollbackException | 
                SecurityException | IllegalStateException e){
            e.printStackTrace();
        }
        
        return results;
    }
}
