package storagebox.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import storagebox.entities.Article;
import storagebox.entities.ArticleStatus;

import java.util.List;

@Repository
public abstract interface ArticleRepository extends JpaRepository<Article, Integer> {

    List<Article> findAllByOrderByIdAsc();


}
