package net.gongmingqm10.markethacker.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
    public static final String PARAM_TOTAL = "param_total";

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

    private ProductAdapter.ProductQuantityListener quantityListener = new ProductAdapter.ProductQuantityListener() {

        @Override
        public void addProduct(String pid) {
            for (Product product : products) {
                if (product.getPid().equals(pid)) {
                    product.setQuantity(product.getQuantity() + 1);
                    break;
                }
            }
            adapter.notifyDataSetChanged();
        }

        @Override
        public void minusProduct(String pid) {
            for (Product product : products) {
                if (product.getPid().equals(pid)) {
                    if (product.getQuantity() == 1) {
                        showDeleteWarningDialog(product);
                    } else {
                        product.setQuantity(product.getQuantity() - 1);
                        adapter.notifyDataSetChanged();
                    }
                    break;
                }
            }
        }
    };

    private void showDeleteWarningDialog(final Product product) {
        new AlertDialog.Builder(this)
                .setTitle(R.string.prompt)
                .setMessage(R.string.confirm_remove_products)
                .setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        products.remove(product);
                        adapter.notifyDataSetChanged();
                    }
                })
                .setNegativeButton(getString(R.string.cancel), null)
                .show();
    }

    private void initView() {
        adapter = new ProductAdapter(this, products);
        adapter.setQuantityListener(quantityListener);
        productList.setAdapter(adapter);

    }

    @OnClick(R.id.confirm_order)
    protected void confirmOrder() {
        Intent intent = new Intent(this, PaymentActivity.class);
        intent.putExtra(PARAM_TOTAL, getTotalPrice());

        startActivity(intent);
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
                addProductToList(product);
            }
        }
    }

    private void addProductToList(Product product) {
        for (Product item: products) {
            if (item.equals(product)) {
                item.setQuantity(item.getQuantity() + 1);
                adapter.notifyDataSetChanged();
                return;
            }
        }

        products.add(product);
        adapter.notifyDataSetChanged();
    }

    private float getTotalPrice() {
        float total = 0;
        for (Product product : products) {
            total += product.getPrice() * product.getQuantity();
        }
        return total;
    }
}
