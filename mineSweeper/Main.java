package mineSweeper;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Main extends Frame {
    private int Width,Height,Key_Click,Key_Flag,Key_Reset,Bomb;
    private Button easy,medium,hard,custom,config;
    
    public Main(){
       setLayout(new FlowLayout());
       setTitle("MineSweeper!");
       setSize(200,155);
       setVisible(true);
       
       easy=new Button("Easy");
       medium=new Button("Medium");
       hard=new Button("Hard");
       custom=new Button("Custom");
       config=new Button("Configure keys");
       add(new Label("Choice one difficult!"));
       add(easy);
       add(medium);
       add(hard);
       add(custom);
       add(config);
       easy.addActionListener(new ActionListener(){
          @Override
          public void actionPerformed(ActionEvent evt){
              Width=10;
              Height=10;
              Bomb=20;
              playGame();
          }
       });
       medium.addActionListener(new ActionListener(){
          @Override
          public void actionPerformed(ActionEvent evt){
              Width=15;
              Height=15;
              Bomb=30;
              playGame();
          }
       });
       hard.addActionListener(new ActionListener(){
          @Override
          public void actionPerformed(ActionEvent evt){
              Width=20;
              Height=20;
              Bomb=40;
              playGame();
          }
       });
       custom.addActionListener(new ActionListener(){
          @Override
          public void actionPerformed(ActionEvent evt){
              new CustomFrame();
          }
       });
       config.addActionListener(new ActionListener(){
          @Override
          public void actionPerformed(ActionEvent evt){
              new ConfigFrame();
          }
       });
       setDefaultCloseOperation(DISPOSE_ON_CLOSE);
       Key_Flag=KeyEvent.VK_F;
       Key_Click=KeyEvent.VK_D;
       Key_Reset=KeyEvent.VK_R;
    }
    private void playGame(){
       Frame game=new Frame();
       game.setVis(true);
       game.setSz(Width,Height,Bomb);
       game.setKeys(Key_Flag,Key_Click,Key_Reset);
    }
    public static void main(String[] args){
        new Main();
    }
    
    public class CustomFrame extends Frame{
        TextField tfWidth,tfHeight,tfBomb;
        public CustomFrame(){
            setLayout(new FlowLayout());
            setTitle("Create your own game!");
            setSize(250,120);
            setVisible(true);
            add(new Label("Width: "));
            tfWidth=new TextField(5);
            add(tfWidth);
            add(new Label("Height: "));
            tfHeight=new TextField(5);
            add(tfHeight);
            add(new Label("Amount of Bombs: "));
            tfBomb=new TextField(5);
            add(tfBomb);
            
            Button ok=new Button("Play!");
            ok.addActionListener(new ActionListener(){
                @Override
                public void actionPerformed(ActionEvent evt){
                    Width=Integer.parseInt(tfWidth.getText());
                    Height=Integer.parseInt(tfHeight.getText());
                    Bomb=Integer.parseInt(tfBomb.getText());
                    playGame();
                    setVisible(false);
                    dispose();
                }
            });
            add(ok);          
        }
    }
    public class ConfigFrame extends Frame{
        TextField tfClick,tfFlag,tfReset;
        public ConfigFrame(){
            setLayout(new FlowLayout());
            setTitle("Configure the Key's");
            setSize(275,140);
            setVisible(true);
            add(new Label("Press the Key for 'Click'"));
            tfClick=new TextField("D",5);
            add(tfClick);
            add(new Label("Press the Key for 'Flags'"));
            tfFlag=new TextField("F",5);
            add(tfFlag);
            add(new Label("Press the Key for 'Reset'"));
            tfReset=new TextField("R",5);
            add(tfReset);
            Button cancel=new Button("Cancel");
            cancel.addActionListener(new ActionListener(){
                @Override
                public void actionPerformed(ActionEvent evt){
                    setVisible(false);
                    dispose();
                }
            });
            add(cancel);
            Button save=new Button("Save");
            save.addActionListener(new ActionListener(){
                @Override
                public void actionPerformed(ActionEvent evt){
                    if(tfClick.getText().length()==1)
                    Key_Click=(int)(Character.toUpperCase(tfClick.getText().charAt(0)));
                    if(tfFlag.getText().length()==1)
                    Key_Flag=(int)(Character.toUpperCase(tfFlag.getText().charAt(0)));
                    if(tfReset.getText().length()==1)
                    Key_Reset=(int)(Character.toUpperCase(tfReset.getText().charAt(0)));
                    System.out.println(Key_Click+" "+Key_Flag+" "+Key_Reset);
                    setVisible(false);
                    dispose();
                }
            });
            add(save);
        }
    }
}
