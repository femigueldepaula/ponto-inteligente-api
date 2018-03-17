package com.kazale.inteligente.api.controllers;

import java.math.BigDecimal;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.kazale.inteligente.api.dtos.FuncionarioDto;
import com.kazale.inteligente.api.entities.Funcionario;
import com.kazale.inteligente.api.response.Response;
import com.kazale.inteligente.api.services.FuncionarioService;
import com.kazale.inteligente.api.utils.PasswordUtils;

@RestController
@RequestMapping("/api/funcionarios")
public class FuncionarioController {

	private static final Logger log = LoggerFactory.getLogger(EmpresaController.class);
	
	@Autowired
	private FuncionarioService funcionarioService;


	/**
	 * Atualiza os dados de um funcionário.
	 * 
	 * @param id
	 * @param funcionarioDto
	 * @param result
	 * @return ResponseEntity<Response<FuncionarioDto>>
	 * @throws NoSuchAlgorithmException
	 */
	@PutMapping(value = "/{id}")
	public ResponseEntity<Response<FuncionarioDto>> atualizar(@PathVariable("id") Long id,
			@Valid @RequestBody FuncionarioDto funcionarioDto, BindingResult result) throws NoSuchAlgorithmException {
		
		log.info("Atualizando Funcionário: {}", funcionarioDto.toString());
		
		Response<FuncionarioDto> response = new Response<FuncionarioDto>();
		
		Optional<Funcionario> funcionario = this.funcionarioService.buscaPorId(id);
		if (!funcionario.isPresent()) {
			result.addError(new ObjectError("funcionario", "Funcionario não encontrado"));
		}
		
		this.atualizarDadosFuncionario(funcionario.get(), funcionarioDto, result);
		
		if (result.hasErrors()) {
			log.error("Erro validando funcionário: {}", result.getAllErrors());
			result.getAllErrors().forEach(error -> response.getErrors().add(error.getDefaultMessage()));
			return ResponseEntity.badRequest().body(response);
		}
		
		this.funcionarioService.persistir(funcionario.get());
		response.setData(this.convertFuncionarioDto(funcionario.get()));
		return ResponseEntity.ok(response);
	}

	/**
	 * Retrona um funcionario por CPF
	 * 
	 * @param cpf
	 * @return ResponseEntity<Response<FuncionarioDto>>
	 */
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

	/**
	 * Retrona um funcionario por ID
	 * 
	 * @param id
	 * @return ResponseEntity<Response<FuncionarioDto>>
	 */
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
	 * Retrona todos os funcionarios
	 * 
	 * 
	 * @return ResponseEntity<List<Response<FuncionarioDto>>>
	 */
	@RequestMapping(value = "/todos")
	public ResponseEntity<Response<List<FuncionarioDto>>> buscarTodos() {

		Response<List<FuncionarioDto>> response = new Response<List<FuncionarioDto>>();
		List<Funcionario> funcionarios = funcionarioService.buscaTodos();

		if (funcionarios == null || funcionarios.isEmpty()) {
			response.getErrors().add("Não existem funcionários cadastrados.");
			return ResponseEntity.badRequest().body(response);
		}

		response.setData(this.convertFuncionariosDto(funcionarios));

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

	/**
	 * Popula um List de DTO com os dados de uma lista funcionarios.
	 * 
	 * @param List<Funcionario>
	 * @return List<FuncionarioDto>
	 */
	private List<FuncionarioDto> convertFuncionariosDto(List<Funcionario> funcionarios) {

		List<FuncionarioDto> funcionariosDto = new ArrayList<FuncionarioDto>();

		if (funcionarios != null && !funcionarios.isEmpty()) {
			for (Funcionario funcionario : funcionarios) {

				FuncionarioDto funcionarioDto = new FuncionarioDto();
				funcionarioDto.setEmail(funcionario.getEmail());
				funcionarioDto.setId(funcionario.getId());
				funcionarioDto.setNome(funcionario.getNome());
				funcionario.getQtdHorasAlmocoOpt().ifPresent(qtdHorasAlmoco -> funcionarioDto
						.setQtdHorasAlmoco(Optional.of(Float.toString(qtdHorasAlmoco))));
				funcionario.getQtdHorasTrabalhoDiaOpt().ifPresent(qtdHorasTrabDia -> funcionarioDto
						.setQtdHorasTrabalhoDia(Optional.of(Float.toString(qtdHorasTrabDia))));
				funcionario.getValorHoraOpt()
						.ifPresent(valorHora -> funcionarioDto.setValorHora(Optional.of(valorHora.toString())));

				funcionariosDto.add(funcionarioDto);
			}
		}
		return funcionariosDto;

	}
	
	/**
	 * Atualiza os dados do funcionário com base nos dados encontrados no DTO.
	 * 
	 * @param funcionario
	 * @param funcionarioDto
	 * @param result
	 * @throws NoSuchAlgorithmException
	 */
	private void atualizarDadosFuncionario(Funcionario funcionario, FuncionarioDto funcionarioDto, BindingResult result)
			throws NoSuchAlgorithmException {
		funcionario.setNome(funcionarioDto.getNome());

		if (!funcionario.getEmail().equals(funcionarioDto.getEmail())) {
			this.funcionarioService.buscaPorEmail(funcionarioDto.getEmail())
					.ifPresent(func -> result.addError(new ObjectError("email", "Email já existente.")));
			funcionario.setEmail(funcionarioDto.getEmail());
		}

		funcionario.setQtdHorasAlmoco(null);
		funcionarioDto.getQtdHorasAlmoco()
				.ifPresent(qtdHorasAlmoco -> funcionario.setQtdHorasAlmoco(Float.valueOf(qtdHorasAlmoco)));

		funcionario.setQtdHorasTrabalhoDia(null);
		funcionarioDto.getQtdHorasTrabalhoDia()
				.ifPresent(qtdHorasTrabDia -> funcionario.setQtdHorasTrabalhoDia(Float.valueOf(qtdHorasTrabDia)));

		funcionario.setValorHora(null);
		funcionarioDto.getValorHora().ifPresent(valorHora -> funcionario.setValorHora(new BigDecimal(valorHora)));

		if (funcionarioDto.getSenha().isPresent()) {
			funcionario.setSenha(PasswordUtils.gerarBCrypt(funcionarioDto.getSenha().get()));
		}
	}
}
