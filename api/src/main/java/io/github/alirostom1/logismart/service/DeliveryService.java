package io.github.alirostom1.logismart.service;

import io.github.alirostom1.logismart.dto.request.delivery.AssignDeliveryRequest;
import io.github.alirostom1.logismart.dto.request.delivery.CreateDeliveryRequest;
import io.github.alirostom1.logismart.dto.request.delivery.SearchDeliveryRequest;
import io.github.alirostom1.logismart.dto.request.delivery.UpdateDeliveryStatusRequest;
import io.github.alirostom1.logismart.dto.response.delivery.DeliveryDetailsResponse;
import io.github.alirostom1.logismart.dto.response.delivery.DeliveryResponse;
import io.github.alirostom1.logismart.dto.response.delivery.DeliveryTrackingResponse;
import io.github.alirostom1.logismart.exception.DeliveryCourierZoneMismatchException;
import io.github.alirostom1.logismart.exception.ResourceNotFoundException;
import io.github.alirostom1.logismart.exception.ZoneNotServicedException;
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

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class DeliveryService {
    private final DeliveryRepo deliveryRepo;
    private final DeliveryMapper deliveryMapper;
    private final SenderRepo senderRepo;
    private final ZonePostalCodeRepo zonePostalCodeRepo;
    private final ZoneService zoneService;
    private final DeliveryHistoryRepo deliveryHistoryRepo;
    private final ProductRepo productRepo;
    private final DeliveryProductRepo deliveryProductRepo;
    private final RecipientService recipientService;
    private final CourierRepo courierRepo;


    //CREATE DELIVERY(FOR SENDER)

    public DeliveryDetailsResponse createDelivery(CreateDeliveryRequest request,Long senderId) {
        //GET SENDER AND RECIPIENT
        Sender sender = senderRepo.findById(senderId)
                .orElseThrow(() -> new ResourceNotFoundException("Sender not found"));
        Recipient recipient = recipientService.findOrCreateRecipient(request.getRecipientData());

        //GET ZONE
        Zone pickupZone = zonePostalCodeRepo.findZoneByPostalCode(request.getPickupPostalCode())
                .orElseThrow(() -> new ZoneNotServicedException("Pickup zone not available"));
        Zone shippingZone = zonePostalCodeRepo.findZoneByPostalCode(request.getShippingPostalCode())
                .orElseThrow(() -> new ZoneNotServicedException("Shipping zone not available"));

        //VALIDATE IF BOTH ZONES ARE ACTIVE
        zoneService.validateDeliveryZones(pickupZone,shippingZone);

        //ATTACH DELIVERY RELATIONSHIPS
        Delivery delivery = deliveryMapper.toEntity(request);
        delivery.setSender(sender);
        delivery.setRecipient(recipient);
        delivery.setPickupZone(pickupZone);
        delivery.setShippingZone(shippingZone);

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
    public DeliveryDetailsResponse getDeliveryById(Long deliveryId){
        Delivery delivery = findById(deliveryId);
        return deliveryMapper.toDetailsResponse(delivery);
    }

    //TRACK SPECIFIC DELIVERY (FOR SENDER AND RECIPIENT)
    @Transactional(readOnly = true)
    public DeliveryTrackingResponse getDeliveryTracking(String trackingNumber) {
        Delivery delivery = deliveryRepo.findByTrackingNumber(trackingNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Delivery not found"));
        return deliveryMapper.toTrackingResponse(delivery);
    }

    //UPDATE DELIVERY STATUS(FOR ALL ACTORS)
    public DeliveryDetailsResponse updateDeliveryStatus(Long deliveryId,
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
    public DeliveryDetailsResponse assignCollectingCourier(Long deliveryId,
                                                   AssignDeliveryRequest request){
        Delivery delivery = findById(deliveryId);
        Courier courier = courierRepo.findById(request.getCourierId())
                .orElseThrow(() -> new ResourceNotFoundException("Courier not found"));
        if(delivery.getPickupZone().getId().equals(courier.getZone().getId())){
            throw new DeliveryCourierZoneMismatchException("Collecting courier isn't available for this specific pickup zone!");
        }
        delivery.setCollectingCourier(courier);
        Delivery savedDelivery = deliveryRepo.save(delivery);
        saveToHistory(savedDelivery,delivery.getStatus(),"Collection Assigned to courier " +
                courier.getFirstName() + " " + courier.getLastName());
        return deliveryMapper.toDetailsResponse(savedDelivery);
    }
    //ASSIGN COURIER TO A SHIP DELIVERY AFTER CREATION (FOR MANAGER)
    public DeliveryDetailsResponse assignShippingCourier(Long deliveryId,
                                                           AssignDeliveryRequest request){
        Delivery delivery = findById(deliveryId);
        Courier courier = courierRepo.findById(request.getCourierId())
                .orElseThrow(() -> new ResourceNotFoundException("Courier not found"));
        if(delivery.getPickupZone().getId().equals(courier.getZone().getId())){
            throw new DeliveryCourierZoneMismatchException("Shipping courier isn't available for this specific shipping zone!");
        }
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
    public Page<DeliveryResponse> getDeliveriesBySender(Long senderId, Pageable pageable){
        Page<Delivery> deliveries = deliveryRepo.findBySenderId(senderId,pageable);
        return deliveries.map(deliveryMapper::toResponse);
    }
    //GET RECIPIENT'S DELIVERIES(FOR RECIPIENT)
    @Transactional(readOnly = true)
    public Page<DeliveryResponse> getDeliveriesByRecipient(String phone, Pageable pageable){
        Page<Delivery> deliveries = deliveryRepo.findByRecipientPhone(phone,pageable);
        return deliveries.map(deliveryMapper::toResponse);
    }
    //GET COURIER'S DELIVERIES(FOR COURIER)
    @Transactional(readOnly = true)
    public Page<DeliveryResponse> getDeliveriesByCourier(Long courierId, Pageable pageable){
        Page<Delivery> deliveries = deliveryRepo.findByCollectingCourierIdOrShippingCourierId(courierId,courierId,pageable);
        return deliveries.map(deliveryMapper::toResponse);
    }

    //DELETE DELIVERY (FOR MANAGER)
    public void deleteDelivery(Long deliveryId){
        Delivery delivery = findById(deliveryId);
        if(delivery.getStatus() != DeliveryStatus.DELIVERED && delivery.getStatus() != DeliveryStatus.CREATED){
            throw new RuntimeException("Couldn't delete a undelievered delivery!");
        }
        deliveryRepo.deleteById(deliveryId);
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
    private Delivery findById(Long deliveryId){
        return deliveryRepo.findById(deliveryId)
                .orElseThrow(() -> new ResourceNotFoundException("Delivery not found"));
    }
    // BUILD SEARCH AND FILTER SPECIFICATION
    private Specification<Delivery> buildSearchSpecification(SearchDeliveryRequest request){
        return Specification.allOf(
                DeliverySpecification.hasSearchTerm(request.getSearchTerm()),
                DeliverySpecification.hasStatus(request.getStatus()),
                DeliverySpecification.hasPriority(request.getPriority()),
                DeliverySpecification.hasPickupZone(request.getPickupZoneId()),
                DeliverySpecification.hasDeliveryZone(request.getDeliveryZoneId())

        );
    }
    //SAVE LIST OF PRODUCTS TO DELIVERY
    private List<DeliveryProduct> createDeliveryProducts(Delivery delivery,
                                                         List<CreateDeliveryRequest.DeliveryProductRequest> productRequests){
        List<DeliveryProduct> deliveryProducts = new ArrayList<>();
        for(CreateDeliveryRequest.DeliveryProductRequest productRequest : productRequests){
            Product product = productRepo.findById(productRequest.getProductId())
                    .orElseThrow(() -> new ResourceNotFoundException("Product not found"));
            BigDecimal totalPrice = product.getUnitPrice().multiply(BigDecimal.valueOf(productRequest.getQuantity()));
            DeliveryProduct deliveryProduct = new DeliveryProduct();
            deliveryProduct.setPriceAtOrder(totalPrice);
            deliveryProduct.setQuantity(productRequest.getQuantity());
            deliveryProduct.setDelivery(delivery);
            deliveryProduct.setProduct(product);
            deliveryProducts.add(deliveryProduct);
        }
        return deliveryProductRepo.saveAll(deliveryProducts);
    }
}
