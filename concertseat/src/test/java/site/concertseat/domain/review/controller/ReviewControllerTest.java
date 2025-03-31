package site.concertseat.domain.review.controller;

import com.epages.restdocs.apispec.ResourceSnippetParameters;
import com.epages.restdocs.apispec.Schema;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import site.concertseat.domain.review.dto.ReviewPostReq;
import site.concertseat.global.jwt.service.JwtUtils;

import java.util.ArrayList;
import java.util.List;

import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document;
import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatTypes.*;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static site.concertseat.global.statuscode.ErrorCode.*;
import static site.concertseat.global.statuscode.SuccessCode.CREATED;
import static site.concertseat.utils.ResponseFieldUtils.getCommonResponseFields;

@Transactional
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
@ExtendWith(RestDocumentationExtension.class)
public class ReviewControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private ObjectMapper objectMapper;

    private static final String UUID = "c2141f87-30d7-4d2e-a9ef-3eff99acd957";

    private String jwtToken;

    @PostConstruct
    public void init() {
        jwtToken = jwtUtils.createAccessToken(UUID, Role.ROLE_USER);
    }

    @Test
    public void 콘서트_리뷰_등록_성공() throws Exception {
        // given
        int seatingId = 1;
        int concertId = 1;
        ReviewPostReq req = getReviewPostReq();

        String content = objectMapper.writeValueAsString(req);

        // when
        ResultActions actions = mockMvc.perform(
                post("/api/reviews/concerts/{concertId}/seating/{seatingId}", concertId, seatingId)
                        .header("Authorization", "Bearer " + jwtToken)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .content(content)
                        .with(csrf())
        );

        // then
        actions
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.header.message").value(CREATED.getMessage()))
                .andDo(document(
                        "콘서트 리뷰 등록 성공",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        resource(ResourceSnippetParameters.builder()
                                .tag("Review API")
                                .summary("콘서트 리뷰 등록 API")
                                .requestHeaders(
                                        headerWithName("Authorization")
                                                .description("JWT 토큰")
                                )
                                .requestFields(
                                        List.of(
                                                fieldWithPath("images[]").type(ARRAY)
                                                        .description("업로드된 url 리스트"),
                                                fieldWithPath("features[]").type(ARRAY)
                                                        .description("특징 Id 리스트"),
                                                fieldWithPath("obstructions[]").type(ARRAY)
                                                        .description("방해요소 Id 리스트"),
                                                fieldWithPath("contents").type(STRING)
                                                        .description("리뷰 내용"),
                                                fieldWithPath("screenDistance").type(STRING)
                                                        .description("전광판과의 거리"),
                                                fieldWithPath("stageDistance").type(STRING)
                                                        .description("본무대와의 거리"),
                                                fieldWithPath("thrustStageDistance").type(STRING)
                                                        .description("돌출무대와의 거리")
                                        )
                                )
                                .responseFields(
                                        getCommonResponseFields(
                                                fieldWithPath("body").type(NULL)
                                                        .description("내용 없음")

                                        )
                                )
                                .requestSchema(Schema.schema("콘서트 리뷰 등록 Request"))
                                .responseSchema(Schema.schema("콘서트 리뷰 등록 Response"))
                                .build()
                        ))
                );
    }

    private static ReviewPostReq getReviewPostReq() {
        List<String> images = new ArrayList<>();
        images.add("url1");
        images.add("url2");
        images.add("url3");
        List<Integer> features = new ArrayList<>();
        features.add(1);

        List<Integer> obstructions = new ArrayList<>();
        obstructions.add(1);

        ReviewPostReq req = new ReviewPostReq();

        req.setContents("넘잘보이고 좋아요 1열 최고");
        req.setImages(images);
        req.setFeatures(features);
        req.setObstructions(obstructions);
        req.setStageDistance("CLOSE");
        req.setScreenDistance("CLOSE");
        req.setThrustStageDistance("CLOSE");

        return req;
    }


}
