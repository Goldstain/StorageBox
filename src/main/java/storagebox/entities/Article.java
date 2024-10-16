package storagebox.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

@Entity
@Table(name = "article")
public class Article {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "category")
    @Enumerated(EnumType.STRING)
    @NotNull(message = "Виберіть категорію, або спочатку створіть нову")
    private Category category;

    @Column(name="name")
    @Size(min = 3, max = 30, message = "Назва має бути розміром від 3 до 30 символів")
    @NotNull
    private String name;

    @Column(name = "purchase")
    @Min(value = 0, message = "Ціна не може бути менше 0")
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
    @Min(value = -1_000_000, message = "Перевірте правильність зазначеного прибутку")
    @Max(value = 1_000_000, message = "Перевірте чи правильно введено прибуток")
    private double profit;

    @Column(name = "quantity")
    @Min(value = 1, message = "Не може бути менше 1")
    @Max(value = 1000, message = "Скільки, скільки?! А якщо серйозно")
    private int quantity;

    @Column(name = "sold_quantity")
    @Min(value = 0, message = "Кількість проданого не може бути менше 0")
    @Max(value = 1000, message = "Давай вірну кількість, курвисько!")
    private int soldQuantity;

    @Column(name = "created_date", updatable = false)
    private LocalDateTime createdDate;

    public Article() {
    }

    public Article(Category category, String name, double purchase, int quantity) {
        this.category = category;
        this.name = name;
        this.purchase = purchase;
        sellingPrize = 0;
        spentMoney = 0;
        profit =0;
        this.quantity = quantity;
        soldQuantity = 0;
        createdDate = LocalDateTime.now();
    }

    public Article(int id, Category category, String name, double purchase,
                   double sellingPrize, double spentMoney, double profit,
                   int quantity, int soldQuantity, LocalDateTime createdDate) {
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
    }


    @PrePersist
    protected void onCreate() {
        createdDate = LocalDateTime.now();
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

    public void setPurchase(  double purchase) {
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

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
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
                ", createdDate=" + createdDate +
                '}';
    }
}
