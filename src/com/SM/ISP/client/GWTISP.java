package com.SM.ISP.client;

import com.SM.ISP.shared.FieldVerifier;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class GWTISP implements EntryPoint {
	/**
	 * The message displayed to the user when the server cannot be reached or
	 * returns an error.
	 */
	private static final String SERVER_ERROR = "An error occurred while "
			+ "attempting to contact the server. Please check your network "
			+ "connection and try again.";

	/**
	 * Create a remote service proxy to talk to the server-side Greeting service.
	 */
	private final GreetingServiceAsync greetingService = GWT
			.create(GreetingService.class);

	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() 
	{
		final Button loginButton = new Button("Login");
		final TextBox usernameField = new TextBox();
		usernameField.getElement().setPropertyString("placeholder", "username");
		final TextBox passwordField = new PasswordTextBox();
		passwordField.getElement().setPropertyString("placeholder", "password");
		//usernameField.setText("username");
		//passwordField.setText("password");
		final Label errorLabel = new Label();
		final Label responseLabel = new Label();

		// We can add style names to widgets
		loginButton.addStyleName("sendButton");

		// Add the nameField and sendButton to the RootPanel
		// Use RootPanel.get() to get the entire body element
		RootPanel.get().addStyleName("gwt-RootPanel");
		RootPanel.get("usernameFieldContainer").add(usernameField);
		RootPanel.get("passwordFieldContainer").add(passwordField);
		RootPanel.get("sendButtonContainer").add(loginButton);
		RootPanel.get("responseContainer").add(responseLabel);
		RootPanel.get("errorLabelContainer").add(errorLabel);

		// Focus the cursor on the name field when the app loads
		usernameField.setFocus(true);
		usernameField.selectAll();

		// Create the popup dialog box
		//final DialogBox dialogBox = new DialogBox();
		//dialogBox.setText("Remote Procedure Call");
		//dialogBox.setAnimationEnabled(true);
		//final Button closeButton = new Button("Close");
		
		// We can set the id of a widget by accessing its Element
		//closeButton.getElement().setId("closeButton");
		final Label textToServerLabel = new Label();
		final HTML serverResponseLabel = new HTML();
		VerticalPanel dialogVPanel = new VerticalPanel();
		dialogVPanel.addStyleName("dialogVPanel");
		dialogVPanel.add(new HTML("<b>Sending name to the server:</b>"));
		dialogVPanel.add(textToServerLabel);
		dialogVPanel.add(new HTML("<br><b>Server replies:</b>"));
		dialogVPanel.add(serverResponseLabel);
		dialogVPanel.setHorizontalAlignment(VerticalPanel.ALIGN_RIGHT);
		//dialogVPanel.add(closeButton);
		//dialogBox.setWidget(dialogVPanel);
		
		// Add a handler to verify login credentials
		loginButton.addClickHandler(new ClickHandler() 
		{
			public void onClick(ClickEvent event) 
			{
				loginButton.setEnabled(true);
				loginButton.setFocus(true);
			}
		});

		// Create a handler for the loginButton, usernameField, and passwordField
		class MyHandler implements ClickHandler, KeyUpHandler 
		{
			/**
			 * Fired when the user clicks on the loginButton.
			 */
			public void onClick(ClickEvent event) 
			{
				sendNameToServer();
			}

			/**
			 * Fired when the user types in the usernameField and password fields.
			 */
			public void onKeyUp(KeyUpEvent event) 
			{
				if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) 
				{
					sendNameToServer();
				}
			}

			/**
			 * Send the name from the nameField to the server and wait for a response.
			 */
			private void sendNameToServer() 
			{
	
				// First, we validate the input by querying RL server
				errorLabel.setText("");
				String usernameToServer = usernameField.getText();
				String passwordToServer = passwordField.getText();
				if (!FieldVerifier.isValidLogin(usernameToServer, passwordToServer)) 
				{
					errorLabel.setText("INVALID USERNAME OR PASSWORD");
					return;
				}
				else
				{
					responseLabel.setText("Loading...");
				}

				// Then, we send the input to the server.
				loginButton.setEnabled(false);
				textToServerLabel.setText(usernameToServer);
				serverResponseLabel.setText("");
				greetingService.greetServer(usernameToServer,
						new AsyncCallback<String>() 
						{
							public void onFailure(Throwable caught) 
							{
								// Show the RPC error message to the user
								/*
								dialogBox
										.setText("Remote Procedure Call - Failure");
								serverResponseLabel
										.addStyleName("serverResponseLabelError");
								serverResponseLabel.setHTML(SERVER_ERROR);
								dialogBox.center();
								closeButton.setFocus(true);
								*/
								//Inform of failure and re-prompt
								responseLabel.setText("Try again");
							}

							public void onSuccess(String result) 
							{
								/*
								dialogBox.setText("Remote Procedure Call");
								serverResponseLabel
										.removeStyleName("serverResponseLabelError");
								serverResponseLabel.setHTML(result);
								dialogBox.center();
								closeButton.setFocus(true);
								*/
								//Change webpage
							}
						});
			}
		}

		// Add a handler to send the name to the server
		MyHandler handler = new MyHandler();
		loginButton.addClickHandler(handler);
		usernameField.addKeyUpHandler(handler);
		passwordField.addKeyUpHandler(handler);
	}
}
