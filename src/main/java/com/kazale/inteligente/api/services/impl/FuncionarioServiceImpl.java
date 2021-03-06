package com.kazale.inteligente.api.services.impl;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kazale.inteligente.api.entities.Funcionario;
import com.kazale.inteligente.api.repositories.FuncionarioRepository;
import com.kazale.inteligente.api.services.FuncionarioService;

@Service
public class FuncionarioServiceImpl implements FuncionarioService {

	private static final Logger log = LoggerFactory.getLogger(FuncionarioServiceImpl.class);
	
	@Autowired
	private FuncionarioRepository funcionarioRepository;	
	
	@Override
	public Funcionario persistir(Funcionario funcionario) {
		log.info("Persistindo funcionario: {}",funcionario);
		return this.funcionarioRepository.save(funcionario);
	}

	@Override
	public Optional<Funcionario> buscaPorCpf(String cpf) {
		log.info("Buscando funcionario pelo cpf: {}",cpf);
		return Optional.ofNullable(this.funcionarioRepository.findByCpf(cpf));
	}

	@Override
	public Optional<Funcionario> buscaPorEmail(String email) {
		log.info("Buscando funcionario pelo email: {}",email);
		return Optional.ofNullable(this.funcionarioRepository.findByEmail(email));
	}

	@Override
	public Optional<Funcionario> buscaPorId(Long id) {
		log.info("Buscando funcionario pelo id: {}",id);
		return Optional.ofNullable(this.funcionarioRepository.findOne(id));
	}

	@Override
	public List<Funcionario> buscaTodos() {
		log.info("Buscando todos os funcioarios");
		return this.funcionarioRepository.findAll();
	}

}
