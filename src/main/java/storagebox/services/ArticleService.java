package storagebox.services;

import storagebox.entities.Article;
import storagebox.entities.ArticleStatus;
import storagebox.exceptions.ArticleNotFoundException;
import storagebox.exceptions.WrongValueException;

import java.util.List;


public interface ArticleService {

    public List<Article> findAll();

    public List<Article> findAllByStatus(ArticleStatus status);

    public List<Article> findAllByName(String name);

    public Article save(Article article) ;

    public Article findById(int id) throws ArticleNotFoundException ;

    public void update(int id, Article articleFromView) throws ArticleNotFoundException, WrongValueException ;

    public void delete(int id) throws ArticleNotFoundException ;

}



