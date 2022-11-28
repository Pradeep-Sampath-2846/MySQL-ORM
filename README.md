# Simple MySQL ORM for JAVA OBJECT

This is simple java object mapper for MySQL table with the help of Annotations



## Supported Data Types
- <h6>Integer</h6>
- <h6>int</h6>
- <h6>String</h6>
- <h6>Double</h6>
- <h6>double</h6>
- <h6>BigDecimal</h6>
- <h6>Date</h6>
- <h6>Time</h6>
- <h6>TimeStamp</h6>
## How to use

```python
**clone project**
$ git clone https://github.com/Pradeep-Sampath-2846/MySQL-ORM.git

$ mvn install

**add dependency for your project**
```
```

        <dependency>
            <groupId>lk.ijse.dep9</groupId>
            <artifactId>simple-orm</artifactId>
            <version>1.0.0</version>
        </dependency>
        
 ```

### Example 
```
Use '@Table' annotation for the class and 
    '@Id' annotation for the primary key attribute


@Table
public class Customer {
    @Id
    private String id;
    private String name;
    private String address;
}

Initialize the database by authenticating 

$ InitializeDB.initialize("host","port","database_name","userName","Password",...pagages to scan );

```

>**Note:**
>static fields will not be mapped into tables

## 🔑 License

[MIT](LICENSE.txt)

## Contact

> Github [@Pradeep Sampath](https://github.com/Pradeep-Sampath-2846)

> Linkedin [@Pradeep Sampath](https://www.linkedin.com/in/pradeep-sampath-2b9a07148/)