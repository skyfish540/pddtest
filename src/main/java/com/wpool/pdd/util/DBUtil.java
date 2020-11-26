package com.wpool.pdd.util;

import java.lang.reflect.Field;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DBUtil {

     private static Connection con;
     private static PreparedStatement ps;

     //在DBUtil类文件被加载时，加载mysql驱动类
     static{
         try {
             Class.forName("com.mysql.jdbc.Driver");
         } catch (ClassNotFoundException e) {
             e.printStackTrace();
             System.out.println("mysql驱动类加载失败");
         }
     }

     //将建立连接通道步骤进行封装
    public static Connection createConnection()throws Exception{
        try {
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/1790?autoReconnect=true&failOverReadOnly=false&useUnicode=true&characterEncoding=utf8","root","root");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("建立连接通道失败.........");
            throw new RuntimeException();
        }
        return con;
     }
     //封装数据库操作对象创建步骤
    public static PreparedStatement createStatement(String sql)throws Exception{
        try {
            ps =createConnection().prepareStatement(sql);
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("创建数据库操作对象失败了");
            throw new RuntimeException();
        }
        return ps;
    }

    //将ResulSet中数据行内容封装到对应的实体类对象中(MyBatis)
                                            //Dept.class
    public static List<Object>  selectList(String sql,Class classFile)throws Exception{
         ResultSet rs = null;
         Field fieldArray[]=null;
         List<Object> list = new ArrayList();
         //1.推送查询语句得到一个结果集
          rs = (ResultSet)execute(sql);
         //2.得到与当前表对应的实体类中【属性信息】
         fieldArray = classFile.getDeclaredFields();//得到属性信息，变相等于知道了当前临时表中有哪些字段存在
        //3.循环结果集，将结果集中数据行中字段内容保存到同名的属性
         while(rs.next()){
             //每得到一个数据行，就创建一个对应的【实体类对象】
             Object  instancer = classFile.newInstance(); // 调用当前类中无参构造方法创建实例对象      instance =  new Dept()
             //将当前数据行字段内容赋值给实例对象中【同名属性】
             for(Field fieldObj: fieldArray){ // private Integer deptNo;
                      String columnName =  fieldObj.getName();// 'deptNo'
                      //读取对应字段中内容
                      String value = rs.getString(columnName);//读取当前数据行deptNo字段内容   String value =  rs.getString("deptNo")
                      //为了正确赋值，需要得到当前属性的数据类型名称
                      String typeName =   fieldObj.getType().getName();//'java.lang.Integer'
                     //可以进行类型转换并赋值
                      fieldObj.setAccessible(true);//通知JVM,强制使用这个属性
                      if(value!=null){
                          if("java.lang.Integer".equals(typeName)){
                              fieldObj.set(instancer,Integer.valueOf(value));//Integer.valueOf(nulll); NumberFormateException:null  emp.comm
                          }else if("java.lang.Double".equals(typeName)){
                              fieldObj.set(instancer,Double.valueOf(value));
                          }else if("java.lang.String".equals(typeName)){
                              fieldObj.set(instancer,value);
                          }
                      }

             }
             list.add(instancer);
         }

         DBUtil.close(rs);

         return list;
    }


    //根据得到实体类对象内容，自动拼接出一条插入语句
    /*
    *  一。插入语句组成
    *
    *      INSERT INTO  [表名]  (字段名1，字段名2)   value(值1，值2)
    *      -----------  -----   -------------------  ----------------
    *            1         2             3                 4
    *
    *   问题1： 如何动态得到插入的[表名]
    *
    *   [解答]：根据规则【实体类的类名与表名相同】
    *
    *
    *   问题2：如何得到需要赋值的字段名
    *   [解答]：根据规则【实体类的属性名称与表中字段名相同】
    *           根据属性内容是否为null,来决定当前字段名是否出现在插入语句
    *
    *   问题3： 如何得到需要赋值内容
    *   [解答]: 根据规则【一个实体类的实例对象用于在内存中保存表中的一个数据行】
    *           读取实体类对象中属性内容得到赋值内容
    *
    */
    public  static int  save(Object instance)throws Exception{

          String tableName = null;
          Class classFile = instance.getClass();//Dept.class
          Field fieldArray[]=classFile.getDeclaredFields();//[private Integer deptNo,private String dname,private String loc]
          StringBuffer columns = new StringBuffer("(");
          StringBuffer sql = new StringBuffer("INSERT INTO ");
          StringBuffer values = new StringBuffer(" value(");
          int flag = 0;
         //1.动态获得本次操作的表名
         tableName = classFile.getSimpleName();//"Dept"

         //2.动态获得插入语句中需要赋值的字段名
         for(Field field:fieldArray){ //private Integer deptNo
            String columnName =  field.getName();//  "deptNo";
            field.setAccessible(true);
            Object value  =field.get(instance);//    Object value =   dept.deptNo
            String typeName = field.getType().getName();
            if(value!=null){
                if(!columns.toString().equals("(")){
                    columns.append(",");
                }
                columns.append(columnName);

                if(!values.toString().equals(" value(")){
                    values.append(",");
                }

                if("java.lang.String".equals(typeName)){
                    values.append("'");
                    values.append(value);
                    values.append("'");
                }else{
                    values.append(value);
                }

            }
        }
         columns.append(")");
         values.append(")");

         sql.append(tableName);
         sql.append(columns);
         sql.append(values);
        System.out.println(sql.toString());

        flag = (int)execute(sql.toString());
        DBUtil.close();

         return flag;

    }


     //推送SQL命令
    public static  Object  execute(String sql)throws Exception{
        createStatement(sql);
        if(sql.indexOf("select")==0){
              ResultSet rs = ps.executeQuery();
              return rs;
        }else{
               int flag = ps.executeUpdate();
               return flag;
        }

    }
     //销毁资源

    public static void close(ResultSet rs){
         if(rs!=null){
             try {
                 rs.close();
             } catch (SQLException e) {
                 e.printStackTrace();
             }
         }

         close();
    }
    public static void close(){

         if(con!=null){
             try {
                 con.close();
             } catch (SQLException e) {
                 e.printStackTrace();
             }
         }

         if(ps!=null){
             try {
                 ps.close();
             } catch (SQLException e) {
                 e.printStackTrace();
             }
         }
    }
}
