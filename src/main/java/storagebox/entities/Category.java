package storagebox.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.UniqueElements;

import java.util.List;

@Entity
@Table(name = "category")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id")
    private int id;

    @Column(name = "category", unique = true)
    @Size(min = 2, max = 25, message = "Назва має бути в діапазоні 2-25 символів")
    private String name;


    public Category() {
    }


    public Category(String category) {
        this.name = category.toUpperCase();
    }

    public Category(int id, String category) {
        this.id = id;
        this.name = category;
    }


    public int getId() {
        return id;
    }

    public void setId(int category_id) {
        this.id = category_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name.toUpperCase();
    }



}
