package chess;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import java.sql.Time;
import java.util.ArrayList;
import java.util.HashMap;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JRadioButton;
/**
 * 五子棋
 * @author Administrator
 *
 */
public class Chess extends JPanel{     //继承JPanel类    使Chess作为一个面板
	private int[][] exist=new int[Config.ROWS][Config.COLUMNS];   //创建一个棋子数组   用于保存棋盘上哪个位置有哪个颜色的棋子
	private ChessListener cl;        //声明事件处理类型的 变量
	public Chess(int r, int c) {
		// TODO Auto-generated constructor stub
	}
	/**
	 * 图像界面的显示方法
	 */
	public void showUI(){
		JFrame frame=new JFrame();  //创建窗体  frame
		frame.setTitle("五子棋 ");   //设置窗体的标题
		frame.setSize(800,650);		//设置大小
		//frame.setResizable(false);	//大小不可变
		frame.setLocationRelativeTo(null);	//窗体居中
		frame.setDefaultCloseOperation(3);	//退出时关闭进程
		cl=new ChessListener(this);			// 实例化事件处理类的对象，将棋盘面板作为参数传递过去
		centerPanel(frame);         //在窗体frame上添加中间面板  ---棋盘
		eastPanel(frame);			//窗体frame上添加东边面板	 ---功能按钮		
		frame.setVisible(true);		//设置窗体可见
		cl.setExist(exist);	//将棋子数组传入到事件监听类中
	}
	/**
	 * 音乐
	 */
	static Clip clip;
    public static void playMusic()  {
    	try
        {
            File musicPath = new File("unreeeals.wav");//导入文件,下同
                    if(musicPath.exists())
                    {
                        AudioInputStream audioInput = AudioSystem.getAudioInputStream(musicPath);
                        clip = AudioSystem.getClip();
                        clip.open(audioInput);
                        FloatControl gainControl = (FloatControl)clip.getControl(FloatControl.Type.MASTER_GAIN);
                        gainControl.setValue(-35.0f);//设置音量，范围为 -60.0f 到 6.0f
                        clip.start();//播放音乐
                        clip.loop(Clip.LOOP_CONTINUOUSLY);//循环播放
                    }
                    else
                    {

                    }


            }
            catch(Exception ex)
            {
                ex.printStackTrace();
            }

        }
    public static void playMusicdown() {
        try
        {
            File musicPath = new File("落子声1.wav");

                if(musicPath.exists())
                {
                    AudioInputStream audioInput = AudioSystem.getAudioInputStream(musicPath);
                    clip = AudioSystem.getClip();
                    clip.open(audioInput);
                    FloatControl gainControl = (FloatControl)clip.getControl(FloatControl.Type.MASTER_GAIN);
                    gainControl.setValue(2.0f);//设置音量，范围为 -60.0f 到 6.0f
                    clip.start();//只执行一次,播放对应音效
                  
                }
                else
                {

                }


        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }

    }
    public static void playMusicwin() {
        try
        {
          
            File musicPath = new File("胜利音效.wav");

                if(musicPath.exists())
                {
                    AudioInputStream audioInput = AudioSystem.getAudioInputStream(musicPath);
                    clip = AudioSystem.getClip();
                    clip.open(audioInput);
                    FloatControl gainControl = (FloatControl)clip.getControl(FloatControl.Type.MASTER_GAIN);
                    gainControl.setValue(-20.0f);//设置音量，范围为 -60.0f 到 6.0f
                    clip.start();
                }
                else
                {

                }


        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }

    }
    public static void playMusicdefeat() {
        try
        {
            File musicPath = new File("失败音效.wav");

                if(musicPath.exists())
                {
                    AudioInputStream audioInput = AudioSystem.getAudioInputStream(musicPath);
                    clip = AudioSystem.getClip();
                    clip.open(audioInput);
                    FloatControl gainControl = (FloatControl)clip.getControl(FloatControl.Type.MASTER_GAIN);
                    gainControl.setValue(-20.0f);//设置音量，范围为 -60.0f 到 6.0f
                    clip.start();
                  
                }
                else
                {

                }


        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }

    }
    
	/**
	 * 重写面板重绘的方法
	 */
	public void paint(Graphics g){
		super.paint(g);
		drawChessTable(g);
		reDrawChess(g);
	}
	
	/**
	 * 往窗体上添加中间面板
	 * @param frame   窗体
	 */
	public void centerPanel(JFrame frame){
		this.setBackground(Color.ORANGE);
		frame.add(this);
	}
	
