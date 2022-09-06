package org.example.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;

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
    private LocalDate date;
}
