package com.makolyte.springsnsstarter.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.makolyte.springsnsstarter.model.EventType;
import com.makolyte.springsnsstarter.model.Message;
import com.makolyte.springsnsstarter.model.SnsResponse;
import com.makolyte.springsnsstarter.service.MessagePublisher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.refEq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class MessageControllerTest {

    private MockMvc mockMvc;
    private ObjectMapper mapper;
    private Message message;
    private String content;

    @Mock
    private MessagePublisher messagePublisher;

    @BeforeEach
    void setup() throws Exception {
        mockMvc = MockMvcBuilders
                .standaloneSetup(new MessageController(messagePublisher))
                .build();

        mapper = new ObjectMapper();
        message = new Message("Books",
                "Keep Sharp: Build a Better Brain at Any Age",
                EventType.INCREASE,
                "Amazon",
                new BigDecimal("16.80"));
        content = mapper.writeValueAsString(message);
    }

    @Test
    void publish() throws Exception {
        SnsResponse snsResponse = new SnsResponse(200, "Success", "123-456");

        when(messagePublisher.publish(refEq(message))).thenReturn(snsResponse);

        mockMvc.perform(post("/publish")
                .content(content)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.statusCode").value(200))
                .andExpect(jsonPath("$.message").value("Success"))
                .andExpect(jsonPath("$.publishedMessageId").value("123-456"));
    }

    @Test
    void publish_exception_throwsInternalServerError() throws Exception {
        SnsResponse snsResponse = new SnsResponse(500, "Incorrect secret key", null);
        String expectedResponse = "SnsResponse{statusCode=500, message='Incorrect secret key', publishedMessageId='null'}";
        RuntimeException exception = new RuntimeException(snsResponse.toString());

        when(messagePublisher.publish(refEq(message))).thenThrow(exception);

        MvcResult result = mockMvc.perform(post("/publish")
                .content(content)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError())
                .andReturn();

        assertThat(result.getResponse().getContentAsString()).isEqualTo(expectedResponse);
    }
}