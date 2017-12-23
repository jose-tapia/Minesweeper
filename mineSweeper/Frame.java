/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author JoseManuelTapiaAvitia
 */
package mineSweeper;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Frame extends JFrame implements KeyListener, MouseListener{
    private int Width=400, Height=400;
    
    private Game game;
    private int difficult,left,top;
    private Screen screen;
    private Font font;
    
    /**
     *
     */
    public Frame(){
        super("MineSweeper!");
        
        game=new Game();
        setResizable(false);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        addMouseListener(this);
        addKeyListener(this);
        screen=new Screen();
        add(screen);
        pack();
        left=getInsets().left;
        top=getInsets().top;
        setSize(Width+left,Height+top+30);
        setLocationRelativeTo(null);
        setVisible(true);

        font=new Font("SansSerif",0,12);
    
    }
    
    @Override
    public void mouseClicked(MouseEvent e){}
    @Override
    public void mouseEntered(MouseEvent e){}
    @Override
    public void mouseExited(MouseEvent e){}
    @Override
    public void mousePressed(MouseEvent e){
        game.checkPressed(e.getX()-left,e.getY()-top);
        screen.repaint();
        
    }
    @Override
    public void mouseReleased(MouseEvent e){
        game.cleanPressed();
        if(e.getButton()==1)game.clickLeft(e.getX()-left,e.getY()-top);
        if(e.getButton()==3)game.clickRight(e.getX()-left,e.getY()-top);
        screen.repaint();
    }
    
    @Override
    public void keyPressed(KeyEvent e){}
    @Override
    public void keyTyped(KeyEvent e){}
    @Override
    public void keyReleased(KeyEvent e){
        if(e.getKeyCode()==KeyEvent.VK_R){
            game.reset();
            screen.repaint();
        }
        
    }
    
    
    public class Screen extends JPanel{
        @Override
        public void paintComponent(Graphics g){
            g.setFont(font);
            game.draw(g);
        }
    }
}
