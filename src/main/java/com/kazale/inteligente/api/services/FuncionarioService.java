package com.kazale.inteligente.api.services;

import java.util.List;
import java.util.Optional;

import com.kazale.inteligente.api.entities.Funcionario;

public interface FuncionarioService {

	/**
	 * Persiste um funcionario na base de dados
	 * 
	 * @param fuincionario
	 * @return Funcionario
	 */
	Funcionario persistir(Funcionario funcionario);
	
	/**
	 * Busca e retorna um funcionario por Cpf
	 * 
	 * @param cpf
	 * @return Optional<Funcionario>
	 */
	Optional<Funcionario> buscaPorCpf(String cpf);
	
	/**
	 * Busca e retorna um funcionario por Email
	 * 
	 * @param email
	 * @return Optional<Funcionario>
	 */
	Optional<Funcionario> buscaPorEmail(String email);
	
	/**
	 * Busca e retorna um funcionario por Id
	 * 
	 * @param id
	 * @return Optional<Funcionario>
	 */
	Optional<Funcionario> buscaPorId(Long id);
	
	/**
	 * Busca e retorna todos os funcionarios
	 * 
	 * @param 
	 * @return Optional<Funcionario>
	 */
	List<Funcionario> buscaTodos();
	
	
}
