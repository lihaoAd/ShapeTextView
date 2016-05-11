package app.shapetextview;

import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

private ScrollView scrollView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        scrollView = (ScrollView) findViewById(R.id.scrollView);

        TextView textView1 = (TextView) findViewById(R.id.text1);
        textView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(scrollView,"四个圆角都是一样的!",Snackbar.LENGTH_SHORT).show();
            }
        });

        TextView textView2 = (TextView) findViewById(R.id.text2);
        textView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(scrollView,"四个圆角都是不一样的!",Snackbar.LENGTH_SHORT).show();
            }
        });

        TextView textView3 = (TextView) findViewById(R.id.text3);
        textView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(scrollView,"注意字体的颜色在变化",Snackbar.LENGTH_SHORT).show();
            }
        });


    }
}
