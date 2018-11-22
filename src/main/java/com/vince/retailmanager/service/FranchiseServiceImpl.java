package com.vince.retailmanager.service;

import com.vince.retailmanager.entity.Company;
import com.vince.retailmanager.entity.Franchisee;
import com.vince.retailmanager.entity.Franchisor;
import com.vince.retailmanager.entity.Payment;
import com.vince.retailmanager.exception.ObjectStateException;
import com.vince.retailmanager.repository.*;
import com.vince.retailmanager.web.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class FranchiseServiceImpl implements FranchiseService {

	private UserService userService;

	private AccessTokensRepository accessTokensRepository;
	private CompanyRepository companyRepository;
	private FranchisorRepository franchisorRepository;
	private FranchiseeRepository franchiseeRepository;
	private PaymentRepository paymentRepository;

	@Autowired
	public FranchiseServiceImpl(
		 UserService userService,
		 AccessTokensRepository accessTokensRepository,
		 CompanyRepository companyRepository,
		 FranchisorRepository franchisorRepository,
		 FranchiseeRepository franchiseeRepository,
		 PaymentRepository paymentRepository) {
		this.userService = userService;
		this.accessTokensRepository = accessTokensRepository;
		this.companyRepository = companyRepository;
		this.franchisorRepository = franchisorRepository;
		this.franchiseeRepository = franchiseeRepository;
		this.paymentRepository = paymentRepository;
	}


	@Override
	@Transactional
	public void saveFranchisor(Franchisor franchisor) {
		franchisorRepository.save(franchisor);
	}

	@Override
	@Transactional(readOnly = true)
	public Franchisor findFranchisorById(int id) throws EntityNotFoundException {
		Franchisor franchisor = franchisorRepository.findById(id).orElse(null);
		if (franchisor == null) {
			throw new EntityNotFoundException(Franchisor.class, "id", String.valueOf(id));
		}
		return franchisor;
	}

	@Override
	@Transactional
	public void saveFranchisee(Franchisee franchisee) {
		franchiseeRepository.save(franchisee);
	}

	@Override
	@Transactional(readOnly = true)
	public List<Franchisor> findAllFranchisors() throws DataAccessException {
		return franchisorRepository.findAll();
	}


	@Override
	@Transactional(readOnly = true)
	public Franchisee findFranchiseeById(int franchiseeId) {
		return franchiseeRepository.findById(franchiseeId).orElse(null);
	}


	@Override
	public void disableFranchisor(Franchisor franchisor) throws ObjectStateException {
		if (!franchisor.getFranchisees().isEmpty()) {
			Map<String, Set<Franchisee>> invalidValues = new HashMap<>();
			invalidValues.put("franchisees", franchisor.getFranchisees());

			throw new ObjectStateException(
				 "franchisor has franchisee(s)",
				 "franchisor",
				 invalidValues);
		}
		disableCompany(franchisor);
	}

	@Override
	@Transactional
	public void disableCompany(Company company) {
		//check if there are any franchisees dependendent on franchisor
		//remove all access tokens associated with company /// but throw exception in constraint validator
		company.setEnabled(false);
		companyRepository.save(company);
	}

	@Override
	@Transactional
	public void savePayment(Payment payment) {
		paymentRepository.save(payment);
	}
}
