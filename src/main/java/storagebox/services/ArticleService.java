package storagebox.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import storagebox.entities.Article;
import storagebox.entities.ArticleStatus;
import storagebox.exceptions.ArticleNotFoundException;
import storagebox.exceptions.WrongValueException;
import storagebox.repositories.ArticleRepository;

import java.util.List;


public interface ArticleService {

    public List<Article> findAll();


    public List<Article> findAll(ArticleStatus status);

    public Article save(Article article) ;

    public Article findById(int id) throws ArticleNotFoundException ;

    public void update(int id, Article articleFromView) throws ArticleNotFoundException, WrongValueException ;


    public void delete(int id) throws ArticleNotFoundException ;
}



