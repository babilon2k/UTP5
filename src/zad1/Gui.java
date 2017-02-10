package zad1;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;

import javax.swing.BoxLayout;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

import zad1.models.Model1;

public class Gui extends JFrame
{

	private static final long serialVersionUID = 1L;

	GenTabModel<?> tabModel;
	Controller c;
	private JButton btnRunScriptFromFile;
	private JButton btnRunScript;
	private JButton btnRun;

	// Components Models
	private DefaultListModel<Model1> modelsModel = new DefaultListModel<Model1>();
	private DefaultListModel<File> dataModel = new DefaultListModel<File>();
	private JList<File> lstData;
	private JList<Model1> lstModels;
	private JTable table;
	private JPanel panelTable;
	private JScrollPane spTable;
	private JScrollPane spTextArea;
	private JTextArea dataTextArea;

	public Gui()
	{
		SwingUtilities.invokeLater(new Runnable()
		{
			@Override
			public void run()
			{
				try
				{
					UIManager.setLookAndFeel(
							"javax.swing.plaf.nimbus.NimbusLookAndFeel");
				} catch (ClassNotFoundException | InstantiationException
						| IllegalAccessException
						| UnsupportedLookAndFeelException e)
				{
					throw new RuntimeException(e);
				}
				createGui();
			}
		});
	}

	protected void createGui()
	{
		initComponents();
		createEvents();
	}

	private void initComponents()
	{
		// Main window
		setTitle("Modelling framework sample");
		setBounds(100, 100, 1400, 700);

		JPanel contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);

		JPanel panelLists = new JPanel();

		JScrollPane spModels = new JScrollPane();
		spModels.setViewportBorder(new TitledBorder(null, "Models",

				TitledBorder.LEADING, TitledBorder.TOP, null, null));

		JScrollPane spData = new JScrollPane();
		spData.setViewportBorder(new TitledBorder(null, "Data",

				TitledBorder.LEADING, TitledBorder.TOP, null, null));

		btnRun = new JButton("Run model");

