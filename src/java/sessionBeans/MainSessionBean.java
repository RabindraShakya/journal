/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sessionBeans;

import entities.Journal;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.persistence.Query;

/**
 *
 * @author RaBnHooD
 */
@Stateless
public class MainSessionBean {

    @PersistenceContext
    private EntityManager em;

    public List<Journal> getAllJournals() {
        System.out.println("em::"+em);
        Query query = em.createQuery("Select * from journalentries");
        return query.getResultList();
    }
    
    public void addJournal(Journal obj){
        em.persist(obj);
    }

}
