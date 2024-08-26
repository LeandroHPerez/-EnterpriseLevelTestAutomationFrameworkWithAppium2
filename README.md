
# Framework genérico de automação de testes de software Enterprise

Esse projeto visa facilitar a adoção e uso de um framework para automação de testes de software, fornecendo uma base inicial testada e pronta, que já consta com boa parte do código comum a todo framework necessário para a automação de testes.

O Framework é desenvolvido tendo em mente os desafios dessa atividade em ambientes e softwares complexos.

O framework serve como um ponta pé inicial, podendo ser estudado, usado sem restrições, aprimorado e modificado para atender necessidades específicas de cada projeto.

A linguagem utilizada é o **Java**, os cenários de teste podem ser escritos utilizando **Gherkin** com uso de Cucumber.  
Podem ser escritos testes para:
- Aplicativos nativos para **Android e iOS** utilizando *Appium*.
- Aplicações **Web** com uso de *Selenium*. (Suporte futuro)
- Testes de **serviços backend** são suportados com uso de *Rest-assured*. (Suporte futuro)

As ferramentas que apoiam o framework são gratuitas, têm código aberto e são vastamente utilizadas no mercado.  
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
## Stack utilizada

**Java:** jUnit, Cucumber, Appium, Selenium, Rest-assured




## Instalação de ferramentas e configuração do Ambiente

Para configurar o ambiente para construir e executar testes os seguintes passos podem ser executados:

### MacOS 10.5 ou mais novo:

#### 1 - Instalar Homebrew
Instale o **Homebrew** (um sistema de gerenciamento de pacotes que facilita a instalação de software em MacOS).  
Encontre o Homebrew e siga as instruções de instalação neste site:  https://brew.sh

Após a instalação do brew, será **necessário** instalar *Carthage*, um gerenciador de dependência, *Node.js* para termos acesso ao *NPM*.

Obs.: Node.js é um componente essencial para executar o Appium. Appium é construído sobre Node.js, que é um ambiente de execução JavaScript.
Node.js vem com npm, um gerenciador de pacotes para JavaScript. O npm permite que você instale, gerencie e distribua facilmente bibliotecas e dependências JavaScript, incluindo o próprio pacote Appium.

Abra um Terminal e digite:

```bash
brew install carthage
```

```bash
brew install node
```

#### 2 - Instalar Java:
Baixe o **JDK** neste site:
https://www.oracle.com/java/technologies/javase-downloads.html

