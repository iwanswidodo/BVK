package BVK.GlobalMethod.Postgres;
import BVK.GlobalMethod.Logger.Log;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static BVK.BE.MainBaseMethod.random;
import static BVK.GlobalMethod.Utils.DateAndTime.*;

public class DbFunction {
    public static Connection DBconnection(String username,String dbname,String password ) {
        Connection c = null;
        Statement stmt = null;
        try {
            Class.forName("org.postgresql.Driver");
            c = DriverManager
                    .getConnection("jdbc:postgresql://komodo-nonprod.cwudgyhgbdjo.ap-southeast-1.rds.amazonaws.com/"+dbname, username, password);
        if(c!=null){
            Log.info("connected");
        }else{
            Log.info("Connection broken");

        }

        } catch (Exception e) {
//           Log.info(String.valueOf(e));
        }
        return c;
    }
    public void read_data (Connection conn, String table_name ){
        Statement stat;
        ResultSet rs = null;
//        ResultSet rs2 = null;
         try{
            String query = String.format("select * from %s",table_name);
            stat = conn.createStatement();
            rs = stat.executeQuery(query);

             ResultSetMetaData metadata = rs.getMetaData();
             int columnCount = metadata.getColumnCount();
             while (rs.next()) {
                 String row = "";
                 for (int i = 1; i <= columnCount; i++) {
                     row += rs.getString(i) + ", ";
                 }
                 System.out.println(row);
             }

         }catch(Exception e){
             Log.info(String.valueOf(e));
        }
    }
    public static void UpdateTcDate (Connection conn, String table_name,String tc_id, int create_at, int update_at, int sent_at ){
        Statement stat;
        ResultSet rs = null;
        String query = String.format("select * from %s WHERE tc_id = '%s'",table_name,tc_id );

//        ResultSet rs2 = null;
        try{
            stat = conn.createStatement();
            rs = stat.executeQuery(query);
            while (rs.next()) {
                System.out.println("TC ID at: "+rs.getString("tc_id"));
                System.out.println("Created at: "+rs.getString("created_at"));
                System.out.println("Updated at: "+rs.getString("updated_at"));
                System.out.println("Sent at: "+rs.getString("sent_at"));
            }


            String update = String.format("UPDATE public.%s SET created_at = '%s', updated_at = '%s', sent_at = '%s' WHERE tc_id = '%s'",table_name,getFutureDate6(create_at),getFutureDate6(update_at),getFutureDate6(sent_at),tc_id);
            stat.executeUpdate(update);


            rs = stat.executeQuery(query);
            while (rs.next()) {
                System.out.println("TC ID at: "+rs.getString("tc_id"));
                System.out.println("Created at: "+rs.getString("created_at"));
                System.out.println("Updated at: "+rs.getString("updated_at"));
                System.out.println("Sent at: "+rs.getString("sent_at"));
            }

        }catch(Exception e){
            Log.info(String.valueOf(e));
        }
    }

    public static void UpdateRfqDate (Connection conn, String table_name,String rfq_id, int due_date ){
        Statement stat;
        ResultSet rs = null;
        String query = String.format("select * from %s WHERE rfq_id = '%s'",table_name,rfq_id );

//        ResultSet rs2 = null;
        try{
            stat = conn.createStatement();
            rs = stat.executeQuery(query);
            while (rs.next()) {
                System.out.println("RFQ ID at: "+rs.getString("rfq_id"));
                System.out.println("Due date at: "+rs.getString("due_date"));
            }


            String update = String.format("UPDATE public.%s SET due_date = '%s' WHERE rfq_id = '%s'",table_name,getFutureDate6(due_date),rfq_id);
            stat.executeUpdate(update);


            rs = stat.executeQuery(query);
            while (rs.next()) {
                System.out.println("RFQ ID at: "+rs.getString("rfq_id"));
                System.out.println("Due date at: "+rs.getString("due_date"));
            }

        }catch(Exception e){
            Log.info(String.valueOf(e));
        }
    }

    public static String getRandomDphId(Connection conn) {
        Statement stat;
        ResultSet rs;
        List<String> dphList = new ArrayList<>();
        String query = String.format("SELECT dph_id FROM public.request_for_quotation_comparisons WHERE (created_at BETWEEN '%s' AND '%s')",getFutureDate4(-30),getFutureDate4(1));
        try{
            stat = conn.createStatement();
            rs = stat.executeQuery(query);
            while (rs.next()) {
                dphList.add(rs.getString("dph_id"));
            }
        }catch(Exception e){
            Log.info(String.valueOf(e));
        }
        return dphList.get(random(dphList.size()-1));
    }

    public static String getRandomPoId(Connection conn) {
        Statement stat;
        ResultSet rs;
        List<String> poList = new ArrayList<>();
        String query = String.format("SELECT po_internal_id FROM public.purchase_orders WHERE (created_at BETWEEN '%s' AND '%s')",getFutureDate4(-30),getFutureDate4(1));
        try{
            stat = conn.createStatement();
            rs = stat.executeQuery(query);
            while (rs.next()) {
                poList.add(rs.getString("po_internal_id"));
            }
        }catch(Exception e){
            Log.info(String.valueOf(e));
        }
        return poList.get(random(poList.size()-1));
    }

    public static String getPOCreateDate(Connection conn, String poId) {
        Statement stat;
        ResultSet rs;
        List<String> poList = new ArrayList<>();
        String query = String.format("SELECT created_at FROM public.purchase_orders WHERE po_internal_id ='%s'",poId);

        try{
            stat = conn.createStatement();
            rs = stat.executeQuery(query);
            while (rs.next()) {
                poList.add(rs.getString("created_at"));
            }
        }catch(Exception e){
            Log.info(String.valueOf(e));
        }
        return poList.get(0);
    }

