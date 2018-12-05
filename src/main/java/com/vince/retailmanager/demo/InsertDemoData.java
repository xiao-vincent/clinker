package com.vince.retailmanager.demo;

import com.vince.retailmanager.entity.*;
import com.vince.retailmanager.repository.*;
import com.vince.retailmanager.service.FranchiseService;
import com.vince.retailmanager.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

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
	@Autowired
	private InvoiceRepository invoiceRepo;

	private Map<String, Franchisor> franchisors = new HashMap<>();

	//defaults
	private static final int NUM_OF_FRANCHISEES = 2;
	private static final int BEGINNING_BALANCE = 1000;
	private static final double PAYMENT_AMOUNT = 100;


	@EventListener
	@Transactional
	public void appReady(ApplicationReadyEvent event) throws Exception {
		System.out.println("Setting up demo data...");
		clearDBTables();
		createFranchises();

		setupAdmin();
		System.out.println("\nDemo data setup complete!");
	}

	void createFranchises() throws Exception {
		String shortNameKey = "mc";
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
		franchisors.put(shortNameKey, franchisor);
		addFranchiseesToFranchisor(shortNameKey, franchisor, NUM_OF_FRANCHISEES);

		Franchisee firstFranchisee = franchisor.getFranchisees().stream().findFirst().orElse(null);
		Invoice invoice = createInvoice(franchisor, firstFranchisee, BEGINNING_BALANCE);
		Payment payment = Payment.builder()
			 .amount(PAYMENT_AMOUNT)
			 .sender(firstFranchisee)
			 .recipient(franchisor)
			 .invoice(invoice)
			 .build();
		paymentRepo.save(payment);

	}


	Invoice createInvoice(Company seller, Company customer, double balance) {
		Invoice invoice = Invoice.builder()
			 .seller(seller)
			 .customer(customer)
			 .balance(balance)
			 .description("royalty payment")
			 .build();

		invoiceRepo.save(invoice);
		return invoice;
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


	public void addFranchiseesToFranchisor(String shortName, Franchisor franchisor, int numOfFranchisees) throws Exception {
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

	private Franchisee setupFranchiseeInDB(String shortName, Franchisor franchisor, int i) throws Exception {
		Franchisee franchisee = Franchisee.builder()
			 .build();

		User franchiseeUser = new User(shortName + "_franchisee_" + i, dummyPassword);
		franchiseeRepo.save(franchisee);
		franchisor.addFranchisee(franchisee);

		franchiseeUser.addAccessToken(franchisee);
		userService.saveUser(franchiseeUser);
		System.out.println("saving " + franchisee);
		return franchisee;
	}
}
