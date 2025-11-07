package io.github.alirostom1.logismart.controller;

import io.github.alirostom1.logismart.dto.request.person.CreatePersonRequest;
import io.github.alirostom1.logismart.dto.request.person.UpdatePersonRequest;
import io.github.alirostom1.logismart.dto.response.client.RecipientResponse;
import io.github.alirostom1.logismart.dto.response.client.SenderResponse;
import io.github.alirostom1.logismart.dto.response.common.ApiResponse;
import io.github.alirostom1.logismart.service.PersonService;
import io.github.alirostom1.logismart.util.ValidUUID;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/v2/persons")
@RequiredArgsConstructor
public class PersonController {
    private final PersonService personService;

    @GetMapping("/senders")
    public ResponseEntity<ApiResponse<Page<SenderResponse>>> showAllSenders(
            @ParameterObject Pageable pageable
            ){
        Page<SenderResponse> responses = personService.getAllSenders(pageable);
        ApiResponse<Page<SenderResponse>> apiResponse = new ApiResponse<>(
                true,
                "Senders retrieved successfully!",
                responses,
                System.currentTimeMillis()
        );
        return ResponseEntity.ok(apiResponse);
    }
    @GetMapping("/recipients")
    public ResponseEntity<ApiResponse<Page<RecipientResponse>>> showAllRecipients(
            @ParameterObject Pageable pageable
    ){
        Page<RecipientResponse> responses = personService.getAllRecipients(pageable);
        ApiResponse<Page<RecipientResponse>> apiResponse = new ApiResponse<>(
                true,
                "Senders retrieved successfully!",
                responses,
                System.currentTimeMillis()
        );
        return ResponseEntity.ok(apiResponse);
    }

    @GetMapping("/sender/{id}")
    public ResponseEntity<ApiResponse<SenderResponse>> showSender(@PathVariable @ValidUUID String id){
        SenderResponse response = personService.getSenderById(id);
        ApiResponse<SenderResponse> apiResponse = new ApiResponse<>(
                true,
                "Sender retrieved successfully!",
                response,
                System.currentTimeMillis()
        );
        return ResponseEntity.ok(apiResponse);
    }
    @GetMapping("/recipient/{id}")
    public ResponseEntity<ApiResponse<RecipientResponse>> showRecipient(@PathVariable @ValidUUID String id){
        RecipientResponse response = personService.getRecipientById(id);
        ApiResponse<RecipientResponse> apiResponse = new ApiResponse<>(
                true,
                "Recipient retrieved successfully!",
                response,
                System.currentTimeMillis()
        );
        return ResponseEntity.ok(apiResponse);
    }
    @PostMapping("/sender")
    public ResponseEntity<ApiResponse<SenderResponse>> createSender(@Valid @RequestBody CreatePersonRequest request){
        SenderResponse response = personService.createSender(request);
        ApiResponse<SenderResponse> apiResponse = new ApiResponse<>(
                true,
                "Sender created retrieved successfully!",
                response,
                System.currentTimeMillis()
        );
        return ResponseEntity.ok(apiResponse);
    }
    @PostMapping("/recipient")
    public ResponseEntity<ApiResponse<RecipientResponse>> createRecipient(@Valid @RequestBody CreatePersonRequest request){
        RecipientResponse response = personService.createRecipient(request);
        ApiResponse<RecipientResponse> apiResponse = new ApiResponse<>(
                true,
                "Recipient created retrieved successfully!",
                response,
                System.currentTimeMillis()
        );
        return ResponseEntity.ok(apiResponse);
    }
    @PutMapping("/sender/{id}")
    public ResponseEntity<ApiResponse<SenderResponse>> updateSender(@PathVariable @ValidUUID String id,@Valid @RequestBody UpdatePersonRequest request){
        SenderResponse response = personService.updateSender(id,request);
        ApiResponse<SenderResponse> apiResponse = new ApiResponse<>(
                true,
                "Sender updated successfully!",
                response,
                System.currentTimeMillis()
        );
        return ResponseEntity.ok(apiResponse);
    }
    @PutMapping("/recipient/{id}")
    public ResponseEntity<ApiResponse<RecipientResponse>> updateRecipient(@PathVariable @ValidUUID String id,@Valid @RequestBody UpdatePersonRequest request){
        RecipientResponse response = personService.updateRecipient(id,request);
        ApiResponse<RecipientResponse> apiResponse = new ApiResponse<>(
                true,
                "Recipient updated successfully!",
                response,
                System.currentTimeMillis()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(apiResponse);
    }

    @DeleteMapping("/sender/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteSender(@PathVariable @ValidUUID String id){
        personService.deleteSender(id);
        ApiResponse<Void> apiResponse = new ApiResponse<>(
                true,
                "Sender deleted successfully!",
                null,
                System.currentTimeMillis()
        );
        return ResponseEntity.ok(apiResponse);
    }
    @DeleteMapping("/recipient/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteRecipient(@PathVariable @ValidUUID String id){
        personService.deleteSender(id);
        ApiResponse<Void> apiResponse = new ApiResponse<>(
                true,
                "Recipient deleted successfully!",
                null,
                System.currentTimeMillis()
        );
        return ResponseEntity.ok(apiResponse);
    }

}
