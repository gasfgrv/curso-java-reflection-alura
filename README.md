# **Java Reflection**

## **Entendendo Metaprogramação**
    
Sobre metaprogramação: Metaprogramação é a programação de programas que escrevem ou manipulam outros programas (ou a si próprios) assim como seus dados. O ideal é utilizar metaprogramação somente em casos onde só se tenha acesso a determinadas informações em tempo de execução.
    
### **Classe Class<T>**
        
**Serve para:**

- Listar atributos (privados ou não);
- Listar métodos (privados ou não);
- Listar construtores;
- Executar métodos;
- Criar objetos da classe em questão.
        
**Obtendo objeto da classe Class<T>**

- `object.getClass()`
- `class literal object.class`
- `Class.forName()`

**Criação de objetos na prática**

- `Class.newInstance()`
  - Foi descontinuado a partir do JDK 9


### **Classe Constructor<T>**
    
**O problema do método newInstance()**

Ele irá propagar exceções, mesmo que sejam checadas do construtor que estivermos invocando.
        
Serve para:
    
- Listar todas as exceções que podem ser lançadas do construtor;
- Listar todos os tipos de parâmetros do construtor;
- Perguntar quantos são os parâmetros que aquele construtor;
- Criar uma instância da classe a qual aquele construtor pertence;
- Todos são obtidos a partir da classe Class.

**Obter um objeto da classe Constructor<T>:**
    
- `getConstructors()`: Irá retornar todos os construtores públicos da classe em questão
- `getConstrutor(Class<?>... args)`: Esse parâmetro representa os tipos de cada um dos parâmetros. Esse método retornará um único construtor público cujos parâmetros tenham exatamente os tipos que passamos para ele. Além disso, ele não considerará os construtores de uma possível superclasse que a nossa classe extenda.
- `getDeclaredConstructors()`: Retorna todo e qualquer construtor da classe, seja ele público, privado, protegido, ou tenha qualquer outro modificador de acesso.
- `getDeclaredConstructor(Class<?>... args)`: Retorna um construtor, seja ele público ou privado, mas não encontrará construtores de uma superclasse;
    
**Um detalhe:** se utilizarmos os métodos getDeclaredConstructors() ou getDeclaredConstructor() e tentarmos manipular um construtor privado, teremos um problema, já que será lançada uma exceção. No entanto, é possível pegar o construtor privado que é objeto da classe Constructor e utilizar o método setAcessible(), passando para ele um valor booleano que será uma flag (boolean flag). Se passarmos true, estaremos indicando para a máquina virtual do Java que estamos dando a possibilidade de manipular aquele construtor, mesmo ele sendo privado.

No entanto, a máquina virtual do Java pode não permitir a troca de acessibilidade desse método por meio do Security Manager.

**A vantagem do Constructor.newInstance()**
    
Quando invocado, ele obriga  que haja um tratamento de uma InvocationTargetException, sendo essa sempre lançada quando o construtor lance uma exceção
    
### **Classe Method**

Serve para:

- listar as exceções que o método em questão pode lançar;
- listar os tipos de parâmetros que ele recebe;
- indicar qual é o retorno que o nosso método tem;
- invocar o método em questão.

**Como obter o objeto:**
    
- `getMethods()`: Com ele, iremos recuperar todos os métodos públicos (e somente eles) referentes à classe em questão que estivermos parametrizando na classe Class<T>, e também aos métodos da superclasse dela ou das interfaces que a nossa classe implemente.
- `getMethod(String nome, Class<?>... args)`: Iremos passar como primeiro argumento uma string representando o nome do método público que queremos recuperar, e como segundo argumento uma lista de objetos do tipo Class<T> representando os tipos dos argumentos que queremos que o método em questão tenha.
- `getDeclaredMethods()`: Retornará todos os métodos públicos, privados ou quaisquer forem os modificadores de acesso que eles tenham. No entanto, diferentemente dos anteriores, ele só levará em consideração os métodos da nossa própria classe - ou seja, não irá considerar alguma possível superclasse nem interfaces que a nossa classe implemente.
- `getDeclaredMethod(String nome, Class<?>...args)`: Retornará um único método, seja público, privado ou tenha outro qualquer modificador de acesso. Deveremos passar uma string como primeiro parâmetro, indicando qual é o nome do método que queremos recuperar, e como segundo parâmetro um varargs representando as classes dos tipos dos parâmetros esperados como argumento do método que queremos recuperar.
    
