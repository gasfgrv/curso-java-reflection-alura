package br.com.alura.alurator;

import java.util.Map;

import br.com.alura.alurator.conversor.ConversorXML;
import br.com.alura.alurator.protocolo.Request;
import br.com.alura.alurator.reflexao.Reflexao;

public class Alurator {

	private String pacoteBase;

	public Alurator(String pacoteBase) {
		this.pacoteBase = pacoteBase;
	}

	public Object executa(String url) {
		Request request = new Request(url);
		Map<String, Object> params = request.getQueryParams();
		
		String nomeControle = request.getNomeControle();
		String nomeMetodo = request.getNomeMetodo();

		Object retorno = new Reflexao()
				.refleteClasse(pacoteBase + nomeControle)
				.criaInstancia()
				.getMetodo(nomeMetodo, params)
				.comTratamentoDeExcecao((metodo, ex) -> {
					System.out.println("Erro no m�todo " + metodo.getName() + " da classe" + metodo.getDeclaringClass().getName() + ".\n\n");
					throw new RuntimeException("Erro no m�todo");
				})
				.invoca();
		
		System.out.println(retorno);

		retorno = new ConversorXML().converte(retorno);
		
		return retorno;
	}
}
