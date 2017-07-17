package com.example.demo;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.bean.Port;
import com.example.bean.Requests;
import com.example.bean.Users;
import com.example.demo.dao.DemoDao;
import com.example.demo.mail.EmailService;

@SpringBootApplication
@Controller
@EnableAutoConfiguration
public class DemoApplication {
	
    @Autowired
    private JdbcTemplate jdbcTemplate;
    
    @Autowired
    @Qualifier("DemoDaoImpl")
    private DemoDao demodao;
    
    @Autowired
    @Qualifier("EmailServiceImpl")
    private EmailService emailService;
    

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}
	
    @RequestMapping("/")
    String index() {
        return "index";
    }
    
	/**
	 * 
	 * @param name
	 * @param pwd
	 * @return
	 */
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public String loginByPost(HttpSession httpSession, @RequestParam(value = "username", required = true) String username,
			@RequestParam(value = "password", required = true) String password,
			Model model) {
		System.out.println("post " + username + " " + password);
        model.addAttribute("username", username);
        httpSession.setAttribute("username", username);
//        String sql = "SELECT PASSWORD FROM USERS where NAME = '" + username + "'";
//        System.out.println(sql);
//        List list = (List<String>) jdbcTemplate.query(sql, new RowMapper<String>(){
//
//            @Override
//            public String mapRow(ResultSet rs, int rowNum) throws SQLException {
//                return rs.getString("PASSWORD");
//            }
//
//        });
//        System.out.println(list.get(0));

		Users user = demodao.AuthenticateUser(username, password);
		if(user == null){
	        model.addAttribute("msg", "Your are not authenticated!");
			return "index";
		}
        httpSession.setAttribute("userid", user.getID());
        httpSession.setAttribute("useremailid", user.getEmail());

		// get requests by user
		// 1. get my requests
		List<Requests> listOfMyRequests = demodao.getMyRequests(user.getID());
//        List<Requests> listOfMyRequests = new ArrayList<Requests>();
//        Requests req = new Requests();
//        req.setPort("1010");
//        req.setOperator("oz");
//        req.setStatus("Pending");
//        listOfMyRequests.add(req);
        model.addAttribute("myRequests", listOfMyRequests);
        
		List<Requests> listOfRequestsToMe = demodao.getRequestsToMe(user.getID());
        List<Requests> pendingRequests = new ArrayList<Requests>();
        List<Requests> acceptedRequests = new ArrayList<Requests>();
		for(Requests req: listOfRequestsToMe){
			if("Pending".equalsIgnoreCase(req.getStatus())){
				pendingRequests.add(req);
			}else if("Accepted".equalsIgnoreCase(req.getStatus())){
				acceptedRequests.add(req);
			}
		}
//        List<Requests> pendingRequests = new ArrayList<Requests>();
//        Requests reqp = new Requests();
//        reqp.setPort("2020");
//        reqp.setOperator("oz");
//        reqp.setStatus("Pending");
//        pendingRequests.add(reqp);
//        reqp = new Requests();
//        reqp.setPort("3030");
//        reqp.setOperator("oz");
//        reqp.setStatus("Pending");
//        pendingRequests.add(reqp);
        model.addAttribute("pendingRequests", pendingRequests);
        
//        List<Requests> acceptedRequests = new ArrayList<Requests>();
//        Requests reqa = new Requests();
//        reqa.setPort("4040");
//        reqa.setOperator("oz");
//        reqa.setStatus("Accepted");
//        acceptedRequests.add(reqa);
//        reqa = new Requests();
//        reqa.setPort("5050");
//        reqa.setOperator("oz");
//        reqa.setStatus("Accepted");
//        acceptedRequests.add(reqa);
        model.addAttribute("acceptedRequests", acceptedRequests);
        
		return "detail";
	}
    
    
    @RequestMapping(value = "/helloa",method = RequestMethod.GET)
    public String hello(Model model) {
        model.addAttribute("name", "Dear");
        return "detail2";
    }
    
    @RequestMapping(value = "/acceptRequest/{requestID}",method = RequestMethod.POST)
    public String acceptRequest(HttpSession httpSession, @PathVariable("requestID") int requestID, Model model) {
    	
    	
    	System.out.println("request id = " + requestID);
    	String username = (String)httpSession.getAttribute("username");
    	int userid = (int)httpSession.getAttribute("userid");
    	String useremailid = (String)httpSession.getAttribute("useremailid");
    	System.out.println("session " + username);
    	//TODO update db to set this port as
        model.addAttribute("username", username);
        //TODO call service to accept
        
        //--------------------------
        Users user = demodao.getOwnerEmail(requestID);
        Port port = demodao.getPorts(requestID);
        //send email
    	emailService.sendSimpleMail(useremailid, user.getEmail(), port.getPort(), "Accepted");
        // updata status
    	demodao.updateRequestStatus(requestID, "Accepted");
    	refreshPage(model, userid);
    	model.addAttribute("msg", "Request has been accepted!");
        return "detail";
    }
    
    @RequestMapping(value = "/rejectRequest/{requestID}",method = RequestMethod.POST)
    public String rejectRequest(HttpSession httpSession, @PathVariable("requestID") int requestID, Model model) {
    	System.out.println("request id = " + requestID);
    	String username = (String)httpSession.getAttribute("username");
    	int userid = (int)httpSession.getAttribute("userid");
    	System.out.println("session " + username);
    	//TODO update db to set this port as
        model.addAttribute("username", username);
    	String useremailid = (String)httpSession.getAttribute("useremailid");

        Users user = demodao.getOwnerEmail(requestID);
        Port port = demodao.getPorts(requestID);
        //send email
    	emailService.sendSimpleMail(useremailid, user.getEmail(), port.getPort(), "Rejected");
    	demodao.updateRequestStatus(requestID, "Rejected");
    	refreshPage(model, userid);
    	model.addAttribute("msg", "Request has been rejected!");
        return "detail";
    }
    
    @RequestMapping(value = "/deleteRequest/{requestID}",method = RequestMethod.POST)
    public String deleteRequest(HttpSession httpSession, @PathVariable("requestID") int requestID, Model model) {
    	System.out.println("request id = " + requestID);
    	String username = (String)httpSession.getAttribute("username");
    	int userid = (int)httpSession.getAttribute("userid");
    	System.out.println("session " + username);
    	//TODO update db to set this port as
        model.addAttribute("username", username);
    	String useremailid = (String)httpSession.getAttribute("useremailid");
        Users user = demodao.getOwnerEmail(requestID);
        Port port = demodao.getPorts(requestID);
        //send email
    	emailService.sendSimpleMail(useremailid, user.getEmail(), port.getPort(), "Deleted");
    	demodao.deleteRequest(requestID);
    	refreshPage(model, userid);
    	model.addAttribute("msg", "Request has been rejected!");
        return "detail";
    }
    
    private void refreshPage(Model model, int userid){
		List<Requests> listOfMyRequests = demodao.getMyRequests(userid);
        model.addAttribute("myRequests", listOfMyRequests);
		List<Requests> listOfRequestsToMe = demodao.getRequestsToMe(userid);
        List<Requests> pendingRequests = new ArrayList<Requests>();
        List<Requests> acceptedRequests = new ArrayList<Requests>();
		for(Requests req: listOfRequestsToMe){
			if("Pending".equalsIgnoreCase(req.getStatus())){
				pendingRequests.add(req);
			}else if("Accepted".equalsIgnoreCase(req.getStatus())){
				acceptedRequests.add(req);
			}
		}
        model.addAttribute("pendingRequests", pendingRequests);
        model.addAttribute("acceptedRequests", acceptedRequests);
    }
    
    
	@RequestMapping(value = "/searchPort", method = RequestMethod.POST)
	@ResponseBody
	public String searchPort(HttpSession httpSession, @RequestBody Port port, Model model) throws Exception {
		System.out.println("post search port = " + port.getPort() );
//    	String username = (String)httpSession.getAttribute("username");
//    	int userid = (int)httpSession.getAttribute("userid");
        
		Users user = demodao.getOwnerByPort(port.getPort());
		System.out.println("post search name = " + user.getID() + " " + user.getName());
		JSONObject json = new JSONObject();
		json.put("name", user.getName());
		json.put("id", user.getID());
		json.put("email", user.getEmail());
		return json.toString();
	}
	
	@RequestMapping(value = "/newRequest", method = RequestMethod.POST)
	public String newRequest(HttpSession httpSession, @RequestParam(value = "port", required = true) String port, 
			@RequestParam(value = "ownerid", required = true) int ownerid, @RequestParam(value = "email", required = true) String email, Model model) {
		System.out.println("post newRequest " + port + " " + ownerid);
    	String username = (String)httpSession.getAttribute("username");
    	int userid = (int)httpSession.getAttribute("userid");
    	System.out.println("session " + username);
    	//TODO update db to set this port as
        model.addAttribute("username", username);
        
    	emailService.sendSimpleMail("", email, port, "New");
    	demodao.newRequest(userid, ownerid, port);
    	refreshPage(model, userid);
    	model.addAttribute("msg", "Your requeste has been sent to the owner!");
		
		return "detail";
	}
    
    
}
