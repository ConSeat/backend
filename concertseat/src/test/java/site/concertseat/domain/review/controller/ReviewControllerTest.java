package site.concertseat.domain.review.controller;

import com.epages.restdocs.apispec.ResourceSnippetParameters;
import com.epages.restdocs.apispec.Schema;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import org.junit.jupiter.api.DisplayName;
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
import site.concertseat.domain.review.dto.req.ReviewPostReq;
import site.concertseat.global.jwt.service.JwtUtils;

import java.util.ArrayList;
import java.util.List;

import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document;
import static com.epages.restdocs.apispec.ResourceDocumentation.parameterWithName;
import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatTypes.*;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static site.concertseat.global.statuscode.ErrorCode.*;
import static site.concertseat.global.statuscode.SuccessCode.*;
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
                                                        .description("이미지 url 리스트"),
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

    @Test
    public void 콘서트_리뷰_등록_실패_잘못된_거리_변수() throws Exception {
        // given
        int seatingId = 1;
        int concertId = 1;
        ReviewPostReq req = getInvalidDistance();

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
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.header.message").value(BAD_REQUEST.getMessage()))
                .andDo(document(
                        "콘서트 리뷰 등록 실패 - 잘못된 거리 변수",
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
                                                        .description("이미지 url 리스트"),
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

    private static ReviewPostReq getInvalidDistance() {
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
        req.setStageDistance("CLOSER");
        req.setScreenDistance("CLOSE");
        req.setThrustStageDistance("CLOSE");

        return req;
    }

    @Test
    public void 콘서트_리뷰_등록_실패_비어있는_리뷰() throws Exception {
        // given
        int seatingId = 1;
        int concertId = 1;
        ReviewPostReq req = getEmptyContents();

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
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.header.message").value(INVALID_ARGUMENT.getMessage()))
                .andDo(document(
                        "콘서트 리뷰 등록 실패 - 비어있는 리뷰",
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
                                                        .description("이미지 url 리스트"),
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

    private static ReviewPostReq getEmptyContents() {
        List<String> images = new ArrayList<>();
        images.add("url1");
        images.add("url2");
        images.add("url3");
        List<Integer> features = new ArrayList<>();
        features.add(1);

        List<Integer> obstructions = new ArrayList<>();
        obstructions.add(1);

        ReviewPostReq req = new ReviewPostReq();

        req.setContents("");
        req.setImages(images);
        req.setFeatures(features);
        req.setObstructions(obstructions);
        req.setStageDistance("CLOSE");
        req.setScreenDistance("CLOSE");
        req.setThrustStageDistance("CLOSE");

        return req;
    }

    @Test
    public void 콘서트_리뷰_등록_실패_리뷰_길이_초과() throws Exception {
        // given
        int seatingId = 1;
        int concertId = 1;
        ReviewPostReq req = getInvalidContents();

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
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.header.message").value(INVALID_ARGUMENT.getMessage()))
                .andDo(document(
                        "콘서트 리뷰 등록 실패 - 리뷰 300자 초과",
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
                                                        .description("이미지 url 리스트"),
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

    private static ReviewPostReq getInvalidContents() {
        List<String> images = new ArrayList<>();
        images.add("url1");
        images.add("url2");
        images.add("url3");
        List<Integer> features = new ArrayList<>();
        features.add(1);

        List<Integer> obstructions = new ArrayList<>();
        obstructions.add(1);

        ReviewPostReq req = new ReviewPostReq();

        req.setContents("와, 이 콘서트는 진짜 말로 다 표현할 수 없을 정도로 멋졌어요! 무대 위에서 빛나는 그들의 모습은 정말 눈을 뗄 수가 없었고, " +
                "팬들과의 소통이 그 무엇보다 감동적이었어요. 첫 곡이 시작되자마자, 그 에너지에 푹 빠져들었고, " +
                "노래 한 곡 한 곡마다 울려 퍼지는 팬들의 함성은 정말 그 순간을 특별하게 만들어주었죠. " +
                "특히, 팬들을 향한 그들의 사랑이 넘치는 표정과 제스처는 나도 모르게 눈물이 맺히게 만들었어요. 어떻게 그리 감동적이고 멋진 모습을 보여줄 수 있죠? " +
                "사랑하는 아이돌의 콘서트는 매번 나를 다시 살아나는 기분이 들게 해요. 그들의 무대는 그저 공연이 아닌, 하나의 예술작품 같아요. " +
                "그 모든 순간들이 정말 꿈만 같았고, 잊지 못할 경험이었습니다. 이번 콘서트는 정말 평생 기억에 남을 거예요.");
        req.setImages(images);
        req.setFeatures(features);
        req.setObstructions(obstructions);
        req.setStageDistance("CLOSE");
        req.setScreenDistance("CLOSE");
        req.setThrustStageDistance("CLOSE");

        return req;
    }

    @Test
    public void 콘서트_리뷰_등록_실패_비어있는_이미지() throws Exception {
        // given
        int seatingId = 1;
        int concertId = 1;
        ReviewPostReq req = getEmptyImage();

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
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.header.message").value(INVALID_ARGUMENT.getMessage()))
                .andDo(document(
                        "콘서트 리뷰 등록 실패 - 비어있는 이미지",
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
                                                        .description("이미지 url 리스트"),
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

    private static ReviewPostReq getEmptyImage() {
        List<String> images = new ArrayList<>();

        List<Integer> features = new ArrayList<>();
        features.add(1);

        List<Integer> obstructions = new ArrayList<>();
        obstructions.add(1);

        ReviewPostReq req = new ReviewPostReq();

        req.setContents("멋져용");
        req.setImages(images);
        req.setFeatures(features);
        req.setObstructions(obstructions);
        req.setStageDistance("CLOSE");
        req.setScreenDistance("CLOSE");
        req.setThrustStageDistance("CLOSE");

        return req;
    }

    @Test
    public void 콘서트_리뷰_등록_실패_이미지_개수_초과() throws Exception {
        // given
        int seatingId = 1;
        int concertId = 1;
        ReviewPostReq req = getInvalidImage();

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
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.header.message").value(INVALID_ARGUMENT.getMessage()))
                .andDo(document(
                        "콘서트 리뷰 등록 실패 - 이미지 4개 초과",
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
                                                        .description("이미지 url 리스트"),
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

    private static ReviewPostReq getInvalidImage() {
        List<String> images = new ArrayList<>();
        images.add("url1");
        images.add("url2");
        images.add("url3");
        images.add("url4");
        images.add("url5");

        List<Integer> features = new ArrayList<>();
        features.add(1);

        List<Integer> obstructions = new ArrayList<>();
        obstructions.add(1);

        ReviewPostReq req = new ReviewPostReq();

        req.setContents("멋져용");
        req.setImages(images);
        req.setFeatures(features);
        req.setObstructions(obstructions);
        req.setStageDistance("CLOSE");
        req.setScreenDistance("CLOSE");
        req.setThrustStageDistance("CLOSE");

        return req;
    }

    private static ReviewPostReq getEmptyFeatureId() {
        List<String> images = new ArrayList<>();
        images.add("url1");

        List<Integer> features = new ArrayList<>();

        List<Integer> obstructions = new ArrayList<>();
        obstructions.add(1);

        ReviewPostReq req = new ReviewPostReq();

        req.setContents("멋져용");
        req.setImages(images);
        req.setFeatures(features);
        req.setObstructions(obstructions);
        req.setStageDistance("CLOSE");
        req.setScreenDistance("CLOSE");
        req.setThrustStageDistance("CLOSE");

        return req;
    }

    @Test
    public void 콘서트_리뷰_등록_실패_잘못된_특징_아이디() throws Exception {
        // given
        int seatingId = 1;
        int concertId = 1;
        ReviewPostReq req = getInvalidFeatureId();

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
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.header.message").value(BAD_REQUEST.getMessage()))
                .andDo(document(
                        "콘서트 리뷰 등록 실패 - 잘못된 특징 아이디",
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
                                                        .description("이미지 url 리스트"),
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

    private static ReviewPostReq getInvalidFeatureId() {
        List<String> images = new ArrayList<>();
        images.add("url1");

        List<Integer> features = new ArrayList<>();
        features.add(3);
        features.add(100);

        List<Integer> obstructions = new ArrayList<>();
        obstructions.add(1);

        ReviewPostReq req = new ReviewPostReq();

        req.setContents("멋져용");
        req.setImages(images);
        req.setFeatures(features);
        req.setObstructions(obstructions);
        req.setStageDistance("CLOSE");
        req.setScreenDistance("CLOSE");
        req.setThrustStageDistance("CLOSE");

        return req;
    }

    @Test
    public void 콘서트_리뷰_등록_실패_잘못된_방해요소_아이디() throws Exception {
        // given
        int seatingId = 1;
        int concertId = 1;
        ReviewPostReq req = getInvalidObstructionId();

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
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.header.message").value(BAD_REQUEST.getMessage()))
                .andDo(document(
                        "콘서트 리뷰 등록 실패 - 잘못된 방해요소 아이디",
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
                                                        .description("이미지 url 리스트"),
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

    private static ReviewPostReq getInvalidObstructionId() {
        List<String> images = new ArrayList<>();
        images.add("url1");

        List<Integer> features = new ArrayList<>();
        features.add(1);

        List<Integer> obstructions = new ArrayList<>();
        obstructions.add(1);
        obstructions.add(100);

        ReviewPostReq req = new ReviewPostReq();

        req.setContents("멋져용");
        req.setImages(images);
        req.setFeatures(features);
        req.setObstructions(obstructions);
        req.setStageDistance("CLOSE");
        req.setScreenDistance("CLOSE");
        req.setThrustStageDistance("CLOSE");

        return req;
    }

    @Test
    public void 콘서트_리뷰_등록_실패_없는_좌석() throws Exception {
        // given
        int seatingId = 100;
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
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.header.message").value(NOT_FOUND.getMessage()))
                .andDo(document(
                        "콘서트 리뷰 등록 실패 - 없는 좌석 아이디",
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
                                                        .description("이미지 url 리스트"),
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

    @Test
    public void 콘서트_리뷰_등록_실패_없는_콘서트_아이디() throws Exception {
        // given
        int seatingId = 1;
        int concertId = 100;
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
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.header.message").value(NOT_FOUND.getMessage()))
                .andDo(document(
                        "콘서트 리뷰 등록 실패 - 없는 콘서트 아이디",
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
                                                        .description("이미지 url 리스트"),
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

    @Test
    public void 이미지_업로드_성공() throws Exception {
        //given
        MockMultipartFile file1 = new MockMultipartFile("files", "sample1.jpg", "image/jpeg", "image/sample1.jpg".getBytes());
        MockMultipartFile file2 = new MockMultipartFile("files", "sample2.jpg", "image/jpeg", "image/sample2.jpg".getBytes());

        //when
        ResultActions actions = mockMvc.perform(
                multipart("/api/reviews/images")
                        .file(file1)
                        .file(file2)
                        .header("Authorization", "Bearer " + jwtToken)
                        .contentType("multipart/form-data")
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .with(csrf())
        );

        //then
        actions
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.header.message").value(CREATED.getMessage()))
                .andDo(document(
                        "이미지 업로드 성공",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        resource(ResourceSnippetParameters.builder()
                                .tag("Review API")
                                .summary("이미지 업로드 API")
                                .requestHeaders(
                                        headerWithName("Authorization")
                                                .description("JWT 토큰")
                                )
                                .responseFields(
                                        getCommonResponseFields(
                                                fieldWithPath("body.originalImage[]").type(ARRAY)
                                                        .description("s3 이미지 url 리스트")
                                        )
                                )
                                .requestSchema(Schema.schema("이미지 업로드 Request"))
                                .responseSchema(Schema.schema("이미지 업로드 Response"))
                                .build()
                        ))
                );
    }

    @Test
    public void 내_후기_경기장_리스트_조회_성공() throws Exception {
        // given

        // when
        ResultActions actions = mockMvc.perform(
                get("/api/reviews/stadiums")
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
                        "내 후기 경기장 리스트 조회 성공",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        resource(ResourceSnippetParameters.builder()
                                .tag("Review API")
                                .summary("내 후기 경기장 리스트 조회 API")
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
                                .requestSchema(Schema.schema("내 후기 경기장 리스트 조회 Request"))
                                .responseSchema(Schema.schema("내 후기 경기장 리스트 조회 Response"))
                                .build()
                        ))
                );
    }

    @Test
    public void 내_후기_목록_조회_성공() throws Exception {
        //given
        Integer stadiumId = 1;
        Long lastReviewId = 10L;

        //when
        ResultActions actions = mockMvc.perform(
                get("/api/reviews")
                        .param("stadiumId", String.valueOf(stadiumId))
                        .param("lastReviewId", String.valueOf(lastReviewId))
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
                        "내 후기 목록 조회 성공",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        resource(ResourceSnippetParameters.builder()
                                .tag("Review API")
                                .summary("내 후기 목록 조회 API")
                                .requestHeaders(
                                        headerWithName("Authorization")
                                                .description("JWT 토큰")
                                )
                                .queryParameters(
                                        List.of(
                                                parameterWithName("stadiumId")
                                                        .description("경기장 아이디"),
                                                parameterWithName("lastReviewId")
                                                        .description("마지막 리뷰 아이디")
                                                        .optional()
                                        )
                                )
                                .responseFields(
                                        getCommonResponseFields(
                                                fieldWithPath("body.reviews.content[].reviewId").type(NUMBER)
                                                        .description("리뷰 아이디"),
                                                fieldWithPath("body.reviews.content[].thumbnailUrl").type(STRING)
                                                        .description("대표 이미지 url"),
                                                fieldWithPath("body.reviews.content[].floorName").type(STRING)
                                                        .description("층 이름"),
                                                fieldWithPath("body.reviews.content[].sectionName").type(STRING)
                                                        .description("구역 이름"),
                                                fieldWithPath("body.reviews.content[].seatingName").type(STRING)
                                                        .description("열 이름"),
                                                fieldWithPath("body.reviews.content[].status").type(STRING)
                                                        .description("리뷰 상태"),
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
                                .requestSchema(Schema.schema("내 후기 목록 조회 Request"))
                                .responseSchema(Schema.schema("내 후기 목록 조회 Response"))
                                .build()
                        ))
                );
    }

    @Test
    public void 내_후기_목록_조회_실패_비어있는_경기장_아이디() throws Exception {
        //given
        Long lastReviewId = 10L;

        //when
        ResultActions actions = mockMvc.perform(
                get("/api/reviews")
                        .param("lastReviewId", String.valueOf(lastReviewId))
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
                        "내 후기 목록 조회 실패 - 비어있는 경기장 아이디",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        resource(ResourceSnippetParameters.builder()
                                .tag("Review API")
                                .summary("내 후기 목록 조회 API")
                                .requestHeaders(
                                        headerWithName("Authorization")
                                                .description("JWT 토큰")
                                )
                                .queryParameters(
                                        List.of(
                                                parameterWithName("lastReviewId")
                                                        .description("마지막 리뷰 아이디")
                                                        .optional()
                                        )
                                )
                                .responseFields(
                                        getCommonResponseFields(
                                                fieldWithPath("body").type(NULL)
                                                        .description("내용 없음")

                                        )
                                )
                                .requestSchema(Schema.schema("내 후기 목록 조회 Request"))
                                .responseSchema(Schema.schema("내 후기 목록 조회 Response"))
                                .build()
                        ))
                );
    }

    @Test
    public void 내_후기_상세_조회_성공() throws Exception {
        //given
        Long reviewId = 1L;

        //when
        ResultActions actions = mockMvc.perform(
                get("/api/reviews/{reviewId}", reviewId)
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
                        "내 후기 상세 조회 성공",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        resource(ResourceSnippetParameters.builder()
                                .tag("Review API")
                                .summary("내 후기 상세 조회 API")
                                .requestHeaders(
                                        headerWithName("Authorization")
                                                .description("JWT 토큰")
                                )
                                .pathParameters(
                                        parameterWithName("reviewId")
                                                .description("리뷰 아이디")
                                )
                                .responseFields(
                                        getCommonResponseFields(
                                                fieldWithPath("body.reviewId").type(NUMBER)
                                                        .description("리뷰 아이디"),
                                                fieldWithPath("body.writerNickname").type(STRING)
                                                        .description("작성자 닉네임"),
                                                fieldWithPath("body.writerSrc").type(STRING)
                                                        .description("작성자 프로필 url"),
                                                fieldWithPath("body.concertName").type(STRING)
                                                        .description("콘서트 이름"),
                                                fieldWithPath("body.images[]").type(STRING)
                                                        .description("이미지 url 리스트"),
                                                fieldWithPath("body.contents").type(STRING)
                                                        .description("후기 내용"),
                                                fieldWithPath("body.createdAt").type(STRING)
                                                        .description("작성 일자"),
                                                fieldWithPath("body.features[]").type(STRING)
                                                        .description("시야 특징"),
                                                fieldWithPath("body.obstructions[]").type(STRING)
                                                        .description("시야 방해요소"),
                                                fieldWithPath("body.status").type(BOOLEAN)
                                                        .description("후기 승인 여부"),
                                                fieldWithPath("body.rejectReason").type(STRING)
                                                        .description("반려 이유(있는 경우에만 값 존재 없으면 null)")
                                        )
                                )
                                .requestSchema(Schema.schema("내 후기 상세 조회 Request"))
                                .responseSchema(Schema.schema("내 후기 상세 조회 Response"))
                                .build()
                        ))
                );
    }

    @Test
    public void 내_후기_상세_조회_실패_없는_리뷰_아이디() throws Exception {
        //given
        Long reviewId = 10001L;

        //when
        ResultActions actions = mockMvc.perform(
                get("/api/reviews/{reviewId}", reviewId)
                        .header("Authorization", "Bearer " + jwtToken)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
        );

        //then
        actions
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.header.message").value(NOT_FOUND.getMessage()))
                .andDo(document(
                        "내 후기 상세 조회 실패 - 없는 리뷰 아이디",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        resource(ResourceSnippetParameters.builder()
                                .tag("Review API")
                                .summary("내 후기 상세 조회 API")
                                .requestHeaders(
                                        headerWithName("Authorization")
                                                .description("JWT 토큰")
                                )
                                .pathParameters(
                                        parameterWithName("reviewId")
                                                .description("리뷰 아이디")
                                )
                                .responseFields(
                                        getCommonResponseFields(
                                                fieldWithPath("body").type(NULL)
                                                        .description("내용 없음")

                                        )
                                )
                                .requestSchema(Schema.schema("내 후기 상세 조회 Request"))
                                .responseSchema(Schema.schema("내 후기 상세 조회 Response"))
                                .build()
                        ))
                );
    }

    @Test
    public void 내_후기_상세_조회_실패_나의_리뷰가_아님() throws Exception {
        //given
        Long reviewId = 14L;

        //when
        ResultActions actions = mockMvc.perform(
                get("/api/reviews/{reviewId}", reviewId)
                        .header("Authorization", "Bearer " + jwtToken)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
        );

        //then
        actions
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.header.message").value(NOT_FOUND.getMessage()))
                .andDo(document(
                        "내 후기 상세 조회 실패 - 나의 후기가 아닌 경우",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        resource(ResourceSnippetParameters.builder()
                                .tag("Review API")
                                .summary("내 후기 상세 조회 API")
                                .requestHeaders(
                                        headerWithName("Authorization")
                                                .description("JWT 토큰")
                                )
                                .pathParameters(
                                        parameterWithName("reviewId")
                                                .description("리뷰 아이디")
                                )
                                .responseFields(
                                        getCommonResponseFields(
                                                fieldWithPath("body").type(NULL)
                                                        .description("내용 없음")

                                        )
                                )
                                .requestSchema(Schema.schema("내 후기 상세 조회 Request"))
                                .responseSchema(Schema.schema("내 후기 상세 조회 Response"))
                                .build()
                        ))
                );
    }

    @Test
    public void 북마크_추가_성공() throws Exception {
        //given
        Long reviewId = 101L;

        //when
        ResultActions actions = mockMvc.perform(
                post("/api/reviews/{reviewId}/bookmarks", reviewId)
                        .header("Authorization", "Bearer " + jwtToken)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .with(csrf())
        );

        //then
        actions
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.header.message").value(CREATED.getMessage()))
                .andDo(document(
                        "북마크 추가 성공",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        resource(ResourceSnippetParameters.builder()
                                .tag("Review API")
                                .summary("북마크 추가 API")
                                .requestHeaders(
                                        headerWithName("Authorization")
                                                .description("JWT 토큰")
                                )
                                .pathParameters(
                                        parameterWithName("reviewId")
                                                .description("리뷰 아이디")
                                )
                                .responseFields(
                                        getCommonResponseFields(
                                                fieldWithPath("body").type(NULL)
                                                        .description("내용 없음")
                                        )
                                )
                                .requestSchema(Schema.schema("북마크 추가 Request"))
                                .responseSchema(Schema.schema("북마크 추가 Response"))
                                .build()
                        ))
                );
    }

    @Test
    public void 북마크_추가_실패_없는_리뷰_아이디() throws Exception {
        //given
        Long reviewId = 10001L;

        //when
        ResultActions actions = mockMvc.perform(
                post("/api/reviews/{reviewId}/bookmarks", reviewId)
                        .header("Authorization", "Bearer " + jwtToken)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .with(csrf())
        );

        //then
        actions
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.header.message").value(NOT_FOUND.getMessage()))
                .andDo(document(
                        "북마크 추가 실패 - 없는 리뷰 아이디",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        resource(ResourceSnippetParameters.builder()
                                .tag("Review API")
                                .summary("북마크 추가 API")
                                .requestHeaders(
                                        headerWithName("Authorization")
                                                .description("JWT 토큰")
                                )
                                .pathParameters(
                                        parameterWithName("reviewId")
                                                .description("리뷰 아이디")
                                )
                                .responseFields(
                                        getCommonResponseFields(
                                                fieldWithPath("body").type(NULL)
                                                        .description("내용 없음")
                                        )
                                )
                                .requestSchema(Schema.schema("북마크 추가 Request"))
                                .responseSchema(Schema.schema("북마크 추가 Response"))
                                .build()
                        ))
                );
    }

    @Test
    public void 북마크_해제_성공() throws Exception {
        //given
        Long reviewId = 102L;

        //when

        ResultActions actions = mockMvc.perform(
                delete("/api/reviews/{reviewId}/bookmarks", reviewId)
                        .header("Authorization", "Bearer " + jwtToken)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .with(csrf())
        );

        //then
        actions
                .andExpect(status().isNoContent())
                .andExpect(jsonPath("$.header.message").value(DELETED.getMessage()))
                .andDo(document(
                        "북마크 해제 성공",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        resource(ResourceSnippetParameters.builder()
                                .tag("Review API")
                                .summary("북마크 해제 API")
                                .requestHeaders(
                                        headerWithName("Authorization")
                                                .description("JWT 토큰")
                                )
                                .pathParameters(
                                        parameterWithName("reviewId")
                                                .description("리뷰 아이디")
                                )
                                .requestSchema(Schema.schema("북마크 해제 Request"))
                                .build()
                        ))
                );
    }

    @Test
    public void 북마크_해제_실패_없는_리뷰_아이디() throws Exception {
        //given
        Long reviewId = 10001L;

        //when
        ResultActions actions = mockMvc.perform(
                delete("/api/reviews/{reviewId}/bookmarks", reviewId)
                        .header("Authorization", "Bearer " + jwtToken)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .with(csrf())
        );

        //then
        actions
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.header.message").value(NOT_FOUND.getMessage()))
                .andDo(document(
                        "북마크 해제 실패 - 없는 리뷰 아이디",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        resource(ResourceSnippetParameters.builder()
                                .tag("Review API")
                                .summary("북마크 추가 API")
                                .requestHeaders(
                                        headerWithName("Authorization")
                                                .description("JWT 토큰")
                                )
                                .pathParameters(
                                        parameterWithName("reviewId")
                                                .description("리뷰 아이디")
                                )
                                .responseFields(
                                        getCommonResponseFields(
                                                fieldWithPath("body").type(NULL)
                                                        .description("내용 없음")
                                        )
                                )
                                .requestSchema(Schema.schema("북마크 해제 Request"))
                                .responseSchema(Schema.schema("북마크 해제 Response"))
                                .build()
                        ))
                );
    }


    @Test
    public void 좋아요_추가_성공() throws Exception {
        //given
        Long reviewId = 101L;

        //when
        ResultActions actions = mockMvc.perform(
                post("/api/reviews/{reviewId}/likes", reviewId)
                        .header("Authorization", "Bearer " + jwtToken)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .with(csrf())
        );

        //then
        actions
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.header.message").value(CREATED.getMessage()))
                .andDo(document(
                        "좋아요 추가 성공",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        resource(ResourceSnippetParameters.builder()
                                .tag("Review API")
                                .summary("좋아요 추가 API")
                                .requestHeaders(
                                        headerWithName("Authorization")
                                                .description("JWT 토큰")
                                )
                                .pathParameters(
                                        parameterWithName("reviewId")
                                                .description("리뷰 아이디")
                                )
                                .responseFields(
                                        getCommonResponseFields(
                                                fieldWithPath("body").type(NULL)
                                                        .description("내용 없음")
                                        )
                                )
                                .requestSchema(Schema.schema("좋아요 추가 Request"))
                                .responseSchema(Schema.schema("좋아요 추가 Response"))
                                .build()
                        ))
                );
    }

    @Test
    public void 좋아요_추가_실패_없는_리뷰_아이디() throws Exception {
        //given
        Long reviewId = 10001L;

        //when
        ResultActions actions = mockMvc.perform(
                post("/api/reviews/{reviewId}/likes", reviewId)
                        .header("Authorization", "Bearer " + jwtToken)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .with(csrf())
        );

        //then
        actions
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.header.message").value(NOT_FOUND.getMessage()))
                .andDo(document(
                        "좋아요 추가 실패 - 없는 리뷰 아이디",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        resource(ResourceSnippetParameters.builder()
                                .tag("Review API")
                                .summary("좋아요 추가 API")
                                .requestHeaders(
                                        headerWithName("Authorization")
                                                .description("JWT 토큰")
                                )
                                .pathParameters(
                                        parameterWithName("reviewId")
                                                .description("리뷰 아이디")
                                )
                                .responseFields(
                                        getCommonResponseFields(
                                                fieldWithPath("body").type(NULL)
                                                        .description("내용 없음")
                                        )
                                )
                                .requestSchema(Schema.schema("좋아요 추가 Request"))
                                .responseSchema(Schema.schema("좋아요 추가 Response"))
                                .build()
                        ))
                );
    }

    @Test
    public void 좋아요_해제_성공() throws Exception {
        //given
        Long reviewId = 102L;

        //when

        ResultActions actions = mockMvc.perform(
                delete("/api/reviews/{reviewId}/likes", reviewId)
                        .header("Authorization", "Bearer " + jwtToken)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .with(csrf())
        );

        //then
        actions
                .andExpect(status().isNoContent())
                .andExpect(jsonPath("$.header.message").value(NO_CONTENT.getMessage()))
                .andDo(document(
                        "좋아요 해제 성공",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        resource(ResourceSnippetParameters.builder()
                                .tag("Review API")
                                .summary("좋아요 해제 API")
                                .requestHeaders(
                                        headerWithName("Authorization")
                                                .description("JWT 토큰")
                                )
                                .pathParameters(
                                        parameterWithName("reviewId")
                                                .description("리뷰 아이디")
                                )
                                .requestSchema(Schema.schema("좋아요 해제 Request"))
                                .build()
                        ))
                );
    }

    @Test
    public void 좋아요_해제_실패_없는_리뷰_아이디() throws Exception {
        //given
        Long reviewId = 10001L;

        //when
        ResultActions actions = mockMvc.perform(
                delete("/api/reviews/{reviewId}/likes", reviewId)
                        .header("Authorization", "Bearer " + jwtToken)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                        .with(csrf())
        );

        //then
        actions
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.header.message").value(NOT_FOUND.getMessage()))
                .andDo(document(
                        "좋아요 해제 실패 - 없는 리뷰 아이디",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        resource(ResourceSnippetParameters.builder()
                                .tag("Review API")
                                .summary("좋아요 추가 API")
                                .requestHeaders(
                                        headerWithName("Authorization")
                                                .description("JWT 토큰")
                                )
                                .pathParameters(
                                        parameterWithName("reviewId")
                                                .description("리뷰 아이디")
                                )
                                .responseFields(
                                        getCommonResponseFields(
                                                fieldWithPath("body").type(NULL)
                                                        .description("내용 없음")
                                        )
                                )
                                .requestSchema(Schema.schema("좋아요 해제 Request"))
                                .responseSchema(Schema.schema("좋아요 해제 Response"))
                                .build()
                        ))
                );
    }

    @Test
    public void 구역별_시야_조회_성공() throws Exception {
        //given
        Long seatingId = 1L;

        ResultActions actions = mockMvc.perform(
                get("/api/reviews/seating/{seatingId}", seatingId)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
        );

        //then
        actions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.header.message").value(OK.getMessage()))
                .andDo(document(
                        "구역별 시야 조회 성공",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        resource(ResourceSnippetParameters.builder()
                                .tag("Review API")
                                .summary("구역별 시야 조회 API")
                                .pathParameters(
                                        parameterWithName("seatingId")
                                                .description("좌석 아이디")
                                )
                                .responseFields(
                                        getCommonResponseFields(
                                                fieldWithPath("body.reviews[].reviewId").type(NUMBER)
                                                        .description("리뷰 아이디"),
                                                fieldWithPath("body.reviews[].writerNickname").type(STRING)
                                                        .description("작성자 닉네임"),
                                                fieldWithPath("body.reviews[].writerSrc").type(STRING)
                                                        .description("작성자 프로필 url"),
                                                fieldWithPath("body.reviews[].concertName").type(STRING)
                                                        .description("콘서트 이름"),
                                                fieldWithPath("body.reviews[].images").type(ARRAY)
                                                        .description("이미지 url 리스트"),
                                                fieldWithPath("body.reviews[].contents").type(STRING)
                                                        .description("리뷰 내용"),
                                                fieldWithPath("body.reviews[].createdAt").type(STRING)
                                                        .description("작성 날짜"),
                                                fieldWithPath("body.reviews[].features").type(ARRAY)
                                                        .description("특징 리스트"),
                                                fieldWithPath("body.reviews[].obstructions").type(ARRAY)
                                                        .description("방해 요소 리스트"),
                                                fieldWithPath("body.reviews[].likesCount").type(NUMBER)
                                                        .description("좋아요 개수"),
                                                fieldWithPath("body.reviews[].isLiked").type(BOOLEAN)
                                                        .description("좋아요 여부"),
                                                fieldWithPath("body.reviews[].isBookmarked").type(BOOLEAN)
                                                        .description("북마크 여부"),
                                                fieldWithPath("body.distanceMessage").type(STRING)
                                                        .description("한줄 요약"),
                                                fieldWithPath("body.thumbnails").type(ARRAY)
                                                        .description("대표 이미지 url(3장)"),
                                                fieldWithPath("body.reviewCount").type(NUMBER)
                                                        .description("리뷰 전체 개수"),
                                                fieldWithPath("body.floorName").type(NUMBER)
                                                        .description("층 이름"),
                                                fieldWithPath("body.sectionName").type(NUMBER)
                                                        .description("구역 이름"),
                                                fieldWithPath("body.seatingName").type(NUMBER)
                                                        .description("열 이름(FLOOR인 경우 null반환)")
                                        )
                                )
                                .requestSchema(Schema.schema("구역별 시야 조회 Request"))
                                .responseSchema(Schema.schema("구역별 시야 조회 Response"))
                                .build()
                        ))
                );

    }

    @Test
    public void 구역별_시야_조회_실패_리뷰가_없는_열에_대한_리뷰_조회() throws Exception {
        //given
        Long seatingId = 20L;

        ResultActions actions = mockMvc.perform(
                get("/api/reviews/seating/{seatingId}", seatingId)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
        );

        //then
        actions
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.header.message").value(NOT_FOUND.getMessage()))
                .andDo(document(
                        "구역별 시야 조회 실패 - 리뷰가 하나도 없는 열에 대한 리뷰 조회하는 경우",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        resource(ResourceSnippetParameters.builder()
                                .tag("Review API")
                                .summary("구역별 시야 조회 API")
                                .pathParameters(
                                        parameterWithName("seatingId")
                                                .description("좌석 아이디")
                                )
                                .responseFields(
                                        getCommonResponseFields(
                                                fieldWithPath("body").type(NULL)
                                                        .description("내용 없음")
                                        )
                                )
                                .requestSchema(Schema.schema("구역별 시야 조회 Request"))
                                .responseSchema(Schema.schema("구역별 시야 조회 Response"))
                                .build()
                        ))
                );

    }

    @Test
    public void 후기_전체_조회_성공() throws Exception {
        //given
        Long seatingId = 1L;
        List<String> features = List.of("1", "2");

        List<String> obstructions = List.of("2");

        Long lastReviewId = 102L;

        String sort = "createdAt";

        ResultActions actions = mockMvc.perform(
                get("/api/reviews/seating/{seatingId}/list", seatingId)
                        .param("features", features.toArray(new String[0]))
                        .param("obstructions", obstructions.toArray(new String[0]))
                        .param("lastReviewId", String.valueOf(lastReviewId))
                        .param("sort", sort)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
        );

        //then
        actions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.header.message").value(OK.getMessage()))
                .andDo(document(
                        "후기 전체 조회 성공",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        resource(ResourceSnippetParameters.builder()
                                .tag("Review API")
                                .summary("후기 전체 조회 API")
                                .pathParameters(
                                        parameterWithName("seatingId")
                                                .description("좌석 아이디")
                                )
                                .queryParameters(
                                        parameterWithName("features")
                                                .description("특징 아이디(리스트)").optional(),
                                        parameterWithName("obstructions")
                                                .description("방해물 아이디(리스트)").optional(),
                                        parameterWithName("lastReviewId")
                                                .description("마지막으로 조회한 리뷰 아이디").optional(),
                                        parameterWithName("sort")
                                                .description("정렬 기준(없는 경우 좋아요순)").optional()
                                )
                                .responseFields(
                                        getCommonResponseFields(
                                                fieldWithPath("body.reviews.content[].reviewId").type(NUMBER)
                                                        .description("리뷰 아이디"),
                                                fieldWithPath("body.reviews.content[].writerNickname").type(STRING)
                                                        .description("작성자 닉네임"),
                                                fieldWithPath("body.reviews.content[].writerSrc").type(STRING)
                                                        .description("작성자 프로필 url"),
                                                fieldWithPath("body.reviews.content[].concertName").type(STRING)
                                                        .description("콘서트 이름"),
                                                fieldWithPath("body.reviews.content[].images").type(ARRAY)
                                                        .description("이미지 url 리스트"),
                                                fieldWithPath("body.reviews.content[].contents").type(STRING)
                                                        .description("리뷰 내용"),
                                                fieldWithPath("body.reviews.content[].createdAt").type(STRING)
                                                        .description("작성 날짜"),
                                                fieldWithPath("body.reviews.content[].features").type(ARRAY)
                                                        .description("특징 리스트"),
                                                fieldWithPath("body.reviews.content[].obstructions").type(ARRAY)
                                                        .description("방해 요소 리스트"),
                                                fieldWithPath("body.reviews.content[].likesCount").type(NUMBER)
                                                        .description("좋아요 개수"),
                                                fieldWithPath("body.reviews.content[].isLiked").type(BOOLEAN)
                                                        .description("좋아요 여부"),
                                                fieldWithPath("body.reviews.content[].isBookmarked").type(BOOLEAN)
                                                        .description("북마크 여부"),
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
                                .requestSchema(Schema.schema("후기 전체 조회 Request"))
                                .responseSchema(Schema.schema("후기 전체 조회 Response"))
                                .build()
                        ))
                );

    }
}