Tanto o getDeclaredMethod() quanto o getDeclaredMethods() podem retornar métodos privados. Se tentarmos manipular um método privado, teremos um erro. Porém, a exemplo dos construtores, também podemos utilizar o método setAccessible(), passando a boolean flag true, indicando que queremos manipular esse método mesmo ele sendo privado.

Entretanto, mesmo nesse caso, podemos ser bloqueados pelo famoso Security Manager, que vimos quando estávamos aprendendo a manipular construtores com a classe Constructor<T>.
Dado que tenhamos uma instância da classe Method representando o método que queremos executar, podemos executá-lo e invocá-lo por meio do método invoke(Object obj, Object... args). No entanto, ele precisa receber dois parâmtros: o primeiro deles é um Object representando a instância do objeto do qual queremos executar o método em questão, e o segundo um varargs representando todos os parâmetros que nosso método precisa receber.

Esse método invoke() nos devolvará um obj, representando o retorno do método que executamos. Veremos mais detalhadamente esses permenores quando estivermos executando nossos testes.


### **Exceptions**

`ClassNotFoundException`

Ela ocorre quando estamos procurando uma clase que não foi compilada pelo Java - por exemplo, quando tentamos utilizar o método classForName() passando o fully qualified name de uma classe que não existia.

`NoClassDefFoundError`

A diferença é que ela ocorre quando estamos procurando uma classe que foi compilada pelo Java, mas erramos o nome dela - por exemplo, quando procuramos uma classe produtoController com p minúsuclo.

`NoSuchMethodException` e `NoSuchFieldException`

Ambas vão acontecer quando tentamos recuperar uma informação da maneira incorreta, ou se a informação não está no local onde estamos procurando - no caso da primeira exceção, essa informação é um método; e no caso da segunda, é um atributo.

`InvocationTargetException`

Essa exceção será lançada quando o construtor ou o método que estivermos invocando lançar dentro dele uma exceção. Ou seja, a InvocationTargetException irá encapsular a exceção lançada dentro do construtor ou do método.

## **Anotações e Injeção de Dependências**
    
### **Manipulação de atributos**

Se tivermos um objeto da classe `Class<T>` parametrizado para a classe que queremos inferir, teremos quatro formas de recuperar so atributos dessa classe.
            
A primeira é o métood `getFields()`, que retornará um array de objetos do tipo `Field`. Essa é uma classe da API de Reflection do Java que existe simplesmente para representar um atributo de uma classe. O array retornado será composto por todos os atributos públicos da classe que está sendo inferida pela classe Class<T>, ou por possíveis superclasses dela.
            
A segunda forma é por meio do método `getField()`, que recebe uma string indicando o nome do atributo que queremos recuperar. Ou seja, se a classe tem um atributo idade, precisaremos passar para o método getField() uma string com o valor idade. Novamente, a exemplo do método anterior, o getField() só retornará atributos públicos da classe que está sendo inferida pela classe Class<T> ou atributos públicos de possíveis superclasses que ela estenda.

Prosseguindo, temos o método `getDeclaredFields()`, que também retornará um array de objetos do tipo Field. A diferença é que esse array será composto por atributos públicos ou não - ou seja, os privados serão considerados nesse caso -, levando em conta somente atributos da classe que está sendo representada e excluindo os de possíveis superclasses.

Por últimos, temos o `getDeclaredField()`, que assim como o getField() recebe uma string representando o nome do atributo que queremos recuperar, nos retornando um objeto do tipo Field caso esse atributo seja encontrado. Do contrário, ele lançará uma exceção do tipo NoSuchFieldException. Esse método também retorna atributos públicos ou não, e apenas da classe inferida pela classe Class<T>.

Nos dois últimos métodos, podemos ter como retorno um atributo privado. Se tentarmos manipulá-lo, por exemplo tentando pegar o seu valor, obteremos uma exceção IlegalAccessException, que já vimos na parte 1. Ou seja, se quisermos manipular um atributo privado, teremos que setar a sua acessibilidade com o já conhecido setAccessible, passando como argumento a flag true. No entanto, pode ser que o Security Manager não permita essa mudança e lance uma exceção na nossa aplicação.
    
### **Anotações**

Surgiu na versão 5 do Java, e possibilitou que os desenvolvedores dessa linguagem colocassem metadados no código. Metadados nada mais são do que dados relativos a uma informação já existente, que é o nosso código. Ou seja, um metadado dentro de um código Java nada mais é do que uma informação relativa a um trecho de código escrito anteriormente.

