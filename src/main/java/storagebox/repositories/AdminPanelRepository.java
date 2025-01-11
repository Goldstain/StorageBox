package storagebox.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import storagebox.entities.Article;

@Repository
public interface AdminPanelRepository extends JpaRepository<Article, Integer> {


}
