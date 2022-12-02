package com.Ecomm.Ecommerce.entities;

import com.Ecomm.Ecommerce.utils.Auditable;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;

@Entity
@Getter
@Setter
@SQLDelete(sql = "UPDATE product SET is_deleted = true WHERE id=?")
@Where(clause = "is_deleted=false")
public class Product extends Auditable<String> {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "product_gen")
    @SequenceGenerator(name="product_gen", sequenceName = "product_seq", initialValue = 1, allocationSize = 1)
    private long id;


    @ManyToOne
    @JoinColumn(name = "seller_id")
    private Seller seller;

    private String name;

    private String description;

    @OneToOne
    @JoinColumn(name = "category_id")
    private Category category;
    private boolean isCancellable ;
    private boolean isReturnable;



    private String brand;

    private boolean isActive;
    private boolean isDeleted;

}
