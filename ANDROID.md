# Configurando o projeto Android da LibGDX

Assim que você fizer um _pull request_ e receber o projeto Android,
você vai precisar ter o Android SDK instalado e devidamente configurado.

1. Para que o NetBeans volte a ficar feliz e abra o projeto, você precisa
   do Andoird SDK. Baixe o [Android SDK][sdk] e siga as instruções no site.
   Você pode baixar:
   - O Android Studio (IDE + SDK); ou
   - Apenas o SDK
     - Eu descompactei o SDK para a pasta `/home/fegemo/Android/Sdk`
1. Agora você precisa falar para o Gradle onde está o Android SDK. Crie um
   arquivo `local.properties` dentro da raiz do projeto (ie, a pasta
   que contém as `android`, `core` e `desktop`)
   - Nesse arquivo, coloque
     `sdk.dir=LOCAL_ONDE_INSTALOU_OU_DESCOMPACTOU_ANDROID_SDK`
     - No meu Ubuntu ficou assim: `sdk.dir=/home/fegemo/Android/Sdk`
1. O Android SDK precisa que você aceite algumas licensas antes de usá-lo.
   Na pasta onde você colocou seu SDK existe o utilitário
   `tools/bin/sdkmanager` (no meu computador:
   `/home/fegemo/Android/Sdk/tools/bin/sdkmanager`).
   - Nessa pasta, no terminal, execute `./sdkmanager --licenses` e
     "aceite" (digite `y`) todas as licensas
1. Agora você está apto a executar o projeto novamente. Basta pedir o NetBeans
   para recarregar o projeto (ou dar um _Clean and Build_)
   - Mesmo que o projeto Android fique dizendo que está com erros, é possível
     compilar/executar normalmente
1. Para executar o jogo em um celular Android, conecte o aparelho na porta USB
   em modo de depuração (Google por Android Developer Mode pra saber como) e,
   então, clicar com o botão direito no projeto `android`, _Tasks_, _Install_,
   _installDebug_ - depois de um tempo, o jogo vai aparecer na lista de
   aplicativos instalados no celular


[sdk]: http://developer.android.com/sdk/index.html
