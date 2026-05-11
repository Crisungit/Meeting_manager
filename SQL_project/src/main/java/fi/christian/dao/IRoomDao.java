package fi.christian.dao;

import fi.christian.Room;
import java.util.List;

public interface IRoomDao {
    void save(Room room);
    void update(Room room);
    void delete(int id);
    Room findById(int id);
    List<Room> findAll();
}
