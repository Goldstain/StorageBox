package storagebox.services;

import storagebox.services.impl.models.ArticlesGroupingByName;

import java.util.List;

public interface AdminPanelService {

    public List<ArticlesGroupingByName> findAllArticles(String sortField, String sortDirection);

    public ArticlesGroupingByName getTotal();
}
