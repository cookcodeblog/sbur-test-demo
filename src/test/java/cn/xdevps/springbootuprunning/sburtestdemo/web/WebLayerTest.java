package cn.xdevps.springbootuprunning.sburtestdemo.web;

import cn.xdevps.springbootuprunning.sburtestdemo.web.HomeController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * The test assertion is the same as in the previous case.
 *
 * However, in this test, Spring Boot instantiates only the web layer rather than the whole context.
 *
 * In an application with multiple controllers, you can even ask for only one to be instantiated by using,
 * for example, @WebMvcTest(HomeController.class).
 */
@WebMvcTest(HomeController.class)
public class WebLayerTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    public void shouldReturnDefaultMessage() throws Exception {
        this.mockMvc.perform(get("/"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Hello, World")));
    }

}
