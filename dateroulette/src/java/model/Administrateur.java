/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package model;

import java.io.Serializable;
import javax.persistence.Entity;

/**
 *
 * @author thomas
 */
@Entity
public class Administrateur extends Utilisateur implements Serializable {
    private static final long serialVersionUID = 1L;
    
}
