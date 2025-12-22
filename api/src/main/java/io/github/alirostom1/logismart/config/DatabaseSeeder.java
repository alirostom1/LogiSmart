package io.github.alirostom1.logismart.config;

import io.github.alirostom1.logismart.model.entity.*;
import io.github.alirostom1.logismart.model.enums.EPermission;
import io.github.alirostom1.logismart.model.enums.ERole;
import io.github.alirostom1.logismart.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Configuration
@RequiredArgsConstructor
public class DatabaseSeeder {
    private final RoleRepository roleRepository;
    private final PermissionRepo permissionRepo;
    private final UserRepo userRepo;
    private final CourierRepo courierRepo;
    private final SenderRepo senderRepo;
    private final ZoneRepo zoneRepo;
    private final ZonePostalCodeRepo zonePostalCodeRepo;
    private final PasswordEncoder passwordEncoder;

    @Bean
    @Order(1)
    public CommandLineRunner seedDatabase(){
        return args -> {
            seedPermissions();
            seedRoles();
            assignPermissionsToRoles();
            seedZones();
            seedTestUsers();
        };
    }

    @Transactional
    protected void seedPermissions(){
        List<Permission> permissionsToSave = new ArrayList<>();
        for (EPermission permissionEnum : EPermission.values()) {
            if (!permissionRepo.existsByName(permissionEnum)) {
                Permission permission = Permission.builder()
                        .name(permissionEnum)
                        .build();
                permissionsToSave.add(permission);
            }
        }
        permissionRepo.saveAll(permissionsToSave);
    }
    @Transactional
    protected void seedRoles(){
        List<Role> rolesToSave = new ArrayList<>();
        for (ERole roleEnum : ERole.values()) {
            if (!roleRepository.existsByName(roleEnum)) {
                Role role = Role.builder()
                        .name(roleEnum)
                        .build();
                rolesToSave.add(role);
            }
        }
        roleRepository.saveAll(rolesToSave);
    }
    @Transactional
    protected void assignPermissionsToRoles(){
        Map<EPermission, Permission> permissionMap = permissionRepo.findAll()
                .stream()
                .collect(Collectors.toMap(Permission::getName, p -> p));

        Map<ERole, Role> roleMap = roleRepository.findAll()
                .stream()
                .collect(Collectors.toMap(Role::getName, r -> r));

        Role managerRole = roleMap.get(ERole.ROLE_MANAGER);
        if (managerRole != null) {
            Set<Permission> managerPermissions = permissionMap.values()
                    .stream()
                    .filter(permission -> !permission.getName().name().startsWith("PERMISSION"))
                    .collect(Collectors.toSet());
            if (!managerRole.getPermissions().containsAll(managerPermissions)) {
                managerRole.getPermissions().addAll(managerPermissions);
                roleRepository.save(managerRole);
            }
        }

        Role courierRole = roleMap.get(ERole.ROLE_COURIER);
        if (courierRole != null) {
            Set<Permission> courierPermissions = getCourierPermissions(permissionMap);
            if (!courierRole.getPermissions().containsAll(courierPermissions)) {
                courierRole.getPermissions().clear();
                courierRole.getPermissions().addAll(courierPermissions);
                roleRepository.save(courierRole);}
        }
        Role senderRole = roleMap.get(ERole.ROLE_SENDER);
        if (senderRole != null) {
            Set<Permission> senderPermissions = getSenderPermissions(permissionMap);
            if (!senderRole.getPermissions().containsAll(senderPermissions)) {
                senderRole.getPermissions().clear();
                senderRole.getPermissions().addAll(senderPermissions);
                roleRepository.save(senderRole);
            }
        }
        Role adminRole = roleMap.get(ERole.ROLE_ADMIN);
        if(adminRole != null){
            Set<Permission> adminPermissions = permissionMap.values()
                    .stream()
                    .filter(p -> p.getName().name().startsWith("PERMISSION"))
                    .collect(Collectors.toSet());
            // Admin also gets all permissions
            adminPermissions.addAll(permissionMap.values());
            if (!adminRole.getPermissions().containsAll(adminPermissions)) {
                adminRole.getPermissions().clear();
                adminRole.getPermissions().addAll(adminPermissions);
                roleRepository.save(adminRole);
            }
        }
    }

    private Set<Permission> getCourierPermissions(Map<EPermission,Permission> permissionMap){
        return Stream.of(
                        EPermission.COURIER_READ_OWN,
                        EPermission.DELIVERY_READ_OWN,
                        EPermission.DELIVERY_UPDATE_STATUS
                )
                .map(permissionMap::get)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
    }
    private Set<Permission> getSenderPermissions(Map<EPermission,Permission> permissionMap){
        return Stream.of(
                        EPermission.DELIVERY_SAVE,
                        EPermission.DELIVERY_READ_OWN,
                        EPermission.SENDER_READ_OWN,
                        EPermission.SENDER_SAVE_OWN,
                        EPermission.PRODUCT_SAVE,
                        EPermission.PRODUCT_READ_OWN
                )
                .map(permissionMap::get)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
    }

