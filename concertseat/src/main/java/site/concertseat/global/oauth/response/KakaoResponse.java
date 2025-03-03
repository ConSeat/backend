package site.concertseat.global.oauth.response;

import java.util.Map;

public class KakaoResponse implements OAuth2Response {
    private final Map<String, Object> properties;
    private final Map<String, Object> account;

    public KakaoResponse(Map<String, Object> attributes) {
        this.properties = (Map<String, Object>) attributes.get("properties");
        this.account = (Map<String, Object>) attributes.get("kakao_account");
    }

    @Override
    public String getProvider() {
        return "kakao";
    }

    @Override
    public String getSocialId() {
        return account.get("email").toString();
    }

    @Override
    public String getNickname() {
        return properties.get("nickname").toString();
    }
}
