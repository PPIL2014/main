/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package control;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import javax.annotation.Resource;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.inject.Named;
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
import model.SignalementUtilisateur;
import model.Utilisateur;

/**
 *
 * @author Romain
 */
@ManagedBean
@Named(value = "signalerProfilBean")
@ViewScoped
public class SignalementProfilBean implements Serializable {

    @PersistenceContext(unitName = "DateRoulettePU")
    private EntityManager em;

    @Resource
    private UserTransaction ut;

    //  @ManagedProperty(value = "#{user}")
    public Utilisateur user;

    //   @ManagedProperty(value = "#{correspondant}")
    public String correspondant;

    @ManagedProperty(value = "#{motif}")
    public String motif;

    private Collection<model.SignalementUtilisateur> signalementsUtilisateurs;

    private UIComponent boutonSignaler;

    public Utilisateur getUser() {
        return user;
    }

    public void setU1(Utilisateur user) {
        this.user = user;
    }

    public String getCorrespondant() {
        return correspondant;
    }

    public void setCorrespondant(String correspondant) {
        this.correspondant = correspondant;
    }

    public String getMotif() {
        return motif;
    }

    public void setMotif(String motif) {
        this.motif = motif;
    }

    public UIComponent getBoutonSignaler() {
        return boutonSignaler;
    }

    public void setBoutonSignaler(UIComponent boutonSignaler) {
        this.boutonSignaler = boutonSignaler;
    }

    /**
     * Creates a new instance of SignalementPhotoBean
     */
    public SignalementProfilBean() {
    }

    public void signaler(String correspondant) {

        HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
        user = (Utilisateur) session.getAttribute("user");
        /*  System.out.println("user = " + user.getPseudo());
         System.out.println("correspondant = " + correspondant);
         System.out.println("motif = " + motif);*/
        Utilisateur utilisateurSignale = (Utilisateur) em.find(Utilisateur.class, correspondant);

        signalementsUtilisateurs = user.getSignalementsUtilisateur();

        SignalementUtilisateur s = new SignalementUtilisateur();
        s.setEmetteur(user);

        Query jQuery = em.createQuery("Select s From SignalementUtilisateur s Where s.estTraitee = 0 AND s.emetteur.pseudo = :u1 AND s.utilisateurSignale.pseudo = :u2");
        jQuery.setParameter("u1", user.getPseudo());
        jQuery.setParameter("u2", correspondant);
        List<SignalementUtilisateur> liste = jQuery.getResultList();
        if (!liste.isEmpty()) {
            FacesContext context = FacesContext.getCurrentInstance();
            context.addMessage(this.boutonSignaler.getClientId(), new FacesMessage(FacesMessage.SEVERITY_ERROR, "Vous avez deja signalé cet utilisateur !", null));
        } else {
            try {

                s.setMotif(motif);

                Date d = new Date();
                s.setDate(d);
                s.setUtilisateurSignale(utilisateurSignale);
                signalementsUtilisateurs.add(s);
                user.setSignalementsUtilisateur(signalementsUtilisateurs);

                ut.begin();
                em.persist(s);
                em.merge(user);
                ut.commit();

            } catch (NotSupportedException | SystemException | RollbackException | HeuristicMixedException | HeuristicRollbackException | SecurityException | IllegalStateException e) {
                e.printStackTrace();
            }

        }

        // return "chat.xhtml";
    }

    public Collection<SignalementUtilisateur> getListeSignalementsProfils() {

        Query jQuery = em.createQuery("Select s From SignalementUtilisateur s Where s.estTraitee = 0");

        List<SignalementUtilisateur> liste = jQuery.getResultList();

        return liste;
    }

    public void bloquerUtilisateur(String emetteur, String utilisateurSignale) {
        try {
            Utilisateur userBloque = em.find(Utilisateur.class, utilisateurSignale);

            Query jQuery = em.createQuery("Select s From SignalementUtilisateur s Where s.estTraitee = 0 AND s.emetteur.pseudo = :u1 AND s.utilisateurSignale.pseudo = :u2");
            jQuery.setParameter("u1", emetteur);
            jQuery.setParameter("u2", utilisateurSignale);
            List<SignalementUtilisateur> liste = jQuery.getResultList();
            SignalementUtilisateur sU = liste.get(0);
            sU.setEstTraitee(Boolean.TRUE);

            userBloque.setEstBloque(Boolean.TRUE);
            ut.begin();
            em.merge(userBloque);
            em.merge(sU);
            ut.commit();
        } catch (NotSupportedException | RollbackException | HeuristicMixedException | HeuristicRollbackException | SecurityException | IllegalStateException | SystemException e) {
            e.printStackTrace();
        }
    }

    public void injustifie(String emetteur, String utilisateurSignale) {
        try {

            Query jQuery = em.createQuery("Select s From SignalementUtilisateur s Where s.estTraitee = 0 AND s.emetteur.pseudo = :u1 AND s.utilisateurSignale.pseudo = :u2");
            jQuery.setParameter("u1", emetteur);
            jQuery.setParameter("u2", utilisateurSignale);
            List<SignalementUtilisateur> liste = jQuery.getResultList();
            SignalementUtilisateur sU = liste.get(0);
            sU.setEstTraitee(Boolean.TRUE);

            ut.begin();
            em.merge(sU);
            ut.commit();
        } catch (NotSupportedException | RollbackException | HeuristicMixedException | HeuristicRollbackException | SecurityException | IllegalStateException | SystemException e) {
            e.printStackTrace();
        }
    }

}
