/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package control;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceUnit;
import model.Utilisateur;


@ManagedBean
@RequestScoped
public class UtilisateurControl {
    @PersistenceUnit(unitName="DateRoulettePU")
    private EntityManagerFactory emf;
    
    @PersistenceContext
    private EntityManager em;
    
    public void ajouterUtilisateur(String pseudo) {
        Utilisateur u = new Utilisateur();
        u.setPrenom("Thomas");
        
    }
    
    @PostConstruct
    public void init() {
        this.em = this.emf.createEntityManager();
    }
    
    @PreDestroy
    public void destroy() {
        this.em.close();
    }
}
