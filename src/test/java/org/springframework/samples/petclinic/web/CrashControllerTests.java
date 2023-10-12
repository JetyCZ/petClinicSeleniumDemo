package org.springframework.samples.petclinic.web;

import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.samples.petclinic.PetClinicApplication;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver;
import spock.lang.Ignore;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for {@link CrashController}
 *
 * @author Colin But
 */
@SpringBootTest(classes = PetClinicApplication.class)
@WebAppConfiguration
// Waiting https://github.com/spring-projects/spring-boot/issues/5574
@Ignore
public class CrashControllerTests {

    @Autowired
    private CrashController crashController;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        SimpleMappingExceptionResolver exceptionResolver = new SimpleMappingExceptionResolver();
        exceptionResolver.setDefaultErrorView("exception");
        this.mockMvc = MockMvcBuilders
            .standaloneSetup(crashController)
            .setHandlerExceptionResolvers(exceptionResolver)
            .build();
    }

    @Test
    void testTriggerException() throws Exception {
        mockMvc.perform(get("/oups"))
            .andExpect(view().name("exception"))
            .andExpect(model().attributeExists("exception"))
            .andExpect(forwardedUrl("exception"))
            .andExpect(status().isOk());
    }
}
