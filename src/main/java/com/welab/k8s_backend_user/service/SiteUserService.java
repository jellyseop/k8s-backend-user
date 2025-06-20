package com.welab.k8s_backend_user.service;

import com.welab.k8s_backend_user.common.exception.BadParameter;
import com.welab.k8s_backend_user.common.exception.NotFound;
import com.welab.k8s_backend_user.domain.SiteUser;
import com.welab.k8s_backend_user.domain.dto.SiteUserLoginDto;
import com.welab.k8s_backend_user.domain.dto.SiteUserRegisterDto;
import com.welab.k8s_backend_user.domain.event.SiteUserInfoEvent;
import com.welab.k8s_backend_user.domain.repository.SiteUserRepository;
import com.welab.k8s_backend_user.event.producer.KafkaEventProducer;
import com.welab.k8s_backend_user.secret.hash.SecureHashUtils;
import com.welab.k8s_backend_user.secret.jwt.TokenGenerator;
import com.welab.k8s_backend_user.secret.jwt.dto.TokenDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class SiteUserService {
    private final SiteUserRepository siteUserRepository;
    private final TokenGenerator tokenGenerator;
    private final KafkaEventProducer kafkaEventProducer;

    @Transactional
    public void save(SiteUserRegisterDto registerDto) {
        SiteUser siteUser = registerDto.toEntity();

        siteUserRepository.save(siteUser);
        SiteUserInfoEvent event = SiteUserInfoEvent.fromEntity("Create", siteUser);

        kafkaEventProducer.send(SiteUserInfoEvent.Topic, event);
    }

    public TokenDto.AccessRefreshToken login(SiteUserLoginDto loginDto) {
        SiteUser user = siteUserRepository.findByUserId(loginDto.getUserId()).
                orElseThrow(() -> new NotFound("아이디 또는 비밀번호를 확인해주세요"));

        if (!SecureHashUtils.matches(user.getPassword(), loginDto.getPassword())) {
            throw new BadParameter("아이디 또는 비밀번호를 확인해주세요");
        }

        return tokenGenerator.generateAccessRefreshToken(user.getUserId(), "WEB");
    }
}
