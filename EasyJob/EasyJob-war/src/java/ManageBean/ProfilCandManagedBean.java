/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ManageBean;

import java.io.Serializable;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

/**
 *
 * @author enis
 */

@Named(value="profilcand")
@SessionScoped
public class ProfilCandManagedBean implements Serializable{
    
    private String nomCand;
    private String prenomCand;
    private String rue;
    private String codePostal;
    private String ville;
    private String pays;
    private String telCand;
    private String dateNaiss;
    private String siteWeb;
    private String noSecu;
    private String adresse;
    private String mail;
    private String niveauetude;
    private String compLing;
    private String domaineEtude;
    private String expPro;
    private String nbrAnneeEtude;
        
    public ProfilCandManagedBean(){
        
    }

    public String getNomCand() {
        return nomCand;
    }

    public String getPrenomCand() {
        return prenomCand;
    }

    public String getRue() {
        return rue;
    }

    public String getCodePostal() {
        return codePostal;
    }

    public String getVille() {
        return ville;
    }

    public String getPays() {
        return pays;
    }

    public String getTelCand() {
        return telCand;
    }

    public String getDateNaiss() {
        return dateNaiss;
    }

    public String getSiteWeb() {
        return siteWeb;
    }

    public String getNoSecu() {
        return noSecu;
    }

    public String getAdresse() {
        return adresse;
    }

    public String getMail() {
        return mail;
    }

    public String getNiveauetude() {
        return niveauetude;
    }

    public String getCompLing() {
        return compLing;
    }

    public String getDomaineEtude() {
        return domaineEtude;
    }

    public String getExpPro() {
        return expPro;
    }

    public String getNbrAnneeEtude() {
        return nbrAnneeEtude;
    }

    public void setNomCand(String nomCand) {
        this.nomCand = nomCand;
    }

    public void setPrenomCand(String prenomCand) {
        this.prenomCand = prenomCand;
    }

    public void setRue(String rue) {
        this.rue = rue;
    }

    public void setCodePostal(String codePostal) {
        this.codePostal = codePostal;
    }

    public void setVille(String ville) {
        this.ville = ville;
    }

    public void setPays(String pays) {
        this.pays = pays;
    }

    public void setTelCand(String telCand) {
        this.telCand = telCand;
    }

    public void setDateNaiss(String dateNaiss) {
        this.dateNaiss = dateNaiss;
    }

    public void setSiteWeb(String siteWeb) {
        this.siteWeb = siteWeb;
    }

    public void setNoSecu(String noSecu) {
        this.noSecu = noSecu;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public void setNiveauetude(String niveauetude) {
        this.niveauetude = niveauetude;
    }

    public void setCompLing(String compLing) {
        this.compLing = compLing;
    }

    public void setDomaineEtude(String domaineEtude) {
        this.domaineEtude = domaineEtude;
    }

    public void setExpPro(String expPro) {
        this.expPro = expPro;
    }

    public void setNbrAnneeEtude(String nbrAnneeEtude) {
        this.nbrAnneeEtude = nbrAnneeEtude;
    }

}