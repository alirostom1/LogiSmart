package io.github.alirostom1.logismart.mapper;

import io.github.alirostom1.logismart.dto.request.person.CreatePersonRequest;
import io.github.alirostom1.logismart.dto.request.person.UpdatePersonRequest;
import io.github.alirostom1.logismart.dto.response.client.RecipientResponse;
import io.github.alirostom1.logismart.dto.response.client.SenderResponse;
import io.github.alirostom1.logismart.dto.response.common.PersonResponse;
import io.github.alirostom1.logismart.model.entity.Sender;
import io.github.alirostom1.logismart.model.entity.Recipient;
import io.github.alirostom1.logismart.model.entity.User;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PersonMapper {
    // RESPONSE MAPPING
    PersonResponse toResponse(User user);

    @Mapping(target = "totalDeliveriesSent", expression = "java(sender.getDeliveries().size())")
    @Mapping(target = "activeDeliveries", expression = "java((int) sender.getDeliveries().stream().filter(d -> d.getStatus() != io.github.alirostom1.logismart.model.enums.DeliveryStatus.DELIVERED).count())")
    SenderResponse toSenderResponse(Sender sender);

    @Mapping(target = "totalDeliveriesReceived", expression = "java(recipient.getDeliveries().size())")
    @Mapping(target = "pendingDeliveries", expression = "java((int) recipient.getDeliveries().stream().filter(d -> d.getStatus() != io.github.alirostom1.logismart.model.enums.DeliveryStatus.DELIVERED).count())")
    RecipientResponse toRecipientResponse(Recipient recipient);


    List<SenderResponse> toSenderResponseList(List<Sender> senders);
    List<RecipientResponse> toRecipientResponseList(List<Recipient> recipients);


    //REQUEST MAPPING
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "deliveries", ignore = true)
    Sender toSenderEntity(CreatePersonRequest request);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "deliveries", ignore = true)
    Recipient toRecipientEntity(CreatePersonRequest request);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "deliveries", ignore = true)
    void updateSenderFromRequest(UpdatePersonRequest request, @MappingTarget Sender sender);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "deliveries", ignore = true)
    void updateRecipientFromRequest(UpdatePersonRequest request, @MappingTarget Recipient recipient);
}