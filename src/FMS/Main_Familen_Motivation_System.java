package FMS;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfWriter;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Tooltip;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;


public class Main_Familen_Motivation_System extends Application {

	Familymembers logedIn;
	Button login = new Button("Login");
	ScrollPane baseTableView = new ScrollPane();
	FlowPane basePerson = new FlowPane();
	FlowPane baseChallenge = new FlowPane();
	FlowPane baseReward = new FlowPane();
	Label showPoint = new Label();
	Stage baseStage = new Stage();
	

	@Override
	public void start(Stage primaryStage) {
		baseStage = primaryStage;
		// set the startsite - 1. Abbildung: Startseite

		Label mainTitel = new Label("Familien Motivation System");
		mainTitel.setId("Titels");
		
		// set base for person button
		basePerson.setHgap(30);
		basePerson.setVgap(30);
		basePerson.setPrefSize(500, 100);
		
		basePerson.setAlignment(Pos.CENTER);
		basePerson.setPadding(new Insets(15,0,15,0));
		basePerson.setId("vbox");

		makePersonButtons(basePerson);
		
		ScrollPane baseScroll = new ScrollPane(basePerson);
		baseScroll.setId("vbox");
		baseScroll.setFitToWidth(true);
		baseScroll.setFitToHeight(true);
		baseScroll.setBackground(new Background(new BackgroundFill(Color.LIGHTBLUE, null, null)));
		baseScroll.vbarPolicyProperty().setValue(ScrollBarPolicy.AS_NEEDED);
		baseScroll.hbarPolicyProperty().setValue(ScrollBarPolicy.NEVER);

		// set the wish list, login and registration buttons
		Button wishList = new Button("Wunschliste");
		wishList.setMaxWidth(500);
		wishList.setId("buttonBig");

		Button registration = new Button("Familienmitglieder anlegen");
		registration.setId("buttonBig");
		login.setId("buttonBig");

		// set the base for login and registration
		HBox baseLogAndReg = new HBox();
		baseLogAndReg.getChildren().addAll(login, registration);
		baseLogAndReg.setSpacing(80);
		baseLogAndReg.setPadding(new Insets(0, 0, 25, 0));
		baseLogAndReg.setAlignment(Pos.TOP_CENTER);

		// set base for the startsite
		VBox baseStartSite = new VBox();
		baseStartSite.setId("vbox");
		baseStartSite.getStylesheets().add("Style.css");
		baseStartSite.getChildren().addAll(mainTitel, baseScroll, wishList, baseLogAndReg);
		baseStartSite.setSpacing(50);
		baseStartSite.setAlignment(Pos.CENTER);
		baseStartSite.setPadding(new Insets(15, 15, 15, 15));
		baseStartSite.setBackground(new Background(new BackgroundFill(Color.LIGHTBLUE, null, null)));

		// set the starter page stage
		primaryStage.setScene(new Scene(baseStartSite));
		primaryStage.setTitle("Startseite");
		primaryStage.show();

		// set wish list panel
		wishList.setOnAction(j -> {
			showWishListPanel();
		});

		// set registration panel
		registration.setOnAction(g -> {
			showRegistrationPanel();
		});

		// set login panel
		login.setOnAction(h -> {
			showLoginPanel();
		});
	}

	
	public static void main(String[] args) {
		System.out.println("FMS ist gestartet: ");
		//try to create person challenge and reward table
		try {
			Database.createPersons();
			System.out.println("Persons Tabelle erzeugt oder schon vorhanden.");
		} catch (SQLException e) {
			System.out.println(e);
			e.printStackTrace();
		}
		try {
			Database.createReward();
			System.out.println("Belohnungen Tabelle erzeugt oder schon vorhanden.");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		try {
			Database.createChallenge();
		} catch (SQLException e) {
			e.printStackTrace();
			}
			System.out.println("Herausforderungen Tabelle erzeugt oder schon vorhanden.");
		
		launch(args);
	}

	
	// set stage for wishlist page - 5. Abbildung: Wunschliste
	private void showWishListPanel() {
		
		//set the elements of the stage
		makeWishListTable(baseTableView);
		
		Label ownerName = new Label("Person:");
		TextField writeOwnerName = new TextField();
		writeOwnerName.setId("field");
		writeOwnerName.setTooltip(new Tooltip("Name von registrierten Benutzer angeben."));
		writeOwnerName.setMinHeight(45);

		Label rewardName = new Label("Geschenk:");
		TextField writeRewardName = new TextField();
		writeRewardName.setId("field");
		writeRewardName.setTooltip(new Tooltip("Name der Belohnung\nMindestens 3 Buchstaben!"));
		writeRewardName.setMinHeight(45);

		Label pointReward = new Label("Punktzahl:");
		TextField writePoint = new TextField();
		writePoint.setId("field");
		writePoint.setTooltip(new Tooltip("Punktwert der Belohnung\nBitte positiven ganzen Zahl angeben"));
		writePoint.setMinHeight(45);
		
		Label error = new Label();
		error.setVisible(false);

		Button saveReward = new Button("Speichern");
		Button cancelReward = new Button("Entfernen");
		

		HBox savAndCancelReward = new HBox();
		savAndCancelReward.getChildren().addAll(saveReward, cancelReward);
		savAndCancelReward.setSpacing(50);

		VBox baseWishList = new VBox();
		baseWishList.getStylesheets().add("Style.css");
		baseWishList.setId("vbox");
		baseWishList.getChildren().addAll(baseTableView, ownerName, writeOwnerName, rewardName, writeRewardName, pointReward,
				writePoint, error, savAndCancelReward);
		baseWishList.setSpacing(10);
		baseWishList.setPadding(new Insets(15, 15, 15, 15));

		Stage wishListStage = new Stage();
		wishListStage.setScene(new Scene(baseWishList));
		wishListStage.setTitle("Wunschliste");
		wishListStage.initModality(Modality.APPLICATION_MODAL);
		wishListStage.show();

		//set cancel button
		cancelReward.setOnAction(e -> {
			writeOwnerName.setText(null);
			writeRewardName.setText(null);
			writePoint.setText(null);
			error.setText(null);
			error.setVisible(false);
		});
		//set save button details
		saveReward.setOnAction(e-> {
			
			int PointRewardValue = 0;

			try {
				PointRewardValue = Integer.parseInt(writePoint.getText());
			} catch (NumberFormatException g) {
			}
			
			ArrayList<Familymembers> listMembers = null;

			try {
				listMembers = Database.readFamilymembers();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			//check if the person from TextField is a registered user
			Familymembers member = null;
			for(Familymembers f : listMembers) {
				if(f.getPersonName().equals(writeOwnerName.getText())) {
					member = f;
				} 
			}
			//check if there are characters in the TextFields
			if (writeOwnerName.getText().length() != 0 && writeRewardName.getText().length() != 0
					&& writePoint.getText().length() != 0) {
				
				//check the length of the person and reward name
				if (writeOwnerName.getText().length() >= 3 && writeRewardName.getText().length() >= 3) {
					//check if integer is in the Point field
					if (PointRewardValue != 0) {
						if (member != null) {
							if(logedIn!=null && logedIn.isAdministrator()) {
								try {
									Database.insertReward(new Reward(1, writeRewardName.getText(), member.getPersonID(),
											member.getPersonName(), PointRewardValue, false));
									makeRewardButtons(baseReward, member);
									makeWishListTable(baseTableView);
									showInfos(writeRewardName.getText() + " wurde angelegt");
									writeOwnerName.setText(null);
									writeRewardName.setText(null);
									writePoint.setText(null);
									

								} catch (SQLException g) {
									g.printStackTrace();
								}
								try {
									member.setReawardList(Database.readRewards(member));
								} catch (SQLException g) {
									g.printStackTrace();
								}

							} else {
								error.setText("Bitte log in");
								error.setVisible(true);
							}
						
						} else {
							error.setText("Unbekannte Person");
							error.setVisible(true);
						}
					} else {
						error.setText("Feld 3: bitte nur Zahlen");
						error.setVisible(true);
					}
				} else {
					error.setText("Feld 1 oder 2: minimum 3 Zeichen");
					error.setVisible(true);
				}
			} else {
				error.setText("Bitte all Felder ausfühllen");
				error.setVisible(true);
			}
		});
		
	}

	
	//create the TableView to the wishListPanel
	@SuppressWarnings("unchecked")
	private void makeWishListTable(ScrollPane baseTableView) {
		
		//create the element of the table
		ObservableList<RewardFX> rewardList = FXCollections.observableArrayList();
		rewardList.addAll(createRewardFXList());
		
//		baseTableView.getChildren().clear();
		baseTableView.setContent(null);

		TableColumn<RewardFX, String> ownerNameCol = new TableColumn<>("Person");
		ownerNameCol.setMinWidth(100);
		ownerNameCol.setCellValueFactory(new PropertyValueFactory<>("ownerName"));
		TableColumn<RewardFX, String> nameCol = new TableColumn<>("Geschenk");
		nameCol.setMinWidth(150);
		nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
		TableColumn<RewardFX, Integer> valueCol = new TableColumn<>("Punkt");
		valueCol.setMinWidth(100);
		valueCol.setCellValueFactory(new PropertyValueFactory<>("point"));

		TableView<RewardFX> tv = new TableView<>(rewardList);

		tv.getColumns().addAll(ownerNameCol, nameCol, valueCol);
		tv.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
		tv.setBackground(new Background(new BackgroundFill(Color.LIGHTBLUE, null, null)));
		tv.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
		
		baseTableView.setContent(tv);
		baseTableView.setFitToWidth(true);
		baseTableView.setPrefHeight(200);
		baseTableView.vbarPolicyProperty().setValue(ScrollBarPolicy.ALWAYS);
		baseTableView.hbarPolicyProperty().setValue(ScrollBarPolicy.NEVER);
		
	}
	
	
	// create ArrayList<RewardFX>
	private ArrayList<RewardFX> createRewardFXList() {
		ArrayList<RewardFX> rewardFXList = new ArrayList<>();
		ArrayList<Reward> readRewardList = new ArrayList<>();
		try {
			readRewardList = Database.readAllRewards();
			for (Reward r : readRewardList) {
				rewardFXList.add(new RewardFX(r));
			}
		} catch (SQLException e) {
		}
		return rewardFXList;
	}

	
	//create personal ArrayList<RewardFX>
	private ArrayList<RewardFX> createPersonalRewardFXList(Familymembers m) {
		ArrayList<RewardFX> rewardFXPersonList = new ArrayList<>();
		ArrayList<Reward> readRewardPersonList = new ArrayList<>();
		try {
			readRewardPersonList = Database.readRewards(m);
			
		} catch (SQLException e) {
		}
		
		for (Reward r : readRewardPersonList) {
				rewardFXPersonList.add(new RewardFX(r));
		}
		return rewardFXPersonList;
	}
	
	
	//create personal ArrayList<ChallangeFX>
	private ArrayList<ChallengeFX> createPersonalChallengeFXList(Familymembers m){
		ArrayList<ChallengeFX> challengeFXPersonList = new ArrayList<>();
		ArrayList<Challenge> readChallengePersonList = new ArrayList<>();
		try {
			readChallengePersonList = Database.readChallenges(m);
			
		} catch (SQLException e) {
		}
		for (Challenge c : readChallengePersonList) {
				challengeFXPersonList.add(new ChallengeFX(c));
		}
		return challengeFXPersonList;
	}
	
	
	// set persons button
	private void makePersonButtons(FlowPane basePerson) {
		ArrayList<Familymembers> listMembers = null;

		//set the shape of the buttons
		Circle circlePerson = new Circle();
		circlePerson.setRadius(50.0);

		basePerson.getChildren().clear();

		try {
			listMembers = Database.readFamilymembers();
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		for (Familymembers f : listMembers) {
				Button setPerson = new Button(f.getPersonName());
				setPerson.setShape(circlePerson);
				setPerson.setId("buttonBig");
				basePerson.getChildren().add(setPerson);
			
			//check if the user is loged in, and he/she is an administrator 	
			if (logedIn != null && logedIn.isAdministrator()) {
				setPerson.setOnMouseClicked(e -> {
					if (e.getButton() == MouseButton.PRIMARY) {
						showPersonalPanel(f);
					}
					if (e.getButton() == MouseButton.SECONDARY) {
						confirmDeletePerson(f);
					}
				});
			}	
			else {
				if (logedIn == null) {
					setPerson.setOnMouseClicked(e->{
						if (e.getButton() == MouseButton.PRIMARY) {
							showPersonalPanel(f);
						}
						if (e.getButton() == MouseButton.SECONDARY) {
							showInfos("Bitte login");
						}
					});
				}
				else {
					setPerson.setOnMouseClicked(e->{
						if (e.getButton() == MouseButton.PRIMARY) {
							showPersonalPanel(f);
						}
						if (e.getButton() == MouseButton.SECONDARY) {
							showInfos("Nur für Eltern");
						}
					});
				}
			}
		}	
	}

	
	// set pages for personal panels - 3. Abbildung: Personal Profil
	private void showPersonalPanel(Familymembers member) {
		// set the elements of the personal profil site
		Label personName = new Label(member.getPersonName());
		personName.setId("Titels");

		HBox baseName = new HBox(personName);
		baseName.setAlignment(Pos.CENTER);
		
		Label actualPoint = new Label("Aktuelle Punktzahl:");
		actualPoint.setId("labelBig");
		showPoint.setId("labelBig");
		showPoint.setText("     " + member.getActualPoint() +"     ");
		HBox basePoint = new HBox(actualPoint, showPoint);
		basePoint.setAlignment(Pos.CENTER);
		basePoint.setSpacing(260);

		Label titelChallenge = new Label("Herausforderungen:");
		titelChallenge.setId("labelBig");
		Label titelReward = new Label("Belohnungen:");
		titelReward.setId("labelBig");
		HBox baseTitelOfMotivationElement = new HBox(titelChallenge, titelReward);
		baseTitelOfMotivationElement.setAlignment(Pos.CENTER);
		baseTitelOfMotivationElement.setSpacing(200);

		// set buttons and base for challenges
		baseChallenge.setVgap(15);
		baseChallenge.setHgap(20);
		makeChallangeButtons(baseChallenge, member);

		// set buttons and base for rewards
		baseReward.setVgap(15);
		baseReward.setHgap(20);
		makeRewardButtons(baseReward, member);

		HBox baseMotivationElement = new HBox(baseChallenge, baseReward);
		baseMotivationElement.setId("vbox");
		baseMotivationElement.setPadding(new Insets(15));
		baseMotivationElement.setAlignment(Pos.CENTER);
		baseMotivationElement.setPrefSize(800, 150);

		ScrollPane baseScrollbar = new ScrollPane();
		baseScrollbar.setContent(baseMotivationElement);
		baseScrollbar.setFitToWidth(true);
		baseScrollbar.setFitToHeight(true);
		baseScrollbar.vbarPolicyProperty().setValue(ScrollBarPolicy.ALWAYS);
		baseScrollbar.hbarPolicyProperty().setValue(ScrollBarPolicy.NEVER);

		Button own = new Button("Eigenen Herausforderungen und Belohnung");
		own.setPrefWidth(750);

		Button insertChallenge = new Button("Herausforderung anlegen");
		Button insertReward = new Button("Belohnung anlegen");

		HBox baseInsertDelete = new HBox(insertChallenge, insertReward);
		baseInsertDelete.setSpacing(40);
		baseInsertDelete.setAlignment(Pos.CENTER);

		VBox basePersonalProfil = new VBox();
		basePersonalProfil.getChildren().addAll(baseName, basePoint,baseTitelOfMotivationElement,
				baseScrollbar, own, baseInsertDelete);
		basePersonalProfil.setId("vbox");
		basePersonalProfil.getStylesheets().add("Style.css");
		basePersonalProfil.setSpacing(20);
		basePersonalProfil.setAlignment(Pos.CENTER);
		basePersonalProfil.setPadding(new Insets(15, 15, 25, 15));

		// set insert challenge panel
		if (logedIn != null) {
			if(logedIn.isAdministrator()) {
				insertChallenge.setOnAction(e -> {
					showInsertChallengePanel(member);
				});
			} else {
				insertChallenge.setOnAction(e -> {
					showInfos("Nur für Eltern");
				});
			}
			
		} else {
			insertChallenge.setOnAction(e -> {
				showInfos("Bitte login");
			});
			
		}
		
		// set insert reward panel
		if (logedIn != null) {
			if(logedIn.isAdministrator()) {
				insertReward.setOnAction(e -> {
					showInsertRewardPanel(member);
				});
			} else {
				insertReward.setOnAction(e -> {
					showInfos("Nur für Eltern");
				});
			}
			
		} else {
			insertReward.setOnAction(e -> {
				showInfos("Bitte login");
			});
			
		}
		
		//set TableView for personal Challenges and Rewards
		own.setOnAction(e->{
			showPersonalChallengeAndRewardPanel(member);
		});
		

		Stage personalProfilStage = new Stage();
		personalProfilStage.setScene(new Scene(basePersonalProfil));
		personalProfilStage.setTitle("Personal Profil für " + member.getPersonName());
		personalProfilStage.show();

	}

	
	//make personal challenge- and reward table
	@SuppressWarnings("unchecked")
	private void showPersonalChallengeAndRewardPanel(Familymembers f) {
		
		//set the elements of the stage
		Label rewards = new Label("Belohnungen");
		Label challenges = new Label("Herausforderungen");
		ObservableList<RewardFX> rewardList = FXCollections.observableArrayList();
		rewardList.addAll(createPersonalRewardFXList(f));

		//create table for personal rewards
		TableColumn<RewardFX, String> ownerNameCol = new TableColumn<>("Person");
		ownerNameCol.setMinWidth(100);
		ownerNameCol.setCellValueFactory(new PropertyValueFactory<>("ownerName"));
		TableColumn<RewardFX, String> nameCol = new TableColumn<>("Name");
		nameCol.setMinWidth(100);
		nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
		TableColumn<RewardFX, Integer> valueCol = new TableColumn<>("Punkt");
		valueCol.setMinWidth(100);
		valueCol.setCellValueFactory(new PropertyValueFactory<>("point"));
		TableColumn<RewardFX, Boolean> fullfilledCol = new TableColumn<RewardFX, Boolean>("Status");
		fullfilledCol.setMinWidth(100);
		fullfilledCol.setCellValueFactory(e->e.getValue().isFullfieldBooleanProperty());
		
		fullfilledCol.setCellFactory(e->  new TableCell<RewardFX, Boolean>() {
			@Override
		    protected void updateItem(Boolean item, boolean empty) {
		        super.updateItem(item, empty) ;
		        setText(empty ? null : item ? "erfüllt" : "unerfüllt" );
		    }
		});

		TableView<RewardFX> tvreward = new TableView<RewardFX>(rewardList);
		tvreward.getColumns().addAll(ownerNameCol, nameCol, valueCol, fullfilledCol);
		tvreward.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
		tvreward.setBackground(new Background(new BackgroundFill(Color.LIGHTBLUE, null, null)));
		tvreward.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
		
		ObservableList<ChallengeFX> challengeList = FXCollections.observableArrayList();
		challengeList.addAll(createPersonalChallengeFXList(f));
		
		
		//create table for personal challenges
		TableColumn<ChallengeFX, String> challengeOwnerNameCol = new TableColumn<>("Person");
		challengeOwnerNameCol.setMinWidth(100);
		challengeOwnerNameCol.setCellValueFactory(new PropertyValueFactory<>("ownerName"));
		TableColumn<ChallengeFX, String> challengeNameCol = new TableColumn<>("Name");
		challengeNameCol.setMinWidth(100);
		challengeNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
		TableColumn<ChallengeFX, Integer> challengeValueCol = new TableColumn<>("Punkt");
		challengeValueCol.setMinWidth(100);
		challengeValueCol.setCellValueFactory(new PropertyValueFactory<>("point"));
		TableColumn<ChallengeFX, Boolean> challengeFullfilledCol = new TableColumn<ChallengeFX, Boolean>("Status");
		challengeFullfilledCol.setMinWidth(100);
		
		challengeFullfilledCol.setCellValueFactory(e->e.getValue().isFullfieldBooleanProperty());
		
		challengeFullfilledCol.setCellFactory(e->  new TableCell<ChallengeFX,Boolean>() {
			@Override
		    protected void updateItem(Boolean item, boolean empty) {
		        super.updateItem(item, empty) ;
		        setText(empty ? null : item ? "erfüllt" : "unerfüllt" );
		    }
		});
			
		
		TableView<ChallengeFX> tvchallenge = new TableView<ChallengeFX>(challengeList);
		tvchallenge.getColumns().addAll(challengeOwnerNameCol,challengeNameCol,challengeValueCol,challengeFullfilledCol);
		tvchallenge.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
		tvchallenge.setBackground(new Background(new BackgroundFill(Color.LIGHTBLUE, null, null)));
		tvchallenge.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
		
		ScrollPane challengsScrollPane = new ScrollPane();
		challengsScrollPane.setContent(tvchallenge);
		challengsScrollPane.setFitToWidth(true);
		challengsScrollPane.setPrefHeight(200);
		challengsScrollPane.vbarPolicyProperty().setValue(ScrollBarPolicy.ALWAYS);
		challengsScrollPane.hbarPolicyProperty().setValue(ScrollBarPolicy.NEVER);
		
		ScrollPane rewardsScrollPane = new ScrollPane();
		rewardsScrollPane.setContent(tvreward);
		rewardsScrollPane.setFitToWidth(true);
		rewardsScrollPane.setPrefHeight(200);
		rewardsScrollPane.vbarPolicyProperty().setValue(ScrollBarPolicy.ALWAYS);
		rewardsScrollPane.hbarPolicyProperty().setValue(ScrollBarPolicy.NEVER);
		
		Button back = new Button("Zurück");
		
		VBox basechallengeAndRewardTabel = new VBox();
		basechallengeAndRewardTabel.getChildren().addAll(challenges,challengsScrollPane,rewards,rewardsScrollPane,back);
		basechallengeAndRewardTabel.setId("vbox");
		basechallengeAndRewardTabel.getStylesheets().add("Style.css");
		basechallengeAndRewardTabel.setSpacing(15);
		basechallengeAndRewardTabel.setPadding(new Insets(15));
		basechallengeAndRewardTabel.setAlignment(Pos.CENTER);
		
		Stage stageChallengeAndRewardTabel = new Stage();
		stageChallengeAndRewardTabel.setScene(new Scene(basechallengeAndRewardTabel));
		stageChallengeAndRewardTabel.setTitle(f.getPersonName() + "'s Herausforderungen und Belohnungen");
		stageChallengeAndRewardTabel.initModality(Modality.APPLICATION_MODAL);
		stageChallengeAndRewardTabel.show();	
		
		back.setOnAction(e->{
			stageChallengeAndRewardTabel.close();
		});
	}
	
	
	// make challenge buttons
	private void makeChallangeButtons(FlowPane baseChallenge, Familymembers member) {
		
		ArrayList<Challenge> personalChallengeList = new ArrayList<>();
		ArrayList<Challenge> personalChallengeListNotFullfield = new ArrayList<>();

		baseChallenge.getChildren().clear();
		

		try {
			personalChallengeList = Database.readChallenges(member);
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		//filter all the challenges, which are not fulfilled
		for(Challenge challange :personalChallengeList) {
			if(!challange.isFullfield()) {
				personalChallengeListNotFullfield.add(challange);
			}
		}
		//check if there is any not fulfilled challenge
		if (personalChallengeListNotFullfield.size() != 0) {
			for (Challenge c : personalChallengeListNotFullfield) {
					Button setChallenge = new Button(c.getName());
					setChallenge.setMinWidth(350);
					setChallenge.setTooltip(new Tooltip(c.getActualPoint() + "/" + c.getPoint()*c.getFrequency()));
					//make progress bar from setChallenge buttons
					setChallenge.setId(setButtonRange((c.getPoint()*c.getFrequency()),c.getActualPoint()));
					setChallenge.getStylesheets().add("Style.css");
					baseChallenge.getChildren().add(setChallenge);

					//check if the user is logedin, and he/she is an administrator
					if (logedIn != null && logedIn.isAdministrator()) {

						setChallenge.setOnMouseClicked(e ->{
							//give new challenge point by left click
							if (e.getButton() == MouseButton.PRIMARY) {
								member.setActualPoint(member.getActualPoint() + c.getPoint());
								c.setActualPoint(c.getActualPoint()+c.getPoint());
								try {
									Database.updatePersonPoints(member, member.getActualPoint());
									Database.updateChallenge(member, c);
									
								} catch (SQLException e1) {
									e1.printStackTrace();
								}
								makeChallangeButtons(baseChallenge, member);
								showPoint.setText("" + member.getActualPoint());
								if ((c.getFrequency()*c.getPoint()) == c.getActualPoint()) {
									//show finish challenge panel
									finishChallenge(member, c);
								}	
							}
							//delete challenge by right click	
							if (e.getButton() == MouseButton.SECONDARY) {
								confirmDeleteChallenge(member, c);
							}	
						});
					} else {
						if(logedIn == null) {
							
							setChallenge.setOnMouseClicked(e ->{
								if (e.getButton() == MouseButton.PRIMARY) {
									showInfos("Bitte anmelden");
								}
								if (e.getButton() == MouseButton.SECONDARY) {
									showInfos("Bitte anmelden");
								}
							});
							
						}
						else {
							setChallenge.setOnMouseClicked(e ->{
								if (e.getButton() == MouseButton.PRIMARY) {
									showInfos("Nur für Eltern!");
								}
								if (e.getButton() == MouseButton.SECONDARY) {
									showInfos("Nur für Eltern!");
								}
							});
						}
					}
					
				}
	
		} else {
			Button setChallenge = new Button("<<Leer>>");
			setChallenge.setMinWidth(350);
			setChallenge.setTooltip(new Tooltip("Erste Heausforderung anlegen"));
			setChallenge.getStylesheets().add("Style.css");
			baseChallenge.getChildren().add(setChallenge);
			
			setChallenge.setOnAction(e->{
				if (logedIn != null && logedIn.isAdministrator()) {
				showInsertChallengePanel(member);
				} else {
					if(logedIn == null) {
					setChallenge.setOnAction(f->{
						showInfos("Bitte anmelden");
					});
				}
					else  {
					setChallenge.setOnAction(f->{
						showInfos("Nur für Eltern!");
					});
				}
			}
			});
		}
	}

	
	// make reward buttons
	private void makeRewardButtons(FlowPane baseReward, Familymembers member) {
		
		ArrayList<Reward> personalRewardList = new ArrayList<>();
		ArrayList<Reward> personalRewardListNotFullfield = new ArrayList<>();

		baseReward.getChildren().clear();

		try {
			personalRewardList = Database.readRewards(member);
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		for(Reward reward : personalRewardList) {
			if(!reward.isFullfield())
				personalRewardListNotFullfield.add(reward);
		}
		
		//check if there is any not fulfilled reward
		if (personalRewardListNotFullfield.size() != 0) {
			for (Reward r : personalRewardListNotFullfield ) {
				Button setReward = new Button(r.getName());
				setReward.setMinWidth(350);
				setReward.setTooltip(new Tooltip("Wert: " + r.getPoint() + " Punkte "));
				setReward.getStylesheets().add("Style.css");
				baseReward.getChildren().add(setReward);
				
				//check if the user is loged in, and he/she is an administrator
				
				if (logedIn!=null) {
					if(logedIn.isAdministrator() || logedIn.getPersonName().equals(member.getPersonName())) {
						setReward.setOnMouseClicked(e->{
							//show exchangePoint Stage by left clicking
							if(e.getButton() == MouseButton.PRIMARY) {
								exchangePoints(member, r);
							}
							//delete reward by right clicking
							if(e.getButton() == MouseButton.SECONDARY) {
								confirmDeleteReward(member, r);
							}
						});
					} else {
						setReward.setOnMouseClicked(e->{
						if(e.getButton() == MouseButton.PRIMARY) {
							showInfos("Nur für Eltern!");
						}
						if(e.getButton() == MouseButton.SECONDARY) {
							showInfos("Nur für Eltern!");
							}
						});
					}
				
				} else {
					if(logedIn == null) {
						setReward.setOnMouseClicked(e->{
							if(e.getButton() == MouseButton.PRIMARY) {
								showInfos("Bitte anmelden");
							}
							if(e.getButton() == MouseButton.SECONDARY) {
								showInfos("Bitte anmelden");
							}
						});
					}
				}
			}
		} else {
			Button setReward = new Button("<<Leer>>");
			setReward.setMinWidth(350);
			setReward.setTooltip(new Tooltip("Klicken hier"));
			setReward.getStylesheets().add("Style.css");
			baseReward.getChildren().add(setReward);
			
			setReward.setOnAction(e->{
				if (logedIn != null && logedIn.isAdministrator()) {
				showInsertRewardPanel(member);
				} else {
					if(logedIn == null) {
						setReward.setOnAction(f->{
						showInfos("Bitte anmelden");
					});
				} else {
						setReward.setOnAction(f->{
						showInfos("Nur für Eltern!");
						});
					}
				}
			});
		}
	}

	
	//make idName for challenge button
	private String setButtonRange(double all, double actual) {

		String buttonrange = "Button0";

		double sz = (actual/all)*100;

		if (sz > 1 && sz <= 10) {
			buttonrange = "Button10";
		}
		if (sz > 11 && sz <= 20) {
			buttonrange = "Button20";
		}
		if (sz > 21 && sz <= 30) {
			buttonrange = "Button30";
		}
		if (sz > 31 && sz <= 40) {
			buttonrange = "Button40";
		}
		if (sz > 41 && sz <= 50) {
			buttonrange = "Button50";
		}
		if (sz > 51 && sz <= 60) {
			buttonrange = "Button60";
		}
		if (sz > 61 && sz <= 70) {
			buttonrange = "Button70";
		}
		if (sz > 71 && sz <= 80) {
			buttonrange = "Button80";
		}
		if (sz > 81 && sz <= 90) {
			buttonrange = "Button90";
		}
		if (sz > 91 && sz <= 100) {
			buttonrange = "Button100";
		}
		return buttonrange;
	}
	
	
	// set stage for exchange of a rewards - 6. Abbildung: Punkte einlösen
	private void exchangePoints(Familymembers m, Reward reward) {
		// set the element of the stage
		Label nameReward = new Label("Belohnung: " + reward.getName());
		Label pointReward = new Label("Wert: " + reward.getPoint());
		VBox baseDetails = new VBox();
		baseDetails.getChildren().addAll(nameReward, pointReward);

		Label exchange = new Label("Möchtest du deine Punkte einlösen?");
		Label error = new Label();

		Button save = new Button("Ja");
		Button cancel = new Button("Nein");
		HBox saveCancel = new HBox();
		saveCancel.getChildren().addAll(save, cancel);
		saveCancel.setAlignment(Pos.CENTER);
		saveCancel.setSpacing(50);

		VBox baseExchangePoints = new VBox();
		baseExchangePoints.getChildren().addAll(baseDetails, exchange, error, saveCancel);
		baseExchangePoints.setId("vbox");
		baseExchangePoints.getStylesheets().add("Style.css");
		baseExchangePoints.setSpacing(20);
		baseExchangePoints.setAlignment(Pos.CENTER);
		baseExchangePoints.setPadding(new Insets(15, 15, 25, 15));

		Stage exchangePoints = new Stage();
		exchangePoints.setTitle("Punkte einlösen");
		exchangePoints.setScene(new Scene(baseExchangePoints));
		exchangePoints.initModality(Modality.APPLICATION_MODAL);
		exchangePoints.show();

		//exchange the point to reward
		save.setOnAction(e -> {
			if (m.getActualPoint() >= reward.getPoint()) {
				reward.setFullfield(true);
				m.setActualPoint(m.getActualPoint() - reward.getPoint());
				showPoint.setText("" + m.getActualPoint());
				try {
					Database.updatePersonPoints(m, m.getActualPoint());
					Database.updateReward(reward);
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
				makeRewardButtons(baseReward, m);
				error.setText("Diese Belohnung gehört jetzt dir :)");
				exchangePoints.close();
			} else {
				error.setText("Nicht genügend Punkte :(");
			}
		});
		
		cancel.setOnAction(f->{
			exchangePoints.close();
		});

	}

	
	//set stage for finish challenge - 4. Abbildung:Durchführung der Herausforderung
	private void finishChallenge(Familymembers m, Challenge c) {
		
		//set stage for close challenges
		Label message = new Label("Ich gratuliere dir zu Erfüllung der Herausforderung!");
		Label question = new Label("Was möchtest du machen?");
		Button goOn = new Button("Ich möchte mit der Herausforderung weiterarbeiten");
		Button finish = new Button("Löschen und Urkunde drucken");
		
		VBox baseFinishChallenge = new VBox();
		baseFinishChallenge.getChildren().addAll(message,question,goOn,finish);
		baseFinishChallenge.setId("vbox");
		baseFinishChallenge.getStylesheets().add("Style.css");
		baseFinishChallenge.setSpacing(15);
		baseFinishChallenge.setAlignment(Pos.CENTER);
		baseFinishChallenge.setPadding(new Insets(15, 15, 25, 15));
		
		Stage finishChallenge = new Stage();
		finishChallenge.setScene(new Scene(baseFinishChallenge));
		finishChallenge.setTitle(c.getName() + " Entfernen");
		finishChallenge.initModality(Modality.APPLICATION_MODAL);
		finishChallenge.show();
		
		//repeat the challenge
		goOn.setOnAction(e->{
			c.setFullfield(false);
			c.setActualPoint(0);
			try {
				Database.updateChallenge(m, c);
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			makeChallangeButtons(baseChallenge, m);
			finishChallenge.close();
			showInfos("Du kannst jetzt mit der Herausforderung:\n " + c.getName() + " \nweiterarbeiten.");
		});
		
		
		//delete the challenge, and print certificate
		finish.setOnAction(f->{
			c.setFullfield(true);
			try {
				Database.updateChallenge(m, c);
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			finishChallenge.close();
			makeChallangeButtons(baseChallenge, m);
			
			//set file chooser
			final FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Save Urkunde");
            fileChooser.setInitialDirectory(new File(System.getProperty("user.dir")));
            fileChooser.setInitialFileName(c.getName() + "_" +m.getPersonName());
            
            //set extension filter
            final FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("PDF files (*.pdf)", "*.pdf");
            fileChooser.getExtensionFilters().add(extFilter);
            
           
            File file = fileChooser.showSaveDialog(finishChallenge);
            String fileData =  file.getPath();
           
            if (file != null) {
			//Create certificate in .pdf from the challenge
            	try {
            		PdfCreator(m.getPersonName(), c.getName(), fileData);
            	} catch (DocumentException | IOException e1) {
            		e1.printStackTrace();
            	}
            }
			
		});	
	}
	
	
	//set Stage to delete challenge
 	private void confirmDeleteChallenge(Familymembers member, Challenge c) {
		
 		//create the element of the stage
 		Label message = new Label("Möchtest du die Herausforderung:\n" + c.getName() + " \nwirklich löschen?");
		Button confirm = new Button("Ja");
		Button cancel = new Button("Nein");
		
		HBox baseSaveCancel = new HBox();
		baseSaveCancel.getChildren().addAll(confirm, cancel);
		baseSaveCancel.setAlignment(Pos.CENTER);
		baseSaveCancel.setSpacing(50);
		
		VBox baseConfirmDelete = new VBox();
		baseConfirmDelete.getChildren().addAll(message, baseSaveCancel);
		baseConfirmDelete.setId("vbox");
		baseConfirmDelete.getStylesheets().add("Style.css");
		baseConfirmDelete.setSpacing(15);
		baseConfirmDelete.setAlignment(Pos.CENTER);
		baseConfirmDelete.setPadding(new Insets(15, 15, 25, 15));
		
		Stage confirmStage = new Stage();
		confirmStage.setScene(new Scene(baseConfirmDelete));
		confirmStage.setTitle(member.getPersonName() + ": " + c.getName());
		confirmStage.initModality(Modality.APPLICATION_MODAL);
		confirmStage.show();
		
		cancel.setOnAction(e->{
			confirmStage.close();
		});
		
		//delete challenge from the table
		confirm.setOnAction(e->{
			try {
				Database.deleteChallenge(c);
				makeChallangeButtons(baseChallenge, member);
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			confirmStage.close();
			showInfos(c.getName() + " is removed succesfully!");
		});
		
	}
	
 	
 	//create a PDF file
 	private void PdfCreator(String person, String challengeName, String fileData) throws DocumentException, IOException{
 				
		//create date to the certificate
		String dateStamp = new SimpleDateFormat("yyyy.MM.dd").format(new Date());
		
		Document document = new Document();
		PdfWriter.getInstance(document, new FileOutputStream(fileData));
		
		Image headline = Image.getInstance((System.getProperty("user.dir")) + "\\Headline.jpg");
		headline.scaleToFit(500, 500);
		
		Image logo = Image.getInstance((System.getProperty("user.dir")) + "\\logo.jpg");
		logo.scaleToFit(250, 250);
		
		Paragraph firstLine = new Paragraph("Liebe/r " + person, FontFactory.getFont(BaseFont.HELVETICA,18));
		Paragraph secondLine = new Paragraph("Ich gratuliere dir zu Erfüllung der Herausforderung: ",
											FontFactory.getFont(BaseFont.HELVETICA,18));
		Paragraph thirdLine = new Paragraph(challengeName, FontFactory.getFont(BaseFont.HELVETICA_BOLD,25));
		thirdLine.setAlignment(Paragraph.ALIGN_CENTER);
		Paragraph dateLine = new Paragraph(dateStamp, FontFactory.getFont(BaseFont.HELVETICA,18));
		dateLine.setAlignment(Paragraph.ALIGN_RIGHT);
		
		document.open();
		document.add(headline);
		document.add(new Paragraph("  "));
		document.add(new Paragraph("  "));
		document.add(firstLine);
		document.add(new Paragraph("  "));
		document.add(secondLine);
		document.add(new Paragraph(thirdLine));
		document.add(new Paragraph("  "));
		document.add(new Paragraph(dateLine));
		document.add(new Paragraph("  "));
		document.add(new Paragraph("  "));
		document.add(new Paragraph("  "));
		document.add(new Paragraph("  "));
		document.add(logo);
		document.close();
 	}
 	
 	
	//set Stage to delete reward
	private void confirmDeleteReward(Familymembers member, Reward r) {
			
			//create the element of the stage
			Label message = new Label("Möchtest du die Belohnung:\n " + r.getName() + "\nwirklich löschen?");
			Button confirm = new Button("JA");
			Button cancel = new Button("Neine");
			
			HBox baseSaveCancel = new HBox();
			baseSaveCancel.getChildren().addAll(confirm, cancel);
			baseSaveCancel.setAlignment(Pos.CENTER);
			baseSaveCancel.setSpacing(50);
			
			VBox baseConfirmDelete = new VBox();
			baseConfirmDelete.getChildren().addAll(message, baseSaveCancel);
			baseConfirmDelete.setId("vbox");
			baseConfirmDelete.getStylesheets().add("Style.css");
			baseConfirmDelete.setSpacing(15);
			baseConfirmDelete.setAlignment(Pos.CENTER);
			baseConfirmDelete.setPadding(new Insets(15, 15, 25, 15));
			
			Stage confirmStage = new Stage();
			confirmStage.setTitle(member.getPersonName() + ": " + r.getName());
			confirmStage.setScene(new Scene(baseConfirmDelete));
			confirmStage.initModality(Modality.APPLICATION_MODAL);
			confirmStage.show();
			
			cancel.setOnAction(e->{
				confirmStage.close();
			});
			
			//delete reward from the table
			confirm.setOnAction(e->{
				try {
					Database.deleteReward(r);;
					makeRewardButtons(baseReward, member);
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
				confirmStage.close();
				showInfos(r.getName() + " is removed succesfully!");
			});
			
		}
	
	
	//set Stage to delete person
	private void confirmDeletePerson(Familymembers member) {
		
		//create the element of the stage
		Label message = new Label("Möchtest du " + member.getPersonName() + " löschen?");
		Button confirm = new Button("Ja");
		Button cancel = new Button("Nein");
		
		HBox baseSaveCancel = new HBox();
		baseSaveCancel.getChildren().addAll(confirm, cancel);
		baseSaveCancel.setAlignment(Pos.CENTER);
		baseSaveCancel.setSpacing(50);
		
		VBox baseConfirmDelete = new VBox();
		baseConfirmDelete.getChildren().addAll(message, baseSaveCancel);
		baseConfirmDelete.setId("vbox");
		baseConfirmDelete.getStylesheets().add("Style.css");
		baseConfirmDelete.setSpacing(25);
		baseConfirmDelete.setAlignment(Pos.CENTER);
		baseConfirmDelete.setPadding(new Insets(15, 15, 25, 15));
		
		Stage confirmStage = new Stage();
		confirmStage.setTitle("Löschen: " + member.getPersonName());
		confirmStage.setScene(new Scene(baseConfirmDelete));
		confirmStage.initModality(Modality.APPLICATION_MODAL);
		confirmStage.show();
		
		cancel.setOnAction(e->{
			confirmStage.close();
		});
		
		confirm.setOnAction(e->{
			//delete data from person table
			try {
				Database.deletePerson(member);
				makePersonButtons(basePerson);
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			//delete all person related data from reward table
			ArrayList<Reward> readRewardPersonList = new ArrayList<Reward>();
			try {
				readRewardPersonList = Database.readRewards(member);
				
			} catch (SQLException k) {
			}
			
			for (Reward r :readRewardPersonList) {
				try {
					Database.deleteReward(r);
					
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			}
			makeRewardButtons(baseReward, member);
			
			//delete all person related data from challenge table
			ArrayList<Challenge> readChallengePersonList = new ArrayList<>();
			try {
				readChallengePersonList = Database.readChallenges(member);
				
			} catch (SQLException l) {
			}
			for (Challenge c : readChallengePersonList) {
				try {
					Database.deleteChallenge(c);
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			}
			makeChallangeButtons(baseChallenge, member);
			
			
			confirmStage.close();
			showInfos(member.getPersonName() + " wurde entfernt!");
		});
		
	}
	
	
	// set Stage for insert new challenge - 7.Abbildung Herausforderung anlegen
	private void showInsertChallengePanel(Familymembers member) {

		// set the elements for the insert challenge site
		Label ChallengeName = new Label("Name der Herausforderung:");
		TextField writeChallengeName = new TextField();
		writeChallengeName.setId("field");
		writeChallengeName.setTooltip(new Tooltip("Name der Herausforderung.\n "
												+ "Mindestens 3 Buchstaben!"));
		writeChallengeName.setMinHeight(45);

		Label pointPerImplementation = new Label("Punkte pro Realisierung:");
		TextField writePointPerImplementation = new TextField();
		writePointPerImplementation.setId("field");
		writePointPerImplementation.setTooltip(new Tooltip("Wie viele Punkte bekomst du nach eine Realisierung?"
															+ "\nBitte einen positiven, ganzen Zahl angeben."));
		writePointPerImplementation.setMinHeight(45);

		Label ChallengeFrequency = new Label("Wie vielmal möchtest du die Herausforderung ausüben?");
		TextField writeChallengeFrequency = new TextField();
		writeChallengeFrequency.setId("field");
		writeChallengeFrequency.setTooltip(new Tooltip("Insgesamt wie vielmal möchtest du die Herausforderung ausüben?"+ 
														"\nBitte einen positiven, ganzen Zahl angeben."));
		writeChallengeFrequency.setMinHeight(45);

		Label error = new Label();

		Button save = new Button("Speichern");
		Button cancel = new Button("Entfernen");

		HBox baseSaveCancel = new HBox(save, cancel);
		baseSaveCancel.setSpacing(50);
		baseSaveCancel.setAlignment(Pos.CENTER);

		// set the base
		VBox baseInserChallege = new VBox(ChallengeName, writeChallengeName, pointPerImplementation,
				writePointPerImplementation, ChallengeFrequency, writeChallengeFrequency, error, baseSaveCancel);
		baseInserChallege.setId("vbox");
		baseInserChallege.getStylesheets().add("Style.css");
		baseInserChallege.setSpacing(15);
		baseInserChallege.setPadding(new Insets(15, 15, 25, 15));

		Stage insertChallenge = new Stage();
		insertChallenge.setScene(new Scene(baseInserChallege));
		insertChallenge.setTitle("Neue Herausforderung anlegen");
		insertChallenge.initModality(Modality.APPLICATION_MODAL);
		insertChallenge.show();

		// clear the text fields
		cancel.setOnAction(f -> {
			writeChallengeName.clear();
			writePointPerImplementation.clear();
			writeChallengeFrequency.clear();
			error.setText("");
		});

		// insert new Challenge
		save.setOnAction(g -> {

			int PointPerImplementation = 0;
			int PointChallengeFrequency = 0;

			try {
				PointPerImplementation = Integer.parseInt(writePointPerImplementation.getText());
				PointChallengeFrequency = Integer.parseInt(writeChallengeFrequency.getText());

			} catch (NumberFormatException e) {
			}
			// check if input fields are empty 
			if(writeChallengeName.getText().length() != 0 && 
				writePointPerImplementation.getText().length() != 0 && 
				writeChallengeFrequency.getText().length() != 0) {
				//check if there are enough characters in name field
				if (writeChallengeName.getText().length()>= 3) {
					//check if there are enough characters in fields per implementation and frequency
					if(writePointPerImplementation.getText().length() >= 1 && writeChallengeFrequency.getText().length() >= 1) {
						//check if there are numbers in fields per implementation and frequency
						if(PointPerImplementation != 0 && PointChallengeFrequency != 0) {
							try {
								Database.insertChallenge(new Challenge(1, writeChallengeName.getText(), member.getPersonID(), 
										member.getPersonName(), PointPerImplementation, false, PointChallengeFrequency, 0));
								makeChallangeButtons(baseChallenge, member);
								insertChallenge.close();
								showInfos(writeChallengeName.getText()+ " wurde angelegt!");

							} catch (SQLException e) {

								e.printStackTrace();
							}

							try {
								member.setChallengeList(Database.readChallenges(member));

							} catch (SQLException e) {
								e.printStackTrace();
							}
							
						} else {
							error.setText("Feld 2 und 3: bitte nur Zahlen");
						}
						
					} else {
						error.setText("Feld 2 und 3: minimum 1 Zeichen");
						error.setVisible(true);
					}
					
				} else {
					error.setText("Feld 1: minimum 3 Zeichen");
					error.setVisible(true);
				}
				
				
			} else {
				error.setText("Bitte all Felder ausfühllen");
				error.setVisible(true);
			}
		});

	}

	
	// set Stage for insert new reward
	private void showInsertRewardPanel(Familymembers member) {
		// set the elements for the insert reward site
		Label nameReward = new Label("Name der Belohnung:");
		TextField writeRewardName = new TextField();
		writeRewardName.setId("field");
		writeRewardName.setTooltip(new Tooltip("Name der Belohnung.\n "
											+ "Mindestens 3 Buchstaben!"));
		writeRewardName.setMinHeight(45);

		Label pointReward = new Label("Wie viele Punkte?");
		TextField writePointReward = new TextField();
		writePointReward.setId("field");
		writePointReward.setTooltip(new Tooltip("Wert der Belohnung in Punkten.\n"
											+ "Bitte einen positiven, ganzen Zahl angeben."));
		writePointReward.setMinHeight(45);

		Label error = new Label();

		Button save = new Button("Speichern");
		Button cancel = new Button("Entfernen");

		HBox baseSaveCancel = new HBox(save, cancel);
		baseSaveCancel.setSpacing(50);
		baseSaveCancel.setAlignment(Pos.CENTER);

		// set the base
		VBox baseInsertReward = new VBox(nameReward, writeRewardName, pointReward, writePointReward, error,
				baseSaveCancel);
		baseInsertReward.setId("vbox");
		baseInsertReward.getStylesheets().add("Style.css");
		baseInsertReward.setSpacing(15);
		baseInsertReward.setPadding(new Insets(15, 15, 25, 15));
		baseInsertReward.setAlignment(Pos.CENTER);

		Stage insertReward = new Stage();
		insertReward.setScene(new Scene(baseInsertReward));
		insertReward.setTitle("Neue Belohnung anlegen");
		insertReward.initModality(Modality.APPLICATION_MODAL);
		insertReward.show();

		// clear the text fields
		cancel.setOnAction(f -> {
			writeRewardName.clear();
			writePointReward.clear();
			error.setText("");
		});

		// insert new Reward
		save.setOnAction(g -> {

			int PointRewardValue = 0;

			try {
				PointRewardValue = Integer.parseInt(writePointReward.getText());
			} catch (NumberFormatException e) {
			}
			
			// check if input fields are empty
			if(writeRewardName.getText().length() > 0 && writePointReward.getText().length()> 0) {
				//check if there are enough characters in writeRewardName field
				if(writeRewardName.getText().length() >= 3) { 
					//check if there is number in writePointReward field
					if(PointRewardValue != 0) {
						try {
							Database.insertReward(
									new Reward(1, writeRewardName.getText(), 
											member.getPersonID(), member.getPersonName(), 
											PointRewardValue, false));
							makeRewardButtons(baseReward, member);
							insertReward.close();
							showInfos(writeRewardName.getText() + " wurde angelegt!");
							
						} catch (SQLException e) {
							e.printStackTrace();
						}

						try {
							member.setReawardList(Database.readRewards(member));
						} catch (SQLException e) {
							e.printStackTrace();
						}
							
					} else {
						error.setText("Bitten nur Zahlen in Feld 2");
						error.setVisible(true);
					}
					
				} else {
					error.setText("Feld 1: minimum 3 Zeichen");
					error.setVisible(true);
				}
				
			} else {
				error.setText("Bitte all Felder ausfühllen");
				error.setVisible(true);
			}
		});
	}

	
	// set registration Stage 2. Abbildung: Neuen Familienmitglieder anlegen
	private void showRegistrationPanel() {

		Label registrationTitel = new Label("Registration");
		registrationTitel.setId("Titels");

		// set the elements of the registration site
		Label name = new Label("Name: ");
		TextField writeName = new TextField();
		writeName.setId("field");
		writeName.setTooltip(new Tooltip("Bitte, die Name anlegen"));
		writeName.setMinHeight(45);
		HBox setName = new HBox();
		setName.getChildren().addAll(name, writeName);
		setName.setAlignment(Pos.CENTER);

		Label password = new Label("Kennwort: ");
		PasswordField writePassword = new PasswordField();
		writePassword.setId("field");
		writePassword.setTooltip(new Tooltip("Bitte, das Kenntwort anlegen"));
		writePassword.setMinHeight(45);
		HBox setPassword = new HBox();
		setPassword.getChildren().addAll(password, writePassword);
		setPassword.setAlignment(Pos.CENTER);

		Label isAdministrator = new Label("Eltern?\t");
		isAdministrator.setTooltip(new Tooltip("Ja: Benutzer mit vollen Berechtigungen (Eltern).\n"
											+ "Nein: Benutzer mit eingeschränkten Berechtigungen (Kindern)."));

		ToggleGroup tg = new ToggleGroup();
		RadioButton parent = new RadioButton("JA\t");
		parent.setToggleGroup(tg);
		parent.setSelected(true);
		RadioButton child = new RadioButton("NEIN");
		child.setToggleGroup(tg);
		GridPane setAdministrator = new GridPane();
		setAdministrator.add(isAdministrator, 0, 0);
		setAdministrator.add(parent, 1, 0);
		setAdministrator.add(child, 2, 0);
		setAdministrator.setAlignment(Pos.CENTER);

		Label error = new Label();
		error.setVisible(false);

		Button registrationVerify = new Button("Bestehtigung");
		Button registrationCacnel = new Button("Entfernen");

		HBox regVerOrCanc = new HBox();
		regVerOrCanc.getChildren().addAll(registrationVerify, registrationCacnel);
		regVerOrCanc.setSpacing(50);
		regVerOrCanc.setAlignment(Pos.CENTER);

		// set base for the registration site
		VBox baseRegistration = new VBox();
		baseRegistration.getChildren().addAll(registrationTitel, setName, setPassword, setAdministrator, error,
				regVerOrCanc);
		baseRegistration.setId("vbox");
		baseRegistration.getStylesheets().add("Style.css");
		baseRegistration.setSpacing(30);
		baseRegistration.setPadding(new Insets(15));
		baseRegistration.setAlignment(Pos.CENTER);

		//set registration stage
		Stage registrationStage = new Stage();
		registrationStage.setScene(new Scene(baseRegistration));
		registrationStage.setTitle("Registration");
		registrationStage.initModality(Modality.APPLICATION_MODAL);
		registrationStage.show();

		// insert new Person to the database
		registrationVerify.setOnAction(f -> {
			
			ArrayList<Familymembers> listRegistration = new ArrayList<>();
			boolean test = false;
			try {
				listRegistration = Database.readFamilymembers();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			
			//check if there is any character in the fields
			if(writeName.getText().length() > 0 && writePassword.getText().length() > 0) {
				//check if there enough character in the fields
				if(writeName.getText().length() >= 3 && writePassword.getText().length() >= 3) {
					
					for (Familymembers g : listRegistration) {
						//check if user name is possessed
						if (writeName.getText().equals(g.getPersonName())) {
							test = true;
							error.setText("Bitte andere Name wahlen");
							error.setVisible(true);
						}
					}
					if (test == false) {
						try {
							Database.insertPerson(new Familymembers(555, writeName.getText(), writePassword.getText(), 0,
									null, null, parent.isSelected()));
							makePersonButtons(basePerson);
							showInfos(writeName.getText() + " wurde angelegt!");
							registrationStage.close();
						} catch (SQLException e) {
							e.printStackTrace();
						}
					}
					
				} else {
					error.setText("Felder 1 und 2 minimum 3 Zeichen");
					error.setVisible(true);
				}
			} else {
				error.setText("Bitte alle Felder befühlen");
				error.setVisible(true);
			}
		});

		//clear textFileds
		registrationCacnel.setOnAction(f->{
			writeName.clear();
			writePassword.clear();
			error.setVisible(false);
		});
	}

	
	// set login Stage
	private void showLoginPanel() {

		// set the elements of the login site
		Label loginTitel = new Label("Login");
		loginTitel.setId("Titels");

		Label name = new Label("Name: ");
		TextField writeName = new TextField();
		writeName.setId("field");
		writeName.setTooltip(new Tooltip("Bitte, schreiben die Name"));
		writeName.setMinHeight(45);
		HBox setName = new HBox();
		setName.getChildren().addAll(name, writeName);
		setName.setAlignment(Pos.CENTER);

		Label password = new Label("Password: ");
		PasswordField writePassword = new PasswordField();
		writePassword.setId("field");
		writePassword.setTooltip(new Tooltip("Bitte, schreiben das Kenntwort"));
		writePassword.setMinHeight(45);
		HBox setPassword = new HBox();
		setPassword.getChildren().addAll(password, writePassword);
		setPassword.setAlignment(Pos.CENTER);

		Label error = new Label();
		error.setVisible(false);

		Button loginVerify = new Button("OK");
		Button loginCancel = new Button("NOT");

		HBox logVerOrCanc = new HBox();
		logVerOrCanc.getChildren().addAll(loginVerify, loginCancel);
		logVerOrCanc.setSpacing(50);
		logVerOrCanc.setAlignment(Pos.CENTER);

		// set base for the login site
		VBox baseRegistration = new VBox();
		baseRegistration.getChildren().addAll(loginTitel, setName, setPassword, error, logVerOrCanc);
		baseRegistration.setId("vbox");
		baseRegistration.getStylesheets().add("Style.css");
		baseRegistration.setSpacing(30);
		baseRegistration.setPadding(new Insets(15, 15, 15, 15));
		baseRegistration.setAlignment(Pos.CENTER);

		Stage loginStage = new Stage();
		loginStage.setTitle("Anmeldung");
		loginStage.setScene(new Scene(baseRegistration));
		loginStage.initModality(Modality.APPLICATION_MODAL);
		loginStage.show();
		
		loginCancel.setOnAction(f->{
			loginStage.close();
			baseStage.setTitle("Startseite");
			login.setText("Login");
		});

		loginVerify.setOnAction(m -> {
			ArrayList<Familymembers> listMembers = new ArrayList<>();
			try {
				listMembers = Database.readFamilymembers();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			//change loged in user
			if(login.getText() == "Log out") {
				login.setText("Login");
				logedIn = null;
				baseStage.setTitle("Startseite");
				
			} else { 
				//check if there any character in the textFields
				if (writeName.getText().length() != 0 && writePassword.getText().length() != 0) {
					//check if there enough characters in the textFields
					if (writeName.getText().length() >= 3 && writePassword.getText().length() >= 3) {
						for (Familymembers f : listMembers) {
							if (f.getPersonName().equals(writeName.getText())
									&& f.getPassworld().replaceAll(" ", "").equals(writePassword.getText())) {
	
								logedIn = f;
								makePersonButtons(basePerson);
								login.setText("Log out");
								
								loginStage.close();
								showInfos(f.getPersonName() + " ist erfolgreich angemeldet");
								baseStage.setTitle(f.getPersonName() + " ist angemeldet");
								break;
							} else {
								error.setVisible(true);
								error.setText("Name oder Kenntwort ist falsch");
							}
						}
					} else {
						error.setVisible(true);
						error.setText("Felder 1 und 2 minimum 3 Zeichen");
					}
					
				} else {
					error.setVisible(true);
					error.setText("Bitte alle Felder befühlen");
				}
			}
		});
	}

	
	// set info stage
	private void showInfos(String info) {

		//set the element of the stage
		Label message = new Label(info);
		Button confimr = new Button("OK");

		VBox baseInfo = new VBox();
		baseInfo.getChildren().addAll(message, confimr);
		baseInfo.setId("vbox");
		baseInfo.getStylesheets().add("Style.css");
		baseInfo.setPadding(new Insets(15));
		baseInfo.setSpacing(30);
		baseInfo.setAlignment(Pos.CENTER);

		Stage infoStage = new Stage();
		infoStage.setScene(new Scene(baseInfo));
		infoStage.initModality(Modality.APPLICATION_MODAL);
		infoStage.show();

		confimr.setOnAction(e -> {
			infoStage.close();
		});
	}
}


