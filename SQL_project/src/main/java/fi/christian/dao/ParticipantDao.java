package fi.christian.dao;

import fi.christian.Participant;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import java.util.List;

public class ParticipantDao implements IParticipantDao {
    private EntityManagerFactory emf;

    public ParticipantDao(EntityManagerFactory emf) {
        this.emf = emf;
    }

    @Override
    public void save(Participant participant) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        em.persist(participant);
        em.getTransaction().commit();
        em.close();
    }

    @Override
    public void update(Participant participant) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        em.merge(participant);
        em.getTransaction().commit();
        em.close();
    }

    @Override
    public void delete(int id) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        Participant p = em.find(Participant.class, id);
        if (p != null) em.remove(p);
        em.getTransaction().commit();
        em.close();
    }

    @Override
    public Participant findByEmail(String email) {
        EntityManager em = emf.createEntityManager();
        try {
            return em.createQuery("SELECT p FROM Participant p WHERE p.email = :email", Participant.class)
                    .setParameter("email", email)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        } finally {
            em.close();
        }
    }

    @Override
    public Participant findById(int id) {
        EntityManager em = emf.createEntityManager();
        Participant p = em.find(Participant.class, id);
        em.close();
        return p;
    }

    @Override
    public List<Participant> findAll() {
        EntityManager em = emf.createEntityManager();
        List<Participant> list = em.createQuery("SELECT p FROM Participant p", Participant.class).getResultList();
        em.close();
        return list;
    }
}
