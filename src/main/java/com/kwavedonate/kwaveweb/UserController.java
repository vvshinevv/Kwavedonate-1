package com.kwavedonate.kwaveweb;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.annotation.Resource;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.kwavedonate.kwaveweb.core.util.BcryptEncoder;
import com.kwavedonate.kwaveweb.core.util.GetIpAddress;
import com.kwavedonate.kwaveweb.user.dao.UserDaoService;
import com.kwavedonate.kwaveweb.user.vo.UserDetailsVo;

@Controller
public class UserController {

	@Autowired
	private PlatformTransactionManager transactionManager;
	
	private String check="check";

	@Autowired
	private JavaMailSender mailSender;
	
	@Resource(name = "userDaoService")
	private UserDaoService dao;

	@Resource(name = "bcryptEncoder")
	private BcryptEncoder encoder;

	/*
	 * 회원가입 페이지 이동
	 */
	@RequestMapping(value="/signin", method=RequestMethod.GET)
	public String signPage(HttpServletRequest request, HttpSession session, Model model) {
		// IP Ȯ��		
		String ipc = GetIpAddress.getClientIP(request);
		System.out.println("Web browser ���� : " +ipc);
		
		model.addAttribute("ipAddress", ipc);
		
		return "signin";
	}
	
	/*
	 * find password by email
	 */
	@RequestMapping("/findPassword")
	public String findpassword() {
		return "findPassword";
	}
	

	/*
	 * ���� ���� ������
	 */
	@RequestMapping("/myAccount")
	public String myAccount(Model model, HttpServletRequest request, Authentication authentication) {
		Locale currentLocale = LocaleContextHolder.getLocale();
		UserDetailsVo u = (UserDetailsVo) authentication.getPrincipal();
		Map<String, Object> map = new HashMap<String, Object>();

		String userEmail = u.getUsername().toString();
		map.put("userEmail", userEmail);
		map.put("currentLocale", currentLocale);
		Map<String, Object> user = dao.selectUserAccount(userEmail);
		List<Map<String, Object>> historyList = dao.selectHistoryList(map);
		model.addAttribute("user", user);
		model.addAttribute("historyList", historyList);

		return "myAccount";
	}


	/*
	 * ȸ�� ���� Controller
	 */
	@ResponseBody
	@RequestMapping(value = "/insertUser", method = RequestMethod.POST)
	public HashMap<String, Object> insertUser(HttpServletRequest request, @RequestParam("userEmail") String userEmail,
			@RequestParam("userPassword") String userPassword, @RequestParam("userName") String userName) {
		
		String ipc = GetIpAddress.getClientIP(request);
		String dbpw = encoder.encode(userPassword);
		
		Map<String, String> paramMap = new HashMap<String, String>();
		HashMap<String, Object> hashmap = new HashMap<String, Object>();
		
		paramMap.put("userEmail", userEmail);
		paramMap.put("userPassword", dbpw);
		paramMap.put("userName", userName);
		if(ipc.equals("en")) {
			paramMap.put("userNation", "ENG");
		} else if(ipc.equals("ko")) {
			paramMap.put("userNation", "KOR");
		} else {
			paramMap.put("userNation", "CHI");
		}
		int result;

		try {
			result = dao.insertUser(paramMap);
		} catch (Exception e) {
			result = 0;
		}

		if (result == 1) {
			hashmap.put("KEY", "SUCCESS");
		} else {
			hashmap.put("KEY", "FAIL");
		}


		return hashmap;
	}

	/**
	 * �ߺ�Ȯ��
	 */
	@ResponseBody
	@RequestMapping(value="/FacebookValidate", method=RequestMethod.POST)
	public HashMap<String, Object> FacebookValidate(HttpServletRequest request, @RequestParam("userEmail") String userEmail, @RequestParam("userName") String userName) {
		
		String ipc = GetIpAddress.getClientIP(request);
		String dbpw = encoder.encode(userEmail+userName+"1@#$@#!$$$#@");
		
		Map<String, Object> paramMap = new HashMap<String, Object>();
		HashMap<String, Object> hashmap = new HashMap<String, Object>();
		
		paramMap.put("userEmail", userEmail);
		paramMap.put("userName", userName);
		paramMap.put("userPassword", dbpw);
		if(ipc.equals("en")) {
			paramMap.put("userNation", "ENG");
		} else if(ipc.equals("ko")) {
			paramMap.put("userNation", "KOR");
		} else {
			paramMap.put("userNation", "CHI");
		}
		int result;

		try {
			//ó�� �����ϴ� ���
			result = dao.insertFacebookUser(paramMap);
		} catch (Exception e) {
			//�̹� ���Ե� ���
			Map<String, Object> snsMap = dao.selectIsSns(userEmail);
			int isSns = Integer.valueOf(snsMap.get("ISSNS").toString());
			if(isSns == 1){
				result = 1; //sns ���Ե� ���
			}else{
				result=0; //�α��� ����
			}
		}

		System.out.println("���Ծȵ�!" + result);
		if (result == 1) {
			hashmap.put("KEY", "SUCCESS"); //ó�� �����ϴ� ���
		}else {
			hashmap.put("KEY", "FAIL");
		}

		return hashmap;
	}
	
