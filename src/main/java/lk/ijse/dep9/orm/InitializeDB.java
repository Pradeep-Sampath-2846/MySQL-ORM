package lk.ijse.dep9.orm;

import lk.ijse.dep9.orm.annotation.Id;
import lk.ijse.dep9.orm.annotation.Table;

import java.io.File;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.*;
import java.util.*;
import java.util.Date;
import java.util.stream.Collectors;

public class InitializeDB {
    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
    public static void initialize(String host,
                                  String port,
                                  String database,
                                  String userName,
                                  String password,
                                  String ... packagesToScan) throws ClassNotFoundException, SQLException {
        String url="jdbc:mysql://%s:%s/%s?createDatabaseIfNotExist=true";
         url = String.format(url,host,port, database);

         Connection connection;

        try {
            connection = DriverManager.getConnection(url,userName,password);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        List<String > classNames=new ArrayList<>();

        for (String packageToScan : packagesToScan) {
            System.out.println(packageToScan);
            var packageName=packageToScan;

            packageToScan = packageToScan.replaceAll("[.]", "/");
            System.out.println(packageToScan);
            URL resource = InitializeDB.class.getResource("/" + packageToScan);
            try {
                File file = new File(resource.toURI());
                classNames.addAll(Arrays.asList(file.list()).stream().map(name->packageName+"."+name.replace(".class",""))
                        .collect(Collectors.toList()));

            } catch (URISyntaxException e) {
                throw new RuntimeException(e);
            }

            for (String className : classNames) {
                Class<?> loadedClass = Class.forName(className);
                Table tableAnnotation = loadedClass.getDeclaredAnnotation(Table.class);
                if (tableAnnotation!=null){
                    createTable(loadedClass,connection);
                    System.out.println("=====================");
                }else {
                    System.out.println("no need to create a table for this class"+ loadedClass.getName());
                }

            }
        }



    }

    public static void createTable(Class<?> classObj,Connection connection) throws SQLException {
        Map<Class<?>,String> supportedDataTypes=new HashMap<>();
        supportedDataTypes.put(int.class,"INT");
        supportedDataTypes.put(String.class,"VARCHAR(256)");
        supportedDataTypes.put(Integer.class,"INT");
        supportedDataTypes.put(Double.class,"DOUBLE(10,2)");
        supportedDataTypes.put(double.class,"DOUBLE(10,2)");
        supportedDataTypes.put(BigDecimal.class,"DECIMAL(10,2)");
        supportedDataTypes.put(Date.class,"DATE");
        supportedDataTypes.put(Time.class,"TIME");
        supportedDataTypes.put(Timestamp.class,"DATETIME");

        StringBuilder ddlBuilder = new StringBuilder();

        ddlBuilder.append("CREATE TABLE IF NOT EXISTS `")
                        .append(classObj.getSimpleName()).append("`(");





        Field[] fields = classObj.getDeclaredFields();
        for (Field field : fields) {
            String name = field.getName();
            Class<?> dataType = field.getType();
            Id primaryKey=field.getDeclaredAnnotation(Id.class);

            if (Modifier.isStatic(field.getModifiers())) continue;
            if (!supportedDataTypes.containsKey(dataType)) throw new RuntimeException("we don't support"+dataType+"yet");

            ddlBuilder.append("`").append(name).append("`").append(" ")
                    .append(supportedDataTypes.get(dataType));

            ddlBuilder=(primaryKey!=null)?ddlBuilder.append(" PRIMARY KEY,"):ddlBuilder.append(",");

        }
        ddlBuilder.deleteCharAt(ddlBuilder.length()-1).append(")");

        connection.createStatement().execute(ddlBuilder.toString());
        System.out.println(ddlBuilder);


    }
}
