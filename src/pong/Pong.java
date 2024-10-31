
package pong;

import javax.swing.JFrame;

public class Pong {

    public static void main(String[] args) {
        
        
        JFrame janela = new JFrame("Game Pong");
        janela.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        janela.setSize(700, 600);
        janela.setLocationRelativeTo(null);
        janela.setResizable(false);
        //janela.setResizable(false);
        
        
        
        Canvas canvas = new Canvas();
        janela.add(canvas);
        
        janela.addKeyListener(canvas);
        
        janela.setVisible(true);
        
    }
    
}
