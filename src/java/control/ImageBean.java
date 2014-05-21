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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;
import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;
import model.Galerie;
import model.Image;
import model.Utilisateur;
import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;

/**
 *
 * @author Yan
 */
@ManagedBean
@Named(value = "imageBean")
@RequestScoped
public class ImageBean {

    @PersistenceContext(unitName = "DateRoulettePU")
    private EntityManager em;
    @Resource
    private UserTransaction ut;
    
    //@ManagedProperty(value = "#{param.nom}")
    private String nomGalerie;
    
    /**
     * Galerie de l'image
     */
    private Galerie galerie;
    
    /**
     * Nom de l'image a ajouter
     */
    private String nomImage;
    
    /**
     * Image en question
     */
    private Image image;
    
    private Utilisateur utilisateur;
    
    @ManagedProperty(value = "#{file}")
    private Part file;
    
    /**
     * Motif signalement
     */
    private String motif;
    
    /**
     * Creates a new instance of ImageBean
     */
    public ImageBean() {
    }
    
    @PostConstruct
    public void init(){
        FacesContext context = FacesContext.getCurrentInstance();
        //System.err.println("id : "+context.getExternalContext().getRequestParameterMap().get("idImg"));
        if(context.getExternalContext().getRequestParameterMap().containsKey("idImg")){
            image = em.find(Image.class, Long.parseLong(context.getExternalContext().getRequestParameterMap().get("idImg")));
        }
    }
    
    public String ajouterImage() {
        FacesContext context = FacesContext.getCurrentInstance();
        utilisateur = getUtilisateurSession();
        if(galerie==null){
            /*System.err.println("bla : "+this.hashCode()+" "+context.getExternalContext().getRequestParameterMap().get("nom"));
            System.err.println("nomGalerie exist : "+context.getExternalContext().getRequestParameterMap().containsKey("nom"));*/
            nomGalerie=(String) context.getExternalContext().getRequestParameterMap().get("nom");
            //System.err.println("nomGalerie : "+nomGalerie);
            galerie = em.find(Galerie.class, nomGalerie);
            if(galerie==null){
                context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"Galerie inconnue",null));
                return "afficherGalerie.xhtml";
            }
        }
        if(nomImage.isEmpty())
            nomImage="Image sans nom";
        image = new Image();
        image.setNom(nomImage);
        image.setGalerie(galerie);
        image.setDate(new Date());
        image.setDescription("Ecrivez ici la description de votre image");
        try {
            Date d = new Date();
            DateFormat f = new SimpleDateFormat("d-M-y");
            upload();
            String path = ((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("resources/images/"+utilisateur.getPseudo()+"/"+nomGalerie+"/");
            image.setUrl(path.substring(path.indexOf("resources"))+"/"+this.utilisateur.getPseudo() + "_" + this.image.getNom()+"."+this.getTypeFile(file));

            ut.begin();
            em.persist(image);
            em.merge(galerie);
            ut.commit();
            
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Image ajoutée", null));

        } catch (NotSupportedException | SystemException | RollbackException 
                | HeuristicMixedException | HeuristicRollbackException | SecurityException 
                | IllegalStateException | IOException ex) {
            ex.printStackTrace();
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erreur lors du televersement de l'image", null));
        }
        
        return "afficherGalerie.xhtml";
        
    }
    
    public void upload() throws IOException {
        InputStream input = file.getInputStream();
        /*Date d = new Date();
        DateFormat f = new SimpleDateFormat("d-M-y");*/
        String path = ((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("resources/images/"+utilisateur.getPseudo()+"/"+nomGalerie+"/");
        File rep = new File(path);
        if(!rep.exists() || !rep.isDirectory())
            rep.mkdirs();
        File image = new File(path+"/"+ this.utilisateur.getPseudo() + "_" + this.image.getNom()+"."+this.getTypeFile(file));
        System.err.println("rep exist : "+image.getAbsolutePath());
        System.err.println("path : "+path);
        FileOutputStream output = new FileOutputStream(image);
        byte[] buf = new byte[1024];
        int len;
        while ((len = input.read(buf)) > 0) {
            output.write(buf, 0, len);
        }
        input.close();
        output.close();

    }
    
    public String getImagePath(){
        if(image!=null)
            return image.getUrl();
        else
            return "";
    }

    public Utilisateur getUtilisateurSession() {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpSession session = (HttpSession) context.getExternalContext().getSession(false);
        Utilisateur utilisateurSession = (Utilisateur) em.find(Utilisateur.class, (String) session.getAttribute("pseudoUtilisateur"));
        return utilisateurSession;
    }
    
    public void validateFile(FacesContext ctx, UIComponent comp, Object value) {
        List<FacesMessage> msgs = new ArrayList<>();
        Part file = (Part) value;
        /*if (file.getSize() > 1024) {
         msgs.add(new FacesMessage("file too big"));
         }*/
        if (!"image/jpeg".equals(file.getContentType())
                && !"image/png".equals(file.getContentType())) {
            msgs.add(new FacesMessage("Type not supported"));
        }
        
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

    public Galerie getGalerie() {
        return galerie;
    }

    public void setGalerie(Galerie galerie) {
        this.galerie = galerie;
    }
    
    public String getNomImage() {
        return nomImage;
    }

    public void setNomImage(String nomImage) {
        this.nomImage = nomImage;
    }

    public String getNomGalerie() {
        return nomGalerie;
    }

    public void setNomGalerie(String nomGalerie) {
        this.nomGalerie = nomGalerie;
    }

    public Image getI() {
        return image;
    }

    public void setI(Image i) {
        this.image = i;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public String getMotif() {
        return motif;
    }

    public void setMotif(String motif) {
        this.motif = motif;
    }
    
}
