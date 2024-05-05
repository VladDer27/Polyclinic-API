package bam.Polyclinic.API.service;

import bam.Polyclinic.API.model.entity.Doctor;
import bam.Polyclinic.API.repository.DoctorRepository;
import bam.Polyclinic.API.utils.enums.MedicalSpeciality;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class DoctorService {
    private final DoctorRepository doctorRepository;

    @Autowired
    public DoctorService(DoctorRepository doctorRepository) {
        this.doctorRepository = doctorRepository;
    }

    public List<Doctor> getAllDoctors() {
        return doctorRepository.findAll();
    }

    public Optional<Doctor> getDoctorById(long id) {
        return doctorRepository.findById(id);
    }

    public List<Doctor> getDoctorsBySpeciality(MedicalSpeciality specialty) {
        return doctorRepository.findAllBySpeciality(specialty);
    }

    @Transactional
    public Optional<Long> addDoctor(Doctor doctor) {
        if (doctor != null)
            return Optional.of(doctorRepository.save(doctor).getId());
        return Optional.empty();
    }
}
