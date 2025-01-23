package storagebox.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import storagebox.entities.Article;
import storagebox.entities.ArticleStatus;
import storagebox.entities.Category;
import storagebox.exceptions.ArticleNotFoundException;
import storagebox.exceptions.WrongValueException;
import storagebox.repositories.ArticleRepository;
import storagebox.services.impl.ArticleServiceImpl;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ArticleServiceTests {

    @Mock
    private ArticleRepository articleRepository;

    @InjectMocks
    private ArticleServiceImpl articleService;

    @Test
    public void shouldReturnAllArticlesOrderedById() {
        List<Article> mockArticles = List.of(
                new Article(new Category("Tools"), "Tool1", 100.0, 5),
                new Article(new Category("Tools"), "Tool2", 1000.0, 10),
                new Article(new Category("Tools"), "Tool3", 2000.0, 1000));

        when(articleRepository.findAllByOrderByIdAsc()).thenReturn(mockArticles);

        List<Article> articles = articleService.findAll();

        assertNotNull(articles);
        assertEquals(3, articles.size());

        Article firstArticle = articles.get(0);
        assertTrue(firstArticle.getCategory().getName().equalsIgnoreCase("Tools"));
        assertEquals("Tool1", firstArticle.getName());
        assertEquals(100, firstArticle.getPurchase());
        assertEquals(5, firstArticle.getQuantity());
    }

    @Test
    public void shouldSaveArticle() {
        Article article = new Article(new Category("Tools"), "Tool1", 100.0, 5);

        when(articleRepository.save(article)).thenReturn(article);

        Article mockArticle = articleService.save(article);

        assertNotNull(mockArticle);
        assertEquals(ArticleStatus.ON_THE_WAY, mockArticle.getStatus());
        assertTrue(mockArticle.equals(article));
    }

    @Test
    public void shouldThrowsExceptionWhenArticleNotFoundInDB() {
        when(articleRepository.findById(anyInt())).thenReturn(Optional.empty());

        assertThrows(ArticleNotFoundException.class, () -> articleService.update(999, new Article()));
    }

    @Test
    public void shouldThrowsExceptionWhenArticleHasWrongValue() {
        int id = 1;
        Article article = new Article(new Category("Tools"), "Tool1", 100.0, 5);

        when(articleRepository.findById(anyInt())).thenReturn(Optional.of(article));

        article.setSpentMoney(-10.0);
        assertThrows(WrongValueException.class, () -> articleService.update(id, article));
        article.setSpentMoney(10.0);

        article.setSoldQuantity(6);
        assertThrows(WrongValueException.class, () -> articleService.update(id, article));

        article.setSoldQuantity(-2);
        assertThrows(WrongValueException.class, () -> articleService.update(id, article));

        verify(articleRepository, times(3)).findById(id);

    }

    @Test
    public void shouldCountDataOfArticleFromDBCorrectly() throws ArticleNotFoundException, WrongValueException {
        Article articleFromDB = new Article(1, new Category(1, "Tools"), "Tool"
                , 500.0, 2000.0, 50.0, 950, 3, 2
                , LocalDate.now(), ArticleStatus.IN_STOCK, "http:/url");
        when(articleRepository.findById(anyInt())).thenReturn(Optional.of(articleFromDB));

        Article articleFromView = new Article(1, new Category(1, "Tools"), "Tool"
                , 500.0, 3000.0, 100.0, 950, 3, 3
                , LocalDate.now(), ArticleStatus.IN_STOCK, "http:/url");

        articleService.update(1, articleFromView);

        assertEquals(1, articleFromDB.getId());
        assertEquals("Tools", articleFromDB.getCategory().getName());
        assertEquals("Tool", articleFromDB.getName());
        assertEquals(500.0, articleFromDB.getPurchase(), 1e-3);
        assertEquals(3000.0, articleFromDB.getSellingPrize(), 1e-3);
        assertEquals(100.0, articleFromDB.getSpentMoney(), 1e-3);
        assertEquals(1400.0, articleFromDB.getProfit(), 1e-3);
        assertEquals(3, articleFromDB.getQuantity());
        assertEquals(3, articleFromDB.getSoldQuantity());
        assertEquals(0, articleFromDB.getRemainder());
        assertEquals(ArticleStatus.OUT_OF_STOCK, articleFromDB.getStatus());

        verify(articleRepository).save(articleFromDB);
    }

    @Test
    public void shouldThrowsExceptionIfArticleNotfoundInDB() throws ArticleNotFoundException {
        when(articleRepository.findById(anyInt())).thenReturn(Optional.empty());

        assertThrows(ArticleNotFoundException.class, () -> articleService.delete(anyInt()));
    }
}
