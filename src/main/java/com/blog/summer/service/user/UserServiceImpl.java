package com.blog.summer.service.user;

import com.blog.summer.config.JwtProvider;
import com.blog.summer.domain.Token;
import com.blog.summer.domain.UserEntity;
import com.blog.summer.dto.TokenDto;
import com.blog.summer.dto.user.UserDto;
import com.blog.summer.exception.NotFoundException;
import com.blog.summer.repository.TokenRepository;
import com.blog.summer.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity userEntity = userRepository.findByEmail(username)
                .orElseThrow(()->new UsernameNotFoundException(username));

        return new User(userEntity.getEmail(), userEntity.getEncryptedPwd(),
                true, true, true, true,
                new ArrayList<>());
    }
    @Override
    public UserDto createUser(UserDto userDto) {
        userDto.setUserId(UUID.randomUUID().toString());
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        UserEntity userEntity = UserEntity.builder()
                .email(userDto.getEmail())
                .name(userDto.getName())
                .userId(userDto.getUserId())
                .build();
        userEntity.setEncryptedPwd(passwordEncoder.encode(userDto.getPwd()));
        userRepository.save(userEntity);

        UserDto returnUserDto = mapper.map(userEntity, UserDto.class);
        return returnUserDto;
    }

    @Override
    public UserDto getUserByUserId(String userId) {
        UserEntity userEntity = userRepository.findByUserId(userId)
                .orElseThrow(() -> new NotFoundException("사용자를 찾을 수 없습니다"));;

        if(userEntity==null)
            throw new UsernameNotFoundException("User not found");

        UserDto userDto = new ModelMapper().map(userEntity, UserDto.class);
        return userDto;
    }

    @Override
    public Iterable<UserEntity> getUserByAll() {
        return userRepository.findAll();
    }

    @Override
    public UserDto getUserDetailsByEmail(String email) {
        UserEntity userEntity = userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("사용자를 찾을 수 없습니다"));;
        if(userEntity ==null)
            throw new UsernameNotFoundException(email);

        UserDto userDto = new ModelMapper().map(userEntity, UserDto.class);
        return userDto;
    }

    @Override
    // Refresh Token을 발급하는 메서드
    public void generateRefreshToken(String userId) {
        //Token이 이미 존재하면 기존 refresh_token발행, 아니면 토큰생성.
        tokenRepository.findById(userId).ifPresentOrElse(
                (token) ->{
                    // 리프레시 토큰 만료일자가 얼마 남지 않았을 때 만료시간 연장
                    if(token.getExpiration() < 10) {
                        token.setExpiration(1000);
                        tokenRepository.save(token);
                    }
                },
                ()->{
                    // Refresh Token 생성 로직을 구현합니다.
                    Token token = tokenRepository.save(
                            Token.builder()
                                    .id(userId)
                                    .refresh_token(UUID.randomUUID().toString())
                                    .expiration(300)
                                    .build()
                    );
                    UserEntity user = userRepository.findByUserId(userId)
                            .orElseThrow(() -> new NotFoundException("사용자를 찾을 수 없습니다."));
                    user.setRefreshToken(token.getRefresh_token());
                }
        );
    }

    @Override
    public Token validRefreshToken(String userId, String refreshToken)  {
        Token token = tokenRepository.findById(userId).orElseThrow(() -> new NotFoundException("만료된 계정입니다. 로그인을 다시 시도하세요"));
        // 해당유저의 Refresh 토큰 만료 : Redis에 해당 유저의 토큰이 존재하지 않음
        if (token.getRefresh_token() == null) {
            return null;
        } else {
            // 리프레시 토큰 만료일자가 얼마 남지 않았을 때 만료시간 연장
            if(token.getExpiration() < 10) {
                token.setExpiration(1000);
                tokenRepository.save(token);
            }
            // 토큰이 같은지 비교
            if(!token.getRefresh_token().equals(refreshToken)) {
                return null;
            } else {
                return token;
            }
        }
    }

    @Override
    public void setRefreshToken(String userId, String refreshToken) {
        UserEntity user = userRepository.findByUserId(userId).orElseThrow(() -> new NotFoundException("사용자를 찾지못했습니다."));
        user.setRefreshToken(refreshToken);
    }

    public TokenDto refreshAccessToken(TokenDto token) {
        String userId = jwtProvider.getAccount(token.getAccess_token());

        Token refreshToken = validRefreshToken(userId, token.getRefresh_token());
        if (refreshToken != null) {
            return TokenDto.builder()
                    .access_token(jwtProvider.createToken(userId))
                    .refresh_token(refreshToken.getRefresh_token())
                    .build();
        } else {
            throw new NotFoundException("로그인을 해주세요");
        }
    }



}
