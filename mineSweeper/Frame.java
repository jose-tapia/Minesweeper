package mineSweeper;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Frame extends JFrame implements KeyListener, MouseListener,MouseMotionListener{
    private int Width=20, Height=20;
    
    private Game game;
    private int left,top;
    private Screen screen;
    private Font font;
    private int xmouse,ymouse,fin;
    private int Key_Flag=KeyEvent.VK_F,Key_Click=KeyEvent.VK_D,Key_Reset=KeyEvent.VK_R;
    private boolean messagePrint;
    
    public Frame(){
        super("MineSweeper!");
        
        game=new Game();
        setResizable(false);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        addMouseListener(this);
        addKeyListener(this);
        addMouseMotionListener(this);
        screen=new Screen();
        add(screen);
        pack();
        left=getInsets().left;
        top=getInsets().top;
        setSize(Width+left,Height+top+30);
        setLocationRelativeTo(null);
        setVisible(true);
        messagePrint=false;
        font=new Font("SansSerif",0,12);
    
    }

    public void setVis(boolean visible){
        setVisible(visible);
    }
    public void setSz(int Width,int Height,int Bomb){
        this.Width=Width;
        this.Height=Height;
        setSize(Width*20+left,Height*20+top+30);
        game.Gamei(Width, Height,Bomb);
    }
    public void setKeys(int Flag,int Click,int Reset){
        Key_Flag=Flag;
        Key_Click=Click;
        Key_Reset=Reset;
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
        if(e.getButton()==1){
            if(game.clickLeft(e.getX()-left,e.getY()-top)){
                fin=0;
                if(game.dead)fin=1;
                else if(game.finish)fin=2;
                if(fin==0)messagePrint=false;
            }
        }
        if(e.getButton()==3)game.clickRight(e.getX()-left,e.getY()-top);
        screen.repaint();
        if(fin>0&&!messagePrint){
            messagePrint=true;
            String word="Congratulations! You won! :)";
            if(fin==1)word="You're dead man dud! :(! Try again pressing 'R'!";
            JOptionPane.showMessageDialog(this,word,"MineSweeper!",JOptionPane.PLAIN_MESSAGE);
        }
    }
    
    @Override
    public void keyPressed(KeyEvent e){
        game.checkPressed(xmouse,ymouse);
        screen.repaint();        
    }
    @Override
    public void keyTyped(KeyEvent e){}
    @Override
    public void keyReleased(KeyEvent e){
        game.cleanPressed();
        screen.repaint();

        if(e.getKeyCode()==Key_Reset){
            game.reset();
            fin=0;
            screen.repaint();
            messagePrint=false;
        }
        if(e.getKeyCode()==Key_Flag){
            game.clickRight(xmouse, ymouse);
            screen.repaint();
        }
        if(e.getKeyCode()==Key_Click){
            if(game.clickLeft(xmouse,ymouse)){
                fin=0;
                if(game.dead)fin=1;
                else if(game.finish)fin=2;
                if(fin==0)messagePrint=false;
            }
            screen.repaint();
            if(fin>0&&!messagePrint){
                messagePrint=true;
                String word="Congratulations! You won! :)";
                if(fin==1)word="You're dead man dude! :(! Try again pressing 'R'!";
                JOptionPane.showMessageDialog(this,word,"MineSweeper!",JOptionPane.PLAIN_MESSAGE);
            }
        }
    }
    
    @Override
    public void mouseDragged(MouseEvent e){
        xmouse=e.getX()-left;
        ymouse=e.getY()-top;
    }
    @Override
    public void mouseMoved(MouseEvent e){
        xmouse=e.getX()-left;
        ymouse=e.getY()-top;
    }  
    
    public class Screen extends JPanel{
        @Override
        public void paintComponent(Graphics g){
            g.setFont(font);
            game.draw(g);
        }
    }
}
