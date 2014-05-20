/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package control;

import java.util.Collection;
import java.util.List;
import javax.annotation.Resource;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.servlet.http.HttpSession;
import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;
import model.Galerie;
import model.Image;
import model.Utilisateur;

@ManagedBean
@RequestScoped
public class GaleriesBean {

    // Injection du manager, qui s'occupe de la connexion avec la BDD
    @PersistenceContext(unitName = "DateRoulettePU")
    private EntityManager em;
    @Resource
    private UserTransaction ut;

    @ManagedProperty(value = "#{param.nom}")
    private String nomGalerie;
    
    private Utilisateur utilisateur;

    public String getNomGalerie() {
        return nomGalerie;
    }

    public void setNomGalerie(String nomGalerie) {
        this.nomGalerie = nomGalerie;
    }

    public boolean exist(String nameG) {

        return em.find(Galerie.class, nameG)!=null;
    }

    public String ajouterGalerie() {
        FacesContext context = FacesContext.getCurrentInstance();
        utilisateur = getUtilisateurSession(); 
        if (this.exist(nomGalerie)) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Nom galerie dèja utilisé  !!", null));
            return "listeGaleries.xhtml";
        } else {
            Galerie galerie = new Galerie();
            galerie.setNom(nomGalerie);
            galerie.setProprietaire(utilisateur);
            utilisateur.addGalerie(galerie);
            try {

                ut.begin();
                em.persist(galerie);
                em.merge(utilisateur);
                ut.commit();
                context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Galerie ajoutée", null));

            } catch (NotSupportedException | SystemException | RollbackException | HeuristicMixedException | HeuristicRollbackException | SecurityException | IllegalStateException ex) {
            }

            return "afficherGalerie.xhtml";

        }
    }

    public Collection<Galerie> getGaleries() {

        utilisateur=getUtilisateurSession();
        if(utilisateur!=null)
            return utilisateur.getGaleries();
        else{
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"Veuillez vous connecter pour acceder à la galerie", null));
            return null;
        }

    }

    public Utilisateur getUtilisateurSession() {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpSession session = (HttpSession) context.getExternalContext().getSession(false);
        Utilisateur utilisateurSession = (Utilisateur) em.find(Utilisateur.class, (String) session.getAttribute("pseudoUtilisateur"));
        return utilisateurSession;
    }

    public Collection<Image> getGalerie() {
        /*for (Galerie g : getUtilisateurSession().getGaleries()) {
            if (g.getNom().equals(nomGalerie)) {
                return g.getImages();
            }
        }*/
        //System.err.println("nom : "+FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("nom"));
        if(nomGalerie==null)
            nomGalerie=FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("nom");
        Galerie g = em.find(Galerie.class, nomGalerie);
        Query q = em.createQuery("SELECT i FROM Image  i WHERE i.galerie=:g");
        q.setParameter("g", g);
        List<Image> results = (List<Image>) q.getResultList();
        return results;
    }
}
