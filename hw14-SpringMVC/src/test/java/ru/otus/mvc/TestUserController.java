package ru.otus.mvc;

import org.hibernate.SessionFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import ru.otus.configurations.AppConfig;
import ru.otus.controllers.UserController;
import ru.otus.core.dao.UserDao;
import ru.otus.core.model.User;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.MediaType.APPLICATION_FORM_URLENCODED;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebAppConfiguration
@ContextConfiguration(classes = {AppConfig.class})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TestUserController {

    // https://stackoverflow.com/questions/16150908/testing-spring-context-without-xml
    // http://zetcode.com/spring/mockmvc/

    @Autowired
    protected WebApplicationContext context;

    protected MockMvc mockMvc;

    @Autowired
    SessionFactory sessionFactory;

    @Autowired
    UserDao userDao;

    @BeforeAll
    public void setup() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(new UserController(userDao)).build();
//        this.mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @Test
    public void infrastructureShouldBeAutowired() {
        assertThat(sessionFactory).isNotNull();
        assertThat(userDao).isNotNull();
    }

    @Test
    public void testMainPage() throws Exception {
        this.mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("userList"))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void testEditPage() throws Exception {
        this.mockMvc.perform(get("/user/edit/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("userForm"))
                .andExpect(model().attributeExists("user"))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void testCancelEdit() throws Exception {
        this.mockMvc.perform(post("/user/edit").param("action", "cancel"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/user/list"))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    // https://www.petrikainulainen.net/programming/spring-framework/integration-testing-of-spring-mvc-applications-forms/
    public void testSubmitEdit() throws Exception {
        User user = new User(1, "user100", 100, "user100", "user100");
        // TODO: status 400
        this.mockMvc.perform(
                post("/user/edit")
                        .contentType(APPLICATION_FORM_URLENCODED)
                        .sessionAttr("user", user)
        )
        .andExpect(status().is4xxClientError());
    }
}
