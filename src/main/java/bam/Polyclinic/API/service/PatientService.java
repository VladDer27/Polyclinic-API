package bam.Polyclinic.API.service;

import bam.Polyclinic.API.model.entity.Patient;
import bam.Polyclinic.API.repository.PatientRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PatientService {
    private final PatientRepository patientRepository;

    @Autowired
    public PatientService(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    public Optional<Patient> getPatientById(long id) {
        return patientRepository.findById(id);
    }

    @Transactional
    public Optional<Long> addPatient(Patient patient) {
        if (patient != null)
            return Optional.of(patientRepository.save(patient).getId());
        return Optional.empty();
    }
}
