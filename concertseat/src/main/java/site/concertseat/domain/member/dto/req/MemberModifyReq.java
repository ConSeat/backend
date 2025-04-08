package site.concertseat.domain.member.dto.req;

import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.springframework.web.multipart.MultipartFile;

@Data
public class MemberModifyReq {
    @Length(max = 20)
    private String nickname;

    private MultipartFile file;
}
