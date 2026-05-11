package fi.christian.dao;

import fi.christian.Room;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.util.List;

public class RoomDao implements IRoomDao {
    private EntityManagerFactory emf;

    public RoomDao(EntityManagerFactory emf) {
        this.emf = emf;
    }

    @Override
    public void save(Room room) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        em.persist(room);
        em.getTransaction().commit();
        em.close();
    }

    @Override
    public void update(Room room) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        em.merge(room);
        em.getTransaction().commit();
        em.close();
    }

    @Override
    public void delete(int id) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        Room r = em.find(Room.class, id);
        if (r != null) em.remove(r);
        em.getTransaction().commit();
        em.close();
    }

    @Override
    public Room findById(int id) {
        EntityManager em = emf.createEntityManager();
        Room r = em.find(Room.class, id);
        em.close();
        return r;
    }

    @Override
    public List<Room> findAll() {
        EntityManager em = emf.createEntityManager();
        List<Room> rooms = em.createQuery("SELECT r FROM Room r", Room.class).getResultList();
        em.close();
        return rooms;
    }
}
