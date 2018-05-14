package gui;

import core.ConfigurationHandler;
import core.Yasu;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import javax.security.auth.login.LoginException;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import net.dv8tion.jda.core.OnlineStatus;
import net.dv8tion.jda.core.exceptions.RateLimitedException;

public class SetupGUI extends JFrame {
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField textToken;
	private JTextField textHostid;
	private JLabel lblPrefixes;
	private JLabel lblTitle;
	private JLabel lblToken;
	private JLabel lblHostid;
	private JButton btnRun;
	private java.awt.List listPrefixes;
	private JLabel lblState;
	private JLabel lblApiKeys;
	private JLabel lblGame;
	private JTextField textGame;
	private JComboBox<OnlineStatus> comboBoxState;
	private JCheckBox chckbxReconnect;
	private JTextField apiKeysField;

	public SetupGUI(ConfigurationHandler config) {
		drawSetup(config);
		setResizable(false);
	}

	private void drawSetup(final ConfigurationHandler config) {
		setDefaultCloseOperation(3);
		setBounds(100, 100, 314, 465);
		this.contentPane = new JPanel();
		this.contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(this.contentPane);
		this.contentPane.setLayout(null);

		this.lblTitle = new JLabel("Bot - Setup");
		this.lblTitle.setHorizontalAlignment(0);
		this.lblTitle.setFont(new Font("Microsoft YaHei Light", 0, 19));
		this.lblTitle.setBounds(10, 36, 287, 26);
		this.contentPane.add(this.lblTitle);

		this.lblToken = new JLabel("Token:");
		this.lblToken.setBounds(10, 48, 287, 14);
		this.contentPane.add(this.lblToken);

		this.textToken = new JTextField();
		this.textToken.setBounds(10, 66, 287, 20);
		this.contentPane.add(this.textToken);
		this.textToken.setColumns(10);

		this.lblHostid = new JLabel("Host ID (Discord ID):");
		this.lblHostid.setBounds(10, 97, 287, 14);
		this.contentPane.add(this.lblHostid);

		this.textHostid = new JTextField();
		this.textHostid.setBounds(10, 114, 287, 20);
		this.contentPane.add(this.textHostid);
		this.textHostid.setColumns(10);

		this.lblPrefixes = new JLabel("Prefixes (max2):");
		this.lblPrefixes.setBounds(10, 145, 287, 14);
		this.contentPane.add(this.lblPrefixes);

		this.listPrefixes = new java.awt.List();
		this.listPrefixes.setMultipleSelections(true);
		this.listPrefixes.setBounds(10, 165, 287, 31);
		String[] values = { "!", "=", "+", "$", ">", "<", ";", ":", "#", "�" };
		for (String v : values) {
			this.listPrefixes.add(v);
		}
		this.contentPane.add(this.listPrefixes);

		this.lblState = new JLabel("State:");
		this.lblState.setBounds(10, 202, 46, 14);
		this.contentPane.add(this.lblState);

		this.comboBoxState = new JComboBox<OnlineStatus>();
		this.comboBoxState.setModel(new DefaultComboBoxModel<OnlineStatus>(OnlineStatus.values()));
		this.comboBoxState.setBounds(10, 218, 287, 20);
		this.contentPane.add(this.comboBoxState);

		this.lblApiKeys = new JLabel("API Keys (seperate by comma)");
		this.lblApiKeys.setBounds(10, 249, 287, 14);
		this.contentPane.add(this.lblApiKeys);

		this.apiKeysField = new JTextField();
		this.apiKeysField.setBounds(10, 274, 288, 39);
		this.contentPane.add(this.apiKeysField);
		this.apiKeysField.setColumns(10);

		this.lblGame = new JLabel("Game State:");
		this.lblGame.setBounds(10, 320, 287, 14);
		this.contentPane.add(this.lblGame);

		this.textGame = new JTextField();
		this.textGame.setBounds(10, 345, 287, 20);
		this.contentPane.add(this.textGame);
		this.textGame.setColumns(10);

		this.chckbxReconnect = new JCheckBox("Set bot auto reconnect?");
		this.chckbxReconnect.setSelected(true);
		this.chckbxReconnect.setBounds(6, 372, 291, 23);
		this.contentPane.add(this.chckbxReconnect);

		this.btnRun = new JButton("Run");
		this.btnRun.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String token = SetupGUI.this.textToken.getText();
				String hostid = SetupGUI.this.textHostid.getText();
				String[] prefixes = SetupGUI.this.listPrefixes.getSelectedItems();
				OnlineStatus state = (OnlineStatus) SetupGUI.this.comboBoxState.getSelectedItem();
				String game = SetupGUI.this.textGame.getText();
				if ((token.equals("")) || (hostid.equals("")) || (state == null) || (game.equals(""))
						|| (prefixes.length != 2)) {
					SetupGUI.this.lblTitle.setText("Something wrong! Oops");
					return;
				}
				java.util.List<String> items = Arrays.asList(SetupGUI.this.apiKeysField.getText().split("\\s*,\\s*"));

				config.setValue("TOKEN", token);
				config.setValue("HOSTID", hostid);
				config.setValue("PASID", "166176374036365312");
				config.setValue("PREFIX1", prefixes[0]);
				config.setValue("PREFIX2", prefixes[1]);
				config.setValue("STATUS", state.getKey());
				config.setValue("GAME", game);
				config.setValue("RECONNECT", Boolean.valueOf(SetupGUI.this.chckbxReconnect.isSelected()).toString());
				config.setValue("YOUTUBE_KEY", (String) items.get(0));
				config.setValue("GIPHY_KEY", (String) items.get(1));
				config.setValue("CLEVERBOT_KEY", (String) items.get(2));
				try {
					Yasu.boot();
				} catch (LoginException e1) {
					e1.printStackTrace();
				} catch (IllegalArgumentException e1) {
					e1.printStackTrace();
				} catch (RateLimitedException e1) {
					e1.printStackTrace();
				}
				SetupGUI.this.dispose();
			}
		});
		this.btnRun.setBounds(10, 402, 287, 23);
		this.contentPane.add(this.btnRun);
	}
}
