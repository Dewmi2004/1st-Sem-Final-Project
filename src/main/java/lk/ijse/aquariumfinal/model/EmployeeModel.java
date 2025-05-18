package lk.ijse.aquariumfinal.model;

import lk.ijse.aquariumfinal.dto.EmployeeDTO;
import lk.ijse.aquariumfinal.util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class EmployeeModel {

        public static boolean saveEmployee(EmployeeDTO employeeDTO) throws SQLException, ClassNotFoundException {
            return CrudUtil.execute("insert into employee values(?,?,?,?,?,?,? )", employeeDTO.getId(), employeeDTO.getName(), employeeDTO.getAddress(),  employeeDTO.getGender(),employeeDTO.getDob(), employeeDTO.getEmail(), employeeDTO.getContact());
        }

        public static Boolean updateEmployee(EmployeeDTO employeeDTO) throws SQLException, ClassNotFoundException {
            return CrudUtil.execute("update employee set employee_Name = ?,employee_Address = ?,employee_Gender = ?,employee_Dob = ?,employee_Email = ?,employee_Contact = ? where employee_Id = ?",employeeDTO.getName(),employeeDTO.getAddress(),employeeDTO.getGender(),employeeDTO.getDob(),employeeDTO.getEmail(),employeeDTO.getContact(),employeeDTO.getId());
        }

        public static Boolean deleteEmployee(String empId) throws SQLException, ClassNotFoundException {

            return CrudUtil.execute("delete from employee where employee_Id = ?",empId);
        }

        public static ArrayList<EmployeeDTO> getAllEmployee() throws SQLException, ClassNotFoundException {
            ResultSet rs = CrudUtil.execute("select * from employee");
            ArrayList<EmployeeDTO> employeeDtoArrayList = new ArrayList<>();
            while (rs.next()) {
                EmployeeDTO employeeDTO = new EmployeeDTO(rs.getString(1),rs.getString(2),rs.getString(3),rs.getString(4),rs.getString(5),rs.getString(6),rs.getString(7));
                employeeDtoArrayList.add(employeeDTO);
            }
            return employeeDtoArrayList;
        }

        public String getNextEmployee() throws SQLException, ClassNotFoundException {
            ResultSet rs = CrudUtil.execute("select employee_Id from employee order by employee_Id DESC limit 1");
            char tableCharactor ='E';
            if(rs.next()){
                String lastId =rs.getString(1);
                String lastIdNumberString = lastId.substring(1);
                int lastIdNumber = Integer.parseInt(lastIdNumberString);
                int nextIdNumber = lastIdNumber + 1;
                String nextIdString =String.format("E%03d", nextIdNumber);
                return nextIdString;
            }
            return tableCharactor+"001";
        }
    }