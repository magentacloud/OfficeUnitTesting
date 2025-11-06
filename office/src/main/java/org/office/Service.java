package org.office;

import java.sql.*;

public class Service {

    public Service(String pathToDB){
        this.pathToDB = pathToDB;
    }

    private final String pathToDB;

    public void createDB() {
        try (Connection con = DriverManager.getConnection(this.pathToDB)) {
            Statement stm = con.createStatement();
            stm.executeUpdate("DROP TABLE Department IF EXISTS");
            stm.executeUpdate("CREATE TABLE Department(ID INT PRIMARY KEY, NAME VARCHAR(255))");
            stm.executeUpdate("INSERT INTO Department VALUES(1,'Accounting')");
            stm.executeUpdate("INSERT INTO Department VALUES(2,'IT')");
            stm.executeUpdate("INSERT INTO Department VALUES(3,'HR')");

            stm.executeUpdate("DROP TABLE Employee IF EXISTS");
            stm.executeUpdate("CREATE TABLE Employee(ID INT PRIMARY KEY, NAME VARCHAR(255), DepartmentID INT)");
            stm.executeUpdate("INSERT INTO Employee VALUES(1,'Pete',1)");
            stm.executeUpdate("INSERT INTO Employee VALUES(2,'Ann',1)");

            stm.executeUpdate("INSERT INTO Employee VALUES(3,'Liz',2)");
            stm.executeUpdate("INSERT INTO Employee VALUES(4,'Tom',2)");

            stm.executeUpdate("INSERT INTO Employee VALUES(5,'Todd',3)");

        } catch (SQLException e) {
            System.out.println(e);
        }
    }

    public void addDepartment(Department d) {
        try (Connection con = DriverManager.getConnection(this.pathToDB)) {
            PreparedStatement stm = con.prepareStatement("INSERT INTO Department VALUES(?,?)");
            stm.setInt(1, d.departmentID);
            stm.setString(2, d.getName());
            stm.executeUpdate();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void removeDepartment(Department d) {
        try (Connection con = DriverManager.getConnection(this.pathToDB)) {
            PreparedStatement stm = con.prepareStatement("DELETE FROM Employee WHERE DepartmentID=?");
            stm.setInt(1, d.departmentID);
            stm.executeUpdate();
        } catch (Exception e) {
            System.out.println(e);
        }

        try (Connection con = DriverManager.getConnection(this.pathToDB)) {
            PreparedStatement stm = con.prepareStatement("DELETE FROM Department WHERE ID=?");
            stm.setInt(1, d.departmentID);
            stm.executeUpdate();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void addEmployee(Employee empl) {
        try (Connection con = DriverManager.getConnection(this.pathToDB)) {
            PreparedStatement stm = con.prepareStatement("INSERT INTO Employee VALUES(?,?,?)");
            stm.setInt(1, empl.getEmployeeId());
            stm.setString(2, empl.getName());
            stm.setInt(3, empl.getDepartmentId());
            stm.executeUpdate();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void removeEmployee(Employee empl) {
        try (Connection con = DriverManager.getConnection(this.pathToDB)) {
            PreparedStatement stm = con.prepareStatement("DELETE FROM Employee WHERE ID=?");
            stm.setInt(1, empl.getEmployeeId());
            stm.executeUpdate();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void printDepartments(){
        try(Connection con = DriverManager.getConnection(this.pathToDB)){
            PreparedStatement stm = con.prepareStatement(
                    "Select ID, NAME as txt from Department where name like ?",
                    ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_UPDATABLE
            );
            String str="%";
            //ResultSet rs= stm.executeQuery("Select ID, NAME as txt from Department");
            stm.setString(1,str);
            ResultSet rs=stm.executeQuery();
            System.out.println("------------------------------------");
            while(rs.next()){
                System.out.println(rs.getInt("ID")+"\t"+rs.getString("name"));
            }
            System.out.println("------------------------------------");
        }catch (SQLException e) {
            System.out.println(e);
        }
    }

    public void printEmployees(){
        try(Connection con = DriverManager.getConnection(this.pathToDB)){
            Statement stm = con.createStatement();
            ResultSet rs= stm.executeQuery("Select Employee.ID, Employee.Name,Department.Name as DepName from Employee join Department on Employee.DepartmentID=Department.ID");
            //ResultSet rs= stm.executeQuery("Select Employee.ID, Employee.Name,Employee.DepartmentID as DepName from Employee");
            System.out.println("------------------------------------");
            ResultSetMetaData metaData= rs.getMetaData();
            while(rs.next()){
                System.out.println(rs.getInt("ID")+"\t"+rs.getString("NAME")+"\t"+rs.getString("DepName"));
            }
            System.out.println("------------------------------------");
        }catch (SQLException e) {
            System.out.println(e);
        }
    }
}
