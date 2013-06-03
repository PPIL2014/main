/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ManagedBean;

import interfaces.CandidatLocal;
import interfaces.EmployeurLocal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import persistence.Adresse;
import persistence.Candidat;

/**
 *
 * @author celie
 */
@ManagedBean(name = "creationCandBean")
@RequestScoped
public class CreationCandManagedBean {
    
    private ArrayList<Integer> listeJour;
    private ArrayList<Integer> listeMois;
    private ArrayList<Integer> listeAnnee;
    
    private String mail1;
    private String mail2;
    private String mdp1;
    private String mdp2;
    private String mdp3;
    
    private String nom;
    private String prenom;

    private int jour;
    private int mois;
    private int annee;
    
    private String telephone;
    private String numSecu;  
    private boolean confidentialite;
    private String sitWeb;
    private String niveauEtudes;
    private String domaineEtudes;
    private String experiences;
    
    private String langue1;
    private String langue2;
    private String langue3;
    
    private String nbExperiences;
    
    private String rue;
    private String codePostal;
    private String ville;
    private String pays;
    
    private boolean inscrit;
    
    @Inject
    private CandidatLocal candidaEJB;
    
    @Inject
    private EmployeurLocal employeurEJB;

    /**
     * Creates a new instance of CreationCandManagedBean
     */
    public CreationCandManagedBean() {
    }
    
    @PostConstruct
    public void initialisation() {
        
        listeJour = new ArrayList<Integer>();
        for(int i=1;i<32;i++) {
            listeJour.add(i);
        }
        
        listeMois = new ArrayList<Integer>();
        for(int i=1;i<13;i++) {
            listeMois.add(i);
        }
        
        Date date = new Date();
        listeAnnee = new ArrayList<Integer>();
        for(int i=1900+date.getYear()-14;i>1934;i--) {
            listeAnnee.add(i);
        }  
        
        FacesContext fc = FacesContext.getCurrentInstance();
        RegisterManagedBean rmb = (RegisterManagedBean) fc.getExternalContext().getSessionMap().get("connexionBean");
        Candidat can = rmb.getCandidat();
                  
        if(can != null) {
            fill(can);
        }

    }
    
    private boolean dateValide() {
        boolean valide = true;
        if(mois==2) {
            if((annee%4==0 && annee%100!=0) || annee%400==0) {
                valide = jour!=30 && jour!=31;
            } else {
                valide = jour!=29 && jour!=30 && jour!=31;
            }
        } else if(mois==4 || mois==6 || mois==9 || mois==11 ) { 
            valide = jour!=31;
        }
        return valide;
    }
    
    private boolean nonValideString(String s) {
        String str = "";
        for(int i=0; i<s.length(); i++) {
            str = str + " ";
        }
        return s.equals(str);
    }
    