	// About You ����
	@ResponseBody
	@RequestMapping(value = "/modifyUser", method = RequestMethod.POST)
	public HashMap<String, Object> modifyUser(@RequestParam("userName") String userName,
			@RequestParam("phone") String phone, @RequestParam("nation") String userNation,
			@RequestParam("userEmail") String userEmail) {
		Map<String, String> paramMap = new HashMap<String, String>();
		HashMap<String, Object> hashmap = new HashMap<String, Object>();
		
		paramMap.put("authority", "ROLE_USER_" + userNation);
		paramMap.put("userName", userName);
		paramMap.put("phone", phone);
		paramMap.put("userNation", userNation);
		paramMap.put("userEmail", userEmail);
		int result;

		try {
			result = dao.modifyUser(paramMap);
		} catch (Exception e) {
			result = 0;
			e.printStackTrace();
		}

		if (result == 1) {
			hashmap.put("KEY", "SUCCESS");
		} else {
			hashmap.put("KEY", "FAIL");
		}

		return hashmap;
	}

	// Shipping Address ����
	@ResponseBody
	@RequestMapping(value = "/modifyAddress", method = RequestMethod.POST)
	public HashMap<String, Object> modifyAddress(Authentication authentication,
			@RequestParam("address1") String address1, @RequestParam("address2") String address2,
			@RequestParam("zipCode") String zipCode, @RequestParam("city") String city,
			@RequestParam("country") String country, @RequestParam("region") String region) {
		UserDetailsVo u = (UserDetailsVo) authentication.getPrincipal();
		String userEmail = u.getUsername().toString();
		
		Map<String, String> paramMap = new HashMap<String, String>();
		HashMap<String, Object> hashmap = new HashMap<String, Object>();
		paramMap.put("userEmail", userEmail);
		paramMap.put("address1", address1);
		paramMap.put("address2", address2);
		paramMap.put("zipCode", zipCode);
		paramMap.put("city", city);
		paramMap.put("country", country);
		paramMap.put("region", region);
		int result;

		try {
			result = dao.modifyAddress(paramMap);
		} catch (Exception e) {
			result = 0;
			e.printStackTrace();
		}

		if (result == 1) {
			hashmap.put("KEY", "SUCCESS");
		} else {
			hashmap.put("KEY", "FAIL");
		}


		return hashmap;
	}

	// password ����
	@ResponseBody
	@RequestMapping(value="/modifyPassword", method=RequestMethod.POST)
	public HashMap<String, Object> modifyPassword(Authentication authentication,
			@RequestParam("currentPassword") String currentPassword, @RequestParam("newPassword") String newPassword) {
		
		UserDetailsVo u = (UserDetailsVo) authentication.getPrincipal();
		String userEmail = u.getUsername().toString();
		int result = 0;
		Map<String, Object> user = dao.selectPassword(userEmail);
		Map<String, String> password = new HashMap<String, String>();
		HashMap<String, Object> hashmap = new HashMap<String, Object>();
		
		boolean check = encoder.matches(currentPassword, user.get("ROWPASSWORD").toString());
		
		System.out.println("TEST password : " + check);
		
		if (check == true) {
			String encodedPassword = encoder.encode(newPassword);
			password.put("userEmail", userEmail);
			password.put("newPassword", encodedPassword);
			try {
				result = dao.modifyPassword(password);
			} catch (Exception e) {
				e.printStackTrace();
				result = 0;
			}

			if (result == 1) {
				hashmap.put("KEY", "SUCCESS");
			} else {
				hashmap.put("KEY", "FAIL");
			}
		} else {
			hashmap.put("KEY", "FAIL");
		}


		return hashmap;
	}
	
