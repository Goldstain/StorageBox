package storagebox.services.impl.models;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ArticlesGroupingByName {

    private String name;
    private int marginality;
    private double profit;
    private int quantity;
    private int soldQuantity;
    private int remainder;


    public ArticlesGroupingByName(String name, int marginality, double profit, int quantity
            , int soldQuantity, int remainder) {
        this.name = name;
        this.marginality = marginality;
        this.profit = profit;
        this.quantity = quantity;
        this.soldQuantity = soldQuantity;
        this.remainder = remainder;
    }
}
