/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mineSweeper;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.*;
import java.util.Random;
public class Game {
    
    private Random random;
    private Mine[][] gridGame;
    private static BufferedImage sprites;
    private static BufferedImage normal;
    private static BufferedImage bombImg;
    private static BufferedImage flagImg;
    private static BufferedImage opened;
    private static BufferedImage[] numbers;
    private static BufferedImage HappyFace;
    private static BufferedImage HappyPush;
    private static BufferedImage Surprise;
    private static BufferedImage DeadFace;
    private static BufferedImage CoolFace;
    private int Width, Height,OpenFlag,LeftM,TOP=31,startFace;
    private boolean finish,dead;
    public boolean pressed,pressedSurp;
    private TextField tfLeft,tfOpenFlag;
    
    
    public Game(){
        sprites  =      InputImage.loadImage("sprites/Sprites.png");
        normal   =      InputImage.scale(InputImage.crop(sprites,new Rectangle(80,16,16,16)),20,20);
        opened   =      InputImage.scale(InputImage.crop(sprites,new Rectangle(0,0,16,16)),20,20);
        numbers=new BufferedImage[9];
        for(int i=1;i<=8;i++)
            numbers[i]= InputImage.scale(InputImage.crop(sprites,new Rectangle(i*16,0,16,16)),20,20);
        bombImg  =      InputImage.scale(InputImage.crop(sprites,new Rectangle(0,16,16,16)),20,20);
        flagImg  =      InputImage.scale(InputImage.crop(sprites,new Rectangle(64,16,16,16)),20,20);
        HappyFace=      InputImage.scale(InputImage.crop(sprites,new Rectangle(27,56,25,25)), 25,25);
        HappyPush=      InputImage.scale(InputImage.crop(sprites,new Rectangle(1,56,25,25)),25,25);
        Surprise =      InputImage.scale(InputImage.crop(sprites,new Rectangle(53,56,25,25)),25,25);
        DeadFace =      InputImage.scale(InputImage.crop(sprites,new Rectangle(79,56,25,25)),25,25);
        CoolFace =      InputImage.scale(InputImage.crop(sprites,new Rectangle(105,56,25,25)),25,25);
        Gamei(20,20);
    }
    public void Gamei(int Width,int Height){
        this.Width=Width;
        this.Height=Height;
        startFace=((Width*20)-25)/2;
        pressed=false;
        gridGame= new Mine[Width][Height];
        for(int x=0;x<Width;x++)for(int y=0;y<Height;y++)
            gridGame[x][y]=new Mine(x,y,normal,opened,flagImg,bombImg,numbers);
        reset();
    }
    private void setBombs(int k){
        int x,y,i;
        for(i=0;i<k;i++){
            do{
                x=random.nextInt();
                if(x<0)x=-x;
                x%=Width;
                y=random.nextInt();
                if(y<0)y=-y;
                y%=Height;
            }while(gridGame[x][y].isBomb());
            gridGame[x][y].setBomb(true);
        }
    }
    private void setAround(){
        int[][] moves={ {-1,-1},{-1,0},{-1,1},{0,-1},{0,1},{1,-1},{1,0},{1,1} };
        int cnt,x,y,p,q,i;
        for(x=0;x<Width;x++)for(y=0;y<Height;y++){
            cnt=0;
            for(i=0;i<8;i++){
                p=x+moves[i][0];
                q=y+moves[i][1];
                if(p<0||q<0||p==Width||q==Height)continue;
                if(gridGame[p][q].isBomb())cnt++;
            }
            gridGame[x][y].setAround(cnt);
        }
    }
    public void reset(){
        int x,y;
        random=new Random();
        OpenFlag=0;
        LeftM=Width*Height;
        cleanPressed();
        dead=false;
        finish=false;
        for(x=0;x<Width;x++)for(y=0;y<Height;y++)
            gridGame[x][y].reset();
        setBombs(Width+Height);
        setAround();
    }
    private boolean checkFinish(){
        int x,y;
        for(x=0;x<Width;x++)for(y=0;y<Height;y++)
            if(!gridGame[x][y].isOpen()&&!gridGame[x][y].isBomb())return false;
        finish=true;
        for(x=0;x<Width;x++)for(y=0;y<Height;y++)
            if(gridGame[x][y].isBomb()&&!gridGame[x][y].isFlag())gridGame[x][y].changeFlag();
        return true;
    }
    public void clickLeft(int x,int y){
        if(y<TOP){
            if(3<=y&&y<=28&&startFace<=x&&x<=startFace+25)reset();
        }
        else open((int)x/20,(int)(y-TOP)/20);
        checkFinish();
    }
    public void clickRight(int x,int y){
        if(y>=TOP)gridGame[(int)x/20][(int)(y-TOP)/20].changeFlag();
    }
    private void openExpansive(int x,int y){
        if(gridGame[x][y].isOpen()||gridGame[x][y].isFlag())return;
        gridGame[x][y].openMine();
        if(gridGame[x][y].howManyAround()==0){
            int[][] moves={ {-1,-1},{-1,0},{-1,1},{0,-1},{0,1},{1,-1},{1,0},{1,1} };
            int p,q,i;
            for(i=0;i<8;i++){
                p=x+moves[i][0];
                q=y+moves[i][1];
                if(p<0||q<0||p==Width||q==Height)continue;
                openExpansive(p,q);
            }
        }
    }
    private void killGame(){
        dead=true;
        for(int x=0;x<Width;x++)for(int y=0;y<Height;y++)gridGame[x][y].openMine();
    }
    public void open(int x,int y){
        if(gridGame[x][y].isFlag())return;
        if(gridGame[x][y].isOpen()){
            int[][] moves={ {-1,-1},{-1,0},{-1,1},{0,-1},{0,1},{1,-1},{1,0},{1,1} };
            int cnt=0,p,q,i;
            for(i=0;i<8;i++){
                p=x+moves[i][0];
                q=y+moves[i][1];
                if(p<0||q<0||p==Width||q==Height)continue;
                cnt+=(gridGame[p][q].isFlag()?1:0);
            }
            if(cnt==gridGame[x][y].howManyAround()){
                for(i=0;i<8;i++){
                    p=x+moves[i][0];
                    q=y+moves[i][1];
                    if(p<0||q<0||p==Width||q==Height)continue;
                    if(gridGame[p][q].isBomb()&&!gridGame[p][q].isFlag())killGame();
                    if(!gridGame[p][q].isFlag()){
                        openExpansive(p,q);
                    }
                }
            }
            return;
        }
        if(gridGame[x][y].isBomb())
            killGame();
        else openExpansive(x,y);
    }
    
