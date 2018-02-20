package com.kazale.inteligente.api.services;

import java.util.Optional;

import com.kazale.inteligente.api.entities.Empresa;

public interface EmpresaService {
	
	
	/**
	 * Retorna um empresa dado um Cnpj
	 * 
	 * @param cnpj
	 * @return Optional<Empresa>
	 */
	Optional<Empresa> buscarPorCnpj(String cnpj);
	
	/**
	 * Cadastra uma nova empresa na base de dados
	 * 
	 * @param empresa
	 * @return Empresa
	 */
	Empresa persistir(Empresa empresa);
}
