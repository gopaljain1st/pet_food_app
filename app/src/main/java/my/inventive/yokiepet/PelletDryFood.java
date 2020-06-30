package my.inventive.yokiepet;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class PelletDryFood extends AppCompatActivity {
    private static final String apiurl="https://inventivepartner.com/petmart/fetchadditem.php";
    RecyclerView recyclerView;
   // ArrayList<TeaAndCoffeeModel> teaAndCoffeeModelArrayList;
    ArrayList<AttaAndOtherModel> teaAndCoffeeModelArrayList;
    AttaAndOtherAdapter gridItemAdapter;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // setContentView(R.layout.activity_tea_and_coffee);
        setContentView(R.layout.activity_atta_and_other);
        ActionBar actionBar=getSupportActionBar();
        actionBar.setTitle("Pellet & Dry food");


       // recyclerView=findViewById(R.id.recycle_view_tea_and_coffee);
        recyclerView=findViewById(R.id.recycle_view_item);
        teaAndCoffeeModelArrayList=new ArrayList<>();

        LinearLayoutManager layoutManager= new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        // recyclerView.setAdapter(teaAndCoffeeAdapter);
        gridItemAdapter=new AttaAndOtherAdapter(this, teaAndCoffeeModelArrayList);
        recyclerView.setAdapter(gridItemAdapter);

        progressDialog=new ProgressDialog(PelletDryFood.this,R.style.MyAlertDialogStyle);
        progressDialog.setTitle("Yokie Pet Mart");
        progressDialog.setMessage("Please Wait......");

        progressDialog.show();
        StringRequest request=new StringRequest(Request.Method.POST, apiurl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                // attaAndOtherModels.clear();
                try {
                    //Toast.makeText(AttaAndOtherActivity.this, "" + response, Toast.LENGTH_SHORT).show();
                    JSONObject jsonObject = new JSONObject(response);
                    String sucess = jsonObject.getString("success");
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    if (sucess.equals("1")) {
                        progressDialog.dismiss();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject object = jsonArray.getJSONObject(i);
                            String category=object.getString("item_category");
                            if (category.equals("Pellet/Dry Food")) {
                                String id = object.getString("id");
                                String name = object.getString("item_name");
                                String product_price = object.getString("item_mrp");
                                String selling = object.getString("item_outprice");
                                String weight = object.getString("item_weight");
                                String seller_id = object.getString("seller_id");
                                String item_descrip = object.getString("item_description");
                                String item_image = object.getString("item_image");
                                String u = "https://inventivepartner.com/petmart/images/" + item_image;
                                teaAndCoffeeModelArrayList.add(new AttaAndOtherModel(u, name, weight, product_price, selling, item_descrip, id, seller_id));
                                gridItemAdapter.notifyDataSetChanged();
                            }
                        }
                    }
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(PelletDryFood.this, ""+error, Toast.LENGTH_SHORT).show();
            }
        });


        RequestQueue requestQueue= Volley.newRequestQueue(this);
        requestQueue.add(request);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);

    }
    @Override
    public void onBackPressed() {
        startActivity(new Intent(this,MainActivity.class));
        finish();
    }
}
