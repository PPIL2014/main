
import java.util.ArrayList;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;
import model.Contact;
import model.FAQ;
import model.Utilisateur;


/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 * Web application lifecycle listener.
 *
 * @author nicolas
 */
public class NewServletListener implements ServletContextListener  {

    @PersistenceContext(unitName = "DateRoulettePU")
    private EntityManager em;
    @Resource
    private UserTransaction ut;

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        

            Utilisateur medi = new Utilisateur();
            medi.setPseudo("medi");
            medi.setMdp("mdp");
            medi.setPrenom("medi2");
            medi.setNom("huss");
            medi.setAdresse("3 rue");
         //   medi.setDdn(1990, 10, 01);
            medi.setMail("@coco.fr");
            medi.setSexe("H");
            medi.setTelephone("06");


            Utilisateur aziz = new Utilisateur();
            aziz.setPseudo("aziz");
            aziz.setMdp("mdp");
            aziz.setPrenom("aziz");
            aziz.setNom("aziz");
            aziz.setAdresse("3 rue");
        //    aziz.setDdn(1990, 10, 01);
            aziz.setMail("@mail");
            aziz.setSexe("H");
            aziz.setTelephone("06");
            aziz.setAdministrateur(Boolean.TRUE);
            
             /* Session session = new Session();
                session.setEstConnecte(Boolean.FALSE);
                medi.setSession(session);
                
                 Session session2 = new Session();
                session2.setEstConnecte(Boolean.FALSE);
                aziz.setSession(session2);*/

        
           Contact c1 = new Contact();
           c1.setType(Contact.Type.ENATTENTE);
           c1.setEstEnContactAvec(aziz);
           Collection cc1 = new ArrayList<Contact>(); 
           cc1.add(c1);
           medi.setContacts(cc1);
           
           Contact c2 = new Contact();
           c2.setType(Contact.Type.DEMANDE);
           c2.setEstEnContactAvec(medi);
           Collection cc2 = new ArrayList<Contact>(); 
           cc2.add(c2);
           aziz.setContacts(cc2);

           
           
           FAQ q1 = new FAQ();
           q1.setQuestionFAQ("Comment puis-je m'inscrire ?");
           q1.setReponseFAQ("Pour vous inscrire cliquez sur le lien Inscription de la page d'accueil\n" +
    "Il vous sera demander votre nom, prenom, pseudo ainsi qu'une adresse mail (valide).");
           
           FAQ q2 = new FAQ();
           q2.setQuestionFAQ("Comment puis-je modifier mon profil ?");
           q2.setReponseFAQ("Accedez à votre profil et cliquez sur le bouton Modifier profil. Vous aurez alors accès à un formulaire avec les champs préremplis par vos anciennes indications. Vous n'aurez qu'à les modifier et cliquer ensuite sur enregistrer. (Voir si les noms de boutons sont corrects).");
           
           FAQ q3 = new FAQ();
           q3.setQuestionFAQ("Comment améliorer la precision de mes affinités ?");
           q3.setReponseFAQ("Pour ameliorer la precision de vos affinités, il vous suffit de repondre aux questions des différents questionnaires mis à disposition par le site. Pour repondre à ces questionnaires, visitez ce lien (ATTENTION : vous devez être connecté pour y avoir accès)");
           
           FAQ q4 = new FAQ();
           q4.setQuestionFAQ("Comment acceder au profil d'un autre utilisateur ?");
           q4.setReponseFAQ("Vous ne pouvez accéder au profil d'un autre utilisateur que s'il vous a envoyé une invitation.\n" +
    "Ensuite, il vous suffit de cliquer sur son nom dans votre liste d'amis.");
           
           FAQ q5 = new FAQ();
           q5.setQuestionFAQ("Comment puis-je avertir l'administrateur d'un abus (profil ou photo) ?");
           q5.setReponseFAQ("Vous pouvez signaler une photo en accedant à la Galerie d'un autre utilisateur et en visionnant la photo en question. Vous verrez un bouton indiquant \"Signaler\". Vous n'aurez qu'à cliquer sur celui-ci puis à donner un motif pour le signalement. Un administrateur s'occupant regulièrement des signalements de photos pourra alors la voir apparaitre comme photo signalée.\n" +
    "Il jugera de la justesse du signalement : \n" +
    "-Suppression si la photo est considéré comme un abus\n" +
    "-Relaxe si elle ne n'est pas considéré comme irrespectueuse des règles du site.");
           
           FAQ q6 = new FAQ();
           q6.setQuestionFAQ("Comment puis-je bloquer/débloquer un utilisateur ?");
           q6.setReponseFAQ("Sur le profil d'un utilisateur, il vous suffit de cliquer sur le bouton Bloquer. Vous ne pourrez plus envoyer ou recevoir de messages de cette personne.\n" +
    "Pour la débloquer, sur la liste de personne que vous avez bloqué, accessible depuis la page d'accueil, cliquez sur le bouton Débloquer à coté du nom de la personne concernée.");
           
           FAQ q7 = new FAQ();
           q7.setQuestionFAQ("Comment puis-je voir les photos d'un ami ?");
           q7.setReponseFAQ("En accedant au profil de l'utilisateur en question (voir Comment acceder au profil d'un autre utilisateur ?).\n" +
    "Vous n'aurez qu'à cliquez sur Visiter la Galerie Photos.");
           
