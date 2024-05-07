package bam.Polyclinic.API.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.DayOfWeek;
import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DoctorScheduleResponse {
    private long id;

    private DayOfWeek dayOfWeek;

    private LocalTime workdayStart;

    private LocalTime workdayEnd;
}
