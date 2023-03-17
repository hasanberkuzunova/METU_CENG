package ceng.ceng351.foodrecdb;
import jdk.jfr.Category;

import java.sql.*;
import java.util.ArrayList;
public class FOODRECDB implements IFOODRECDB{
    private static Connection connection = null;
    private static String user = "e2376028"; // TODO: Your userName
    private static String password = "iwYzjEkHESldJHXE"; //  TODO: Your password
    private static String host = "momcorp.ceng.metu.edu.tr"; // host name
    private static String database = "db2376028"; // TODO: Your database name
    private static int port = 8080; // port
    @Override
    public void initialize() {
        String url = "jdbc:mysql://" + host + ":" + port + "/" + database;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection =  DriverManager.getConnection(url, user, password);
        }
        catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int createTables() {

        int numberofTablesInserted = 0;


        String queryCreateMenuItemsTable = "create table if not exists MenuItems (" +
                "itemID int NOT NULL," +
                "itemName varchar(40) ," +
                "cuisine varchar(20) ," +
                "price int ," +
                "primary key (itemID))";


        String queryCreateIngredientsTable = "create table if not exists Ingredients (" +
                "ingredientID int NOT NULL," +
                "ingredientName varchar(40) ," +
                "primary key (ingredientID)) " ;


        String queryCreateIncludesTable = "create table if not exists Includes (" +
                "itemID int NOT NULL," +
                "ingredientID int NOT NULL," +
                "primary key (itemID,ingredientID),"+
                "foreign key (itemID) references MenuItems(itemID) on delete cascade on update cascade,"+
                "foreign key (ingredientID) references Ingredients(ingredientID) on delete cascade on update cascade)";;


        String queryCreateRatingsTable = "create table if not exists Ratings (" +
                "ratingID int NOT NULL," +
                "itemID int," +
                "rating int," +
                "ratingDate date," +
                "primary key (ratingID),"+
                "foreign key (itemID) references MenuItems(itemID) on delete cascade on update cascade)";


        String queryCreateDietaryCategoriesTable = "create table if not exists DietaryCategories (" +
                "ingredientID int NOT NULL," +
                "dietaryCategory varchar(20) NOT NULL," +
                "primary key (ingredientID, dietaryCategory),"+
                "foreign key (ingredientID) references Ingredients(ingredientID) on delete cascade on update cascade)";


        try {
            Statement statement = this.connection.createStatement();

            statement.executeUpdate(queryCreateMenuItemsTable);
            numberofTablesInserted++;

            statement.executeUpdate(queryCreateIngredientsTable);
            numberofTablesInserted++;

            statement.executeUpdate(queryCreateIncludesTable);
            numberofTablesInserted++;

            statement.executeUpdate(queryCreateRatingsTable);
            numberofTablesInserted++;

            statement.executeUpdate(queryCreateDietaryCategoriesTable);
            numberofTablesInserted++;

            statement.close();
        }

        catch (SQLException e) {
            e.printStackTrace();
        }

        return numberofTablesInserted;
    }

