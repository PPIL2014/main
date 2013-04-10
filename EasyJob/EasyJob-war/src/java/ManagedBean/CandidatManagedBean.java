/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ManagedBean;

import interfaces.CandidatLocal;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.inject.Inject;
import persistence.Candidat;

/**
 *
 * @author margaux
 */
@ManagedBean
public class CandidatManagedBean implements Serializable {
    
    private Candidat candidat;
    
    @Inject
    CandidatLocal candidatEJB;
    
    @PostConstruct
    public void initialisation() {
        candidat = candidatEJB.getCandidatByMail("toto@toto.fr"); 
    }

    /**
     * Creates a new instance of CandidatManagedBean
     */
    public CandidatManagedBean() {
    }

    public Candidat getCandidat() {
        return candidat;
    }

    public void setCandidat(Candidat candidat) {
        this.candidat = candidat;
    }
    
}