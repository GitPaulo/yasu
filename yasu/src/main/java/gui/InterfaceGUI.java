package gui;

import core.DiscordMessage;
import core.Yasu;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.Timer;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.TextChannel;
import org.apache.maven.model.Model;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;
import utilities.SystemInfo;

public class InterfaceGUI extends JFrame {
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTabbedPane tabbedPane;
	private JPanel panelOverview;
	private JPanel panelLogs;
	private JPanel panelCmd;
	private JPanel panelSettings;
	private JTextField textCMD;
	private JLabel lblTitle;
	private JLabel lblVersion;
	private JLabel lblDevelopers;
	private JLabel lblMOTD;
	private JTextPane textPaneMOTD;
	private JLabel lblMemoryUsage;
	private JProgressBar progressBarMemory;
	private JLabel lblCpuUsage;
	private JProgressBar progressBarCPU;
	private JLabel lblServers;
	private JLabel lblUptime;
	private JTextArea textArea;
	private JLabel lblOperatingSystem;
	private JPanel panelDiscord;
	private JButton buttonShutdown;
	private JButton btnBMOTD;
	private JButton btnBTTSMOTD;
	private JButton buttonSend;
	private JLabel lblServerData;
	private JTable tableServerData;
	private JScrollPane scrollPaneServerData;
	private Object[] serverDataColumns = { "ID", "Name", "#Members", "#TextChannels", "#VoiceChannels", "Owner" };
	private JLabel lblBotSpecificData;
	private JTextField textBotID;
	private JTextField textAvatarID;
	private JTextField textBotDiscriminator;
	private JTextField textToken;
	private JLabel lblPingThreads;
	private Border border = BorderFactory.createLineBorder(Color.BLACK);

	public InterfaceGUI() {
		drawInterface();
		setResizable(false);
	}

	private void drawInterface() {
		MavenXpp3Reader reader = new MavenXpp3Reader();
		Model model = null;
		try {
			model = reader.read(new FileReader("pom.xml"));
		} catch (IOException | XmlPullParserException e) {
			System.out.println("Could not read pom.xml! On the interface draw method");
			throw new RuntimeException(e);
		}
		drawOverview(model);
		drawDiscord();
		drawLogging();
		drawCMD();
		drawSettings();

		ActionListener actionListener = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				InterfaceGUI.this.lblUptime.setText("Uptime: " + Yasu.getUptime() / 1000L + "s");
				InterfaceGUI.this.lblPingThreads
						.setText("Ping: " + Yasu.bot.getInstance().getPing() + "ms | Threads: " + Thread.activeCount());
				InterfaceGUI.this.progressBarMemory.setValue(InterfaceGUI.this.progressBarMemory.getMaximum()
						- (int) (Runtime.getRuntime().freeMemory() / 1000L));
				try {
					InterfaceGUI.this.progressBarCPU.setValue((int) SystemInfo.getProcessCpuLoad());
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		};
		Timer timer = new Timer(1000, actionListener);
		timer.start();
	}

