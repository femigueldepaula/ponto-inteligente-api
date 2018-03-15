package com.kazale.inteligente.api.controllers;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kazale.inteligente.api.dtos.FuncionarioDto;
import com.kazale.inteligente.api.entities.Funcionario;
import com.kazale.inteligente.api.response.Response;
import com.kazale.inteligente.api.services.FuncionarioService;

@RestController
@RequestMapping("/api/funcionarios")
public class FuncionarioController {

	@Autowired
	private FuncionarioService funcionarioService;
	
	@RequestMapping(value = "/cpf/{cpf}")
	public ResponseEntity<Response<FuncionarioDto>> buscarPorCpf(@PathVariable("cpf") String cpf) {
		
		Response<FuncionarioDto> response = new Response<FuncionarioDto>();
		Optional<Funcionario> funcionario = funcionarioService.buscaPorCpf(cpf);
		
		if (!funcionario.isPresent()) {
			response.getErrors().add("Funcionário não encontrado para o CPF " + cpf);
			return ResponseEntity.badRequest().body(response);
		}
		
		response.setData(this.convertFuncionarioDto(funcionario.get()));
		
		return ResponseEntity.ok(response);
	
	}
	
	@RequestMapping(value = "/id/{id}")
	public ResponseEntity<Response<FuncionarioDto>> buscarPorId(@PathVariable("id") Long id) {
		
		Response<FuncionarioDto> response = new Response<FuncionarioDto>();
		Optional<Funcionario> funcionario = funcionarioService.buscaPorId(id);
		
		if (!funcionario.isPresent()) {
			response.getErrors().add("Funcionário não encontrado para o ID " + id);
			return ResponseEntity.badRequest().body(response);
		}
		
		response.setData(this.convertFuncionarioDto(funcionario.get()));
			
		return ResponseEntity.ok(response);
	}
	
	/**
	 * Popula um DTO com os dados de um funcionario.
	 * 
	 * @param funcionario
	 * @return FuncionarioDto
	 */
	private FuncionarioDto convertFuncionarioDto(Funcionario funcionario) {
		
		FuncionarioDto funcionarioDto = new FuncionarioDto();
		funcionarioDto.setEmail(funcionario.getEmail());
		funcionarioDto.setId(funcionario.getId());
		funcionarioDto.setNome(funcionario.getNome());
		funcionario.getQtdHorasAlmocoOpt().ifPresent(
				qtdHorasAlmoco -> funcionarioDto.setQtdHorasAlmoco(Optional.of(Float.toString(qtdHorasAlmoco))));
		funcionario.getQtdHorasTrabalhoDiaOpt().ifPresent(
				qtdHorasTrabDia -> funcionarioDto.setQtdHorasTrabalhoDia(Optional.of(Float.toString(qtdHorasTrabDia))));
		funcionario.getValorHoraOpt()
				.ifPresent(valorHora -> funcionarioDto.setValorHora(Optional.of(valorHora.toString())));
		
		return funcionarioDto;
		
	}
}
