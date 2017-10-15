### Tic Cat Dog

#### Inspiração

Com inspiração em jogo da velha (tic-tac-toe), criou-se o tic-cat-dog, um minigame em que cachorros e gatos podem se divertir e competir entre si. 

![Minigame 'Tic Cat Dog'](docs/ticcatdog-gameplay.png)

#### Qualidade SuperPremium

O jogador, representado pelo cachorro, disputa contra um _bot_, representado pelo gato. Nos movimentos do gato, fez-se uso de **inteligência artificial** através do uso do algoritmo minimax, ou seja, o gato opta pelo movimento em que minimiza as chances do cachorro de obter pontos ao passo que maximiza as suas.

#### Condições de vitória e derrota

Neste jogo, o jogador irá perder se:

1. O tempo acabar e o jogo da velha não tiver sido concluído;
2. O gato ganhar o jogo. 

Caso contrário, o jogador será considerado ganhador.

#### Nível de dificuldade

O nível de dificuldade do jogo foi traduzido como o primeiro a realizar o movimento. No nível fácil, quem dá o primeiro movimento é o jogador, ao passo que, no nível difícil, é o gato. 

#### Emissão de sons

Ao fim da partida se o ganhador for o jogador, será emitido um latido de cachorro, porém se o gato for o ganhador será emitido um gato miando.  

#### Independência de resolução

A imagem de _background_ foi desenhada na origem (0,0) com comprimento "viewport.getWorldWidth()" e altura "viewport.getWorldHeight()". Os quadrados do jogo da velha também foram desenhados em função destas variáveis: o comprimento e a altura de cada quadrado foram definidos como sendo "viewport.getWorldWidth()/5" e "viewport.getWorldHeight()/5", respectivamente.

#### Referências

##### Sons

* [Cachorro latindo](https://www.youtube.com/watch?v=iuy-oOJCOoM)

* [Gato miando](https://www.youtube.com/watch?v=o8aTnc8qVY0&t=2s)

##### Imagens

* [Imagem do cachorro que compôs o background](http://scromy.com/black-and-white-dog-wallpapers-wide/black-and-white-dog-wallpapers-wide-with-high-definition-wallpaper/)

* [Imagem do gato que compôs o background](https://w-dog.net/wallpaper/cat-silhouette-cat-black-background-background-black-fon/id/259288/)

* [Imagem do cachorro de óculos utilizada para identificar um movimento](http://www.airpets.com/export-your-pet-from-uk)

* [Imagem do gato rasgando folha utilizada para identificar um movimento](https://www.petful.com/pet-health/common-toxins-cat/)

##### Algoritmo

* [Algoritmo Minimax](http://neverstopbuilding.com/minimax)

