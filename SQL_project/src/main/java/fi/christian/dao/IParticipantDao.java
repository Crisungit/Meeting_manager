package fi.christian.dao;

import fi.christian.Participant;
import java.util.List;

public interface IParticipantDao {
    void save(Participant participant);
    void update(Participant participant);
    void delete(int id);
    Participant findByEmail(String email);
    Participant findById(int id);
    List<Participant> findAll();
}
