package com.vince.retailmanager.demo;

import com.vince.retailmanager.model.entity.Company;
import com.vince.retailmanager.model.entity.Franchisee;
import com.vince.retailmanager.model.entity.Franchisor;
import com.vince.retailmanager.model.entity.IncomeStatement;
import com.vince.retailmanager.model.entity.Invoice;
import com.vince.retailmanager.model.entity.Payment;
import com.vince.retailmanager.model.entity.User;
import com.vince.retailmanager.repository.AccessTokensRepository;
import com.vince.retailmanager.repository.CompanyRepository;
import com.vince.retailmanager.repository.FranchiseeRepository;
import com.vince.retailmanager.repository.FranchisorRepository;
import com.vince.retailmanager.repository.IncomeStatementRepository;
import com.vince.retailmanager.repository.InvoiceRepository;
import com.vince.retailmanager.repository.PaymentRepository;
import com.vince.retailmanager.repository.UserRepository;
import com.vince.retailmanager.service.FranchiseService;
import com.vince.retailmanager.service.UserService;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class InsertDemoData {

  private static final String dummyPassword = "password";
  //defaults
  private static final int NUM_OF_FRANCHISEES = 2;
  private static final int BEGINNING_BALANCE = 1000;
  private static final double PAYMENT_AMOUNT = 100;
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
  @Autowired
  private IncomeStatementRepository incomeStatementRepo;
  private Map<String, Franchisor> franchisors = new HashMap<>();

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
        .royaltyFeePercent(.08)
        .marketingFeePercent(.02)
        .feeFrequency(12)
        .build();
    franchisors.put(shortNameKey, franchisor);
    addFranchiseesToFranchisor(shortNameKey, franchisor, NUM_OF_FRANCHISEES);

    Franchisee firstFranchisee = franchisor.getFranchisees().stream().findFirst().orElse(null);
    Invoice invoice = createInvoice(franchisor, firstFranchisee, BEGINNING_BALANCE);
    Payment payment = Payment.builder()
        .amount(PAYMENT_AMOUNT)
        .invoice(invoice)
        .build();
    firstFranchisee.addPaymentSent(payment);
    franchisor.addPaymentReceived(payment);
    paymentRepo.save(payment);

    createIncomeStatements(firstFranchisee, 3);

//    IncomeStatementStatistics total = new IncomeStatementStatistics(
//        createIncomeStatements(firstFranchisee, 3));
//    System.out.println("total = " + total);

//		Royalty royalty = franchiseService.saveRoyalty();
//		System.out.println("royalty = " + royalty);
//		MarketingFee marketingFee = franchiseService.requestMarketingFee(franchisor, incomeStatement);
//		System.out.println("marketingFee = " + marketingFee);

  }

  Invoice createInvoice(Company sender,
      Company recipient,
      double balance) {
    Invoice invoice = Invoice.builder()
        .due(balance)
        .description("sample invoice description")
        .build();
    sender.addInvoiceSent(invoice);
    recipient.addInvoiceReceived(invoice);
    invoiceRepo.save(invoice);
    return invoice;
  }

  Collection<IncomeStatement> createIncomeStatements(Company company,
      int numOfIncomeStatements) {
    List<IncomeStatement> incomeStatements = new ArrayList<>();
    for (int i = 0; i < numOfIncomeStatements; i++) {
      IncomeStatement incomeStatement = IncomeStatement.builder()
          .sales(1000.0 * i)
          .costOfGoodsSold(200.0 * i)
          .operatingExpenses(100.0 * i)
          .generalAndAdminExpenses(50.0 * i)
          .date(2018, 10 + i)
          .build();
      company.addIncomeStatement(incomeStatement);
      incomeStatementRepo.save(incomeStatement);
      incomeStatements.add(incomeStatement);
    }
    return incomeStatements;
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


  public void addFranchiseesToFranchisor(String shortName,
      Franchisor franchisor,
      int numOfFranchisees) throws Exception {
    shortName = shortName.toLowerCase();
    setupFranchisorInDB(shortName, franchisor);
    for (int i = 1; i <= numOfFranchisees; i++) {
      setupFranchiseeInDB(shortName, franchisor, i);
    }

  }

  private void setupFranchisorInDB(String shortName,
      Franchisor franchisor) throws Exception {
    User franchisorUser = new User(shortName + "_franchisor", dummyPassword);
    franchisorRepo.save(franchisor);

    franchisorUser.addAccessToken(franchisor);
    userService.saveUser(franchisorUser);
  }

  private Franchisee setupFranchiseeInDB(String shortName,
      Franchisor franchisor,
      int i) throws Exception {
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
