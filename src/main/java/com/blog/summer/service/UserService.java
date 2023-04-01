package com.blog.summer.service;

import com.blog.summer.domain.User;
import com.blog.summer.dto.UserJoinDto;
import com.blog.summer.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    ModelMapper mapper = new ModelMapper();
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public void createUser(UserJoinDto userJoinDto) {
        User user = mapper.map(userJoinDto, User.class);
        user.setRole("ROLE_USER");
        user.setEncryptedPwd(passwordEncoder.encode(userJoinDto.getPassword()));
        userRepository.save(user);

    }
}
