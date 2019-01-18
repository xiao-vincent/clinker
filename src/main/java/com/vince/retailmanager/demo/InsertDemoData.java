package com.vince.retailmanager.demo;

import com.vince.retailmanager.model.entity.authorization.User;
import com.vince.retailmanager.model.entity.companies.Company;
import com.vince.retailmanager.model.entity.companies.Franchisee;
import com.vince.retailmanager.model.entity.companies.Franchisor;
import com.vince.retailmanager.model.entity.financials.DateRange;
import com.vince.retailmanager.model.entity.financials.IncomeStatement;
import com.vince.retailmanager.model.entity.financials.IncomeStatementStatistics;
import com.vince.retailmanager.model.entity.transactions.Invoice;
import com.vince.retailmanager.model.entity.transactions.Payment;
import com.vince.retailmanager.repository.AccessTokensRepository;
import com.vince.retailmanager.repository.CompanyRepository;
import com.vince.retailmanager.repository.FranchiseeRepository;
import com.vince.retailmanager.repository.FranchisorRepository;
import com.vince.retailmanager.repository.IncomeStatementRepository;
import com.vince.retailmanager.repository.InvoiceRepository;
import com.vince.retailmanager.repository.PaymentRepository;
import com.vince.retailmanager.repository.UserRepository;
import com.vince.retailmanager.security.RoleType;
import com.vince.retailmanager.service.FranchiseService;
import com.vince.retailmanager.service.UserService;
import java.time.LocalDate;
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

  private static final String DUMMY_PASSWORD = "password";
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
    addNewUser();
    System.out.println("\nDemo data setup complete!");
  }

  void addNewUser() {
    User user = new User("new_user", DUMMY_PASSWORD);
    userService.saveUser(user);
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
    payment.setSender(firstFranchisee);
    payment.setRecipient(franchisor);
    paymentRepo.save(payment);
    createIncomeStatements(firstFranchisee, 3);

    LocalDate startDate = LocalDate.of(2018, 10, 1);
    LocalDate endDate = LocalDate.of(2018, 12, 31);
    DateRange dateRange = new DateRange(startDate, endDate);
    IncomeStatementStatistics statistics = IncomeStatementStatistics
        .create(firstFranchisee, dateRange);
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
    for (int i = 1; i <= numOfIncomeStatements; i++) {
      IncomeStatement incomeStatement = IncomeStatement.builder()
          .sales(1000.0 * i)
          .costOfGoodsSold(200.0 * i)
          .operatingExpenses(100.0 * i)
          .generalAndAdminExpenses(50.0 * i)
          .date(2018, 10 + i - 1)
          .build();
      company.addIncomeStatement(incomeStatement);
      incomeStatementRepo.save(incomeStatement);
      incomeStatements.add(incomeStatement);
    }
    return incomeStatements;
  }

  private void setupAdmin() throws Exception {
    User admin = new User("admin", "adminpassword");
    admin.addRole(RoleType.ADMIN);
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
      Franchisor franchisor) {
    User franchisorUser = new User(shortName + "_franchisor", DUMMY_PASSWORD);
    franchisorRepo.save(franchisor);
    userService.saveUser(franchisorUser);
    userService.addAccessToken(franchisorUser.getUsername(), franchisor);
  }

  private Franchisee setupFranchiseeInDB(String shortName,
      Franchisor franchisor,
      int i) {
    Franchisee franchisee = Franchisee.builder()
        .build();

    User franchiseeUser = new User(shortName + "_franchisee_" + i, DUMMY_PASSWORD);
    franchiseeRepo.save(franchisee);
    franchisor.addFranchisee(franchisee);

    userService.saveUser(franchiseeUser);
    userService.addAccessToken(franchiseeUser.getUsername(), franchisee);
    return franchisee;
  }
}