    public static void UpdatePoDate (Connection conn,String po_id, int due_date ){
        Statement stat;
        ResultSet rs = null;
        String query = String.format("select created_at from purchase_orders WHERE po_internal_id = '%s'",po_id );
        try{
            stat = conn.createStatement();
            String update = String.format("UPDATE purchase_orders SET created_at = '%s' WHERE po_internal_id = '%s'",getFutureDate8(due_date),po_id);
            stat.executeUpdate(update);

            rs = stat.executeQuery(query);
            while (rs.next()) {
                System.out.println("created at: "+rs.getString("created_at"));
            }

        }catch(Exception e){
            Log.info(String.valueOf(e));
        }
    }

    public static String getRFQCreateDate(Connection conn, String rfqId) {
        Statement stat;
        ResultSet rs;
        List<String> poList = new ArrayList<>();
        String query = String.format("SELECT created_at FROM request_for_quotations WHERE rfq_no ='%s'",rfqId);

        try{
            stat = conn.createStatement();
            rs = stat.executeQuery(query);
            while (rs.next()) {
                poList.add(rs.getString("created_at"));
            }
        }catch(Exception e){
            Log.info(String.valueOf(e));
        }
        return poList.get(0);
    }

    public static String getLatestRFQ(Connection conn) {
        Statement stat;
        ResultSet rs;
        List<String> poList = new ArrayList<>();
        String query = String.format("SELECT rfq_no FROM request_for_quotations ORDER BY created_at desc limit 1");

        try{
            stat = conn.createStatement();
            rs = stat.executeQuery(query);
            while (rs.next()) {
                poList.add(rs.getString("rfq_no"));
            }
        }catch(Exception e){
            Log.info(String.valueOf(e));
        }
        return poList.get(0);
    }

    public static String getLatestPoFull(Connection conn,int itemVar) {
        Statement stat;
        ResultSet rs;
        List<String> poList = new ArrayList<>();
        String query = String.format("SELECT po_internal_id FROM purchase_requisition_item_erp_purchase_order ORDER BY created_at desc limit %s",itemVar);


        try{
            stat = conn.createStatement();
            rs = stat.executeQuery(query);
            while (rs.next()) {
                poList.add(rs.getString("po_internal_id"));
            }
        }catch(Exception e){
            Log.info(String.valueOf(e));
        }
        System.out.println(poList);
        return poList.toString();
    }

    public static String getPOFullCreateDate(Connection conn, String po_internal_id) {
        Statement stat;
        ResultSet rs;
        List<String> poList = new ArrayList<>();
        String query = String.format("SELECT created_at FROM purchase_requisition_item_erp_purchase_order WHERE po_internal_id ='%s'",po_internal_id);

        try{
            stat = conn.createStatement();
            rs = stat.executeQuery(query);
            while (rs.next()) {
                poList.add(rs.getString("created_at"));
            }
        }catch(Exception e){
            Log.info(String.valueOf(e));
        }
        return poList.get(0);
    }


    public static String getTcNumber(Connection conn) {
        Statement stat;
        ResultSet rs;
        List<String> poList = new ArrayList<>();
        String query = String.format("SELECT tc_no FROM trade_confirmations ORDER BY created_at desc limit 1");

        try{
            stat = conn.createStatement();
            rs = stat.executeQuery(query);
            while (rs.next()) {
                poList.add(rs.getString("tc_no"));
            }
        }catch(Exception e){
            Log.info(String.valueOf(e));
        }
        return poList.get(0);

    }
    public static String getTcDate(Connection conn, String TcId) {
        Statement stat;
        ResultSet rs;
        List<String> poList = new ArrayList<>();
        String query = String.format("SELECT created_at FROM trade_confirmations WHERE tc_no ='%s'",TcId);

        try{
            stat = conn.createStatement();
            rs = stat.executeQuery(query);
            while (rs.next()) {
                poList.add(rs.getString("created_at"));
            }
        }catch(Exception e){
            Log.info(String.valueOf(e));
        }
        return poList.get(0);

    }


    public static String getGokomodoAppCustomerNumber(){
        Statement stat;
        ResultSet rs;
        String query = String.format("SELECT phone_number FROM external_app_users WHERE status = 'active' LIMIT 10");
        Connection conn = DBconnection("postgres","db_gokomodo_backend_dev","gokomodo");
        List<String> phoneNumber = new ArrayList<>();
        try{
            stat = conn.createStatement();
            rs = stat.executeQuery(query);
            while (rs.next()) {
                phoneNumber.add(rs.getString("phone_number"));
            }
        }catch(Exception e){
            Log.info(String.valueOf(e));
        }
        return phoneNumber.get(random(phoneNumber.size()));
    }

    public static List<String> getDbData(String selectQuery, String dbname, String columnLabel){
        Statement stat;
        ResultSet rs;
        Connection conn = DBconnection("postgres",dbname,"gokomodo");
        List<String> column = new ArrayList<>();
        try{
            stat = conn.createStatement();
            rs = stat.executeQuery(selectQuery);
            while (rs.next()) {
                column.add(rs.getString(columnLabel));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return column;
    }

    public static void UpdateDbData(String updateQuery,String dbname ){
        Statement stat;
        ResultSet rs = null;
        Connection conn = DBconnection("postgres",dbname,"gokomodo");
        try{
            stat = conn.createStatement();
            stat.executeUpdate(updateQuery);
            Log.info("DB updated");
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}


