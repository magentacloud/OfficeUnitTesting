import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.office.Department;
import org.office.Employee;
import org.office.Service;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Tests {
    @BeforeEach
    public void setup(){
        Service.createDB();
    }

    @Test
    public void removeDepartmentTest(){
        int removableDepartmentID = 1;
        List<Employee> employeeList = getEmployeesListByDepartmentID(removableDepartmentID);
        Department department = new Department(1, "Accounting");


        Assertions.assertNotEquals(0, employeeList.size(), "Пустой список сотрудников отдела с ID="
                + removableDepartmentID);

        Service.removeDepartment(department);

        List<Department> departmentList = getDepartmentByID(removableDepartmentID);

        Assertions.assertEquals(0, departmentList.size());

        employeeList = getEmployeesListByDepartmentID(removableDepartmentID);

        Assertions.assertEquals(0, employeeList.size());
    }

    private List<Employee> getEmployeesListByDepartmentID(int removableDepartmentID){
        List<Employee> employeeList = new ArrayList<>();

        try (Connection con = DriverManager.getConnection("jdbc:h2:.\\Office")) {
            Statement stm = con.createStatement();
            ResultSet rs= stm.executeQuery("""
                    Select *
                     from Employee
                     where DepartmentID = %d
                    """.formatted(removableDepartmentID));
            while(rs.next()) {
                Integer id = rs.getInt("ID");
                String name = rs.getString("Name");
                Integer departmentID = rs.getInt("DepartmentID");
                Employee employee = new Employee(id, name, departmentID);
                employeeList.add(employee);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return employeeList;
    }

    private List<Department> getDepartmentByID(int departmentID){
        List<Department> departmentList = new ArrayList<>();

        try (Connection con = DriverManager.getConnection("jdbc:h2:.\\Office")) {
            Statement stm = con.createStatement();
            ResultSet rs= stm.executeQuery("""
                    Select *
                     from Department
                     where ID = %d
                    """.formatted(departmentID));
            while(rs.next()) {
                Integer id = rs.getInt("ID");
                String name = rs.getString("Name");
                Department department = new Department(id, name);
                departmentList.add(department);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return departmentList;
    }
}
