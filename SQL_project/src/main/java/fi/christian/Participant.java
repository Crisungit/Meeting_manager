package fi.christian;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "Participants")
public class Participant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private String name;

    @Column(unique = true, nullable = false)
    private String email;

    @ManyToMany(mappedBy = "participants")
    private Set<Meeting> meetings = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "department_id")
    private Department department;

    public Participant() {}
    public Participant(String name, String email) {
        this.name = name;
        this.email = email;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public Set<Meeting> getMeetings() { return meetings; }
    public Department getDepartment() { return department; }
    public void setDepartment(Department department) { this.department = department; }
}