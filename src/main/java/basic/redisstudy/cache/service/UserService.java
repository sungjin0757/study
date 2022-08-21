package basic.redisstudy.cache.service;

import basic.redisstudy.cache.domain.User;
import basic.redisstudy.cache.dto.UserDto;

public interface UserService {
    User save(UserDto userDto);
    User findById(Long id);
    User update(UserDto userDto);
    void delete(Long id);
}
