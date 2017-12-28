package com.jessyend.sheare.PlaylistView;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.jessyend.sheare.R;

import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * The Pie Activity displays a pie chart given the user track count.
 * @author Jessye Nana Davies <nanadav2@illinois.edu>
 */
public class PieActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pie);

        Bundle extra = getIntent().getBundleExtra("extra");
        ArrayList<String> users = (ArrayList<String>) extra.getSerializable("users");
        ArrayList<String> count = (ArrayList<String>) extra.getSerializable("count");

        PieChart mPieChart = (PieChart) findViewById(R.id.piechart);

        String[] colors = new String[4];
        colors[0] = "#DB9ECB";
        colors[1] = "#785187";
        colors[2] = "#B5F5D8";
        colors[3] = "#FED70E";

        for(int i = 0; i < users.size(); i++) {
            mPieChart.addPieSlice(new PieModel(users.get(i),
                    Integer.parseInt(count.get(i)), Color.parseColor(colors[i])));
        }

        mPieChart.startAnimation();
    }
}
