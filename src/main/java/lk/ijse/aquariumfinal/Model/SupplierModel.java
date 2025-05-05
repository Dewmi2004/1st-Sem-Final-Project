package lk.ijse.aquariumfinal.Model;

import lk.ijse.aquariumfinal.DTO.CustomerDTO;
import lk.ijse.aquariumfinal.DTO.SupplierDTO;
import lk.ijse.aquariumfinal.util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class SupplierModel {
    public static boolean saveSupplier(SupplierDTO supDTO) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("insert into supplier values(?,?,?,?,? )", supDTO.getSupId(), supDTO.getName(), supDTO.getContact(),  supDTO.getCompanyAddress(),supDTO.getSupplyType(), supDTO.getEmail());

    }

    public static Boolean updateSupplier(SupplierDTO supDTO) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("update supplier set name = ?,contact = ?,company_Address = ?,supply_Type = ?,Email = ? where sup_Id =  ?",supDTO.getName(),supDTO.getContact(),supDTO.getCompanyAddress(),supDTO.getSupplyType(),supDTO.getEmail(),supDTO.getSupId());

    }

    public ArrayList<SupplierDTO> getAllSupplier() throws SQLException, ClassNotFoundException {
        ResultSet rs = CrudUtil.execute("select * from supplier");
        ArrayList<SupplierDTO> supplierDtoArrayList = new ArrayList<>();
        while (rs.next()) {
            SupplierDTO supplierDto = new SupplierDTO(rs.getString(1),rs.getString(2),rs.getString(3),rs.getString(4),rs.getString(5),rs.getString(6));
            supplierDtoArrayList.add(supplierDto);
        }
        return supplierDtoArrayList;
    }

    public String getNextSupplier() throws SQLException, ClassNotFoundException {
        ResultSet rs = CrudUtil.execute("select sup_Id from supplier order by sup_Id DESC limit 1");
        char tableCharactor ='S';
        if(rs.next()){
            String lastId =rs.getString(1);
            String lastIdNumberString = lastId.substring(1);
            int lastIdNumber = Integer.parseInt(lastIdNumberString);
            int nextIdNumber = lastIdNumber + 1;
            String nextIdString =String.format("S%03d", nextIdNumber);
            return nextIdString;
        }
        return tableCharactor+"001";
    }

    public Boolean deleteSupplier(String id) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("delete from supplier where sup_Id= ?",id);
    }
}
