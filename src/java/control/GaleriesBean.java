/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package control;

import java.util.Collection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
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
    private String nomImage;

    public String getNomImage() {
        return nomImage;
    }

    public void setNomImage(String nomImage) {
        this.nomImage = nomImage;
    }

    public String getNomGalerie() {
        return nomGalerie;
    }

    public void setNomGalerie(String nomGalerie) {
        this.nomGalerie = nomGalerie;
    }

    public boolean isExist(String nameG) {
        boolean ret = false;
        FacesContext context = FacesContext.getCurrentInstance();
        Utilisateur u = em.find(Utilisateur.class, getUtilisateurSession().getPseudo());
        for (Galerie g : u.getGaleries()) {
            if (g.getNom().equals(nomGalerie)) {
                ret = true;
            }
        }

        return ret;
    }

    public String ajouterGalerie() {
        FacesContext context = FacesContext.getCurrentInstance();
        Utilisateur u = em.find(Utilisateur.class, getUtilisateurSession().getPseudo());
        Galerie g = new Galerie();
        if (this.isExist(nomGalerie)) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Nom galerie dèja utilisé  !!", null));
            return "ajouterGalerie";
        } else {
            g.setNom(nomGalerie+"/"+u.getPseudo());
            g.setProprietaire(u);
            u.addGalerie(g);
            try {

                ut.begin();
                em.merge(u);
                ut.commit();
                context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Galerie ajoutée", null));

            } catch (NotSupportedException | SystemException | RollbackException | HeuristicMixedException | HeuristicRollbackException | SecurityException | IllegalStateException ex) {
            }
            return "afficherGalerie";

        }
    }

    public Collection<Galerie> getGaleries() {
        return getUtilisateurSession().getGaleries();
    }

    public Utilisateur getUtilisateurSession() {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpSession session = (HttpSession) context.getExternalContext().getSession(false);
        Utilisateur utilisateurSession = (Utilisateur) em.find(Utilisateur.class, (String) session.getAttribute("pseudoUtilisateur"));
        return utilisateurSession;
    }

    public Collection<Image> getGalerie() {
        for (Galerie g : getUtilisateurSession().getGaleries()) {
            if (g.getNom().equals(nomGalerie)) {
                return g.getImages();
            }
        }

        return null;
    }

    public String ajouterImage() {
        FacesContext context = FacesContext.getCurrentInstance();
        Utilisateur u = em.find(Utilisateur.class, getUtilisateurSession().getPseudo());
        Image i = new Image();
        i.setNom(nomImage);
        for (Galerie g : u.getGaleries()) {
            if (g.getNom().equals(nomGalerie)) {
                g.getImages().add(i);
                break;
            }
        }

        try {

            ut.begin();
            em.merge(u);
            ut.commit();

        } catch (NotSupportedException | SystemException | RollbackException | HeuristicMixedException | HeuristicRollbackException | SecurityException | IllegalStateException ex) {
        }
        context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Image ajoutée", null));

        return "afficherGalerie";
    }
}
