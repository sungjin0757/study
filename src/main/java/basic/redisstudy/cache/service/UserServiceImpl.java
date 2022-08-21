package basic.redisstudy.cache.service;

import basic.redisstudy.cache.config.property.RedisCacheProperty;
import basic.redisstudy.cache.domain.User;
import basic.redisstudy.cache.dto.UserDto;
import basic.redisstudy.cache.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{
    private final UserRepository userRepository;

    @Transactional
    @Override
    public User save(UserDto userDto) {
        User user = new User(userDto.getName(), userDto.getAge());
        userRepository.save(user);
        return user;
    }

    @Cacheable(value = RedisCacheProperty.USER, key = "#id", unless = "#result == null")
    @Override
    public User findById(Long id) {
        return findUserById(id);
    }

    @Transactional
    @CachePut(value = RedisCacheProperty.USER, key = "#userDto.id", unless = "#result == null")
    @Override
    public User update(UserDto userDto) {
        User findUser = findUserById(userDto.getId());
        findUser.updateToDto(userDto);
        return findUser;
    }

    @Transactional
    @CacheEvict(value = RedisCacheProperty.USER, key = "#id")
    @Override
    public void delete(Long id) {
        try {
            userRepository.deleteById(id);
        } catch (Exception e) {
            throw new IllegalArgumentException("Not Found Id!");
        }
    }

    private User findUserById(Long id) {
        User findUser = userRepository.findById(id).orElseThrow(() -> {
            throw new IllegalArgumentException("Not Found Id!");
        });
        return findUser;
    }
}
