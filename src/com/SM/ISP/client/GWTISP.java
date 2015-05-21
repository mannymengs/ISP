package com.SM.ISP.client;

import com.SM.ISP.server.ISPDB;
import com.SM.ISP.shared.FieldVerifier;
import com.SM.ISP.shared.ISP;
import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.*;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class GWTISP implements EntryPoint 
{
	/**
	 * @author mannymengs
	 * This class represents an ISP cell in the cell list of projects
	 */
	public static class ISPCell extends AbstractCell<ISP> 
	{
		private String name;
		private String _topic;
		public ISPCell(ISP p)
		{
			super();
			name = p.getSf() + " " + p.getSl();
			_topic = p.getT();
		}
		
		/**
		 * 
		 */
		public void render(com.google.gwt.cell.client.Cell.Context context, ISP value, SafeHtmlBuilder sb) 
		{
			// TODO Auto-generated method stub
		}
	}

	private static final String directions = "Click on a project in the left panel to open it in this display.";
	private static final int bw = 2;
	private static final String header = "Roxbury Latin Independent Senior Project Database";

	private boolean loggedIn= false;

	private RootLayoutPanel rp = RootLayoutPanel.get();
	private Label head = new Label();
	private DockLayoutPanel sections = new DockLayoutPanel(Unit.PCT);//panel dividing the sorting section and the access section
	private HorizontalPanel sorting = new HorizontalPanel();//panel hosting sorting and searching features
	private HorizontalPanel access = new HorizontalPanel();//panel hosting project lists and display
	private VerticalPanel projects = new VerticalPanel();//panel hosting list of project
	private HorizontalPanel options = new HorizontalPanel();//panel hosting options for interacting with objects
	private VerticalPanel display = new VerticalPanel();//panel hosting display outlets

	//Create the buttons/textbox for sorting panel
	private Button home = new Button("Home (Alphabetical By First)");
	private Button last = new Button("By Last");
	private Button topic = new Button("By Topic");
	private Button year = new Button("By Year");
	private TextBox searchField = new TextBox();
	private Button search = new Button("Search");

	//Create buttons for options panel
	private Button back = new Button("Back");
	private Button add = new Button("Add");
	private Button edit = new Button("Edit");

	//Create cell list for project list panel
	private CellList<ISP> pList;

	private ISPDB db;
	
	TextArea details = new TextArea();//Create text area for details

	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() 
	{
		if(!loggedIn) login();
		/*
		Window.enableScrolling(false);
		if(loggedIn)
		{
			RootPanel.get("errorLabelContainer").add(new Label("Must be on Roxbury Latin campus and connected to school wifi"));
			return;
		}
		db = new ISPDB();
		setup();
		size();
		home();
		
		Window.addResizeHandler(new ResizeHandler()
		{
			public void onResize(ResizeEvent event) 
			{
				size();
			}
		});
		search.addClickHandler(new ClickHandler()
		{
			public void onClick(ClickEvent event)
			{
				String searchText = searchField.getText();
				db.search(searchText);
			}
		});
		*/
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
		display.add(new Label(directions));
		sections.addNorth(sorting, 4);
		sections.addWest(projects, 10);
		sections.add(display);
		projects.add(options);
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

		//Add buttons for project options to options panel
		options.add(back);
		options.add(add);
		options.add(edit);
		edit.setEnabled(false);
		back.setEnabled(false);

		//Add text area for ISP details and add to display panel
		details.setReadOnly(true);
		details.setSize(display.getOffsetWidth() + "px", display.getOffsetHeight() + "px");
		display.add(details);
		details.setText("An east weds underneath the heel. The graffito boosts a harmless spirit. The bull quota flies around a genuine ham. The bow rocket sweeps in the movie. The award relaxes over a pending individual.");
	
		//fill cell list with 
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
		RootPanel.get().add(egg, 
				Window.getClientWidth(), 
				Window.getClientHeight());
		egg.setText("Egg");
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