    public String valider() {
        String next = "creationCand";
        if(!mail1.equals(mail2)) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"Erreur", "Les adresses mail ne sont pas identiques"));  
        } else if(!mdp1.equals(mdp2)) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"Erreur", "Les mots de passe ne sont pas identiques"));  
        } else if(candidaEJB.existeMail(mail1) || employeurEJB.existeMail(mail1)) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"Erreur", "L'adresse mail existe déjà"));  
        } else if(!dateValide()) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"Erreur", "La date n'est pas valide"));  
        } else if (nonValideString(nom) || nonValideString(prenom)) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"Erreur", "Vous devez renseigner vos nom et prénom"));  
        } else if(nonValideString(rue) || nonValideString(ville) || nonValideString(pays)) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"Erreur", "Vous devez renseigner votre adresse"));  
        }else {
            inscrit = true;
            Date date = new Date(annee-1900, mois-1, jour);
            Candidat candidat = new Candidat(nom, prenom, telephone, numSecu, date, confidentialite);
            candidat.setMail(mail1);
            candidat.setMdp(mdp1);
            Adresse adr = new Adresse(rue, codePostal, ville, pays);
            candidat.setAdresse(adr);
            candidat.setDomaineEtudes(domaineEtudes);
            candidat.setNbExperiences(Integer.parseInt(nbExperiences));
            candidat.setNiveauEtudes(niveauEtudes);
            candidat.setSitWeb(sitWeb);
            
            ArrayList<String> langues = new ArrayList<String>();
            if(langue1.length() != 0) {
                langues.add(langue1);
            } 
            if(langue2.length() != 0) {
                langues.add(langue2);
            } 
            if(langue3.length() != 0) {
                langues.add(langue3);
            } 
            candidat.setLangues(langues);
            
            if(experiences!=null && experiences.length()!=0) {
                candidat.setExperiences(exp());
            }
            
            candidaEJB.saveCandidat(candidat);
            FacesContext fc = FacesContext.getCurrentInstance();
            RegisterManagedBean rmb = (RegisterManagedBean) fc.getExternalContext().getSessionMap().get("connexionBean");
            rmb.setCandidat(candidaEJB.getCandidatByMail(candidat.getMail()));
            next = "profilCand";
        }
                    
        return next;
    }
    
    private ArrayList<String> exp() {
        ArrayList<String> exp = new ArrayList<String>();
        if(!experiences.contains("\n")) {
            exp.add(experiences);
        } else {
            int i = 0;
            int j = 0;
            while(i<experiences.length())  {
                if(experiences.charAt(i)=='\n') {
                    exp.add(experiences.substring(j, i));
                    j = i+1;
                }
                i++;
            }
                    
            exp.add(experiences.substring(j, i));
        }
        return exp;
    }
    
    public String modifier() {
        String next = "modifierCandidat";
        
         FacesContext fc = FacesContext.getCurrentInstance();
         RegisterManagedBean rmb = (RegisterManagedBean) fc.getExternalContext().getSessionMap().get("connexionBean");
         
         Candidat cand = rmb.getCandidat();
         
         if (cand != null) {
             if(!mdp3.equals(cand.getMdp())) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"Erreur", "Mot de passe incorrect"));
                return null;
             } else if (nonValideString(nom) || nonValideString(prenom)) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"Erreur", "Vous devez renseigner vos nom et prénom")); 
                return null;
             } else if(nonValideString(rue) || nonValideString(ville) || nonValideString(pays)) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"Erreur", "Vous devez renseigner votre adresse"));  
                return null;
             }else {
                if(!rue.equals("")) {
                    cand.getAdresse().setRue(getRue());
                }
                if(!codePostal.equals("")) {
                    cand.getAdresse().setCodePostal(getCodePostal());
                }
                if(!pays.equals("")) {
                    cand.getAdresse().setPays(getPays());
                }
                if(!ville.equals("")) {
                    cand.getAdresse().setVille(getVille());
                }
                if(!telephone.equals("")) {
                    cand.setTelephone(getTelephone());
                }
                if(!sitWeb.equals(cand.getSitWeb())) {
                    cand.setSitWeb(getSitWeb());
                }
                if(!niveauEtudes.equals(cand.getNiveauEtudes())) {
                    cand.setNiveauEtudes(getNiveauEtudes());
                }
                ArrayList<String> langues = new ArrayList<String>();
                if(langue1.length() != 0) {
                    langues.add(langue1);
                } 
                if(langue2.length() != 0) {
                    langues.add(langue2);
                } 
                if(langue3.length() != 0) {
                    langues.add(langue3);
                } 
                cand.setLangues(langues);
                
                cand.setDomaineEtudes(domaineEtudes);
                
                if(experiences!=null && experiences.length()!=0) {
                    cand.setExperiences(exp());
                }
                if(!nbExperiences.equals("")) {
                    cand.setNbExperiences(Integer.parseInt(nbExperiences));
                }
                cand.setConfidentialite(isConfidentialite());
                          
                if(!mdp1.equals("")) {
                    if(!mdp1.equals(mdp2)) {
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"Erreur", "Les mots de passe ne sont pas identiques"));  
                    } else if (getMdp1().length() < 6) {
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"Erreur", "Mot de passe trop court")); 
                    } else if (!isConforme(mdp1)) {
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"Erreur", "Le mot de passe doit contenir un chiffre")); 
                    } else {
                        cand.setMdp(getMdp1());

                        candidaEJB.majCandidat(cand) ;
                        next = "donneesCand";
                    }
                } else {
                    candidaEJB.majCandidat(cand);
                    next = "donneesCand";
                }
             }
         }
         
        return next;
    }
    
    public void fill(Candidat c) {
        setNom(c.getNom());
        setPrenom(c.getPrenom());
        setNumSecu(c.getNumSecu());
        setRue(c.getAdresse().getRue());
        setCodePostal(c.getAdresse().getCodePostal());
        setPays(c.getAdresse().getPays());
        setVille(c.getAdresse().getVille());
        setTelephone(c.getTelephone());
        setSitWeb(c.getSitWeb());
        setNiveauEtudes(c.getNiveauEtudes());
        setDomaineEtudes(c.getDomaineEtudes());
        setExperiences(corrigeString(c.getExperiences()));
        setNbExperiences(""+c.getNbExperiences());
        setMail1(c.getMail());
        
        if(c.getLangues().size() > 0 && c.getLangues().get(0) != null) {
            setLangue1(c.getLangues().get(0));
            if(c.getLangues().size() > 1 && c.getLangues().get(1) != null) {
                setLangue2(c.getLangues().get(1));
                if(c.getLangues().size() > 2 && c.getLangues().get(2) != null) {
                    setLangue3(c.getLangues().get(2));
                }
            }
        }
    }
    
    public String corrigeString(List<String> s) {
        String str = "";
        for(String st : s) {
            str = str + st ;
        }
        return str;
    }
    
    public boolean isConforme(String mdp) {
        boolean valid = mdp.contains("0") || mdp.contains("1") || mdp.contains("2") || mdp.contains("3") || mdp.contains("4") 
               || mdp.contains("5") || mdp.contains("6") || mdp.contains("7") || mdp.contains("8") || mdp.contains("9");
        return valid;
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
     * @return the prenom
     */
    public String getPrenom() {
        return prenom;
    }

    /**
     * @param prenom the prenom to set
     */
    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    /**
     * @return the jour
     */
    public int getJour() {
        return jour;
    }

    /**
     * @param jour the jour to set
     */
    public void setJour(int jour) {
        this.jour = jour;
    }

    /**
     * @return the mois
     */
    public int getMois() {
        return mois;
    }

    /**
     * @param mois the mois to set
     */
    public void setMois(int mois) {
        this.mois = mois;
    }

    /**
     * @return the annee
     */
    public int getAnnee() {
        return annee;
    }

    /**
     * @param annee the annee to set
     */
    public void setAnnee(int annee) {
        this.annee = annee;
    }

    /**
     * @return the telephone
     */
    public String getTelephone() {
        return telephone;
    }

    /**
     * @param telephone the telephone to set
     */
    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    /**
     * @return the numSecu
     */
    public String getNumSecu() {
        return numSecu;
    }

    /**
     * @param numSecu the numSecu to set
     */
    public void setNumSecu(String numSecu) {
        this.numSecu = numSecu;
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
     * @return the sitWeb
     */
    public String getSitWeb() {
        return sitWeb;
    }

    /**
     * @param sitWeb the sitWeb to set
     */
    public void setSitWeb(String sitWeb) {
        this.sitWeb = sitWeb;
    }

    /**
     * @return the niveauEtudes
     */
    public String getNiveauEtudes() {
        return niveauEtudes;
    }

    /**
     * @param niveauEtudes the niveauEtudes to set
     */
    public void setNiveauEtudes(String niveauEtudes) {
        this.niveauEtudes = niveauEtudes;
    }

    /**
     * @return the domaineEtudes
     */
    public String getDomaineEtudes() {
        return domaineEtudes;
    }

    /**
     * @param domaineEtudes the domaineEtudes to set
     */
    public void setDomaineEtudes(String domaineEtudes) {
        this.domaineEtudes = domaineEtudes;
    }

    /**
     * @return the experiences
     */
    public String getExperiences() {
        return experiences;
    }

    /**
     * @param experiences the experiences to set
     */
    public void setExperiences(String experiences) {
        this.experiences = experiences;
    }

    /**
     * @return the nbExperiences
     */
    public String getNbExperiences() {
        return nbExperiences;
    }

    /**
     * @param nbExperiences the nbExperiences to set
     */
    public void setNbExperiences(String nbExperiences) {
        this.nbExperiences = nbExperiences;
    }


    /**
     * @return the listeJour
     */
    public ArrayList<Integer> getListeJour() {
        return listeJour;
    }

    /**
     * @param listeJour the listeJour to set
     */
    public void setListeJour(ArrayList<Integer> ListeJour) {
        this.listeJour = ListeJour;
    }

    /**
     * @return the listeMois
     */
    public ArrayList<Integer> getListeMois() {
        return listeMois;
    }

    /**
     * @param listeMois the listeMois to set
     */
    public void setListeMois(ArrayList<Integer> listeMois) {
        this.listeMois = listeMois;
    }

    /**
     * @return the listeAnnee
     */
    public ArrayList<Integer> getListeAnnee() {
        return listeAnnee;
    }

    /**
     * @param listeAnnee the listeAnnee to set
     */
    public void setListeAnnee(ArrayList<Integer> listeAnnee) {
        this.listeAnnee = listeAnnee;
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
     * @return the codePostal
     */
    public String getCodePostal() {
        return codePostal;
    }

    /**
     * @param codePostal the codePostal to set
     */
    public void setCodePostal(String codePostal) {
        this.codePostal = codePostal;
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
     * @return the langue1
     */
    public String getLangue1() {
        return langue1;
    }

    /**
     * @param langue1 the langue1 to set
     */
    public void setLangue1(String langue1) {
        this.langue1 = langue1;
    }

    /**
     * @return the langue2
     */
    public String getLangue2() {
        return langue2;
    }

    /**
     * @param langue2 the langue2 to set
     */
    public void setLangue2(String langue2) {
        this.langue2 = langue2;
    }

    /**
     * @return the langue3
     */
    public String getLangue3() {
        return langue3;
    }

    /**
     * @param langue3 the langue3 to set
     */
    public void setLangue3(String langue3) {
        this.langue3 = langue3;
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


}
