package fi.christian.dao;

import fi.christian.Meeting;
import java.util.List;

public interface IMeetingDao {
    void save(Meeting meeting);
    void update(Meeting meeting);
    void delete(int id);
    Meeting findById(int id);
    List<Meeting> findAll();
}
