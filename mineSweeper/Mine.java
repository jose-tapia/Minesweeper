package mineSweeper;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class Mine {
    private BufferedImage normal,opened,flagImg;
    private BufferedImage bombImg,fakeBomb,bigBomb;
    private BufferedImage[] numbers;
    private final int TOP=31;;
    private int x,y,bombsAround,width=20,height=20;
    private boolean flag,bomb,open,finish,problemBomb;
    public boolean pressed;
    

    public Mine(int x,int y,BufferedImage normal,BufferedImage opened,BufferedImage flagImg,BufferedImage bombImg,BufferedImage[] numbers,BufferedImage fakeBomb, BufferedImage bigBomb){
        this.x=x;
        this.y=y;
        this.normal=normal;
        this.opened=opened;
        this.flagImg=flagImg;
        this.bombImg=bombImg;
        this.numbers=numbers;
        this.fakeBomb=fakeBomb;
        this.bigBomb=bigBomb;
    }
    
    
    
    public void setSize(int width,int height){this.width=width;this.height=height;}
    public void setBomb(boolean bomb){this.bomb=bomb;}
    public void setFlag(boolean flag){this.flag=flag;}
    public void setAround(int bombsAround){this.bombsAround=bombsAround;}
    public void setFinish(boolean finish){this.finish=finish;}
    public void setTrouble(boolean problemBomb){this.problemBomb=problemBomb;}
    
    public boolean isOpen(){return open;}
    public boolean isBomb(){return bomb;}   
    public boolean isFlag(){return flag;}    
    public boolean isFinish(){return finish;}
    public int howManyAround(){return bombsAround;}
    
    public boolean openMine(){
        if(open)return false;
        open=true;
        return true;
    }
    public void changeFlag(){
        if(flag)flag=false;
        else if(!open)flag=true;
    }
    
    public void draw(Graphics g){
        BufferedImage cell;       
        if(open){
            if(bomb){
                if(problemBomb)cell=bigBomb;
                else cell=bombImg;
            }
            else {
                if(bombsAround==0)cell=opened;
                else cell=numbers[bombsAround];
            }
        }
        else {
            if(!finish){
                if(flag)cell=flagImg;
                else {
                    if(pressed) cell=opened;
                    else cell=normal;
                }
            }
            else {
                if(flag){
                    if(bomb)cell=flagImg;
                    else cell=fakeBomb;
                }
                else {
                    if(problemBomb)cell=bigBomb;
                    else {
                        if(bomb)cell=bombImg;
                        else cell=normal;
                    }
                }
            }
        }
        g.drawImage(cell,x*width,y*height+TOP,null);
    }
    public void reset(){
        bomb=false;
        flag=false;
        open=false;
        pressed=false;
        problemBomb=false;
        finish=false;
        bombsAround=0;
    }
}
