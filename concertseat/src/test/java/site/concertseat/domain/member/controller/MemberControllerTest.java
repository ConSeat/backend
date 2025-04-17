package site.concertseat.domain.member.controller;

import com.epages.restdocs.apispec.ResourceSnippetParameters;
import com.epages.restdocs.apispec.Schema;
import jakarta.annotation.PostConstruct;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;
import site.concertseat.domain.member.enums.Role;
import site.concertseat.global.jwt.service.JwtUtils;


import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document;
import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatTypes.*;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.multipart;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static site.concertseat.global.statuscode.SuccessCode.OK;
import static site.concertseat.utils.ResponseFieldUtils.getCommonResponseFields;

@Transactional
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
@ExtendWith(RestDocumentationExtension.class)
public class MemberControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JwtUtils jwtUtils;

    private static final String UUID = "c2141f87-30d7-4d2e-a9ef-3eff99acd957";

    private String jwtToken;

    @PostConstruct
    public void init() {
        jwtToken = jwtUtils.createAccessToken(UUID, Role.ROLE_USER);
    }

    @Test
    public void 마이페이지_정보_조회_성공() throws Exception {
        // given

        // when
        ResultActions actions = mockMvc.perform(
                get("/api/members")
                        .header("Authorization", "Bearer " + jwtToken)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
        );

        // then
        actions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.header.message").value(OK.getMessage()))
                .andDo(document(
                        "마이페이지 정보 조회 성공",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        resource(ResourceSnippetParameters.builder()
                                .tag("Member API")
                                .summary("마이페이지 정보 조회 API")
                                .requestHeaders(
                                        headerWithName("Authorization")
                                                .description("JWT 토큰")
                                )
                                .responseFields(
                                        getCommonResponseFields(
                                                fieldWithPath("body.nickname").type(STRING)
                                                        .description("멤버 닉네임"),
                                                fieldWithPath("body.profileImage").type(STRING)
                                                        .description("멤버 프로필 이미지 url"),
                                                fieldWithPath("body.email").type(STRING)
                                                        .description("멤버 이메일"),
                                                fieldWithPath("body.favoriteCount").type(NUMBER)
                                                        .description("관심 시야 개수"),
                                                fieldWithPath("body.myReviewCount").type(NUMBER)
                                                        .description("내 후기 개수")
                                        )
                                )
                                .requestSchema(Schema.schema("마이페이지 정보 조회 Request"))
                                .responseSchema(Schema.schema("마이페이지 정보 조회 Response"))
                                .build()
                        ))
                );
    }

    @Test
    public void 프로필_수정_성공() throws Exception {
        // given
        MockMultipartFile file1 = new MockMultipartFile("file", "sample1.jpg", "image/jpeg", "image/sample1.jpg".getBytes());
        String nickname = "닉네임1";

        // when
        ResultActions actions = mockMvc.perform(
                multipart("/api/members")
                        .file(file1)
                        .param("nickname", nickname)
                        .header("Authorization", "Bearer " + jwtToken)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .characterEncoding("UTF-8")
                        .with(csrf())
        );

        // then
        actions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.header.message").value(OK.getMessage()))
                .andDo(document(
                        "프로필 수정 성공",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        resource(ResourceSnippetParameters.builder()
                                .tag("Member API")
                                .summary("프로필 수정 API")
                                .requestHeaders(
                                        headerWithName("Authorization")
                                                .description("JWT 토큰")
                                )
                                .responseFields(
                                        getCommonResponseFields(
                                                fieldWithPath("body.nickname").type(STRING)
                                                        .description("멤버 닉네임"),
                                                fieldWithPath("body.profileImage").type(STRING)
                                                        .description("멤버 프로필 이미지 url")
                                        )
                                )
                                .requestSchema(Schema.schema("프로필 수정 Request"))
                                .responseSchema(Schema.schema("프로필 수정 Response"))
                                .build()
                        ))
                );
    }

    @Test
    public void 프로필_정보_조회_성공() throws Exception {
        // given

        // when
        ResultActions actions = mockMvc.perform(
                get("/api/members/profile")
                        .header("Authorization", "Bearer " + jwtToken)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
        );

        // then
        actions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.header.message").value(OK.getMessage()))
                .andDo(document(
                        "프로필 정보 조회 성공",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        resource(ResourceSnippetParameters.builder()
                                .tag("Member API")
                                .summary("프로필 정보 조회 API")
                                .requestHeaders(
                                        headerWithName("Authorization")
                                                .description("JWT 토큰")
                                )
                                .responseFields(
                                        getCommonResponseFields(
                                                fieldWithPath("body.nickname").type(STRING)
                                                        .description("멤버 닉네임"),
                                                fieldWithPath("body.profileImage").type(STRING)
                                                        .description("멤버 프로필 이미지 url"),
                                                fieldWithPath("body.email").type(STRING)
                                                        .description("멤버 이메일")
                                        )
                                )
                                .requestSchema(Schema.schema("프로필 정보 조회 Request"))
                                .responseSchema(Schema.schema("프로필 정보 조회 Response"))
                                .build()
                        ))
                );
    }
}
