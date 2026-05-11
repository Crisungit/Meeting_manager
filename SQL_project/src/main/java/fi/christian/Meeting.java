package fi.christian;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "Meetings")
public class Meeting {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String title;
    
    @Column(name = "start_time")
    private LocalDateTime startTime;

    @ManyToOne
    @JoinColumn(name = "room_id")
    private Room room;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
        name = "Meeting_Participants",
        joinColumns = @JoinColumn(name = "meeting_id"),
        inverseJoinColumns = @JoinColumn(name = "participant_id")
    )
    private Set<Participant> participants = new HashSet<>();

    public Meeting() {}
    public Meeting(String title, LocalDateTime startTime) {
        this.title = title;
        this.startTime = startTime;
    }

    public void addParticipant(Participant p) {
        this.participants.add(p);
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public LocalDateTime getTime() {
        return startTime;
    }

    public void setTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public Set<Participant> getParticipants() {
        return participants;
    }

    public void setParticipants(Set<Participant> participants) {
        this.participants = participants;
    }
}