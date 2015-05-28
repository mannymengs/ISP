package com.SM.ISP.client;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import com.SM.ISP.server.ISPDB;
import com.SM.ISP.shared.FieldVerifier;
import com.SM.ISP.shared.ISP;
import com.google.appengine.api.search.query.QueryParser.item_return;
import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.*;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.safehtml.client.SafeHtmlTemplates;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.google.gwt.user.client.ui.*;
import com.google.gwt.user.client.ui.FlexTable.FlexCellFormatter;
import com.google.gwt.view.client.NoSelectionModel;
import com.google.gwt.view.client.ProvidesKey;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SelectionModel;
import com.google.gwt.view.client.SingleSelectionModel;
import com.google.gwt.event.dom.client.ScrollEvent;
import com.google.gwt.event.dom.client.ScrollHandler;
import com.google.gwt.user.cellview.client.AbstractPager;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.HasRows;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class GWTISP implements EntryPoint 
{
	private DBConnectionAsync rpc;

	public GWTISP()
	{
		rpc = (DBConnectionAsync) GWT.create(DBConnection.class);
		ServiceDefTarget target = (ServiceDefTarget) rpc;
		String moduleRelativeURL = GWT.getModuleBaseURL() + "db";
		target.setServiceEntryPoint(moduleRelativeURL);
	}

	private class RPCHandler<T> implements AsyncCallback<ArrayList<ISP>> 
	{
		public void onFailure(Throwable ex) 
		{
			ex.printStackTrace();
		}
		public void onSuccess(ArrayList<ISP> result) 
		{
			db = result;
			populate();
		}
	}

	private class HomeHandler implements ClickHandler 
	{
		public void onClick(ClickEvent event) 
		{

		}
	}

	/**
	 * @author mannymengs
	 * This class represents an ISP cell in the cell list of projects
	 */
	public static class ISPCell extends AbstractCell<ISP> 
	{
		public void render(com.google.gwt.cell.client.Cell.Context context,
				ISP value, SafeHtmlBuilder sb) {
			if(value != null){
				sb.appendEscaped(value.getSf());
				sb.appendEscaped(" ");
				sb.appendEscaped(value.getSl());
			}
		}
	}

	private String output = "Names\n";

	private static final String directions = "Click on a project in the left panel to open it in this display.";
	private static final int bw = 2;
	private static final String header = "Roxbury Latin Independent Senior Project Database";

	private boolean loggedIn = false;
	private boolean loaded = false;
	private boolean success = false;
	private ArrayList<ISP> db = new ArrayList<ISP>();//array list to hold ISP objects
	private CellList<ISP> pList; //Create cell list for project list panel

	private RootLayoutPanel rp = RootLayoutPanel.get();
	private Label head = new Label();
	private DockLayoutPanel sections = new DockLayoutPanel(Unit.PCT);//panel dividing the sorting section and the access section
	private HorizontalPanel sorting = new HorizontalPanel();//panel hosting sorting and searching features
	private HorizontalPanel access = new HorizontalPanel();//panel hosting project lists and display
	private VerticalPanel projects = new VerticalPanel();//panel hosting list of project
	private VerticalPanel cell = new VerticalPanel();//panel holding cell
	private HorizontalPanel options = new HorizontalPanel();//panel hosting options for interacting with objects
	private VerticalPanel display = new VerticalPanel();//panel hosting display outlets 
	private final FlexTable flexTable = new FlexTable();//creating a flextable for the ISP display labels and textboxes
	private FlexCellFormatter cellFormatter = flexTable.getFlexCellFormatter();//creating a flexcell formatter to format the flex table

	//Create the buttons/textbox for sorting panel
	private Button home = new Button("Home (Alphabetical By First)");
	private Button last = new Button("By Last");
	private Button topic = new Button("By Topic");
	private Button year = new Button("By Year");
	private TextBox searchField = new TextBox();
	private Button search = new Button("Search");

	//Create buttons for options panel
	private Button done = new Button("Done");
	private Button add = new Button("Add");
	private Button edit = new Button("Edit");

	//Create the TextBoxes and Labels to display the project
	private TextBox studentText = new TextBox();
	private Label studentLabel = new Label();
	private TextBox topicText = new TextBox();
	private Label topicLabel = new Label();
	private TextBox contactText = new TextBox();
	private Label contactLabel = new Label();
	private TextBox orgText = new TextBox();
	private Label orgLabel = new Label();
	private TextBox org2Text = new TextBox();
	private Label org2Label = new Label();
	private TextBox addressText = new TextBox();
	private Label addressLabel = new Label();
	private TextBox cityText = new TextBox();
	private Label cityLabel = new Label();
	private TextBox stateText = new TextBox();
	private Label stateLabel = new Label();
	private TextBox zipText = new TextBox();
	private Label zipLabel = new Label();
	private TextBox onAdvisorText = new TextBox();
	private Label onAdvisorLabel = new Label();
	private TextBox offAdvisorText = new TextBox();
	private Label offAdvisorLabel = new Label();
	private TextBox advisorEmailText = new TextBox();
	private Label advisorEmailLabel = new Label();
	private TextBox advisorPhoneText = new TextBox();
	private Label advisorPhoneLabel = new Label();
	private TextArea abstractText = new TextArea();
	private Label abstractLabel = new Label();
	private TextArea reflectionText = new TextArea();
	private Label reflectionLabel = new Label();

	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() 
	{	
		Window.enableScrolling(false);
		Window.addResizeHandler(new ResizeHandler()
		{
			public void onResize(ResizeEvent event) 
			{
				size();
			}
		});

		setup();
		size();
		home();

		search.addClickHandler(new ClickHandler()
		{
			public void onClick(ClickEvent event)
			{
				String searchText = searchField.getText();
				rpc.search(searchText, new AsyncCallback<ArrayList<ISP>>()
						{
					public void onFailure(Throwable caught) 
					{
						caught.printStackTrace();
					}

					public void onSuccess(ArrayList<ISP> result) 
					{
						db = result;
					}
				});
			}
		});
		searchField.addKeyDownHandler(new KeyDownHandler()
		{
			public void onKeyDown(KeyDownEvent event) 
			{
				search.setEnabled(true);
				if(event.getNativeKeyCode() == KeyCodes.KEY_ENTER)
				{
					//db.search(searchField.getText());
					//repopulate
				}
				if(searchField.getText() == null || searchField.getText().length() < 1)
				{
					search.setEnabled(false);
				}
			}	
		});
		searchField.addKeyUpHandler(new KeyUpHandler()
		{
			public void onKeyUp(KeyUpEvent event)
			{
				if(searchField.getText().length() > 0)
				{
					search.setEnabled(true);
				}
				else if(event.getNativeKeyCode() == KeyCodes.KEY_BACKSPACE && searchField.getText().length() < 1)
				{
					search.setEnabled(false);
				}
			}	
		});
	}

	public void loadDatabase()
	{
		rpc = (DBConnectionAsync) GWT.create(DBConnection.class);
		ServiceDefTarget target = (ServiceDefTarget) rpc;
		String moduleRelativeURL = GWT.getModuleBaseURL() + "db";
		target.setServiceEntryPoint(moduleRelativeURL);

		rpc.loadDB(new AsyncCallback<Boolean>()
		{
			public void onFailure(Throwable caught) 
			{
				caught.printStackTrace();
				loaded = false;
				success = false;
			}
			public void onSuccess(Boolean result)
			{
				success = true;
				System.out.println("loadDB");
				if(result)
				{
					System.out.println("loaded");
					rpc.byFirst(new RPCHandler<ArrayList<ISP>>());
				}
				else
				{
					System.out.println("not loaded");
				}
				loaded = result;
			}
		});
	}

	public void setup()
	{
		/*
		//Setup panel layout
		head.setText(header);
		sorting.add(head);
		sections.add(sorting);
		sorting.setVerticalAlignment(HasVerticalAlignment.ALIGN_TOP);
		sections.add(access);
		access.add(projects);
		access.add(display);
		projects.add(options);
		rp.add(sections);//Add sections to root
		 */

		head.setText(header);
		sorting.add(head);
		//display.add(new Label(directions));
		sections.addNorth(sorting, 5);
		//sections.addWest(projects, 10);
		sections.addWest(cell, 11);
		//sections.add(display);
		//projects.add(options);
		rp.add(sections);
	}

	public void size() 
	{
		/*
		sections.setSize("100%", "100%");
		access.setSize("100%", "100%");
		projects.setSize(back.getOffsetWidth() + add.getOffsetWidth() + edit.getOffsetWidth() + "", "100%");
		details.setSize("100%", "100%");
		 */
	}

	private void populate()
	{
		//Provides a unique key identifier for each object of ISP in the Cell
		ProvidesKey<ISP> keyProvider = new ProvidesKey<ISP>()
		{
			public Object getKey(ISP item)
			{
				return (item==null) ? null : item.getKey();
			}
		};

		//Creates a new CellList with a keyProvider defined above
		pList = new CellList<ISP>(new ISPCell(), keyProvider);

		//Setting the CellList's rows and data
		pList.setRowCount(db.size(), true);
		pList.setRowData(0, db);

		//final SelectionModel<ISP> selectionModel = new NoSelectionModel<ISP>(keyProvider);

		final SingleSelectionModel<ISP> singleSelectionModel = new SingleSelectionModel<ISP>(keyProvider);
		singleSelectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() 
		{
			@Override
			public void onSelectionChange(SelectionChangeEvent event) {
				ISP placeHolder = singleSelectionModel.getSelectedObject();
				studentText.setText(placeHolder.getSf() + " " + placeHolder.getSl());
				if(placeHolder.getT()!= null) topicText.setText(placeHolder.getT());
				else topicText.setText("N/A");
				if(placeHolder.getO1()!= null) orgText.setText(placeHolder.getO1());
				else orgText.setText("N/A");
				if(placeHolder.getO2()!= null) org2Text.setText(placeHolder.getO2());
				else org2Text.setText("N/A");
				if(placeHolder.getoA()!= null) addressText.setText(placeHolder.getoA());
				else addressText.setText("N/A");
				if(placeHolder.getoC()!= null) cityText.setText(placeHolder.getoC());
				else cityText.setText("N/A");
				if(placeHolder.getoS()!= null) stateText.setText(placeHolder.getoS());
				else stateText.setText("N/A");
				if(placeHolder.getoZ()!= null) zipText.setText(placeHolder.getoZ());
				else zipText.setText("N/A");
				if(placeHolder.getAn()!= null) onAdvisorText.setText(placeHolder.getAn());
				else onAdvisorText.setText("N/A");
				if(placeHolder.getCt()!= null) offAdvisorText.setText(placeHolder.getCt() + " " + placeHolder.getCf() + " " + placeHolder.getCl());
				else offAdvisorText.setText("N/A");
				if(placeHolder.getCe()!= null) advisorEmailText.setText(placeHolder.getCe());
				else advisorEmailText.setText("N/A");
				if(placeHolder.getCp()!= null) advisorPhoneText.setText(placeHolder.getCp());
				else advisorPhoneText.setText("N/A");
				if(placeHolder.getA()!= null) abstractText.setText(placeHolder.getA());
				else abstractText.setText("N/A");
				if(placeHolder.getR()!= null) reflectionText.setText(placeHolder.getR());
				else reflectionText.setText("N/A");
			}
		});
		ISP first = db.get(0);
		singleSelectionModel.setSelected(first, true);
		pList.setSelectionModel(singleSelectionModel);//Using the KeyProvider defined above, a selection model is created

		edit.addClickHandler(new ClickHandler()
		{
			@Override
			public void onClick(ClickEvent event) 
			{
				studentText.setEnabled(true);
				topicText.setEnabled(true);
				orgText.setEnabled(true);
				org2Text.setEnabled(true);
				addressText.setEnabled(true);
				cityText.setEnabled(true);
				stateText.setEnabled(true);
				zipText.setEnabled(true);
				onAdvisorText.setEnabled(true);
				offAdvisorText.setEnabled(true);
				advisorEmailText.setEnabled(true);
				advisorPhoneText.setEnabled(true);
				abstractText.setEnabled(true);
				reflectionText.setEnabled(true);
				done.setEnabled(true);
			}
		});

		done.addClickHandler(new ClickHandler()
		{
			@Override
			public void onClick(ClickEvent event) 
			{
				studentText.setEnabled(false);
				topicText.setEnabled(false);
				orgText.setEnabled(false);
				org2Text.setEnabled(false);
				addressText.setEnabled(false);
				cityText.setEnabled(false);
				stateText.setEnabled(false);
				zipText.setEnabled(false);
				onAdvisorText.setEnabled(false);
				offAdvisorText.setEnabled(false);
				advisorEmailText.setEnabled(false);
				advisorPhoneText.setEnabled(false);
				abstractText.setEnabled(false);
				reflectionText.setEnabled(false);
				done.setEnabled(false);
			}
		});

		//Add buttons for project options to options panel
		options.add(add);
		options.add(edit);
		options.add(done);
		cell.add(options);
		cell.add(pList);
		cell.setBorderWidth(1);
		edit.setEnabled(true);
		done.setEnabled(false);

		//Setting the Label's text
		studentLabel.setText("Student:");
		topicLabel.setText("Topic:");
		orgLabel.setText("Primary Organization:");
		org2Label.setText("Secondary Organization:");
		addressLabel.setText("Address:");
		cityLabel.setText("City:");
		stateLabel.setText("State:");
		zipLabel.setText("Zip Code:");
		onAdvisorLabel.setText("On-Campus Advisor:");
		offAdvisorLabel.setText("Off-Campus Advisor:");
		advisorEmailLabel.setText("Off-Campus Advisor Email:");
		advisorPhoneLabel.setText("Off-Campus Advisor #:");
		abstractLabel.setText("Abstract:");
		reflectionLabel.setText("Reflection:");

		//formating the flextable's width, style and padding
		flexTable.addStyleName("flexTable");
		flexTable.setWidth("400px");
		flexTable.setCellPadding(3);

		//Adding all the labels and textboxes to the flexTable
		flexTable.setWidget(0, 0, studentLabel);
		flexTable.setWidget(0, 1, studentText);
		flexTable.setWidget(1, 0, topicLabel);
		flexTable.setWidget(1, 1, topicText);
		flexTable.setWidget(2, 0, orgLabel);
		flexTable.setWidget(2, 1, orgText);
		flexTable.setWidget(3, 0, org2Label);
		flexTable.setWidget(3, 1, org2Text);
		flexTable.setWidget(4, 0, addressLabel);
		flexTable.setWidget(4, 1, addressText);
		flexTable.setWidget(5, 0, cityLabel);
		flexTable.setWidget(5, 1, cityText);
		flexTable.setWidget(6, 0, stateLabel);
		flexTable.setWidget(6, 1, stateText);
		flexTable.setWidget(7, 0, zipLabel);
		flexTable.setWidget(7, 1, zipText);
		flexTable.setWidget(8, 0, onAdvisorLabel);
		flexTable.setWidget(8, 1, onAdvisorText);
		flexTable.setWidget(9, 0, offAdvisorLabel);
		flexTable.setWidget(9, 1, offAdvisorText);
		flexTable.setWidget(10, 0, advisorEmailLabel);
		flexTable.setWidget(10, 1, advisorEmailText);
		flexTable.setWidget(11, 0, advisorPhoneLabel);
		flexTable.setWidget(11, 1, advisorPhoneText);
		flexTable.setWidget(12, 0, abstractLabel);
		flexTable.setWidget(12, 1, abstractText);
		flexTable.setWidget(13, 0, reflectionLabel);
		flexTable.setWidget(13, 1, reflectionText);

		//disabling the textboxes from having input
		studentText.setEnabled(false);
		topicText.setEnabled(false);
		orgText.setEnabled(false);
		org2Text.setEnabled(false);
		addressText.setEnabled(false);
		cityText.setEnabled(false);
		stateText.setEnabled(false);
		zipText.setEnabled(false);
		onAdvisorText.setEnabled(false);
		offAdvisorText.setEnabled(false);
		advisorEmailText.setEnabled(false);
		advisorPhoneText.setEnabled(false);
		abstractText.setEnabled(false);
		reflectionText.setEnabled(false);

		studentText.setVisibleLength(25);
		topicText.setVisibleLength(25);
		orgText.setVisibleLength(25);
		org2Text.setVisibleLength(25);
		addressText.setVisibleLength(25);
		cityText.setVisibleLength(25);
		stateText.setVisibleLength(25);
		zipText.setVisibleLength(25);
		onAdvisorText.setVisibleLength(25);
		offAdvisorText.setVisibleLength(25);
		advisorEmailText.setVisibleLength(25);
		advisorPhoneText.setVisibleLength(25);
		abstractText.setCharacterWidth(90);
		abstractText.setVisibleLines(8);
		reflectionText.setCharacterWidth(90);
		reflectionText.setVisibleLines(8);

		//Adding the flexTable to sections
		sections.add(flexTable);
	}
	
	private void home() 
	{
		//Add borders
		sorting.setStylePrimaryName("sortingPanel");
		access.setStylePrimaryName("accessPanel");
		projects.setStylePrimaryName("projectsPanel");
		options.setStylePrimaryName("optionsPanel");
		display.setStylePrimaryName("displayPanel");
		sorting.setBorderWidth(bw);
		access.setBorderWidth(bw);
		projects.setBorderWidth(bw);
		options.setBorderWidth(bw);
		display.setBorderWidth(bw);

		//Add buttons/search fields for sorting to sorting panel
		search.getElement().setPropertyString("placeholder", "search");//sets background text of search field
		sorting.add(home);
		sorting.add(last);
		sorting.add(topic);
		sorting.add(year);
		sorting.add(searchField);
		sorting.add(search);
		home.setEnabled(false);
		search.setEnabled(false);
		
		loadDatabase();
	}

	public boolean login()
	{
		String instructions = "Login in with @roxburylatin.org credentials to access the Roxbury Latin ISP Database";
		final Label instruction = new Label(instructions);
		final Button loginButton = new Button("Login");
		final TextBox usernameField = new TextBox();
		usernameField.getElement().setPropertyString("placeholder", "username");
		final TextBox passwordField = new PasswordTextBox();
		passwordField.getElement().setPropertyString("placeholder", "password");
		//usernameField.setText("username");
		//passwordField.setText("password");
		final Label errorLabel = new Label();
		final Label responseLabel = new Label();
		final Label egg = new Label();

		// We can add style names to widgets
		loginButton.addStyleName("sendButton");

		// Add the nameField and sendButton to the RootPanel
		// Use RootPanel.get() to get the entire body element
		RootPanel.get().addStyleName("gwt-RootPanel");
		RootPanel.get("instructionContainer").add(instruction);
		RootPanel.get("usernameFieldContainer").add(usernameField);
		RootPanel.get("passwordFieldContainer").add(passwordField);
		RootPanel.get("sendButtonContainer").add(loginButton);
		RootPanel.get("responseContainer").add(responseLabel);
		RootPanel.get("errorLabelContainer").add(errorLabel);
		//RootPanel.get().add(egg, Window.getClientWidth(), Window.getClientHeight());
		//RootPanel.get().add(new Image("https://media.licdn.com/media/p/2/000/036/32c/37bc12a.png"));
		//egg.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);

		// Focus the cursor on the name field when the app loads
		usernameField.setFocus(true);

		// Add a handler to verify login credentials

		// Create a handler for the loginButton, usernameField, and passwordField
		class MyHandler implements ClickHandler, KeyUpHandler, MouseOverHandler, MouseOutHandler, KeyDownHandler
		{
			public void onMouseOver(MouseOverEvent event) 
			{
				egg.setText("Jonah sucks");
			}

			public void onMouseOut(MouseOutEvent event)
			{
				egg.setText("");
			}

			public void onClick(ClickEvent event) 
			{
				loginButton.setEnabled(true);
				loginButton.setFocus(true);
				verifyLogin();
			}
			public void onKeyUp(KeyUpEvent event) 
			{
				if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) 
				{
					verifyLogin();
				}
			}
			private void verifyLogin()
			{
				// First, we validate the input by querying RL server
				String usernameToServer = usernameField.getText();
				String passwordToServer = passwordField.getText();
				if (!FieldVerifier.isValidLogin(usernameToServer, passwordToServer)) 
				{
					responseLabel.setText("Try again.");
					errorLabel.setText("INVALID USERNAME OR PASSWORD");
					usernameField.setFocus(true);
					usernameField.selectAll();
				}
				else
				{
					responseLabel.setText("Loading...");
					loggedIn = true;
				}
			}
			public void onKeyDown(KeyDownEvent event)
			{
				errorLabel.setText("");
				responseLabel.setText("");
				egg.setText("");
			}
		}

		// Add a handler to send the name to the server
		MyHandler handler = new MyHandler();
		loginButton.addMouseOverHandler(handler);
		loginButton.addMouseOutHandler(handler);
		loginButton.addClickHandler(handler);
		usernameField.addKeyUpHandler(handler);
		passwordField.addKeyUpHandler(handler);
		usernameField.addKeyDownHandler(handler);
		passwordField.addKeyDownHandler(handler);

		return loggedIn;
	}
}