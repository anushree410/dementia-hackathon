package com.hackathon.dementia.service;

import com.hackathon.dementia.models.Medicine;
import com.hackathon.dementia.models.Notification;
import com.hackathon.dementia.models.Patient;
import com.hackathon.dementia.repository.MedicineRepo;
import com.hackathon.dementia.repository.NotificationRepo;
import com.hackathon.dementia.repository.PatientRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class NotificationService {
    private static final Logger logger = LoggerFactory.getLogger(NotificationService.class);

    @Autowired
    private PatientRepo patientRepo;

    @Autowired
    private MedicineRepo medicineRepo;

    @Autowired
    private NotificationRepo notificationRepo;

    public static LocalDateTime getPreviousDay(LocalDateTime dateTime) {
        // Convert LocalDateTime to LocalDate
        LocalDate date = dateTime.toLocalDate();

        // Subtract one day
        LocalDate previousDate = date.minusDays(1);

        // Get the start of the day (midnight) and add the original time
        return previousDate.atStartOfDay().plusHours(dateTime.getHour()).plusMinutes(dateTime.getMinute()).plusSeconds(dateTime.getSecond());
    }

    public void loadNotificationsForPatient(Long patientId) {
        Optional<Patient> patientOptional = patientRepo.findById(patientId);
        if (!patientOptional.isPresent()) {
            System.out.println("Patient not found");
            return;
        }
        Patient patient = patientOptional.get();

        List<Medicine> medicines = medicineRepo.findByPatientId(patientId);

        for (Medicine medicine : medicines) {
            String[] freqParts = medicine.getFreq().split(",");

            LocalDateTime startTime = medicine.getStarttime();
            LocalDateTime endTime = medicine.getEndtime();
            LocalDateTime endminusOne = getPreviousDay(endTime);

            while (!startTime.isAfter(endminusOne)) {
                int morningTablets = Integer.parseInt(freqParts[0]); // 8 AM
                int afternoonTablets = Integer.parseInt(freqParts[1]); // 1 PM
                int eveningTablets = Integer.parseInt(freqParts[2]); // 8 PM

                LocalDateTime nextDay = startTime.plusDays(1);
                LocalDateTime current = startTime;

                boolean notifyMorning = morningTablets > 0;
                boolean notifyAfternoon = afternoonTablets > 0;
                boolean notifyEvening = eveningTablets > 0;
                current=LocalDateTime.of(startTime.toLocalDate(), LocalTime.parse("08:00:00"));

                if (notifyMorning && current.isBefore(endTime)) {
                    String message = String.format("Take %d tablets of %s", morningTablets, medicine.getName());
                    logger.info("Created notification: {}", message);
                    Notification noti = new Notification();
                    noti.setPatient(patient);
                    noti.setMessage(message);
                    noti.setNotetime(current);
                    notificationRepo.save(noti);
                }
                current=LocalDateTime.of(startTime.toLocalDate(), LocalTime.parse("13:00:00"));

                if (notifyAfternoon && current.isBefore(endTime)) {
                    String message = String.format("Take %d tablets of %s", afternoonTablets, medicine.getName());
                    logger.info("Created notification: {}", message);
                    Notification noti = new Notification();
                    noti.setPatient(patient);
                    noti.setMessage(message);
                    noti.setNotetime(current);
                    notificationRepo.save(noti);
                }
                current=LocalDateTime.of(startTime.toLocalDate(), LocalTime.parse("20:00:00"));

                if (notifyEvening && current.isBefore(endTime)) {
                    String message = String.format("Take %d tablets of %s", eveningTablets, medicine.getName());
                    logger.info("Created notification: {}", message);
                    Notification noti = new Notification();
                    noti.setPatient(patient);
                    noti.setMessage(message);
                    noti.setNotetime(current);
                    notificationRepo.save(noti);
                }
                startTime = nextDay;
            }
        }
    }

}

