package org.zelator.service;


import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.zelator.entity.CalendarEvent;
import org.zelator.entity.MassRequest;
import org.zelator.repository.MassRequestRepository;

@Service
@RequiredArgsConstructor
public class MassRequestService {


    private final MassRequestRepository massRequestRepository;
    private final CalendarEventService calendarEventService;


    public void rejectRequest(Long id) {
        MassRequest request = massRequestRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Request not found."));

        request.setStatus(MassRequest.MassStatus.REJECTED);

        massRequestRepository.save(request);
    }


    public void approveRequest(Long id) {
        MassRequest request = massRequestRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Request not found."));

        request.setStatus(MassRequest.MassStatus.APPROVED);

        calendarEventService.createMassEvent(request);
        massRequestRepository.save(request);
    }

}
