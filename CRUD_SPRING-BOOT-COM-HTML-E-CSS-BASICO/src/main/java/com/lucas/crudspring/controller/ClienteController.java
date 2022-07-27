package com.lucas.crudspring.controller;

import com.lucas.crudspring.model.entities.Cliente;
import com.lucas.crudspring.model.entities.Produto;
import com.lucas.crudspring.model.repositories.ClienteRepository;
import com.lucas.crudspring.model.repositories.ProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;

@Controller
//@RestController //somente para postman
@RequestMapping(path = "/clientes")
public class ClienteController {
	
	@Autowired
	private ClienteRepository clienteRepository;
	@Autowired
	ProdutoRepository produtoRepository;

	public ClienteRepository getClienteRepository() {
		return clienteRepository;
	}

	public void setClienteRepository(ClienteRepository clienteRepository) {
		this.clienteRepository = clienteRepository;
	}

	
	//USANDO HTML
	
	//LENDO CLIENTE
	@RequestMapping(value ="/cadastrarCliente", method = RequestMethod.GET)
	public String formCliente() {
		return "cliente/formCliente";
	}
	
	//CRIANDO CLIENTE
	@RequestMapping(value = "/cadastrarCliente", method = RequestMethod.POST)
	public String formCliente(Cliente cliente) {
		clienteRepository.save(cliente);
		return "redirect:/clientes/cadastrarCliente";	
	}
	
	//LISTA DE CLIENTES NA VIEW 
	@RequestMapping("/buscarClientes")
	public ModelAndView listaCliente() {
		ModelAndView mv = new ModelAndView("index");
		Iterable<Cliente> listaClientes = clienteRepository.findAll();
		mv.addObject("clientes", listaClientes);
		return mv;
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ModelAndView detalhesCliente(@PathVariable int id) {
		Cliente cliente = clienteRepository.findById(id);
		ModelAndView mv = new ModelAndView("cliente/detalhesClientes");
		mv.addObject("cliente", cliente);
		Iterable<Produto> produtos = produtoRepository.findByCliente(cliente);
		mv.addObject("produtos",produtos);
		return mv;
	}


	@RequestMapping(value = "/{id}", method = RequestMethod.POST)
	public String detalhesClientePost(@PathVariable int id, Produto produto) {
		Cliente cliente = clienteRepository.findById(id);
		produto.setCliente(cliente);
		produtoRepository.save(produto);
		return "redirect:/clientes/{id}";
	}
	@RequestMapping("/deletarCliente")
	public String deletarCliente(int id){
		Cliente cliente = clienteRepository.findById(id);
		clienteRepository.delete(cliente);
		return "redirect:/clientes/buscarClientes";
	}

	@RequestMapping(value = "/updateCliente/{id}", method = RequestMethod.GET)
	public ModelAndView updateCliente(@PathVariable int id){
		ModelAndView mv = new ModelAndView("cliente/alterarCliente");
		Cliente cliente = clienteRepository.findById(id);
		mv.addObject("cliente", cliente);
		return mv;
	}
	@RequestMapping(value = "/updateCliente/{id}", method = RequestMethod.POST)
	public String updateClientePost(@PathVariable int id, @Valid Cliente cliente){
		clienteRepository.findById(id);
		clienteRepository.save(cliente);
		return "redirect:/clientes/buscarClientes";
	}
	

	//USANDO POSTMAN
	
	//CADASTRAR UM NOVO CLIENTE
	@PostMapping("/cadastrar")
	public @ResponseBody Cliente novoCliente(@Valid Cliente cliente) {
		clienteRepository.save(cliente);
		return cliente;
	}
	
	//ALTERAR CLIENTE
	@PutMapping("/alterar")
	public @ResponseBody Cliente alterarCliente(@Valid Cliente cliente) {
		clienteRepository.save(cliente);
		return cliente;
	}
	
	//PESQUISAR CLIENTE POR ID
	@GetMapping("/pesquisar/{id}")
	public Cliente pesquisarCliente(@PathVariable int id) {
		return clienteRepository.findById(id);	
	}
	
	//DELETAR CLIENTE POR ID
	@DeleteMapping("/deletar/{id}")
	public void deletarClientePostMan(@PathVariable int id) {
		 clienteRepository.deleteById(id);
	}
	
	//BUSCAR POR PAGINA OS CLIENTES
	@GetMapping("/buscarlista/{numeroPagina}/{qntPagina}")
	public Iterable<Cliente> buscarPorPagina(@PathVariable int numeroPagina, @PathVariable int qntPagina) {
		if(qntPagina >=20) {
			qntPagina = 10;
		}
		Pageable page = PageRequest.of(numeroPagina, qntPagina);
		return clienteRepository.findAll(page);
	}
	
	//BUSCAR CLIENTES POR NOME
	@GetMapping("/buscar/{nomeCliente}")
	public Iterable<Cliente> buscarPorNome(@PathVariable String nomeCliente) {
			return clienteRepository.findByNomeContainingIgnoreCase(nomeCliente);
	}
	
		
				
	
	
}
