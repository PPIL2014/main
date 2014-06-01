package model;

import java.util.ArrayList;

public class ListeAffinite {
    private ArrayList<Utilisateur> listeUtilisateur;
    private ArrayList<Affinite> listeAffinite;
    
    public ListeAffinite(){
        listeAffinite = new ArrayList<Affinite>();
        listeUtilisateur = new ArrayList<Utilisateur>();
    }
    
    public ArrayList<Affinite> getListeAffinite(){
        return listeAffinite;
    }
    
    public Utilisateur getCorrespondant(Utilisateur u1, ArrayList<Utilisateur> listeAttente)
    {
        Affinite max = null;
        for (Affinite a : listeAffinite)
        {
            Utilisateur u2 = null;
            if (a.getUtilisateur1().equals(u1))
                u2 = a.getUtilisateur2();
            else if (a.getUtilisateur2().equals(u2))
                u2 = a.getUtilisateur1();
            
            if (listeAttente.contains(u2))
            {
                if (max == null)
                    max = a;
                else if (a.getAffinite() > max.getAffinite())
                    max = a;
            }
        }
        
        if (max != null)
        {
            if (max.getUtilisateur1().equals(u1))
                return max.getUtilisateur2();
            else
                return max.getUtilisateur1();
        }
        return null;
    }
    
    public void ajouterUtilisateur(Utilisateur u1){
        if (!listeUtilisateur.contains(u1))
        {
            for (Utilisateur u2 : listeUtilisateur)
                listeAffinite.add(new Affinite(u1, u2));
            listeUtilisateur.add(u1);
        }
        
    }
    
    public void supprimerUtilisateur(Utilisateur u1){
        listeUtilisateur.remove(u1);
        ArrayList<Affinite> toDel = new ArrayList<>();
        for (Affinite a : listeAffinite)
            if (a.getUtilisateur1().equals(u1) || a.getUtilisateur2().equals(u1))
                toDel.add(a);
        listeAffinite.removeAll(toDel);
    }
}
