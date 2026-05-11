package fi.christian.dao;

import fi.christian.Department;
import java.util.List;

public interface IDepartmentDao {
    void save(Department department);
    void update(Department department);
    void delete(int id);
    Department findById(int id);
    List<Department> findAll();
}
