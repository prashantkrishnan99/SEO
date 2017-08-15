package practice;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/search")
public class S extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public S() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		List<Url> sd1 = new ArrayList<Url>();
	//	Test.getCosineSimilarity();
		request.getRequestDispatcher("d.jsp").forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		try {
		//List<Url> entries = List
		//List<String> list = 
			List<Url> sd1 = new ArrayList<Url>();
			List<String> entries = null;
			String s = request.getParameter("t");
		
			Search sd= new Search();
		
			sd.search(s);
			entries = sd.geturl1();
			System.out.println(entries.get(2));
			sd1 = new ArrayList<Url>();
			for(int i=0;i<entries.size();i++)
			{
				sd1.add(new Url(entries.get(i)));
			}
			//getServletContext().setAttribute("sd1", sd1);
			
			//HttpSession b1 = request.getSession();
			getServletContext().setAttribute("di", sd1);
			//b.setAttribute("di", sd1);
			request.getRequestDispatcher("d.jsp").forward(request, response);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		 }
		
	}



}