           FAQ q8 = new FAQ();
           q8.setQuestionFAQ("Comment puis-je ajouter/supprimer une/des photo(s) dans ma galerie ?");
           q8.setReponseFAQ("Depuis la page Galerie de photos, vous pouvez ajouter des photos en cliquant sur le lien Ajouter Photo.");
           
           FAQ q9 = new FAQ();
           q9.setQuestionFAQ("Je veux faire des rencontres, comment faire ?");
           q9.setReponseFAQ("Il vous suffit d'acceder au chat disponible depuis la page d'accueil du site (lorsque vous êtes connectés). Vous aurez alors le choix entre plusieurs mode de Chat : \n" +
    "\n" +
    "- Mode avec affinités :\n" +
    "Ce mode permet d'utiliser les reponses aux questions des différents questionnaires auxquels vous avez repondu afin de trouver des personnes ayant des affinités les plus similaires aux votres.\n" +
    "\n" +
    "- Mode 60 secondes chrono :\n" +
    "Si vous aimez le challenge, les courtes conversations ou ecnore entrer directement dans le vif du sujet, vous pouvez utiliser le mode 60 secondes chrono. Il consiste à la dematerialisation de la conversation du chat par la limitation de la duree de celle-ci, à savoir une limitation de 60 secondes, et par l'utilisation de la technologie sans support matériel fixes appelée Virtuel ou plus communément Le monde Virtuel. Vous aurez donc 60 secondes pour parler, draguer, rire, pleurer, bref seduire la personne avec qui vous serez en contact. Au bout de ces 60 secondes, il sera demander à vous même et à votre interlocuteur si vous voulez vous ajouter en ami. Selon votre prestation votre interlocuteur pourra repondre oui ou non, et vice versa.\n" +
    "\n" +
    "- Mode sans affinités :\n" +
    "Ce mode est un chat banal, sans limitation de duree et sans recherche de personne ayant plus d'affinités avec vous (les reponses que vous avez fourni aux questionnaires ne seront pas prises en compte). Cela vous permet ainsi d'élargir vos horizons et de ne pas être nécessairement guidé par vos reponses aux questionnaires.");
           
           FAQ q10 = new FAQ();
           q10.setQuestionFAQ("Comment consulter ou envoyer des messageries privées ?");
           q10.setReponseFAQ("Depuis n'importe où, il suffit de trouver le bouton Acceder à ma messagerie");
           
           FAQ q11 = new FAQ();
           q11.setQuestionFAQ("Comment desactiver/supprimer son compte ?");
           q11.setReponseFAQ("Sur votre profil, un bouton Gestion permet d'accéder à ces fonctionnalités.\n" +
    "Ensuite, il vous suffit de cliquez sir le bouton qui concerne ce que vous souhaitez faire, et confirmer l'action.");
                  
           FAQ q12 = new FAQ();
           q12.setQuestionFAQ("Comment me connecter ?");
           q12.setReponseFAQ("Sur la page d'accueil du site, il vous suffit de renseigner les champs Pseudo et Mot de passe et de cliquez sur Connexion.");
           
           FAQ q13 = new FAQ();
           q13.setQuestionFAQ("Comment retrouver mes identifiants ?");
           q13.setReponseFAQ("Sur la page de connexion, cliquez sur Identifiants oubliés ?, puis renseignez votre adresse mail. Vos identifiants vous seront alors renvoyez dans les plus bref délais.");
           
           FAQ q14 = new FAQ();
           q14.setQuestionFAQ("Mon compte a été bloqué, que puis-je faire ?");
           q14.setReponseFAQ("Sur votre page de profil, vous pouvez envoyer un message à un administrateur expliquant votre situation.\n" +
    "S'il juge que la raison de votre blocage n'était pas justifiée, ou s'il souhaite vous accorder une seconde chance, il pourra vous débloquez.\n" +
    "N'oubliez cependant pas que vous avez été bloqué pour une raison. Le respect est donc de mise.");
           
           FAQ q15 = new FAQ();
           q15.setQuestionFAQ("Ma question ne se trouve pas ici ?");
           q15.setReponseFAQ("Un formulaire de la page Support permet d'envoyer un message à un administrateur qui vous répondra dans les plus brefs délais.");
       
     
       

            try {
                
                ut.begin();
                
                em.persist(medi);
                em.persist(aziz);
                
                 ut.commit();
                 
            }catch (NotSupportedException | SystemException | RollbackException | HeuristicMixedException | HeuristicRollbackException | SecurityException | IllegalStateException ex) {
                Logger.getLogger(NewServletListener.class.getName()).log(Level.SEVERE, null, ex);
            }
              try {
            //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            
                 ut.begin();
                em.persist(q1);
                em.persist(q2);
                em.persist(q3);   
                em.persist(q4);
                em.persist(q5);
                em.persist(q6);
                em.persist(q7);
                em.persist(q8);
                em.persist(q9);
                em.persist(q10);
                em.persist(q11);
                em.persist(q12);
                em.persist(q13);
                em.persist(q14);
                em.persist(q15);
                ut.commit();
        }catch (NotSupportedException | SystemException | RollbackException | HeuristicMixedException | HeuristicRollbackException | SecurityException | IllegalStateException ex) {
                Logger.getLogger(NewServletListener.class.getName()).log(Level.SEVERE, null, ex);
            }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
