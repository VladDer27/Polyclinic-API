package bam.Polyclinic.API.service;

import bam.Polyclinic.API.model.entity.Appointment;
import bam.Polyclinic.API.utils.enums.AppointmentStatus;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;


@Service
public class EmailService {
    private final JavaMailSender emailSender;

    @Autowired
    public EmailService(JavaMailSenderImpl emailSender) {
        this.emailSender = emailSender;
    }

    @Async
    public void appointmentMessage(Appointment appointment, AppointmentStatus status) {
        MimeMessage message = emailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setFrom("vitality-clinic@yandex.ru");
            helper.setTo(appointment.getPatient().getUser().getLogin());
            helper.setSubject(status.name().equals("ACTIVE") ?
                    "Заявка на запись подтверждена" : "Отмена записи");

            String htmlContent = generateHtmlContent(appointment, status);
            helper.setText(htmlContent, true);
            emailSender.send(message);
        } catch (MessagingException e) {
            System.out.println("Ошибка при отправке сообщения");
        }
    }

    private String generateHtmlContent(Appointment appointment, AppointmentStatus status) {
        StringBuilder htmlContent = new StringBuilder();
        htmlContent.append("<!DOCTYPE html>");
        htmlContent.append("<html lang=\"ru\">");
        htmlContent.append("<head>");
        htmlContent.append("<meta charset=\"UTF-8\">");
        if (status == AppointmentStatus.ACTIVE) {
            htmlContent.append("<title>Заявка на запись подтверждена</title>");
        } else {
            htmlContent.append("<title>Отмена записи</title>");
        }
        htmlContent.append("</head>");
        htmlContent.append("<body>");
        htmlContent.append("<main>");
        htmlContent.append("<p>");
        htmlContent.append("Уважаем");
        htmlContent.append(appointment.getPatient().getGender().name().equals("MALE") ? "ый" : "ая");
        htmlContent.append(" ");
        htmlContent.append(appointment.getPatient().getUser().getFirstName() != null ? appointment.getPatient().getUser().getFirstName() : "");
        htmlContent.append(" ");
        htmlContent.append(appointment.getPatient().getUser().getMiddleName() != null ? appointment.getPatient().getUser().getMiddleName() : "");
        htmlContent.append("</p>");
        htmlContent.append("<p>");
        if (status == AppointmentStatus.ACTIVE) {
            htmlContent.append("Ваша заявка на запись была подтверждена. При посещении нашего медицинского учреждения пациентам и сопровождающим ");
            htmlContent.append("обязательно иметь при себе оригинал паспорта. При сопровождении ребенка доверенным лицом, не являющимся законным представителем ребенка, ");
            htmlContent.append("необходима нотариально заверенная доверенность от законных представителей. ");
            htmlContent.append("Лица, не предоставившие оригиналы (нотариально заверенные копии) документов, на территорию поликлиники не допускаются. ");
        } else {
            htmlContent.append("Ваша запись на прием у врача была отменена.");
        }
        htmlContent.append("</p>");
        htmlContent.append("<p>");
        htmlContent.append("Детали заявки:<br>");
        htmlContent.append("<b>Дата: </b>").append(appointment.getDate()).append("<br>");
        htmlContent.append("<b>Время: </b>").append(appointment.getStartTime()).append(" - ").append(appointment.getStartTime().plusMinutes(appointment.getDoctor().getAppointmentDuration())).append("<br>");
        htmlContent.append("<b>Кабинет: </b>").append(appointment.getDoctor().getRoom()).append("<br>");
        htmlContent.append("<b>Специальность врача: </b>").append(appointment.getDoctor().getSpeciality()).append("<br>");
        htmlContent.append("<b>Врач: </b>").append(appointment.getDoctor().getUser().getLastName() != null ? appointment.getDoctor().getUser().getLastName() : "")
                .append(" ")
                .append(appointment.getDoctor().getUser().getFirstName() != null ? appointment.getDoctor().getUser().getFirstName() : "")
                .append(" ")
                .append(appointment.getDoctor().getUser().getMiddleName() != null ? appointment.getDoctor().getUser().getMiddleName() : "")
                .append("<br>");
        htmlContent.append("</p>");
        htmlContent.append("</main>");
        htmlContent.append("</body>");
        htmlContent.append("</html>");
        return htmlContent.toString();
    }

}
