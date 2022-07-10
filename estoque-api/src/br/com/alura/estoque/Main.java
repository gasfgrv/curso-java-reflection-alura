package br.com.alura.estoque;

import java.util.Scanner;

import br.com.alura.alurator.Alurator;

public class Main {

	public static void main(String[] args) throws Exception {
		
		try (Scanner s = new Scanner(System.in)) {
			String url = s.nextLine();
			
			Alurator alurator = new Alurator("br.com.alura.estoque.controle.");
			while (!url.equals("exit")) {
				Object response = alurator.executa(url);
				
				System.out.println("Response: " + response);
				
				url = s.nextLine();
			}
		}
	}

}
