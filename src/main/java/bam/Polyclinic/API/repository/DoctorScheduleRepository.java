package bam.Polyclinic.API.repository;

import bam.Polyclinic.API.model.entity.Doctor;
import bam.Polyclinic.API.model.entity.DoctorSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.DayOfWeek;
import java.util.Optional;

@Repository
public interface DoctorScheduleRepository extends JpaRepository<DoctorSchedule, Long> {
    void deleteAllByDoctor(Doctor doctor);
    Optional<DoctorSchedule> getByDoctorAndDayOfWeek(Doctor doctor, DayOfWeek dayOfWeek);
}
