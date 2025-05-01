package com.example.demo;

import com.example.demo.model.Patient;
import com.example.demo.repository.PatientRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

//@ExtendWith(SpringExtension.class)@DataJpaTest
//@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
//@TestPropertySource(locations = "classpath:application-test.properties")
//@ActiveProfiles("test")
//@DataJpaTest

@ActiveProfiles("test")
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class DemoApplicationTests {
    @Autowired
    private PatientRepository patientRepository;


    @Test
    void firstTest() {
        System.out.println("Hello World");
        Assertions.assertTrue(true);
    }
    @Transactional
    @Test
    void secondTest() {
        Patient patient = new Patient();
        patient.setName("John Doe");
        patient.setEmail("john@example.com");
        patient.setAddress("123 Test Lane");
        patient.setDateOfBirth(LocalDate.of(1990, 1, 1));
        patient.setRegisteredDate(LocalDate.now());

        patientRepository.save(patient);

        List<Patient> list = patientRepository.findAll();
        Assertions.assertNotNull(list);
        Assertions.assertTrue(list.size() > 0);
    }

    @Transactional
    @Test
    void thirdTest_Delete(){
        Patient patient=new Patient();
        patient.setId("ff2a540b-25ab-4817-a4e4-fe2cb091af70");
        patient.setName("John1 Doe");
        patient.setEmail("john1@example.com");
        patient.setAddress("123 Test Lane");
        patient.setDateOfBirth(LocalDate.of(1990, 1, 1));
        patient.setRegisteredDate(LocalDate.now());
        patientRepository.save(patient);
        patientRepository.deleteById(patient.getId());
        Optional<Patient> deletedPatient=patientRepository.findById(patient.getId());
        Assertions.assertTrue(deletedPatient.isEmpty());

    }


}


