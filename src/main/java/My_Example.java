import java.sql.*;

public class My_Example {

    private Connection getConection() throws SQLException {



        String url = "jdbc:mysql://localhost:3306/library?sslMode=DISABLED";// sslMode -> BUG
        String user = "root";
        String pass = "1234";


        return DriverManager.getConnection(url, user, pass);

    }


    private void printResults(ResultSet rs) throws SQLException {
        rs.beforeFirst();
        //parcurgem iteratorul

        while (rs.next()) {

            int id = rs.getInt("id");
            String title = rs.getString("title");
            String owner = rs.getString("owner_id");



            System.out.println(id + "," + title + " ," + owner) ;
        }

    }


    public void useQuery() {

        Connection connection = null;
        Statement stmt = null;
        try {
            connection = this.getConection();
            stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT id ,title,owner_id FROM Book");
            this.printResults(rs);

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                stmt.close();
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }
    }


    public void useDelete() {

        try (Connection connection1 = this.getConection();
             Statement stmt1 = connection1.createStatement()) {
            stmt1.executeUpdate("DELETE FROM User WHERE id=2");

        } catch (SQLException e) {
            e.printStackTrace();
        }


    }

    public void usePreparedStatment() {


        try (Connection connection1 = this.getConection();
             PreparedStatement stmt = connection1.prepareStatement("SELECT * FROM Book WHERE id  > ?")) {
            stmt.setLong(1, 1L);
            stmt.executeQuery();
            ResultSet rs = stmt.executeQuery();
            this.printResults(rs);

        } catch (SQLException e) {
            e.printStackTrace();
        }


    }

    public void useTransaction(){
        try(Connection connection = this.getConection();
            Statement statement =connection.createStatement()

        ){

            //setam autocomitul pe false
            connection.setAutoCommit(false);

            String sqlQuery ="SELECT * FROM Book";
            System.out.println("Initial state");
            ResultSet rs = statement.executeQuery(sqlQuery);
            this.printResults(rs);

            statement.executeUpdate("DELETE FROM User where id=6");
            System.out.println("State after delete");

            printResults(statement.executeQuery(sqlQuery));

            connection.rollback();
            System.out.println("State after rollback");
            printResults(statement.executeQuery(sqlQuery));
            connection.commit();
            System.out.println("State after commit");

        }

        catch (SQLException e) {
            e.printStackTrace();
        }


    }


    public static void main(String[] args) {
        My_Example example = new My_Example();

        System.out.println("List of books");
         example.useQuery();

        System.out.println("DELETE");

        //example.useDelete();

        // example.usePreparedStatment();
        // example.useTransaction();


        System.out.println();

    }





}

