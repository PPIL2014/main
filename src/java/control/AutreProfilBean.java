/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package control;

import javax.annotation.Resource;
import javax.inject.Named;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.UserTransaction;
import model.Utilisateur;

/**
 *
 * @author Romain
 */
@Named(value = "autreProfilBean")
@ManagedBean
@RequestScoped
public class AutreProfilBean {
    
    @PersistenceContext(unitName = "DateRoulettePU")
    private EntityManager em;

    @Resource 
    private UserTransaction ut;
    
    public Utilisateur u; 

    /**
     * Creates a new instance of AutreProfilBean
     */
    public AutreProfilBean() {
    }
    
    public Utilisateur getUtilisateur(){
        return (Utilisateur) em.find(Utilisateur.class, "test");
    }
    
    
    
}
