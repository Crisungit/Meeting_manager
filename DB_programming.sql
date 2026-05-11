CREATE DATABASE IF NOT EXISTS MeetingCalendarDB;
USE MeetingCalendarDB;

CREATE TABLE Departments (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL
);

CREATE TABLE Rooms (
    id INT AUTO_INCREMENT PRIMARY KEY,
    room_name VARCHAR(50) NOT NULL,
    capacity INT
);

CREATE TABLE Participants (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    department_id INT,
    CONSTRAINT fk_participant_department 
        FOREIGN KEY (department_id) REFERENCES Departments(id) 
        ON DELETE SET NULL
);

CREATE TABLE Meetings (
    id INT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(150) NOT NULL,
    start_time DATETIME NOT NULL,
    room_id INT,
    CONSTRAINT fk_meeting_room 
        FOREIGN KEY (room_id) REFERENCES Rooms(id) 
        ON DELETE SET NULL
);

CREATE TABLE Meeting_Participants (
    meeting_id INT NOT NULL,
    participant_id INT NOT NULL,
    PRIMARY KEY (meeting_id, participant_id),
    CONSTRAINT fk_link_meeting 
        FOREIGN KEY (meeting_id) REFERENCES Meetings(id) 
        ON DELETE CASCADE,
    CONSTRAINT fk_link_participant 
        FOREIGN KEY (participant_id) REFERENCES Participants(id) 
        ON DELETE CASCADE
);