package bam.Polyclinic.API.service;

import bam.Polyclinic.API.model.entity.Appointment;
import bam.Polyclinic.API.model.entity.Doctor;
import bam.Polyclinic.API.model.entity.DoctorSchedule;
import bam.Polyclinic.API.repository.AppointmentRepository;
import bam.Polyclinic.API.repository.DoctorScheduleRepository;
import bam.Polyclinic.API.utils.enums.AppointmentStatus;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

@Service
public class DoctorScheduleService {
    private final EmailService emailService;
    private final AppointmentRepository appointmentRepository;
    private final DoctorScheduleRepository doctorScheduleRepository;

    @Autowired
    public DoctorScheduleService(EmailService emailService,
                                 AppointmentRepository appointmentRepository,
                                 DoctorScheduleRepository doctorScheduleRepository) {
        this.emailService = emailService;
        this.appointmentRepository = appointmentRepository;
        this.doctorScheduleRepository = doctorScheduleRepository;
    }

    @Transactional
    public void makeDoctorScheduleFromRequest(Doctor doctor, HttpServletRequest request) {
        List<Appointment> appointmentsToDelete =
                appointmentRepository.findAllByDoctorAndStatus(doctor, AppointmentStatus.ACTIVE);
        for (Appointment appointment : appointmentsToDelete)
            emailService.appointmentMessage(appointment, AppointmentStatus.CANCELED);

        appointmentRepository.deleteAllByDoctorAndStatus(doctor, AppointmentStatus.ACTIVE);
        doctorScheduleRepository.deleteAllByDoctor(doctor);

        Map<String, String[]> params = request.getParameterMap();
        for (DayOfWeek day : DayOfWeek.values()) {
            String[] workdayStart = params.get(day + "_start");
            String[] workdayEnd = params.get(day + "_end");
            if (workdayStart == null || workdayEnd == null)
                continue;
            if (workdayStart[0].length() > 0 && workdayEnd[0].length() > 0) {
                DoctorSchedule schedule = new DoctorSchedule();
                schedule.setDayOfWeek(day);
                schedule.setWorkdayStart(LocalTime.parse(workdayStart[0]));
                schedule.setWorkdayEnd(LocalTime.parse(workdayEnd[0]));
                doctor.addSchedule(schedule);
            }
        }
    }
}
