/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package autoComplete;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

/**
 *
 * @author Yann
 */
@ManagedBean(name = "autoCompleteManagedBean")
@RequestScoped
public class AutoCompleteManagedBean {
    
    /**
     * Creates a new instance of AutoCompleteManagedBean
     */
    public AutoCompleteManagedBean() {
    }
    
    public String affichageDate(Date d) {
        SimpleDateFormat da = new SimpleDateFormat("dd/MM/yyyy");
        if(d!=null) {
            return da.format(d);
        } else {
            return null;
        }
    }
    
    public ArrayList<String> getStatutEntreprise(){
        ArrayList<String> liste = new ArrayList<String>();
        liste.add("EURL");
        liste.add("SARL");
        liste.add("SAS");
        liste.add("SA");
        
        return liste;
    }
    
    public ArrayList<String> getNombreHeures(){
        ArrayList<String> liste = new ArrayList<String>();
        for(int i=0;i<36;i++){
            liste.add(""+i);
        }
        return liste;
        
    }
    
    public ArrayList<String> getNiveauEtude(){
        ArrayList<String> liste = new ArrayList<String>();
        liste.add("Aucun");
        liste.add("CAP");
        liste.add("BAC");
        for(int i=0;i<8;i++){
            liste.add("BAC+"+(i+1));
        }
        return liste;
    }
    
    public ArrayList<String> getSalaire(){
        ArrayList<String> liste = new ArrayList<String>();
        for(int i=0; i<10; i++){
            liste.add(""+i*500);
        }
        return liste;
        
    }
    
    public ArrayList<String> getExperience(){
        ArrayList<String> liste = new ArrayList<String>();
        for(int i=0; i<20; i++){
            liste.add(""+i);
        }
        return liste;
        
    }
    public ArrayList<String> getContrat(){
        String contrat[] = {   
        "CDD alternance",
        "CDD insertion",
        "CDD senior",
        "CDD tout public",
        "CDI alternance",
        "CDI insertion",
        "CDI tout public",
        "Contrat de travail intermittent",
        "Contrat de travail saisonnier",
        "Franchise",
        "Mission d interim",
        "Profession commerciale",
        "Profession liberale",
        "Reprise d entreprise"
        };
        ArrayList<String> liste = new ArrayList<String>();
        for(String i : contrat){
            liste.add(i);
        }
        return liste;
        
    }
    public ArrayList<String> getDomaineActivite(){
        String domaineA[] = {            
    "Industries alimentaires",
    "Fabrication de textiles",
    "Industrie de l habillement",
    "Industrie chimique",
    "Industrie pharmaceutique",
    "Industrie automobile",
    "Restauration",
    "Edition",
    "Cinema",
    "Programmation et diffusion",
    "Telecommunications",
    "Informatique",
    "Services d information",
    "Assurance",
    "Activites juridiques et comptables",
    "Activites d architecture ",
    "Recherche scientifique",
    "Publicite et etudes de marche",
    "Activites veterinaires",
    "Activites des agences de voyage",
    "Enquetes et sécurite",
    "Administration publique et défense",
    "Enseignement",
    "Sante",
    "Activites culturelles",
    "Activites sportives et de loisirs",
    "Activites associatives",
    "Autres services personnels",
    "Autres"};
        
        ArrayList<String> liste = new ArrayList<String>();
        for(String i : domaineA){
            liste.add(i);
        }
        return liste;
    }
    
    

}
