package bam.Polyclinic.API.model.response;

import bam.Polyclinic.API.utils.enums.MedicalSpeciality;
import lombok.Data;

import java.time.DayOfWeek;

@Data
public class DoctorCreateResponse {
    private DayOfWeek[] dayOfWeeks;
    private MedicalSpeciality[] medicalSpecialities;
}
