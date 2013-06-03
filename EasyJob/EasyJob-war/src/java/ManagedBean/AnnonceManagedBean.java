/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ManagedBean;

import interfaces.AnnonceLocal;
import interfaces.CandidatLocal;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Inject;
import persistence.Annonce;
import persistence.Candidat;

/**
 *
 * @author Marie
 */

@ManagedBean
@SessionScoped
public class AnnonceManagedBean implements Serializable {
    
    private Annonce annonce;
    private List<Candidat> lc;
    
    @Inject
    private AnnonceLocal annonceEJB;    
    @Inject
    private CandidatLocal candidatEJB;
    
    @PostConstruct
    public void initialisation() {
       
    }
    
    public String supprimerAnnonce(){
        annonceEJB.supprimerAnnonce(getAnnonce());        
        return "profilEmpl";
    }
    
    public boolean aPostuler() {
        FacesContext fc = FacesContext.getCurrentInstance();
        Candidat cand = ((RegisterManagedBean)fc.getExternalContext().getSessionMap().get("connexionBean")).getCandidat() ;
        return annonceEJB.aPostuler(getAnnonce(), cand) ;
    }
    
    public boolean proprietaireAnnonce(){
        FacesContext fc = FacesContext.getCurrentInstance();
        RegisterManagedBean rmb = (RegisterManagedBean) fc.getExternalContext().getSessionMap().get("connexionBean");
        boolean retour=false;
        if(rmb!=null&&annonceEJB!=null){
            retour=annonceEJB.estProprietaire(getAnnonce(), rmb.getEmployeur());
        }
        return retour;
    }

    public String consulterParRecherche(ActionEvent e ) {
        try{
            String myAttribute = e.getComponent().getAttributes().get( "Mannonce" ).toString();
            setAnnonce(annonceEJB.getAnnonceById(Integer.parseInt(myAttribute)));
        }catch(Exception ex){
            ex.printStackTrace();
        }
        return "consulterAnnonce.xhtml";
    }
    
    public List<Candidat> listeCandidat() {
        lc = new ArrayList<Candidat>();
        String s = null;
        Integer e = null;
        String et = null;
        if(annonce.getSecteur()!=null && annonce.getSecteur().length()!=0){
            s = annonce.getSecteur();
        }
        
        if(annonce.getExperience()>=0){
            e = annonce.getExperience();
        }
        
        if(annonce.getEtudes()!=null && annonce.getEtudes().length()!=0){
            et = annonce.getEtudes();
        }
        lc = candidatEJB.rechercheCandidat(s,e,et,null,null,null,null);
        return lc;
    }

    /**
     * @return the annonce
     */
    public Annonce getAnnonce() {
        return annonce;
    }

    /**
     * @param annonce the annonce to set
     */
    public void setAnnonce(Annonce annonce) {
        this.annonce = annonce;
    }

    /**
     * @return the lc
     */
    public List<Candidat> getLc() {
        return lc;
    }

    /**
     * @param lc the lc to set
     */
    public void setLc(List<Candidat> lc) {
        this.lc = lc;
    }

    
}
