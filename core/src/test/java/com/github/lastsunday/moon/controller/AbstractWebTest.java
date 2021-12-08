package com.github.lastsunday.moon.controller;

import com.github.lastsunday.moon.config.AppConfig;
import org.junit.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.mock.http.MockHttpInputMessage;
import org.springframework.mock.http.MockHttpOutputMessage;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.web.context.WebApplicationContext;
import redis.embedded.RedisServer;

import java.io.IOException;
import java.util.Arrays;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

public abstract class AbstractWebTest {

    protected Logger log = LoggerFactory.getLogger(AbstractWebTest.class);

    @Autowired
    protected AppConfig appConfig;

    @SuppressWarnings("rawtypes")
    private HttpMessageConverter mappingJackson2HttpMessageConverter;

    @SuppressWarnings("rawtypes")
    private HttpMessageConverter stringHttpMessageConverter;

    @Autowired
    private WebApplicationContext webApplicationContext;

    protected MediaType contentType = MediaType.APPLICATION_JSON;

    protected String token;

    protected MockMvc mockMvc;

    private static RedisServer redisServer;

    @Autowired
    void setConverters(HttpMessageConverter<?>[] converters) {

        this.mappingJackson2HttpMessageConverter = Arrays.stream(converters)
                .filter(hmc -> hmc instanceof MappingJackson2HttpMessageConverter)
                .findAny()
                .get();

        this.stringHttpMessageConverter = Arrays.stream(converters)
                .filter(hmc -> hmc instanceof StringHttpMessageConverter)
                .findAny()
                .get();

        Assert.assertNotNull("the JSON message converter must not be null",
                this.mappingJackson2HttpMessageConverter);
    }

    @BeforeClass
    public static void beforeClass() throws Exception {
        redisServer = new RedisServer(26379);
        redisServer.start();
    }

    @AfterClass
    public static void afterClass() {
        redisServer.stop();
    }

    @Before
    public void setup() throws Exception {
        log.info("Executing setup");
        if (this.mockMvc == null) {
            this.mockMvc = webAppContextSetup(webApplicationContext)
                    .apply(springSecurity()).build();
        }
    }

    @After
    public void teardown() throws Exception {
        log.info("Executing teardown");
        log.info("Executed teardown");
    }

    protected <T> ResultActions doPost(String urlTemplate) throws Exception {
        MockHttpServletRequestBuilder postRequest = post(urlTemplate);
        setJwtToken(postRequest);
        return mockMvc.perform(postRequest);
    }

    protected <T> ResultActions doPost(String urlTemplate, T content) throws Exception {
        MockHttpServletRequestBuilder postRequest = post(urlTemplate);
        setJwtToken(postRequest);
        String json = json(content);
        postRequest.contentType(contentType).content(json);
        return mockMvc.perform(postRequest);
    }

    protected void setJwtToken(MockHttpServletRequestBuilder request) {
        if (this.token != null) {
            request.header(appConfig.getService().getToken().getHeader(),this.token);
        }
    }

    @SuppressWarnings("unchecked")
    protected String json(Object o) throws IOException {
        MockHttpOutputMessage mockHttpOutputMessage = new MockHttpOutputMessage();

        HttpMessageConverter converter = o instanceof String ? stringHttpMessageConverter : mappingJackson2HttpMessageConverter;
        converter.write(o, MediaType.APPLICATION_JSON, mockHttpOutputMessage);
        return mockHttpOutputMessage.getBodyAsString();
    }

    protected <T> T readResponse(ResultActions result, Class<T> responseClass) throws Exception {
        byte[] content = result.andReturn().getResponse().getContentAsByteArray();
        MockHttpInputMessage mockHttpInputMessage = new MockHttpInputMessage(content);
        HttpMessageConverter converter = responseClass.equals(String.class) ? stringHttpMessageConverter : mappingJackson2HttpMessageConverter;
        return (T) converter.read(responseClass, mockHttpInputMessage);
    }
}
