/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package control;

import java.util.ArrayList;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;



public class SessionListener implements HttpSessionListener{

    @Override
    public void sessionCreated(HttpSessionEvent se) {
      
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent se) {
        ServletContext servletContext = se.getSession().getServletContext();
        ArrayList<String> liste = (ArrayList<String>) servletContext.getAttribute("listeUtilisateursConnecte");
        HttpSession session = se.getSession();
        String u = (String) session.getAttribute("pseudoUtilisateur");
       if(u != null ){
           liste.remove(u);
           System.out.println("Deconnexion " + u);
       }
    }
       
    
}
