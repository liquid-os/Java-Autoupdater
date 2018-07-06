package code;

import java.awt.Color;
import java.awt.Desktop;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import org.apache.commons.io.FileUtils;

public class Base {
	
	String backgroundImage = "cqbg.png";
	String titleImage = "cqlogo.png";
	String iconImage = "icon_cq.png";
	String title = "Conquest of Azeroth";
	String customDataFolder = "Conquest";
	
	String URLPatch = "http://logon.talesofterroth.com/patch-M.mpq";
	String URLLauncher = "http://logon.talesofterroth.com/Conquest.exe";
	String URLVersion = "http://logon.talesofterroth.com/version.txt";
	
	String patchName = "patch-M.mpq";
	String exeName = "Conquest.exe";
	
	String realmlist = "logon.talesofterroth.com";

	JFrame frame = new JFrame();
	CQPanel panel = new CQPanel();
	
	JTextArea txtGen = new JTextArea();
	
	public static void main(String[] args){
		new Base();
	}
	
	public Base(){
		
		if(new File(this.getDirectory()+"Data").exists()){
			
		}else{
			JOptionPane.showMessageDialog(null, "ERROR! Client Not Found!\n\nPlease place this .jar file inside of your World of Warcraft 3.3.5a client and then run it again.");
			System.exit(0);
		}
		
		frame.setSize(800, 550);
		frame.setVisible(true);
		frame.setEnabled(true);
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setTitle(title+" Launcher");
		frame.setLayout(new FlowLayout());
		panel.setSize(800, 600);
		frame.setContentPane(panel);
		
		frame.setIconImage(Filestream.getImageFromPath(iconImage));

		setContentsRawdir(this.getDirectory()+"Data/enUS/realmlist.wtf", "set realmlist "+realmlist);

		new File(getDirectory()+customDataFolder+"/").mkdirs();
		
		int lastVersion = Integer.parseInt(readAll(new File(getDirectory()+customDataFolder+"/version.txt")));
		
		File cqLauncher = new File(getDirectory()+exeName);
		try {
			if(cqLauncher.createNewFile()){
				JOptionPane.showMessageDialog(null, "No game launcher was found. The download will begin now.");
				panel.text = "Downloading game launcher...";
				panel.repaint();
				FileUtils.copyURLToFile(new URL(URLLauncher), cqLauncher);
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		File vCheck = new File(getDirectory()+customDataFolder+"/version.txt");
		try {
			vCheck.createNewFile();
			panel.text = "Checking last patch version...";
			FileUtils.copyURLToFile(new URL(URLVersion), vCheck);
		} catch (IOException e) {
			e.printStackTrace();
		}
		panel.text = "Checking for updates...";
		panel.repaint();
		int newVersion = Integer.parseInt(readAll(new File(getDirectory()+customDataFolder+"/version.txt")));
		
		File patch = new File(getDirectory()+"Data/"+patchName);

		if(lastVersion < newVersion){
			panel.text = "Update found!";
			panel.repaint();

			int selection = JOptionPane.showConfirmDialog(null, "There is a new update availabe. Would you like to download it now?");
			if(selection == JOptionPane.OK_OPTION){
				try {
					patch.createNewFile();
					panel.text = "Downloading patch (v"+newVersion+")...";
					panel.repaint();

					FileUtils.copyURLToFile(new URL(URLPatch), patch);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		panel.btnPlay.setText("Start Game");
		panel.text = "Ready to play!\nRealmlist: logon.talesofterroth.com";
		panel.repaint();
	}
	
	public String getDirectory(){
		String ret = "";
		try {
			String path = Base.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath();
			String[] split = path.split("/");
			for(String s : split){
				if(!s.contains(".jar")){
					ret+=(s+("/"));
				}
			}
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		return ret;
	}
	
	public static void setContentsRawdir(String file, String n){
		File f = new File(file);
		try {
			f.createNewFile();
			FileWriter w = new FileWriter(f);
			BufferedWriter bw = new BufferedWriter(w);
			bw.flush();
			bw.write(n);
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public String getJarName(){
		return new File(Base.class.getProtectionDomain()
				  .getCodeSource()
				  .getLocation()
				  .getPath())
				.getName();
	}
	
	public static String readAll(File file){
		String str = "0";
		try {
			file.createNewFile();
			FileReader w = new FileReader(file);
			BufferedReader br = new BufferedReader(w);
			String line = br.readLine();
		    while (line != null) {
		        str+=line;
		        line = br.readLine();
		    }

		    br.close();			
		    } catch (IOException e) {
			e.printStackTrace();
		}
		return str;
	}
	
	public class CQPanel extends JPanel{
		
		private Image bg = Filestream.getImageFromPath(backgroundImage);
		private Image logo = Filestream.getImageFromPath(titleImage);
		private Image icon = Filestream.getImageFromPath(iconImage);
		
		JButton btnPlay = new JButton();

		public CQPanel(){
			this.setLayout(null);
			btnPlay.setBounds(-5, 450, 805, 70);
			btnPlay.setFont(new Font(Font.DIALOG, Font.PLAIN, 20));
			btnPlay.setBackground(Color.BLACK);
			btnPlay.setForeground(Color.WHITE);
			btnPlay.setEnabled(true);
			btnPlay.setText("...");
			btnPlay.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent arg0) {
					if(btnPlay.getText() == "Start Game"){
						try {
							Process p = Runtime.getRuntime().exec(new File(getDirectory()+exeName).getAbsolutePath());
							System.exit(0);
						} catch (IOException e) {
							e.printStackTrace();
						}
					}else{
						JOptionPane.showMessageDialog(null, "Please wait for the download to complete.");
					}
				}
			});
			this.add(btnPlay);
		}
		public String text = "";
		public boolean ready = false;
		public void paintComponent(Graphics g){
			super.paintComponent(g);
			g.drawImage(bg, 0, 0, 800, 450, null);
			g.drawImage(icon, 400 - 100, 0, 200, 200, null);
			g.drawImage(logo, 400 - 200, 10, 400, 160, null);
			
			g.setColor(text == "Ready to play!" ? Color.GREEN : Color.WHITE);
			g.setFont(new Font(Font.MONOSPACED, Font.HANGING_BASELINE, 40));
			g.drawString(text, 800 / 2 - g.getFontMetrics().stringWidth(text) / 2, 350);
			
			g.setFont(new Font(Font.DIALOG, Font.PLAIN, 12));
			
			/*g.setColor(Color.BLACK);
			g.drawString("Created by Liquid Outerspace", 0, 10);
			g.setColor(Color.WHITE);
			g.drawString("Created by Liquid Outerspace", 1, 11);*/
		}
	}
}
