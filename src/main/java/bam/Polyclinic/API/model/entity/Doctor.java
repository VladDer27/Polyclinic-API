package bam.Polyclinic.API.model.entity;

import bam.Polyclinic.API.utils.enums.MedicalSpeciality;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "doctors")
public class Doctor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "speciality")
    @Enumerated(EnumType.STRING)
    private MedicalSpeciality speciality;

    @Column(name = "appointment_duration")
    private int appointmentDuration;

    @Column(name = "room")
    private int room;

    @OneToMany(mappedBy = "doctor", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DoctorSchedule> schedules = new ArrayList<>();

    @OneToOne(cascade = CascadeType.ALL,
            orphanRemoval = true)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "doctor",
            cascade = CascadeType.REMOVE,
            orphanRemoval = true)
    private List<Appointment> appointments;

    public void addSchedule(DoctorSchedule schedule) {
        schedules.add(schedule);
        schedule.setDoctor(this);
    }

    public void addAppointment(Appointment appointment) {
        appointments.add(appointment);
        appointment.setDoctor(this);
    }
}
