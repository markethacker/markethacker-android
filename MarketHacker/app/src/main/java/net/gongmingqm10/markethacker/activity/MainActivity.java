package net.gongmingqm10.markethacker.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;

import net.gongmingqm10.markethacker.R;
import net.gongmingqm10.markethacker.model.Product;
import net.gongmingqm10.markethacker.view.ProductAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

public class MainActivity extends BaseActivity {

    @Bind(R.id.product_list)
    protected ListView productList;

    private static final int SCAN_REQUEST = 100;

    private List<Product> products = new ArrayList<>();
    private ProductAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setHomeButtonEnabled(false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        }

        initView();
    }

    private void initView() {
        adapter = new ProductAdapter(this, products);
        productList.setAdapter(adapter);
    }

    @OnClick(R.id.confirm_order)
    protected void confirmOrder() {
        startActivity(new Intent(this, PaymentActivity.class));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_scan) {
            startActivityForResult(new Intent(this, ScanActivity.class), SCAN_REQUEST);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == SCAN_REQUEST) {
            Product product = (Product) data.getSerializableExtra(ScanActivity.PARAM_PRODUCT);
            if (product != null) {
                products.add(product);
                adapter.notifyDataSetChanged();
            }
        }
    }
}
