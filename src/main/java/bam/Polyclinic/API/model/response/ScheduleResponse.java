package bam.Polyclinic.API.model.response;

import bam.Polyclinic.API.utils.enums.DayAvailability;
import bam.Polyclinic.API.utils.enums.MedicalSpeciality;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Data
public class ScheduleResponse {
    private List<LocalDate> dates;
    private List<DoctorResponseWithSchedule> doctors;
    private Map<Long, Map<LocalDate, DayAvailability>> availabilityMap;
    private MedicalSpeciality[] specialities;
    private LocalDate tomorrow;
}
