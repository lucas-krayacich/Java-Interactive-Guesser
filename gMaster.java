package com.example.guessmaster;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.Scanner;
import java.util.Random;

import android.content.DialogInterface;
import android.widget.Toast;

public class gMaster extends AppCompatActivity {


    private int numOfEntities;
    private Entity[] entities;
    private int totalTicketNum = 0; ///
    String entName;
    int entityid = 0;
    int currentTicketWon = 0;

    private TextView entityName;
    private TextView ticketsum;
    private Button guessButton;
    private EditText userIn;
    private Button btnclearContent;
    private String user_input;
    private ImageView entityImage;
    String answer;

    Politician trudeau = new Politician("Justin Trudeau", new Date("December", 25, 1971), "Male", "Liberal", 0.25);
    Singer dion = new Singer("Celine Dion", new Date("March", 30, 1961), "Female", "La voix du bon Dieu", new Date("November", 6, 1981), 0.5);
    Person myCreator = new Person("myCreator", new Date("September", 1, 2000), "Female", 1);
    Country usa = new Country("United States", new Date("July", 4, 1776), "Washington D.C.", 0.1);

    final gMaster gm = new gMaster();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        entityName = (TextView) findViewById(R.id.entityname);
        ticketsum = (TextView) findViewById(R.id.ticket);
        guessButton = (Button) findViewById(R.id.btnguess);
        userIn = (EditText) findViewById(R.id.guessinput);
        btnclearContent = (Button) findViewById(R.id.btnclear);
        entityImage = (ImageView) findViewById(R.id.entityimage);

        btnclearContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeEntity();
            }
        });

        guessButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v){
                playGame();
            }
        });

    }

    public gMaster() {
        numOfEntities = 0;
        entities = new Entity[10];
    }

    public void changeEntity(){
        //clear the user entries from edit text
        userIn.getText().clear();
        //choose a random new entity
        int newEntID = genRandomEntityId();
        Entity newEnt = entities[newEntID];
        entityName.setText(newEnt.getName());
    }

    public void imageSetter(Entity newEntity){
        if (newEntity.toString().equals(trudeau.toString())){
            entityImage.setImageResource(R.drawable.justint);
        }
        else if (newEntity.toString().equals(dion.toString())){
            entityImage.setImageResource(R.drawable.celidion);
        }
        else if (newEntity.toString().equals(usa.toString())){
            entityImage.setImageResource(R.drawable.usaflag);
        }
        else if (newEntity.toString().equals(myCreator.toString())){
            entityImage.setImageResource(R.drawable.mycreator);
        }
    }

    public void welcomeToGame(Entity entity){
        AlertDialog.Builder welcomealert = new AlertDialog.Builder(gMaster.this);
        welcomealert.setTitle("GuessMaster Game v3");
        welcomealert.setMessage(entity.welcomeMessage());
        welcomealert.setCancelable(false);
        welcomealert.setNegativeButton("START GAME", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getBaseContext(), "Game is Starting...", Toast.LENGTH_SHORT).show();
            }
        });
        AlertDialog dialog = welcomealert.create();
        dialog.show();
    }




    public void addEntity(Entity entity) {
//		entities[numOfEntities++] = new Entity(entity);
//		entities[numOfEntities++] = entity;//////
        entities[numOfEntities++] = entity.clone();
    }

    public void playGame(int entityId) {
        Entity entity = entities[entityId];
        playGame(entity);
    }

    public void playGame(Entity entity) {
        System.out.println("***************************");////
        System.out.printf(entity.welcomeMessage());////
        if(entity.getName() != "Myself")
            System.out.printf("\n\nGuess %s's birthday\n", entity.getName());
        else
            System.out.printf("\n\nGuess my birthday\n", entity.getName());
        System.out.println("(mm/dd/yyyy)");

        Scanner scanner = new Scanner(System.in);

            entityName.setText(entity.getName());


            String answer = userIn.getText().toString();
            answer = answer.replace("\n", "").replace("\r", "");
            Date date = new Date(answer);

            if (answer.equals("quit")) {
                System.exit(0);
            }


//			System.out.println("you guess is: " + date);

            if (date.precedes(entity.getBorn())) {

                AlertDialog.Builder tooLate = new AlertDialog.Builder(gMaster.this);
                tooLate.setIcon(R.drawable.ic_error_outline_black_24dp);
                tooLate.setTitle("Incorrect");
                tooLate.setMessage("Try a later date than " +date.toString());
                tooLate.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Toast.makeText(getBaseContext(), "Game is Starting...", Toast.LENGTH_SHORT).show();
                    }
                });
                tooLate.show();
            }
            else if (entity.getBorn().precedes(date)) {
                AlertDialog.Builder tooEarly = new AlertDialog.Builder(gMaster.this);
                tooEarly.setIcon(R.drawable.ic_check_circle_black_24dp);
                tooEarly.setTitle("Incorrect");
                tooEarly.setMessage("Try an earlier date than " + date.toString());
                tooEarly.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                });
                tooEarly.show();
            }
            else {
                int currentTicketWon = (int) ((entity.getDifficulty()) * 100);
                totalTicketNum += currentTicketWon;
                AlertDialog.Builder correctDate = new AlertDialog.Builder(gMaster.this);
                correctDate.setIcon(R.drawable.ic_check_circle_black_24dp);
                correctDate.setTitle("You Won!");
                correctDate.setMessage("Bingo! " +entity.closingMessage());
                correctDate.setNegativeButton("Continue", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ContinueGame();
                    }
                });
                correctDate.show();
                ticketsum.setText("Total Tickets: " + currentTicketWon);

        }
    }

    public void playGame(){
            int entityId = genRandomEntityId();
            playGame(entityId);
    }

    public int genRandomEntityId() {
        Random randomNumber = new Random();
        return randomNumber.nextInt(numOfEntities);
    }

    public void ContinueGame(){
        entityid = genRandomEntityId();
        Entity entity = entities[entityid];
        entName = entity.getName();
        imageSetter(entity);
        entityName.setText(entName);
        userIn.getText().clear();

    }



}