package com.example.mealer_project.ui.screens;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import com.example.mealer_project.R;
import com.example.mealer_project.app.App;
import com.example.mealer_project.data.handlers.UserHandler;
import com.example.mealer_project.data.models.inbox.Complaint;
import com.example.mealer_project.ui.core.StatefulView;
import com.example.mealer_project.ui.core.UIScreen;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class ComplaintScreen extends UIScreen implements StatefulView{
    private Button banButton;
    private DatePickerDialog datePickerDialog;
    DatePickerDialog.OnDateSetListener dateSetListener;
    Complaint complaintData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complaint_screen);
        banButton = findViewById(R.id.ban_chef);

        try {

            complaintData = (Complaint) getIntent().getSerializableExtra(AdminScreen.COMPLAINT_OBJ_INTENT_KEY);

            updateComplaintScreen(complaintData.getTitle(), complaintData.getClientId(), complaintData.getChefId(), complaintData.getOrderId(), complaintData.getDescription());

        }catch (Exception e) {
            Log.e("ComplaintScreen", "unable to create complaint object");
            displayErrorToast("Unable to display complaint!");
        }

        banButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                datePickerDialog = new DatePickerDialog(ComplaintScreen.this, android.R.style.Theme_Holo_Light_Dialog_MinWidth, dateSetListener, year, month, day);
                datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                datePickerDialog.show();
            }
        });

        dateSetListener = new DatePickerDialog.OnDateSetListener(){
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
                System.out.println("THIS GOT CALLED");
                String suspendString = Integer.toString(month + 1) + "/" + Integer.toString(dayOfMonth) + "/" + Integer.toString(year);
                Date suspendDate = null;
                try {
                    suspendDate = DateFormat.getDateInstance(DateFormat.SHORT, Locale.US).parse(suspendString);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                App.getUserHandler().suspendChef(complaintData.getChefId(), suspendDate);
            }
        };
    }

    @Override
    public void updateUI() {
    }


    /**
     * Updates the text on the complaint screen
     * @param title
     * @param client
     * @param chef
     * @param meal
     * @param description
     */
    public void updateComplaintScreen(String title, String client, String chef, String meal, String description) {
        // sets the complaint header title
        TextView textTitle = (TextView)findViewById(R.id.complaintHeader);
        textTitle.setText(title);

        // sets the text for client name
        TextView textClient = (TextView)findViewById(R.id.clientComplaintName);
        textClient.setText(client);

        // sets the text for chef name
        TextView textChef = (TextView)findViewById(R.id.chefComplaintName);
        textChef.setText(chef);

        // sets the text for meal
        TextView textMeal = (TextView)findViewById(R.id.mealComplaintName);
        textMeal.setText(meal);

        // sets the text for description
        TextView textDescription = (TextView)findViewById(R.id.complaintDescription);
        textDescription.setText(description);
    }

    @Override
    public void showNextScreen() {

    }
}
