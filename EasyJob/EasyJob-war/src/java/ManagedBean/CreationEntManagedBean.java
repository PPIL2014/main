/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ManagedBean;

import interfaces.CandidatLocal;
import interfaces.EmployeurLocal;
import interfaces.EntrepriseLocal;
import java.io.Serializable;
import java.util.ArrayList;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import persistence.Adresse;
import persistence.Employeur;
import persistence.Entreprise;

/**
 *
 * @author margaux
 */
@ManagedBean(name = "creationEntBean")
@ViewScoped
public class CreationEntManagedBean implements Serializable {

    private ArrayList<String> emps = new ArrayList<String>();
    
    private String nom = "";

    private String statut;
    private String domaine;

    private String rue;
    private String cp;
    private String ville;
    private String pays;

    private String tel;
    private String site;

    private String desc;
    private String nbEmp;
    
    private String nomRec;
    private String prenomRec;
    
    private String telRec;
    
    private String mail1;
    private String mail2;
    private String mdp1;
    private String mdp2;
    private String mdp3;
    
    private boolean confidentialite;
    
    private boolean inscrit;
    private boolean existeEntreprise;
    private boolean debut;
    
    @Inject
    private CandidatLocal candidaEJB;
    
    @Inject
    private EmployeurLocal employeurEJB;
    
    @Inject
    private EntrepriseLocal entrepriseEJB;

    /**
     * Creates a new instance of CreationEntManagedBean
     */
    public CreationEntManagedBean() {
    }
    
    @PostConstruct
    public void initialisation() {
        
        emps.add("< 20");
        emps.add(">= 20 et < 100");
        emps.add(">= 100 et < 500");
        emps.add(">= 500 et < 1000");
        emps.add("> 1000");
        
        inscrit = false;
        confidentialite = false;
        existeEntreprise = false;
        setDebut(true);
        
        FacesContext fc = FacesContext.getCurrentInstance();
        RegisterManagedBean rmb = (RegisterManagedBean) fc.getExternalContext().getSessionMap().get("connexionBean");
        Employeur emp = rmb.getEmployeur();
                  
        if(emp != null) {
            fill(emp);
        }
      
    }
    
    private boolean nonValideString(String s) {
        String str = "";
        for(int i=0; i<s.length(); i++) {
            str = str + " ";
        }
        return s.equals(str);
    }
    