    public void draw(Graphics g){
        int x,y;
        g.setColor(Color.BLACK);
        g.drawString("Left mines: "+LeftM,0,0);
        
        
        
        if(pressed)g.drawImage(HappyPush,startFace,3,null);
        else {
            if(dead)g.drawImage(DeadFace,startFace,3,null);
            else {
                if(finish)g.drawImage(CoolFace,startFace,3,null);
                else {
                    if(pressedSurp)g.drawImage(Surprise,startFace,3,null);
                    else g.drawImage(HappyFace,startFace,3,null);
                }
            }
        }    
        for(x=0;x<Width;x++)for(y=0;y<Height;y++){
            gridGame[x][y].draw(g);
        }
        if(dead){
            g.setColor(Color.RED);
            g.drawString("You're dead man :(, if can you retry again, press R or in the dead face", 10, 12);
        }
        else if(finish){
            g.setColor(Color.BLUE);
            g.drawString("Congratulation!! :) You did it!, if you can play again, press R or in the cool face",10,12);
        }
        
    }   
    public void cleanPressed(){
        pressed=false;
        pressedSurp=false;
        for(int x=0;x<Width;x++)for(int y=0;y<Height;y++)
            gridGame[x][y].pressed=false;
    }
    public void checkPressed(int x,int y){
        if(3<=y&&y<=28&&startFace<=x&&x<=startFace+25){
            pressed=true;
            pressedSurp=false;
            return;
        }
        pressed=false;
        pressedSurp=false;
        if(y>=TOP){
            pressedSurp=true;
            gridGame[(int)x/20][(int)(y-TOP)/20].pressed=true;
        }
    }
    
}
