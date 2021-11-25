
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Properties;

public class Project3 {

	/**
	 * Object that stores the connection to the database
	 */
    private Connection conn;
    
 /**
  * This constructor should create and initialize the connection to the database. 
  * @param username the mysql username
  * @param password the mysql password
  * @param schema the mysql schema
 * @throws SQLException 
  */
    public Project3(String username, String password, String schema) throws SQLException {
	// Fill in code here to initialize conn so it connects to the database
	// using the provided parameters
    	Properties info = new Properties();
    	info.put("user", username);
    	info.put("password", password);
    	
    	conn = DriverManager.getConnection("jdbc:mysql://COMPDBS300/" + schema, info);
    }
  
    /**  This method implements:
     * Search for a store: Assuming that we are looking for two products, 
     * Assuming that we are looking for two products, find the store name and location of 
     * all the stores that have both products in stock. Do not display the stores that contain only one of the products. 
     * Outputs: prints on the screen the store name and location of all the stores that sell both these products.
     * Action: Searches the database for all the stores that have both products in stock.
     * Error handling: display an error message if one of the productIDs does not exist.
     *  @param productID1 integer containing the ID of the first product
     *  @param productID2 integer containing the ID of the second product
     *  @return none.
     */
    public void searchForStore(int productID1, int productID2) {
    }
    /**  This method implements: Search for a coupon: 
     * display all the coupons for all the products whose name contains a certain keyword. 
     * Do not include expired coupons.   
     * Outputs: prints on the screen a table containing coupon code, 
     * productID, product name, product brand, product price, discount, and expiration date.
     * Action: Searches the database to find unexpired coupons for
     *  products whose name contains the user-specified keyword.
     * @param keyword a string containing a keyword to search for.
     * @return none.
     */
    public void searchForCoupon(String keyword) {
    }
    /** This method implements: Search for an invoice: 
     * display all the invoices that belong to member with rewards number and 
     * were issued on particular date. Display all the information related to that 
     * invoice including all the attributes from the invoice table, and all the attributes from the purchase table. 
     * The output should be two tables one on top of the other.
     * Outputs: displays on the screen the required tuples. Display the invoice tuple on top. 
     * Then display the purchase matching tuples underneath in a table format.
     * Action: searches the database for the required information.
     * Error handling: displays an error message if the rewards number does not
     * exist or if the date inputted is not a valid date.
     * @param memberNo integer containing the rewards number for the member
     * @param invoiceDate a String containing the purchase date of the invoice.
     * @return none.
     */
    public void searchForInvoice(int memberNo,  String invoiceDate) {
    }
    /** This method implements Start a new invoice:
     *  generate an invoice to record a user’s purchase.
     *  Assume that the productid[0] has quantity[0].
     *  Outputs: A message informing the user that the purchase was successful and 
     *  include the invoice number for later reference.
     *  
     *  Action: This function mimics a purchase for the user. 
     *  
     *  Calculates the subtotal for each product: 
     *  Retrieves the price of the product from the database. Subtotal = price *quantity
     *  
     *  Calculates the number of items and the total for the invoice. 
     *  
     *  Calculates the new invoice number by finding the highest invoice number in the database and incrementing it.
     *  
     *  Inserts a new invoice tuple into the invoice table using the calculated invoice number, calculated total, 
     *  calculated number of items, today’s date for the dateofPurchase, now for the timeofPurchase, 
     *  the inputted rewards number, the inputted store ID.
     *  
     *  For each purchased product, it inserts a new purchase tuple using the calculated invoice number, 
     *  inputted ProductID, inputted quantity, and calculated subtotal.
     *  
     *  Error handling: displays an error message in each of the following cases:
     *  Rewards number is not valid.
     *  Store ID is not valid.
     *  Product ID is not valid.
     *  Quantity is less than or equal to zero. 
     *  @param memberNo integer containing member's rewards number.
     *  @param storeID integer containing the store ID
     *  @param productIDs an array containing the productIDs of the products included in the purchase
     *  @param quantity an array containing the corresponding quantities of the products in productIDs array.
     *  @return none.
     * @throws SQLException 
     */
    public void addInvoice(int memberNo, int storeID, int[] productIDs, int[] quantity ) throws SQLException {
    	Properties info = new Properties();
    	info.put("user", "u263730");
    	info.put("password", "p263730");
    	String schema = "schema263730";
    	Connection conn = DriverManager.getConnection("jdbc:mysql://COMPDBS300/" + schema, info);
    	
    	/*
    	 * checking the validity of the RewardNo
    	 */
    	PreparedStatement pcheck = conn.prepareStatement("select RewardsNo from member");
    	ResultSet rstcheck = pcheck.executeQuery();
    	ArrayList<Integer> listCode = new ArrayList<Integer>();
    	while(rstcheck.next()) {
    		listCode.add(rstcheck.getInt(1));
    	}
    	boolean isok = true;
    
    	for(int i = 0; i < listCode.size(); i++) {
    		isok |= memberNo == listCode.get(i);
    	}
    	if(!isok) {
    		throw new SQLException("RewardsNo is not valid.");
    	}
    	
    	/*
    	 * checking if the quantity is valid
    	 */
    	for(int i : quantity) {
    		if(i < 0) {
    			throw new SQLException("Quantity should be positive");
    		}
    	}
    	
    	/*
    	 * checking if productID and storeID are valid
    	 */
    	PreparedStatement pcheck1 = conn.prepareStatement("select ProductID from Product");
		PreparedStatement pcheck2 = conn.prepareStatement("select code from Store");
    	ResultSet rstcheck1 = pcheck1.executeQuery();
    	ResultSet rstcheck2 = pcheck2.executeQuery();
    	ArrayList<Integer> listID = new ArrayList<>();
    	ArrayList<Integer> code = new ArrayList<>();
    	while(rstcheck2.next()) {
    		code.add(rstcheck2.getInt(1));
    	}
	    	while(rstcheck1.next()) {
    		listID.add(rstcheck1.getInt(1));
    	}
    	boolean isok1inner = false;
    	boolean isok2 = true;
    	boolean isok1outer = true;
    	
    	for(int i = 0; i < code.size(); i++) {
    		isok2 |= storeID == code.get(i);
    	}
    	/*
    	 * to be work again!
    	 */
    	for(int i = 0; i < listID.size(); i++) {
    		for(int j = i; j < productIDs.length; j++) {
    			isok1inner |= productIDs[j] == listID.get(i);
    		}
    		isok1outer &= isok1inner;
    	
    	}
    	if(!isok1outer) {
    		throw new SQLException("This ProductID is not valid");
    	}
    	if(!isok2) {
    		throw new SQLException("The storeID is not valid");
    	}
    	ArrayList<Double> subtotals = new ArrayList<>();
    	for(int i = 0; i < productIDs.length; i++) {
    		PreparedStatement p = conn.prepareStatement("select price from product where ProductID = ?;");
    		p.setInt(1, productIDs[i]);
    		ResultSet rst = p.executeQuery();
    		rst.next();
    		subtotals.add(Double.valueOf(rst.getString(1)) * quantity[i]);
    	}
    	
    	int newNumItems = productIDs.length;
    	int newInvoiceTotal = 0;
    	for(Double d : subtotals) {
    		newInvoiceTotal += d;
    	}
    	
    	PreparedStatement ck = conn.prepareStatement("select InvoiceID from Invoice;");
    	ResultSet rstck = ck.executeQuery();
    	ArrayList<Integer> list = new ArrayList<>();
    	
    	while(rstck.next()) {
    		list.add(rstck.getInt(1));
    	}
    	int max = 0;
    	for(int i = 0; i < list.size(); i++) {
    		if(list.get(i) > max) max = list.get(i);
    	}
    	int newInvoiceID = max + 1;
    	
    	PreparedStatement pstmt = conn.prepareStatement("insert into Invoice values(?,?,?,current_date(),current_time(),?,?);");
    	pstmt.setInt(1, newInvoiceID);
    	pstmt.setDouble(2,newInvoiceTotal);
    	pstmt.setInt(3, newNumItems);
    	pstmt.setInt(4, memberNo);
    	pstmt.setInt(5, storeID);
    	
    	if(pstmt.executeUpdate()>0) System.out.println("invoice succesfully inserted with ID " + max);
    	
    }
    /** This method implements Add a new member to the grocery store:
     * Outputs: A message indicating that the insertion was successful.
     * Action: Inserts a new member into the database.
     * Error Handling: Display an error message when the Rewards number already exists in the database
     * @param memberNo integer containing the new member's rewards number
     * @param fname String containing the member's first name
     * @param lname String containing the member's last name
     * @param email String containing the member's email address.
     * @param address String containing the member's address
     * @return none.
     * @throws SQLException 
	 */
    public void addMember(int memberNo, String fname, String lname, String email, String address) {
    	
    	String sql1 = "insert into member values(?,?,?,?,?);";
    	try {
    		
    		PreparedStatement pcheck = conn.prepareStatement("select RewardsNo from member");
        	ResultSet rstcheck = pcheck.executeQuery();
        	ArrayList<Integer> listCode = new ArrayList<Integer>();
        	while(rstcheck.next()) {
        		listCode.add(rstcheck.getInt(1));
        	}
        	boolean isok = true;
        
        	for(int i = 0; i < listCode.size(); i++) {
        		isok &= memberNo != listCode.get(i);
        	}
        	if(isok) {
		    	PreparedStatement p = conn.prepareStatement(sql1);
		    	p.setInt(1,memberNo);
		    	p.setString(2, fname);
		    	p.setString(3, lname);
		    	p.setString(4, email);
		    	p.setString(5, address);
		    	if(p.executeUpdate()>0) System.out.println("account sucessfully created!");
        	}
        	else {
        		throw new SQLException("This reward number is already used");
        	}
    	}catch(SQLException e) {
    		System.out.println(e.getMessage());
    	}
    }
    /** This method implements: Add a new product to the grocery store: 
     * Inputs: ProductID, department, name, brand, price, cost, quantity for the product table. 
     * Outputs: A message indicating that the product is inserted successfully.
     * Action: 
     * Inserts the new product into the product table.
     * Inserts the new product inventory into the inventory table. 
     * Error Handling: Display an error message in the following cases:
     * ProductID already exists in the product table
     * Price < cost
     * Quantity is not equal to QtyonHand.
     * Store ID is not valid.
     * @param productID ID of the product to be inserted
     * @param department String containing department
     * @param name String containing product name
     * @param brand String containing product brand
     * @param price double price to sell
     * @param cost double price paid for the product
     * @param quantity total quantity in all stores
     * @param storeID int ID of the store that has this product in stock
     * @param qtyOnHand int quantity on hand at this store
     * @return none
     * @throws SQLException 
	*/
    public void addProduct(int productID, String department, String name, String brand, double price,
    		double cost, int quantity, int storeID, int qtyOnHand) throws SQLException {	
    	String sql1 = "insert into product values(?,?,?,?,?,?,?);";
    	String sql2 = "insert into inventory values(?,?,?);";
    	
    	try {
    		if(price < cost) {
    			throw new SQLException("Price can not be greater than cost");
    		}
    		if(quantity != qtyOnHand) {
    			throw new SQLException("The quantity in the store and the inventory doesnt match");
    		}
    		
    		PreparedStatement pcheck1 = conn.prepareStatement("select ProductID from Product");
    		PreparedStatement pcheck2 = conn.prepareStatement("select code from Store");
	    	ResultSet rstcheck1 = pcheck1.executeQuery();
	    	ResultSet rstcheck2 = pcheck2.executeQuery();
	    	ArrayList<Integer> listID = new ArrayList<>();
	    	ArrayList<Integer> code = new ArrayList<>();
	    	while(rstcheck2.next()) {
	    		code.add(rstcheck2.getInt(1));
	    	}
 	    	while(rstcheck1.next()) {
	    		listID.add(rstcheck1.getInt(1));
	    	}
	    	boolean isok1 = true;
	    	boolean isok2 = true;
	    	
	    	for(int i = 0; i < code.size(); i++) {
	    		isok2 &= storeID != code.get(i);
	    	}
	    	for(int i = 0; i < listID.size(); i++) {
	    		isok1 &= productID != listID.get(i);
	    	}
	    	if(!isok1) {
	    		throw new SQLException("This ProductID is already used");
	    	}
	    	else {
		    	PreparedStatement p1 = conn.prepareStatement(sql1);
		    	p1.setInt(1,productID);
		    	p1.setString(2, department);
		    	p1.setString(3, name);
		    	p1.setString(4, brand);
		    	p1.setDouble(5, price);
		    	p1.setDouble(6, cost);
		    	p1.setDouble(7, quantity);
		    	if(isok2) {
		    		throw new SQLException("This storeID does not exist");
		    	}else {
				    if(p1.executeUpdate()>0) {
				    	PreparedStatement p2 = conn.prepareStatement(sql2);
					    p2.setDouble(1, storeID);
					    p2.setDouble(2, productID);
					    p2.setDouble(3, qtyOnHand);
					    if(p2.executeUpdate()>0) System.out.println("product successfully added");
				    	}
		    		 }
	    		 }
    }catch(SQLException e) {
    	System.out.println(e.getMessage());
    }
   }

