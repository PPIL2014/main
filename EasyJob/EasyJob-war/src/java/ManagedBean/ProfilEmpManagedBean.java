/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ManagedBean;

import java.io.Serializable;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

/**
 *
 * @author enis
 */

@Named
@SessionScoped
public class ProfilEmpManagedBean implements Serializable{
    
    private String nomEntr="aaaa";
    private String domaineActi="zaeaze";
    private String rue;
    private String codePostal;
    private String ville;
    private String pays;
    private String telephoneEnt;
    private String statut;
    private String siteWeb;
    private String description;
    private int nbEmployes;
    private String mail;
    private String nom;
    private String prenom;
    private String telephoneEmp;
    
    
    public ProfilEmpManagedBean(){
        
    }

    public String getNomEntr() {
        return nomEntr;
    }

    public void setNomEntr(String nomEntr) {
        this.nomEntr = nomEntr;
    }

    public String getDomaineActi() {
        return domaineActi;
    }

    public void setDomaineActi(String domaineActi) {
        this.domaineActi = domaineActi;
    }

    public String getRue() {
        return rue;
    }

    public void setRue(String rue) {
        this.rue = rue;
    }

    public String getCodePostal() {
        return codePostal;
    }

    public void setCodePostal(String codePostal) {
        this.codePostal = codePostal;
    }

    public String getVille() {
        return ville;
    }

    public void setVille(String ville) {
        this.ville = ville;
    }

    public String getPays() {
        return pays;
    }

    public void setPays(String pays) {
        this.pays = pays;
    }

    public String getTelephoneEnt() {
        return telephoneEnt;
    }

    public void setTelephoneEnt(String telephoneEnt) {
        this.telephoneEnt = telephoneEnt;
    }

    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }

    public String getSiteWeb() {
        return siteWeb;
    }

    public void setSiteWeb(String siteWeb) {
        this.siteWeb = siteWeb;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getNbEmployes() {
        return nbEmployes;
    }

    public void setNbEmployes(int nbEmployes) {
        this.nbEmployes = nbEmployes;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getTelephoneEmp() {
        return telephoneEmp;
    }

    public void setTelephoneEmp(String telephoneEmp) {
        this.telephoneEmp = telephoneEmp;
    }
}