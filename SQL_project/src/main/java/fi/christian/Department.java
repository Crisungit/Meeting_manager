package fi.christian;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Departments")
public class Department {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private String name;

    @OneToMany(mappedBy = "department")
    private List<Participant> participants = new ArrayList<>();

    public Department() {}
    public Department(String name) { this.name = name; }

    public int getId() { return id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public List<Participant> getParticipants() { return participants; }
}
