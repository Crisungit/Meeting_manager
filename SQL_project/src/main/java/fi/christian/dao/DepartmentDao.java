package fi.christian.dao;

import fi.christian.Department;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import java.util.List;

public class DepartmentDao implements IDepartmentDao {
    private EntityManagerFactory emf;

    public DepartmentDao(EntityManagerFactory emf) {
        this.emf = emf;
    }

    @Override
    public void save(Department department) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        em.persist(department);
        em.getTransaction().commit();
        em.close();
    }

    @Override
    public void update(Department department) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        em.merge(department);
        em.getTransaction().commit();
        em.close();
    }

    @Override
    public void delete(int id) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        Department d = em.find(Department.class, id);
        if (d != null) em.remove(d);
        em.getTransaction().commit();
        em.close();
    }

    @Override
    public Department findById(int id) {
        EntityManager em = emf.createEntityManager();
        try {
            return em.createQuery(
                "SELECT d FROM Department d LEFT JOIN FETCH d.participants WHERE d.id = :id", 
                Department.class)
                .setParameter("id", id)
                .getSingleResult();
        } catch (NoResultException e) {
            return null;
        } finally {
            em.close();
        }
    }

    @Override
    public List<Department> findAll() {
        EntityManager em = emf.createEntityManager();
        List<Department> departments = em.createQuery(
            "SELECT DISTINCT d FROM Department d LEFT JOIN FETCH d.participants", 
            Department.class).getResultList();
        em.close();
        return departments;
    }
}
