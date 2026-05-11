package fi.christian;

import fi.christian.dao.*;
import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Scanner;

public class App {
  private static EntityManagerFactory emf = Persistence.createEntityManagerFactory("JPA-PU");
  private static Scanner scanner = new Scanner(System.in);
  
  private static IMeetingDao meetingDao = new MeetingDao(emf);
  private static IRoomDao roomDao = new RoomDao(emf);
  private static IParticipantDao participantDao = new ParticipantDao(emf);
  private static IDepartmentDao departmentDao = new DepartmentDao(emf);

  public static void main(String[] args) {
    boolean running = true;
    while (running) {
      System.out.println("\n--- MEETING MANAGER ---");
      System.out.println("1) List all meetings");
      System.out.println("2) Add new meeting");
      System.out.println("3) Update meeting");
      System.out.println("4) Delete a meeting");
      System.out.println("5) Manage Rooms (Add/Update/Delete/List)");
      System.out.println("6) Manage Departments (Add/Update/Delete/List)");
      System.out.println("7) Manage Staff Members (Add/Update/Delete/List)");
      System.out.println("0) Exit");
      System.out.print("Select: ");

      String choice = scanner.nextLine();
      switch (choice) {
        case "1": listMeetings(); break;
        case "2": createMeeting(); break;
        case "3": updateMeeting(); break;
        case "4": deleteMeeting(); break;
        case "5": manageRooms(); break;
        case "6": manageDepartments(); break;
        case "7": manageStaffMembers(); break;
        case "0": running = false; break;
        default: System.out.println("Invalid selection. Please try again (0-7).");
      }
    }
    emf.close();
  }

  private static void manageRooms() {
    boolean back = false;
    while (!back) {
      System.out.println("\n--- Manage Rooms ---");
      System.out.println("1) List Rooms");
      System.out.println("2) Add Room");
      System.out.println("3) Update Room");
      System.out.println("4) Delete Room");
      System.out.println("0) Back to Main Menu");
      System.out.print("Select: ");
      String choice = scanner.nextLine();
      try {
        switch (choice) {
          case "1": listRooms(); break;
          case "2": createRoom(); break;
          case "3": updateRoom(); break;
          case "4": deleteRoom(); break;
          case "0": back = true; break;
          default: System.out.println("Invalid selection. Please try again.");
        }
      } catch (Exception e) {
        System.out.println("ERROR: " + e.getMessage());
      }
    }
  }

  private static void listRooms() {
    roomDao.findAll().forEach(r -> System.out.println(r.getId() + ") " + r.getRoomName() + " (Cap: " + r.getCapacity() + ")"));
  }

  private static void createRoom() {
    System.out.print("Room name: ");
    String name = scanner.nextLine();
    if (name.trim().isEmpty()) throw new IllegalArgumentException("Name cannot be empty");
    System.out.print("Capacity: ");
    int cap = Integer.parseInt(scanner.nextLine());
    if (cap <= 0) throw new IllegalArgumentException("Capacity must be positive");
    roomDao.save(new Room(name, cap));
    System.out.println("LOG: Room saved.");
  }

  private static void updateRoom() {
    listRooms();
    System.out.print("ID to update: ");
    int id = Integer.parseInt(scanner.nextLine());
    Room r = roomDao.findById(id);
    if (r == null) { System.out.println("Room not found."); return; }
    System.out.print("New name (current: " + r.getRoomName() + "): ");
    String name = scanner.nextLine();
    if (!name.trim().isEmpty()) r.setRoomName(name);
    System.out.print("New capacity (current: " + r.getCapacity() + "): ");
    String capStr = scanner.nextLine();
    if (!capStr.trim().isEmpty()) r.setCapacity(Integer.parseInt(capStr));
    roomDao.update(r);
    System.out.println("LOG: Room updated.");
  }

  private static void deleteRoom() {
    listRooms();
    System.out.print("ID to delete: ");
    roomDao.delete(Integer.parseInt(scanner.nextLine()));
    System.out.println("LOG: Room deleted.");
  }

  private static void manageDepartments() {
    boolean back = false;
    while (!back) {
      System.out.println("\n--- Manage Departments ---");
      System.out.println("1) List Departments");
      System.out.println("2) Add Department");
      System.out.println("3) Update Department");
      System.out.println("4) Delete Department");
      System.out.println("5) Add Staff Members to Department");
      System.out.println("0) Back to Main Menu");
      System.out.print("Select: ");
      String choice = scanner.nextLine();
      try {
        switch (choice) {
          case "1": listDepartments(); break;
          case "2": createDepartment(); break;
          case "3": updateDepartment(); break;
          case "4": deleteDepartment(); break;
          case "5": addMembersToDepartment(); break;
          case "0": back = true; break;
          default: System.out.println("Invalid selection. Please try again.");
        }
      } catch (Exception e) {
        System.out.println("ERROR: " + e.getMessage());
      }
    }
  }

