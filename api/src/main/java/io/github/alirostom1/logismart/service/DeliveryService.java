package io.github.alirostom1.logismart.service;

import io.github.alirostom1.logismart.dto.request.delivery.AssignDeliveryRequest;
import io.github.alirostom1.logismart.dto.request.delivery.CreateDeliveryRequest;
import io.github.alirostom1.logismart.dto.request.delivery.SearchDeliveryRequest;
import io.github.alirostom1.logismart.dto.request.delivery.UpdateDeliveryStatusRequest;
import io.github.alirostom1.logismart.dto.response.delivery.DeliveryDetailsResponse;
import io.github.alirostom1.logismart.dto.response.delivery.DeliveryResponse;
import io.github.alirostom1.logismart.dto.response.delivery.DeliveryTrackingResponse;
import io.github.alirostom1.logismart.exception.ResourceNotFoundException;
import io.github.alirostom1.logismart.mapper.DeliveryMapper;
import io.github.alirostom1.logismart.model.entity.*;
import io.github.alirostom1.logismart.model.enums.DeliveryStatus;
import io.github.alirostom1.logismart.repository.*;
import io.github.alirostom1.logismart.repository.specification.DeliverySpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class DeliveryService {
    private final DeliveryRepo deliveryRepo;
    private final DeliveryMapper deliveryMapper;
    private final PersonRepo personRepo;
    private final ZoneRepo zoneRepo;
    private final DeliveryHistoryRepo deliveryHistoryRepo;
    private final CourierRepo courierRepo;
    private final ProductRepo productRepo;
    private final DeliveryProductRepo deliveryProductRepo;


    //CREATE DELIVERY(FOR SENDER)

    public DeliveryDetailsResponse createDelivery(CreateDeliveryRequest request) {
        //GET SENDER AND RECIPIENT
        Person senderPerson = personRepo.findById(UUID.fromString(request.getSenderId()))
                .orElseThrow(() -> new ResourceNotFoundException("Sender",request.getSenderId()));
        Person recipientPerson = personRepo.findById(UUID.fromString(request.getRecipientId()))
                .orElseThrow(() -> new ResourceNotFoundException("Recipient",request.getRecipientId()));
        if(!(senderPerson instanceof Sender)){
            throw new ResourceNotFoundException("Sender",request.getSenderId());
        }
        if(!(recipientPerson instanceof Recipient)){
            throw new ResourceNotFoundException("Recipient",request.getRecipientId());
        }
        Sender sender = (Sender) senderPerson;
        Recipient recipient = (Recipient) recipientPerson;

        //GET ZONE
        Zone zone = zoneRepo.findById(UUID.fromString(request.getZoneId()))
                .orElseThrow(() -> new ResourceNotFoundException("Zone", request.getZoneId()));
        //ATTACH DELIVERY RELATIONSHIPS
        Delivery delivery = deliveryMapper.toEntity(request);
        delivery.setSender(sender);
        delivery.setRecipient(recipient);
        delivery.setZone(zone);

        //SAVE DELIVERY
        Delivery savedDelivery = deliveryRepo.save(delivery);

        //HANDLE DELIVERY PRODUCTS
        if(request.getProducts() != null && !request.getProducts().isEmpty()){
            List<DeliveryProduct> deliveryProducts = createDeliveryProducts(savedDelivery,request.getProducts());
            savedDelivery.setDeliveryProducts(deliveryProducts);
        }

        // CREATE DELIVERY HISTORY RECORD
        DeliveryHistory deliveryHistory = saveToHistory(savedDelivery,DeliveryStatus.CREATED,"Delivery created");
        savedDelivery.getDeliveryHistoryList().add(deliveryHistory);
        return deliveryMapper.toDetailsResponse(savedDelivery);
    }


    // RETRIEVE SPECIFIC DELIVERY (FOR MANAGER)
    @Transactional(readOnly = true)
    public DeliveryDetailsResponse getDeliveryById(String deliveryId){
        Delivery delivery = findById(deliveryId);
        return deliveryMapper.toDetailsResponse(delivery);
    }

    //TRACK SPECIFIC DELIVERY (FOR SENDER AND RECIPIENT)
    @Transactional(readOnly = true)
    public DeliveryTrackingResponse getDeliveryTracking(String deliveryId) {
        Delivery delivery = findById(deliveryId);
        return deliveryMapper.toTrackingResponse(delivery);
    }

    //UPDATE DELIVERY STATUS(FOR ALL ACTORS)
    public DeliveryDetailsResponse updateDeliveryStatus(String deliveryId,
                                                        UpdateDeliveryStatusRequest request){
        Delivery delivery = findById(deliveryId);
        DeliveryStatus status = DeliveryStatus.valueOf(request.getStatus());
        delivery.setStatus(status);
        Delivery savedDelivery = deliveryRepo.save(delivery);
        DeliveryHistory deliveryHistory = saveToHistory(savedDelivery,status,request.getComment());
        savedDelivery.getDeliveryHistoryList().add(deliveryHistory);
        return deliveryMapper.toDetailsResponse(savedDelivery);
    }

    //ASSIGN COURIER TO A COLLECT DELIVERY AFTER CREATION (FOR MANAGER)
    public DeliveryDetailsResponse assignCollectingCourier(String deliveryId,
                                                   AssignDeliveryRequest request){
        Delivery delivery = findById(deliveryId);
        Courier courier = courierRepo.findById(UUID.fromString(request.getCourierId()))
                .orElseThrow(() -> new ResourceNotFoundException("Courier",request.getCourierId()));
        delivery.setCollectingCourier(courier);
        Delivery savedDelivery = deliveryRepo.save(delivery);
        saveToHistory(savedDelivery,delivery.getStatus(),"Collection Assigned to courier " +
                courier.getFirstName() + " " + courier.getLastName());
        return deliveryMapper.toDetailsResponse(savedDelivery);
    }
    //ASSIGN COURIER TO A SHIP DELIVERY AFTER CREATION (FOR MANAGER)
    public DeliveryDetailsResponse assignShippingCourier(String deliveryId,
                                                           AssignDeliveryRequest request){
        Delivery delivery = findById(deliveryId);
        Courier courier = courierRepo.findById(UUID.fromString(request.getCourierId()))
                .orElseThrow(() -> new ResourceNotFoundException("Courier",request.getCourierId()));
        delivery.setShippingCourier(courier);
        Delivery savedDelivery = deliveryRepo.save(delivery);
        saveToHistory(savedDelivery,delivery.getStatus(),"Shipping Assigned to courier " +
                courier.getFirstName() + " " + courier.getLastName());
        return deliveryMapper.toDetailsResponse(savedDelivery);
    }

    //SEARCH DELIVERY WITH MULTIPLE FILTERS AND SORT (FOR MORE DETAILS CHECK THE SEARCH REQUEST DTO),(( FOR MANAGER))
    @Transactional(readOnly = true)
    public Page<DeliveryResponse> searchDeliveries(SearchDeliveryRequest request,Pageable pageable){
        Specification<Delivery> spec = buildSearchSpecification(request);
        Page<Delivery> deliveries = deliveryRepo.findAll(spec,pageable);
        return deliveries.map(deliveryMapper::toResponse);
    }

    //GET SENDER'S DELIVERIES(FOR SENDER)
    @Transactional(readOnly = true)
    public Page<DeliveryResponse> getDeliveriesBySender(String senderId, Pageable pageable){
        Page<Delivery> deliveries = deliveryRepo.findBySenderId(UUID.fromString(senderId),pageable);
        return deliveries.map(deliveryMapper::toResponse);
    }
    //GET RECIPIENT'S DELIVERIES(FOR RECIPIENT)
    @Transactional(readOnly = true)
    public Page<DeliveryResponse> getDeliveriesByRecipient(String recipientId, Pageable pageable){
        Page<Delivery> deliveries = deliveryRepo.findByRecipientId(UUID.fromString(recipientId),pageable);
        return deliveries.map(deliveryMapper::toResponse);
    }
    //GET COURIER'S DELIVERIES(FOR COURIER)
    @Transactional(readOnly = true)
    public Page<DeliveryResponse> getDeliveriesByCourier(String courierId, Pageable pageable){
        UUID id = UUID.fromString(courierId);
        Page<Delivery> deliveries = deliveryRepo.findByCollectingCourierIdOrShippingCourierId(id,id,pageable);
        return deliveries.map(deliveryMapper::toResponse);
    }

    //DELETE DELIVERY (FOR MANAGER)
    public void deleteDelivery(String deliveryId){
        Delivery delivery = findById(deliveryId);
        if(delivery.getStatus() != DeliveryStatus.DELIVERED && delivery.getStatus() != DeliveryStatus.CREATED){
            throw new RuntimeException("Couldn't delete a undelievered delivery!");
        }
        deliveryRepo.deleteById(UUID.fromString(deliveryId));
    }



    // UTIL METHODS
    // SAVE HISTORY UPDATE
    private DeliveryHistory saveToHistory(Delivery delivery, DeliveryStatus status,String comment){
        DeliveryHistory history = new DeliveryHistory();
        history.setDelivery(delivery);
        history.setStatus(status);
        history.setComment(comment);
        return deliveryHistoryRepo.save(history);
    }
    // FIND DELIVERY BY ID AND THROW EXCEPTION IF NOT FOUND
    private Delivery findById(String deliveryId){
        return deliveryRepo.findById(UUID.fromString(deliveryId))
                .orElseThrow(() -> new ResourceNotFoundException("Delivery",deliveryId));
    }
    // BUILD SEARCH AND FILTER SPECIFICATION
    private Specification<Delivery> buildSearchSpecification(SearchDeliveryRequest request){
        return Specification.allOf(
                DeliverySpecification.hasSearchTerm(request.getSearchTerm()),
                DeliverySpecification.hasStatus(request.getStatus()),
                DeliverySpecification.hasPriority(request.getPriority()),
                DeliverySpecification.hasZoneId(request.getZoneId()),
                DeliverySpecification.hasCity(request.getCity()),
                DeliverySpecification.hasCourierId(request.getCourierId()),
                DeliverySpecification.hasSenderId(request.getSenderId()),
                DeliverySpecification.hasRecipientId(request.getRecipientId())
        );
    }
    //SAVE LIST OF PRODUCTS TO DELIVERY
    private List<DeliveryProduct> createDeliveryProducts(Delivery delivery,
                                                         List<CreateDeliveryRequest.DeliveryProductRequest> productRequests){
        List<DeliveryProduct> deliveryProducts = new ArrayList<>();
        for(CreateDeliveryRequest.DeliveryProductRequest productRequest : productRequests){
            Product product = productRepo.findById(UUID.fromString(productRequest.getProductId()))
                    .orElseThrow(() -> new ResourceNotFoundException("Product",productRequest.getProductId()));
            double totalPrice = product.getUnitPrice() * productRequest.getQuantity();
            DeliveryProduct deliveryProduct = new DeliveryProduct();
            deliveryProduct.setId(new DeliveryProductId(product.getId(),delivery.getId()));
            deliveryProduct.setPrice(totalPrice);
            deliveryProduct.setQuantity(productRequest.getQuantity());
            deliveryProduct.setDelivery(delivery);
            deliveryProduct.setProduct(product);
            deliveryProducts.add(deliveryProduct);
        }
        return deliveryProductRepo.saveAll(deliveryProducts);
    }
}