    /** This method implements Update product quantity:
     * Inputs: 
     * ProductID, overall quantity to use to update the product table
     * Per inventory entry, storeID and additional QtyOnHand
     * Outputs: A message indicating that the update was successful.
     * Action: Performs the following steps:
     * 
     * Updates the product table for the specified productID by adding the specified quantity to the
     * quantity in the database.
     * 
     * Updates the inventory table for each specified store by adding the specified QtyOnHand 
     * to the stored QtyOnHand. 
     * 
     * If an inventory tuple does not exist for this store, 
     * this method should insert a new inventory tuple. Assume that qtyAtStore[i] 
     * corresponds to the additional quantity to be added to the inventory at store with storeID[i]
     * 
     * Error Handling: Display an error message in the following cases:
     * ProductID does not exist in the database
     * StoreID does not exist in the database
     * @param productID the ID of the product the stores purchased more of
     * @param overallQty the quantity to be added to the product table's quantity
     * @param storeID an array of store IDs
     * @param qtyAtStore an array of additional quantities for the stores specified in storeID array
     * @return none.
     * @throws SQLException 
     */
    public void updateQuantity(int productID, int overallQty, int[] storeID, int[] qtyAtStore) throws SQLException {
   
    		String sql1 = "update Product set quantity = ? where ProductID = ?";
    	    String sql2 = "update Inventory set QtyInStore = ? where Store_ID = ?";
    	    String sqlcheck1 = "select ProductID from Product where productID = ?";
    	    String sqlcheck2 = "select code from Store";
    	    
    	    PreparedStatement pcheck1 = conn.prepareStatement(sqlcheck1);
    		pcheck1.setInt(1, productID);
    		ResultSet rst = pcheck1.executeQuery();
    		if(!rst.next()) throw new SQLException("Enter a valid productID");
    		
    		PreparedStatement pcheck2 = conn.prepareStatement(sqlcheck2);
    		ResultSet rstcheck2 = pcheck2.executeQuery();
    		ArrayList<Integer> storeDBID = new ArrayList<>();
    		for(int i = 0; i < storeID.length; i++) {
    			rstcheck2.next();
    			storeDBID.add(rstcheck2.getInt(1));
    		}
    		boolean inner = false;
    		boolean outer = true;
    		for(int i = 0; i < storeID.length; i++) {
    			for(int j = i; j < storeDBID.size(); j++) {
    				inner |= storeID[i] == storeDBID.get(i);
    			}
    			outer &= inner;
    		}
    		if(!outer) throw new SQLException("Invalid storeID(s)");
    		PreparedStatement pquery1 = conn.prepareStatement(sql1);
    		pquery1.setInt(1, overallQty);
    		pquery1.setInt(2,productID);
    		
    		if(pquery1.executeUpdate() > 0) {
    			PreparedStatement pquery2 = conn.prepareStatement(sql2);
    			for(int i = 0; i < storeID.length; i++) {
    					pquery2.setInt(2, storeID[i]);
    					pquery2.setInt(1, qtyAtStore[i]);
    					
    				}
    			if (pquery2.executeUpdate() > 0) System.out.println("update successfiul!");
    		}
    		
    }
    /**
     * This method implements the functionality necessary to exit the application: 
     * this should allow the user to cleanly exit the application properly.
     * This should close the connection and any prepared statements.  
     * @throws SQLException 
     */
    public void exitApplication() throws SQLException {
    	this.conn.close();
    	
    }
    
    /**
     * This is the main method that should test all the methods above.
     * It is sufficient to call each method above once.  This method should not throw any exceptions.
     * @throws SQLException 
     */
     public static void main(String args[]) throws SQLException {
    	 Project3 p = new Project3("u263730","p263730","schema263730");
//    	 p.addMember(166,"jonh","wesley","wesljo@gmail.com","23 Walnut Street, Pittsburgh, PA 15232");
//    	 p.addProduct(1666, "Boullette", "Chips", "Cracks", 7.99, 4.99, 10, 93,100);
    	 int [] storeIDs = {33,44,55};
    	 int [] quantity = {4,5,6};
//    	 p.addInvoice(166, 33, productIDs, quantity);
    	 p.updateQuantity(1200, 666, storeIDs, quantity);
    	 

     }
}
