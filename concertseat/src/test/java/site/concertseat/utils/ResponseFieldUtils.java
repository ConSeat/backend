package site.concertseat.utils;

import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.payload.JsonFieldType;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;

public class ResponseFieldUtils {
    public static List<FieldDescriptor> getCommonResponseFields(FieldDescriptor... additionalFields) {
        List<FieldDescriptor> responseFields = new ArrayList<>();

        responseFields.add(fieldWithPath("header.message").type(JsonFieldType.STRING).description("응답 메시지"));

        if (additionalFields != null && additionalFields.length > 0) {
            responseFields.addAll(List.of(additionalFields));
        }
        return responseFields;
    }
}