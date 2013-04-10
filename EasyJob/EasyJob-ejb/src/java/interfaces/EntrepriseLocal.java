/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package interfaces;

import javax.ejb.Local;
import persistence.Entreprise;



/**
 *
 * @author celie
 */
@Local
public interface EntrepriseLocal {

    
    public Entreprise getEntrepriseByNom(String nom);

}
