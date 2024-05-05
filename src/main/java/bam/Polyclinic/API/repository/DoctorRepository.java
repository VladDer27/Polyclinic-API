package bam.Polyclinic.API.repository;

import bam.Polyclinic.API.model.entity.Doctor;
import bam.Polyclinic.API.utils.enums.MedicalSpeciality;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Long> {
    List<Doctor> findAllBySpeciality(MedicalSpeciality speciality);
}
