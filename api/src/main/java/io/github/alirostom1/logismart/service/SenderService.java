package io.github.alirostom1.logismart.service;

import io.github.alirostom1.logismart.dto.request.person.UpdatePersonRequest;
import io.github.alirostom1.logismart.dto.response.client.RecipientResponse;
import io.github.alirostom1.logismart.dto.response.client.SenderResponse;
import io.github.alirostom1.logismart.exception.EmailAlreadyExistsException;
import io.github.alirostom1.logismart.exception.PhoneAlreadyExistsException;
import io.github.alirostom1.logismart.exception.ResourceNotFoundException;
import io.github.alirostom1.logismart.mapper.PersonMapper;
import io.github.alirostom1.logismart.model.entity.Recipient;
import io.github.alirostom1.logismart.model.entity.Sender;
import io.github.alirostom1.logismart.repository.SenderRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class SenderService{
    private final SenderRepo senderRepo;
    private final PersonMapper personMapper;

    // GET SENDER BY ID (FOR MANAGER/SENDER)
    @Transactional(readOnly = true)
    public SenderResponse getSenderById(Long senderId) {
        Sender sender = findSenderById(senderId);
        return personMapper.toSenderResponse(sender);
    }

    // GET ALL SENDERS (FOR MANAGER)
    @Transactional(readOnly = true)
    public Page<SenderResponse> getAllSenders(Pageable pageable) {
        Page<Sender> senders = senderRepo.findAll(pageable);
        return senders.map(personMapper::toSenderResponse);
    }

    // UPDATE SENDER (FOR MANAGER/SENDER)
    public SenderResponse updateSender(Long senderId, UpdatePersonRequest request) {
        Sender sender = findSenderById(senderId);
        validatePhoneAndEmailUpdate(request.getPhone(),request.getEmail(),sender.getId());
        personMapper.updateSenderFromRequest(request,sender);
        Sender updatedSender = senderRepo.save(sender);
        return personMapper.toSenderResponse(updatedSender);
    }


    // DELETE SENDER (FOR MANAGER)
    public void deleteSender(Long senderId) {
        if(!senderRepo.existsById(senderId)){
            throw new ResourceNotFoundException("Sender not found");
        }
        senderRepo.deleteById(senderId);
    }

    // UTIL METHODS
    private Sender findSenderById(Long senderId) {
        return senderRepo.findById(senderId)
                .orElseThrow(() -> new ResourceNotFoundException("Sender not found"));
    }

    // VALIDATION FOR EXISTING PHONE OR EMAIL(PERSON CREATION CASE)
    private void validatePhoneAndEmailUpdate(String phone,String email,Long id){
        if(senderRepo.existsByEmailAndIdNot(email,id)){
            throw new EmailAlreadyExistsException(email);
        }
        if(senderRepo.existsByPhoneAndIdNot(phone,id)){
            throw new PhoneAlreadyExistsException(phone);
        }
    }
}