package io.github.alirostom1.logismart.model.entity;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "couriers")
public class Courier{
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "last_name",nullable = false)
    private String lastName;

    @Column(name = "first_name",nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String vehicle;

    @Column(name = "phone_number",nullable = false,unique = true)
    private String phoneNumber;

    @OneToMany(mappedBy = "collectingCourier", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Delivery> collectingDeliveries = new ArrayList<>();

    @OneToMany(mappedBy = "shippingCourier", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Delivery> shippingDeliveries = new ArrayList<>();
    

    @ManyToOne
    @JoinColumn(name = "zone_id",nullable = false)
    private Zone zone;

    @CreationTimestamp
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public Courier(String lastName, String firstName, String vehicle, String phoneNumber) {
        this.lastName = lastName;
        this.firstName = firstName;
        this.vehicle = vehicle;
        this.phoneNumber = phoneNumber;
    }
}