		JLabel lblSelectModel = new JLabel("Select model and data");
		lblSelectModel.setFont(new Font("Tahoma", Font.BOLD, 12));
		GroupLayout gl_panelLists = new GroupLayout(panelLists);
		gl_panelLists.setHorizontalGroup(gl_panelLists
				.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panelLists.createSequentialGroup()
						.addGroup(gl_panelLists
								.createParallelGroup(Alignment.LEADING)
								.addGroup(gl_panelLists.createSequentialGroup()
										.addContainerGap()
										.addComponent(spModels,
												GroupLayout.DEFAULT_SIZE, 183,
												Short.MAX_VALUE)
										.addPreferredGap(
												ComponentPlacement.UNRELATED)
										.addComponent(spData,
												GroupLayout.DEFAULT_SIZE, 182,
												Short.MAX_VALUE)
										.addGap(4))
								.addGroup(gl_panelLists.createSequentialGroup()
										.addGap(152)
										.addComponent(btnRun,
												GroupLayout.PREFERRED_SIZE, 99,
												GroupLayout.PREFERRED_SIZE)
										.addPreferredGap(
												ComponentPlacement.RELATED, 134,
												Short.MAX_VALUE)))
						.addGap(16))
				.addGroup(gl_panelLists
						.createSequentialGroup().addComponent(lblSelectModel,
								GroupLayout.DEFAULT_SIZE, 377, Short.MAX_VALUE)
						.addGap(28)));
		gl_panelLists.setVerticalGroup(gl_panelLists
				.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panelLists.createSequentialGroup()
						.addContainerGap()
						.addComponent(lblSelectModel,
								GroupLayout.PREFERRED_SIZE, 42,
								GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(ComponentPlacement.UNRELATED)
						.addGroup(gl_panelLists
								.createParallelGroup(Alignment.LEADING)
								.addComponent(spData, GroupLayout.DEFAULT_SIZE,
										463, Short.MAX_VALUE)
								.addComponent(spModels,
										GroupLayout.DEFAULT_SIZE, 463,
										Short.MAX_VALUE))
						.addGap(18)
						.addComponent(btnRun, GroupLayout.PREFERRED_SIZE, 26,
								GroupLayout.PREFERRED_SIZE)
						.addGap(34)));

		lstData = new JList<File>(dataModel);
		spData.setViewportView(lstData);
		initlstData();

		lstModels = new JList<Model1>(modelsModel);
		spModels.setViewportView(lstModels);
		panelLists.setLayout(gl_panelLists);
		initlstModels();

		// Table panel
		panelTable = new JPanel();
		btnRunScriptFromFile = new JButton("Run Script From File");
		btnRunScript = new JButton("Create and run ad hoc script");
		initTable();

		spTextArea = new JScrollPane();
		spTextArea.setViewportBorder(new TitledBorder(null, "Computadet data",
				TitledBorder.LEADING, TitledBorder.TOP, null, null));

		GroupLayout gl_panelTable = new GroupLayout(panelTable);
		gl_panelTable.setHorizontalGroup(gl_panelTable
				.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_panelTable.createSequentialGroup()
						.addGroup(gl_panelTable
								.createParallelGroup(Alignment.LEADING)
								.addGroup(gl_panelTable.createSequentialGroup()
										.addGap(68)
										.addComponent(btnRunScriptFromFile)
										.addGap(144).addComponent(btnRunScript))
								.addGroup(gl_panelTable.createSequentialGroup()
										.addContainerGap().addComponent(spTable,
												GroupLayout.DEFAULT_SIZE, 815,
												Short.MAX_VALUE)))
						.addGap(22))
				.addGroup(Alignment.LEADING,
						gl_panelTable.createSequentialGroup().addContainerGap()
								.addComponent(spTextArea,
										GroupLayout.DEFAULT_SIZE, 816,
										Short.MAX_VALUE)
								.addGap(21)));
		gl_panelTable.setVerticalGroup(gl_panelTable
				.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_panelTable.createSequentialGroup().addGap(56)
						.addComponent(spTable, GroupLayout.PREFERRED_SIZE, 220,
								GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(ComponentPlacement.UNRELATED)
						.addComponent(spTextArea, GroupLayout.DEFAULT_SIZE, 282,
								Short.MAX_VALUE)
						.addPreferredGap(ComponentPlacement.UNRELATED)
						.addGroup(gl_panelTable
								.createParallelGroup(Alignment.BASELINE)
								.addComponent(btnRunScriptFromFile)
								.addComponent(btnRunScript))
						.addGap(41)));

		dataTextArea = new JTextArea();
		spTextArea.setViewportView(dataTextArea);

		panelTable.setLayout(gl_panelTable);
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(gl_contentPane
				.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup().addGap(7)
						.addComponent(panelLists, GroupLayout.PREFERRED_SIZE,
								GroupLayout.DEFAULT_SIZE,
								GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(ComponentPlacement.RELATED)
						.addComponent(panelTable, GroupLayout.DEFAULT_SIZE, 847,
								Short.MAX_VALUE)
						.addGap(109)));
		gl_contentPane.setVerticalGroup(gl_contentPane
				.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup().addGap(7)
						.addGroup(gl_contentPane
								.createParallelGroup(Alignment.LEADING)
								.addComponent(panelTable,
										GroupLayout.DEFAULT_SIZE, 644,
										Short.MAX_VALUE)
								.addComponent(panelLists,
										GroupLayout.DEFAULT_SIZE, 644,
										Short.MAX_VALUE))));
		contentPane.setLayout(gl_contentPane);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setVisible(true);
	}

	private void initTable()
	{
		spTable = new JScrollPane();
		table = new JTable(new DefaultTableModel(
				new Object[][]{{"twKI", null, null, null, null, null},
						{"twKS", null, null, null, null, null},
						{"twNW", null, null, null, null, null},
						{"twEKS", null, null, null, null, null},
						{"twMP", null, null, null, null, null},
						{"KI", null, null, null, null, null},
						{"KS", null, null, null, null, null},
						{"INW", null, null, null, null, null},
						{"EKS", null, null, null, null, null},
						{"IMP", null, null, null, null, null},
						{"PKB", null, null, null, null, null},
						{"ZDEKS", null, null, null, null, null},},
				new String[]{"", "2015", "2016", "New column", "New column",
						"New column"}));
		table.setColumnSelectionAllowed(false);
		spTable.setViewportView(table);
	}

	// Initialize model list with models Objects.
	private void initlstModels()
	{
		modelsModel.addElement(new Model1());
	}
	// Initialize data list with data to chose from files.
	private void initlstData()
	{
		// Cell renderer to display just file name with extension, and not full
		// path.
		lstData.setCellRenderer(new DefaultListCellRenderer()
		{
			private static final long serialVersionUID = 1L;

			@Override
			public Component getListCellRendererComponent(JList<?> list,
					Object value, int index, boolean isSelected,
					boolean cellHasFocus)
			{
				Component renderer = super.getListCellRendererComponent(list,
						value, index, isSelected, cellHasFocus);
				if (renderer instanceof JLabel && value instanceof File)
				{
					// Here value will be of the Type 'Reservation'
					((JLabel) renderer).setText(((File) value).getName());
				}
				return renderer;
			}
		});

		File dataDir = new File(
				System.getProperty("user.home") + "/Modeling/data/");

		File[] filesList = dataDir.listFiles();
		for (File f : filesList)
		{
			dataModel.addElement(f);
		}
		lstData.setModel(dataModel);
	}

	private void createEvents()
	{
		// Model list handlers
		lstModels.addMouseListener(new MouseAdapter()
		{
			@Override
			public void mouseClicked(MouseEvent e)
			{
			}
		});

		// Data list handlers
		lstData.addMouseListener(new MouseAdapter()
		{
			@Override
			public void mouseClicked(MouseEvent e)
			{
			}
		});

		// Button Run in Lists Panel. Selected model and selected data
		btnRun.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				if (lstData.getSelectedValue() != null
						&& lstModels.getSelectedValue() != null)
				{
					c = new Controller(lstModels.getSelectedValue().toString());

					JFileChooser fc = new JFileChooser();
					fc.setSelectedFile(new File(System.getProperty("user.home")
							+ "/Modeling/scripts/script1.groovy"));
					String filename = fc.getSelectedFile().getPath();
					c.readDataFrom(lstData.getSelectedValue().toString())
							.runModel().runScriptFromFile(filename);

					// Do usuniecia jak table bedzie dzialac
					String res = c.getResultsAsTsv();
					dataTextArea.setText(res);

				} else
					JOptionPane.showMessageDialog(null,
							"Please select model and data");
			}
		});

		// Run Script From File
		btnRunScriptFromFile.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				if (lstData.getSelectedValue() != null
						&& lstModels.getSelectedValue() != null)
				{
					c = new Controller(lstModels.getSelectedValue().toString());
					JFileChooser fc = new JFileChooser();
					fc.setCurrentDirectory(
							new File(System.getProperty("user.home")
									+ "/Modeling/scripts/"));
					int result = fc.showOpenDialog(null);
					if (result == JFileChooser.APPROVE_OPTION)
					{
						String filename = fc.getSelectedFile().getPath();
						c.readDataFrom(lstData.getSelectedValue().toString())
								.runModel().runScriptFromFile(filename);

						// Do usuniecia jak table bedzie dzialac
						String res = c.getResultsAsTsv();
						dataTextArea.setText(res);
					} else if (result == JFileChooser.CANCEL_OPTION)
					{
						JOptionPane.showMessageDialog(null,
								"You didn't select a file.");
					} else if (result == JFileChooser.ERROR_OPTION)
					{
						JOptionPane.showInternalMessageDialog(null, "Error");
					}
				} else
					JOptionPane.showMessageDialog(null,
							"Please select model and data");
			}
		});

		// Run script from JTextArea and new input window
		btnRunScript.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				JFrame jf = new JFrame("Script");
				JPanel panel = new JPanel();
				panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

				JTextArea scriptTxtArea = new JTextArea(20, 40);
				JScrollPane sp = new JScrollPane(scriptTxtArea);

				JPanel inputpanel = new JPanel();
				inputpanel.setLayout(new FlowLayout());
				// Buttons
				JButton btnOk = new JButton("OK");
				btnOk.addActionListener(new ActionListener()
				{
					public void actionPerformed(ActionEvent e)
					{
						if (lstData.getSelectedValue() != null
								&& lstModels.getSelectedValue() != null)
						{
							c = new Controller(
									lstModels.getSelectedValue().toString());

							c.readDataFrom(
									lstData.getSelectedValue().toString())
									.runModel()
									.runScript(scriptTxtArea.getText());

							// Do usuniecia jak table bedzie dzialac
							String res = c.getResultsAsTsv();
							dataTextArea.setText(res);
						} else
							JOptionPane.showMessageDialog(null,
									"Please select model and data");
					}
				});
				JButton btnCancel = new JButton("Cancel");
				btnCancel.addActionListener(new ActionListener()
				{
					public void actionPerformed(ActionEvent e)
					{
						jf.dispose();
					}
				});

				panel.add(sp);
				inputpanel.add(btnOk);
				inputpanel.add(btnCancel);
				panel.add(inputpanel);
				jf.getContentPane().add(BorderLayout.CENTER, panel);
				jf.setBounds(100, 100, 600, 400);
				jf.setLocationRelativeTo(btnRunScript);
				jf.setVisible(true);
			}
		});
	}
}
