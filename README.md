## Grupo
- Bruna Cruz
- Guilherme Vanderley
- Joel Medeiros
- Sarah Rocha


## Setup

Caso não tenha instalado o Java e o Node.js em sua máquina, siga os passos dos sites abaixo para a instalação:

 - [Instalar Java](https://www.oracle.com/java/technologies/downloads/?er=221886)
 - [Instalar Node](https://nodejs.org/pt)

Entre no diretório *server* e rode o comando:

```bash
  cd server
  npm install
```

Volte para a raiz do projeto e digite o comando para gerar as classes da gramática:

```bash
  cd ..
  java -jar lib/antlr.jar FormDsl.g4 -o src 
```

Compile os arquivos Java:

```bash
  javac -cp .:lib/antlr.jar src/FormGenerator.java src/FormDsl*.java
```

Escreva o código para seu formulário no arquivo input.formdsl de acordo com a documentação abaixo.

Caminhe para o diretório src e rode o comando para iniciar o gerador:

```bash
  cd src
  java FormGenerator
```

## Documentação
### Regras da Gramática
A DSL permite definir formulários com campos de texto e seleções, bem como personalizar botões e títulos. Abaixo estão as regras básicas da gramática:

### Definindo um Formulário
A definição de um formulário começa com a palavra-chave Form, seguida pelo nome do formulário e um conjunto de campos entre chaves {}.

```
Form FormName {
  field1: text,
  field2: select { option1, option2, option3 }
}
````

### Definindo Campos
Os campos podem ser do tipo text ou select. Campos de seleção (select) devem incluir um conjunto de opções entre chaves {}.

```
fieldName: text,
fieldName: select { option1, option2, option3 }
````

### Personalizando Botões
A personalização de botões é feita com a palavra-chave Button, seguida por um conjunto de atributos entre chaves {}. Os atributos incluem text e color.

````
Button {
  text: "Submit",
  color: "#FFA500"
}
````

### Personalizando Títulos
A personalização de títulos é feita com a palavra-chave Title, seguida por um conjunto de atributos entre chaves {}. O atributo incluído é color.

```
Title {
  color: "#000000"
}
```
### Exemplo Completo
```
Form Cadastro {
  nome: text,
  idade: text,
  endereco: select { option1, option2, option3 }
}

Button {
  text: "Enviar",
  color: "#FFA500"
}

Title {
  color: "#000000"
}
```