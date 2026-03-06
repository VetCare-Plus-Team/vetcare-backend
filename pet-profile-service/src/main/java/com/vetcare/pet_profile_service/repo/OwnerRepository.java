package com.vetcare.pet_profile_service.repo;

import com.vetcare.pet_profile_service.entity.Owner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface OwnerRepository extends JpaRepository<Owner,Long> {
}
