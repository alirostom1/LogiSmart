package io.github.alirostom1.logismart.service;

import io.github.alirostom1.logismart.dto.request.person.CreatePersonRequest;
import io.github.alirostom1.logismart.dto.request.person.UpdatePersonRequest;
import io.github.alirostom1.logismart.dto.response.client.RecipientResponse;
import io.github.alirostom1.logismart.dto.response.client.SenderResponse;
import io.github.alirostom1.logismart.exception.EmailAlreadyExistsException;
import io.github.alirostom1.logismart.exception.PhoneAlreadyExistsException;
import io.github.alirostom1.logismart.exception.ResourceNotFoundException;
import io.github.alirostom1.logismart.mapper.PersonMapper;
import io.github.alirostom1.logismart.model.entity.Recipient;
import io.github.alirostom1.logismart.model.entity.Sender;
import io.github.alirostom1.logismart.repository.PersonRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.Mock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
public class PersonServiceUnitTest {

    @Mock
    PersonRepo personRepo;

    PersonMapper personMapper = Mappers.getMapper(PersonMapper.class);
    PersonService personService;

    @BeforeEach
    public void setup() {
        personService = new PersonService(personRepo, personMapper);
    }

    // CREATE SENDER
    @Test
    public void createSender_should_return_sender_response() {
        CreatePersonRequest req = new CreatePersonRequest("Doe", "John", "john@mail.com", "0711111111", "123 Road");
        Sender sender = personMapper.toSenderEntity(req);
        Sender savedSender = sender; // In real use, assign an ID here if needed
        SenderResponse expectedResp = personMapper.toSenderResponse(savedSender);

        when(personRepo.existsByEmail(req.getEmail())).thenReturn(false);
        when(personRepo.existsByPhone(req.getPhone())).thenReturn(false);
        when(personRepo.save(any(Sender.class))).thenReturn(savedSender);

        SenderResponse response = personService.createSender(req);

        assertEquals(expectedResp, response);
    }

    @Test
    public void createSender_should_throw_email_duplication() {
        CreatePersonRequest req = new CreatePersonRequest("Doe", "John", "john@mail.com", "0711111111", "123 Road");
        when(personRepo.existsByEmail(req.getEmail())).thenReturn(true);
        assertThrows(EmailAlreadyExistsException.class, () -> personService.createSender(req));
    }

    @Test
    public void createSender_should_throw_phone_duplication() {
        CreatePersonRequest req = new CreatePersonRequest("Doe", "John", "john@mail.com", "0711111111", "123 Road");
        when(personRepo.existsByEmail(req.getEmail())).thenReturn(false);
        when(personRepo.existsByPhone(req.getPhone())).thenReturn(true);
        assertThrows(PhoneAlreadyExistsException.class, () -> personService.createSender(req));
    }

    // CREATE RECIPIENT
    @Test
    public void createRecipient_should_return_recipient_response() {
        CreatePersonRequest req = new CreatePersonRequest("Smith", "Jane", "jane@mail.com", "0722222222", "456 Lane");
        Recipient recipient = personMapper.toRecipientEntity(req);
        Recipient savedRecipient = recipient;
        RecipientResponse expectedResp = personMapper.toRecipientResponse(savedRecipient);

        when(personRepo.existsByEmail(req.getEmail())).thenReturn(false);
        when(personRepo.existsByPhone(req.getPhone())).thenReturn(false);
        when(personRepo.save(any(Recipient.class))).thenReturn(savedRecipient);

        RecipientResponse response = personService.createRecipient(req);

        assertEquals(expectedResp, response);
    }

    // GET SENDER BY ID
    @Test
    public void getSenderById_should_return_sender_response() {
        UUID id = UUID.randomUUID();
        Sender sender = new Sender();
        sender.setId(id);
        sender.setLastName("Doe");
        sender.setFirstName("John");
        sender.setEmail("mail@mail.com");
        sender.setPhone("9999999999");
        sender.setAddress("Address");

        SenderResponse expectedResp = personMapper.toSenderResponse(sender);

        when(personRepo.findSenderById(id)).thenReturn(Optional.of(sender));

        SenderResponse response = personService.getSenderById(id.toString());
        assertEquals(expectedResp, response);
    }

