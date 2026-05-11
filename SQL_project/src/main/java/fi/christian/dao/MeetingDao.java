package fi.christian.dao;

import fi.christian.Meeting;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.util.List;

public class MeetingDao implements IMeetingDao {
    private EntityManagerFactory emf;

    public MeetingDao(EntityManagerFactory emf) {
        this.emf = emf;
    }

    @Override
    public void save(Meeting meeting) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        em.merge(meeting);
        em.getTransaction().commit();
        em.close();
    }

    @Override
    public void update(Meeting meeting) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        em.merge(meeting);
        em.getTransaction().commit();
        em.close();
    }

    @Override
    public void delete(int id) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        Meeting m = em.find(Meeting.class, id);
        if (m != null) em.remove(m);
        em.getTransaction().commit();
        em.close();
    }

    @Override
    public Meeting findById(int id) {
        EntityManager em = emf.createEntityManager();
        try {
            return em.createQuery(
                "SELECT DISTINCT m FROM Meeting m LEFT JOIN FETCH m.participants LEFT JOIN FETCH m.room WHERE m.id = :id", 
                Meeting.class)
                .setParameter("id", id)
                .getSingleResult();
        } catch (javax.persistence.NoResultException e) {
            return null;
        } finally {
            em.close();
        }
    }

    @Override
    public List<Meeting> findAll() {
        EntityManager em = emf.createEntityManager();
        List<Meeting> meetings = em.createQuery(
            "SELECT DISTINCT m FROM Meeting m LEFT JOIN FETCH m.participants LEFT JOIN FETCH m.room", 
            Meeting.class).getResultList();
        em.close();
        return meetings;
    }
}
