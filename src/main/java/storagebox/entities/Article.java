package storagebox.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "article")
public class Article {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "article_id")
    private int id;


    @ManyToOne()
    @JoinColumn(name = "category_id")
    @NotNull(message = "Виберіть або створіть нову категорію")
    private Category category;

    @Column(name = "name")
    @Size(min = 3, max = 30, message = "Назва має бути розміром від 3 до 30 символів")
    @NotNull
    private String name;

    @Column(name = "purchase")
    @Min(value = 1, message = "Ціна не може бути менше 0")
    @Max(value = 1_000_000, message = "Перевірте чи правильно введена ціна")
    private double purchase;

    @Column(name = "selling_prize")
    @Min(value = 0, message = "Ціна продажу не може бути менше 0")
    @Max(value = 1_000_000, message = "Перевірте чи правильно введена ціна")
    private double sellingPrize;

    @Column(name = "spent_money")
    @Min(value = 0, message = "Витрати не можуть бути менше 0")
    @Max(value = 1_000_000, message = "Перевірте чи правильно введена сума")
    private double spentMoney;

    @Column(name = "profit")
    private double profit;

    @Column(name = "quantity")
    @Min(value = 1, message = "Кількість не може бути менше 1")
    @Max(value = 1000, message = "Скільки, скільки?! А якщо серйозно")
    private int quantity;

    @Column(name = "sold_quantity")
    @Min(value = 0, message = "Кількість проданого не може бути менше 1")
    @Max(value = 1000, message = "Давай вірну кількість, курвисько!")
    private int soldQuantity;

    @Column(name = "remainder")
    private int remainder;

    @Column(name = "created_date", updatable = false)
    private LocalDate createdDate;

    @Enumerated(EnumType.STRING)
    private ArticleStatus status;


    public Article() {
    }

    public Article(Category category, String name, double purchase, int quantity) {
        this.category = category;
        this.name = name;
        this.purchase = purchase;
        sellingPrize = 0;
        spentMoney = 0;
        profit = sellingPrize - purchase * soldQuantity - spentMoney;
        remainder = quantity - soldQuantity;
        this.quantity = quantity;
        soldQuantity = 0;
        status = ArticleStatus.ON_THE_WAY;
    }

    public Article(int id, Category category, String name, double purchase,
                   double sellingPrize, double spentMoney, double profit,
                   int quantity, int soldQuantity, LocalDate createdDate, ArticleStatus status) {
        this.id = id;
        this.category = category;
        this.name = name;
        this.purchase = purchase;
        this.sellingPrize = sellingPrize;
        this.spentMoney = spentMoney;
        this.profit = profit;
        this.quantity = quantity;
        this.soldQuantity = soldQuantity;
        this.createdDate = createdDate;
        this.status = status;
    }


    @PrePersist
    protected void onCreate() {
        createdDate = LocalDate.now();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPurchase() {
        return purchase;
    }

    public void setPurchase(double purchase) {
        this.purchase = purchase;
    }


    public double getSellingPrize() {
        return sellingPrize;
    }

    public void setSellingPrize(double sellingPrize) {
        this.sellingPrize = sellingPrize;
    }

    public double getSpentMoney() {
        return spentMoney;
    }

    public void setSpentMoney(double spentMoney) {
        this.spentMoney = spentMoney;
    }

    public double getProfit() {
        return profit;
    }

    public void setProfit(double profit) {
        this.profit = profit;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getSoldQuantity() {
        return soldQuantity;
    }

    public void setSoldQuantity(int soldQuantity) {
        this.soldQuantity = soldQuantity;
    }

    public LocalDate getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDate createdDate) {
        this.createdDate = createdDate;
    }

    public int getRemainder() {
        return remainder;
    }

    public void setRemainder(int remainder) {
        this.remainder = remainder;
    }

    public ArticleStatus getStatus() {
        return status;
    }

    public void setStatus(ArticleStatus status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Article{" +
                "id=" + id +
                ", category=" + category +
                ", name='" + name + '\'' +
                ", purchase=" + purchase +
                ", sellingPrize=" + sellingPrize +
                ", spentMoney=" + spentMoney +
                ", profit=" + profit +
                ", quantity=" + quantity +
                ", soldQuantity=" + soldQuantity +
                ", remainder=" + remainder +
                ", createdDate=" + createdDate +
                ", status=" + status +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Article article = (Article) o;
        return id == article.id && Double.compare(purchase, article.purchase) == 0
                && Double.compare(sellingPrize, article.sellingPrize) == 0
                && Double.compare(spentMoney, article.spentMoney) == 0
                && Double.compare(profit, article.profit) == 0
                && quantity == article.quantity
                && soldQuantity == article.soldQuantity
                && remainder == article.remainder
                && Objects.equals(category, article.category)
                && Objects.equals(name, article.name)
                && Objects.equals(createdDate, article.createdDate)
                && status == article.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, category, name, purchase, sellingPrize, spentMoney
                , profit, quantity, soldQuantity, remainder, createdDate, status);
    }
}
