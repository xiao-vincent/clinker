package com.vince.retailmanager.demo;

import com.vince.retailmanager.entity.Franchisee;
import com.vince.retailmanager.entity.Franchisor;
import com.vince.retailmanager.entity.User;
import com.vince.retailmanager.repository.*;
import com.vince.retailmanager.service.FranchiseService;
import com.vince.retailmanager.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class InsertDemoData {
	private static final String dummyPassword = "password";

	@Autowired
	private AccessTokensRepository accessTokenRepo;
	@Autowired
	private UserRepository userRepo;
	@Autowired
	private UserService userService;
	@Autowired
	private FranchiseService franchiseService;
	@Autowired
	private CompanyRepository companyRepo;
	@Autowired
	private FranchisorRepository franchisorRepo;
	@Autowired
	private FranchiseeRepository franchiseeRepo;
	@Autowired
	private PaymentRepository paymentRepo;

	@EventListener
	@Transactional
	public void appReady(ApplicationReadyEvent event) throws Exception {
		System.out.println("Setting up demo data...");
		clearDBTables();
		createFranchise();

		setupAdmin();
		System.out.println("\nDemo data setup complete!");
//		System.out.println(companyRepo.findById(1));

//		Company company = companyRepo.findById(1).orElse(null);
//		User user = userRepo.findByUsername("mc_franchisor").orElse(null);
//
//		AccessToken accessToken = accessTokenRepo.findById(1).orElse(null);
//		user.removeAccessToken(accessToken);
//		accessTokenRepo.delete(accessToken);
//		companyRepo.delete(companyRepo.findById(1).orElse(null));
//
//		System.out.println(accessToken);
//		System.out.println(company);

//		Payment payment = new Payment();
//		System.out.println(franchisees.get(0));
//		payment.setSender(franchisees.get(0));
//		payment.setRecipient(franchisors.get(0));
//		payment.setAmount(new BigDecimal(2250.34444));
//		paymentRepo.save(payment);
//		System.out.println(payment);

	}

	public void createFranchise() throws Exception {
		Franchisor franchisor = Franchisor.builder()
			 .name("McDonald's Corporation")
			 .website("mcdonalds.com")
			 .description("Fast food company")
			 .franchiseFee(30000.0)
			 .liquidCapitalRequirement(200000.0)
			 .royaltyFee(.08)
			 .marketingFee(.02)
			 .feeFrequency(12)
			 .build();
		setupFranchisorsAndFranchisees("mc", franchisor, 2);
	}

	private void setupAdmin() throws Exception {
		User admin = new User("admin", "adminpassword");
		admin.addRole("ADMIN");
		userService.saveUser(admin);
		System.out.println("saving " + admin);

	}

	private void clearDBTables() {
		accessTokenRepo.deleteAll();

		userRepo.deleteAll();
		paymentRepo.deleteAll();
		franchisorRepo.deleteAll();
		franchiseeRepo.deleteAll();
	}


	public void setupFranchisorsAndFranchisees(String shortName, Franchisor franchisor, int numOfFranchisees) throws Exception {
		shortName = shortName.toLowerCase();
		setupFranchisorInDB(shortName, franchisor);

		for (int i = 1; i <= numOfFranchisees; i++) {
			setupFranchiseeInDB(shortName, franchisor, i);
		}

	}

	private void setupFranchisorInDB(String shortName, Franchisor franchisor) throws Exception {
		User franchisorUser = new User(shortName + "_franchisor", dummyPassword);
		franchisorRepo.save(franchisor);

		franchisorUser.addAccessToken(franchisor);
		userService.saveUser(franchisorUser);
	}

	private void setupFranchiseeInDB(String shortName, Franchisor franchisor, int i) throws Exception {
		Franchisee franchisee = Franchisee.builder()
			 .build();

		User franchiseeUser = new User(shortName + "_franchisee_" + i, dummyPassword);
		franchiseeRepo.save(franchisee);
		franchisor.addFranchisee(franchisee);

		franchiseeUser.addAccessToken(franchisee);
		userService.saveUser(franchiseeUser);
		System.out.println("saving " + franchisee);
	}
}