O [JDK Development Kit 21](https://www.oracle.com/java/technologies/downloads/?er=221886#jdk21-mac) é a versão utilizada para a construção do Framework, assim sendo, essa versão é a indicada para a instalação. Atente-se para instalar a versão correta para teu sistema operacional.

Execute o instalador baixado e siga as instruções que você verá na tela.

Após a instalação, execute o seguinte comando no Terminal:
```bash
/usr/libexec/java_home --v
```
Isso gerará uma string como /Library/Java/JavaVirtualMachines/graalvm-jdk-21.0.4/Contents/Home. Esta é a localização do JDK no seu computador. **Copie este valor**.

Abra seu arquivo .zprofile (caso exista) em um editor, se preferir digite no terminal:
```bash
open ~/.zprofile
```

Se o arquivo .zprofile não existir, crie um via Terminal e após a criação é possível abri-lo com o comando *open ~/.zprofile*:
```bash
touch ~/.zprofile
```

e adicione as duas linhas a seguir substituindo o valor variável *JAVA_HOME* pelo que foi copiado anteriormente:
 ```bash
export JAVA_HOME="copied-path-to-JDK-directory"
export PATH=$JAVA_HOME/bin:$PATH
```
Salve as alterações no arquivo .zprofile.
Abra um terminal e execute para aplicar as mudanças:
 ```bash
source ~/.zprofile
```
Reinicie o Terminal para obter as novas configurações .zprofile.

\- ou -

Alternativamente pode ser feito assim (exportação de uma versão específica).
Adicione as duas linhas a seguir para automaticamente usar a versão JDK21.
 ```bash
export JAVA_HOME="$(/usr/libexec/java_home -v 21)"
export PATH=$JAVA_HOME/bin:$PATH
```

Salve as alterações no arquivo .zprofile.
Abra um terminal e execute para aplicar as mudanças:
 ```bash
source ~/.zprofile
```
Reinicie o Terminal para obter as novas configurações .zprofile.

\- ou -

**Caso não queira abrir e editar o arquivo manualmente, é possível adicionar as linhas ao arquivo conforme exemplo**:
 ```bash
echo export "JAVA_HOME=\$(/usr/libexec/java_home -v 21)" >> ~/.zprofile
echo export "PATH=\$PATH:\$JAVA_HOME/bin" >> ~/.bash_profile
```
O **parâmetro -v** *em "/usr/libexec/java_home -v 21"* informa a versão específica de qual Java será utilizado, no caso a versão 21. Pode haver mais que uma versão do Java instalada na mesma máquina, assim sendo, é recomendado informar qual versão será utilizada.

Abra um terminal e execute para aplicar as mudanças:
 ```bash
source ~/.zprofile
```
Reinicie o Terminal para obter as novas configurações .zprofile.

Verifique se as variáveis foram corretamente ajustadas, as saídas devem exibir o path correto:

 ```bash
echo $JAVA_HOME
echo $PATH
```

#### 3 - Instalar Appium:
Você pode fazer isso de uma das seguintes maneiras:
Instale o Appium Server.

Você pode baixar o instalador neste site (este método de instalação do Appium não é recomendado nem suportado):
https://github.com/appium/appium/releases


\- ou -

Instale o Appium com npm (**método recomendado**).  
Execute o seguinte comando na janela do Terminal:
```bash
npm install -g appium
```
Isso instalará a última versão do Appium.

##### **Para instalar uma versão específica utilize (recomendado):**
```bash
npm install -g appium@version
```
**Ex.:**
```bash
npm install -g appium@2.11
```


#### 4 - Appium Doctor - (opcional, porém altamente recomendado)
Configurar o ambiente para Appium não é uma tarefa trivial, muitas coisas podem dar errado ou não estarem perfeitamente ajustadas. O Appium Doctor ajuda a encontrar e ajustar falhas no ambiente.

```bash
npm install @appium/doctor -g
```


#### 4 - Verificar ambiente - (opcional, porém altamente recomendado)
Utilize o Appium Doctor (instalado anteriormente) para certificar que o ambiente está pronto e conforme às expectativas e necessidades para a automação de testes utilizando Appium.  
Execute o comando abaixo no Terminal para procurar falhas no ambiente.

No caso de falhas encontradas: será necessário ajustar aquela falha apontada e rodar novamente o comando abaixo, até que toda a saída do Appium Doctor indique que não há problemas. Algumas etapas necessárias como a configuração do Android Studio serão feitas nas próximas seções.

```bash
appium-doctor --dev
```

#### 5 - Instalação e configuração do Android Studio
Baixe o pacote de instalação no seguinte site:
https://developer.android.com/studio/

Execute o instalador e siga as instruções que você verá na tela.

Ao instalar, certifique-se de selecionar o componente *Android Virtual Device*, que são os emuladores de smartphones com sistema Android.

Inicie o Android Studio.

Na caixa de diálogo Bem-vindo, clique no ícone de configurar (três pontos) > SDK Manager. Isso abrirá a caixa de diálogo na aba *SDK Platforms*.   
Caso já tenha previamente aberto um projeto no Android Studio e a caixa de boas vindas não tenha sido exibida: Acesse via menu "Tools > SDK Manager".

Na guia Plataformas SDK, selecione os SDKs (versão de Android) necessários para teste.

Na parte superior da caixa de diálogo do SDK Manager há uma caixa de texto com o  *Android SDK Location* . Precisaremos deste valor mais tarde.
*Android SDK Location* terá uma string como */Users/leandroperez/Library/Android/sdk*. Esta é a localização do de todo o Android SDK no seu computador. **Copie este valor**.

Selecione pelo menos um SDK na lista. Ex.: Android 14.0.

Mude para a aba *SDK Tools* e certifique-se de que as seguintes ferramentas estejam selecionadas lá:

* Android SDK Build-tools
* Android SDK Platform-Tools
* Android Emulator

Obs.: Recomendado sempre usar as versões mais recentes das ferramentas.


Abra seu arquivo .zprofile em um editor, se preferir digite no terminal:
```bash
open ~/.zprofile
```

Adicione as duas linhas a seguir substituindo o valor variável *ANDROID_HOME* pelo que foi copiado anteriormente:
 ```bash
export ANDROID_HOME="copied-Android-SDK-location"
export PATH=$PATH:$ANDROID_HOME/tools:$ANDROID_HOME/platform-tools
export PATH=$PATH:$ANDROID_HOME/cmdline-tools/latest/bin
```
Salve as alterações no arquivo .zprofile.
Abra um terminal e execute para aplicar as mudanças:
 ```bash
source ~/.zprofile
```
Reinicie o Terminal para obter as novas configurações .zprofile.

\- ou -

**Caso não queria abrir e editar o arquivo manualmente, é possível adicionar as linhas ao arquivo conforme exemplo**:
 ```bash
echo export "ANDROID_HOME=~/Library/Android/sdk" >> ~/.zprofile
echo export "PATH=\$PATH:\$ANDROID_HOME/tools:\$ANDROID_HOME/tools/bin:\$ANDROID_HOME/platform-tools" >> ~/.zprofile
echo export "PATH=\$PATH:\$ANDROID_HOME/cmdline-tools/latest/bin" >> ~/.zprofile
```
O **parâmetro ~** recupera o caminho da pasta do usuário.

Reexecute a etapa *4 - Verificar ambiente* para certificar que tudo está ok e faça os ajustes se necessário.

```bash
appium-doctor --dev
```

Estando a saída do Appium Doctor sem apontar erros, você acabou de configurar o ambiente.

#### 6 - Instalação e configurações adicionais
Baixe o pacote de instalação no seguinte site:

Appium inspector
https://github.com/appium/appium-inspector/releases

https://appium.io/docs/en/2.3/ecosystem/drivers/
https://appium.io/docs/en/latest/guides/caps/


## Rodando localmente

Clone o projeto

```bash
  git clone https://github.com/LeandroHPerez/-EnterpriseLevelTestAutomationFrameworkWithAppium2
```

Entre no diretório do projeto

```bash
  cd m-EnterpriseLevelTestAutomationFrameworkWithAppium2
```

Verifique a seção de Instalação e informações adicionais no arquivo README.md

```bash
  README.md
```


## Referência
- [jUnit](https://junit.org/junit5/)
- [Cucumber](https://cucumber.io/)
- [Appium](https://appium.io/)
- [Selenium](https://www.selenium.dev/)
- [Rest-assured](https://rest-assured.io/)


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