  private static void addMembersToDepartment() {
    listDepartments();
    System.out.print("Select Department ID to add members to: ");
    Department d = departmentDao.findById(Integer.parseInt(scanner.nextLine()));
    if (d == null) { System.out.println("Department not found."); return; }

    boolean adding = true;
    while (adding) {
      System.out.println("\n--- Adding Member to " + d.getName() + " ---");
      System.out.print("Name: "); String name = scanner.nextLine();
      System.out.print("Email: "); String email = scanner.nextLine();

      if (participantDao.findByEmail(email) != null) {
        System.out.println("ERROR: This email is already taken!");
      } else {
        Participant p = new Participant(name, email);
        p.setDepartment(d);
        participantDao.save(p);
        System.out.println("LOG: " + name + " added to " + d.getName());
      }

      System.out.print("Add another member? (1: Yes | 0: No): ");
      if (!scanner.nextLine().equals("1")) adding = false;
    }
  }

  private static void listDepartments() {
    departmentDao.findAll().forEach(d -> System.out.println(d.getId() + ") " + d.getName()));
  }

  private static void createDepartment() {
    System.out.print("Department name: ");
    String name = scanner.nextLine();
    if (name.trim().isEmpty()) throw new IllegalArgumentException("Name cannot be empty");
    departmentDao.save(new Department(name));
    System.out.println("LOG: Department saved.");
  }

  private static void updateDepartment() {
    listDepartments();
    System.out.print("ID to update: ");
    int id = Integer.parseInt(scanner.nextLine());
    Department d = departmentDao.findById(id);
    if (d == null) { System.out.println("Department not found."); return; }
    System.out.print("New name: ");
    String name = scanner.nextLine();
    if (!name.trim().isEmpty()) d.setName(name);
    departmentDao.update(d);
  }

  private static void deleteDepartment() {
    listDepartments();
    System.out.print("ID to delete: ");
    departmentDao.delete(Integer.parseInt(scanner.nextLine()));
  }

  private static void manageStaffMembers() {
    boolean back = false;
    while (!back) {
      System.out.println("\n--- Manage Staff Members ---");
      System.out.println("1) List Staff");
      System.out.println("2) Add Staff Member");
      System.out.println("3) Update Staff Member");
      System.out.println("4) Delete Staff Member");
      System.out.println("0) Back to Main Menu");
      System.out.print("Select: ");
      String choice = scanner.nextLine();
      try {
        switch (choice) {
          case "1": listStaff(); break;
          case "2": createStaffMember(); break;
          case "3": updateStaffMember(); break;
          case "4": deleteStaffMember(); break;
          case "0": back = true; break;
          default: System.out.println("Invalid selection. Please try again.");
        }
      } catch (Exception e) {
        System.out.println("ERROR: " + e.getMessage());
      }
    }
  }

  private static void listStaff() {
    participantDao.findAll().forEach(p -> {
      String dept = (p.getDepartment() != null) ? " (" + p.getDepartment().getName() + ")" : "";
      System.out.println(p.getId() + ") " + p.getName() + " <" + p.getEmail() + ">" + dept);
    });
  }

  private static void createStaffMember() {
    System.out.print("Name: ");
    String name = scanner.nextLine();
    if (name.trim().isEmpty()) throw new IllegalArgumentException("Name cannot be empty");
    System.out.print("Email: ");
    String email = scanner.nextLine();
    if (email.trim().isEmpty() || !email.contains("@")) throw new IllegalArgumentException("Invalid email");

    if (participantDao.findByEmail(email) != null) {
      throw new IllegalArgumentException("Staff member with email " + email + " already exists.");
    }

    Participant p = new Participant(name, email);
    listDepartments();
    System.out.print("Select Department ID (0 for none): ");
    int dId = Integer.parseInt(scanner.nextLine());
    if (dId > 0) p.setDepartment(departmentDao.findById(dId));
    
    participantDao.save(p);
    System.out.println("LOG: Staff member added.");
  }

  private static void updateStaffMember() {
    listStaff();
    System.out.print("ID to update: ");
    int id = Integer.parseInt(scanner.nextLine());
    Participant p = participantDao.findById(id);
    if (p == null) { System.out.println("Staff member not found."); return; }
    
    System.out.print("New name (current: " + p.getName() + "): ");
    String name = scanner.nextLine();
    if (!name.trim().isEmpty()) p.setName(name);
    
    System.out.print("New email (current: " + p.getEmail() + "): ");
    String email = scanner.nextLine();
    if (!email.trim().isEmpty()) p.setEmail(email);

    System.out.println("Department management: 1) Set/Change Department | 2) Remove Department | 0) Skip");
    String deptOpt = scanner.nextLine();
    if (deptOpt.equals("1")) {
      listDepartments();
      System.out.print("Select New Department ID: ");
      Department newDept = departmentDao.findById(Integer.parseInt(scanner.nextLine()));
      if (newDept != null) p.setDepartment(newDept);
    } else if (deptOpt.equals("2")) {
      p.setDepartment(null);
      System.out.println("LOG: Department removed.");
    }

    participantDao.update(p);
    System.out.println("LOG: Staff member updated.");
  }