	/**
	 * 往窗体上添加东边面板  ---用于放置功能按钮
	 * @param frame		窗体
	 */
	public void eastPanel(JFrame frame){
		JPanel epanel=new JPanel();			//创建一个面板对象
		epanel.setBackground(Color.GRAY);	//背景颜色设置为gray
		epanel.setPreferredSize(new Dimension(150,600));   //设置大小
		epanel.setLayout(new FlowLayout());//默认也是流式布局
		String[] buttonArray={"开始游戏","认输","关于","音乐"}; //数组存储     功能按钮命令
		for(int i=0;i<buttonArray.length;i++){			//使用循环实例化按钮对象
			JButton button=new JButton(buttonArray[i]); //实例化按钮对象
			button.setPreferredSize(new Dimension(100,50));	//设置大小
			epanel.add(button);							//在面板上添加按钮
			button.addActionListener(cl);				//为每一个按钮添加监听
		}
		String[] radioArray={"人人对战","人机对战","人机对战(对方先手)"};	//数组存储      单选按钮命令
		ButtonGroup bg=new ButtonGroup();			//实例化一个按钮组的对象
		for(int i=0;i<radioArray.length;i++){		//使用循环创建单选按钮对象
			JRadioButton radioButton=new JRadioButton(radioArray[i]);	//实例化单选按钮对象
			bg.add(radioButton);					//将每个创建的单选按钮添加到同一组中
			radioButton.setPreferredSize(new Dimension(100,50));	//设置单选按钮大小
			radioButton.setOpaque(false);			//设置不透明
			radioButton.setForeground(Color.WHITE);	//前景色为白色
			if(i==0){								//默认选中第一个单选按钮
				radioButton.setSelected(true);
			}
			epanel.add(radioButton);				//在面板上添加单选按钮
			radioButton.addActionListener(cl);		//加监听器
		}
	JProgressBar bar=new JProgressBar(0,1200);
		for(int i=0;i<1200;i++) {
			bar.setValue(i);
			epanel.add(bar);// TODO Auto-generated method stub
		}
		frame.add(epanel,BorderLayout.EAST);		//为窗体(边框布局)添加面板---放置在东侧  
	}
	
/*
	 * 绘制棋盘的方法
	 * @param g  传入画笔
	 */
	public void drawChessTable(Graphics g){
		for(int r=0;r<Config.ROWS;r++){    			//行           x 不变    y变
			g.drawLine(Config.X0, Config.Y0+r*Config.SIZE, Config.X0+(Config.COLUMNS-1)*Config.SIZE, Config.Y0+r*Config.SIZE);
		}
		for(int c=0;c<Config.COLUMNS;c++){			//列            x变         y不变
			g.drawLine(Config.X0+Config.SIZE*c,Config.Y0, Config.X0+Config.SIZE*c, Config.Y0+(Config.ROWS-1)*Config.SIZE);
		}
	}
	/**
	 * 棋子重绘的方法
	 * @param g
	 */
	public void reDrawChess(Graphics g){
		Graphics2D g2d=(Graphics2D) g;		//转为Graphics2D   后面要为画笔设置颜色
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		for(int  r=0;r<Config.ROWS;r++){			//外循环控制行
			for(int c=0;c<Config.COLUMNS;c++){		//内循环控制列
				if(exist[r][c]!=0){			//如果该位置不为空
					if(exist[r][c]==1){			//该位置是黑子
						g2d.setColor(Color.BLACK);
						g2d.fillOval(Config.X0+c*Config.SIZE-Config.CHESS_SIZE/2,Config.Y0+r*Config.SIZE-Config.CHESS_SIZE/2 , Config.CHESS_SIZE, Config.CHESS_SIZE);
					}else if(exist[r][c]==-1){  //该位置是白子
						g2d.setColor(Color.WHITE);
						g2d.fillOval(Config.X0+c*Config.SIZE-Config.CHESS_SIZE/2,Config.Y0+r*Config.SIZE-Config.CHESS_SIZE/2 , Config.CHESS_SIZE, Config.CHESS_SIZE);
					}
				}
			}
		}
	}
	public interface Config {
		public static final int X0=20;//棋盘起点坐标x0
		public static final int Y0=30;  //棋盘起点坐标y0
		public static final int ROWS=15;  //行数
		public static final int COLUMNS =15;  //列数
		public static final int CHESS_SIZE=40;   //棋子的大小
		public static final int SIZE=40;      //棋盘行与行  列与列之间的距离
	}
	public class ChessListener extends MouseAdapter implements ActionListener{
		private int x,y;    //记录点击坐标
		private Graphics g;   //存储画笔
		private int count=1;  //判别人人对战时下一步下黑子还是白子
		private int[][] exist;   //生命棋子数组
		private Graphics2D g2d;   //画笔对象
		private HashMap<String, Integer> map=new HashMap<String,Integer>();//创建集合对象，用途是存储每一种棋子相连对应的权值
		private Chess aChess; //生命面板类型的变量   
		private WhoWin win;			//生命判断输赢类的对象
		private String mode="人人对战";   //默认的对战模式为人人对战
		private int flag=0;			//记录下棋的步数
		private ArrayList<Chess> list=new ArrayList<Chess>();    //数组队列   存储的Chess类型的对象
		/**
		 * 构造方法
		 * @param fiveChess
		 */
		public ChessListener(Chess aChess) {
			this.aChess=aChess;
		}
		/**
		 * 设置方法，接收数组
		 * @param exist 存储棋盘上棋子的位置
		 */
		public void setExist(int[][] exist){
			this.exist=exist;
			win=new WhoWin(exist);
		}
		/**
		 * 点击事件处理方法
		 */
		public void actionPerformed(ActionEvent e){
			if(e.getSource() instanceof JRadioButton){		//点击单选按钮
				mode=e.getActionCommand();
			}else if(e.getSource() instanceof JButton){		//点击其他功能按钮
				if(e.getActionCommand().equals("开始游戏")){
					MouseListener[] mls=aChess.getMouseListeners();
					if(mls.length>0){		//如果还有其他监听  ---移除
						aChess.removeMouseListener(this);
					}
					JOptionPane.showConfirmDialog(null, "五手两打");
					reset();				//调用棋盘回到初始状态的方法
					aChess.addMouseListener(this);		// 为棋盘添加鼠标监听
				}else if(e.getActionCommand().equals("悔棋")){    //悔棋的算法
				}else if(e.getActionCommand().equals("认输")){			//认输的算法
					if(flag<10){
						JOptionPane.showMessageDialog(aChess, "总步数小于10，不能认输");
					}else{					
						if(mode.equals("人人对战")){
							if(count==1){		//本来应该下黑色棋子了    但是点击了认输，所以白色棋子获胜
								JOptionPane.showMessageDialog(aChess, "白色棋子获得胜利");
							}else if(count==-1){	//本来应该下白色棋子了    但是点击了认输，所以黑色棋子获胜
								JOptionPane.showMessageDialog(aChess, "黑色棋子获得胜利");
							}
						}else if(mode.equals("人机对战")){		//电脑不会认输的
							JOptionPane.showMessageDialog(aChess, "白色棋子AI获得胜利");
						}
						aChess.removeMouseListener(this);			//认输后不能在棋盘上上下棋了   所以要移除棋盘上的鼠标监听
					}
				}else if(e.getActionCommand().equals("关于")) {
					JOptionPane.showMessageDialog(aChess," 棋盘：15×15围棋的棋盘。\r\n"
							+ "棋子：黑白两种围棋棋子。\r\n"
							+ "棋规：\r\n"
							+ "1. 先后手的确定：可由大赛组委会抽签或对局前猜先。\r\n"
							+ "2. 开局：包括指定开局、自由开局两种，全国博弈大赛拟采用指定开局模式。\r\n"
							+ "3. 对局双方各执一色棋子，黑先、白后交替下在棋盘的交叉点上，棋子下定后，不得向其它点移动，不得从棋盘上拿起另落别处。每次只能下一子（指定开局、三手交换和五手N打、行使PASS权除外）。\r\n"
							+ "在采用指定开局时黑方的第一枚棋子应下在天元上。同时在下面的对局中应执行三手交换和五手N打及禁手规则。\r\n"
							+ "4. 指定开局：指黑方决定了前三个棋子落于何处，其中包括两个黑子和一个白子，一般由黑方完成。黑方应同时给出第五手需要的打点数量。\r\n"
							+ "5.采用指定开局办法的比赛均采用斜指或直指开局（26种），黑方第一子应落在天元处（黑1）。\r\n"
							+ "6.黑方还决定了白方的第一子的落点（白2）。黑方的第二子（黑3），应落在围绕天元点5线×5线而形成的以天元为正中的由交叉点组成的区域内。");
				}else if(e.getActionCommand().equals("音乐")){
			playMusic();	
		}}}
		public void mouseClicked(MouseEvent e){			//鼠标点击事件的处理方法
			x=e.getX();		//获取点击位置的x坐标
			y=e.getY();		//获取点击位置的y坐标
			if(mode.equals("人人对战")){     	     //调用人人对战方法
				pvp(x,y);
			}else if(mode.equals("人机对战")){		//调用人机对战的方法
				pvai(x,y);	
			}
		}
		/**
		 * 设置棋盘回到初始状态的方法
		 */
		public void reset(){
			count=1;		//默认黑色棋子先下棋
			flag=0;			//下棋步数重置为0
			for(int r=0;r<exist.length;r++){				//遍历二维数组，将所有位置清空
				for(int c=0;c<exist[r].length;c++){
					exist[r][c]=0;
				}
			}
			aChess.repaint();   //调用棋盘重绘的方法
		}
		/**
		 * 人人对战的方法
		 * @param x
		 * @param y
		 */
		public void pvp(int x,int y){
			flag++;		//步数+1
			g=aChess.getGraphics();	//从棋盘面板类中获取画布
			g2d=(Graphics2D) g;
			g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			for(int  r=0;r<Config.ROWS;r++){	//行
				for(int c=0;c<Config.COLUMNS;c++){	//列
					if(exist[r][c]==0){				//判断该位置上是否有棋子
						if(flag<7) {
							if((Math.abs(x-Config.X0-c*Config.SIZE)<Config.SIZE/3.0)&&(Math.abs(y-Config.Y0-r*Config.SIZE)<Config.SIZE/3.0)){//将棋子放到交叉点上，误差为1/3
								if(count==1){			//count 为1放置黑子
									g2d.setColor(Color.BLACK);
									exist[r][c]=1;		//记录下了黑色棋子r与c的位置		
									g2d.fillOval(Config.X0+c*Config.SIZE-Config.CHESS_SIZE/2,Config.Y0+r*Config.SIZE-Config.CHESS_SIZE/2 , Config.CHESS_SIZE, Config.CHESS_SIZE);
									playMusicdown();
									count=-1;
									list.add(new Chess(r,c));		//将棋子对象存到数组队列中，保存了棋子的属性  r，c
									if(win.checkWin()==1){		//判断黑色棋子是否赢了
										JOptionPane.showMessageDialog(aChess, "黑色棋子获得胜利");
										aChess.removeMouseListener(this);	//获胜之后，不允许再在棋盘上下棋子，所以移除鼠标监听
										playMusicwin();
										return;
									}
								}else if(count==-1){	//放置白子
									g2d.setColor(Color.WHITE);
									exist[r][c]=-1;
									g2d.fillOval(Config.X0+c*Config.SIZE-Config.CHESS_SIZE/2,Config.Y0+r*Config.SIZE-Config.CHESS_SIZE/2 , Config.CHESS_SIZE, Config.CHESS_SIZE);
									playMusicdown();
									count=1;		//下一步要下黑色棋子
									list.add(new Chess(r,c));		//将棋子对象存到数组队列中，保存了棋子的属性  r，c
									if(win.checkWin()==-1){		//判断白色棋子是否赢了
										JOptionPane.showMessageDialog(aChess, "白色棋子获得胜利");
										aChess.removeMouseListener(this);
										playMusicwin();
										return;
									}
									}}
							}else if(flag==7) {
								int op=JOptionPane.showConfirmDialog(null,"是否进行三手交换");
								if(op==JOptionPane.NO_OPTION) {//否,继续运行
									  if((Math.abs(x-Config.X0-c*Config.SIZE)<Config.SIZE/3.0)&&(Math.abs(y-Config.Y0-r*Config.SIZE)<Config.SIZE/3.0)){//将棋子放到交叉点上，误差为1/3
											if(count==1){			//count 为1放置黑子
												g2d.setColor(Color.BLACK);
												exist[r][c]=1;		//记录下了黑色棋子r与c的位置
												g2d.fillOval(Config.X0+c*Config.SIZE-Config.CHESS_SIZE/2,Config.Y0+r*Config.SIZE-Config.CHESS_SIZE/2 , Config.CHESS_SIZE, Config.CHESS_SIZE);
												playMusicdown();
												count=-1;			//下一次点击时  下白色的棋子
												list.add(new Chess(r,c));
												//将棋子对象存到数组队列中，保存了棋子的属性  r，c
												if(win.checkWin()==1){		//判断白色棋子是否赢了
													JOptionPane.showMessageDialog(aChess, "黑色棋子获得胜利");				
													aChess.removeMouseListener(this);
													playMusicwin();
													return;
												}
											}else if(count==-1){	//放置白子
												g2d.setColor(Color.WHITE);
												exist[r][c]=-1;
												g2d.fillOval(Config.X0+c*Config.SIZE-Config.CHESS_SIZE/2,Config.Y0+r*Config.SIZE-Config.CHESS_SIZE/2 , Config.CHESS_SIZE, Config.CHESS_SIZE);
												playMusicdown();
												count=1;		//下一步要下黑色棋子
												list.add(new Chess(r,c));		//将棋子对象存到数组队列中，保存了棋子的属性  r，c
												
												if(win.checkWin()==-1){		//判断白色棋子是否赢了
													JOptionPane.showMessageDialog(aChess, "白色棋子获得胜利");	
													playMusicwin();
													aChess.removeMouseListener(this);
													return;
							}
						}
											}
								}else if(op==JOptionPane.YES_OPTION){
							 if((Math.abs(x-Config.X0-c*Config.SIZE)<Config.SIZE/3.0)&&(Math.abs(y-Config.Y0-r*Config.SIZE)<Config.SIZE/3.0)){//将棋子放到交叉点上，误差为1/3
									if(count==1){			//count 为1放置黑子
										g2d.setColor(Color.BLACK);
										exist[r][c]=1;		//记录下了黑色棋子r与c的位置
										g2d.fillOval(Config.X0+c*Config.SIZE-Config.CHESS_SIZE/2,Config.Y0+r*Config.SIZE-Config.CHESS_SIZE/2 , Config.CHESS_SIZE, Config.CHESS_SIZE);
										playMusicdown();
										count=-1;			//下一次点击时  下白色的棋子
										list.add(new Chess(r,c));		//将棋子对象存到数组队列中，保存了棋子的属性  r，c
										if(win.checkWin()==1){		//判断白色棋子是否赢了
											JOptionPane.showMessageDialog(aChess, "黑色棋子获得胜利");	
											playMusicwin();
											aChess.removeMouseListener(this);
											return;
										}
									}else if(count==-1){	//放置白子
										g2d.setColor(Color.WHITE);
										exist[r][c]=-1;
										g2d.fillOval(Config.X0+c*Config.SIZE-Config.CHESS_SIZE/2,Config.Y0+r*Config.SIZE-Config.CHESS_SIZE/2 , Config.CHESS_SIZE, Config.CHESS_SIZE);
										playMusicdown();
										count=1;		//下一步要下黑色棋子
										list.add(new Chess(r,c));		//将棋子对象存到数组队列中，保存了棋子的属性  r，c
										if(win.checkWin()==-1){		//判断白色棋子是否赢了
											JOptionPane.showMessageDialog(aChess, "白色棋子获得胜利");				
											aChess.removeMouseListener(this);
											playMusicwin();
											return;
					}
}				
 }
								}
								flag++;
						}else if(flag>7) {
							 if((Math.abs(x-Config.X0-c*Config.SIZE)<Config.SIZE/3.0)&&(Math.abs(y-Config.Y0-r*Config.SIZE)<Config.SIZE/3.0)){//将棋子放到交叉点上，误差为1/3
									if(count==1){			//count 为1放置黑子
										g2d.setColor(Color.BLACK);
										exist[r][c]=1;		//记录下了黑色棋子r与c的位置
										g2d.fillOval(Config.X0+c*Config.SIZE-Config.CHESS_SIZE/2,Config.Y0+r*Config.SIZE-Config.CHESS_SIZE/2 , Config.CHESS_SIZE, Config.CHESS_SIZE);
										playMusicdown();
										count=-1;			//下一次点击时  下白色的棋子
										list.add(new Chess(r,c));		//将棋子对象存到数组队列中，保存了棋子的属性  r，c
										if(win.checkWin()==1){		//判断白色棋子是否赢了
											JOptionPane.showMessageDialog(aChess, "黑色棋子获得胜利");				
											aChess.removeMouseListener(this);
											playMusicwin();
											return;
										}
									}else if(count==-1){	//放置白子
										g2d.setColor(Color.WHITE);
										exist[r][c]=-1;
										g2d.fillOval(Config.X0+c*Config.SIZE-Config.CHESS_SIZE/2,Config.Y0+r*Config.SIZE-Config.CHESS_SIZE/2 , Config.CHESS_SIZE, Config.CHESS_SIZE);
										playMusicdown();
										count=1;		//下一步要下黑色棋子
										list.add(new Chess(r,c));		//将棋子对象存到数组队列中，保存了棋子的属性  r，c
										if(win.checkWin()==-1){		//判断白色棋子是否赢了
											JOptionPane.showMessageDialog(aChess, "白色棋子获得胜利");				
											aChess.removeMouseListener(this);
											playMusicwin();
											return;
					}
						}}}		  
								}}}
			}
				
