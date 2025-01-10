package org.zelator.service;


import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.zelator.entity.Mystery;
import org.zelator.entity.User;
import org.zelator.repository.MysteryRepository;
import org.zelator.repository.UserRepository;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MysteryService {


    private final MysteryRepository mysteryRepository;
    private final UserRepository userRepository;


    public List<Mystery> getAvailableMysteries(Long roseId) {
        List<User> members = userRepository.findByGroupId(roseId)
                .orElseThrow(() -> new EntityNotFoundException("Ta róża nie ma członków."));

        List<Mystery> assignedMysteries = members.stream()
                .map(User::getMystery)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        return mysteryRepository.findAll().stream()
                .filter(mystery -> !assignedMysteries.contains(mystery))
                .collect(Collectors.toList());
    }


    public List<Mystery> getAllMysteries() {
        return mysteryRepository.findAll();
    }

}
