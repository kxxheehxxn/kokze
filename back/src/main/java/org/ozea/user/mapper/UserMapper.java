package org.ozea.user.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.ozea.user.domain.User;

@Mapper
public interface UserMapper {
    User findByEmail(String email);
    int insertUser(User user);
    int updateLastLogin(Long id);
    int updatePasswordHash(Long id, String passwordHash);
}