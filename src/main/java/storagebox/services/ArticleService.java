package storagebox.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import storagebox.entities.Article;
import storagebox.entities.ArticleStatus;
import storagebox.exceptions.ArticleNotFoundException;
import storagebox.repositories.ArticleRepository;

import java.util.List;

@Service
public class ArticleService {

    private final ArticleRepository articleRepository;

    @Autowired
    public ArticleService(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

    public List<Article> findAll() {
        return articleRepository.findAllByOrderByIdAsc();
    }

    public List<Article> findAll(ArticleStatus status) {
        return articleRepository.findAllByOrderByIdAsc().stream()
                .filter((article -> article.getStatus().equals(status))).toList();
    }


    public Article save(Article article) {
        article.setStatus(ArticleStatus.ON_THE_WAY);
        return articleRepository.save(article);
    }

    public Article findById(int id) throws ArticleNotFoundException {
        return articleRepository.findById(id).orElseThrow(
                () -> new ArticleNotFoundException("Товар не знайдено"));
    }

    @Transactional
    public void update(int id, Article articleForEdit) throws ArticleNotFoundException {
        Article articleFromDB = articleRepository.findById(id).orElseThrow(() -> new ArticleNotFoundException(
                "Товар не знайдено"));

        articleFromDB.setName(articleForEdit.getName());
        articleFromDB.setPurchase(articleForEdit.getPurchase());
        articleFromDB.setSellingPrize(articleForEdit.getSellingPrize());
        articleFromDB.setSpentMoney(articleForEdit.getSpentMoney());
        articleFromDB.setQuantity(articleForEdit.getQuantity());
        articleFromDB.setSoldQuantity(articleForEdit.getSoldQuantity());
        articleFromDB.setProfit(articleForEdit.getProfit());
        articleFromDB.setRemainder(articleForEdit.getRemainder());
        if (articleForEdit.getQuantity() - articleForEdit.getSoldQuantity() == 0) {
            articleFromDB.setStatus(ArticleStatus.OUT_OF_STOCK);
        } else {
            articleFromDB.setStatus(articleForEdit.getStatus());
        }
        articleRepository.save(articleFromDB);
    }


    public void delete(int id) throws ArticleNotFoundException {
        Article articleForDelete = articleRepository.findById(id)
                .orElseThrow(() -> new ArticleNotFoundException("Товар не знайдено"));
        articleRepository.delete(articleForDelete);
    }
}
