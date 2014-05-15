/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package control;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;
import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;
import model.Image;
import model.Utilisateur;

/**
 *
 * @author hssaineelmahdi
 */
@ManagedBean
@RequestScoped
public class UploadBean {

    private Part file;

    @ManagedProperty(value = "#{param.pseudo}") // appelle setParam();
    private String param;

    @PersistenceContext(unitName = "DateRoulettePU")
    private EntityManager em;
    @Resource
    private UserTransaction ut;
    private Utilisateur utilisateur;

    public void setParam(String param) {
        this.param = param;
    }

    public String getParam() {
        return param;
    }
    
    public Utilisateur getUtilisateurSession () {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpSession session = (HttpSession) context.getExternalContext().getSession(false);
        Utilisateur utilisateurSession = (Utilisateur)em.find(Utilisateur.class,(String)session.getAttribute("pseudoUtilisateur")) ;
        return utilisateurSession;
    }

    public void upload() throws IOException {
        utilisateur = getUtilisateurSession();
        if(utilisateur.getAvatar() == null){
            Image avatar = new Image();
            avatar.setDate(new Date());
            avatar.setNom(this.utilisateur.getPseudo() + "."+ this.getTypeFile(file));
            avatar.setDescription("file://"+System.getProperty("user.home") + "/dateImages/" + this.utilisateur.getPseudo() + "." + this.getTypeFile(file));
        }
        else if (utilisateur != null) {
            utilisateur.getAvatar().setDate(new Date());
            utilisateur.getAvatar().setNom(this.utilisateur.getPseudo() + "."+ this.getTypeFile(file));
            utilisateur.getAvatar().setDescription("file://"+System.getProperty("user.home") + "/dateImages/" + this.utilisateur.getPseudo() + "." + this.getTypeFile(file));
            try {

                ut.begin();
                em.merge(utilisateur);
                ut.commit();

            } catch (NotSupportedException | SystemException | RollbackException | HeuristicMixedException | HeuristicRollbackException | SecurityException | IllegalStateException ex) {
                Logger.getLogger(EditProfilBean.class.getName()).log(Level.SEVERE, null, ex);
            }

            InputStream input = file.getInputStream();
            File image = new File(System.getProperty("user.home") + "/dateImages/" + this.utilisateur.getPseudo() + "." + this.getTypeFile(file));
            FileOutputStream output = new FileOutputStream(new File(image, ""));
            byte[] buf = new byte[1024];
            int len;
            while ((len = input.read(buf)) > 0) {
                output.write(buf, 0, len);
            }
            input.close();
            output.close();
        }

    }

    public void validateFile(FacesContext ctx, UIComponent comp, Object value) {
        List<FacesMessage> msgs = new ArrayList<FacesMessage>();
        Part file = (Part) value;
        /* if (file.getSize() > 1024) {
         msgs.add(new FacesMessage("file too big"));
         }*/
        /* if (!"image/jpeg".equals(file.getContentType())) {
         msgs.add(new FacesMessage("Type not supported"));
         }*/
        if (!msgs.isEmpty()) {
            throw new ValidatorException(msgs);
        }
    }

    public String getTypeFile(Part file) {
        String type = this.file.getContentType();
        type = type.split("/")[1];
        return type;
    }

    public Part getFile() {
        return file;
    }

    public void setFile(Part file) {
        this.file = file;
    }
}