		/**
		 * 人机对战的方法
		 * @param x  人所下棋子的横坐标
		 * @param y	  人所下棋子的纵坐标
		 */
		public void pvai(int x,int y){	
			flag++;//步数＋1
			g=aChess.getGraphics();
			g2d=(Graphics2D) g;
			g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			for(int  r=0;r<Config.ROWS;r++){
				for(int c=0;c<Config.COLUMNS;c++){
					if(exist[r][c]==0){//判断该位置上是否有棋子
						
						if((Math.abs(x-Config.X0-c*Config.SIZE)<Config.SIZE/3.0)&&(Math.abs(y-Config.Y0-r*Config.SIZE)<Config.SIZE/3.0)){//将棋子放到交叉点上，误差为1/3
								g2d.setColor(Color.BLACK);
								exist[r][c]=1;		//记录下了黑色棋子的位置
								g2d.fillOval(Config.X0+c*Config.SIZE-Config.CHESS_SIZE/2,Config.Y0+r*Config.SIZE-Config.CHESS_SIZE/2 , Config.CHESS_SIZE, Config.CHESS_SIZE);
								playMusicdown();
								list.add(new Chess(r,c));	//队列中添加棋子数组对象  存   r   c
								if(win.checkWin()==1){		//判断黑色棋子是否胜利
									JOptionPane.showMessageDialog(aChess, "黑色棋子获得胜利");
									aChess.removeMouseListener(this);
									playMusicwin();
									return;
								}			
								ai(g2d);		//调用ai下棋的方法
								playMusicdown();
								if(win.checkWin()==-1){		//ai下的是白色棋子，每下完一步，都要判断一次是否获胜
									JOptionPane.showMessageDialog(aChess, "白色棋子获得胜利");				
									aChess.removeMouseListener(this);
									playMusicdefeat();
									return;
								}
						}
					}
				}
			}
		}
		/**
		 三手交换
		 */
		/**
		 * ai 下棋的方法
		 * @param g2d
		 */
		public void ai(Graphics2D g2d){
			g2d.setColor(Color.WHITE);		//设置ai所下棋子的颜色为白色
			int[][] weightArray=new int[Config.ROWS][Config.COLUMNS];//创建一个存储权值的二维数组
			/**
			 * 设置每种棋子相连情况下的权值
			 */
			map.put("010", 1);
			map.put("0110", 20);
			map.put("01110", 50);
			map.put("011110", 500);
			map.put("0-10", 10);
			map.put("0-1-10", 30);
			map.put("0-1-1-10", 70);
			map.put("0-1-1-1-10", 500);
			map.put("-110", 1);
			map.put("-1110", 10);
			map.put("-11110", 30);
			map.put("-111110", 500);
			map.put("1-10", 1);
			map.put("1-1-10", 10);
			map.put("1-1-1-10", 30);
			map.put("1-1-1-1-10", 500);
			for(int r=0;r<exist.length;r++){	//求出权值  将权值存到数组中
				for(int c=0;c<exist[r].length;c++){ 
					if(exist[r][c]==0){			//判断是否是空位
						String code=countHL(r,c);
						Integer weight = map.get(code);//获取棋子相连情况对应的权值
						if(null != weight){//判断权值不为null
							weightArray[r][c] += weight;//累加权值
						}
						code=countVU(r,c);
						weight = map.get(code);//获取棋子相连情况对应的权值
						if(null != weight){//判断权值不为null
							weightArray[r][c] += weight;//累加权值
						}
						code=countLLU(r,c);
						weight = map.get(code);//获取棋子相连情况对应的权值
						if(null != weight){//判断权值不为null
							weightArray[r][c] += weight;//累加权值
						}
						code=countLRU(r,c);
						weight = map.get(code);//获取棋子相连情况对应的权值
						if(null != weight){//判断权值不为null
							weightArray[r][c] += weight;//累加权值
						}
					}
				}
			}
			int max=weightArray[0][0];                    //找出最大的权值
			for(int r=0;r<weightArray.length;r++){
				for(int c=0;c<weightArray[r].length;c++){
					if(weightArray[r][c]>max){
						max=weightArray[r][c];
					}
				}
			}
			for(int r=0;r<weightArray.length;r++){				//随机取最大权值处所在的点
				for(int c=0;c<weightArray[r].length;c++){
					if(weightArray[r][c]==max&&exist[r][c]==0){	//权值最大且是空位
						exist[r][c]=-1;
						g2d.fillOval(Config.X0+c*Config.SIZE-Config.CHESS_SIZE/2,Config.Y0+r*Config.SIZE-Config.CHESS_SIZE/2 , Config.CHESS_SIZE, Config.CHESS_SIZE);
						list.add(new Chess(r,c));
						return;
					}
				}
			}
		}
		/**
		 * 水平向左方向统计棋子相连的情况
		 * @param r   行
		 * @param c	    列
		 * @return
		 */
		private String countHL(int r,int c){
			String code = "0";	//默认记录r,c位置的情况
			int chess = 0;		//记录第一次出现的棋子
			for(int c1=c-1;c1>=0;c1--){		//向左统计
				if(exist[r][c1]==0){		//表示没有棋子的位置
					if(c1+1==c){			//相邻的空位
						break;
					}else{					//空位不相连
						code = exist[r][c1] + code;//记录棋子相连的情况
						break;
					}
				}else{				//表示有棋子
					if(chess==0){	//判断是否是第一次出现棋子
						code = exist[r][c1] + code;	//记录棋子相连的情况
						chess = exist[r][c1];		//记录第一次的棋子的颜色
					}else if(chess==exist[r][c1]){	//表示之后的棋子和第一次的棋子颜色一致
						code = exist[r][c1] + code;	//记录棋子相连的情况
					}else{							//表示之后的棋子和第一次的棋子颜色不同
						code = exist[r][c1] + code;	//记录棋子相连的情况
						break;
					}
				}
			}
			return code;
		}
		
