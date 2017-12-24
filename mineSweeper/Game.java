package mineSweeper;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.*;
import java.util.Random;

public class Game {
    
    private Random random;
    private Mine[][] gridGame;
    private static BufferedImage sprites;
    private static BufferedImage normal,opened,flagImg;
    private static BufferedImage bombImg,fakeBomb,bigBomb;
    private static BufferedImage[] numbers;
    private static BufferedImage HappyFace,HappyPush,Surprise,DeadFace,CoolFace;

    private final int TOP=31;
    private int Width, Height,leftFlags,startFace,Bomb;
    private boolean firstClick;
    public boolean pressed,pressedSurp,finish,dead;
    private TextField tfLeftFlags;
    private final int[][] moves={ {-1,-1},{-1,0},{-1,1},{0,-1},{0,1},{1,-1},{1,0},{1,1} };
    
    public Game(){
        sprites  =      InputImage.loadImage("sprites/Sprites.png");//Read all sprites
        normal   =      InputImage.scale(InputImage.crop(sprites,new Rectangle(80,16,16,16)),20,20);
        opened   =      InputImage.scale(InputImage.crop(sprites,new Rectangle(0,0,16,16)),20,20);
        numbers=new BufferedImage[9];
        for(int i=1;i<=8;i++)
            numbers[i]= InputImage.scale(InputImage.crop(sprites,new Rectangle(i*16,0,16,16)),20,20);
        bombImg  =      InputImage.scale(InputImage.crop(sprites,new Rectangle(0,16,16,16)),20,20);
        fakeBomb  =      InputImage.scale(InputImage.crop(sprites,new Rectangle(16,16,16,16)),20,20);
        bigBomb  =      InputImage.scale(InputImage.crop(sprites,new Rectangle(32,16,16,16)),20,20);
        flagImg  =      InputImage.scale(InputImage.crop(sprites,new Rectangle(64,16,16,16)),20,20);
        HappyFace=      InputImage.scale(InputImage.crop(sprites,new Rectangle(27,56,25,25)), 25,25);
        HappyPush=      InputImage.scale(InputImage.crop(sprites,new Rectangle(1,56,25,25)),25,25);
        Surprise =      InputImage.scale(InputImage.crop(sprites,new Rectangle(53,56,25,25)),25,25);
        DeadFace =      InputImage.scale(InputImage.crop(sprites,new Rectangle(79,56,25,25)),25,25);
        CoolFace =      InputImage.scale(InputImage.crop(sprites,new Rectangle(105,56,25,25)),25,25);
        
        Gamei(10,10,10);
    }   
    public void Gamei(int Width,int Height,int Bomb){
        this.Width=Width;
        this.Height=Height;
        this.Bomb=Bomb;
        startFace=((Width*20)-25)/2;
        pressed=false;
        gridGame= new Mine[Width][Height];
        for(int x=0;x<Width;x++)for(int y=0;y<Height;y++)
            gridGame[x][y]=new Mine(x,y,normal,opened,flagImg,bombImg,numbers,fakeBomb,bigBomb);
        reset();
    }
    private void setBombs(int k){//Place k bombs in the grid
        int x,y,i;
        if(k>Width*Height)k=Width*Height;
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
    private void setAround(){//Update the grid of numbers of bombs around each cell
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
    public void reset(){//Reset the grid and all variables
        int x,y;
        random=new Random();
        leftFlags=Bomb;
        cleanPressed();
        firstClick=true;
        dead=false;
        finish=false;
        for(x=0;x<Width;x++)for(y=0;y<Height;y++)
            gridGame[x][y].reset();       
        setBombs(Bomb);
        setAround();
    }
    private void resetI(){//Reset the grid and all variables
        int x,y;
        boolean flag;
        random=new Random();
        cleanPressed();
        for(x=0;x<Width;x++)for(y=0;y<Height;y++){
            flag=gridGame[x][y].isFlag();
            gridGame[x][y].reset();      
            gridGame[x][y].setFlag(flag);
        }
        setBombs(Bomb);
        setAround();
    }
    private void finishGame(){
        for(int x=0;x<Width;x++)for(int y=0;y<Height;y++)
            gridGame[x][y].setFinish(true);
        
    }
    private boolean checkFinish(){//Check if the game is already finish
        int x,y;
        for(x=0;x<Width;x++)for(y=0;y<Height;y++)
            if(!gridGame[x][y].isOpen()&&!gridGame[x][y].isBomb())return false;
        finish=true;
        finishGame();
        if(dead)return true;
        for(x=0;x<Width;x++)for(y=0;y<Height;y++)
            if(gridGame[x][y].isBomb()&&!gridGame[x][y].isFlag()){
                gridGame[x][y].changeFlag();
                leftFlags--;
            }
        return true;
    }
    public boolean clickLeft(int x,int y){//Open a cell
        if(x<0||y<0)return false;
        if(y<TOP){
            if(3<=y&&y<=28&&startFace<=x&&x<=startFace+25)reset();
            return true;
        }
        else {
            int xi=(int)x/20,yi=(int)(y-TOP)/20;
            if(xi<0||yi<0||xi>=Width||yi>=Height)return false;
            if(firstClick){
                if(Width*Height-Bomb>9){
                    while(gridGame[xi][yi].howManyAround()!=0||gridGame[xi][yi].isBomb())
                        resetI();
                }
                else {
                    if(Width*Height>Bomb)
                        while(gridGame[xi][yi].isBomb())resetI();
                }
                firstClick=false;
            }
            open(xi,yi);
        }
        checkFinish();
        if(dead||finish)return true;
        else return false;
    }
    public void clickRight(int x,int y){//Place a flag
        if(x<0||y<0)return;
        if(y>=TOP){
            int xi=(int)x/20,yi=(int)(y-TOP)/20;
            if(xi<0||yi<0||xi>=Width||yi>=Height)return;
            if(gridGame[xi][yi].isFlag())leftFlags++;
            else if(!gridGame[xi][yi].isOpen())leftFlags--;
            gridGame[xi][yi].changeFlag();
        }
    }
    private void openExpansive(int x,int y){//Open a cell where is a zero
        if(gridGame[x][y].isOpen()||gridGame[x][y].isFlag())return;
        gridGame[x][y].openMine();
        if(gridGame[x][y].howManyAround()==0){
            int p,q,i;
            for(i=0;i<8;i++){
                p=x+moves[i][0];
                q=y+moves[i][1];
                if(p<0||q<0||p==Width||q==Height)continue;
                openExpansive(p,q);
            }
        }
    }
    private void killGame(){//Open all grid
        dead=true;
        for(int x=0;x<Width;x++)for(int y=0;y<Height;y++)
            if(gridGame[x][y].isBomb()&&!gridGame[x][y].isFlag())gridGame[x][y].openMine();
        finishGame();
    }
    private void open(int x,int y){//open a cell
        if(gridGame[x][y].isFinish())return;
        if(gridGame[x][y].isFlag())return;
        if(gridGame[x][y].isOpen()){
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
        if(gridGame[x][y].isBomb()){
            gridGame[x][y].setTrouble(true);
            killGame();
        }
        else openExpansive(x,y);
    }
    
    public void draw(Graphics g){//draw the grid
        int x,y;
        g.setColor(Color.BLACK);
        g.drawString("Left flags: "+leftFlags,10,20);
        BufferedImage face;
        
        
        if(pressed)face=HappyPush;
        else {
            if(dead)face=DeadFace;
            else {
                if(finish)face=CoolFace;
                else {
                    if(pressedSurp)face=Surprise;
                    else face=HappyFace;
                }
            }
        }    
        g.drawImage(face,startFace,3,null);
        for(x=0;x<Width;x++)for(y=0;y<Height;y++)
            gridGame[x][y].draw(g);    
    }   
    public void cleanPressed(){//Clean the pressed variables
        pressed=false;
        pressedSurp=false;
        for(int x=0;x<Width;x++)for(int y=0;y<Height;y++)
            gridGame[x][y].pressed=false;
    }
    public void checkPressed(int x,int y){//Pressed mouse
        if(3<=y&&y<=28&&startFace<=x&&x<=startFace+25){
            pressed=true;
            pressedSurp=false;
            return;
        }
        pressed=false;
        pressedSurp=false;
        if(x<0||y<0)return;
        if(y>=TOP){
            pressedSurp=true;
            int xi=(int)x/20,yi=(int)(y-TOP)/20;
            if(xi<0||yi<0||xi>=Width||yi>=Height)return;
            gridGame[xi][yi].pressed=true;
        }
    }
    
}
