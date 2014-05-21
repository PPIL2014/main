/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.text.DecimalFormat;
import javax.inject.Named;
import javax.enterprise.context.Dependent;

@Named(value = "affinite")
@Dependent
public class Affinite {

    private Utilisateur utilisateur1 , utilisateur2 ;
    
    /*
     * le coefficient d'affinte est compris entre 0 et 1.
     */
    private double affinite ;
    
    public Affinite(Utilisateur u1 , Utilisateur u2) {
        utilisateur1 = u1 ;
        utilisateur2 = u2 ;
        
        //calcul de l'affinité
        affinite =  Math.round(Math.random()*100) / 100 ;
    }

    public Utilisateur getUtilisateur1() {
        return utilisateur1;
    }

    public void setUtilisateur1(Utilisateur utilisateur1) {
        this.utilisateur1 = utilisateur1;
    }

    public Utilisateur getUtilisateur2() {
        return utilisateur2;
    }

    public void setUtilisateur2(Utilisateur utilisateur2) {
        this.utilisateur2 = utilisateur2;
    }

    public double getAffinite() {
        return affinite;
    }

    public void setAffinite(double affinite) {
        this.affinite = affinite;
    }

    public boolean contientUtilisateur(Utilisateur u1) {
        if (utilisateur1.equals(u1))
            return true ;
        
        return utilisateur2.equals(u1) ;
    }
}
