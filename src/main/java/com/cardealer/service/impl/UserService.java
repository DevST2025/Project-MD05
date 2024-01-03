package com.cardealer.service.impl;

import com.cardealer.exception.NotFoundException;
import com.cardealer.exception.PasswordNotMatchException;
import com.cardealer.model.common.ERole;
import com.cardealer.model.dto.request.PasswordRequest;
import com.cardealer.model.dto.request.RegisterRequest;
import com.cardealer.model.dto.request.UserRequest;
import com.cardealer.model.entity.Role;
import com.cardealer.model.entity.ShopingCart;
import com.cardealer.model.entity.User;
import com.cardealer.repository.ICartRepository;
import com.cardealer.repository.IRoleRepository;
import com.cardealer.repository.IUserRepository;
import com.cardealer.service.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService {
    private final IUserRepository userRepository;
    private final IRoleRepository roleRepository;
    private final ICartRepository cartRepository;
    private final UploadService uploadService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public User getLoginUser() throws NotFoundException {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userRepository.findByUsername(userDetails.getUsername()).orElseThrow(() -> new NotFoundException("Không tìm thấy user có username: " + userDetails.getUsername()));
    }
    @Override
    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    @Override
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public boolean existsByPhone(String phone) {
        return userRepository.existsByPhone(phone);
    }

    @Override
    public User findByUsername(String username) throws NotFoundException {
        return userRepository.findByUsername(username).orElseThrow(() -> new NotFoundException("User with username \" " + username + " \" not found." ));
    }

    @Override
    public User findByEmail(String email) throws NotFoundException {
        return userRepository.findByEmail(email).orElseThrow(() -> new NotFoundException("User with email \" " + email + " \" not found." ));
    }

    @Override
    public User findByPhone(String phone) throws NotFoundException {
        return userRepository.findByPhone(phone).orElseThrow(() -> new NotFoundException("User with phone \" " + phone + " \" not found." ));
    }

    @Override
    public User loadByUsername(String username) throws NotFoundException {
        return userRepository.loadByUsername(username).orElseThrow(() -> new NotFoundException("User with username \" " + username + " \" not found." ));
    }

    @Override
    public void register(RegisterRequest request) {
        String avatarUrl = "https://iphonecugiare.com/wp-content/uploads/2020/03/84156601_1148106832202066_479016465572298752_o.jpg";
        Set<Role> roleSet = new HashSet<>();
        if (request.getListRoles() == null || request.getListRoles().isEmpty()) {
            roleSet.add(roleRepository.findByRoleName(ERole.ROLE_USER).orElseThrow(() -> new NoSuchElementException("Resource not found.")));
        } else {
            request.getListRoles().forEach(role -> {
                switch (role) {
                    case "admin":
                        roleSet.add(roleRepository.findByRoleName(ERole.ROLE_ADMIN).orElseThrow(() -> new NoSuchElementException("Resource not found.")));
                    case "user":
                    default:
                        roleSet.add(roleRepository.findByRoleName(ERole.ROLE_USER).orElseThrow(() -> new NoSuchElementException("Resource not found.")));
                }
            });
        }
        User user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .fullName(request.getFullName())
                .password(passwordEncoder.encode(request.getPassword()))
                .avatar(avatarUrl)
                .phone(request.getPhone())
                .address(request.getAddress())
                .birthday(request.getBirthday())
                .createdAt(request.getCreatedAt())
                .updatedAt(null)
                .status(true)
                .roles(roleSet)
                .build();
        userRepository.save(user);
    }

    @Override
    public Page<User> findAll(Pageable pageable) {
        pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), pageable.getSort());
        return userRepository.findAll(pageable);
    }

    @Override
    public User upgradeAccount(String id) throws NotFoundException {
        User user = userRepository.findById(id).orElseThrow(() -> new NotFoundException("Không tìm thấy người dùng có ID: " + id));
        user.getRoles().add(roleRepository.findByRoleName(ERole.ROLE_ADMIN).orElseThrow(() -> new NoSuchElementException("Resource not found.")));
        userRepository.save(user);
        return user;
    }

    @Override
    public User downgradeAccount(String id) throws NotFoundException {
        User user = userRepository.findById(id).orElseThrow(() -> new NotFoundException("Không tìm thấy người dùng có ID: " + id));
        user.getRoles().remove(roleRepository.findByRoleName(ERole.ROLE_ADMIN));
        userRepository.save(user);
        return user;
    }

    @Override
    public User receptorAccount(String id) throws NotFoundException {
        User user = userRepository.findById(id).orElseThrow(() -> new NotFoundException("Không tìm thấy người dùng có ID: " + id));
        user.setStatus(!user.getStatus());
        userRepository.save(user);
        return user;
    }

    @Override
    public Page<User> findUserByName(String name, Pageable pageable) {
        pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), pageable.getSort());
        return userRepository.findByFullNameContaining(name, pageable);
    }

    @Override
    public List<ShopingCart> findAllCartItem() throws NotFoundException {
        User user = getLoginUser();
        return cartRepository.findByUser(user);
    }

    @Override
    public User showProfile() throws NotFoundException {
        return getLoginUser();
    }

    @Override
    public User editProfile(UserRequest userRequest) throws NotFoundException {
        User userLogin = getLoginUser();
        String avaUrl = userLogin.getAvatar();
        if (!userRequest.getFile().isEmpty() || userRequest.getFile() != null) {
            avaUrl = uploadService.uploadFile(userRequest.getFile());
        }
        User user = User.builder()
                .id(userLogin.getId())
                .username(userLogin.getUsername())
                .email(userRequest.getEmail())
                .fullName(userRequest.getFullName())
                .password(userLogin.getPassword())
                .avatar(avaUrl)
                .phone(userRequest.getPhone())
                .address(userRequest.getAddress())
                .birthday(userRequest.getBirthday())
                .createdAt(userLogin.getCreatedAt())
                .updatedAt(new Date(System.currentTimeMillis()))
                .status(userLogin.getStatus())
                .roles(userLogin.getRoles())
                .build();
        userRepository.save(user);
        return user;
    }

    @Override
    public void changePassword(PasswordRequest passwordRequest) throws NotFoundException, PasswordNotMatchException {
        User userLogin = getLoginUser();
        if (!passwordEncoder.matches(passwordRequest.getOldPass(), userLogin.getPassword())) {
            throw new PasswordNotMatchException("Mật khẩu không trùng khớp");
        }
        userLogin.setPassword(passwordEncoder.encode(passwordRequest.getNewPass()));
        userRepository.save(userLogin);
    }


}


