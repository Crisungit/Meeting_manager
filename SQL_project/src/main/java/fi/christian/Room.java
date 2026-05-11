package fi.christian;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Rooms")
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "room_name", nullable = false)
    private String roomName;

    private int capacity;

    @OneToMany(mappedBy = "room")
    private List<Meeting> meetings = new ArrayList<>();

    public Room() {}
    public Room(String roomName, int capacity) {
        this.roomName = roomName;
        this.capacity = capacity;
    }

    public int getId() { return id; }
    public String getRoomName() { return roomName; }
    public void setRoomName(String roomName) { this.roomName = roomName; }
    public int getCapacity() { return capacity; }
    public void setCapacity(int capacity) { this.capacity = capacity; }
}
