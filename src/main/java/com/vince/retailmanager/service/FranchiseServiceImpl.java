package com.vince.retailmanager.service;

import com.vince.retailmanager.entity.Franchisee;
import com.vince.retailmanager.entity.Franchisor;
import com.vince.retailmanager.entity.Payment;
import com.vince.retailmanager.repository.FranchiseeRepository;
import com.vince.retailmanager.repository.FranchisorRepository;
import com.vince.retailmanager.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class FranchiseServiceImpl implements FranchiseService {
	private FranchisorRepository franchisorRepository;
	private FranchiseeRepository franchiseeRepository;
	private PaymentRepository paymentRepository;

	@Autowired
	public FranchiseServiceImpl(
		 FranchisorRepository franchisorRepository,
		 FranchiseeRepository franchiseeRepository,
		 PaymentRepository paymentRepository
	) {
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
	public Franchisor findFranchisorById(int id) {
		return franchisorRepository.findById(id).orElse(null);
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
	public Franchisee findFranchiseeById(int franchiseeId) {
		return franchiseeRepository.findById(franchiseeId).orElse(null);
	}

	@Override
	@Transactional
	public void savePayment(Payment payment) {
		paymentRepository.save(payment);
	}


}
