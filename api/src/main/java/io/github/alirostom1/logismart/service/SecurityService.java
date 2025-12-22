package io.github.alirostom1.logismart.service;

import io.github.alirostom1.logismart.model.entity.User;
import io.github.alirostom1.logismart.repository.CourierRepo;
import io.github.alirostom1.logismart.repository.SenderRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SecurityService {
    private final CourierRepo courierRepo;
    private final SenderRepo senderRepo;

    public boolean isSameUser(Long id){
        Long userId = getCurrentId();
        return userId.equals(id);
    }

    public Long getCurrentId(){
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return user.getId();
    }

}
