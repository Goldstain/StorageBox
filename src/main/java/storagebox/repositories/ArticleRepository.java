package storagebox.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import storagebox.entities.Article;

import java.util.List;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Integer> {

    List<Article> findAllByOrderByIdAsc();

    List<Article> findAllByNameContainingIgnoreCaseOrderByIdAsc(String name);


}
