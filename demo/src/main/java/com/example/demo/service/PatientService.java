package com.example.demo.service;

import billing.BillingResponse;
import com.example.demo.dto.PatientRequestDTO;
import com.example.demo.dto.PatientResponseDTO;
import com.example.demo.exception.EmailAlreadyExistsException;
import com.example.demo.exception.PatientNotFoundException;
import com.example.demo.grpc.BilllingServiceGrpcClient;
import com.example.demo.kafka.KafkaProducer;
import com.example.demo.mapper.PatientMapper;
import com.example.demo.model.Patient;
import com.example.demo.repository.PatientRepository;
import org.springframework.stereotype.Service;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class PatientService {
    private static final Logger log = LoggerFactory.getLogger(PatientService.class);
    private final KafkaProducer kafkaProducer;

    private PatientRepository patientRepository;
    private BilllingServiceGrpcClient billlingServiceGrpcClient;

    public PatientService(PatientRepository patientRepository, BilllingServiceGrpcClient billlingServiceGrpcClient, KafkaProducer kafkaProducer) {
        this.patientRepository = patientRepository;
        this.billlingServiceGrpcClient = billlingServiceGrpcClient;
        this.kafkaProducer = kafkaProducer;
    }

    public List<PatientResponseDTO> getPatients(){
        List<Patient> patients = patientRepository.findAll();
        List<PatientResponseDTO> patientResponseDtos = patients.stream()
                .map(PatientMapper::toDTO)
                .collect(Collectors.toList());
        return patientResponseDtos;
    }

    public PatientResponseDTO createPatient(PatientRequestDTO patientRequestDTO) {
        try {
            if (patientRepository.existsByEmail(patientRequestDTO.getEmail())) {
                throw new EmailAlreadyExistsException("An user With this Email already exists, Email Search Result: " + patientRequestDTO.getEmail());
            }

            Patient newPatient = patientRepository.save(PatientMapper.toModel(patientRequestDTO));

            // Call gRPC service
            BillingResponse billingResponse = billlingServiceGrpcClient.createBillingAccount(
                    newPatient.getId().toString(), newPatient.getName(), newPatient.getEmail());
            kafkaProducer.sendEvent(newPatient);

            // Log gRPC response for debugging
            log.info("Billing service response: {}", billingResponse);

            return PatientMapper.toDTO(newPatient);
        } catch (Exception e) {
            log.error("Error creating patient: {}", e.getMessage(), e);
            throw new RuntimeException("Internal server error while creating patient", e);
        }
    }

    public PatientResponseDTO updatePatient(String id, PatientRequestDTO patientRequestDTO) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new PatientNotFoundException("Patient with this id can't be found: " + id));

        if (patientRepository.existsByEmailAndIdNot(patientRequestDTO.getEmail(), id)) {
            throw new EmailAlreadyExistsException("An user with this Email already exists: " + patientRequestDTO.getEmail());
        }

        patient.setName(patientRequestDTO.getName());
        patient.setAddress(patientRequestDTO.getAddress());
        patient.setEmail(patientRequestDTO.getEmail());

        String dob = patientRequestDTO.getDateOfBirth();
        if (dob != null && !dob.isEmpty()) {
            patient.setDateOfBirth(LocalDate.parse(dob));
        }

        String registered = patientRequestDTO.getRegisteredDate();
        if (registered != null && !registered.isEmpty()) {
            patient.setRegisteredDate(LocalDate.parse(registered));
        }

        Patient updatedPatient = patientRepository.save(patient);
        return PatientMapper.toDTO(updatedPatient);
    }

    public void deletePatient(String id){
        patientRepository.deleteById(id);
    }
}
