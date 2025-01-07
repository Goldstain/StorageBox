package storagebox.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import storagebox.entities.Article;
import storagebox.repositories.AdminPanelRepository;
import storagebox.services.AdminPanelService;
import storagebox.dto.ArticlesGroupingByName;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class AdminPanelServiceImpl implements AdminPanelService {

    private final AdminPanelRepository adminPanelRepository;

    @Autowired
    public AdminPanelServiceImpl(AdminPanelRepository adminPanelRepository) {
        this.adminPanelRepository = adminPanelRepository;
    }


    public List<Article> findAllArticles() {
        return adminPanelRepository.findAll();
    }

    @Override
    public List<ArticlesGroupingByName> findAllArticles(String sortField, String sortDirection) {
        List<Article> articles = findAllArticles();
        if (articles.isEmpty()) {
            return new ArrayList<>();
        }

        List<ArticlesGroupingByName> groupingByName = getGroupingByName(articles);

        Comparator<ArticlesGroupingByName> comparator = getComparator(sortField);
        if ("desc".equalsIgnoreCase(sortDirection)) {
            comparator = comparator.reversed();
        }
        groupingByName.sort(comparator);

        return groupingByName;
    }


    private static List<ArticlesGroupingByName> getGroupingByName(List<Article> articles) {
        List<ArticlesGroupingByName> articlesGroupingByNames = new ArrayList<>();

        Map<String, List<Article>> groupByNameArticles = articles
                .stream()
                .collect(Collectors.groupingBy(article -> article.getName()));

        for (Map.Entry<String, List<Article>> entry : groupByNameArticles.entrySet()) {
            double sellingPrize = 0.0;
            int marginality = 0;
            double profit = 0.0;
            int quantity = 0;
            int soldQuantity = 0;
            int remainder = 0;

            for (Article article : entry.getValue()) {
                sellingPrize += article.getSellingPrize();
                profit += article.getProfit();
                quantity += article.getQuantity();
                soldQuantity += article.getSoldQuantity();
                remainder += article.getRemainder();
            }

            if (sellingPrize > 0) {
                marginality = (int) Math.round((profit / sellingPrize) * 100);
            }

            ArticlesGroupingByName articlesGroupingByName = new ArticlesGroupingByName(
                    entry.getKey(),
                    marginality,
                    profit,
                    quantity,
                    soldQuantity,
                    remainder
            );
            articlesGroupingByNames.add(articlesGroupingByName);
        }
        return articlesGroupingByNames;
    }


    private Comparator<ArticlesGroupingByName> getComparator(String sortField) {
        switch (sortField) {
            case "name":
                return Comparator.comparing(ArticlesGroupingByName::getName, String.CASE_INSENSITIVE_ORDER);
            case "marginality":
                return Comparator.comparing(ArticlesGroupingByName::getMarginality);
            case "profit":
                return Comparator.comparing(ArticlesGroupingByName::getProfit);
            case "quantity":
                return Comparator.comparing(ArticlesGroupingByName::getQuantity);
            case "soldQuantity":
                return Comparator.comparing(ArticlesGroupingByName::getSoldQuantity);
            case "remainder":
                return Comparator.comparing(ArticlesGroupingByName::getRemainder);
            default:
                return Comparator.comparing(ArticlesGroupingByName::getProfit).reversed();
        }
    }

    public ArticlesGroupingByName getTotal() {

        List<ArticlesGroupingByName> groupingByName = getGroupingByName(findAllArticles());
        ArticlesGroupingByName total = new ArticlesGroupingByName();
        int totalMarginarity = 0;
        int numbersOfMarginarity = 0;
        for (ArticlesGroupingByName articles : groupingByName) {
            total.setProfit(total.getProfit() + articles.getProfit());
            total.setQuantity(total.getQuantity() + articles.getQuantity());
            total.setSoldQuantity(total.getSoldQuantity() + articles.getSoldQuantity());
            total.setRemainder(total.getRemainder() + articles.getRemainder());

            totalMarginarity += articles.getMarginality();
            if (articles.getSoldQuantity() > 0) {
                numbersOfMarginarity++;
            }
        }
        if (numbersOfMarginarity > 0) {
            total.setMarginality(totalMarginarity / numbersOfMarginarity);
        }
        return total;
    }

}