	private void drawOverview(Model model) {
		setDefaultCloseOperation(3);
		setBounds(100, 100, 598, 460);
		this.contentPane = new JPanel();
		this.contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(this.contentPane);
		this.contentPane.setLayout(null);

		this.tabbedPane = new JTabbedPane(1);
		this.tabbedPane.setBounds(10, 11, 572, 412);
		this.contentPane.add(this.tabbedPane);

		this.panelOverview = new JPanel();
		this.tabbedPane.addTab("Overview", null, this.panelOverview, null);
		this.panelOverview.setLayout(null);

		this.lblTitle = new JLabel(Yasu.bot.getInstance().getSelfUser().getName() + " - Overview \r\n");
		this.lblTitle.setHorizontalAlignment(0);
		this.lblTitle.setBounds(10, 11, 547, 14);
		this.panelOverview.add(this.lblTitle);

		this.lblVersion = new JLabel("VERSION: " + model.getVersion());
		this.lblVersion.setBounds(10, 29, 547, 14);
		this.panelOverview.add(this.lblVersion);

		this.lblDevelopers = new JLabel("Developer group: " + model.getGroupId());
		this.lblDevelopers.setBounds(10, 47, 547, 14);
		this.panelOverview.add(this.lblDevelopers);

		this.lblUptime = new JLabel("Uptime: " + Yasu.getUptime() / 1000L + "s");
		this.lblUptime.setBounds(10, 84, 547, 14);
		this.panelOverview.add(this.lblUptime);

		this.lblOperatingSystem = new JLabel("System Information - OS: " + System.getProperty("os.name") + "("
				+ System.getProperty("os.arch") + ") -  User: " + System.getProperty("user.name"));
		this.lblOperatingSystem.setBounds(10, 102, 547, 14);
		this.panelOverview.add(this.lblOperatingSystem);

		this.lblPingThreads = new JLabel(
				"Ping: " + Yasu.bot.getInstance().getPing() + "ms | Threads: " + Thread.activeCount());
		this.lblPingThreads.setBounds(10, 119, 547, 14);
		this.panelOverview.add(this.lblPingThreads);

		this.lblMOTD = new JLabel("Message of the Day:");
		this.lblMOTD.setHorizontalAlignment(0);
		this.lblMOTD.setBounds(10, 232, 537, 14);
		this.panelOverview.add(this.lblMOTD);

		this.textPaneMOTD = new JTextPane();
		this.textPaneMOTD.setForeground(new Color(0, 0, 0));
		this.textPaneMOTD.setBounds(10, 247, 547, 92);
		this.textPaneMOTD.setBorder(
				BorderFactory.createCompoundBorder(this.border, BorderFactory.createEmptyBorder(10, 10, 10, 10)));
		this.panelOverview.add(this.textPaneMOTD);

		this.lblMemoryUsage = new JLabel("Memory usage: (Mbs)(estimate)\r\n");
		this.lblMemoryUsage.setBounds(10, 157, 537, 14);
		this.panelOverview.add(this.lblMemoryUsage);

		this.progressBarMemory = new JProgressBar();
		this.progressBarMemory.setBounds(10, 176, 547, 14);
		this.progressBarMemory.setMaximum((int) (Runtime.getRuntime().totalMemory() / 1000L));
		this.progressBarMemory.setMinimum(0);
		this.progressBarMemory
				.setValue(this.progressBarMemory.getMaximum() - (int) (Runtime.getRuntime().freeMemory() / 1000L));
		this.panelOverview.add(this.progressBarMemory);

		this.lblCpuUsage = new JLabel("CPU usage (%):");
		this.lblCpuUsage.setBounds(10, 192, 537, 14);
		this.panelOverview.add(this.lblCpuUsage);

		this.progressBarCPU = new JProgressBar();
		this.progressBarCPU.setBounds(10, 207, 547, 14);
		this.progressBarCPU.setMaximum(100);
		this.progressBarCPU.setMinimum(0);
		try {
			this.progressBarCPU.setValue((int) SystemInfo.getProcessCpuLoad());
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		this.panelOverview.add(this.progressBarCPU);

		this.lblServers = new JLabel("Servers: #" + Yasu.bot.getInstance().getGuilds().size());
		this.lblServers.setBounds(10, 65, 547, 14);
		this.panelOverview.add(this.lblServers);

		this.btnBMOTD = new JButton("Broadcast MOTD");
		this.btnBMOTD.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JDA nis = Yasu.bot.getInstance();
				for (TextChannel t : nis.getTextChannels()) {
					t.sendMessage("```xl\n MOTD:\n " + InterfaceGUI.this.textPaneMOTD.getText() + "```").queue();
				}
			}
		});
		this.btnBMOTD.setBounds(10, 350, 173, 23);
		this.panelOverview.add(this.btnBMOTD);

		this.btnBTTSMOTD = new JButton("TTS Broadcast MOTD");
		this.btnBTTSMOTD.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JDA nis = Yasu.bot.getInstance();
				for (TextChannel t : nis.getTextChannels()) {
					t.sendMessage("(message trasmited via tts)").queue();
					t.sendMessage(DiscordMessage.toTTS(InterfaceGUI.this.textPaneMOTD.getText())).queue();
				}
			}
		});
		this.btnBTTSMOTD.setBounds(193, 350, 177, 23);
		this.panelOverview.add(this.btnBTTSMOTD);

		this.buttonShutdown = new JButton("Shutdown\r\n");
		this.buttonShutdown.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JDA nis = Yasu.bot.getInstance();
				for (TextChannel t : nis.getTextChannels()) {
					t.sendMessage(DiscordMessage.embedNotification(nis.getSelfUser(),
							"Shutingdown.... waaaaa niiii sama! dai suki! \n (������������������� �����?���"))
							.complete();
				}
				InterfaceGUI.this.dispose();
				System.exit(0);
			}
		});
		this.buttonShutdown.setBackground(new Color(220, 20, 60));
		this.buttonShutdown.setBounds(380, 350, 177, 23);
		this.panelOverview.add(this.buttonShutdown);
	}

	private void drawDiscord() {
		this.panelDiscord = new JPanel();
		this.tabbedPane.addTab("Discord Profiling", null, this.panelDiscord, null);
		this.panelDiscord.setLayout(null);

		this.lblServerData = new JLabel("Server Specific Data");
		this.lblServerData.setBounds(10, 11, 547, 14);
		this.panelDiscord.add(this.lblServerData);

		this.scrollPaneServerData = new JScrollPane();
		this.scrollPaneServerData.setBounds(10, 36, 547, 198);
		this.panelDiscord.add(this.scrollPaneServerData);

		this.tableServerData = new JTable();
		DefaultTableModel model = new DefaultTableModel();
		model.setColumnIdentifiers(this.serverDataColumns);
		for (Guild g : Yasu.bot.getInstance().getGuilds()) {
			model.addRow(new Object[] { g.getId(), g.getName(), Integer.toString(g.getMembers().size()),
					Integer.toString(g.getTextChannels().size()), Integer.toString(g.getVoiceChannels().size()),
					g.getOwner().getNickname() });
		}
		this.tableServerData.setModel(model);
		this.scrollPaneServerData.setViewportView(this.tableServerData);

		this.lblBotSpecificData = new JLabel("Bot Specific Data:");
		this.lblBotSpecificData.setBounds(10, 245, 547, 14);
		this.panelDiscord.add(this.lblBotSpecificData);

		this.textBotID = new JTextField("Bot ID: " + Yasu.bot.getInstance().getSelfUser().getIdLong());
		this.textBotID.setBounds(10, 295, 547, 20);
		this.panelDiscord.add(this.textBotID);
		this.textBotID.setColumns(10);

		this.textAvatarID = new JTextField("Avatar ID: " + Yasu.bot.getInstance().getSelfUser().getAvatarId());
		this.textAvatarID.setColumns(10);
		this.textAvatarID.setBounds(10, 326, 547, 20);
		this.panelDiscord.add(this.textAvatarID);

		this.textBotDiscriminator = new JTextField(
				"Discriminator: " + Yasu.bot.getInstance().getSelfUser().getDiscriminator());
		this.textBotDiscriminator.setColumns(10);
		this.textBotDiscriminator.setBounds(10, 353, 547, 20);
		this.panelDiscord.add(this.textBotDiscriminator);

		this.textToken = new JTextField("Token: " + Yasu.bot.getInstance().getToken());
		this.textToken.setBounds(10, 264, 547, 20);
		this.panelDiscord.add(this.textToken);
		this.textToken.setColumns(10);
	}

	private void drawLogging() {
		this.panelLogs = new JPanel();
		this.tabbedPane.addTab("Logging", null, this.panelLogs, null);
		this.panelLogs.setLayout(null);
	}

	private void drawCMD() {
		this.panelCmd = new JPanel();
		this.tabbedPane.addTab("CMD", null, this.panelCmd, null);
		this.panelCmd.setLayout(null);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 11, 537, 297);
		this.panelCmd.add(scrollPane);

		this.textArea = new JTextArea();
		scrollPane.setViewportView(this.textArea);

		PrintStream out = new PrintStream(new TextOutputStream(this.textArea));

		System.setOut(out);

		System.setErr(out);

		this.textCMD = new JTextField();
		this.textCMD.setBounds(99, 354, 448, 20);
		this.panelCmd.add(this.textCMD);
		this.textCMD.setColumns(10);

		JButton btnClear = new JButton("C");
		btnClear.setBounds(10, 353, 45, 23);
		this.panelCmd.add(btnClear);

		this.buttonSend = new JButton(">");
		this.buttonSend.setBounds(54, 353, 45, 23);
		this.panelCmd.add(this.buttonSend);
	}

	private void drawSettings() {
		this.panelSettings = new JPanel();
		this.tabbedPane.addTab("Settings", null, this.panelSettings, null);
		this.panelSettings.setLayout(null);
	}
}