	// userEmail�� ��ȣȭ, ��ȣȭ�� ����ؾ���, ����� bcrypt��ȣȭ ������ ��ȣȭ�� ������� ����. �񱳸� ����
	@ResponseBody
	@RequestMapping(value="/sendLink", method=RequestMethod.POST)
	public Map<String, Object> sendLink (@RequestParam("userEmail")String userEmail) {
		HashMap<String, Object> result = new HashMap<String, Object>();
		Map<String, Object> userEmailExist = dao.selectEmail(userEmail);
	
		if (userEmailExist != null) {
			String ue = userEmailExist.get("USEREMAIL").toString();
			String encEmail = encoder.encode(ue);
			
			System.out.println(encEmail);
			String htmlContent = "<h1>�Ʒ� �ּҸ� Ŭ���Ͽ� ��й�ȣ�� �����ϼ���.</h1><br/>"
					+ "<h3>��ũ�� ������ �� ��� ��ȣ�� �������� ������ �̸����� �ٽ� �����ؾ� �մϴ�.</h3>"
					+ "http://localhost:8181/kwaveweb/pwdService?bep=" 
					+ encEmail + "&ue=" + ue;
			try {
				MimeMessage message = mailSender.createMimeMessage();
				
				message.setFrom(new InternetAddress("tantosuperb@gmail.com"));
				message.addRecipient(javax.mail.Message.RecipientType.TO, new InternetAddress(userEmailExist.get("USEREMAIL").toString()));
				message.setSubject("KWAVE DONATE ��й�ȣ ���� �ȳ��Դϴ�.");
				message.setText(htmlContent, "UTF-8", "html");
				
				mailSender.send(message);
				result.put("KEY", "SUCCESS");
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println(e);
			}
		} else {
			result.put("KEY", "FAIL");
		}
		return result;
	}

	@RequestMapping(value="/pwdService", method=RequestMethod.GET)
	public String pwdService(HttpServletRequest request, Model model) {

		String bep = request.getParameter("bep").toString();
		String ue = request.getParameter("ue").toString();
		
		if (!(check.equals(bep))) {
			model.addAttribute("ue", ue);
			check = bep;
			if(encoder.matches(ue, bep)) {
				return "pwdService";
				
			} else {
				System.out.println("�ȵǳ�");
				return "/errorPage";
			}
		} else {
			return "/errorPage";
		}
	}
	
	@ResponseBody
	@RequestMapping(value="/pwdmodifyService", method=RequestMethod.POST)
	public HashMap<String, Object> pwdmodifyService(@RequestParam("userEmail")String userEmail, @RequestParam("userPassword")String userPassword) {
		HashMap<String, Object> hashmap = new HashMap<String, Object>();
		String encodedPassword = encoder.encode(userPassword);
		Map<String, String> password = new HashMap<String, String>();
		
		password.put("userEmail", userEmail);
		password.put("newPassword", encodedPassword);
		
		if(dao.modifyPassword(password)==1) {
			hashmap.put("KEY", "SUCCESS");
		} else {
			hashmap.put("KEY", "FAIL");
		}
		return hashmap;
		
	}
	
	/*
	 * �������� ��Ʈ�ѷ�
	 */
	@ResponseBody
	@RequestMapping(value="insertDeliveryENG", method=RequestMethod.POST)
	public HashMap<String, Object> insertDeliveryENG(
			@RequestParam("imp_uid")String imp_uid, @RequestParam("merchant_uid")String merchant_uid,
			@RequestParam("userEmail")String userEmail,
			@RequestParam("campaignName")String campaignName,
			@RequestParam("rewardNum")String rewardNum,
			@RequestParam("totalAmount")String totalAmount,
			@RequestParam("note")String note,
			@RequestParam("shippingAmount")String shippingAmount,
			@RequestParam("shippingMethod")String shippingMethod,
			@RequestParam("userName")String userName,
			@RequestParam("phone")String phone,
			@RequestParam("address1")String address1,
			@RequestParam("address2")String address2,
			@RequestParam("zipCode")String zipCode,
			@RequestParam("city")String city,
			@RequestParam("country")String country,
			@RequestParam("region")String region ) {
		int resultD = 0, resultP = 0;
		System.out.println("ENG");
		System.out.println(note);
		
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("imp_uid", imp_uid);
		paramMap.put("userEmail", userEmail);
		paramMap.put("merchant_uid", merchant_uid);
		paramMap.put("campaignName", campaignName);
		paramMap.put("rewardNum", Integer.parseInt(rewardNum));
		paramMap.put("totalAmount", Integer.parseInt(totalAmount));
		paramMap.put("shippingAmount", Integer.parseInt(shippingAmount));
		paramMap.put("shippingMethod", shippingMethod);
		paramMap.put("userName", userName);
		paramMap.put("phone", phone);
		paramMap.put("note", note);
		paramMap.put("address1", address1);
		paramMap.put("address2", address2);
		paramMap.put("zipCode", zipCode);
		paramMap.put("city", city);
		paramMap.put("country", country);
		paramMap.put("region", region);
		
		HashMap<String, Object> hashmap = new HashMap<String, Object>();
		
		/*resultD = dao.insertDelivery(paramMap);
		resultP = dao.insertPayments(paramMap);
		if(resultD == 1 && resultP == 1) {
			hashmap.put("KEY", "SUCCESS");
		}*/
		
		DefaultTransactionDefinition def = new DefaultTransactionDefinition();
		def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
		
		TransactionStatus status = transactionManager.getTransaction(def);
		try {
			resultD = dao.insertDelivery(paramMap);
			resultP = dao.insertPayments(paramMap);
			if(resultD == 1 && resultP == 1) {
				hashmap.put("KEY", "SUCCESS");
			}
		} catch (Exception e) {
			e.printStackTrace();
			transactionManager.rollback(status);
			hashmap.put("KEY", "FAIL");
			e.printStackTrace();
		}
		transactionManager.commit(status);
		
		return hashmap;
	}
	
