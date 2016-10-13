package techinvogue.net.googlemapaddon;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;


public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        item.setTitle("FAQ");
        int id = item.getItemId();


        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(this, FAQ.class);//when the action bar FAQ is clicked, it'll be redirected to FAQ class
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onNavigate(View v) {
        if (v.getId() == R.id.btnNavigation) {
            startActivity(new Intent(this, MapsActivity.class));//when the Navigation button is clicked, it'll be redirected to MapsActivity
        }
    }

    public void onSearch(View v) {
        if (v.getId() == R.id.btnSearch) {
            startActivity(new Intent(this, SearchActivity.class));//when the Search button is clicked, it'll be redirected to SearchActivity
        }
    }

}
