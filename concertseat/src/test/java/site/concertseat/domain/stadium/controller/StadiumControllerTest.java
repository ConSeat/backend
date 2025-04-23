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
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static site.concertseat.global.statuscode.ErrorCode.NOT_FOUND;
import static site.concertseat.global.statuscode.SuccessCode.OK;
import static site.concertseat.utils.ResponseFieldUtils.getCommonResponseFields;

@Transactional
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
@ExtendWith(RestDocumentationExtension.class)
public class StadiumControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    public void 콘서트장_목록_조회_성공() throws Exception {
        //given

        //when
        ResultActions actions = mockMvc.perform(
                get("/api/stadiums")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
        );

        //then
        actions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.header.message").value(OK.getMessage()))
                .andDo(document(
                        "콘서트장 목록 조회 성공",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        resource(ResourceSnippetParameters.builder()
                                .tag("Stadium API")
                                .summary("콘서트장 목록 조회 API")
                                .responseFields(
                                        getCommonResponseFields(
                                                fieldWithPath("body.active[].stadiumId").type(NUMBER)
                                                        .description("활성화된 콘서트장 Id"),
                                                fieldWithPath("body.active[].stadiumName").type(STRING)
                                                        .description("활성화된 콘서트장 이름"),
                                                fieldWithPath("body.active[].stadiumImage").type(STRING)
                                                        .description("활성화된 콘서트장 이미지 src"),
                                                fieldWithPath("body.inactive[].stadiumId").type(NUMBER)
                                                        .description("비활성화된 콘서트장 Id"),
                                                fieldWithPath("body.inactive[].stadiumName").type(STRING)
                                                        .description("비활성화된 콘서트장 이름"),
                                                fieldWithPath("body.inactive[].stadiumImage").type(STRING)
                                                        .description("비활성화된 콘서트장 이미지 src"),
                                                fieldWithPath("body.totalReviewCount").type(NUMBER)
                                                        .description("전체 리뷰 개수")
                                        )
                                )
                                .responseSchema(Schema.schema("콘서트장 목록 조회 Response"))
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

    @Test
    public void 콘서트장_정보_조회_실패_없는_경기장_아이디() throws Exception {
        //given
        int stadiumId = 1000;

        //when
        ResultActions actions = mockMvc.perform(
                get("/api/stadiums/{stadiumId}", stadiumId)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
        );

        //then
        actions
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.header.message").value(NOT_FOUND.getMessage()))
                .andDo(document(
                        "콘서트장 정보 조회 실패(없는 경기장 아이디)",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        resource(ResourceSnippetParameters.builder()
                                .tag("Stadium API")
                                .summary("콘서트장 정보 조회 API")
                                .responseFields(
                                        getCommonResponseFields(
                                                fieldWithPath("body").type(NULL)
                                                        .description("본문 없음")
                                        )
                                )
                                .responseSchema(Schema.schema("콘서트장 정보 조회 Response"))
                                .build()
                        ))
                );
    }

    @Test
    public void 구역_리스트_조회_성공() throws Exception {
        //given
        int stadiumId = 1;

        //when
        ResultActions actions = mockMvc.perform(
                get("/api/stadiums/{stadiumId}/sections", stadiumId)
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .with(csrf())
        );

        //then
        actions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.header.message").value(OK.getMessage()))
                .andDo(document(
                        "구역 리스트 조회 성공",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        resource(ResourceSnippetParameters.builder()
                                .tag("Stadium API")
                                .summary("구역 리스트 조회 API")
                                .responseFields(
                                        getCommonResponseFields(
                                                fieldWithPath("body.floors").type(ARRAY)
                                                        .description("층 목록"),
                                                fieldWithPath("body.floors[].name").type(STRING)
                                                        .description("층 이름"),
                                                fieldWithPath("body.floors[].sections").type(ARRAY)
                                                        .description("구역 목록"),
                                                fieldWithPath("body.floors[].sections[].sectionId").type(NUMBER)
                                                        .description("구역 아이디"),
                                                fieldWithPath("body.floors[].sections[].name").type(STRING)
                                                        .description("구역 이름")
                                        )
                                )
                                .responseSchema(Schema.schema("구역 리스트 조회 Response"))
                                .build()
                        ))
                );
    }

    @Test
    public void 구역_리스트_조회_실패_없는_공연장_아이디() throws Exception {
        //given
        int stadiumId = 10001;

        //when
        ResultActions actions = mockMvc.perform(
                get("/api/stadiums/{stadiumId}/sections", stadiumId)
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .with(csrf())
        );

        //then
        actions
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.header.message").value(NOT_FOUND.getMessage()))
                .andDo(document(
                        "구역 리스트 조회 실패 - 존재하지 않는 공연장 아이디",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        resource(ResourceSnippetParameters.builder()
                                .tag("Stadium API")
                                .summary("구역 리스트 조회 API")
                                .responseSchema(Schema.schema("구역 리스트 조회 Response"))
                                .build()
                        ))
                );
    }

    @Test
    public void 좌석_리스트_조회_성공() throws Exception {
        //given
        int sectionId = 4;

        //when
        ResultActions actions = mockMvc.perform(
                get("/api/stadiums/sections/{sectionId}/seating", sectionId)
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .with(csrf())
        );

        //then
        actions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.header.message").value(OK.getMessage()))
                .andDo(document(
                        "좌석(열) 리스트 조회 성공",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        resource(ResourceSnippetParameters.builder()
                                .tag("Stadium API")
                                .summary("좌석(열) 리스트 조회 API")
                                .responseFields(
                                        getCommonResponseFields(
                                                fieldWithPath("body.sectionInfo").type(STRING)
                                                        .description("구역 정보"),
                                                fieldWithPath("body.seating").type(ARRAY)
                                                    .description("좌석(열) 목록"),
                                                fieldWithPath("body.seating[].seatingId").type(NUMBER)
                                                        .description("좌석(열) 아이디"),
                                                fieldWithPath("body.seating[].name").type(STRING)
                                                        .description("좌석(열) 이름"),
                                                fieldWithPath("body.seating[].reviewCount").type(NUMBER)
                                                        .description("리뷰 개수")
                                        )
                                )
                                .responseSchema(Schema.schema("좌석(열) 리스트 조회 Response"))
                                .build()
                        ))
                );
    }

    @Test
    public void 좌석_리스트_조회_실패_없는_공연장_아이디() throws Exception {
        //given
        int sectionId = 10004;

        //when
        ResultActions actions = mockMvc.perform(
                get("/api/stadiums/sections/{sectionId}/seating", sectionId)
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .with(csrf())
        );

        //then
        actions
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.header.message").value(NOT_FOUND.getMessage()))
                .andDo(document(
                        "좌석(열) 리스트 조회 실패 - 없는 공연장 아이디",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        resource(ResourceSnippetParameters.builder()
                                .tag("Stadium API")
                                .summary("좌석(열) 리스트 조회 API")
                                .responseSchema(Schema.schema("좌석(열) 리스트 조회 Response"))
                                .build()
                        ))
                );
    }

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
    public void 콘서트장_특징_정보_조회_성공() throws Exception {
        //given

        //when
        ResultActions actions = mockMvc.perform(
                get("/api/stadiums/features")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
        );

        //then
        actions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.header.message").value(OK.getMessage()))
                .andDo(document(
                        "콘서트장 특징 조회 성공",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        resource(ResourceSnippetParameters.builder()
                                .tag("Stadium API")
                                .summary("콘서트장 특징 조회 API")
                                .responseFields(
                                        getCommonResponseFields(
                                                fieldWithPath("body.features[].featureId").type(NUMBER)
                                                        .description("특징 아이디"),
                                                fieldWithPath("body.features[].name").type(STRING)
                                                        .description("특징 정보")

                                        )
                                )
                                .responseSchema(Schema.schema("콘서트장 특징 조회 Response"))
                                .build()
                        ))
                );
    }

    @Test
    public void 콘서트장_방해_요소_조회_성공() throws Exception {
        //given

        //when
        ResultActions actions = mockMvc.perform(
                get("/api/stadiums/obstructions")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
        );

        //then
        actions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.header.message").value(OK.getMessage()))
                .andDo(document(
                        "콘서트장 방해요소 조회 성공",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        resource(ResourceSnippetParameters.builder()
                                .tag("Stadium API")
                                .summary("콘서트장 방해요소 조회 API")
                                .responseFields(
                                        getCommonResponseFields(
                                                fieldWithPath("body.obstructions[].obstructionId").type(NUMBER)
                                                        .description("방해요소 아이디"),
                                                fieldWithPath("body.obstructions[].name").type(STRING)
                                                        .description("방해요소 정보")

                                        )
                                )
                                .responseSchema(Schema.schema("콘서트장 방해요소 조회 Response"))
                                .build()
                        ))
                );
    }
}