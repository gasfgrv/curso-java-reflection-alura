package br.com.alura.alurator;

import java.util.Map;

import br.com.alura.alurator.conversor.ConversorXML;
import br.com.alura.alurator.ioc.ContainerIoC;
import br.com.alura.alurator.protocolo.Request;
import br.com.alura.alurator.reflexao.ManipuladorObjeto;
import br.com.alura.alurator.reflexao.Reflexao;

public class Alurator {

	private String pacoteBase;
	private ContainerIoC container;

	public Alurator(String pacoteBase) {
		this.pacoteBase = pacoteBase;
		this.container = new ContainerIoC();
	}

	public Object executa(String url) {
		Request request = new Request(url);
		Map<String, Object> params = request.getQueryParams();
		
		String nomeControle = request.getNomeControle();
		String nomeMetodo = request.getNomeMetodo();

		Class<?> classeControle = new Reflexao().getClasse(pacoteBase + nomeControle);
		Object instanciaControle = container.getInstancia(classeControle);
		Object retorno = new ManipuladorObjeto(instanciaControle)
				.getMetodo(nomeMetodo, params)
				.comTratamentoDeExcecao((metodo, ex) -> {
					System.out.println("Erro no método " + metodo.getName() + " da classe" + metodo.getDeclaringClass().getName() + ".\n\n");
					throw new RuntimeException("Erro no método");
				})
				.invoca();
		
		System.out.println(retorno);

		retorno = new ConversorXML().converte(retorno);
		
		return retorno;
	}
}