    @Test
    public void getSenderById_should_throw_not_found() {
        UUID id = UUID.randomUUID();
        when(personRepo.findSenderById(id)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> personService.getSenderById(id.toString()));
    }

    // GET RECIPIENT BY ID
    @Test
    public void getRecipientById_should_return_recipient_response() {
        UUID id = UUID.randomUUID();
        Recipient recipient = new Recipient();
        recipient.setId(id);
        recipient.setLastName("Smith");
        recipient.setFirstName("Jane");
        recipient.setEmail("jane@mail.com");
        recipient.setPhone("8888888888");
        recipient.setAddress("Somewhere");

        RecipientResponse expectedResp = personMapper.toRecipientResponse(recipient);

        when(personRepo.findRecipientById(id)).thenReturn(Optional.of(recipient));

        RecipientResponse response = personService.getRecipientById(id.toString());
        assertEquals(expectedResp, response);
    }

    @Test
    public void getRecipientById_should_throw_not_found() {
        UUID id = UUID.randomUUID();
        when(personRepo.findRecipientById(id)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> personService.getRecipientById(id.toString()));
    }

    // GET ALL SENDERS
    @Test
    public void getAllSenders_should_return_senders() {
        Sender sender1 = new Sender(UUID.randomUUID(), "Doe", "John", "john@mail.com", "0711111111", "Address",List.of());
        Sender sender2 = new Sender(UUID.randomUUID(), "Doe2", "John2", "john2@mail.com", "0712222222", "Address2",List.of());
        Pageable pageable = PageRequest.of(0, 10);
        Page<Sender> page = new PageImpl<>(List.of(sender1, sender2), pageable, 2);
        Page<SenderResponse> expected = page.map(personMapper::toSenderResponse);

        when(personRepo.findSenders(pageable)).thenReturn(page);

        Page<SenderResponse> response = personService.getAllSenders(pageable);
        assertEquals(expected, response);
    }

    // GET ALL RECIPIENTS
    @Test
    public void getAllRecipients_should_return_recipients() {
        Recipient recipient1 = new Recipient(UUID.randomUUID(), "Smith", "Jane", "jane@mail.com", "0722222222", "Place",List.of());
        Recipient recipient2 = new Recipient(UUID.randomUUID(), "Brown", "Alice", "alice@mail.com", "0733333333", "Another Place",List.of());
        Pageable pageable = PageRequest.of(0, 10);
        Page<Recipient> page = new PageImpl<>(List.of(recipient1, recipient2), pageable, 2);
        Page<RecipientResponse> expected = page.map(personMapper::toRecipientResponse);

        when(personRepo.findRecipients(pageable)).thenReturn(page);

        Page<RecipientResponse> response = personService.getAllRecipients(pageable);
        assertEquals(expected, response);
    }

    // UPDATE SENDER
    @Test
    public void updateSender_should_update_and_return() {
        UUID id = UUID.randomUUID();
        UpdatePersonRequest req = new UpdatePersonRequest("Doe", "John", "john@newmail.com", "0799999999", "New Address");
        Sender sender = new Sender(id, "Doe", "John", "john@mail.com", "0711111111", "Old Address",List.of());
        Sender updatedSender = sender;
        personMapper.updateSenderFromRequest(req, sender);
        SenderResponse expectedResp = personMapper.toSenderResponse(updatedSender);

        when(personRepo.findSenderById(id)).thenReturn(Optional.of(sender));
        when(personRepo.existsByEmailAndIdNot(req.getEmail(), id)).thenReturn(false);
        when(personRepo.existsByPhoneAndIdNot(req.getPhone(), id)).thenReturn(false);
        when(personRepo.save(sender)).thenReturn(updatedSender);

        SenderResponse response = personService.updateSender(id.toString(), req);
        assertEquals(expectedResp, response);
    }