  private static void deleteStaffMember() {
    listStaff();
    System.out.print("ID to delete: ");
    participantDao.delete(Integer.parseInt(scanner.nextLine()));
  }

  private static void listMeetings() {
    System.out.println("\n--- All Meetings ---");
    for (Meeting m : meetingDao.findAll()) {
      String r = (m.getRoom() != null) ? m.getRoom().getRoomName() : "None";
      System.out.println("[" + m.getId() + "] " + m.getTitle() + " | Room: " + r + " | Time: " + m.getTime());
      if (!m.getParticipants().isEmpty()) {
        System.out.println("   All Meeting Participants:");
        m.getParticipants().forEach(p -> {
          String dept = (p.getDepartment() != null) ? " (" + p.getDepartment().getName() + ")" : "";
          System.out.println("   - " + p.getName() + dept);
        });
      } else {
        System.out.println("   No participants registered.");
      }
    }
  }

  private static void createMeeting() {
    List<Room> rooms = roomDao.findAll();
    if (rooms.isEmpty()) { System.out.println("Add a room first."); return; }

    try {
      System.out.print("Title: "); String title = scanner.nextLine();
      System.out.print("Date (YYYY-MM-DD): "); LocalDate d = LocalDate.parse(scanner.nextLine());
      System.out.print("Time (HH:MM): "); 
      String timeInput = scanner.nextLine().replace(".", ":"); // Allow dots
      LocalTime t = LocalTime.parse(timeInput);

      System.out.println("Select Room:");
      rooms.forEach(room -> System.out.println(room.getId() + ") " + room.getRoomName()));
      Room room = roomDao.findById(Integer.parseInt(scanner.nextLine()));

      LocalDateTime newStart = LocalDateTime.of(d, t);
      LocalDateTime newEnd = newStart.plusHours(1);

      boolean conflict = meetingDao.findAll().stream()
          .anyMatch(m -> {
              if (m.getRoom() == null || m.getRoom().getId() != room.getId()) return false;
              LocalDateTime existingStart = m.getTime();
              LocalDateTime existingEnd = existingStart.plusHours(1);
              return newStart.isBefore(existingEnd) && newEnd.isAfter(existingStart);
          });
      
      if (conflict) {
        System.out.println("ERROR: This room is already booked within that hour! Choose another room or time.");
        return;
      }

      Meeting meeting = new Meeting(title, newStart);
      meeting.setRoom(room);

      boolean adding = true;
      while (adding) {
        System.out.println("\nAdd Participants:");
        System.out.println("1) Individually | 2) By Department | 0) Done");
        String opt = scanner.nextLine();

        if (opt.equals("1")) {
          System.out.print("Name: "); String name = scanner.nextLine();
          System.out.print("Email: "); String email = scanner.nextLine();
          Participant p = participantDao.findByEmail(email);
          if (p == null) {
            p = new Participant(name, email);
            System.out.println("Select Department (or 0 for none):");
            List<Department> depts = departmentDao.findAll();
            depts.forEach(dept -> System.out.println(dept.getId() + ") " + dept.getName()));
            int dId = Integer.parseInt(scanner.nextLine());
            if (dId > 0) p.setDepartment(departmentDao.findById(dId));
            participantDao.save(p);
          }
          meeting.addParticipant(p);
        } else if (opt.equals("2")) {
          System.out.println("Select Department to add ALL members:");
          List<Department> depts = departmentDao.findAll();
          depts.forEach(dept -> System.out.println(dept.getId() + ") " + dept.getName()));
          Department dTarget = departmentDao.findById(Integer.parseInt(scanner.nextLine()));
          if (dTarget != null) {
            dTarget.getParticipants().forEach(meeting::addParticipant);
            System.out.println("LOG: Added all " + dTarget.getParticipants().size() + " members.");
          }
        } else {
          adding = false;
        }
      }
      meetingDao.save(meeting);
      System.out.println("LOG: Meeting saved.");
    } catch (Exception e) { System.out.println("ERROR: " + e.getMessage()); }
  }

