package com.vetcare.medical_record_service.service;

import com.vetcare.medical_record_service.dtos.MedicalRecordDto;
import com.vetcare.medical_record_service.entity.MedicalRecord;
import com.vetcare.medical_record_service.repo.MedicalRecordRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MedicalRecordService {

  private final MedicalRecordRepository medicalRecordRepository;
  private final ModelMapper modelMapper;

  public MedicalRecordDto createRecord(MedicalRecordDto recordDto) {
    MedicalRecord record = modelMapper.map(recordDto, MedicalRecord.class);
    MedicalRecord saved = medicalRecordRepository.save(record);
    return modelMapper.map(saved, MedicalRecordDto.class);
  }

  public List<MedicalRecordDto> getRecordsByPet(Long petId) {
    return medicalRecordRepository.findByPetId(petId).stream()
        .map(record -> modelMapper.map(record, MedicalRecordDto.class))
        .collect(Collectors.toList());
  }

  public MedicalRecordDto getRecordById(Long id) {
    MedicalRecord record = medicalRecordRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Medical record not found with id: " + id));
    return modelMapper.map(record, MedicalRecordDto.class);
  }
}
