package com.sample.smartrestaurants;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
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
    Double evaluationSum;
    Float rate;
    RatingBar ratingBar;
    int numberEvaluation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evaluate);

        name = getIntent().getStringExtra("name");

        btnEvaluate = findViewById(R.id.btnEvaluate);
        rating = findViewById(R.id.rating);
        ratingBar = findViewById(R.id.ratingbar);
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
                    numberEvaluation = ds.child("evaluationNum").getValue(Integer.class);
                    evaluationSum = ds.child("evaluationSum").getValue(Double.class);

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
                String rating = "Rating is: " + ratingBar.getRating();
                Toast.makeText(EvaluateActivity.this,"Rating were added! " + rating, Toast.LENGTH_SHORT).show();
                query.addChildEventListener(
                        new ChildEventListener() {

                            @Override
                            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                                //rate = Double.parseDouble(rating.getText().toString().trim());
                                rate = ratingBar.getRating();
                                numberEvaluation++;
                                double finalRate = calculateEvaluation(evaluation, rate, evaluationSum, numberEvaluation);
                                evaluationSum = evaluationSum + rate;
                                mRef.child(dataSnapshot.getKey()).child("evaluation").setValue(finalRate);
                                mRef.child(dataSnapshot.getKey()).child("evaluationSum").setValue(evaluationSum);
                                mRef.child(dataSnapshot.getKey()).child("evaluationNum").setValue(numberEvaluation);

                                //showInfo();

                                Intent i = new Intent(EvaluateActivity.this, MapsActivity.class);
                                startActivity(i);
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

                                //showInfo();
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
        Toast.makeText(this, "Rating were added" + rating, Toast.LENGTH_LONG).show();
    }

    public double calculateEvaluation(double evaluation, double currentEvaluation, double sumEvaluation, int numberEvaluation) {

        double finalEval = 0;

        if(numberEvaluation == 1) {
            finalEval = currentEvaluation;
        } else {
            double pom1 = sumEvaluation + currentEvaluation;
            finalEval = pom1 / numberEvaluation;
        }

        return finalEval;
    }

}