	@ResponseBody
	@RequestMapping(value="insertDeliveryKOR", method=RequestMethod.POST)
	public HashMap<String, Object> insertDeliveryKOR(
			@RequestParam("imp_uid")String imp_uid, 
			@RequestParam("merchant_uid")String merchant_uid, 
			@RequestParam("receipt_url")String receipt_url,
			@RequestParam("userEmail")String userEmail,
			@RequestParam("campaignName")String campaignName,
			@RequestParam("rewardNum")String rewardNum,
			@RequestParam("rewardAmount")String rewardAmount,
			@RequestParam("totalAmount")String totalAmount,
			@RequestParam("shippingAmount")String shippingAmount,
			@RequestParam("shippingMethod")String shippingMethod,
			@RequestParam("note")String note,
			@RequestParam("country")String country,
			@RequestParam("phone")String phone,
			@RequestParam("address1")String address1,
			@RequestParam("address2")String address2,
			@RequestParam("zipCode")String zipCode ) {
		System.out.println("KOR");
		int resultD = 0, resultP=0;
		
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("imp_uid", imp_uid);
		paramMap.put("merchant_uid", merchant_uid);
		paramMap.put("userEmail", userEmail);
		paramMap.put("campaignName", campaignName);
		paramMap.put("rewardNum", Integer.parseInt(rewardNum));
		paramMap.put("rewardAmount", Integer.parseInt(rewardAmount));
		paramMap.put("totalAmount", Integer.parseInt(totalAmount));
		paramMap.put("shippingAmount", Integer.parseInt(shippingAmount));
		paramMap.put("shippingMethod", shippingMethod);
		paramMap.put("note", note);
		paramMap.put("city", "city");
		paramMap.put("region", "region");
		paramMap.put("country", country);
		paramMap.put("phone", phone);
		paramMap.put("address1", address1);
		paramMap.put("address2", address2);
		paramMap.put("zipCode", zipCode);
		paramMap.put("receipt_url", receipt_url);
		
		System.out.println("imp_uid : " + receipt_url);
		
		
		
		HashMap<String, Object> hashmap = new HashMap<String, Object>();
		
		DefaultTransactionDefinition def = new DefaultTransactionDefinition();
		def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
		
		TransactionStatus status = transactionManager.getTransaction(def);
		try {
			System.out.println("try문 들어옴");
			resultP = dao.insertPayments(paramMap);
			resultD = dao.insertDelivery(paramMap);
			
			dao.updateRewardsByPayment(paramMap);
			dao.updateCampaignsByPayment(paramMap);
			transactionManager.commit(status);
			if(resultD == 1 && resultP == 1) {
				hashmap.put("KEY", "SUCCESS");
			}
		} catch (Exception e) {
			System.out.println("catch문 들어옴");
			try {
				transactionManager.rollback(status);
			} catch(Exception ee) {
				 System.out.println("Exception in commit or rollback : "+ee);
			}
			
			System.out.println("Exception in saveTemplatesToPCA() : "+e);
			hashmap.put("KEY", "FAIL");
		}
		
		
		return hashmap;
	}

}
