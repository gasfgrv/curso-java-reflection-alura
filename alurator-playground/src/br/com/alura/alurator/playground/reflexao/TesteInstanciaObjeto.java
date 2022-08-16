package br.com.alura.alurator.playground.reflexao;

import java.lang.reflect.InvocationTargetException;

import br.com.alura.alurator.playground.controle.Controle;

public class TesteInstanciaObjeto {

	public static void main(String[] args) throws ClassNotFoundException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
		Class<Controle> controleClasse1 = Controle.class;
		
		Controle instanciaControle = new Controle();
		Class<? extends Controle> controleClasse2 = instanciaControle.getClass();
		
		Class<?> controleClasse3 = Class.forName(Controle.class.getName());
		
		
		Controle objetoControle1 = controleClasse1.getDeclaredConstructor(null).newInstance(null);
		
		System.out.println(objetoControle1 instanceof Controle);
	}
	
}