Quando os desenvolvedores do Java criaram a especificação de anotações, para não criarem uma nova palavra chave, simplesmente adicionaram o caractere @ antes da palavra interface. Isso porque as anotações, na realidade, nada mais são do que interfaces especiais, já que toda anotação é, por baixo dos panos, uma sub-interface da interface Annotation do pacote java.lang.annotation.

A declaração de uma anotação precisa seguir algumas regras,  ela não pode estender nenhuma classe nem implementar uma interface, ainda que toda anotação seja uma interface de Annotation. Além disso, precisamos informar ao java quando essa anotação deverá ser levada em consideração - se é no momento em que a aplicação for executada, se é antes de compilar, etc. Para configurar as anotações dessa forma, existem as meta-anotações.
                
A primeira delas é a `@Retention`, do pacote java.lang.annotation. Essa meta-anotação precisa receber uma configuração entre parênteses informando quando a nossa anotação deverá ser levada em conta.

Em seguida, informaremos onde queremos utilizar a anotação que estamos criando, e para isso usaremos a meta-anotação `@Target`, também do pacote java.lang.annotation. Ela também como parâmetros os valores de onde queremos utilizar a anotação, como na declaração de uma classe, no atributo de uma classe, em um método, em um parâmetro, etc. Como queremos fazer isso em mais de um lugar, passaremos um array {} com os valores ElementType.TYPE, para definirmos a utilização dessa anotação na declaração de uma classe, e ElementType,FIELD, para definirmos a utilização em um atributo de uma classe qualquer.

Por convenção, toda anotação que tem apenas um atributo precisa que esse atributo receba o nome `value()`.

Quando definimos o único atributo da nossa atuação como value, não precisamos escrever o nome do atributo nos usos dessa anotação.

**Meta anotações**

São anotações que são usadas para acrescentar informações à anotações que estão sendo criadas.
- `@Retention`: especifica como a anotação deve ser considerada pelo Java.
  - `RetentionPolicy.SOURCE`: A anotação é considerada apenas a nível de código fonte e ignorada pelo compilador.
  - `RetentionPolicy.CLASS` **(valor padrão)**: A anotação é levada em consideração pelo compilador em tempo de compilação, mas ignorada pela JVM em tempo de execução.
  - `RetentionPolicy.RUNTIME`: A anotação será considerada pela JVM em tempo de execução.
- `@Documented`: Indica se a anotação deve ser documentada no Javadoc juntos aos elementos onde for utilizada.
- `@Target`: Indica onde a anotação pode ser utilizada no código Java.
  - `ElementType.ANNOTATION_TYPE`: pode ser aplicada numa anotação.
  - `ElementType.CONSTRUCTOR`: pode ser aplicada num construtor.
  - `ElementType.FIELD`: pode ser aplicada num atributo.
  - `ElementType.LOCAL_VARIABLE`: pode ser aplicada numa variável local.
  - `ElementType.METHOD`: pode ser aplicado num método.
  - `ElementType.PACKAGE`: pode ser aplicada numa declaração de pacote.
  - `ElementType.PARAMETER`: pode ser aplicada num parâmetro de método.
  - `ElementType.TYPE`: pode ser aplicada na declaração de uma classe, interface (incluindo anotações) ou enum.
- `@Inherited`: indica que a anotação será herdada por classes filhas quando usada numa super classe. Essa herança não ocorre por padrão! Além disso, anotações "herdáveis" só podem ser aplicadas em classes.
- `@Repeatable`: introduzida a partir do Java 8, indica que a anotação pode ser aplicada mais de uma vez no mesmo lugar.
        
Toda classe que representa no Java algum pedaço do nosso código (seja uma classe, um construtor, um método ou um atributo) possui métodos responsáveis por verificar se determinada anotação está sendo utilizada e qual é o valor dela.
    
### **Injeção de dependências**
    
**Inversão de controle e injeção de dependências**

Ambos os conceitos são muito importantes na carreira de um desenvolvedor - tão importantes que o Java EE possui uma especificação apenas para trabalhar com eles, chamada CDI. É ela que define como se darão essas questões no mundo Java, como quem será responsável por criar as dependências que as classes precisas. No caso, foi definido que os responsáveis pela criação dessas dependências são os chamados container de injeção de dependência, e o maior deles, e mais conhecido - considerado a implementação padrão dessa especificação -, é o famoso Weld.