package org.example.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "purchases")
@NoArgsConstructor
@AllArgsConstructor
public class Purchases {
    @Id
    @Getter
    private int id;

    @OneToOne(targetEntity = Customers.class, fetch = FetchType.EAGER)
    @JoinColumn(name = "customer_id", referencedColumnName = "id")
    @Getter
    @Setter
    Customers customer;

    @OneToOne(targetEntity = Products.class, fetch = FetchType.EAGER)
    @JoinColumn(name = "product_id", referencedColumnName = "id")
    @Getter
    @Setter
    Products product;

    @Column(name = "value_date")
    @Getter
    @Setter
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date date;
}