    @Transactional
    protected void seedZones() {
        if (zoneRepo.count() > 0) {
            return; // Zones already exist
        }

        Zone zone1 = Zone.builder()
                .name("Zone Nord")
                .code("ZN")
                .active(true)
                .build();
        zone1 = zoneRepo.save(zone1);

        Zone zone2 = Zone.builder()
                .name("Zone Sud")
                .code("ZS")
                .active(true)
                .build();
        zone2 = zoneRepo.save(zone2);

        Zone zone3 = Zone.builder()
                .name("Zone Centre")
                .code("ZC")
                .active(true)
                .build();
        zone3 = zoneRepo.save(zone3);

        // Add postal codes to zones
        List<String> postalCodes1 = Arrays.asList("75001", "75002", "75003", "75004", "75005");
        List<String> postalCodes2 = Arrays.asList("75014", "75015", "75016", "75017", "75018");
        List<String> postalCodes3 = Arrays.asList("75006", "75007", "75008", "75009", "75010");

        for (String code : postalCodes1) {
            ZonePostalCode zpc = new ZonePostalCode();
            zpc.setZone(zone1);
            zpc.setPostalCode(code);
            zonePostalCodeRepo.save(zpc);
        }

        for (String code : postalCodes2) {
            ZonePostalCode zpc = new ZonePostalCode();
            zpc.setZone(zone2);
            zpc.setPostalCode(code);
            zonePostalCodeRepo.save(zpc);
        }

        for (String code : postalCodes3) {
            ZonePostalCode zpc = new ZonePostalCode();
            zpc.setZone(zone3);
            zpc.setPostalCode(code);
            zonePostalCodeRepo.save(zpc);
        }
    }

    @Transactional
    protected void seedTestUsers() {
        if (userRepo.count() > 0) {
            return; // Users already exist
        }

        Map<ERole, Role> roleMap = roleRepository.findAll()
                .stream()
                .collect(Collectors.toMap(Role::getName, r -> r));

        List<Zone> zones = zoneRepo.findAll();
        if (zones.isEmpty()) {
            return; // Zones must be created first
        }

        // Create Admin
        Role adminRole = roleMap.get(ERole.ROLE_ADMIN);
        if (adminRole != null && !userRepo.existsByEmail("admin@logismart.com")) {
            Manager admin = Manager.builder()
                    .firstName("Admin")
                    .lastName("System")
                    .email("admin@logismart.com")
                    .password(passwordEncoder.encode("admin123"))
                    .phone("+33123456789")
                    .role(adminRole)
                    .build();
            userRepo.save(admin);
        }

        // Create Manager
        Role managerRole = roleMap.get(ERole.ROLE_MANAGER);
        if (managerRole != null && !userRepo.existsByEmail("manager@logismart.com")) {
            Manager manager = Manager.builder()
                    .firstName("Jean")
                    .lastName("Dupont")
                    .email("manager@logismart.com")
                    .password(passwordEncoder.encode("manager123"))
                    .phone("+33123456790")
                    .role(managerRole)
                    .build();
            userRepo.save(manager);
        }

        // Create Couriers
        Role courierRole = roleMap.get(ERole.ROLE_COURIER);
        if (courierRole != null) {
            if (!userRepo.existsByEmail("courier1@logismart.com")) {
                Courier courier1 = Courier.builder()
                        .firstName("Pierre")
                        .lastName("Martin")
                        .email("courier1@logismart.com")
                        .password(passwordEncoder.encode("courier123"))
                        .phone("+33123456791")
                        .vehicle("Vélo électrique")
                        .role(courierRole)
                        .zone(zones.get(0))
                        .build();
                courierRepo.save(courier1);
            }

            if (!userRepo.existsByEmail("courier2@logismart.com") && zones.size() > 1) {
                Courier courier2 = Courier.builder()
                        .firstName("Marie")
                        .lastName("Bernard")
                        .email("courier2@logismart.com")
                        .password(passwordEncoder.encode("courier123"))
                        .phone("+33123456792")
                        .vehicle("Scooter")
                        .role(courierRole)
                        .zone(zones.get(1))
                        .build();
                courierRepo.save(courier2);
            }
        }

        // Create Senders
        Role senderRole = roleMap.get(ERole.ROLE_SENDER);
        if (senderRole != null) {
            if (!userRepo.existsByEmail("sender1@logismart.com")) {
                Sender sender1 = Sender.builder()
                        .firstName("Sophie")
                        .lastName("Lefebvre")
                        .email("sender1@logismart.com")
                        .password(passwordEncoder.encode("sender123"))
                        .phone("+33123456793")
                        .role(senderRole)
                        .build();
                senderRepo.save(sender1);
            }

            if (!userRepo.existsByEmail("sender2@logismart.com")) {
                Sender sender2 = Sender.builder()
                        .firstName("Thomas")
                        .lastName("Moreau")
                        .email("sender2@logismart.com")
                        .password(passwordEncoder.encode("sender123"))
                        .phone("+33123456794")
                        .role(senderRole)
                        .build();
                senderRepo.save(sender2);
            }
        }
    }

}
