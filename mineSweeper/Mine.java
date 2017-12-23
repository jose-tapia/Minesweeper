/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mineSweeper;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class Mine {
    private BufferedImage normal;
    private BufferedImage opened;
    private BufferedImage flagImg;
    private BufferedImage bombImg;
    private BufferedImage[] numbers;
    private int x,y,bombsAround,TOP=31;
    private boolean flag,bomb,open;
    public boolean pressed;
    
    private int width=20;
    private int height=20; 
    
    public Mine(int x,int y,BufferedImage normal,BufferedImage opened,BufferedImage flagImg,BufferedImage bombImg,BufferedImage[] numbers){
        this.x=x;
        this.y=y;
        this.normal=normal;
        this.opened=opened;
        this.flagImg=flagImg;
        this.bombImg=bombImg;
        this.numbers=numbers;
    }
    public void setSize(int width,int height){
        this.width=width;
        this.height=height;
    }
    public void setBomb(boolean bomb){
        this.bomb=bomb;
    }
    public void setFlag(boolean flag){
        this.flag=flag;
    }
    public void setAround(int bombsAround){
        this.bombsAround=bombsAround;
    }
    public boolean openMine(){
        if(open)return false;
        open=true;
        return true;
    }
    public boolean isOpen(){
        return open;
    }
    public boolean isBomb(){
        return bomb;
    } 
    public boolean isFlag(){
        return flag;
    }
    public void changeFlag(){
        if(flag)flag=false;
        else if(!open)flag=true;
    }
    public int howManyAround(){
        return bombsAround;
    }
    public void draw(Graphics g){
        if(open){
            if(bomb)g.drawImage(bombImg,x*width,y*height+TOP,null);
            else {
                if(bombsAround==0)g.drawImage(opened,x*width,y*height+TOP,null);
                else g.drawImage(numbers[bombsAround],x*width,y*height+TOP,null);
            }
        }
        else {
            if(flag)g.drawImage(flagImg,x*width,y*height+TOP,null);
            else {
                if(pressed) g.drawImage(opened,x*width,y*height+TOP,null);
                else g.drawImage(normal,x*width,y*height+TOP,null);
            }
        }
    }
    public void reset(){
        bomb=false;
        flag=false;
        open=false;
        pressed=false;
    }
}
