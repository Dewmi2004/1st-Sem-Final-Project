package lk.ijse.aquariumfinal.Model;

import lk.ijse.aquariumfinal.DTO.CustomerDTO;
import lk.ijse.aquariumfinal.util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class CustomerModel {
    public boolean saveCustomer(CustomerDTO customerDto) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("insert into customer values(?,?,?,?,?,?,? )", customerDto.getId(), customerDto.getName(), customerDto.getAddress(), customerDto.getDob(), customerDto.getGender(), customerDto.getContact(), customerDto.getEmail());
    }

public ArrayList<CustomerDTO> getAllCustomer() throws SQLException, ClassNotFoundException {
    ResultSet rs = CrudUtil.execute("select * from customer");
    ArrayList<CustomerDTO> customerDtoArrayList = new ArrayList<>();
    while (rs.next()) {
        CustomerDTO customerDto = new CustomerDTO(rs.getString(1),rs.getString(2),rs.getString(3),rs.getString(4),rs.getString(5),rs.getString(6),rs.getString(7));
        customerDtoArrayList.add(customerDto);
    }
    return customerDtoArrayList;
}

    public String getNextCustomer() throws SQLException, ClassNotFoundException {
        ResultSet rs = CrudUtil.execute("select customer_id from customer order by customer_id DESC limit 1");
        char tableCharactor ='C';
        if(rs.next()){
            String lastId =rs.getString(1);
            String lastIdNumberString = lastId.substring(1);
            int lastIdNumber = Integer.parseInt(lastIdNumberString);
            int nextIdNumber = lastIdNumber + 1;
            String nextIdString =String.format("C%03d", nextIdNumber);
            return nextIdString;
        }
        return tableCharactor+"001";
    }
}