    public String valider() {
                   
        String next = null;
        if(!mail1.equals(mail2)) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"Erreur", "Les adresses mail ne sont pas identiques"));  
        } else if(!mdp1.equals(mdp2)) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"Erreur", "Les mots de passe ne sont pas identiques"));  
        } else if(candidaEJB.existeMail(getMail1()) || employeurEJB.existeMail(getMail1())) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"Erreur", "L'adresse mail existe déjà"));  
        } else if(nonValideString(rue) || nonValideString(ville) || nonValideString(pays)) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"Erreur", "Vous devez renseigner votre adresse"));  
            return  null;
        } else {
            setInscrit(true);
            Employeur employeur = new Employeur();
            if(entrepriseEJB.existByName(getNom())) {              
                //l'entreprise existe déjà dans la base de données
                Entreprise ent = entrepriseEJB.getEntrepriseByNom(getNom());
                employeur = new Employeur(getNomRec(), getPrenomRec(), getMail1(), getMdp1(), isConfidentialite(), ent);
                employeur.setTelephone(getTelRec());
                ent.getEmployeurs().add(employeur);
                
                employeurEJB.saveEmployeur(employeur);
            } else {
                
                boolean vide = (rue==null || rue.length()==0) && (ville==null || ville.length()==0) && (cp==null || cp.length()==0) && (pays==null || pays.length()==0);
                boolean remplie = (rue!=null && rue.length()!=0) && (ville!=null && ville.length()!=0) && (cp!=null && cp.length()!=0) && (pays!=null && pays.length()!=0);
        
                if(!vide && !remplie) {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"Erreur", "L'adresse doit être vide ou remplie complètement"));  
                    return null;
                } 
                //l'entreprise n'existe pas dans la base de données
                Adresse adr = new Adresse(getRue(), getCp(), getVille(), getPays());
                Entreprise ent = new Entreprise(getNom(), getDomaine(), getTel(), getStatut(), adr);

                ent.setDescription(getDesc());
                ent.setSiteWeb(getSite());
                ent.setNbEmployes(getNbEmp());
                

                entrepriseEJB.saveEntreprise(ent);
                
                employeur = new Employeur(getNomRec(), getPrenomRec(), getMail1(), getMdp1(), isConfidentialite(), ent);
                employeur.setTelephone(getTelRec());
                ent.getEmployeurs().add(employeur);
                
                employeurEJB.saveEmployeur(employeur);
            }
            
            FacesContext fc = FacesContext.getCurrentInstance();
            RegisterManagedBean rmb = (RegisterManagedBean) fc.getExternalContext().getSessionMap().get("connexionBean");
            rmb.setEmployeur(employeurEJB.getEmployeurByMail(employeur.getMail()));
                    
            next = "profilEmpl";
        }
                          
        return next;
        
    }
    
    public String rechercher() {
        setDebut(false);
        setExisteEntreprise(entrepriseEJB.existByName(getNom()));
        if(entrepriseEJB.existByName(getNom())) {
            setExisteEntreprise(true);
            Entreprise ent = entrepriseEJB.getEntrepriseByNom(getNom());
            setStatut(ent.getStatut());
            setDomaine(ent.getDomaine());
            setRue(ent.getAdresse().getRue());
            setCp(ent.getAdresse().getCodePostal());
            setVille(ent.getAdresse().getVille());
            setPays(ent.getAdresse().getPays());
            setTel(ent.getTelephone());
            setDesc(ent.getDescription());
            setNbEmp(ent.getNbEmployes());
        } else {
            setExisteEntreprise(false);
        }
        return null;
    }
    
    public String modifier() {
        
         String next = "modifierEmployeur";
        
         FacesContext fc = FacesContext.getCurrentInstance();
         RegisterManagedBean rmb = (RegisterManagedBean) fc.getExternalContext().getSessionMap().get("connexionBean");
         Employeur emp = rmb.getEmployeur();
        
         if(emp != null) {
            Entreprise ent = emp.getEntreprise();
            if(!mdp3.equals(emp.getMdp())) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"Erreur", "Mot de passe incorrect"));
            } else if(nonValideString(rue) || nonValideString(ville) || nonValideString(pays)) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"Erreur", "Vous devez renseigner votre adresse"));  
                return  null;
            } else {
                
                boolean vide = (rue==null || rue.length()==0) && (ville==null || ville.length()==0) && (cp==null || cp.length()==0) && (pays==null || pays.length()==0);
                boolean remplie = (rue!=null && rue.length()!=0) && (ville!=null && ville.length()!=0) && (cp!=null && cp.length()!=0) && (pays!=null && pays.length()!=0);
        
                if(!vide && !remplie) {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"Erreur", "L'adresse doit être vide ou remplie complètement"));  
                    return null;
                } 
                
                if(!domaine.equals("")) {
                    ent.setDomaine(getDomaine());
                }
                if(!statut.equals("")) {
                    ent.setStatut(getStatut());
                }
                if(!rue.equals("")) {
                    ent.getAdresse().setRue(getRue());
                }
                if(!cp.equals("")) {
                    ent.getAdresse().setCodePostal(getCp());
                }
                if(!pays.equals("")) {
                    ent.getAdresse().setPays(getPays());
                }
                if(!ville.equals("")) {
                    ent.getAdresse().setVille(getVille());
                }
                if(!tel.equals("")) {
                    ent.setTelephone(getTel());
                }
                if(!site.equals("")) {
                    ent.setSiteWeb(getSite());
                }
                if(!desc.equals("")) {
                    ent.setDescription(getDesc());
                }
                if(!nbEmp.equals("")) {
                    ent.setNbEmployes(getNbEmp());
                }
                
                if(!telRec.equals("")) {
                    emp.setTelephone(getTelRec());
                }
                if(!mdp1.equals("")) {
                    if(!mdp1.equals(mdp2)) {
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"Erreur", "Les mots de passe ne sont pas identiques"));  
                    } else if (getMdp1().length() < 6) {
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"Erreur", "Mot de passe trop court")); 
                    } else if (!isConforme(mdp1)) {
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"Erreur", "Doit contenir un chiffre")); 
                    } else {
                        emp.setMdp(getMdp1());
                        emp.setConfidentialite(isConfidentialite());

                        employeurEJB.change(emp);  
                        entrepriseEJB.change(ent);
                        next = "profilEmpl";
                    }
                } else {
                    emp.setConfidentialite(isConfidentialite());

                    employeurEJB.change(emp);  
                    entrepriseEJB.change(ent);
                    next = "donneesEmpl";
                }
            }
         }   
         
         return next;
    }
    
    public void fill(Employeur emp) {
        //String next = "accueil";
        /**FacesContext fc = FacesContext.getCurrentInstance();
        RegisterManagedBean rmb = (RegisterManagedBean) fc.getExternalContext().getSessionMap().get("connexionBean");
        Employeur emp = rmb.getEmployeur();
                  
        if(emp != null) {*/
            Entreprise ent = emp.getEntreprise();
            setNom(ent.getNomEntreprise());
            setStatut(ent.getStatut());
            setDomaine(ent.getDomaine());
            setRue(ent.getAdresse().getRue());
            setCp(ent.getAdresse().getCodePostal());
            setVille(ent.getAdresse().getVille());
            setPays(ent.getAdresse().getPays());
            setTel(ent.getTelephone());
            setDesc(ent.getDescription());
            setNbEmp(ent.getNbEmployes());
            setSite(ent.getSiteWeb());
            //next = "modifierEmployeur";
            
            setMail1(emp.getMail());
            setNomRec(emp.getNom());
            setPrenomRec(emp.getPrenom());
            setTelRec(emp.getTelephone());
            
            setConfidentialite(emp.isConfidentialite());
         //}
        
        //return next;
    }
    
    public boolean isConforme(String mdp) {
        boolean valid = mdp.contains("0") || mdp.contains("1") || mdp.contains("2") || mdp.contains("3") || mdp.contains("4") 
               || mdp.contains("5") || mdp.contains("6") || mdp.contains("7") || mdp.contains("8") || mdp.contains("9");
        return valid;
    }

    /**
     * @return the emps
     */
    public ArrayList<String> getEmps() {
        return emps;
    }

    /**
     * @param emps the emps to set
     */
    public void setEmps(ArrayList<String> emps) {
        this.emps = emps;
    }

    /**
     * @return the nom
     */
    public String getNom() {
        return nom;
    }

    /**
     * @param nom the nom to set
     */
    public void setNom(String nom) {
        this.nom = nom;
    }

    /**
     * @return the statut
     */
    public String getStatut() {
        return statut;
    }

    /**
     * @param statut the statut to set
     */
    public void setStatut(String statut) {
        this.statut = statut;
    }

    /**
     * @return the domaine
     */
    public String getDomaine() {
        return domaine;
    }

    /**
     * @param domaine the domaine to set
     */
    public void setDomaine(String domaine) {
        this.domaine = domaine;
    }

    /**
     * @return the rue
     */
    public String getRue() {
        return rue;
    }

    /**
     * @param rue the rue to set
     */
    public void setRue(String rue) {
        this.rue = rue;
    }

    /**
     * @return the cp
     */
    public String getCp() {
        return cp;
    }

    /**
     * @param cp the cp to set
     */
    public void setCp(String cp) {
        this.cp = cp;
    }

    /**
     * @return the ville
     */
    public String getVille() {
        return ville;
    }

    /**
     * @param ville the ville to set
     */
    public void setVille(String ville) {
        this.ville = ville;
    }

    /**
     * @return the pays
     */
    public String getPays() {
        return pays;
    }

    /**
     * @param pays the pays to set
     */
    public void setPays(String pays) {
        this.pays = pays;
    }

    /**
     * @return the tel
     */
    public String getTel() {
        return tel;
    }

    /**
     * @param tel the tel to set
     */
    public void setTel(String tel) {
        this.tel = tel;
    }

    /**
     * @return the site
     */
    public String getSite() {
        return site;
    }

    /**
     * @param site the site to set
     */
    public void setSite(String site) {
        this.site = site;
    }

    /**
     * @return the desc
     */
    public String getDesc() {
        return desc;
    }

    /**
     * @param desc the desc to set
     */
    public void setDesc(String desc) {
        this.desc = desc;
    }

    /**
     * @return the nbEmp
     */
    public String getNbEmp() {
        return nbEmp;
    }

    /**
     * @param nbEmp the nbEmp to set
     */
    public void setNbEmp(String nbEmp) {
        this.nbEmp = nbEmp;
    }

    /**
     * @return the nomRec
     */
    public String getNomRec() {
        return nomRec;
    }

    /**
     * @param nomRec the nomRec to set
     */
    public void setNomRec(String nomRec) {
        this.nomRec = nomRec;
    }

    /**
     * @return the prenomRec
     */
    public String getPrenomRec() {
        return prenomRec;
    }

    /**
     * @param prenomRec the prenomRec to set
     */
    public void setPrenomRec(String prenomRec) {
        this.prenomRec = prenomRec;
    }

    /**
     * @return the telRec
     */
    public String getTelRec() {
        return telRec;
    }

    /**
     * @param telRec the telRec to set
     */
    public void setTelRec(String telRec) {
        this.telRec = telRec;
    }

    /**
     * @return the mail1
     */
    public String getMail1() {
        return mail1;
    }

    /**
     * @param mail1 the mail1 to set
     */
    public void setMail1(String mail1) {
        this.mail1 = mail1;
    }

    /**
     * @return the mail2
     */
    public String getMail2() {
        return mail2;
    }

    /**
     * @param mail2 the mail2 to set
     */
    public void setMail2(String mail2) {
        this.mail2 = mail2;
    }

    /**
     * @return the mdp1
     */
    public String getMdp1() {
        return mdp1;
    }

    /**
     * @param mdp1 the mdp1 to set
     */
    public void setMdp1(String mdp1) {
        this.mdp1 = mdp1;
    }

    /**
     * @return the mdp2
     */
    public String getMdp2() {
        return mdp2;
    }

    /**
     * @param mdp2 the mdp2 to set
     */
    public void setMdp2(String mdp2) {
        this.mdp2 = mdp2;
    }

    /**
     * @return the mdp3
     */
    public String getMdp3() {
        return mdp3;
    }

    /**
     * @param mdp3 the mdp3 to set
     */
    public void setMdp3(String mdp3) {
        this.mdp3 = mdp3;
    }

    /**
     * @return the confidentialite
     */
    public boolean isConfidentialite() {
        return confidentialite;
    }

    /**
     * @param confidentialite the confidentialite to set
     */
    public void setConfidentialite(boolean confidentialite) {
        this.confidentialite = confidentialite;
    }

    /**
     * @return the inscrit
     */
    public boolean isInscrit() {
        return inscrit;
    }

    /**
     * @param inscrit the inscrit to set
     */
    public void setInscrit(boolean inscrit) {
        this.inscrit = inscrit;
    }

    /**
     * @return the existeEntreprise
     */
    public boolean isExisteEntreprise() {
        return existeEntreprise;
    }

    /**
     * @param existeEntreprise the existeEntreprise to set
     */
    public void setExisteEntreprise(boolean existeEntreprise) {
        this.existeEntreprise = existeEntreprise;
    }

    /**
     * @return the debut
     */
    public boolean isDebut() {
        return debut;
    }

    /**
     * @param debut the debut to set
     */
    public void setDebut(boolean debut) {
        this.debut = debut;
    }


}
