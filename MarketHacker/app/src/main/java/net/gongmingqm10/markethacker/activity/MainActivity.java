package net.gongmingqm10.markethacker.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import net.gongmingqm10.markethacker.R;
import net.gongmingqm10.markethacker.model.Order;
import net.gongmingqm10.markethacker.model.Product;
import net.gongmingqm10.markethacker.model.ProductRequest;
import net.gongmingqm10.markethacker.rest.RestClient;
import net.gongmingqm10.markethacker.view.ProductAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class MainActivity extends BaseActivity {

    @Bind(R.id.product_list)
    protected ListView productList;

    @Bind(R.id.empty_view)
    protected View emptyView;

    @Bind(R.id.confirm_order)
    protected Button confirmOrderBtn;

    private static final int SCAN_REQUEST = 100;
    public static final String PARAM_ORDER = "param_total";

    private ArrayList<Product> products = new ArrayList<>();
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

                        if (products.size() == 0) {
                            confirmOrderBtn.setEnabled(false);
                        }

                    }
                })
                .setNegativeButton(getString(R.string.cancel), null)
                .show();
    }

    private void initView() {
        confirmOrderBtn.setEnabled(false);
        adapter = new ProductAdapter(this, products);
        adapter.setQuantityListener(quantityListener);
        productList.setAdapter(adapter);
        productList.setEmptyView(emptyView);

    }

    @OnClick(R.id.confirm_order)
    protected void confirmOrder() {
        showProgressDialog();
        ProductRequest request = new ProductRequest(products);

        RestClient.getInstance().getProductService().sendOrders(request, new Callback<Order>() {
            @Override
            public void success(Order order, Response response) {
                dismissProgressDialog();
                Intent intent = new Intent(MainActivity.this, PaymentActivity.class);
                intent.putExtra(PARAM_ORDER, order);
                startActivity(intent);
            }

            @Override
            public void failure(RetrofitError error) {
                dismissProgressDialog();
                Toast.makeText(MainActivity.this, R.string.order_failed_please_retry, Toast.LENGTH_LONG).show();
            }
        });
    }

    private ProgressDialog progressDialog;

    private void showProgressDialog() {
        progressDialog = ProgressDialog.show(this, "", getString(R.string.is_pushing_order));
    }

    private void dismissProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing() && !isFinishing()) {
            progressDialog.dismiss();
        }
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

        confirmOrderBtn.setEnabled(true);
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
