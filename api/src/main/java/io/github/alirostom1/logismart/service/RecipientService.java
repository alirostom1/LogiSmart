package io.github.alirostom1.logismart.service;

import io.github.alirostom1.logismart.dto.request.delivery.CreateDeliveryRequest;
import io.github.alirostom1.logismart.dto.request.person.UpdatePersonRequest;
import io.github.alirostom1.logismart.dto.response.client.RecipientResponse;
import io.github.alirostom1.logismart.exception.EmailAlreadyExistsException;
import io.github.alirostom1.logismart.exception.PhoneAlreadyExistsException;
import io.github.alirostom1.logismart.exception.ResourceNotFoundException;
import io.github.alirostom1.logismart.mapper.PersonMapper;
import io.github.alirostom1.logismart.model.entity.Recipient;
import io.github.alirostom1.logismart.repository.RecipientRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class RecipientService {

    private final RecipientRepo recipientRepo;
    private final PersonMapper personMapper;

    public Recipient findOrCreateRecipient(CreateDeliveryRequest.RecipientRequest request) {
        if(!request.isNewRecipient()){
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

    // GET RECIPIENT BY ID (FOR MANAGER)
    @Transactional(readOnly = true)
    public RecipientResponse getRecipientById(Long recipientId) {
        Recipient recipient = findRecipientById(recipientId);
        return personMapper.toRecipientResponse(recipient);
    }

    // GET ALL RECIPIENTS (FOR MANAGER)
    @Transactional(readOnly = true)
    public Page<RecipientResponse> getAllRecipients(Pageable pageable) {
        Page<Recipient> recipients = recipientRepo.findAll(pageable);
        return recipients.map(personMapper::toRecipientResponse);
    }

    // UPDATE RECIPIENT (FOR MANAGER)
    public RecipientResponse updateRecipient(Long recipientId, UpdatePersonRequest request) {
        Recipient recipient = findRecipientById(recipientId);
        validatePhoneAndEmailUpdate(request.getPhone(), request.getEmail(), recipient.getId());
        // Update name field (Recipient uses name, not firstName/lastName)
        recipient.setName(request.getFirstName() + " " + request.getLastName());
        recipient.setEmail(request.getEmail());
        recipient.setPhone(request.getPhone());
        Recipient updatedRecipient = recipientRepo.save(recipient);
        return personMapper.toRecipientResponse(updatedRecipient);
    }

    // DELETE RECIPIENT (FOR MANAGER)
    public void deleteRecipient(Long recipientId) {
        if(!recipientRepo.existsById(recipientId)){
            throw new ResourceNotFoundException("Recipient not found");
        }
        recipientRepo.deleteById(recipientId);
    }

    // UTIL METHODS
    private Recipient findRecipientById(Long recipientId) {
        return recipientRepo.findById(recipientId)
                .orElseThrow(() -> new ResourceNotFoundException("Recipient not found"));
    }

    // VALIDATION FOR EXISTING PHONE OR EMAIL
    private void validatePhoneAndEmailUpdate(String phone, String email, Long id){
        if(recipientRepo.existsByEmailAndIdNot(email, id)){
            throw new EmailAlreadyExistsException(email);
        }
        if(recipientRepo.existsByPhoneAndIdNot(phone, id)){
            throw new PhoneAlreadyExistsException(phone);
        }
    }
}
