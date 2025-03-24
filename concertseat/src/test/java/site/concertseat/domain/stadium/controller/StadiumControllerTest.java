package site.concertseat.domain.stadium.controller;


import com.epages.restdocs.apispec.ResourceSnippetParameters;
import com.epages.restdocs.apispec.Schema;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document;
import static com.epages.restdocs.apispec.ResourceDocumentation.parameterWithName;
import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatTypes.*;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static site.concertseat.global.statuscode.ErrorCode.NOT_FOUND;
import static site.concertseat.global.statuscode.SuccessCode.OK;
import static site.concertseat.utils.ResponseFieldUtils.getCommonResponseFields;

@WithMockUser
@Transactional
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
@ExtendWith(RestDocumentationExtension.class)
public class StadiumControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    public void 콘서트_목록_조회_기본값_검색_성공() throws Exception {
        // given
        int stadiumId = 1;

        // when
        ResultActions actions = mockMvc.perform(
                get("/api/stadiums/{stadiumId}/concerts", stadiumId)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
        );

        // then
        actions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.header.message").value(OK.getMessage()))
                .andDo(document(
                        "콘서트 목록 조회 성공(기본값)",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        resource(ResourceSnippetParameters.builder()
                                .tag("Stadium API")
                                .summary("콘서트 목록 조회 API")
                                .responseFields(
                                        getCommonResponseFields(
                                                fieldWithPath("body.concerts[].concertId").type(NUMBER)
                                                        .description("콘서트 아이디"),
                                                fieldWithPath("body.concerts[].concertName").type(STRING)
                                                        .description("콘서트 이름")

                                        )
                                )
                                .responseSchema(Schema.schema("콘서트 목록 조회 Response"))
                                .build()
                        ))
                );
    }

    @Test
    public void 콘서트_목록_조회_조건_검색_성공() throws Exception {
        // given
        int stadiumId = 1;

        // when
        ResultActions actions = mockMvc.perform(
                get("/api/stadiums/{stadiumId}/concerts", stadiumId)
                        .param("query", "TOMORROW")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
        );

        // then
        actions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.header.message").value(OK.getMessage()))
                .andDo(document(
                        "콘서트 목록 조회 성공(검색조건 있는 경우)",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        resource(ResourceSnippetParameters.builder()
                                .tag("Stadium API")
                                .summary("콘서트 목록 조회 API")
                                .queryParameters(
                                        parameterWithName("query").description("검색할 콘서트 이름").optional()
                                )
                                .responseFields(
                                        getCommonResponseFields(
                                                fieldWithPath("body.concerts[].concertId").type(NUMBER)
                                                        .description("콘서트 아이디"),
                                                fieldWithPath("body.concerts[].concertName").type(STRING)
                                                        .description("콘서트 이름")

                                        )
                                )
                                .responseSchema(Schema.schema("콘서트 목록 조회 Response"))
                                .build()
                        ))
                );
    }

    @Test
    public void 콘서트장_정보_조회_성공() throws Exception {
        //given
        int stadiumId = 1;

        //when
        ResultActions actions = mockMvc.perform(
                get("/api/stadiums/{stadiumId}", stadiumId)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
        );

        //then
        actions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.header.message").value(OK.getMessage()))
                .andDo(document(
                        "콘서트장 정보 조회 성공",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        resource(ResourceSnippetParameters.builder()
                                .tag("Stadium API")
                                .summary("콘서트장 정보 조회 API")
                                .responseFields(
                                        getCommonResponseFields(
                                                fieldWithPath("body.floors[].name").type(STRING)
                                                        .description("구분 이름(FLOOR, 1F, 2F)"),
                                                fieldWithPath("body.floors[].sections[].name").type(STRING)
                                                        .description("구역 이름"),
                                                fieldWithPath("body.floors[].sections[].seats[].name").type(STRING)
                                                        .description("열 이름"),
                                                fieldWithPath("body.floors[].sections[].seats[].seatingId").type(NUMBER)
                                                        .description("열 아이디"),
                                                fieldWithPath("body.floors[].sections[].seats[].name").type(STRING)
                                                        .description("열 이름")

                                        )
                                )
                                .responseSchema(Schema.schema("콘서트장 정보 조회 Response"))
                                .build()
                        ))
                );
    }
}
