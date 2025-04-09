package site.concertseat.domain.bookmark.controller;

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
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;
import site.concertseat.domain.member.enums.Role;
import site.concertseat.global.jwt.service.JwtUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document;
import static com.epages.restdocs.apispec.ResourceDocumentation.parameterWithName;
import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatTypes.*;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static site.concertseat.global.statuscode.ErrorCode.INVALID_ARGUMENT;
import static site.concertseat.global.statuscode.SuccessCode.OK;
import static site.concertseat.utils.ResponseFieldUtils.getCommonResponseFields;

@Transactional
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
@ExtendWith(RestDocumentationExtension.class)
public class BookmarkControllerTest {
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
    public void 관심_시야_경기장_리스트_조회_성공() throws Exception {
        // given

        // when
        ResultActions actions = mockMvc.perform(
                get("/api/members/bookmarks/stadiums")
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
                        "관심 시야 경기장 리스트 조회 성공",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        resource(ResourceSnippetParameters.builder()
                                .tag("Member API")
                                .summary("관심 시야 경기장 리스트 조회 API")
                                .requestHeaders(
                                        headerWithName("Authorization")
                                                .description("JWT 토큰")
                                )
                                .responseFields(
                                        getCommonResponseFields(
                                                fieldWithPath("body.stadiums[].stadiumId").type(STRING)
                                                        .description("경기장 아이디"),
                                                fieldWithPath("body.stadiums[].stadiumName").type(STRING)
                                                        .description("경기장 이름")
                                        )
                                )
                                .requestSchema(Schema.schema("관심 시야 경기장 리스트 조회 Request"))
                                .responseSchema(Schema.schema("관심 시야 경기장 리스트 조회 Response"))
                                .build()
                        ))
                );
    }

    @Test
    public void 관심_시야_목록_조회_성공() throws Exception {
        //given
        Integer stadiumId = 1;
        LocalDateTime now = LocalDateTime.now().minusMinutes(5);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String lastModifiedAt = now.format(formatter);

        //when
        ResultActions actions = mockMvc.perform(
                get("/api/members/bookmarks")
                        .param("stadiumId", String.valueOf(stadiumId))
                        .param("lastModifiedAt", lastModifiedAt)
                        .header("Authorization", "Bearer " + jwtToken)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
        );

        //then
        actions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.header.message").value(OK.getMessage()))
                .andDo(document(
                        "관심 시야 목록 조회 성공",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        resource(ResourceSnippetParameters.builder()
                                .tag("Member API")
                                .summary("관심 시야 목록 조회 API")
                                .requestHeaders(
                                        headerWithName("Authorization")
                                                .description("JWT 토큰")
                                )
                                .queryParameters(
                                        List.of(
                                                parameterWithName("stadiumId")
                                                        .description("경기장 아이디"),
                                                parameterWithName("lastModifiedAt")
                                                        .description("마지막 수정 일자(yyyy-MM-dd HH:mm:ss)")
                                                        .optional()
                                        )
                                )
                                .responseFields(
                                        getCommonResponseFields(
                                                fieldWithPath("body.reviews.content[].reviewId").type(NUMBER)
                                                        .description("경기장 아이디"),
                                                fieldWithPath("body.reviews.content[].thumbnailUrl").type(STRING)
                                                        .description("대표 이미지 url"),
                                                fieldWithPath("body.reviews.content[].floorName").type(STRING)
                                                        .description("층 이름"),
                                                fieldWithPath("body.reviews.content[].sectionName").type(STRING)
                                                        .description("구역 이름"),
                                                fieldWithPath("body.reviews.content[].seatingName").type(STRING)
                                                        .description("열 이름"),
                                                fieldWithPath("body.reviews.content[].modifiedAt").type(STRING)
                                                        .description("마지막 수정일자"),
                                                fieldWithPath("body.reviews.sliceNumber").type(NUMBER)
                                                        .description("현재 페이지 숫자"),
                                                fieldWithPath("body.reviews.size").type(NUMBER)
                                                        .description("페이지 개수"),
                                                fieldWithPath("body.reviews.hasNext").type(BOOLEAN)
                                                        .description("다음 페이지 존재 유무"),
                                                fieldWithPath("body.reviews.numberOfElements").type(NUMBER)
                                                        .description("contents 배열 사이즈")

                                        )
                                )
                                .requestSchema(Schema.schema("관심 시야 목록 조회 Request"))
                                .responseSchema(Schema.schema("관심 시야 목록 조회 Response"))
                                .build()
                        ))
                );
    }

    @Test
    public void 관심_시야_목록_조회_실패_비어있는_경기장_아이디() throws Exception {
        //given
        LocalDateTime now = LocalDateTime.now().minusMinutes(5);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String lastModifiedAt = now.format(formatter);

        //when
        ResultActions actions = mockMvc.perform(
                get("/api/members/bookmarks")
                        .param("lastModifiedAt", lastModifiedAt)
                        .header("Authorization", "Bearer " + jwtToken)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
        );

        //then
        actions
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.header.message").value(INVALID_ARGUMENT.getMessage()))
                .andDo(document(
                        "관심 시야 목록 조회 실패 - 비어있는 경기장 아이디",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        resource(ResourceSnippetParameters.builder()
                                .tag("Member API")
                                .summary("관심 시야 목록 조회 API")
                                .requestHeaders(
                                        headerWithName("Authorization")
                                                .description("JWT 토큰")
                                )
                                .queryParameters(
                                        List.of(
                                                parameterWithName("lastModifiedAt")
                                                        .description("마지막 수정 일자(yyyy-MM-dd HH:mm:ss)")
                                                        .optional()
                                        )
                                )
                                .responseFields(
                                        getCommonResponseFields(
                                                fieldWithPath("body").type(NULL)
                                                        .description("내용 없음")

                                        )
                                )
                                .requestSchema(Schema.schema("관심 시야 목록 조회 Request"))
                                .responseSchema(Schema.schema("관심 시야 목록 조회 Response"))
                                .build()
                        ))
                );
    }
}
