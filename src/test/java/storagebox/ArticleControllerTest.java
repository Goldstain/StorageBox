//package storagebox;
//
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.test.web.servlet.MockMvc;
//import storagebox.controllers.ArticleController;
//import storagebox.entities.Category;
//import storagebox.services.ArticleService;
//import storagebox.services.CategoryService;
//
//import static org.hamcrest.Matchers.containsString;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
//@WebMvcTest(ArticleController.class)
//public class ArticleControllerTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @MockBean
//    private ArticleService articleService;
//
//    @MockBean
//    private CategoryService categoryService;
//
//    @Test
//    public void testNewArticlePage() throws Exception {
//        mockMvc.perform(get("/articles/new-article"))
//                .andExpect(status().isOk())
//                .andExpect(view().name("new-article"))
//                .andExpect(content().string(
//                        containsString("Додати новий товар")));
//    }
//
//    @Test
//    public void testArticlePage() throws Exception {
//        mockMvc.perform(get("/articles"))
//                .andExpect(status().isOk())
//                .andExpect(view().name("articles"));
//    }
//}
