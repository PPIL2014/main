/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package control;

import model.Conversation;
import model.MessageConversation;

/**
 *
 * l'objet correspondant aux résumés de messages.
 * 
 * @author Renaud
 */
public class ResumeMessages 
{
    private String resume;
    
    public ResumeMessages (Conversation conv)
    {
        MessageConversation[] m = (MessageConversation[]) conv.getMessages().toArray();
        int i = m.length;
        this.resume= "";
        if (i>0)
            this.resume = this.resume + 
                    "("+m[0].getDate().toString()+") " +
                    m[0].getExpediteur().getPrenom() +
                    " : "  +m[0].getContenu().substring(0, 15);
        else
            this.resume = "aucun message";
        
        if (i>1)
            this.resume = this.resume + 
                    "("+m[1].getDate().toString()+") " +
                    m[1].getExpediteur().getPrenom() + " : "  +
                    m[1].getContenu().substring(0, 15);
        if (i>2)
            this.resume = this.resume + 
                    "("+m[2].getDate().toString()+") " +
                    m[2].getExpediteur().getPrenom() + " : "  +
                    m[2].getContenu().substring(0, 15);
    }
    
}
