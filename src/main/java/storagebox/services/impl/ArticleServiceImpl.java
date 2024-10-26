package storagebox.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import storagebox.entities.Article;
import storagebox.entities.ArticleStatus;
import storagebox.exceptions.ArticleNotFoundException;
import storagebox.exceptions.WrongValueException;
import storagebox.repositories.ArticleRepository;
import storagebox.services.ArticleService;

import java.util.List;

@Service
public class ArticleServiceImpl implements ArticleService {

    private final ArticleRepository articleRepository;

    @Autowired
    public ArticleServiceImpl(ArticleRepository articleRepository) {
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
    public void update(int id, Article articleFromView) throws ArticleNotFoundException, WrongValueException {
        Article articleFromDB = articleRepository.findById(id).orElseThrow(() -> new ArticleNotFoundException(
                "Article has not been found"));
        if (articleFromView.getSpentMoney() < 0
                || articleFromView.getQuantity() < articleFromView.getSoldQuantity()
                || articleFromView.getSoldQuantity() < 0) {
            throw new WrongValueException("Wrong value in field");
        }
        articleFromDB.setName(articleFromView.getName());
        articleFromDB.setPurchase(articleFromView.getPurchase());
        articleFromDB.setSellingPrize(articleFromView.getSellingPrize());
        articleFromDB.setSpentMoney(articleFromView.getSpentMoney());
        articleFromDB.setQuantity(articleFromView.getQuantity());
        articleFromDB.setSoldQuantity(articleFromView.getSoldQuantity());
        articleFromDB.setProfit(articleFromView.getSellingPrize() - articleFromView.getSpentMoney()
                - articleFromView.getPurchase() * articleFromView.getSoldQuantity());
        articleFromDB.setRemainder(articleFromView.getQuantity() - articleFromView.getSoldQuantity());
        if (articleFromView.getQuantity() - articleFromView.getSoldQuantity() == 0) {
            articleFromDB.setStatus(ArticleStatus.OUT_OF_STOCK);
        } else if (articleFromView.getSoldQuantity() > 0) {
            articleFromDB.setStatus(ArticleStatus.IN_STOCK);
        } else {
            articleFromDB.setStatus(articleFromView.getStatus());
        }
        articleRepository.save(articleFromDB);
    }


    public void delete(int id) throws ArticleNotFoundException {
        Article articleForDelete = articleRepository.findById(id)
                .orElseThrow(() -> new ArticleNotFoundException("Товар не знайдено"));
        articleRepository.delete(articleForDelete);
    }
}



