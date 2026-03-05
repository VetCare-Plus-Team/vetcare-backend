package com.vetcare.vaccination_service.service;

import com.vetcare.vaccination_service.dtos.VaccinationDto;
import com.vetcare.vaccination_service.entity.Vaccination;
import com.vetcare.vaccination_service.repo.VaccinationRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VaccinationService {

  private final VaccinationRepository vaccinationRepository;
  private final ModelMapper modelMapper;

  public VaccinationDto createVaccination(VaccinationDto vaccinationDto) {
    Vaccination vaccination = modelMapper.map(vaccinationDto, Vaccination.class);
    Vaccination saved = vaccinationRepository.save(vaccination);
    return modelMapper.map(saved, VaccinationDto.class);
  }

  public List<VaccinationDto> getVaccinationsByPet(Long petId) {
    return vaccinationRepository.findByPetId(petId).stream()
        .map(vaccination -> modelMapper.map(vaccination, VaccinationDto.class))
        .collect(Collectors.toList());
  }

  public VaccinationDto getVaccinationById(Long id) {
    Vaccination vaccination = vaccinationRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Vaccination record not found with id: " + id));
    return modelMapper.map(vaccination, VaccinationDto.class);
  }
}
