import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.sql.ResultSet;
/**
 * Servlet implementation class ShoppingCartServlet
 */
@WebServlet(name = "ShoppingCartServlet", urlPatterns = "/api/cart")
public class ShoppingCartServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	
	@Resource(name = "jdbc/moviedb")
	private DataSource dataSource; //variable to determine where information is being pulled from
	
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ShoppingCartServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.setContentType("application/json");
		
		PrintWriter out = response.getWriter();
		
		String action = request.getParameter("action");
		String movie_id = request.getParameter("id");
		System.out.println(movie_id);
		System.out.println(action);
		HttpSession session = request.getSession();

		try {
			Connection dbcon = dataSource.getConnection();
			String query = "select id, title from movies where id = '" + movie_id + "'";
			
			Statement statement = dbcon.createStatement();
			
			ResultSet rs = statement.executeQuery(query);
			
			rs.next();
			String id = rs.getString("id");
			String title = rs.getString("title");
			
			HashMap<String, HashMap<String, Object>> cart = (HashMap) session.getAttribute("cart");
//			if(action.equals("add")) {
				if (cart == null) {
					cart = new HashMap<String, HashMap<String, Object>>();
					HashMap<String, Object> product_info = new HashMap<String, Object>();
					product_info.put("title", title);
					product_info.put("quantity", 1);
					cart.put(id, product_info);
				
					session.setAttribute("cart", cart);
				} else {
					synchronized(cart) {
						if(!cart.containsKey(id)) {
							HashMap<String, Object> product_info = new HashMap<String, Object>();
							product_info.put("title", title);
							product_info.put("quantity", 1);
							cart.put(id, product_info);
						} else {
							int value = (Integer) cart.get(id).get("quantity");
							HashMap<String, Object> updated_product = new HashMap<String, Object>();
							updated_product.put("title", cart.get(id).get("title"));
							updated_product.put("quantity", value+1);
							cart.put(id, updated_product);
						}
					}
				}
//			} else if(action.equals("delete")) {
//				if(cart != null) {
//					cart.remove(id);
//				}
//			}
			
			Gson gson = new Gson();
			String cartItems = gson.toJson(cart);
			out.write(cartItems.toString());
			
			response.setStatus(200);
			rs.close();
			statement.close();
			dbcon.close();
			
		}catch (Exception e) {
			JsonObject jsonObject = new JsonObject();
			jsonObject.addProperty("errorMessage", e.getMessage());
			out.write(jsonObject.toString());
			
			response.setStatus(500);
		}
		out.close();		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}