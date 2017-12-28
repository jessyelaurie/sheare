package com.jessyend.sheare.PlaylistView;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.jessyend.sheare.R;

import org.eazegraph.lib.charts.BarChart;
import org.eazegraph.lib.models.BarModel;

import java.util.ArrayList;

/**
 * The Graph Activity displays a bar chart given the values
 * @author Jessye Nana Davies <nanadav2@illinois.edu>
 */
public class GraphActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);
        Bundle extra = getIntent().getBundleExtra("extra");
        String[] tracks = (String[]) extra.getSerializable("tracks");
        float[] values = (float[]) extra.getSerializable("values");
        TextView title = (TextView) findViewById(R.id.graph_title);
        title.setText(getIntent().getStringExtra("title"));
        BarChart mBarChart = (BarChart) findViewById(R.id.barchart);
        mBarChart.addBar(new BarModel(values[0], 0xFF47A9C9));
        mBarChart.addBar(new BarModel(values[1],  0xFF5BBEDE));
        mBarChart.addBar(new BarModel(values[2], 0xFFAAE6FA));
        mBarChart.startAnimation();
        TextView first = (TextView) findViewById(R.id.graph_first);
        TextView second = (TextView) findViewById(R.id.graph_second);
        TextView third = (TextView) findViewById(R.id.graph_third);
        first.setText("1. " + tracks[0]);
        second.setText("2. " + tracks[1]);
        third.setText("3. " + tracks[2]);

    }
}
