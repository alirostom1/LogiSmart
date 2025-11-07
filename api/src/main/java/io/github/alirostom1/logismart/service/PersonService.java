package io.github.alirostom1.logismart.service;

import io.github.alirostom1.logismart.dto.request.person.CreatePersonRequest;
import io.github.alirostom1.logismart.dto.request.person.UpdatePersonRequest;
import io.github.alirostom1.logismart.dto.response.client.RecipientResponse;
import io.github.alirostom1.logismart.dto.response.client.SenderResponse;
import io.github.alirostom1.logismart.dto.response.common.PersonResponse;
import io.github.alirostom1.logismart.exception.EmailAlreadyExistsException;
import io.github.alirostom1.logismart.exception.PhoneAlreadyExistsException;
import io.github.alirostom1.logismart.exception.ResourceNotFoundException;
import io.github.alirostom1.logismart.mapper.PersonMapper;
import io.github.alirostom1.logismart.model.entity.Recipient;
import io.github.alirostom1.logismart.model.entity.Sender;
import io.github.alirostom1.logismart.repository.PersonRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class PersonService {
    private final PersonRepo personRepo;
    private final PersonMapper personMapper;

    // CREATE SENDER (FOR MANAGER)
    public SenderResponse createSender(CreatePersonRequest request) {
        validatePhoneAndEmailCreation(request.getPhone(),request.getEmail());
        Sender sender = personMapper.toSenderEntity(request);
        Sender savedSender = personRepo.save(sender);
        return personMapper.toSenderResponse(savedSender);
    }

    // CREATE RECIPIENT (FOR MANAGER/SENDER)
    public RecipientResponse createRecipient(CreatePersonRequest request) {
        validatePhoneAndEmailCreation(request.getPhone(),request.getEmail());
        Recipient recipient = personMapper.toRecipientEntity(request);
        Recipient savedRecipient = personRepo.save(recipient);
        return personMapper.toRecipientResponse(savedRecipient);
    }

    // GET SENDER BY ID (FOR MANAGER/SENDER)
    @Transactional(readOnly = true)
    public SenderResponse getSenderById(String senderId) {
        Sender sender = findSenderById(senderId);
        return personMapper.toSenderResponse(sender);
    }

    // GET RECIPIENT BY ID (FOR MANAGER/SENDER/RECIPIENT)
    @Transactional(readOnly = true)
    public RecipientResponse getRecipientById(String recipientId) {
        Recipient recipient = findRecipientById(recipientId);
        return personMapper.toRecipientResponse(recipient);
    }

    // GET ALL SENDERS (FOR MANAGER)
    @Transactional(readOnly = true)
    public Page<SenderResponse> getAllSenders(Pageable pageable) {
        Page<Sender> senders = personRepo.findSenders(pageable);
        return senders.map(personMapper::toSenderResponse);
    }

    // GET ALL RECIPIENTS (FOR MANAGER)
    @Transactional(readOnly = true)
    public Page<RecipientResponse> getAllRecipients(Pageable pageable) {
        Page<Recipient> recipients = personRepo.findRecipients(pageable);
        return recipients.map(personMapper::toRecipientResponse);
    }

    // UPDATE SENDER (FOR MANAGER/SENDER)
    public SenderResponse updateSender(String senderId, UpdatePersonRequest request) {
        Sender sender = findSenderById(senderId);
        validatePhoneAndEmailUpdate(request.getPhone(),request.getEmail(),sender.getId());
        personMapper.updateSenderFromRequest(request,sender);
        Sender updatedSender = personRepo.save(sender);
        return personMapper.toSenderResponse(updatedSender);
    }

    // UPDATE RECIPIENT (FOR MANAGER/RECIPIENT)
    public RecipientResponse updateRecipient(String recipientId, UpdatePersonRequest request) {
        Recipient recipient = findRecipientById(recipientId);
        validatePhoneAndEmailUpdate(request.getPhone(),request.getEmail(),recipient.getId());
        personMapper.updateRecipientFromRequest(request,recipient);
        Recipient updatedRecipient = personRepo.save(recipient);
        return personMapper.toRecipientResponse(updatedRecipient);
    }

    // DELETE SENDER (FOR MANAGER)
    public void deleteSender(String senderId) {
        if(!personRepo.existsById(UUID.fromString(senderId))){
            throw new ResourceNotFoundException("Sender",senderId);
        }
        personRepo.deleteById(UUID.fromString(senderId));
    }

    // DELETE RECIPIENT (FOR MANAGER)
    public void deleteRecipient(String recipientId) {
        if(!personRepo.existsById(UUID.fromString(recipientId))){
            throw new ResourceNotFoundException("Recipient",recipientId);
        }
        personRepo.deleteById(UUID.fromString(recipientId));
    }

    // UTIL METHODS
    private Sender findSenderById(String senderId) {
        return personRepo.findSenderById(UUID.fromString(senderId))
                .orElseThrow(() -> new ResourceNotFoundException("Sender", senderId));
    }

    private Recipient findRecipientById(String recipientId) {
        return personRepo.findRecipientById(UUID.fromString(recipientId))
                .orElseThrow(() -> new ResourceNotFoundException("Recipient", recipientId));
    }

    // VALIDATION FOR EXISTING PHONE OR EMAIL(PERSON CREATION CASE)
    private void validatePhoneAndEmailCreation(String phone,String email){
        if(personRepo.existsByEmail(email)){
            throw new EmailAlreadyExistsException(email);
        }
        if(personRepo.existsByPhone(phone)){
            throw new PhoneAlreadyExistsException(phone);
        }
    }
    // VALIDATION FOR EXISTING PHONE OR EMAIL(PERSON CREATION CASE)
    private void validatePhoneAndEmailUpdate(String phone,String email,UUID id){
        if(personRepo.existsByEmailAndIdNot(email,id)){
            throw new EmailAlreadyExistsException(email);
        }
        if(personRepo.existsByPhoneAndIdNot(phone,id)){
            throw new PhoneAlreadyExistsException(phone);
        }
    }
}