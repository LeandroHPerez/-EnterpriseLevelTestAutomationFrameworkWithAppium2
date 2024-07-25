
# Framework genérico de automação de testes de software Enterprise

Esse projeto visa facilitar a adoção e uso de um framework para automação de testes de software, fornecendo uma base inicial testada e pronta, que já consta com boa parte do código comum a todo framework necessário para a automação de testes.  

O Framework é desenvolvido tendo em mente os desafios dessa atividade em ambientes e softwares complexos.  

O framework serve como um ponta pé inicial, podendo ser estudado, usado sem restrições, aprimorado e modificado para atender necessidades específicas de cada projeto.  

A linguagem utilizada é o **Java**, os cenários de teste podem ser escritos utilizando **Gherkin** com uso de Cucumber.  
Podem ser escritos testes para:
- Aplicativos nativos para **Android e iOS** utilizando *Appium*.  
- Aplicações **Web** com uso de *Selenium*. (Suporte futuro)
- Testes de **serviços backend** são suportados com uso de *Rest-assured*. (Suporte futuro)  

As ferramentas que apoiam o framework são gratuítas, têm código aberto e são vastamente utilizadas no mercado.  
Consulte a licença de cada ferramenta utilizada para mais detalhes.


**Obs.**: Esse projeto é desenvolvido e mantido por [@Leandro Henrique Perez](https://www.github.com/LeandroHPerez) e têm licença permissiva MIT.  
Créditos ao autor não são necessários, mas são apreciados.

## Capacidades suportadas
- Construção de cenários de teste utilizando Gherkin e possibilidade de aplicação de BDD
- Testes para aplicativos mobile Android e iOS utilizando Appium
- Testes em aplicações web (browser) utilizando Selenium (suporte futuro)
- Testes em serviços backend utilizando Rest-assured (suporte futuro)

**Obs.**: Testes para iOS só são possíveis quando o ambiente de desenvolvimento é MacOS.
Não é possível construir nem executar os testes em aplicação para iOS fora do ecossistema da Apple / macOS.
## Referência
 - [jUnit](https://junit.org/junit5/)
 - [Cucumber](https://cucumber.io/)
 - [Appium](hhttps://appium.io/)
 - [Selenium](https://www.selenium.dev/)
 - [Rest-assured](https://rest-assured.io/)


## Stack utilizada

**Java:** jUnit, Cucumber, Appium, Selenium, Rest-assured




## Instalação de ferramentas

Para configurar o ambiente para construir e executar testes os seguintes passos podem ser executados:

### MacOS:

#### 1 - Instalar Homebrew  
Instale o **Homebrew** (um sistema de gerenciamento de pacotes que facilita a instalação de software em  MacOS).  
Encontre o Homebrew e siga as instruções de instalação neste site:  https://brew.sh

Após a instalação do brew, será **necessário** instalar *Carthage*, um gerenciador de dependência e *Node.js* para termos acesso ao *NPM*

abra um Terminal e digite:

```bash
brew install carthage
```

```bash
brew install node
```

#### 2 - Instalar Java:
Baixe o **JDK** neste site:
https://www.oracle.com/java/technologies/javase-downloads.html

Execute o instalador baixado e siga as instruções que você verá na tela.

Após a instalação, execute o seguinte comando no Terminal:
```bash
/usr/libexec/java_home --v
```
Isso gerará uma string como /Library/Java/JavaVirtualMachines/jdk1.8.0_171.jdk/Contents/Home. Esta é a localização do JDK no seu computador. **Copie este valor**.

Abra seu arquivo .bash_profile em um editor, se preferir digite no terminal:
```bash
open ~/.bash_profile
```
 e adicione as duas linhas a seguir:
 ```bash
export JAVA_HOME="copied-path-to-JDK-directory"
export PATH=$JAVA_HOME/bin:$PATH
```


Salve as alterações no arquivo .bash_profile.

Reinicie o Terminal para obter as novas configurações .bash_profile.

#### 3 - Instalar Appium:
Você pode fazer isso de uma das seguintes maneiras:
Instale o Appium Server. 

Você pode baixar o instalador neste site (este método de instalação do Appium não é recomendado nem suportado):
https://github.com/appium/appium/releases


\- ou -

Instale o Appium com npm (**método recomendado**).  
Execute o seguinte comando na janela do Terminal:
```bash
npm instalar -g appium
```
Isso instalará a última versão do appium.

##### **Para instalar um versão específica utilize (recomendado):**
```bash
npm install -g appium@version
```
**Ex.:**
```bash
npm install -g appium@2.11
```


#### 4 - Appium Doctor - (opcional, porém altamente recomendado)
Configurar o ambiente para Appium não é uma tarefa trivial, muitas coisas podem dar errado ou não estarem perfeitamente ajustadas. O AppiumDoctor ajuda a encontrar e ajustar falhas no ambiente.

```bash
npm install @appium/doctor -g
```


## Rodando localmente

Clone o projeto

```bash
  git clone hhttps://github.com/LeandroHPerez/-EnterpriseLevelTestAutomationFrameworkWithAppium2
```

Entre no diretório do projeto

```bash
  cd m-EnterpriseLevelTestAutomationFrameworkWithAppium2
```

Verifique a seção de Instalação e informações adicionais no arquivo README.md

```bash
  README.md
```


## Licença
MIT License

Copyright (c) [2024] [Leandro Henrique Perez]

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.


## Autores

- [@Leandro Henrique Perez](https://www.github.com/LeandroHPerez)

