package br.com.alura.estoque;

import java.util.Scanner;

import br.com.alura.alurator.Alurator;
import br.com.alura.estoque.dao.ProdutoDao;
import br.com.alura.estoque.dao.ProdutoDaoMock;

public class Main {

	public static void main(String[] args) throws Exception {

		try (Scanner s = new Scanner(System.in)) {
			String url = s.nextLine();

			Alurator alurator = new Alurator("br.com.alura.estoque.controle.");
			alurator.registra(ProdutoDao.class, ProdutoDaoMock.class);

			while (!url.equals("exit")) {
				Object response = alurator.executa(url);

				System.out.println("Response: " + response);

				url = s.nextLine();
			}
		}
	}

}