    @Test
    public void updateSender_should_throw_email_exists() {
        UUID id = UUID.randomUUID();
        UpdatePersonRequest req = new UpdatePersonRequest("Doe", "John", "mail@mail.com", "0799999999", "Address");
        Sender sender = new Sender();
        sender.setId(id);
        when(personRepo.findSenderById(id)).thenReturn(Optional.of(sender));
        when(personRepo.existsByEmailAndIdNot(req.getEmail(), id)).thenReturn(true);
        assertThrows(EmailAlreadyExistsException.class, () -> personService.updateSender(id.toString(), req));
    }

    @Test
    public void updateSender_should_throw_phone_exists() {
        UUID id = UUID.randomUUID();
        UpdatePersonRequest req = new UpdatePersonRequest("Doe", "John", "mail@mail.com", "0799999999", "Address");
        Sender sender = new Sender();
        sender.setId(id);
        when(personRepo.findSenderById(id)).thenReturn(Optional.of(sender));
        when(personRepo.existsByEmailAndIdNot(req.getEmail(), id)).thenReturn(false);
        when(personRepo.existsByPhoneAndIdNot(req.getPhone(), id)).thenReturn(true);
        assertThrows(PhoneAlreadyExistsException.class, () -> personService.updateSender(id.toString(), req));
    }

    @Test
    public void updateSender_should_throw_not_found() {
        UUID id = UUID.randomUUID();
        UpdatePersonRequest req = new UpdatePersonRequest("Doe", "John", "mail@mail.com", "0799999999", "Address");
        when(personRepo.findSenderById(id)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> personService.updateSender(id.toString(), req));
    }

    // UPDATE RECIPIENT
    @Test
    public void updateRecipient_should_update_and_return() {
        UUID id = UUID.randomUUID();
        UpdatePersonRequest req = new UpdatePersonRequest("Smith", "Jane", "jane@newmail.com", "0788888888", "New Place");
        Recipient recipient = new Recipient(id, "Smith", "Jane", "jane@mail.com", "0722222222", "Old Place",List.of());
        Recipient updatedRecipient = recipient;
        personMapper.updateRecipientFromRequest(req, recipient); // MapStruct updates in-place
        RecipientResponse expectedResp = personMapper.toRecipientResponse(updatedRecipient);

        when(personRepo.findRecipientById(id)).thenReturn(Optional.of(recipient));
        when(personRepo.existsByEmailAndIdNot(req.getEmail(), id)).thenReturn(false);
        when(personRepo.existsByPhoneAndIdNot(req.getPhone(), id)).thenReturn(false);
        when(personRepo.save(recipient)).thenReturn(updatedRecipient);

        RecipientResponse response = personService.updateRecipient(id.toString(), req);
        assertEquals(expectedResp, response);
    }

    // DELETE SENDER
    @Test
    public void deleteSender_should_delete_if_exists() {
        UUID id = UUID.randomUUID();
        when(personRepo.existsById(id)).thenReturn(true);
        personService.deleteSender(id.toString());
        verify(personRepo, times(1)).deleteById(id);
    }

    @Test
    public void deleteSender_should_throw_not_found() {
        UUID id = UUID.randomUUID();
        when(personRepo.existsById(id)).thenReturn(false);
        assertThrows(ResourceNotFoundException.class, () -> personService.deleteSender(id.toString()));
    }

    // DELETE RECIPIENT
    @Test
    public void deleteRecipient_should_delete_if_exists() {
        UUID id = UUID.randomUUID();
        when(personRepo.existsById(id)).thenReturn(true);
        personService.deleteRecipient(id.toString());
        verify(personRepo, times(1)).deleteById(id);
    }

    @Test
    public void deleteRecipient_should_throw_not_found() {
        UUID id = UUID.randomUUID();
        when(personRepo.existsById(id)).thenReturn(false);
        assertThrows(ResourceNotFoundException.class, () -> personService.deleteRecipient(id.toString()));
    }
}
