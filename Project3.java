
import java.sql.*;
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

    	Properties info = new Properties();
    	info.put("user", username);
    	info.put("password", password);
    	
		conn = DriverManager.getConnection("jdbc:mysql://COMPDBS300/"+schema, info);

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
     * @throws SQLException 
     */
    public void searchForStore(int productID1, int productID2) throws SQLException {
			PreparedStatement check1 = conn.prepareStatement("select * from product where productid = "
					+ String.valueOf(productID1));
			PreparedStatement check2 = conn.prepareStatement("select * from product where productid = "
					+ String.valueOf(productID2));
			ResultSet rst1 = check1.executeQuery();
			ResultSet rst2 = check2.executeQuery();

			if(!rst1.next()) {
				System.err.println("Product "+productID1+" does not exist");
				return;
			}
			if(!rst2.next()) {
				System.err.println("Product "+productID2+" does not exist");
				return;
			}

			check1.close();
			check2.close();
			
			PreparedStatement ps = conn.prepareStatement("select name, location from store where"
					+ " code in (select store_id from inventory where productid = ? and qtyInStore > 0) and code in "
					+ "(select store_id from inventory where productid = ? and qtyInStore > 0)");
			ps.setString(1, String.valueOf(productID1));
			ps.setString(2, String.valueOf(productID2));
			
			ResultSet rst = ps.executeQuery();
			
			if(!rst.next()) {
				System.out.println("No stores contained both products "+productID1+" and "+productID2);
			}
			else {
				//since the next has already been called, there is no need to call it before the loop
				do {
					StringBuilder tuple = new StringBuilder();
					tuple.append(rst.getString(1) + "\t");
					tuple.append(rst.getString(2) + "\t");
					System.out.println(tuple.toString());
				}while(rst.next());
			}
			ps.close();
    	
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
     * @throws SQLException 
     */
    public void searchForCoupon(String keyword) throws SQLException {
    	PreparedStatement ps = conn.prepareStatement("select couponcode, productID, name, brand, price, "
    			+ "discount, expDate from coupon natural join product where expDate >= curdate() and name like '%" + keyword + "%'");
    	
    	ResultSet rst = ps.executeQuery();
    	ResultSetMetaData rsmd = rst.getMetaData();
		if(!rst.next()) {
			System.out.println("No coupons were found with the keyword \""+keyword+"\"");
		}
		else {
			//since the next has already been called and tested, there is no need to call it before the loop
	    	System.out.format("%-12s%-12s%-12s%-12s%-12s%-12s%-12s", rsmd.getColumnName(1), rsmd.getColumnName(2), 
	    			rsmd.getColumnName(3), rsmd.getColumnName(4), rsmd.getColumnName(5), rsmd.getColumnName(6), rsmd.getColumnName(7));
	    	System.out.println();
	    
	    	do {
	    		System.out.format("%-12s%-12s%-12s%-12s%-12s%-12s%-12s", rst.getString(1), rst.getString(2), rst.getString(3), rst.getString(4),
	    				rst.getString(5), rst.getString(6), rst.getString(7));
	    		System.out.println();
	    	} while(rst.next());
		}
    	ps.close();
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
     * @throws SQLException 
     */
    public void searchForInvoice(int memberNo,  String invoiceDate) {
    	//make sure the date is in a valid format
    	try{Date.valueOf(invoiceDate);}
    	catch(Exception e){
    		System.err.println("Invalid date");
    		return;
    	}
    	
    	try{
    		//check to make sure the member exists
    		PreparedStatement check1 = conn.prepareStatement("select * from member where RewardsNo = "
					+ String.valueOf(memberNo));
			ResultSet rst1 = check1.executeQuery();
			
			if(!rst1.next()) {
				System.err.println("Member "+memberNo+" does not exist");
				return;
			}
			
			check1.close();
			
			//check to make sure the invoice exists
    		PreparedStatement check2 = conn.prepareStatement("select invoiceID from invoice where RewardsNo = "+memberNo+" and dateOfPurchase = \""+invoiceDate+"\"");
			ResultSet rst2 = check2.executeQuery();
	
			if(!rst2.next()) {
				System.out.println("No invoices are recorded for "+memberNo+" on "+invoiceDate);
				return;
			}
			
			check2.close();
			
			//this gets the invoice table rows that meet the specs
			PreparedStatement ps = conn.prepareStatement("select InvoiceID, InvoiceTotal, NoItems, dateOfPurchase, timeOfPurchase, RewardsNo, StoreID from invoice join purchase on(invoiceid = invoiceno)"
					+ " left outer join coupon using(couponcode) where rewardsno = " + memberNo + " and dateOfPurchase = \"" + invoiceDate + "\"");
			//this gets the purchase table rows that meet the specs
			PreparedStatement ps2 = conn.prepareStatement("select InvoiceNo, purchase.ProductID, CouponCode, quantity, subtotal from invoice join purchase on(invoiceid = invoiceno)"
					+ " left outer join coupon using(couponcode) where rewardsno = " + memberNo + " and dateOfPurchase = \"" + invoiceDate + "\"");
			
			//print out the invoice data first
			ResultSet rst = ps.executeQuery();
	    	ResultSetMetaData rsmd = rst.getMetaData();
	    	
	    	//prints the column headers
	    	System.out.format("%-12s%-15s%-9s%-15s%-15s%-12s%-12s", rsmd.getColumnName(1), rsmd.getColumnName(2), 
	    			rsmd.getColumnName(3), rsmd.getColumnName(4), rsmd.getColumnName(5), rsmd.getColumnName(6), rsmd.getColumnName(7));
	    	System.out.println();
	    	
	    	//and then the values of each row
	    	while(rst.next()) {
	    		System.out.format("%-12s%-15s%-9s%-15s%-15s%-12s%-12s", rst.getString(1), rst.getString(2), rst.getString(3), rst.getString(4),
	    				rst.getString(5), rst.getString(6), rst.getString(7));
	    		System.out.println();
	    	}
	    	System.out.println();
	    	
	    	//now print the purchase data
	    	ResultSet rst3 = ps2.executeQuery();
	    	ResultSetMetaData rsmd2 = rst3.getMetaData();
	    	
	    	//print the column headers
	    	System.out.format("%-12s%-12s%-12s%-12s%-12s", rsmd2.getColumnName(1), rsmd2.getColumnName(2), 
	    			rsmd2.getColumnName(3), rsmd2.getColumnName(4), rsmd2.getColumnName(5));
	    	System.out.println();
	    	//and then the values of each row
	    	while(rst3.next()) {
	    		System.out.format("%-12s%-12s%-12s%-12s%-12s", rst3.getString(1), rst3.getString(2), rst3.getString(3), rst3.getString(4),
	    				rst3.getString(5));
	    		System.out.println();
	    	}
	    	System.out.println();
	    	
	    	//all done, close the prepared statments
	    	ps.close();
	    	ps2.close();
		}catch(Exception ex) {ex.printStackTrace();}
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
    public void addInvoice(int memberNo, int storeID, int[] productIDs, int[] quantity ){
    	//check if the memberNo is valid
    	try {
    		PreparedStatement check1 = conn.prepareStatement("select * from member where RewardsNo = "
					+ String.valueOf(memberNo));
			ResultSet rst1 = check1.executeQuery();
	
			if(!rst1.next()) {
				System.err.println("Member "+memberNo+" does not exist");
				return;
			}
			
			check1.close();
	    	
	    	//check if the quantity is non-negative and non-zero
	    	for(int i : quantity) {
	    		if(i <= 0) {
	    			System.err.println("Quantity should be positive");
	    			return;
	    		}
	    	}
	    	
	    	//check if the store ID is valid
	    	PreparedStatement pcheck2 = conn.prepareStatement("select code from Store");
		    ResultSet rstcheck2 = pcheck2.executeQuery();
		    if(!rstcheck2.next()) {
				System.err.println("The store ID "+storeID+" is not valid");
				return;
			}
		    //start checking if the product IDs are valid
		    PreparedStatement pcheck1 = conn.prepareStatement("select ProductID from Product");
		    ResultSet rstcheck1 = pcheck1.executeQuery();
		    ArrayList<Integer> listID = new ArrayList<>();
		    //store the product IDs
			while(rstcheck1.next()) {
		    	listID.add(rstcheck1.getInt(1));
		    }
			//check each of the inputed productIDs against the list from the database
			boolean hasMatch;
		    for(int i = 0; i < productIDs.length; i++) {
		    	hasMatch = false;
		    	for(int j = i; j < listID.size(); j++) {
		    		if(productIDs[i] == listID.get(j)) {
		    			hasMatch = true;
		    			break;
		    		}
		    	}
		    	//if any of them don't exist, end
		    	if(!hasMatch) {
		    		System.err.println("The product ID "+productIDs[i]+" is not valid");
		    		return;
		    	}
		    }
		    pcheck2.close();
		    pcheck1.close();
		    
		    //get the subtotals
	    	ArrayList<Double> subtotals = new ArrayList<>();
	    	for(int i = 0; i < productIDs.length; i++) {
	    		PreparedStatement p = conn.prepareStatement("select price from product where ProductID = ?;");
		    	p.setInt(1, productIDs[i]);
		    	ResultSet rst = p.executeQuery();
		    	rst.next();
		    	subtotals.add(Double.valueOf(rst.getString(1)) * quantity[i]);
		    	p.close();
	    	}
	    	
	    	//get the number of items
	    	int newNumItems = productIDs.length;
	    	
	    	//sum the subtotals to get the total
	    	int newInvoiceTotal = 0;
	    	for(Double d : subtotals) {
	    		newInvoiceTotal += d;
	    	}
	    	
	    	//get all invoice IDs
	    	PreparedStatement ck = conn.prepareStatement("select InvoiceID from Invoice;");
	    	ResultSet rstck = ck.executeQuery();
	    	
	    	//find the largest ID
	    	int max = 0;
	    	while(rstck.next()) {
	    		if(rstck.getInt(1) > max) max = rstck.getInt(1);
	    	}
	    	
	    	//new invoice ID is one larger
	    	int newInvoiceID = max + 1;
	    	ck.close();
	    	
	    	//insert the completed statement into the table
	    	PreparedStatement pstmt = conn.prepareStatement("insert into Invoice values(?,?,?,current_date(),current_time(),?,?);");
	    	pstmt.setInt(1, newInvoiceID);
	    	pstmt.setDouble(2,newInvoiceTotal);
	    	pstmt.setInt(3, newNumItems);
	    	pstmt.setInt(4, memberNo);
	    	pstmt.setInt(5, storeID);
	    	
	    	//report the (potentially) successful insertion to the user
	    	if(pstmt.executeUpdate()>0) System.out.println("invoice succesfully inserted with ID " + newInvoiceID);
	    	pstmt.close();
    	}catch(Exception ex) {ex.printStackTrace();}
    	
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
    	try {
    		//make sure that noone already has that member number
    		PreparedStatement check1 = conn.prepareStatement("select * from member where RewardsNo = "
					+ String.valueOf(memberNo));
			ResultSet rst1 = check1.executeQuery();
	
			if(rst1.next()) {
				System.err.println("The reward number "+memberNo+" is already in use");
				return;
			}
			check1.close();
			
			//add the member
        	PreparedStatement p = conn.prepareStatement("insert into member values(?,?,?,?,?);");
		    p.setInt(1,memberNo);
		    p.setString(2, fname);
		    p.setString(3, lname);
		    p.setString(4, email);
		    p.setString(5, address);
		    if(p.executeUpdate()>0) System.out.println("account sucessfully created!");
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
    		double cost, int quantity, int storeID, int qtyOnHand){	
    	
    	try {
    		//check the price
    		if(price < cost) {
    			System.err.println("Price can not be greater than cost");
    			return;
    		}
    		//make sure the amount of the product checks out
    		if(quantity != qtyOnHand) {
    			System.err.println("The quantity in the store and the inventory doesnt match");
    			return;
    		}
    		
    		//check for any existing product with the same ID
    		PreparedStatement check1 = conn.prepareStatement("select * from product where ProductID = "
					+ String.valueOf(productID));
			ResultSet rst1 = check1.executeQuery();
	
			if(rst1.next()) {
				System.err.println("The product ID "+productID+" is already in use");
				return;
			}
			check1.close();
			//make sure the store exists
			PreparedStatement check2 = conn.prepareStatement("select * from store where code = "
					+ String.valueOf(storeID));
			ResultSet rst2 = check2.executeQuery();
	
			if(!rst2.next()) {
				System.err.println("Store "+storeID+" does not exist");
				return;
			}
			
			check2.close();
			
			//create the product
		    PreparedStatement p1 = conn.prepareStatement("insert into product values(?,?,?,?,?,?,?);");
		    p1.setInt(1,productID);
		    p1.setString(2, department);
		    p1.setString(3, name);
		    p1.setString(4, brand);
		    p1.setDouble(5, price);
		    p1.setDouble(6, cost);
		    p1.setDouble(7, quantity);
		    
		    //if the product created properly, create the inventory row
		    if(p1.executeUpdate()>0) {
			    PreparedStatement p2 = conn.prepareStatement("insert into inventory values(?,?,?);");
				p2.setDouble(1, storeID);
				p2.setDouble(2, productID);
				p2.setDouble(3, qtyOnHand);
				if(p2.executeUpdate()>0) System.out.println("product successfully added");
				p2.close();
			}
		    p1.close();
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
    public void updateQuantity(int productID, int overallQty, int[] storeID, int[] qtyAtStore){
    	try {
    		//check to make sure the product exists in the first place
    		PreparedStatement pcheck1 = conn.prepareStatement("select ProductID from Product where productID = ?");
    		pcheck1.setInt(1, productID);
    		ResultSet rst = pcheck1.executeQuery();
    		if(!rst.next()) System.err.println("Product "+productID+" does not exist");
    		
    		//make sure all the stores exist
    		PreparedStatement pcheck2 = conn.prepareStatement("select code from Store");
    		ResultSet rstcheck2 = pcheck2.executeQuery();
    		
    		//create a list of all the store codes from the database
    		ArrayList<Integer> storeDBID = new ArrayList<>();
    		while(rstcheck2.next()) {
    			storeDBID.add(rstcheck2.getInt(1));
    		}
    		
    		//make sure all of your codes are in the database
    		boolean hasMatch;
    		for(int i = 0; i < storeID.length; i++) {
    			hasMatch = false;
    			for(int j = i; j < storeDBID.size(); j++) {
    				if(storeID[i] == storeDBID.get(i)) {
    					hasMatch=true;
    					break;
    				}
    			}
    			if(!hasMatch) {
    				System.err.println("one or more of the store IDs don't exist");
    				return;
    			}
    		}
    		//increase the quantity of that product by the overall quantity
    		PreparedStatement pquery1 = conn.prepareStatement("update Product set quantity = quantity + ? where ProductID = ?");
    		pquery1.setInt(1, overallQty);
    		pquery1.setInt(2,productID);
    		
    		//if that worked, increase the store's quantities by their respective amounts
    		if(pquery1.executeUpdate() > 0) {
    			PreparedStatement pquery2 = conn.prepareStatement("update Inventory set QtyInStore = QtyInStore + ? where Store_ID = ?");
    			int lastUpdateReturn = 0;
    			for(int i = 0; i < storeID.length; i++) {
    					pquery2.setInt(2, storeID[i]);
    					pquery2.setInt(1, qtyAtStore[i]);
    					lastUpdateReturn = pquery2.executeUpdate();
    			}
    			//let the user know of the success
    			if (lastUpdateReturn > 0) System.out.println("update successfiul!");
    		}
    	}catch(Exception ex) {ex.printStackTrace();}
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
    	 Project3 p = new Project3("u257793","p257793","schema257793");
    	 
    	 int [] storeIDs = {33,44,55};
    	 int [] productIDs = {1233, 1234, 1235};
    	 int [] quantity = {4,5,6};
    	 
    	 p.searchForStore(1233, 1234);
		 System.out.println();
		 p.searchForCoupon("coffee");
		 System.out.println();
		 p.searchForInvoice(112, "2021-03-26");
    	 p.addInvoice(113, 33, productIDs, quantity);
    	 p.addMember(166,"Jonh","Wesley","wesljo@gmail.com","23 Walnut Street, Pittsburgh, PA 15232");
    	 p.addProduct(1666, "Boullette", "Chips", "Cracks", 7.99, 4.99, 100, 55,100);
    	 p.updateQuantity(1233, 666, storeIDs, quantity);
     }
}
