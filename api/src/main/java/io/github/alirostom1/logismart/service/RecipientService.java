package io.github.alirostom1.logismart.service;

import io.github.alirostom1.logismart.dto.request.delivery.CreateDeliveryRequest;
import io.github.alirostom1.logismart.exception.ResourceNotFoundException;
import io.github.alirostom1.logismart.model.entity.Recipient;
import io.github.alirostom1.logismart.repository.RecipientRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class RecipientService {

    private final RecipientRepo recipientRepo;

    public Recipient findOrCreateRecipient(CreateDeliveryRequest.RecipientRequest request) {
        if(request.isNewRecipient()){
            return recipientRepo.findByPhone(request.phone())
                    .orElseThrow(() -> new ResourceNotFoundException("Recipient Not found"));
        }
        return createNewRecipient(request);
    }

    private Recipient createNewRecipient(CreateDeliveryRequest.RecipientRequest request) {
        Recipient recipient = Recipient.builder()
                .name(request.name())
                .phone(request.phone())
                .email(request.email())
                .build();
        return recipientRepo.save(recipient);
    }
}
