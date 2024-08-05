package com.example.authentication.service.impl;

import com.example.authentication.Exception.BadRequestException;
import com.example.authentication.Exception.NotFoundException;
import com.example.authentication.dto.UserDto;
import com.example.authentication.entity.User;
import com.example.authentication.repository.UserRepository;
import com.example.authentication.service.AuthService;
import com.example.authentication.service.UserService;
import com.example.authentication.component.JwtComponent;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

import static com.example.authentication.repository.UserRepository.FIND_USER;
import static com.example.authentication.repository.UserRepository.PROPERTY_NAME;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    AuthService authService;

    @Autowired
    ObjectMapper mapper;

    @Autowired
    JwtComponent jwtComponent;

    private Query convertQueryToSql(final String sql, final String property, final String value) throws Exception {
        return switch (property.toLowerCase()) {
            case "id" -> entityManager.createNativeQuery(sql, User.class).setParameter("value", Long.parseLong(value));
            case "name", "email", "contact" ->
                    entityManager.createNativeQuery(sql, User.class).setParameter("value", value);
            case "gender" ->
                    entityManager.createNativeQuery(sql, User.class).setParameter("value", Boolean.getBoolean(value));
            case "age" ->
                    entityManager.createNativeQuery(sql, User.class).setParameter("value", Integer.parseInt(value));
            default -> throw new Exception("Invalid property");
        };
    }

    @Override
    public List<User> getUsersByPropertyName(String property, String value) throws Exception {
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        final String sql = FIND_USER.replace(PROPERTY_NAME, property);
        Query query = convertQueryToSql(sql, property, value);
        final List<User> users = mapper.convertValue(query.getResultList(), new TypeReference<>() {
        });
        entityManager.close();
        return users;
    }

    @Override
    public User createUser(final UserDto userDto) {
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        userDto.setPassword(this.authService.hashPassword(userDto.getPassword()));
        final User user = mapper.convertValue(userDto, User.class);
        return this.userRepository.save(user);
    }

    @Override
    public String login(String email, String password, HttpServletResponse response) throws Exception {
        Optional<User> user = this.getUsersByPropertyName("email", email).stream().findFirst();
        if (user.isEmpty() || Boolean.TRUE.equals(user.get().getDeleted())) {
            throw new NotFoundException("email not found");
        }
        final AtomicReference<String> jwt = new AtomicReference<>();

        if (this.authService.checkPassword(password, user.get().getPassword())) {
            response.addCookie(jwtComponent.getJwtCookie(String.valueOf(user.get().getId())));
        } else {
            throw new BadRequestException("Invalid password");
        }
        return jwt.get();
    }
    private User toUserDto(final UserDto source, final User target) {
        target.setAge(source.getAge());
        if(Objects.nonNull(source.getGender())){
            target.setGender(source.getGender());
        }
        if(Objects.nonNull(source.getEmail())){
            target.setEmail(source.getEmail());
        }
        if(Objects.nonNull(source.getName())){
            target.setName(source.getName());
        }
        if(Objects.nonNull(source.getContact())){
            target.setContact(source.getContact());
        }
        if(Objects.nonNull(source.getPassword())){
            target.setPassword(this.authService.hashPassword(source.getPassword()));
        }
        return target;
    }
    @Override
    public User updateUser(final Long id,final UserDto userDto) {
        if(Objects.isNull(id)){
            throw new NotFoundException("User to update not found");
        }
        if(Objects.isNull(userDto)){
            throw new NotFoundException("User payload to update not found");
        }
        Optional<User> userOptional = this.userRepository.findById(id);
        final AtomicReference<User> userAtomicReference = new AtomicReference<>(null);
        userOptional.ifPresent(user -> {
            if(Boolean.FALSE.equals(user.getDeleted())){
                userAtomicReference.set(this.userRepository.save(this.toUserDto(userDto,user)));
            }
        });
        if(Objects.isNull(userAtomicReference.get())){
            throw new NotFoundException("User to update not found");
        }
        return userAtomicReference.get();
    }

    public Boolean deleteUser(final Long id) {
        Optional<User> userOptional = this.userRepository.findById(id);
        if(userOptional.isEmpty()){
            return false;
        }
        User user = userOptional.get();
        user.setDeleted(Boolean.TRUE);
        this.userRepository.save(user);
        return true;
    }

}
