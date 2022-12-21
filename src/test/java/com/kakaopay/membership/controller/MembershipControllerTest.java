package com.kakaopay.membership.controller;

import com.kakaopay.membership.dto.RequestDTO;
import com.kakaopay.membership.utils.ResponseMessage;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import com.google.gson.*;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Random;

@SpringBootTest
@AutoConfigureMockMvc
@WebAppConfiguration
class MembershipControllerTest {
    @Autowired
    private MockMvc mockMvc; // mockMvc 생성

    @Autowired
    protected ObjectMapper objectMapper;

    @Nested
    @DisplayName("통합 바코드 발급 Test")
    class barcodeTest {
        @Test
        @DisplayName("신규 바코드 생성")
        void getNewBarcode() throws Exception {
            //userId 채번, 강제적인 난수라서 테스트 시에는 기존 ID와 중복으로 걸릴 수 있음.
            Random rand = new Random();
            String temp = Integer.toString( rand.nextInt(8) + 1);
            for (int i = 0; i < 8; i++) {
                temp+= Integer.toString(rand.nextInt(9));
            }
            int userId = Integer.parseInt(temp);

            mockMvc.perform(
                            get("/api/v1/membership/barcode")
                                    .header("userId", userId))
                    .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                    .andExpect(jsonPath("$.message").value(ResponseMessage.BARCODE_CREATED_SUCCESS))
                    .andExpect(jsonPath("$.data").isNotEmpty())
                    .andDo(print());
        }

        @Test
        @DisplayName("바코드 발급")
        void getIssuanceBarcode() throws Exception {
            int userId = 323456789;
            mockMvc.perform(
                            get("/api/v1/membership/barcode")
                                    .header("userId", userId))
                    .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                    .andExpect(jsonPath("$.message").value(ResponseMessage.BARCODE_ISSUANCE_SUCCESS))
                    .andExpect(jsonPath("$.data").isNotEmpty())
                    .andDo(print());
        }
    }

    @Nested
    @DisplayName("포인트 적립 Test")
    class earnPointTest {
        @Test
        @DisplayName("성공")
        void earnPoint() throws Exception {
            String content = objectMapper.writeValueAsString(new RequestDTO("10001", "1000", "32a1242901"));
            mockMvc.perform(
                            post("/api/v1/membership/point")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(content)
                    )
                    .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                    .andExpect(jsonPath("$.message").value(ResponseMessage.EARN_POINT_SUCCESS))
                    .andDo(print());
        }

        @Test
        @DisplayName("상점 에러")
        void earnPointStoreErr() throws Exception {
            String content = objectMapper.writeValueAsString(new RequestDTO("15001", "1000", "32a1242901"));
            mockMvc.perform(
                            post("/api/v1/membership/point")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(content)
                    )
                    .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                    .andExpect(jsonPath("$.message").value(ResponseMessage.NOT_FOUND_STORE))
                    .andDo(print());
        }

        @Test
        @DisplayName("바코드 에러")
        void earnPointBarcodeErr() throws Exception {
            String content = objectMapper.writeValueAsString(new RequestDTO("10001", "1000", "20dded01ed"));
            mockMvc.perform(
                            post("/api/v1/membership/point")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(content)
                    )
                    .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                    .andExpect(jsonPath("$.message").value(ResponseMessage.NOT_REGISTERED_BARCODE))
                    .andDo(print());
        }
    }

    @Nested
    @DisplayName("포인트 사용 Test")
    class usePointTest {
        @Test
        @DisplayName("성공")
        void usePoint() throws Exception {
            String content = objectMapper.writeValueAsString(new RequestDTO("10001", "1000", "32a1242901"));
            mockMvc.perform(
                            put("/api/v1/membership/point")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(content)
                    )
                    .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                    .andExpect(jsonPath("$.message").value(ResponseMessage.USE_POINT_SUCCESS))
                    .andDo(print());
        }

        @Test
        @DisplayName("상점 에러")
        void usePointStoreErr() throws Exception {
            String content = objectMapper.writeValueAsString(new RequestDTO("15001", "1000", "32a1242901"));
            mockMvc.perform(
                            put("/api/v1/membership/point")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(content)
                    )
                    .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                    .andExpect(jsonPath("$.message").value(ResponseMessage.NOT_FOUND_STORE))
                    .andDo(print());
        }
        @Test
        @DisplayName("바코드 에러")
        void usePointBarcodeErr() throws Exception {
            String content = objectMapper.writeValueAsString(new RequestDTO("10001", "1000", "20dded01ed"));
            mockMvc.perform(
                            put("/api/v1/membership/point")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(content)
                    )
                    .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                    .andExpect(jsonPath("$.message").value(ResponseMessage.NOT_REGISTERED_BARCODE))
                    .andDo(print());
        }

        @Test
        @DisplayName("포인트 부족 에러")
        void usePointLackErr() throws Exception {
            String content = objectMapper.writeValueAsString(new RequestDTO("10001", "9999000", "32a1242901"));
            mockMvc.perform(
                            put("/api/v1/membership/point")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(content)
                    )
                    .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                    .andExpect(jsonPath("$.message").value(ResponseMessage.LACK_OF_POINT))
                    .andDo(print());
        }
    }


    @Nested
    @DisplayName("내역 조회 Test")
    class HistoryTest{
        @Test
        @DisplayName("성공")
        void getHistory() throws Exception {
            String startDate = "2022-10-31";
            String endDate = "2022-12-31";
            String barcode = "32a1242901";
            mockMvc.perform(
                            get("/api/v1/membership/barcode/history")
                                    .param("startDate",startDate)
                                    .param("endDate",endDate)
                                    .param("barcode",barcode))
                    .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                    .andExpect(jsonPath("$.message").value(ResponseMessage.READ_BARCODE_HISTORY))
                    .andExpect(jsonPath("$.data").isNotEmpty())
                    .andDo(print());
        }

        @Test
        @DisplayName("바코드 에러")
        void getHistoryBarcodeErr() throws Exception {
            String startDate = "2022-10-31";
            String endDate = "2022-12-31";
            String barcode = "20dded01ed";
            mockMvc.perform(
                            get("/api/v1/membership/barcode/history")
                                    .param("startDate",startDate)
                                    .param("endDate",endDate)
                                    .param("barcode",barcode))
                    .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                    .andExpect(jsonPath("$.message").value(ResponseMessage.NOT_REGISTERED_BARCODE))
                    .andDo(print());
        }
    }

}