package cloud.fengdu.ticketing.auth.domain.repository;


import org.apache.ibatis.annotations.Mapper;

import cloud.fengdu.ticketing.auth.domain.entity.User;

@Mapper
public interface UserRepository {
    
    User findByEmailAddress(String emailAddress);
    void insert(User user);
}
