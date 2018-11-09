package com.vince.retailmanager.demo;

import com.vince.retailmanager.entity.Franchisee;
import com.vince.retailmanager.entity.Franchisor;
import com.vince.retailmanager.entity.User;
import com.vince.retailmanager.repository.FranchiseeRepository;
import com.vince.retailmanager.repository.FranchisorRepository;
import com.vince.retailmanager.repository.PaymentRepository;
import com.vince.retailmanager.repository.UserRepository;
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
	private UserRepository userRepo;
	@Autowired
	private UserService userService;
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
		setupAdmin();
		createFranchise();


		System.out.println("\nDemo data setup complete!");

		//newfake scenario for testing
//		Payment payment = new Payment();
//		System.out.println(franchisees.get(0));
//		payment.setPayer(franchisees.get(0));
//		payment.setRecipient(franchisors.get(0));
//		payment.setAmount(new BigDecimal(2250.34444));
//		paymentRepo.save(payment);
//		System.out.println(payment);

	}

	public void createFranchise() throws Exception {
		Franchisor franchisor = Franchisor.builder()
			 .name("Pizza Hut")
			 .website("pizzahut.com")
			 .description("Restaurant company")
			 .build();
		setupFranchisorsAndFranchisees("ph", franchisor, 1);

		franchisor = Franchisor.builder()
			 .name("McDonald's Corporation")
			 .website("mcdonalds.com")
			 .description("Fast food company")
			 .build();
		setupFranchisorsAndFranchisees("mc", franchisor, 2);
	}

	private void setupAdmin() throws Exception {
		User admin = new User("admin", "adminpassword");
		admin.addRole("ADMIN");
		userService.saveUser(admin);
	}

	private void clearDBTables() {
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
		userService.saveUser(franchisorUser);
		franchisor.setUser(franchisorUser);
		franchisorRepo.save(franchisor);
	}

	private void setupFranchiseeInDB(String shortName, Franchisor franchisor, int i) throws Exception {
		Franchisee franchisee = Franchisee.builder()
			 .build();
		franchisee.setFranchisor(franchisor);

		User franchiseeUser = new User(shortName + "_franchisee_" + i, dummyPassword);
		franchisee.setUser(franchiseeUser);
		userService.saveUser(franchiseeUser);
		System.out.println("saving " + franchisee);
		franchiseeRepo.save(franchisee);
	}
}