    @Override
    public int dropTables() {
        int numberofTablesDropped = 0;

        String queryDropMenuItemsTable = "drop table if exists MenuItems";

        String queryDropIngredientsTable = "drop table if exists Ingredients";

        String queryDropIncludesTable = "drop table if exists Includes";

        String queryDropRatingsTable = "drop table if exists Ratings";

        String queryDropDietaryCategoriesTable = "drop table if exists DietaryCategories";


        try {
            Statement statement = this.connection.createStatement();

            statement.executeUpdate(queryDropDietaryCategoriesTable);
            numberofTablesDropped++;

            statement.executeUpdate(queryDropIncludesTable);
            numberofTablesDropped++;

            statement.executeUpdate(queryDropIngredientsTable);
            numberofTablesDropped++;

            statement.executeUpdate(queryDropRatingsTable);
            numberofTablesDropped++;


            statement.executeUpdate(queryDropMenuItemsTable);
            numberofTablesDropped++;


            statement.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return numberofTablesDropped;

    }


    @Override
    public int insertMenuItems(MenuItem[] items) {

        int numberofRowsInserted = 0;

        for (int i = 0; i < items.length; i++)
        {
            try {
                MenuItem menuitem = items[i];

                PreparedStatement statement=this.connection.prepareStatement("insert ignore into MenuItems values(?,?,?,?)");
                statement.setInt(1,menuitem.getItemID());
                statement.setString(2,menuitem.getItemName());
                statement.setString(3,menuitem.getCuisine());
                statement.setInt(4,menuitem.getPrice());

                statement.executeUpdate();
                statement.close();

                numberofRowsInserted++;

            }
            catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return numberofRowsInserted;
    }

    @Override
    public int insertIngredients(Ingredient[] ingredients) {

        int numberofRowsInserted = 0;

        for (int i = 0; i < ingredients.length; i++)
        {
            try {
                Ingredient ingr = ingredients[i];

                PreparedStatement statement=this.connection.prepareStatement("insert ignore into Ingredients values(?,?)");
                statement.setInt(1,ingr.getIngredientID());
                statement.setString(2,ingr.getIngredientName());

                statement.executeUpdate();
                statement.close();

                numberofRowsInserted++;

            }
            catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return numberofRowsInserted;
    }

    @Override
    public int insertIncludes(Includes[] includes) {

        int numberofRowsInserted = 0;

        for (int i = 0; i < includes.length; i++)
        {
            try {
                Includes incl = includes[i];

                PreparedStatement statement=this.connection.prepareStatement("insert ignore into Includes values(?,?)");
                statement.setInt(1,incl.getItemID());
                statement.setInt(2,incl.getIngredientID());

                statement.executeUpdate();
                statement.close();

                numberofRowsInserted++;

            }
            catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return numberofRowsInserted;
    }

    @Override
    public int insertDietaryCategories(DietaryCategory[] categories) {

        int numberofRowsInserted = 0;

        for (int i = 0; i < categories.length; i++)
        {
            try {
                DietaryCategory dc = categories[i];

                PreparedStatement statement=this.connection.prepareStatement("insert ignore into DietaryCategories values(?,?)");
                statement.setInt(1,dc.getIngredientID());
                statement.setString(2,dc.getDietaryCategory());

                statement.executeUpdate();
                statement.close();

                numberofRowsInserted++;

            }
            catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return numberofRowsInserted;
    }

    @Override
    public int insertRatings(Rating[] ratings) {

        int numberofRowsInserted = 0;

        for (int i = 0; i < ratings.length; i++)
        {
            try {
                Rating rtg = ratings[i];

                PreparedStatement statement=this.connection.prepareStatement("insert ignore into Ratings values(?,?,?,?)");
                statement.setInt(1,rtg.getRatingID());
                statement.setInt(2,rtg.getItemID());
                statement.setInt(3,rtg.getRating());
                statement.setString(4,rtg.getRatingDate());

                statement.executeUpdate();
                statement.close();

                numberofRowsInserted++;

            }
            catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return numberofRowsInserted;
    }

    @Override
    public MenuItem[] getMenuItemsWithGivenIngredient(String name) {

        ResultSet rs;
        ArrayList<MenuItem> reslist = new ArrayList<>();

        try {

            PreparedStatement stmt=this.connection.prepareStatement("select distinct m.itemID,m.itemName ,m.cuisine ,m.price  from MenuItems m inner join Includes i on m.itemID=i.itemID\n" +
                            "WHERE i.ingredientID=(select ingredientID from Ingredients i2 where i2.ingredientName=?)\n" +
                            "ORDER BY itemID ASC;");

            stmt.setString(1,name);
            rs=stmt.executeQuery();

            while(rs.next()){
                Integer itemID = rs.getInt("itemID");
                String itemName = rs.getString("itemName");
                String cuisine = rs.getString("cuisine");
                Integer price = rs.getInt("price");

                MenuItem obj = new MenuItem(itemID,itemName,cuisine,price);
                reslist.add(obj);
            }

            stmt.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        MenuItem[] resarray = new MenuItem[reslist.size()];

        return reslist.toArray(resarray);
    }

    @Override
    public MenuItem[] getMenuItemsWithoutAnyIngredient() {
        ResultSet rs;
        ArrayList<MenuItem> reslist = new ArrayList<>();

        try {

            PreparedStatement stmt=this.connection.prepareStatement("select DISTINCT m.itemID,m.itemName ,m.cuisine ,m.price  from MenuItems m\n" +
                    "where m.itemID not in\n" +
                    "(SELECT i.itemID from Includes i)\n" +
                    "ORDER BY m.itemID ASC;");

            rs=stmt.executeQuery();

            while(rs.next()){
                Integer itemID = rs.getInt("itemID");
                String itemName = rs.getString("itemName");
                String cuisine = rs.getString("cuisine");
                Integer price = rs.getInt("price");

                MenuItem obj = new MenuItem(itemID,itemName,cuisine,price);
                reslist.add(obj);
            }

            stmt.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        MenuItem[] resarray = new MenuItem[reslist.size()];

        return reslist.toArray(resarray);
    }

    @Override
    public Ingredient[] getNotIncludedIngredients() {

        ResultSet rs;
        ArrayList<Ingredient> reslist = new ArrayList<>();

        try {

            PreparedStatement stmt=this.connection.prepareStatement("SELECT DISTINCT i.ingredientID,i.ingredientName from Ingredients i\n" +
                    "where i.ingredientID not IN\n" +
                    "(select i2.ingredientID from Includes i2)\n" +
                    "ORDER BY i.ingredientID ASC;");

            rs=stmt.executeQuery();

            while(rs.next()){
                Integer ingredientID = rs.getInt("ingredientID");
                String ingredientName = rs.getString("ingredientName");


                Ingredient obj = new Ingredient(ingredientID,ingredientName);
                reslist.add(obj);
            }

            stmt.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        Ingredient[] resarray = new Ingredient[reslist.size()];

        return reslist.toArray(resarray);
    }

    @Override
    public MenuItem getMenuItemWithMostIngredients() {

        ResultSet rs;
        ArrayList<MenuItem> reslist = new ArrayList<>();

        try {

            PreparedStatement stmt=this.connection.prepareStatement("select * from MenuItems mi where itemID=\n" +
                    "(select i.itemID mycount\n" +
                    "from Includes i\n" +
                    "group by i.itemID ORDER BY COUNT(ingredientID) DESC limit 1)");


            rs= stmt.executeQuery();
            rs.next();
            Integer itemID = rs.getInt("itemID");
            String itemName = rs.getString("itemName");
            String cuisine = rs.getString("cuisine");
            Integer price = rs.getInt("price");

            MenuItem obj = new MenuItem(itemID,itemName,cuisine,price);

            reslist.add(obj);
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return reslist.get(0);

    }

    @Override
    public QueryResult.MenuItemAverageRatingResult[] getMenuItemsWithAvgRatings() {

        ResultSet rs;
        ArrayList<QueryResult.MenuItemAverageRatingResult> reslist = new ArrayList<>();

        String query = " select mi.itemID ,mi.itemName ,AVG(r.rating) avgRating\n" +
                "        from MenuItems mi left outer join Ratings r on mi.itemID=r.itemID\n" +
                "        group by itemID\n" +
                "        ORDER BY AVG(r.rating) DESC;" ;
        try {
            Statement st = this.connection.createStatement();
            rs = st.executeQuery(query);

            while (rs.next()) {

                String itemID = rs.getString("itemID");
                String itemName = rs.getString("itemName");
                String avgRating = rs.getString("avgRating");

                QueryResult.MenuItemAverageRatingResult obj = new QueryResult.MenuItemAverageRatingResult(itemID, itemName, avgRating);
                reslist.add(obj);
            }


            st.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        QueryResult.MenuItemAverageRatingResult[] resarray = new QueryResult.MenuItemAverageRatingResult[reslist.size()];

        return reslist.toArray(resarray);
    }

    @Override
    public MenuItem[] getMenuItemsForDietaryCategory(String category) {

        ResultSet rs;
        ArrayList<MenuItem> reslist = new ArrayList<>();

        try {

            PreparedStatement stmt=this.connection.prepareStatement("select * from MenuItems mi \n" +
                    "\n" +
                    "where mi.itemID NOT IN \n" +
                    "(select DISTINCT i.itemID  from Includes i \n" +
                    "inner join DietaryCategories dc on i.ingredientID =dc.ingredientID\n" +
                    "where dc.ingredientID  in\n" +
                    "(select DISTINCT dc.ingredientID  from DietaryCategories dc\n" +
                    "where dc.ingredientID not in\n" +
                    "(select distinct ingredientID  FROM DietaryCategories d\n" +
                    "where d.dietaryCategory =?)\t) order by itemid desc\t)\n" +
                    "\n" +
                    "and mi.itemID IN \n" +
                    "(select DISTINCT itemID from Includes i)\n" +
                    "\n" +
                    "Order by itemID ASC;");

            stmt.setString(1, category);
            rs=stmt.executeQuery();

            while(rs.next()){
                Integer itemID = rs.getInt("itemID");
                String itemName = rs.getString("itemName");
                String cuisine = rs.getString("cuisine");
                Integer price = rs.getInt("price");

                MenuItem obj = new MenuItem(itemID,itemName,cuisine,price);
                reslist.add(obj);
            }

            stmt.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        MenuItem[] resarray = new MenuItem[reslist.size()];

        return reslist.toArray(resarray);
    }

    @Override
    public Ingredient getMostUsedIngredient() {

        ResultSet rs;
        ArrayList<Ingredient> reslist = new ArrayList<>();

        try {

            PreparedStatement stmt=this.connection.prepareStatement(" select * from Ingredients i2 where ingredientID IN\n" +
                    "(select i.ingredientID  from Includes i group by ingredientID\n" +
                    "HAVING COUNT(*)=\n" +
                    "(select MAX(mycount)\n" +
                    "from\n" +
                    "(select i.ingredientID ,COUNT(*) mycount  from Includes i group by ingredientID)sub)\n" +
                    ") order by ingredientID ASC LIMIT 1");


            rs= stmt.executeQuery();
            rs.next();
            Integer ingredientID = rs.getInt("ingredientID");
            String ingredientName = rs.getString("ingredientName");


            Ingredient obj = new Ingredient(ingredientID,ingredientName);

            reslist.add(obj);
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return reslist.get(0);
    }

    @Override
    public QueryResult.CuisineWithAverageResult[] getCuisinesWithAvgRating() {

        ResultSet rs;
        ArrayList<QueryResult.CuisineWithAverageResult> reslist = new ArrayList<>();

        String query = "select mi.cuisine,AVG(rating) averageRating from MenuItems mi\n" +
                "        left outer join Ratings r on mi.itemID=r.itemID\n" +
                "        group by mi.cuisine\n" +
                "        order by AVG(rating) DESC;" ;
        try {
            Statement st = this.connection.createStatement();
            rs = st.executeQuery(query);

            while (rs.next()) {


                String cuisine = rs.getString("cuisine");
                String averageRating = rs.getString("averageRating");

                QueryResult.CuisineWithAverageResult obj = new QueryResult.CuisineWithAverageResult(cuisine,averageRating);
                reslist.add(obj);
            }


            st.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        QueryResult.CuisineWithAverageResult[] resarray = new QueryResult.CuisineWithAverageResult[reslist.size()];

        return reslist.toArray(resarray);
    }

    @Override
    public QueryResult.CuisineWithAverageResult[] getCuisinesWithAvgIngredientCount() {

        ResultSet rs;
        ArrayList<QueryResult.CuisineWithAverageResult> reslist = new ArrayList<>();

        String query = "select cuisine,AVG(mycount) averageCount from\n" +
                "          (select mi.cuisine ,COUNT(i.ingredientID) mycount from MenuItems mi " +
                "left outer join Includes i on mi.itemID=i.itemID\n" +
                "                group by mi.cuisine,i.itemID\n" +
                "                order by COUNT(i.ingredientID) DESC)sub\n" +
                "                group by cuisine\n" +
                "                order by AVG(mycount) desc;" ;
        try {
            Statement st = this.connection.createStatement();
            rs = st.executeQuery(query);

            while (rs.next()) {


                String cuisine = rs.getString("cuisine");
                String averageCount = rs.getString("averageCount");

                QueryResult.CuisineWithAverageResult obj = new QueryResult.CuisineWithAverageResult(cuisine,averageCount);
                reslist.add(obj);
            }


            st.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        QueryResult.CuisineWithAverageResult[] resarray = new QueryResult.CuisineWithAverageResult[reslist.size()];

        return reslist.toArray(resarray);
    }

    @Override
    public int increasePrice(String ingredientName, String increaseAmount) {

        int numberofRowsAffected = 0;
        try {

            PreparedStatement stmt=this.connection.prepareStatement("UPDATE MenuItems\n" +
                    "        set price=price+?\n" +
                    "        where itemID IN\n" +
                    "        (select itemID from Includes where ingredientID in\n" +
                    "        (select ingredientID from Ingredients\n" +
                    "        where ingredientName=?));");
            stmt.setString(1,increaseAmount);
            stmt.setString(2,ingredientName);

            numberofRowsAffected=stmt.executeUpdate();
            stmt.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return numberofRowsAffected;
    }

    @Override
    public Rating[] deleteOlderRatings(String date) {

        ResultSet rs;
        ArrayList<Rating> reslist = new ArrayList<>();

        try {

            PreparedStatement stmt=this.connection.prepareStatement("select * from Ratings where ratingDate < ?");
            stmt.setString(1,date);
            rs = stmt.executeQuery();

            while (rs.next()) {


                Integer ratingID = rs.getInt("ratingID");
                Integer itemID = rs.getInt("itemID");
                Integer rating = rs.getInt("rating");
                String ratingDate = rs.getString("ratingDate");

                Rating obj = new Rating(ratingID,itemID,rating,ratingDate);
                reslist.add(obj);
            }

            try {

                PreparedStatement stmt2=this.connection.prepareStatement("delete from Ratings where ratingDate<?");
                stmt2.setString(1,date);

                stmt2.executeUpdate();

                stmt2.close();

            } catch (SQLException e) {
                e.printStackTrace();
            }


            stmt.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

       Rating[] resarray = new Rating[reslist.size()];

        return reslist.toArray(resarray);
    }
}
