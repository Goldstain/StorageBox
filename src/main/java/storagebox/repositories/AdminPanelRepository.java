package storagebox.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import storagebox.entities.Article;

public interface AdminPanelRepository extends JpaRepository<Article, Integer> {


}
