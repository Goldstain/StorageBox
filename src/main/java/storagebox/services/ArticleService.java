package storagebox.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import storagebox.entities.Article;
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

    public Article save(Article article) {
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

        articleRepository.save(articleFromDB);
    }


    public void delete(int id) throws ArticleNotFoundException {
        Article articleForDelete = articleRepository.findById(id)
                .orElseThrow(() -> new ArticleNotFoundException("Товар не знайдено"));
        articleRepository.delete(articleForDelete);
    }
}
