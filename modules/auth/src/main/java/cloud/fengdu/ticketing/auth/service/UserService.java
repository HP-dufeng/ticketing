package cloud.fengdu.ticketing.auth.service;

import cloud.fengdu.ticketing.auth.domain.entity.User;

public interface UserService {
    
    User getUser(String emailAddress);

    void saveUser(User user);
}
