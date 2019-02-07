package com.vince.retailmanager.demo;

import com.vince.retailmanager.model.entity.authorization.User;
import com.vince.retailmanager.model.entity.companies.Company;
import com.vince.retailmanager.model.entity.companies.Franchisee;
import com.vince.retailmanager.model.entity.companies.Franchisor;
import com.vince.retailmanager.model.entity.financials.IncomeStatement;
import com.vince.retailmanager.model.entity.transactions.Invoice;
import com.vince.retailmanager.repository.AccessTokensRepository;
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
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class InsertDemoData {

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
  private FranchisorRepository franchisorRepo;
  @Autowired
  private FranchiseeRepository franchiseeRepo;
  @Autowired
  private PaymentRepository paymentRepo;
  @Autowired
  private InvoiceRepository invoiceRepo;
  @Autowired
  private IncomeStatementRepository incomeStatementRepo;

  @EventListener
  @Transactional
  public void appReady(ApplicationReadyEvent event) throws Exception {
    System.out.println("Setting up demo data...");
    clearDBTables();
    createFranchises();
    TestData.setupAdmin(userService);
    addNewUser();
    System.out.println("\nDemo data setup complete!");
  }

  private void clearDBTables() {
    accessTokenRepo.deleteAll();
    userRepo.deleteAll();
    paymentRepo.deleteAll();
    franchisorRepo.deleteAll();
    franchiseeRepo.deleteAll();
  }

  void addNewUser() {
    User user = new User("new_user", TestData.DUMMY_PASSWORD);
    userService.saveUser(user);
  }

  void createFranchises() throws Exception {
    Franchisor franchisor = TestData.createFranchisor();
    addFranchiseesToFranchisor("mc", franchisor, NUM_OF_FRANCHISEES);

//    Franchisee firstFranchisee = franchisor.getFranchisees().stream().findFirst().orElse(null);
//    Invoice invoice = createInvoice(franchisor, firstFranchisee, BEGINNING_BALANCE);
//    Payment payment = Payment.builder()
//        .amount(PAYMENT_AMOUNT)
//        .invoice(invoice)
//        .build();
//    payment.setSender(firstFranchisee);
//    payment.setRecipient(franchisor);
//    paymentRepo.save(payment);
//    createIncomeStatements(firstFranchisee, 3);
//
//    LocalDate startDate = LocalDate.of(2018, 10, 1);
//    LocalDate endDate = LocalDate.of(2018, 12, 31);
//    DateRange dateRange = new DateRange(startDate, endDate);
//    IncomeStatementStatistics statistics = IncomeStatementStatistics
//        .create(firstFranchisee, dateRange);
  }

  Invoice createInvoice(Company sender,
      Company recipient,
      double balance) {
    Invoice invoice = Invoice.builder()
        .due(balance)
        .description("sample invoice description")
        .recipient(recipient)
        .sender(sender)
        .build();
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


  public void addFranchiseesToFranchisor(String shortName,
      Franchisor franchisor,
      int numOfFranchisees) {
    setupFranchisorInDB(shortName, franchisor);
    for (int i = 1; i <= numOfFranchisees; i++) {
      setupFranchiseeInDB(shortName, franchisor, i);
    }

  }

  private void setupFranchisorInDB(String shortName,
      Franchisor franchisor) {
    User franchisorUser = new User(shortName + "_franchisor", TestData.DUMMY_PASSWORD);
    franchisorRepo.save(franchisor);
    userService.saveUser(franchisorUser);
    userService.addAccessToken(franchisorUser.getUsername(), franchisor);
  }

  private Franchisee setupFranchiseeInDB(String shortName,
      Franchisor franchisor,
      int i) {
    Franchisee franchisee = Franchisee.builder()
        .build();

    User franchiseeUser = new User(shortName + "_franchisee_" + i, TestData.DUMMY_PASSWORD);
    franchiseeRepo.save(franchisee);
    franchisor.addFranchisee(franchisee);
    userService.saveUser(franchiseeUser);
    userService.addAccessToken(franchiseeUser.getUsername(), franchisee);
    return franchisee;
  }
}
