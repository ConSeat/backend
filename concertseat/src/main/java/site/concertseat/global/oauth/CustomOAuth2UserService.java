package site.concertseat.global.oauth;

import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import site.concertseat.domain.member.entity.Member;
import site.concertseat.domain.member.repository.MemberRepository;
import site.concertseat.global.oauth.response.KakaoResponse;
import site.concertseat.global.oauth.response.OAuth2Response;
import site.concertseat.global.oauth.response.TwitterResponse;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {
    private final MemberRepository memberRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        String registration = userRequest.getClientRegistration().getRegistrationId();

        OAuth2Response oAuth2Response = null;

        switch (registration) {
            case "kakao" -> oAuth2Response = new KakaoResponse(oAuth2User.getAttributes());
            case "twitter" -> oAuth2Response = new TwitterResponse(oAuth2User.getAttributes());
        }

        if (oAuth2Response == null) return null;

        String socialId = oAuth2Response.getSocialId();

        Member member = memberRepository.findBySocialId(socialId).orElseGet(oAuth2Response::toEntity);

        memberRepository.save(member);

        return new CustomOAuth2User(member, oAuth2User);
    }
}