  private static void updateMeeting() {
    listMeetings();
    try {
      System.out.print("Meeting ID to update: ");
      int id = Integer.parseInt(scanner.nextLine());
      Meeting m = meetingDao.findById(id);
      if (m == null) { System.out.println("Meeting not found."); return; }
      
      boolean updating = true;
      while (updating) {
        System.out.println("\n--- Updating Meeting: " + m.getTitle() + " ---");
        System.out.println("1) Change Title (Current: " + m.getTitle() + ")");
        System.out.println("2) Change Room (Current: " + (m.getRoom() != null ? m.getRoom().getRoomName() : "None") + ")");
        System.out.println("3) Change Date/Time (Current: " + m.getTime() + ")");
        System.out.println("4) Add Existing Staff Member");
        System.out.println("5) Remove Participant");
        System.out.println("6) Create and Add New Member");
        System.out.println("7) Add All Members from a Department");
        System.out.println("8) Remove All Members of a Department");
        System.out.println("0) Exit Update");
        System.out.print("Select: ");
        String opt = scanner.nextLine();

        switch (opt) {
          case "1":
            System.out.print("New title: ");
            String title = scanner.nextLine();
            if (!title.trim().isEmpty()) m.setTitle(title);
            break;
          case "2":
            listRooms();
            System.out.print("New Room ID: ");
            m.setRoom(roomDao.findById(Integer.parseInt(scanner.nextLine())));
            break;
          case "3":
            System.out.print("New Date (YYYY-MM-DD) [Empty to skip]: ");
            String dateStr = scanner.nextLine();
            LocalDate newD = dateStr.isEmpty() ? m.getTime().toLocalDate() : LocalDate.parse(dateStr);
            System.out.print("New Time (HH:MM) [Empty to skip]: ");
            String timeStr = scanner.nextLine().replace(".", ":");
            LocalTime newT = timeStr.isEmpty() ? m.getTime().toLocalTime() : LocalTime.parse(timeStr);
            m.setTime(LocalDateTime.of(newD, newT));
            break;
          case "4":
            listStaff();
            System.out.print("Staff ID to add: ");
            Participant pAdd = participantDao.findById(Integer.parseInt(scanner.nextLine()));
            if (pAdd != null) m.addParticipant(pAdd);
            break;
          case "5":
            System.out.println("Current participants:");
            m.getParticipants().forEach(p -> System.out.println(p.getId() + ") " + p.getName()));
            System.out.print("ID to remove: ");
            int removeId = Integer.parseInt(scanner.nextLine());
            m.getParticipants().removeIf(p -> p.getId() == removeId);
            break;
          case "6":
            System.out.print("Name: "); String name = scanner.nextLine();
            System.out.print("Email: "); String email = scanner.nextLine();
            Participant pNew = new Participant(name, email);
            participantDao.save(pNew);
            m.addParticipant(pNew);
            System.out.println("LOG: New staff member created and added.");
            break;
          case "7":
            listDepartments();
            System.out.print("Select Department ID to add ALL members: ");
            Department dAdd = departmentDao.findById(Integer.parseInt(scanner.nextLine()));
            if (dAdd != null) {
              dAdd.getParticipants().forEach(m::addParticipant);
              System.out.println("LOG: Added all members from " + dAdd.getName());
            }
            break;
          case "8":
            listDepartments();
            System.out.print("Select Department ID to REMOVE ALL its members: ");
            int dRemoveId = Integer.parseInt(scanner.nextLine());
            m.getParticipants().removeIf(p -> p.getDepartment() != null && p.getDepartment().getId() == dRemoveId);
            System.out.println("LOG: Removed department members.");
            break;
          case "0":
            updating = false;
            break;
          default:
            System.out.println("Invalid selection. Please try again.");
            break;
        }
      }
      
      LocalDateTime mStart = m.getTime();
      LocalDateTime mEnd = mStart.plusHours(1);

      boolean conflict = meetingDao.findAll().stream()
          .anyMatch(other -> {
              if (other.getId() == m.getId() || other.getRoom() == null || m.getRoom() == null) return false;
              if (other.getRoom().getId() != m.getRoom().getId()) return false;
              
              LocalDateTime otherStart = other.getTime();
              LocalDateTime otherEnd = otherStart.plusHours(1);
              return mStart.isBefore(otherEnd) && mEnd.isAfter(otherStart);
          });

      if (conflict) {
        System.out.println("ERROR: Update failed! Room is already booked for that 1-hour window.");
        return;
      }
      
      meetingDao.update(m);
      System.out.println("LOG: Meeting updated successfully.");
    } catch (Exception e) {
      System.out.println("ERROR: " + e.getMessage());
    }
  }

  private static void deleteMeeting() {
    listMeetings();
    try {
      System.out.print("Meeting ID to delete: ");
      meetingDao.delete(Integer.parseInt(scanner.nextLine()));
      System.out.println("LOG: Meeting deleted.");
    } catch (Exception e) {
      System.out.println("ERROR: " + e.getMessage());
    }
  }
}
