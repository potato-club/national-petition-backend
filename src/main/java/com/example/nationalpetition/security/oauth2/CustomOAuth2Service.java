package com.example.nationalpetition.security.oauth2;

import com.example.nationalpetition.domain.member.repository.MemberRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Slf4j
@Service
public class CustomOAuth2Service implements OAuth2UserService {

    private final MemberRepository memberRepository;

    public CustomOAuth2Service(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        final OAuth2UserService<OAuth2UserRequest, OAuth2User> oAuth2UserService = new DefaultOAuth2UserService();
        final OAuth2User oAuth2User = oAuth2UserService.loadUser(userRequest);
        final String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();

        final OAuthAttributes authAttributes = OAuthAttributes.of(userNameAttributeName, oAuth2User.getAttributes());

        saveOrUpdate(authAttributes);

        return new DefaultOAuth2User(null, authAttributes.getAttributes(), authAttributes.getNameAttributeKey());
    }

    private void saveOrUpdate(OAuthAttributes authAttributes) {
        memberRepository.save(memberRepository.findByEmail(authAttributes.getEmail())
                .map(u -> u.update(authAttributes.getName(), authAttributes.getPicture()))
                .orElse(authAttributes.toEntity()));
    }

}
