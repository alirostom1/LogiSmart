package io.github.alirostom1.logismart.controller;

import io.github.alirostom1.logismart.dto.request.person.CreatePersonRequest;
import io.github.alirostom1.logismart.dto.request.person.UpdatePersonRequest;
import io.github.alirostom1.logismart.dto.response.client.RecipientResponse;
import io.github.alirostom1.logismart.dto.response.client.SenderResponse;
import io.github.alirostom1.logismart.dto.response.common.DefaultApiResponse;
import io.github.alirostom1.logismart.service.PersonService;
import io.github.alirostom1.logismart.util.ValidUUID;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v2/persons")
@Tag(name = "Person API", description = "Operations for senders and recipients")
@RequiredArgsConstructor
public class PersonController {
    private final PersonService personService;

    @Operation(summary = "Get all senders")
    @ApiResponse(responseCode = "200", description = "Senders list retrieved successfully")
    @GetMapping("/senders")
    public ResponseEntity<DefaultApiResponse<Page<SenderResponse>>> showAllSenders(
            @ParameterObject Pageable pageable
            ){
        Page<SenderResponse> responses = personService.getAllSenders(pageable);
        DefaultApiResponse<Page<SenderResponse>> defaultApiResponse = new DefaultApiResponse<>(
                true,
                "Senders retrieved successfully!",
                responses,
                System.currentTimeMillis()
        );
        return ResponseEntity.ok(defaultApiResponse);
    }

    @Operation(summary = "Get all recipients")
    @ApiResponse(responseCode = "200", description = "Recipients list retrieved successfully")
    @GetMapping("/recipients")
    public ResponseEntity<DefaultApiResponse<Page<RecipientResponse>>> showAllRecipients(
            @ParameterObject Pageable pageable
    ){
        Page<RecipientResponse> responses = personService.getAllRecipients(pageable);
        DefaultApiResponse<Page<RecipientResponse>> defaultApiResponse = new DefaultApiResponse<>(
                true,
                "Senders retrieved successfully!",
                responses,
                System.currentTimeMillis()
        );
        return ResponseEntity.ok(defaultApiResponse);
    }

    @Operation(summary = "Get sender by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Sender retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Sender not found")
    })
    @GetMapping("/sender/{id}")
    public ResponseEntity<DefaultApiResponse<SenderResponse>> showSender(
            @Parameter(description = "Sender ID") @PathVariable @ValidUUID String id){
        SenderResponse response = personService.getSenderById(id);
        DefaultApiResponse<SenderResponse> defaultApiResponse = new DefaultApiResponse<>(
                true,
                "Sender retrieved successfully!",
                response,
                System.currentTimeMillis()
        );
        return ResponseEntity.ok(defaultApiResponse);
    }

    @Operation(summary = "Get recipient by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Recipient retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Recipient not found")
    })
    @GetMapping("/recipient/{id}")
    public ResponseEntity<DefaultApiResponse<RecipientResponse>> showRecipient(
            @Parameter(description = "Recipient ID") @PathVariable @ValidUUID String id){
        RecipientResponse response = personService.getRecipientById(id);
        DefaultApiResponse<RecipientResponse> defaultApiResponse = new DefaultApiResponse<>(
                true,
                "Recipient retrieved successfully!",
                response,
                System.currentTimeMillis()
        );
        return ResponseEntity.ok(defaultApiResponse);
    }

    @Operation(summary = "Create new recipient")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Recipient created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request body")
    })
    @PostMapping("/sender")
    public ResponseEntity<DefaultApiResponse<SenderResponse>> createSender(@Valid @RequestBody CreatePersonRequest request){
        SenderResponse response = personService.createSender(request);
        DefaultApiResponse<SenderResponse> defaultApiResponse = new DefaultApiResponse<>(
                true,
                "Sender created retrieved successfully!",
                response,
                System.currentTimeMillis()
        );
        return ResponseEntity.ok(defaultApiResponse);
    }

    @Operation(summary = "Create new recipient")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Recipient created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request body")
    })
    @PostMapping("/recipient")
    public ResponseEntity<DefaultApiResponse<RecipientResponse>> createRecipient(@Valid @RequestBody CreatePersonRequest request){
        RecipientResponse response = personService.createRecipient(request);
        DefaultApiResponse<RecipientResponse> defaultApiResponse = new DefaultApiResponse<>(
                true,
                "Recipient created retrieved successfully!",
                response,
                System.currentTimeMillis()
        );
        return ResponseEntity.ok(defaultApiResponse);
    }

    @Operation(summary = "Update sender")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Sender updated successfully"),
            @ApiResponse(responseCode = "404", description = "Sender not found")
    })
    @PutMapping("/sender/{id}")
    public ResponseEntity<DefaultApiResponse<SenderResponse>> updateSender(
            @Parameter(description = "Sender ID") @PathVariable @ValidUUID String id,
            @Valid @RequestBody UpdatePersonRequest request){
        SenderResponse response = personService.updateSender(id,request);
        DefaultApiResponse<SenderResponse> defaultApiResponse = new DefaultApiResponse<>(
                true,
                "Sender updated successfully!",
                response,
                System.currentTimeMillis()
        );
        return ResponseEntity.ok(defaultApiResponse);
    }

    @Operation(summary = "Update recipient")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Recipient updated successfully"),
            @ApiResponse(responseCode = "404", description = "Recipient not found")
    })
    @PutMapping("/recipient/{id}")
    public ResponseEntity<DefaultApiResponse<RecipientResponse>> updateRecipient(
            @Parameter(description = "Recipient ID") @PathVariable @ValidUUID String id,
            @Valid @RequestBody UpdatePersonRequest request){
        RecipientResponse response = personService.updateRecipient(id,request);
        DefaultApiResponse<RecipientResponse> defaultApiResponse = new DefaultApiResponse<>(
                true,
                "Recipient updated successfully!",
                response,
                System.currentTimeMillis()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(defaultApiResponse);
    }


    @Operation(summary = "Delete sender")
    @ApiResponse(responseCode = "200", description = "Sender deleted successfully")
    @DeleteMapping("/sender/{id}")
    public ResponseEntity<DefaultApiResponse<Void>> deleteSender(
            @Parameter(description = "Sender ID") @PathVariable @ValidUUID String id){
        personService.deleteSender(id);
        DefaultApiResponse<Void> defaultApiResponse = new DefaultApiResponse<>(
                true,
                "Sender deleted successfully!",
                null,
                System.currentTimeMillis()
        );
        return ResponseEntity.ok(defaultApiResponse);
    }

    @Operation(summary = "Delete recipient")
    @ApiResponse(responseCode = "200", description = "Recipient deleted successfully")
    @DeleteMapping("/recipient/{id}")
    public ResponseEntity<DefaultApiResponse<Void>> deleteRecipient(
            @Parameter(description = "Recipient ID") @PathVariable @ValidUUID String id){
        personService.deleteSender(id);
        DefaultApiResponse<Void> defaultApiResponse = new DefaultApiResponse<>(
                true,
                "Recipient deleted successfully!",
                null,
                System.currentTimeMillis()
        );
        return ResponseEntity.ok(defaultApiResponse);
    }

}
