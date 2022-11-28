package lk.ijse.dep9.orm.entity;

import lk.ijse.dep9.orm.annotation.Id;
import lk.ijse.dep9.orm.annotation.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Table
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Customer {
    @Id
    String id;
    String name;
     private static String address;
}
