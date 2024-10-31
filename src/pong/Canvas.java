package pong;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

class Canvas extends JPanel implements Runnable, KeyListener {

    BufferedImage imagemVidas;
    BufferedImage bola;
    int posBolaX = 0;
    int posBolaY = 0;
    int posPlayerX = 300;
    int posPlayerY = 533;
    int direcaoPosBolaX = 3;
    int direcaoPosBolaY = 3;
    int velocidadeBola = 2;
    int velocidadePlayer = 5;
    int tamBola = 25;
    int tamPlayerY = 25;
    int tamPlayerX = 100;
    boolean esquerda = false;
    boolean direita = false;
    int playerVidaInicial = 5;
    int playerVidas = playerVidaInicial;
    int pontuacao = 0;

    public Canvas() {
        setFocusable(true); // Garante que o JPanel pode capturar eventos de teclado
        addKeyListener(this); // Adiciona KeyListener ao JPanel

        Thread gameLoop = new Thread(this);
        gameLoop.start();

        try {
            imagemVidas = ImageIO.read(new File("src//pong//vidas.png"));
            bola = ImageIO.read(new File("src//pong//bola.gif"));
        } catch (IOException ex) {
            System.out.println("Erro ao carregar as imagens!");
        }
    }

    @Override
    public void run() {
        while (true) {
            atualizar();
            repaint();
            dorme();
        }
    }

    public void atualizar() {
        //velocidadeBola = 2 + pontuacao / 10;
        //velocidadePlayer = 10 + pontuacao / 10;

        // Movimenta a bola
        posBolaX += velocidadeBola * direcaoPosBolaX;
        posBolaY += velocidadeBola * direcaoPosBolaY;

        // Verifica colisão com as bordas
        if (posBolaX >= (700 - tamBola) || posBolaX <= 0) {
            direcaoPosBolaX *= -1;
        }

        if (posBolaY <= 0) {
            direcaoPosBolaY *= -1;
        }

        if (posBolaY >= 600) {
            posBolaY = 0;
            playerVidas--;

            if (playerVidas == 0) {
                int resposta = JOptionPane.showConfirmDialog(this, "Você perdeu! Quer jogar novamente?");
                if (resposta == JOptionPane.OK_OPTION) {
                    playerVidas = playerVidaInicial;
                    pontuacao = 0;
                } else {
                    System.exit(0);
                }
            }
        }

        // Verifica colisão com o jogador
        if (posBolaX >= posPlayerX && posBolaX <= posPlayerX + tamPlayerX) {
            if (posBolaY + tamBola >= posPlayerY && posBolaY <= posPlayerY + tamPlayerY) {
                direcaoPosBolaY *= -1;
                pontuacao++;
                if (pontuacao % 10 == 0) { // Por exemplo, a cada 10 pontos, aumenta uma vida
                    playerVidas++;
                }
                if (pontuacao >= 50) { // Condição de vitória
                    int resposta = JOptionPane.showConfirmDialog(this, "Você ganhou! Quer jogar novamente?");
                    if (resposta == JOptionPane.OK_OPTION) {
                        playerVidas = playerVidaInicial;
                        pontuacao = 0;
                    } else {
                        System.exit(0);
                    }
                }
            }
        }

        // Movimenta o jogador
        if (esquerda && posPlayerX > 0) {
            posPlayerX -= velocidadePlayer;
        } else if (direita && posPlayerX + tamPlayerX < 700) {
            posPlayerX += velocidadePlayer;
        }
    }

    @Override
    protected void paintComponent(Graphics g2) {
        super.paintComponent(g2);

        Graphics2D g = (Graphics2D) g2.create();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        // Fundo
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, 700, 600);

        // Bola
        g.setColor(Color.YELLOW);
        g.fillOval(posBolaX, posBolaY, tamBola, tamBola);

        // Player
        g.setColor(Color.BLUE);
        g.fillRoundRect(posPlayerX, posPlayerY, tamPlayerX, tamPlayerY, 15, 15);

        // Pontuação
        g.setColor(Color.WHITE);
        g.drawString("Pontuação: " + pontuacao, 600, 20);

        // Vidas
        for (int i = 0; i < playerVidas; i++) {
            g.drawImage(imagemVidas, 5 + (i * 30), 20, 30, 30, null);
        }
    }

    private void dorme() {
        try {
            Thread.sleep(16); // Cerca de 60 frames por segundo
        } catch (InterruptedException ex) {
            System.out.println("Erro no loop de jogo!");
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            esquerda = true;
        } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            direita = true;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            esquerda = false;
        } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            direita = false;
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // Método não utilizado
    }
}

