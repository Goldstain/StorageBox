package storagebox.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.RepositoryDefinition;
import org.springframework.stereotype.Repository;
import storagebox.entities.Article;

import java.util.List;

@Repository
public abstract interface ArticleRepository extends JpaRepository<Article, Integer> {

}
