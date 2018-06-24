package com.sample.smartrestaurants;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class EvaluateActivity extends AppCompatActivity {

    FirebaseDatabase mFirebaseDatabase;
    DatabaseReference mRef;
    Query query;
    Query queryEval;

    Button btnEvaluate;
    TextView nameRestaurant;
    EditText rating;

    String name;
    Double evaluation;
    Double rate;
    int numberEvaluation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evaluate);

        name = getIntent().getStringExtra("name");

        btnEvaluate = findViewById(R.id.btnEvaluate);
        rating = findViewById(R.id.rating);
        nameRestaurant = findViewById(R.id.nameRes);
        nameRestaurant.setText(name);

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mRef = mFirebaseDatabase.getReference("Restaurant");

        query = FirebaseDatabase.getInstance().getReference("Restaurant")
                .orderByChild("name")
                .equalTo(name);

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    numberEvaluation = ds.child("numberEvaluation").getValue(Integer.class);

                    evaluation = ds.child("evaluation").getValue(Double.class);
                    nameRestaurant.setText(name);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        ////////////////////////////////////////////////////
        queryEval = mRef.orderByChild("name").equalTo(name);

        btnEvaluate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                query.addChildEventListener(
                        new ChildEventListener() {

                            @Override
                            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                                rate = Double.parseDouble(rating.getText().toString().trim());
                                double finalRate = calculateEvaluation(evaluation, rate, numberEvaluation);
                                int finalNumberEval = numberEvaluation++;
                                mRef.child(dataSnapshot.getKey()).child("evaluation").setValue(finalRate);
                                mRef.child(dataSnapshot.getKey()).child("numberEvaluation").setValue(finalNumberEval);

                                showInfo();
                            }

                            @Override
                            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                            }

                            @Override
                            public void onChildRemoved(DataSnapshot dataSnapshot) {

                            }

                            @Override
                            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                query.addChildEventListener(
                        new ChildEventListener() {

                            @Override
                            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                                int finalNumberEval = numberEvaluation++;
                                mRef.child(dataSnapshot.getKey()).child("numberEvaluation").setValue(finalNumberEval);

                                showInfo();
                            }

                            @Override
                            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                            }

                            @Override
                            public void onChildRemoved(DataSnapshot dataSnapshot) {

                            }

                            @Override
                            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
            }
        });
    }

    public void showInfo() {
        Toast.makeText(this, "Rating were added", Toast.LENGTH_LONG).show();
    }

    public double calculateEvaluation(double evaluation, double currentEval, int numberOfEvaluation) {

        double finalEval = 0;

        if(numberOfEvaluation == 0) {
            finalEval = currentEval;
        } else {
            double pom1 = numberOfEvaluation * evaluation;
            numberOfEvaluation++;
            double pom2 = evaluation + currentEval;
            finalEval = pom2 / numberOfEvaluation;
        }

        return finalEval;
    }

}