		/**
		 * 垂直向上统计棋子相连的情况
		 * @param r	行
		 * @param c	列
		 * @return
		 */
		private String countVU(int r,int c){     
			String code = "0";					//默认记录r,c位置的情况
			int chess = 0;						//记录第一次出现的棋子
			for(int r1=r-1;r1>=0;r1--){			//向上统计
				if(exist[r1][c]==0){			//表示没有棋子
					if(r1+1==r){				//相邻的空位
						break;
					}else{						//不相邻的空位
						code = exist[r1][c] + code;	//记录棋子相连的情况
						break;
					}
				}else{				//表示有棋子
					if(chess==0){	//判断是否是第一次出现棋子
						code = exist[r1][c] + code;		//记录棋子相连的情况
						chess = exist[r1][c];			//记录第一次的棋子的颜色
					}else if(chess==exist[r1][c]){		//表示之后的棋子和第一次的棋子颜色一致
						code = exist[r1][c] + code;		//记录棋子相连的情况
					}else{		//表示之后的棋子和第一次的棋子颜色不同
						code = exist[r1][c] + code;		//记录棋子相连的情况
						break;
					}
				}
			}
			return code;
		}
		/**
		 * 正斜(\)   棋子相连统计
		 * @param r
		 * @param c
		 * @return
		 */
		private String countLLU(int r,int c){   
			String code = "0";		//默认记录r,c位置的情况
			int chess = 0;			//记录第一次出现的棋子
			for(int r1=r-1,c1=c-1;r1>=0&&c1>0;r1--,c1--){//向上统计
				if(exist[r1][c1]==0){	//表示没有棋子
					if(r1+1==r&&c1+1==c){	//相邻的空位
						break;
					}else{					//不相邻的空位
						code = exist[r1][c1] + code;	//记录棋子相连的情况
						break;
					}
				}else{		//表示有棋子
					if(chess==0){	//判断是否是第一次出现棋子
						code = exist[r1][c1] + code;	//记录棋子相连的情况
						chess = exist[r1][c1];		//记录第一次的棋子的颜色
					}else if(chess==exist[r1][c1]){		//表示之后的棋子和第一次的棋子颜色一致
						code = exist[r1][c1] + code;	//记录棋子相连的情况
					}else{					//表示之后的棋子和第一次的棋子颜色不同
						code = exist[r1][c1] + code;	//记录棋子相连的情况
						break;
					}
				}
			}
			return code;
		}
		/**
		 * 反斜(/)   棋子相连的统计
		 * @param r
		 * @param c
		 * @return
		 */
		private String countLRU(int r,int c){    
			String code = "0";		//默认记录r,c位置的情况
			int chess = 0;			//记录第一次出现的棋子
			for(int r1=r-1,c1=c+1;r1>=0&&c1<Config.COLUMNS;r1--,c1++){
				if(exist[r1][c1]==0){	//表示没有棋子
					if(r1+1==r&&c1-1==c){	//相邻的空位
						break;
					}else{
						code = exist[r1][c1] + code;	//记录棋子相连的情况
						break;
					}
				}else{		//表示有棋子
					if(chess==0){//判断是否是第一次出现棋子
						code = exist[r1][c1] + code;		//记录棋子相连的情况
						chess = exist[r1][c1];			//记录第一次的棋子的颜色
					}else if(chess==exist[r1][c1]){		//表示之后的棋子和第一次的棋子颜色一致
						code = exist[r1][c1] + code;	//记录棋子相连的情况
					}else{					//表示之后的棋子和第一次的棋子颜色不同
						code = exist[r1][c1] + code;	//记录棋子相连的情况
						break;
					}
				}
			}
			return code;
		}
	}
	public class WhoWin {
		private int[][]	exist;
		public WhoWin(int exist[][]){
			this.exist=exist;
		}
		/**
		 * 判断输赢的方法
		 */
		public int checkWin(){
				if((rowWin()==1)||(columnWin()==1)||(rightSideWin()==1)||(leftSideWin()==1)){
					return 1;
				}else if((rowWin()==-1)||(columnWin()==-1)||(rightSideWin()==-1)||(leftSideWin()==-1)){
					return -1;
				}
				return 0;
		}
		/**
		 * 
		 */
		public int rowWin(){
			for(int i=0;i<Config.ROWS;i++){
				for(int j=0;j<Config.COLUMNS-4;j++){
					if(exist[i][j]==-1){
						if(exist[i][j+1]==-1&&exist[i][j+2]==-1&&exist[i][j+3]==-1&&exist[i][j+4]==-1){
							return -1;
						}
					}
					if(exist[i][j]==1){
						if(exist[i][j+1]==1&&exist[i][j+2]==1&&exist[i][j+3]==1&&exist[i][j+4]==1){
							return 1;
						}
					}
				}
			}
			return 0;
		}
		/**
		 * 
		 */
		public int columnWin(){
			for(int i=0;i<Config.ROWS-4;i++){
				for(int j=0;j<Config.COLUMNS;j++){
					if(exist[i][j]==-1){
						if(exist[i+1][j]==-1&&exist[i+2][j]==-1&&exist[i+3][j]==-1&&exist[i+4][j]==-1){
							return -1;
						}
					}
					if(exist[i][j]==1){
						if(exist[i+1][j]==1&&exist[i+2][j]==1&&exist[i+3][j]==1&&exist[i+4][j]==1){
							return 1;
						}
					}
			}
		}	
			return 0;
		}
		/**
		 * 
		 */
		public int rightSideWin(){  //正斜
			for(int i=0;i<Config.ROWS-4;i++){
				for(int j=0;j<Config.COLUMNS-4;j++){
					if(exist[i][j]==-1){
						if(exist[i+1][j+1]==-1&&exist[i+2][j+2]==-1&&exist[i+3][j+3]==-1&&exist[i+4][j+4]==-1){
							return -1;
						}
					}
					if(exist[i][j]==1){
						if(exist[i+1][j+1]==1&&exist[i+2][j+2]==1&&exist[i+3][j+3]==1&&exist[i+4][j+4]==1){
							return 1;
						}
					}
				}
			}
			return 0;
		}
		public int leftSideWin(){   //反斜
			for(int i=4;i<Config.ROWS;i++){
				for(int j=0;j<Config.COLUMNS-4;j++){
					if(exist[i][j]==-1){
						if(exist[i-1][j+1]==-1&&exist[i-2][j+2]==-1&&exist[i-3][j+3]==-1&&exist[i-4][j+4]==-1){
							return -1;
						}
					}
					if(exist[i][j]==1){
						if(exist[i-1][j+1]==1&&exist[i-2][j+2]==1&&exist[i-3][j+3]==1&&exist[i-4][j+4]==1){
							return 1;
						}
					}
				}
			}
			return 0;
		}

	/**
	 * 棋子类---具有棋子行与列的属性
	 * @author Administrator
	 *
	 */
	public class Chesses {
		private int r;	//行
		private int c;	//列
		public Chesses(int r,int c){
			this.r=r;
			this.setC(c);
		}
		public int getR() {
			return r;
		}
		public void setR(int r) {
			this.r = r;
		}
		public int getC() {
			return c;
		}
		public void setC(int c) {
			this.c = c;
		}
	}

	public static void main(String[] args) {
		Chess chess=new Chess(0,0);
		chess.showUI();
	}
	}
	}
