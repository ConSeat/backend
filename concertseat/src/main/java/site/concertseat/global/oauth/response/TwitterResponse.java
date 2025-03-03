package site.concertseat.global.oauth.response;

import java.util.Map;

public class TwitterResponse implements OAuth2Response {
    private final Map<String, Object> data;

    public TwitterResponse(Map<String, Object> attributes) {
        this.data = (Map<String, Object>) attributes.get("data");
    }

    @Override
    public String getProvider() {
        return "twitter";
    }

    @Override
    public String getSocialId() {
        return data.get("id").toString();
    }

    @Override
    public String getNickname() {
        return data.get("name").toString();
    }
}
