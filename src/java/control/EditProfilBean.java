/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package control;

import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpSession;
import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;
import model.Utilisateur;

/**
 *
 * @author nicolas
 */
@ManagedBean
@RequestScoped
public class EditProfilBean {
 
    /**
     * Creates a new instance of EditProfilBean
     */
    public EditProfilBean() {
    }
    
      // Injection du manager, qui s'occupe de la connexion avec la BDD
    @PersistenceContext( unitName = "DateRoulettePU" )
    private EntityManager em;
    @Resource 
    private UserTransaction ut;
    
    HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
    @ManagedProperty(value="#{param.pseudo}") // appelle setParam();
    private String param;

    private Utilisateur utilisateur;
        
    public String getParam() {
        return param;
    }

    public void setParam(String param) {
        this.param = param;
        utilisateur = getUtilisateurSession();
    }
    
    public Utilisateur getUtilisateurSession () {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpSession session = (HttpSession) context.getExternalContext().getSession(false);
        Utilisateur utilisateurSession = (Utilisateur)em.find(Utilisateur.class,(String)session.getAttribute("pseudoUtilisateur")) ;
        return utilisateurSession ;
    }
    
    public Utilisateur getUtilisateur(){ 
       return utilisateur;
    }
   
    public String edit(){
        if(utilisateur != null)
        {   
            try {
                
                ut.begin();
                em.merge(utilisateur);
                ut.commit();
                
            } catch (NotSupportedException ex) {
                Logger.getLogger(EditProfilBean.class.getName()).log(Level.SEVERE, null, ex);
            } catch (SystemException ex) {
                Logger.getLogger(EditProfilBean.class.getName()).log(Level.SEVERE, null, ex);
            } catch (RollbackException ex) {
                Logger.getLogger(EditProfilBean.class.getName()).log(Level.SEVERE, null, ex);
            } catch (HeuristicMixedException ex) {
                Logger.getLogger(EditProfilBean.class.getName()).log(Level.SEVERE, null, ex);
            } catch (HeuristicRollbackException ex) {
                Logger.getLogger(EditProfilBean.class.getName()).log(Level.SEVERE, null, ex);
            } catch (SecurityException ex) {
                Logger.getLogger(EditProfilBean.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IllegalStateException ex) {
                Logger.getLogger(EditProfilBean.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return "myProfil";

    }
    
    
    
    
}
